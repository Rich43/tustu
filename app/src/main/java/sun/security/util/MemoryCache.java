package sun.security.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import sun.security.util.Cache;

/* compiled from: Cache.java */
/* loaded from: rt.jar:sun/security/util/MemoryCache.class */
class MemoryCache<K, V> extends Cache<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final boolean DEBUG = false;
    private final Map<K, CacheEntry<K, V>> cacheMap;
    private int maxSize;
    private long lifetime;
    private long nextExpirationTime;
    private final ReferenceQueue<V> queue;

    /* compiled from: Cache.java */
    /* loaded from: rt.jar:sun/security/util/MemoryCache$CacheEntry.class */
    private interface CacheEntry<K, V> {
        boolean isValid(long j2);

        void invalidate();

        K getKey();

        V getValue();

        long getExpirationTime();
    }

    public MemoryCache(boolean z2, int i2) {
        this(z2, i2, 0);
    }

    public MemoryCache(boolean z2, int i2, int i3) {
        this.nextExpirationTime = Long.MAX_VALUE;
        this.maxSize = i2;
        this.lifetime = i3 * 1000;
        if (z2) {
            this.queue = new ReferenceQueue<>();
        } else {
            this.queue = null;
        }
        this.cacheMap = new LinkedHashMap(1, 0.75f, true);
    }

    private void emptyQueue() {
        CacheEntry<K, V> cacheEntryRemove;
        if (this.queue == null) {
            return;
        }
        this.cacheMap.size();
        while (true) {
            CacheEntry<K, V> cacheEntry = (CacheEntry) this.queue.poll();
            if (cacheEntry != null) {
                K key = cacheEntry.getKey();
                if (key != null && (cacheEntryRemove = this.cacheMap.remove(key)) != null && cacheEntry != cacheEntryRemove) {
                    this.cacheMap.put(key, cacheEntryRemove);
                }
            } else {
                return;
            }
        }
    }

    private void expungeExpiredEntries() {
        emptyQueue();
        if (this.lifetime == 0) {
            return;
        }
        int i2 = 0;
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (this.nextExpirationTime > jCurrentTimeMillis) {
            return;
        }
        this.nextExpirationTime = Long.MAX_VALUE;
        Iterator<CacheEntry<K, V>> it = this.cacheMap.values().iterator();
        while (it.hasNext()) {
            CacheEntry<K, V> next = it.next();
            if (!next.isValid(jCurrentTimeMillis)) {
                it.remove();
                i2++;
            } else if (this.nextExpirationTime > next.getExpirationTime()) {
                this.nextExpirationTime = next.getExpirationTime();
            }
        }
    }

    @Override // sun.security.util.Cache
    public synchronized int size() {
        expungeExpiredEntries();
        return this.cacheMap.size();
    }

    @Override // sun.security.util.Cache
    public synchronized void clear() {
        if (this.queue != null) {
            Iterator<CacheEntry<K, V>> it = this.cacheMap.values().iterator();
            while (it.hasNext()) {
                it.next().invalidate();
            }
            while (this.queue.poll() != null) {
            }
        }
        this.cacheMap.clear();
    }

    @Override // sun.security.util.Cache
    public synchronized void put(K k2, V v2) {
        emptyQueue();
        long jCurrentTimeMillis = this.lifetime == 0 ? 0L : System.currentTimeMillis() + this.lifetime;
        if (jCurrentTimeMillis < this.nextExpirationTime) {
            this.nextExpirationTime = jCurrentTimeMillis;
        }
        CacheEntry<K, V> cacheEntryPut = this.cacheMap.put(k2, newEntry(k2, v2, jCurrentTimeMillis, this.queue));
        if (cacheEntryPut != null) {
            cacheEntryPut.invalidate();
            return;
        }
        if (this.maxSize > 0 && this.cacheMap.size() > this.maxSize) {
            expungeExpiredEntries();
            if (this.cacheMap.size() > this.maxSize) {
                Iterator<CacheEntry<K, V>> it = this.cacheMap.values().iterator();
                CacheEntry<K, V> next = it.next();
                it.remove();
                next.invalidate();
            }
        }
    }

    @Override // sun.security.util.Cache
    public synchronized V get(Object obj) {
        emptyQueue();
        CacheEntry<K, V> cacheEntry = this.cacheMap.get(obj);
        if (cacheEntry == null) {
            return null;
        }
        if (!cacheEntry.isValid(this.lifetime == 0 ? 0L : System.currentTimeMillis())) {
            this.cacheMap.remove(obj);
            return null;
        }
        return cacheEntry.getValue();
    }

    @Override // sun.security.util.Cache
    public synchronized void remove(Object obj) {
        emptyQueue();
        CacheEntry<K, V> cacheEntryRemove = this.cacheMap.remove(obj);
        if (cacheEntryRemove != null) {
            cacheEntryRemove.invalidate();
        }
    }

    @Override // sun.security.util.Cache
    public synchronized V pull(Object obj) {
        emptyQueue();
        CacheEntry<K, V> cacheEntryRemove = this.cacheMap.remove(obj);
        if (cacheEntryRemove == null) {
            return null;
        }
        if (cacheEntryRemove.isValid(this.lifetime == 0 ? 0L : System.currentTimeMillis())) {
            V value = cacheEntryRemove.getValue();
            cacheEntryRemove.invalidate();
            return value;
        }
        return null;
    }

    @Override // sun.security.util.Cache
    public synchronized void setCapacity(int i2) {
        expungeExpiredEntries();
        if (i2 > 0 && this.cacheMap.size() > i2) {
            Iterator<CacheEntry<K, V>> it = this.cacheMap.values().iterator();
            for (int size = this.cacheMap.size() - i2; size > 0; size--) {
                CacheEntry<K, V> next = it.next();
                it.remove();
                next.invalidate();
            }
        }
        this.maxSize = i2 > 0 ? i2 : 0;
    }

    @Override // sun.security.util.Cache
    public synchronized void setTimeout(int i2) {
        emptyQueue();
        this.lifetime = i2 > 0 ? i2 * 1000 : 0L;
    }

    @Override // sun.security.util.Cache
    public synchronized void accept(Cache.CacheVisitor<K, V> cacheVisitor) {
        expungeExpiredEntries();
        cacheVisitor.visit(getCachedEntries());
    }

    private Map<K, V> getCachedEntries() {
        HashMap map = new HashMap(this.cacheMap.size());
        for (CacheEntry<K, V> cacheEntry : this.cacheMap.values()) {
            map.put(cacheEntry.getKey(), cacheEntry.getValue());
        }
        return map;
    }

    protected CacheEntry<K, V> newEntry(K k2, V v2, long j2, ReferenceQueue<V> referenceQueue) {
        if (referenceQueue != null) {
            return new SoftCacheEntry(k2, v2, j2, referenceQueue);
        }
        return new HardCacheEntry(k2, v2, j2);
    }

    /* compiled from: Cache.java */
    /* loaded from: rt.jar:sun/security/util/MemoryCache$HardCacheEntry.class */
    private static class HardCacheEntry<K, V> implements CacheEntry<K, V> {
        private K key;
        private V value;
        private long expirationTime;

        HardCacheEntry(K k2, V v2, long j2) {
            this.key = k2;
            this.value = v2;
            this.expirationTime = j2;
        }

        @Override // sun.security.util.MemoryCache.CacheEntry
        public K getKey() {
            return this.key;
        }

        @Override // sun.security.util.MemoryCache.CacheEntry
        public V getValue() {
            return this.value;
        }

        @Override // sun.security.util.MemoryCache.CacheEntry
        public long getExpirationTime() {
            return this.expirationTime;
        }

        @Override // sun.security.util.MemoryCache.CacheEntry
        public boolean isValid(long j2) {
            boolean z2 = j2 <= this.expirationTime;
            if (!z2) {
                invalidate();
            }
            return z2;
        }

        @Override // sun.security.util.MemoryCache.CacheEntry
        public void invalidate() {
            this.key = null;
            this.value = null;
            this.expirationTime = -1L;
        }
    }

    /* compiled from: Cache.java */
    /* loaded from: rt.jar:sun/security/util/MemoryCache$SoftCacheEntry.class */
    private static class SoftCacheEntry<K, V> extends SoftReference<V> implements CacheEntry<K, V> {
        private K key;
        private long expirationTime;

        SoftCacheEntry(K k2, V v2, long j2, ReferenceQueue<V> referenceQueue) {
            super(v2, referenceQueue);
            this.key = k2;
            this.expirationTime = j2;
        }

        @Override // sun.security.util.MemoryCache.CacheEntry
        public K getKey() {
            return this.key;
        }

        @Override // sun.security.util.MemoryCache.CacheEntry
        public V getValue() {
            return get();
        }

        @Override // sun.security.util.MemoryCache.CacheEntry
        public long getExpirationTime() {
            return this.expirationTime;
        }

        @Override // sun.security.util.MemoryCache.CacheEntry
        public boolean isValid(long j2) {
            boolean z2 = j2 <= this.expirationTime && get() != null;
            if (!z2) {
                invalidate();
            }
            return z2;
        }

        @Override // sun.security.util.MemoryCache.CacheEntry
        public void invalidate() {
            clear();
            this.key = null;
            this.expirationTime = -1L;
        }
    }
}
