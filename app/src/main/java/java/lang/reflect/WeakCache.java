package java.lang.reflect;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/* loaded from: rt.jar:java/lang/reflect/WeakCache.class */
final class WeakCache<K, P, V> {
    private final ReferenceQueue<K> refQueue = new ReferenceQueue<>();
    private final ConcurrentMap<Object, ConcurrentMap<Object, Supplier<V>>> map = new ConcurrentHashMap();
    private final ConcurrentMap<Supplier<V>, Boolean> reverseMap = new ConcurrentHashMap();
    private final BiFunction<K, P, ?> subKeyFactory;
    private final BiFunction<K, P, V> valueFactory;

    /* loaded from: rt.jar:java/lang/reflect/WeakCache$Value.class */
    private interface Value<V> extends Supplier<V> {
    }

    public WeakCache(BiFunction<K, P, ?> biFunction, BiFunction<K, P, V> biFunction2) {
        this.subKeyFactory = (BiFunction) Objects.requireNonNull(biFunction);
        this.valueFactory = (BiFunction) Objects.requireNonNull(biFunction2);
    }

    public V get(K k2, P p2) {
        V v2;
        Objects.requireNonNull(p2);
        expungeStaleEntries();
        Object objValueOf = CacheKey.valueOf(k2, this.refQueue);
        ConcurrentMap<Object, Supplier<V>> concurrentMap = this.map.get(objValueOf);
        if (concurrentMap == null) {
            ConcurrentMap<Object, ConcurrentMap<Object, Supplier<V>>> concurrentMap2 = this.map;
            ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
            concurrentMap = concurrentHashMap;
            ConcurrentMap<Object, Supplier<V>> concurrentMapPutIfAbsent = concurrentMap2.putIfAbsent(objValueOf, concurrentHashMap);
            if (concurrentMapPutIfAbsent != null) {
                concurrentMap = concurrentMapPutIfAbsent;
            }
        }
        Object objRequireNonNull = Objects.requireNonNull(this.subKeyFactory.apply(k2, p2));
        Supplier<V> supplierPutIfAbsent = concurrentMap.get(objRequireNonNull);
        Factory factory = null;
        while (true) {
            if (supplierPutIfAbsent != null && (v2 = supplierPutIfAbsent.get()) != null) {
                return v2;
            }
            if (factory == null) {
                factory = new Factory(k2, p2, objRequireNonNull, concurrentMap);
            }
            if (supplierPutIfAbsent == null) {
                supplierPutIfAbsent = concurrentMap.putIfAbsent(objRequireNonNull, factory);
                if (supplierPutIfAbsent == null) {
                    supplierPutIfAbsent = factory;
                }
            } else if (concurrentMap.replace(objRequireNonNull, supplierPutIfAbsent, factory)) {
                supplierPutIfAbsent = factory;
            } else {
                supplierPutIfAbsent = concurrentMap.get(objRequireNonNull);
            }
        }
    }

    public boolean containsValue(V v2) {
        Objects.requireNonNull(v2);
        expungeStaleEntries();
        return this.reverseMap.containsKey(new LookupValue(v2));
    }

    public int size() {
        expungeStaleEntries();
        return this.reverseMap.size();
    }

    private void expungeStaleEntries() {
        while (true) {
            CacheKey cacheKey = (CacheKey) this.refQueue.poll();
            if (cacheKey != null) {
                cacheKey.expungeFrom(this.map, this.reverseMap);
            } else {
                return;
            }
        }
    }

    /* loaded from: rt.jar:java/lang/reflect/WeakCache$Factory.class */
    private final class Factory implements Supplier<V> {
        private final K key;
        private final P parameter;
        private final Object subKey;
        private final ConcurrentMap<Object, Supplier<V>> valuesMap;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !WeakCache.class.desiredAssertionStatus();
        }

        Factory(K k2, P p2, Object obj, ConcurrentMap<Object, Supplier<V>> concurrentMap) {
            this.key = k2;
            this.parameter = p2;
            this.subKey = obj;
            this.valuesMap = concurrentMap;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.function.Supplier
        public synchronized V get() {
            if (this.valuesMap.get(this.subKey) != this) {
                return null;
            }
            V vRequireNonNull = null;
            try {
                vRequireNonNull = Objects.requireNonNull(WeakCache.this.valueFactory.apply(this.key, this.parameter));
                if (vRequireNonNull == null) {
                    this.valuesMap.remove(this.subKey, this);
                }
                if (!$assertionsDisabled && vRequireNonNull == null) {
                    throw new AssertionError();
                }
                CacheValue cacheValue = new CacheValue(vRequireNonNull);
                WeakCache.this.reverseMap.put(cacheValue, Boolean.TRUE);
                if (!this.valuesMap.replace(this.subKey, this, cacheValue)) {
                    throw new AssertionError((Object) "Should not reach here");
                }
                return vRequireNonNull;
            } catch (Throwable th) {
                if (vRequireNonNull == null) {
                    this.valuesMap.remove(this.subKey, this);
                }
                throw th;
            }
        }
    }

    /* loaded from: rt.jar:java/lang/reflect/WeakCache$LookupValue.class */
    private static final class LookupValue<V> implements Value<V> {
        private final V value;

        LookupValue(V v2) {
            this.value = v2;
        }

        @Override // java.util.function.Supplier
        public V get() {
            return this.value;
        }

        public int hashCode() {
            return System.identityHashCode(this.value);
        }

        public boolean equals(Object obj) {
            return obj == this || ((obj instanceof Value) && this.value == ((Value) obj).get());
        }
    }

    /* loaded from: rt.jar:java/lang/reflect/WeakCache$CacheValue.class */
    private static final class CacheValue<V> extends WeakReference<V> implements Value<V> {
        private final int hash;

        CacheValue(V v2) {
            super(v2);
            this.hash = System.identityHashCode(v2);
        }

        public int hashCode() {
            return this.hash;
        }

        public boolean equals(Object obj) {
            V v2;
            return obj == this || ((obj instanceof Value) && (v2 = get()) != null && v2 == ((Value) obj).get());
        }
    }

    /* loaded from: rt.jar:java/lang/reflect/WeakCache$CacheKey.class */
    private static final class CacheKey<K> extends WeakReference<K> {
        private static final Object NULL_KEY = new Object();
        private final int hash;

        static <K> Object valueOf(K k2, ReferenceQueue<K> referenceQueue) {
            return k2 == null ? NULL_KEY : new CacheKey(k2, referenceQueue);
        }

        private CacheKey(K k2, ReferenceQueue<K> referenceQueue) {
            super(k2, referenceQueue);
            this.hash = System.identityHashCode(k2);
        }

        public int hashCode() {
            return this.hash;
        }

        public boolean equals(Object obj) {
            K k2;
            return obj == this || (obj != null && obj.getClass() == getClass() && (k2 = get()) != null && k2 == ((CacheKey) obj).get());
        }

        void expungeFrom(ConcurrentMap<?, ? extends ConcurrentMap<?, ?>> concurrentMap, ConcurrentMap<?, Boolean> concurrentMap2) {
            ConcurrentMap<?, ?> concurrentMapRemove = concurrentMap.remove(this);
            if (concurrentMapRemove != null) {
                Iterator<?> it = concurrentMapRemove.values().iterator();
                while (it.hasNext()) {
                    concurrentMap2.remove(it.next());
                }
            }
        }
    }
}
