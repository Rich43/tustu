package java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/* loaded from: rt.jar:java/util/Vector.class */
public class Vector<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable, Serializable {
    protected Object[] elementData;
    protected int elementCount;
    protected int capacityIncrement;
    private static final long serialVersionUID = -2767605614048989439L;
    private static final int MAX_ARRAY_SIZE = 2147483639;

    public Vector(int i2, int i3) {
        if (i2 < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + i2);
        }
        this.elementData = new Object[i2];
        this.capacityIncrement = i3;
    }

    public Vector(int i2) {
        this(i2, 0);
    }

    public Vector() {
        this(10);
    }

    public Vector(Collection<? extends E> collection) {
        Object[] array = collection.toArray();
        this.elementCount = array.length;
        if (collection.getClass() == ArrayList.class) {
            this.elementData = array;
        } else {
            this.elementData = Arrays.copyOf(array, this.elementCount, Object[].class);
        }
    }

    public synchronized void copyInto(Object[] objArr) {
        System.arraycopy(this.elementData, 0, objArr, 0, this.elementCount);
    }

    public synchronized void trimToSize() {
        this.modCount++;
        if (this.elementCount < this.elementData.length) {
            this.elementData = Arrays.copyOf(this.elementData, this.elementCount);
        }
    }

    public synchronized void ensureCapacity(int i2) {
        if (i2 > 0) {
            this.modCount++;
            ensureCapacityHelper(i2);
        }
    }

    private void ensureCapacityHelper(int i2) {
        if (i2 - this.elementData.length > 0) {
            grow(i2);
        }
    }

    private void grow(int i2) {
        int length = this.elementData.length;
        int iHugeCapacity = length + (this.capacityIncrement > 0 ? this.capacityIncrement : length);
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

    public synchronized void setSize(int i2) {
        this.modCount++;
        if (i2 > this.elementCount) {
            ensureCapacityHelper(i2);
        } else {
            for (int i3 = i2; i3 < this.elementCount; i3++) {
                this.elementData[i3] = null;
            }
        }
        this.elementCount = i2;
    }

    public synchronized int capacity() {
        return this.elementData.length;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public synchronized int size() {
        return this.elementCount;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public synchronized boolean isEmpty() {
        return this.elementCount == 0;
    }

    public Enumeration<E> elements() {
        return new Enumeration<E>() { // from class: java.util.Vector.1
            int count = 0;

            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                return this.count < Vector.this.elementCount;
            }

            @Override // java.util.Enumeration
            public E nextElement() {
                synchronized (Vector.this) {
                    if (this.count < Vector.this.elementCount) {
                        Vector vector = Vector.this;
                        int i2 = this.count;
                        this.count = i2 + 1;
                        return (E) vector.elementData(i2);
                    }
                    throw new NoSuchElementException("Vector Enumeration");
                }
            }
        };
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        return indexOf(obj, 0) >= 0;
    }

    @Override // java.util.AbstractList, java.util.List
    public int indexOf(Object obj) {
        return indexOf(obj, 0);
    }

    public synchronized int indexOf(Object obj, int i2) {
        if (obj == null) {
            for (int i3 = i2; i3 < this.elementCount; i3++) {
                if (this.elementData[i3] == null) {
                    return i3;
                }
            }
            return -1;
        }
        for (int i4 = i2; i4 < this.elementCount; i4++) {
            if (obj.equals(this.elementData[i4])) {
                return i4;
            }
        }
        return -1;
    }

    @Override // java.util.AbstractList, java.util.List
    public synchronized int lastIndexOf(Object obj) {
        return lastIndexOf(obj, this.elementCount - 1);
    }

    public synchronized int lastIndexOf(Object obj, int i2) {
        if (i2 >= this.elementCount) {
            throw new IndexOutOfBoundsException(i2 + " >= " + this.elementCount);
        }
        if (obj == null) {
            for (int i3 = i2; i3 >= 0; i3--) {
                if (this.elementData[i3] == null) {
                    return i3;
                }
            }
            return -1;
        }
        for (int i4 = i2; i4 >= 0; i4--) {
            if (obj.equals(this.elementData[i4])) {
                return i4;
            }
        }
        return -1;
    }

    public synchronized E elementAt(int i2) {
        if (i2 >= this.elementCount) {
            throw new ArrayIndexOutOfBoundsException(i2 + " >= " + this.elementCount);
        }
        return elementData(i2);
    }

    public synchronized E firstElement() {
        if (this.elementCount == 0) {
            throw new NoSuchElementException();
        }
        return elementData(0);
    }

    public synchronized E lastElement() {
        if (this.elementCount == 0) {
            throw new NoSuchElementException();
        }
        return elementData(this.elementCount - 1);
    }

    public synchronized void setElementAt(E e2, int i2) {
        if (i2 >= this.elementCount) {
            throw new ArrayIndexOutOfBoundsException(i2 + " >= " + this.elementCount);
        }
        this.elementData[i2] = e2;
    }

    public synchronized void removeElementAt(int i2) {
        this.modCount++;
        if (i2 >= this.elementCount) {
            throw new ArrayIndexOutOfBoundsException(i2 + " >= " + this.elementCount);
        }
        if (i2 < 0) {
            throw new ArrayIndexOutOfBoundsException(i2);
        }
        int i3 = (this.elementCount - i2) - 1;
        if (i3 > 0) {
            System.arraycopy(this.elementData, i2 + 1, this.elementData, i2, i3);
        }
        this.elementCount--;
        this.elementData[this.elementCount] = null;
    }

    public synchronized void insertElementAt(E e2, int i2) {
        this.modCount++;
        if (i2 > this.elementCount) {
            throw new ArrayIndexOutOfBoundsException(i2 + " > " + this.elementCount);
        }
        ensureCapacityHelper(this.elementCount + 1);
        System.arraycopy(this.elementData, i2, this.elementData, i2 + 1, this.elementCount - i2);
        this.elementData[i2] = e2;
        this.elementCount++;
    }

    public synchronized void addElement(E e2) {
        this.modCount++;
        ensureCapacityHelper(this.elementCount + 1);
        Object[] objArr = this.elementData;
        int i2 = this.elementCount;
        this.elementCount = i2 + 1;
        objArr[i2] = e2;
    }

    public synchronized boolean removeElement(Object obj) {
        this.modCount++;
        int iIndexOf = indexOf(obj);
        if (iIndexOf >= 0) {
            removeElementAt(iIndexOf);
            return true;
        }
        return false;
    }

    public synchronized void removeAllElements() {
        this.modCount++;
        for (int i2 = 0; i2 < this.elementCount; i2++) {
            this.elementData[i2] = null;
        }
        this.elementCount = 0;
    }

    public synchronized Object clone() {
        try {
            Vector vector = (Vector) super.clone();
            vector.elementData = Arrays.copyOf(this.elementData, this.elementCount);
            vector.modCount = 0;
            return vector;
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public synchronized Object[] toArray() {
        return Arrays.copyOf(this.elementData, this.elementCount);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public synchronized <T> T[] toArray(T[] tArr) {
        if (tArr.length < this.elementCount) {
            return (T[]) Arrays.copyOf(this.elementData, this.elementCount, tArr.getClass());
        }
        System.arraycopy(this.elementData, 0, tArr, 0, this.elementCount);
        if (tArr.length > this.elementCount) {
            tArr[this.elementCount] = null;
        }
        return tArr;
    }

    E elementData(int i2) {
        return (E) this.elementData[i2];
    }

    @Override // java.util.AbstractList, java.util.List
    public synchronized E get(int i2) {
        if (i2 >= this.elementCount) {
            throw new ArrayIndexOutOfBoundsException(i2);
        }
        return elementData(i2);
    }

    @Override // java.util.AbstractList, java.util.List
    public synchronized E set(int i2, E e2) {
        if (i2 >= this.elementCount) {
            throw new ArrayIndexOutOfBoundsException(i2);
        }
        E eElementData = elementData(i2);
        this.elementData[i2] = e2;
        return eElementData;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public synchronized boolean add(E e2) {
        this.modCount++;
        ensureCapacityHelper(this.elementCount + 1);
        Object[] objArr = this.elementData;
        int i2 = this.elementCount;
        this.elementCount = i2 + 1;
        objArr[i2] = e2;
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object obj) {
        return removeElement(obj);
    }

    @Override // java.util.AbstractList, java.util.List
    public void add(int i2, E e2) {
        insertElementAt(e2, i2);
    }

    @Override // java.util.AbstractList, java.util.List
    public synchronized E remove(int i2) {
        this.modCount++;
        if (i2 >= this.elementCount) {
            throw new ArrayIndexOutOfBoundsException(i2);
        }
        E eElementData = elementData(i2);
        int i3 = (this.elementCount - i2) - 1;
        if (i3 > 0) {
            System.arraycopy(this.elementData, i2 + 1, this.elementData, i2, i3);
        }
        Object[] objArr = this.elementData;
        int i4 = this.elementCount - 1;
        this.elementCount = i4;
        objArr[i4] = null;
        return eElementData;
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        removeAllElements();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public synchronized boolean containsAll(Collection<?> collection) {
        return super.containsAll(collection);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public synchronized boolean addAll(Collection<? extends E> collection) {
        this.modCount++;
        Object[] array = collection.toArray();
        int length = array.length;
        ensureCapacityHelper(this.elementCount + length);
        System.arraycopy(array, 0, this.elementData, this.elementCount, length);
        this.elementCount += length;
        return length != 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public synchronized boolean removeAll(Collection<?> collection) {
        return super.removeAll(collection);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public synchronized boolean retainAll(Collection<?> collection) {
        return super.retainAll(collection);
    }

    @Override // java.util.AbstractList, java.util.List
    public synchronized boolean addAll(int i2, Collection<? extends E> collection) {
        this.modCount++;
        if (i2 < 0 || i2 > this.elementCount) {
            throw new ArrayIndexOutOfBoundsException(i2);
        }
        Object[] array = collection.toArray();
        int length = array.length;
        ensureCapacityHelper(this.elementCount + length);
        int i3 = this.elementCount - i2;
        if (i3 > 0) {
            System.arraycopy(this.elementData, i2, this.elementData, i2 + length, i3);
        }
        System.arraycopy(array, 0, this.elementData, i2, length);
        this.elementCount += length;
        return length != 0;
    }

    @Override // java.util.AbstractList, java.util.Collection, java.util.List
    public synchronized boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override // java.util.AbstractList, java.util.Collection, java.util.List
    public synchronized int hashCode() {
        return super.hashCode();
    }

    @Override // java.util.AbstractCollection
    public synchronized String toString() {
        return super.toString();
    }

    @Override // java.util.AbstractList, java.util.List
    public synchronized List<E> subList(int i2, int i3) {
        return Collections.synchronizedList(super.subList(i2, i3), this);
    }

    @Override // java.util.AbstractList
    protected synchronized void removeRange(int i2, int i3) {
        this.modCount++;
        System.arraycopy(this.elementData, i3, this.elementData, i2, this.elementCount - i3);
        int i4 = this.elementCount - (i3 - i2);
        while (this.elementCount != i4) {
            Object[] objArr = this.elementData;
            int i5 = this.elementCount - 1;
            this.elementCount = i5;
            objArr[i5] = null;
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        int i2 = fields.get("elementCount", 0);
        Object[] objArr = (Object[]) fields.get("elementData", (Object) null);
        if (i2 < 0 || objArr == null || i2 > objArr.length) {
            throw new StreamCorruptedException("Inconsistent vector internals");
        }
        this.elementCount = i2;
        this.elementData = (Object[]) objArr.clone();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        Object[] objArr;
        ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
        synchronized (this) {
            putFieldPutFields.put("capacityIncrement", this.capacityIncrement);
            putFieldPutFields.put("elementCount", this.elementCount);
            objArr = (Object[]) this.elementData.clone();
        }
        putFieldPutFields.put("elementData", objArr);
        objectOutputStream.writeFields();
    }

    @Override // java.util.AbstractList, java.util.List
    public synchronized ListIterator<E> listIterator(int i2) {
        if (i2 < 0 || i2 > this.elementCount) {
            throw new IndexOutOfBoundsException("Index: " + i2);
        }
        return new ListItr(i2);
    }

    @Override // java.util.AbstractList, java.util.List
    public synchronized ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public synchronized Iterator<E> iterator() {
        return new Itr();
    }

    /* loaded from: rt.jar:java/util/Vector$Itr.class */
    private class Itr implements Iterator<E> {
        int cursor;
        int lastRet;
        int expectedModCount;

        private Itr() {
            this.lastRet = -1;
            this.expectedModCount = Vector.this.modCount;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.cursor != Vector.this.elementCount;
        }

        @Override // java.util.Iterator
        public E next() {
            E e2;
            synchronized (Vector.this) {
                checkForComodification();
                int i2 = this.cursor;
                if (i2 >= Vector.this.elementCount) {
                    throw new NoSuchElementException();
                }
                this.cursor = i2 + 1;
                Vector vector = Vector.this;
                this.lastRet = i2;
                e2 = (E) vector.elementData(i2);
            }
            return e2;
        }

        @Override // java.util.Iterator
        public void remove() {
            if (this.lastRet == -1) {
                throw new IllegalStateException();
            }
            synchronized (Vector.this) {
                checkForComodification();
                Vector.this.remove(this.lastRet);
                this.expectedModCount = Vector.this.modCount;
            }
            this.cursor = this.lastRet;
            this.lastRet = -1;
        }

        @Override // java.util.Iterator
        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            synchronized (Vector.this) {
                int i2 = Vector.this.elementCount;
                int i3 = this.cursor;
                if (i3 >= i2) {
                    return;
                }
                Object[] objArr = Vector.this.elementData;
                if (i3 >= objArr.length) {
                    throw new ConcurrentModificationException();
                }
                while (i3 != i2 && Vector.this.modCount == this.expectedModCount) {
                    int i4 = i3;
                    i3++;
                    consumer.accept(objArr[i4]);
                }
                this.cursor = i3;
                this.lastRet = i3 - 1;
                checkForComodification();
            }
        }

        final void checkForComodification() {
            if (Vector.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    /* loaded from: rt.jar:java/util/Vector$ListItr.class */
    final class ListItr extends Vector<E>.Itr implements ListIterator<E> {
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
            E e2;
            synchronized (Vector.this) {
                checkForComodification();
                int i2 = this.cursor - 1;
                if (i2 < 0) {
                    throw new NoSuchElementException();
                }
                this.cursor = i2;
                Vector vector = Vector.this;
                this.lastRet = i2;
                e2 = (E) vector.elementData(i2);
            }
            return e2;
        }

        @Override // java.util.ListIterator
        public void set(E e2) {
            if (this.lastRet == -1) {
                throw new IllegalStateException();
            }
            synchronized (Vector.this) {
                checkForComodification();
                Vector.this.set(this.lastRet, e2);
            }
        }

        @Override // java.util.ListIterator
        public void add(E e2) {
            int i2 = this.cursor;
            synchronized (Vector.this) {
                checkForComodification();
                Vector.this.add(i2, e2);
                this.expectedModCount = Vector.this.modCount;
            }
            this.cursor = i2 + 1;
            this.lastRet = -1;
        }
    }

    @Override // java.lang.Iterable
    public synchronized void forEach(Consumer<? super E> consumer) {
        Objects.requireNonNull(consumer);
        int i2 = this.modCount;
        Object[] objArr = this.elementData;
        int i3 = this.elementCount;
        for (int i4 = 0; this.modCount == i2 && i4 < i3; i4++) {
            consumer.accept(objArr[i4]);
        }
        if (this.modCount != i2) {
            throw new ConcurrentModificationException();
        }
    }

    @Override // java.util.Collection
    public synchronized boolean removeIf(Predicate<? super E> predicate) {
        Objects.requireNonNull(predicate);
        int i2 = 0;
        int i3 = this.elementCount;
        BitSet bitSet = new BitSet(i3);
        int i4 = this.modCount;
        for (int i5 = 0; this.modCount == i4 && i5 < i3; i5++) {
            if (predicate.test(this.elementData[i5])) {
                bitSet.set(i5);
                i2++;
            }
        }
        if (this.modCount != i4) {
            throw new ConcurrentModificationException();
        }
        boolean z2 = i2 > 0;
        if (z2) {
            int i6 = i3 - i2;
            int i7 = 0;
            for (int i8 = 0; i7 < i3 && i8 < i6; i8++) {
                int iNextClearBit = bitSet.nextClearBit(i7);
                this.elementData[i8] = this.elementData[iNextClearBit];
                i7 = iNextClearBit + 1;
            }
            for (int i9 = i6; i9 < i3; i9++) {
                this.elementData[i9] = null;
            }
            this.elementCount = i6;
            if (this.modCount != i4) {
                throw new ConcurrentModificationException();
            }
            this.modCount++;
        }
        return z2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.List
    public synchronized void replaceAll(UnaryOperator<E> unaryOperator) {
        Objects.requireNonNull(unaryOperator);
        int i2 = this.modCount;
        int i3 = this.elementCount;
        for (int i4 = 0; this.modCount == i2 && i4 < i3; i4++) {
            this.elementData[i4] = unaryOperator.apply(this.elementData[i4]);
        }
        if (this.modCount != i2) {
            throw new ConcurrentModificationException();
        }
        this.modCount++;
    }

    @Override // java.util.List, com.sun.javafx.collections.SortableList
    public synchronized void sort(Comparator<? super E> comparator) {
        int i2 = this.modCount;
        Arrays.sort(this.elementData, 0, this.elementCount, comparator);
        if (this.modCount != i2) {
            throw new ConcurrentModificationException();
        }
        this.modCount++;
    }

    @Override // java.util.Collection, java.lang.Iterable
    public Spliterator<E> spliterator() {
        return new VectorSpliterator(this, null, 0, -1, 0);
    }

    /* loaded from: rt.jar:java/util/Vector$VectorSpliterator.class */
    static final class VectorSpliterator<E> implements Spliterator<E> {
        private final Vector<E> list;
        private Object[] array;
        private int index;
        private int fence;
        private int expectedModCount;

        VectorSpliterator(Vector<E> vector, Object[] objArr, int i2, int i3, int i4) {
            this.list = vector;
            this.array = objArr;
            this.index = i2;
            this.fence = i3;
            this.expectedModCount = i4;
        }

        private int getFence() {
            int i2 = this.fence;
            int i3 = i2;
            if (i2 < 0) {
                synchronized (this.list) {
                    this.array = this.list.elementData;
                    this.expectedModCount = this.list.modCount;
                    int i4 = this.list.elementCount;
                    this.fence = i4;
                    i3 = i4;
                }
            }
            return i3;
        }

        @Override // java.util.Spliterator
        public Spliterator<E> trySplit() {
            int fence = getFence();
            int i2 = this.index;
            int i3 = (i2 + fence) >>> 1;
            if (i2 >= i3) {
                return null;
            }
            Vector<E> vector = this.list;
            Object[] objArr = this.array;
            this.index = i3;
            return new VectorSpliterator(vector, objArr, i2, i3, this.expectedModCount);
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super E> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            int fence = getFence();
            int i2 = this.index;
            if (fence > i2) {
                this.index = i2 + 1;
                consumer.accept(this.array[i2]);
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
            if (consumer == null) {
                throw new NullPointerException();
            }
            Vector<E> vector = this.list;
            if (vector != null) {
                int i2 = this.fence;
                int i3 = i2;
                if (i2 < 0) {
                    synchronized (vector) {
                        this.expectedModCount = vector.modCount;
                        Object[] objArr2 = vector.elementData;
                        this.array = objArr2;
                        objArr = objArr2;
                        int i4 = vector.elementCount;
                        this.fence = i4;
                        i3 = i4;
                    }
                } else {
                    objArr = this.array;
                }
                if (objArr != null) {
                    int i5 = this.index;
                    int i6 = i5;
                    if (i5 >= 0) {
                        int i7 = i3;
                        this.index = i7;
                        if (i7 <= objArr.length) {
                            while (i6 < i3) {
                                int i8 = i6;
                                i6++;
                                consumer.accept(objArr[i8]);
                            }
                            if (vector.modCount == this.expectedModCount) {
                                return;
                            }
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
}
