package sun.rmi.transport;

import java.io.InvalidClassException;
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.net.SocketPermission;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.rmi.dgc.DGC;
import java.rmi.dgc.Lease;
import java.rmi.dgc.VMID;
import java.rmi.server.ObjID;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permissions;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import sun.misc.GC;
import sun.rmi.runtime.Log;
import sun.rmi.runtime.NewThreadAction;
import sun.rmi.server.UnicastRef;
import sun.rmi.server.Util;
import sun.security.action.GetLongAction;

/* loaded from: rt.jar:sun/rmi/transport/DGCClient.class */
final class DGCClient {
    private static final int dirtyFailureRetries = 5;
    private static final int cleanFailureRetries = 5;
    private static final AccessControlContext SOCKET_ACC;
    private static long nextSequenceNum = Long.MIN_VALUE;
    private static VMID vmid = new VMID();
    private static final long leaseValue = ((Long) AccessController.doPrivileged(new GetLongAction("java.rmi.dgc.leaseValue", 600000))).longValue();
    private static final long cleanInterval = ((Long) AccessController.doPrivileged(new GetLongAction("sun.rmi.dgc.cleanInterval", 180000))).longValue();
    private static final long gcInterval = ((Long) AccessController.doPrivileged(new GetLongAction("sun.rmi.dgc.client.gcInterval", 3600000))).longValue();
    private static final ObjID[] emptyObjIDArray = new ObjID[0];
    private static final ObjID dgcID = new ObjID(2);

    static {
        Permissions permissions = new Permissions();
        permissions.add(new SocketPermission("*", "connect,resolve"));
        SOCKET_ACC = new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(null, permissions)});
    }

    private DGCClient() {
    }

    static void registerRefs(Endpoint endpoint, List<LiveRef> list) {
        while (!EndpointEntry.lookup(endpoint).registerRefs(list)) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized long getNextSequenceNum() {
        long j2 = nextSequenceNum;
        nextSequenceNum = j2 + 1;
        return j2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long computeRenewTime(long j2, long j3) {
        return j2 + (j3 / 2);
    }

    /* loaded from: rt.jar:sun/rmi/transport/DGCClient$EndpointEntry.class */
    private static class EndpointEntry {
        private Endpoint endpoint;
        private DGC dgc;
        private long dirtyFailureStartTime;
        private long dirtyFailureDuration;
        private Thread renewCleanThread;
        private static Map<Endpoint, EndpointEntry> endpointTable;
        private static GC.LatencyRequest gcLatencyRequest;
        static final /* synthetic */ boolean $assertionsDisabled;
        private Map<LiveRef, RefEntry> refTable = new HashMap(5);
        private Set<RefEntry> invalidRefs = new HashSet(5);
        private boolean removed = false;
        private long renewTime = Long.MAX_VALUE;
        private long expirationTime = Long.MIN_VALUE;
        private int dirtyFailures = 0;
        private boolean interruptible = false;
        private ReferenceQueue<LiveRef> refQueue = new ReferenceQueue<>();
        private Set<CleanRequest> pendingCleans = new HashSet(5);

        static {
            $assertionsDisabled = !DGCClient.class.desiredAssertionStatus();
            endpointTable = new HashMap(5);
            gcLatencyRequest = null;
        }

        public static EndpointEntry lookup(Endpoint endpoint) {
            EndpointEntry endpointEntry;
            synchronized (endpointTable) {
                EndpointEntry endpointEntry2 = endpointTable.get(endpoint);
                if (endpointEntry2 == null) {
                    endpointEntry2 = new EndpointEntry(endpoint);
                    endpointTable.put(endpoint, endpointEntry2);
                    if (gcLatencyRequest == null) {
                        gcLatencyRequest = GC.requestLatency(DGCClient.gcInterval);
                    }
                }
                endpointEntry = endpointEntry2;
            }
            return endpointEntry;
        }

        private EndpointEntry(Endpoint endpoint) {
            this.endpoint = endpoint;
            try {
                this.dgc = (DGC) Util.createProxy(DGCImpl.class, new UnicastRef(new LiveRef(DGCClient.dgcID, endpoint, false)), true);
                this.renewCleanThread = (Thread) AccessController.doPrivileged(new NewThreadAction(new RenewCleanThread(), "RenewClean-" + ((Object) endpoint), true));
                this.renewCleanThread.start();
            } catch (RemoteException e2) {
                throw new Error("internal error creating DGC stub");
            }
        }

        public boolean registerRefs(List<LiveRef> list) {
            if (!$assertionsDisabled && Thread.holdsLock(this)) {
                throw new AssertionError();
            }
            HashSet hashSet = null;
            synchronized (this) {
                if (this.removed) {
                    return false;
                }
                for (LiveRef liveRef : list) {
                    if (!$assertionsDisabled && !liveRef.getEndpoint().equals(this.endpoint)) {
                        throw new AssertionError();
                    }
                    RefEntry refEntry = this.refTable.get(liveRef);
                    if (refEntry == null) {
                        LiveRef liveRef2 = (LiveRef) liveRef.clone();
                        refEntry = new RefEntry(liveRef2);
                        this.refTable.put(liveRef2, refEntry);
                        if (hashSet == null) {
                            hashSet = new HashSet(5);
                        }
                        hashSet.add(refEntry);
                    }
                    refEntry.addInstanceToRefSet(liveRef);
                }
                if (hashSet == null) {
                    return true;
                }
                hashSet.addAll(this.invalidRefs);
                this.invalidRefs.clear();
                makeDirtyCall(hashSet, DGCClient.getNextSequenceNum());
                return true;
            }
        }

        private void removeRefEntry(RefEntry refEntry) {
            if (!$assertionsDisabled && !Thread.holdsLock(this)) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && this.removed) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && !this.refTable.containsKey(refEntry.getRef())) {
                throw new AssertionError();
            }
            this.refTable.remove(refEntry.getRef());
            this.invalidRefs.remove(refEntry);
            if (this.refTable.isEmpty()) {
                synchronized (endpointTable) {
                    endpointTable.remove(this.endpoint);
                    this.endpoint.getOutboundTransport().free(this.endpoint);
                    if (endpointTable.isEmpty()) {
                        if (!$assertionsDisabled && gcLatencyRequest == null) {
                            throw new AssertionError();
                        }
                        gcLatencyRequest.cancel();
                        gcLatencyRequest = null;
                    }
                    this.removed = true;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void makeDirtyCall(Set<RefEntry> set, long j2) {
            ObjID[] objIDArrCreateObjIDArray;
            if (!$assertionsDisabled && Thread.holdsLock(this)) {
                throw new AssertionError();
            }
            if (set == null) {
                objIDArrCreateObjIDArray = DGCClient.emptyObjIDArray;
            } else {
                objIDArrCreateObjIDArray = createObjIDArray(set);
            }
            long jCurrentTimeMillis = System.currentTimeMillis();
            try {
                long value = this.dgc.dirty(objIDArrCreateObjIDArray, j2, new Lease(DGCClient.vmid, DGCClient.leaseValue)).getValue();
                long jComputeRenewTime = DGCClient.computeRenewTime(jCurrentTimeMillis, value);
                long j3 = jCurrentTimeMillis + value;
                synchronized (this) {
                    this.dirtyFailures = 0;
                    setRenewTime(jComputeRenewTime);
                    this.expirationTime = j3;
                }
            } catch (Exception e2) {
                long jCurrentTimeMillis2 = System.currentTimeMillis();
                synchronized (this) {
                    this.dirtyFailures++;
                    if ((e2 instanceof UnmarshalException) && (e2.getCause() instanceof InvalidClassException)) {
                        DGCImpl.dgcLog.log(Log.BRIEF, "InvalidClassException exception in DGC dirty call", e2);
                        return;
                    }
                    if (this.dirtyFailures == 1) {
                        this.dirtyFailureStartTime = jCurrentTimeMillis;
                        this.dirtyFailureDuration = jCurrentTimeMillis2 - jCurrentTimeMillis;
                        setRenewTime(jCurrentTimeMillis2);
                    } else {
                        int i2 = this.dirtyFailures - 2;
                        if (i2 == 0) {
                            this.dirtyFailureDuration = Math.max((this.dirtyFailureDuration + (jCurrentTimeMillis2 - jCurrentTimeMillis)) >> 1, 1000L);
                        }
                        long j4 = jCurrentTimeMillis2 + (this.dirtyFailureDuration << i2);
                        if (j4 < this.expirationTime || this.dirtyFailures < 5 || j4 < this.dirtyFailureStartTime + DGCClient.leaseValue) {
                            setRenewTime(j4);
                        } else {
                            setRenewTime(Long.MAX_VALUE);
                        }
                    }
                    if (set != null) {
                        this.invalidRefs.addAll(set);
                        Iterator<RefEntry> it = set.iterator();
                        while (it.hasNext()) {
                            it.next().markDirtyFailed();
                        }
                    }
                    if (this.renewTime >= this.expirationTime) {
                        this.invalidRefs.addAll(this.refTable.values());
                    }
                }
            }
        }

        private void setRenewTime(long j2) {
            if (!$assertionsDisabled && !Thread.holdsLock(this)) {
                throw new AssertionError();
            }
            if (j2 < this.renewTime) {
                this.renewTime = j2;
                if (this.interruptible) {
                    AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.rmi.transport.DGCClient.EndpointEntry.1
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedAction
                        /* renamed from: run */
                        public Void run2() {
                            EndpointEntry.this.renewCleanThread.interrupt();
                            return null;
                        }
                    });
                    return;
                }
                return;
            }
            this.renewTime = j2;
        }

        /* loaded from: rt.jar:sun/rmi/transport/DGCClient$EndpointEntry$RenewCleanThread.class */
        private class RenewCleanThread implements Runnable {
            private RenewCleanThread() {
            }

            @Override // java.lang.Runnable
            public void run() {
                long jMax;
                while (true) {
                    RefEntry.PhantomLiveRef phantomLiveRef = null;
                    boolean z2 = false;
                    Set set = null;
                    long nextSequenceNum = Long.MIN_VALUE;
                    synchronized (EndpointEntry.this) {
                        jMax = Math.max(EndpointEntry.this.renewTime - System.currentTimeMillis(), 1L);
                        if (!EndpointEntry.this.pendingCleans.isEmpty()) {
                            jMax = Math.min(jMax, DGCClient.cleanInterval);
                        }
                        EndpointEntry.this.interruptible = true;
                    }
                    try {
                        phantomLiveRef = (RefEntry.PhantomLiveRef) EndpointEntry.this.refQueue.remove(jMax);
                    } catch (InterruptedException e2) {
                    }
                    synchronized (EndpointEntry.this) {
                        EndpointEntry.this.interruptible = false;
                        Thread.interrupted();
                        if (phantomLiveRef != null) {
                            EndpointEntry.this.processPhantomRefs(phantomLiveRef);
                        }
                        if (System.currentTimeMillis() > EndpointEntry.this.renewTime) {
                            z2 = true;
                            if (!EndpointEntry.this.invalidRefs.isEmpty()) {
                                set = EndpointEntry.this.invalidRefs;
                                EndpointEntry.this.invalidRefs = new HashSet(5);
                            }
                            nextSequenceNum = DGCClient.getNextSequenceNum();
                        }
                    }
                    final boolean z3 = z2;
                    final Set set2 = set;
                    final long j2 = nextSequenceNum;
                    AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.rmi.transport.DGCClient.EndpointEntry.RenewCleanThread.1
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedAction
                        /* renamed from: run */
                        public Void run2() {
                            if (z3) {
                                EndpointEntry.this.makeDirtyCall(set2, j2);
                            }
                            if (!EndpointEntry.this.pendingCleans.isEmpty()) {
                                EndpointEntry.this.makeCleanCalls();
                                return null;
                            }
                            return null;
                        }
                    }, DGCClient.SOCKET_ACC);
                    if (EndpointEntry.this.removed && EndpointEntry.this.pendingCleans.isEmpty()) {
                        return;
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void processPhantomRefs(RefEntry.PhantomLiveRef phantomLiveRef) {
            RefEntry.PhantomLiveRef phantomLiveRef2;
            if (!$assertionsDisabled && !Thread.holdsLock(this)) {
                throw new AssertionError();
            }
            HashSet hashSet = null;
            HashSet hashSet2 = null;
            do {
                RefEntry refEntry = phantomLiveRef.getRefEntry();
                refEntry.removeInstanceFromRefSet(phantomLiveRef);
                if (refEntry.isRefSetEmpty()) {
                    if (refEntry.hasDirtyFailed()) {
                        if (hashSet == null) {
                            hashSet = new HashSet(5);
                        }
                        hashSet.add(refEntry);
                    } else {
                        if (hashSet2 == null) {
                            hashSet2 = new HashSet(5);
                        }
                        hashSet2.add(refEntry);
                    }
                    removeRefEntry(refEntry);
                }
                phantomLiveRef2 = (RefEntry.PhantomLiveRef) this.refQueue.poll();
                phantomLiveRef = phantomLiveRef2;
            } while (phantomLiveRef2 != null);
            if (hashSet != null) {
                this.pendingCleans.add(new CleanRequest(createObjIDArray(hashSet), DGCClient.getNextSequenceNum(), true));
            }
            if (hashSet2 != null) {
                this.pendingCleans.add(new CleanRequest(createObjIDArray(hashSet2), DGCClient.getNextSequenceNum(), false));
            }
        }

        /* loaded from: rt.jar:sun/rmi/transport/DGCClient$EndpointEntry$CleanRequest.class */
        private static class CleanRequest {
            final ObjID[] objIDs;
            final long sequenceNum;
            final boolean strong;
            int failures = 0;

            CleanRequest(ObjID[] objIDArr, long j2, boolean z2) {
                this.objIDs = objIDArr;
                this.sequenceNum = j2;
                this.strong = z2;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void makeCleanCalls() {
            if (!$assertionsDisabled && Thread.holdsLock(this)) {
                throw new AssertionError();
            }
            Iterator<CleanRequest> it = this.pendingCleans.iterator();
            while (it.hasNext()) {
                CleanRequest next = it.next();
                try {
                    this.dgc.clean(next.objIDs, next.sequenceNum, DGCClient.vmid, next.strong);
                    it.remove();
                } catch (Exception e2) {
                    int i2 = next.failures + 1;
                    next.failures = i2;
                    if (i2 >= 5) {
                        it.remove();
                    }
                }
            }
        }

        private static ObjID[] createObjIDArray(Set<RefEntry> set) {
            ObjID[] objIDArr = new ObjID[set.size()];
            Iterator<RefEntry> it = set.iterator();
            for (int i2 = 0; i2 < objIDArr.length; i2++) {
                objIDArr[i2] = it.next().getRef().getObjID();
            }
            return objIDArr;
        }

        /* loaded from: rt.jar:sun/rmi/transport/DGCClient$EndpointEntry$RefEntry.class */
        private class RefEntry {
            private LiveRef ref;
            private Set<PhantomLiveRef> refSet = new HashSet(5);
            private boolean dirtyFailed = false;
            static final /* synthetic */ boolean $assertionsDisabled;

            static {
                $assertionsDisabled = !DGCClient.class.desiredAssertionStatus();
            }

            public RefEntry(LiveRef liveRef) {
                this.ref = liveRef;
            }

            public LiveRef getRef() {
                return this.ref;
            }

            public void addInstanceToRefSet(LiveRef liveRef) {
                if (!$assertionsDisabled && !Thread.holdsLock(EndpointEntry.this)) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && !liveRef.equals(this.ref)) {
                    throw new AssertionError();
                }
                this.refSet.add(new PhantomLiveRef(liveRef));
            }

            public void removeInstanceFromRefSet(PhantomLiveRef phantomLiveRef) {
                if (!$assertionsDisabled && !Thread.holdsLock(EndpointEntry.this)) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && !this.refSet.contains(phantomLiveRef)) {
                    throw new AssertionError();
                }
                this.refSet.remove(phantomLiveRef);
            }

            public boolean isRefSetEmpty() {
                if ($assertionsDisabled || Thread.holdsLock(EndpointEntry.this)) {
                    return this.refSet.size() == 0;
                }
                throw new AssertionError();
            }

            public void markDirtyFailed() {
                if (!$assertionsDisabled && !Thread.holdsLock(EndpointEntry.this)) {
                    throw new AssertionError();
                }
                this.dirtyFailed = true;
            }

            public boolean hasDirtyFailed() {
                if ($assertionsDisabled || Thread.holdsLock(EndpointEntry.this)) {
                    return this.dirtyFailed;
                }
                throw new AssertionError();
            }

            /* loaded from: rt.jar:sun/rmi/transport/DGCClient$EndpointEntry$RefEntry$PhantomLiveRef.class */
            private class PhantomLiveRef extends PhantomReference<LiveRef> {
                public PhantomLiveRef(LiveRef liveRef) {
                    super(liveRef, EndpointEntry.this.refQueue);
                }

                public RefEntry getRefEntry() {
                    return RefEntry.this;
                }
            }
        }
    }
}
