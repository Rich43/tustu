package java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import sun.misc.SharedSecrets;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/util/Hashtable.class */
public class Hashtable<K, V> extends Dictionary<K, V> implements Map<K, V>, Cloneable, Serializable {
    private transient Entry<?, ?>[] table;
    private transient int count;
    private int threshold;
    private float loadFactor;
    private transient int modCount;
    private static final long serialVersionUID = 1421746759512286392L;
    private static final int MAX_ARRAY_SIZE = 2147483639;
    private volatile transient Set<K> keySet;
    private volatile transient Set<Map.Entry<K, V>> entrySet;
    private volatile transient Collection<V> values;
    private static final int KEYS = 0;
    private static final int VALUES = 1;
    private static final int ENTRIES = 2;

    static /* synthetic */ int access$508(Hashtable hashtable) {
        int i2 = hashtable.modCount;
        hashtable.modCount = i2 + 1;
        return i2;
    }

    static /* synthetic */ int access$210(Hashtable hashtable) {
        int i2 = hashtable.count;
        hashtable.count = i2 - 1;
        return i2;
    }

    public Hashtable(int i2, float f2) {
        this.modCount = 0;
        if (i2 < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + i2);
        }
        if (f2 <= 0.0f || Float.isNaN(f2)) {
            throw new IllegalArgumentException("Illegal Load: " + f2);
        }
        i2 = i2 == 0 ? 1 : i2;
        this.loadFactor = f2;
        this.table = new Entry[i2];
        this.threshold = (int) Math.min(i2 * f2, 2.1474836E9f);
    }

    public Hashtable(int i2) {
        this(i2, 0.75f);
    }

    public Hashtable() {
        this(11, 0.75f);
    }

    public Hashtable(Map<? extends K, ? extends V> map) {
        this(Math.max(2 * map.size(), 11), 0.75f);
        putAll(map);
    }

    @Override // java.util.Dictionary
    public synchronized int size() {
        return this.count;
    }

    @Override // java.util.Dictionary
    public synchronized boolean isEmpty() {
        return this.count == 0;
    }

    @Override // java.util.Dictionary
    public synchronized Enumeration<K> keys() {
        return (Enumeration<K>) getEnumeration(0);
    }

    @Override // java.util.Dictionary
    public synchronized Enumeration<V> elements() {
        return (Enumeration<V>) getEnumeration(1);
    }

    public synchronized boolean contains(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        Entry[] entryArr = this.table;
        int length = entryArr.length;
        while (true) {
            int i2 = length;
            length--;
            if (i2 > 0) {
                Entry entry = entryArr[length];
                while (true) {
                    Entry entry2 = entry;
                    if (entry2 != null) {
                        if (!entry2.value.equals(obj)) {
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

    @Override // java.util.Map
    public boolean containsValue(Object obj) {
        return contains(obj);
    }

    @Override // java.util.Map
    public synchronized boolean containsKey(Object obj) {
        Entry[] entryArr = this.table;
        int iHashCode = obj.hashCode();
        Entry entry = entryArr[(iHashCode & Integer.MAX_VALUE) % entryArr.length];
        while (true) {
            Entry entry2 = entry;
            if (entry2 != null) {
                if (entry2.hash != iHashCode || !entry2.key.equals(obj)) {
                    entry = entry2.next;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    @Override // java.util.Dictionary
    public synchronized V get(Object obj) {
        Entry[] entryArr = this.table;
        int iHashCode = obj.hashCode();
        Entry entry = entryArr[(iHashCode & Integer.MAX_VALUE) % entryArr.length];
        while (true) {
            Entry entry2 = entry;
            if (entry2 != null) {
                if (entry2.hash != iHashCode || !entry2.key.equals(obj)) {
                    entry = entry2.next;
                } else {
                    return entry2.value;
                }
            } else {
                return null;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void rehash() {
        int length = this.table.length;
        Entry<?, ?>[] entryArr = this.table;
        int i2 = (length << 1) + 1;
        if (i2 - MAX_ARRAY_SIZE > 0) {
            if (length == MAX_ARRAY_SIZE) {
                return;
            } else {
                i2 = MAX_ARRAY_SIZE;
            }
        }
        Entry<?, ?>[] entryArr2 = new Entry[i2];
        this.modCount++;
        this.threshold = (int) Math.min(i2 * this.loadFactor, 2.1474836E9f);
        this.table = entryArr2;
        int i3 = length;
        while (true) {
            int i4 = i3;
            i3--;
            if (i4 > 0) {
                Entry<?, ?> entry = entryArr[i3];
                while (entry != null) {
                    Entry<?, ?> entry2 = entry;
                    entry = entry.next;
                    int i5 = (entry2.hash & Integer.MAX_VALUE) % i2;
                    entry2.next = (Entry<K, V>) entryArr2[i5];
                    entryArr2[i5] = entry2;
                }
            } else {
                return;
            }
        }
    }

    private void addEntry(int i2, K k2, V v2, int i3) {
        this.modCount++;
        Entry<?, ?>[] entryArr = this.table;
        if (this.count >= this.threshold) {
            rehash();
            entryArr = this.table;
            i2 = k2.hashCode();
            i3 = (i2 & Integer.MAX_VALUE) % entryArr.length;
        }
        entryArr[i3] = new Entry<>(i2, k2, v2, entryArr[i3]);
        this.count++;
    }

    @Override // java.util.Dictionary
    public synchronized V put(K k2, V v2) {
        if (v2 == null) {
            throw new NullPointerException();
        }
        Entry[] entryArr = this.table;
        int iHashCode = k2.hashCode();
        int length = (iHashCode & Integer.MAX_VALUE) % entryArr.length;
        Entry entry = entryArr[length];
        while (true) {
            Entry entry2 = entry;
            if (entry2 != null) {
                if (entry2.hash != iHashCode || !entry2.key.equals(k2)) {
                    entry = entry2.next;
                } else {
                    V v3 = entry2.value;
                    entry2.value = v2;
                    return v3;
                }
            } else {
                addEntry(iHashCode, k2, v2, length);
                return null;
            }
        }
    }

    @Override // java.util.Dictionary
    public synchronized V remove(Object obj) {
        Entry<?, ?>[] entryArr = this.table;
        int iHashCode = obj.hashCode();
        int length = (iHashCode & Integer.MAX_VALUE) % entryArr.length;
        Entry<?, ?> entry = null;
        for (Entry<?, ?> entry2 = entryArr[length]; entry2 != null; entry2 = entry2.next) {
            if (entry2.hash != iHashCode || !entry2.key.equals(obj)) {
                entry = entry2;
            } else {
                this.modCount++;
                if (entry != null) {
                    entry.next = (Entry<K, V>) entry2.next;
                } else {
                    entryArr[length] = entry2.next;
                }
                this.count--;
                V v2 = entry2.value;
                entry2.value = null;
                return v2;
            }
        }
        return null;
    }

    public synchronized void putAll(Map<? extends K, ? extends V> map) {
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    public synchronized void clear() {
        Entry<?, ?>[] entryArr = this.table;
        this.modCount++;
        int length = entryArr.length;
        while (true) {
            length--;
            if (length >= 0) {
                entryArr[length] = null;
            } else {
                this.count = 0;
                return;
            }
        }
    }

    public synchronized Object clone() {
        try {
            Hashtable hashtable = (Hashtable) super.clone();
            hashtable.table = new Entry[this.table.length];
            int length = this.table.length;
            while (true) {
                int i2 = length;
                length--;
                if (i2 > 0) {
                    hashtable.table[length] = this.table[length] != null ? (Entry) this.table[length].clone() : null;
                } else {
                    hashtable.keySet = null;
                    hashtable.entrySet = null;
                    hashtable.values = null;
                    hashtable.modCount = 0;
                    return hashtable;
                }
            }
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }

    public synchronized String toString() {
        int size = size() - 1;
        if (size == -1) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<K, V>> it = entrySet().iterator();
        sb.append('{');
        int i2 = 0;
        while (true) {
            Map.Entry<K, V> next = it.next();
            K key = next.getKey();
            V value = next.getValue();
            sb.append(key == this ? "(this Map)" : key.toString());
            sb.append('=');
            sb.append(value == this ? "(this Map)" : value.toString());
            if (i2 == size) {
                return sb.append('}').toString();
            }
            sb.append(", ");
            i2++;
        }
    }

    private <T> Enumeration<T> getEnumeration(int i2) {
        if (this.count == 0) {
            return Collections.emptyEnumeration();
        }
        return new Enumerator(i2, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public <T> Iterator<T> getIterator(int i2) {
        if (this.count == 0) {
            return Collections.emptyIterator();
        }
        return new Enumerator(i2, true);
    }

    public Set<K> keySet() {
        if (this.keySet == null) {
            this.keySet = Collections.synchronizedSet(new KeySet(), this);
        }
        return this.keySet;
    }

    /* loaded from: rt.jar:java/util/Hashtable$KeySet.class */
    private class KeySet extends AbstractSet<K> {
        private KeySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<K> iterator() {
            return Hashtable.this.getIterator(0);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return Hashtable.this.count;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return Hashtable.this.containsKey(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            return Hashtable.this.remove(obj) != null;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            Hashtable.this.clear();
        }
    }

    public Set<Map.Entry<K, V>> entrySet() {
        if (this.entrySet == null) {
            this.entrySet = Collections.synchronizedSet(new EntrySet(), this);
        }
        return this.entrySet;
    }

    /* loaded from: rt.jar:java/util/Hashtable$EntrySet.class */
    private class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        private EntrySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<Map.Entry<K, V>> iterator() {
            return Hashtable.this.getIterator(2);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public boolean add(Map.Entry<K, V> entry) {
            return super.add((EntrySet) entry);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            Object key = entry.getKey();
            Entry<K, V>[] entryArr = Hashtable.this.table;
            int iHashCode = key.hashCode();
            Entry<K, V> entry2 = entryArr[(iHashCode & Integer.MAX_VALUE) % entryArr.length];
            while (true) {
                Entry<K, V> entry3 = entry2;
                if (entry3 != null) {
                    if (entry3.hash != iHashCode || !entry3.equals(entry)) {
                        entry2 = entry3.next;
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            Object key = entry.getKey();
            Entry<K, V>[] entryArr = Hashtable.this.table;
            int iHashCode = key.hashCode();
            int length = (iHashCode & Integer.MAX_VALUE) % entryArr.length;
            Entry<K, V> entry2 = null;
            for (Entry<K, V> entry3 = entryArr[length]; entry3 != null; entry3 = entry3.next) {
                if (entry3.hash != iHashCode || !entry3.equals(entry)) {
                    entry2 = entry3;
                } else {
                    Hashtable.access$508(Hashtable.this);
                    if (entry2 != null) {
                        entry2.next = entry3.next;
                    } else {
                        entryArr[length] = entry3.next;
                    }
                    Hashtable.access$210(Hashtable.this);
                    entry3.value = null;
                    return true;
                }
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return Hashtable.this.count;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            Hashtable.this.clear();
        }
    }

    public Collection<V> values() {
        if (this.values == null) {
            this.values = Collections.synchronizedCollection(new ValueCollection(), this);
        }
        return this.values;
    }

    /* loaded from: rt.jar:java/util/Hashtable$ValueCollection.class */
    private class ValueCollection extends AbstractCollection<V> {
        private ValueCollection() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<V> iterator() {
            return Hashtable.this.getIterator(1);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return Hashtable.this.count;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return Hashtable.this.containsValue(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            Hashtable.this.clear();
        }
    }

    @Override // java.util.Map
    public synchronized boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Map)) {
            return false;
        }
        Map map = (Map) obj;
        if (map.size() != size()) {
            return false;
        }
        try {
            for (Map.Entry<K, V> entry : entrySet()) {
                K key = entry.getKey();
                V value = entry.getValue();
                if (value == null) {
                    if (map.get(key) != null || !map.containsKey(key)) {
                        return false;
                    }
                } else if (!value.equals(map.get(key))) {
                    return false;
                }
            }
            return true;
        } catch (ClassCastException e2) {
            return false;
        } catch (NullPointerException e3) {
            return false;
        }
    }

    @Override // java.util.Map
    public synchronized int hashCode() {
        int iHashCode = 0;
        if (this.count == 0 || this.loadFactor < 0.0f) {
            return 0;
        }
        this.loadFactor = -this.loadFactor;
        for (Entry entry : this.table) {
            while (true) {
                Entry entry2 = entry;
                if (entry2 != null) {
                    iHashCode += entry2.hashCode();
                    entry = entry2.next;
                }
            }
        }
        this.loadFactor = -this.loadFactor;
        return iHashCode;
    }

    public synchronized V getOrDefault(Object obj, V v2) {
        V v3 = get(obj);
        return null == v3 ? v2 : v3;
    }

    public synchronized void forEach(BiConsumer<? super K, ? super V> biConsumer) {
        Objects.requireNonNull(biConsumer);
        int i2 = this.modCount;
        Entry<?, ?>[] entryArr = this.table;
        int length = entryArr.length;
        for (int i3 = 0; i3 < length; i3++) {
            Entry<?, ?> entry = entryArr[i3];
            while (entry != null) {
                biConsumer.accept(entry.key, entry.value);
                entry = entry.next;
                if (i2 != this.modCount) {
                    throw new ConcurrentModificationException();
                }
            }
        }
    }

    public synchronized void replaceAll(BiFunction<? super K, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        int i2 = this.modCount;
        Entry<?, ?>[] entryArr = this.table;
        int length = entryArr.length;
        for (int i3 = 0; i3 < length; i3++) {
            Entry<?, ?> entry = entryArr[i3];
            while (entry != null) {
                entry.value = (V) Objects.requireNonNull(biFunction.apply(entry.key, entry.value));
                entry = entry.next;
                if (i2 != this.modCount) {
                    throw new ConcurrentModificationException();
                }
            }
        }
    }

    public synchronized V putIfAbsent(K k2, V v2) {
        Objects.requireNonNull(v2);
        Entry[] entryArr = this.table;
        int iHashCode = k2.hashCode();
        int length = (iHashCode & Integer.MAX_VALUE) % entryArr.length;
        Entry entry = entryArr[length];
        while (true) {
            Entry entry2 = entry;
            if (entry2 != null) {
                if (entry2.hash != iHashCode || !entry2.key.equals(k2)) {
                    entry = entry2.next;
                } else {
                    V v3 = entry2.value;
                    if (v3 == null) {
                        entry2.value = v2;
                    }
                    return v3;
                }
            } else {
                addEntry(iHashCode, k2, v2, length);
                return null;
            }
        }
    }

    public synchronized boolean remove(Object obj, Object obj2) {
        Objects.requireNonNull(obj2);
        Entry<?, ?>[] entryArr = this.table;
        int iHashCode = obj.hashCode();
        int length = (iHashCode & Integer.MAX_VALUE) % entryArr.length;
        Entry<?, ?> entry = null;
        for (Entry<?, ?> entry2 = entryArr[length]; entry2 != null; entry2 = entry2.next) {
            if (entry2.hash != iHashCode || !entry2.key.equals(obj) || !entry2.value.equals(obj2)) {
                entry = entry2;
            } else {
                this.modCount++;
                if (entry != null) {
                    entry.next = (Entry<K, V>) entry2.next;
                } else {
                    entryArr[length] = entry2.next;
                }
                this.count--;
                entry2.value = null;
                return true;
            }
        }
        return false;
    }

    public synchronized boolean replace(K k2, V v2, V v3) {
        Objects.requireNonNull(v2);
        Objects.requireNonNull(v3);
        Entry[] entryArr = this.table;
        int iHashCode = k2.hashCode();
        Entry entry = entryArr[(iHashCode & Integer.MAX_VALUE) % entryArr.length];
        while (true) {
            Entry entry2 = entry;
            if (entry2 != null) {
                if (entry2.hash != iHashCode || !entry2.key.equals(k2)) {
                    entry = entry2.next;
                } else {
                    if (entry2.value.equals(v2)) {
                        entry2.value = v3;
                        return true;
                    }
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    public synchronized V replace(K k2, V v2) {
        Objects.requireNonNull(v2);
        Entry[] entryArr = this.table;
        int iHashCode = k2.hashCode();
        Entry entry = entryArr[(iHashCode & Integer.MAX_VALUE) % entryArr.length];
        while (true) {
            Entry entry2 = entry;
            if (entry2 != null) {
                if (entry2.hash != iHashCode || !entry2.key.equals(k2)) {
                    entry = entry2.next;
                } else {
                    V v3 = entry2.value;
                    entry2.value = v2;
                    return v3;
                }
            } else {
                return null;
            }
        }
    }

    public synchronized V computeIfAbsent(K k2, Function<? super K, ? extends V> function) {
        Objects.requireNonNull(function);
        Entry[] entryArr = this.table;
        int iHashCode = k2.hashCode();
        int length = (iHashCode & Integer.MAX_VALUE) % entryArr.length;
        Entry entry = entryArr[length];
        while (true) {
            Entry entry2 = entry;
            if (entry2 != null) {
                if (entry2.hash != iHashCode || !entry2.key.equals(k2)) {
                    entry = entry2.next;
                } else {
                    return entry2.value;
                }
            } else {
                V vApply = function.apply(k2);
                if (vApply != null) {
                    addEntry(iHashCode, k2, vApply, length);
                }
                return vApply;
            }
        }
    }

    public synchronized V computeIfPresent(K k2, BiFunction<? super K, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        Entry<?, ?>[] entryArr = this.table;
        int iHashCode = k2.hashCode();
        int length = (iHashCode & Integer.MAX_VALUE) % entryArr.length;
        Entry<?, ?> entry = null;
        for (Entry<?, ?> entry2 = entryArr[length]; entry2 != null; entry2 = entry2.next) {
            if (entry2.hash != iHashCode || !entry2.key.equals(k2)) {
                entry = entry2;
            } else {
                V vApply = biFunction.apply(k2, entry2.value);
                if (vApply == null) {
                    this.modCount++;
                    if (entry != null) {
                        entry.next = (Entry<K, V>) entry2.next;
                    } else {
                        entryArr[length] = entry2.next;
                    }
                    this.count--;
                } else {
                    entry2.value = vApply;
                }
                return vApply;
            }
        }
        return null;
    }

    public synchronized V compute(K k2, BiFunction<? super K, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        Entry<?, ?>[] entryArr = this.table;
        int iHashCode = k2.hashCode();
        int length = (iHashCode & Integer.MAX_VALUE) % entryArr.length;
        Entry<?, ?> entry = null;
        for (Entry<?, ?> entry2 = entryArr[length]; entry2 != null; entry2 = entry2.next) {
            if (entry2.hash != iHashCode || !Objects.equals(entry2.key, k2)) {
                entry = entry2;
            } else {
                V vApply = biFunction.apply(k2, entry2.value);
                if (vApply == null) {
                    this.modCount++;
                    if (entry != null) {
                        entry.next = (Entry<K, V>) entry2.next;
                    } else {
                        entryArr[length] = entry2.next;
                    }
                    this.count--;
                } else {
                    entry2.value = vApply;
                }
                return vApply;
            }
        }
        V vApply2 = biFunction.apply(k2, null);
        if (vApply2 != null) {
            addEntry(iHashCode, k2, vApply2, length);
        }
        return vApply2;
    }

    public synchronized V merge(K k2, V v2, BiFunction<? super V, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        Entry<?, ?>[] entryArr = this.table;
        int iHashCode = k2.hashCode();
        int length = (iHashCode & Integer.MAX_VALUE) % entryArr.length;
        Entry<?, ?> entry = null;
        for (Entry<?, ?> entry2 = entryArr[length]; entry2 != null; entry2 = entry2.next) {
            if (entry2.hash != iHashCode || !entry2.key.equals(k2)) {
                entry = entry2;
            } else {
                V vApply = biFunction.apply(entry2.value, v2);
                if (vApply == null) {
                    this.modCount++;
                    if (entry != null) {
                        entry.next = (Entry<K, V>) entry2.next;
                    } else {
                        entryArr[length] = entry2.next;
                    }
                    this.count--;
                } else {
                    entry2.value = vApply;
                }
                return vApply;
            }
        }
        if (v2 != null) {
            addEntry(iHashCode, k2, v2, length);
        }
        return v2;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        Entry<K, V> entry = null;
        synchronized (this) {
            objectOutputStream.defaultWriteObject();
            objectOutputStream.writeInt(this.table.length);
            objectOutputStream.writeInt(this.count);
            for (int i2 = 0; i2 < this.table.length; i2++) {
                for (Entry<?, ?> entry2 = this.table[i2]; entry2 != null; entry2 = entry2.next) {
                    entry = new Entry<>(0, entry2.key, entry2.value, entry);
                }
            }
        }
        while (entry != null) {
            objectOutputStream.writeObject(entry.key);
            objectOutputStream.writeObject(entry.value);
            entry = entry.next;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        float f2 = objectInputStream.readFields().get("loadFactor", 0.75f);
        if (f2 <= 0.0f || Float.isNaN(f2)) {
            throw new StreamCorruptedException("Illegal load factor: " + f2);
        }
        float fMin = Math.min(Math.max(0.25f, f2), 4.0f);
        int i2 = objectInputStream.readInt();
        int i3 = objectInputStream.readInt();
        if (i3 < 0) {
            throw new StreamCorruptedException("Illegal # of Elements: " + i3);
        }
        int iMax = Math.max(i2, ((int) (i3 / fMin)) + 1);
        int i4 = ((int) ((i3 + (i3 / 20)) / fMin)) + 3;
        if (i4 > i3 && (i4 & 1) == 0) {
            i4--;
        }
        int iMin = Math.min(i4, iMax);
        if (iMin < 0) {
            iMin = iMax;
        }
        SharedSecrets.getJavaOISAccess().checkArray(objectInputStream, Map.Entry[].class, iMin);
        UnsafeHolder.putLoadFactor(this, fMin);
        this.table = new Entry[iMin];
        this.threshold = (int) Math.min(iMin * fMin, 2.1474836E9f);
        this.count = 0;
        while (i3 > 0) {
            reconstitutionPut(this.table, objectInputStream.readObject(), objectInputStream.readObject());
            i3--;
        }
    }

    /* loaded from: rt.jar:java/util/Hashtable$UnsafeHolder.class */
    private static final class UnsafeHolder {
        private static final Unsafe unsafe = Unsafe.getUnsafe();
        private static final long LF_OFFSET;

        private UnsafeHolder() {
            throw new InternalError();
        }

        static {
            try {
                LF_OFFSET = unsafe.objectFieldOffset(Hashtable.class.getDeclaredField("loadFactor"));
            } catch (NoSuchFieldException e2) {
                throw new InternalError();
            }
        }

        static void putLoadFactor(Hashtable<?, ?> hashtable, float f2) {
            unsafe.putFloat(hashtable, LF_OFFSET, f2);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void reconstitutionPut(Entry<?, ?>[] entryArr, K k2, V v2) throws StreamCorruptedException {
        if (v2 == null) {
            throw new StreamCorruptedException();
        }
        int iHashCode = k2.hashCode();
        int length = (iHashCode & Integer.MAX_VALUE) % entryArr.length;
        Entry<K, V> entry = (Entry<K, V>) entryArr[length];
        while (true) {
            Entry entry2 = entry;
            if (entry2 != null) {
                if (entry2.hash != iHashCode || !entry2.key.equals(k2)) {
                    entry = entry2.next;
                } else {
                    throw new StreamCorruptedException();
                }
            } else {
                entryArr[length] = new Entry(iHashCode, k2, v2, entryArr[length]);
                this.count++;
                return;
            }
        }
    }

    /* loaded from: rt.jar:java/util/Hashtable$Entry.class */
    private static class Entry<K, V> implements Map.Entry<K, V> {
        final int hash;
        final K key;
        V value;
        Entry<K, V> next;

        protected Entry(int i2, K k2, V v2, Entry<K, V> entry) {
            this.hash = i2;
            this.key = k2;
            this.value = v2;
            this.next = entry;
        }

        protected Object clone() {
            return new Entry(this.hash, this.key, this.value, this.next == null ? null : (Entry) this.next.clone());
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
            if (v2 == null) {
                throw new NullPointerException();
            }
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
            if (this.key != null ? this.key.equals(entry.getKey()) : entry.getKey() == null) {
                if (this.value != null ? this.value.equals(entry.getValue()) : entry.getValue() == null) {
                    return true;
                }
            }
            return false;
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return this.hash ^ Objects.hashCode(this.value);
        }

        public String toString() {
            return this.key.toString() + "=" + this.value.toString();
        }
    }

    /* loaded from: rt.jar:java/util/Hashtable$Enumerator.class */
    private class Enumerator<T> implements Enumeration<T>, Iterator<T> {
        Entry<?, ?>[] table;
        int index;
        Entry<?, ?> entry;
        Entry<?, ?> lastReturned;
        int type;
        boolean iterator;
        protected int expectedModCount;

        Enumerator(int i2, boolean z2) {
            this.table = Hashtable.this.table;
            this.index = this.table.length;
            this.expectedModCount = Hashtable.this.modCount;
            this.type = i2;
            this.iterator = z2;
        }

        @Override // java.util.Enumeration
        public boolean hasMoreElements() {
            Entry<?, ?> entry = this.entry;
            int i2 = this.index;
            Entry<?, ?>[] entryArr = this.table;
            while (entry == null && i2 > 0) {
                i2--;
                entry = entryArr[i2];
            }
            this.entry = entry;
            this.index = i2;
            return entry != null;
        }

        /* JADX WARN: Type inference failed for: r1v4, types: [T, java.util.Hashtable$Entry, java.util.Hashtable$Entry<?, ?>] */
        @Override // java.util.Enumeration
        public T nextElement() {
            Entry<?, ?> entry = this.entry;
            int i2 = this.index;
            Entry<?, ?>[] entryArr = this.table;
            while (entry == null && i2 > 0) {
                i2--;
                entry = entryArr[i2];
            }
            this.entry = entry;
            this.index = i2;
            if (entry != null) {
                ?? r1 = (T) this.entry;
                this.lastReturned = r1;
                this.entry = r1.next;
                return this.type == 0 ? r1.key : this.type == 1 ? r1.value : r1;
            }
            throw new NoSuchElementException("Hashtable Enumerator");
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return hasMoreElements();
        }

        @Override // java.util.Iterator
        public T next() {
            if (Hashtable.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            return nextElement();
        }

        @Override // java.util.Iterator
        public void remove() {
            if (!this.iterator) {
                throw new UnsupportedOperationException();
            }
            if (this.lastReturned != null) {
                if (Hashtable.this.modCount != this.expectedModCount) {
                    throw new ConcurrentModificationException();
                }
                synchronized (Hashtable.this) {
                    Entry<K, V>[] entryArr = Hashtable.this.table;
                    int length = (this.lastReturned.hash & Integer.MAX_VALUE) % entryArr.length;
                    Entry<K, V> entry = null;
                    for (Entry<K, V> entry2 = entryArr[length]; entry2 != null; entry2 = entry2.next) {
                        if (entry2 != this.lastReturned) {
                            entry = entry2;
                        } else {
                            Hashtable.access$508(Hashtable.this);
                            this.expectedModCount++;
                            if (entry == null) {
                                entryArr[length] = entry2.next;
                            } else {
                                entry.next = entry2.next;
                            }
                            Hashtable.access$210(Hashtable.this);
                            this.lastReturned = null;
                        }
                    }
                    throw new ConcurrentModificationException();
                }
                return;
            }
            throw new IllegalStateException("Hashtable Enumerator");
        }
    }
}
