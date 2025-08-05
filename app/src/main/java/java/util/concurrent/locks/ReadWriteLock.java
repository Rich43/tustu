package java.util.concurrent.locks;

/* loaded from: rt.jar:java/util/concurrent/locks/ReadWriteLock.class */
public interface ReadWriteLock {
    Lock readLock();

    Lock writeLock();
}
