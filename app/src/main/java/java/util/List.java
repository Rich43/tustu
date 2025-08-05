package java.util;

import java.util.function.UnaryOperator;

/* loaded from: rt.jar:java/util/List.class */
public interface List<E> extends Collection<E> {
    @Override // java.util.Collection, java.util.Set
    int size();

    @Override // java.util.Collection
    boolean isEmpty();

    @Override // java.util.Collection, java.util.Set
    boolean contains(Object obj);

    Iterator<E> iterator();

    Object[] toArray();

    @Override // java.util.Collection
    <T> T[] toArray(T[] tArr);

    boolean add(E e2);

    @Override // java.util.Collection, java.util.Set
    boolean remove(Object obj);

    @Override // java.util.Collection
    boolean containsAll(Collection<?> collection);

    @Override // java.util.Collection
    boolean addAll(Collection<? extends E> collection);

    boolean addAll(int i2, Collection<? extends E> collection);

    @Override // java.util.Collection
    boolean removeAll(Collection<?> collection);

    @Override // java.util.Collection
    boolean retainAll(Collection<?> collection);

    void clear();

    boolean equals(Object obj);

    int hashCode();

    E get(int i2);

    E set(int i2, E e2);

    void add(int i2, E e2);

    E remove(int i2);

    int indexOf(Object obj);

    int lastIndexOf(Object obj);

    ListIterator<E> listIterator();

    ListIterator<E> listIterator(int i2);

    List<E> subList(int i2, int i3);

    /* JADX WARN: Multi-variable type inference failed */
    default void replaceAll(UnaryOperator<E> unaryOperator) {
        Objects.requireNonNull(unaryOperator);
        ListIterator<E> listIterator = listIterator();
        while (listIterator.hasNext()) {
            listIterator.set(unaryOperator.apply(listIterator.next()));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    default void sort(Comparator<? super E> comparator) {
        Object[] array = toArray();
        Arrays.sort(array, comparator);
        ListIterator<E> listIterator = listIterator();
        for (Object obj : array) {
            listIterator.next();
            listIterator.set(obj);
        }
    }

    @Override // java.util.Collection, java.lang.Iterable
    default Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, 16);
    }
}
