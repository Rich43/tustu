package com.sun.org.apache.xerces.internal.impl.xs.util;

import com.sun.org.apache.xerces.internal.xs.LSInputList;
import java.lang.reflect.Array;
import java.util.AbstractList;
import org.w3c.dom.ls.LSInput;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/util/LSInputListImpl.class */
public final class LSInputListImpl extends AbstractList implements LSInputList {
    public static final LSInputListImpl EMPTY_LIST = new LSInputListImpl(new LSInput[0], 0);
    private final LSInput[] fArray;
    private final int fLength;

    public LSInputListImpl(LSInput[] array, int length) {
        this.fArray = array;
        this.fLength = length;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.LSInputList
    public int getLength() {
        return this.fLength;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.LSInputList
    public LSInput item(int index) {
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
