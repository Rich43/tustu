package com.sun.org.apache.xerces.internal.impl.xs.util;

import com.sun.org.apache.xerces.internal.xs.ShortList;
import com.sun.org.apache.xerces.internal.xs.XSException;
import java.util.AbstractList;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/util/ShortListImpl.class */
public final class ShortListImpl extends AbstractList implements ShortList {
    public static final ShortListImpl EMPTY_LIST = new ShortListImpl(new short[0], 0);
    private final short[] fArray;
    private final int fLength;

    public ShortListImpl(short[] array, int length) {
        this.fArray = array;
        this.fLength = length;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.ShortList
    public int getLength() {
        return this.fLength;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.ShortList
    public boolean contains(short item) {
        for (int i2 = 0; i2 < this.fLength; i2++) {
            if (this.fArray[i2] == item) {
                return true;
            }
        }
        return false;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.ShortList
    public short item(int index) throws XSException {
        if (index < 0 || index >= this.fLength) {
            throw new XSException((short) 2, null);
        }
        return this.fArray[index];
    }

    @Override // java.util.AbstractList, java.util.Collection, java.util.List
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ShortList)) {
            return false;
        }
        ShortList rhs = (ShortList) obj;
        if (this.fLength != rhs.getLength()) {
            return false;
        }
        for (int i2 = 0; i2 < this.fLength; i2++) {
            if (this.fArray[i2] != rhs.item(i2)) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.AbstractList, java.util.List
    public Object get(int index) {
        if (index >= 0 && index < this.fLength) {
            return new Short(this.fArray[index]);
        }
        throw new IndexOutOfBoundsException("Index: " + index);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return getLength();
    }
}
