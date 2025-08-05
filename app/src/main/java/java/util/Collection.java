package java.util;

import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/* loaded from: rt.jar:java/util/Collection.class */
public interface Collection<E> extends Iterable<E> {
    int size();

    boolean isEmpty();

    boolean contains(Object obj);

    @Override // java.lang.Iterable, java.util.List
    Iterator<E> iterator();

    Object[] toArray();

    <T> T[] toArray(T[] tArr);

    boolean add(E e2);

    boolean remove(Object obj);

    boolean containsAll(Collection<?> collection);

    boolean addAll(Collection<? extends E> collection);

    boolean removeAll(Collection<?> collection);

    boolean retainAll(Collection<?> collection);

    void clear();

    boolean equals(Object obj);

    int hashCode();

    default boolean removeIf(Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        boolean z2 = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (predicate.test(it.next())) {
                it.remove();
                z2 = true;
            }
        }
        return z2;
    }

    @Override // java.lang.Iterable
    default Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, 0);
    }

    default Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    default Stream<E> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }
}
