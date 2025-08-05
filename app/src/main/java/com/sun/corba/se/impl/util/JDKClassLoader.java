package com.sun.corba.se.impl.util;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import sun.corba.Bridge;

/* loaded from: rt.jar:com/sun/corba/se/impl/util/JDKClassLoader.class */
class JDKClassLoader {
    private static final JDKClassLoaderCache classCache = new JDKClassLoaderCache();
    private static final Bridge bridge = (Bridge) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.corba.se.impl.util.JDKClassLoader.1
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public Object run2() {
            return Bridge.get();
        }
    });

    JDKClassLoader() {
    }

    static Class loadClass(Class cls, String str) throws ClassNotFoundException {
        ClassLoader latestUserDefinedLoader;
        if (str == null) {
            throw new NullPointerException();
        }
        if (str.length() == 0) {
            throw new ClassNotFoundException();
        }
        if (cls != null) {
            latestUserDefinedLoader = cls.getClassLoader();
        } else {
            latestUserDefinedLoader = bridge.getLatestUserDefinedLoader();
        }
        Object objCreateKey = classCache.createKey(str, latestUserDefinedLoader);
        if (classCache.knownToFail(objCreateKey)) {
            throw new ClassNotFoundException(str);
        }
        try {
            return Class.forName(str, false, latestUserDefinedLoader);
        } catch (ClassNotFoundException e2) {
            classCache.recordFailure(objCreateKey);
            throw e2;
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/util/JDKClassLoader$JDKClassLoaderCache.class */
    private static class JDKClassLoaderCache {
        private final Map cache;
        private static final Object KNOWN_TO_FAIL = new Object();

        private JDKClassLoaderCache() {
            this.cache = Collections.synchronizedMap(new WeakHashMap());
        }

        public final void recordFailure(Object obj) {
            this.cache.put(obj, KNOWN_TO_FAIL);
        }

        public final Object createKey(String str, ClassLoader classLoader) {
            return new CacheKey(str, classLoader);
        }

        public final boolean knownToFail(Object obj) {
            return this.cache.get(obj) == KNOWN_TO_FAIL;
        }

        /* loaded from: rt.jar:com/sun/corba/se/impl/util/JDKClassLoader$JDKClassLoaderCache$CacheKey.class */
        private static class CacheKey {
            String className;
            ClassLoader loader;

            public CacheKey(String str, ClassLoader classLoader) {
                this.className = str;
                this.loader = classLoader;
            }

            public int hashCode() {
                if (this.loader == null) {
                    return this.className.hashCode();
                }
                return this.className.hashCode() ^ this.loader.hashCode();
            }

            public boolean equals(Object obj) {
                if (obj == null) {
                    return false;
                }
                try {
                    CacheKey cacheKey = (CacheKey) obj;
                    if (this.className.equals(cacheKey.className)) {
                        if (this.loader == cacheKey.loader) {
                            return true;
                        }
                    }
                    return false;
                } catch (ClassCastException e2) {
                    return false;
                }
            }
        }
    }
}
