package java.util;

/* loaded from: rt.jar:java/util/Set.class */
public interface Set<E> extends Collection<E> {
    int size();

    @Override // java.util.Collection
    boolean isEmpty();

    boolean contains(Object obj);

    @Override // java.util.Collection, java.lang.Iterable, java.util.List
    Iterator<E> iterator();

    @Override // java.util.Collection, java.util.List
    Object[] toArray();

    @Override // java.util.Collection
    <T> T[] toArray(T[] tArr);

    @Override // java.util.Collection, java.util.List
    boolean add(E e2);

    boolean remove(Object obj);

    @Override // java.util.Collection
    boolean containsAll(Collection<?> collection);

    @Override // java.util.Collection
    boolean addAll(Collection<? extends E> collection);

    @Override // java.util.Collection
    boolean retainAll(Collection<?> collection);

    @Override // java.util.Collection
    boolean removeAll(Collection<?> collection);

    @Override // java.util.Collection, java.util.List
    void clear();

    @Override // java.util.Collection, java.util.List
    boolean equals(Object obj);

    @Override // java.util.Collection, java.util.List
    int hashCode();

    @Override // java.util.Collection, java.lang.Iterable
    default Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, 1);
    }
}
