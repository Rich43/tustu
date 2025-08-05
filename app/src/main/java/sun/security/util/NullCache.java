package sun.security.util;

import sun.security.util.Cache;

/* compiled from: Cache.java */
/* loaded from: rt.jar:sun/security/util/NullCache.class */
class NullCache<K, V> extends Cache<K, V> {
    static final Cache<Object, Object> INSTANCE = new NullCache();

    private NullCache() {
    }

    @Override // sun.security.util.Cache
    public int size() {
        return 0;
    }

    @Override // sun.security.util.Cache
    public void clear() {
    }

    @Override // sun.security.util.Cache
    public void put(K k2, V v2) {
    }

    @Override // sun.security.util.Cache
    public V get(Object obj) {
        return null;
    }

    @Override // sun.security.util.Cache
    public void remove(Object obj) {
    }

    @Override // sun.security.util.Cache
    public V pull(Object obj) {
        return null;
    }

    @Override // sun.security.util.Cache
    public void setCapacity(int i2) {
    }

    @Override // sun.security.util.Cache
    public void setTimeout(int i2) {
    }

    @Override // sun.security.util.Cache
    public void accept(Cache.CacheVisitor<K, V> cacheVisitor) {
    }
}
