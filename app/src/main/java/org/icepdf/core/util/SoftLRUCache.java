package org.icepdf.core.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

/* loaded from: icepdf-core.jar:org/icepdf/core/util/SoftLRUCache.class */
public class SoftLRUCache<K, V> {
    private LinkedHashMap<K, SoftReference<V>> lruCache;
    private ReferenceQueue<? super V> reqQueue = new ReferenceQueue<>();

    public SoftLRUCache(int aInitialSize) {
        this.lruCache = new LinkedHashMap<>(aInitialSize, 0.75f, true);
    }

    public V get(K aKey) {
        diposeStaleEntries();
        SoftReference<V> ref = this.lruCache.get(aKey);
        if (ref != null) {
            return ref.get();
        }
        return null;
    }

    public V put(K aKey, V aValue) {
        diposeStaleEntries();
        SoftReference<V> oldValue = this.lruCache.put(aKey, new KeyReference(aKey, aValue, this.reqQueue));
        if (oldValue != null) {
            return oldValue.get();
        }
        return null;
    }

    private void diposeStaleEntries() {
        while (true) {
            KeyReference<K, V> ref = (KeyReference) this.reqQueue.poll();
            if (ref != null) {
                this.lruCache.remove(ref.getKey());
            } else {
                return;
            }
        }
    }

    public void clear() {
        this.lruCache.clear();
    }

    /* loaded from: icepdf-core.jar:org/icepdf/core/util/SoftLRUCache$KeyReference.class */
    private static class KeyReference<K, V> extends SoftReference<V> {
        private K key;

        public KeyReference(K key, V value, ReferenceQueue<? super V> refQueue) {
            super(value, refQueue);
            this.key = key;
        }

        public K getKey() {
            return this.key;
        }
    }
}
