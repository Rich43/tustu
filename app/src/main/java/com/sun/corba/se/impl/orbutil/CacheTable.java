package com.sun.corba.se.impl.orbutil;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;

/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/CacheTable.class */
public class CacheTable {
    private boolean noReverseMap;
    static final int INITIAL_SIZE = 16;
    static final int MAX_SIZE = 1073741824;
    int size;
    int entryCount;
    private Entry[] map;
    private Entry[] rmap;
    private ORB orb;
    private ORBUtilSystemException wrapper;

    /* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/CacheTable$Entry.class */
    class Entry {
        Object key;
        int val;
        Entry next = null;
        Entry rnext = null;

        public Entry(Object obj, int i2) {
            this.key = obj;
            this.val = i2;
        }
    }

    private CacheTable() {
    }

    public CacheTable(ORB orb, boolean z2) {
        this.orb = orb;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_ENCODING);
        this.noReverseMap = z2;
        this.size = 16;
        this.entryCount = 0;
        initTables();
    }

    private void initTables() {
        this.map = new Entry[this.size];
        this.rmap = this.noReverseMap ? null : new Entry[this.size];
    }

    private void grow() {
        if (this.size == 1073741824) {
            return;
        }
        Entry[] entryArr = this.map;
        int i2 = this.size;
        this.size <<= 1;
        initTables();
        for (int i3 = 0; i3 < i2; i3++) {
            Entry entry = entryArr[i3];
            while (true) {
                Entry entry2 = entry;
                if (entry2 != null) {
                    put_table(entry2.key, entry2.val);
                    entry = entry2.next;
                }
            }
        }
    }

    private int moduloTableSize(int i2) {
        int i3 = i2 + ((i2 << 9) ^ (-1));
        int i4 = i3 ^ (i3 >>> 14);
        int i5 = i4 + (i4 << 4);
        return (i5 ^ (i5 >>> 10)) & (this.size - 1);
    }

    private int hash(Object obj) {
        return moduloTableSize(System.identityHashCode(obj));
    }

    private int hash(int i2) {
        return moduloTableSize(i2);
    }

    public final void put(Object obj, int i2) {
        if (put_table(obj, i2)) {
            this.entryCount++;
            if (this.entryCount > (this.size * 3) / 4) {
                grow();
            }
        }
    }

    private boolean put_table(Object obj, int i2) {
        int iHash = hash(obj);
        Entry entry = this.map[iHash];
        while (true) {
            Entry entry2 = entry;
            if (entry2 != null) {
                if (entry2.key != obj) {
                    entry = entry2.next;
                } else {
                    if (entry2.val != i2) {
                        throw this.wrapper.duplicateIndirectionOffset();
                    }
                    return false;
                }
            } else {
                Entry entry3 = new Entry(obj, i2);
                entry3.next = this.map[iHash];
                this.map[iHash] = entry3;
                if (!this.noReverseMap) {
                    int iHash2 = hash(i2);
                    entry3.rnext = this.rmap[iHash2];
                    this.rmap[iHash2] = entry3;
                    return true;
                }
                return true;
            }
        }
    }

    public final boolean containsKey(Object obj) {
        return getVal(obj) != -1;
    }

    public final int getVal(Object obj) {
        Entry entry = this.map[hash(obj)];
        while (true) {
            Entry entry2 = entry;
            if (entry2 != null) {
                if (entry2.key != obj) {
                    entry = entry2.next;
                } else {
                    return entry2.val;
                }
            } else {
                return -1;
            }
        }
    }

    public final boolean containsVal(int i2) {
        return getKey(i2) != null;
    }

    public final boolean containsOrderedVal(int i2) {
        return containsVal(i2);
    }

    public final Object getKey(int i2) {
        Entry entry = this.rmap[hash(i2)];
        while (true) {
            Entry entry2 = entry;
            if (entry2 != null) {
                if (entry2.val != i2) {
                    entry = entry2.rnext;
                } else {
                    return entry2.key;
                }
            } else {
                return null;
            }
        }
    }

    public void done() {
        this.map = null;
        this.rmap = null;
    }
}
