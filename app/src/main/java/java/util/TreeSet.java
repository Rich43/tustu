package java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

/* loaded from: rt.jar:java/util/TreeSet.class */
public class TreeSet<E> extends AbstractSet<E> implements NavigableSet<E>, Cloneable, Serializable {

    /* renamed from: m, reason: collision with root package name */
    private transient NavigableMap<E, Object> f12569m;
    private static final Object PRESENT = new Object();
    private static final long serialVersionUID = -2479143000061671589L;

    TreeSet(NavigableMap<E, Object> navigableMap) {
        this.f12569m = navigableMap;
    }

    public TreeSet() {
        this(new TreeMap());
    }

    public TreeSet(Comparator<? super E> comparator) {
        this(new TreeMap(comparator));
    }

    public TreeSet(Collection<? extends E> collection) {
        this();
        addAll(collection);
    }

    public TreeSet(SortedSet<E> sortedSet) {
        this(sortedSet.comparator());
        addAll(sortedSet);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<E> iterator() {
        return this.f12569m.navigableKeySet().iterator();
    }

    @Override // java.util.NavigableSet
    public Iterator<E> descendingIterator() {
        return this.f12569m.descendingKeySet().iterator();
    }

    @Override // java.util.NavigableSet
    public NavigableSet<E> descendingSet() {
        return new TreeSet(this.f12569m.descendingMap());
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.f12569m.size();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean isEmpty() {
        return this.f12569m.isEmpty();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        return this.f12569m.containsKey(obj);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(E e2) {
        return this.f12569m.put(e2, PRESENT) == null;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object obj) {
        return this.f12569m.remove(obj) == PRESENT;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        this.f12569m.clear();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean addAll(Collection<? extends E> collection) {
        SortedSet sortedSet;
        TreeMap treeMap;
        Comparator<? super E> comparator;
        Comparator<? super E> comparator2;
        if (this.f12569m.size() == 0 && collection.size() > 0 && (collection instanceof SortedSet) && (this.f12569m instanceof TreeMap) && ((comparator = (sortedSet = (SortedSet) collection).comparator()) == (comparator2 = (treeMap = (TreeMap) this.f12569m).comparator()) || (comparator != null && comparator.equals(comparator2)))) {
            treeMap.addAllForTreeSet(sortedSet, PRESENT);
            return true;
        }
        return super.addAll(collection);
    }

    @Override // java.util.NavigableSet
    public NavigableSet<E> subSet(E e2, boolean z2, E e3, boolean z3) {
        return new TreeSet(this.f12569m.subMap(e2, z2, e3, z3));
    }

    @Override // java.util.NavigableSet
    public NavigableSet<E> headSet(E e2, boolean z2) {
        return new TreeSet(this.f12569m.headMap(e2, z2));
    }

    @Override // java.util.NavigableSet
    public NavigableSet<E> tailSet(E e2, boolean z2) {
        return new TreeSet(this.f12569m.tailMap(e2, z2));
    }

    @Override // java.util.NavigableSet
    public SortedSet<E> subSet(E e2, E e3) {
        return subSet(e2, true, e3, false);
    }

    @Override // java.util.NavigableSet
    public SortedSet<E> headSet(E e2) {
        return headSet(e2, false);
    }

    @Override // java.util.NavigableSet
    public SortedSet<E> tailSet(E e2) {
        return tailSet(e2, true);
    }

    @Override // java.util.SortedSet
    public Comparator<? super E> comparator() {
        return this.f12569m.comparator();
    }

    @Override // java.util.SortedSet
    public E first() {
        return this.f12569m.firstKey();
    }

    @Override // java.util.SortedSet
    public E last() {
        return this.f12569m.lastKey();
    }

    @Override // java.util.NavigableSet
    public E lower(E e2) {
        return this.f12569m.lowerKey(e2);
    }

    @Override // java.util.NavigableSet
    public E floor(E e2) {
        return this.f12569m.floorKey(e2);
    }

    @Override // java.util.NavigableSet
    public E ceiling(E e2) {
        return this.f12569m.ceilingKey(e2);
    }

    @Override // java.util.NavigableSet
    public E higher(E e2) {
        return this.f12569m.higherKey(e2);
    }

    @Override // java.util.NavigableSet
    public E pollFirst() {
        Map.Entry<E, Object> entryPollFirstEntry = this.f12569m.pollFirstEntry();
        if (entryPollFirstEntry == null) {
            return null;
        }
        return entryPollFirstEntry.getKey();
    }

    @Override // java.util.NavigableSet
    public E pollLast() {
        Map.Entry<E, Object> entryPollLastEntry = this.f12569m.pollLastEntry();
        if (entryPollLastEntry == null) {
            return null;
        }
        return entryPollLastEntry.getKey();
    }

    public Object clone() {
        try {
            TreeSet treeSet = (TreeSet) super.clone();
            treeSet.f12569m = new TreeMap((SortedMap) this.f12569m);
            return treeSet;
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(this.f12569m.comparator());
        objectOutputStream.writeInt(this.f12569m.size());
        Iterator<E> it = this.f12569m.keySet().iterator();
        while (it.hasNext()) {
            objectOutputStream.writeObject(it.next());
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        TreeMap treeMap = new TreeMap((Comparator) objectInputStream.readObject());
        this.f12569m = treeMap;
        treeMap.readTreeSet(objectInputStream.readInt(), objectInputStream, PRESENT);
    }

    @Override // java.util.Collection, java.lang.Iterable
    public Spliterator<E> spliterator() {
        return TreeMap.keySpliteratorFor(this.f12569m);
    }
}
