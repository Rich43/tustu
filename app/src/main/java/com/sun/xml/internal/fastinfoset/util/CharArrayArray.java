package com.sun.xml.internal.fastinfoset.util;

import com.sun.xml.internal.fastinfoset.CommonResourceBundle;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/util/CharArrayArray.class */
public class CharArrayArray extends ValueArray {
    private CharArray[] _array;
    private CharArrayArray _readOnlyArray;

    public CharArrayArray(int initialCapacity, int maximumCapacity) {
        this._array = new CharArray[initialCapacity];
        this._maximumCapacity = maximumCapacity;
    }

    public CharArrayArray() {
        this(10, Integer.MAX_VALUE);
    }

    @Override // com.sun.xml.internal.fastinfoset.util.ValueArray
    public final void clear() {
        for (int i2 = 0; i2 < this._size; i2++) {
            this._array[i2] = null;
        }
        this._size = 0;
    }

    public final CharArray[] getArray() {
        if (this._array == null) {
            return null;
        }
        CharArray[] clonedArray = new CharArray[this._array.length];
        System.arraycopy(this._array, 0, clonedArray, 0, this._array.length);
        return clonedArray;
    }

    @Override // com.sun.xml.internal.fastinfoset.util.ValueArray
    public final void setReadOnlyArray(ValueArray readOnlyArray, boolean clear) {
        if (!(readOnlyArray instanceof CharArrayArray)) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.illegalClass", new Object[]{readOnlyArray}));
        }
        setReadOnlyArray((CharArrayArray) readOnlyArray, clear);
    }

    public final void setReadOnlyArray(CharArrayArray readOnlyArray, boolean clear) {
        if (readOnlyArray != null) {
            this._readOnlyArray = readOnlyArray;
            this._readOnlyArraySize = readOnlyArray.getSize();
            if (clear) {
                clear();
            }
        }
    }

    public final CharArray get(int i2) {
        if (this._readOnlyArray == null) {
            return this._array[i2];
        }
        if (i2 < this._readOnlyArraySize) {
            return this._readOnlyArray.get(i2);
        }
        return this._array[i2 - this._readOnlyArraySize];
    }

    public final void add(CharArray s2) {
        if (this._size == this._array.length) {
            resize();
        }
        CharArray[] charArrayArr = this._array;
        int i2 = this._size;
        this._size = i2 + 1;
        charArrayArr[i2] = s2;
    }

    protected final void resize() {
        if (this._size == this._maximumCapacity) {
            throw new ValueArrayResourceException(CommonResourceBundle.getInstance().getString("message.arrayMaxCapacity"));
        }
        int newSize = ((this._size * 3) / 2) + 1;
        if (newSize > this._maximumCapacity) {
            newSize = this._maximumCapacity;
        }
        CharArray[] newArray = new CharArray[newSize];
        System.arraycopy(this._array, 0, newArray, 0, this._size);
        this._array = newArray;
    }
}
