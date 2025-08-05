package java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.function.Consumer;
import sun.misc.SharedSecrets;

/* loaded from: rt.jar:java/util/PriorityQueue.class */
public class PriorityQueue<E> extends AbstractQueue<E> implements Serializable {
    private static final long serialVersionUID = -7720805057305804111L;
    private static final int DEFAULT_INITIAL_CAPACITY = 11;
    transient Object[] queue;
    private int size;
    private final Comparator<? super E> comparator;
    transient int modCount;
    private static final int MAX_ARRAY_SIZE = 2147483639;

    public PriorityQueue() {
        this(11, null);
    }

    public PriorityQueue(int i2) {
        this(i2, null);
    }

    public PriorityQueue(Comparator<? super E> comparator) {
        this(11, comparator);
    }

    public PriorityQueue(int i2, Comparator<? super E> comparator) {
        this.size = 0;
        this.modCount = 0;
        if (i2 < 1) {
            throw new IllegalArgumentException();
        }
        this.queue = new Object[i2];
        this.comparator = comparator;
    }

    public PriorityQueue(Collection<? extends E> collection) {
        this.size = 0;
        this.modCount = 0;
        if (collection instanceof SortedSet) {
            SortedSet sortedSet = (SortedSet) collection;
            this.comparator = sortedSet.comparator();
            initElementsFromCollection(sortedSet);
        } else if (collection instanceof PriorityQueue) {
            PriorityQueue<? extends E> priorityQueue = (PriorityQueue) collection;
            this.comparator = priorityQueue.comparator();
            initFromPriorityQueue(priorityQueue);
        } else {
            this.comparator = null;
            initFromCollection(collection);
        }
    }

    public PriorityQueue(PriorityQueue<? extends E> priorityQueue) {
        this.size = 0;
        this.modCount = 0;
        this.comparator = priorityQueue.comparator();
        initFromPriorityQueue(priorityQueue);
    }

    public PriorityQueue(SortedSet<? extends E> sortedSet) {
        this.size = 0;
        this.modCount = 0;
        this.comparator = sortedSet.comparator();
        initElementsFromCollection(sortedSet);
    }

    private void initFromPriorityQueue(PriorityQueue<? extends E> priorityQueue) {
        if (priorityQueue.getClass() == PriorityQueue.class) {
            this.queue = priorityQueue.toArray();
            this.size = priorityQueue.size();
        } else {
            initFromCollection(priorityQueue);
        }
    }

    private void initElementsFromCollection(Collection<? extends E> collection) {
        Object[] array = collection.toArray();
        if (collection.getClass() != ArrayList.class) {
            array = Arrays.copyOf(array, array.length, Object[].class);
        }
        if (array.length == 1 || this.comparator != null) {
            for (Object obj : array) {
                if (obj == null) {
                    throw new NullPointerException();
                }
            }
        }
        this.queue = array;
        this.size = array.length;
    }

    private void initFromCollection(Collection<? extends E> collection) {
        initElementsFromCollection(collection);
        heapify();
    }

    private void grow(int i2) {
        int length = this.queue.length;
        int iHugeCapacity = length + (length < 64 ? length + 2 : length >> 1);
        if (iHugeCapacity - MAX_ARRAY_SIZE > 0) {
            iHugeCapacity = hugeCapacity(i2);
        }
        this.queue = Arrays.copyOf(this.queue, iHugeCapacity);
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

    @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(E e2) {
        return offer(e2);
    }

    @Override // java.util.Queue
    public boolean offer(E e2) {
        if (e2 == null) {
            throw new NullPointerException();
        }
        this.modCount++;
        int i2 = this.size;
        if (i2 >= this.queue.length) {
            grow(i2 + 1);
        }
        this.size = i2 + 1;
        if (i2 == 0) {
            this.queue[0] = e2;
            return true;
        }
        siftUp(i2, e2);
        return true;
    }

    @Override // java.util.Queue
    public E peek() {
        if (this.size == 0) {
            return null;
        }
        return (E) this.queue[0];
    }

    private int indexOf(Object obj) {
        if (obj != null) {
            for (int i2 = 0; i2 < this.size; i2++) {
                if (obj.equals(this.queue[i2])) {
                    return i2;
                }
            }
            return -1;
        }
        return -1;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object obj) {
        int iIndexOf = indexOf(obj);
        if (iIndexOf == -1) {
            return false;
        }
        removeAt(iIndexOf);
        return true;
    }

    boolean removeEq(Object obj) {
        for (int i2 = 0; i2 < this.size; i2++) {
            if (obj == this.queue[i2]) {
                removeAt(i2);
                return true;
            }
        }
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        return indexOf(obj) != -1;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public Object[] toArray() {
        return Arrays.copyOf(this.queue, this.size);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        int i2 = this.size;
        if (tArr.length < i2) {
            return (T[]) Arrays.copyOf(this.queue, i2, tArr.getClass());
        }
        System.arraycopy(this.queue, 0, tArr, 0, i2);
        if (tArr.length > i2) {
            tArr[i2] = null;
        }
        return tArr;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<E> iterator() {
        return new Itr();
    }

    /* loaded from: rt.jar:java/util/PriorityQueue$Itr.class */
    private final class Itr implements Iterator<E> {
        private int cursor;
        private int lastRet;
        private ArrayDeque<E> forgetMeNot;
        private E lastRetElt;
        private int expectedModCount;

        private Itr() {
            this.cursor = 0;
            this.lastRet = -1;
            this.forgetMeNot = null;
            this.lastRetElt = null;
            this.expectedModCount = PriorityQueue.this.modCount;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.cursor < PriorityQueue.this.size || !(this.forgetMeNot == null || this.forgetMeNot.isEmpty());
        }

        @Override // java.util.Iterator
        public E next() {
            if (this.expectedModCount == PriorityQueue.this.modCount) {
                if (this.cursor < PriorityQueue.this.size) {
                    Object[] objArr = PriorityQueue.this.queue;
                    int i2 = this.cursor;
                    this.cursor = i2 + 1;
                    this.lastRet = i2;
                    return (E) objArr[i2];
                }
                if (this.forgetMeNot != null) {
                    this.lastRet = -1;
                    this.lastRetElt = this.forgetMeNot.poll();
                    if (this.lastRetElt != null) {
                        return this.lastRetElt;
                    }
                }
                throw new NoSuchElementException();
            }
            throw new ConcurrentModificationException();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Iterator
        public void remove() {
            if (this.expectedModCount != PriorityQueue.this.modCount) {
                throw new ConcurrentModificationException();
            }
            if (this.lastRet != -1) {
                Object objRemoveAt = PriorityQueue.this.removeAt(this.lastRet);
                this.lastRet = -1;
                if (objRemoveAt == null) {
                    this.cursor--;
                } else {
                    if (this.forgetMeNot == null) {
                        this.forgetMeNot = new ArrayDeque<>();
                    }
                    this.forgetMeNot.add(objRemoveAt);
                }
            } else if (this.lastRetElt != null) {
                PriorityQueue.this.removeEq(this.lastRetElt);
                this.lastRetElt = null;
            } else {
                throw new IllegalStateException();
            }
            this.expectedModCount = PriorityQueue.this.modCount;
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.size;
    }

    @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        this.modCount++;
        for (int i2 = 0; i2 < this.size; i2++) {
            this.queue[i2] = null;
        }
        this.size = 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Queue
    public E poll() {
        if (this.size == 0) {
            return null;
        }
        int i2 = this.size - 1;
        this.size = i2;
        this.modCount++;
        E e2 = (E) this.queue[0];
        Object obj = this.queue[i2];
        this.queue[i2] = null;
        if (i2 != 0) {
            siftDown(0, obj);
        }
        return e2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public E removeAt(int i2) {
        this.modCount++;
        int i3 = this.size - 1;
        this.size = i3;
        if (i3 == i2) {
            this.queue[i2] = null;
            return null;
        }
        E e2 = (E) this.queue[i3];
        this.queue[i3] = null;
        siftDown(i2, e2);
        if (this.queue[i2] == e2) {
            siftUp(i2, e2);
            if (this.queue[i2] != e2) {
                return e2;
            }
            return null;
        }
        return null;
    }

    private void siftUp(int i2, E e2) {
        if (this.comparator != null) {
            siftUpUsingComparator(i2, e2);
        } else {
            siftUpComparable(i2, e2);
        }
    }

    private void siftUpComparable(int i2, E e2) {
        Comparable comparable = (Comparable) e2;
        while (i2 > 0) {
            int i3 = (i2 - 1) >>> 1;
            Object obj = this.queue[i3];
            if (comparable.compareTo(obj) >= 0) {
                break;
            }
            this.queue[i2] = obj;
            i2 = i3;
        }
        this.queue[i2] = comparable;
    }

    private void siftUpUsingComparator(int i2, E e2) {
        while (i2 > 0) {
            int i3 = (i2 - 1) >>> 1;
            Object obj = this.queue[i3];
            if (this.comparator.compare(e2, obj) >= 0) {
                break;
            }
            this.queue[i2] = obj;
            i2 = i3;
        }
        this.queue[i2] = e2;
    }

    private void siftDown(int i2, E e2) {
        if (this.comparator != null) {
            siftDownUsingComparator(i2, e2);
        } else {
            siftDownComparable(i2, e2);
        }
    }

    private void siftDownComparable(int i2, E e2) {
        Comparable comparable = (Comparable) e2;
        int i3 = this.size >>> 1;
        while (i2 < i3) {
            int i4 = (i2 << 1) + 1;
            Object obj = this.queue[i4];
            int i5 = i4 + 1;
            if (i5 < this.size && ((Comparable) obj).compareTo(this.queue[i5]) > 0) {
                i4 = i5;
                obj = this.queue[i5];
            }
            if (comparable.compareTo(obj) <= 0) {
                break;
            }
            this.queue[i2] = obj;
            i2 = i4;
        }
        this.queue[i2] = comparable;
    }

    private void siftDownUsingComparator(int i2, E e2) {
        int i3 = this.size >>> 1;
        while (i2 < i3) {
            int i4 = (i2 << 1) + 1;
            Object obj = this.queue[i4];
            int i5 = i4 + 1;
            if (i5 < this.size && this.comparator.compare(obj, this.queue[i5]) > 0) {
                i4 = i5;
                obj = this.queue[i5];
            }
            if (this.comparator.compare(e2, obj) <= 0) {
                break;
            }
            this.queue[i2] = obj;
            i2 = i4;
        }
        this.queue[i2] = e2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void heapify() {
        for (int i2 = (this.size >>> 1) - 1; i2 >= 0; i2--) {
            siftDown(i2, this.queue[i2]);
        }
    }

    public Comparator<? super E> comparator() {
        return this.comparator;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(Math.max(2, this.size + 1));
        for (int i2 = 0; i2 < this.size; i2++) {
            objectOutputStream.writeObject(this.queue[i2]);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        objectInputStream.readInt();
        SharedSecrets.getJavaOISAccess().checkArray(objectInputStream, Object[].class, this.size);
        this.queue = new Object[this.size];
        for (int i2 = 0; i2 < this.size; i2++) {
            this.queue[i2] = objectInputStream.readObject();
        }
        heapify();
    }

    @Override // java.util.Collection, java.lang.Iterable
    public final Spliterator<E> spliterator() {
        return new PriorityQueueSpliterator(this, 0, -1, 0);
    }

    /* loaded from: rt.jar:java/util/PriorityQueue$PriorityQueueSpliterator.class */
    static final class PriorityQueueSpliterator<E> implements Spliterator<E> {
        private final PriorityQueue<E> pq;
        private int index;
        private int fence;
        private int expectedModCount;

        PriorityQueueSpliterator(PriorityQueue<E> priorityQueue, int i2, int i3, int i4) {
            this.pq = priorityQueue;
            this.index = i2;
            this.fence = i3;
            this.expectedModCount = i4;
        }

        private int getFence() {
            int i2 = this.fence;
            int i3 = i2;
            if (i2 < 0) {
                this.expectedModCount = this.pq.modCount;
                int i4 = ((PriorityQueue) this.pq).size;
                this.fence = i4;
                i3 = i4;
            }
            return i3;
        }

        @Override // java.util.Spliterator
        public PriorityQueueSpliterator<E> trySplit() {
            int fence = getFence();
            int i2 = this.index;
            int i3 = (i2 + fence) >>> 1;
            if (i2 >= i3) {
                return null;
            }
            PriorityQueue<E> priorityQueue = this.pq;
            this.index = i3;
            return new PriorityQueueSpliterator<>(priorityQueue, i2, i3, this.expectedModCount);
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super E> consumer) {
            Object[] objArr;
            int i2;
            if (consumer == null) {
                throw new NullPointerException();
            }
            PriorityQueue<E> priorityQueue = this.pq;
            if (priorityQueue != null && (objArr = priorityQueue.queue) != null) {
                int i3 = this.fence;
                int i4 = i3;
                if (i3 < 0) {
                    i2 = priorityQueue.modCount;
                    i4 = ((PriorityQueue) priorityQueue).size;
                } else {
                    i2 = this.expectedModCount;
                }
                int i5 = this.index;
                int i6 = i5;
                if (i5 >= 0) {
                    int i7 = i4;
                    this.index = i7;
                    if (i7 <= objArr.length) {
                        while (true) {
                            if (i6 < i4) {
                                Object obj = objArr[i6];
                                if (obj == null) {
                                    break;
                                }
                                consumer.accept(obj);
                                i6++;
                            } else if (priorityQueue.modCount == i2) {
                                return;
                            }
                        }
                    }
                }
            }
            throw new ConcurrentModificationException();
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super E> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            int fence = getFence();
            int i2 = this.index;
            if (i2 >= 0 && i2 < fence) {
                this.index = i2 + 1;
                Object obj = this.pq.queue[i2];
                if (obj == null) {
                    throw new ConcurrentModificationException();
                }
                consumer.accept(obj);
                if (this.pq.modCount != this.expectedModCount) {
                    throw new ConcurrentModificationException();
                }
                return true;
            }
            return false;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return getFence() - this.index;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return 16704;
        }
    }
}
