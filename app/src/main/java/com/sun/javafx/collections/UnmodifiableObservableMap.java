package com.sun.javafx.collections;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javafx.beans.InvalidationListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.collections.WeakMapChangeListener;

/* loaded from: jfxrt.jar:com/sun/javafx/collections/UnmodifiableObservableMap.class */
public class UnmodifiableObservableMap<K, V> extends AbstractMap<K, V> implements ObservableMap<K, V> {
    private MapListenerHelper<K, V> listenerHelper;
    private final ObservableMap<K, V> backingMap;
    private final MapChangeListener<K, V> listener = c2 -> {
        callObservers(new MapAdapterChange(this, c2));
    };
    private Set<K> keyset;
    private Collection<V> values;
    private Set<Map.Entry<K, V>> entryset;

    public UnmodifiableObservableMap(ObservableMap<K, V> map) {
        this.backingMap = map;
        this.backingMap.addListener(new WeakMapChangeListener(this.listener));
    }

    private void callObservers(MapChangeListener.Change<? extends K, ? extends V> c2) {
        MapListenerHelper.fireValueChangedEvent(this.listenerHelper, c2);
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

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        return this.backingMap.size();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean isEmpty() {
        return this.backingMap.isEmpty();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object key) {
        return this.backingMap.containsKey(key);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsValue(Object value) {
        return this.backingMap.containsValue(value);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V get(Object key) {
        return this.backingMap.get(key);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<K> keySet() {
        if (this.keyset == null) {
            this.keyset = Collections.unmodifiableSet(this.backingMap.keySet());
        }
        return this.keyset;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Collection<V> values() {
        if (this.values == null) {
            this.values = Collections.unmodifiableCollection(this.backingMap.values());
        }
        return this.values;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        if (this.entryset == null) {
            this.entryset = Collections.unmodifiableMap(this.backingMap).entrySet();
        }
        return this.entryset;
    }
}
