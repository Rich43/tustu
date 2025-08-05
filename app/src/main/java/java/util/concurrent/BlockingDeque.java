package java.util.concurrent;

import java.util.Deque;
import java.util.Iterator;

/* loaded from: rt.jar:java/util/concurrent/BlockingDeque.class */
public interface BlockingDeque<E> extends BlockingQueue<E>, Deque<E> {
    @Override // java.util.Deque
    void addFirst(E e2);

    @Override // java.util.Deque
    void addLast(E e2);

    @Override // java.util.Deque
    boolean offerFirst(E e2);

    @Override // java.util.Deque
    boolean offerLast(E e2);

    void putFirst(E e2) throws InterruptedException;

    void putLast(E e2) throws InterruptedException;

    boolean offerFirst(E e2, long j2, TimeUnit timeUnit) throws InterruptedException;

    boolean offerLast(E e2, long j2, TimeUnit timeUnit) throws InterruptedException;

    E takeFirst() throws InterruptedException;

    E takeLast() throws InterruptedException;

    E pollFirst(long j2, TimeUnit timeUnit) throws InterruptedException;

    E pollLast(long j2, TimeUnit timeUnit) throws InterruptedException;

    @Override // java.util.Deque
    boolean removeFirstOccurrence(Object obj);

    @Override // java.util.Deque
    boolean removeLastOccurrence(Object obj);

    @Override // java.util.concurrent.BlockingQueue, java.util.Queue, java.util.Collection, java.util.List
    boolean add(E e2);

    @Override // java.util.concurrent.BlockingQueue, java.util.Queue
    boolean offer(E e2);

    @Override // java.util.concurrent.BlockingQueue
    void put(E e2) throws InterruptedException;

    @Override // java.util.concurrent.BlockingQueue
    boolean offer(E e2, long j2, TimeUnit timeUnit) throws InterruptedException;

    @Override // java.util.Queue
    E remove();

    @Override // java.util.Queue
    E poll();

    @Override // java.util.concurrent.BlockingQueue
    E take() throws InterruptedException;

    @Override // java.util.concurrent.BlockingQueue
    E poll(long j2, TimeUnit timeUnit) throws InterruptedException;

    @Override // java.util.Queue
    E element();

    @Override // java.util.Queue
    E peek();

    @Override // java.util.concurrent.BlockingQueue, java.util.Collection, java.util.Set
    boolean remove(Object obj);

    @Override // java.util.concurrent.BlockingQueue, java.util.Collection, java.util.Set
    boolean contains(Object obj);

    @Override // java.util.Collection, java.util.Set
    int size();

    @Override // java.util.Collection, java.lang.Iterable, java.util.List
    Iterator<E> iterator();

    @Override // java.util.Deque
    void push(E e2);
}
