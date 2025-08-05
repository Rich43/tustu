package org.apache.commons.math3.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/OpenIntToFieldHashMap.class */
public class OpenIntToFieldHashMap<T extends FieldElement<T>> implements Serializable {
    protected static final byte FREE = 0;
    protected static final byte FULL = 1;
    protected static final byte REMOVED = 2;
    private static final long serialVersionUID = -9179080286849120720L;
    private static final float LOAD_FACTOR = 0.5f;
    private static final int DEFAULT_EXPECTED_SIZE = 16;
    private static final int RESIZE_MULTIPLIER = 2;
    private static final int PERTURB_SHIFT = 5;
    private final Field<T> field;
    private int[] keys;
    private T[] values;
    private byte[] states;
    private final T missingEntries;
    private int size;
    private int mask;
    private transient int count;

    public OpenIntToFieldHashMap(Field<T> field) {
        this(field, 16, field.getZero());
    }

    public OpenIntToFieldHashMap(Field<T> field, T missingEntries) {
        this(field, 16, missingEntries);
    }

    public OpenIntToFieldHashMap(Field<T> field, int expectedSize) {
        this(field, expectedSize, field.getZero());
    }

    public OpenIntToFieldHashMap(Field<T> field, int i2, T t2) {
        this.field = field;
        int iComputeCapacity = computeCapacity(i2);
        this.keys = new int[iComputeCapacity];
        this.values = (T[]) buildArray(iComputeCapacity);
        this.states = new byte[iComputeCapacity];
        this.missingEntries = t2;
        this.mask = iComputeCapacity - 1;
    }

    public OpenIntToFieldHashMap(OpenIntToFieldHashMap<T> openIntToFieldHashMap) {
        this.field = openIntToFieldHashMap.field;
        int length = openIntToFieldHashMap.keys.length;
        this.keys = new int[length];
        System.arraycopy(openIntToFieldHashMap.keys, 0, this.keys, 0, length);
        this.values = (T[]) buildArray(length);
        System.arraycopy(openIntToFieldHashMap.values, 0, this.values, 0, length);
        this.states = new byte[length];
        System.arraycopy(openIntToFieldHashMap.states, 0, this.states, 0, length);
        this.missingEntries = openIntToFieldHashMap.missingEntries;
        this.size = openIntToFieldHashMap.size;
        this.mask = openIntToFieldHashMap.mask;
        this.count = openIntToFieldHashMap.count;
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

    public T get(int key) {
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

    public OpenIntToFieldHashMap<T>.Iterator iterator() {
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

    public T remove(int i2) {
        int iHashOf = hashOf(i2);
        int i3 = iHashOf & this.mask;
        if (containsKey(i2, i3)) {
            return (T) doRemove(i3);
        }
        if (this.states[i3] == 0) {
            return this.missingEntries;
        }
        int iProbe = i3;
        int iPerturb = perturb(iHashOf);
        while (true) {
            int i4 = iPerturb;
            if (this.states[i3] != 0) {
                iProbe = probe(i4, iProbe);
                i3 = iProbe & this.mask;
                if (!containsKey(i2, i3)) {
                    iPerturb = i4 >> 5;
                } else {
                    return (T) doRemove(i3);
                }
            } else {
                return this.missingEntries;
            }
        }
    }

    private boolean containsKey(int key, int index) {
        return (key != 0 || this.states[index] == 1) && this.keys[index] == key;
    }

    private T doRemove(int index) {
        this.keys[index] = 0;
        this.states[index] = 2;
        T previous = this.values[index];
        this.values[index] = this.missingEntries;
        this.size--;
        this.count++;
        return previous;
    }

    public T put(int key, T value) {
        int index = findInsertionIndex(key);
        T previous = this.missingEntries;
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
        int length = this.states.length;
        int[] iArr = this.keys;
        T[] tArr = this.values;
        byte[] bArr = this.states;
        int i2 = 2 * length;
        int[] iArr2 = new int[i2];
        T[] tArr2 = (T[]) buildArray(i2);
        byte[] bArr2 = new byte[i2];
        int i3 = i2 - 1;
        for (int i4 = 0; i4 < length; i4++) {
            if (bArr[i4] == 1) {
                int i5 = iArr[i4];
                int iFindInsertionIndex = findInsertionIndex(iArr2, bArr2, i5, i3);
                iArr2[iFindInsertionIndex] = i5;
                tArr2[iFindInsertionIndex] = tArr[i4];
                bArr2[iFindInsertionIndex] = 1;
            }
        }
        this.mask = i3;
        this.keys = iArr2;
        this.values = tArr2;
        this.states = bArr2;
    }

    private boolean shouldGrowTable() {
        return ((float) this.size) > ((float) (this.mask + 1)) * 0.5f;
    }

    private static int hashOf(int key) {
        int h2 = key ^ ((key >>> 20) ^ (key >>> 12));
        return (h2 ^ (h2 >>> 7)) ^ (h2 >>> 4);
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/OpenIntToFieldHashMap$Iterator.class */
    public class Iterator {
        private final int referenceCount;
        private int current;
        private int next;

        private Iterator() throws ConcurrentModificationException {
            this.referenceCount = OpenIntToFieldHashMap.this.count;
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
            if (this.referenceCount != OpenIntToFieldHashMap.this.count) {
                throw new ConcurrentModificationException();
            }
            if (this.current >= 0) {
                return OpenIntToFieldHashMap.this.keys[this.current];
            }
            throw new NoSuchElementException();
        }

        public T value() throws NoSuchElementException, ConcurrentModificationException {
            if (this.referenceCount != OpenIntToFieldHashMap.this.count) {
                throw new ConcurrentModificationException();
            }
            if (this.current >= 0) {
                return (T) OpenIntToFieldHashMap.this.values[this.current];
            }
            throw new NoSuchElementException();
        }

        public void advance() throws NoSuchElementException, ConcurrentModificationException {
            byte[] bArr;
            int i2;
            if (this.referenceCount != OpenIntToFieldHashMap.this.count) {
                throw new ConcurrentModificationException();
            }
            this.current = this.next;
            do {
                try {
                    bArr = OpenIntToFieldHashMap.this.states;
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

    private T[] buildArray(int i2) {
        return (T[]) ((FieldElement[]) Array.newInstance(this.field.getRuntimeClass(), i2));
    }
}
