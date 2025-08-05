package java.util.concurrent;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/* loaded from: rt.jar:java/util/concurrent/CountDownLatch.class */
public class CountDownLatch {
    private final Sync sync;

    /* loaded from: rt.jar:java/util/concurrent/CountDownLatch$Sync.class */
    private static final class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 4982264981922014374L;

        Sync(int i2) {
            setState(i2);
        }

        int getCount() {
            return getState();
        }

        @Override // java.util.concurrent.locks.AbstractQueuedSynchronizer
        protected int tryAcquireShared(int i2) {
            return getState() == 0 ? 1 : -1;
        }

        @Override // java.util.concurrent.locks.AbstractQueuedSynchronizer
        protected boolean tryReleaseShared(int i2) {
            int state;
            int i3;
            do {
                state = getState();
                if (state == 0) {
                    return false;
                }
                i3 = state - 1;
            } while (!compareAndSetState(state, i3));
            return i3 == 0;
        }
    }

    public CountDownLatch(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("count < 0");
        }
        this.sync = new Sync(i2);
    }

    public void await() throws InterruptedException {
        this.sync.acquireSharedInterruptibly(1);
    }

    public boolean await(long j2, TimeUnit timeUnit) throws InterruptedException {
        return this.sync.tryAcquireSharedNanos(1, timeUnit.toNanos(j2));
    }

    public void countDown() {
        this.sync.releaseShared(1);
    }

    public long getCount() {
        return this.sync.getCount();
    }

    public String toString() {
        return super.toString() + "[Count = " + this.sync.getCount() + "]";
    }
}
