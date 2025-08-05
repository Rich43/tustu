package sun.rmi.transport;

import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.dgc.VMID;
import java.rmi.server.ObjID;
import java.rmi.server.Unreferenced;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import sun.rmi.runtime.Log;
import sun.rmi.runtime.NewThreadAction;
import sun.rmi.server.Dispatcher;

/* loaded from: rt.jar:sun/rmi/transport/Target.class */
public final class Target {
    private final ObjID id;
    private final boolean permanent;
    private final WeakRef weakImpl;
    private volatile Dispatcher disp;
    private final Remote stub;
    private final ClassLoader ccl;
    private static int nextThreadNum = 0;
    private final Vector<VMID> refSet = new Vector<>();
    private final Hashtable<VMID, SequenceEntry> sequenceTable = new Hashtable<>(5);
    private int callCount = 0;
    private boolean removed = false;
    private volatile Transport exportedTransport = null;
    private final AccessControlContext acc = AccessController.getContext();

    public Target(Remote remote, Dispatcher dispatcher, Remote remote2, ObjID objID, boolean z2) {
        this.weakImpl = new WeakRef(remote, ObjectTable.reapQueue);
        this.disp = dispatcher;
        this.stub = remote2;
        this.id = objID;
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader classLoader = remote.getClass().getClassLoader();
        if (checkLoaderAncestry(contextClassLoader, classLoader)) {
            this.ccl = contextClassLoader;
        } else {
            this.ccl = classLoader;
        }
        this.permanent = z2;
        if (z2) {
            pinImpl();
        }
    }

    private static boolean checkLoaderAncestry(ClassLoader classLoader, ClassLoader classLoader2) {
        if (classLoader2 == null) {
            return true;
        }
        if (classLoader == null) {
            return false;
        }
        ClassLoader parent = classLoader;
        while (true) {
            ClassLoader classLoader3 = parent;
            if (classLoader3 != null) {
                if (classLoader3 != classLoader2) {
                    parent = classLoader3.getParent();
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    public Remote getStub() {
        return this.stub;
    }

    ObjectEndpoint getObjectEndpoint() {
        return new ObjectEndpoint(this.id, this.exportedTransport);
    }

    WeakRef getWeakImpl() {
        return this.weakImpl;
    }

    Dispatcher getDispatcher() {
        return this.disp;
    }

    AccessControlContext getAccessControlContext() {
        return this.acc;
    }

    ClassLoader getContextClassLoader() {
        return this.ccl;
    }

    Remote getImpl() {
        return (Remote) this.weakImpl.get();
    }

    boolean isPermanent() {
        return this.permanent;
    }

    synchronized void pinImpl() {
        this.weakImpl.pin();
    }

    synchronized void unpinImpl() {
        if (!this.permanent && this.refSet.isEmpty()) {
            this.weakImpl.unpin();
        }
    }

    void setExportedTransport(Transport transport) {
        if (this.exportedTransport == null) {
            this.exportedTransport = transport;
        }
    }

    synchronized void referenced(long j2, VMID vmid) {
        SequenceEntry sequenceEntry = this.sequenceTable.get(vmid);
        if (sequenceEntry == null) {
            this.sequenceTable.put(vmid, new SequenceEntry(j2));
        } else if (sequenceEntry.sequenceNum < j2) {
            sequenceEntry.update(j2);
        } else {
            return;
        }
        if (!this.refSet.contains(vmid)) {
            pinImpl();
            if (getImpl() == null) {
                return;
            }
            if (DGCImpl.dgcLog.isLoggable(Log.VERBOSE)) {
                DGCImpl.dgcLog.log(Log.VERBOSE, "add to dirty set: " + ((Object) vmid));
            }
            this.refSet.addElement(vmid);
            DGCImpl.getDGCImpl().registerTarget(vmid, this);
        }
    }

    synchronized void unreferenced(long j2, VMID vmid, boolean z2) {
        SequenceEntry sequenceEntry = this.sequenceTable.get(vmid);
        if (sequenceEntry == null || sequenceEntry.sequenceNum > j2) {
            return;
        }
        if (z2) {
            sequenceEntry.retain(j2);
        } else if (!sequenceEntry.keep) {
            this.sequenceTable.remove(vmid);
        }
        if (DGCImpl.dgcLog.isLoggable(Log.VERBOSE)) {
            DGCImpl.dgcLog.log(Log.VERBOSE, "remove from dirty set: " + ((Object) vmid));
        }
        refSetRemove(vmid);
    }

    private synchronized void refSetRemove(VMID vmid) {
        DGCImpl.getDGCImpl().unregisterTarget(vmid, this);
        if (this.refSet.removeElement(vmid) && this.refSet.isEmpty()) {
            if (DGCImpl.dgcLog.isLoggable(Log.VERBOSE)) {
                DGCImpl.dgcLog.log(Log.VERBOSE, "reference set is empty: target = " + ((Object) this));
            }
            Remote impl = getImpl();
            if (impl instanceof Unreferenced) {
                Unreferenced unreferenced = (Unreferenced) impl;
                Runnable runnable = () -> {
                    Thread.currentThread().setContextClassLoader(this.ccl);
                    AccessController.doPrivileged(() -> {
                        unreferenced.unreferenced();
                        return null;
                    }, this.acc);
                };
                StringBuilder sbAppend = new StringBuilder().append("Unreferenced-");
                int i2 = nextThreadNum;
                nextThreadNum = i2 + 1;
                ((Thread) AccessController.doPrivileged(new NewThreadAction(runnable, sbAppend.append(i2).toString(), false, true))).start();
            }
            unpinImpl();
        }
    }

    synchronized boolean unexport(boolean z2) {
        if (z2 || this.callCount == 0 || this.disp == null) {
            this.disp = null;
            unpinImpl();
            DGCImpl dGCImpl = DGCImpl.getDGCImpl();
            Enumeration<VMID> enumerationElements = this.refSet.elements();
            while (enumerationElements.hasMoreElements()) {
                dGCImpl.unregisterTarget(enumerationElements.nextElement2(), this);
            }
            return true;
        }
        return false;
    }

    synchronized void markRemoved() {
        if (this.removed) {
            throw new AssertionError();
        }
        this.removed = true;
        if (!this.permanent && this.callCount == 0) {
            ObjectTable.decrementKeepAliveCount();
        }
        if (this.exportedTransport != null) {
            this.exportedTransport.targetUnexported();
        }
    }

    synchronized void incrementCallCount() throws NoSuchObjectException {
        if (this.disp != null) {
            this.callCount++;
            return;
        }
        throw new NoSuchObjectException("object not accepting new calls");
    }

    synchronized void decrementCallCount() {
        int i2 = this.callCount - 1;
        this.callCount = i2;
        if (i2 < 0) {
            throw new Error("internal error: call count less than zero");
        }
        if (!this.permanent && this.removed && this.callCount == 0) {
            ObjectTable.decrementKeepAliveCount();
        }
    }

    boolean isEmpty() {
        return this.refSet.isEmpty();
    }

    public synchronized void vmidDead(VMID vmid) {
        if (DGCImpl.dgcLog.isLoggable(Log.BRIEF)) {
            DGCImpl.dgcLog.log(Log.BRIEF, "removing endpoint " + ((Object) vmid) + " from reference set");
        }
        this.sequenceTable.remove(vmid);
        refSetRemove(vmid);
    }
}
