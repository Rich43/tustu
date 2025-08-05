package com.sun.xml.internal.fastinfoset.util;

import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
import com.sun.xml.internal.fastinfoset.QualifiedName;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/util/QualifiedNameArray.class */
public class QualifiedNameArray extends ValueArray {
    public QualifiedName[] _array;
    private QualifiedNameArray _readOnlyArray;

    public QualifiedNameArray(int initialCapacity, int maximumCapacity) {
        this._array = new QualifiedName[initialCapacity];
        this._maximumCapacity = maximumCapacity;
    }

    public QualifiedNameArray() {
        this(10, Integer.MAX_VALUE);
    }

    @Override // com.sun.xml.internal.fastinfoset.util.ValueArray
    public final void clear() {
        this._size = this._readOnlyArraySize;
    }

    public final QualifiedName[] getArray() {
        if (this._array == null) {
            return null;
        }
        QualifiedName[] clonedArray = new QualifiedName[this._array.length];
        System.arraycopy(this._array, 0, clonedArray, 0, this._array.length);
        return clonedArray;
    }

    @Override // com.sun.xml.internal.fastinfoset.util.ValueArray
    public final void setReadOnlyArray(ValueArray readOnlyArray, boolean clear) {
        if (!(readOnlyArray instanceof QualifiedNameArray)) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.illegalClass", new Object[]{readOnlyArray}));
        }
        setReadOnlyArray((QualifiedNameArray) readOnlyArray, clear);
    }

    public final void setReadOnlyArray(QualifiedNameArray readOnlyArray, boolean clear) {
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

    public final QualifiedName[] getCompleteArray() {
        if (this._readOnlyArray == null) {
            return getArray();
        }
        QualifiedName[] ra = this._readOnlyArray.getCompleteArray();
        QualifiedName[] a2 = new QualifiedName[this._readOnlyArraySize + this._array.length];
        System.arraycopy(ra, 0, a2, 0, this._readOnlyArraySize);
        return a2;
    }

    public final QualifiedName getNext() {
        if (this._size == this._array.length) {
            return null;
        }
        return this._array[this._size];
    }

    public final void add(QualifiedName s2) {
        if (this._size == this._array.length) {
            resize();
        }
        QualifiedName[] qualifiedNameArr = this._array;
        int i2 = this._size;
        this._size = i2 + 1;
        qualifiedNameArr[i2] = s2;
    }

    protected final void resize() {
        if (this._size == this._maximumCapacity) {
            throw new ValueArrayResourceException(CommonResourceBundle.getInstance().getString("message.arrayMaxCapacity"));
        }
        int newSize = ((this._size * 3) / 2) + 1;
        if (newSize > this._maximumCapacity) {
            newSize = this._maximumCapacity;
        }
        QualifiedName[] newArray = new QualifiedName[newSize];
        System.arraycopy(this._array, 0, newArray, 0, this._size);
        this._array = newArray;
    }
}
