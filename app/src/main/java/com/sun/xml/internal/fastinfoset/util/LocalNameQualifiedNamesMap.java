package com.sun.xml.internal.fastinfoset.util;

import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
import com.sun.xml.internal.fastinfoset.QualifiedName;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/util/LocalNameQualifiedNamesMap.class */
public class LocalNameQualifiedNamesMap extends KeyIntMap {
    private LocalNameQualifiedNamesMap _readOnlyMap;
    private int _index;
    private Entry[] _table;

    /* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/util/LocalNameQualifiedNamesMap$Entry.class */
    public static class Entry {
        final String _key;
        final int _hash;
        public QualifiedName[] _value = new QualifiedName[1];
        public int _valueIndex;
        Entry _next;

        public Entry(String key, int hash, Entry next) {
            this._key = key;
            this._hash = hash;
            this._next = next;
        }

        public void addQualifiedName(QualifiedName name) {
            if (this._valueIndex < this._value.length) {
                QualifiedName[] qualifiedNameArr = this._value;
                int i2 = this._valueIndex;
                this._valueIndex = i2 + 1;
                qualifiedNameArr[i2] = name;
                return;
            }
            if (this._valueIndex == this._value.length) {
                QualifiedName[] newValue = new QualifiedName[((this._valueIndex * 3) / 2) + 1];
                System.arraycopy(this._value, 0, newValue, 0, this._valueIndex);
                this._value = newValue;
                QualifiedName[] qualifiedNameArr2 = this._value;
                int i3 = this._valueIndex;
                this._valueIndex = i3 + 1;
                qualifiedNameArr2[i3] = name;
            }
        }
    }

    public LocalNameQualifiedNamesMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        this._table = new Entry[this._capacity];
    }

    public LocalNameQualifiedNamesMap(int initialCapacity) {
        this(initialCapacity, 0.75f);
    }

    public LocalNameQualifiedNamesMap() {
        this(16, 0.75f);
    }

    @Override // com.sun.xml.internal.fastinfoset.util.KeyIntMap
    public final void clear() {
        for (int i2 = 0; i2 < this._table.length; i2++) {
            this._table[i2] = null;
        }
        this._size = 0;
        if (this._readOnlyMap != null) {
            this._index = this._readOnlyMap.getIndex();
        } else {
            this._index = 0;
        }
    }

    @Override // com.sun.xml.internal.fastinfoset.util.KeyIntMap
    public final void setReadOnlyMap(KeyIntMap readOnlyMap, boolean clear) {
        if (!(readOnlyMap instanceof LocalNameQualifiedNamesMap)) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.illegalClass", new Object[]{readOnlyMap}));
        }
        setReadOnlyMap((LocalNameQualifiedNamesMap) readOnlyMap, clear);
    }

    public final void setReadOnlyMap(LocalNameQualifiedNamesMap readOnlyMap, boolean clear) {
        this._readOnlyMap = readOnlyMap;
        if (this._readOnlyMap != null) {
            this._readOnlyMapSize = this._readOnlyMap.size();
            this._index = this._readOnlyMap.getIndex();
            if (clear) {
                clear();
                return;
            }
            return;
        }
        this._readOnlyMapSize = 0;
        this._index = 0;
    }

    public final boolean isQNameFromReadOnlyMap(QualifiedName name) {
        return this._readOnlyMap != null && name.index <= this._readOnlyMap.getIndex();
    }

    public final int getNextIndex() {
        int i2 = this._index;
        this._index = i2 + 1;
        return i2;
    }

    public final int getIndex() {
        return this._index;
    }

    public final Entry obtainEntry(String key) {
        Entry entry;
        int hash = hashHash(key.hashCode());
        if (this._readOnlyMap != null && (entry = this._readOnlyMap.getEntry(key, hash)) != null) {
            return entry;
        }
        int tableIndex = indexFor(hash, this._table.length);
        Entry entry2 = this._table[tableIndex];
        while (true) {
            Entry e2 = entry2;
            if (e2 != null) {
                if (e2._hash != hash || !eq(key, e2._key)) {
                    entry2 = e2._next;
                } else {
                    return e2;
                }
            } else {
                return addEntry(key, hash, tableIndex);
            }
        }
    }

    public final Entry obtainDynamicEntry(String key) {
        int hash = hashHash(key.hashCode());
        int tableIndex = indexFor(hash, this._table.length);
        Entry entry = this._table[tableIndex];
        while (true) {
            Entry e2 = entry;
            if (e2 != null) {
                if (e2._hash != hash || !eq(key, e2._key)) {
                    entry = e2._next;
                } else {
                    return e2;
                }
            } else {
                return addEntry(key, hash, tableIndex);
            }
        }
    }

    private final Entry getEntry(String key, int hash) {
        Entry entry;
        if (this._readOnlyMap != null && (entry = this._readOnlyMap.getEntry(key, hash)) != null) {
            return entry;
        }
        int tableIndex = indexFor(hash, this._table.length);
        Entry entry2 = this._table[tableIndex];
        while (true) {
            Entry e2 = entry2;
            if (e2 != null) {
                if (e2._hash != hash || !eq(key, e2._key)) {
                    entry2 = e2._next;
                } else {
                    return e2;
                }
            } else {
                return null;
            }
        }
    }

    private final Entry addEntry(String key, int hash, int bucketIndex) {
        Entry e2 = this._table[bucketIndex];
        this._table[bucketIndex] = new Entry(key, hash, e2);
        Entry e3 = this._table[bucketIndex];
        int i2 = this._size;
        this._size = i2 + 1;
        if (i2 >= this._threshold) {
            resize(2 * this._table.length);
        }
        return e3;
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

    private final boolean eq(String x2, String y2) {
        return x2 == y2 || x2.equals(y2);
    }
}
