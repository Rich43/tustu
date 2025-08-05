package java.util.concurrent;

import java.lang.Thread;
import java.security.AccessControlContext;
import java.security.ProtectionDomain;
import java.util.concurrent.ForkJoinPool;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/util/concurrent/ForkJoinWorkerThread.class */
public class ForkJoinWorkerThread extends Thread {
    final ForkJoinPool pool;
    final ForkJoinPool.WorkQueue workQueue;
    private static final AccessControlContext INNOCUOUS_ACC = new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(null, null)});

    /* renamed from: U, reason: collision with root package name */
    private static final Unsafe f12588U;
    private static final long THREADLOCALS;
    private static final long INHERITABLETHREADLOCALS;
    private static final long INHERITEDACCESSCONTROLCONTEXT;

    static {
        try {
            f12588U = Unsafe.getUnsafe();
            THREADLOCALS = f12588U.objectFieldOffset(Thread.class.getDeclaredField("threadLocals"));
            INHERITABLETHREADLOCALS = f12588U.objectFieldOffset(Thread.class.getDeclaredField("inheritableThreadLocals"));
            INHERITEDACCESSCONTROLCONTEXT = f12588U.objectFieldOffset(Thread.class.getDeclaredField("inheritedAccessControlContext"));
        } catch (Exception e2) {
            throw new Error(e2);
        }
    }

    protected ForkJoinWorkerThread(ForkJoinPool forkJoinPool) {
        super("aForkJoinWorkerThread");
        this.pool = forkJoinPool;
        this.workQueue = forkJoinPool.registerWorker(this);
    }

    ForkJoinWorkerThread(ForkJoinPool forkJoinPool, boolean z2) {
        super("aForkJoinWorkerThread");
        if (z2) {
            f12588U.putOrderedObject(this, INHERITEDACCESSCONTROLCONTEXT, INNOCUOUS_ACC);
        }
        this.pool = forkJoinPool;
        this.workQueue = forkJoinPool.registerWorker(this);
    }

    ForkJoinWorkerThread(ForkJoinPool forkJoinPool, ThreadGroup threadGroup, AccessControlContext accessControlContext) {
        super(threadGroup, null, "aForkJoinWorkerThread");
        f12588U.putOrderedObject(this, INHERITEDACCESSCONTROLCONTEXT, accessControlContext);
        eraseThreadLocals();
        this.pool = forkJoinPool;
        this.workQueue = forkJoinPool.registerWorker(this);
    }

    public ForkJoinPool getPool() {
        return this.pool;
    }

    public int getPoolIndex() {
        return this.workQueue.getPoolIndex();
    }

    protected void onStart() {
    }

    protected void onTermination(Throwable th) {
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        if (this.workQueue.array == null) {
            Throwable th = null;
            try {
                onStart();
                this.pool.runWorker(this.workQueue);
                try {
                    onTermination(null);
                    this.pool.deregisterWorker(this, null);
                } catch (Throwable th2) {
                    if (0 == 0) {
                        th = th2;
                    }
                    this.pool.deregisterWorker(this, th);
                }
            } catch (Throwable th3) {
                Throwable th4 = th3;
                try {
                    onTermination(th4);
                    this.pool.deregisterWorker(this, th4);
                } catch (Throwable th5) {
                    this.pool.deregisterWorker(this, th4);
                    throw th5;
                }
            }
        }
    }

    final void eraseThreadLocals() {
        f12588U.putObject(this, THREADLOCALS, (Object) null);
        f12588U.putObject(this, INHERITABLETHREADLOCALS, (Object) null);
    }

    void afterTopLevelExec() {
    }

    /* loaded from: rt.jar:java/util/concurrent/ForkJoinWorkerThread$InnocuousForkJoinWorkerThread.class */
    static final class InnocuousForkJoinWorkerThread extends ForkJoinWorkerThread {
        private static final ThreadGroup innocuousThreadGroup = createThreadGroup();

        InnocuousForkJoinWorkerThread(ForkJoinPool forkJoinPool) {
            super(forkJoinPool, innocuousThreadGroup, ForkJoinWorkerThread.INNOCUOUS_ACC);
        }

        @Override // java.util.concurrent.ForkJoinWorkerThread
        void afterTopLevelExec() {
            eraseThreadLocals();
        }

        @Override // java.lang.Thread
        public ClassLoader getContextClassLoader() {
            return ClassLoader.getSystemClassLoader();
        }

        @Override // java.lang.Thread
        public void setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        }

        @Override // java.lang.Thread
        public void setContextClassLoader(ClassLoader classLoader) {
            throw new SecurityException("setContextClassLoader");
        }

        private static ThreadGroup createThreadGroup() {
            try {
                Unsafe unsafe = Unsafe.getUnsafe();
                long jObjectFieldOffset = unsafe.objectFieldOffset(Thread.class.getDeclaredField("group"));
                long jObjectFieldOffset2 = unsafe.objectFieldOffset(ThreadGroup.class.getDeclaredField("parent"));
                ThreadGroup threadGroup = (ThreadGroup) unsafe.getObject(Thread.currentThread(), jObjectFieldOffset);
                while (threadGroup != null) {
                    ThreadGroup threadGroup2 = (ThreadGroup) unsafe.getObject(threadGroup, jObjectFieldOffset2);
                    if (threadGroup2 == null) {
                        return new ThreadGroup(threadGroup, "InnocuousForkJoinWorkerThreadGroup");
                    }
                    threadGroup = threadGroup2;
                }
                throw new Error("Cannot create ThreadGroup");
            } catch (Exception e2) {
                throw new Error(e2);
            }
        }
    }
}
