package sun.security.pkcs11;

import java.lang.ref.WeakReference;
import java.security.Key;
import java.util.IdentityHashMap;
import java.util.Map;
import sun.security.util.Cache;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/KeyCache.class */
final class KeyCache {
    private final Cache<IdentityWrapper, P11Key> strongCache = Cache.newHardMemoryCache(16);
    private WeakReference<Map<Key, P11Key>> cacheReference;

    KeyCache() {
    }

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/KeyCache$IdentityWrapper.class */
    private static final class IdentityWrapper {
        final Object obj;

        IdentityWrapper(Object obj) {
            this.obj = obj;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return (obj instanceof IdentityWrapper) && this.obj == ((IdentityWrapper) obj).obj;
        }

        public int hashCode() {
            return System.identityHashCode(this.obj);
        }
    }

    synchronized P11Key get(Key key) {
        P11Key p11Key = this.strongCache.get(new IdentityWrapper(key));
        if (p11Key != null) {
            return p11Key;
        }
        Map<Key, P11Key> map = this.cacheReference == null ? null : this.cacheReference.get();
        if (map == null) {
            return null;
        }
        return map.get(key);
    }

    synchronized void put(Key key, P11Key p11Key) {
        this.strongCache.put(new IdentityWrapper(key), p11Key);
        Map<Key, P11Key> identityHashMap = this.cacheReference == null ? null : this.cacheReference.get();
        if (identityHashMap == null) {
            identityHashMap = new IdentityHashMap();
            this.cacheReference = new WeakReference<>(identityHashMap);
        }
        identityHashMap.put(key, p11Key);
    }
}
