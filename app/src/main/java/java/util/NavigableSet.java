package java.util;

/* loaded from: rt.jar:java/util/NavigableSet.class */
public interface NavigableSet<E> extends SortedSet<E> {
    E lower(E e2);

    E floor(E e2);

    E ceiling(E e2);

    E higher(E e2);

    E pollFirst();

    E pollLast();

    @Override // java.util.Set, java.util.Collection, java.lang.Iterable, java.util.List
    Iterator<E> iterator();

    NavigableSet<E> descendingSet();

    Iterator<E> descendingIterator();

    NavigableSet<E> subSet(E e2, boolean z2, E e3, boolean z3);

    NavigableSet<E> headSet(E e2, boolean z2);

    NavigableSet<E> tailSet(E e2, boolean z2);

    SortedSet<E> subSet(E e2, E e3);

    SortedSet<E> headSet(E e2);

    SortedSet<E> tailSet(E e2);
}
