package java.util;

import java.util.Map;

/* loaded from: rt.jar:java/util/NavigableMap.class */
public interface NavigableMap<K, V> extends SortedMap<K, V> {
    Map.Entry<K, V> lowerEntry(K k2);

    K lowerKey(K k2);

    Map.Entry<K, V> floorEntry(K k2);

    K floorKey(K k2);

    Map.Entry<K, V> ceilingEntry(K k2);

    K ceilingKey(K k2);

    Map.Entry<K, V> higherEntry(K k2);

    K higherKey(K k2);

    Map.Entry<K, V> firstEntry();

    Map.Entry<K, V> lastEntry();

    Map.Entry<K, V> pollFirstEntry();

    Map.Entry<K, V> pollLastEntry();

    NavigableMap<K, V> descendingMap();

    NavigableSet<K> navigableKeySet();

    NavigableSet<K> descendingKeySet();

    NavigableMap<K, V> subMap(K k2, boolean z2, K k3, boolean z3);

    NavigableMap<K, V> headMap(K k2, boolean z2);

    NavigableMap<K, V> tailMap(K k2, boolean z2);

    @Override // java.util.SortedMap
    SortedMap<K, V> subMap(K k2, K k3);

    @Override // java.util.SortedMap
    SortedMap<K, V> headMap(K k2);

    @Override // java.util.SortedMap
    SortedMap<K, V> tailMap(K k2);
}
