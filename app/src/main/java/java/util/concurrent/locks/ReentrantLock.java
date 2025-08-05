package java.util.concurrent.locks;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/* loaded from: rt.jar:java/util/concurrent/locks/ReentrantLock.class */
public class ReentrantLock implements Lock, Serializable {
    private static final long serialVersionUID = 7373984872572414699L;
    private final Sync sync;

    /* loaded from: rt.jar:java/util/concurrent/locks/ReentrantLock$Sync.class */
    static abstract class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = -5179523762034025860L;

        abstract void lock();

        Sync() {
        }

        final boolean nonfairTryAcquire(int i2) {
            Thread threadCurrentThread = Thread.currentThread();
            int state = getState();
            if (state == 0) {
                if (compareAndSetState(0, i2)) {
                    setExclusiveOwnerThread(threadCurrentThread);
                    return true;
                }
                return false;
            }
            if (threadCurrentThread == getExclusiveOwnerThread()) {
                int i3 = state + i2;
                if (i3 < 0) {
                    throw new Error("Maximum lock count exceeded");
                }
                setState(i3);
                return true;
            }
            return false;
        }

        @Override // java.util.concurrent.locks.AbstractQueuedSynchronizer
        protected final boolean tryRelease(int i2) {
            int state = getState() - i2;
            if (Thread.currentThread() != getExclusiveOwnerThread()) {
                throw new IllegalMonitorStateException();
            }
            boolean z2 = false;
            if (state == 0) {
                z2 = true;
                setExclusiveOwnerThread(null);
            }
            setState(state);
            return z2;
        }

        @Override // java.util.concurrent.locks.AbstractQueuedSynchronizer
        protected final boolean isHeldExclusively() {
            return getExclusiveOwnerThread() == Thread.currentThread();
        }

        final AbstractQueuedSynchronizer.ConditionObject newCondition() {
            return new AbstractQueuedSynchronizer.ConditionObject();
        }

        final Thread getOwner() {
            if (getState() == 0) {
                return null;
            }
            return getExclusiveOwnerThread();
        }

        final int getHoldCount() {
            if (isHeldExclusively()) {
                return getState();
            }
            return 0;
        }

        final boolean isLocked() {
            return getState() != 0;
        }

        private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
            objectInputStream.defaultReadObject();
            setState(0);
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/locks/ReentrantLock$NonfairSync.class */
    static final class NonfairSync extends Sync {
        private static final long serialVersionUID = 7316153563782823691L;

        NonfairSync() {
        }

        @Override // java.util.concurrent.locks.ReentrantLock.Sync
        final void lock() {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
            } else {
                acquire(1);
            }
        }

        @Override // java.util.concurrent.locks.AbstractQueuedSynchronizer
        protected final boolean tryAcquire(int i2) {
            return nonfairTryAcquire(i2);
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/locks/ReentrantLock$FairSync.class */
    static final class FairSync extends Sync {
        private static final long serialVersionUID = -3000897897090466540L;

        FairSync() {
        }

        @Override // java.util.concurrent.locks.ReentrantLock.Sync
        final void lock() {
            acquire(1);
        }

        @Override // java.util.concurrent.locks.AbstractQueuedSynchronizer
        protected final boolean tryAcquire(int i2) {
            Thread threadCurrentThread = Thread.currentThread();
            int state = getState();
            if (state == 0) {
                if (!hasQueuedPredecessors() && compareAndSetState(0, i2)) {
                    setExclusiveOwnerThread(threadCurrentThread);
                    return true;
                }
                return false;
            }
            if (threadCurrentThread == getExclusiveOwnerThread()) {
                int i3 = state + i2;
                if (i3 < 0) {
                    throw new Error("Maximum lock count exceeded");
                }
                setState(i3);
                return true;
            }
            return false;
        }
    }

    public ReentrantLock() {
        this.sync = new NonfairSync();
    }

    public ReentrantLock(boolean z2) {
        this.sync = z2 ? new FairSync() : new NonfairSync();
    }

    @Override // java.util.concurrent.locks.Lock
    public void lock() {
        this.sync.lock();
    }

    @Override // java.util.concurrent.locks.Lock
    public void lockInterruptibly() throws InterruptedException {
        this.sync.acquireInterruptibly(1);
    }

    @Override // java.util.concurrent.locks.Lock
    public boolean tryLock() {
        return this.sync.nonfairTryAcquire(1);
    }

    @Override // java.util.concurrent.locks.Lock
    public boolean tryLock(long j2, TimeUnit timeUnit) throws InterruptedException {
        return this.sync.tryAcquireNanos(1, timeUnit.toNanos(j2));
    }

    @Override // java.util.concurrent.locks.Lock
    public void unlock() {
        this.sync.release(1);
    }

    @Override // java.util.concurrent.locks.Lock
    public Condition newCondition() {
        return this.sync.newCondition();
    }

    public int getHoldCount() {
        return this.sync.getHoldCount();
    }

    public boolean isHeldByCurrentThread() {
        return this.sync.isHeldExclusively();
    }

    public boolean isLocked() {
        return this.sync.isLocked();
    }

    public final boolean isFair() {
        return this.sync instanceof FairSync;
    }

    protected Thread getOwner() {
        return this.sync.getOwner();
    }

    public final boolean hasQueuedThreads() {
        return this.sync.hasQueuedThreads();
    }

    public final boolean hasQueuedThread(Thread thread) {
        return this.sync.isQueued(thread);
    }

    public final int getQueueLength() {
        return this.sync.getQueueLength();
    }

    protected Collection<Thread> getQueuedThreads() {
        return this.sync.getQueuedThreads();
    }

    public boolean hasWaiters(Condition condition) {
        if (condition == null) {
            throw new NullPointerException();
        }
        if (!(condition instanceof AbstractQueuedSynchronizer.ConditionObject)) {
            throw new IllegalArgumentException("not owner");
        }
        return this.sync.hasWaiters((AbstractQueuedSynchronizer.ConditionObject) condition);
    }

    public int getWaitQueueLength(Condition condition) {
        if (condition == null) {
            throw new NullPointerException();
        }
        if (!(condition instanceof AbstractQueuedSynchronizer.ConditionObject)) {
            throw new IllegalArgumentException("not owner");
        }
        return this.sync.getWaitQueueLength((AbstractQueuedSynchronizer.ConditionObject) condition);
    }

    protected Collection<Thread> getWaitingThreads(Condition condition) {
        if (condition == null) {
            throw new NullPointerException();
        }
        if (!(condition instanceof AbstractQueuedSynchronizer.ConditionObject)) {
            throw new IllegalArgumentException("not owner");
        }
        return this.sync.getWaitingThreads((AbstractQueuedSynchronizer.ConditionObject) condition);
    }

    public String toString() {
        Thread owner = this.sync.getOwner();
        return super.toString() + (owner == null ? "[Unlocked]" : "[Locked by thread " + owner.getName() + "]");
    }
}
