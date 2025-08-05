package com.sun.jmx.mbeanserver;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Map;

/* loaded from: rt.jar:com/sun/jmx/mbeanserver/WeakIdentityHashMap.class */
class WeakIdentityHashMap<K, V> {
    private Map<WeakReference<K>, V> map = Util.newMap();
    private ReferenceQueue<K> refQueue = new ReferenceQueue<>();

    private WeakIdentityHashMap() {
    }

    static <K, V> WeakIdentityHashMap<K, V> make() {
        return new WeakIdentityHashMap<>();
    }

    V get(K k2) {
        expunge();
        return this.map.get(makeReference(k2));
    }

    public V put(K k2, V v2) {
        expunge();
        if (k2 == null) {
            throw new IllegalArgumentException("Null key");
        }
        return this.map.put(makeReference(k2, this.refQueue), v2);
    }

    public V remove(K k2) {
        expunge();
        return this.map.remove(makeReference(k2));
    }

    private void expunge() {
        while (true) {
            Reference<? extends K> referencePoll = this.refQueue.poll();
            if (referencePoll != null) {
                this.map.remove(referencePoll);
            } else {
                return;
            }
        }
    }

    private WeakReference<K> makeReference(K k2) {
        return new IdentityWeakReference(k2);
    }

    private WeakReference<K> makeReference(K k2, ReferenceQueue<K> referenceQueue) {
        return new IdentityWeakReference(k2, referenceQueue);
    }

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/WeakIdentityHashMap$IdentityWeakReference.class */
    private static class IdentityWeakReference<T> extends WeakReference<T> {
        private final int hashCode;

        IdentityWeakReference(T t2) {
            this(t2, null);
        }

        IdentityWeakReference(T t2, ReferenceQueue<T> referenceQueue) {
            super(t2, referenceQueue);
            this.hashCode = t2 == null ? 0 : System.identityHashCode(t2);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof IdentityWeakReference)) {
                return false;
            }
            IdentityWeakReference identityWeakReference = (IdentityWeakReference) obj;
            T t2 = get();
            return t2 != null && t2 == identityWeakReference.get();
        }

        public int hashCode() {
            return this.hashCode;
        }
    }
}
