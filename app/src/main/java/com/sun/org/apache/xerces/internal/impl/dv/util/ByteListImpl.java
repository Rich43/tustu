package com.sun.org.apache.xerces.internal.impl.dv.util;

import com.sun.org.apache.xerces.internal.xs.XSException;
import com.sun.org.apache.xerces.internal.xs.datatypes.ByteList;
import java.util.AbstractList;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/util/ByteListImpl.class */
public class ByteListImpl extends AbstractList implements ByteList {
    protected final byte[] data;
    protected String canonical;

    public ByteListImpl(byte[] data) {
        this.data = data;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.datatypes.ByteList
    public int getLength() {
        return this.data.length;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.datatypes.ByteList
    public boolean contains(byte item) {
        for (int i2 = 0; i2 < this.data.length; i2++) {
            if (this.data[i2] == item) {
                return true;
            }
        }
        return false;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.datatypes.ByteList
    public byte item(int index) throws XSException {
        if (index < 0 || index > this.data.length - 1) {
            throw new XSException((short) 2, null);
        }
        return this.data[index];
    }

    @Override // java.util.AbstractList, java.util.List
    public Object get(int index) {
        if (index >= 0 && index < this.data.length) {
            return new Byte(this.data[index]);
        }
        throw new IndexOutOfBoundsException("Index: " + index);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return getLength();
    }
}
