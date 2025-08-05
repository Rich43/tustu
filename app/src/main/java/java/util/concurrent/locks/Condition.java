package java.util.concurrent.locks;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/* loaded from: rt.jar:java/util/concurrent/locks/Condition.class */
public interface Condition {
    void await() throws InterruptedException;

    void awaitUninterruptibly();

    long awaitNanos(long j2) throws InterruptedException;

    boolean await(long j2, TimeUnit timeUnit) throws InterruptedException;

    boolean awaitUntil(Date date) throws InterruptedException;

    void signal();

    void signalAll();
}
