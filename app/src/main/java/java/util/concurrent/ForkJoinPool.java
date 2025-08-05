package java.util.concurrent;

import java.lang.Thread;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permissions;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.atomic.AtomicLong;
import javax.swing.JSplitPane;
import sun.misc.Contended;
import sun.misc.Unsafe;

@Contended
/* loaded from: rt.jar:java/util/concurrent/ForkJoinPool.class */
public class ForkJoinPool extends AbstractExecutorService {
    static final int SMASK = 65535;
    static final int MAX_CAP = 32767;
    static final int EVENMASK = 65534;
    static final int SQMASK = 126;
    static final int SCANNING = 1;
    static final int INACTIVE = Integer.MIN_VALUE;
    static final int SS_SEQ = 65536;
    static final int MODE_MASK = -65536;
    static final int LIFO_QUEUE = 0;
    static final int FIFO_QUEUE = 65536;
    static final int SHARED_QUEUE = Integer.MIN_VALUE;
    public static final ForkJoinWorkerThreadFactory defaultForkJoinWorkerThreadFactory;
    private static final RuntimePermission modifyThreadPermission;
    static final ForkJoinPool common;
    static final int commonParallelism;
    private static int commonMaxSpares;
    private static int poolNumberSequence;
    private static final long IDLE_TIMEOUT = 2000000000;
    private static final long TIMEOUT_SLOP = 20000000;
    private static final int DEFAULT_COMMON_MAX_SPARES = 256;
    private static final int SPINS = 0;
    private static final int SEED_INCREMENT = -1640531527;
    private static final long SP_MASK = 4294967295L;
    private static final long UC_MASK = -4294967296L;
    private static final int AC_SHIFT = 48;
    private static final long AC_UNIT = 281474976710656L;
    private static final long AC_MASK = -281474976710656L;
    private static final int TC_SHIFT = 32;
    private static final long TC_UNIT = 4294967296L;
    private static final long TC_MASK = 281470681743360L;
    private static final long ADD_WORKER = 140737488355328L;
    private static final int RSLOCK = 1;
    private static final int RSIGNAL = 2;
    private static final int STARTED = 4;
    private static final int STOP = 536870912;
    private static final int TERMINATED = 1073741824;
    private static final int SHUTDOWN = Integer.MIN_VALUE;
    volatile long ctl;
    volatile int runState;
    final int config;
    int indexSeed;
    volatile WorkQueue[] workQueues;
    final ForkJoinWorkerThreadFactory factory;
    final Thread.UncaughtExceptionHandler ueh;
    final String workerNamePrefix;
    volatile AtomicLong stealCounter;

    /* renamed from: U, reason: collision with root package name */
    private static final Unsafe f12585U;
    private static final int ABASE;
    private static final int ASHIFT;
    private static final long CTL;
    private static final long RUNSTATE;
    private static final long STEALCOUNTER;
    private static final long PARKBLOCKER;
    private static final long QTOP;
    private static final long QLOCK;
    private static final long QSCANSTATE;
    private static final long QPARKER;
    private static final long QCURRENTSTEAL;
    private static final long QCURRENTJOIN;

    /* loaded from: rt.jar:java/util/concurrent/ForkJoinPool$ForkJoinWorkerThreadFactory.class */
    public interface ForkJoinWorkerThreadFactory {
        ForkJoinWorkerThread newThread(ForkJoinPool forkJoinPool);
    }

    /* loaded from: rt.jar:java/util/concurrent/ForkJoinPool$ManagedBlocker.class */
    public interface ManagedBlocker {
        boolean block() throws InterruptedException;

        boolean isReleasable();
    }

    @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
    public /* bridge */ /* synthetic */ Future submit(Runnable runnable, Object obj) {
        return submit(runnable, (Runnable) obj);
    }

    private static void checkPermission() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(modifyThreadPermission);
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ForkJoinPool$DefaultForkJoinWorkerThreadFactory.class */
    static final class DefaultForkJoinWorkerThreadFactory implements ForkJoinWorkerThreadFactory {
        DefaultForkJoinWorkerThreadFactory() {
        }

        @Override // java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory
        public final ForkJoinWorkerThread newThread(ForkJoinPool forkJoinPool) {
            return new ForkJoinWorkerThread(forkJoinPool);
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ForkJoinPool$EmptyTask.class */
    static final class EmptyTask extends ForkJoinTask<Void> {
        private static final long serialVersionUID = -7721805057305804111L;

        EmptyTask() {
            this.status = -268435456;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.concurrent.ForkJoinTask
        public final Void getRawResult() {
            return null;
        }

        @Override // java.util.concurrent.ForkJoinTask
        public final void setRawResult(Void r2) {
        }

        @Override // java.util.concurrent.ForkJoinTask
        public final boolean exec() {
            return true;
        }
    }

    @Contended
    /* loaded from: rt.jar:java/util/concurrent/ForkJoinPool$WorkQueue.class */
    static final class WorkQueue {
        static final int INITIAL_QUEUE_CAPACITY = 8192;
        static final int MAXIMUM_QUEUE_CAPACITY = 67108864;
        volatile int scanState;
        int stackPred;
        int nsteals;
        int hint;
        int config;
        volatile int qlock;
        ForkJoinTask<?>[] array;
        final ForkJoinPool pool;
        final ForkJoinWorkerThread owner;
        volatile Thread parker;
        volatile ForkJoinTask<?> currentJoin;
        volatile ForkJoinTask<?> currentSteal;

        /* renamed from: U, reason: collision with root package name */
        private static final Unsafe f12586U;
        private static final int ABASE;
        private static final int ASHIFT;
        private static final long QTOP;
        private static final long QLOCK;
        private static final long QCURRENTSTEAL;
        int top = 4096;
        volatile int base = 4096;

        WorkQueue(ForkJoinPool forkJoinPool, ForkJoinWorkerThread forkJoinWorkerThread) {
            this.pool = forkJoinPool;
            this.owner = forkJoinWorkerThread;
        }

        final int getPoolIndex() {
            return (this.config & 65535) >>> 1;
        }

        final int queueSize() {
            int i2 = this.base - this.top;
            if (i2 >= 0) {
                return 0;
            }
            return -i2;
        }

        final boolean isEmpty() {
            ForkJoinTask<?>[] forkJoinTaskArr;
            int length;
            int i2 = this.base;
            int i3 = this.top;
            int i4 = i2 - i3;
            return i4 >= 0 || (i4 == -1 && ((forkJoinTaskArr = this.array) == null || (length = forkJoinTaskArr.length - 1) < 0 || f12586U.getObject(forkJoinTaskArr, ((long) ((length & (i3 - 1)) << ASHIFT)) + ((long) ABASE)) == null));
        }

        final void push(ForkJoinTask<?> forkJoinTask) {
            int i2 = this.base;
            int i3 = this.top;
            ForkJoinTask<?>[] forkJoinTaskArr = this.array;
            if (forkJoinTaskArr != null) {
                int length = forkJoinTaskArr.length - 1;
                f12586U.putOrderedObject(forkJoinTaskArr, ((length & i3) << ASHIFT) + ABASE, forkJoinTask);
                f12586U.putOrderedInt(this, QTOP, i3 + 1);
                int i4 = i3 - i2;
                if (i4 <= 1) {
                    ForkJoinPool forkJoinPool = this.pool;
                    if (forkJoinPool != null) {
                        forkJoinPool.signalWork(forkJoinPool.workQueues, this);
                        return;
                    }
                    return;
                }
                if (i4 >= length) {
                    growArray();
                }
            }
        }

        final ForkJoinTask<?>[] growArray() {
            int length;
            ForkJoinTask<?>[] forkJoinTaskArr = this.array;
            int length2 = forkJoinTaskArr != null ? forkJoinTaskArr.length << 1 : 8192;
            if (length2 > 67108864) {
                throw new RejectedExecutionException("Queue capacity exceeded");
            }
            ForkJoinTask<?>[] forkJoinTaskArr2 = new ForkJoinTask[length2];
            this.array = forkJoinTaskArr2;
            if (forkJoinTaskArr != null && (length = forkJoinTaskArr.length - 1) >= 0) {
                int i2 = this.top;
                int i3 = this.base;
                int i4 = i3;
                if (i2 - i3 > 0) {
                    int i5 = length2 - 1;
                    do {
                        int i6 = ((i4 & length) << ASHIFT) + ABASE;
                        int i7 = ((i4 & i5) << ASHIFT) + ABASE;
                        ForkJoinTask forkJoinTask = (ForkJoinTask) f12586U.getObjectVolatile(forkJoinTaskArr, i6);
                        if (forkJoinTask != null && f12586U.compareAndSwapObject(forkJoinTaskArr, i6, forkJoinTask, null)) {
                            f12586U.putObjectVolatile(forkJoinTaskArr2, i7, forkJoinTask);
                        }
                        i4++;
                    } while (i4 != i2);
                }
            }
            return forkJoinTaskArr2;
        }

        final ForkJoinTask<?> pop() {
            int length;
            int i2;
            long j2;
            ForkJoinTask<?> forkJoinTask;
            ForkJoinTask<?>[] forkJoinTaskArr = this.array;
            if (forkJoinTaskArr != null && (length = forkJoinTaskArr.length - 1) >= 0) {
                do {
                    i2 = this.top - 1;
                    if (i2 - this.base >= 0) {
                        j2 = ((length & i2) << ASHIFT) + ABASE;
                        forkJoinTask = (ForkJoinTask) f12586U.getObject(forkJoinTaskArr, j2);
                        if (forkJoinTask == null) {
                            return null;
                        }
                    } else {
                        return null;
                    }
                } while (!f12586U.compareAndSwapObject(forkJoinTaskArr, j2, forkJoinTask, null));
                f12586U.putOrderedInt(this, QTOP, i2);
                return forkJoinTask;
            }
            return null;
        }

        final ForkJoinTask<?> pollAt(int i2) {
            ForkJoinTask<?>[] forkJoinTaskArr = this.array;
            if (forkJoinTaskArr != null) {
                int length = (((forkJoinTaskArr.length - 1) & i2) << ASHIFT) + ABASE;
                ForkJoinTask<?> forkJoinTask = (ForkJoinTask) f12586U.getObjectVolatile(forkJoinTaskArr, length);
                if (forkJoinTask != null && this.base == i2 && f12586U.compareAndSwapObject(forkJoinTaskArr, length, forkJoinTask, null)) {
                    this.base = i2 + 1;
                    return forkJoinTask;
                }
                return null;
            }
            return null;
        }

        final ForkJoinTask<?> poll() {
            ForkJoinTask<?>[] forkJoinTaskArr;
            while (true) {
                int i2 = this.base;
                if (i2 - this.top < 0 && (forkJoinTaskArr = this.array) != null) {
                    int length = (((forkJoinTaskArr.length - 1) & i2) << ASHIFT) + ABASE;
                    ForkJoinTask<?> forkJoinTask = (ForkJoinTask) f12586U.getObjectVolatile(forkJoinTaskArr, length);
                    if (this.base == i2) {
                        if (forkJoinTask != null) {
                            if (f12586U.compareAndSwapObject(forkJoinTaskArr, length, forkJoinTask, null)) {
                                this.base = i2 + 1;
                                return forkJoinTask;
                            }
                        } else if (i2 + 1 == this.top) {
                            return null;
                        }
                    }
                } else {
                    return null;
                }
            }
        }

        final ForkJoinTask<?> nextLocalTask() {
            return (this.config & 65536) == 0 ? pop() : poll();
        }

        final ForkJoinTask<?> peek() {
            int length;
            ForkJoinTask<?>[] forkJoinTaskArr = this.array;
            if (forkJoinTaskArr == null || (length = forkJoinTaskArr.length - 1) < 0) {
                return null;
            }
            return (ForkJoinTask) f12586U.getObjectVolatile(forkJoinTaskArr, ((((this.config & 65536) == 0 ? this.top - 1 : this.base) & length) << ASHIFT) + ABASE);
        }

        final boolean tryUnpush(ForkJoinTask<?> forkJoinTask) {
            int i2;
            ForkJoinTask<?>[] forkJoinTaskArr = this.array;
            if (forkJoinTaskArr != null && (i2 = this.top) != this.base) {
                int i3 = i2 - 1;
                if (f12586U.compareAndSwapObject(forkJoinTaskArr, (((forkJoinTaskArr.length - 1) & i3) << ASHIFT) + ABASE, forkJoinTask, null)) {
                    f12586U.putOrderedInt(this, QTOP, i3);
                    return true;
                }
                return false;
            }
            return false;
        }

        final void cancelAll() {
            ForkJoinTask<?> forkJoinTask = this.currentJoin;
            if (forkJoinTask != null) {
                this.currentJoin = null;
                ForkJoinTask.cancelIgnoringExceptions(forkJoinTask);
            }
            ForkJoinTask<?> forkJoinTask2 = this.currentSteal;
            if (forkJoinTask2 != null) {
                this.currentSteal = null;
                ForkJoinTask.cancelIgnoringExceptions(forkJoinTask2);
            }
            while (true) {
                ForkJoinTask<?> forkJoinTaskPoll = poll();
                if (forkJoinTaskPoll != null) {
                    ForkJoinTask.cancelIgnoringExceptions(forkJoinTaskPoll);
                } else {
                    return;
                }
            }
        }

        final void pollAndExecAll() {
            while (true) {
                ForkJoinTask<?> forkJoinTaskPoll = poll();
                if (forkJoinTaskPoll != null) {
                    forkJoinTaskPoll.doExec();
                } else {
                    return;
                }
            }
        }

        final void execLocalTasks() {
            int length;
            int i2;
            int i3;
            int i4 = this.base;
            ForkJoinTask<?>[] forkJoinTaskArr = this.array;
            int i5 = this.top - 1;
            int i6 = i5;
            if (i4 - i5 <= 0 && forkJoinTaskArr != null && (length = forkJoinTaskArr.length - 1) >= 0) {
                if ((this.config & 65536) != 0) {
                    pollAndExecAll();
                    return;
                }
                do {
                    ForkJoinTask forkJoinTask = (ForkJoinTask) f12586U.getAndSetObject(forkJoinTaskArr, ((length & i6) << ASHIFT) + ABASE, null);
                    if (forkJoinTask != null) {
                        f12586U.putOrderedInt(this, QTOP, i6);
                        forkJoinTask.doExec();
                        i2 = this.base;
                        i3 = this.top - 1;
                        i6 = i3;
                    } else {
                        return;
                    }
                } while (i2 - i3 <= 0);
            }
        }

        final void runTask(ForkJoinTask<?> forkJoinTask) {
            if (forkJoinTask != null) {
                this.scanState &= -2;
                this.currentSteal = forkJoinTask;
                forkJoinTask.doExec();
                f12586U.putOrderedObject(this, QCURRENTSTEAL, null);
                execLocalTasks();
                ForkJoinWorkerThread forkJoinWorkerThread = this.owner;
                int i2 = this.nsteals + 1;
                this.nsteals = i2;
                if (i2 < 0) {
                    transferStealCount(this.pool);
                }
                this.scanState |= 1;
                if (forkJoinWorkerThread != null) {
                    forkJoinWorkerThread.afterTopLevelExec();
                }
            }
        }

        final void transferStealCount(ForkJoinPool forkJoinPool) {
            AtomicLong atomicLong;
            if (forkJoinPool != null && (atomicLong = forkJoinPool.stealCounter) != null) {
                int i2 = this.nsteals;
                this.nsteals = 0;
                atomicLong.getAndAdd(i2 < 0 ? Integer.MAX_VALUE : i2);
            }
        }

        final boolean tryRemoveAndExec(ForkJoinTask<?> forkJoinTask) {
            int length;
            ForkJoinTask<?>[] forkJoinTaskArr = this.array;
            if (forkJoinTaskArr != null && (length = forkJoinTaskArr.length - 1) >= 0 && forkJoinTask != null) {
                do {
                    int i2 = this.top;
                    int i3 = i2;
                    int i4 = this.base;
                    int i5 = i2 - i4;
                    int i6 = i5;
                    if (i5 > 0) {
                        do {
                            i3--;
                            long j2 = ((i3 & length) << ASHIFT) + ABASE;
                            ForkJoinTask<?> forkJoinTask2 = (ForkJoinTask) f12586U.getObject(forkJoinTaskArr, j2);
                            if (forkJoinTask2 == null) {
                                return i3 + 1 == this.top;
                            }
                            if (forkJoinTask2 == forkJoinTask) {
                                boolean zCompareAndSwapObject = false;
                                if (i3 + 1 == this.top) {
                                    if (f12586U.compareAndSwapObject(forkJoinTaskArr, j2, forkJoinTask, null)) {
                                        f12586U.putOrderedInt(this, QTOP, i3);
                                        zCompareAndSwapObject = true;
                                    }
                                } else if (this.base == i4) {
                                    zCompareAndSwapObject = f12586U.compareAndSwapObject(forkJoinTaskArr, j2, forkJoinTask, new EmptyTask());
                                }
                                if (zCompareAndSwapObject) {
                                    forkJoinTask.doExec();
                                }
                            } else if (forkJoinTask2.status < 0 && i3 + 1 == this.top) {
                                if (f12586U.compareAndSwapObject(forkJoinTaskArr, j2, forkJoinTask2, null)) {
                                    f12586U.putOrderedInt(this, QTOP, i3);
                                }
                            } else {
                                i6--;
                            }
                        } while (i6 != 0);
                        return false;
                    }
                    return true;
                } while (forkJoinTask.status >= 0);
                return false;
            }
            return true;
        }

        final CountedCompleter<?> popCC(CountedCompleter<?> countedCompleter, int i2) {
            ForkJoinTask<?>[] forkJoinTaskArr;
            long j2;
            Object objectVolatile;
            int i3 = this.base;
            int i4 = this.top;
            if (i3 - i4 < 0 && (forkJoinTaskArr = this.array) != null && (objectVolatile = f12586U.getObjectVolatile(forkJoinTaskArr, (((forkJoinTaskArr.length - 1) & (i4 - 1)) << ASHIFT) + ABASE)) != null && (objectVolatile instanceof CountedCompleter)) {
                CountedCompleter<?> countedCompleter2 = (CountedCompleter) objectVolatile;
                CountedCompleter<?> countedCompleter3 = countedCompleter2;
                while (countedCompleter3 != countedCompleter) {
                    CountedCompleter<?> countedCompleter4 = countedCompleter3.completer;
                    countedCompleter3 = countedCompleter4;
                    if (countedCompleter4 == null) {
                        return null;
                    }
                }
                if (i2 < 0) {
                    if (f12586U.compareAndSwapInt(this, QLOCK, 0, 1)) {
                        if (this.top == i4 && this.array == forkJoinTaskArr && f12586U.compareAndSwapObject(forkJoinTaskArr, j2, countedCompleter2, null)) {
                            f12586U.putOrderedInt(this, QTOP, i4 - 1);
                            f12586U.putOrderedInt(this, QLOCK, 0);
                            return countedCompleter2;
                        }
                        f12586U.compareAndSwapInt(this, QLOCK, 1, 0);
                        return null;
                    }
                    return null;
                }
                if (f12586U.compareAndSwapObject(forkJoinTaskArr, j2, countedCompleter2, null)) {
                    f12586U.putOrderedInt(this, QTOP, i4 - 1);
                    return countedCompleter2;
                }
                return null;
            }
            return null;
        }

        final int pollAndExecCC(CountedCompleter<?> countedCompleter) {
            int i2;
            ForkJoinTask<?>[] forkJoinTaskArr;
            int i3 = this.base;
            if (i3 - this.top >= 0 || (forkJoinTaskArr = this.array) == null) {
                i2 = i3 | Integer.MIN_VALUE;
            } else {
                long length = (((forkJoinTaskArr.length - 1) & i3) << ASHIFT) + ABASE;
                Object objectVolatile = f12586U.getObjectVolatile(forkJoinTaskArr, length);
                if (objectVolatile == null) {
                    i2 = 2;
                } else if (!(objectVolatile instanceof CountedCompleter)) {
                    i2 = -1;
                } else {
                    CountedCompleter<?> countedCompleter2 = (CountedCompleter) objectVolatile;
                    CountedCompleter<?> countedCompleter3 = countedCompleter2;
                    while (true) {
                        if (countedCompleter3 == countedCompleter) {
                            if (this.base == i3 && f12586U.compareAndSwapObject(forkJoinTaskArr, length, countedCompleter2, null)) {
                                this.base = i3 + 1;
                                countedCompleter2.doExec();
                                i2 = 1;
                            } else {
                                i2 = 2;
                            }
                        } else {
                            CountedCompleter<?> countedCompleter4 = countedCompleter3.completer;
                            countedCompleter3 = countedCompleter4;
                            if (countedCompleter4 == null) {
                                i2 = -1;
                                break;
                            }
                        }
                    }
                }
            }
            return i2;
        }

        final boolean isApparentlyUnblocked() {
            ForkJoinWorkerThread forkJoinWorkerThread;
            Thread.State state;
            return (this.scanState < 0 || (forkJoinWorkerThread = this.owner) == null || (state = forkJoinWorkerThread.getState()) == Thread.State.BLOCKED || state == Thread.State.WAITING || state == Thread.State.TIMED_WAITING) ? false : true;
        }

        static {
            try {
                f12586U = Unsafe.getUnsafe();
                QTOP = f12586U.objectFieldOffset(WorkQueue.class.getDeclaredField(JSplitPane.TOP));
                QLOCK = f12586U.objectFieldOffset(WorkQueue.class.getDeclaredField("qlock"));
                QCURRENTSTEAL = f12586U.objectFieldOffset(WorkQueue.class.getDeclaredField("currentSteal"));
                ABASE = f12586U.arrayBaseOffset(ForkJoinTask[].class);
                int iArrayIndexScale = f12586U.arrayIndexScale(ForkJoinTask[].class);
                if ((iArrayIndexScale & (iArrayIndexScale - 1)) != 0) {
                    throw new Error("data type scale not a power of two");
                }
                ASHIFT = 31 - Integer.numberOfLeadingZeros(iArrayIndexScale);
            } catch (Exception e2) {
                throw new Error(e2);
            }
        }
    }

    private static final synchronized int nextPoolId() {
        int i2 = poolNumberSequence + 1;
        poolNumberSequence = i2;
        return i2;
    }

    private int lockRunState() {
        int i2 = this.runState;
        if ((i2 & 1) == 0) {
            int i3 = i2 | 1;
            if (f12585U.compareAndSwapInt(this, RUNSTATE, i2, i3)) {
                return i3;
            }
        }
        return awaitRunStateLock();
    }

    private int awaitRunStateLock() {
        int i2;
        AtomicLong atomicLong;
        boolean z2 = false;
        int i3 = 0;
        int iNextSecondarySeed = 0;
        while (true) {
            int i4 = this.runState;
            if ((i4 & 1) == 0) {
                i2 = i4 | 1;
                if (f12585U.compareAndSwapInt(this, RUNSTATE, i4, i2)) {
                    break;
                }
            } else if (iNextSecondarySeed == 0) {
                iNextSecondarySeed = ThreadLocalRandom.nextSecondarySeed();
            } else if (i3 > 0) {
                int i5 = iNextSecondarySeed ^ (iNextSecondarySeed << 6);
                int i6 = i5 ^ (i5 >>> 21);
                iNextSecondarySeed = i6 ^ (i6 << 7);
                if (iNextSecondarySeed >= 0) {
                    i3--;
                }
            } else if ((i4 & 4) == 0 || (atomicLong = this.stealCounter) == null) {
                Thread.yield();
            } else if (f12585U.compareAndSwapInt(this, RUNSTATE, i4, i4 | 2)) {
                synchronized (atomicLong) {
                    if ((this.runState & 2) != 0) {
                        try {
                            atomicLong.wait();
                        } catch (InterruptedException e2) {
                            if (!(Thread.currentThread() instanceof ForkJoinWorkerThread)) {
                                z2 = true;
                            }
                        }
                    } else {
                        atomicLong.notifyAll();
                    }
                }
            } else {
                continue;
            }
        }
        if (z2) {
            try {
                Thread.currentThread().interrupt();
            } catch (SecurityException e3) {
            }
        }
        return i2;
    }

    private void unlockRunState(int i2, int i3) {
        if (!f12585U.compareAndSwapInt(this, RUNSTATE, i2, i3)) {
            AtomicLong atomicLong = this.stealCounter;
            this.runState = i3;
            if (atomicLong != null) {
                synchronized (atomicLong) {
                    atomicLong.notifyAll();
                }
            }
        }
    }

    private boolean createWorker() {
        ForkJoinWorkerThreadFactory forkJoinWorkerThreadFactory = this.factory;
        Throwable th = null;
        ForkJoinWorkerThread forkJoinWorkerThread = null;
        if (forkJoinWorkerThreadFactory != null) {
            try {
                ForkJoinWorkerThread forkJoinWorkerThreadNewThread = forkJoinWorkerThreadFactory.newThread(this);
                forkJoinWorkerThread = forkJoinWorkerThreadNewThread;
                if (forkJoinWorkerThreadNewThread != null) {
                    forkJoinWorkerThread.start();
                    return true;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
        deregisterWorker(forkJoinWorkerThread, th);
        return false;
    }

    private void tryAddWorker(long j2) {
        boolean zCompareAndSwapLong = false;
        do {
            long j3 = (AC_MASK & (j2 + AC_UNIT)) | (TC_MASK & (j2 + 4294967296L));
            if (this.ctl == j2) {
                int iLockRunState = lockRunState();
                int i2 = iLockRunState & 536870912;
                if (i2 == 0) {
                    zCompareAndSwapLong = f12585U.compareAndSwapLong(this, CTL, j2, j3);
                }
                unlockRunState(iLockRunState, iLockRunState & (-2));
                if (i2 == 0) {
                    if (zCompareAndSwapLong) {
                        createWorker();
                        return;
                    }
                } else {
                    return;
                }
            }
            long j4 = this.ctl;
            j2 = j4;
            if ((j4 & ADD_WORKER) == 0) {
                return;
            }
        } while (((int) j2) == 0);
    }

    final WorkQueue registerWorker(ForkJoinWorkerThread forkJoinWorkerThread) {
        forkJoinWorkerThread.setDaemon(true);
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = this.ueh;
        if (uncaughtExceptionHandler != null) {
            forkJoinWorkerThread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        }
        WorkQueue workQueue = new WorkQueue(this, forkJoinWorkerThread);
        int i2 = 0;
        int i3 = this.config & (-65536);
        int iLockRunState = lockRunState();
        try {
            WorkQueue[] workQueueArr = this.workQueues;
            WorkQueue[] workQueueArr2 = workQueueArr;
            if (workQueueArr != null) {
                int length = workQueueArr2.length;
                int i4 = length;
                if (length > 0) {
                    int i5 = this.indexSeed + SEED_INCREMENT;
                    this.indexSeed = i5;
                    int i6 = i4 - 1;
                    i2 = ((i5 << 1) | 1) & i6;
                    if (workQueueArr2[i2] != null) {
                        int i7 = 0;
                        int i8 = i4 <= 4 ? 2 : ((i4 >>> 1) & 65534) + 2;
                        while (true) {
                            int i9 = (i2 + i8) & i6;
                            i2 = i9;
                            if (workQueueArr2[i9] == null) {
                                break;
                            }
                            i7++;
                            if (i7 >= i4) {
                                int i10 = i4 << 1;
                                i4 = i10;
                                WorkQueue[] workQueueArr3 = (WorkQueue[]) Arrays.copyOf(workQueueArr2, i10);
                                workQueueArr2 = workQueueArr3;
                                this.workQueues = workQueueArr3;
                                i6 = i4 - 1;
                                i7 = 0;
                            }
                        }
                    }
                    workQueue.hint = i5;
                    workQueue.config = i2 | i3;
                    workQueue.scanState = i2;
                    workQueueArr2[i2] = workQueue;
                }
            }
            forkJoinWorkerThread.setName(this.workerNamePrefix.concat(Integer.toString(i2 >>> 1)));
            return workQueue;
        } finally {
            unlockRunState(iLockRunState, iLockRunState & (-2));
        }
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [long, sun.misc.Unsafe] */
    final void deregisterWorker(ForkJoinWorkerThread forkJoinWorkerThread, Throwable th) {
        ?? r0;
        long j2;
        WorkQueue[] workQueueArr;
        int length;
        WorkQueue workQueue = null;
        if (forkJoinWorkerThread != null) {
            WorkQueue workQueue2 = forkJoinWorkerThread.workQueue;
            workQueue = workQueue2;
            if (workQueue2 != null) {
                int i2 = workQueue.config & 65535;
                int iLockRunState = lockRunState();
                WorkQueue[] workQueueArr2 = this.workQueues;
                if (workQueueArr2 != null && workQueueArr2.length > i2 && workQueueArr2[i2] == workQueue) {
                    workQueueArr2[i2] = null;
                }
                unlockRunState(iLockRunState, iLockRunState & (-2));
            }
        }
        do {
            r0 = f12585U;
            long j3 = CTL;
            j2 = this.ctl;
        } while (!r0.compareAndSwapLong(this, r0, j2, (AC_MASK & (j2 - AC_UNIT)) | (TC_MASK & (j2 - 4294967296L)) | (4294967295L & j2)));
        if (workQueue != null) {
            workQueue.qlock = -1;
            workQueue.transferStealCount(this);
            workQueue.cancelAll();
        }
        while (true) {
            if (tryTerminate(false, false) || workQueue == null || workQueue.array == null || (this.runState & 536870912) != 0 || (workQueueArr = this.workQueues) == null || (length = workQueueArr.length - 1) < 0) {
                break;
            }
            long j4 = this.ctl;
            int i3 = (int) j4;
            if (i3 != 0) {
                if (tryRelease(j4, workQueueArr[i3 & length], AC_UNIT)) {
                    break;
                }
            } else if (th != null && (j4 & ADD_WORKER) != 0) {
                tryAddWorker(j4);
            }
        }
        if (th == null) {
            ForkJoinTask.helpExpungeStaleExceptions();
        } else {
            ForkJoinTask.rethrow(th);
        }
    }

    final void signalWork(WorkQueue[] workQueueArr, WorkQueue workQueue) {
        int i2;
        WorkQueue workQueue2;
        while (true) {
            long j2 = this.ctl;
            if (j2 < 0) {
                int i3 = (int) j2;
                if (i3 == 0) {
                    if ((j2 & ADD_WORKER) != 0) {
                        tryAddWorker(j2);
                        return;
                    }
                    return;
                }
                if (workQueueArr != null && workQueueArr.length > (i2 = i3 & 65535) && (workQueue2 = workQueueArr[i2]) != null) {
                    int i4 = (i3 + 65536) & Integer.MAX_VALUE;
                    int i5 = i3 - workQueue2.scanState;
                    long j3 = (UC_MASK & (j2 + AC_UNIT)) | (4294967295L & workQueue2.stackPred);
                    if (i5 == 0 && f12585U.compareAndSwapLong(this, CTL, j2, j3)) {
                        workQueue2.scanState = i4;
                        Thread thread = workQueue2.parker;
                        if (thread != null) {
                            f12585U.unpark(thread);
                            return;
                        }
                        return;
                    }
                    if (workQueue != null && workQueue.base == workQueue.top) {
                        return;
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private boolean tryRelease(long j2, WorkQueue workQueue, long j3) {
        int i2 = (int) j2;
        int i3 = (i2 + 65536) & Integer.MAX_VALUE;
        if (workQueue != null && workQueue.scanState == i2) {
            if (f12585U.compareAndSwapLong(this, CTL, j2, (UC_MASK & (j2 + j3)) | (4294967295L & workQueue.stackPred))) {
                workQueue.scanState = i3;
                Thread thread = workQueue.parker;
                if (thread != null) {
                    f12585U.unpark(thread);
                    return true;
                }
                return true;
            }
            return false;
        }
        return false;
    }

    final void runWorker(WorkQueue workQueue) {
        workQueue.growArray();
        int i2 = workQueue.hint;
        int i3 = i2 == 0 ? 1 : i2;
        while (true) {
            int i4 = i3;
            ForkJoinTask<?> forkJoinTaskScan = scan(workQueue, i4);
            if (forkJoinTaskScan != null) {
                workQueue.runTask(forkJoinTaskScan);
            } else if (!awaitWork(workQueue, i4)) {
                return;
            }
            int i5 = i4 ^ (i4 << 13);
            int i6 = i5 ^ (i5 >>> 17);
            i3 = i6 ^ (i6 << 5);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:44:0x012e A[PHI: r14
  0x012e: PHI (r14v3 int) = (r14v1 int), (r14v6 int) binds: [B:41:0x011f, B:43:0x012b] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.util.concurrent.ForkJoinTask<?> scan(java.util.concurrent.ForkJoinPool.WorkQueue r10, int r11) {
        /*
            Method dump skipped, instructions count: 416
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinPool.scan(java.util.concurrent.ForkJoinPool$WorkQueue, int):java.util.concurrent.ForkJoinTask");
    }

    private boolean awaitWork(WorkQueue workQueue, int i2) {
        WorkQueue[] workQueueArr;
        int i3;
        WorkQueue workQueue2;
        long jNanoTime;
        long j2;
        long j3;
        if (workQueue == null || workQueue.qlock < 0) {
            return false;
        }
        int i4 = workQueue.stackPred;
        int i5 = 0;
        while (true) {
            int i6 = workQueue.scanState;
            if (i6 < 0) {
                if (i5 > 0) {
                    int i7 = i2 ^ (i2 << 6);
                    int i8 = i7 ^ (i7 >>> 21);
                    i2 = i8 ^ (i8 << 7);
                    if (i2 >= 0) {
                        i5--;
                        if (i5 == 0 && i4 != 0 && (workQueueArr = this.workQueues) != null && (i3 = i4 & 65535) < workQueueArr.length && (workQueue2 = workQueueArr[i3]) != null && (workQueue2.parker == null || workQueue2.scanState >= 0)) {
                            i5 = 0;
                        }
                    }
                } else {
                    if (workQueue.qlock < 0) {
                        return false;
                    }
                    if (Thread.interrupted()) {
                        continue;
                    } else {
                        long j4 = this.ctl;
                        int i9 = ((int) (j4 >> 48)) + (this.config & 65535);
                        if ((i9 <= 0 && tryTerminate(false, false)) || (this.runState & 536870912) != 0) {
                            return false;
                        }
                        if (i9 <= 0 && i6 == ((int) j4)) {
                            j3 = (UC_MASK & (j4 + AC_UNIT)) | (4294967295L & i4);
                            short s2 = (short) (j4 >>> 32);
                            if (s2 > 2 && f12585U.compareAndSwapLong(this, CTL, j4, j3)) {
                                return false;
                            }
                            j2 = IDLE_TIMEOUT * (s2 >= 0 ? 1 : 1 - s2);
                            jNanoTime = (System.nanoTime() + j2) - TIMEOUT_SLOP;
                        } else {
                            jNanoTime = 0;
                            j2 = 0;
                            j3 = 0;
                        }
                        Thread threadCurrentThread = Thread.currentThread();
                        f12585U.putObject(threadCurrentThread, PARKBLOCKER, this);
                        workQueue.parker = threadCurrentThread;
                        if (workQueue.scanState < 0 && this.ctl == j4) {
                            f12585U.park(false, j2);
                        }
                        f12585U.putOrderedObject(workQueue, QPARKER, null);
                        f12585U.putObject(threadCurrentThread, PARKBLOCKER, (Object) null);
                        if (workQueue.scanState < 0) {
                            if (j2 != 0 && this.ctl == j4 && jNanoTime - System.nanoTime() <= 0 && f12585U.compareAndSwapLong(this, CTL, j4, j3)) {
                                return false;
                            }
                        } else {
                            return true;
                        }
                    }
                }
            } else {
                return true;
            }
        }
    }

    final int helpComplete(WorkQueue workQueue, CountedCompleter<?> countedCompleter, int i2) {
        int length;
        CountedCompleter<?> countedCompleterPopCC;
        int i3 = 0;
        WorkQueue[] workQueueArr = this.workQueues;
        if (workQueueArr != null && (length = workQueueArr.length - 1) >= 0 && countedCompleter != null && workQueue != null) {
            int i4 = workQueue.config;
            int i5 = workQueue.hint ^ workQueue.top;
            int i6 = i5 & length;
            int i7 = 1;
            int i8 = i6;
            int i9 = 0;
            int i10 = 0;
            while (true) {
                int i11 = countedCompleter.status;
                i3 = i11;
                if (i11 < 0) {
                    break;
                }
                if (i7 == 1 && (countedCompleterPopCC = workQueue.popCC(countedCompleter, i4)) != null) {
                    countedCompleterPopCC.doExec();
                    if (i2 != 0) {
                        i2--;
                        if (i2 == 0) {
                            break;
                        }
                    }
                    i6 = i8;
                    i10 = 0;
                    i9 = 0;
                } else {
                    WorkQueue workQueue2 = workQueueArr[i8];
                    if (workQueue2 == null) {
                        i7 = 0;
                    } else {
                        int iPollAndExecCC = workQueue2.pollAndExecCC(countedCompleter);
                        i7 = iPollAndExecCC;
                        if (iPollAndExecCC < 0) {
                            i10 += i7;
                        }
                    }
                    if (i7 > 0) {
                        if (i7 == 1 && i2 != 0) {
                            i2--;
                            if (i2 == 0) {
                                break;
                            }
                        }
                        int i12 = i5 ^ (i5 << 13);
                        int i13 = i12 ^ (i12 >>> 17);
                        i5 = i13 ^ (i13 << 5);
                        int i14 = i5 & length;
                        i8 = i14;
                        i6 = i14;
                        i10 = 0;
                        i9 = 0;
                    } else {
                        int i15 = (i8 + 1) & length;
                        i8 = i15;
                        if (i15 == i6) {
                            int i16 = i9;
                            int i17 = i10;
                            i9 = i17;
                            if (i16 == i17) {
                                break;
                            }
                            i10 = 0;
                        } else {
                            continue;
                        }
                    }
                }
            }
        }
        return i3;
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x0062, code lost:
    
        r15.hint = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x007c, code lost:
    
        r1 = r0.base;
        r12 = r12 + r1;
        r0 = r0.currentJoin;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0095, code lost:
    
        if (r14.status < 0) goto L68;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x009f, code lost:
    
        if (r15.currentJoin != r14) goto L69;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x00a9, code lost:
    
        if (r0.currentSteal == r14) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x00b7, code lost:
    
        if ((r1 - r0.top) >= 0) goto L76;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00ba, code lost:
    
        r0 = r0.array;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00c2, code lost:
    
        if (r0 != null) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00c5, code lost:
    
        r14 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00ca, code lost:
    
        if (r0 != null) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00d0, code lost:
    
        r15 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00d7, code lost:
    
        r0 = (((r0.length - 1) & r1) << java.util.concurrent.ForkJoinPool.ASHIFT) + java.util.concurrent.ForkJoinPool.ABASE;
        r21 = (java.util.concurrent.ForkJoinTask) java.util.concurrent.ForkJoinPool.f12585U.getObjectVolatile(r0, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0100, code lost:
    
        if (r0.base != r1) goto L81;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0105, code lost:
    
        if (r21 != null) goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x0119, code lost:
    
        if (java.util.concurrent.ForkJoinPool.f12585U.compareAndSwapObject(r0, r0, r21, null) == false) goto L82;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x011c, code lost:
    
        r0.base = r1 + 1;
        r0 = r8.currentSteal;
        r0 = r8.top;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0131, code lost:
    
        java.util.concurrent.ForkJoinPool.f12585U.putOrderedObject(r8, java.util.concurrent.ForkJoinPool.QCURRENTSTEAL, r21);
        r21.doExec();
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x0147, code lost:
    
        if (r9.status < 0) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0150, code lost:
    
        if (r8.top == r0) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0153, code lost:
    
        r0 = r8.pop();
        r21 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x015a, code lost:
    
        if (r0 != null) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x015d, code lost:
    
        java.util.concurrent.ForkJoinPool.f12585U.putOrderedObject(r8, java.util.concurrent.ForkJoinPool.QCURRENTSTEAL, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x0171, code lost:
    
        if (r8.base == r8.top) goto L83;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x0174, code lost:
    
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void helpStealer(java.util.concurrent.ForkJoinPool.WorkQueue r8, java.util.concurrent.ForkJoinTask<?> r9) {
        /*
            Method dump skipped, instructions count: 394
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinPool.helpStealer(java.util.concurrent.ForkJoinPool$WorkQueue, java.util.concurrent.ForkJoinTask):void");
    }

    private boolean tryCompensate(WorkQueue workQueue) {
        boolean zCompareAndSwapLong;
        WorkQueue[] workQueueArr;
        int length;
        int i2;
        if (workQueue == null || workQueue.qlock < 0 || (workQueueArr = this.workQueues) == null || (length = workQueueArr.length - 1) <= 0 || (i2 = this.config & 65535) == 0) {
            zCompareAndSwapLong = false;
        } else {
            long j2 = this.ctl;
            int i3 = (int) j2;
            if (i3 != 0) {
                zCompareAndSwapLong = tryRelease(j2, workQueueArr[i3 & length], 0L);
            } else {
                int i4 = ((int) (j2 >> 48)) + i2;
                int i5 = ((short) (j2 >> 32)) + i2;
                int i6 = 0;
                for (int i7 = 0; i7 <= length; i7++) {
                    WorkQueue workQueue2 = workQueueArr[((i7 << 1) | 1) & length];
                    if (workQueue2 != null) {
                        if ((workQueue2.scanState & 1) != 0) {
                            break;
                        }
                        i6++;
                    }
                }
                if (i6 != (i5 << 1) || this.ctl != j2) {
                    zCompareAndSwapLong = false;
                } else if (i5 >= i2 && i4 > 1 && workQueue.isEmpty()) {
                    zCompareAndSwapLong = f12585U.compareAndSwapLong(this, CTL, j2, (AC_MASK & (j2 - AC_UNIT)) | (281474976710655L & j2));
                } else {
                    if (i5 >= 32767 || (this == common && i5 >= i2 + commonMaxSpares)) {
                        throw new RejectedExecutionException("Thread limit exceeded replacing blocked worker");
                    }
                    boolean zCompareAndSwapLong2 = false;
                    long j3 = (AC_MASK & j2) | (TC_MASK & (j2 + 4294967296L));
                    int iLockRunState = lockRunState();
                    if ((iLockRunState & 536870912) == 0) {
                        zCompareAndSwapLong2 = f12585U.compareAndSwapLong(this, CTL, j2, j3);
                    }
                    unlockRunState(iLockRunState, iLockRunState & (-2));
                    zCompareAndSwapLong = zCompareAndSwapLong2 && createWorker();
                }
            }
        }
        return zCompareAndSwapLong;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:21:0x005e  */
    /* JADX WARN: Type inference failed for: r0v6, types: [sun.misc.Unsafe] */
    /* JADX WARN: Type inference failed for: r3v0, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r3v1 */
    /* JADX WARN: Type inference failed for: r3v3 */
    /* JADX WARN: Type inference failed for: r3v4 */
    /* JADX WARN: Type inference failed for: r3v5 */
    /* JADX WARN: Type inference failed for: r3v6 */
    /* JADX WARN: Type inference failed for: r3v7 */
    /* JADX WARN: Type inference failed for: r3v8 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    final int awaitJoin(java.util.concurrent.ForkJoinPool.WorkQueue r8, java.util.concurrent.ForkJoinTask<?> r9, long r10) {
        /*
            Method dump skipped, instructions count: 206
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinPool.awaitJoin(java.util.concurrent.ForkJoinPool$WorkQueue, java.util.concurrent.ForkJoinTask, long):int");
    }

    private WorkQueue findNonEmptyStealQueue() {
        int length;
        int iNextSecondarySeed = ThreadLocalRandom.nextSecondarySeed();
        WorkQueue[] workQueueArr = this.workQueues;
        if (workQueueArr != null && (length = workQueueArr.length - 1) >= 0) {
            int i2 = iNextSecondarySeed & length;
            int i3 = i2;
            int i4 = 0;
            int i5 = 0;
            while (true) {
                WorkQueue workQueue = workQueueArr[i3];
                if (workQueue != null) {
                    int i6 = workQueue.base;
                    if (i6 - workQueue.top < 0) {
                        return workQueue;
                    }
                    i5 += i6;
                }
                int i7 = (i3 + 1) & length;
                i3 = i7;
                if (i7 == i2) {
                    int i8 = i4;
                    int i9 = i5;
                    i4 = i9;
                    if (i8 != i9) {
                        i5 = 0;
                    } else {
                        return null;
                    }
                }
            }
        } else {
            return null;
        }
    }

    final void helpQuiescePool(WorkQueue workQueue) {
        ForkJoinTask<?> forkJoinTaskPollAt;
        ForkJoinTask<?> forkJoinTask = workQueue.currentSteal;
        boolean z2 = true;
        while (true) {
            workQueue.execLocalTasks();
            WorkQueue workQueueFindNonEmptyStealQueue = findNonEmptyStealQueue();
            if (workQueueFindNonEmptyStealQueue != null) {
                if (!z2) {
                    z2 = true;
                    f12585U.getAndAddLong(this, CTL, AC_UNIT);
                }
                int i2 = workQueueFindNonEmptyStealQueue.base;
                if (i2 - workQueueFindNonEmptyStealQueue.top < 0 && (forkJoinTaskPollAt = workQueueFindNonEmptyStealQueue.pollAt(i2)) != null) {
                    f12585U.putOrderedObject(workQueue, QCURRENTSTEAL, forkJoinTaskPollAt);
                    forkJoinTaskPollAt.doExec();
                    int i3 = workQueue.nsteals + 1;
                    workQueue.nsteals = i3;
                    if (i3 < 0) {
                        workQueue.transferStealCount(this);
                    }
                }
            } else if (z2) {
                long j2 = this.ctl;
                long j3 = (AC_MASK & (AC_MASK - AC_UNIT)) | (281474976710655L & j2);
                if (((int) (j3 >> 48)) + (this.config & 65535) <= 0) {
                    break;
                } else if (f12585U.compareAndSwapLong(this, CTL, j2, j3)) {
                    z2 = false;
                }
            } else {
                long j4 = this.ctl;
                if (((int) (j4 >> 48)) + (this.config & 65535) <= 0 && f12585U.compareAndSwapLong(this, CTL, j4, j4 + AC_UNIT)) {
                    break;
                }
            }
        }
        f12585U.putOrderedObject(workQueue, QCURRENTSTEAL, forkJoinTask);
    }

    final ForkJoinTask<?> nextTaskFor(WorkQueue workQueue) {
        ForkJoinTask<?> forkJoinTaskPollAt;
        while (true) {
            ForkJoinTask<?> forkJoinTaskNextLocalTask = workQueue.nextLocalTask();
            if (forkJoinTaskNextLocalTask != null) {
                return forkJoinTaskNextLocalTask;
            }
            WorkQueue workQueueFindNonEmptyStealQueue = findNonEmptyStealQueue();
            if (workQueueFindNonEmptyStealQueue == null) {
                return null;
            }
            int i2 = workQueueFindNonEmptyStealQueue.base;
            if (i2 - workQueueFindNonEmptyStealQueue.top < 0 && (forkJoinTaskPollAt = workQueueFindNonEmptyStealQueue.pollAt(i2)) != null) {
                return forkJoinTaskPollAt;
            }
        }
    }

    static int getSurplusQueuedTaskCount() {
        int i2;
        Thread threadCurrentThread = Thread.currentThread();
        if (threadCurrentThread instanceof ForkJoinWorkerThread) {
            ForkJoinWorkerThread forkJoinWorkerThread = (ForkJoinWorkerThread) threadCurrentThread;
            ForkJoinPool forkJoinPool = forkJoinWorkerThread.pool;
            int i3 = forkJoinPool.config & 65535;
            WorkQueue workQueue = forkJoinWorkerThread.workQueue;
            int i4 = workQueue.top - workQueue.base;
            int i5 = ((int) (forkJoinPool.ctl >> 48)) + i3;
            int i6 = i3 >>> 1;
            if (i5 > i6) {
                i2 = 0;
            } else {
                int i7 = i6 >>> 1;
                if (i5 > i7) {
                    i2 = 1;
                } else {
                    int i8 = i7 >>> 1;
                    i2 = i5 > i8 ? 2 : i5 > (i8 >>> 1) ? 4 : 8;
                }
            }
            return i4 - i2;
        }
        return 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:107:0x022d, code lost:
    
        return true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:144:?, code lost:
    
        return true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x0138, code lost:
    
        if ((r8.runState & 1073741824) != 0) goto L107;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x013b, code lost:
    
        r0 = lockRunState();
        unlockRunState(r0, (r0 & (-2)) | 1073741824);
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0150, code lost:
    
        monitor-enter(r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x0151, code lost:
    
        notifyAll();
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0157, code lost:
    
        monitor-exit(r8);
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v0 */
    /* JADX WARN: Type inference failed for: r2v18 */
    /* JADX WARN: Type inference failed for: r2v4 */
    /* JADX WARN: Type inference failed for: r8v0, types: [java.lang.Object, java.util.concurrent.ForkJoinPool, long] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean tryTerminate(boolean r9, boolean r10) {
        /*
            Method dump skipped, instructions count: 559
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinPool.tryTerminate(boolean, boolean):boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:41:0x013b A[Catch: all -> 0x0187, TryCatch #1 {all -> 0x0187, blocks: (B:39:0x012b, B:43:0x0146, B:44:0x0176, B:41:0x013b), top: B:74:0x012b }] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0146 A[Catch: all -> 0x0187, PHI: r17
  0x0146: PHI (r17v4 java.util.concurrent.ForkJoinTask<?>[]) = (r17v3 java.util.concurrent.ForkJoinTask<?>[]), (r17v5 java.util.concurrent.ForkJoinTask<?>[]) binds: [B:40:0x0138, B:42:0x0143] A[DONT_GENERATE, DONT_INLINE], TryCatch #1 {all -> 0x0187, blocks: (B:39:0x012b, B:43:0x0146, B:44:0x0176, B:41:0x013b), top: B:74:0x012b }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void externalSubmit(java.util.concurrent.ForkJoinTask<?> r9) {
        /*
            Method dump skipped, instructions count: 545
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinPool.externalSubmit(java.util.concurrent.ForkJoinTask):void");
    }

    final void externalPush(ForkJoinTask<?> forkJoinTask) {
        int length;
        WorkQueue workQueue;
        int length2;
        int i2;
        int i3;
        int probe = ThreadLocalRandom.getProbe();
        int i4 = this.runState;
        WorkQueue[] workQueueArr = this.workQueues;
        if (workQueueArr != null && (length = workQueueArr.length - 1) >= 0 && (workQueue = workQueueArr[length & probe & 126]) != null && probe != 0 && i4 > 0 && f12585U.compareAndSwapInt(workQueue, QLOCK, 0, 1)) {
            ForkJoinTask<?>[] forkJoinTaskArr = workQueue.array;
            if (forkJoinTaskArr != null && (length2 = forkJoinTaskArr.length - 1) > (i3 = (i2 = workQueue.top) - workQueue.base)) {
                f12585U.putOrderedObject(forkJoinTaskArr, ((length2 & i2) << ASHIFT) + ABASE, forkJoinTask);
                f12585U.putOrderedInt(workQueue, QTOP, i2 + 1);
                f12585U.putIntVolatile(workQueue, QLOCK, 0);
                if (i3 <= 1) {
                    signalWork(workQueueArr, workQueue);
                    return;
                }
                return;
            }
            f12585U.compareAndSwapInt(workQueue, QLOCK, 1, 0);
        }
        externalSubmit(forkJoinTask);
    }

    static WorkQueue commonSubmitterQueue() {
        WorkQueue[] workQueueArr;
        int length;
        ForkJoinPool forkJoinPool = common;
        int probe = ThreadLocalRandom.getProbe();
        if (forkJoinPool == null || (workQueueArr = forkJoinPool.workQueues) == null || (length = workQueueArr.length - 1) < 0) {
            return null;
        }
        return workQueueArr[length & probe & 126];
    }

    final boolean tryExternalUnpush(ForkJoinTask<?> forkJoinTask) {
        int length;
        WorkQueue workQueue;
        ForkJoinTask<?>[] forkJoinTaskArr;
        int i2;
        int probe = ThreadLocalRandom.getProbe();
        WorkQueue[] workQueueArr = this.workQueues;
        if (workQueueArr != null && (length = workQueueArr.length - 1) >= 0 && (workQueue = workQueueArr[length & probe & 126]) != null && (forkJoinTaskArr = workQueue.array) != null && (i2 = workQueue.top) != workQueue.base) {
            long length2 = (((forkJoinTaskArr.length - 1) & (i2 - 1)) << ASHIFT) + ABASE;
            if (f12585U.compareAndSwapInt(workQueue, QLOCK, 0, 1)) {
                if (workQueue.top == i2 && workQueue.array == forkJoinTaskArr && f12585U.getObject(forkJoinTaskArr, length2) == forkJoinTask && f12585U.compareAndSwapObject(forkJoinTaskArr, length2, forkJoinTask, null)) {
                    f12585U.putOrderedInt(workQueue, QTOP, i2 - 1);
                    f12585U.putOrderedInt(workQueue, QLOCK, 0);
                    return true;
                }
                f12585U.compareAndSwapInt(workQueue, QLOCK, 1, 0);
                return false;
            }
            return false;
        }
        return false;
    }

    final int externalHelpComplete(CountedCompleter<?> countedCompleter, int i2) {
        int length;
        int probe = ThreadLocalRandom.getProbe();
        WorkQueue[] workQueueArr = this.workQueues;
        if (workQueueArr == null || (length = workQueueArr.length) == 0) {
            return 0;
        }
        return helpComplete(workQueueArr[(length - 1) & probe & 126], countedCompleter, i2);
    }

    public ForkJoinPool() {
        this(Math.min(32767, Runtime.getRuntime().availableProcessors()), defaultForkJoinWorkerThreadFactory, null, false);
    }

    public ForkJoinPool(int i2) {
        this(i2, defaultForkJoinWorkerThreadFactory, null, false);
    }

    public ForkJoinPool(int i2, ForkJoinWorkerThreadFactory forkJoinWorkerThreadFactory, Thread.UncaughtExceptionHandler uncaughtExceptionHandler, boolean z2) {
        this(checkParallelism(i2), checkFactory(forkJoinWorkerThreadFactory), uncaughtExceptionHandler, z2 ? 65536 : 0, "ForkJoinPool-" + nextPoolId() + "-worker-");
        checkPermission();
    }

    private static int checkParallelism(int i2) {
        if (i2 <= 0 || i2 > 32767) {
            throw new IllegalArgumentException();
        }
        return i2;
    }

    private static ForkJoinWorkerThreadFactory checkFactory(ForkJoinWorkerThreadFactory forkJoinWorkerThreadFactory) {
        if (forkJoinWorkerThreadFactory == null) {
            throw new NullPointerException();
        }
        return forkJoinWorkerThreadFactory;
    }

    private ForkJoinPool(int i2, ForkJoinWorkerThreadFactory forkJoinWorkerThreadFactory, Thread.UncaughtExceptionHandler uncaughtExceptionHandler, int i3, String str) {
        this.workerNamePrefix = str;
        this.factory = forkJoinWorkerThreadFactory;
        this.ueh = uncaughtExceptionHandler;
        this.config = (i2 & 65535) | i3;
        long j2 = -i2;
        this.ctl = ((j2 << 48) & AC_MASK) | ((j2 << 32) & TC_MASK);
    }

    public static ForkJoinPool commonPool() {
        return common;
    }

    public <T> T invoke(ForkJoinTask<T> forkJoinTask) {
        if (forkJoinTask == null) {
            throw new NullPointerException();
        }
        externalPush(forkJoinTask);
        return forkJoinTask.join();
    }

    public void execute(ForkJoinTask<?> forkJoinTask) {
        if (forkJoinTask == null) {
            throw new NullPointerException();
        }
        externalPush(forkJoinTask);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.Executor
    public void execute(Runnable runnable) {
        ForkJoinTask<?> runnableExecuteAction;
        if (runnable == 0) {
            throw new NullPointerException();
        }
        if (runnable instanceof ForkJoinTask) {
            runnableExecuteAction = (ForkJoinTask) runnable;
        } else {
            runnableExecuteAction = new ForkJoinTask.RunnableExecuteAction(runnable);
        }
        externalPush(runnableExecuteAction);
    }

    public <T> ForkJoinTask<T> submit(ForkJoinTask<T> forkJoinTask) {
        if (forkJoinTask == null) {
            throw new NullPointerException();
        }
        externalPush(forkJoinTask);
        return forkJoinTask;
    }

    @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
    public <T> ForkJoinTask<T> submit(Callable<T> callable) {
        ForkJoinTask.AdaptedCallable adaptedCallable = new ForkJoinTask.AdaptedCallable(callable);
        externalPush(adaptedCallable);
        return adaptedCallable;
    }

    @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
    public <T> ForkJoinTask<T> submit(Runnable runnable, T t2) {
        ForkJoinTask.AdaptedRunnable adaptedRunnable = new ForkJoinTask.AdaptedRunnable(runnable, t2);
        externalPush(adaptedRunnable);
        return adaptedRunnable;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
    public ForkJoinTask<?> submit(Runnable runnable) {
        ForkJoinTask<?> adaptedRunnableAction;
        if (runnable == 0) {
            throw new NullPointerException();
        }
        if (runnable instanceof ForkJoinTask) {
            adaptedRunnableAction = (ForkJoinTask) runnable;
        } else {
            adaptedRunnableAction = new ForkJoinTask.AdaptedRunnableAction(runnable);
        }
        externalPush(adaptedRunnableAction);
        return adaptedRunnableAction;
    }

    @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection) {
        ArrayList arrayList = new ArrayList(collection.size());
        try {
            Iterator<? extends Callable<T>> it = collection.iterator();
            while (it.hasNext()) {
                ForkJoinTask.AdaptedCallable adaptedCallable = new ForkJoinTask.AdaptedCallable(it.next());
                arrayList.add(adaptedCallable);
                externalPush(adaptedCallable);
            }
            int size = arrayList.size();
            for (int i2 = 0; i2 < size; i2++) {
                ((ForkJoinTask) arrayList.get(i2)).quietlyJoin();
            }
            if (1 == 0) {
                int size2 = arrayList.size();
                for (int i3 = 0; i3 < size2; i3++) {
                    ((Future) arrayList.get(i3)).cancel(false);
                }
            }
            return arrayList;
        } catch (Throwable th) {
            if (0 == 0) {
                int size3 = arrayList.size();
                for (int i4 = 0; i4 < size3; i4++) {
                    ((Future) arrayList.get(i4)).cancel(false);
                }
            }
            throw th;
        }
    }

    public ForkJoinWorkerThreadFactory getFactory() {
        return this.factory;
    }

    public Thread.UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return this.ueh;
    }

    public int getParallelism() {
        int i2 = this.config & 65535;
        if (i2 > 0) {
            return i2;
        }
        return 1;
    }

    public static int getCommonPoolParallelism() {
        return commonParallelism;
    }

    public int getPoolSize() {
        return (this.config & 65535) + ((short) (this.ctl >>> 32));
    }

    public boolean getAsyncMode() {
        return (this.config & 65536) != 0;
    }

    public int getRunningThreadCount() {
        int i2 = 0;
        WorkQueue[] workQueueArr = this.workQueues;
        if (workQueueArr != null) {
            for (int i3 = 1; i3 < workQueueArr.length; i3 += 2) {
                WorkQueue workQueue = workQueueArr[i3];
                if (workQueue != null && workQueue.isApparentlyUnblocked()) {
                    i2++;
                }
            }
        }
        return i2;
    }

    public int getActiveThreadCount() {
        int i2 = (this.config & 65535) + ((int) (this.ctl >> 48));
        if (i2 <= 0) {
            return 0;
        }
        return i2;
    }

    public boolean isQuiescent() {
        return (this.config & 65535) + ((int) (this.ctl >> 48)) <= 0;
    }

    public long getStealCount() {
        AtomicLong atomicLong = this.stealCounter;
        long j2 = atomicLong == null ? 0L : atomicLong.get();
        WorkQueue[] workQueueArr = this.workQueues;
        if (workQueueArr != null) {
            for (int i2 = 1; i2 < workQueueArr.length; i2 += 2) {
                if (workQueueArr[i2] != null) {
                    j2 += r0.nsteals;
                }
            }
        }
        return j2;
    }

    public long getQueuedTaskCount() {
        long jQueueSize = 0;
        WorkQueue[] workQueueArr = this.workQueues;
        if (workQueueArr != null) {
            for (int i2 = 1; i2 < workQueueArr.length; i2 += 2) {
                if (workQueueArr[i2] != null) {
                    jQueueSize += r0.queueSize();
                }
            }
        }
        return jQueueSize;
    }

    public int getQueuedSubmissionCount() {
        int iQueueSize = 0;
        WorkQueue[] workQueueArr = this.workQueues;
        if (workQueueArr != null) {
            for (int i2 = 0; i2 < workQueueArr.length; i2 += 2) {
                WorkQueue workQueue = workQueueArr[i2];
                if (workQueue != null) {
                    iQueueSize += workQueue.queueSize();
                }
            }
        }
        return iQueueSize;
    }

    public boolean hasQueuedSubmissions() {
        WorkQueue[] workQueueArr = this.workQueues;
        if (workQueueArr != null) {
            for (int i2 = 0; i2 < workQueueArr.length; i2 += 2) {
                WorkQueue workQueue = workQueueArr[i2];
                if (workQueue != null && !workQueue.isEmpty()) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    protected ForkJoinTask<?> pollSubmission() {
        ForkJoinTask<?> forkJoinTaskPoll;
        WorkQueue[] workQueueArr = this.workQueues;
        if (workQueueArr != null) {
            for (int i2 = 0; i2 < workQueueArr.length; i2 += 2) {
                WorkQueue workQueue = workQueueArr[i2];
                if (workQueue != null && (forkJoinTaskPoll = workQueue.poll()) != null) {
                    return forkJoinTaskPoll;
                }
            }
            return null;
        }
        return null;
    }

    protected int drainTasksTo(Collection<? super ForkJoinTask<?>> collection) {
        int i2 = 0;
        WorkQueue[] workQueueArr = this.workQueues;
        if (workQueueArr != null) {
            for (WorkQueue workQueue : workQueueArr) {
                if (workQueue != null) {
                    while (true) {
                        ForkJoinTask<?> forkJoinTaskPoll = workQueue.poll();
                        if (forkJoinTaskPoll != null) {
                            collection.add(forkJoinTaskPoll);
                            i2++;
                        }
                    }
                }
            }
        }
        return i2;
    }

    public String toString() {
        long j2 = 0;
        long j3 = 0;
        int i2 = 0;
        AtomicLong atomicLong = this.stealCounter;
        long j4 = atomicLong == null ? 0L : atomicLong.get();
        long j5 = this.ctl;
        WorkQueue[] workQueueArr = this.workQueues;
        if (workQueueArr != null) {
            for (int i3 = 0; i3 < workQueueArr.length; i3++) {
                WorkQueue workQueue = workQueueArr[i3];
                if (workQueue != null) {
                    int iQueueSize = workQueue.queueSize();
                    if ((i3 & 1) == 0) {
                        j3 += iQueueSize;
                    } else {
                        j2 += iQueueSize;
                        j4 += workQueue.nsteals;
                        if (workQueue.isApparentlyUnblocked()) {
                            i2++;
                        }
                    }
                }
            }
        }
        int i4 = this.config & 65535;
        int i5 = i4 + ((short) (j5 >>> 32));
        int i6 = i4 + ((int) (j5 >> 48));
        if (i6 < 0) {
            i6 = 0;
        }
        int i7 = this.runState;
        return super.toString() + "[" + ((i7 & 1073741824) != 0 ? "Terminated" : (i7 & 536870912) != 0 ? "Terminating" : (i7 & Integer.MIN_VALUE) != 0 ? "Shutting down" : "Running") + ", parallelism = " + i4 + ", size = " + i5 + ", active = " + i6 + ", running = " + i2 + ", steals = " + j4 + ", tasks = " + j2 + ", submissions = " + j3 + "]";
    }

    @Override // java.util.concurrent.ExecutorService
    public void shutdown() {
        checkPermission();
        tryTerminate(false, true);
    }

    @Override // java.util.concurrent.ExecutorService
    public List<Runnable> shutdownNow() {
        checkPermission();
        tryTerminate(true, true);
        return Collections.emptyList();
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isTerminated() {
        return (this.runState & 1073741824) != 0;
    }

    public boolean isTerminating() {
        int i2 = this.runState;
        return (i2 & 536870912) != 0 && (i2 & 1073741824) == 0;
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isShutdown() {
        return (this.runState & Integer.MIN_VALUE) != 0;
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean awaitTermination(long j2, TimeUnit timeUnit) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        if (this == common) {
            awaitQuiescence(j2, timeUnit);
            return false;
        }
        long nanos = timeUnit.toNanos(j2);
        if (isTerminated()) {
            return true;
        }
        if (nanos <= 0) {
            return false;
        }
        long jNanoTime = System.nanoTime() + nanos;
        synchronized (this) {
            while (!isTerminated()) {
                if (nanos <= 0) {
                    return false;
                }
                long millis = TimeUnit.NANOSECONDS.toMillis(nanos);
                wait(millis > 0 ? millis : 1L);
                nanos = jNanoTime - System.nanoTime();
            }
            return true;
        }
    }

    public boolean awaitQuiescence(long j2, TimeUnit timeUnit) {
        WorkQueue[] workQueueArr;
        int length;
        WorkQueue workQueue;
        long nanos = timeUnit.toNanos(j2);
        Thread threadCurrentThread = Thread.currentThread();
        if (threadCurrentThread instanceof ForkJoinWorkerThread) {
            ForkJoinWorkerThread forkJoinWorkerThread = (ForkJoinWorkerThread) threadCurrentThread;
            if (forkJoinWorkerThread.pool == this) {
                helpQuiescePool(forkJoinWorkerThread.workQueue);
                return true;
            }
        }
        long jNanoTime = System.nanoTime();
        int i2 = 0;
        boolean z2 = true;
        while (!isQuiescent() && (workQueueArr = this.workQueues) != null && (length = workQueueArr.length - 1) >= 0) {
            if (!z2) {
                if (System.nanoTime() - jNanoTime > nanos) {
                    return false;
                }
                Thread.yield();
            }
            z2 = false;
            int i3 = (length + 1) << 2;
            while (true) {
                if (i3 >= 0) {
                    int i4 = i2;
                    i2++;
                    int i5 = i4 & length;
                    if (i5 <= length && i5 >= 0 && (workQueue = workQueueArr[i5]) != null) {
                        int i6 = workQueue.base;
                        if (i6 - workQueue.top < 0) {
                            z2 = true;
                            ForkJoinTask<?> forkJoinTaskPollAt = workQueue.pollAt(i6);
                            if (forkJoinTaskPollAt != null) {
                                forkJoinTaskPollAt.doExec();
                            }
                        }
                    }
                    i3--;
                }
            }
        }
        return true;
    }

    static void quiesceCommonPool() {
        common.awaitQuiescence(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }

    public static void managedBlock(ManagedBlocker managedBlocker) throws InterruptedException {
        ForkJoinWorkerThread forkJoinWorkerThread;
        ForkJoinPool forkJoinPool;
        Thread threadCurrentThread = Thread.currentThread();
        if ((threadCurrentThread instanceof ForkJoinWorkerThread) && (forkJoinPool = (forkJoinWorkerThread = (ForkJoinWorkerThread) threadCurrentThread).pool) != null) {
            WorkQueue workQueue = forkJoinWorkerThread.workQueue;
            while (!managedBlocker.isReleasable()) {
                if (forkJoinPool.tryCompensate(workQueue)) {
                    while (!managedBlocker.isReleasable() && !managedBlocker.block()) {
                        try {
                        } catch (Throwable th) {
                            f12585U.getAndAddLong(forkJoinPool, CTL, AC_UNIT);
                            throw th;
                        }
                    }
                    f12585U.getAndAddLong(forkJoinPool, CTL, AC_UNIT);
                    return;
                }
            }
            return;
        }
        while (!managedBlocker.isReleasable() && !managedBlocker.block()) {
        }
    }

    @Override // java.util.concurrent.AbstractExecutorService
    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T t2) {
        return new ForkJoinTask.AdaptedRunnable(runnable, t2);
    }

    @Override // java.util.concurrent.AbstractExecutorService
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return new ForkJoinTask.AdaptedCallable(callable);
    }

    static {
        try {
            f12585U = Unsafe.getUnsafe();
            CTL = f12585U.objectFieldOffset(ForkJoinPool.class.getDeclaredField("ctl"));
            RUNSTATE = f12585U.objectFieldOffset(ForkJoinPool.class.getDeclaredField("runState"));
            STEALCOUNTER = f12585U.objectFieldOffset(ForkJoinPool.class.getDeclaredField("stealCounter"));
            PARKBLOCKER = f12585U.objectFieldOffset(Thread.class.getDeclaredField("parkBlocker"));
            QTOP = f12585U.objectFieldOffset(WorkQueue.class.getDeclaredField(JSplitPane.TOP));
            QLOCK = f12585U.objectFieldOffset(WorkQueue.class.getDeclaredField("qlock"));
            QSCANSTATE = f12585U.objectFieldOffset(WorkQueue.class.getDeclaredField("scanState"));
            QPARKER = f12585U.objectFieldOffset(WorkQueue.class.getDeclaredField("parker"));
            QCURRENTSTEAL = f12585U.objectFieldOffset(WorkQueue.class.getDeclaredField("currentSteal"));
            QCURRENTJOIN = f12585U.objectFieldOffset(WorkQueue.class.getDeclaredField("currentJoin"));
            ABASE = f12585U.arrayBaseOffset(ForkJoinTask[].class);
            int iArrayIndexScale = f12585U.arrayIndexScale(ForkJoinTask[].class);
            if ((iArrayIndexScale & (iArrayIndexScale - 1)) != 0) {
                throw new Error("data type scale not a power of two");
            }
            ASHIFT = 31 - Integer.numberOfLeadingZeros(iArrayIndexScale);
            commonMaxSpares = 256;
            defaultForkJoinWorkerThreadFactory = new DefaultForkJoinWorkerThreadFactory();
            modifyThreadPermission = new RuntimePermission("modifyThread");
            common = (ForkJoinPool) AccessController.doPrivileged(new PrivilegedAction<ForkJoinPool>() { // from class: java.util.concurrent.ForkJoinPool.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public ForkJoinPool run() {
                    return ForkJoinPool.makeCommonPool();
                }
            });
            int i2 = common.config & 65535;
            commonParallelism = i2 > 0 ? i2 : 1;
        } catch (Exception e2) {
            throw new Error(e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ForkJoinPool makeCommonPool() {
        int i2 = -1;
        ForkJoinWorkerThreadFactory innocuousForkJoinWorkerThreadFactory = null;
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = null;
        try {
            String property = System.getProperty("java.util.concurrent.ForkJoinPool.common.parallelism");
            String property2 = System.getProperty("java.util.concurrent.ForkJoinPool.common.threadFactory");
            String property3 = System.getProperty("java.util.concurrent.ForkJoinPool.common.exceptionHandler");
            if (property != null) {
                i2 = Integer.parseInt(property);
            }
            if (property2 != null) {
                innocuousForkJoinWorkerThreadFactory = (ForkJoinWorkerThreadFactory) ClassLoader.getSystemClassLoader().loadClass(property2).newInstance();
            }
            if (property3 != null) {
                uncaughtExceptionHandler = (Thread.UncaughtExceptionHandler) ClassLoader.getSystemClassLoader().loadClass(property3).newInstance();
            }
        } catch (Exception e2) {
        }
        if (innocuousForkJoinWorkerThreadFactory == null) {
            if (System.getSecurityManager() == null) {
                innocuousForkJoinWorkerThreadFactory = new DefaultCommonPoolForkJoinWorkerThreadFactory();
            } else {
                innocuousForkJoinWorkerThreadFactory = new InnocuousForkJoinWorkerThreadFactory();
            }
        }
        if (i2 < 0) {
            int iAvailableProcessors = Runtime.getRuntime().availableProcessors() - 1;
            i2 = iAvailableProcessors;
            if (iAvailableProcessors <= 0) {
                i2 = 1;
            }
        }
        if (i2 > 32767) {
            i2 = 32767;
        }
        return new ForkJoinPool(i2, innocuousForkJoinWorkerThreadFactory, uncaughtExceptionHandler, 0, "ForkJoinPool.commonPool-worker-");
    }

    /* loaded from: rt.jar:java/util/concurrent/ForkJoinPool$DefaultCommonPoolForkJoinWorkerThreadFactory.class */
    static final class DefaultCommonPoolForkJoinWorkerThreadFactory implements ForkJoinWorkerThreadFactory {
        DefaultCommonPoolForkJoinWorkerThreadFactory() {
        }

        @Override // java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory
        public final ForkJoinWorkerThread newThread(ForkJoinPool forkJoinPool) {
            return new ForkJoinWorkerThread(forkJoinPool, true);
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ForkJoinPool$InnocuousForkJoinWorkerThreadFactory.class */
    static final class InnocuousForkJoinWorkerThreadFactory implements ForkJoinWorkerThreadFactory {
        private static final AccessControlContext innocuousAcc;

        InnocuousForkJoinWorkerThreadFactory() {
        }

        static {
            Permissions permissions = new Permissions();
            permissions.add(ForkJoinPool.modifyThreadPermission);
            permissions.add(new RuntimePermission("enableContextClassLoaderOverride"));
            permissions.add(new RuntimePermission("modifyThreadGroup"));
            innocuousAcc = new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(null, permissions)});
        }

        @Override // java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory
        public final ForkJoinWorkerThread newThread(final ForkJoinPool forkJoinPool) {
            return (ForkJoinWorkerThread.InnocuousForkJoinWorkerThread) AccessController.doPrivileged(new PrivilegedAction<ForkJoinWorkerThread>() { // from class: java.util.concurrent.ForkJoinPool.InnocuousForkJoinWorkerThreadFactory.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public ForkJoinWorkerThread run() {
                    return new ForkJoinWorkerThread.InnocuousForkJoinWorkerThread(forkJoinPool);
                }
            }, innocuousAcc);
        }
    }
}
