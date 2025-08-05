package com.sun.xml.internal.fastinfoset.util;

import com.sun.xml.internal.fastinfoset.CommonResourceBundle;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/util/StringArray.class */
public class StringArray extends ValueArray {
    public String[] _array;
    private StringArray _readOnlyArray;
    private boolean _clear;

    public StringArray(int initialCapacity, int maximumCapacity, boolean clear) {
        this._array = new String[initialCapacity];
        this._maximumCapacity = maximumCapacity;
        this._clear = clear;
    }

    public StringArray() {
        this(10, Integer.MAX_VALUE, false);
    }

    @Override // com.sun.xml.internal.fastinfoset.util.ValueArray
    public final void clear() {
        if (this._clear) {
            for (int i2 = this._readOnlyArraySize; i2 < this._size; i2++) {
                this._array[i2] = null;
            }
        }
        this._size = this._readOnlyArraySize;
    }

    public final String[] getArray() {
        if (this._array == null) {
            return null;
        }
        String[] clonedArray = new String[this._array.length];
        System.arraycopy(this._array, 0, clonedArray, 0, this._array.length);
        return clonedArray;
    }

    @Override // com.sun.xml.internal.fastinfoset.util.ValueArray
    public final void setReadOnlyArray(ValueArray readOnlyArray, boolean clear) {
        if (!(readOnlyArray instanceof StringArray)) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.illegalClass", new Object[]{readOnlyArray}));
        }
        setReadOnlyArray((StringArray) readOnlyArray, clear);
    }

    public final void setReadOnlyArray(StringArray readOnlyArray, boolean clear) {
        if (readOnlyArray != null) {
            this._readOnlyArray = readOnlyArray;
            this._readOnlyArraySize = readOnlyArray.getSize();
            if (clear) {
                clear();
            }
            this._array = getCompleteArray();
            this._size = this._readOnlyArraySize;
        }
    }

    public final String[] getCompleteArray() {
        if (this._readOnlyArray == null) {
            return getArray();
        }
        String[] ra = this._readOnlyArray.getCompleteArray();
        String[] a2 = new String[this._readOnlyArraySize + this._array.length];
        System.arraycopy(ra, 0, a2, 0, this._readOnlyArraySize);
        return a2;
    }

    public final String get(int i2) {
        return this._array[i2];
    }

    public final int add(String s2) {
        if (this._size == this._array.length) {
            resize();
        }
        String[] strArr = this._array;
        int i2 = this._size;
        this._size = i2 + 1;
        strArr[i2] = s2;
        return this._size;
    }

    protected final void resize() {
        if (this._size == this._maximumCapacity) {
            throw new ValueArrayResourceException(CommonResourceBundle.getInstance().getString("message.arrayMaxCapacity"));
        }
        int newSize = ((this._size * 3) / 2) + 1;
        if (newSize > this._maximumCapacity) {
            newSize = this._maximumCapacity;
        }
        String[] newArray = new String[newSize];
        System.arraycopy(this._array, 0, newArray, 0, this._size);
        this._array = newArray;
    }
}
