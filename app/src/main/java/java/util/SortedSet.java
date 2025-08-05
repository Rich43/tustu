package java.util;

import java.util.Spliterators;

/* loaded from: rt.jar:java/util/SortedSet.class */
public interface SortedSet<E> extends Set<E> {
    Comparator<? super E> comparator();

    SortedSet<E> subSet(E e2, E e3);

    SortedSet<E> headSet(E e2);

    SortedSet<E> tailSet(E e2);

    E first();

    E last();

    @Override // java.util.Set, java.util.Collection, java.lang.Iterable
    default Spliterator<E> spliterator() {
        return new Spliterators.IteratorSpliterator<E>(this, 21) { // from class: java.util.SortedSet.1
            @Override // java.util.Spliterators.IteratorSpliterator, java.util.Spliterator
            public Comparator<? super E> getComparator() {
                return SortedSet.this.comparator();
            }
        };
    }
}
