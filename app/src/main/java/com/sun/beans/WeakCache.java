package com.sun.beans;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

/* loaded from: rt.jar:com/sun/beans/WeakCache.class */
public final class WeakCache<K, V> {
    private final Map<K, Reference<V>> map = new WeakHashMap();

    public V get(K k2) {
        Reference<V> reference = this.map.get(k2);
        if (reference == null) {
            return null;
        }
        V v2 = reference.get();
        if (v2 == null) {
            this.map.remove(k2);
        }
        return v2;
    }

    public void put(K k2, V v2) {
        if (v2 != null) {
            this.map.put(k2, new WeakReference(v2));
        } else {
            this.map.remove(k2);
        }
    }

    public void clear() {
        this.map.clear();
    }
}
