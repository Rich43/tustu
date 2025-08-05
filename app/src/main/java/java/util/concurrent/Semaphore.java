package java.util.concurrent;

import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/* loaded from: rt.jar:java/util/concurrent/Semaphore.class */
public class Semaphore implements Serializable {
    private static final long serialVersionUID = -3222578661600680210L;
    private final Sync sync;

    /* loaded from: rt.jar:java/util/concurrent/Semaphore$Sync.class */
    static abstract class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 1192457210091910933L;

        Sync(int i2) {
            setState(i2);
        }

        final int getPermits() {
            return getState();
        }

        final int nonfairTryAcquireShared(int i2) {
            int state;
            int i3;
            do {
                state = getState();
                i3 = state - i2;
                if (i3 < 0) {
                    break;
                }
            } while (!compareAndSetState(state, i3));
            return i3;
        }

        @Override // java.util.concurrent.locks.AbstractQueuedSynchronizer
        protected final boolean tryReleaseShared(int i2) {
            int state;
            int i3;
            do {
                state = getState();
                i3 = state + i2;
                if (i3 < state) {
                    throw new Error("Maximum permit count exceeded");
                }
            } while (!compareAndSetState(state, i3));
            return true;
        }

        final void reducePermits(int i2) {
            int state;
            int i3;
            do {
                state = getState();
                i3 = state - i2;
                if (i3 > state) {
                    throw new Error("Permit count underflow");
                }
            } while (!compareAndSetState(state, i3));
        }

        final int drainPermits() {
            int state;
            do {
                state = getState();
                if (state == 0) {
                    break;
                }
            } while (!compareAndSetState(state, 0));
            return state;
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/Semaphore$NonfairSync.class */
    static final class NonfairSync extends Sync {
        private static final long serialVersionUID = -2694183684443567898L;

        NonfairSync(int i2) {
            super(i2);
        }

        @Override // java.util.concurrent.locks.AbstractQueuedSynchronizer
        protected int tryAcquireShared(int i2) {
            return nonfairTryAcquireShared(i2);
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/Semaphore$FairSync.class */
    static final class FairSync extends Sync {
        private static final long serialVersionUID = 2014338818796000944L;

        FairSync(int i2) {
            super(i2);
        }

        @Override // java.util.concurrent.locks.AbstractQueuedSynchronizer
        protected int tryAcquireShared(int i2) {
            while (!hasQueuedPredecessors()) {
                int state = getState();
                int i3 = state - i2;
                if (i3 < 0 || compareAndSetState(state, i3)) {
                    return i3;
                }
            }
            return -1;
        }
    }

    public Semaphore(int i2) {
        this.sync = new NonfairSync(i2);
    }

    public Semaphore(int i2, boolean z2) {
        this.sync = z2 ? new FairSync(i2) : new NonfairSync(i2);
    }

    public void acquire() throws InterruptedException {
        this.sync.acquireSharedInterruptibly(1);
    }

    public void acquireUninterruptibly() {
        this.sync.acquireShared(1);
    }

    public boolean tryAcquire() {
        return this.sync.nonfairTryAcquireShared(1) >= 0;
    }

    public boolean tryAcquire(long j2, TimeUnit timeUnit) throws InterruptedException {
        return this.sync.tryAcquireSharedNanos(1, timeUnit.toNanos(j2));
    }

    public void release() {
        this.sync.releaseShared(1);
    }

    public void acquire(int i2) throws InterruptedException {
        if (i2 < 0) {
            throw new IllegalArgumentException();
        }
        this.sync.acquireSharedInterruptibly(i2);
    }

    public void acquireUninterruptibly(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException();
        }
        this.sync.acquireShared(i2);
    }

    public boolean tryAcquire(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException();
        }
        return this.sync.nonfairTryAcquireShared(i2) >= 0;
    }

    public boolean tryAcquire(int i2, long j2, TimeUnit timeUnit) throws InterruptedException {
        if (i2 < 0) {
            throw new IllegalArgumentException();
        }
        return this.sync.tryAcquireSharedNanos(i2, timeUnit.toNanos(j2));
    }

    public void release(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException();
        }
        this.sync.releaseShared(i2);
    }

    public int availablePermits() {
        return this.sync.getPermits();
    }

    public int drainPermits() {
        return this.sync.drainPermits();
    }

    protected void reducePermits(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException();
        }
        this.sync.reducePermits(i2);
    }

    public boolean isFair() {
        return this.sync instanceof FairSync;
    }

    public final boolean hasQueuedThreads() {
        return this.sync.hasQueuedThreads();
    }

    public final int getQueueLength() {
        return this.sync.getQueueLength();
    }

    protected Collection<Thread> getQueuedThreads() {
        return this.sync.getQueuedThreads();
    }

    public String toString() {
        return super.toString() + "[Permits = " + this.sync.getPermits() + "]";
    }
}
