package sun.rmi.transport;

import java.lang.ref.ReferenceQueue;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.dgc.VMID;
import java.rmi.server.ExportException;
import java.rmi.server.ObjID;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;
import sun.misc.GC;
import sun.rmi.runtime.Log;
import sun.rmi.runtime.NewThreadAction;
import sun.security.action.GetLongAction;

/* loaded from: rt.jar:sun/rmi/transport/ObjectTable.class */
public final class ObjectTable {
    private static final long gcInterval = ((Long) AccessController.doPrivileged(new GetLongAction("sun.rmi.dgc.server.gcInterval", 3600000))).longValue();
    private static final Object tableLock = new Object();
    private static final Map<ObjectEndpoint, Target> objTable = new HashMap();
    private static final Map<WeakRef, Target> implTable = new HashMap();
    private static final Object keepAliveLock = new Object();
    private static int keepAliveCount = 0;
    private static Thread reaper = null;
    static final ReferenceQueue<Object> reapQueue = new ReferenceQueue<>();
    private static GC.LatencyRequest gcLatencyRequest = null;

    private ObjectTable() {
    }

    static Target getTarget(ObjectEndpoint objectEndpoint) {
        Target target;
        synchronized (tableLock) {
            target = objTable.get(objectEndpoint);
        }
        return target;
    }

    public static Target getTarget(Remote remote) {
        Target target;
        synchronized (tableLock) {
            target = implTable.get(new WeakRef(remote));
        }
        return target;
    }

    public static Remote getStub(Remote remote) throws NoSuchObjectException {
        Target target = getTarget(remote);
        if (target == null) {
            throw new NoSuchObjectException("object not exported");
        }
        return target.getStub();
    }

    public static boolean unexportObject(Remote remote, boolean z2) throws NoSuchObjectException {
        synchronized (tableLock) {
            Target target = getTarget(remote);
            if (target == null) {
                throw new NoSuchObjectException("object not exported");
            }
            if (target.unexport(z2)) {
                removeTarget(target);
                return true;
            }
            return false;
        }
    }

    static void putTarget(Target target) throws ExportException {
        ObjectEndpoint objectEndpoint = target.getObjectEndpoint();
        WeakRef weakImpl = target.getWeakImpl();
        if (DGCImpl.dgcLog.isLoggable(Log.VERBOSE)) {
            DGCImpl.dgcLog.log(Log.VERBOSE, "add object " + ((Object) objectEndpoint));
        }
        synchronized (tableLock) {
            if (target.getImpl() != null) {
                if (objTable.containsKey(objectEndpoint)) {
                    throw new ExportException("internal error: ObjID already in use");
                }
                if (implTable.containsKey(weakImpl)) {
                    throw new ExportException("object already exported");
                }
                objTable.put(objectEndpoint, target);
                implTable.put(weakImpl, target);
                if (!target.isPermanent()) {
                    incrementKeepAliveCount();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void removeTarget(Target target) {
        ObjectEndpoint objectEndpoint = target.getObjectEndpoint();
        WeakRef weakImpl = target.getWeakImpl();
        if (DGCImpl.dgcLog.isLoggable(Log.VERBOSE)) {
            DGCImpl.dgcLog.log(Log.VERBOSE, "remove object " + ((Object) objectEndpoint));
        }
        objTable.remove(objectEndpoint);
        implTable.remove(weakImpl);
        target.markRemoved();
    }

    static void referenced(ObjID objID, long j2, VMID vmid) {
        synchronized (tableLock) {
            Target target = objTable.get(new ObjectEndpoint(objID, Transport.currentTransport()));
            if (target != null) {
                target.referenced(j2, vmid);
            }
        }
    }

    static void unreferenced(ObjID objID, long j2, VMID vmid, boolean z2) {
        synchronized (tableLock) {
            Target target = objTable.get(new ObjectEndpoint(objID, Transport.currentTransport()));
            if (target != null) {
                target.unreferenced(j2, vmid, z2);
            }
        }
    }

    static void incrementKeepAliveCount() {
        synchronized (keepAliveLock) {
            keepAliveCount++;
            if (reaper == null) {
                reaper = (Thread) AccessController.doPrivileged(new NewThreadAction(new Reaper(), "Reaper", false));
                reaper.start();
            }
            if (gcLatencyRequest == null) {
                gcLatencyRequest = GC.requestLatency(gcInterval);
            }
        }
    }

    static void decrementKeepAliveCount() {
        synchronized (keepAliveLock) {
            keepAliveCount--;
            if (keepAliveCount == 0) {
                if (reaper == null) {
                    throw new AssertionError();
                }
                AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.rmi.transport.ObjectTable.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public Void run2() {
                        ObjectTable.reaper.interrupt();
                        return null;
                    }
                });
                reaper = null;
                gcLatencyRequest.cancel();
                gcLatencyRequest = null;
            }
        }
    }

    /* loaded from: rt.jar:sun/rmi/transport/ObjectTable$Reaper.class */
    private static class Reaper implements Runnable {
        private Reaper() {
        }

        @Override // java.lang.Runnable
        public void run() {
            do {
                try {
                    WeakRef weakRef = (WeakRef) ObjectTable.reapQueue.remove();
                    synchronized (ObjectTable.tableLock) {
                        Target target = (Target) ObjectTable.implTable.get(weakRef);
                        if (target != null) {
                            if (!target.isEmpty()) {
                                throw new Error("object with known references collected");
                            }
                            if (!target.isPermanent()) {
                                ObjectTable.removeTarget(target);
                            } else {
                                throw new Error("permanent object collected");
                            }
                        }
                    }
                } catch (InterruptedException e2) {
                    return;
                }
            } while (!Thread.interrupted());
        }
    }
}
