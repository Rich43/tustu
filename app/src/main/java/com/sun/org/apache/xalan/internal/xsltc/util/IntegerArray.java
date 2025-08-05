package com.sun.org.apache.xalan.internal.xsltc.util;

import java.io.PrintStream;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/util/IntegerArray.class */
public final class IntegerArray {
    private static final int InitialSize = 32;
    private int[] _array;
    private int _size;
    private int _free;

    public IntegerArray() {
        this(32);
    }

    public IntegerArray(int size) {
        this._free = 0;
        this._size = size;
        this._array = new int[size];
    }

    public IntegerArray(int[] array) {
        this(array.length);
        int[] iArr = this._array;
        int i2 = this._size;
        this._free = i2;
        System.arraycopy(array, 0, iArr, 0, i2);
    }

    public void clear() {
        this._free = 0;
    }

    public Object clone() {
        IntegerArray clone = new IntegerArray(this._free > 0 ? this._free : 1);
        System.arraycopy(this._array, 0, clone._array, 0, this._free);
        clone._free = this._free;
        return clone;
    }

    public int[] toIntArray() {
        int[] result = new int[cardinality()];
        System.arraycopy(this._array, 0, result, 0, cardinality());
        return result;
    }

    public final int at(int index) {
        return this._array[index];
    }

    public final void set(int index, int value) {
        this._array[index] = value;
    }

    public int indexOf(int n2) {
        for (int i2 = 0; i2 < this._free; i2++) {
            if (n2 == this._array[i2]) {
                return i2;
            }
        }
        return -1;
    }

    public final void add(int value) {
        if (this._free == this._size) {
            growArray(this._size * 2);
        }
        int[] iArr = this._array;
        int i2 = this._free;
        this._free = i2 + 1;
        iArr[i2] = value;
    }

    public void addNew(int value) {
        for (int i2 = 0; i2 < this._free; i2++) {
            if (this._array[i2] == value) {
                return;
            }
        }
        add(value);
    }

    public void reverse() {
        int left = 0;
        int right = this._free - 1;
        while (left < right) {
            int temp = this._array[left];
            int i2 = left;
            left++;
            this._array[i2] = this._array[right];
            int i3 = right;
            right--;
            this._array[i3] = temp;
        }
    }

    public void merge(IntegerArray other) {
        int newSize = this._free + other._free;
        int[] newArray = new int[newSize];
        int i2 = 0;
        int j2 = 0;
        int k2 = 0;
        while (i2 < this._free && j2 < other._free) {
            int x2 = this._array[i2];
            int y2 = other._array[j2];
            if (x2 < y2) {
                newArray[k2] = x2;
                i2++;
            } else if (x2 > y2) {
                newArray[k2] = y2;
                j2++;
            } else {
                newArray[k2] = x2;
                i2++;
                j2++;
            }
            k2++;
        }
        if (i2 >= this._free) {
            while (j2 < other._free) {
                int i3 = k2;
                k2++;
                int i4 = j2;
                j2++;
                newArray[i3] = other._array[i4];
            }
        } else {
            while (i2 < this._free) {
                int i5 = k2;
                k2++;
                int i6 = i2;
                i2++;
                newArray[i5] = this._array[i6];
            }
        }
        this._array = newArray;
        this._size = newSize;
        this._free = newSize;
    }

    public void sort() {
        quicksort(this._array, 0, this._free - 1);
    }

    private static void quicksort(int[] array, int p2, int r2) {
        if (p2 < r2) {
            int q2 = partition(array, p2, r2);
            quicksort(array, p2, q2);
            quicksort(array, q2 + 1, r2);
        }
    }

    private static int partition(int[] array, int p2, int r2) {
        int x2 = array[(p2 + r2) >>> 1];
        int i2 = p2 - 1;
        int j2 = r2 + 1;
        while (true) {
            j2--;
            if (x2 >= array[j2]) {
                do {
                    i2++;
                } while (x2 > array[i2]);
                if (i2 < j2) {
                    int temp = array[i2];
                    array[i2] = array[j2];
                    array[j2] = temp;
                } else {
                    return j2;
                }
            }
        }
    }

    private void growArray(int size) {
        this._size = size;
        int[] newArray = new int[size];
        System.arraycopy(this._array, 0, newArray, 0, this._free);
        this._array = newArray;
    }

    public int popLast() {
        int[] iArr = this._array;
        int i2 = this._free - 1;
        this._free = i2;
        return iArr[i2];
    }

    public int last() {
        return this._array[this._free - 1];
    }

    public void setLast(int n2) {
        this._array[this._free - 1] = n2;
    }

    public void pop() {
        this._free--;
    }

    public void pop(int n2) {
        this._free -= n2;
    }

    public final int cardinality() {
        return this._free;
    }

    public void print(PrintStream out) {
        if (this._free > 0) {
            for (int i2 = 0; i2 < this._free - 1; i2++) {
                out.print(this._array[i2]);
                out.print(' ');
            }
            out.println(this._array[this._free - 1]);
            return;
        }
        out.println("IntegerArray: empty");
    }
}
