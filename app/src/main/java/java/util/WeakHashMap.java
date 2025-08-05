package java.util;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.AbstractMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/* loaded from: rt.jar:java/util/WeakHashMap.class */
public class WeakHashMap<K, V> extends AbstractMap<K, V> implements Map<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final int MAXIMUM_CAPACITY = 1073741824;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    Entry<K, V>[] table;
    private int size;
    private int threshold;
    private final float loadFactor;
    private final ReferenceQueue<Object> queue;
    int modCount;
    private static final Object NULL_KEY = new Object();
    private transient Set<Map.Entry<K, V>> entrySet;

    private Entry<K, V>[] newTable(int i2) {
        return new Entry[i2];
    }

    public WeakHashMap(int i2, float f2) {
        this.queue = new ReferenceQueue<>();
        if (i2 < 0) {
            throw new IllegalArgumentException("Illegal Initial Capacity: " + i2);
        }
        i2 = i2 > 1073741824 ? 1073741824 : i2;
        if (f2 <= 0.0f || Float.isNaN(f2)) {
            throw new IllegalArgumentException("Illegal Load factor: " + f2);
        }
        int i3 = 1;
        while (true) {
            int i4 = i3;
            if (i4 < i2) {
                i3 = i4 << 1;
            } else {
                this.table = newTable(i4);
                this.loadFactor = f2;
                this.threshold = (int) (i4 * f2);
                return;
            }
        }
    }

    public WeakHashMap(int i2) {
        this(i2, 0.75f);
    }

    public WeakHashMap() {
        this(16, 0.75f);
    }

    public WeakHashMap(Map<? extends K, ? extends V> map) {
        this(Math.max(((int) (map.size() / 0.75f)) + 1, 16), 0.75f);
        putAll(map);
    }

    private static Object maskNull(Object obj) {
        return obj == null ? NULL_KEY : obj;
    }

    static Object unmaskNull(Object obj) {
        if (obj == NULL_KEY) {
            return null;
        }
        return obj;
    }

    private static boolean eq(Object obj, Object obj2) {
        return obj == obj2 || obj.equals(obj2);
    }

    final int hash(Object obj) {
        int iHashCode = obj.hashCode();
        int i2 = iHashCode ^ ((iHashCode >>> 20) ^ (iHashCode >>> 12));
        return (i2 ^ (i2 >>> 7)) ^ (i2 >>> 4);
    }

    private static int indexFor(int i2, int i3) {
        return i2 & (i3 - 1);
    }

    private void expungeStaleEntries() {
        while (true) {
            Reference<? extends Object> referencePoll = this.queue.poll();
            if (referencePoll != null) {
                synchronized (this.queue) {
                    Entry<K, V> entry = (Entry) referencePoll;
                    int iIndexFor = indexFor(entry.hash, this.table.length);
                    Entry<K, V> entry2 = this.table[iIndexFor];
                    Entry<K, V> entry3 = entry2;
                    while (true) {
                        if (entry3 == null) {
                            break;
                        }
                        Entry<K, V> entry4 = entry3.next;
                        if (entry3 == entry) {
                            if (entry2 == entry) {
                                this.table[iIndexFor] = entry4;
                            } else {
                                entry2.next = entry4;
                            }
                            entry.value = null;
                            this.size--;
                        } else {
                            entry2 = entry3;
                            entry3 = entry4;
                        }
                    }
                }
            } else {
                return;
            }
        }
    }

    private Entry<K, V>[] getTable() {
        expungeStaleEntries();
        return this.table;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        if (this.size == 0) {
            return 0;
        }
        expungeStaleEntries();
        return this.size;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V get(Object obj) {
        Object objMaskNull = maskNull(obj);
        int iHash = hash(objMaskNull);
        Entry<K, V>[] table = getTable();
        Entry<K, V> entry = table[indexFor(iHash, table.length)];
        while (true) {
            Entry<K, V> entry2 = entry;
            if (entry2 != null) {
                if (entry2.hash == iHash && eq(objMaskNull, entry2.get())) {
                    return entry2.value;
                }
                entry = entry2.next;
            } else {
                return null;
            }
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object obj) {
        return getEntry(obj) != null;
    }

    Entry<K, V> getEntry(Object obj) {
        Entry<K, V> entry;
        Object objMaskNull = maskNull(obj);
        int iHash = hash(objMaskNull);
        Entry<K, V>[] table = getTable();
        Entry<K, V> entry2 = table[indexFor(iHash, table.length)];
        while (true) {
            entry = entry2;
            if (entry == null || (entry.hash == iHash && eq(objMaskNull, entry.get()))) {
                break;
            }
            entry2 = entry.next;
        }
        return entry;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V put(K k2, V v2) {
        Object objMaskNull = maskNull(k2);
        int iHash = hash(objMaskNull);
        Entry<K, V>[] table = getTable();
        int iIndexFor = indexFor(iHash, table.length);
        Entry<K, V> entry = table[iIndexFor];
        while (true) {
            Entry<K, V> entry2 = entry;
            if (entry2 != null) {
                if (iHash != entry2.hash || !eq(objMaskNull, entry2.get())) {
                    entry = entry2.next;
                } else {
                    V v3 = entry2.value;
                    if (v2 != v3) {
                        entry2.value = v2;
                    }
                    return v3;
                }
            } else {
                this.modCount++;
                table[iIndexFor] = new Entry<>(objMaskNull, v2, this.queue, iHash, table[iIndexFor]);
                int i2 = this.size + 1;
                this.size = i2;
                if (i2 >= this.threshold) {
                    resize(table.length * 2);
                    return null;
                }
                return null;
            }
        }
    }

    void resize(int i2) {
        Entry<K, V>[] table = getTable();
        if (table.length == 1073741824) {
            this.threshold = Integer.MAX_VALUE;
            return;
        }
        Entry<K, V>[] entryArrNewTable = newTable(i2);
        transfer(table, entryArrNewTable);
        this.table = entryArrNewTable;
        if (this.size >= this.threshold / 2) {
            this.threshold = (int) (i2 * this.loadFactor);
            return;
        }
        expungeStaleEntries();
        transfer(entryArrNewTable, table);
        this.table = table;
    }

    private void transfer(Entry<K, V>[] entryArr, Entry<K, V>[] entryArr2) {
        for (int i2 = 0; i2 < entryArr.length; i2++) {
            Entry<K, V> entry = entryArr[i2];
            entryArr[i2] = null;
            while (entry != null) {
                Entry<K, V> entry2 = entry.next;
                if (entry.get() == null) {
                    entry.next = null;
                    entry.value = null;
                    this.size--;
                } else {
                    int iIndexFor = indexFor(entry.hash, entryArr2.length);
                    entry.next = entryArr2[iIndexFor];
                    entryArr2[iIndexFor] = entry;
                }
                entry = entry2;
            }
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void putAll(Map<? extends K, ? extends V> map) {
        int i2;
        int size = map.size();
        if (size == 0) {
            return;
        }
        if (size > this.threshold) {
            int i3 = (int) ((size / this.loadFactor) + 1.0f);
            if (i3 > 1073741824) {
                i3 = 1073741824;
            }
            int length = this.table.length;
            while (true) {
                i2 = length;
                if (i2 >= i3) {
                    break;
                } else {
                    length = i2 << 1;
                }
            }
            if (i2 > this.table.length) {
                resize(i2);
            }
        }
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V remove(Object obj) {
        Object objMaskNull = maskNull(obj);
        int iHash = hash(objMaskNull);
        Entry<K, V>[] table = getTable();
        int iIndexFor = indexFor(iHash, table.length);
        Entry<K, V> entry = table[iIndexFor];
        Entry<K, V> entry2 = entry;
        while (true) {
            Entry<K, V> entry3 = entry2;
            if (entry3 != null) {
                Entry<K, V> entry4 = entry3.next;
                if (iHash == entry3.hash && eq(objMaskNull, entry3.get())) {
                    this.modCount++;
                    this.size--;
                    if (entry == entry3) {
                        table[iIndexFor] = entry4;
                    } else {
                        entry.next = entry4;
                    }
                    return entry3.value;
                }
                entry = entry3;
                entry2 = entry4;
            } else {
                return null;
            }
        }
    }

    boolean removeMapping(Object obj) {
        if (!(obj instanceof Map.Entry)) {
            return false;
        }
        Entry<K, V>[] table = getTable();
        Map.Entry entry = (Map.Entry) obj;
        int iHash = hash(maskNull(entry.getKey()));
        int iIndexFor = indexFor(iHash, table.length);
        Entry<K, V> entry2 = table[iIndexFor];
        Entry<K, V> entry3 = entry2;
        while (true) {
            Entry<K, V> entry4 = entry3;
            if (entry4 != null) {
                Entry<K, V> entry5 = entry4.next;
                if (iHash == entry4.hash && entry4.equals(entry)) {
                    this.modCount++;
                    this.size--;
                    if (entry2 == entry4) {
                        table[iIndexFor] = entry5;
                        return true;
                    }
                    entry2.next = entry5;
                    return true;
                }
                entry2 = entry4;
                entry3 = entry5;
            } else {
                return false;
            }
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        while (this.queue.poll() != null) {
        }
        this.modCount++;
        Arrays.fill(this.table, (Object) null);
        this.size = 0;
        while (this.queue.poll() != null) {
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsValue(Object obj) {
        if (obj == null) {
            return containsNullValue();
        }
        Entry<K, V>[] table = getTable();
        int length = table.length;
        while (true) {
            int i2 = length;
            length--;
            if (i2 > 0) {
                Entry<K, V> entry = table[length];
                while (true) {
                    Entry<K, V> entry2 = entry;
                    if (entry2 != null) {
                        if (!obj.equals(entry2.value)) {
                            entry = entry2.next;
                        } else {
                            return true;
                        }
                    }
                }
            } else {
                return false;
            }
        }
    }

    private boolean containsNullValue() {
        Entry<K, V>[] table = getTable();
        int length = table.length;
        while (true) {
            int i2 = length;
            length--;
            if (i2 > 0) {
                Entry<K, V> entry = table[length];
                while (true) {
                    Entry<K, V> entry2 = entry;
                    if (entry2 != null) {
                        if (entry2.value != null) {
                            entry = entry2.next;
                        } else {
                            return true;
                        }
                    }
                }
            } else {
                return false;
            }
        }
    }

    /* loaded from: rt.jar:java/util/WeakHashMap$Entry.class */
    private static class Entry<K, V> extends WeakReference<Object> implements Map.Entry<K, V> {
        V value;
        final int hash;
        Entry<K, V> next;

        Entry(Object obj, V v2, ReferenceQueue<Object> referenceQueue, int i2, Entry<K, V> entry) {
            super(obj, referenceQueue);
            this.value = v2;
            this.hash = i2;
            this.next = entry;
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            return (K) WeakHashMap.unmaskNull(get());
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
            K key = getKey();
            Object key2 = entry.getKey();
            if (key == key2 || (key != null && key.equals(key2))) {
                V value = getValue();
                Object value2 = entry.getValue();
                if (value == value2) {
                    return true;
                }
                if (value != null && value.equals(value2)) {
                    return true;
                }
                return false;
            }
            return false;
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return Objects.hashCode(getKey()) ^ Objects.hashCode(getValue());
        }

        public String toString() {
            return ((Object) getKey()) + "=" + ((Object) getValue());
        }
    }

    /* loaded from: rt.jar:java/util/WeakHashMap$HashIterator.class */
    private abstract class HashIterator<T> implements Iterator<T> {
        private int index;
        private Entry<K, V> entry;
        private Entry<K, V> lastReturned;
        private int expectedModCount;
        private Object nextKey;
        private Object currentKey;

        HashIterator() {
            this.expectedModCount = WeakHashMap.this.modCount;
            this.index = WeakHashMap.this.isEmpty() ? 0 : WeakHashMap.this.table.length;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            Entry<K, V>[] entryArr = WeakHashMap.this.table;
            while (this.nextKey == null) {
                Entry<K, V> entry = this.entry;
                int i2 = this.index;
                while (entry == null && i2 > 0) {
                    i2--;
                    entry = entryArr[i2];
                }
                this.entry = entry;
                this.index = i2;
                if (entry == null) {
                    this.currentKey = null;
                    return false;
                }
                this.nextKey = entry.get();
                if (this.nextKey == null) {
                    this.entry = this.entry.next;
                }
            }
            return true;
        }

        protected Entry<K, V> nextEntry() {
            if (WeakHashMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            if (this.nextKey == null && !hasNext()) {
                throw new NoSuchElementException();
            }
            this.lastReturned = this.entry;
            this.entry = this.entry.next;
            this.currentKey = this.nextKey;
            this.nextKey = null;
            return this.lastReturned;
        }

        @Override // java.util.Iterator
        public void remove() {
            if (this.lastReturned == null) {
                throw new IllegalStateException();
            }
            if (WeakHashMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            WeakHashMap.this.remove(this.currentKey);
            this.expectedModCount = WeakHashMap.this.modCount;
            this.lastReturned = null;
            this.currentKey = null;
        }
    }

    /* loaded from: rt.jar:java/util/WeakHashMap$ValueIterator.class */
    private class ValueIterator extends WeakHashMap<K, V>.HashIterator<V> {
        private ValueIterator() {
            super();
        }

        @Override // java.util.Iterator
        public V next() {
            return nextEntry().value;
        }
    }

    /* loaded from: rt.jar:java/util/WeakHashMap$KeyIterator.class */
    private class KeyIterator extends WeakHashMap<K, V>.HashIterator<K> {
        private KeyIterator() {
            super();
        }

        @Override // java.util.Iterator
        public K next() {
            return nextEntry().getKey();
        }
    }

    /* loaded from: rt.jar:java/util/WeakHashMap$EntryIterator.class */
    private class EntryIterator extends WeakHashMap<K, V>.HashIterator<Map.Entry<K, V>> {
        private EntryIterator() {
            super();
        }

        @Override // java.util.Iterator
        public Map.Entry<K, V> next() {
            return nextEntry();
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<K> keySet() {
        Set<K> keySet = this.keySet;
        if (keySet == null) {
            keySet = new KeySet();
            this.keySet = keySet;
        }
        return keySet;
    }

    /* loaded from: rt.jar:java/util/WeakHashMap$KeySet.class */
    private class KeySet extends AbstractSet<K> {
        private KeySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return WeakHashMap.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return WeakHashMap.this.containsKey(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            if (WeakHashMap.this.containsKey(obj)) {
                WeakHashMap.this.remove(obj);
                return true;
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            WeakHashMap.this.clear();
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Spliterator<K> spliterator() {
            return new KeySpliterator(WeakHashMap.this, 0, -1, 0, 0);
        }
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

    /* loaded from: rt.jar:java/util/WeakHashMap$Values.class */
    private class Values extends AbstractCollection<V> {
        private Values() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return WeakHashMap.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return WeakHashMap.this.containsValue(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            WeakHashMap.this.clear();
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Spliterator<V> spliterator() {
            return new ValueSpliterator(WeakHashMap.this, 0, -1, 0, 0);
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> set = this.entrySet;
        if (set != null) {
            return set;
        }
        EntrySet entrySet = new EntrySet();
        this.entrySet = entrySet;
        return entrySet;
    }

    /* loaded from: rt.jar:java/util/WeakHashMap$EntrySet.class */
    private class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        private EntrySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            Entry<K, V> entry2 = WeakHashMap.this.getEntry(entry.getKey());
            return entry2 != null && entry2.equals(entry);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            return WeakHashMap.this.removeMapping(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return WeakHashMap.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            WeakHashMap.this.clear();
        }

        private List<Map.Entry<K, V>> deepCopy() {
            ArrayList arrayList = new ArrayList(size());
            Iterator<Map.Entry<K, V>> it = iterator();
            while (it.hasNext()) {
                arrayList.add(new AbstractMap.SimpleEntry(it.next()));
            }
            return arrayList;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public Object[] toArray() {
            return deepCopy().toArray();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            return (T[]) deepCopy().toArray(tArr);
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Spliterator<Map.Entry<K, V>> spliterator() {
            return new EntrySpliterator(WeakHashMap.this, 0, -1, 0, 0);
        }
    }

    @Override // java.util.Map
    public void forEach(BiConsumer<? super K, ? super V> biConsumer) {
        Objects.requireNonNull(biConsumer);
        int i2 = this.modCount;
        Entry<K, V>[] table = getTable();
        int length = table.length;
        for (int i3 = 0; i3 < length; i3++) {
            Entry<K, V> entry = table[i3];
            while (entry != null) {
                Object obj = entry.get();
                if (obj != null) {
                    biConsumer.accept((Object) unmaskNull(obj), entry.value);
                }
                entry = entry.next;
                if (i2 != this.modCount) {
                    throw new ConcurrentModificationException();
                }
            }
        }
    }

    @Override // java.util.Map
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        int i2 = this.modCount;
        Entry<K, V>[] table = getTable();
        int length = table.length;
        for (int i3 = 0; i3 < length; i3++) {
            Entry<K, V> entry = table[i3];
            while (entry != null) {
                Object obj = entry.get();
                if (obj != null) {
                    entry.value = biFunction.apply((Object) unmaskNull(obj), entry.value);
                }
                entry = entry.next;
                if (i2 != this.modCount) {
                    throw new ConcurrentModificationException();
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/WeakHashMap$WeakHashMapSpliterator.class */
    static class WeakHashMapSpliterator<K, V> {
        final WeakHashMap<K, V> map;
        Entry<K, V> current;
        int index;
        int fence;
        int est;
        int expectedModCount;

        WeakHashMapSpliterator(WeakHashMap<K, V> weakHashMap, int i2, int i3, int i4, int i5) {
            this.map = weakHashMap;
            this.index = i2;
            this.fence = i3;
            this.est = i4;
            this.expectedModCount = i5;
        }

        final int getFence() {
            int i2 = this.fence;
            int i3 = i2;
            if (i2 < 0) {
                WeakHashMap<K, V> weakHashMap = this.map;
                this.est = weakHashMap.size();
                this.expectedModCount = weakHashMap.modCount;
                int length = weakHashMap.table.length;
                this.fence = length;
                i3 = length;
            }
            return i3;
        }

        public final long estimateSize() {
            getFence();
            return this.est;
        }
    }

    /* loaded from: rt.jar:java/util/WeakHashMap$KeySpliterator.class */
    static final class KeySpliterator<K, V> extends WeakHashMapSpliterator<K, V> implements Spliterator<K> {
        KeySpliterator(WeakHashMap<K, V> weakHashMap, int i2, int i3, int i4, int i5) {
            super(weakHashMap, i2, i3, i4, i5);
        }

        @Override // java.util.Spliterator
        public KeySpliterator<K, V> trySplit() {
            int fence = getFence();
            int i2 = this.index;
            int i3 = (i2 + fence) >>> 1;
            if (i2 >= i3) {
                return null;
            }
            WeakHashMap<K, V> weakHashMap = this.map;
            this.index = i3;
            int i4 = this.est >>> 1;
            this.est = i4;
            return new KeySpliterator<>(weakHashMap, i2, i3, i4, this.expectedModCount);
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super K> consumer) {
            int i2;
            if (consumer == null) {
                throw new NullPointerException();
            }
            WeakHashMap<K, V> weakHashMap = this.map;
            Entry<K, V>[] entryArr = weakHashMap.table;
            int i3 = this.fence;
            int i4 = i3;
            if (i3 < 0) {
                int i5 = weakHashMap.modCount;
                this.expectedModCount = i5;
                i2 = i5;
                int length = entryArr.length;
                this.fence = length;
                i4 = length;
            } else {
                i2 = this.expectedModCount;
            }
            if (entryArr.length >= i4) {
                int i6 = this.index;
                int i7 = i6;
                if (i6 >= 0) {
                    int i8 = i4;
                    this.index = i8;
                    if (i7 < i8 || this.current != null) {
                        Entry<K, V> entry = this.current;
                        this.current = null;
                        while (true) {
                            if (entry == null) {
                                int i9 = i7;
                                i7++;
                                entry = entryArr[i9];
                            } else {
                                Object obj = entry.get();
                                entry = entry.next;
                                if (obj != null) {
                                    consumer.accept((Object) WeakHashMap.unmaskNull(obj));
                                }
                            }
                            if (entry == null && i7 >= i4) {
                                break;
                            }
                        }
                    }
                }
            }
            if (weakHashMap.modCount != i2) {
                throw new ConcurrentModificationException();
            }
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super K> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Entry<K, V>[] entryArr = this.map.table;
            int length = entryArr.length;
            int fence = getFence();
            if (length < fence || this.index < 0) {
                return false;
            }
            while (true) {
                if (this.current != null || this.index < fence) {
                    if (this.current == null) {
                        int i2 = this.index;
                        this.index = i2 + 1;
                        this.current = entryArr[i2];
                    } else {
                        Object obj = this.current.get();
                        this.current = this.current.next;
                        if (obj != null) {
                            consumer.accept((Object) WeakHashMap.unmaskNull(obj));
                            if (this.map.modCount != this.expectedModCount) {
                                throw new ConcurrentModificationException();
                            }
                            return true;
                        }
                    }
                } else {
                    return false;
                }
            }
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return 1;
        }
    }

    /* loaded from: rt.jar:java/util/WeakHashMap$ValueSpliterator.class */
    static final class ValueSpliterator<K, V> extends WeakHashMapSpliterator<K, V> implements Spliterator<V> {
        ValueSpliterator(WeakHashMap<K, V> weakHashMap, int i2, int i3, int i4, int i5) {
            super(weakHashMap, i2, i3, i4, i5);
        }

        @Override // java.util.Spliterator
        public ValueSpliterator<K, V> trySplit() {
            int fence = getFence();
            int i2 = this.index;
            int i3 = (i2 + fence) >>> 1;
            if (i2 >= i3) {
                return null;
            }
            WeakHashMap<K, V> weakHashMap = this.map;
            this.index = i3;
            int i4 = this.est >>> 1;
            this.est = i4;
            return new ValueSpliterator<>(weakHashMap, i2, i3, i4, this.expectedModCount);
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super V> consumer) {
            int i2;
            if (consumer == null) {
                throw new NullPointerException();
            }
            WeakHashMap<K, V> weakHashMap = this.map;
            Entry<K, V>[] entryArr = weakHashMap.table;
            int i3 = this.fence;
            int i4 = i3;
            if (i3 < 0) {
                int i5 = weakHashMap.modCount;
                this.expectedModCount = i5;
                i2 = i5;
                int length = entryArr.length;
                this.fence = length;
                i4 = length;
            } else {
                i2 = this.expectedModCount;
            }
            if (entryArr.length >= i4) {
                int i6 = this.index;
                int i7 = i6;
                if (i6 >= 0) {
                    int i8 = i4;
                    this.index = i8;
                    if (i7 < i8 || this.current != null) {
                        Entry<K, V> entry = this.current;
                        this.current = null;
                        while (true) {
                            if (entry == null) {
                                int i9 = i7;
                                i7++;
                                entry = entryArr[i9];
                            } else {
                                Object obj = entry.get();
                                V v2 = entry.value;
                                entry = entry.next;
                                if (obj != null) {
                                    consumer.accept(v2);
                                }
                            }
                            if (entry == null && i7 >= i4) {
                                break;
                            }
                        }
                    }
                }
            }
            if (weakHashMap.modCount != i2) {
                throw new ConcurrentModificationException();
            }
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super V> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Entry<K, V>[] entryArr = this.map.table;
            int length = entryArr.length;
            int fence = getFence();
            if (length < fence || this.index < 0) {
                return false;
            }
            while (true) {
                if (this.current != null || this.index < fence) {
                    if (this.current == null) {
                        int i2 = this.index;
                        this.index = i2 + 1;
                        this.current = entryArr[i2];
                    } else {
                        Object obj = this.current.get();
                        V v2 = this.current.value;
                        this.current = this.current.next;
                        if (obj != null) {
                            consumer.accept(v2);
                            if (this.map.modCount != this.expectedModCount) {
                                throw new ConcurrentModificationException();
                            }
                            return true;
                        }
                    }
                } else {
                    return false;
                }
            }
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return 0;
        }
    }

    /* loaded from: rt.jar:java/util/WeakHashMap$EntrySpliterator.class */
    static final class EntrySpliterator<K, V> extends WeakHashMapSpliterator<K, V> implements Spliterator<Map.Entry<K, V>> {
        EntrySpliterator(WeakHashMap<K, V> weakHashMap, int i2, int i3, int i4, int i5) {
            super(weakHashMap, i2, i3, i4, i5);
        }

        @Override // java.util.Spliterator
        public EntrySpliterator<K, V> trySplit() {
            int fence = getFence();
            int i2 = this.index;
            int i3 = (i2 + fence) >>> 1;
            if (i2 >= i3) {
                return null;
            }
            WeakHashMap<K, V> weakHashMap = this.map;
            this.index = i3;
            int i4 = this.est >>> 1;
            this.est = i4;
            return new EntrySpliterator<>(weakHashMap, i2, i3, i4, this.expectedModCount);
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super Map.Entry<K, V>> consumer) {
            int i2;
            if (consumer == null) {
                throw new NullPointerException();
            }
            WeakHashMap<K, V> weakHashMap = this.map;
            Entry<K, V>[] entryArr = weakHashMap.table;
            int i3 = this.fence;
            int i4 = i3;
            if (i3 < 0) {
                int i5 = weakHashMap.modCount;
                this.expectedModCount = i5;
                i2 = i5;
                int length = entryArr.length;
                this.fence = length;
                i4 = length;
            } else {
                i2 = this.expectedModCount;
            }
            if (entryArr.length >= i4) {
                int i6 = this.index;
                int i7 = i6;
                if (i6 >= 0) {
                    int i8 = i4;
                    this.index = i8;
                    if (i7 < i8 || this.current != null) {
                        Entry<K, V> entry = this.current;
                        this.current = null;
                        while (true) {
                            if (entry == null) {
                                int i9 = i7;
                                i7++;
                                entry = entryArr[i9];
                            } else {
                                Object obj = entry.get();
                                V v2 = entry.value;
                                entry = entry.next;
                                if (obj != null) {
                                    consumer.accept(new AbstractMap.SimpleImmutableEntry(WeakHashMap.unmaskNull(obj), v2));
                                }
                            }
                            if (entry == null && i7 >= i4) {
                                break;
                            }
                        }
                    }
                }
            }
            if (weakHashMap.modCount != i2) {
                throw new ConcurrentModificationException();
            }
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super Map.Entry<K, V>> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Entry<K, V>[] entryArr = this.map.table;
            int length = entryArr.length;
            int fence = getFence();
            if (length < fence || this.index < 0) {
                return false;
            }
            while (true) {
                if (this.current != null || this.index < fence) {
                    if (this.current == null) {
                        int i2 = this.index;
                        this.index = i2 + 1;
                        this.current = entryArr[i2];
                    } else {
                        Object obj = this.current.get();
                        V v2 = this.current.value;
                        this.current = this.current.next;
                        if (obj != null) {
                            consumer.accept(new AbstractMap.SimpleImmutableEntry(WeakHashMap.unmaskNull(obj), v2));
                            if (this.map.modCount != this.expectedModCount) {
                                throw new ConcurrentModificationException();
                            }
                            return true;
                        }
                    }
                } else {
                    return false;
                }
            }
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return 1;
        }
    }
}
