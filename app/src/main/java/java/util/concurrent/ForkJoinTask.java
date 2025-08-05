package java.util.concurrent;

import com.sun.org.apache.xml.internal.dtm.DTMManager;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.locks.ReentrantLock;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/util/concurrent/ForkJoinTask.class */
public abstract class ForkJoinTask<V> implements Future<V>, Serializable {
    volatile int status;
    static final int DONE_MASK = -268435456;
    static final int NORMAL = -268435456;
    static final int CANCELLED = -1073741824;
    static final int EXCEPTIONAL = Integer.MIN_VALUE;
    static final int SIGNAL = 65536;
    static final int SMASK = 65535;
    private static final int EXCEPTION_MAP_CAPACITY = 32;
    private static final long serialVersionUID = -7721805057305804111L;

    /* renamed from: U, reason: collision with root package name */
    private static final Unsafe f12587U;
    private static final long STATUS;
    private static final ReentrantLock exceptionTableLock = new ReentrantLock();
    private static final ReferenceQueue<Object> exceptionTableRefQueue = new ReferenceQueue<>();
    private static final ExceptionNode[] exceptionTable = new ExceptionNode[32];

    public abstract V getRawResult();

    protected abstract void setRawResult(V v2);

    protected abstract boolean exec();

    private int setCompletion(int i2) {
        int i3;
        do {
            i3 = this.status;
            if (i3 < 0) {
                return i3;
            }
        } while (!f12587U.compareAndSwapInt(this, STATUS, i3, i3 | i2));
        if ((i3 >>> 16) != 0) {
            synchronized (this) {
                notifyAll();
            }
        }
        return i2;
    }

    final int doExec() {
        int i2 = this.status;
        int completion = i2;
        if (i2 >= 0) {
            try {
                if (exec()) {
                    completion = setCompletion(-268435456);
                }
            } catch (Throwable th) {
                return setExceptionalCompletion(th);
            }
        }
        return completion;
    }

    final void internalWait(long j2) {
        int i2 = this.status;
        if (i2 >= 0 && f12587U.compareAndSwapInt(this, STATUS, i2, i2 | 65536)) {
            synchronized (this) {
                if (this.status >= 0) {
                    try {
                        wait(j2);
                    } catch (InterruptedException e2) {
                    }
                } else {
                    notifyAll();
                }
            }
        }
    }

    private int externalAwaitDone() {
        int iDoExec;
        int i2;
        if (this instanceof CountedCompleter) {
            iDoExec = ForkJoinPool.common.externalHelpComplete((CountedCompleter) this, 0);
        } else {
            iDoExec = ForkJoinPool.common.tryExternalUnpush(this) ? doExec() : 0;
        }
        int i3 = iDoExec;
        if (i3 >= 0) {
            int i4 = this.status;
            i3 = i4;
            if (i4 >= 0) {
                boolean z2 = false;
                do {
                    if (f12587U.compareAndSwapInt(this, STATUS, i3, i3 | 65536)) {
                        synchronized (this) {
                            if (this.status >= 0) {
                                try {
                                    wait(0L);
                                } catch (InterruptedException e2) {
                                    z2 = true;
                                }
                            } else {
                                notifyAll();
                            }
                        }
                    }
                    i2 = this.status;
                    i3 = i2;
                } while (i2 >= 0);
                if (z2) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return i3;
    }

    private int externalInterruptibleAwaitDone() throws InterruptedException {
        int iDoExec;
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        int i2 = this.status;
        int i3 = i2;
        if (i2 >= 0) {
            if (this instanceof CountedCompleter) {
                iDoExec = ForkJoinPool.common.externalHelpComplete((CountedCompleter) this, 0);
            } else {
                iDoExec = ForkJoinPool.common.tryExternalUnpush(this) ? doExec() : 0;
            }
            i3 = iDoExec;
            if (iDoExec >= 0) {
                while (true) {
                    int i4 = this.status;
                    i3 = i4;
                    if (i4 < 0) {
                        break;
                    }
                    if (f12587U.compareAndSwapInt(this, STATUS, i3, i3 | 65536)) {
                        synchronized (this) {
                            if (this.status >= 0) {
                                wait(0L);
                            } else {
                                notifyAll();
                            }
                        }
                    }
                }
            }
        }
        return i3;
    }

    private int doJoin() {
        int iDoExec;
        int i2 = this.status;
        if (i2 < 0) {
            return i2;
        }
        Thread threadCurrentThread = Thread.currentThread();
        if (!(threadCurrentThread instanceof ForkJoinWorkerThread)) {
            return externalAwaitDone();
        }
        ForkJoinWorkerThread forkJoinWorkerThread = (ForkJoinWorkerThread) threadCurrentThread;
        ForkJoinPool.WorkQueue workQueue = forkJoinWorkerThread.workQueue;
        return (!workQueue.tryUnpush(this) || (iDoExec = doExec()) >= 0) ? forkJoinWorkerThread.pool.awaitJoin(workQueue, this, 0L) : iDoExec;
    }

    private int doInvoke() {
        int iDoExec = doExec();
        if (iDoExec < 0) {
            return iDoExec;
        }
        Thread threadCurrentThread = Thread.currentThread();
        if (threadCurrentThread instanceof ForkJoinWorkerThread) {
            ForkJoinWorkerThread forkJoinWorkerThread = (ForkJoinWorkerThread) threadCurrentThread;
            return forkJoinWorkerThread.pool.awaitJoin(forkJoinWorkerThread.workQueue, this, 0L);
        }
        return externalAwaitDone();
    }

    /* loaded from: rt.jar:java/util/concurrent/ForkJoinTask$ExceptionNode.class */
    static final class ExceptionNode extends WeakReference<ForkJoinTask<?>> {
        final Throwable ex;
        ExceptionNode next;
        final long thrower;
        final int hashCode;

        ExceptionNode(ForkJoinTask<?> forkJoinTask, Throwable th, ExceptionNode exceptionNode) {
            super(forkJoinTask, ForkJoinTask.exceptionTableRefQueue);
            this.ex = th;
            this.next = exceptionNode;
            this.thrower = Thread.currentThread().getId();
            this.hashCode = System.identityHashCode(forkJoinTask);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x0035, code lost:
    
        r0[r0] = new java.util.concurrent.ForkJoinTask.ExceptionNode(r9, r10, r0[r0]);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    final int recordExceptionalCompletion(java.lang.Throwable r10) {
        /*
            r9 = this;
            r0 = r9
            int r0 = r0.status
            r1 = r0
            r11 = r1
            if (r0 < 0) goto L7a
            r0 = r9
            int r0 = java.lang.System.identityHashCode(r0)
            r12 = r0
            java.util.concurrent.locks.ReentrantLock r0 = java.util.concurrent.ForkJoinTask.exceptionTableLock
            r13 = r0
            r0 = r13
            r0.lock()
            expungeStaleExceptions()     // Catch: java.lang.Throwable -> L69
            java.util.concurrent.ForkJoinTask$ExceptionNode[] r0 = java.util.concurrent.ForkJoinTask.exceptionTable     // Catch: java.lang.Throwable -> L69
            r14 = r0
            r0 = r12
            r1 = r14
            int r1 = r1.length     // Catch: java.lang.Throwable -> L69
            r2 = 1
            int r1 = r1 - r2
            r0 = r0 & r1
            r15 = r0
            r0 = r14
            r1 = r15
            r0 = r0[r1]     // Catch: java.lang.Throwable -> L69
            r16 = r0
        L30:
            r0 = r16
            if (r0 != 0) goto L4b
            r0 = r14
            r1 = r15
            java.util.concurrent.ForkJoinTask$ExceptionNode r2 = new java.util.concurrent.ForkJoinTask$ExceptionNode     // Catch: java.lang.Throwable -> L69
            r3 = r2
            r4 = r9
            r5 = r10
            r6 = r14
            r7 = r15
            r6 = r6[r7]     // Catch: java.lang.Throwable -> L69
            r3.<init>(r4, r5, r6)     // Catch: java.lang.Throwable -> L69
            r0[r1] = r2     // Catch: java.lang.Throwable -> L69
            goto L61
        L4b:
            r0 = r16
            java.lang.Object r0 = r0.get()     // Catch: java.lang.Throwable -> L69
            r1 = r9
            if (r0 != r1) goto L57
            goto L61
        L57:
            r0 = r16
            java.util.concurrent.ForkJoinTask$ExceptionNode r0 = r0.next     // Catch: java.lang.Throwable -> L69
            r16 = r0
            goto L30
        L61:
            r0 = r13
            r0.unlock()
            goto L73
        L69:
            r17 = move-exception
            r0 = r13
            r0.unlock()
            r0 = r17
            throw r0
        L73:
            r0 = r9
            r1 = -2147483648(0xffffffff80000000, float:-0.0)
            int r0 = r0.setCompletion(r1)
            r11 = r0
        L7a:
            r0 = r11
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ForkJoinTask.recordExceptionalCompletion(java.lang.Throwable):int");
    }

    private int setExceptionalCompletion(Throwable th) {
        int iRecordExceptionalCompletion = recordExceptionalCompletion(th);
        if ((iRecordExceptionalCompletion & (-268435456)) == Integer.MIN_VALUE) {
            internalPropagateException(th);
        }
        return iRecordExceptionalCompletion;
    }

    void internalPropagateException(Throwable th) {
    }

    static final void cancelIgnoringExceptions(ForkJoinTask<?> forkJoinTask) {
        if (forkJoinTask != null && forkJoinTask.status >= 0) {
            try {
                forkJoinTask.cancel(false);
            } catch (Throwable th) {
            }
        }
    }

    private void clearExceptionalCompletion() {
        int iIdentityHashCode = System.identityHashCode(this);
        ReentrantLock reentrantLock = exceptionTableLock;
        reentrantLock.lock();
        try {
            ExceptionNode[] exceptionNodeArr = exceptionTable;
            int length = iIdentityHashCode & (exceptionNodeArr.length - 1);
            ExceptionNode exceptionNode = exceptionNodeArr[length];
            ExceptionNode exceptionNode2 = null;
            while (true) {
                if (exceptionNode == null) {
                    break;
                }
                ExceptionNode exceptionNode3 = exceptionNode.next;
                if (exceptionNode.get() == this) {
                    if (exceptionNode2 == null) {
                        exceptionNodeArr[length] = exceptionNode3;
                    } else {
                        exceptionNode2.next = exceptionNode3;
                    }
                } else {
                    exceptionNode2 = exceptionNode;
                    exceptionNode = exceptionNode3;
                }
            }
            expungeStaleExceptions();
            this.status = 0;
            reentrantLock.unlock();
        } catch (Throwable th) {
            reentrantLock.unlock();
            throw th;
        }
    }

    private Throwable getThrowableException() {
        Throwable th;
        Throwable th2;
        if ((this.status & (-268435456)) != Integer.MIN_VALUE) {
            return null;
        }
        int iIdentityHashCode = System.identityHashCode(this);
        ReentrantLock reentrantLock = exceptionTableLock;
        reentrantLock.lock();
        try {
            expungeStaleExceptions();
            ExceptionNode[] exceptionNodeArr = exceptionTable;
            ExceptionNode exceptionNode = exceptionNodeArr[iIdentityHashCode & (exceptionNodeArr.length - 1)];
            while (exceptionNode != null) {
                if (exceptionNode.get() == this) {
                    break;
                }
                exceptionNode = exceptionNode.next;
            }
            if (exceptionNode == null || (th = exceptionNode.ex) == null) {
                return null;
            }
            if (exceptionNode.thrower != Thread.currentThread().getId()) {
                try {
                    Constructor<?> constructor = null;
                    for (Constructor<?> constructor2 : th.getClass().getConstructors()) {
                        Class<?>[] parameterTypes = constructor2.getParameterTypes();
                        if (parameterTypes.length == 0) {
                            constructor = constructor2;
                        } else if (parameterTypes.length == 1 && parameterTypes[0] == Throwable.class) {
                            Throwable th3 = (Throwable) constructor2.newInstance(th);
                            return th3 == null ? th : th3;
                        }
                    }
                    if (constructor != null && (th2 = (Throwable) constructor.newInstance(new Object[0])) != null) {
                        th2.initCause(th);
                        return th2;
                    }
                } catch (Exception e2) {
                }
            }
            return th;
        } finally {
            reentrantLock.unlock();
        }
    }

    private static void expungeStaleExceptions() {
        while (true) {
            Reference<? extends Object> referencePoll = exceptionTableRefQueue.poll();
            if (referencePoll != null) {
                if (referencePoll instanceof ExceptionNode) {
                    int i2 = ((ExceptionNode) referencePoll).hashCode;
                    ExceptionNode[] exceptionNodeArr = exceptionTable;
                    int length = i2 & (exceptionNodeArr.length - 1);
                    ExceptionNode exceptionNode = exceptionNodeArr[length];
                    ExceptionNode exceptionNode2 = null;
                    while (true) {
                        if (exceptionNode != null) {
                            ExceptionNode exceptionNode3 = exceptionNode.next;
                            if (exceptionNode == referencePoll) {
                                if (exceptionNode2 == null) {
                                    exceptionNodeArr[length] = exceptionNode3;
                                } else {
                                    exceptionNode2.next = exceptionNode3;
                                }
                            } else {
                                exceptionNode2 = exceptionNode;
                                exceptionNode = exceptionNode3;
                            }
                        }
                    }
                }
            } else {
                return;
            }
        }
    }

    static final void helpExpungeStaleExceptions() {
        ReentrantLock reentrantLock = exceptionTableLock;
        if (reentrantLock.tryLock()) {
            try {
                expungeStaleExceptions();
            } finally {
                reentrantLock.unlock();
            }
        }
    }

    static void rethrow(Throwable th) {
        if (th != null) {
            uncheckedThrow(th);
        }
    }

    static <T extends Throwable> void uncheckedThrow(Throwable th) throws Throwable {
        throw th;
    }

    private void reportException(int i2) {
        if (i2 == CANCELLED) {
            throw new CancellationException();
        }
        if (i2 == Integer.MIN_VALUE) {
            rethrow(getThrowableException());
        }
    }

    public final ForkJoinTask<V> fork() {
        Thread threadCurrentThread = Thread.currentThread();
        if (threadCurrentThread instanceof ForkJoinWorkerThread) {
            ((ForkJoinWorkerThread) threadCurrentThread).workQueue.push(this);
        } else {
            ForkJoinPool.common.externalPush(this);
        }
        return this;
    }

    public final V join() {
        int iDoJoin = doJoin() & (-268435456);
        if (iDoJoin != -268435456) {
            reportException(iDoJoin);
        }
        return getRawResult();
    }

    public final V invoke() {
        int iDoInvoke = doInvoke() & (-268435456);
        if (iDoInvoke != -268435456) {
            reportException(iDoInvoke);
        }
        return getRawResult();
    }

    public static void invokeAll(ForkJoinTask<?> forkJoinTask, ForkJoinTask<?> forkJoinTask2) {
        forkJoinTask2.fork();
        int iDoInvoke = forkJoinTask.doInvoke() & (-268435456);
        if (iDoInvoke != -268435456) {
            forkJoinTask.reportException(iDoInvoke);
        }
        int iDoJoin = forkJoinTask2.doJoin() & (-268435456);
        if (iDoJoin != -268435456) {
            forkJoinTask2.reportException(iDoJoin);
        }
    }

    public static void invokeAll(ForkJoinTask<?>... forkJoinTaskArr) {
        Throwable exception = null;
        int length = forkJoinTaskArr.length - 1;
        for (int i2 = length; i2 >= 0; i2--) {
            ForkJoinTask<?> forkJoinTask = forkJoinTaskArr[i2];
            if (forkJoinTask == null) {
                if (exception == null) {
                    exception = new NullPointerException();
                }
            } else if (i2 != 0) {
                forkJoinTask.fork();
            } else if (forkJoinTask.doInvoke() < -268435456 && exception == null) {
                exception = forkJoinTask.getException();
            }
        }
        for (int i3 = 1; i3 <= length; i3++) {
            ForkJoinTask<?> forkJoinTask2 = forkJoinTaskArr[i3];
            if (forkJoinTask2 != null) {
                if (exception != null) {
                    forkJoinTask2.cancel(false);
                } else if (forkJoinTask2.doJoin() < -268435456) {
                    exception = forkJoinTask2.getException();
                }
            }
        }
        if (exception != null) {
            rethrow(exception);
        }
    }

    public static <T extends ForkJoinTask<?>> Collection<T> invokeAll(Collection<T> collection) {
        if (!(collection instanceof RandomAccess) || !(collection instanceof List)) {
            invokeAll((ForkJoinTask<?>[]) collection.toArray(new ForkJoinTask[collection.size()]));
            return collection;
        }
        List list = (List) collection;
        Throwable exception = null;
        int size = list.size() - 1;
        for (int i2 = size; i2 >= 0; i2--) {
            ForkJoinTask forkJoinTask = (ForkJoinTask) list.get(i2);
            if (forkJoinTask == null) {
                if (exception == null) {
                    exception = new NullPointerException();
                }
            } else if (i2 != 0) {
                forkJoinTask.fork();
            } else if (forkJoinTask.doInvoke() < -268435456 && exception == null) {
                exception = forkJoinTask.getException();
            }
        }
        for (int i3 = 1; i3 <= size; i3++) {
            ForkJoinTask forkJoinTask2 = (ForkJoinTask) list.get(i3);
            if (forkJoinTask2 != null) {
                if (exception != null) {
                    forkJoinTask2.cancel(false);
                } else if (forkJoinTask2.doJoin() < -268435456) {
                    exception = forkJoinTask2.getException();
                }
            }
        }
        if (exception != null) {
            rethrow(exception);
        }
        return collection;
    }

    @Override // java.util.concurrent.Future
    public boolean cancel(boolean z2) {
        return (setCompletion(CANCELLED) & (-268435456)) == CANCELLED;
    }

    @Override // java.util.concurrent.Future
    public final boolean isDone() {
        return this.status < 0;
    }

    @Override // java.util.concurrent.Future
    public final boolean isCancelled() {
        return (this.status & (-268435456)) == CANCELLED;
    }

    public final boolean isCompletedAbnormally() {
        return this.status < -268435456;
    }

    public final boolean isCompletedNormally() {
        return (this.status & (-268435456)) == -268435456;
    }

    public final Throwable getException() {
        int i2 = this.status & (-268435456);
        if (i2 >= -268435456) {
            return null;
        }
        return i2 == CANCELLED ? new CancellationException() : getThrowableException();
    }

    public void completeExceptionally(Throwable th) {
        setExceptionalCompletion(((th instanceof RuntimeException) || (th instanceof Error)) ? th : new RuntimeException(th));
    }

    public void complete(V v2) {
        try {
            setRawResult(v2);
            setCompletion(-268435456);
        } catch (Throwable th) {
            setExceptionalCompletion(th);
        }
    }

    public final void quietlyComplete() {
        setCompletion(-268435456);
    }

    @Override // java.util.concurrent.Future
    public final V get() throws ExecutionException, InterruptedException {
        Throwable throwableException;
        int iDoJoin = (Thread.currentThread() instanceof ForkJoinWorkerThread ? doJoin() : externalInterruptibleAwaitDone()) & (-268435456);
        if (iDoJoin == CANCELLED) {
            throw new CancellationException();
        }
        if (iDoJoin == Integer.MIN_VALUE && (throwableException = getThrowableException()) != null) {
            throw new ExecutionException(throwableException);
        }
        return getRawResult();
    }

    @Override // java.util.concurrent.Future
    public final V get(long j2, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
        int iDoExec;
        int i2;
        long nanos = timeUnit.toNanos(j2);
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        int i3 = this.status;
        int iAwaitJoin = i3;
        if (i3 >= 0 && nanos > 0) {
            long jNanoTime = System.nanoTime() + nanos;
            long j3 = jNanoTime == 0 ? 1L : jNanoTime;
            Thread threadCurrentThread = Thread.currentThread();
            if (threadCurrentThread instanceof ForkJoinWorkerThread) {
                ForkJoinWorkerThread forkJoinWorkerThread = (ForkJoinWorkerThread) threadCurrentThread;
                iAwaitJoin = forkJoinWorkerThread.pool.awaitJoin(forkJoinWorkerThread.workQueue, this, j3);
            } else {
                if (this instanceof CountedCompleter) {
                    iDoExec = ForkJoinPool.common.externalHelpComplete((CountedCompleter) this, 0);
                } else {
                    iDoExec = ForkJoinPool.common.tryExternalUnpush(this) ? doExec() : 0;
                }
                iAwaitJoin = iDoExec;
                if (iDoExec >= 0) {
                    while (true) {
                        int i4 = this.status;
                        iAwaitJoin = i4;
                        if (i4 < 0) {
                            break;
                        }
                        int i5 = i2;
                        if (j3 - System.nanoTime() <= 0) {
                            break;
                        }
                        long millis = TimeUnit.NANOSECONDS.toMillis(i5);
                        if (i2 > 0) {
                            i2 = iAwaitJoin;
                            if (f12587U.compareAndSwapInt(this, STATUS, i2, iAwaitJoin | 65536)) {
                                synchronized (this) {
                                    if (this.status >= 0) {
                                        wait(millis);
                                    } else {
                                        notifyAll();
                                    }
                                }
                            } else {
                                continue;
                            }
                        }
                    }
                }
            }
        }
        if (iAwaitJoin >= 0) {
            iAwaitJoin = this.status;
        }
        int i6 = iAwaitJoin & (-268435456);
        if (i6 != -268435456) {
            if (i6 == CANCELLED) {
                throw new CancellationException();
            }
            if (i6 != Integer.MIN_VALUE) {
                throw new TimeoutException();
            }
            Throwable throwableException = getThrowableException();
            if (throwableException != null) {
                throw new ExecutionException(throwableException);
            }
        }
        return getRawResult();
    }

    public final void quietlyJoin() {
        doJoin();
    }

    public final void quietlyInvoke() {
        doInvoke();
    }

    public static void helpQuiesce() {
        Thread threadCurrentThread = Thread.currentThread();
        if (threadCurrentThread instanceof ForkJoinWorkerThread) {
            ForkJoinWorkerThread forkJoinWorkerThread = (ForkJoinWorkerThread) threadCurrentThread;
            forkJoinWorkerThread.pool.helpQuiescePool(forkJoinWorkerThread.workQueue);
        } else {
            ForkJoinPool.quiesceCommonPool();
        }
    }

    public void reinitialize() {
        if ((this.status & (-268435456)) == Integer.MIN_VALUE) {
            clearExceptionalCompletion();
        } else {
            this.status = 0;
        }
    }

    public static ForkJoinPool getPool() {
        Thread threadCurrentThread = Thread.currentThread();
        if (threadCurrentThread instanceof ForkJoinWorkerThread) {
            return ((ForkJoinWorkerThread) threadCurrentThread).pool;
        }
        return null;
    }

    public static boolean inForkJoinPool() {
        return Thread.currentThread() instanceof ForkJoinWorkerThread;
    }

    public boolean tryUnfork() {
        Thread threadCurrentThread = Thread.currentThread();
        if (threadCurrentThread instanceof ForkJoinWorkerThread) {
            return ((ForkJoinWorkerThread) threadCurrentThread).workQueue.tryUnpush(this);
        }
        return ForkJoinPool.common.tryExternalUnpush(this);
    }

    public static int getQueuedTaskCount() {
        ForkJoinPool.WorkQueue workQueueCommonSubmitterQueue;
        Thread threadCurrentThread = Thread.currentThread();
        if (threadCurrentThread instanceof ForkJoinWorkerThread) {
            workQueueCommonSubmitterQueue = ((ForkJoinWorkerThread) threadCurrentThread).workQueue;
        } else {
            workQueueCommonSubmitterQueue = ForkJoinPool.commonSubmitterQueue();
        }
        if (workQueueCommonSubmitterQueue == null) {
            return 0;
        }
        return workQueueCommonSubmitterQueue.queueSize();
    }

    public static int getSurplusQueuedTaskCount() {
        return ForkJoinPool.getSurplusQueuedTaskCount();
    }

    protected static ForkJoinTask<?> peekNextLocalTask() {
        ForkJoinPool.WorkQueue workQueueCommonSubmitterQueue;
        Thread threadCurrentThread = Thread.currentThread();
        if (threadCurrentThread instanceof ForkJoinWorkerThread) {
            workQueueCommonSubmitterQueue = ((ForkJoinWorkerThread) threadCurrentThread).workQueue;
        } else {
            workQueueCommonSubmitterQueue = ForkJoinPool.commonSubmitterQueue();
        }
        if (workQueueCommonSubmitterQueue == null) {
            return null;
        }
        return workQueueCommonSubmitterQueue.peek();
    }

    protected static ForkJoinTask<?> pollNextLocalTask() {
        Thread threadCurrentThread = Thread.currentThread();
        if (threadCurrentThread instanceof ForkJoinWorkerThread) {
            return ((ForkJoinWorkerThread) threadCurrentThread).workQueue.nextLocalTask();
        }
        return null;
    }

    protected static ForkJoinTask<?> pollTask() {
        Thread threadCurrentThread = Thread.currentThread();
        if (!(threadCurrentThread instanceof ForkJoinWorkerThread)) {
            return null;
        }
        ForkJoinWorkerThread forkJoinWorkerThread = (ForkJoinWorkerThread) threadCurrentThread;
        return forkJoinWorkerThread.pool.nextTaskFor(forkJoinWorkerThread.workQueue);
    }

    public final short getForkJoinTaskTag() {
        return (short) this.status;
    }

    public final short setForkJoinTaskTag(short s2) {
        Unsafe unsafe;
        long j2;
        int i2;
        do {
            unsafe = f12587U;
            j2 = STATUS;
            i2 = this.status;
        } while (!unsafe.compareAndSwapInt(this, j2, i2, (i2 & DTMManager.IDENT_DTM_DEFAULT) | (s2 & 65535)));
        return (short) i2;
    }

    public final boolean compareAndSetForkJoinTaskTag(short s2, short s3) {
        int i2;
        do {
            i2 = this.status;
            if (((short) i2) != s2) {
                return false;
            }
        } while (!f12587U.compareAndSwapInt(this, STATUS, i2, (i2 & DTMManager.IDENT_DTM_DEFAULT) | (s3 & 65535)));
        return true;
    }

    /* loaded from: rt.jar:java/util/concurrent/ForkJoinTask$AdaptedRunnable.class */
    static final class AdaptedRunnable<T> extends ForkJoinTask<T> implements RunnableFuture<T> {
        final Runnable runnable;
        T result;
        private static final long serialVersionUID = 5232453952276885070L;

        AdaptedRunnable(Runnable runnable, T t2) {
            if (runnable == null) {
                throw new NullPointerException();
            }
            this.runnable = runnable;
            this.result = t2;
        }

        @Override // java.util.concurrent.ForkJoinTask
        public final T getRawResult() {
            return this.result;
        }

        @Override // java.util.concurrent.ForkJoinTask
        public final void setRawResult(T t2) {
            this.result = t2;
        }

        @Override // java.util.concurrent.ForkJoinTask
        public final boolean exec() {
            this.runnable.run();
            return true;
        }

        @Override // java.util.concurrent.RunnableFuture, java.lang.Runnable
        public final void run() {
            invoke();
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ForkJoinTask$AdaptedRunnableAction.class */
    static final class AdaptedRunnableAction extends ForkJoinTask<Void> implements RunnableFuture<Void> {
        final Runnable runnable;
        private static final long serialVersionUID = 5232453952276885070L;

        AdaptedRunnableAction(Runnable runnable) {
            if (runnable == null) {
                throw new NullPointerException();
            }
            this.runnable = runnable;
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
            this.runnable.run();
            return true;
        }

        @Override // java.util.concurrent.RunnableFuture, java.lang.Runnable
        public final void run() {
            invoke();
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ForkJoinTask$RunnableExecuteAction.class */
    static final class RunnableExecuteAction extends ForkJoinTask<Void> {
        final Runnable runnable;
        private static final long serialVersionUID = 5232453952276885070L;

        RunnableExecuteAction(Runnable runnable) {
            if (runnable == null) {
                throw new NullPointerException();
            }
            this.runnable = runnable;
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
            this.runnable.run();
            return true;
        }

        @Override // java.util.concurrent.ForkJoinTask
        void internalPropagateException(Throwable th) {
            rethrow(th);
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ForkJoinTask$AdaptedCallable.class */
    static final class AdaptedCallable<T> extends ForkJoinTask<T> implements RunnableFuture<T> {
        final Callable<? extends T> callable;
        T result;
        private static final long serialVersionUID = 2838392045355241008L;

        AdaptedCallable(Callable<? extends T> callable) {
            if (callable == null) {
                throw new NullPointerException();
            }
            this.callable = callable;
        }

        @Override // java.util.concurrent.ForkJoinTask
        public final T getRawResult() {
            return this.result;
        }

        @Override // java.util.concurrent.ForkJoinTask
        public final void setRawResult(T t2) {
            this.result = t2;
        }

        @Override // java.util.concurrent.ForkJoinTask
        public final boolean exec() {
            try {
                this.result = this.callable.call();
                return true;
            } catch (Error e2) {
                throw e2;
            } catch (RuntimeException e3) {
                throw e3;
            } catch (Exception e4) {
                throw new RuntimeException(e4);
            }
        }

        @Override // java.util.concurrent.RunnableFuture, java.lang.Runnable
        public final void run() {
            invoke();
        }
    }

    public static ForkJoinTask<?> adapt(Runnable runnable) {
        return new AdaptedRunnableAction(runnable);
    }

    public static <T> ForkJoinTask<T> adapt(Runnable runnable, T t2) {
        return new AdaptedRunnable(runnable, t2);
    }

    public static <T> ForkJoinTask<T> adapt(Callable<? extends T> callable) {
        return new AdaptedCallable(callable);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(getException());
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        Object object = objectInputStream.readObject();
        if (object != null) {
            setExceptionalCompletion((Throwable) object);
        }
    }

    static {
        try {
            f12587U = Unsafe.getUnsafe();
            STATUS = f12587U.objectFieldOffset(ForkJoinTask.class.getDeclaredField("status"));
        } catch (Exception e2) {
            throw new Error(e2);
        }
    }
}
