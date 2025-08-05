package com.sun.org.apache.xerces.internal.util;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/SymbolHash.class */
public class SymbolHash {
    protected static final int TABLE_SIZE = 101;
    protected static final int MAX_HASH_COLLISIONS = 40;
    protected static final int MULTIPLIERS_SIZE = 32;
    protected static final int MULTIPLIERS_MASK = 31;
    protected int fTableSize;
    protected Entry[] fBuckets;
    protected int fNum;
    protected int[] fHashMultipliers;

    public SymbolHash() {
        this(101);
    }

    public SymbolHash(int size) {
        this.fNum = 0;
        this.fTableSize = size;
        this.fBuckets = new Entry[this.fTableSize];
    }

    public void put(Object key, Object value) {
        int collisionCount = 0;
        int hash = hash(key);
        int bucket = hash % this.fTableSize;
        Entry entry = this.fBuckets[bucket];
        while (true) {
            Entry entry2 = entry;
            if (entry2 != null) {
                if (key.equals(entry2.key)) {
                    entry2.value = value;
                    return;
                } else {
                    collisionCount++;
                    entry = entry2.next;
                }
            } else {
                if (this.fNum >= this.fTableSize) {
                    rehash();
                    bucket = hash % this.fTableSize;
                } else if (collisionCount >= 40 && (key instanceof String)) {
                    rebalance();
                    bucket = hash(key) % this.fTableSize;
                }
                this.fBuckets[bucket] = new Entry(key, value, this.fBuckets[bucket]);
                this.fNum++;
                return;
            }
        }
    }

    public Object get(Object key) {
        int bucket = hash(key) % this.fTableSize;
        Entry entry = search(key, bucket);
        if (entry != null) {
            return entry.value;
        }
        return null;
    }

    public int getLength() {
        return this.fNum;
    }

    public int getValues(Object[] elements, int from) {
        int j2 = 0;
        for (int i2 = 0; i2 < this.fTableSize && j2 < this.fNum; i2++) {
            Entry entry = this.fBuckets[i2];
            while (true) {
                Entry entry2 = entry;
                if (entry2 != null) {
                    elements[from + j2] = entry2.value;
                    j2++;
                    entry = entry2.next;
                }
            }
        }
        return this.fNum;
    }

    public Object[] getEntries() {
        Object[] entries = new Object[this.fNum << 1];
        int j2 = 0;
        for (int i2 = 0; i2 < this.fTableSize && j2 < (this.fNum << 1); i2++) {
            Entry entry = this.fBuckets[i2];
            while (true) {
                Entry entry2 = entry;
                if (entry2 != null) {
                    entries[j2] = entry2.key;
                    int j3 = j2 + 1;
                    entries[j3] = entry2.value;
                    j2 = j3 + 1;
                    entry = entry2.next;
                }
            }
        }
        return entries;
    }

    public SymbolHash makeClone() {
        SymbolHash newTable = new SymbolHash(this.fTableSize);
        newTable.fNum = this.fNum;
        newTable.fHashMultipliers = this.fHashMultipliers != null ? (int[]) this.fHashMultipliers.clone() : null;
        for (int i2 = 0; i2 < this.fTableSize; i2++) {
            if (this.fBuckets[i2] != null) {
                newTable.fBuckets[i2] = this.fBuckets[i2].makeClone();
            }
        }
        return newTable;
    }

    public void clear() {
        for (int i2 = 0; i2 < this.fTableSize; i2++) {
            this.fBuckets[i2] = null;
        }
        this.fNum = 0;
        this.fHashMultipliers = null;
    }

    protected Entry search(Object key, int bucket) {
        Entry entry = this.fBuckets[bucket];
        while (true) {
            Entry entry2 = entry;
            if (entry2 != null) {
                if (!key.equals(entry2.key)) {
                    entry = entry2.next;
                } else {
                    return entry2;
                }
            } else {
                return null;
            }
        }
    }

    protected int hash(Object key) {
        if (this.fHashMultipliers == null || !(key instanceof String)) {
            return key.hashCode() & Integer.MAX_VALUE;
        }
        return hash0((String) key);
    }

    private int hash0(String symbol) {
        int code = 0;
        int length = symbol.length();
        int[] multipliers = this.fHashMultipliers;
        for (int i2 = 0; i2 < length; i2++) {
            code = (code * multipliers[i2 & 31]) + symbol.charAt(i2);
        }
        return code & Integer.MAX_VALUE;
    }

    protected void rehash() {
        rehashCommon((this.fBuckets.length << 1) + 1);
    }

    protected void rebalance() {
        if (this.fHashMultipliers == null) {
            this.fHashMultipliers = new int[32];
        }
        PrimeNumberSequenceGenerator.generateSequence(this.fHashMultipliers);
        rehashCommon(this.fBuckets.length);
    }

    private void rehashCommon(int newCapacity) {
        int oldCapacity = this.fBuckets.length;
        Entry[] oldTable = this.fBuckets;
        Entry[] newTable = new Entry[newCapacity];
        this.fBuckets = newTable;
        this.fTableSize = this.fBuckets.length;
        int i2 = oldCapacity;
        while (true) {
            int i3 = i2;
            i2--;
            if (i3 > 0) {
                Entry old = oldTable[i2];
                while (old != null) {
                    Entry e2 = old;
                    old = old.next;
                    int index = hash(e2.key) % newCapacity;
                    e2.next = newTable[index];
                    newTable[index] = e2;
                }
            } else {
                return;
            }
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/SymbolHash$Entry.class */
    protected static final class Entry {
        public Object key;
        public Object value;
        public Entry next;

        public Entry() {
            this.key = null;
            this.value = null;
            this.next = null;
        }

        public Entry(Object key, Object value, Entry next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public Entry makeClone() {
            Entry entry = new Entry();
            entry.key = this.key;
            entry.value = this.value;
            if (this.next != null) {
                entry.next = this.next.makeClone();
            }
            return entry;
        }
    }
}
