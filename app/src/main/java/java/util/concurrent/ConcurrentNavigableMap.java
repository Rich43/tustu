package java.util.concurrent;

import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.SortedMap;

/* loaded from: rt.jar:java/util/concurrent/ConcurrentNavigableMap.class */
public interface ConcurrentNavigableMap<K, V> extends ConcurrentMap<K, V>, NavigableMap<K, V> {
    @Override // java.util.NavigableMap
    ConcurrentNavigableMap<K, V> subMap(K k2, boolean z2, K k3, boolean z3);

    @Override // java.util.NavigableMap
    ConcurrentNavigableMap<K, V> headMap(K k2, boolean z2);

    @Override // java.util.NavigableMap
    ConcurrentNavigableMap<K, V> tailMap(K k2, boolean z2);

    @Override // java.util.NavigableMap, java.util.SortedMap
    ConcurrentNavigableMap<K, V> subMap(K k2, K k3);

    @Override // java.util.NavigableMap, java.util.SortedMap
    ConcurrentNavigableMap<K, V> headMap(K k2);

    @Override // java.util.NavigableMap, java.util.SortedMap
    ConcurrentNavigableMap<K, V> tailMap(K k2);

    @Override // java.util.NavigableMap
    ConcurrentNavigableMap<K, V> descendingMap();

    @Override // java.util.NavigableMap
    NavigableSet<K> navigableKeySet();

    @Override // java.util.Map
    NavigableSet<K> keySet();

    @Override // java.util.NavigableMap
    NavigableSet<K> descendingKeySet();

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableMap, java.util.SortedMap
    /* bridge */ /* synthetic */ default SortedMap tailMap(Object obj) {
        return tailMap((ConcurrentNavigableMap<K, V>) obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableMap, java.util.SortedMap
    /* bridge */ /* synthetic */ default SortedMap headMap(Object obj) {
        return headMap((ConcurrentNavigableMap<K, V>) obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableMap
    /* bridge */ /* synthetic */ default NavigableMap tailMap(Object obj, boolean z2) {
        return tailMap((ConcurrentNavigableMap<K, V>) obj, z2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableMap
    /* bridge */ /* synthetic */ default NavigableMap headMap(Object obj, boolean z2) {
        return headMap((ConcurrentNavigableMap<K, V>) obj, z2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.NavigableMap
    /* bridge */ /* synthetic */ default NavigableMap subMap(Object obj, boolean z2, Object obj2, boolean z3) {
        return subMap((boolean) obj, z2, (boolean) obj2, z3);
    }
}
