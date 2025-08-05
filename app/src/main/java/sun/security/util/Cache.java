package sun.security.util;

import java.util.Arrays;
import java.util.Map;

/* loaded from: rt.jar:sun/security/util/Cache.class */
public abstract class Cache<K, V> {

    /* loaded from: rt.jar:sun/security/util/Cache$CacheVisitor.class */
    public interface CacheVisitor<K, V> {
        void visit(Map<K, V> map);
    }

    public abstract int size();

    public abstract void clear();

    public abstract void put(K k2, V v2);

    public abstract V get(Object obj);

    public abstract void remove(Object obj);

    public abstract V pull(Object obj);

    public abstract void setCapacity(int i2);

    public abstract void setTimeout(int i2);

    public abstract void accept(CacheVisitor<K, V> cacheVisitor);

    protected Cache() {
    }

    public static <K, V> Cache<K, V> newSoftMemoryCache(int i2) {
        return new MemoryCache(true, i2);
    }

    public static <K, V> Cache<K, V> newSoftMemoryCache(int i2, int i3) {
        return new MemoryCache(true, i2, i3);
    }

    public static <K, V> Cache<K, V> newHardMemoryCache(int i2) {
        return new MemoryCache(false, i2);
    }

    public static <K, V> Cache<K, V> newNullCache() {
        return (Cache<K, V>) NullCache.INSTANCE;
    }

    public static <K, V> Cache<K, V> newHardMemoryCache(int i2, int i3) {
        return new MemoryCache(false, i2, i3);
    }

    /* loaded from: rt.jar:sun/security/util/Cache$EqualByteArray.class */
    public static class EqualByteArray {

        /* renamed from: b, reason: collision with root package name */
        private final byte[] f13666b;
        private volatile int hash;

        public EqualByteArray(byte[] bArr) {
            this.f13666b = bArr;
        }

        public int hashCode() {
            int length = this.hash;
            if (length == 0) {
                length = this.f13666b.length + 1;
                for (int i2 = 0; i2 < this.f13666b.length; i2++) {
                    length += (this.f13666b[i2] & 255) * 37;
                }
                this.hash = length;
            }
            return length;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof EqualByteArray)) {
                return false;
            }
            return Arrays.equals(this.f13666b, ((EqualByteArray) obj).f13666b);
        }
    }
}
