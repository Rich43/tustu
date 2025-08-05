package com.sun.xml.internal.fastinfoset.util;

import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
import com.sun.xml.internal.fastinfoset.util.KeyIntMap;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/util/StringIntMap.class */
public class StringIntMap extends KeyIntMap {
    protected static final Entry NULL_ENTRY = new Entry(null, 0, -1, null);
    protected StringIntMap _readOnlyMap;
    protected Entry _lastEntry;
    protected Entry[] _table;
    protected int _index;
    protected int _totalCharacterCount;

    /* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/util/StringIntMap$Entry.class */
    protected static class Entry extends KeyIntMap.BaseEntry {
        final String _key;
        Entry _next;

        public Entry(String key, int hash, int value, Entry next) {
            super(hash, value);
            this._key = key;
            this._next = next;
        }
    }

    public StringIntMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        this._lastEntry = NULL_ENTRY;
        this._table = new Entry[this._capacity];
    }

    public StringIntMap(int initialCapacity) {
        this(initialCapacity, 0.75f);
    }

    public StringIntMap() {
        this(16, 0.75f);
    }

    @Override // com.sun.xml.internal.fastinfoset.util.KeyIntMap
    public void clear() {
        for (int i2 = 0; i2 < this._table.length; i2++) {
            this._table[i2] = null;
        }
        this._lastEntry = NULL_ENTRY;
        this._size = 0;
        this._index = this._readOnlyMapSize;
        this._totalCharacterCount = 0;
    }

    @Override // com.sun.xml.internal.fastinfoset.util.KeyIntMap
    public void setReadOnlyMap(KeyIntMap readOnlyMap, boolean clear) {
        if (!(readOnlyMap instanceof StringIntMap)) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.illegalClass", new Object[]{readOnlyMap}));
        }
        setReadOnlyMap((StringIntMap) readOnlyMap, clear);
    }

    public final void setReadOnlyMap(StringIntMap readOnlyMap, boolean clear) {
        this._readOnlyMap = readOnlyMap;
        if (this._readOnlyMap != null) {
            this._readOnlyMapSize = this._readOnlyMap.size();
            this._index = this._size + this._readOnlyMapSize;
            if (clear) {
                clear();
                return;
            }
            return;
        }
        this._readOnlyMapSize = 0;
        this._index = this._size;
    }

    public final int getNextIndex() {
        int i2 = this._index;
        this._index = i2 + 1;
        return i2;
    }

    public final int getIndex() {
        return this._index;
    }

    public final int obtainIndex(String key) {
        int index;
        int hash = hashHash(key.hashCode());
        if (this._readOnlyMap != null && (index = this._readOnlyMap.get(key, hash)) != -1) {
            return index;
        }
        int tableIndex = indexFor(hash, this._table.length);
        Entry entry = this._table[tableIndex];
        while (true) {
            Entry e2 = entry;
            if (e2 != null) {
                if (e2._hash != hash || !eq(key, e2._key)) {
                    entry = e2._next;
                } else {
                    return e2._value;
                }
            } else {
                addEntry(key, hash, tableIndex);
                return -1;
            }
        }
    }

    public final void add(String key) {
        int hash = hashHash(key.hashCode());
        int tableIndex = indexFor(hash, this._table.length);
        addEntry(key, hash, tableIndex);
    }

    public final int get(String key) {
        if (key == this._lastEntry._key) {
            return this._lastEntry._value;
        }
        return get(key, hashHash(key.hashCode()));
    }

    public final int getTotalCharacterCount() {
        return this._totalCharacterCount;
    }

    private final int get(String key, int hash) {
        int i2;
        if (this._readOnlyMap != null && (i2 = this._readOnlyMap.get(key, hash)) != -1) {
            return i2;
        }
        int tableIndex = indexFor(hash, this._table.length);
        Entry entry = this._table[tableIndex];
        while (true) {
            Entry e2 = entry;
            if (e2 != null) {
                if (e2._hash != hash || !eq(key, e2._key)) {
                    entry = e2._next;
                } else {
                    this._lastEntry = e2;
                    return e2._value;
                }
            } else {
                return -1;
            }
        }
    }

    private final void addEntry(String key, int hash, int bucketIndex) {
        Entry e2 = this._table[bucketIndex];
        Entry[] entryArr = this._table;
        int i2 = this._index;
        this._index = i2 + 1;
        entryArr[bucketIndex] = new Entry(key, hash, i2, e2);
        this._totalCharacterCount += key.length();
        int i3 = this._size;
        this._size = i3 + 1;
        if (i3 >= this._threshold) {
            resize(2 * this._table.length);
        }
    }

    protected final void resize(int newCapacity) {
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

    private final boolean eq(String x2, String y2) {
        return x2 == y2 || x2.equals(y2);
    }
}
