package sun.rmi.transport;

import java.net.SocketPermission;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.dgc.DGC;
import java.rmi.dgc.Lease;
import java.rmi.dgc.VMID;
import java.rmi.server.LogStream;
import java.rmi.server.ObjID;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UID;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permissions;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import sun.misc.ObjectInputFilter;
import sun.rmi.runtime.Log;
import sun.rmi.runtime.RuntimeUtil;
import sun.rmi.server.UnicastRef;
import sun.rmi.server.UnicastServerRef;
import sun.rmi.server.Util;
import sun.security.action.GetLongAction;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/rmi/transport/DGCImpl.class */
final class DGCImpl implements DGC {
    private static DGCImpl dgc;
    private Map<VMID, LeaseInfo> leaseTable;
    private Future<?> checker;
    private static final String DGC_FILTER_PROPNAME = "sun.rmi.transport.dgcFilter";
    static final Log dgcLog = Log.getLog("sun.rmi.dgc", "dgc", LogStream.parseLevel((String) AccessController.doPrivileged(new GetPropertyAction("sun.rmi.dgc.logLevel"))));
    private static final long leaseValue = ((Long) AccessController.doPrivileged(new GetLongAction("java.rmi.dgc.leaseValue", 600000))).longValue();
    private static final long leaseCheckInterval = ((Long) AccessController.doPrivileged(new GetLongAction("sun.rmi.dgc.checkInterval", leaseValue / 2))).longValue();
    private static final ScheduledExecutorService scheduler = ((RuntimeUtil) AccessController.doPrivileged(new RuntimeUtil.GetInstanceAction())).getScheduler();
    private static int DGC_MAX_DEPTH = 5;
    private static int DGC_MAX_ARRAY_SIZE = 10000;
    private static final ObjectInputFilter dgcFilter = (ObjectInputFilter) AccessController.doPrivileged(DGCImpl::initDgcFilter);

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.rmi.transport.DGCImpl.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                try {
                    Thread.currentThread().setContextClassLoader(ClassLoader.getSystemClassLoader());
                    try {
                        DGCImpl unused = DGCImpl.dgc = new DGCImpl();
                        final ObjID objID = new ObjID(2);
                        LiveRef liveRef = new LiveRef(objID, 0);
                        final UnicastServerRef unicastServerRef = new UnicastServerRef(liveRef, filterInfo -> {
                            return DGCImpl.checkInput(filterInfo);
                        });
                        final Remote remoteCreateProxy = Util.createProxy(DGCImpl.class, new UnicastRef(liveRef), true);
                        unicastServerRef.setSkeleton(DGCImpl.dgc);
                        Permissions permissions = new Permissions();
                        permissions.add(new SocketPermission("*", "accept,resolve"));
                        ObjectTable.putTarget((Target) AccessController.doPrivileged(new PrivilegedAction<Target>() { // from class: sun.rmi.transport.DGCImpl.2.1
                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // java.security.PrivilegedAction
                            /* renamed from: run */
                            public Target run2() {
                                return new Target(DGCImpl.dgc, unicastServerRef, remoteCreateProxy, objID, true);
                            }
                        }, new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(null, permissions)})));
                        Thread.currentThread().setContextClassLoader(contextClassLoader);
                        return null;
                    } catch (RemoteException e2) {
                        throw new Error("exception initializing server-side DGC", e2);
                    }
                } catch (Throwable th) {
                    Thread.currentThread().setContextClassLoader(contextClassLoader);
                    throw th;
                }
            }
        });
    }

    static DGCImpl getDGCImpl() {
        return dgc;
    }

    private static ObjectInputFilter initDgcFilter() {
        ObjectInputFilter objectInputFilterCreateFilter = null;
        String property = System.getProperty(DGC_FILTER_PROPNAME);
        if (property == null) {
            property = Security.getProperty(DGC_FILTER_PROPNAME);
        }
        if (property != null) {
            objectInputFilterCreateFilter = ObjectInputFilter.Config.createFilter(property);
            if (dgcLog.isLoggable(Log.BRIEF)) {
                dgcLog.log(Log.BRIEF, "dgcFilter = " + ((Object) objectInputFilterCreateFilter));
            }
        }
        return objectInputFilterCreateFilter;
    }

    private DGCImpl() {
        this.leaseTable = new HashMap();
        this.checker = null;
    }

    @Override // java.rmi.dgc.DGC
    public Lease dirty(ObjID[] objIDArr, long j2, Lease lease) {
        String clientHost;
        VMID vmid = lease.getVMID();
        long j3 = leaseValue;
        if (dgcLog.isLoggable(Log.VERBOSE)) {
            dgcLog.log(Log.VERBOSE, "vmid = " + ((Object) vmid));
        }
        if (vmid == null) {
            vmid = new VMID();
            if (dgcLog.isLoggable(Log.BRIEF)) {
                try {
                    clientHost = RemoteServer.getClientHost();
                } catch (ServerNotActiveException e2) {
                    clientHost = "<unknown host>";
                }
                dgcLog.log(Log.BRIEF, " assigning vmid " + ((Object) vmid) + " to client " + clientHost);
            }
        }
        Lease lease2 = new Lease(vmid, j3);
        synchronized (this.leaseTable) {
            LeaseInfo leaseInfo = this.leaseTable.get(vmid);
            if (leaseInfo == null) {
                this.leaseTable.put(vmid, new LeaseInfo(vmid, j3));
                if (this.checker == null) {
                    this.checker = scheduler.scheduleWithFixedDelay(new Runnable() { // from class: sun.rmi.transport.DGCImpl.1
                        @Override // java.lang.Runnable
                        public void run() {
                            DGCImpl.this.checkLeases();
                        }
                    }, leaseCheckInterval, leaseCheckInterval, TimeUnit.MILLISECONDS);
                }
            } else {
                leaseInfo.renew(j3);
            }
        }
        for (ObjID objID : objIDArr) {
            if (dgcLog.isLoggable(Log.VERBOSE)) {
                dgcLog.log(Log.VERBOSE, "id = " + ((Object) objID) + ", vmid = " + ((Object) vmid) + ", duration = " + j3);
            }
            ObjectTable.referenced(objID, j2, vmid);
        }
        return lease2;
    }

    @Override // java.rmi.dgc.DGC
    public void clean(ObjID[] objIDArr, long j2, VMID vmid, boolean z2) {
        for (ObjID objID : objIDArr) {
            if (dgcLog.isLoggable(Log.VERBOSE)) {
                dgcLog.log(Log.VERBOSE, "id = " + ((Object) objID) + ", vmid = " + ((Object) vmid) + ", strong = " + z2);
            }
            ObjectTable.unreferenced(objID, j2, vmid, z2);
        }
    }

    void registerTarget(VMID vmid, Target target) {
        synchronized (this.leaseTable) {
            LeaseInfo leaseInfo = this.leaseTable.get(vmid);
            if (leaseInfo == null) {
                target.vmidDead(vmid);
            } else {
                leaseInfo.notifySet.add(target);
            }
        }
    }

    void unregisterTarget(VMID vmid, Target target) {
        synchronized (this.leaseTable) {
            LeaseInfo leaseInfo = this.leaseTable.get(vmid);
            if (leaseInfo != null) {
                leaseInfo.notifySet.remove(target);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkLeases() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        ArrayList<LeaseInfo> arrayList = new ArrayList();
        synchronized (this.leaseTable) {
            Iterator<LeaseInfo> it = this.leaseTable.values().iterator();
            while (it.hasNext()) {
                LeaseInfo next = it.next();
                if (next.expired(jCurrentTimeMillis)) {
                    arrayList.add(next);
                    it.remove();
                }
            }
            if (this.leaseTable.isEmpty()) {
                this.checker.cancel(false);
                this.checker = null;
            }
        }
        for (LeaseInfo leaseInfo : arrayList) {
            Iterator<Target> it2 = leaseInfo.notifySet.iterator();
            while (it2.hasNext()) {
                it2.next().vmidDead(leaseInfo.vmid);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ObjectInputFilter.Status checkInput(ObjectInputFilter.FilterInfo filterInfo) {
        ObjectInputFilter.Status statusCheckInput;
        if (dgcFilter != null && (statusCheckInput = dgcFilter.checkInput(filterInfo)) != ObjectInputFilter.Status.UNDECIDED) {
            return statusCheckInput;
        }
        if (filterInfo.depth() > DGC_MAX_DEPTH) {
            return ObjectInputFilter.Status.REJECTED;
        }
        Class<?> clsSerialClass = filterInfo.serialClass();
        if (clsSerialClass != null) {
            while (clsSerialClass.isArray()) {
                if (filterInfo.arrayLength() >= 0 && filterInfo.arrayLength() > DGC_MAX_ARRAY_SIZE) {
                    return ObjectInputFilter.Status.REJECTED;
                }
                clsSerialClass = clsSerialClass.getComponentType();
            }
            if (clsSerialClass.isPrimitive()) {
                return ObjectInputFilter.Status.ALLOWED;
            }
            return (clsSerialClass == ObjID.class || clsSerialClass == UID.class || clsSerialClass == VMID.class || clsSerialClass == Lease.class) ? ObjectInputFilter.Status.ALLOWED : ObjectInputFilter.Status.REJECTED;
        }
        return ObjectInputFilter.Status.UNDECIDED;
    }

    /* loaded from: rt.jar:sun/rmi/transport/DGCImpl$LeaseInfo.class */
    private static class LeaseInfo {
        VMID vmid;
        long expiration;
        Set<Target> notifySet = new HashSet();

        LeaseInfo(VMID vmid, long j2) {
            this.vmid = vmid;
            this.expiration = System.currentTimeMillis() + j2;
        }

        synchronized void renew(long j2) {
            long jCurrentTimeMillis = System.currentTimeMillis() + j2;
            if (jCurrentTimeMillis > this.expiration) {
                this.expiration = jCurrentTimeMillis;
            }
        }

        boolean expired(long j2) {
            if (this.expiration < j2) {
                if (DGCImpl.dgcLog.isLoggable(Log.BRIEF)) {
                    DGCImpl.dgcLog.log(Log.BRIEF, this.vmid.toString());
                    return true;
                }
                return true;
            }
            return false;
        }
    }
}
