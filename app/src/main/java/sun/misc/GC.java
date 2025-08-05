package sun.misc;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.SortedSet;
import java.util.TreeSet;

/* loaded from: rt.jar:sun/misc/GC.class */
public class GC {
    private static final long NO_TARGET = Long.MAX_VALUE;
    private static long latencyTarget = Long.MAX_VALUE;
    private static Thread daemon = null;
    private static Object lock = new LatencyLock();

    public static native long maxObjectInspectionAge();

    private GC() {
    }

    /* loaded from: rt.jar:sun/misc/GC$LatencyLock.class */
    private static class LatencyLock {
        private LatencyLock() {
        }
    }

    /* loaded from: rt.jar:sun/misc/GC$Daemon.class */
    private static class Daemon extends Thread {
        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (true) {
                synchronized (GC.lock) {
                    long j2 = GC.latencyTarget;
                    if (j2 == Long.MAX_VALUE) {
                        Thread unused = GC.daemon = null;
                        return;
                    }
                    long jMaxObjectInspectionAge = GC.maxObjectInspectionAge();
                    if (jMaxObjectInspectionAge >= j2) {
                        System.gc();
                        jMaxObjectInspectionAge = 0;
                    }
                    try {
                        GC.lock.wait(j2 - jMaxObjectInspectionAge);
                    } catch (InterruptedException e2) {
                    }
                }
            }
        }

        private Daemon(ThreadGroup threadGroup) {
            super(threadGroup, "GC Daemon");
        }

        public static void create() {
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.misc.GC.Daemon.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
                    ThreadGroup parent = threadGroup;
                    while (true) {
                        ThreadGroup threadGroup2 = parent;
                        if (threadGroup2 != null) {
                            threadGroup = threadGroup2;
                            parent = threadGroup.getParent();
                        } else {
                            Daemon daemon = new Daemon(threadGroup);
                            daemon.setDaemon(true);
                            daemon.setPriority(2);
                            daemon.start();
                            Thread unused = GC.daemon = daemon;
                            return null;
                        }
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void setLatencyTarget(long j2) {
        latencyTarget = j2;
        if (daemon == null) {
            Daemon.create();
        } else {
            lock.notify();
        }
    }

    /* loaded from: rt.jar:sun/misc/GC$LatencyRequest.class */
    public static class LatencyRequest implements Comparable<LatencyRequest> {
        private static long counter = 0;
        private static SortedSet<LatencyRequest> requests = null;
        private long latency;
        private long id;

        private static void adjustLatencyIfNeeded() {
            if (requests == null || requests.isEmpty()) {
                if (GC.latencyTarget != Long.MAX_VALUE) {
                    GC.setLatencyTarget(Long.MAX_VALUE);
                }
            } else {
                LatencyRequest latencyRequestFirst = requests.first();
                if (latencyRequestFirst.latency != GC.latencyTarget) {
                    GC.setLatencyTarget(latencyRequestFirst.latency);
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        private LatencyRequest(long j2) {
            if (j2 <= 0) {
                throw new IllegalArgumentException("Non-positive latency: " + j2);
            }
            this.latency = j2;
            synchronized (GC.lock) {
                long j3 = counter + 1;
                counter = this;
                this.id = j3;
                if (requests == null) {
                    requests = new TreeSet();
                }
                requests.add(this);
                adjustLatencyIfNeeded();
            }
        }

        public void cancel() {
            synchronized (GC.lock) {
                if (this.latency == Long.MAX_VALUE) {
                    throw new IllegalStateException("Request already cancelled");
                }
                if (!requests.remove(this)) {
                    throw new InternalError("Latency request " + ((Object) this) + " not found");
                }
                if (requests.isEmpty()) {
                    requests = null;
                }
                this.latency = Long.MAX_VALUE;
                adjustLatencyIfNeeded();
            }
        }

        @Override // java.lang.Comparable
        public int compareTo(LatencyRequest latencyRequest) {
            long j2 = this.latency - latencyRequest.latency;
            if (j2 == 0) {
                j2 = this.id - latencyRequest.id;
            }
            if (j2 < 0) {
                return -1;
            }
            return j2 > 0 ? 1 : 0;
        }

        public String toString() {
            return LatencyRequest.class.getName() + "[" + this.latency + "," + this.id + "]";
        }
    }

    public static LatencyRequest requestLatency(long j2) {
        return new LatencyRequest(j2);
    }

    public static long currentLatencyTarget() {
        long j2 = latencyTarget;
        if (j2 == Long.MAX_VALUE) {
            return 0L;
        }
        return j2;
    }
}
