package com.sun.org.apache.xerces.internal.util;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/SymbolTable.class */
public class SymbolTable {
    protected static final int TABLE_SIZE = 101;
    protected static final int MAX_HASH_COLLISIONS = 40;
    protected static final int MULTIPLIERS_SIZE = 32;
    protected static final int MULTIPLIERS_MASK = 31;
    protected Entry[] fBuckets;
    protected int fTableSize;
    protected transient int fCount;
    protected int fThreshold;
    protected float fLoadFactor;
    protected final int fCollisionThreshold;
    protected int[] fHashMultipliers;

    public SymbolTable(int initialCapacity, float loadFactor) {
        this.fBuckets = null;
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }
        if (loadFactor <= 0.0f || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal Load: " + loadFactor);
        }
        initialCapacity = initialCapacity == 0 ? 1 : initialCapacity;
        this.fLoadFactor = loadFactor;
        this.fTableSize = initialCapacity;
        this.fBuckets = new Entry[this.fTableSize];
        this.fThreshold = (int) (this.fTableSize * loadFactor);
        this.fCollisionThreshold = (int) (40.0f * loadFactor);
        this.fCount = 0;
    }

    public SymbolTable(int initialCapacity) {
        this(initialCapacity, 0.75f);
    }

    public SymbolTable() {
        this(101, 0.75f);
    }

    public String addSymbol(String symbol) {
        int collisionCount = 0;
        int bucket = hash(symbol) % this.fTableSize;
        Entry entry = this.fBuckets[bucket];
        while (true) {
            Entry entry2 = entry;
            if (entry2 != null) {
                if (entry2.symbol.equals(symbol)) {
                    return entry2.symbol;
                }
                collisionCount++;
                entry = entry2.next;
            } else {
                return addSymbol0(symbol, bucket, collisionCount);
            }
        }
    }

    private String addSymbol0(String symbol, int bucket, int collisionCount) {
        if (this.fCount >= this.fThreshold) {
            rehash();
            bucket = hash(symbol) % this.fTableSize;
        } else if (collisionCount >= this.fCollisionThreshold) {
            rebalance();
            bucket = hash(symbol) % this.fTableSize;
        }
        Entry entry = new Entry(symbol, this.fBuckets[bucket]);
        this.fBuckets[bucket] = entry;
        this.fCount++;
        return entry.symbol;
    }

    public String addSymbol(char[] buffer, int offset, int length) {
        int collisionCount = 0;
        int bucket = hash(buffer, offset, length) % this.fTableSize;
        Entry entry = this.fBuckets[bucket];
        while (true) {
            Entry entry2 = entry;
            if (entry2 != null) {
                if (length == entry2.characters.length) {
                    for (int i2 = 0; i2 < length; i2++) {
                        if (buffer[offset + i2] != entry2.characters[i2]) {
                            break;
                        }
                    }
                    return entry2.symbol;
                }
                collisionCount++;
                entry = entry2.next;
            } else {
                return addSymbol0(buffer, offset, length, bucket, collisionCount);
            }
        }
    }

    private String addSymbol0(char[] buffer, int offset, int length, int bucket, int collisionCount) {
        if (this.fCount >= this.fThreshold) {
            rehash();
            bucket = hash(buffer, offset, length) % this.fTableSize;
        } else if (collisionCount >= this.fCollisionThreshold) {
            rebalance();
            bucket = hash(buffer, offset, length) % this.fTableSize;
        }
        Entry entry = new Entry(buffer, offset, length, this.fBuckets[bucket]);
        this.fBuckets[bucket] = entry;
        this.fCount++;
        return entry.symbol;
    }

    public int hash(String symbol) {
        if (this.fHashMultipliers == null) {
            return symbol.hashCode() & Integer.MAX_VALUE;
        }
        return hash0(symbol);
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

    public int hash(char[] buffer, int offset, int length) {
        if (this.fHashMultipliers == null) {
            int code = 0;
            for (int i2 = 0; i2 < length; i2++) {
                code = (code * 31) + buffer[offset + i2];
            }
            return code & Integer.MAX_VALUE;
        }
        return hash0(buffer, offset, length);
    }

    private int hash0(char[] buffer, int offset, int length) {
        int code = 0;
        int[] multipliers = this.fHashMultipliers;
        for (int i2 = 0; i2 < length; i2++) {
            code = (code * multipliers[i2 & 31]) + buffer[offset + i2];
        }
        return code & Integer.MAX_VALUE;
    }

    protected void rehash() {
        rehashCommon((this.fBuckets.length * 2) + 1);
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
        this.fThreshold = (int) (newCapacity * this.fLoadFactor);
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
                    int index = hash(e2.symbol) % newCapacity;
                    e2.next = newTable[index];
                    newTable[index] = e2;
                }
            } else {
                return;
            }
        }
    }

    public boolean containsSymbol(String symbol) {
        int bucket = hash(symbol) % this.fTableSize;
        int length = symbol.length();
        Entry entry = this.fBuckets[bucket];
        while (true) {
            Entry entry2 = entry;
            if (entry2 != null) {
                if (length == entry2.characters.length) {
                    for (int i2 = 0; i2 < length; i2++) {
                        if (symbol.charAt(i2) != entry2.characters[i2]) {
                            break;
                        }
                    }
                    return true;
                }
                entry = entry2.next;
            } else {
                return false;
            }
        }
    }

    public boolean containsSymbol(char[] buffer, int offset, int length) {
        int bucket = hash(buffer, offset, length) % this.fTableSize;
        Entry entry = this.fBuckets[bucket];
        while (true) {
            Entry entry2 = entry;
            if (entry2 != null) {
                if (length == entry2.characters.length) {
                    for (int i2 = 0; i2 < length; i2++) {
                        if (buffer[offset + i2] != entry2.characters[i2]) {
                            break;
                        }
                    }
                    return true;
                }
                entry = entry2.next;
            } else {
                return false;
            }
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/SymbolTable$Entry.class */
    protected static final class Entry {
        public final String symbol;
        public final char[] characters;
        public Entry next;

        public Entry(String symbol, Entry next) {
            this.symbol = symbol.intern();
            this.characters = new char[symbol.length()];
            symbol.getChars(0, this.characters.length, this.characters, 0);
            this.next = next;
        }

        public Entry(char[] ch, int offset, int length, Entry next) {
            this.characters = new char[length];
            System.arraycopy(ch, offset, this.characters, 0, length);
            this.symbol = new String(this.characters).intern();
            this.next = next;
        }
    }
}
