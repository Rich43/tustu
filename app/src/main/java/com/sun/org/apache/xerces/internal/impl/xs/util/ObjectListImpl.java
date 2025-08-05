package com.sun.org.apache.xerces.internal.impl.xs.util;

import com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList;
import java.lang.reflect.Array;
import java.util.AbstractList;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/util/ObjectListImpl.class */
public final class ObjectListImpl extends AbstractList implements ObjectList {
    public static final ObjectListImpl EMPTY_LIST = new ObjectListImpl(new Object[0], 0);
    private final Object[] fArray;
    private final int fLength;

    public ObjectListImpl(Object[] array, int length) {
        this.fArray = array;
        this.fLength = length;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList
    public int getLength() {
        return this.fLength;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object item) {
        if (item == null) {
            for (int i2 = 0; i2 < this.fLength; i2++) {
                if (this.fArray[i2] == null) {
                    return true;
                }
            }
            return false;
        }
        for (int i3 = 0; i3 < this.fLength; i3++) {
            if (item.equals(this.fArray[i3])) {
                return true;
            }
        }
        return false;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList
    public Object item(int index) {
        if (index < 0 || index >= this.fLength) {
            return null;
        }
        return this.fArray[index];
    }

    @Override // java.util.AbstractList, java.util.List
    public Object get(int index) {
        if (index >= 0 && index < this.fLength) {
            return this.fArray[index];
        }
        throw new IndexOutOfBoundsException("Index: " + index);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return getLength();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public Object[] toArray() {
        Object[] a2 = new Object[this.fLength];
        toArray0(a2);
        return a2;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public Object[] toArray(Object[] a2) {
        if (a2.length < this.fLength) {
            Class arrayClass = a2.getClass();
            Class componentType = arrayClass.getComponentType();
            a2 = (Object[]) Array.newInstance((Class<?>) componentType, this.fLength);
        }
        toArray0(a2);
        if (a2.length > this.fLength) {
            a2[this.fLength] = null;
        }
        return a2;
    }

    private void toArray0(Object[] a2) {
        if (this.fLength > 0) {
            System.arraycopy(this.fArray, 0, a2, 0, this.fLength);
        }
    }
}
