package java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.Enum;
import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.Map;
import sun.misc.SharedSecrets;

/* loaded from: rt.jar:java/util/EnumMap.class */
public class EnumMap<K extends Enum<K>, V> extends AbstractMap<K, V> implements Serializable, Cloneable {
    private final Class<K> keyType;
    private transient K[] keyUniverse;
    private transient Object[] vals;
    private transient int size;
    private static final Object NULL = new Object() { // from class: java.util.EnumMap.1
        public int hashCode() {
            return 0;
        }

        public String toString() {
            return "java.util.EnumMap.NULL";
        }
    };
    private static final Enum<?>[] ZERO_LENGTH_ENUM_ARRAY = new Enum[0];
    private transient Set<Map.Entry<K, V>> entrySet;
    private static final long serialVersionUID = 458661240069192865L;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractMap, java.util.Map
    public /* bridge */ /* synthetic */ Object put(Object obj, Object obj2) {
        return put((EnumMap<K, V>) obj, (Enum) obj2);
    }

    static /* synthetic */ int access$210(EnumMap enumMap) {
        int i2 = enumMap.size;
        enumMap.size = i2 - 1;
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object maskNull(Object obj) {
        return obj == null ? NULL : obj;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public V unmaskNull(Object obj) {
        if (obj == NULL) {
            return null;
        }
        return obj;
    }

    public EnumMap(Class<K> cls) {
        this.size = 0;
        this.keyType = cls;
        this.keyUniverse = (K[]) getKeyUniverse(cls);
        this.vals = new Object[this.keyUniverse.length];
    }

    public EnumMap(EnumMap<K, ? extends V> enumMap) {
        this.size = 0;
        this.keyType = enumMap.keyType;
        this.keyUniverse = enumMap.keyUniverse;
        this.vals = (Object[]) enumMap.vals.clone();
        this.size = enumMap.size;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public EnumMap(Map<K, ? extends V> map) {
        this.size = 0;
        if (map instanceof EnumMap) {
            EnumMap enumMap = (EnumMap) map;
            this.keyType = enumMap.keyType;
            this.keyUniverse = enumMap.keyUniverse;
            this.vals = (Object[]) enumMap.vals.clone();
            this.size = enumMap.size;
            return;
        }
        if (map.isEmpty()) {
            throw new IllegalArgumentException("Specified map is empty");
        }
        this.keyType = ((Enum) map.keySet().iterator().next()).getDeclaringClass();
        this.keyUniverse = (K[]) getKeyUniverse(this.keyType);
        this.vals = new Object[this.keyUniverse.length];
        putAll(map);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        return this.size;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsValue(Object obj) {
        Object objMaskNull = maskNull(obj);
        for (Object obj2 : this.vals) {
            if (objMaskNull.equals(obj2)) {
                return true;
            }
        }
        return false;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object obj) {
        return isValidKey(obj) && this.vals[((Enum) obj).ordinal()] != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean containsMapping(Object obj, Object obj2) {
        return isValidKey(obj) && maskNull(obj2).equals(this.vals[((Enum) obj).ordinal()]);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V get(Object obj) {
        if (isValidKey(obj)) {
            return unmaskNull(this.vals[((Enum) obj).ordinal()]);
        }
        return null;
    }

    public V put(K k2, V v2) {
        typeCheck(k2);
        int iOrdinal = k2.ordinal();
        Object obj = this.vals[iOrdinal];
        this.vals[iOrdinal] = maskNull(v2);
        if (obj == null) {
            this.size++;
        }
        return unmaskNull(obj);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V remove(Object obj) {
        if (!isValidKey(obj)) {
            return null;
        }
        int iOrdinal = ((Enum) obj).ordinal();
        Object obj2 = this.vals[iOrdinal];
        this.vals[iOrdinal] = null;
        if (obj2 != null) {
            this.size--;
        }
        return unmaskNull(obj2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean removeMapping(Object obj, Object obj2) {
        if (!isValidKey(obj)) {
            return false;
        }
        int iOrdinal = ((Enum) obj).ordinal();
        if (maskNull(obj2).equals(this.vals[iOrdinal])) {
            this.vals[iOrdinal] = null;
            this.size--;
            return true;
        }
        return false;
    }

    private boolean isValidKey(Object obj) {
        if (obj == null) {
            return false;
        }
        Class<?> cls = obj.getClass();
        return cls == this.keyType || cls.getSuperclass() == this.keyType;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void putAll(Map<? extends K, ? extends V> map) {
        if (map instanceof EnumMap) {
            EnumMap enumMap = (EnumMap) map;
            if (enumMap.keyType != this.keyType) {
                if (enumMap.isEmpty()) {
                    return;
                } else {
                    throw new ClassCastException(((Object) enumMap.keyType) + " != " + ((Object) this.keyType));
                }
            }
            for (int i2 = 0; i2 < this.keyUniverse.length; i2++) {
                Object obj = enumMap.vals[i2];
                if (obj != null) {
                    if (this.vals[i2] == null) {
                        this.size++;
                    }
                    this.vals[i2] = obj;
                }
            }
            return;
        }
        super.putAll(map);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        Arrays.fill(this.vals, (Object) null);
        this.size = 0;
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

    /* loaded from: rt.jar:java/util/EnumMap$KeySet.class */
    private class KeySet extends AbstractSet<K> {
        private KeySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return EnumMap.this.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return EnumMap.this.containsKey(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            int i2 = EnumMap.this.size;
            EnumMap.this.remove(obj);
            return EnumMap.this.size != i2;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            EnumMap.this.clear();
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

    /* loaded from: rt.jar:java/util/EnumMap$Values.class */
    private class Values extends AbstractCollection<V> {
        private Values() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return EnumMap.this.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return EnumMap.this.containsValue(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            Object objMaskNull = EnumMap.this.maskNull(obj);
            for (int i2 = 0; i2 < EnumMap.this.vals.length; i2++) {
                if (objMaskNull.equals(EnumMap.this.vals[i2])) {
                    EnumMap.this.vals[i2] = null;
                    EnumMap.access$210(EnumMap.this);
                    return true;
                }
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            EnumMap.this.clear();
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

    /* loaded from: rt.jar:java/util/EnumMap$EntrySet.class */
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
            return EnumMap.this.containsMapping(entry.getKey(), entry.getValue());
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            return EnumMap.this.removeMapping(entry.getKey(), entry.getValue());
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return EnumMap.this.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public void clear() {
            EnumMap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public Object[] toArray() {
            return fillEntryArray(new Object[EnumMap.this.size]);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v15, types: [java.lang.Object[]] */
        @Override // java.util.AbstractCollection, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            int size = size();
            if (tArr.length < size) {
                tArr = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), size);
            }
            if (tArr.length > size) {
                tArr[size] = null;
            }
            return (T[]) fillEntryArray(tArr);
        }

        private Object[] fillEntryArray(Object[] objArr) {
            int i2 = 0;
            for (int i3 = 0; i3 < EnumMap.this.vals.length; i3++) {
                if (EnumMap.this.vals[i3] != null) {
                    int i4 = i2;
                    i2++;
                    objArr[i4] = new AbstractMap.SimpleEntry(EnumMap.this.keyUniverse[i3], EnumMap.this.unmaskNull(EnumMap.this.vals[i3]));
                }
            }
            return objArr;
        }
    }

    /* loaded from: rt.jar:java/util/EnumMap$EnumMapIterator.class */
    private abstract class EnumMapIterator<T> implements Iterator<T> {
        int index;
        int lastReturnedIndex;

        private EnumMapIterator() {
            this.index = 0;
            this.lastReturnedIndex = -1;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            while (this.index < EnumMap.this.vals.length && EnumMap.this.vals[this.index] == null) {
                this.index++;
            }
            return this.index != EnumMap.this.vals.length;
        }

        @Override // java.util.Iterator
        public void remove() {
            checkLastReturnedIndex();
            if (EnumMap.this.vals[this.lastReturnedIndex] != null) {
                EnumMap.this.vals[this.lastReturnedIndex] = null;
                EnumMap.access$210(EnumMap.this);
            }
            this.lastReturnedIndex = -1;
        }

        private void checkLastReturnedIndex() {
            if (this.lastReturnedIndex < 0) {
                throw new IllegalStateException();
            }
        }
    }

    /* loaded from: rt.jar:java/util/EnumMap$KeyIterator.class */
    private class KeyIterator extends EnumMap<K, V>.EnumMapIterator<K> {
        private KeyIterator() {
            super();
        }

        @Override // java.util.Iterator
        public K next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int i2 = this.index;
            this.index = i2 + 1;
            this.lastReturnedIndex = i2;
            return (K) EnumMap.this.keyUniverse[this.lastReturnedIndex];
        }
    }

    /* loaded from: rt.jar:java/util/EnumMap$ValueIterator.class */
    private class ValueIterator extends EnumMap<K, V>.EnumMapIterator<V> {
        private ValueIterator() {
            super();
        }

        @Override // java.util.Iterator
        public V next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int i2 = this.index;
            this.index = i2 + 1;
            this.lastReturnedIndex = i2;
            return (V) EnumMap.this.unmaskNull(EnumMap.this.vals[this.lastReturnedIndex]);
        }
    }

    /* loaded from: rt.jar:java/util/EnumMap$EntryIterator.class */
    private class EntryIterator extends EnumMap<K, V>.EnumMapIterator<Map.Entry<K, V>> {
        private EnumMap<K, V>.EntryIterator.Entry lastReturnedEntry;

        private EntryIterator() {
            super();
        }

        @Override // java.util.Iterator
        public Map.Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int i2 = this.index;
            this.index = i2 + 1;
            this.lastReturnedEntry = new Entry(i2);
            return this.lastReturnedEntry;
        }

        @Override // java.util.EnumMap.EnumMapIterator, java.util.Iterator
        public void remove() {
            this.lastReturnedIndex = null == this.lastReturnedEntry ? -1 : ((Entry) this.lastReturnedEntry).index;
            super.remove();
            ((Entry) this.lastReturnedEntry).index = this.lastReturnedIndex;
            this.lastReturnedEntry = null;
        }

        /* loaded from: rt.jar:java/util/EnumMap$EntryIterator$Entry.class */
        private class Entry implements Map.Entry<K, V> {
            private int index;

            private Entry(int i2) {
                this.index = i2;
            }

            @Override // java.util.Map.Entry
            public K getKey() {
                checkIndexForEntryUse();
                return (K) EnumMap.this.keyUniverse[this.index];
            }

            @Override // java.util.Map.Entry
            public V getValue() {
                checkIndexForEntryUse();
                return (V) EnumMap.this.unmaskNull(EnumMap.this.vals[this.index]);
            }

            @Override // java.util.Map.Entry
            public V setValue(V v2) {
                checkIndexForEntryUse();
                V v3 = (V) EnumMap.this.unmaskNull(EnumMap.this.vals[this.index]);
                EnumMap.this.vals[this.index] = EnumMap.this.maskNull(v2);
                return v3;
            }

            @Override // java.util.Map.Entry
            public boolean equals(Object obj) {
                if (this.index < 0) {
                    return obj == this;
                }
                if (!(obj instanceof Map.Entry)) {
                    return false;
                }
                Map.Entry entry = (Map.Entry) obj;
                Object objUnmaskNull = EnumMap.this.unmaskNull(EnumMap.this.vals[this.index]);
                Object value = entry.getValue();
                return entry.getKey() == EnumMap.this.keyUniverse[this.index] && (objUnmaskNull == value || (objUnmaskNull != null && objUnmaskNull.equals(value)));
            }

            @Override // java.util.Map.Entry
            public int hashCode() {
                if (this.index >= 0) {
                    return EnumMap.this.entryHashCode(this.index);
                }
                return super.hashCode();
            }

            public String toString() {
                if (this.index >= 0) {
                    return ((Object) EnumMap.this.keyUniverse[this.index]) + "=" + EnumMap.this.unmaskNull(EnumMap.this.vals[this.index]);
                }
                return super.toString();
            }

            private void checkIndexForEntryUse() {
                if (this.index < 0) {
                    throw new IllegalStateException("Entry was removed");
                }
            }
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof EnumMap) {
            return equals((EnumMap<?, ?>) obj);
        }
        if (!(obj instanceof Map)) {
            return false;
        }
        Map map = (Map) obj;
        if (this.size != map.size()) {
            return false;
        }
        for (int i2 = 0; i2 < this.keyUniverse.length; i2++) {
            if (null != this.vals[i2]) {
                K k2 = this.keyUniverse[i2];
                V vUnmaskNull = unmaskNull(this.vals[i2]);
                if (null == vUnmaskNull) {
                    if (null != map.get(k2) || !map.containsKey(k2)) {
                        return false;
                    }
                } else if (!vUnmaskNull.equals(map.get(k2))) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean equals(EnumMap<?, ?> enumMap) {
        if (enumMap.keyType != this.keyType) {
            return this.size == 0 && enumMap.size == 0;
        }
        for (int i2 = 0; i2 < this.keyUniverse.length; i2++) {
            Object obj = this.vals[i2];
            Object obj2 = enumMap.vals[i2];
            if (obj2 != obj && (obj2 == null || !obj2.equals(obj))) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int hashCode() {
        int iEntryHashCode = 0;
        for (int i2 = 0; i2 < this.keyUniverse.length; i2++) {
            if (null != this.vals[i2]) {
                iEntryHashCode += entryHashCode(i2);
            }
        }
        return iEntryHashCode;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int entryHashCode(int i2) {
        return this.keyUniverse[i2].hashCode() ^ this.vals[i2].hashCode();
    }

    @Override // java.util.AbstractMap
    public EnumMap<K, V> clone() {
        try {
            EnumMap<K, V> enumMap = (EnumMap) super.clone();
            enumMap.vals = (Object[]) enumMap.vals.clone();
            enumMap.entrySet = null;
            return enumMap;
        } catch (CloneNotSupportedException e2) {
            throw new AssertionError();
        }
    }

    private void typeCheck(K k2) {
        Class<?> cls = k2.getClass();
        if (cls != this.keyType && cls.getSuperclass() != this.keyType) {
            throw new ClassCastException(((Object) cls) + " != " + ((Object) this.keyType));
        }
    }

    private static <K extends Enum<K>> K[] getKeyUniverse(Class<K> cls) {
        return (K[]) SharedSecrets.getJavaLangAccess().getEnumConstantsShared(cls);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(this.size);
        int i2 = this.size;
        int i3 = 0;
        while (i2 > 0) {
            if (null != this.vals[i3]) {
                objectOutputStream.writeObject(this.keyUniverse[i3]);
                objectOutputStream.writeObject(unmaskNull(this.vals[i3]));
                i2--;
            }
            i3++;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.keyUniverse = (K[]) getKeyUniverse(this.keyType);
        this.vals = new Object[this.keyUniverse.length];
        int i2 = objectInputStream.readInt();
        for (int i3 = 0; i3 < i2; i3++) {
            put((EnumMap<K, V>) objectInputStream.readObject(), (Enum) objectInputStream.readObject());
        }
    }
}
