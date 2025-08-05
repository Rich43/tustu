package java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.function.Consumer;
import sun.misc.SharedSecrets;

/* loaded from: rt.jar:java/util/ArrayDeque.class */
public class ArrayDeque<E> extends AbstractCollection<E> implements Deque<E>, Cloneable, Serializable {
    transient Object[] elements;
    transient int head;
    transient int tail;
    private static final int MIN_INITIAL_CAPACITY = 8;
    private static final long serialVersionUID = 2340985798034038923L;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ArrayDeque.class.desiredAssertionStatus();
    }

    private static int calculateSize(int i2) {
        int i3 = 8;
        if (i2 >= 8) {
            int i4 = i2 | (i2 >>> 1);
            int i5 = i4 | (i4 >>> 2);
            int i6 = i5 | (i5 >>> 4);
            int i7 = i6 | (i6 >>> 8);
            i3 = (i7 | (i7 >>> 16)) + 1;
            if (i3 < 0) {
                i3 >>>= 1;
            }
        }
        return i3;
    }

    private void allocateElements(int i2) {
        this.elements = new Object[calculateSize(i2)];
    }

    private void doubleCapacity() {
        if (!$assertionsDisabled && this.head != this.tail) {
            throw new AssertionError();
        }
        int i2 = this.head;
        int length = this.elements.length;
        int i3 = length - i2;
        int i4 = length << 1;
        if (i4 < 0) {
            throw new IllegalStateException("Sorry, deque too big");
        }
        Object[] objArr = new Object[i4];
        System.arraycopy(this.elements, i2, objArr, 0, i3);
        System.arraycopy(this.elements, 0, objArr, i3, i2);
        this.elements = objArr;
        this.head = 0;
        this.tail = length;
    }

    private <T> T[] copyElements(T[] tArr) {
        if (this.head < this.tail) {
            System.arraycopy(this.elements, this.head, tArr, 0, size());
        } else if (this.head > this.tail) {
            int length = this.elements.length - this.head;
            System.arraycopy(this.elements, this.head, tArr, 0, length);
            System.arraycopy(this.elements, 0, tArr, length, this.tail);
        }
        return tArr;
    }

    public ArrayDeque() {
        this.elements = new Object[16];
    }

    public ArrayDeque(int i2) {
        allocateElements(i2);
    }

    public ArrayDeque(Collection<? extends E> collection) {
        allocateElements(collection.size());
        addAll(collection);
    }

    @Override // java.util.Deque
    public void addFirst(E e2) {
        if (e2 == null) {
            throw new NullPointerException();
        }
        Object[] objArr = this.elements;
        int length = (this.head - 1) & (this.elements.length - 1);
        this.head = length;
        objArr[length] = e2;
        if (this.head == this.tail) {
            doubleCapacity();
        }
    }

    @Override // java.util.Deque
    public void addLast(E e2) {
        if (e2 == null) {
            throw new NullPointerException();
        }
        this.elements[this.tail] = e2;
        int length = (this.tail + 1) & (this.elements.length - 1);
        this.tail = length;
        if (length == this.head) {
            doubleCapacity();
        }
    }

    @Override // java.util.Deque
    public boolean offerFirst(E e2) {
        addFirst(e2);
        return true;
    }

    @Override // java.util.Deque
    public boolean offerLast(E e2) {
        addLast(e2);
        return true;
    }

    @Override // java.util.Deque
    public E removeFirst() {
        E ePollFirst = pollFirst();
        if (ePollFirst == null) {
            throw new NoSuchElementException();
        }
        return ePollFirst;
    }

    @Override // java.util.Deque
    public E removeLast() {
        E ePollLast = pollLast();
        if (ePollLast == null) {
            throw new NoSuchElementException();
        }
        return ePollLast;
    }

    @Override // java.util.Deque
    public E pollFirst() {
        int i2 = this.head;
        E e2 = (E) this.elements[i2];
        if (e2 == null) {
            return null;
        }
        this.elements[i2] = null;
        this.head = (i2 + 1) & (this.elements.length - 1);
        return e2;
    }

    @Override // java.util.Deque
    public E pollLast() {
        int length = (this.tail - 1) & (this.elements.length - 1);
        E e2 = (E) this.elements[length];
        if (e2 == null) {
            return null;
        }
        this.elements[length] = null;
        this.tail = length;
        return e2;
    }

    @Override // java.util.Deque
    public E getFirst() {
        E e2 = (E) this.elements[this.head];
        if (e2 == null) {
            throw new NoSuchElementException();
        }
        return e2;
    }

    @Override // java.util.Deque
    public E getLast() {
        E e2 = (E) this.elements[(this.tail - 1) & (this.elements.length - 1)];
        if (e2 == null) {
            throw new NoSuchElementException();
        }
        return e2;
    }

    @Override // java.util.Deque
    public E peekFirst() {
        return (E) this.elements[this.head];
    }

    @Override // java.util.Deque
    public E peekLast() {
        return (E) this.elements[(this.tail - 1) & (this.elements.length - 1)];
    }

    @Override // java.util.Deque
    public boolean removeFirstOccurrence(Object obj) {
        if (obj == null) {
            return false;
        }
        int length = this.elements.length - 1;
        int i2 = this.head;
        while (true) {
            int i3 = i2;
            Object obj2 = this.elements[i3];
            if (obj2 != null) {
                if (obj.equals(obj2)) {
                    delete(i3);
                    return true;
                }
                i2 = (i3 + 1) & length;
            } else {
                return false;
            }
        }
    }

    @Override // java.util.Deque
    public boolean removeLastOccurrence(Object obj) {
        if (obj == null) {
            return false;
        }
        int length = this.elements.length - 1;
        int i2 = this.tail;
        while (true) {
            int i3 = (i2 - 1) & length;
            Object obj2 = this.elements[i3];
            if (obj2 != null) {
                if (obj.equals(obj2)) {
                    delete(i3);
                    return true;
                }
                i2 = i3;
            } else {
                return false;
            }
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(E e2) {
        addLast(e2);
        return true;
    }

    @Override // java.util.Deque, java.util.Queue
    public boolean offer(E e2) {
        return offerLast(e2);
    }

    @Override // java.util.Deque, java.util.Queue
    public E remove() {
        return removeFirst();
    }

    @Override // java.util.Deque, java.util.Queue
    public E poll() {
        return pollFirst();
    }

    @Override // java.util.Deque, java.util.Queue
    public E element() {
        return getFirst();
    }

    @Override // java.util.Deque, java.util.Queue
    public E peek() {
        return peekFirst();
    }

    @Override // java.util.Deque
    public void push(E e2) {
        addFirst(e2);
    }

    @Override // java.util.Deque
    public E pop() {
        return removeFirst();
    }

    private void checkInvariants() {
        if (!$assertionsDisabled && this.elements[this.tail] != null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && (this.head != this.tail ? this.elements[this.head] == null || this.elements[(this.tail - 1) & (this.elements.length - 1)] == null : this.elements[this.head] != null)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && this.elements[(this.head - 1) & (this.elements.length - 1)] != null) {
            throw new AssertionError();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean delete(int i2) {
        checkInvariants();
        Object[] objArr = this.elements;
        int length = objArr.length - 1;
        int i3 = this.head;
        int i4 = this.tail;
        int i5 = (i2 - i3) & length;
        int i6 = (i4 - i2) & length;
        if (i5 >= ((i4 - i3) & length)) {
            throw new ConcurrentModificationException();
        }
        if (i5 < i6) {
            if (i3 <= i2) {
                System.arraycopy(objArr, i3, objArr, i3 + 1, i5);
            } else {
                System.arraycopy(objArr, 0, objArr, 1, i2);
                objArr[0] = objArr[length];
                System.arraycopy(objArr, i3, objArr, i3 + 1, length - i3);
            }
            objArr[i3] = null;
            this.head = (i3 + 1) & length;
            return false;
        }
        if (i2 < i4) {
            System.arraycopy(objArr, i2 + 1, objArr, i2, i6);
            this.tail = i4 - 1;
            return true;
        }
        System.arraycopy(objArr, i2 + 1, objArr, i2, length - i2);
        objArr[length] = objArr[0];
        System.arraycopy(objArr, 1, objArr, 0, i4);
        this.tail = (i4 - 1) & length;
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return (this.tail - this.head) & (this.elements.length - 1);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean isEmpty() {
        return this.head == this.tail;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<E> iterator() {
        return new DeqIterator();
    }

    @Override // java.util.Deque
    public Iterator<E> descendingIterator() {
        return new DescendingIterator();
    }

    /* loaded from: rt.jar:java/util/ArrayDeque$DeqIterator.class */
    private class DeqIterator implements Iterator<E> {
        private int cursor;
        private int fence;
        private int lastRet;

        private DeqIterator() {
            this.cursor = ArrayDeque.this.head;
            this.fence = ArrayDeque.this.tail;
            this.lastRet = -1;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.cursor != this.fence;
        }

        @Override // java.util.Iterator
        public E next() {
            if (this.cursor == this.fence) {
                throw new NoSuchElementException();
            }
            E e2 = (E) ArrayDeque.this.elements[this.cursor];
            if (ArrayDeque.this.tail != this.fence || e2 == null) {
                throw new ConcurrentModificationException();
            }
            this.lastRet = this.cursor;
            this.cursor = (this.cursor + 1) & (ArrayDeque.this.elements.length - 1);
            return e2;
        }

        @Override // java.util.Iterator
        public void remove() {
            if (this.lastRet >= 0) {
                if (ArrayDeque.this.delete(this.lastRet)) {
                    this.cursor = (this.cursor - 1) & (ArrayDeque.this.elements.length - 1);
                    this.fence = ArrayDeque.this.tail;
                }
                this.lastRet = -1;
                return;
            }
            throw new IllegalStateException();
        }

        @Override // java.util.Iterator
        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            Object[] objArr = ArrayDeque.this.elements;
            int length = objArr.length - 1;
            int i2 = this.fence;
            int i3 = this.cursor;
            this.cursor = i2;
            while (i3 != i2) {
                Object obj = objArr[i3];
                i3 = (i3 + 1) & length;
                if (obj == null) {
                    throw new ConcurrentModificationException();
                }
                consumer.accept(obj);
            }
        }
    }

    /* loaded from: rt.jar:java/util/ArrayDeque$DescendingIterator.class */
    private class DescendingIterator implements Iterator<E> {
        private int cursor;
        private int fence;
        private int lastRet;

        private DescendingIterator() {
            this.cursor = ArrayDeque.this.tail;
            this.fence = ArrayDeque.this.head;
            this.lastRet = -1;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.cursor != this.fence;
        }

        @Override // java.util.Iterator
        public E next() {
            if (this.cursor == this.fence) {
                throw new NoSuchElementException();
            }
            this.cursor = (this.cursor - 1) & (ArrayDeque.this.elements.length - 1);
            E e2 = (E) ArrayDeque.this.elements[this.cursor];
            if (ArrayDeque.this.head != this.fence || e2 == null) {
                throw new ConcurrentModificationException();
            }
            this.lastRet = this.cursor;
            return e2;
        }

        @Override // java.util.Iterator
        public void remove() {
            if (this.lastRet >= 0) {
                if (!ArrayDeque.this.delete(this.lastRet)) {
                    this.cursor = (this.cursor + 1) & (ArrayDeque.this.elements.length - 1);
                    this.fence = ArrayDeque.this.head;
                }
                this.lastRet = -1;
                return;
            }
            throw new IllegalStateException();
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        if (obj == null) {
            return false;
        }
        int length = this.elements.length - 1;
        int i2 = this.head;
        while (true) {
            int i3 = i2;
            Object obj2 = this.elements[i3];
            if (obj2 != null) {
                if (obj.equals(obj2)) {
                    return true;
                }
                i2 = (i3 + 1) & length;
            } else {
                return false;
            }
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object obj) {
        return removeFirstOccurrence(obj);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        int i2 = this.head;
        int i3 = this.tail;
        if (i2 != i3) {
            this.tail = 0;
            this.head = 0;
            int i4 = i2;
            int length = this.elements.length - 1;
            do {
                this.elements[i4] = null;
                i4 = (i4 + 1) & length;
            } while (i4 != i3);
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public Object[] toArray() {
        return copyElements(new Object[size()]);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v15, types: [java.lang.Object[]] */
    @Override // java.util.AbstractCollection, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        int size = size();
        if (tArr.length < size) {
            tArr = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), size);
        }
        copyElements(tArr);
        if (tArr.length > size) {
            tArr[size] = null;
        }
        return tArr;
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public ArrayDeque<E> m3367clone() {
        try {
            ArrayDeque<E> arrayDeque = (ArrayDeque) super.clone();
            arrayDeque.elements = Arrays.copyOf(this.elements, this.elements.length);
            return arrayDeque;
        } catch (CloneNotSupportedException e2) {
            throw new AssertionError();
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(size());
        int length = this.elements.length - 1;
        int i2 = this.head;
        while (true) {
            int i3 = i2;
            if (i3 != this.tail) {
                objectOutputStream.writeObject(this.elements[i3]);
                i2 = (i3 + 1) & length;
            } else {
                return;
            }
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        int i2 = objectInputStream.readInt();
        SharedSecrets.getJavaOISAccess().checkArray(objectInputStream, Object[].class, calculateSize(i2));
        allocateElements(i2);
        this.head = 0;
        this.tail = i2;
        for (int i3 = 0; i3 < i2; i3++) {
            this.elements[i3] = objectInputStream.readObject();
        }
    }

    @Override // java.util.Collection, java.lang.Iterable
    public Spliterator<E> spliterator() {
        return new DeqSpliterator(this, -1, -1);
    }

    /* loaded from: rt.jar:java/util/ArrayDeque$DeqSpliterator.class */
    static final class DeqSpliterator<E> implements Spliterator<E> {
        private final ArrayDeque<E> deq;
        private int fence;
        private int index;

        DeqSpliterator(ArrayDeque<E> arrayDeque, int i2, int i3) {
            this.deq = arrayDeque;
            this.index = i2;
            this.fence = i3;
        }

        private int getFence() {
            int i2 = this.fence;
            int i3 = i2;
            if (i2 < 0) {
                int i4 = this.deq.tail;
                this.fence = i4;
                i3 = i4;
                this.index = this.deq.head;
            }
            return i3;
        }

        @Override // java.util.Spliterator
        public DeqSpliterator<E> trySplit() {
            int fence = getFence();
            int i2 = this.index;
            int length = this.deq.elements.length;
            if (i2 != fence && ((i2 + 1) & (length - 1)) != fence) {
                if (i2 > fence) {
                    fence += length;
                }
                int i3 = ((i2 + fence) >>> 1) & (length - 1);
                ArrayDeque<E> arrayDeque = this.deq;
                this.index = i3;
                return new DeqSpliterator<>(arrayDeque, i2, i3);
            }
            return null;
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super E> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Object[] objArr = this.deq.elements;
            int length = objArr.length - 1;
            int fence = getFence();
            int i2 = this.index;
            this.index = fence;
            while (i2 != fence) {
                Object obj = objArr[i2];
                i2 = (i2 + 1) & length;
                if (obj == null) {
                    throw new ConcurrentModificationException();
                }
                consumer.accept(obj);
            }
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super E> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Object[] objArr = this.deq.elements;
            int length = objArr.length - 1;
            getFence();
            int i2 = this.index;
            if (i2 != this.fence) {
                Object obj = objArr[i2];
                this.index = (i2 + 1) & length;
                if (obj == null) {
                    throw new ConcurrentModificationException();
                }
                consumer.accept(obj);
                return true;
            }
            return false;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            int fence = getFence() - this.index;
            if (fence < 0) {
                fence += this.deq.elements.length;
            }
            return fence;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return 16720;
        }
    }
}
