package com.sun.org.apache.xerces.internal.impl.xs.util;

import com.sun.org.apache.xerces.internal.xs.XSObject;
import com.sun.org.apache.xerces.internal.xs.XSObjectList;
import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/util/XSObjectListImpl.class */
public class XSObjectListImpl extends AbstractList implements XSObjectList {
    public static final XSObjectListImpl EMPTY_LIST = new XSObjectListImpl(new XSObject[0], 0);
    private static final ListIterator EMPTY_ITERATOR = new ListIterator() { // from class: com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl.1
        @Override // java.util.ListIterator, java.util.Iterator
        public boolean hasNext() {
            return false;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public Object next() {
            throw new NoSuchElementException();
        }

        @Override // java.util.ListIterator
        public boolean hasPrevious() {
            return false;
        }

        @Override // java.util.ListIterator
        public Object previous() {
            throw new NoSuchElementException();
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return 0;
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return -1;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.ListIterator
        public void set(Object object) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.ListIterator
        public void add(Object object) {
            throw new UnsupportedOperationException();
        }
    };
    private static final int DEFAULT_SIZE = 4;
    private XSObject[] fArray;
    private int fLength;

    public XSObjectListImpl() {
        this.fArray = null;
        this.fLength = 0;
        this.fArray = new XSObject[4];
        this.fLength = 0;
    }

    public XSObjectListImpl(XSObject[] array, int length) {
        this.fArray = null;
        this.fLength = 0;
        this.fArray = array;
        this.fLength = length;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObjectList
    public int getLength() {
        return this.fLength;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObjectList
    public XSObject item(int index) {
        if (index < 0 || index >= this.fLength) {
            return null;
        }
        return this.fArray[index];
    }

    public void clearXSObjectList() {
        for (int i2 = 0; i2 < this.fLength; i2++) {
            this.fArray[i2] = null;
        }
        this.fArray = null;
        this.fLength = 0;
    }

    public void addXSObject(XSObject object) {
        if (this.fLength == this.fArray.length) {
            XSObject[] temp = new XSObject[this.fLength + 4];
            System.arraycopy(this.fArray, 0, temp, 0, this.fLength);
            this.fArray = temp;
        }
        XSObject[] xSObjectArr = this.fArray;
        int i2 = this.fLength;
        this.fLength = i2 + 1;
        xSObjectArr[i2] = object;
    }

    public void addXSObject(int index, XSObject object) {
        this.fArray[index] = object;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object value) {
        return value == null ? containsNull() : containsObject(value);
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

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator iterator() {
        return listIterator0(0);
    }

    @Override // java.util.AbstractList, java.util.List
    public ListIterator listIterator() {
        return listIterator0(0);
    }

    @Override // java.util.AbstractList, java.util.List
    public ListIterator listIterator(int index) {
        if (index >= 0 && index < this.fLength) {
            return listIterator0(index);
        }
        throw new IndexOutOfBoundsException("Index: " + index);
    }

    private ListIterator listIterator0(int index) {
        return this.fLength == 0 ? EMPTY_ITERATOR : new XSObjectListIterator(index);
    }

    private boolean containsObject(Object value) {
        for (int i2 = this.fLength - 1; i2 >= 0; i2--) {
            if (value.equals(this.fArray[i2])) {
                return true;
            }
        }
        return false;
    }

    private boolean containsNull() {
        for (int i2 = this.fLength - 1; i2 >= 0; i2--) {
            if (this.fArray[i2] == null) {
                return true;
            }
        }
        return false;
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

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/util/XSObjectListImpl$XSObjectListIterator.class */
    private final class XSObjectListIterator implements ListIterator {
        private int index;

        public XSObjectListIterator(int index) {
            this.index = index;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public boolean hasNext() {
            return this.index < XSObjectListImpl.this.fLength;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public Object next() {
            if (this.index < XSObjectListImpl.this.fLength) {
                XSObject[] xSObjectArr = XSObjectListImpl.this.fArray;
                int i2 = this.index;
                this.index = i2 + 1;
                return xSObjectArr[i2];
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.ListIterator
        public boolean hasPrevious() {
            return this.index > 0;
        }

        @Override // java.util.ListIterator
        public Object previous() {
            if (this.index > 0) {
                XSObject[] xSObjectArr = XSObjectListImpl.this.fArray;
                int i2 = this.index - 1;
                this.index = i2;
                return xSObjectArr[i2];
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return this.index;
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return this.index - 1;
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.ListIterator
        public void set(Object o2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.ListIterator
        public void add(Object o2) {
            throw new UnsupportedOperationException();
        }
    }
}
