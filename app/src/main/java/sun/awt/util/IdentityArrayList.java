package sun.awt.util;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;

/* loaded from: rt.jar:sun/awt/util/IdentityArrayList.class */
public class IdentityArrayList<E> extends AbstractList<E> implements List<E>, RandomAccess {
    private transient Object[] elementData;
    private int size;

    public IdentityArrayList(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + i2);
        }
        this.elementData = new Object[i2];
    }

    public IdentityArrayList() {
        this(10);
    }

    public IdentityArrayList(Collection<? extends E> collection) {
        this.elementData = collection.toArray();
        this.size = this.elementData.length;
        if (this.elementData.getClass() != Object[].class) {
            this.elementData = Arrays.copyOf(this.elementData, this.size, Object[].class);
        }
    }

    public void trimToSize() {
        this.modCount++;
        if (this.size < this.elementData.length) {
            this.elementData = Arrays.copyOf(this.elementData, this.size);
        }
    }

    public void ensureCapacity(int i2) {
        this.modCount++;
        int length = this.elementData.length;
        if (i2 > length) {
            Object[] objArr = this.elementData;
            int i3 = ((length * 3) / 2) + 1;
            if (i3 < i2) {
                i3 = i2;
            }
            this.elementData = Arrays.copyOf(this.elementData, i3);
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.size;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        return indexOf(obj) >= 0;
    }

    @Override // java.util.AbstractList, java.util.List
    public int indexOf(Object obj) {
        for (int i2 = 0; i2 < this.size; i2++) {
            if (obj == this.elementData[i2]) {
                return i2;
            }
        }
        return -1;
    }

    @Override // java.util.AbstractList, java.util.List
    public int lastIndexOf(Object obj) {
        for (int i2 = this.size - 1; i2 >= 0; i2--) {
            if (obj == this.elementData[i2]) {
                return i2;
            }
        }
        return -1;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public Object[] toArray() {
        return Arrays.copyOf(this.elementData, this.size);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        if (tArr.length < this.size) {
            return (T[]) Arrays.copyOf(this.elementData, this.size, tArr.getClass());
        }
        System.arraycopy(this.elementData, 0, tArr, 0, this.size);
        if (tArr.length > this.size) {
            tArr[this.size] = null;
        }
        return tArr;
    }

    @Override // java.util.AbstractList, java.util.List
    public E get(int i2) {
        rangeCheck(i2);
        return (E) this.elementData[i2];
    }

    @Override // java.util.AbstractList, java.util.List
    public E set(int i2, E e2) {
        rangeCheck(i2);
        E e3 = (E) this.elementData[i2];
        this.elementData[i2] = e2;
        return e3;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(E e2) {
        ensureCapacity(this.size + 1);
        Object[] objArr = this.elementData;
        int i2 = this.size;
        this.size = i2 + 1;
        objArr[i2] = e2;
        return true;
    }

    @Override // java.util.AbstractList, java.util.List
    public void add(int i2, E e2) {
        rangeCheckForAdd(i2);
        ensureCapacity(this.size + 1);
        System.arraycopy(this.elementData, i2, this.elementData, i2 + 1, this.size - i2);
        this.elementData[i2] = e2;
        this.size++;
    }

    @Override // java.util.AbstractList, java.util.List
    public E remove(int i2) {
        rangeCheck(i2);
        this.modCount++;
        E e2 = (E) this.elementData[i2];
        int i3 = (this.size - i2) - 1;
        if (i3 > 0) {
            System.arraycopy(this.elementData, i2 + 1, this.elementData, i2, i3);
        }
        Object[] objArr = this.elementData;
        int i4 = this.size - 1;
        this.size = i4;
        objArr[i4] = null;
        return e2;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object obj) {
        for (int i2 = 0; i2 < this.size; i2++) {
            if (obj == this.elementData[i2]) {
                fastRemove(i2);
                return true;
            }
        }
        return false;
    }

    private void fastRemove(int i2) {
        this.modCount++;
        int i3 = (this.size - i2) - 1;
        if (i3 > 0) {
            System.arraycopy(this.elementData, i2 + 1, this.elementData, i2, i3);
        }
        Object[] objArr = this.elementData;
        int i4 = this.size - 1;
        this.size = i4;
        objArr[i4] = null;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        this.modCount++;
        for (int i2 = 0; i2 < this.size; i2++) {
            this.elementData[i2] = null;
        }
        this.size = 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean addAll(Collection<? extends E> collection) {
        Object[] array = collection.toArray();
        int length = array.length;
        ensureCapacity(this.size + length);
        System.arraycopy(array, 0, this.elementData, this.size, length);
        this.size += length;
        return length != 0;
    }

    @Override // java.util.AbstractList, java.util.List
    public boolean addAll(int i2, Collection<? extends E> collection) {
        rangeCheckForAdd(i2);
        Object[] array = collection.toArray();
        int length = array.length;
        ensureCapacity(this.size + length);
        int i3 = this.size - i2;
        if (i3 > 0) {
            System.arraycopy(this.elementData, i2, this.elementData, i2 + length, i3);
        }
        System.arraycopy(array, 0, this.elementData, i2, length);
        this.size += length;
        return length != 0;
    }

    @Override // java.util.AbstractList
    protected void removeRange(int i2, int i3) {
        this.modCount++;
        System.arraycopy(this.elementData, i3, this.elementData, i2, this.size - i3);
        int i4 = this.size - (i3 - i2);
        while (this.size != i4) {
            Object[] objArr = this.elementData;
            int i5 = this.size - 1;
            this.size = i5;
            objArr[i5] = null;
        }
    }

    private void rangeCheck(int i2) {
        if (i2 >= this.size) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(i2));
        }
    }

    private void rangeCheckForAdd(int i2) {
        if (i2 > this.size || i2 < 0) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(i2));
        }
    }

    private String outOfBoundsMsg(int i2) {
        return "Index: " + i2 + ", Size: " + this.size;
    }
}
