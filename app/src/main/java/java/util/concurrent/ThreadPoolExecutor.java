package java.util.concurrent;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: rt.jar:java/util/concurrent/ThreadPoolExecutor.class */
public class ThreadPoolExecutor extends AbstractExecutorService {
    private final AtomicInteger ctl;
    private static final int COUNT_BITS = 29;
    private static final int CAPACITY = 536870911;
    private static final int RUNNING = -536870912;
    private static final int SHUTDOWN = 0;
    private static final int STOP = 536870912;
    private static final int TIDYING = 1073741824;
    private static final int TERMINATED = 1610612736;
    private final BlockingQueue<Runnable> workQueue;
    private final ReentrantLock mainLock;
    private final HashSet<Worker> workers;
    private final Condition termination;
    private int largestPoolSize;
    private long completedTaskCount;
    private volatile ThreadFactory threadFactory;
    private volatile RejectedExecutionHandler handler;
    private volatile long keepAliveTime;
    private volatile boolean allowCoreThreadTimeOut;
    private volatile int corePoolSize;
    private volatile int maximumPoolSize;
    private static final RejectedExecutionHandler defaultHandler = new AbortPolicy();
    private static final RuntimePermission shutdownPerm = new RuntimePermission("modifyThread");
    private final AccessControlContext acc;
    private static final boolean ONLY_ONE = true;

    private static int runStateOf(int i2) {
        return i2 & RUNNING;
    }

    private static int workerCountOf(int i2) {
        return i2 & CAPACITY;
    }

    private static int ctlOf(int i2, int i3) {
        return i2 | i3;
    }

    private static boolean runStateLessThan(int i2, int i3) {
        return i2 < i3;
    }

    private static boolean runStateAtLeast(int i2, int i3) {
        return i2 >= i3;
    }

    private static boolean isRunning(int i2) {
        return i2 < 0;
    }

    private boolean compareAndIncrementWorkerCount(int i2) {
        return this.ctl.compareAndSet(i2, i2 + 1);
    }

    private boolean compareAndDecrementWorkerCount(int i2) {
        return this.ctl.compareAndSet(i2, i2 - 1);
    }

    private void decrementWorkerCount() {
        while (!compareAndDecrementWorkerCount(this.ctl.get())) {
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ThreadPoolExecutor$Worker.class */
    private final class Worker extends AbstractQueuedSynchronizer implements Runnable {
        private static final long serialVersionUID = 6138294804551838833L;
        final Thread thread;
        Runnable firstTask;
        volatile long completedTasks;

        Worker(Runnable runnable) {
            setState(-1);
            this.firstTask = runnable;
            this.thread = ThreadPoolExecutor.this.getThreadFactory().newThread(this);
        }

        @Override // java.lang.Runnable
        public void run() {
            ThreadPoolExecutor.this.runWorker(this);
        }

        @Override // java.util.concurrent.locks.AbstractQueuedSynchronizer
        protected boolean isHeldExclusively() {
            return getState() != 0;
        }

        @Override // java.util.concurrent.locks.AbstractQueuedSynchronizer
        protected boolean tryAcquire(int i2) {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override // java.util.concurrent.locks.AbstractQueuedSynchronizer
        protected boolean tryRelease(int i2) {
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        public void lock() {
            acquire(1);
        }

        public boolean tryLock() {
            return tryAcquire(1);
        }

        public void unlock() {
            release(1);
        }

        public boolean isLocked() {
            return isHeldExclusively();
        }

        void interruptIfStarted() {
            Thread thread;
            if (getState() >= 0 && (thread = this.thread) != null && !thread.isInterrupted()) {
                try {
                    thread.interrupt();
                } catch (SecurityException e2) {
                }
            }
        }
    }

    private void advanceRunState(int i2) {
        int i3;
        do {
            i3 = this.ctl.get();
            if (runStateAtLeast(i3, i2)) {
                return;
            }
        } while (!this.ctl.compareAndSet(i3, ctlOf(i2, workerCountOf(i3))));
    }

    final void tryTerminate() {
        while (true) {
            int i2 = this.ctl.get();
            if (!isRunning(i2) && !runStateAtLeast(i2, 1073741824)) {
                if (runStateOf(i2) == 0 && !this.workQueue.isEmpty()) {
                    return;
                }
                if (workerCountOf(i2) != 0) {
                    interruptIdleWorkers(true);
                    return;
                }
                ReentrantLock reentrantLock = this.mainLock;
                reentrantLock.lock();
                try {
                    if (this.ctl.compareAndSet(i2, ctlOf(1073741824, 0))) {
                        try {
                            terminated();
                            this.ctl.set(ctlOf(TERMINATED, 0));
                            this.termination.signalAll();
                            return;
                        } catch (Throwable th) {
                            this.ctl.set(ctlOf(TERMINATED, 0));
                            this.termination.signalAll();
                            throw th;
                        }
                    }
                    reentrantLock.unlock();
                } finally {
                    reentrantLock.unlock();
                }
            } else {
                return;
            }
        }
    }

    private void checkShutdownAccess() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(shutdownPerm);
            ReentrantLock reentrantLock = this.mainLock;
            reentrantLock.lock();
            try {
                Iterator<Worker> it = this.workers.iterator();
                while (it.hasNext()) {
                    securityManager.checkAccess(it.next().thread);
                }
            } finally {
                reentrantLock.unlock();
            }
        }
    }

    private void interruptWorkers() {
        ReentrantLock reentrantLock = this.mainLock;
        reentrantLock.lock();
        try {
            Iterator<Worker> it = this.workers.iterator();
            while (it.hasNext()) {
                it.next().interruptIfStarted();
            }
        } finally {
            reentrantLock.unlock();
        }
    }

    private void interruptIdleWorkers(boolean z2) {
        ReentrantLock reentrantLock = this.mainLock;
        reentrantLock.lock();
        try {
            Iterator<Worker> it = this.workers.iterator();
            while (it.hasNext()) {
                Worker next = it.next();
                Thread thread = next.thread;
                if (!thread.isInterrupted() && next.tryLock()) {
                    try {
                        thread.interrupt();
                        next.unlock();
                    } catch (SecurityException e2) {
                        next.unlock();
                    } catch (Throwable th) {
                        next.unlock();
                        throw th;
                    }
                }
                if (z2) {
                    break;
                }
            }
        } finally {
            reentrantLock.unlock();
        }
    }

    private void interruptIdleWorkers() {
        interruptIdleWorkers(false);
    }

    final void reject(Runnable runnable) {
        this.handler.rejectedExecution(runnable, this);
    }

    void onShutdown() {
    }

    final boolean isRunningOrShutdown(boolean z2) {
        int iRunStateOf = runStateOf(this.ctl.get());
        return iRunStateOf == RUNNING || (iRunStateOf == 0 && z2);
    }

    private List<Runnable> drainQueue() {
        BlockingQueue<Runnable> blockingQueue = this.workQueue;
        ArrayList arrayList = new ArrayList();
        blockingQueue.drainTo(arrayList);
        if (!blockingQueue.isEmpty()) {
            for (Runnable runnable : (Runnable[]) blockingQueue.toArray(new Runnable[0])) {
                if (blockingQueue.remove(runnable)) {
                    arrayList.add(runnable);
                }
            }
        }
        return arrayList;
    }

    /* JADX WARN: Finally extract failed */
    private boolean addWorker(Runnable runnable, boolean z2) {
        while (true) {
            int i2 = this.ctl.get();
            int iRunStateOf = runStateOf(i2);
            if (iRunStateOf >= 0 && (iRunStateOf != 0 || runnable != null || this.workQueue.isEmpty())) {
                return false;
            }
            do {
                int iWorkerCountOf = workerCountOf(i2);
                if (iWorkerCountOf >= CAPACITY) {
                    return false;
                }
                if (iWorkerCountOf >= (z2 ? this.corePoolSize : this.maximumPoolSize)) {
                    return false;
                }
                if (!compareAndIncrementWorkerCount(i2)) {
                    i2 = this.ctl.get();
                } else {
                    boolean z3 = false;
                    boolean z4 = false;
                    Worker worker = null;
                    try {
                        worker = new Worker(runnable);
                        Thread thread = worker.thread;
                        if (thread != null) {
                            ReentrantLock reentrantLock = this.mainLock;
                            reentrantLock.lock();
                            try {
                                int iRunStateOf2 = runStateOf(this.ctl.get());
                                if (iRunStateOf2 < 0 || (iRunStateOf2 == 0 && runnable == null)) {
                                    if (thread.isAlive()) {
                                        throw new IllegalThreadStateException();
                                    }
                                    this.workers.add(worker);
                                    int size = this.workers.size();
                                    if (size > this.largestPoolSize) {
                                        this.largestPoolSize = size;
                                    }
                                    z4 = true;
                                }
                                reentrantLock.unlock();
                                if (z4) {
                                    thread.start();
                                    z3 = true;
                                }
                            } catch (Throwable th) {
                                reentrantLock.unlock();
                                throw th;
                            }
                        }
                        if (!z3) {
                            addWorkerFailed(worker);
                        }
                        return z3;
                    } catch (Throwable th2) {
                        if (0 == 0) {
                            addWorkerFailed(worker);
                        }
                        throw th2;
                    }
                }
            } while (runStateOf(i2) == iRunStateOf);
        }
    }

    private void addWorkerFailed(Worker worker) {
        ReentrantLock reentrantLock = this.mainLock;
        reentrantLock.lock();
        if (worker != null) {
            try {
                this.workers.remove(worker);
            } finally {
                reentrantLock.unlock();
            }
        }
        decrementWorkerCount();
        tryTerminate();
    }

    private void processWorkerExit(Worker worker, boolean z2) {
        if (z2) {
            decrementWorkerCount();
        }
        ReentrantLock reentrantLock = this.mainLock;
        reentrantLock.lock();
        try {
            this.completedTaskCount += worker.completedTasks;
            this.workers.remove(worker);
            reentrantLock.unlock();
            tryTerminate();
            int i2 = this.ctl.get();
            if (runStateLessThan(i2, 536870912)) {
                if (!z2) {
                    int i3 = this.allowCoreThreadTimeOut ? 0 : this.corePoolSize;
                    if (i3 == 0 && !this.workQueue.isEmpty()) {
                        i3 = 1;
                    }
                    if (workerCountOf(i2) >= i3) {
                        return;
                    }
                }
                addWorker(null, false);
            }
        } catch (Throwable th) {
            reentrantLock.unlock();
            throw th;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x002a, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0025, code lost:
    
        decrementWorkerCount();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.Runnable getTask() {
        /*
            r5 = this;
            r0 = 0
            r6 = r0
        L2:
            r0 = r5
            java.util.concurrent.atomic.AtomicInteger r0 = r0.ctl
            int r0 = r0.get()
            r7 = r0
            r0 = r7
            int r0 = runStateOf(r0)
            r8 = r0
            r0 = r8
            if (r0 < 0) goto L2b
            r0 = r8
            r1 = 536870912(0x20000000, float:1.0842022E-19)
            if (r0 >= r1) goto L25
            r0 = r5
            java.util.concurrent.BlockingQueue<java.lang.Runnable> r0 = r0.workQueue
            boolean r0 = r0.isEmpty()
            if (r0 == 0) goto L2b
        L25:
            r0 = r5
            r0.decrementWorkerCount()
            r0 = 0
            return r0
        L2b:
            r0 = r7
            int r0 = workerCountOf(r0)
            r9 = r0
            r0 = r5
            boolean r0 = r0.allowCoreThreadTimeOut
            if (r0 != 0) goto L41
            r0 = r9
            r1 = r5
            int r1 = r1.corePoolSize
            if (r0 <= r1) goto L45
        L41:
            r0 = 1
            goto L46
        L45:
            r0 = 0
        L46:
            r10 = r0
            r0 = r9
            r1 = r5
            int r1 = r1.maximumPoolSize
            if (r0 > r1) goto L5a
            r0 = r10
            if (r0 == 0) goto L76
            r0 = r6
            if (r0 == 0) goto L76
        L5a:
            r0 = r9
            r1 = 1
            if (r0 > r1) goto L6c
            r0 = r5
            java.util.concurrent.BlockingQueue<java.lang.Runnable> r0 = r0.workQueue
            boolean r0 = r0.isEmpty()
            if (r0 == 0) goto L76
        L6c:
            r0 = r5
            r1 = r7
            boolean r0 = r0.compareAndDecrementWorkerCount(r1)
            if (r0 == 0) goto L2
            r0 = 0
            return r0
        L76:
            r0 = r10
            if (r0 == 0) goto L91
            r0 = r5
            java.util.concurrent.BlockingQueue<java.lang.Runnable> r0 = r0.workQueue     // Catch: java.lang.InterruptedException -> Lac
            r1 = r5
            long r1 = r1.keepAliveTime     // Catch: java.lang.InterruptedException -> Lac
            java.util.concurrent.TimeUnit r2 = java.util.concurrent.TimeUnit.NANOSECONDS     // Catch: java.lang.InterruptedException -> Lac
            java.lang.Object r0 = r0.poll2(r1, r2)     // Catch: java.lang.InterruptedException -> Lac
            java.lang.Runnable r0 = (java.lang.Runnable) r0     // Catch: java.lang.InterruptedException -> Lac
            goto L9d
        L91:
            r0 = r5
            java.util.concurrent.BlockingQueue<java.lang.Runnable> r0 = r0.workQueue     // Catch: java.lang.InterruptedException -> Lac
            java.lang.Object r0 = r0.take2()     // Catch: java.lang.InterruptedException -> Lac
            java.lang.Runnable r0 = (java.lang.Runnable) r0     // Catch: java.lang.InterruptedException -> Lac
        L9d:
            r11 = r0
            r0 = r11
            if (r0 == 0) goto La7
            r0 = r11
            return r0
        La7:
            r0 = 1
            r6 = r0
            goto Lb0
        Lac:
            r11 = move-exception
            r0 = 0
            r6 = r0
        Lb0:
            goto L2
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ThreadPoolExecutor.getTask():java.lang.Runnable");
    }

    /* JADX WARN: Finally extract failed */
    final void runWorker(Worker worker) {
        Thread threadCurrentThread = Thread.currentThread();
        Runnable runnable = worker.firstTask;
        worker.firstTask = null;
        worker.unlock();
        while (true) {
            if (runnable == null) {
                try {
                    Runnable task = getTask();
                    runnable = task;
                    if (task == null) {
                        processWorkerExit(worker, false);
                        return;
                    }
                } catch (Throwable th) {
                    processWorkerExit(worker, true);
                    throw th;
                }
            }
            worker.lock();
            if ((runStateAtLeast(this.ctl.get(), 536870912) || (Thread.interrupted() && runStateAtLeast(this.ctl.get(), 536870912))) && !threadCurrentThread.isInterrupted()) {
                threadCurrentThread.interrupt();
            }
            try {
                beforeExecute(threadCurrentThread, runnable);
                try {
                    try {
                        runnable.run();
                        afterExecute(runnable, null);
                        runnable = null;
                        worker.completedTasks++;
                        worker.unlock();
                    } catch (Error e2) {
                        throw e2;
                    } catch (RuntimeException e3) {
                        throw e3;
                    } catch (Throwable th2) {
                        throw new Error(th2);
                    }
                } catch (Throwable th3) {
                    afterExecute(runnable, null);
                    throw th3;
                }
            } catch (Throwable th4) {
                worker.completedTasks++;
                worker.unlock();
                throw th4;
            }
        }
    }

    public ThreadPoolExecutor(int i2, int i3, long j2, TimeUnit timeUnit, BlockingQueue<Runnable> blockingQueue) {
        this(i2, i3, j2, timeUnit, blockingQueue, Executors.defaultThreadFactory(), defaultHandler);
    }

    public ThreadPoolExecutor(int i2, int i3, long j2, TimeUnit timeUnit, BlockingQueue<Runnable> blockingQueue, ThreadFactory threadFactory) {
        this(i2, i3, j2, timeUnit, blockingQueue, threadFactory, defaultHandler);
    }

    public ThreadPoolExecutor(int i2, int i3, long j2, TimeUnit timeUnit, BlockingQueue<Runnable> blockingQueue, RejectedExecutionHandler rejectedExecutionHandler) {
        this(i2, i3, j2, timeUnit, blockingQueue, Executors.defaultThreadFactory(), rejectedExecutionHandler);
    }

    public ThreadPoolExecutor(int i2, int i3, long j2, TimeUnit timeUnit, BlockingQueue<Runnable> blockingQueue, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
        this.ctl = new AtomicInteger(ctlOf(RUNNING, 0));
        this.mainLock = new ReentrantLock();
        this.workers = new HashSet<>();
        this.termination = this.mainLock.newCondition();
        if (i2 < 0 || i3 <= 0 || i3 < i2 || j2 < 0) {
            throw new IllegalArgumentException();
        }
        if (blockingQueue == null || threadFactory == null || rejectedExecutionHandler == null) {
            throw new NullPointerException();
        }
        this.acc = System.getSecurityManager() == null ? null : AccessController.getContext();
        this.corePoolSize = i2;
        this.maximumPoolSize = i3;
        this.workQueue = blockingQueue;
        this.keepAliveTime = timeUnit.toNanos(j2);
        this.threadFactory = threadFactory;
        this.handler = rejectedExecutionHandler;
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable runnable) {
        if (runnable == null) {
            throw new NullPointerException();
        }
        int i2 = this.ctl.get();
        if (workerCountOf(i2) < this.corePoolSize) {
            if (addWorker(runnable, true)) {
                return;
            } else {
                i2 = this.ctl.get();
            }
        }
        if (isRunning(i2) && this.workQueue.offer(runnable)) {
            int i3 = this.ctl.get();
            if (!isRunning(i3) && remove(runnable)) {
                reject(runnable);
                return;
            } else {
                if (workerCountOf(i3) == 0) {
                    addWorker(null, false);
                    return;
                }
                return;
            }
        }
        if (!addWorker(runnable, false)) {
            reject(runnable);
        }
    }

    @Override // java.util.concurrent.ExecutorService
    public void shutdown() {
        ReentrantLock reentrantLock = this.mainLock;
        reentrantLock.lock();
        try {
            checkShutdownAccess();
            advanceRunState(0);
            interruptIdleWorkers();
            onShutdown();
            tryTerminate();
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.concurrent.ExecutorService
    public List<Runnable> shutdownNow() {
        ReentrantLock reentrantLock = this.mainLock;
        reentrantLock.lock();
        try {
            checkShutdownAccess();
            advanceRunState(536870912);
            interruptWorkers();
            List<Runnable> listDrainQueue = drainQueue();
            tryTerminate();
            return listDrainQueue;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isShutdown() {
        return !isRunning(this.ctl.get());
    }

    public boolean isTerminating() {
        int i2 = this.ctl.get();
        return !isRunning(i2) && runStateLessThan(i2, TERMINATED);
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isTerminated() {
        return runStateAtLeast(this.ctl.get(), TERMINATED);
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean awaitTermination(long j2, TimeUnit timeUnit) throws InterruptedException {
        long nanos = timeUnit.toNanos(j2);
        ReentrantLock reentrantLock = this.mainLock;
        reentrantLock.lock();
        while (!runStateAtLeast(this.ctl.get(), TERMINATED)) {
            try {
                if (nanos > 0) {
                    nanos = this.termination.awaitNanos(nanos);
                } else {
                    reentrantLock.unlock();
                    return false;
                }
            } finally {
                reentrantLock.unlock();
            }
        }
        return true;
    }

    protected void finalize() {
        if (System.getSecurityManager() == null || this.acc == null) {
            shutdown();
        } else {
            AccessController.doPrivileged(() -> {
                shutdown();
                return null;
            }, this.acc);
        }
    }

    public void setThreadFactory(ThreadFactory threadFactory) {
        if (threadFactory == null) {
            throw new NullPointerException();
        }
        this.threadFactory = threadFactory;
    }

    public ThreadFactory getThreadFactory() {
        return this.threadFactory;
    }

    public void setRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler) {
        if (rejectedExecutionHandler == null) {
            throw new NullPointerException();
        }
        this.handler = rejectedExecutionHandler;
    }

    public RejectedExecutionHandler getRejectedExecutionHandler() {
        return this.handler;
    }

    public void setCorePoolSize(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException();
        }
        int i3 = i2 - this.corePoolSize;
        this.corePoolSize = i2;
        if (workerCountOf(this.ctl.get()) > i2) {
            interruptIdleWorkers();
            return;
        }
        if (i3 > 0) {
            int iMin = Math.min(i3, this.workQueue.size());
            do {
                int i4 = iMin;
                iMin--;
                if (i4 <= 0 || !addWorker(null, true)) {
                    return;
                }
            } while (!this.workQueue.isEmpty());
        }
    }

    public int getCorePoolSize() {
        return this.corePoolSize;
    }

    public boolean prestartCoreThread() {
        return workerCountOf(this.ctl.get()) < this.corePoolSize && addWorker(null, true);
    }

    void ensurePrestart() {
        int iWorkerCountOf = workerCountOf(this.ctl.get());
        if (iWorkerCountOf < this.corePoolSize) {
            addWorker(null, true);
        } else if (iWorkerCountOf == 0) {
            addWorker(null, false);
        }
    }

    public int prestartAllCoreThreads() {
        int i2 = 0;
        while (addWorker(null, true)) {
            i2++;
        }
        return i2;
    }

    public boolean allowsCoreThreadTimeOut() {
        return this.allowCoreThreadTimeOut;
    }

    public void allowCoreThreadTimeOut(boolean z2) {
        if (z2 && this.keepAliveTime <= 0) {
            throw new IllegalArgumentException("Core threads must have nonzero keep alive times");
        }
        if (z2 != this.allowCoreThreadTimeOut) {
            this.allowCoreThreadTimeOut = z2;
            if (z2) {
                interruptIdleWorkers();
            }
        }
    }

    public void setMaximumPoolSize(int i2) {
        if (i2 <= 0 || i2 < this.corePoolSize) {
            throw new IllegalArgumentException();
        }
        this.maximumPoolSize = i2;
        if (workerCountOf(this.ctl.get()) > i2) {
            interruptIdleWorkers();
        }
    }

    public int getMaximumPoolSize() {
        return this.maximumPoolSize;
    }

    public void setKeepAliveTime(long j2, TimeUnit timeUnit) {
        if (j2 < 0) {
            throw new IllegalArgumentException();
        }
        if (j2 == 0 && allowsCoreThreadTimeOut()) {
            throw new IllegalArgumentException("Core threads must have nonzero keep alive times");
        }
        long nanos = timeUnit.toNanos(j2);
        long j3 = nanos - this.keepAliveTime;
        this.keepAliveTime = nanos;
        if (j3 < 0) {
            interruptIdleWorkers();
        }
    }

    public long getKeepAliveTime(TimeUnit timeUnit) {
        return timeUnit.convert(this.keepAliveTime, TimeUnit.NANOSECONDS);
    }

    public BlockingQueue<Runnable> getQueue() {
        return this.workQueue;
    }

    public boolean remove(Runnable runnable) {
        boolean zRemove = this.workQueue.remove(runnable);
        tryTerminate();
        return zRemove;
    }

    public void purge() {
        BlockingQueue<Runnable> blockingQueue = this.workQueue;
        try {
            Iterator<Runnable> it = blockingQueue.iterator();
            while (it.hasNext()) {
                Runnable next = it.next();
                if ((next instanceof Future) && ((Future) next).isCancelled()) {
                    it.remove();
                }
            }
        } catch (ConcurrentModificationException e2) {
            for (Object obj : blockingQueue.toArray()) {
                if ((obj instanceof Future) && ((Future) obj).isCancelled()) {
                    blockingQueue.remove(obj);
                }
            }
        }
        tryTerminate();
    }

    public int getPoolSize() {
        ReentrantLock reentrantLock = this.mainLock;
        reentrantLock.lock();
        try {
            return runStateAtLeast(this.ctl.get(), 1073741824) ? 0 : this.workers.size();
        } finally {
            reentrantLock.unlock();
        }
    }

    public int getActiveCount() {
        ReentrantLock reentrantLock = this.mainLock;
        reentrantLock.lock();
        try {
            int i2 = 0;
            Iterator<Worker> it = this.workers.iterator();
            while (it.hasNext()) {
                if (it.next().isLocked()) {
                    i2++;
                }
            }
            return i2;
        } finally {
            reentrantLock.unlock();
        }
    }

    public int getLargestPoolSize() {
        ReentrantLock reentrantLock = this.mainLock;
        reentrantLock.lock();
        try {
            return this.largestPoolSize;
        } finally {
            reentrantLock.unlock();
        }
    }

    public long getTaskCount() {
        ReentrantLock reentrantLock = this.mainLock;
        reentrantLock.lock();
        try {
            long j2 = this.completedTaskCount;
            Iterator<Worker> it = this.workers.iterator();
            while (it.hasNext()) {
                Worker next = it.next();
                j2 += next.completedTasks;
                if (next.isLocked()) {
                    j2++;
                }
            }
            long size = j2 + this.workQueue.size();
            reentrantLock.unlock();
            return size;
        } catch (Throwable th) {
            reentrantLock.unlock();
            throw th;
        }
    }

    public long getCompletedTaskCount() {
        ReentrantLock reentrantLock = this.mainLock;
        reentrantLock.lock();
        try {
            long j2 = this.completedTaskCount;
            Iterator<Worker> it = this.workers.iterator();
            while (it.hasNext()) {
                j2 += it.next().completedTasks;
            }
            return j2;
        } finally {
            reentrantLock.unlock();
        }
    }

    public String toString() {
        String str;
        ReentrantLock reentrantLock = this.mainLock;
        reentrantLock.lock();
        try {
            long j2 = this.completedTaskCount;
            int i2 = 0;
            int size = this.workers.size();
            Iterator<Worker> it = this.workers.iterator();
            while (it.hasNext()) {
                Worker next = it.next();
                j2 += next.completedTasks;
                if (next.isLocked()) {
                    i2++;
                }
            }
            int i3 = this.ctl.get();
            if (runStateLessThan(i3, 0)) {
                str = "Running";
            } else {
                str = runStateAtLeast(i3, TERMINATED) ? "Terminated" : "Shutting down";
            }
            return super.toString() + "[" + str + ", pool size = " + size + ", active threads = " + i2 + ", queued tasks = " + this.workQueue.size() + ", completed tasks = " + j2 + "]";
        } finally {
            reentrantLock.unlock();
        }
    }

    protected void beforeExecute(Thread thread, Runnable runnable) {
    }

    protected void afterExecute(Runnable runnable, Throwable th) {
    }

    protected void terminated() {
    }

    /* loaded from: rt.jar:java/util/concurrent/ThreadPoolExecutor$CallerRunsPolicy.class */
    public static class CallerRunsPolicy implements RejectedExecutionHandler {
        @Override // java.util.concurrent.RejectedExecutionHandler
        public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
            if (!threadPoolExecutor.isShutdown()) {
                runnable.run();
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ThreadPoolExecutor$AbortPolicy.class */
    public static class AbortPolicy implements RejectedExecutionHandler {
        @Override // java.util.concurrent.RejectedExecutionHandler
        public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
            throw new RejectedExecutionException("Task " + runnable.toString() + " rejected from " + threadPoolExecutor.toString());
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ThreadPoolExecutor$DiscardPolicy.class */
    public static class DiscardPolicy implements RejectedExecutionHandler {
        @Override // java.util.concurrent.RejectedExecutionHandler
        public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ThreadPoolExecutor$DiscardOldestPolicy.class */
    public static class DiscardOldestPolicy implements RejectedExecutionHandler {
        @Override // java.util.concurrent.RejectedExecutionHandler
        public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
            if (!threadPoolExecutor.isShutdown()) {
                threadPoolExecutor.getQueue().poll();
                threadPoolExecutor.execute(runnable);
            }
        }
    }
}
