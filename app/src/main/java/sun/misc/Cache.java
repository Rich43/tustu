package sun.misc;

import java.util.Dictionary;
import java.util.Enumeration;

/* loaded from: rt.jar:sun/misc/Cache.class */
public class Cache extends Dictionary {
    private CacheEntry[] table;
    private int count;
    private int threshold;
    private float loadFactor;

    private void init(int i2, float f2) {
        if (i2 <= 0 || f2 <= 0.0d) {
            throw new IllegalArgumentException();
        }
        this.loadFactor = f2;
        this.table = new CacheEntry[i2];
        this.threshold = (int) (i2 * f2);
    }

    public Cache(int i2, float f2) {
        init(i2, f2);
    }

    public Cache(int i2) {
        init(i2, 0.75f);
    }

    public Cache() {
        try {
            init(101, 0.75f);
        } catch (IllegalArgumentException e2) {
            throw new Error("panic");
        }
    }

    @Override // java.util.Dictionary
    public int size() {
        return this.count;
    }

    @Override // java.util.Dictionary
    public boolean isEmpty() {
        return this.count == 0;
    }

    @Override // java.util.Dictionary
    public synchronized Enumeration keys() {
        return new CacheEnumerator(this.table, true);
    }

    @Override // java.util.Dictionary
    public synchronized Enumeration elements() {
        return new CacheEnumerator(this.table, false);
    }

    @Override // java.util.Dictionary
    public synchronized Object get(Object obj) {
        CacheEntry[] cacheEntryArr = this.table;
        int iHashCode = obj.hashCode();
        CacheEntry cacheEntry = cacheEntryArr[(iHashCode & Integer.MAX_VALUE) % cacheEntryArr.length];
        while (true) {
            CacheEntry cacheEntry2 = cacheEntry;
            if (cacheEntry2 != null) {
                if (cacheEntry2.hash != iHashCode || !cacheEntry2.key.equals(obj)) {
                    cacheEntry = cacheEntry2.next;
                } else {
                    return cacheEntry2.check();
                }
            } else {
                return null;
            }
        }
    }

    protected void rehash() {
        int length = this.table.length;
        CacheEntry[] cacheEntryArr = this.table;
        int i2 = (length * 2) + 1;
        CacheEntry[] cacheEntryArr2 = new CacheEntry[i2];
        this.threshold = (int) (i2 * this.loadFactor);
        this.table = cacheEntryArr2;
        int i3 = length;
        while (true) {
            int i4 = i3;
            i3--;
            if (i4 > 0) {
                CacheEntry cacheEntry = cacheEntryArr[i3];
                while (cacheEntry != null) {
                    CacheEntry cacheEntry2 = cacheEntry;
                    cacheEntry = cacheEntry.next;
                    if (cacheEntry2.check() != null) {
                        int i5 = (cacheEntry2.hash & Integer.MAX_VALUE) % i2;
                        cacheEntry2.next = cacheEntryArr2[i5];
                        cacheEntryArr2[i5] = cacheEntry2;
                    } else {
                        this.count--;
                    }
                }
            } else {
                return;
            }
        }
    }

    @Override // java.util.Dictionary
    public synchronized Object put(Object obj, Object obj2) {
        if (obj2 == null) {
            throw new NullPointerException();
        }
        CacheEntry[] cacheEntryArr = this.table;
        int iHashCode = obj.hashCode();
        int length = (iHashCode & Integer.MAX_VALUE) % cacheEntryArr.length;
        CacheEntry cacheEntry = null;
        CacheEntry cacheEntry2 = cacheEntryArr[length];
        while (true) {
            CacheEntry cacheEntry3 = cacheEntry2;
            if (cacheEntry3 != null) {
                if (cacheEntry3.hash == iHashCode && cacheEntry3.key.equals(obj)) {
                    Object objCheck = cacheEntry3.check();
                    cacheEntry3.setThing(obj2);
                    return objCheck;
                }
                if (cacheEntry3.check() == null) {
                    cacheEntry = cacheEntry3;
                }
                cacheEntry2 = cacheEntry3.next;
            } else {
                if (this.count >= this.threshold) {
                    rehash();
                    return put(obj, obj2);
                }
                if (cacheEntry == null) {
                    cacheEntry = new CacheEntry();
                    cacheEntry.next = cacheEntryArr[length];
                    cacheEntryArr[length] = cacheEntry;
                    this.count++;
                }
                cacheEntry.hash = iHashCode;
                cacheEntry.key = obj;
                cacheEntry.setThing(obj2);
                return null;
            }
        }
    }

    @Override // java.util.Dictionary
    public synchronized Object remove(Object obj) {
        CacheEntry[] cacheEntryArr = this.table;
        int iHashCode = obj.hashCode();
        int length = (iHashCode & Integer.MAX_VALUE) % cacheEntryArr.length;
        CacheEntry cacheEntry = null;
        for (CacheEntry cacheEntry2 = cacheEntryArr[length]; cacheEntry2 != null; cacheEntry2 = cacheEntry2.next) {
            if (cacheEntry2.hash != iHashCode || !cacheEntry2.key.equals(obj)) {
                cacheEntry = cacheEntry2;
            } else {
                if (cacheEntry != null) {
                    cacheEntry.next = cacheEntry2.next;
                } else {
                    cacheEntryArr[length] = cacheEntry2.next;
                }
                this.count--;
                return cacheEntry2.check();
            }
        }
        return null;
    }
}
