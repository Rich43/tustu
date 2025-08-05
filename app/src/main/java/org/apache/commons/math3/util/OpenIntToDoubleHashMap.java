package org.apache.commons.math3.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/OpenIntToDoubleHashMap.class */
public class OpenIntToDoubleHashMap implements Serializable {
    protected static final byte FREE = 0;
    protected static final byte FULL = 1;
    protected static final byte REMOVED = 2;
    private static final long serialVersionUID = -3646337053166149105L;
    private static final float LOAD_FACTOR = 0.5f;
    private static final int DEFAULT_EXPECTED_SIZE = 16;
    private static final int RESIZE_MULTIPLIER = 2;
    private static final int PERTURB_SHIFT = 5;
    private int[] keys;
    private double[] values;
    private byte[] states;
    private final double missingEntries;
    private int size;
    private int mask;
    private transient int count;

    public OpenIntToDoubleHashMap() {
        this(16, Double.NaN);
    }

    public OpenIntToDoubleHashMap(double missingEntries) {
        this(16, missingEntries);
    }

    public OpenIntToDoubleHashMap(int expectedSize) {
        this(expectedSize, Double.NaN);
    }

    public OpenIntToDoubleHashMap(int expectedSize, double missingEntries) {
        int capacity = computeCapacity(expectedSize);
        this.keys = new int[capacity];
        this.values = new double[capacity];
        this.states = new byte[capacity];
        this.missingEntries = missingEntries;
        this.mask = capacity - 1;
    }

    public OpenIntToDoubleHashMap(OpenIntToDoubleHashMap source) {
        int length = source.keys.length;
        this.keys = new int[length];
        System.arraycopy(source.keys, 0, this.keys, 0, length);
        this.values = new double[length];
        System.arraycopy(source.values, 0, this.values, 0, length);
        this.states = new byte[length];
        System.arraycopy(source.states, 0, this.states, 0, length);
        this.missingEntries = source.missingEntries;
        this.size = source.size;
        this.mask = source.mask;
        this.count = source.count;
    }

    private static int computeCapacity(int expectedSize) {
        if (expectedSize == 0) {
            return 1;
        }
        int capacity = (int) FastMath.ceil(expectedSize / 0.5f);
        int powerOfTwo = Integer.highestOneBit(capacity);
        if (powerOfTwo == capacity) {
            return capacity;
        }
        return nextPowerOfTwo(capacity);
    }

    private static int nextPowerOfTwo(int i2) {
        return Integer.highestOneBit(i2) << 1;
    }

    public double get(int key) {
        int hash = hashOf(key);
        int index = hash & this.mask;
        if (containsKey(key, index)) {
            return this.values[index];
        }
        if (this.states[index] == 0) {
            return this.missingEntries;
        }
        int j2 = index;
        int iPerturb = perturb(hash);
        while (true) {
            int perturb = iPerturb;
            if (this.states[index] != 0) {
                j2 = probe(perturb, j2);
                index = j2 & this.mask;
                if (!containsKey(key, index)) {
                    iPerturb = perturb >> 5;
                } else {
                    return this.values[index];
                }
            } else {
                return this.missingEntries;
            }
        }
    }

    public boolean containsKey(int key) {
        int hash = hashOf(key);
        int index = hash & this.mask;
        if (containsKey(key, index)) {
            return true;
        }
        if (this.states[index] == 0) {
            return false;
        }
        int j2 = index;
        int iPerturb = perturb(hash);
        while (true) {
            int perturb = iPerturb;
            if (this.states[index] != 0) {
                j2 = probe(perturb, j2);
                index = j2 & this.mask;
                if (!containsKey(key, index)) {
                    iPerturb = perturb >> 5;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    public Iterator iterator() {
        return new Iterator();
    }

    private static int perturb(int hash) {
        return hash & Integer.MAX_VALUE;
    }

    private int findInsertionIndex(int key) {
        return findInsertionIndex(this.keys, this.states, key, this.mask);
    }

    private static int findInsertionIndex(int[] keys, byte[] states, int key, int mask) {
        int hash = hashOf(key);
        int index = hash & mask;
        if (states[index] == 0) {
            return index;
        }
        if (states[index] == 1 && keys[index] == key) {
            return changeIndexSign(index);
        }
        int perturb = perturb(hash);
        int j2 = index;
        if (states[index] == 1) {
            do {
                j2 = probe(perturb, j2);
                index = j2 & mask;
                perturb >>= 5;
                if (states[index] != 1) {
                    break;
                }
            } while (keys[index] != key);
        }
        if (states[index] == 0) {
            return index;
        }
        if (states[index] == 1) {
            return changeIndexSign(index);
        }
        int firstRemoved = index;
        while (true) {
            j2 = probe(perturb, j2);
            int index2 = j2 & mask;
            if (states[index2] == 0) {
                return firstRemoved;
            }
            if (states[index2] == 1 && keys[index2] == key) {
                return changeIndexSign(index2);
            }
            perturb >>= 5;
        }
    }

    private static int probe(int perturb, int j2) {
        return (j2 << 2) + j2 + perturb + 1;
    }

    private static int changeIndexSign(int index) {
        return (-index) - 1;
    }

    public int size() {
        return this.size;
    }

    public double remove(int key) {
        int hash = hashOf(key);
        int index = hash & this.mask;
        if (containsKey(key, index)) {
            return doRemove(index);
        }
        if (this.states[index] == 0) {
            return this.missingEntries;
        }
        int j2 = index;
        int iPerturb = perturb(hash);
        while (true) {
            int perturb = iPerturb;
            if (this.states[index] != 0) {
                j2 = probe(perturb, j2);
                index = j2 & this.mask;
                if (!containsKey(key, index)) {
                    iPerturb = perturb >> 5;
                } else {
                    return doRemove(index);
                }
            } else {
                return this.missingEntries;
            }
        }
    }

    private boolean containsKey(int key, int index) {
        return (key != 0 || this.states[index] == 1) && this.keys[index] == key;
    }

    private double doRemove(int index) {
        this.keys[index] = 0;
        this.states[index] = 2;
        double previous = this.values[index];
        this.values[index] = this.missingEntries;
        this.size--;
        this.count++;
        return previous;
    }

    public double put(int key, double value) {
        int index = findInsertionIndex(key);
        double previous = this.missingEntries;
        boolean newMapping = true;
        if (index < 0) {
            index = changeIndexSign(index);
            previous = this.values[index];
            newMapping = false;
        }
        this.keys[index] = key;
        this.states[index] = 1;
        this.values[index] = value;
        if (newMapping) {
            this.size++;
            if (shouldGrowTable()) {
                growTable();
            }
            this.count++;
        }
        return previous;
    }

    private void growTable() {
        int oldLength = this.states.length;
        int[] oldKeys = this.keys;
        double[] oldValues = this.values;
        byte[] oldStates = this.states;
        int newLength = 2 * oldLength;
        int[] newKeys = new int[newLength];
        double[] newValues = new double[newLength];
        byte[] newStates = new byte[newLength];
        int newMask = newLength - 1;
        for (int i2 = 0; i2 < oldLength; i2++) {
            if (oldStates[i2] == 1) {
                int key = oldKeys[i2];
                int index = findInsertionIndex(newKeys, newStates, key, newMask);
                newKeys[index] = key;
                newValues[index] = oldValues[i2];
                newStates[index] = 1;
            }
        }
        this.mask = newMask;
        this.keys = newKeys;
        this.values = newValues;
        this.states = newStates;
    }

    private boolean shouldGrowTable() {
        return ((float) this.size) > ((float) (this.mask + 1)) * 0.5f;
    }

    private static int hashOf(int key) {
        int h2 = key ^ ((key >>> 20) ^ (key >>> 12));
        return (h2 ^ (h2 >>> 7)) ^ (h2 >>> 4);
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/OpenIntToDoubleHashMap$Iterator.class */
    public class Iterator {
        private final int referenceCount;
        private int current;
        private int next;

        private Iterator() throws ConcurrentModificationException {
            this.referenceCount = OpenIntToDoubleHashMap.this.count;
            this.next = -1;
            try {
                advance();
            } catch (NoSuchElementException e2) {
            }
        }

        public boolean hasNext() {
            return this.next >= 0;
        }

        public int key() throws NoSuchElementException, ConcurrentModificationException {
            if (this.referenceCount != OpenIntToDoubleHashMap.this.count) {
                throw new ConcurrentModificationException();
            }
            if (this.current >= 0) {
                return OpenIntToDoubleHashMap.this.keys[this.current];
            }
            throw new NoSuchElementException();
        }

        public double value() throws NoSuchElementException, ConcurrentModificationException {
            if (this.referenceCount != OpenIntToDoubleHashMap.this.count) {
                throw new ConcurrentModificationException();
            }
            if (this.current >= 0) {
                return OpenIntToDoubleHashMap.this.values[this.current];
            }
            throw new NoSuchElementException();
        }

        public void advance() throws NoSuchElementException, ConcurrentModificationException {
            byte[] bArr;
            int i2;
            if (this.referenceCount != OpenIntToDoubleHashMap.this.count) {
                throw new ConcurrentModificationException();
            }
            this.current = this.next;
            do {
                try {
                    bArr = OpenIntToDoubleHashMap.this.states;
                    i2 = this.next + 1;
                    this.next = i2;
                } catch (ArrayIndexOutOfBoundsException e2) {
                    this.next = -2;
                    if (this.current < 0) {
                        throw new NoSuchElementException();
                    }
                    return;
                }
            } while (bArr[i2] != 1);
        }
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.count = 0;
    }
}
