package java.util.concurrent;

import java.util.Collection;
import java.util.Queue;

/* loaded from: rt.jar:java/util/concurrent/BlockingQueue.class */
public interface BlockingQueue<E> extends Queue<E> {
    @Override // java.util.Queue, java.util.Collection, java.util.List
    boolean add(E e2);

    @Override // java.util.Queue
    boolean offer(E e2);

    void put(E e2) throws InterruptedException;

    boolean offer(E e2, long j2, TimeUnit timeUnit) throws InterruptedException;

    E take() throws InterruptedException;

    E poll(long j2, TimeUnit timeUnit) throws InterruptedException;

    int remainingCapacity();

    @Override // java.util.Collection, java.util.Set
    boolean remove(Object obj);

    @Override // java.util.Collection, java.util.Set
    boolean contains(Object obj);

    int drainTo(Collection<? super E> collection);

    int drainTo(Collection<? super E> collection, int i2);
}
