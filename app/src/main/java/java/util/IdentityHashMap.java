package java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import sun.misc.SharedSecrets;

/* loaded from: rt.jar:java/util/IdentityHashMap.class */
public class IdentityHashMap<K, V> extends AbstractMap<K, V> implements Map<K, V>, Serializable, Cloneable {
    private static final int DEFAULT_CAPACITY = 32;
    private static final int MINIMUM_CAPACITY = 4;
    private static final int MAXIMUM_CAPACITY = 536870912;
    transient Object[] table;
    int size;
    transient int modCount;
    static final Object NULL_KEY = new Object();
    private transient Set<Map.Entry<K, V>> entrySet;
    private static final long serialVersionUID = 8188218128353913216L;

    private static Object maskNull(Object obj) {
        return obj == null ? NULL_KEY : obj;
    }

    static final Object unmaskNull(Object obj) {
        if (obj == NULL_KEY) {
            return null;
        }
        return obj;
    }

    public IdentityHashMap() {
        init(32);
    }

    public IdentityHashMap(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("expectedMaxSize is negative: " + i2);
        }
        init(capacity(i2));
    }

    private static int capacity(int i2) {
        if (i2 > 178956970) {
            return 536870912;
        }
        if (i2 <= 2) {
            return 4;
        }
        return Integer.highestOneBit(i2 + (i2 << 1));
    }

    private void init(int i2) {
        this.table = new Object[2 * i2];
    }

    public IdentityHashMap(Map<? extends K, ? extends V> map) {
        this((int) ((1 + map.size()) * 1.1d));
        putAll(map);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        return this.size;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean isEmpty() {
        return this.size == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int hash(Object obj, int i2) {
        int iIdentityHashCode = System.identityHashCode(obj);
        return ((iIdentityHashCode << 1) - (iIdentityHashCode << 8)) & (i2 - 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int nextKeyIndex(int i2, int i3) {
        if (i2 + 2 < i3) {
            return i2 + 2;
        }
        return 0;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V get(Object obj) {
        Object objMaskNull = maskNull(obj);
        Object[] objArr = this.table;
        int length = objArr.length;
        int iHash = hash(objMaskNull, length);
        while (true) {
            int i2 = iHash;
            Object obj2 = objArr[i2];
            if (obj2 == objMaskNull) {
                return (V) objArr[i2 + 1];
            }
            if (obj2 == null) {
                return null;
            }
            iHash = nextKeyIndex(i2, length);
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object obj) {
        Object objMaskNull = maskNull(obj);
        Object[] objArr = this.table;
        int length = objArr.length;
        int iHash = hash(objMaskNull, length);
        while (true) {
            int i2 = iHash;
            Object obj2 = objArr[i2];
            if (obj2 == objMaskNull) {
                return true;
            }
            if (obj2 == null) {
                return false;
            }
            iHash = nextKeyIndex(i2, length);
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsValue(Object obj) {
        Object[] objArr = this.table;
        for (int i2 = 1; i2 < objArr.length; i2 += 2) {
            if (objArr[i2] == obj && objArr[i2 - 1] != null) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean containsMapping(Object obj, Object obj2) {
        Object objMaskNull = maskNull(obj);
        Object[] objArr = this.table;
        int length = objArr.length;
        int iHash = hash(objMaskNull, length);
        while (true) {
            int i2 = iHash;
            Object obj3 = objArr[i2];
            if (obj3 == objMaskNull) {
                return objArr[i2 + 1] == obj2;
            }
            if (obj3 == null) {
                return false;
            }
            iHash = nextKeyIndex(i2, length);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0049, code lost:
    
        r0 = r4.size + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x005a, code lost:
    
        if ((r0 + (r0 << 1)) <= r0) goto L19;
     */
    @Override // java.util.AbstractMap, java.util.Map
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public V put(K r5, V r6) {
        /*
            r4 = this;
            r0 = r5
            java.lang.Object r0 = maskNull(r0)
            r7 = r0
        L5:
            r0 = r4
            java.lang.Object[] r0 = r0.table
            r8 = r0
            r0 = r8
            int r0 = r0.length
            r9 = r0
            r0 = r7
            r1 = r9
            int r0 = hash(r0, r1)
            r10 = r0
        L18:
            r0 = r8
            r1 = r10
            r0 = r0[r1]
            r1 = r0
            r11 = r1
            if (r0 == 0) goto L49
            r0 = r11
            r1 = r7
            if (r0 != r1) goto L3d
            r0 = r8
            r1 = r10
            r2 = 1
            int r1 = r1 + r2
            r0 = r0[r1]
            r12 = r0
            r0 = r8
            r1 = r10
            r2 = 1
            int r1 = r1 + r2
            r2 = r6
            r0[r1] = r2
            r0 = r12
            return r0
        L3d:
            r0 = r10
            r1 = r9
            int r0 = nextKeyIndex(r0, r1)
            r10 = r0
            goto L18
        L49:
            r0 = r4
            int r0 = r0.size
            r1 = 1
            int r0 = r0 + r1
            r11 = r0
            r0 = r11
            r1 = r11
            r2 = 1
            int r1 = r1 << r2
            int r0 = r0 + r1
            r1 = r9
            if (r0 <= r1) goto L69
            r0 = r4
            r1 = r9
            boolean r0 = r0.resize(r1)
            if (r0 == 0) goto L69
            goto L5
        L69:
            r0 = r4
            r1 = r0
            int r1 = r1.modCount
            r2 = 1
            int r1 = r1 + r2
            r0.modCount = r1
            r0 = r8
            r1 = r10
            r2 = r7
            r0[r1] = r2
            r0 = r8
            r1 = r10
            r2 = 1
            int r1 = r1 + r2
            r2 = r6
            r0[r1] = r2
            r0 = r4
            r1 = r11
            r0.size = r1
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.IdentityHashMap.put(java.lang.Object, java.lang.Object):java.lang.Object");
    }

    private boolean resize(int i2) {
        int i3;
        int i4 = i2 * 2;
        Object[] objArr = this.table;
        int length = objArr.length;
        if (length == 1073741824) {
            if (this.size == 536870911) {
                throw new IllegalStateException("Capacity exhausted.");
            }
            return false;
        }
        if (length >= i4) {
            return false;
        }
        Object[] objArr2 = new Object[i4];
        for (int i5 = 0; i5 < length; i5 += 2) {
            Object obj = objArr[i5];
            if (obj != null) {
                Object obj2 = objArr[i5 + 1];
                objArr[i5] = null;
                objArr[i5 + 1] = null;
                int iHash = hash(obj, i4);
                while (true) {
                    i3 = iHash;
                    if (objArr2[i3] == null) {
                        break;
                    }
                    iHash = nextKeyIndex(i3, i4);
                }
                objArr2[i3] = obj;
                objArr2[i3 + 1] = obj2;
            }
        }
        this.table = objArr2;
        return true;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void putAll(Map<? extends K, ? extends V> map) {
        int size = map.size();
        if (size == 0) {
            return;
        }
        if (size > this.size) {
            resize(capacity(size));
        }
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V remove(Object obj) {
        Object objMaskNull = maskNull(obj);
        Object[] objArr = this.table;
        int length = objArr.length;
        int iHash = hash(objMaskNull, length);
        while (true) {
            int i2 = iHash;
            Object obj2 = objArr[i2];
            if (obj2 == objMaskNull) {
                this.modCount++;
                this.size--;
                V v2 = (V) objArr[i2 + 1];
                objArr[i2 + 1] = null;
                objArr[i2] = null;
                closeDeletion(i2);
                return v2;
            }
            if (obj2 == null) {
                return null;
            }
            iHash = nextKeyIndex(i2, length);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean removeMapping(Object obj, Object obj2) {
        Object objMaskNull = maskNull(obj);
        Object[] objArr = this.table;
        int length = objArr.length;
        int iHash = hash(objMaskNull, length);
        while (true) {
            int i2 = iHash;
            Object obj3 = objArr[i2];
            if (obj3 == objMaskNull) {
                if (objArr[i2 + 1] != obj2) {
                    return false;
                }
                this.modCount++;
                this.size--;
                objArr[i2] = null;
                objArr[i2 + 1] = null;
                closeDeletion(i2);
                return true;
            }
            if (obj3 == null) {
                return false;
            }
            iHash = nextKeyIndex(i2, length);
        }
    }

    private void closeDeletion(int i2) {
        Object[] objArr = this.table;
        int length = objArr.length;
        int iNextKeyIndex = nextKeyIndex(i2, length);
        while (true) {
            int i3 = iNextKeyIndex;
            Object obj = objArr[i3];
            if (obj != null) {
                int iHash = hash(obj, length);
                if ((i3 < iHash && (iHash <= i2 || i2 <= i3)) || (iHash <= i2 && i2 <= i3)) {
                    objArr[i2] = obj;
                    objArr[i2 + 1] = objArr[i3 + 1];
                    objArr[i3] = null;
                    objArr[i3 + 1] = null;
                    i2 = i3;
                }
                iNextKeyIndex = nextKeyIndex(i3, length);
            } else {
                return;
            }
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        this.modCount++;
        Object[] objArr = this.table;
        for (int i2 = 0; i2 < objArr.length; i2++) {
            objArr[i2] = null;
        }
        this.size = 0;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof IdentityHashMap) {
            IdentityHashMap identityHashMap = (IdentityHashMap) obj;
            if (identityHashMap.size() != this.size) {
                return false;
            }
            Object[] objArr = identityHashMap.table;
            for (int i2 = 0; i2 < objArr.length; i2 += 2) {
                Object obj2 = objArr[i2];
                if (obj2 != null && !containsMapping(obj2, objArr[i2 + 1])) {
                    return false;
                }
            }
            return true;
        }
        if (obj instanceof Map) {
            return entrySet().equals(((Map) obj).entrySet());
        }
        return false;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int hashCode() {
        int iIdentityHashCode = 0;
        Object[] objArr = this.table;
        for (int i2 = 0; i2 < objArr.length; i2 += 2) {
            Object obj = objArr[i2];
            if (obj != null) {
                iIdentityHashCode += System.identityHashCode(unmaskNull(obj)) ^ System.identityHashCode(objArr[i2 + 1]);
            }
        }
        return iIdentityHashCode;
    }

    @Override // java.util.AbstractMap
    public Object clone() {
        try {
            IdentityHashMap identityHashMap = (IdentityHashMap) super.clone();
            identityHashMap.entrySet = null;
            identityHashMap.table = (Object[]) this.table.clone();
            return identityHashMap;
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }

    /* loaded from: rt.jar:java/util/IdentityHashMap$IdentityHashMapIterator.class */
    private abstract class IdentityHashMapIterator<T> implements Iterator<T> {
        int index;
        int expectedModCount;
        int lastReturnedIndex;
        boolean indexValid;
        Object[] traversalTable;

        private IdentityHashMapIterator() {
            this.index = IdentityHashMap.this.size != 0 ? 0 : IdentityHashMap.this.table.length;
            this.expectedModCount = IdentityHashMap.this.modCount;
            this.lastReturnedIndex = -1;
            this.traversalTable = IdentityHashMap.this.table;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            Object[] objArr = this.traversalTable;
            for (int i2 = this.index; i2 < objArr.length; i2 += 2) {
                if (objArr[i2] != null) {
                    this.index = i2;
                    this.indexValid = true;
                    return true;
                }
            }
            this.index = objArr.length;
            return false;
        }

        protected int nextIndex() {
            if (IdentityHashMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            if (!this.indexValid && !hasNext()) {
                throw new NoSuchElementException();
            }
            this.indexValid = false;
            this.lastReturnedIndex = this.index;
            this.index += 2;
            return this.lastReturnedIndex;
        }

        @Override // java.util.Iterator
        public void remove() {
            if (this.lastReturnedIndex == -1) {
                throw new IllegalStateException();
            }
            if (IdentityHashMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            IdentityHashMap identityHashMap = IdentityHashMap.this;
            int i2 = identityHashMap.modCount + 1;
            identityHashMap.modCount = i2;
            this.expectedModCount = i2;
            int i3 = this.lastReturnedIndex;
            this.lastReturnedIndex = -1;
            this.index = i3;
            this.indexValid = false;
            Object[] objArr = this.traversalTable;
            int length = objArr.length;
            int i4 = i3;
            Object obj = objArr[i4];
            objArr[i4] = null;
            objArr[i4 + 1] = null;
            if (objArr != IdentityHashMap.this.table) {
                IdentityHashMap.this.remove(obj);
                this.expectedModCount = IdentityHashMap.this.modCount;
                return;
            }
            IdentityHashMap.this.size--;
            int iNextKeyIndex = IdentityHashMap.nextKeyIndex(i4, length);
            while (true) {
                int i5 = iNextKeyIndex;
                Object obj2 = objArr[i5];
                if (obj2 != null) {
                    int iHash = IdentityHashMap.hash(obj2, length);
                    if ((i5 < iHash && (iHash <= i4 || i4 <= i5)) || (iHash <= i4 && i4 <= i5)) {
                        if (i5 < i3 && i4 >= i3 && this.traversalTable == IdentityHashMap.this.table) {
                            int i6 = length - i3;
                            Object[] objArr2 = new Object[i6];
                            System.arraycopy(objArr, i3, objArr2, 0, i6);
                            this.traversalTable = objArr2;
                            this.index = 0;
                        }
                        objArr[i4] = obj2;
                        objArr[i4 + 1] = objArr[i5 + 1];
                        objArr[i5] = null;
                        objArr[i5 + 1] = null;
                        i4 = i5;
                    }
                    iNextKeyIndex = IdentityHashMap.nextKeyIndex(i5, length);
                } else {
                    return;
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/IdentityHashMap$KeyIterator.class */
    private class KeyIterator extends IdentityHashMap<K, V>.IdentityHashMapIterator<K> {
        private KeyIterator() {
            super();
        }

        @Override // java.util.Iterator
        public K next() {
            return (K) IdentityHashMap.unmaskNull(this.traversalTable[nextIndex()]);
        }
    }

    /* loaded from: rt.jar:java/util/IdentityHashMap$ValueIterator.class */
    private class ValueIterator extends IdentityHashMap<K, V>.IdentityHashMapIterator<V> {
        private ValueIterator() {
            super();
        }

        @Override // java.util.Iterator
        public V next() {
            return (V) this.traversalTable[nextIndex() + 1];
        }
    }

    /* loaded from: rt.jar:java/util/IdentityHashMap$EntryIterator.class */
    private class EntryIterator extends IdentityHashMap<K, V>.IdentityHashMapIterator<Map.Entry<K, V>> {
        private IdentityHashMap<K, V>.EntryIterator.Entry lastReturnedEntry;

        private EntryIterator() {
            super();
        }

        @Override // java.util.Iterator
        public Map.Entry<K, V> next() {
            this.lastReturnedEntry = new Entry(nextIndex());
            return this.lastReturnedEntry;
        }

        @Override // java.util.IdentityHashMap.IdentityHashMapIterator, java.util.Iterator
        public void remove() {
            this.lastReturnedIndex = null == this.lastReturnedEntry ? -1 : ((Entry) this.lastReturnedEntry).index;
            super.remove();
            ((Entry) this.lastReturnedEntry).index = this.lastReturnedIndex;
            this.lastReturnedEntry = null;
        }

        /* loaded from: rt.jar:java/util/IdentityHashMap$EntryIterator$Entry.class */
        private class Entry implements Map.Entry<K, V> {
            private int index;

            private Entry(int i2) {
                this.index = i2;
            }

            @Override // java.util.Map.Entry
            public K getKey() {
                checkIndexForEntryUse();
                return (K) IdentityHashMap.unmaskNull(EntryIterator.this.traversalTable[this.index]);
            }

            @Override // java.util.Map.Entry
            public V getValue() {
                checkIndexForEntryUse();
                return (V) EntryIterator.this.traversalTable[this.index + 1];
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Map.Entry
            public V setValue(V v2) {
                checkIndexForEntryUse();
                V v3 = (V) EntryIterator.this.traversalTable[this.index + 1];
                EntryIterator.this.traversalTable[this.index + 1] = v2;
                if (EntryIterator.this.traversalTable != IdentityHashMap.this.table) {
                    IdentityHashMap.this.put(EntryIterator.this.traversalTable[this.index], v2);
                }
                return v3;
            }

            @Override // java.util.Map.Entry
            public boolean equals(Object obj) {
                if (this.index < 0) {
                    return super.equals(obj);
                }
                if (!(obj instanceof Map.Entry)) {
                    return false;
                }
                Map.Entry entry = (Map.Entry) obj;
                return entry.getKey() == IdentityHashMap.unmaskNull(EntryIterator.this.traversalTable[this.index]) && entry.getValue() == EntryIterator.this.traversalTable[this.index + 1];
            }

            @Override // java.util.Map.Entry
            public int hashCode() {
                if (EntryIterator.this.lastReturnedIndex < 0) {
                    return super.hashCode();
                }
                return System.identityHashCode(IdentityHashMap.unmaskNull(EntryIterator.this.traversalTable[this.index])) ^ System.identityHashCode(EntryIterator.this.traversalTable[this.index + 1]);
            }

            public String toString() {
                if (this.index < 0) {
                    return super.toString();
                }
                return IdentityHashMap.unmaskNull(EntryIterator.this.traversalTable[this.index]) + "=" + EntryIterator.this.traversalTable[this.index + 1];
            }

            private void checkIndexForEntryUse() {
                if (this.index < 0) {
                    throw new IllegalStateException("Entry was removed");
                }
            }
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

    /* loaded from: rt.jar:java/util/IdentityHashMap$KeySet.class */
    private class KeySet extends AbstractSet<K> {
        private KeySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return IdentityHashMap.this.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return IdentityHashMap.this.containsKey(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            int i2 = IdentityHashMap.this.size;
            IdentityHashMap.this.remove(obj);
            return IdentityHashMap.this.size != i2;
        }

        @Override // java.util.AbstractSet, java.util.AbstractCollection, java.util.Collection
        public boolean removeAll(Collection<?> collection) {
            Objects.requireNonNull(collection);
            boolean z2 = false;
            Iterator<K> it = iterator();
            while (it.hasNext()) {
                if (collection.contains(it.next())) {
                    it.remove();
                    z2 = true;
                }
            }
            return z2;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            IdentityHashMap.this.clear();
        }

        @Override // java.util.AbstractSet, java.util.Collection, java.util.List
        public int hashCode() {
            int iIdentityHashCode = 0;
            Iterator<K> it = iterator();
            while (it.hasNext()) {
                iIdentityHashCode += System.identityHashCode(it.next());
            }
            return iIdentityHashCode;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public Object[] toArray() {
            return toArray(new Object[0]);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v23 */
        /* JADX WARN: Type inference failed for: r0v29, types: [java.lang.Object[]] */
        @Override // java.util.AbstractCollection, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            int i2 = IdentityHashMap.this.modCount;
            int size = size();
            if (tArr.length < size) {
                tArr = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), size);
            }
            Object[] objArr = IdentityHashMap.this.table;
            int i3 = 0;
            for (int i4 = 0; i4 < objArr.length; i4 += 2) {
                Object obj = objArr[i4];
                if (obj != null) {
                    if (i3 >= size) {
                        throw new ConcurrentModificationException();
                    }
                    int i5 = i3;
                    i3++;
                    tArr[i5] = IdentityHashMap.unmaskNull(obj);
                }
            }
            if (i3 < size || i2 != IdentityHashMap.this.modCount) {
                throw new ConcurrentModificationException();
            }
            if (i3 < tArr.length) {
                tArr[i3] = null;
            }
            return tArr;
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Spliterator<K> spliterator() {
            return new KeySpliterator(IdentityHashMap.this, 0, -1, 0, 0);
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

    /* loaded from: rt.jar:java/util/IdentityHashMap$Values.class */
    private class Values extends AbstractCollection<V> {
        private Values() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return IdentityHashMap.this.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return IdentityHashMap.this.containsValue(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            Iterator<V> it = iterator();
            while (it.hasNext()) {
                if (it.next() == obj) {
                    it.remove();
                    return true;
                }
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            IdentityHashMap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public Object[] toArray() {
            return toArray(new Object[0]);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v23 */
        /* JADX WARN: Type inference failed for: r0v29, types: [java.lang.Object[]] */
        @Override // java.util.AbstractCollection, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            int i2 = IdentityHashMap.this.modCount;
            int size = size();
            if (tArr.length < size) {
                tArr = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), size);
            }
            Object[] objArr = IdentityHashMap.this.table;
            int i3 = 0;
            for (int i4 = 0; i4 < objArr.length; i4 += 2) {
                if (objArr[i4] != null) {
                    if (i3 >= size) {
                        throw new ConcurrentModificationException();
                    }
                    int i5 = i3;
                    i3++;
                    tArr[i5] = objArr[i4 + 1];
                }
            }
            if (i3 < size || i2 != IdentityHashMap.this.modCount) {
                throw new ConcurrentModificationException();
            }
            if (i3 < tArr.length) {
                tArr[i3] = null;
            }
            return tArr;
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Spliterator<V> spliterator() {
            return new ValueSpliterator(IdentityHashMap.this, 0, -1, 0, 0);
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

    /* loaded from: rt.jar:java/util/IdentityHashMap$EntrySet.class */
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
            return IdentityHashMap.this.containsMapping(entry.getKey(), entry.getValue());
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            return IdentityHashMap.this.removeMapping(entry.getKey(), entry.getValue());
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return IdentityHashMap.this.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            IdentityHashMap.this.clear();
        }

        @Override // java.util.AbstractSet, java.util.AbstractCollection, java.util.Collection
        public boolean removeAll(Collection<?> collection) {
            Objects.requireNonNull(collection);
            boolean z2 = false;
            Iterator<Map.Entry<K, V>> it = iterator();
            while (it.hasNext()) {
                if (collection.contains(it.next())) {
                    it.remove();
                    z2 = true;
                }
            }
            return z2;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public Object[] toArray() {
            return toArray(new Object[0]);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v23 */
        /* JADX WARN: Type inference failed for: r0v29, types: [java.lang.Object[]] */
        @Override // java.util.AbstractCollection, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            int i2 = IdentityHashMap.this.modCount;
            int size = size();
            if (tArr.length < size) {
                tArr = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), size);
            }
            Object[] objArr = IdentityHashMap.this.table;
            int i3 = 0;
            for (int i4 = 0; i4 < objArr.length; i4 += 2) {
                Object obj = objArr[i4];
                if (obj != null) {
                    if (i3 >= size) {
                        throw new ConcurrentModificationException();
                    }
                    int i5 = i3;
                    i3++;
                    tArr[i5] = new AbstractMap.SimpleEntry(IdentityHashMap.unmaskNull(obj), objArr[i4 + 1]);
                }
            }
            if (i3 < size || i2 != IdentityHashMap.this.modCount) {
                throw new ConcurrentModificationException();
            }
            if (i3 < tArr.length) {
                tArr[i3] = null;
            }
            return tArr;
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Spliterator<Map.Entry<K, V>> spliterator() {
            return new EntrySpliterator(IdentityHashMap.this, 0, -1, 0, 0);
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(this.size);
        Object[] objArr = this.table;
        for (int i2 = 0; i2 < objArr.length; i2 += 2) {
            Object obj = objArr[i2];
            if (obj != null) {
                objectOutputStream.writeObject(unmaskNull(obj));
                objectOutputStream.writeObject(objArr[i2 + 1]);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.readFields();
        int i2 = objectInputStream.readInt();
        if (i2 < 0) {
            throw new StreamCorruptedException("Illegal mappings count: " + i2);
        }
        int iCapacity = capacity(i2);
        SharedSecrets.getJavaOISAccess().checkArray(objectInputStream, Object[].class, iCapacity * 2);
        this.size = i2;
        init(iCapacity);
        for (int i3 = 0; i3 < i2; i3++) {
            putForCreate(objectInputStream.readObject(), objectInputStream.readObject());
        }
    }

    private void putForCreate(K k2, V v2) throws StreamCorruptedException {
        Object objMaskNull = maskNull(k2);
        Object[] objArr = this.table;
        int length = objArr.length;
        int iHash = hash(objMaskNull, length);
        while (true) {
            int i2 = iHash;
            Object obj = objArr[i2];
            if (obj != null) {
                if (obj == objMaskNull) {
                    throw new StreamCorruptedException();
                }
                iHash = nextKeyIndex(i2, length);
            } else {
                objArr[i2] = objMaskNull;
                objArr[i2 + 1] = v2;
                return;
            }
        }
    }

    @Override // java.util.Map
    public void forEach(BiConsumer<? super K, ? super V> biConsumer) {
        Objects.requireNonNull(biConsumer);
        int i2 = this.modCount;
        Object[] objArr = this.table;
        for (int i3 = 0; i3 < objArr.length; i3 += 2) {
            Object obj = objArr[i3];
            if (obj != null) {
                biConsumer.accept((Object) unmaskNull(obj), objArr[i3 + 1]);
            }
            if (this.modCount != i2) {
                throw new ConcurrentModificationException();
            }
        }
    }

    @Override // java.util.Map
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        int i2 = this.modCount;
        Object[] objArr = this.table;
        for (int i3 = 0; i3 < objArr.length; i3 += 2) {
            Object obj = objArr[i3];
            if (obj != null) {
                objArr[i3 + 1] = biFunction.apply((Object) unmaskNull(obj), objArr[i3 + 1]);
            }
            if (this.modCount != i2) {
                throw new ConcurrentModificationException();
            }
        }
    }

    /* loaded from: rt.jar:java/util/IdentityHashMap$IdentityHashMapSpliterator.class */
    static class IdentityHashMapSpliterator<K, V> {
        final IdentityHashMap<K, V> map;
        int index;
        int fence;
        int est;
        int expectedModCount;

        IdentityHashMapSpliterator(IdentityHashMap<K, V> identityHashMap, int i2, int i3, int i4, int i5) {
            this.map = identityHashMap;
            this.index = i2;
            this.fence = i3;
            this.est = i4;
            this.expectedModCount = i5;
        }

        final int getFence() {
            int i2 = this.fence;
            int i3 = i2;
            if (i2 < 0) {
                this.est = this.map.size;
                this.expectedModCount = this.map.modCount;
                int length = this.map.table.length;
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

    /* loaded from: rt.jar:java/util/IdentityHashMap$KeySpliterator.class */
    static final class KeySpliterator<K, V> extends IdentityHashMapSpliterator<K, V> implements Spliterator<K> {
        KeySpliterator(IdentityHashMap<K, V> identityHashMap, int i2, int i3, int i4, int i5) {
            super(identityHashMap, i2, i3, i4, i5);
        }

        @Override // java.util.Spliterator
        public KeySpliterator<K, V> trySplit() {
            int fence = getFence();
            int i2 = this.index;
            int i3 = ((i2 + fence) >>> 1) & (-2);
            if (i2 >= i3) {
                return null;
            }
            IdentityHashMap<K, V> identityHashMap = this.map;
            this.index = i3;
            int i4 = this.est >>> 1;
            this.est = i4;
            return new KeySpliterator<>(identityHashMap, i2, i3, i4, this.expectedModCount);
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super K> consumer) {
            Object[] objArr;
            if (consumer == null) {
                throw new NullPointerException();
            }
            IdentityHashMap<K, V> identityHashMap = this.map;
            if (identityHashMap != null && (objArr = identityHashMap.table) != null) {
                int i2 = this.index;
                if (i2 >= 0) {
                    int fence = getFence();
                    this.index = fence;
                    if (fence <= objArr.length) {
                        for (int i3 = i2; i3 < fence; i3 += 2) {
                            Object obj = objArr[i3];
                            if (obj != null) {
                                consumer.accept((Object) IdentityHashMap.unmaskNull(obj));
                            }
                        }
                        if (identityHashMap.modCount == this.expectedModCount) {
                            return;
                        }
                    }
                }
            }
            throw new ConcurrentModificationException();
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super K> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Object[] objArr = this.map.table;
            int fence = getFence();
            while (this.index < fence) {
                Object obj = objArr[this.index];
                this.index += 2;
                if (obj != null) {
                    consumer.accept((Object) IdentityHashMap.unmaskNull(obj));
                    if (this.map.modCount != this.expectedModCount) {
                        throw new ConcurrentModificationException();
                    }
                    return true;
                }
            }
            return false;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return ((this.fence < 0 || this.est == this.map.size) ? 64 : 0) | 1;
        }
    }

    /* loaded from: rt.jar:java/util/IdentityHashMap$ValueSpliterator.class */
    static final class ValueSpliterator<K, V> extends IdentityHashMapSpliterator<K, V> implements Spliterator<V> {
        ValueSpliterator(IdentityHashMap<K, V> identityHashMap, int i2, int i3, int i4, int i5) {
            super(identityHashMap, i2, i3, i4, i5);
        }

        @Override // java.util.Spliterator
        public ValueSpliterator<K, V> trySplit() {
            int fence = getFence();
            int i2 = this.index;
            int i3 = ((i2 + fence) >>> 1) & (-2);
            if (i2 >= i3) {
                return null;
            }
            IdentityHashMap<K, V> identityHashMap = this.map;
            this.index = i3;
            int i4 = this.est >>> 1;
            this.est = i4;
            return new ValueSpliterator<>(identityHashMap, i2, i3, i4, this.expectedModCount);
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super V> consumer) {
            Object[] objArr;
            if (consumer == null) {
                throw new NullPointerException();
            }
            IdentityHashMap<K, V> identityHashMap = this.map;
            if (identityHashMap != null && (objArr = identityHashMap.table) != null) {
                int i2 = this.index;
                if (i2 >= 0) {
                    int fence = getFence();
                    this.index = fence;
                    if (fence <= objArr.length) {
                        for (int i3 = i2; i3 < fence; i3 += 2) {
                            if (objArr[i3] != null) {
                                consumer.accept(objArr[i3 + 1]);
                            }
                        }
                        if (identityHashMap.modCount == this.expectedModCount) {
                            return;
                        }
                    }
                }
            }
            throw new ConcurrentModificationException();
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super V> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Object[] objArr = this.map.table;
            int fence = getFence();
            while (this.index < fence) {
                Object obj = objArr[this.index];
                Object obj2 = objArr[this.index + 1];
                this.index += 2;
                if (obj != null) {
                    consumer.accept(obj2);
                    if (this.map.modCount != this.expectedModCount) {
                        throw new ConcurrentModificationException();
                    }
                    return true;
                }
            }
            return false;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return (this.fence < 0 || this.est == this.map.size) ? 64 : 0;
        }
    }

    /* loaded from: rt.jar:java/util/IdentityHashMap$EntrySpliterator.class */
    static final class EntrySpliterator<K, V> extends IdentityHashMapSpliterator<K, V> implements Spliterator<Map.Entry<K, V>> {
        EntrySpliterator(IdentityHashMap<K, V> identityHashMap, int i2, int i3, int i4, int i5) {
            super(identityHashMap, i2, i3, i4, i5);
        }

        @Override // java.util.Spliterator
        public EntrySpliterator<K, V> trySplit() {
            int fence = getFence();
            int i2 = this.index;
            int i3 = ((i2 + fence) >>> 1) & (-2);
            if (i2 >= i3) {
                return null;
            }
            IdentityHashMap<K, V> identityHashMap = this.map;
            this.index = i3;
            int i4 = this.est >>> 1;
            this.est = i4;
            return new EntrySpliterator<>(identityHashMap, i2, i3, i4, this.expectedModCount);
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super Map.Entry<K, V>> consumer) {
            Object[] objArr;
            if (consumer == null) {
                throw new NullPointerException();
            }
            IdentityHashMap<K, V> identityHashMap = this.map;
            if (identityHashMap != null && (objArr = identityHashMap.table) != null) {
                int i2 = this.index;
                if (i2 >= 0) {
                    int fence = getFence();
                    this.index = fence;
                    if (fence <= objArr.length) {
                        for (int i3 = i2; i3 < fence; i3 += 2) {
                            Object obj = objArr[i3];
                            if (obj != null) {
                                consumer.accept(new AbstractMap.SimpleImmutableEntry(IdentityHashMap.unmaskNull(obj), objArr[i3 + 1]));
                            }
                        }
                        if (identityHashMap.modCount == this.expectedModCount) {
                            return;
                        }
                    }
                }
            }
            throw new ConcurrentModificationException();
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super Map.Entry<K, V>> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Object[] objArr = this.map.table;
            int fence = getFence();
            while (this.index < fence) {
                Object obj = objArr[this.index];
                Object obj2 = objArr[this.index + 1];
                this.index += 2;
                if (obj != null) {
                    consumer.accept(new AbstractMap.SimpleImmutableEntry(IdentityHashMap.unmaskNull(obj), obj2));
                    if (this.map.modCount != this.expectedModCount) {
                        throw new ConcurrentModificationException();
                    }
                    return true;
                }
            }
            return false;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return ((this.fence < 0 || this.est == this.map.size) ? 64 : 0) | 1;
        }
    }
}
