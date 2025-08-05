package java.util.concurrent;

import sun.misc.Unsafe;

/* loaded from: rt.jar:java/util/concurrent/CountedCompleter.class */
public abstract class CountedCompleter<T> extends ForkJoinTask<T> {
    private static final long serialVersionUID = 5232453752276485070L;
    final CountedCompleter<?> completer;
    volatile int pending;

    /* renamed from: U, reason: collision with root package name */
    private static final Unsafe f12580U;
    private static final long PENDING;

    public abstract void compute();

    protected CountedCompleter(CountedCompleter<?> countedCompleter, int i2) {
        this.completer = countedCompleter;
        this.pending = i2;
    }

    protected CountedCompleter(CountedCompleter<?> countedCompleter) {
        this.completer = countedCompleter;
    }

    protected CountedCompleter() {
        this.completer = null;
    }

    public void onCompletion(CountedCompleter<?> countedCompleter) {
    }

    public boolean onExceptionalCompletion(Throwable th, CountedCompleter<?> countedCompleter) {
        return true;
    }

    public final CountedCompleter<?> getCompleter() {
        return this.completer;
    }

    public final int getPendingCount() {
        return this.pending;
    }

    public final void setPendingCount(int i2) {
        this.pending = i2;
    }

    public final void addToPendingCount(int i2) {
        f12580U.getAndAddInt(this, PENDING, i2);
    }

    public final boolean compareAndSetPendingCount(int i2, int i3) {
        return f12580U.compareAndSwapInt(this, PENDING, i2, i3);
    }

    public final int decrementPendingCountUnlessZero() {
        int i2;
        do {
            i2 = this.pending;
            if (i2 == 0) {
                break;
            }
        } while (!f12580U.compareAndSwapInt(this, PENDING, i2, i2 - 1));
        return i2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final CountedCompleter<?> getRoot() {
        CountedCompleter countedCompleter = this;
        while (true) {
            CountedCompleter countedCompleter2 = countedCompleter;
            CountedCompleter countedCompleter3 = countedCompleter2.completer;
            if (countedCompleter3 != null) {
                countedCompleter = countedCompleter3;
            } else {
                return countedCompleter2;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void tryComplete() {
        CountedCompleter countedCompleter = this;
        CountedCompleter countedCompleter2 = countedCompleter;
        while (true) {
            int i2 = countedCompleter.pending;
            if (i2 == 0) {
                countedCompleter.onCompletion(countedCompleter2);
                CountedCompleter countedCompleter3 = countedCompleter;
                countedCompleter2 = countedCompleter3;
                CountedCompleter countedCompleter4 = countedCompleter3.completer;
                countedCompleter = countedCompleter4;
                if (countedCompleter4 == null) {
                    countedCompleter2.quietlyComplete();
                    return;
                }
            } else if (f12580U.compareAndSwapInt(countedCompleter, PENDING, i2, i2 - 1)) {
                return;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void propagateCompletion() {
        CountedCompleter countedCompleter = this;
        while (true) {
            int i2 = countedCompleter.pending;
            if (i2 == 0) {
                CountedCompleter countedCompleter2 = countedCompleter;
                CountedCompleter countedCompleter3 = countedCompleter2.completer;
                countedCompleter = countedCompleter3;
                if (countedCompleter3 == null) {
                    countedCompleter2.quietlyComplete();
                    return;
                }
            } else if (f12580U.compareAndSwapInt(countedCompleter, PENDING, i2, i2 - 1)) {
                return;
            }
        }
    }

    @Override // java.util.concurrent.ForkJoinTask
    public void complete(T t2) {
        setRawResult(t2);
        onCompletion(this);
        quietlyComplete();
        CountedCompleter<?> countedCompleter = this.completer;
        if (countedCompleter != null) {
            countedCompleter.tryComplete();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final CountedCompleter<?> firstComplete() {
        int i2;
        do {
            i2 = this.pending;
            if (i2 == 0) {
                return this;
            }
        } while (!f12580U.compareAndSwapInt(this, PENDING, i2, i2 - 1));
        return null;
    }

    public final CountedCompleter<?> nextComplete() {
        CountedCompleter<?> countedCompleter = this.completer;
        if (countedCompleter != null) {
            return countedCompleter.firstComplete();
        }
        quietlyComplete();
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void quietlyCompleteRoot() {
        CountedCompleter countedCompleter = this;
        while (true) {
            CountedCompleter countedCompleter2 = countedCompleter;
            CountedCompleter countedCompleter3 = countedCompleter2.completer;
            if (countedCompleter3 == null) {
                countedCompleter2.quietlyComplete();
                return;
            }
            countedCompleter = countedCompleter3;
        }
    }

    public final void helpComplete(int i2) {
        if (i2 > 0 && this.status >= 0) {
            Thread threadCurrentThread = Thread.currentThread();
            if (threadCurrentThread instanceof ForkJoinWorkerThread) {
                ForkJoinWorkerThread forkJoinWorkerThread = (ForkJoinWorkerThread) threadCurrentThread;
                forkJoinWorkerThread.pool.helpComplete(forkJoinWorkerThread.workQueue, this, i2);
            } else {
                ForkJoinPool.common.externalHelpComplete(this, i2);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.ForkJoinTask
    void internalPropagateException(Throwable th) {
        CountedCompleter countedCompleter = this;
        CountedCompleter countedCompleter2 = countedCompleter;
        while (countedCompleter.onExceptionalCompletion(th, countedCompleter2)) {
            CountedCompleter countedCompleter3 = countedCompleter;
            countedCompleter2 = countedCompleter3;
            CountedCompleter countedCompleter4 = countedCompleter3.completer;
            countedCompleter = countedCompleter4;
            if (countedCompleter4 == null || countedCompleter.status < 0 || countedCompleter.recordExceptionalCompletion(th) != Integer.MIN_VALUE) {
                return;
            }
        }
    }

    @Override // java.util.concurrent.ForkJoinTask
    protected final boolean exec() {
        compute();
        return false;
    }

    @Override // java.util.concurrent.ForkJoinTask
    public T getRawResult() {
        return null;
    }

    @Override // java.util.concurrent.ForkJoinTask
    protected void setRawResult(T t2) {
    }

    static {
        try {
            f12580U = Unsafe.getUnsafe();
            PENDING = f12580U.objectFieldOffset(CountedCompleter.class.getDeclaredField("pending"));
        } catch (Exception e2) {
            throw new Error(e2);
        }
    }
}
