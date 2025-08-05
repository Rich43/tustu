package java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.util.AbstractMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/* loaded from: rt.jar:java/util/TreeMap.class */
public class TreeMap<K, V> extends AbstractMap<K, V> implements NavigableMap<K, V>, Cloneable, Serializable {
    private final Comparator<? super K> comparator;
    private transient Entry<K, V> root;
    private transient int size;
    private transient int modCount;
    private transient TreeMap<K, V>.EntrySet entrySet;
    private transient KeySet<K> navigableKeySet;
    private transient NavigableMap<K, V> descendingMap;
    private static final Object UNBOUNDED = new Object();
    private static final boolean RED = false;
    private static final boolean BLACK = true;
    private static final long serialVersionUID = 919286545866124006L;

    public TreeMap() {
        this.size = 0;
        this.modCount = 0;
        this.comparator = null;
    }

    public TreeMap(Comparator<? super K> comparator) {
        this.size = 0;
        this.modCount = 0;
        this.comparator = comparator;
    }

    public TreeMap(Map<? extends K, ? extends V> map) {
        this.size = 0;
        this.modCount = 0;
        this.comparator = null;
        putAll(map);
    }

    public TreeMap(SortedMap<K, ? extends V> sortedMap) {
        this.size = 0;
        this.modCount = 0;
        this.comparator = sortedMap.comparator();
        try {
            buildFromSorted(sortedMap.size(), sortedMap.entrySet().iterator(), null, null);
        } catch (IOException e2) {
        } catch (ClassNotFoundException e3) {
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        return this.size;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object obj) {
        return getEntry(obj) != null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsValue(Object obj) {
        Entry<K, V> firstEntry = getFirstEntry();
        while (true) {
            Entry<K, V> entry = firstEntry;
            if (entry != null) {
                if (!valEquals(obj, entry.value)) {
                    firstEntry = successor(entry);
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V get(Object obj) {
        Entry<K, V> entry = getEntry(obj);
        if (entry == null) {
            return null;
        }
        return entry.value;
    }

    @Override // java.util.SortedMap
    public Comparator<? super K> comparator() {
        return this.comparator;
    }

    @Override // java.util.SortedMap
    public K firstKey() {
        return (K) key(getFirstEntry());
    }

    @Override // java.util.SortedMap
    public K lastKey() {
        return (K) key(getLastEntry());
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void putAll(Map<? extends K, ? extends V> map) {
        Comparator<? super K> comparator;
        int size = map.size();
        if (this.size == 0 && size != 0 && (map instanceof SortedMap) && ((comparator = ((SortedMap) map).comparator()) == this.comparator || (comparator != null && comparator.equals(this.comparator)))) {
            this.modCount++;
            try {
                buildFromSorted(size, map.entrySet().iterator(), null, null);
                return;
            } catch (IOException e2) {
                return;
            } catch (ClassNotFoundException e3) {
                return;
            }
        }
        super.putAll(map);
    }

    final Entry<K, V> getEntry(Object obj) {
        if (this.comparator != null) {
            return getEntryUsingComparator(obj);
        }
        if (obj == null) {
            throw new NullPointerException();
        }
        Comparable comparable = (Comparable) obj;
        Entry<K, V> entry = this.root;
        while (true) {
            Entry<K, V> entry2 = entry;
            if (entry2 != null) {
                int iCompareTo = comparable.compareTo(entry2.key);
                if (iCompareTo < 0) {
                    entry = entry2.left;
                } else if (iCompareTo > 0) {
                    entry = entry2.right;
                } else {
                    return entry2;
                }
            } else {
                return null;
            }
        }
    }

    final Entry<K, V> getEntryUsingComparator(Object obj) {
        Comparator<? super K> comparator = this.comparator;
        if (comparator != null) {
            Entry<K, V> entry = this.root;
            while (true) {
                Entry<K, V> entry2 = entry;
                if (entry2 != null) {
                    int iCompare = comparator.compare(obj, entry2.key);
                    if (iCompare < 0) {
                        entry = entry2.left;
                    } else if (iCompare > 0) {
                        entry = entry2.right;
                    } else {
                        return entry2;
                    }
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    final Entry<K, V> getCeilingEntry(K k2) {
        Entry<K, V> entry = this.root;
        while (true) {
            Entry<K, V> entry2 = entry;
            if (entry2 != null) {
                int iCompare = compare(k2, entry2.key);
                if (iCompare < 0) {
                    if (entry2.left != null) {
                        entry = entry2.left;
                    } else {
                        return entry2;
                    }
                } else if (iCompare > 0) {
                    if (entry2.right != null) {
                        entry = entry2.right;
                    } else {
                        Entry<K, V> entry3 = entry2.parent;
                        Entry<K, V> entry4 = entry2;
                        while (entry3 != null && entry4 == entry3.right) {
                            entry4 = entry3;
                            entry3 = entry3.parent;
                        }
                        return entry3;
                    }
                } else {
                    return entry2;
                }
            } else {
                return null;
            }
        }
    }

    final Entry<K, V> getFloorEntry(K k2) {
        Entry<K, V> entry = this.root;
        while (true) {
            Entry<K, V> entry2 = entry;
            if (entry2 != null) {
                int iCompare = compare(k2, entry2.key);
                if (iCompare > 0) {
                    if (entry2.right != null) {
                        entry = entry2.right;
                    } else {
                        return entry2;
                    }
                } else if (iCompare < 0) {
                    if (entry2.left != null) {
                        entry = entry2.left;
                    } else {
                        Entry<K, V> entry3 = entry2.parent;
                        Entry<K, V> entry4 = entry2;
                        while (entry3 != null && entry4 == entry3.left) {
                            entry4 = entry3;
                            entry3 = entry3.parent;
                        }
                        return entry3;
                    }
                } else {
                    return entry2;
                }
            } else {
                return null;
            }
        }
    }

    final Entry<K, V> getHigherEntry(K k2) {
        Entry<K, V> entry = this.root;
        while (true) {
            Entry<K, V> entry2 = entry;
            if (entry2 != null) {
                if (compare(k2, entry2.key) < 0) {
                    if (entry2.left != null) {
                        entry = entry2.left;
                    } else {
                        return entry2;
                    }
                } else if (entry2.right != null) {
                    entry = entry2.right;
                } else {
                    Entry<K, V> entry3 = entry2.parent;
                    Entry<K, V> entry4 = entry2;
                    while (entry3 != null && entry4 == entry3.right) {
                        entry4 = entry3;
                        entry3 = entry3.parent;
                    }
                    return entry3;
                }
            } else {
                return null;
            }
        }
    }

    final Entry<K, V> getLowerEntry(K k2) {
        Entry<K, V> entry = this.root;
        while (true) {
            Entry<K, V> entry2 = entry;
            if (entry2 != null) {
                if (compare(k2, entry2.key) > 0) {
                    if (entry2.right != null) {
                        entry = entry2.right;
                    } else {
                        return entry2;
                    }
                } else if (entry2.left != null) {
                    entry = entry2.left;
                } else {
                    Entry<K, V> entry3 = entry2.parent;
                    Entry<K, V> entry4 = entry2;
                    while (entry3 != null && entry4 == entry3.left) {
                        entry4 = entry3;
                        entry3 = entry3.parent;
                    }
                    return entry3;
                }
            } else {
                return null;
            }
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V put(K k2, V v2) {
        Entry<K, V> entry;
        int iCompareTo;
        Entry<K, V> entry2 = this.root;
        if (entry2 == null) {
            compare(k2, k2);
            this.root = new Entry<>(k2, v2, null);
            this.size = 1;
            this.modCount++;
            return null;
        }
        Comparator<? super K> comparator = this.comparator;
        if (comparator != null) {
            do {
                entry = entry2;
                iCompareTo = comparator.compare(k2, entry2.key);
                if (iCompareTo < 0) {
                    entry2 = entry2.left;
                } else if (iCompareTo > 0) {
                    entry2 = entry2.right;
                } else {
                    return entry2.setValue(v2);
                }
            } while (entry2 != null);
        } else {
            if (k2 == null) {
                throw new NullPointerException();
            }
            Comparable comparable = (Comparable) k2;
            do {
                entry = entry2;
                iCompareTo = comparable.compareTo(entry2.key);
                if (iCompareTo < 0) {
                    entry2 = entry2.left;
                } else if (iCompareTo > 0) {
                    entry2 = entry2.right;
                } else {
                    return entry2.setValue(v2);
                }
            } while (entry2 != null);
        }
        Entry<K, V> entry3 = new Entry<>(k2, v2, entry);
        if (iCompareTo < 0) {
            entry.left = entry3;
        } else {
            entry.right = entry3;
        }
        fixAfterInsertion(entry3);
        this.size++;
        this.modCount++;
        return null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V remove(Object obj) {
        Entry<K, V> entry = getEntry(obj);
        if (entry == null) {
            return null;
        }
        V v2 = entry.value;
        deleteEntry(entry);
        return v2;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        this.modCount++;
        this.size = 0;
        this.root = null;
    }

    @Override // java.util.AbstractMap
    public Object clone() {
        try {
            TreeMap treeMap = (TreeMap) super.clone();
            treeMap.root = null;
            treeMap.size = 0;
            treeMap.modCount = 0;
            treeMap.entrySet = null;
            treeMap.navigableKeySet = null;
            treeMap.descendingMap = null;
            try {
                treeMap.buildFromSorted(this.size, entrySet().iterator(), null, null);
            } catch (IOException e2) {
            } catch (ClassNotFoundException e3) {
            }
            return treeMap;
        } catch (CloneNotSupportedException e4) {
            throw new InternalError(e4);
        }
    }

    @Override // java.util.NavigableMap
    public Map.Entry<K, V> firstEntry() {
        return exportEntry(getFirstEntry());
    }

    @Override // java.util.NavigableMap
    public Map.Entry<K, V> lastEntry() {
        return exportEntry(getLastEntry());
    }

    @Override // java.util.NavigableMap
    public Map.Entry<K, V> pollFirstEntry() {
        Entry<K, V> firstEntry = getFirstEntry();
        Map.Entry<K, V> entryExportEntry = exportEntry(firstEntry);
        if (firstEntry != null) {
            deleteEntry(firstEntry);
        }
        return entryExportEntry;
    }

    @Override // java.util.NavigableMap
    public Map.Entry<K, V> pollLastEntry() {
        Entry<K, V> lastEntry = getLastEntry();
        Map.Entry<K, V> entryExportEntry = exportEntry(lastEntry);
        if (lastEntry != null) {
            deleteEntry(lastEntry);
        }
        return entryExportEntry;
    }

    @Override // java.util.NavigableMap
    public Map.Entry<K, V> lowerEntry(K k2) {
        return exportEntry(getLowerEntry(k2));
    }

    @Override // java.util.NavigableMap
    public K lowerKey(K k2) {
        return (K) keyOrNull(getLowerEntry(k2));
    }

    @Override // java.util.NavigableMap
    public Map.Entry<K, V> floorEntry(K k2) {
        return exportEntry(getFloorEntry(k2));
    }

    @Override // java.util.NavigableMap
    public K floorKey(K k2) {
        return (K) keyOrNull(getFloorEntry(k2));
    }

    @Override // java.util.NavigableMap
    public Map.Entry<K, V> ceilingEntry(K k2) {
        return exportEntry(getCeilingEntry(k2));
    }

    @Override // java.util.NavigableMap
    public K ceilingKey(K k2) {
        return (K) keyOrNull(getCeilingEntry(k2));
    }

    @Override // java.util.NavigableMap
    public Map.Entry<K, V> higherEntry(K k2) {
        return exportEntry(getHigherEntry(k2));
    }

    @Override // java.util.NavigableMap
    public K higherKey(K k2) {
        return (K) keyOrNull(getHigherEntry(k2));
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<K> keySet() {
        return navigableKeySet();
    }

    @Override // java.util.NavigableMap
    public NavigableSet<K> navigableKeySet() {
        KeySet<K> keySet = this.navigableKeySet;
        if (keySet != null) {
            return keySet;
        }
        KeySet<K> keySet2 = new KeySet<>(this);
        this.navigableKeySet = keySet2;
        return keySet2;
    }

    @Override // java.util.NavigableMap
    public NavigableSet<K> descendingKeySet() {
        return descendingMap().navigableKeySet();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Collection<V> values() {
        Collection<V> values = this.values;
        if (values == null) {
            values = new Values();
            this.values = values;
        }
        return values;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        TreeMap<K, V>.EntrySet entrySet = this.entrySet;
        if (entrySet != null) {
            return entrySet;
        }
        TreeMap<K, V>.EntrySet entrySet2 = new EntrySet();
        this.entrySet = entrySet2;
        return entrySet2;
    }

    @Override // java.util.NavigableMap
    public NavigableMap<K, V> descendingMap() {
        NavigableMap<K, V> navigableMap = this.descendingMap;
        if (navigableMap != null) {
            return navigableMap;
        }
        DescendingSubMap descendingSubMap = new DescendingSubMap(this, true, null, true, true, null, true);
        this.descendingMap = descendingSubMap;
        return descendingSubMap;
    }

    @Override // java.util.NavigableMap
    public NavigableMap<K, V> subMap(K k2, boolean z2, K k3, boolean z3) {
        return new AscendingSubMap(this, false, k2, z2, false, k3, z3);
    }

    @Override // java.util.NavigableMap
    public NavigableMap<K, V> headMap(K k2, boolean z2) {
        return new AscendingSubMap(this, true, null, true, false, k2, z2);
    }

    @Override // java.util.NavigableMap
    public NavigableMap<K, V> tailMap(K k2, boolean z2) {
        return new AscendingSubMap(this, false, k2, z2, true, null, true);
    }

    @Override // java.util.NavigableMap, java.util.SortedMap
    public SortedMap<K, V> subMap(K k2, K k3) {
        return subMap(k2, true, k3, false);
    }

    @Override // java.util.NavigableMap, java.util.SortedMap
    public SortedMap<K, V> headMap(K k2) {
        return headMap(k2, false);
    }

    @Override // java.util.NavigableMap, java.util.SortedMap
    public SortedMap<K, V> tailMap(K k2) {
        return tailMap(k2, true);
    }

    @Override // java.util.Map
    public boolean replace(K k2, V v2, V v3) {
        Entry<K, V> entry = getEntry(k2);
        if (entry != null && Objects.equals(v2, entry.value)) {
            entry.value = v3;
            return true;
        }
        return false;
    }

    @Override // java.util.Map
    public V replace(K k2, V v2) {
        Entry<K, V> entry = getEntry(k2);
        if (entry != null) {
            V v3 = entry.value;
            entry.value = v2;
            return v3;
        }
        return null;
    }

    @Override // java.util.Map
    public void forEach(BiConsumer<? super K, ? super V> biConsumer) {
        Objects.requireNonNull(biConsumer);
        int i2 = this.modCount;
        Entry<K, V> firstEntry = getFirstEntry();
        while (true) {
            Entry<K, V> entry = firstEntry;
            if (entry != null) {
                biConsumer.accept(entry.key, entry.value);
                if (i2 == this.modCount) {
                    firstEntry = successor(entry);
                } else {
                    throw new ConcurrentModificationException();
                }
            } else {
                return;
            }
        }
    }

    @Override // java.util.Map
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        int i2 = this.modCount;
        Entry<K, V> firstEntry = getFirstEntry();
        while (true) {
            Entry<K, V> entry = firstEntry;
            if (entry != null) {
                entry.value = biFunction.apply(entry.key, entry.value);
                if (i2 == this.modCount) {
                    firstEntry = successor(entry);
                } else {
                    throw new ConcurrentModificationException();
                }
            } else {
                return;
            }
        }
    }

    /* loaded from: rt.jar:java/util/TreeMap$Values.class */
    class Values extends AbstractCollection<V> {
        Values() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<V> iterator() {
            return new ValueIterator(TreeMap.this.getFirstEntry());
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return TreeMap.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return TreeMap.this.containsValue(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            Entry<K, V> firstEntry = TreeMap.this.getFirstEntry();
            while (true) {
                Entry<K, V> entry = firstEntry;
                if (entry != null) {
                    if (TreeMap.valEquals(entry.getValue(), obj)) {
                        TreeMap.this.deleteEntry(entry);
                        return true;
                    }
                    firstEntry = TreeMap.successor(entry);
                } else {
                    return false;
                }
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            TreeMap.this.clear();
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Spliterator<V> spliterator() {
            return new ValueSpliterator(TreeMap.this, null, null, 0, -1, 0);
        }
    }

    /* loaded from: rt.jar:java/util/TreeMap$EntrySet.class */
    class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        EntrySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator(TreeMap.this.getFirstEntry());
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            Object value = entry.getValue();
            Entry<K, V> entry2 = TreeMap.this.getEntry(entry.getKey());
            return entry2 != null && TreeMap.valEquals(entry2.getValue(), value);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            Object value = entry.getValue();
            Entry<K, V> entry2 = TreeMap.this.getEntry(entry.getKey());
            if (entry2 != null && TreeMap.valEquals(entry2.getValue(), value)) {
                TreeMap.this.deleteEntry(entry2);
                return true;
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return TreeMap.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            TreeMap.this.clear();
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Spliterator<Map.Entry<K, V>> spliterator() {
            return new EntrySpliterator(TreeMap.this, null, null, 0, -1, 0);
        }
    }

    Iterator<K> keyIterator() {
        return new KeyIterator(getFirstEntry());
    }

    Iterator<K> descendingKeyIterator() {
        return new DescendingKeyIterator(getLastEntry());
    }

    /* loaded from: rt.jar:java/util/TreeMap$KeySet.class */
    static final class KeySet<E> extends AbstractSet<E> implements NavigableSet<E> {

        /* renamed from: m, reason: collision with root package name */
        private final NavigableMap<E, ?> f12567m;

        KeySet(NavigableMap<E, ?> navigableMap) {
            this.f12567m = navigableMap;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<E> iterator() {
            if (this.f12567m instanceof TreeMap) {
                return ((TreeMap) this.f12567m).keyIterator();
            }
            return ((NavigableSubMap) this.f12567m).keyIterator();
        }

        @Override // java.util.NavigableSet
        public Iterator<E> descendingIterator() {
            if (this.f12567m instanceof TreeMap) {
                return ((TreeMap) this.f12567m).descendingKeyIterator();
            }
            return ((NavigableSubMap) this.f12567m).descendingKeyIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.f12567m.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return this.f12567m.isEmpty();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return this.f12567m.containsKey(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            this.f12567m.clear();
        }

        @Override // java.util.NavigableSet
        public E lower(E e2) {
            return this.f12567m.lowerKey(e2);
        }

        @Override // java.util.NavigableSet
        public E floor(E e2) {
            return this.f12567m.floorKey(e2);
        }

        @Override // java.util.NavigableSet
        public E ceiling(E e2) {
            return this.f12567m.ceilingKey(e2);
        }

        @Override // java.util.NavigableSet
        public E higher(E e2) {
            return this.f12567m.higherKey(e2);
        }

        @Override // java.util.SortedSet
        public E first() {
            return this.f12567m.firstKey();
        }

        @Override // java.util.SortedSet
        public E last() {
            return this.f12567m.lastKey();
        }

        @Override // java.util.SortedSet
        public Comparator<? super E> comparator() {
            return this.f12567m.comparator();
        }

        @Override // java.util.NavigableSet
        public E pollFirst() {
            Map.Entry<E, ?> entryPollFirstEntry = this.f12567m.pollFirstEntry();
            if (entryPollFirstEntry == null) {
                return null;
            }
            return entryPollFirstEntry.getKey();
        }

        @Override // java.util.NavigableSet
        public E pollLast() {
            Map.Entry<E, ?> entryPollLastEntry = this.f12567m.pollLastEntry();
            if (entryPollLastEntry == null) {
                return null;
            }
            return entryPollLastEntry.getKey();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            int size = size();
            this.f12567m.remove(obj);
            return size() != size;
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> subSet(E e2, boolean z2, E e3, boolean z3) {
            return new KeySet(this.f12567m.subMap(e2, z2, e3, z3));
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> headSet(E e2, boolean z2) {
            return new KeySet(this.f12567m.headMap(e2, z2));
        }

        @Override // java.util.NavigableSet
        public NavigableSet<E> tailSet(E e2, boolean z2) {
            return new KeySet(this.f12567m.tailMap(e2, z2));
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

        @Override // java.util.NavigableSet
        public NavigableSet<E> descendingSet() {
            return new KeySet(this.f12567m.descendingMap());
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Spliterator<E> spliterator() {
            return TreeMap.keySpliteratorFor(this.f12567m);
        }
    }

    /* loaded from: rt.jar:java/util/TreeMap$PrivateEntryIterator.class */
    abstract class PrivateEntryIterator<T> implements Iterator<T> {
        Entry<K, V> next;
        Entry<K, V> lastReturned = null;
        int expectedModCount;

        PrivateEntryIterator(Entry<K, V> entry) {
            this.expectedModCount = TreeMap.this.modCount;
            this.next = entry;
        }

        @Override // java.util.Iterator
        public final boolean hasNext() {
            return this.next != null;
        }

        final Entry<K, V> nextEntry() {
            Entry<K, V> entry = this.next;
            if (entry != null) {
                if (TreeMap.this.modCount != this.expectedModCount) {
                    throw new ConcurrentModificationException();
                }
                this.next = TreeMap.successor(entry);
                this.lastReturned = entry;
                return entry;
            }
            throw new NoSuchElementException();
        }

        final Entry<K, V> prevEntry() {
            Entry<K, V> entry = this.next;
            if (entry != null) {
                if (TreeMap.this.modCount != this.expectedModCount) {
                    throw new ConcurrentModificationException();
                }
                this.next = TreeMap.predecessor(entry);
                this.lastReturned = entry;
                return entry;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            if (this.lastReturned != null) {
                if (TreeMap.this.modCount != this.expectedModCount) {
                    throw new ConcurrentModificationException();
                }
                if (this.lastReturned.left != null && this.lastReturned.right != null) {
                    this.next = this.lastReturned;
                }
                TreeMap.this.deleteEntry(this.lastReturned);
                this.expectedModCount = TreeMap.this.modCount;
                this.lastReturned = null;
                return;
            }
            throw new IllegalStateException();
        }
    }

    /* loaded from: rt.jar:java/util/TreeMap$EntryIterator.class */
    final class EntryIterator extends TreeMap<K, V>.PrivateEntryIterator<Map.Entry<K, V>> {
        EntryIterator(Entry<K, V> entry) {
            super(entry);
        }

        @Override // java.util.Iterator
        public Map.Entry<K, V> next() {
            return nextEntry();
        }
    }

    /* loaded from: rt.jar:java/util/TreeMap$ValueIterator.class */
    final class ValueIterator extends TreeMap<K, V>.PrivateEntryIterator<V> {
        ValueIterator(Entry<K, V> entry) {
            super(entry);
        }

        @Override // java.util.Iterator
        public V next() {
            return nextEntry().value;
        }
    }

    /* loaded from: rt.jar:java/util/TreeMap$KeyIterator.class */
    final class KeyIterator extends TreeMap<K, V>.PrivateEntryIterator<K> {
        KeyIterator(Entry<K, V> entry) {
            super(entry);
        }

        @Override // java.util.Iterator
        public K next() {
            return nextEntry().key;
        }
    }

    /* loaded from: rt.jar:java/util/TreeMap$DescendingKeyIterator.class */
    final class DescendingKeyIterator extends TreeMap<K, V>.PrivateEntryIterator<K> {
        DescendingKeyIterator(Entry<K, V> entry) {
            super(entry);
        }

        @Override // java.util.Iterator
        public K next() {
            return prevEntry().key;
        }

        @Override // java.util.TreeMap.PrivateEntryIterator, java.util.Iterator
        public void remove() {
            if (this.lastReturned != null) {
                if (TreeMap.this.modCount == this.expectedModCount) {
                    TreeMap.this.deleteEntry(this.lastReturned);
                    this.lastReturned = null;
                    this.expectedModCount = TreeMap.this.modCount;
                    return;
                }
                throw new ConcurrentModificationException();
            }
            throw new IllegalStateException();
        }
    }

    final int compare(Object obj, Object obj2) {
        return this.comparator == null ? ((Comparable) obj).compareTo(obj2) : this.comparator.compare(obj, obj2);
    }

    static final boolean valEquals(Object obj, Object obj2) {
        return obj == null ? obj2 == null : obj.equals(obj2);
    }

    static <K, V> Map.Entry<K, V> exportEntry(Entry<K, V> entry) {
        if (entry == null) {
            return null;
        }
        return new AbstractMap.SimpleImmutableEntry(entry);
    }

    static <K, V> K keyOrNull(Entry<K, V> entry) {
        if (entry == null) {
            return null;
        }
        return entry.key;
    }

    static <K> K key(Entry<K, ?> entry) {
        if (entry == null) {
            throw new NoSuchElementException();
        }
        return entry.key;
    }

    /* loaded from: rt.jar:java/util/TreeMap$NavigableSubMap.class */
    static abstract class NavigableSubMap<K, V> extends AbstractMap<K, V> implements NavigableMap<K, V>, Serializable {
        private static final long serialVersionUID = -2102997345730753016L;

        /* renamed from: m, reason: collision with root package name */
        final TreeMap<K, V> f12568m;
        final K lo;
        final K hi;
        final boolean fromStart;
        final boolean toEnd;
        final boolean loInclusive;
        final boolean hiInclusive;
        transient NavigableMap<K, V> descendingMapView;
        transient NavigableSubMap<K, V>.EntrySetView entrySetView;
        transient KeySet<K> navigableKeySetView;

        abstract Entry<K, V> subLowest();

        abstract Entry<K, V> subHighest();

        abstract Entry<K, V> subCeiling(K k2);

        abstract Entry<K, V> subHigher(K k2);

        abstract Entry<K, V> subFloor(K k2);

        abstract Entry<K, V> subLower(K k2);

        abstract Iterator<K> keyIterator();

        abstract Spliterator<K> keySpliterator();

        abstract Iterator<K> descendingKeyIterator();

        NavigableSubMap(TreeMap<K, V> treeMap, boolean z2, K k2, boolean z3, boolean z4, K k3, boolean z5) {
            if (!z2 && !z4) {
                if (treeMap.compare(k2, k3) > 0) {
                    throw new IllegalArgumentException("fromKey > toKey");
                }
            } else {
                if (!z2) {
                    treeMap.compare(k2, k2);
                }
                if (!z4) {
                    treeMap.compare(k3, k3);
                }
            }
            this.f12568m = treeMap;
            this.fromStart = z2;
            this.lo = k2;
            this.loInclusive = z3;
            this.toEnd = z4;
            this.hi = k3;
            this.hiInclusive = z5;
        }

        final boolean tooLow(Object obj) {
            if (!this.fromStart) {
                int iCompare = this.f12568m.compare(obj, this.lo);
                if (iCompare < 0) {
                    return true;
                }
                if (iCompare == 0 && !this.loInclusive) {
                    return true;
                }
                return false;
            }
            return false;
        }

        final boolean tooHigh(Object obj) {
            if (!this.toEnd) {
                int iCompare = this.f12568m.compare(obj, this.hi);
                if (iCompare > 0) {
                    return true;
                }
                if (iCompare == 0 && !this.hiInclusive) {
                    return true;
                }
                return false;
            }
            return false;
        }

        final boolean inRange(Object obj) {
            return (tooLow(obj) || tooHigh(obj)) ? false : true;
        }

        final boolean inClosedRange(Object obj) {
            return (this.fromStart || this.f12568m.compare(obj, this.lo) >= 0) && (this.toEnd || this.f12568m.compare(this.hi, obj) >= 0);
        }

        final boolean inRange(Object obj, boolean z2) {
            return z2 ? inRange(obj) : inClosedRange(obj);
        }

        final Entry<K, V> absLowest() {
            Entry<K, V> higherEntry;
            if (this.fromStart) {
                higherEntry = this.f12568m.getFirstEntry();
            } else if (this.loInclusive) {
                higherEntry = this.f12568m.getCeilingEntry(this.lo);
            } else {
                higherEntry = this.f12568m.getHigherEntry(this.lo);
            }
            Entry<K, V> entry = higherEntry;
            if (entry == null || tooHigh(entry.key)) {
                return null;
            }
            return entry;
        }

        final Entry<K, V> absHighest() {
            Entry<K, V> lowerEntry;
            if (this.toEnd) {
                lowerEntry = this.f12568m.getLastEntry();
            } else if (this.hiInclusive) {
                lowerEntry = this.f12568m.getFloorEntry(this.hi);
            } else {
                lowerEntry = this.f12568m.getLowerEntry(this.hi);
            }
            Entry<K, V> entry = lowerEntry;
            if (entry == null || tooLow(entry.key)) {
                return null;
            }
            return entry;
        }

        final Entry<K, V> absCeiling(K k2) {
            if (tooLow(k2)) {
                return absLowest();
            }
            Entry<K, V> ceilingEntry = this.f12568m.getCeilingEntry(k2);
            if (ceilingEntry == null || tooHigh(ceilingEntry.key)) {
                return null;
            }
            return ceilingEntry;
        }

        final Entry<K, V> absHigher(K k2) {
            if (tooLow(k2)) {
                return absLowest();
            }
            Entry<K, V> higherEntry = this.f12568m.getHigherEntry(k2);
            if (higherEntry == null || tooHigh(higherEntry.key)) {
                return null;
            }
            return higherEntry;
        }

        final Entry<K, V> absFloor(K k2) {
            if (tooHigh(k2)) {
                return absHighest();
            }
            Entry<K, V> floorEntry = this.f12568m.getFloorEntry(k2);
            if (floorEntry == null || tooLow(floorEntry.key)) {
                return null;
            }
            return floorEntry;
        }

        final Entry<K, V> absLower(K k2) {
            if (tooHigh(k2)) {
                return absHighest();
            }
            Entry<K, V> lowerEntry = this.f12568m.getLowerEntry(k2);
            if (lowerEntry == null || tooLow(lowerEntry.key)) {
                return null;
            }
            return lowerEntry;
        }

        final Entry<K, V> absHighFence() {
            if (this.toEnd) {
                return null;
            }
            if (this.hiInclusive) {
                return this.f12568m.getHigherEntry(this.hi);
            }
            return this.f12568m.getCeilingEntry(this.hi);
        }

        final Entry<K, V> absLowFence() {
            if (this.fromStart) {
                return null;
            }
            if (this.loInclusive) {
                return this.f12568m.getLowerEntry(this.lo);
            }
            return this.f12568m.getFloorEntry(this.lo);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean isEmpty() {
            return (this.fromStart && this.toEnd) ? this.f12568m.isEmpty() : entrySet().isEmpty();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public int size() {
            return (this.fromStart && this.toEnd) ? this.f12568m.size() : entrySet().size();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public final boolean containsKey(Object obj) {
            return inRange(obj) && this.f12568m.containsKey(obj);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public final V put(K k2, V v2) {
            if (!inRange(k2)) {
                throw new IllegalArgumentException("key out of range");
            }
            return this.f12568m.put(k2, v2);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public final V get(Object obj) {
            if (inRange(obj)) {
                return this.f12568m.get(obj);
            }
            return null;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public final V remove(Object obj) {
            if (inRange(obj)) {
                return this.f12568m.remove(obj);
            }
            return null;
        }

        @Override // java.util.NavigableMap
        public final Map.Entry<K, V> ceilingEntry(K k2) {
            return TreeMap.exportEntry(subCeiling(k2));
        }

        @Override // java.util.NavigableMap
        public final K ceilingKey(K k2) {
            return (K) TreeMap.keyOrNull(subCeiling(k2));
        }

        @Override // java.util.NavigableMap
        public final Map.Entry<K, V> higherEntry(K k2) {
            return TreeMap.exportEntry(subHigher(k2));
        }

        @Override // java.util.NavigableMap
        public final K higherKey(K k2) {
            return (K) TreeMap.keyOrNull(subHigher(k2));
        }

        @Override // java.util.NavigableMap
        public final Map.Entry<K, V> floorEntry(K k2) {
            return TreeMap.exportEntry(subFloor(k2));
        }

        @Override // java.util.NavigableMap
        public final K floorKey(K k2) {
            return (K) TreeMap.keyOrNull(subFloor(k2));
        }

        @Override // java.util.NavigableMap
        public final Map.Entry<K, V> lowerEntry(K k2) {
            return TreeMap.exportEntry(subLower(k2));
        }

        @Override // java.util.NavigableMap
        public final K lowerKey(K k2) {
            return (K) TreeMap.keyOrNull(subLower(k2));
        }

        @Override // java.util.SortedMap
        public final K firstKey() {
            return (K) TreeMap.key(subLowest());
        }

        @Override // java.util.SortedMap
        public final K lastKey() {
            return (K) TreeMap.key(subHighest());
        }

        @Override // java.util.NavigableMap
        public final Map.Entry<K, V> firstEntry() {
            return TreeMap.exportEntry(subLowest());
        }

        @Override // java.util.NavigableMap
        public final Map.Entry<K, V> lastEntry() {
            return TreeMap.exportEntry(subHighest());
        }

        @Override // java.util.NavigableMap
        public final Map.Entry<K, V> pollFirstEntry() {
            Entry<K, V> entrySubLowest = subLowest();
            Map.Entry<K, V> entryExportEntry = TreeMap.exportEntry(entrySubLowest);
            if (entrySubLowest != null) {
                this.f12568m.deleteEntry(entrySubLowest);
            }
            return entryExportEntry;
        }

        @Override // java.util.NavigableMap
        public final Map.Entry<K, V> pollLastEntry() {
            Entry<K, V> entrySubHighest = subHighest();
            Map.Entry<K, V> entryExportEntry = TreeMap.exportEntry(entrySubHighest);
            if (entrySubHighest != null) {
                this.f12568m.deleteEntry(entrySubHighest);
            }
            return entryExportEntry;
        }

        @Override // java.util.NavigableMap
        public final NavigableSet<K> navigableKeySet() {
            KeySet<K> keySet = this.navigableKeySetView;
            if (keySet != null) {
                return keySet;
            }
            KeySet<K> keySet2 = new KeySet<>(this);
            this.navigableKeySetView = keySet2;
            return keySet2;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public final Set<K> keySet() {
            return navigableKeySet();
        }

        @Override // java.util.NavigableMap
        public NavigableSet<K> descendingKeySet() {
            return descendingMap().navigableKeySet();
        }

        @Override // java.util.NavigableMap, java.util.SortedMap
        public final SortedMap<K, V> subMap(K k2, K k3) {
            return subMap(k2, true, k3, false);
        }

        @Override // java.util.NavigableMap, java.util.SortedMap
        public final SortedMap<K, V> headMap(K k2) {
            return headMap(k2, false);
        }

        @Override // java.util.NavigableMap, java.util.SortedMap
        public final SortedMap<K, V> tailMap(K k2) {
            return tailMap(k2, true);
        }

        /* loaded from: rt.jar:java/util/TreeMap$NavigableSubMap$EntrySetView.class */
        abstract class EntrySetView extends AbstractSet<Map.Entry<K, V>> {
            private transient int size = -1;
            private transient int sizeModCount;

            EntrySetView() {
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                if (NavigableSubMap.this.fromStart && NavigableSubMap.this.toEnd) {
                    return NavigableSubMap.this.f12568m.size();
                }
                if (this.size == -1 || this.sizeModCount != ((TreeMap) NavigableSubMap.this.f12568m).modCount) {
                    this.sizeModCount = ((TreeMap) NavigableSubMap.this.f12568m).modCount;
                    this.size = 0;
                    Iterator<Map.Entry<K, V>> it = iterator();
                    while (it.hasNext()) {
                        this.size++;
                        it.next();
                    }
                }
                return this.size;
            }

            @Override // java.util.AbstractCollection, java.util.Collection
            public boolean isEmpty() {
                Entry<K, V> entryAbsLowest = NavigableSubMap.this.absLowest();
                return entryAbsLowest == null || NavigableSubMap.this.tooHigh(entryAbsLowest.key);
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean contains(Object obj) {
                Entry<K, V> entry;
                if (!(obj instanceof Map.Entry)) {
                    return false;
                }
                Map.Entry entry2 = (Map.Entry) obj;
                Object key = entry2.getKey();
                return NavigableSubMap.this.inRange(key) && (entry = NavigableSubMap.this.f12568m.getEntry(key)) != null && TreeMap.valEquals(entry.getValue(), entry2.getValue());
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean remove(Object obj) {
                Entry<K, V> entry;
                if (!(obj instanceof Map.Entry)) {
                    return false;
                }
                Map.Entry entry2 = (Map.Entry) obj;
                Object key = entry2.getKey();
                if (NavigableSubMap.this.inRange(key) && (entry = NavigableSubMap.this.f12568m.getEntry(key)) != null && TreeMap.valEquals(entry.getValue(), entry2.getValue())) {
                    NavigableSubMap.this.f12568m.deleteEntry(entry);
                    return true;
                }
                return false;
            }
        }

        /* loaded from: rt.jar:java/util/TreeMap$NavigableSubMap$SubMapIterator.class */
        abstract class SubMapIterator<T> implements Iterator<T> {
            Entry<K, V> lastReturned = null;
            Entry<K, V> next;
            final Object fenceKey;
            int expectedModCount;

            SubMapIterator(Entry<K, V> entry, Entry<K, V> entry2) {
                this.expectedModCount = ((TreeMap) NavigableSubMap.this.f12568m).modCount;
                this.next = entry;
                this.fenceKey = entry2 == null ? TreeMap.UNBOUNDED : entry2.key;
            }

            @Override // java.util.Iterator
            public final boolean hasNext() {
                return (this.next == null || this.next.key == this.fenceKey) ? false : true;
            }

            final Entry<K, V> nextEntry() {
                Entry<K, V> entry = this.next;
                if (entry != null && entry.key != this.fenceKey) {
                    if (((TreeMap) NavigableSubMap.this.f12568m).modCount != this.expectedModCount) {
                        throw new ConcurrentModificationException();
                    }
                    this.next = TreeMap.successor(entry);
                    this.lastReturned = entry;
                    return entry;
                }
                throw new NoSuchElementException();
            }

            final Entry<K, V> prevEntry() {
                Entry<K, V> entry = this.next;
                if (entry != null && entry.key != this.fenceKey) {
                    if (((TreeMap) NavigableSubMap.this.f12568m).modCount != this.expectedModCount) {
                        throw new ConcurrentModificationException();
                    }
                    this.next = TreeMap.predecessor(entry);
                    this.lastReturned = entry;
                    return entry;
                }
                throw new NoSuchElementException();
            }

            final void removeAscending() {
                if (this.lastReturned != null) {
                    if (((TreeMap) NavigableSubMap.this.f12568m).modCount != this.expectedModCount) {
                        throw new ConcurrentModificationException();
                    }
                    if (this.lastReturned.left != null && this.lastReturned.right != null) {
                        this.next = this.lastReturned;
                    }
                    NavigableSubMap.this.f12568m.deleteEntry(this.lastReturned);
                    this.lastReturned = null;
                    this.expectedModCount = ((TreeMap) NavigableSubMap.this.f12568m).modCount;
                    return;
                }
                throw new IllegalStateException();
            }

            final void removeDescending() {
                if (this.lastReturned != null) {
                    if (((TreeMap) NavigableSubMap.this.f12568m).modCount == this.expectedModCount) {
                        NavigableSubMap.this.f12568m.deleteEntry(this.lastReturned);
                        this.lastReturned = null;
                        this.expectedModCount = ((TreeMap) NavigableSubMap.this.f12568m).modCount;
                        return;
                    }
                    throw new ConcurrentModificationException();
                }
                throw new IllegalStateException();
            }
        }

        /* loaded from: rt.jar:java/util/TreeMap$NavigableSubMap$SubMapEntryIterator.class */
        final class SubMapEntryIterator extends NavigableSubMap<K, V>.SubMapIterator<Map.Entry<K, V>> {
            SubMapEntryIterator(Entry<K, V> entry, Entry<K, V> entry2) {
                super(entry, entry2);
            }

            @Override // java.util.Iterator
            public Map.Entry<K, V> next() {
                return nextEntry();
            }

            @Override // java.util.Iterator
            public void remove() {
                removeAscending();
            }
        }

        /* loaded from: rt.jar:java/util/TreeMap$NavigableSubMap$DescendingSubMapEntryIterator.class */
        final class DescendingSubMapEntryIterator extends NavigableSubMap<K, V>.SubMapIterator<Map.Entry<K, V>> {
            DescendingSubMapEntryIterator(Entry<K, V> entry, Entry<K, V> entry2) {
                super(entry, entry2);
            }

            @Override // java.util.Iterator
            public Map.Entry<K, V> next() {
                return prevEntry();
            }

            @Override // java.util.Iterator
            public void remove() {
                removeDescending();
            }
        }

        /* loaded from: rt.jar:java/util/TreeMap$NavigableSubMap$SubMapKeyIterator.class */
        final class SubMapKeyIterator extends NavigableSubMap<K, V>.SubMapIterator<K> implements Spliterator<K> {
            SubMapKeyIterator(Entry<K, V> entry, Entry<K, V> entry2) {
                super(entry, entry2);
            }

            @Override // java.util.Iterator
            public K next() {
                return nextEntry().key;
            }

            @Override // java.util.Iterator
            public void remove() {
                removeAscending();
            }

            @Override // java.util.Spliterator
            public Spliterator<K> trySplit() {
                return null;
            }

            @Override // java.util.Iterator
            public void forEachRemaining(Consumer<? super K> consumer) {
                while (hasNext()) {
                    consumer.accept((Object) next());
                }
            }

            @Override // java.util.Spliterator
            public boolean tryAdvance(Consumer<? super K> consumer) {
                if (hasNext()) {
                    consumer.accept((Object) next());
                    return true;
                }
                return false;
            }

            @Override // java.util.Spliterator
            public long estimateSize() {
                return Long.MAX_VALUE;
            }

            @Override // java.util.Spliterator
            public int characteristics() {
                return 21;
            }

            @Override // java.util.Spliterator
            public final Comparator<? super K> getComparator() {
                return NavigableSubMap.this.comparator();
            }
        }

        /* loaded from: rt.jar:java/util/TreeMap$NavigableSubMap$DescendingSubMapKeyIterator.class */
        final class DescendingSubMapKeyIterator extends NavigableSubMap<K, V>.SubMapIterator<K> implements Spliterator<K> {
            DescendingSubMapKeyIterator(Entry<K, V> entry, Entry<K, V> entry2) {
                super(entry, entry2);
            }

            @Override // java.util.Iterator
            public K next() {
                return prevEntry().key;
            }

            @Override // java.util.Iterator
            public void remove() {
                removeDescending();
            }

            @Override // java.util.Spliterator
            public Spliterator<K> trySplit() {
                return null;
            }

            @Override // java.util.Iterator
            public void forEachRemaining(Consumer<? super K> consumer) {
                while (hasNext()) {
                    consumer.accept((Object) next());
                }
            }

            @Override // java.util.Spliterator
            public boolean tryAdvance(Consumer<? super K> consumer) {
                if (hasNext()) {
                    consumer.accept((Object) next());
                    return true;
                }
                return false;
            }

            @Override // java.util.Spliterator
            public long estimateSize() {
                return Long.MAX_VALUE;
            }

            @Override // java.util.Spliterator
            public int characteristics() {
                return 17;
            }
        }
    }

    /* loaded from: rt.jar:java/util/TreeMap$AscendingSubMap.class */
    static final class AscendingSubMap<K, V> extends NavigableSubMap<K, V> {
        private static final long serialVersionUID = 912986545866124060L;

        AscendingSubMap(TreeMap<K, V> treeMap, boolean z2, K k2, boolean z3, boolean z4, K k3, boolean z5) {
            super(treeMap, z2, k2, z3, z4, k3, z5);
        }

        @Override // java.util.SortedMap
        public Comparator<? super K> comparator() {
            return this.f12568m.comparator();
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> subMap(K k2, boolean z2, K k3, boolean z3) {
            if (!inRange(k2, z2)) {
                throw new IllegalArgumentException("fromKey out of range");
            }
            if (!inRange(k3, z3)) {
                throw new IllegalArgumentException("toKey out of range");
            }
            return new AscendingSubMap(this.f12568m, false, k2, z2, false, k3, z3);
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> headMap(K k2, boolean z2) {
            if (!inRange(k2, z2)) {
                throw new IllegalArgumentException("toKey out of range");
            }
            return new AscendingSubMap(this.f12568m, this.fromStart, this.lo, this.loInclusive, false, k2, z2);
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> tailMap(K k2, boolean z2) {
            if (!inRange(k2, z2)) {
                throw new IllegalArgumentException("fromKey out of range");
            }
            return new AscendingSubMap(this.f12568m, false, k2, z2, this.toEnd, this.hi, this.hiInclusive);
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> descendingMap() {
            NavigableMap<K, V> navigableMap = this.descendingMapView;
            if (navigableMap != null) {
                return navigableMap;
            }
            DescendingSubMap descendingSubMap = new DescendingSubMap(this.f12568m, this.fromStart, this.lo, this.loInclusive, this.toEnd, this.hi, this.hiInclusive);
            this.descendingMapView = descendingSubMap;
            return descendingSubMap;
        }

        @Override // java.util.TreeMap.NavigableSubMap
        Iterator<K> keyIterator() {
            return new NavigableSubMap.SubMapKeyIterator(absLowest(), absHighFence());
        }

        @Override // java.util.TreeMap.NavigableSubMap
        Spliterator<K> keySpliterator() {
            return new NavigableSubMap.SubMapKeyIterator(absLowest(), absHighFence());
        }

        @Override // java.util.TreeMap.NavigableSubMap
        Iterator<K> descendingKeyIterator() {
            return new NavigableSubMap.DescendingSubMapKeyIterator(absHighest(), absLowFence());
        }

        /* loaded from: rt.jar:java/util/TreeMap$AscendingSubMap$AscendingEntrySetView.class */
        final class AscendingEntrySetView extends NavigableSubMap<K, V>.EntrySetView {
            AscendingEntrySetView() {
                super();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
            public Iterator<Map.Entry<K, V>> iterator() {
                return new NavigableSubMap.SubMapEntryIterator(AscendingSubMap.this.absLowest(), AscendingSubMap.this.absHighFence());
            }
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<Map.Entry<K, V>> entrySet() {
            NavigableSubMap<K, V>.EntrySetView entrySetView = this.entrySetView;
            if (entrySetView != null) {
                return entrySetView;
            }
            AscendingEntrySetView ascendingEntrySetView = new AscendingEntrySetView();
            this.entrySetView = ascendingEntrySetView;
            return ascendingEntrySetView;
        }

        @Override // java.util.TreeMap.NavigableSubMap
        Entry<K, V> subLowest() {
            return absLowest();
        }

        @Override // java.util.TreeMap.NavigableSubMap
        Entry<K, V> subHighest() {
            return absHighest();
        }

        @Override // java.util.TreeMap.NavigableSubMap
        Entry<K, V> subCeiling(K k2) {
            return absCeiling(k2);
        }

        @Override // java.util.TreeMap.NavigableSubMap
        Entry<K, V> subHigher(K k2) {
            return absHigher(k2);
        }

        @Override // java.util.TreeMap.NavigableSubMap
        Entry<K, V> subFloor(K k2) {
            return absFloor(k2);
        }

        @Override // java.util.TreeMap.NavigableSubMap
        Entry<K, V> subLower(K k2) {
            return absLower(k2);
        }
    }

    /* loaded from: rt.jar:java/util/TreeMap$DescendingSubMap.class */
    static final class DescendingSubMap<K, V> extends NavigableSubMap<K, V> {
        private static final long serialVersionUID = 912986545866120460L;
        private final Comparator<? super K> reverseComparator;

        DescendingSubMap(TreeMap<K, V> treeMap, boolean z2, K k2, boolean z3, boolean z4, K k3, boolean z5) {
            super(treeMap, z2, k2, z3, z4, k3, z5);
            this.reverseComparator = Collections.reverseOrder(((TreeMap) this.f12568m).comparator);
        }

        @Override // java.util.SortedMap
        public Comparator<? super K> comparator() {
            return this.reverseComparator;
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> subMap(K k2, boolean z2, K k3, boolean z3) {
            if (!inRange(k2, z2)) {
                throw new IllegalArgumentException("fromKey out of range");
            }
            if (!inRange(k3, z3)) {
                throw new IllegalArgumentException("toKey out of range");
            }
            return new DescendingSubMap(this.f12568m, false, k3, z3, false, k2, z2);
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> headMap(K k2, boolean z2) {
            if (!inRange(k2, z2)) {
                throw new IllegalArgumentException("toKey out of range");
            }
            return new DescendingSubMap(this.f12568m, false, k2, z2, this.toEnd, this.hi, this.hiInclusive);
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> tailMap(K k2, boolean z2) {
            if (!inRange(k2, z2)) {
                throw new IllegalArgumentException("fromKey out of range");
            }
            return new DescendingSubMap(this.f12568m, this.fromStart, this.lo, this.loInclusive, false, k2, z2);
        }

        @Override // java.util.NavigableMap
        public NavigableMap<K, V> descendingMap() {
            NavigableMap<K, V> navigableMap = this.descendingMapView;
            if (navigableMap != null) {
                return navigableMap;
            }
            AscendingSubMap ascendingSubMap = new AscendingSubMap(this.f12568m, this.fromStart, this.lo, this.loInclusive, this.toEnd, this.hi, this.hiInclusive);
            this.descendingMapView = ascendingSubMap;
            return ascendingSubMap;
        }

        @Override // java.util.TreeMap.NavigableSubMap
        Iterator<K> keyIterator() {
            return new NavigableSubMap.DescendingSubMapKeyIterator(absHighest(), absLowFence());
        }

        @Override // java.util.TreeMap.NavigableSubMap
        Spliterator<K> keySpliterator() {
            return new NavigableSubMap.DescendingSubMapKeyIterator(absHighest(), absLowFence());
        }

        @Override // java.util.TreeMap.NavigableSubMap
        Iterator<K> descendingKeyIterator() {
            return new NavigableSubMap.SubMapKeyIterator(absLowest(), absHighFence());
        }

        /* loaded from: rt.jar:java/util/TreeMap$DescendingSubMap$DescendingEntrySetView.class */
        final class DescendingEntrySetView extends NavigableSubMap<K, V>.EntrySetView {
            DescendingEntrySetView() {
                super();
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
            public Iterator<Map.Entry<K, V>> iterator() {
                return new NavigableSubMap.DescendingSubMapEntryIterator(DescendingSubMap.this.absHighest(), DescendingSubMap.this.absLowFence());
            }
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<Map.Entry<K, V>> entrySet() {
            NavigableSubMap<K, V>.EntrySetView entrySetView = this.entrySetView;
            if (entrySetView != null) {
                return entrySetView;
            }
            DescendingEntrySetView descendingEntrySetView = new DescendingEntrySetView();
            this.entrySetView = descendingEntrySetView;
            return descendingEntrySetView;
        }

        @Override // java.util.TreeMap.NavigableSubMap
        Entry<K, V> subLowest() {
            return absHighest();
        }

        @Override // java.util.TreeMap.NavigableSubMap
        Entry<K, V> subHighest() {
            return absLowest();
        }

        @Override // java.util.TreeMap.NavigableSubMap
        Entry<K, V> subCeiling(K k2) {
            return absFloor(k2);
        }

        @Override // java.util.TreeMap.NavigableSubMap
        Entry<K, V> subHigher(K k2) {
            return absLower(k2);
        }

        @Override // java.util.TreeMap.NavigableSubMap
        Entry<K, V> subFloor(K k2) {
            return absCeiling(k2);
        }

        @Override // java.util.TreeMap.NavigableSubMap
        Entry<K, V> subLower(K k2) {
            return absHigher(k2);
        }
    }

    /* loaded from: rt.jar:java/util/TreeMap$SubMap.class */
    private class SubMap extends AbstractMap<K, V> implements SortedMap<K, V>, Serializable {
        private static final long serialVersionUID = -6520786458950516097L;
        private boolean fromStart = false;
        private boolean toEnd = false;
        private K fromKey;
        private K toKey;

        private SubMap() {
        }

        private Object readResolve() {
            return new AscendingSubMap(TreeMap.this, this.fromStart, this.fromKey, true, this.toEnd, this.toKey, false);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<Map.Entry<K, V>> entrySet() {
            throw new InternalError();
        }

        @Override // java.util.SortedMap
        public K lastKey() {
            throw new InternalError();
        }

        @Override // java.util.SortedMap
        public K firstKey() {
            throw new InternalError();
        }

        @Override // java.util.SortedMap
        public SortedMap<K, V> subMap(K k2, K k3) {
            throw new InternalError();
        }

        @Override // java.util.SortedMap
        public SortedMap<K, V> headMap(K k2) {
            throw new InternalError();
        }

        @Override // java.util.SortedMap
        public SortedMap<K, V> tailMap(K k2) {
            throw new InternalError();
        }

        @Override // java.util.SortedMap
        public Comparator<? super K> comparator() {
            throw new InternalError();
        }
    }

    /* loaded from: rt.jar:java/util/TreeMap$Entry.class */
    static final class Entry<K, V> implements Map.Entry<K, V> {
        K key;
        V value;
        Entry<K, V> left;
        Entry<K, V> right;
        Entry<K, V> parent;
        boolean color = true;

        Entry(K k2, V v2, Entry<K, V> entry) {
            this.key = k2;
            this.value = v2;
            this.parent = entry;
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            return this.key;
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            return this.value;
        }

        @Override // java.util.Map.Entry
        public V setValue(V v2) {
            V v3 = this.value;
            this.value = v2;
            return v3;
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            return TreeMap.valEquals(this.key, entry.getKey()) && TreeMap.valEquals(this.value, entry.getValue());
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return (this.key == null ? 0 : this.key.hashCode()) ^ (this.value == null ? 0 : this.value.hashCode());
        }

        public String toString() {
            return ((Object) this.key) + "=" + ((Object) this.value);
        }
    }

    final Entry<K, V> getFirstEntry() {
        Entry<K, V> entry = this.root;
        if (entry != null) {
            while (entry.left != null) {
                entry = entry.left;
            }
        }
        return entry;
    }

    final Entry<K, V> getLastEntry() {
        Entry<K, V> entry = this.root;
        if (entry != null) {
            while (entry.right != null) {
                entry = entry.right;
            }
        }
        return entry;
    }

    static <K, V> Entry<K, V> successor(Entry<K, V> entry) {
        if (entry == null) {
            return null;
        }
        if (entry.right != null) {
            Entry<K, V> entry2 = entry.right;
            while (true) {
                Entry<K, V> entry3 = entry2;
                if (entry3.left != null) {
                    entry2 = entry3.left;
                } else {
                    return entry3;
                }
            }
        } else {
            Entry<K, V> entry4 = entry.parent;
            Entry<K, V> entry5 = entry;
            while (entry4 != null && entry5 == entry4.right) {
                entry5 = entry4;
                entry4 = entry4.parent;
            }
            return entry4;
        }
    }

    static <K, V> Entry<K, V> predecessor(Entry<K, V> entry) {
        if (entry == null) {
            return null;
        }
        if (entry.left != null) {
            Entry<K, V> entry2 = entry.left;
            while (true) {
                Entry<K, V> entry3 = entry2;
                if (entry3.right != null) {
                    entry2 = entry3.right;
                } else {
                    return entry3;
                }
            }
        } else {
            Entry<K, V> entry4 = entry.parent;
            Entry<K, V> entry5 = entry;
            while (entry4 != null && entry5 == entry4.left) {
                entry5 = entry4;
                entry4 = entry4.parent;
            }
            return entry4;
        }
    }

    private static <K, V> boolean colorOf(Entry<K, V> entry) {
        if (entry == null) {
            return true;
        }
        return entry.color;
    }

    private static <K, V> Entry<K, V> parentOf(Entry<K, V> entry) {
        if (entry == null) {
            return null;
        }
        return entry.parent;
    }

    private static <K, V> void setColor(Entry<K, V> entry, boolean z2) {
        if (entry != null) {
            entry.color = z2;
        }
    }

    private static <K, V> Entry<K, V> leftOf(Entry<K, V> entry) {
        if (entry == null) {
            return null;
        }
        return entry.left;
    }

    private static <K, V> Entry<K, V> rightOf(Entry<K, V> entry) {
        if (entry == null) {
            return null;
        }
        return entry.right;
    }

    private void rotateLeft(Entry<K, V> entry) {
        if (entry != null) {
            Entry<K, V> entry2 = entry.right;
            entry.right = entry2.left;
            if (entry2.left != null) {
                entry2.left.parent = entry;
            }
            entry2.parent = entry.parent;
            if (entry.parent == null) {
                this.root = entry2;
            } else if (entry.parent.left == entry) {
                entry.parent.left = entry2;
            } else {
                entry.parent.right = entry2;
            }
            entry2.left = entry;
            entry.parent = entry2;
        }
    }

    private void rotateRight(Entry<K, V> entry) {
        if (entry != null) {
            Entry<K, V> entry2 = entry.left;
            entry.left = entry2.right;
            if (entry2.right != null) {
                entry2.right.parent = entry;
            }
            entry2.parent = entry.parent;
            if (entry.parent == null) {
                this.root = entry2;
            } else if (entry.parent.right == entry) {
                entry.parent.right = entry2;
            } else {
                entry.parent.left = entry2;
            }
            entry2.right = entry;
            entry.parent = entry2;
        }
    }

    private void fixAfterInsertion(Entry<K, V> entry) {
        entry.color = false;
        while (entry != null && entry != this.root && !entry.parent.color) {
            if (parentOf(entry) == leftOf(parentOf(parentOf(entry)))) {
                Entry entryRightOf = rightOf(parentOf(parentOf(entry)));
                if (!colorOf(entryRightOf)) {
                    setColor(parentOf(entry), true);
                    setColor(entryRightOf, true);
                    setColor(parentOf(parentOf(entry)), false);
                    entry = parentOf(parentOf(entry));
                } else {
                    if (entry == rightOf(parentOf(entry))) {
                        entry = parentOf(entry);
                        rotateLeft(entry);
                    }
                    setColor(parentOf(entry), true);
                    setColor(parentOf(parentOf(entry)), false);
                    rotateRight(parentOf(parentOf(entry)));
                }
            } else {
                Entry entryLeftOf = leftOf(parentOf(parentOf(entry)));
                if (!colorOf(entryLeftOf)) {
                    setColor(parentOf(entry), true);
                    setColor(entryLeftOf, true);
                    setColor(parentOf(parentOf(entry)), false);
                    entry = parentOf(parentOf(entry));
                } else {
                    if (entry == leftOf(parentOf(entry))) {
                        entry = parentOf(entry);
                        rotateRight(entry);
                    }
                    setColor(parentOf(entry), true);
                    setColor(parentOf(parentOf(entry)), false);
                    rotateLeft(parentOf(parentOf(entry)));
                }
            }
        }
        this.root.color = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deleteEntry(Entry<K, V> entry) {
        this.modCount++;
        this.size--;
        if (entry.left != null && entry.right != null) {
            Entry<K, V> entrySuccessor = successor(entry);
            entry.key = entrySuccessor.key;
            entry.value = entrySuccessor.value;
            entry = entrySuccessor;
        }
        Entry<K, V> entry2 = entry.left != null ? entry.left : entry.right;
        if (entry2 != null) {
            entry2.parent = entry.parent;
            if (entry.parent == null) {
                this.root = entry2;
            } else if (entry == entry.parent.left) {
                entry.parent.left = entry2;
            } else {
                entry.parent.right = entry2;
            }
            entry.parent = null;
            entry.right = null;
            entry.left = null;
            if (entry.color) {
                fixAfterDeletion(entry2);
                return;
            }
            return;
        }
        if (entry.parent == null) {
            this.root = null;
            return;
        }
        if (entry.color) {
            fixAfterDeletion(entry);
        }
        if (entry.parent != null) {
            if (entry == entry.parent.left) {
                entry.parent.left = null;
            } else if (entry == entry.parent.right) {
                entry.parent.right = null;
            }
            entry.parent = null;
        }
    }

    private void fixAfterDeletion(Entry<K, V> entry) {
        while (entry != this.root && colorOf(entry)) {
            if (entry == leftOf(parentOf(entry))) {
                Entry<K, V> entryRightOf = rightOf(parentOf(entry));
                if (!colorOf(entryRightOf)) {
                    setColor(entryRightOf, true);
                    setColor(parentOf(entry), false);
                    rotateLeft(parentOf(entry));
                    entryRightOf = rightOf(parentOf(entry));
                }
                if (colorOf(leftOf(entryRightOf)) && colorOf(rightOf(entryRightOf))) {
                    setColor(entryRightOf, false);
                    entry = parentOf(entry);
                } else {
                    if (colorOf(rightOf(entryRightOf))) {
                        setColor(leftOf(entryRightOf), true);
                        setColor(entryRightOf, false);
                        rotateRight(entryRightOf);
                        entryRightOf = rightOf(parentOf(entry));
                    }
                    setColor(entryRightOf, colorOf(parentOf(entry)));
                    setColor(parentOf(entry), true);
                    setColor(rightOf(entryRightOf), true);
                    rotateLeft(parentOf(entry));
                    entry = this.root;
                }
            } else {
                Entry<K, V> entryLeftOf = leftOf(parentOf(entry));
                if (!colorOf(entryLeftOf)) {
                    setColor(entryLeftOf, true);
                    setColor(parentOf(entry), false);
                    rotateRight(parentOf(entry));
                    entryLeftOf = leftOf(parentOf(entry));
                }
                if (colorOf(rightOf(entryLeftOf)) && colorOf(leftOf(entryLeftOf))) {
                    setColor(entryLeftOf, false);
                    entry = parentOf(entry);
                } else {
                    if (colorOf(leftOf(entryLeftOf))) {
                        setColor(rightOf(entryLeftOf), true);
                        setColor(entryLeftOf, false);
                        rotateLeft(entryLeftOf);
                        entryLeftOf = leftOf(parentOf(entry));
                    }
                    setColor(entryLeftOf, colorOf(parentOf(entry)));
                    setColor(parentOf(entry), true);
                    setColor(leftOf(entryLeftOf), true);
                    rotateRight(parentOf(entry));
                    entry = this.root;
                }
            }
        }
        setColor(entry, true);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(this.size);
        for (Map.Entry<K, V> entry : entrySet()) {
            objectOutputStream.writeObject(entry.getKey());
            objectOutputStream.writeObject(entry.getValue());
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        buildFromSorted(objectInputStream.readInt(), null, objectInputStream, null);
    }

    void readTreeSet(int i2, ObjectInputStream objectInputStream, V v2) throws IOException, ClassNotFoundException {
        buildFromSorted(i2, null, objectInputStream, v2);
    }

    void addAllForTreeSet(SortedSet<? extends K> sortedSet, V v2) {
        try {
            buildFromSorted(sortedSet.size(), sortedSet.iterator(), null, v2);
        } catch (IOException e2) {
        } catch (ClassNotFoundException e3) {
        }
    }

    private void buildFromSorted(int i2, Iterator<?> it, ObjectInputStream objectInputStream, V v2) throws IOException, ClassNotFoundException {
        this.size = i2;
        this.root = buildFromSorted(0, 0, i2 - 1, computeRedLevel(i2), it, objectInputStream, v2);
    }

    private final Entry<K, V> buildFromSorted(int i2, int i3, int i4, int i5, Iterator<?> it, ObjectInputStream objectInputStream, V v2) throws IOException, ClassNotFoundException {
        Object object;
        Object object2;
        if (i4 < i3) {
            return null;
        }
        int i6 = (i3 + i4) >>> 1;
        Entry<K, V> entryBuildFromSorted = null;
        if (i3 < i6) {
            entryBuildFromSorted = buildFromSorted(i2 + 1, i3, i6 - 1, i5, it, objectInputStream, v2);
        }
        if (it != null) {
            if (v2 == null) {
                Map.Entry entry = (Map.Entry) it.next();
                object = entry.getKey();
                object2 = entry.getValue();
            } else {
                object = it.next();
                object2 = v2;
            }
        } else {
            object = objectInputStream.readObject();
            object2 = v2 != null ? v2 : objectInputStream.readObject();
        }
        Entry<K, V> entry2 = new Entry<>(object, object2, null);
        if (i2 == i5) {
            entry2.color = false;
        }
        if (entryBuildFromSorted != null) {
            entry2.left = entryBuildFromSorted;
            entryBuildFromSorted.parent = entry2;
        }
        if (i6 < i4) {
            Entry<K, V> entryBuildFromSorted2 = buildFromSorted(i2 + 1, i6 + 1, i4, i5, it, objectInputStream, v2);
            entry2.right = entryBuildFromSorted2;
            entryBuildFromSorted2.parent = entry2;
        }
        return entry2;
    }

    private static int computeRedLevel(int i2) {
        int i3 = 0;
        int i4 = i2;
        while (true) {
            int i5 = i4 - 1;
            if (i5 >= 0) {
                i3++;
                i4 = i5 / 2;
            } else {
                return i3;
            }
        }
    }

    static <K> Spliterator<K> keySpliteratorFor(NavigableMap<K, ?> navigableMap) {
        if (navigableMap instanceof TreeMap) {
            return ((TreeMap) navigableMap).keySpliterator();
        }
        if (navigableMap instanceof DescendingSubMap) {
            DescendingSubMap descendingSubMap = (DescendingSubMap) navigableMap;
            TreeMap<K, V> treeMap = descendingSubMap.f12568m;
            if (descendingSubMap == ((TreeMap) treeMap).descendingMap) {
                return treeMap.descendingKeySpliterator();
            }
        }
        return ((NavigableSubMap) navigableMap).keySpliterator();
    }

    final Spliterator<K> keySpliterator() {
        return new KeySpliterator(this, null, null, 0, -1, 0);
    }

    final Spliterator<K> descendingKeySpliterator() {
        return new DescendingKeySpliterator(this, null, null, 0, -2, 0);
    }

    /* loaded from: rt.jar:java/util/TreeMap$TreeMapSpliterator.class */
    static class TreeMapSpliterator<K, V> {
        final TreeMap<K, V> tree;
        Entry<K, V> current;
        Entry<K, V> fence;
        int side;
        int est;
        int expectedModCount;

        TreeMapSpliterator(TreeMap<K, V> treeMap, Entry<K, V> entry, Entry<K, V> entry2, int i2, int i3, int i4) {
            this.tree = treeMap;
            this.current = entry;
            this.fence = entry2;
            this.side = i2;
            this.est = i3;
            this.expectedModCount = i4;
        }

        final int getEstimate() {
            int i2 = this.est;
            int i3 = i2;
            if (i2 < 0) {
                TreeMap<K, V> treeMap = this.tree;
                if (treeMap != null) {
                    this.current = i3 == -1 ? treeMap.getFirstEntry() : treeMap.getLastEntry();
                    int i4 = ((TreeMap) treeMap).size;
                    this.est = i4;
                    i3 = i4;
                    this.expectedModCount = ((TreeMap) treeMap).modCount;
                } else {
                    this.est = 0;
                    i3 = 0;
                }
            }
            return i3;
        }

        public final long estimateSize() {
            return getEstimate();
        }
    }

    /* loaded from: rt.jar:java/util/TreeMap$KeySpliterator.class */
    static final class KeySpliterator<K, V> extends TreeMapSpliterator<K, V> implements Spliterator<K> {
        KeySpliterator(TreeMap<K, V> treeMap, Entry<K, V> entry, Entry<K, V> entry2, int i2, int i3, int i4) {
            super(treeMap, entry, entry2, i2, i3, i4);
        }

        @Override // java.util.Spliterator
        public KeySpliterator<K, V> trySplit() {
            Entry<K, V> entry;
            if (this.est < 0) {
                getEstimate();
            }
            int i2 = this.side;
            Entry<K, V> entry2 = this.current;
            Entry<K, V> entry3 = this.fence;
            if (entry2 == null || entry2 == entry3) {
                entry = null;
            } else if (i2 == 0) {
                entry = ((TreeMap) this.tree).root;
            } else {
                entry = i2 > 0 ? entry2.right : (i2 >= 0 || entry3 == null) ? null : entry3.left;
            }
            Entry<K, V> entry4 = entry;
            if (entry4 != null && entry4 != entry2 && entry4 != entry3 && this.tree.compare(entry2.key, entry4.key) < 0) {
                this.side = 1;
                TreeMap<K, V> treeMap = this.tree;
                this.current = entry4;
                int i3 = this.est >>> 1;
                this.est = i3;
                return new KeySpliterator<>(treeMap, entry2, entry4, -1, i3, this.expectedModCount);
            }
            return null;
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super K> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            if (this.est < 0) {
                getEstimate();
            }
            Entry<K, V> entry = this.fence;
            Entry<K, V> entry2 = this.current;
            Entry<K, V> entry3 = entry2;
            if (entry2 != null && entry3 != entry) {
                this.current = entry;
                do {
                    consumer.accept(entry3.key);
                    Entry<K, V> entry4 = entry3.right;
                    Entry<K, V> entry5 = entry4;
                    if (entry4 != null) {
                        while (true) {
                            Entry<K, V> entry6 = entry5.left;
                            if (entry6 == null) {
                                break;
                            } else {
                                entry5 = entry6;
                            }
                        }
                    } else {
                        while (true) {
                            Entry<K, V> entry7 = entry3.parent;
                            entry5 = entry7;
                            if (entry7 == null || entry3 != entry5.right) {
                                break;
                            } else {
                                entry3 = entry5;
                            }
                        }
                    }
                    Entry<K, V> entry8 = entry5;
                    entry3 = entry8;
                    if (entry8 == null) {
                        break;
                    }
                } while (entry3 != entry);
                if (((TreeMap) this.tree).modCount != this.expectedModCount) {
                    throw new ConcurrentModificationException();
                }
            }
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super K> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            if (this.est < 0) {
                getEstimate();
            }
            Entry<K, V> entry = this.current;
            if (entry == null || entry == this.fence) {
                return false;
            }
            this.current = TreeMap.successor(entry);
            consumer.accept(entry.key);
            if (((TreeMap) this.tree).modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            return true;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return (this.side == 0 ? 64 : 0) | 1 | 4 | 16;
        }

        @Override // java.util.Spliterator
        public final Comparator<? super K> getComparator() {
            return ((TreeMap) this.tree).comparator;
        }
    }

    /* loaded from: rt.jar:java/util/TreeMap$DescendingKeySpliterator.class */
    static final class DescendingKeySpliterator<K, V> extends TreeMapSpliterator<K, V> implements Spliterator<K> {
        DescendingKeySpliterator(TreeMap<K, V> treeMap, Entry<K, V> entry, Entry<K, V> entry2, int i2, int i3, int i4) {
            super(treeMap, entry, entry2, i2, i3, i4);
        }

        @Override // java.util.Spliterator
        public DescendingKeySpliterator<K, V> trySplit() {
            Entry<K, V> entry;
            if (this.est < 0) {
                getEstimate();
            }
            int i2 = this.side;
            Entry<K, V> entry2 = this.current;
            Entry<K, V> entry3 = this.fence;
            if (entry2 == null || entry2 == entry3) {
                entry = null;
            } else if (i2 == 0) {
                entry = ((TreeMap) this.tree).root;
            } else {
                entry = i2 < 0 ? entry2.left : (i2 <= 0 || entry3 == null) ? null : entry3.right;
            }
            Entry<K, V> entry4 = entry;
            if (entry4 != null && entry4 != entry2 && entry4 != entry3 && this.tree.compare(entry2.key, entry4.key) > 0) {
                this.side = 1;
                TreeMap<K, V> treeMap = this.tree;
                this.current = entry4;
                int i3 = this.est >>> 1;
                this.est = i3;
                return new DescendingKeySpliterator<>(treeMap, entry2, entry4, -1, i3, this.expectedModCount);
            }
            return null;
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super K> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            if (this.est < 0) {
                getEstimate();
            }
            Entry<K, V> entry = this.fence;
            Entry<K, V> entry2 = this.current;
            Entry<K, V> entry3 = entry2;
            if (entry2 != null && entry3 != entry) {
                this.current = entry;
                do {
                    consumer.accept(entry3.key);
                    Entry<K, V> entry4 = entry3.left;
                    Entry<K, V> entry5 = entry4;
                    if (entry4 != null) {
                        while (true) {
                            Entry<K, V> entry6 = entry5.right;
                            if (entry6 == null) {
                                break;
                            } else {
                                entry5 = entry6;
                            }
                        }
                    } else {
                        while (true) {
                            Entry<K, V> entry7 = entry3.parent;
                            entry5 = entry7;
                            if (entry7 == null || entry3 != entry5.left) {
                                break;
                            } else {
                                entry3 = entry5;
                            }
                        }
                    }
                    Entry<K, V> entry8 = entry5;
                    entry3 = entry8;
                    if (entry8 == null) {
                        break;
                    }
                } while (entry3 != entry);
                if (((TreeMap) this.tree).modCount != this.expectedModCount) {
                    throw new ConcurrentModificationException();
                }
            }
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super K> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            if (this.est < 0) {
                getEstimate();
            }
            Entry<K, V> entry = this.current;
            if (entry == null || entry == this.fence) {
                return false;
            }
            this.current = TreeMap.predecessor(entry);
            consumer.accept(entry.key);
            if (((TreeMap) this.tree).modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            return true;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return (this.side == 0 ? 64 : 0) | 1 | 16;
        }
    }

    /* loaded from: rt.jar:java/util/TreeMap$ValueSpliterator.class */
    static final class ValueSpliterator<K, V> extends TreeMapSpliterator<K, V> implements Spliterator<V> {
        ValueSpliterator(TreeMap<K, V> treeMap, Entry<K, V> entry, Entry<K, V> entry2, int i2, int i3, int i4) {
            super(treeMap, entry, entry2, i2, i3, i4);
        }

        @Override // java.util.Spliterator
        public ValueSpliterator<K, V> trySplit() {
            Entry<K, V> entry;
            if (this.est < 0) {
                getEstimate();
            }
            int i2 = this.side;
            Entry<K, V> entry2 = this.current;
            Entry<K, V> entry3 = this.fence;
            if (entry2 == null || entry2 == entry3) {
                entry = null;
            } else if (i2 == 0) {
                entry = ((TreeMap) this.tree).root;
            } else {
                entry = i2 > 0 ? entry2.right : (i2 >= 0 || entry3 == null) ? null : entry3.left;
            }
            Entry<K, V> entry4 = entry;
            if (entry4 != null && entry4 != entry2 && entry4 != entry3 && this.tree.compare(entry2.key, entry4.key) < 0) {
                this.side = 1;
                TreeMap<K, V> treeMap = this.tree;
                this.current = entry4;
                int i3 = this.est >>> 1;
                this.est = i3;
                return new ValueSpliterator<>(treeMap, entry2, entry4, -1, i3, this.expectedModCount);
            }
            return null;
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super V> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            if (this.est < 0) {
                getEstimate();
            }
            Entry<K, V> entry = this.fence;
            Entry<K, V> entry2 = this.current;
            Entry<K, V> entry3 = entry2;
            if (entry2 != null && entry3 != entry) {
                this.current = entry;
                do {
                    consumer.accept(entry3.value);
                    Entry<K, V> entry4 = entry3.right;
                    Entry<K, V> entry5 = entry4;
                    if (entry4 != null) {
                        while (true) {
                            Entry<K, V> entry6 = entry5.left;
                            if (entry6 == null) {
                                break;
                            } else {
                                entry5 = entry6;
                            }
                        }
                    } else {
                        while (true) {
                            Entry<K, V> entry7 = entry3.parent;
                            entry5 = entry7;
                            if (entry7 == null || entry3 != entry5.right) {
                                break;
                            } else {
                                entry3 = entry5;
                            }
                        }
                    }
                    Entry<K, V> entry8 = entry5;
                    entry3 = entry8;
                    if (entry8 == null) {
                        break;
                    }
                } while (entry3 != entry);
                if (((TreeMap) this.tree).modCount != this.expectedModCount) {
                    throw new ConcurrentModificationException();
                }
            }
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super V> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            if (this.est < 0) {
                getEstimate();
            }
            Entry<K, V> entry = this.current;
            if (entry == null || entry == this.fence) {
                return false;
            }
            this.current = TreeMap.successor(entry);
            consumer.accept(entry.value);
            if (((TreeMap) this.tree).modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            return true;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return (this.side == 0 ? 64 : 0) | 16;
        }
    }

    /* loaded from: rt.jar:java/util/TreeMap$EntrySpliterator.class */
    static final class EntrySpliterator<K, V> extends TreeMapSpliterator<K, V> implements Spliterator<Map.Entry<K, V>> {
        private static /* synthetic */ Object $deserializeLambda$(SerializedLambda serializedLambda) {
            switch (serializedLambda.getImplMethodName()) {
                case "lambda$getComparator$d5a01062$1":
                    if (serializedLambda.getImplMethodKind() == 6 && serializedLambda.getFunctionalInterfaceClass().equals("java/util/Comparator") && serializedLambda.getFunctionalInterfaceMethodName().equals("compare") && serializedLambda.getFunctionalInterfaceMethodSignature().equals("(Ljava/lang/Object;Ljava/lang/Object;)I") && serializedLambda.getImplClass().equals("java/util/TreeMap$EntrySpliterator") && serializedLambda.getImplMethodSignature().equals("(Ljava/util/Map$Entry;Ljava/util/Map$Entry;)I")) {
                        return (entry, entry2) -> {
                            return ((Comparable) entry.getKey()).compareTo(entry2.getKey());
                        };
                    }
                    break;
            }
            throw new IllegalArgumentException("Invalid lambda deserialization");
        }

        EntrySpliterator(TreeMap<K, V> treeMap, Entry<K, V> entry, Entry<K, V> entry2, int i2, int i3, int i4) {
            super(treeMap, entry, entry2, i2, i3, i4);
        }

        @Override // java.util.Spliterator
        public EntrySpliterator<K, V> trySplit() {
            Entry<K, V> entry;
            if (this.est < 0) {
                getEstimate();
            }
            int i2 = this.side;
            Entry<K, V> entry2 = this.current;
            Entry<K, V> entry3 = this.fence;
            if (entry2 == null || entry2 == entry3) {
                entry = null;
            } else if (i2 == 0) {
                entry = ((TreeMap) this.tree).root;
            } else {
                entry = i2 > 0 ? entry2.right : (i2 >= 0 || entry3 == null) ? null : entry3.left;
            }
            Entry<K, V> entry4 = entry;
            if (entry4 != null && entry4 != entry2 && entry4 != entry3 && this.tree.compare(entry2.key, entry4.key) < 0) {
                this.side = 1;
                TreeMap<K, V> treeMap = this.tree;
                this.current = entry4;
                int i3 = this.est >>> 1;
                this.est = i3;
                return new EntrySpliterator<>(treeMap, entry2, entry4, -1, i3, this.expectedModCount);
            }
            return null;
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super Map.Entry<K, V>> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            if (this.est < 0) {
                getEstimate();
            }
            Entry<K, V> entry = this.fence;
            Entry<K, V> entry2 = this.current;
            Entry<K, V> entry3 = entry2;
            if (entry2 != null && entry3 != entry) {
                this.current = entry;
                do {
                    consumer.accept(entry3);
                    Entry<K, V> entry4 = entry3.right;
                    Entry<K, V> entry5 = entry4;
                    if (entry4 != null) {
                        while (true) {
                            Entry<K, V> entry6 = entry5.left;
                            if (entry6 == null) {
                                break;
                            } else {
                                entry5 = entry6;
                            }
                        }
                    } else {
                        while (true) {
                            Entry<K, V> entry7 = entry3.parent;
                            entry5 = entry7;
                            if (entry7 == null || entry3 != entry5.right) {
                                break;
                            } else {
                                entry3 = entry5;
                            }
                        }
                    }
                    Entry<K, V> entry8 = entry5;
                    entry3 = entry8;
                    if (entry8 == null) {
                        break;
                    }
                } while (entry3 != entry);
                if (((TreeMap) this.tree).modCount != this.expectedModCount) {
                    throw new ConcurrentModificationException();
                }
            }
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super Map.Entry<K, V>> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            if (this.est < 0) {
                getEstimate();
            }
            Entry<K, V> entry = this.current;
            if (entry == null || entry == this.fence) {
                return false;
            }
            this.current = TreeMap.successor(entry);
            consumer.accept(entry);
            if (((TreeMap) this.tree).modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            return true;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return (this.side == 0 ? 64 : 0) | 1 | 4 | 16;
        }

        @Override // java.util.Spliterator
        public Comparator<Map.Entry<K, V>> getComparator() {
            if (((TreeMap) this.tree).comparator != null) {
                return Map.Entry.comparingByKey(((TreeMap) this.tree).comparator);
            }
            return (Comparator) ((Serializable) (entry, entry2) -> {
                return ((Comparable) entry.getKey()).compareTo(entry2.getKey());
            });
        }
    }
}
