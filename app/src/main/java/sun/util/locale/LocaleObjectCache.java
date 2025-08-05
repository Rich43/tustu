package sun.util.locale;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/* loaded from: rt.jar:sun/util/locale/LocaleObjectCache.class */
public abstract class LocaleObjectCache<K, V> {
    private ConcurrentMap<K, CacheEntry<K, V>> map;
    private ReferenceQueue<V> queue;

    protected abstract V createObject(K k2);

    public LocaleObjectCache() {
        this(16, 0.75f, 16);
    }

    public LocaleObjectCache(int i2, float f2, int i3) {
        this.queue = new ReferenceQueue<>();
        this.map = new ConcurrentHashMap(i2, f2, i3);
    }

    public V get(K k2) {
        V v2 = null;
        cleanStaleEntries();
        CacheEntry<K, V> cacheEntry = this.map.get(k2);
        if (cacheEntry != null) {
            v2 = cacheEntry.get();
        }
        if (v2 == null) {
            V vCreateObject = createObject(k2);
            K kNormalizeKey = normalizeKey(k2);
            if (kNormalizeKey == null || vCreateObject == null) {
                return null;
            }
            CacheEntry<K, V> cacheEntry2 = new CacheEntry<>(kNormalizeKey, vCreateObject, this.queue);
            CacheEntry<K, V> cacheEntryPutIfAbsent = this.map.putIfAbsent(kNormalizeKey, cacheEntry2);
            if (cacheEntryPutIfAbsent == null) {
                v2 = vCreateObject;
            } else {
                v2 = cacheEntryPutIfAbsent.get();
                if (v2 == null) {
                    this.map.put(kNormalizeKey, cacheEntry2);
                    v2 = vCreateObject;
                }
            }
        }
        return v2;
    }

    protected V put(K k2, V v2) {
        CacheEntry<K, V> cacheEntryPut = this.map.put(k2, new CacheEntry<>(k2, v2, this.queue));
        if (cacheEntryPut == null) {
            return null;
        }
        return cacheEntryPut.get();
    }

    private void cleanStaleEntries() {
        while (true) {
            CacheEntry cacheEntry = (CacheEntry) this.queue.poll();
            if (cacheEntry != null) {
                this.map.remove(cacheEntry.getKey());
            } else {
                return;
            }
        }
    }

    protected K normalizeKey(K k2) {
        return k2;
    }

    /* loaded from: rt.jar:sun/util/locale/LocaleObjectCache$CacheEntry.class */
    private static class CacheEntry<K, V> extends SoftReference<V> {
        private K key;

        CacheEntry(K k2, V v2, ReferenceQueue<V> referenceQueue) {
            super(v2, referenceQueue);
            this.key = k2;
        }

        K getKey() {
            return this.key;
        }
    }
}
