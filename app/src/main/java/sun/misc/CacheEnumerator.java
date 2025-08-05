package sun.misc;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/* compiled from: Cache.java */
/* loaded from: rt.jar:sun/misc/CacheEnumerator.class */
class CacheEnumerator implements Enumeration {
    boolean keys;
    int index;
    CacheEntry[] table;
    CacheEntry entry;

    CacheEnumerator(CacheEntry[] cacheEntryArr, boolean z2) {
        this.table = cacheEntryArr;
        this.keys = z2;
        this.index = cacheEntryArr.length;
    }

    @Override // java.util.Enumeration
    public boolean hasMoreElements() {
        CacheEntry cacheEntry;
        while (this.index >= 0) {
            while (this.entry != null) {
                if (this.entry.check() != null) {
                    return true;
                }
                this.entry = this.entry.next;
            }
            do {
                int i2 = this.index - 1;
                this.index = i2;
                if (i2 >= 0) {
                    cacheEntry = this.table[this.index];
                    this.entry = cacheEntry;
                }
            } while (cacheEntry == null);
        }
        return false;
    }

    @Override // java.util.Enumeration
    public Object nextElement() {
        CacheEntry cacheEntry;
        while (this.index >= 0) {
            if (this.entry == null) {
                do {
                    int i2 = this.index - 1;
                    this.index = i2;
                    if (i2 < 0) {
                        break;
                    }
                    cacheEntry = this.table[this.index];
                    this.entry = cacheEntry;
                } while (cacheEntry == null);
            }
            if (this.entry != null) {
                CacheEntry cacheEntry2 = this.entry;
                this.entry = cacheEntry2.next;
                if (cacheEntry2.check() != null) {
                    return this.keys ? cacheEntry2.key : cacheEntry2.check();
                }
            }
        }
        throw new NoSuchElementException("CacheEnumerator");
    }
}
