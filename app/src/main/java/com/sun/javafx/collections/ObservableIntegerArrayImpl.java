package com.sun.javafx.collections;

import java.util.Arrays;
import javafx.collections.ObservableArrayBase;
import javafx.collections.ObservableIntegerArray;
import javafx.fxml.FXMLLoader;

/* loaded from: jfxrt.jar:com/sun/javafx/collections/ObservableIntegerArrayImpl.class */
public class ObservableIntegerArrayImpl extends ObservableArrayBase<ObservableIntegerArray> implements ObservableIntegerArray {
    private static final int[] INITIAL = new int[0];
    private int[] array = INITIAL;
    private int size = 0;
    private static final int MAX_ARRAY_SIZE = 2147483639;

    public ObservableIntegerArrayImpl() {
    }

    public ObservableIntegerArrayImpl(int... elements) {
        setAll(elements);
    }

    public ObservableIntegerArrayImpl(ObservableIntegerArray src) {
        setAll(src);
    }

    @Override // javafx.collections.ObservableArray
    public void clear() {
        resize(0);
    }

    @Override // javafx.collections.ObservableArray
    public int size() {
        return this.size;
    }

    private void addAllInternal(ObservableIntegerArray src, int srcIndex, int length) {
        growCapacity(length);
        src.copyTo(srcIndex, this.array, this.size, length);
        this.size += length;
        fireChange(length != 0, this.size - length, this.size);
    }

    private void addAllInternal(int[] src, int srcIndex, int length) {
        growCapacity(length);
        System.arraycopy(src, srcIndex, this.array, this.size, length);
        this.size += length;
        fireChange(length != 0, this.size - length, this.size);
    }

    @Override // javafx.collections.ObservableIntegerArray
    public void addAll(ObservableIntegerArray src) {
        addAllInternal(src, 0, src.size());
    }

    @Override // javafx.collections.ObservableIntegerArray
    public void addAll(int... elements) {
        addAllInternal(elements, 0, elements.length);
    }

    @Override // javafx.collections.ObservableIntegerArray
    public void addAll(ObservableIntegerArray src, int srcIndex, int length) {
        rangeCheck(src, srcIndex, length);
        addAllInternal(src, srcIndex, length);
    }

    @Override // javafx.collections.ObservableIntegerArray
    public void addAll(int[] src, int srcIndex, int length) {
        rangeCheck(src, srcIndex, length);
        addAllInternal(src, srcIndex, length);
    }

    private void setAllInternal(ObservableIntegerArray src, int srcIndex, int length) {
        boolean sizeChanged = size() != length;
        if (src == this) {
            if (srcIndex == 0) {
                resize(length);
                return;
            }
            System.arraycopy(this.array, srcIndex, this.array, 0, length);
            this.size = length;
            fireChange(sizeChanged, 0, this.size);
            return;
        }
        this.size = 0;
        ensureCapacity(length);
        src.copyTo(srcIndex, this.array, 0, length);
        this.size = length;
        fireChange(sizeChanged, 0, this.size);
    }

    private void setAllInternal(int[] src, int srcIndex, int length) {
        boolean sizeChanged = size() != length;
        this.size = 0;
        ensureCapacity(length);
        System.arraycopy(src, srcIndex, this.array, 0, length);
        this.size = length;
        fireChange(sizeChanged, 0, this.size);
    }

    @Override // javafx.collections.ObservableIntegerArray
    public void setAll(ObservableIntegerArray src) {
        setAllInternal(src, 0, src.size());
    }

    @Override // javafx.collections.ObservableIntegerArray
    public void setAll(ObservableIntegerArray src, int srcIndex, int length) {
        rangeCheck(src, srcIndex, length);
        setAllInternal(src, srcIndex, length);
    }

    @Override // javafx.collections.ObservableIntegerArray
    public void setAll(int[] src, int srcIndex, int length) {
        rangeCheck(src, srcIndex, length);
        setAllInternal(src, srcIndex, length);
    }

    @Override // javafx.collections.ObservableIntegerArray
    public void setAll(int[] src) {
        setAllInternal(src, 0, src.length);
    }

    @Override // javafx.collections.ObservableIntegerArray
    public void set(int destIndex, int[] src, int srcIndex, int length) {
        rangeCheck(destIndex + length);
        System.arraycopy(src, srcIndex, this.array, destIndex, length);
        fireChange(false, destIndex, destIndex + length);
    }

    @Override // javafx.collections.ObservableIntegerArray
    public void set(int destIndex, ObservableIntegerArray src, int srcIndex, int length) {
        rangeCheck(destIndex + length);
        src.copyTo(srcIndex, this.array, destIndex, length);
        fireChange(false, destIndex, destIndex + length);
    }

    @Override // javafx.collections.ObservableIntegerArray
    public int[] toArray(int[] dest) {
        if (dest == null || size() > dest.length) {
            dest = new int[size()];
        }
        System.arraycopy(this.array, 0, dest, 0, size());
        return dest;
    }

    @Override // javafx.collections.ObservableIntegerArray
    public int get(int index) {
        rangeCheck(index + 1);
        return this.array[index];
    }

    @Override // javafx.collections.ObservableIntegerArray
    public void set(int index, int value) {
        rangeCheck(index + 1);
        this.array[index] = value;
        fireChange(false, index, index + 1);
    }

    @Override // javafx.collections.ObservableIntegerArray
    public int[] toArray(int index, int[] dest, int length) {
        rangeCheck(index + length);
        if (dest == null || length > dest.length) {
            dest = new int[length];
        }
        System.arraycopy(this.array, index, dest, 0, length);
        return dest;
    }

    @Override // javafx.collections.ObservableIntegerArray
    public void copyTo(int srcIndex, int[] dest, int destIndex, int length) {
        rangeCheck(srcIndex + length);
        System.arraycopy(this.array, srcIndex, dest, destIndex, length);
    }

    @Override // javafx.collections.ObservableIntegerArray
    public void copyTo(int srcIndex, ObservableIntegerArray dest, int destIndex, int length) {
        rangeCheck(srcIndex + length);
        dest.set(destIndex, this.array, srcIndex, length);
    }

    @Override // javafx.collections.ObservableArray
    public void resize(int newSize) {
        if (newSize < 0) {
            throw new NegativeArraySizeException("Can't resize to negative value: " + newSize);
        }
        ensureCapacity(newSize);
        int minSize = Math.min(this.size, newSize);
        boolean sizeChanged = this.size != newSize;
        this.size = newSize;
        Arrays.fill(this.array, minSize, this.size, 0);
        fireChange(sizeChanged, minSize, newSize);
    }

    private void growCapacity(int length) {
        int minCapacity = this.size + length;
        int oldCapacity = this.array.length;
        if (minCapacity <= this.array.length) {
            if (length > 0 && minCapacity < 0) {
                throw new OutOfMemoryError();
            }
        } else {
            int newCapacity = oldCapacity + (oldCapacity >> 1);
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            if (newCapacity > MAX_ARRAY_SIZE) {
                newCapacity = hugeCapacity(minCapacity);
            }
            ensureCapacity(newCapacity);
        }
    }

    @Override // javafx.collections.ObservableArray
    public void ensureCapacity(int capacity) {
        if (this.array.length < capacity) {
            this.array = Arrays.copyOf(this.array, capacity);
        }
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) {
            throw new OutOfMemoryError();
        }
        if (minCapacity > MAX_ARRAY_SIZE) {
            return Integer.MAX_VALUE;
        }
        return MAX_ARRAY_SIZE;
    }

    @Override // javafx.collections.ObservableArray
    public void trimToSize() {
        if (this.array.length != this.size) {
            int[] newArray = new int[this.size];
            System.arraycopy(this.array, 0, newArray, 0, this.size);
            this.array = newArray;
        }
    }

    private void rangeCheck(int size) {
        if (size > this.size) {
            throw new ArrayIndexOutOfBoundsException(this.size);
        }
    }

    private void rangeCheck(ObservableIntegerArray src, int srcIndex, int length) {
        if (src == null) {
            throw new NullPointerException();
        }
        if (srcIndex < 0 || srcIndex + length > src.size()) {
            throw new ArrayIndexOutOfBoundsException(src.size());
        }
        if (length < 0) {
            throw new ArrayIndexOutOfBoundsException(-1);
        }
    }

    private void rangeCheck(int[] src, int srcIndex, int length) {
        if (src == null) {
            throw new NullPointerException();
        }
        if (srcIndex < 0 || srcIndex + length > src.length) {
            throw new ArrayIndexOutOfBoundsException(src.length);
        }
        if (length < 0) {
            throw new ArrayIndexOutOfBoundsException(-1);
        }
    }

    public String toString() {
        if (this.array == null) {
            return FXMLLoader.NULL_KEYWORD;
        }
        int iMax = size() - 1;
        if (iMax == -1) {
            return "[]";
        }
        StringBuilder b2 = new StringBuilder();
        b2.append('[');
        int i2 = 0;
        while (true) {
            b2.append(this.array[i2]);
            if (i2 == iMax) {
                return b2.append(']').toString();
            }
            b2.append(", ");
            i2++;
        }
    }
}
