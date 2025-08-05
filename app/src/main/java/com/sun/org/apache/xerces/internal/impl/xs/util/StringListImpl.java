package com.sun.org.apache.xerces.internal.impl.xs.util;

import com.sun.org.apache.xerces.internal.xs.StringList;
import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/util/StringListImpl.class */
public final class StringListImpl extends AbstractList implements StringList {
    public static final StringListImpl EMPTY_LIST = new StringListImpl(new String[0], 0);
    private final String[] fArray;
    private final int fLength;
    private final Vector fVector;

    public StringListImpl(Vector v2) {
        this.fVector = v2;
        this.fLength = v2 == null ? 0 : v2.size();
        this.fArray = null;
    }

    public StringListImpl(String[] array, int length) {
        this.fArray = array;
        this.fLength = length;
        this.fVector = null;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.StringList
    public int getLength() {
        return this.fLength;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.StringList
    public boolean contains(String item) {
        if (this.fVector != null) {
            return this.fVector.contains(item);
        }
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

    @Override // com.sun.org.apache.xerces.internal.xs.StringList
    public String item(int index) {
        if (index < 0 || index >= this.fLength) {
            return null;
        }
        if (this.fVector != null) {
            return (String) this.fVector.elementAt(index);
        }
        return this.fArray[index];
    }

    @Override // java.util.AbstractList, java.util.List
    public Object get(int index) {
        if (index >= 0 && index < this.fLength) {
            if (this.fVector != null) {
                return this.fVector.elementAt(index);
            }
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
        if (this.fVector != null) {
            return this.fVector.toArray();
        }
        Object[] a2 = new Object[this.fLength];
        toArray0(a2);
        return a2;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public Object[] toArray(Object[] a2) {
        if (this.fVector != null) {
            return this.fVector.toArray(a2);
        }
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
