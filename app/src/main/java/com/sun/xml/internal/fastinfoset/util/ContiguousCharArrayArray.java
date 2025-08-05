package com.sun.xml.internal.fastinfoset.util;

import com.sun.xml.internal.fastinfoset.CommonResourceBundle;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/util/ContiguousCharArrayArray.class */
public class ContiguousCharArrayArray extends ValueArray {
    public static final int INITIAL_CHARACTER_SIZE = 512;
    public static final int MAXIMUM_CHARACTER_SIZE = Integer.MAX_VALUE;
    protected int _maximumCharacterSize;
    public int[] _offset;
    public int[] _length;
    public char[] _array;
    public int _arrayIndex;
    public int _readOnlyArrayIndex;
    private String[] _cachedStrings;
    public int _cachedIndex;
    private ContiguousCharArrayArray _readOnlyArray;

    public ContiguousCharArrayArray(int initialCapacity, int maximumCapacity, int initialCharacterSize, int maximumCharacterSize) {
        this._offset = new int[initialCapacity];
        this._length = new int[initialCapacity];
        this._array = new char[initialCharacterSize];
        this._maximumCapacity = maximumCapacity;
        this._maximumCharacterSize = maximumCharacterSize;
    }

    public ContiguousCharArrayArray() {
        this(10, Integer.MAX_VALUE, 512, Integer.MAX_VALUE);
    }

    @Override // com.sun.xml.internal.fastinfoset.util.ValueArray
    public final void clear() {
        this._arrayIndex = this._readOnlyArrayIndex;
        this._size = this._readOnlyArraySize;
        if (this._cachedStrings != null) {
            for (int i2 = this._readOnlyArraySize; i2 < this._cachedStrings.length; i2++) {
                this._cachedStrings[i2] = null;
            }
        }
    }

    public final int getArrayIndex() {
        return this._arrayIndex;
    }

    @Override // com.sun.xml.internal.fastinfoset.util.ValueArray
    public final void setReadOnlyArray(ValueArray readOnlyArray, boolean clear) {
        if (!(readOnlyArray instanceof ContiguousCharArrayArray)) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.illegalClass", new Object[]{readOnlyArray}));
        }
        setReadOnlyArray((ContiguousCharArrayArray) readOnlyArray, clear);
    }

    public final void setReadOnlyArray(ContiguousCharArrayArray readOnlyArray, boolean clear) {
        if (readOnlyArray != null) {
            this._readOnlyArray = readOnlyArray;
            this._readOnlyArraySize = readOnlyArray.getSize();
            this._readOnlyArrayIndex = readOnlyArray.getArrayIndex();
            if (clear) {
                clear();
            }
            this._array = getCompleteCharArray();
            this._offset = getCompleteOffsetArray();
            this._length = getCompleteLengthArray();
            this._size = this._readOnlyArraySize;
            this._arrayIndex = this._readOnlyArrayIndex;
        }
    }

    public final char[] getCompleteCharArray() {
        if (this._readOnlyArray == null) {
            if (this._array == null) {
                return null;
            }
            char[] clonedArray = new char[this._array.length];
            System.arraycopy(this._array, 0, clonedArray, 0, this._array.length);
            return clonedArray;
        }
        char[] ra = this._readOnlyArray.getCompleteCharArray();
        char[] a2 = new char[this._readOnlyArrayIndex + this._array.length];
        System.arraycopy(ra, 0, a2, 0, this._readOnlyArrayIndex);
        return a2;
    }

    public final int[] getCompleteOffsetArray() {
        if (this._readOnlyArray == null) {
            if (this._offset == null) {
                return null;
            }
            int[] clonedArray = new int[this._offset.length];
            System.arraycopy(this._offset, 0, clonedArray, 0, this._offset.length);
            return clonedArray;
        }
        int[] ra = this._readOnlyArray.getCompleteOffsetArray();
        int[] a2 = new int[this._readOnlyArraySize + this._offset.length];
        System.arraycopy(ra, 0, a2, 0, this._readOnlyArraySize);
        return a2;
    }

    public final int[] getCompleteLengthArray() {
        if (this._readOnlyArray == null) {
            if (this._length == null) {
                return null;
            }
            int[] clonedArray = new int[this._length.length];
            System.arraycopy(this._length, 0, clonedArray, 0, this._length.length);
            return clonedArray;
        }
        int[] ra = this._readOnlyArray.getCompleteLengthArray();
        int[] a2 = new int[this._readOnlyArraySize + this._length.length];
        System.arraycopy(ra, 0, a2, 0, this._readOnlyArraySize);
        return a2;
    }

    public final String getString(int i2) {
        if (this._cachedStrings != null && i2 < this._cachedStrings.length) {
            String s2 = this._cachedStrings[i2];
            if (s2 != null) {
                return s2;
            }
            String[] strArr = this._cachedStrings;
            String str = new String(this._array, this._offset[i2], this._length[i2]);
            strArr[i2] = str;
            return str;
        }
        String[] newCachedStrings = new String[this._offset.length];
        if (this._cachedStrings != null && i2 >= this._cachedStrings.length) {
            System.arraycopy(this._cachedStrings, 0, newCachedStrings, 0, this._cachedStrings.length);
        }
        this._cachedStrings = newCachedStrings;
        String[] strArr2 = this._cachedStrings;
        String str2 = new String(this._array, this._offset[i2], this._length[i2]);
        strArr2[i2] = str2;
        return str2;
    }

    public final void ensureSize(int l2) {
        if (this._arrayIndex + l2 >= this._array.length) {
            resizeArray(this._arrayIndex + l2);
        }
    }

    public final void add(int l2) {
        if (this._size == this._offset.length) {
            resize();
        }
        this._cachedIndex = this._size;
        this._offset[this._size] = this._arrayIndex;
        int[] iArr = this._length;
        int i2 = this._size;
        this._size = i2 + 1;
        iArr[i2] = l2;
        this._arrayIndex += l2;
    }

    public final int add(char[] c2, int l2) {
        if (this._size == this._offset.length) {
            resize();
        }
        int oldArrayIndex = this._arrayIndex;
        int arrayIndex = oldArrayIndex + l2;
        this._cachedIndex = this._size;
        this._offset[this._size] = oldArrayIndex;
        int[] iArr = this._length;
        int i2 = this._size;
        this._size = i2 + 1;
        iArr[i2] = l2;
        if (arrayIndex >= this._array.length) {
            resizeArray(arrayIndex);
        }
        System.arraycopy(c2, 0, this._array, oldArrayIndex, l2);
        this._arrayIndex = arrayIndex;
        return oldArrayIndex;
    }

    protected final void resize() {
        if (this._size == this._maximumCapacity) {
            throw new ValueArrayResourceException(CommonResourceBundle.getInstance().getString("message.arrayMaxCapacity"));
        }
        int newSize = ((this._size * 3) / 2) + 1;
        if (newSize > this._maximumCapacity) {
            newSize = this._maximumCapacity;
        }
        int[] offset = new int[newSize];
        System.arraycopy(this._offset, 0, offset, 0, this._size);
        this._offset = offset;
        int[] length = new int[newSize];
        System.arraycopy(this._length, 0, length, 0, this._size);
        this._length = length;
    }

    protected final void resizeArray(int requestedSize) {
        if (this._arrayIndex == this._maximumCharacterSize) {
            throw new ValueArrayResourceException(CommonResourceBundle.getInstance().getString("message.maxNumberOfCharacters"));
        }
        int newSize = ((requestedSize * 3) / 2) + 1;
        if (newSize > this._maximumCharacterSize) {
            newSize = this._maximumCharacterSize;
        }
        char[] array = new char[newSize];
        System.arraycopy(this._array, 0, array, 0, this._arrayIndex);
        this._array = array;
    }
}
