package com.sun.xml.internal.fastinfoset.util;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/util/ValueArray.class */
public abstract class ValueArray {
    public static final int DEFAULT_CAPACITY = 10;
    public static final int MAXIMUM_CAPACITY = Integer.MAX_VALUE;
    protected int _size;
    protected int _readOnlyArraySize;
    protected int _maximumCapacity;

    public abstract void setReadOnlyArray(ValueArray valueArray, boolean z2);

    public abstract void clear();

    public int getSize() {
        return this._size;
    }

    public int getMaximumCapacity() {
        return this._maximumCapacity;
    }

    public void setMaximumCapacity(int maximumCapacity) {
        this._maximumCapacity = maximumCapacity;
    }
}
