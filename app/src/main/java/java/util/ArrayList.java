package java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import sun.misc.SharedSecrets;

/* loaded from: rt.jar:java/util/ArrayList.class */
public class ArrayList<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable, Serializable {
    private static final long serialVersionUID = 8683452581122892189L;
    private static final int DEFAULT_CAPACITY = 10;
    private static final Object[] EMPTY_ELEMENTDATA = new Object[0];
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = new Object[0];
    transient Object[] elementData;
    private int size;
    private static final int MAX_ARRAY_SIZE = 2147483639;

    public ArrayList(int i2) {
        if (i2 > 0) {
            this.elementData = new Object[i2];
        } else {
            if (i2 == 0) {
                this.elementData = EMPTY_ELEMENTDATA;
                return;
            }
            throw new IllegalArgumentException("Illegal Capacity: " + i2);
        }
    }

    public ArrayList() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }

    public ArrayList(Collection<? extends E> collection) {
        Object[] array = collection.toArray();
        int length = array.length;
        this.size = length;
        if (length != 0) {
            if (collection.getClass() == ArrayList.class) {
                this.elementData = array;
                return;
            } else {
                this.elementData = Arrays.copyOf(array, this.size, Object[].class);
                return;
            }
        }
        this.elementData = EMPTY_ELEMENTDATA;
    }

    public void trimToSize() {
        this.modCount++;
        if (this.size < this.elementData.length) {
            this.elementData = this.size == 0 ? EMPTY_ELEMENTDATA : Arrays.copyOf(this.elementData, this.size);
        }
    }

    public void ensureCapacity(int i2) {
        if (i2 > (this.elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA ? 0 : 10)) {
            ensureExplicitCapacity(i2);
        }
    }

    private static int calculateCapacity(Object[] objArr, int i2) {
        if (objArr == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            return Math.max(10, i2);
        }
        return i2;
    }

    private void ensureCapacityInternal(int i2) {
        ensureExplicitCapacity(calculateCapacity(this.elementData, i2));
    }

    private void ensureExplicitCapacity(int i2) {
        this.modCount++;
        if (i2 - this.elementData.length > 0) {
            grow(i2);
        }
    }

    private void grow(int i2) {
        int length = this.elementData.length;
        int iHugeCapacity = length + (length >> 1);
        if (iHugeCapacity - i2 < 0) {
            iHugeCapacity = i2;
        }
        if (iHugeCapacity - MAX_ARRAY_SIZE > 0) {
            iHugeCapacity = hugeCapacity(i2);
        }
        this.elementData = Arrays.copyOf(this.elementData, iHugeCapacity);
    }

    private static int hugeCapacity(int i2) {
        if (i2 < 0) {
            throw new OutOfMemoryError();
        }
        if (i2 > MAX_ARRAY_SIZE) {
            return Integer.MAX_VALUE;
        }
        return MAX_ARRAY_SIZE;
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
        if (obj == null) {
            for (int i2 = 0; i2 < this.size; i2++) {
                if (this.elementData[i2] == null) {
                    return i2;
                }
            }
            return -1;
        }
        for (int i3 = 0; i3 < this.size; i3++) {
            if (obj.equals(this.elementData[i3])) {
                return i3;
            }
        }
        return -1;
    }

    @Override // java.util.AbstractList, java.util.List
    public int lastIndexOf(Object obj) {
        if (obj == null) {
            for (int i2 = this.size - 1; i2 >= 0; i2--) {
                if (this.elementData[i2] == null) {
                    return i2;
                }
            }
            return -1;
        }
        for (int i3 = this.size - 1; i3 >= 0; i3--) {
            if (obj.equals(this.elementData[i3])) {
                return i3;
            }
        }
        return -1;
    }

    public Object clone() {
        try {
            ArrayList arrayList = (ArrayList) super.clone();
            arrayList.elementData = Arrays.copyOf(this.elementData, this.size);
            arrayList.modCount = 0;
            return arrayList;
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
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

    E elementData(int i2) {
        return (E) this.elementData[i2];
    }

    @Override // java.util.AbstractList, java.util.List
    public E get(int i2) {
        rangeCheck(i2);
        return elementData(i2);
    }

    @Override // java.util.AbstractList, java.util.List
    public E set(int i2, E e2) {
        rangeCheck(i2);
        E eElementData = elementData(i2);
        this.elementData[i2] = e2;
        return eElementData;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(E e2) {
        ensureCapacityInternal(this.size + 1);
        Object[] objArr = this.elementData;
        int i2 = this.size;
        this.size = i2 + 1;
        objArr[i2] = e2;
        return true;
    }

    @Override // java.util.AbstractList, java.util.List
    public void add(int i2, E e2) {
        rangeCheckForAdd(i2);
        ensureCapacityInternal(this.size + 1);
        System.arraycopy(this.elementData, i2, this.elementData, i2 + 1, this.size - i2);
        this.elementData[i2] = e2;
        this.size++;
    }

    @Override // java.util.AbstractList, java.util.List
    public E remove(int i2) {
        rangeCheck(i2);
        this.modCount++;
        E eElementData = elementData(i2);
        int i3 = (this.size - i2) - 1;
        if (i3 > 0) {
            System.arraycopy(this.elementData, i2 + 1, this.elementData, i2, i3);
        }
        Object[] objArr = this.elementData;
        int i4 = this.size - 1;
        this.size = i4;
        objArr[i4] = null;
        return eElementData;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object obj) {
        if (obj == null) {
            for (int i2 = 0; i2 < this.size; i2++) {
                if (this.elementData[i2] == null) {
                    fastRemove(i2);
                    return true;
                }
            }
            return false;
        }
        for (int i3 = 0; i3 < this.size; i3++) {
            if (obj.equals(this.elementData[i3])) {
                fastRemove(i3);
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
        ensureCapacityInternal(this.size + length);
        System.arraycopy(array, 0, this.elementData, this.size, length);
        this.size += length;
        return length != 0;
    }

    @Override // java.util.AbstractList, java.util.List
    public boolean addAll(int i2, Collection<? extends E> collection) {
        rangeCheckForAdd(i2);
        Object[] array = collection.toArray();
        int length = array.length;
        ensureCapacityInternal(this.size + length);
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
        for (int i5 = i4; i5 < this.size; i5++) {
            this.elementData[i5] = null;
        }
        this.size = i4;
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

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean removeAll(Collection<?> collection) {
        Objects.requireNonNull(collection);
        return batchRemove(collection, false);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean retainAll(Collection<?> collection) {
        Objects.requireNonNull(collection);
        return batchRemove(collection, true);
    }

    /* JADX WARN: Finally extract failed */
    private boolean batchRemove(Collection<?> collection, boolean z2) {
        Object[] objArr = this.elementData;
        int i2 = 0;
        int i3 = 0;
        boolean z3 = false;
        while (i2 < this.size) {
            try {
                if (collection.contains(objArr[i2]) == z2) {
                    int i4 = i3;
                    i3++;
                    objArr[i4] = objArr[i2];
                }
                i2++;
            } catch (Throwable th) {
                if (i2 != this.size) {
                    System.arraycopy(objArr, i2, objArr, i3, this.size - i2);
                    i3 += this.size - i2;
                }
                if (i3 != this.size) {
                    for (int i5 = i3; i5 < this.size; i5++) {
                        objArr[i5] = null;
                    }
                    this.modCount += this.size - i3;
                    this.size = i3;
                }
                throw th;
            }
        }
        if (i2 != this.size) {
            System.arraycopy(objArr, i2, objArr, i3, this.size - i2);
            i3 += this.size - i2;
        }
        if (i3 != this.size) {
            for (int i6 = i3; i6 < this.size; i6++) {
                objArr[i6] = null;
            }
            this.modCount += this.size - i3;
            this.size = i3;
            z3 = true;
        }
        return z3;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        int i2 = this.modCount;
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(this.size);
        for (int i3 = 0; i3 < this.size; i3++) {
            objectOutputStream.writeObject(this.elementData[i3]);
        }
        if (this.modCount != i2) {
            throw new ConcurrentModificationException();
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.elementData = EMPTY_ELEMENTDATA;
        objectInputStream.defaultReadObject();
        objectInputStream.readInt();
        if (this.size > 0) {
            SharedSecrets.getJavaOISAccess().checkArray(objectInputStream, Object[].class, calculateCapacity(this.elementData, this.size));
            ensureCapacityInternal(this.size);
            Object[] objArr = this.elementData;
            for (int i2 = 0; i2 < this.size; i2++) {
                objArr[i2] = objectInputStream.readObject();
            }
        }
    }

    @Override // java.util.AbstractList, java.util.List
    public ListIterator<E> listIterator(int i2) {
        if (i2 < 0 || i2 > this.size) {
            throw new IndexOutOfBoundsException("Index: " + i2);
        }
        return new ListItr(i2);
    }

    @Override // java.util.AbstractList, java.util.List
    public ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<E> iterator() {
        return new Itr();
    }

    /* loaded from: rt.jar:java/util/ArrayList$Itr.class */
    private class Itr implements Iterator<E> {
        int cursor;
        int lastRet = -1;
        int expectedModCount;

        Itr() {
            this.expectedModCount = ArrayList.this.modCount;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.cursor != ArrayList.this.size;
        }

        @Override // java.util.Iterator
        public E next() {
            checkForComodification();
            int i2 = this.cursor;
            if (i2 >= ArrayList.this.size) {
                throw new NoSuchElementException();
            }
            Object[] objArr = ArrayList.this.elementData;
            if (i2 >= objArr.length) {
                throw new ConcurrentModificationException();
            }
            this.cursor = i2 + 1;
            this.lastRet = i2;
            return (E) objArr[i2];
        }

        @Override // java.util.Iterator
        public void remove() {
            if (this.lastRet < 0) {
                throw new IllegalStateException();
            }
            checkForComodification();
            try {
                ArrayList.this.remove(this.lastRet);
                this.cursor = this.lastRet;
                this.lastRet = -1;
                this.expectedModCount = ArrayList.this.modCount;
            } catch (IndexOutOfBoundsException e2) {
                throw new ConcurrentModificationException();
            }
        }

        @Override // java.util.Iterator
        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            int i2 = ArrayList.this.size;
            int i3 = this.cursor;
            if (i3 >= i2) {
                return;
            }
            Object[] objArr = ArrayList.this.elementData;
            if (i3 >= objArr.length) {
                throw new ConcurrentModificationException();
            }
            while (i3 != i2 && ArrayList.this.modCount == this.expectedModCount) {
                int i4 = i3;
                i3++;
                consumer.accept(objArr[i4]);
            }
            this.cursor = i3;
            this.lastRet = i3 - 1;
            checkForComodification();
        }

        final void checkForComodification() {
            if (ArrayList.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    /* loaded from: rt.jar:java/util/ArrayList$ListItr.class */
    private class ListItr extends ArrayList<E>.Itr implements ListIterator<E> {
        ListItr(int i2) {
            super();
            this.cursor = i2;
        }

        @Override // java.util.ListIterator
        public boolean hasPrevious() {
            return this.cursor != 0;
        }

        @Override // java.util.ListIterator
        public int nextIndex() {
            return this.cursor;
        }

        @Override // java.util.ListIterator
        public int previousIndex() {
            return this.cursor - 1;
        }

        @Override // java.util.ListIterator
        public E previous() {
            checkForComodification();
            int i2 = this.cursor - 1;
            if (i2 < 0) {
                throw new NoSuchElementException();
            }
            Object[] objArr = ArrayList.this.elementData;
            if (i2 >= objArr.length) {
                throw new ConcurrentModificationException();
            }
            this.cursor = i2;
            this.lastRet = i2;
            return (E) objArr[i2];
        }

        @Override // java.util.ListIterator
        public void set(E e2) {
            if (this.lastRet < 0) {
                throw new IllegalStateException();
            }
            checkForComodification();
            try {
                ArrayList.this.set(this.lastRet, e2);
            } catch (IndexOutOfBoundsException e3) {
                throw new ConcurrentModificationException();
            }
        }

        @Override // java.util.ListIterator
        public void add(E e2) {
            checkForComodification();
            try {
                int i2 = this.cursor;
                ArrayList.this.add(i2, e2);
                this.cursor = i2 + 1;
                this.lastRet = -1;
                this.expectedModCount = ArrayList.this.modCount;
            } catch (IndexOutOfBoundsException e3) {
                throw new ConcurrentModificationException();
            }
        }
    }

    @Override // java.util.AbstractList, java.util.List
    public List<E> subList(int i2, int i3) {
        subListRangeCheck(i2, i3, this.size);
        return new SubList(this, 0, i2, i3);
    }

    static void subListRangeCheck(int i2, int i3, int i4) {
        if (i2 < 0) {
            throw new IndexOutOfBoundsException("fromIndex = " + i2);
        }
        if (i3 > i4) {
            throw new IndexOutOfBoundsException("toIndex = " + i3);
        }
        if (i2 > i3) {
            throw new IllegalArgumentException("fromIndex(" + i2 + ") > toIndex(" + i3 + ")");
        }
    }

    /* loaded from: rt.jar:java/util/ArrayList$SubList.class */
    private class SubList extends AbstractList<E> implements RandomAccess {
        private final AbstractList<E> parent;
        private final int parentOffset;
        private final int offset;
        int size;

        SubList(AbstractList<E> abstractList, int i2, int i3, int i4) {
            this.parent = abstractList;
            this.parentOffset = i3;
            this.offset = i2 + i3;
            this.size = i4 - i3;
            this.modCount = ArrayList.this.modCount;
        }

        @Override // java.util.AbstractList, java.util.List
        public E set(int i2, E e2) {
            rangeCheck(i2);
            checkForComodification();
            E e3 = (E) ArrayList.this.elementData(this.offset + i2);
            ArrayList.this.elementData[this.offset + i2] = e2;
            return e3;
        }

        @Override // java.util.AbstractList, java.util.List
        public E get(int i2) {
            rangeCheck(i2);
            checkForComodification();
            return (E) ArrayList.this.elementData(this.offset + i2);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            checkForComodification();
            return this.size;
        }

        @Override // java.util.AbstractList, java.util.List
        public void add(int i2, E e2) {
            rangeCheckForAdd(i2);
            checkForComodification();
            this.parent.add(this.parentOffset + i2, e2);
            this.modCount = this.parent.modCount;
            this.size++;
        }

        @Override // java.util.AbstractList, java.util.List
        public E remove(int i2) {
            rangeCheck(i2);
            checkForComodification();
            E eRemove = this.parent.remove(this.parentOffset + i2);
            this.modCount = this.parent.modCount;
            this.size--;
            return eRemove;
        }

        @Override // java.util.AbstractList
        protected void removeRange(int i2, int i3) {
            checkForComodification();
            this.parent.removeRange(this.parentOffset + i2, this.parentOffset + i3);
            this.modCount = this.parent.modCount;
            this.size -= i3 - i2;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean addAll(Collection<? extends E> collection) {
            return addAll(this.size, collection);
        }

        @Override // java.util.AbstractList, java.util.List
        public boolean addAll(int i2, Collection<? extends E> collection) {
            rangeCheckForAdd(i2);
            int size = collection.size();
            if (size == 0) {
                return false;
            }
            checkForComodification();
            this.parent.addAll(this.parentOffset + i2, collection);
            this.modCount = this.parent.modCount;
            this.size += size;
            return true;
        }

        @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<E> iterator() {
            return listIterator();
        }

        @Override // java.util.AbstractList, java.util.List
        public ListIterator<E> listIterator(final int i2) {
            checkForComodification();
            rangeCheckForAdd(i2);
            final int i3 = this.offset;
            return new ListIterator<E>() { // from class: java.util.ArrayList.SubList.1
                int cursor;
                int lastRet = -1;
                int expectedModCount;

                {
                    this.cursor = i2;
                    this.expectedModCount = ArrayList.this.modCount;
                }

                @Override // java.util.ListIterator, java.util.Iterator
                public boolean hasNext() {
                    return this.cursor != SubList.this.size;
                }

                @Override // java.util.ListIterator, java.util.Iterator
                public E next() {
                    checkForComodification();
                    int i4 = this.cursor;
                    if (i4 >= SubList.this.size) {
                        throw new NoSuchElementException();
                    }
                    Object[] objArr = ArrayList.this.elementData;
                    if (i3 + i4 >= objArr.length) {
                        throw new ConcurrentModificationException();
                    }
                    this.cursor = i4 + 1;
                    int i5 = i3;
                    this.lastRet = i4;
                    return (E) objArr[i5 + i4];
                }

                @Override // java.util.ListIterator
                public boolean hasPrevious() {
                    return this.cursor != 0;
                }

                @Override // java.util.ListIterator
                public E previous() {
                    checkForComodification();
                    int i4 = this.cursor - 1;
                    if (i4 < 0) {
                        throw new NoSuchElementException();
                    }
                    Object[] objArr = ArrayList.this.elementData;
                    if (i3 + i4 >= objArr.length) {
                        throw new ConcurrentModificationException();
                    }
                    this.cursor = i4;
                    int i5 = i3;
                    this.lastRet = i4;
                    return (E) objArr[i5 + i4];
                }

                @Override // java.util.Iterator
                public void forEachRemaining(Consumer<? super E> consumer) {
                    Objects.requireNonNull(consumer);
                    int i4 = SubList.this.size;
                    int i5 = this.cursor;
                    if (i5 >= i4) {
                        return;
                    }
                    Object[] objArr = ArrayList.this.elementData;
                    if (i3 + i5 >= objArr.length) {
                        throw new ConcurrentModificationException();
                    }
                    while (i5 != i4 && SubList.this.modCount == this.expectedModCount) {
                        int i6 = i5;
                        i5++;
                        consumer.accept(objArr[i3 + i6]);
                    }
                    int i7 = i5;
                    this.cursor = i7;
                    this.lastRet = i7;
                    checkForComodification();
                }

                @Override // java.util.ListIterator
                public int nextIndex() {
                    return this.cursor;
                }

                @Override // java.util.ListIterator
                public int previousIndex() {
                    return this.cursor - 1;
                }

                @Override // java.util.ListIterator, java.util.Iterator
                public void remove() {
                    if (this.lastRet < 0) {
                        throw new IllegalStateException();
                    }
                    checkForComodification();
                    try {
                        SubList.this.remove(this.lastRet);
                        this.cursor = this.lastRet;
                        this.lastRet = -1;
                        this.expectedModCount = ArrayList.this.modCount;
                    } catch (IndexOutOfBoundsException e2) {
                        throw new ConcurrentModificationException();
                    }
                }

                @Override // java.util.ListIterator
                public void set(E e2) {
                    if (this.lastRet < 0) {
                        throw new IllegalStateException();
                    }
                    checkForComodification();
                    try {
                        ArrayList.this.set(i3 + this.lastRet, e2);
                    } catch (IndexOutOfBoundsException e3) {
                        throw new ConcurrentModificationException();
                    }
                }

                @Override // java.util.ListIterator
                public void add(E e2) {
                    checkForComodification();
                    try {
                        int i4 = this.cursor;
                        SubList.this.add(i4, e2);
                        this.cursor = i4 + 1;
                        this.lastRet = -1;
                        this.expectedModCount = ArrayList.this.modCount;
                    } catch (IndexOutOfBoundsException e3) {
                        throw new ConcurrentModificationException();
                    }
                }

                final void checkForComodification() {
                    if (this.expectedModCount != ArrayList.this.modCount) {
                        throw new ConcurrentModificationException();
                    }
                }
            };
        }

        @Override // java.util.AbstractList, java.util.List
        public List<E> subList(int i2, int i3) {
            ArrayList.subListRangeCheck(i2, i3, this.size);
            return new SubList(this, this.offset, i2, i3);
        }

        private void rangeCheck(int i2) {
            if (i2 < 0 || i2 >= this.size) {
                throw new IndexOutOfBoundsException(outOfBoundsMsg(i2));
            }
        }

        private void rangeCheckForAdd(int i2) {
            if (i2 < 0 || i2 > this.size) {
                throw new IndexOutOfBoundsException(outOfBoundsMsg(i2));
            }
        }

        private String outOfBoundsMsg(int i2) {
            return "Index: " + i2 + ", Size: " + this.size;
        }

        private void checkForComodification() {
            if (ArrayList.this.modCount != this.modCount) {
                throw new ConcurrentModificationException();
            }
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Spliterator<E> spliterator() {
            checkForComodification();
            return new ArrayListSpliterator(ArrayList.this, this.offset, this.offset + this.size, this.modCount);
        }
    }

    @Override // java.lang.Iterable
    public void forEach(Consumer<? super E> consumer) {
        Objects.requireNonNull(consumer);
        int i2 = this.modCount;
        Object[] objArr = this.elementData;
        int i3 = this.size;
        for (int i4 = 0; this.modCount == i2 && i4 < i3; i4++) {
            consumer.accept(objArr[i4]);
        }
        if (this.modCount != i2) {
            throw new ConcurrentModificationException();
        }
    }

    @Override // java.util.Collection, java.lang.Iterable
    public Spliterator<E> spliterator() {
        return new ArrayListSpliterator(this, 0, -1, 0);
    }

    /* loaded from: rt.jar:java/util/ArrayList$ArrayListSpliterator.class */
    static final class ArrayListSpliterator<E> implements Spliterator<E> {
        private final ArrayList<E> list;
        private int index;
        private int fence;
        private int expectedModCount;

        ArrayListSpliterator(ArrayList<E> arrayList, int i2, int i3, int i4) {
            this.list = arrayList;
            this.index = i2;
            this.fence = i3;
            this.expectedModCount = i4;
        }

        private int getFence() {
            int i2 = this.fence;
            int i3 = i2;
            if (i2 < 0) {
                ArrayList<E> arrayList = this.list;
                if (arrayList == null) {
                    this.fence = 0;
                    i3 = 0;
                } else {
                    this.expectedModCount = arrayList.modCount;
                    int i4 = ((ArrayList) arrayList).size;
                    this.fence = i4;
                    i3 = i4;
                }
            }
            return i3;
        }

        @Override // java.util.Spliterator
        public ArrayListSpliterator<E> trySplit() {
            int fence = getFence();
            int i2 = this.index;
            int i3 = (i2 + fence) >>> 1;
            if (i2 >= i3) {
                return null;
            }
            ArrayList<E> arrayList = this.list;
            this.index = i3;
            return new ArrayListSpliterator<>(arrayList, i2, i3, this.expectedModCount);
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super E> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            int fence = getFence();
            int i2 = this.index;
            if (i2 < fence) {
                this.index = i2 + 1;
                consumer.accept(this.list.elementData[i2]);
                if (this.list.modCount != this.expectedModCount) {
                    throw new ConcurrentModificationException();
                }
                return true;
            }
            return false;
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super E> consumer) {
            Object[] objArr;
            int i2;
            if (consumer == null) {
                throw new NullPointerException();
            }
            ArrayList<E> arrayList = this.list;
            if (arrayList != null && (objArr = arrayList.elementData) != null) {
                int i3 = this.fence;
                int i4 = i3;
                if (i3 < 0) {
                    i2 = arrayList.modCount;
                    i4 = ((ArrayList) arrayList).size;
                } else {
                    i2 = this.expectedModCount;
                }
                int i5 = this.index;
                if (i5 >= 0) {
                    int i6 = i4;
                    this.index = i6;
                    if (i6 <= objArr.length) {
                        for (int i7 = i5; i7 < i4; i7++) {
                            consumer.accept(objArr[i7]);
                        }
                        if (arrayList.modCount == i2) {
                            return;
                        }
                    }
                }
            }
            throw new ConcurrentModificationException();
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return getFence() - this.index;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return 16464;
        }
    }

    @Override // java.util.Collection
    public boolean removeIf(Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        int i2 = 0;
        BitSet bitSet = new BitSet(this.size);
        int i3 = this.modCount;
        int i4 = this.size;
        for (int i5 = 0; this.modCount == i3 && i5 < i4; i5++) {
            if (predicate.test(this.elementData[i5])) {
                bitSet.set(i5);
                i2++;
            }
        }
        if (this.modCount != i3) {
            throw new ConcurrentModificationException();
        }
        boolean z2 = i2 > 0;
        if (z2) {
            int i6 = i4 - i2;
            int i7 = 0;
            for (int i8 = 0; i7 < i4 && i8 < i6; i8++) {
                int iNextClearBit = bitSet.nextClearBit(i7);
                this.elementData[i8] = this.elementData[iNextClearBit];
                i7 = iNextClearBit + 1;
            }
            for (int i9 = i6; i9 < i4; i9++) {
                this.elementData[i9] = null;
            }
            this.size = i6;
            if (this.modCount != i3) {
                throw new ConcurrentModificationException();
            }
            this.modCount++;
        }
        return z2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.List
    public void replaceAll(UnaryOperator<E> unaryOperator) {
        Objects.requireNonNull(unaryOperator);
        int i2 = this.modCount;
        int i3 = this.size;
        for (int i4 = 0; this.modCount == i2 && i4 < i3; i4++) {
            this.elementData[i4] = unaryOperator.apply(this.elementData[i4]);
        }
        if (this.modCount != i2) {
            throw new ConcurrentModificationException();
        }
        this.modCount++;
    }

    @Override // java.util.List, com.sun.javafx.collections.SortableList
    public void sort(Comparator<? super E> comparator) {
        int i2 = this.modCount;
        Arrays.sort(this.elementData, 0, this.size, comparator);
        if (this.modCount != i2) {
            throw new ConcurrentModificationException();
        }
        this.modCount++;
    }
}
