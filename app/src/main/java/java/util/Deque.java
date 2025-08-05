package java.util;

/* loaded from: rt.jar:java/util/Deque.class */
public interface Deque<E> extends Queue<E> {
    void addFirst(E e2);

    void addLast(E e2);

    boolean offerFirst(E e2);

    boolean offerLast(E e2);

    E removeFirst();

    E removeLast();

    E pollFirst();

    E pollLast();

    E getFirst();

    E getLast();

    E peekFirst();

    E peekLast();

    boolean removeFirstOccurrence(Object obj);

    boolean removeLastOccurrence(Object obj);

    @Override // java.util.Queue, java.util.Collection, java.util.List
    boolean add(E e2);

    @Override // java.util.Queue
    boolean offer(E e2);

    @Override // java.util.Queue
    E remove();

    @Override // java.util.Queue
    E poll();

    @Override // java.util.Queue
    E element();

    @Override // java.util.Queue
    E peek();

    void push(E e2);

    E pop();

    @Override // java.util.Collection, java.util.Set
    boolean remove(Object obj);

    @Override // java.util.Collection, java.util.Set
    boolean contains(Object obj);

    @Override // java.util.Collection, java.util.Set
    int size();

    @Override // java.util.Collection, java.lang.Iterable, java.util.List
    Iterator<E> iterator();

    Iterator<E> descendingIterator();
}
