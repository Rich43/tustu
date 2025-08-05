package com.sun.xml.internal.fastinfoset.util;

import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
import com.sun.xml.internal.fastinfoset.util.KeyIntMap;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/util/CharArrayIntMap.class */
public class CharArrayIntMap extends KeyIntMap {
    private CharArrayIntMap _readOnlyMap;
    protected int _totalCharacterCount;
    private Entry[] _table;

    /* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/util/CharArrayIntMap$Entry.class */
    static class Entry extends KeyIntMap.BaseEntry {
        final char[] _ch;
        final int _start;
        final int _length;
        Entry _next;

        public Entry(char[] ch, int start, int length, int hash, int value, Entry next) {
            super(hash, value);
            this._ch = ch;
            this._start = start;
            this._length = length;
            this._next = next;
        }

        public final boolean equalsCharArray(char[] ch, int start, int length) {
            int i2;
            int i3;
            if (this._length == length) {
                int n2 = this._length;
                int i4 = this._start;
                int j2 = start;
                do {
                    int i5 = n2;
                    n2--;
                    if (i5 == 0) {
                        return true;
                    }
                    i2 = i4;
                    i4++;
                    i3 = j2;
                    j2++;
                } while (this._ch[i2] == ch[i3]);
                return false;
            }
            return false;
        }
    }

    public CharArrayIntMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        this._table = new Entry[this._capacity];
    }

    public CharArrayIntMap(int initialCapacity) {
        this(initialCapacity, 0.75f);
    }

    public CharArrayIntMap() {
        this(16, 0.75f);
    }

    @Override // com.sun.xml.internal.fastinfoset.util.KeyIntMap
    public final void clear() {
        for (int i2 = 0; i2 < this._table.length; i2++) {
            this._table[i2] = null;
        }
        this._size = 0;
        this._totalCharacterCount = 0;
    }

    @Override // com.sun.xml.internal.fastinfoset.util.KeyIntMap
    public final void setReadOnlyMap(KeyIntMap readOnlyMap, boolean clear) {
        if (!(readOnlyMap instanceof CharArrayIntMap)) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.illegalClass", new Object[]{readOnlyMap}));
        }
        setReadOnlyMap((CharArrayIntMap) readOnlyMap, clear);
    }

    public final void setReadOnlyMap(CharArrayIntMap readOnlyMap, boolean clear) {
        this._readOnlyMap = readOnlyMap;
        if (this._readOnlyMap != null) {
            this._readOnlyMapSize = this._readOnlyMap.size();
            if (clear) {
                clear();
                return;
            }
            return;
        }
        this._readOnlyMapSize = 0;
    }

    public final int get(char[] ch, int start, int length) {
        int hash = hashHash(CharArray.hashCode(ch, start, length));
        return get(ch, start, length, hash);
    }

    public final int obtainIndex(char[] ch, int start, int length, boolean clone) {
        int index;
        int hash = hashHash(CharArray.hashCode(ch, start, length));
        if (this._readOnlyMap != null && (index = this._readOnlyMap.get(ch, start, length, hash)) != -1) {
            return index;
        }
        int tableIndex = indexFor(hash, this._table.length);
        Entry entry = this._table[tableIndex];
        while (true) {
            Entry e2 = entry;
            if (e2 != null) {
                if (e2._hash != hash || !e2.equalsCharArray(ch, start, length)) {
                    entry = e2._next;
                } else {
                    return e2._value;
                }
            } else {
                if (clone) {
                    char[] chClone = new char[length];
                    System.arraycopy(ch, start, chClone, 0, length);
                    ch = chClone;
                    start = 0;
                }
                addEntry(ch, start, length, hash, this._size + this._readOnlyMapSize, tableIndex);
                return -1;
            }
        }
    }

    public final int getTotalCharacterCount() {
        return this._totalCharacterCount;
    }

    private final int get(char[] ch, int start, int length, int hash) {
        int i2;
        if (this._readOnlyMap != null && (i2 = this._readOnlyMap.get(ch, start, length, hash)) != -1) {
            return i2;
        }
        int tableIndex = indexFor(hash, this._table.length);
        Entry entry = this._table[tableIndex];
        while (true) {
            Entry e2 = entry;
            if (e2 != null) {
                if (e2._hash != hash || !e2.equalsCharArray(ch, start, length)) {
                    entry = e2._next;
                } else {
                    return e2._value;
                }
            } else {
                return -1;
            }
        }
    }

    private final void addEntry(char[] ch, int start, int length, int hash, int value, int bucketIndex) {
        Entry e2 = this._table[bucketIndex];
        this._table[bucketIndex] = new Entry(ch, start, length, hash, value, e2);
        this._totalCharacterCount += length;
        int i2 = this._size;
        this._size = i2 + 1;
        if (i2 >= this._threshold) {
            resize(2 * this._table.length);
        }
    }

    private final void resize(int newCapacity) {
        this._capacity = newCapacity;
        Entry[] oldTable = this._table;
        int oldCapacity = oldTable.length;
        if (oldCapacity == 1048576) {
            this._threshold = Integer.MAX_VALUE;
            return;
        }
        Entry[] newTable = new Entry[this._capacity];
        transfer(newTable);
        this._table = newTable;
        this._threshold = (int) (this._capacity * this._loadFactor);
    }

    private final void transfer(Entry[] newTable) {
        Entry[] src = this._table;
        int newCapacity = newTable.length;
        for (int j2 = 0; j2 < src.length; j2++) {
            Entry e2 = src[j2];
            if (e2 != null) {
                src[j2] = null;
                do {
                    Entry next = e2._next;
                    int i2 = indexFor(e2._hash, newCapacity);
                    e2._next = newTable[i2];
                    newTable[i2] = e2;
                    e2 = next;
                } while (e2 != null);
            }
        }
    }
}
