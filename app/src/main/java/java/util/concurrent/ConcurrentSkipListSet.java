package java.util.concurrent;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Spliterator;
import java.util.concurrent.ConcurrentSkipListMap;
import org.icepdf.core.util.PdfOps;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/util/concurrent/ConcurrentSkipListSet.class */
public class ConcurrentSkipListSet<E> extends AbstractSet<E> implements NavigableSet<E>, Cloneable, Serializable {
    private static final long serialVersionUID = -2479143111061671589L;

    /* renamed from: m, reason: collision with root package name */
    private final ConcurrentNavigableMap<E, Object> f12577m;
    private static final Unsafe UNSAFE;
    private static final long mapOffset;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableSet
    public /* bridge */ /* synthetic */ SortedSet tailSet(Object obj) {
        return tailSet((ConcurrentSkipListSet<E>) obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableSet
    public /* bridge */ /* synthetic */ SortedSet headSet(Object obj) {
        return headSet((ConcurrentSkipListSet<E>) obj);
    }

    public ConcurrentSkipListSet() {
        this.f12577m = new ConcurrentSkipListMap();
    }

    public ConcurrentSkipListSet(Comparator<? super E> comparator) {
        this.f12577m = new ConcurrentSkipListMap(comparator);
    }

    public ConcurrentSkipListSet(Collection<? extends E> collection) {
        this.f12577m = new ConcurrentSkipListMap();
        addAll(collection);
    }

    public ConcurrentSkipListSet(SortedSet<E> sortedSet) {
        this.f12577m = new ConcurrentSkipListMap(sortedSet.comparator());
        addAll(sortedSet);
    }

    ConcurrentSkipListSet(ConcurrentNavigableMap<E, Object> concurrentNavigableMap) {
        this.f12577m = concurrentNavigableMap;
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public ConcurrentSkipListSet<E> m3450clone() {
        try {
            ConcurrentSkipListSet<E> concurrentSkipListSet = (ConcurrentSkipListSet) super.clone();
            concurrentSkipListSet.setMap(new ConcurrentSkipListMap((SortedMap) this.f12577m));
            return concurrentSkipListSet;
        } catch (CloneNotSupportedException e2) {
            throw new InternalError();
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.f12577m.size();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean isEmpty() {
        return this.f12577m.isEmpty();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        return this.f12577m.containsKey(obj);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(E e2) {
        return this.f12577m.putIfAbsent(e2, Boolean.TRUE) == null;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object obj) {
        return this.f12577m.remove(obj, Boolean.TRUE);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        this.f12577m.clear();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<E> iterator() {
        return this.f12577m.navigableKeySet().iterator();
    }

    @Override // java.util.NavigableSet
    public Iterator<E> descendingIterator() {
        return this.f12577m.descendingKeySet().iterator();
    }

    @Override // java.util.AbstractSet, java.util.Collection, java.util.List
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Set)) {
            return false;
        }
        Collection<?> collection = (Collection) obj;
        try {
            if (containsAll(collection)) {
                if (collection.containsAll(this)) {
                    return true;
                }
            }
            return false;
        } catch (ClassCastException e2) {
            return false;
        } catch (NullPointerException e3) {
            return false;
        }
    }

    @Override // java.util.AbstractSet, java.util.AbstractCollection, java.util.Collection
    public boolean removeAll(Collection<?> collection) {
        boolean z2 = false;
        Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            if (remove(it.next())) {
                z2 = true;
            }
        }
        return z2;
    }

    @Override // java.util.NavigableSet
    public E lower(E e2) {
        return this.f12577m.lowerKey(e2);
    }

    @Override // java.util.NavigableSet
    public E floor(E e2) {
        return this.f12577m.floorKey(e2);
    }

    @Override // java.util.NavigableSet
    public E ceiling(E e2) {
        return this.f12577m.ceilingKey(e2);
    }

    @Override // java.util.NavigableSet
    public E higher(E e2) {
        return this.f12577m.higherKey(e2);
    }

    @Override // java.util.NavigableSet
    public E pollFirst() {
        Map.Entry<E, Object> entryPollFirstEntry = this.f12577m.pollFirstEntry();
        if (entryPollFirstEntry == null) {
            return null;
        }
        return entryPollFirstEntry.getKey();
    }

    @Override // java.util.NavigableSet
    public E pollLast() {
        Map.Entry<E, Object> entryPollLastEntry = this.f12577m.pollLastEntry();
        if (entryPollLastEntry == null) {
            return null;
        }
        return entryPollLastEntry.getKey();
    }

    @Override // java.util.SortedSet
    public Comparator<? super E> comparator() {
        return this.f12577m.comparator();
    }

    @Override // java.util.SortedSet
    public E first() {
        return this.f12577m.firstKey();
    }

    @Override // java.util.SortedSet
    public E last() {
        return this.f12577m.lastKey();
    }

    @Override // java.util.NavigableSet
    public NavigableSet<E> subSet(E e2, boolean z2, E e3, boolean z3) {
        return new ConcurrentSkipListSet(this.f12577m.subMap((boolean) e2, z2, (boolean) e3, z3));
    }

    @Override // java.util.NavigableSet
    public NavigableSet<E> headSet(E e2, boolean z2) {
        return new ConcurrentSkipListSet(this.f12577m.headMap((ConcurrentNavigableMap<E, Object>) e2, z2));
    }

    @Override // java.util.NavigableSet
    public NavigableSet<E> tailSet(E e2, boolean z2) {
        return new ConcurrentSkipListSet(this.f12577m.tailMap((ConcurrentNavigableMap<E, Object>) e2, z2));
    }

    @Override // java.util.NavigableSet
    public NavigableSet<E> subSet(E e2, E e3) {
        return subSet(e2, true, e3, false);
    }

    @Override // java.util.NavigableSet
    public NavigableSet<E> headSet(E e2) {
        return headSet(e2, false);
    }

    @Override // java.util.NavigableSet
    public NavigableSet<E> tailSet(E e2) {
        return tailSet(e2, true);
    }

    @Override // java.util.NavigableSet
    public NavigableSet<E> descendingSet() {
        return new ConcurrentSkipListSet(this.f12577m.descendingMap());
    }

    @Override // java.util.Collection, java.lang.Iterable
    public Spliterator<E> spliterator() {
        if (this.f12577m instanceof ConcurrentSkipListMap) {
            return ((ConcurrentSkipListMap) this.f12577m).keySpliterator();
        }
        return (Spliterator) ((ConcurrentSkipListMap.SubMap) this.f12577m).keyIterator();
    }

    private void setMap(ConcurrentNavigableMap<E, Object> concurrentNavigableMap) {
        UNSAFE.putObjectVolatile(this, mapOffset, concurrentNavigableMap);
    }

    static {
        try {
            UNSAFE = Unsafe.getUnsafe();
            mapOffset = UNSAFE.objectFieldOffset(ConcurrentSkipListSet.class.getDeclaredField(PdfOps.m_TOKEN));
        } catch (Exception e2) {
            throw new Error(e2);
        }
    }
}
