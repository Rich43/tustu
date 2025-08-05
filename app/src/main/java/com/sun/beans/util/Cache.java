package com.sun.beans.util;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Objects;

/* loaded from: rt.jar:com/sun/beans/util/Cache.class */
public abstract class Cache<K, V> {
    private static final int MAXIMUM_CAPACITY = 1073741824;
    private final boolean identity;
    private final Kind keyKind;
    private final Kind valueKind;
    private final ReferenceQueue<Object> queue;
    private volatile Cache<K, V>.CacheEntry<K, V>[] table;
    private int threshold;
    private int size;

    /* loaded from: rt.jar:com/sun/beans/util/Cache$Ref.class */
    private interface Ref<T> {
        Object getOwner();

        T getReferent();

        boolean isStale();

        void removeOwner();
    }

    public abstract V create(K k2);

    static /* synthetic */ int access$1110(Cache cache) {
        int i2 = cache.size;
        cache.size = i2 - 1;
        return i2;
    }

    public Cache(Kind kind, Kind kind2) {
        this(kind, kind2, false);
    }

    public Cache(Kind kind, Kind kind2, boolean z2) {
        this.queue = new ReferenceQueue<>();
        this.table = newTable(8);
        this.threshold = 6;
        Objects.requireNonNull(kind, "keyKind");
        Objects.requireNonNull(kind2, "valueKind");
        this.keyKind = kind;
        this.valueKind = kind2;
        this.identity = z2;
    }

    public final V get(K k2) {
        Objects.requireNonNull(k2, "key");
        removeStaleEntries();
        int iHash = hash(k2);
        Cache<K, V>.CacheEntry<K, V>[] cacheEntryArr = this.table;
        V entryValue = getEntryValue(k2, iHash, cacheEntryArr[index(iHash, cacheEntryArr)]);
        if (entryValue != null) {
            return entryValue;
        }
        synchronized (this.queue) {
            V entryValue2 = getEntryValue(k2, iHash, this.table[index(iHash, this.table)]);
            if (entryValue2 != null) {
                return entryValue2;
            }
            V vCreate = create(k2);
            Objects.requireNonNull(vCreate, "value");
            int iIndex = index(iHash, this.table);
            this.table[iIndex] = new CacheEntry<>(iHash, k2, vCreate, this.table[iIndex]);
            int i2 = this.size + 1;
            this.size = i2;
            if (i2 >= this.threshold) {
                if (this.table.length == 1073741824) {
                    this.threshold = Integer.MAX_VALUE;
                } else {
                    removeStaleEntries();
                    Cache<K, V>.CacheEntry<K, V>[] cacheEntryArrNewTable = newTable(this.table.length << 1);
                    transfer(this.table, cacheEntryArrNewTable);
                    if (this.size >= this.threshold / 2) {
                        this.table = cacheEntryArrNewTable;
                        this.threshold <<= 1;
                    } else {
                        transfer(cacheEntryArrNewTable, this.table);
                    }
                    removeStaleEntries();
                }
            }
            return vCreate;
        }
    }

    public final void remove(K k2) {
        if (k2 != null) {
            synchronized (this.queue) {
                removeStaleEntries();
                int iHash = hash(k2);
                int iIndex = index(iHash, this.table);
                Cache<K, V>.CacheEntry<K, V> cacheEntry = this.table[iIndex];
                Cache<K, V>.CacheEntry<K, V> cacheEntry2 = cacheEntry;
                while (true) {
                    if (cacheEntry2 == null) {
                        break;
                    }
                    Cache<K, V>.CacheEntry<K, V> cacheEntry3 = ((CacheEntry) cacheEntry2).next;
                    if (cacheEntry2.matches(iHash, k2)) {
                        if (cacheEntry2 == cacheEntry) {
                            this.table[iIndex] = cacheEntry3;
                        } else {
                            ((CacheEntry) cacheEntry).next = cacheEntry3;
                        }
                        cacheEntry2.unlink();
                    } else {
                        cacheEntry = cacheEntry2;
                        cacheEntry2 = cacheEntry3;
                    }
                }
            }
        }
    }

    public final void clear() {
        synchronized (this.queue) {
            int length = this.table.length;
            while (true) {
                int i2 = length;
                length--;
                if (0 >= i2) {
                    break;
                }
                Cache<K, V>.CacheEntry<K, V> cacheEntry = this.table[length];
                while (cacheEntry != null) {
                    Cache<K, V>.CacheEntry<K, V> cacheEntry2 = ((CacheEntry) cacheEntry).next;
                    cacheEntry.unlink();
                    cacheEntry = cacheEntry2;
                }
                this.table[length] = null;
            }
            while (null != this.queue.poll()) {
            }
        }
    }

    private int hash(Object obj) {
        if (this.identity) {
            int iIdentityHashCode = System.identityHashCode(obj);
            return (iIdentityHashCode << 1) - (iIdentityHashCode << 8);
        }
        int iHashCode = obj.hashCode();
        int i2 = iHashCode ^ ((iHashCode >>> 20) ^ (iHashCode >>> 12));
        return (i2 ^ (i2 >>> 7)) ^ (i2 >>> 4);
    }

    private static int index(int i2, Object[] objArr) {
        return i2 & (objArr.length - 1);
    }

    private Cache<K, V>.CacheEntry<K, V>[] newTable(int i2) {
        return new CacheEntry[i2];
    }

    private V getEntryValue(K k2, int i2, Cache<K, V>.CacheEntry<K, V> cacheEntry) {
        while (cacheEntry != null) {
            if (cacheEntry.matches(i2, k2)) {
                return (V) ((CacheEntry) cacheEntry).value.getReferent();
            }
            cacheEntry = ((CacheEntry) cacheEntry).next;
        }
        return null;
    }

    private void removeStaleEntries() {
        Cache<K, V>.CacheEntry<K, V> cacheEntry;
        Reference<? extends Object> referencePoll = this.queue.poll();
        if (referencePoll != null) {
            synchronized (this.queue) {
                do {
                    if ((referencePoll instanceof Ref) && (cacheEntry = (CacheEntry) ((Ref) referencePoll).getOwner()) != null) {
                        int iIndex = index(((CacheEntry) cacheEntry).hash, this.table);
                        Cache<K, V>.CacheEntry<K, V> cacheEntry2 = this.table[iIndex];
                        Cache<K, V>.CacheEntry<K, V> cacheEntry3 = cacheEntry2;
                        while (true) {
                            if (cacheEntry3 == null) {
                                break;
                            }
                            Cache<K, V>.CacheEntry<K, V> cacheEntry4 = ((CacheEntry) cacheEntry3).next;
                            if (cacheEntry3 == cacheEntry) {
                                if (cacheEntry3 == cacheEntry2) {
                                    this.table[iIndex] = cacheEntry4;
                                } else {
                                    ((CacheEntry) cacheEntry2).next = cacheEntry4;
                                }
                                cacheEntry3.unlink();
                            } else {
                                cacheEntry2 = cacheEntry3;
                                cacheEntry3 = cacheEntry4;
                            }
                        }
                    }
                    referencePoll = this.queue.poll();
                } while (referencePoll != null);
            }
        }
    }

    private void transfer(Cache<K, V>.CacheEntry<K, V>[] cacheEntryArr, Cache<K, V>.CacheEntry<K, V>[] cacheEntryArr2) {
        int length = cacheEntryArr.length;
        while (true) {
            int i2 = length;
            length--;
            if (0 < i2) {
                Cache<K, V>.CacheEntry<K, V> cacheEntry = cacheEntryArr[length];
                cacheEntryArr[length] = null;
                while (cacheEntry != null) {
                    Cache<K, V>.CacheEntry<K, V> cacheEntry2 = ((CacheEntry) cacheEntry).next;
                    if (((CacheEntry) cacheEntry).key.isStale() || ((CacheEntry) cacheEntry).value.isStale()) {
                        cacheEntry.unlink();
                    } else {
                        int iIndex = index(((CacheEntry) cacheEntry).hash, cacheEntryArr2);
                        ((CacheEntry) cacheEntry).next = cacheEntryArr2[iIndex];
                        cacheEntryArr2[iIndex] = cacheEntry;
                    }
                    cacheEntry = cacheEntry2;
                }
            } else {
                return;
            }
        }
    }

    /* loaded from: rt.jar:com/sun/beans/util/Cache$CacheEntry.class */
    private final class CacheEntry<K, V> {
        private final int hash;
        private final Ref<K> key;
        private final Ref<V> value;
        private volatile Cache<K, V>.CacheEntry<K, V> next;

        private CacheEntry(int i2, K k2, V v2, Cache<K, V>.CacheEntry<K, V> cacheEntry) {
            this.hash = i2;
            this.key = Cache.this.keyKind.create(this, k2, Cache.this.queue);
            this.value = Cache.this.valueKind.create(this, v2, Cache.this.queue);
            this.next = cacheEntry;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean matches(int i2, Object obj) {
            if (this.hash != i2) {
                return false;
            }
            K referent = this.key.getReferent();
            return referent == obj || !(Cache.this.identity || referent == null || !referent.equals(obj));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void unlink() {
            this.next = null;
            this.key.removeOwner();
            this.value.removeOwner();
            Cache.access$1110(Cache.this);
        }
    }

    /* loaded from: rt.jar:com/sun/beans/util/Cache$Kind.class */
    public enum Kind {
        STRONG { // from class: com.sun.beans.util.Cache.Kind.1
            @Override // com.sun.beans.util.Cache.Kind
            <T> Ref<T> create(Object obj, T t2, ReferenceQueue<? super T> referenceQueue) {
                return new Strong(obj, t2);
            }
        },
        SOFT { // from class: com.sun.beans.util.Cache.Kind.2
            @Override // com.sun.beans.util.Cache.Kind
            <T> Ref<T> create(Object obj, T t2, ReferenceQueue<? super T> referenceQueue) {
                return t2 == null ? new Strong(obj, t2) : new Soft(obj, t2, referenceQueue);
            }
        },
        WEAK { // from class: com.sun.beans.util.Cache.Kind.3
            @Override // com.sun.beans.util.Cache.Kind
            <T> Ref<T> create(Object obj, T t2, ReferenceQueue<? super T> referenceQueue) {
                return t2 == null ? new Strong(obj, t2) : new Weak(obj, t2, referenceQueue);
            }
        };

        abstract <T> Ref<T> create(Object obj, T t2, ReferenceQueue<? super T> referenceQueue);

        /* loaded from: rt.jar:com/sun/beans/util/Cache$Kind$Strong.class */
        private static final class Strong<T> implements Ref<T> {
            private Object owner;
            private final T referent;

            private Strong(Object obj, T t2) {
                this.owner = obj;
                this.referent = t2;
            }

            @Override // com.sun.beans.util.Cache.Ref
            public Object getOwner() {
                return this.owner;
            }

            @Override // com.sun.beans.util.Cache.Ref
            public T getReferent() {
                return this.referent;
            }

            @Override // com.sun.beans.util.Cache.Ref
            public boolean isStale() {
                return false;
            }

            @Override // com.sun.beans.util.Cache.Ref
            public void removeOwner() {
                this.owner = null;
            }
        }

        /* loaded from: rt.jar:com/sun/beans/util/Cache$Kind$Soft.class */
        private static final class Soft<T> extends SoftReference<T> implements Ref<T> {
            private Object owner;

            private Soft(Object obj, T t2, ReferenceQueue<? super T> referenceQueue) {
                super(t2, referenceQueue);
                this.owner = obj;
            }

            @Override // com.sun.beans.util.Cache.Ref
            public Object getOwner() {
                return this.owner;
            }

            @Override // com.sun.beans.util.Cache.Ref
            public T getReferent() {
                return get();
            }

            @Override // com.sun.beans.util.Cache.Ref
            public boolean isStale() {
                return null == get();
            }

            @Override // com.sun.beans.util.Cache.Ref
            public void removeOwner() {
                this.owner = null;
            }
        }

        /* loaded from: rt.jar:com/sun/beans/util/Cache$Kind$Weak.class */
        private static final class Weak<T> extends WeakReference<T> implements Ref<T> {
            private Object owner;

            private Weak(Object obj, T t2, ReferenceQueue<? super T> referenceQueue) {
                super(t2, referenceQueue);
                this.owner = obj;
            }

            @Override // com.sun.beans.util.Cache.Ref
            public Object getOwner() {
                return this.owner;
            }

            @Override // com.sun.beans.util.Cache.Ref
            public T getReferent() {
                return get();
            }

            @Override // com.sun.beans.util.Cache.Ref
            public boolean isStale() {
                return null == get();
            }

            @Override // com.sun.beans.util.Cache.Ref
            public void removeOwner() {
                this.owner = null;
            }
        }
    }
}
