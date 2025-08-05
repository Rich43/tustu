package java.util.concurrent;

import java.util.AbstractQueue;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: rt.jar:java/util/concurrent/ScheduledThreadPoolExecutor.class */
public class ScheduledThreadPoolExecutor extends ThreadPoolExecutor implements ScheduledExecutorService {
    private volatile boolean continueExistingPeriodicTasksAfterShutdown;
    private volatile boolean executeExistingDelayedTasksAfterShutdown;
    private volatile boolean removeOnCancel;
    private static final AtomicLong sequencer = new AtomicLong();

    final long now() {
        return System.nanoTime();
    }

    /* loaded from: rt.jar:java/util/concurrent/ScheduledThreadPoolExecutor$ScheduledFutureTask.class */
    private class ScheduledFutureTask<V> extends FutureTask<V> implements RunnableScheduledFuture<V> {
        private final long sequenceNumber;
        private long time;
        private final long period;
        RunnableScheduledFuture<V> outerTask;
        int heapIndex;

        ScheduledFutureTask(Runnable runnable, V v2, long j2) {
            super(runnable, v2);
            this.outerTask = this;
            this.time = j2;
            this.period = 0L;
            this.sequenceNumber = ScheduledThreadPoolExecutor.sequencer.getAndIncrement();
        }

        ScheduledFutureTask(Runnable runnable, V v2, long j2, long j3) {
            super(runnable, v2);
            this.outerTask = this;
            this.time = j2;
            this.period = j3;
            this.sequenceNumber = ScheduledThreadPoolExecutor.sequencer.getAndIncrement();
        }

        ScheduledFutureTask(Callable<V> callable, long j2) {
            super(callable);
            this.outerTask = this;
            this.time = j2;
            this.period = 0L;
            this.sequenceNumber = ScheduledThreadPoolExecutor.sequencer.getAndIncrement();
        }

        @Override // java.util.concurrent.Delayed
        public long getDelay(TimeUnit timeUnit) {
            return timeUnit.convert(this.time - ScheduledThreadPoolExecutor.this.now(), TimeUnit.NANOSECONDS);
        }

        @Override // java.lang.Comparable
        public int compareTo(Delayed delayed) {
            if (delayed == this) {
                return 0;
            }
            if (delayed instanceof ScheduledFutureTask) {
                ScheduledFutureTask scheduledFutureTask = (ScheduledFutureTask) delayed;
                long j2 = this.time - scheduledFutureTask.time;
                if (j2 < 0) {
                    return -1;
                }
                if (j2 <= 0 && this.sequenceNumber < scheduledFutureTask.sequenceNumber) {
                    return -1;
                }
                return 1;
            }
            long delay = getDelay(TimeUnit.NANOSECONDS) - delayed.getDelay(TimeUnit.NANOSECONDS);
            if (delay < 0) {
                return -1;
            }
            return delay > 0 ? 1 : 0;
        }

        @Override // java.util.concurrent.RunnableScheduledFuture
        public boolean isPeriodic() {
            return this.period != 0;
        }

        private void setNextRunTime() {
            long j2 = this.period;
            if (j2 > 0) {
                this.time += j2;
            } else {
                this.time = ScheduledThreadPoolExecutor.this.triggerTime(-j2);
            }
        }

        @Override // java.util.concurrent.FutureTask, java.util.concurrent.Future
        public boolean cancel(boolean z2) {
            boolean zCancel = super.cancel(z2);
            if (zCancel && ScheduledThreadPoolExecutor.this.removeOnCancel && this.heapIndex >= 0) {
                ScheduledThreadPoolExecutor.this.remove(this);
            }
            return zCancel;
        }

        @Override // java.util.concurrent.FutureTask, java.util.concurrent.RunnableFuture, java.lang.Runnable
        public void run() {
            boolean zIsPeriodic = isPeriodic();
            if (!ScheduledThreadPoolExecutor.this.canRunInCurrentRunState(zIsPeriodic)) {
                cancel(false);
                return;
            }
            if (!zIsPeriodic) {
                super.run();
            } else if (super.runAndReset()) {
                setNextRunTime();
                ScheduledThreadPoolExecutor.this.reExecutePeriodic(this.outerTask);
            }
        }
    }

    boolean canRunInCurrentRunState(boolean z2) {
        return isRunningOrShutdown(z2 ? this.continueExistingPeriodicTasksAfterShutdown : this.executeExistingDelayedTasksAfterShutdown);
    }

    private void delayedExecute(RunnableScheduledFuture<?> runnableScheduledFuture) {
        if (isShutdown()) {
            reject(runnableScheduledFuture);
            return;
        }
        super.getQueue().add(runnableScheduledFuture);
        if (isShutdown() && !canRunInCurrentRunState(runnableScheduledFuture.isPeriodic()) && remove(runnableScheduledFuture)) {
            runnableScheduledFuture.cancel(false);
        } else {
            ensurePrestart();
        }
    }

    void reExecutePeriodic(RunnableScheduledFuture<?> runnableScheduledFuture) {
        if (canRunInCurrentRunState(true)) {
            super.getQueue().add(runnableScheduledFuture);
            if (!canRunInCurrentRunState(true) && remove(runnableScheduledFuture)) {
                runnableScheduledFuture.cancel(false);
            } else {
                ensurePrestart();
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x00a4  */
    @Override // java.util.concurrent.ThreadPoolExecutor
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void onShutdown() {
        /*
            r3 = this;
            r0 = r3
            java.util.concurrent.BlockingQueue r0 = super.getQueue()
            r4 = r0
            r0 = r3
            boolean r0 = r0.getExecuteExistingDelayedTasksAfterShutdownPolicy()
            r5 = r0
            r0 = r3
            boolean r0 = r0.getContinueExistingPeriodicTasksAfterShutdownPolicy()
            r6 = r0
            r0 = r5
            if (r0 != 0) goto L58
            r0 = r6
            if (r0 != 0) goto L58
            r0 = r4
            java.lang.Object[] r0 = r0.toArray()
            r7 = r0
            r0 = r7
            int r0 = r0.length
            r8 = r0
            r0 = 0
            r9 = r0
        L27:
            r0 = r9
            r1 = r8
            if (r0 >= r1) goto L4f
            r0 = r7
            r1 = r9
            r0 = r0[r1]
            r10 = r0
            r0 = r10
            boolean r0 = r0 instanceof java.util.concurrent.RunnableScheduledFuture
            if (r0 == 0) goto L49
            r0 = r10
            java.util.concurrent.RunnableScheduledFuture r0 = (java.util.concurrent.RunnableScheduledFuture) r0
            r1 = 0
            boolean r0 = r0.cancel(r1)
        L49:
            int r9 = r9 + 1
            goto L27
        L4f:
            r0 = r4
            r0.clear()
            goto Lbe
        L58:
            r0 = r4
            java.lang.Object[] r0 = r0.toArray()
            r7 = r0
            r0 = r7
            int r0 = r0.length
            r8 = r0
            r0 = 0
            r9 = r0
        L68:
            r0 = r9
            r1 = r8
            if (r0 >= r1) goto Lbe
            r0 = r7
            r1 = r9
            r0 = r0[r1]
            r10 = r0
            r0 = r10
            boolean r0 = r0 instanceof java.util.concurrent.RunnableScheduledFuture
            if (r0 == 0) goto Lb8
            r0 = r10
            java.util.concurrent.RunnableScheduledFuture r0 = (java.util.concurrent.RunnableScheduledFuture) r0
            r11 = r0
            r0 = r11
            boolean r0 = r0.isPeriodic()
            if (r0 == 0) goto L96
            r0 = r6
            if (r0 != 0) goto L9a
            goto La4
        L96:
            r0 = r5
            if (r0 == 0) goto La4
        L9a:
            r0 = r11
            boolean r0 = r0.isCancelled()
            if (r0 == 0) goto Lb8
        La4:
            r0 = r4
            r1 = r11
            boolean r0 = r0.remove(r1)
            if (r0 == 0) goto Lb8
            r0 = r11
            r1 = 0
            boolean r0 = r0.cancel(r1)
        Lb8:
            int r9 = r9 + 1
            goto L68
        Lbe:
            r0 = r3
            r0.tryTerminate()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ScheduledThreadPoolExecutor.onShutdown():void");
    }

    protected <V> RunnableScheduledFuture<V> decorateTask(Runnable runnable, RunnableScheduledFuture<V> runnableScheduledFuture) {
        return runnableScheduledFuture;
    }

    protected <V> RunnableScheduledFuture<V> decorateTask(Callable<V> callable, RunnableScheduledFuture<V> runnableScheduledFuture) {
        return runnableScheduledFuture;
    }

    public ScheduledThreadPoolExecutor(int i2) {
        super(i2, Integer.MAX_VALUE, 0L, TimeUnit.NANOSECONDS, new DelayedWorkQueue());
        this.executeExistingDelayedTasksAfterShutdown = true;
        this.removeOnCancel = false;
    }

    public ScheduledThreadPoolExecutor(int i2, ThreadFactory threadFactory) {
        super(i2, Integer.MAX_VALUE, 0L, TimeUnit.NANOSECONDS, new DelayedWorkQueue(), threadFactory);
        this.executeExistingDelayedTasksAfterShutdown = true;
        this.removeOnCancel = false;
    }

    public ScheduledThreadPoolExecutor(int i2, RejectedExecutionHandler rejectedExecutionHandler) {
        super(i2, Integer.MAX_VALUE, 0L, TimeUnit.NANOSECONDS, new DelayedWorkQueue(), rejectedExecutionHandler);
        this.executeExistingDelayedTasksAfterShutdown = true;
        this.removeOnCancel = false;
    }

    public ScheduledThreadPoolExecutor(int i2, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
        super(i2, Integer.MAX_VALUE, 0L, TimeUnit.NANOSECONDS, new DelayedWorkQueue(), threadFactory, rejectedExecutionHandler);
        this.executeExistingDelayedTasksAfterShutdown = true;
        this.removeOnCancel = false;
    }

    private long triggerTime(long j2, TimeUnit timeUnit) {
        return triggerTime(timeUnit.toNanos(j2 < 0 ? 0L : j2));
    }

    long triggerTime(long j2) {
        return now() + (j2 < 4611686018427387903L ? j2 : overflowFree(j2));
    }

    private long overflowFree(long j2) {
        Delayed delayed = (Delayed) super.getQueue().peek();
        if (delayed != null) {
            long delay = delayed.getDelay(TimeUnit.NANOSECONDS);
            if (delay < 0 && j2 - delay < 0) {
                j2 = Long.MAX_VALUE + delay;
            }
        }
        return j2;
    }

    @Override // java.util.concurrent.ScheduledExecutorService
    public ScheduledFuture<?> schedule(Runnable runnable, long j2, TimeUnit timeUnit) {
        if (runnable == null || timeUnit == null) {
            throw new NullPointerException();
        }
        RunnableScheduledFuture<?> runnableScheduledFutureDecorateTask = decorateTask(runnable, new ScheduledFutureTask(runnable, null, triggerTime(j2, timeUnit)));
        delayedExecute(runnableScheduledFutureDecorateTask);
        return runnableScheduledFutureDecorateTask;
    }

    @Override // java.util.concurrent.ScheduledExecutorService
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long j2, TimeUnit timeUnit) {
        if (callable == null || timeUnit == null) {
            throw new NullPointerException();
        }
        RunnableScheduledFuture<V> runnableScheduledFutureDecorateTask = decorateTask(callable, new ScheduledFutureTask(callable, triggerTime(j2, timeUnit)));
        delayedExecute(runnableScheduledFutureDecorateTask);
        return runnableScheduledFutureDecorateTask;
    }

    @Override // java.util.concurrent.ScheduledExecutorService
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long j2, long j3, TimeUnit timeUnit) {
        if (runnable == null || timeUnit == null) {
            throw new NullPointerException();
        }
        if (j3 <= 0) {
            throw new IllegalArgumentException();
        }
        ScheduledFutureTask scheduledFutureTask = new ScheduledFutureTask(runnable, null, triggerTime(j2, timeUnit), timeUnit.toNanos(j3));
        RunnableScheduledFuture<V> runnableScheduledFutureDecorateTask = decorateTask(runnable, scheduledFutureTask);
        scheduledFutureTask.outerTask = runnableScheduledFutureDecorateTask;
        delayedExecute(runnableScheduledFutureDecorateTask);
        return runnableScheduledFutureDecorateTask;
    }

    @Override // java.util.concurrent.ScheduledExecutorService
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable runnable, long j2, long j3, TimeUnit timeUnit) {
        if (runnable == null || timeUnit == null) {
            throw new NullPointerException();
        }
        if (j3 <= 0) {
            throw new IllegalArgumentException();
        }
        ScheduledFutureTask scheduledFutureTask = new ScheduledFutureTask(runnable, null, triggerTime(j2, timeUnit), timeUnit.toNanos(-j3));
        RunnableScheduledFuture<V> runnableScheduledFutureDecorateTask = decorateTask(runnable, scheduledFutureTask);
        scheduledFutureTask.outerTask = runnableScheduledFutureDecorateTask;
        delayedExecute(runnableScheduledFutureDecorateTask);
        return runnableScheduledFutureDecorateTask;
    }

    @Override // java.util.concurrent.ThreadPoolExecutor, java.util.concurrent.Executor
    public void execute(Runnable runnable) {
        schedule(runnable, 0L, TimeUnit.NANOSECONDS);
    }

    @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
    public Future<?> submit(Runnable runnable) {
        return schedule(runnable, 0L, TimeUnit.NANOSECONDS);
    }

    @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
    public <T> Future<T> submit(Runnable runnable, T t2) {
        return schedule(Executors.callable(runnable, t2), 0L, TimeUnit.NANOSECONDS);
    }

    @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
    public <T> Future<T> submit(Callable<T> callable) {
        return schedule(callable, 0L, TimeUnit.NANOSECONDS);
    }

    public void setContinueExistingPeriodicTasksAfterShutdownPolicy(boolean z2) {
        this.continueExistingPeriodicTasksAfterShutdown = z2;
        if (!z2 && isShutdown()) {
            onShutdown();
        }
    }

    public boolean getContinueExistingPeriodicTasksAfterShutdownPolicy() {
        return this.continueExistingPeriodicTasksAfterShutdown;
    }

    public void setExecuteExistingDelayedTasksAfterShutdownPolicy(boolean z2) {
        this.executeExistingDelayedTasksAfterShutdown = z2;
        if (!z2 && isShutdown()) {
            onShutdown();
        }
    }

    public boolean getExecuteExistingDelayedTasksAfterShutdownPolicy() {
        return this.executeExistingDelayedTasksAfterShutdown;
    }

    public void setRemoveOnCancelPolicy(boolean z2) {
        this.removeOnCancel = z2;
    }

    public boolean getRemoveOnCancelPolicy() {
        return this.removeOnCancel;
    }

    @Override // java.util.concurrent.ThreadPoolExecutor, java.util.concurrent.ExecutorService
    public void shutdown() {
        super.shutdown();
    }

    @Override // java.util.concurrent.ThreadPoolExecutor, java.util.concurrent.ExecutorService
    public List<Runnable> shutdownNow() {
        return super.shutdownNow();
    }

    @Override // java.util.concurrent.ThreadPoolExecutor
    public BlockingQueue<Runnable> getQueue() {
        return super.getQueue();
    }

    /* loaded from: rt.jar:java/util/concurrent/ScheduledThreadPoolExecutor$DelayedWorkQueue.class */
    static class DelayedWorkQueue extends AbstractQueue<Runnable> implements BlockingQueue<Runnable> {
        private static final int INITIAL_CAPACITY = 16;
        private RunnableScheduledFuture<?>[] queue = new RunnableScheduledFuture[16];
        private final ReentrantLock lock = new ReentrantLock();
        private int size = 0;
        private Thread leader = null;
        private final Condition available = this.lock.newCondition();

        DelayedWorkQueue() {
        }

        private void setIndex(RunnableScheduledFuture<?> runnableScheduledFuture, int i2) {
            if (runnableScheduledFuture instanceof ScheduledFutureTask) {
                ((ScheduledFutureTask) runnableScheduledFuture).heapIndex = i2;
            }
        }

        private void siftUp(int i2, RunnableScheduledFuture<?> runnableScheduledFuture) {
            while (i2 > 0) {
                int i3 = (i2 - 1) >>> 1;
                RunnableScheduledFuture<?> runnableScheduledFuture2 = this.queue[i3];
                if (runnableScheduledFuture.compareTo(runnableScheduledFuture2) >= 0) {
                    break;
                }
                this.queue[i2] = runnableScheduledFuture2;
                setIndex(runnableScheduledFuture2, i2);
                i2 = i3;
            }
            this.queue[i2] = runnableScheduledFuture;
            setIndex(runnableScheduledFuture, i2);
        }

        private void siftDown(int i2, RunnableScheduledFuture<?> runnableScheduledFuture) {
            int i3 = this.size >>> 1;
            while (i2 < i3) {
                int i4 = (i2 << 1) + 1;
                RunnableScheduledFuture<?> runnableScheduledFuture2 = this.queue[i4];
                int i5 = i4 + 1;
                if (i5 < this.size && runnableScheduledFuture2.compareTo(this.queue[i5]) > 0) {
                    i4 = i5;
                    runnableScheduledFuture2 = this.queue[i5];
                }
                if (runnableScheduledFuture.compareTo(runnableScheduledFuture2) <= 0) {
                    break;
                }
                this.queue[i2] = runnableScheduledFuture2;
                setIndex(runnableScheduledFuture2, i2);
                i2 = i4;
            }
            this.queue[i2] = runnableScheduledFuture;
            setIndex(runnableScheduledFuture, i2);
        }

        private void grow() {
            int length = this.queue.length;
            int i2 = length + (length >> 1);
            if (i2 < 0) {
                i2 = Integer.MAX_VALUE;
            }
            this.queue = (RunnableScheduledFuture[]) Arrays.copyOf(this.queue, i2);
        }

        private int indexOf(Object obj) {
            if (obj != null) {
                if (obj instanceof ScheduledFutureTask) {
                    int i2 = ((ScheduledFutureTask) obj).heapIndex;
                    if (i2 >= 0 && i2 < this.size && this.queue[i2] == obj) {
                        return i2;
                    }
                    return -1;
                }
                for (int i3 = 0; i3 < this.size; i3++) {
                    if (obj.equals(this.queue[i3])) {
                        return i3;
                    }
                }
                return -1;
            }
            return -1;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                return indexOf(obj) != -1;
            } finally {
                reentrantLock.unlock();
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                int iIndexOf = indexOf(obj);
                if (iIndexOf < 0) {
                    return false;
                }
                setIndex(this.queue[iIndexOf], -1);
                int i2 = this.size - 1;
                this.size = i2;
                RunnableScheduledFuture<?> runnableScheduledFuture = this.queue[i2];
                this.queue[i2] = null;
                if (i2 != iIndexOf) {
                    siftDown(iIndexOf, runnableScheduledFuture);
                    if (this.queue[iIndexOf] == runnableScheduledFuture) {
                        siftUp(iIndexOf, runnableScheduledFuture);
                    }
                }
                reentrantLock.unlock();
                return true;
            } finally {
                reentrantLock.unlock();
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                return this.size;
            } finally {
                reentrantLock.unlock();
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return size() == 0;
        }

        @Override // java.util.concurrent.BlockingQueue
        public int remainingCapacity() {
            return Integer.MAX_VALUE;
        }

        @Override // java.util.Queue
        public RunnableScheduledFuture<?> peek() {
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                return this.queue[0];
            } finally {
                reentrantLock.unlock();
            }
        }

        @Override // java.util.Queue
        public boolean offer(Runnable runnable) {
            if (runnable == null) {
                throw new NullPointerException();
            }
            RunnableScheduledFuture<?> runnableScheduledFuture = (RunnableScheduledFuture) runnable;
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                int i2 = this.size;
                if (i2 >= this.queue.length) {
                    grow();
                }
                this.size = i2 + 1;
                if (i2 == 0) {
                    this.queue[0] = runnableScheduledFuture;
                    setIndex(runnableScheduledFuture, 0);
                } else {
                    siftUp(i2, runnableScheduledFuture);
                }
                if (this.queue[0] == runnableScheduledFuture) {
                    this.leader = null;
                    this.available.signal();
                }
                return true;
            } finally {
                reentrantLock.unlock();
            }
        }

        @Override // java.util.concurrent.BlockingQueue
        public void put(Runnable runnable) {
            offer(runnable);
        }

        @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean add(Runnable runnable) {
            return offer(runnable);
        }

        @Override // java.util.concurrent.BlockingQueue
        public boolean offer(Runnable runnable, long j2, TimeUnit timeUnit) {
            return offer(runnable);
        }

        private RunnableScheduledFuture<?> finishPoll(RunnableScheduledFuture<?> runnableScheduledFuture) {
            int i2 = this.size - 1;
            this.size = i2;
            RunnableScheduledFuture<?> runnableScheduledFuture2 = this.queue[i2];
            this.queue[i2] = null;
            if (i2 != 0) {
                siftDown(0, runnableScheduledFuture2);
            }
            setIndex(runnableScheduledFuture, -1);
            return runnableScheduledFuture;
        }

        @Override // java.util.Queue
        public RunnableScheduledFuture<?> poll() {
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                RunnableScheduledFuture<?> runnableScheduledFuture = this.queue[0];
                if (runnableScheduledFuture == null || runnableScheduledFuture.getDelay(TimeUnit.NANOSECONDS) > 0) {
                    return null;
                }
                RunnableScheduledFuture<?> runnableScheduledFutureFinishPoll = finishPoll(runnableScheduledFuture);
                reentrantLock.unlock();
                return runnableScheduledFutureFinishPoll;
            } finally {
                reentrantLock.unlock();
            }
        }

        @Override // java.util.concurrent.BlockingQueue
        /* renamed from: take, reason: merged with bridge method [inline-methods] */
        public Runnable take2() throws InterruptedException {
            RunnableScheduledFuture<?> runnableScheduledFuture;
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lockInterruptibly();
            while (true) {
                try {
                    runnableScheduledFuture = this.queue[0];
                    if (runnableScheduledFuture == null) {
                        this.available.await();
                    } else {
                        long delay = runnableScheduledFuture.getDelay(TimeUnit.NANOSECONDS);
                        if (delay <= 0) {
                            break;
                        }
                        if (this.leader != null) {
                            this.available.await();
                        } else {
                            Thread threadCurrentThread = Thread.currentThread();
                            this.leader = threadCurrentThread;
                            try {
                                this.available.awaitNanos(delay);
                                if (this.leader == threadCurrentThread) {
                                    this.leader = null;
                                }
                            } catch (Throwable th) {
                                if (this.leader == threadCurrentThread) {
                                    this.leader = null;
                                }
                                throw th;
                            }
                        }
                    }
                } catch (Throwable th2) {
                    if (this.leader == null && this.queue[0] != null) {
                        this.available.signal();
                    }
                    reentrantLock.unlock();
                    throw th2;
                }
            }
            RunnableScheduledFuture<?> runnableScheduledFutureFinishPoll = finishPoll(runnableScheduledFuture);
            if (this.leader == null && this.queue[0] != null) {
                this.available.signal();
            }
            reentrantLock.unlock();
            return runnableScheduledFutureFinishPoll;
        }

        @Override // java.util.concurrent.BlockingQueue
        /* renamed from: poll, reason: merged with bridge method [inline-methods] */
        public Runnable poll2(long j2, TimeUnit timeUnit) throws InterruptedException {
            long nanos = timeUnit.toNanos(j2);
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lockInterruptibly();
            while (true) {
                try {
                    RunnableScheduledFuture<?> runnableScheduledFuture = this.queue[0];
                    if (runnableScheduledFuture != null) {
                        long delay = runnableScheduledFuture.getDelay(TimeUnit.NANOSECONDS);
                        if (delay <= 0) {
                            RunnableScheduledFuture<?> runnableScheduledFutureFinishPoll = finishPoll(runnableScheduledFuture);
                            if (this.leader == null && this.queue[0] != null) {
                                this.available.signal();
                            }
                            reentrantLock.unlock();
                            return runnableScheduledFutureFinishPoll;
                        }
                        if (nanos <= 0) {
                            if (this.leader == null && this.queue[0] != null) {
                                this.available.signal();
                            }
                            reentrantLock.unlock();
                            return null;
                        }
                        if (nanos < delay || this.leader != null) {
                            nanos = this.available.awaitNanos(nanos);
                        } else {
                            Thread threadCurrentThread = Thread.currentThread();
                            this.leader = threadCurrentThread;
                            try {
                                nanos -= delay - this.available.awaitNanos(delay);
                                if (this.leader == threadCurrentThread) {
                                    this.leader = null;
                                }
                            } catch (Throwable th) {
                                if (this.leader == threadCurrentThread) {
                                    this.leader = null;
                                }
                                throw th;
                            }
                        }
                    } else {
                        if (nanos <= 0) {
                            return null;
                        }
                        nanos = this.available.awaitNanos(nanos);
                    }
                } finally {
                    if (this.leader == null && this.queue[(char) 0] != null) {
                        this.available.signal();
                    }
                    reentrantLock.unlock();
                }
            }
        }

        @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            for (int i2 = 0; i2 < this.size; i2++) {
                try {
                    RunnableScheduledFuture<?> runnableScheduledFuture = this.queue[i2];
                    if (runnableScheduledFuture != null) {
                        this.queue[i2] = null;
                        setIndex(runnableScheduledFuture, -1);
                    }
                } catch (Throwable th) {
                    reentrantLock.unlock();
                    throw th;
                }
            }
            this.size = 0;
            reentrantLock.unlock();
        }

        private RunnableScheduledFuture<?> peekExpired() {
            RunnableScheduledFuture<?> runnableScheduledFuture = this.queue[0];
            if (runnableScheduledFuture == null || runnableScheduledFuture.getDelay(TimeUnit.NANOSECONDS) > 0) {
                return null;
            }
            return runnableScheduledFuture;
        }

        @Override // java.util.concurrent.BlockingQueue
        public int drainTo(Collection<? super Runnable> collection) {
            if (collection == null) {
                throw new NullPointerException();
            }
            if (collection == this) {
                throw new IllegalArgumentException();
            }
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            int i2 = 0;
            while (true) {
                try {
                    RunnableScheduledFuture<?> runnableScheduledFuturePeekExpired = peekExpired();
                    if (runnableScheduledFuturePeekExpired != null) {
                        collection.add(runnableScheduledFuturePeekExpired);
                        finishPoll(runnableScheduledFuturePeekExpired);
                        i2++;
                    } else {
                        return i2;
                    }
                } finally {
                    reentrantLock.unlock();
                }
            }
        }

        @Override // java.util.concurrent.BlockingQueue
        public int drainTo(Collection<? super Runnable> collection, int i2) {
            if (collection == null) {
                throw new NullPointerException();
            }
            if (collection == this) {
                throw new IllegalArgumentException();
            }
            if (i2 <= 0) {
                return 0;
            }
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            int i3 = 0;
            while (i3 < i2) {
                try {
                    RunnableScheduledFuture<?> runnableScheduledFuturePeekExpired = peekExpired();
                    if (runnableScheduledFuturePeekExpired == null) {
                        break;
                    }
                    collection.add(runnableScheduledFuturePeekExpired);
                    finishPoll(runnableScheduledFuturePeekExpired);
                    i3++;
                } finally {
                    reentrantLock.unlock();
                }
            }
            return i3;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public Object[] toArray() {
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                return Arrays.copyOf(this.queue, this.size, Object[].class);
            } finally {
                reentrantLock.unlock();
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            try {
                if (tArr.length < this.size) {
                    T[] tArr2 = (T[]) Arrays.copyOf(this.queue, this.size, tArr.getClass());
                    reentrantLock.unlock();
                    return tArr2;
                }
                System.arraycopy(this.queue, 0, tArr, 0, this.size);
                if (tArr.length > this.size) {
                    tArr[this.size] = null;
                }
                return tArr;
            } finally {
                reentrantLock.unlock();
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<Runnable> iterator() {
            return new Itr((RunnableScheduledFuture[]) Arrays.copyOf(this.queue, this.size));
        }

        /* loaded from: rt.jar:java/util/concurrent/ScheduledThreadPoolExecutor$DelayedWorkQueue$Itr.class */
        private class Itr implements Iterator<Runnable> {
            final RunnableScheduledFuture<?>[] array;
            int cursor = 0;
            int lastRet = -1;

            Itr(RunnableScheduledFuture<?>[] runnableScheduledFutureArr) {
                this.array = runnableScheduledFutureArr;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.cursor < this.array.length;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public Runnable next() {
                if (this.cursor >= this.array.length) {
                    throw new NoSuchElementException();
                }
                this.lastRet = this.cursor;
                RunnableScheduledFuture<?>[] runnableScheduledFutureArr = this.array;
                int i2 = this.cursor;
                this.cursor = i2 + 1;
                return runnableScheduledFutureArr[i2];
            }

            @Override // java.util.Iterator
            public void remove() {
                if (this.lastRet < 0) {
                    throw new IllegalStateException();
                }
                DelayedWorkQueue.this.remove(this.array[this.lastRet]);
                this.lastRet = -1;
            }
        }
    }
}
