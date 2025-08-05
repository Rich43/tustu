package com.sun.javafx.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javafx.beans.InvalidationListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

/* loaded from: jfxrt.jar:com/sun/javafx/collections/ObservableMapWrapper.class */
public class ObservableMapWrapper<K, V> implements ObservableMap<K, V> {
    private ObservableMapWrapper<K, V>.ObservableEntrySet entrySet;
    private ObservableMapWrapper<K, V>.ObservableKeySet keySet;
    private ObservableMapWrapper<K, V>.ObservableValues values;
    private MapListenerHelper<K, V> listenerHelper;
    private final Map<K, V> backingMap;

    public ObservableMapWrapper(Map<K, V> map) {
        this.backingMap = map;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/collections/ObservableMapWrapper$SimpleChange.class */
    private class SimpleChange extends MapChangeListener.Change<K, V> {
        private final K key;
        private final V old;
        private final V added;
        private final boolean wasAdded;
        private final boolean wasRemoved;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ObservableMapWrapper.class.desiredAssertionStatus();
        }

        public SimpleChange(K key, V old, V added, boolean wasAdded, boolean wasRemoved) {
            super(ObservableMapWrapper.this);
            if (!$assertionsDisabled && !wasAdded && !wasRemoved) {
                throw new AssertionError();
            }
            this.key = key;
            this.old = old;
            this.added = added;
            this.wasAdded = wasAdded;
            this.wasRemoved = wasRemoved;
        }

        @Override // javafx.collections.MapChangeListener.Change
        public boolean wasAdded() {
            return this.wasAdded;
        }

        @Override // javafx.collections.MapChangeListener.Change
        public boolean wasRemoved() {
            return this.wasRemoved;
        }

        @Override // javafx.collections.MapChangeListener.Change
        public K getKey() {
            return this.key;
        }

        @Override // javafx.collections.MapChangeListener.Change
        public V getValueAdded() {
            return this.added;
        }

        @Override // javafx.collections.MapChangeListener.Change
        public V getValueRemoved() {
            return this.old;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            if (this.wasAdded) {
                if (this.wasRemoved) {
                    builder.append("replaced ").append((Object) this.old).append("by ").append((Object) this.added);
                } else {
                    builder.append("added ").append((Object) this.added);
                }
            } else {
                builder.append("removed ").append((Object) this.old);
            }
            builder.append(" at key ").append((Object) this.key);
            return builder.toString();
        }
    }

    protected void callObservers(MapChangeListener.Change<K, V> change) {
        MapListenerHelper.fireValueChangedEvent(this.listenerHelper, change);
    }

    @Override // javafx.beans.Observable
    public void addListener(InvalidationListener listener) {
        this.listenerHelper = MapListenerHelper.addListener(this.listenerHelper, listener);
    }

    @Override // javafx.beans.Observable
    public void removeListener(InvalidationListener listener) {
        this.listenerHelper = MapListenerHelper.removeListener(this.listenerHelper, listener);
    }

    @Override // javafx.collections.ObservableMap
    public void addListener(MapChangeListener<? super K, ? super V> observer) {
        this.listenerHelper = MapListenerHelper.addListener(this.listenerHelper, observer);
    }

    @Override // javafx.collections.ObservableMap
    public void removeListener(MapChangeListener<? super K, ? super V> observer) {
        this.listenerHelper = MapListenerHelper.removeListener(this.listenerHelper, observer);
    }

    @Override // java.util.Map
    public int size() {
        return this.backingMap.size();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return this.backingMap.isEmpty();
    }

    @Override // java.util.Map
    public boolean containsKey(Object key) {
        return this.backingMap.containsKey(key);
    }

    @Override // java.util.Map
    public boolean containsValue(Object value) {
        return this.backingMap.containsValue(value);
    }

    @Override // java.util.Map
    public V get(Object key) {
        return this.backingMap.get(key);
    }

    @Override // java.util.Map
    public V put(K key, V value) {
        V ret;
        if (this.backingMap.containsKey(key)) {
            ret = this.backingMap.put(key, value);
            if ((ret == null && value != null) || (ret != null && !ret.equals(value))) {
                callObservers(new SimpleChange(key, ret, value, true, true));
            }
        } else {
            ret = this.backingMap.put(key, value);
            callObservers(new SimpleChange(key, ret, value, true, false));
        }
        return ret;
    }

    @Override // java.util.Map
    public V remove(Object key) {
        if (!this.backingMap.containsKey(key)) {
            return null;
        }
        V ret = this.backingMap.remove(key);
        callObservers(new SimpleChange(key, ret, null, false, true));
        return ret;
    }

    @Override // java.util.Map
    public void putAll(Map<? extends K, ? extends V> m2) {
        for (Map.Entry<? extends K, ? extends V> e2 : m2.entrySet()) {
            put(e2.getKey(), e2.getValue());
        }
    }

    @Override // java.util.Map
    public void clear() {
        Iterator<Map.Entry<K, V>> i2 = this.backingMap.entrySet().iterator();
        while (i2.hasNext()) {
            Map.Entry<K, V> e2 = i2.next();
            K key = e2.getKey();
            V val = e2.getValue();
            i2.remove();
            callObservers(new SimpleChange(key, val, null, false, true));
        }
    }

    @Override // java.util.Map
    public Set<K> keySet() {
        if (this.keySet == null) {
            this.keySet = new ObservableKeySet();
        }
        return this.keySet;
    }

    @Override // java.util.Map
    public Collection<V> values() {
        if (this.values == null) {
            this.values = new ObservableValues();
        }
        return this.values;
    }

    @Override // java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        if (this.entrySet == null) {
            this.entrySet = new ObservableEntrySet();
        }
        return this.entrySet;
    }

    public String toString() {
        return this.backingMap.toString();
    }

    @Override // java.util.Map
    public boolean equals(Object obj) {
        return this.backingMap.equals(obj);
    }

    @Override // java.util.Map
    public int hashCode() {
        return this.backingMap.hashCode();
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/collections/ObservableMapWrapper$ObservableKeySet.class */
    private class ObservableKeySet implements Set<K> {
        private ObservableKeySet() {
        }

        @Override // java.util.Set
        public int size() {
            return ObservableMapWrapper.this.backingMap.size();
        }

        @Override // java.util.Set, java.util.Collection
        public boolean isEmpty() {
            return ObservableMapWrapper.this.backingMap.isEmpty();
        }

        @Override // java.util.Set
        public boolean contains(Object o2) {
            return ObservableMapWrapper.this.backingMap.keySet().contains(o2);
        }

        @Override // java.util.Set, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<K> iterator() {
            return new Iterator<K>() { // from class: com.sun.javafx.collections.ObservableMapWrapper.ObservableKeySet.1
                private Iterator<Map.Entry<K, V>> entryIt;
                private K lastKey;
                private V lastValue;

                {
                    this.entryIt = ObservableMapWrapper.this.backingMap.entrySet().iterator();
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.entryIt.hasNext();
                }

                @Override // java.util.Iterator
                public K next() {
                    Map.Entry<K, V> last = this.entryIt.next();
                    this.lastKey = last.getKey();
                    this.lastValue = last.getValue();
                    return last.getKey();
                }

                @Override // java.util.Iterator
                public void remove() {
                    this.entryIt.remove();
                    ObservableMapWrapper.this.callObservers(new SimpleChange(this.lastKey, this.lastValue, null, false, true));
                }
            };
        }

        @Override // java.util.Set, java.util.Collection, java.util.List
        public Object[] toArray() {
            return ObservableMapWrapper.this.backingMap.keySet().toArray();
        }

        @Override // java.util.Set, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            return (T[]) ObservableMapWrapper.this.backingMap.keySet().toArray(tArr);
        }

        @Override // java.util.Set, java.util.Collection, java.util.List
        public boolean add(K e2) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override // java.util.Set
        public boolean remove(Object o2) {
            return ObservableMapWrapper.this.remove(o2) != null;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean containsAll(Collection<?> c2) {
            return ObservableMapWrapper.this.backingMap.keySet().containsAll(c2);
        }

        @Override // java.util.Set, java.util.Collection
        public boolean addAll(Collection<? extends K> c2) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override // java.util.Set, java.util.Collection
        public boolean retainAll(Collection<?> c2) {
            return removeRetain(c2, false);
        }

        private boolean removeRetain(Collection<?> c2, boolean remove) {
            boolean removed = false;
            Iterator<Map.Entry<K, V>> i2 = ObservableMapWrapper.this.backingMap.entrySet().iterator();
            while (i2.hasNext()) {
                Map.Entry<K, V> e2 = i2.next();
                if (remove == c2.contains(e2.getKey())) {
                    removed = true;
                    K key = e2.getKey();
                    V value = e2.getValue();
                    i2.remove();
                    ObservableMapWrapper.this.callObservers(new SimpleChange(key, value, null, false, true));
                }
            }
            return removed;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean removeAll(Collection<?> c2) {
            return removeRetain(c2, true);
        }

        @Override // java.util.Set, java.util.Collection, java.util.List
        public void clear() {
            ObservableMapWrapper.this.clear();
        }

        public String toString() {
            return ObservableMapWrapper.this.backingMap.keySet().toString();
        }

        @Override // java.util.Set, java.util.Collection, java.util.List
        public boolean equals(Object obj) {
            return ObservableMapWrapper.this.backingMap.keySet().equals(obj);
        }

        @Override // java.util.Set, java.util.Collection, java.util.List
        public int hashCode() {
            return ObservableMapWrapper.this.backingMap.keySet().hashCode();
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/collections/ObservableMapWrapper$ObservableValues.class */
    private class ObservableValues implements Collection<V> {
        private ObservableValues() {
        }

        @Override // java.util.Collection, java.util.Set
        public int size() {
            return ObservableMapWrapper.this.backingMap.size();
        }

        @Override // java.util.Collection
        public boolean isEmpty() {
            return ObservableMapWrapper.this.backingMap.isEmpty();
        }

        @Override // java.util.Collection, java.util.Set
        public boolean contains(Object o2) {
            return ObservableMapWrapper.this.backingMap.values().contains(o2);
        }

        @Override // java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<V> iterator() {
            return new Iterator<V>() { // from class: com.sun.javafx.collections.ObservableMapWrapper.ObservableValues.1
                private Iterator<Map.Entry<K, V>> entryIt;
                private K lastKey;
                private V lastValue;

                {
                    this.entryIt = ObservableMapWrapper.this.backingMap.entrySet().iterator();
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.entryIt.hasNext();
                }

                @Override // java.util.Iterator
                public V next() {
                    Map.Entry<K, V> last = this.entryIt.next();
                    this.lastKey = last.getKey();
                    this.lastValue = last.getValue();
                    return this.lastValue;
                }

                @Override // java.util.Iterator
                public void remove() {
                    this.entryIt.remove();
                    ObservableMapWrapper.this.callObservers(new SimpleChange(this.lastKey, this.lastValue, null, false, true));
                }
            };
        }

        @Override // java.util.Collection, java.util.List
        public Object[] toArray() {
            return ObservableMapWrapper.this.backingMap.values().toArray();
        }

        @Override // java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            return (T[]) ObservableMapWrapper.this.backingMap.values().toArray(tArr);
        }

        @Override // java.util.Collection, java.util.List
        public boolean add(V e2) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override // java.util.Collection, java.util.Set
        public boolean remove(Object o2) {
            Iterator<V> i2 = iterator();
            while (i2.hasNext()) {
                if (i2.next().equals(o2)) {
                    i2.remove();
                    return true;
                }
            }
            return false;
        }

        @Override // java.util.Collection
        public boolean containsAll(Collection<?> c2) {
            return ObservableMapWrapper.this.backingMap.values().containsAll(c2);
        }

        @Override // java.util.Collection
        public boolean addAll(Collection<? extends V> c2) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override // java.util.Collection
        public boolean removeAll(Collection<?> c2) {
            return removeRetain(c2, true);
        }

        private boolean removeRetain(Collection<?> c2, boolean remove) {
            boolean removed = false;
            Iterator<Map.Entry<K, V>> i2 = ObservableMapWrapper.this.backingMap.entrySet().iterator();
            while (i2.hasNext()) {
                Map.Entry<K, V> e2 = i2.next();
                if (remove == c2.contains(e2.getValue())) {
                    removed = true;
                    K key = e2.getKey();
                    V value = e2.getValue();
                    i2.remove();
                    ObservableMapWrapper.this.callObservers(new SimpleChange(key, value, null, false, true));
                }
            }
            return removed;
        }

        @Override // java.util.Collection
        public boolean retainAll(Collection<?> c2) {
            return removeRetain(c2, false);
        }

        @Override // java.util.Collection, java.util.List
        public void clear() {
            ObservableMapWrapper.this.clear();
        }

        public String toString() {
            return ObservableMapWrapper.this.backingMap.values().toString();
        }

        @Override // java.util.Collection, java.util.List
        public boolean equals(Object obj) {
            return ObservableMapWrapper.this.backingMap.values().equals(obj);
        }

        @Override // java.util.Collection, java.util.List
        public int hashCode() {
            return ObservableMapWrapper.this.backingMap.values().hashCode();
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/collections/ObservableMapWrapper$ObservableEntry.class */
    private class ObservableEntry implements Map.Entry<K, V> {
        private final Map.Entry<K, V> backingEntry;

        public ObservableEntry(Map.Entry<K, V> backingEntry) {
            this.backingEntry = backingEntry;
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            return this.backingEntry.getKey();
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            return this.backingEntry.getValue();
        }

        @Override // java.util.Map.Entry
        public V setValue(V value) {
            V oldValue = this.backingEntry.setValue(value);
            ObservableMapWrapper.this.callObservers(new SimpleChange(getKey(), oldValue, value, true, true));
            return oldValue;
        }

        @Override // java.util.Map.Entry
        public final boolean equals(Object o2) {
            if (!(o2 instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e2 = (Map.Entry) o2;
            Object k1 = getKey();
            Object k2 = e2.getKey();
            if (k1 == k2 || (k1 != null && k1.equals(k2))) {
                Object v1 = getValue();
                Object v2 = e2.getValue();
                if (v1 == v2) {
                    return true;
                }
                if (v1 != null && v1.equals(v2)) {
                    return true;
                }
                return false;
            }
            return false;
        }

        @Override // java.util.Map.Entry
        public final int hashCode() {
            return (getKey() == null ? 0 : getKey().hashCode()) ^ (getValue() == null ? 0 : getValue().hashCode());
        }

        public final String toString() {
            return getKey() + "=" + getValue();
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/collections/ObservableMapWrapper$ObservableEntrySet.class */
    private class ObservableEntrySet implements Set<Map.Entry<K, V>> {
        private ObservableEntrySet() {
        }

        @Override // java.util.Set
        public int size() {
            return ObservableMapWrapper.this.backingMap.size();
        }

        @Override // java.util.Set, java.util.Collection
        public boolean isEmpty() {
            return ObservableMapWrapper.this.backingMap.isEmpty();
        }

        @Override // java.util.Set
        public boolean contains(Object o2) {
            return ObservableMapWrapper.this.backingMap.entrySet().contains(o2);
        }

        @Override // java.util.Set, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<Map.Entry<K, V>> iterator() {
            return new Iterator<Map.Entry<K, V>>() { // from class: com.sun.javafx.collections.ObservableMapWrapper.ObservableEntrySet.1
                private Iterator<Map.Entry<K, V>> backingIt;
                private K lastKey;
                private V lastValue;

                {
                    this.backingIt = ObservableMapWrapper.this.backingMap.entrySet().iterator();
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.backingIt.hasNext();
                }

                @Override // java.util.Iterator
                public Map.Entry<K, V> next() {
                    Map.Entry<K, V> last = this.backingIt.next();
                    this.lastKey = last.getKey();
                    this.lastValue = last.getValue();
                    return new ObservableEntry(last);
                }

                @Override // java.util.Iterator
                public void remove() {
                    this.backingIt.remove();
                    ObservableMapWrapper.this.callObservers(new SimpleChange(this.lastKey, this.lastValue, null, false, true));
                }
            };
        }

        @Override // java.util.Set, java.util.Collection, java.util.List
        public Object[] toArray() {
            Object[] array = ObservableMapWrapper.this.backingMap.entrySet().toArray();
            for (int i2 = 0; i2 < array.length; i2++) {
                array[i2] = new ObservableEntry((Map.Entry) array[i2]);
            }
            return array;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Set, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            T[] tArr2 = (T[]) ObservableMapWrapper.this.backingMap.entrySet().toArray(tArr);
            for (int i2 = 0; i2 < tArr2.length; i2++) {
                tArr2[i2] = new ObservableEntry((Map.Entry) tArr2[i2]);
            }
            return tArr2;
        }

        @Override // java.util.Set, java.util.Collection, java.util.List
        public boolean add(Map.Entry<K, V> e2) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override // java.util.Set
        public boolean remove(Object o2) {
            boolean ret = ObservableMapWrapper.this.backingMap.entrySet().remove(o2);
            if (ret) {
                Map.Entry<K, V> entry = (Map.Entry) o2;
                ObservableMapWrapper.this.callObservers(new SimpleChange(entry.getKey(), entry.getValue(), null, false, true));
            }
            return ret;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean containsAll(Collection<?> c2) {
            return ObservableMapWrapper.this.backingMap.entrySet().containsAll(c2);
        }

        @Override // java.util.Set, java.util.Collection
        public boolean addAll(Collection<? extends Map.Entry<K, V>> c2) {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override // java.util.Set, java.util.Collection
        public boolean retainAll(Collection<?> c2) {
            return removeRetain(c2, false);
        }

        private boolean removeRetain(Collection<?> c2, boolean remove) {
            boolean removed = false;
            Iterator<Map.Entry<K, V>> i2 = ObservableMapWrapper.this.backingMap.entrySet().iterator();
            while (i2.hasNext()) {
                Map.Entry<K, V> e2 = i2.next();
                if (remove == c2.contains(e2)) {
                    removed = true;
                    K key = e2.getKey();
                    V value = e2.getValue();
                    i2.remove();
                    ObservableMapWrapper.this.callObservers(new SimpleChange(key, value, null, false, true));
                }
            }
            return removed;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean removeAll(Collection<?> c2) {
            return removeRetain(c2, true);
        }

        @Override // java.util.Set, java.util.Collection, java.util.List
        public void clear() {
            ObservableMapWrapper.this.clear();
        }

        public String toString() {
            return ObservableMapWrapper.this.backingMap.entrySet().toString();
        }

        @Override // java.util.Set, java.util.Collection, java.util.List
        public boolean equals(Object obj) {
            return ObservableMapWrapper.this.backingMap.entrySet().equals(obj);
        }

        @Override // java.util.Set, java.util.Collection, java.util.List
        public int hashCode() {
            return ObservableMapWrapper.this.backingMap.entrySet().hashCode();
        }
    }
}
