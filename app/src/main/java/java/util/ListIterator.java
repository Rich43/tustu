package java.util;

/* loaded from: rt.jar:java/util/ListIterator.class */
public interface ListIterator<E> extends Iterator<E> {
    @Override // java.util.Iterator
    boolean hasNext();

    @Override // java.util.Iterator
    E next();

    boolean hasPrevious();

    E previous();

    int nextIndex();

    int previousIndex();

    @Override // java.util.Iterator
    void remove();

    void set(E e2);

    void add(E e2);
}
