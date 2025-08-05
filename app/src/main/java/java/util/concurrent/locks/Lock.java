package java.util.concurrent.locks;

import java.util.concurrent.TimeUnit;

/* loaded from: rt.jar:java/util/concurrent/locks/Lock.class */
public interface Lock {
    void lock();

    void lockInterruptibly() throws InterruptedException;

    boolean tryLock();

    boolean tryLock(long j2, TimeUnit timeUnit) throws InterruptedException;

    void unlock();

    Condition newCondition();
}
