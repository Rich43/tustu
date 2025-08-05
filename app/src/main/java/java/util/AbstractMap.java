package java.util;

import java.io.Serializable;
import java.util.Map;

/* loaded from: rt.jar:java/util/AbstractMap.class */
public abstract class AbstractMap<K, V> implements Map<K, V> {
    transient Set<K> keySet;
    transient Collection<V> values;

    @Override // java.util.Map
    public abstract Set<Map.Entry<K, V>> entrySet();

    protected AbstractMap() {
    }

    @Override // java.util.Map
    public int size() {
        return entrySet().size();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override // java.util.Map
    public boolean containsValue(Object obj) {
        Iterator<Map.Entry<K, V>> it = entrySet().iterator();
        if (obj == null) {
            while (it.hasNext()) {
                if (it.next().getValue() == null) {
                    return true;
                }
            }
            return false;
        }
        while (it.hasNext()) {
            if (obj.equals(it.next().getValue())) {
                return true;
            }
        }
        return false;
    }

    @Override // java.util.Map
    public boolean containsKey(Object obj) {
        Iterator<Map.Entry<K, V>> it = entrySet().iterator();
        if (obj == null) {
            while (it.hasNext()) {
                if (it.next().getKey() == null) {
                    return true;
                }
            }
            return false;
        }
        while (it.hasNext()) {
            if (obj.equals(it.next().getKey())) {
                return true;
            }
        }
        return false;
    }

    @Override // java.util.Map
    public V get(Object obj) {
        Iterator<Map.Entry<K, V>> it = entrySet().iterator();
        if (obj == null) {
            while (it.hasNext()) {
                Map.Entry<K, V> next = it.next();
                if (next.getKey() == null) {
                    return next.getValue();
                }
            }
            return null;
        }
        while (it.hasNext()) {
            Map.Entry<K, V> next2 = it.next();
            if (obj.equals(next2.getKey())) {
                return next2.getValue();
            }
        }
        return null;
    }

    @Override // java.util.Map
    public V put(K k2, V v2) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map
    public V remove(Object obj) {
        Iterator<Map.Entry<K, V>> it = entrySet().iterator();
        Map.Entry<K, V> entry = null;
        if (obj == null) {
            while (entry == null && it.hasNext()) {
                Map.Entry<K, V> next = it.next();
                if (next.getKey() == null) {
                    entry = next;
                }
            }
        } else {
            while (entry == null && it.hasNext()) {
                Map.Entry<K, V> next2 = it.next();
                if (obj.equals(next2.getKey())) {
                    entry = next2;
                }
            }
        }
        V value = null;
        if (entry != null) {
            value = entry.getValue();
            it.remove();
        }
        return value;
    }

    @Override // java.util.Map
    public void putAll(Map<? extends K, ? extends V> map) {
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override // java.util.Map
    public void clear() {
        entrySet().clear();
    }

    @Override // java.util.Map
    public Set<K> keySet() {
        Set<K> set = this.keySet;
        if (set == null) {
            set = new AbstractSet<K>() { // from class: java.util.AbstractMap.1
                @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
                public Iterator<K> iterator() {
                    return new Iterator<K>() { // from class: java.util.AbstractMap.1.1

                        /* renamed from: i, reason: collision with root package name */
                        private Iterator<Map.Entry<K, V>> f12493i;

                        {
                            this.f12493i = AbstractMap.this.entrySet().iterator();
                        }

                        @Override // java.util.Iterator
                        public boolean hasNext() {
                            return this.f12493i.hasNext();
                        }

                        @Override // java.util.Iterator
                        public K next() {
                            return this.f12493i.next().getKey();
                        }

                        @Override // java.util.Iterator
                        public void remove() {
                            this.f12493i.remove();
                        }
                    };
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                public int size() {
                    return AbstractMap.this.size();
                }

                @Override // java.util.AbstractCollection, java.util.Collection
                public boolean isEmpty() {
                    return AbstractMap.this.isEmpty();
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
                public void clear() {
                    AbstractMap.this.clear();
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                public boolean contains(Object obj) {
                    return AbstractMap.this.containsKey(obj);
                }
            };
            this.keySet = set;
        }
        return set;
    }

    @Override // java.util.Map
    public Collection<V> values() {
        Collection<V> collection = this.values;
        if (collection == null) {
            collection = new AbstractCollection<V>() { // from class: java.util.AbstractMap.2
                @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
                public Iterator<V> iterator() {
                    return new Iterator<V>() { // from class: java.util.AbstractMap.2.1

                        /* renamed from: i, reason: collision with root package name */
                        private Iterator<Map.Entry<K, V>> f12494i;

                        {
                            this.f12494i = AbstractMap.this.entrySet().iterator();
                        }

                        @Override // java.util.Iterator
                        public boolean hasNext() {
                            return this.f12494i.hasNext();
                        }

                        @Override // java.util.Iterator
                        public V next() {
                            return this.f12494i.next().getValue();
                        }

                        @Override // java.util.Iterator
                        public void remove() {
                            this.f12494i.remove();
                        }
                    };
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                public int size() {
                    return AbstractMap.this.size();
                }

                @Override // java.util.AbstractCollection, java.util.Collection
                public boolean isEmpty() {
                    return AbstractMap.this.isEmpty();
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
                public void clear() {
                    AbstractMap.this.clear();
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                public boolean contains(Object obj) {
                    return AbstractMap.this.containsValue(obj);
                }
            };
            this.values = collection;
        }
        return collection;
    }

    @Override // java.util.Map
    public boolean equals(Object obj) {
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
    public int hashCode() {
        int iHashCode = 0;
        Iterator<Map.Entry<K, V>> it = entrySet().iterator();
        while (it.hasNext()) {
            iHashCode += it.next().hashCode();
        }
        return iHashCode;
    }

    public String toString() {
        Iterator<Map.Entry<K, V>> it = entrySet().iterator();
        if (!it.hasNext()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        while (true) {
            Map.Entry<K, V> next = it.next();
            K key = next.getKey();
            V value = next.getValue();
            sb.append(key == this ? "(this Map)" : key);
            sb.append('=');
            sb.append(value == this ? "(this Map)" : value);
            if (!it.hasNext()) {
                return sb.append('}').toString();
            }
            sb.append(',').append(' ');
        }
    }

    protected Object clone() throws CloneNotSupportedException {
        AbstractMap abstractMap = (AbstractMap) super.clone();
        abstractMap.keySet = null;
        abstractMap.values = null;
        return abstractMap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean eq(Object obj, Object obj2) {
        return obj == null ? obj2 == null : obj.equals(obj2);
    }

    /* loaded from: rt.jar:java/util/AbstractMap$SimpleEntry.class */
    public static class SimpleEntry<K, V> implements Map.Entry<K, V>, Serializable {
        private static final long serialVersionUID = -8499721149061103585L;
        private final K key;
        private V value;

        public SimpleEntry(K k2, V v2) {
            this.key = k2;
            this.value = v2;
        }

        public SimpleEntry(Map.Entry<? extends K, ? extends V> entry) {
            this.key = entry.getKey();
            this.value = entry.getValue();
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
            return AbstractMap.eq(this.key, entry.getKey()) && AbstractMap.eq(this.value, entry.getValue());
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return (this.key == null ? 0 : this.key.hashCode()) ^ (this.value == null ? 0 : this.value.hashCode());
        }

        public String toString() {
            return ((Object) this.key) + "=" + ((Object) this.value);
        }
    }

    /* loaded from: rt.jar:java/util/AbstractMap$SimpleImmutableEntry.class */
    public static class SimpleImmutableEntry<K, V> implements Map.Entry<K, V>, Serializable {
        private static final long serialVersionUID = 7138329143949025153L;
        private final K key;
        private final V value;

        public SimpleImmutableEntry(K k2, V v2) {
            this.key = k2;
            this.value = v2;
        }

        public SimpleImmutableEntry(Map.Entry<? extends K, ? extends V> entry) {
            this.key = entry.getKey();
            this.value = entry.getValue();
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
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            return AbstractMap.eq(this.key, entry.getKey()) && AbstractMap.eq(this.value, entry.getValue());
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return (this.key == null ? 0 : this.key.hashCode()) ^ (this.value == null ? 0 : this.value.hashCode());
        }

        public String toString() {
            return ((Object) this.key) + "=" + ((Object) this.value);
        }
    }
}
