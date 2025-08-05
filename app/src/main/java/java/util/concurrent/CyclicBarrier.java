package java.util.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: rt.jar:java/util/concurrent/CyclicBarrier.class */
public class CyclicBarrier {
    private final ReentrantLock lock;
    private final Condition trip;
    private final int parties;
    private final Runnable barrierCommand;
    private Generation generation;
    private int count;

    /* loaded from: rt.jar:java/util/concurrent/CyclicBarrier$Generation.class */
    private static class Generation {
        boolean broken;

        private Generation() {
            this.broken = false;
        }
    }

    private void nextGeneration() {
        this.trip.signalAll();
        this.count = this.parties;
        this.generation = new Generation();
    }

    private void breakBarrier() {
        this.generation.broken = true;
        this.count = this.parties;
        this.trip.signalAll();
    }

    /* JADX WARN: Finally extract failed */
    private int dowait(boolean z2, long j2) throws InterruptedException, TimeoutException, BrokenBarrierException {
        Generation generation;
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            generation = this.generation;
        } finally {
            reentrantLock.unlock();
        }
        if (generation.broken) {
            throw new BrokenBarrierException();
        }
        if (Thread.interrupted()) {
            breakBarrier();
            throw new InterruptedException();
        }
        int i2 = this.count - 1;
        this.count = i2;
        if (i2 == 0) {
            boolean z3 = false;
            try {
                Runnable runnable = this.barrierCommand;
                if (runnable != null) {
                    runnable.run();
                }
                z3 = true;
                nextGeneration();
                if (1 == 0) {
                    breakBarrier();
                }
                return 0;
            } catch (Throwable th) {
                if (!z3) {
                    breakBarrier();
                }
                throw th;
            }
        }
        while (true) {
            if (!z2) {
                try {
                    this.trip.await();
                } catch (InterruptedException e2) {
                    if (generation == this.generation && !generation.broken) {
                        breakBarrier();
                        throw e2;
                    }
                    Thread.currentThread().interrupt();
                }
            } else if (j2 > 0) {
                j2 = this.trip.awaitNanos(j2);
            }
            if (generation.broken) {
                throw new BrokenBarrierException();
            }
            if (generation == this.generation) {
                if (z2 && j2 <= 0) {
                    breakBarrier();
                    throw new TimeoutException();
                }
            } else {
                reentrantLock.unlock();
                return i2;
            }
        }
        reentrantLock.unlock();
    }

    public CyclicBarrier(int i2, Runnable runnable) {
        this.lock = new ReentrantLock();
        this.trip = this.lock.newCondition();
        this.generation = new Generation();
        if (i2 <= 0) {
            throw new IllegalArgumentException();
        }
        this.parties = i2;
        this.count = i2;
        this.barrierCommand = runnable;
    }

    public CyclicBarrier(int i2) {
        this(i2, null);
    }

    public int getParties() {
        return this.parties;
    }

    public int await() throws InterruptedException, BrokenBarrierException {
        try {
            return dowait(false, 0L);
        } catch (TimeoutException e2) {
            throw new Error(e2);
        }
    }

    public int await(long j2, TimeUnit timeUnit) throws InterruptedException, TimeoutException, BrokenBarrierException {
        return dowait(true, timeUnit.toNanos(j2));
    }

    public boolean isBroken() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return this.generation.broken;
        } finally {
            reentrantLock.unlock();
        }
    }

    public void reset() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            breakBarrier();
            nextGeneration();
        } finally {
            reentrantLock.unlock();
        }
    }

    public int getNumberWaiting() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return this.parties - this.count;
        } finally {
            reentrantLock.unlock();
        }
    }
}
