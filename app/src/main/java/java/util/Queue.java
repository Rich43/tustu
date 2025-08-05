package java.util;

/* loaded from: rt.jar:java/util/Queue.class */
public interface Queue<E> extends Collection<E> {
    @Override // java.util.Collection, java.util.List
    boolean add(E e2);

    boolean offer(E e2);

    E remove();

    E poll();

    E element();

    E peek();
}
