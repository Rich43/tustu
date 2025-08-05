package java.util;

import java.util.Map;

/* loaded from: rt.jar:java/util/SortedMap.class */
public interface SortedMap<K, V> extends Map<K, V> {
    Comparator<? super K> comparator();

    SortedMap<K, V> subMap(K k2, K k3);

    SortedMap<K, V> headMap(K k2);

    SortedMap<K, V> tailMap(K k2);

    K firstKey();

    K lastKey();

    @Override // java.util.Map
    Set<K> keySet();

    @Override // java.util.Map
    Collection<V> values();

    @Override // java.util.Map
    Set<Map.Entry<K, V>> entrySet();
}
