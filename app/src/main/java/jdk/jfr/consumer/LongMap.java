package jdk.jfr.consumer;

import java.util.HashMap;
import java.util.Iterator;

/* loaded from: jfr.jar:jdk/jfr/consumer/LongMap.class */
final class LongMap<T> implements Iterable<T> {
    private final HashMap<Long, T> map = new HashMap<>(101);

    LongMap() {
    }

    void put(long j2, T t2) {
        this.map.put(Long.valueOf(j2), t2);
    }

    T get(long j2) {
        return this.map.get(Long.valueOf(j2));
    }

    @Override // java.lang.Iterable, java.util.List
    public Iterator<T> iterator() {
        return this.map.values().iterator();
    }

    Iterator<Long> keys() {
        return this.map.keySet().iterator();
    }
}
