package com.sun.xml.internal.bind.v2.schemagen;

import java.lang.Comparable;
import java.util.Map;
import java.util.TreeMap;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/MultiMap.class */
final class MultiMap<K extends Comparable<K>, V> extends TreeMap<K, V> {
    private final V many;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.TreeMap, java.util.AbstractMap, java.util.Map
    public /* bridge */ /* synthetic */ Object put(Object obj, Object obj2) {
        return put((MultiMap<K, V>) obj, (Comparable) obj2);
    }

    public MultiMap(V many) {
        this.many = many;
    }

    public V put(K k2, V v2) {
        V v3 = (V) super.put((MultiMap<K, V>) k2, (K) v2);
        if (v3 != null && !v3.equals(v2)) {
            super.put((MultiMap<K, V>) k2, (K) this.many);
        }
        return v3;
    }

    @Override // java.util.TreeMap, java.util.AbstractMap, java.util.Map
    public void putAll(Map<? extends K, ? extends V> map) {
        throw new UnsupportedOperationException();
    }
}
