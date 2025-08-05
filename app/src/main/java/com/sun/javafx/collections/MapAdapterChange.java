package com.sun.javafx.collections;

import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

/* loaded from: jfxrt.jar:com/sun/javafx/collections/MapAdapterChange.class */
public class MapAdapterChange<K, V> extends MapChangeListener.Change<K, V> {
    private final MapChangeListener.Change<? extends K, ? extends V> change;

    public MapAdapterChange(ObservableMap<K, V> map, MapChangeListener.Change<? extends K, ? extends V> change) {
        super(map);
        this.change = change;
    }

    @Override // javafx.collections.MapChangeListener.Change
    public boolean wasAdded() {
        return this.change.wasAdded();
    }

    @Override // javafx.collections.MapChangeListener.Change
    public boolean wasRemoved() {
        return this.change.wasRemoved();
    }

    @Override // javafx.collections.MapChangeListener.Change
    public K getKey() {
        return this.change.getKey();
    }

    @Override // javafx.collections.MapChangeListener.Change
    public V getValueAdded() {
        return this.change.getValueAdded();
    }

    @Override // javafx.collections.MapChangeListener.Change
    public V getValueRemoved() {
        return this.change.getValueRemoved();
    }

    public String toString() {
        return this.change.toString();
    }
}
