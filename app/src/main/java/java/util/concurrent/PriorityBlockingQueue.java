package java.util.concurrent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.SortedSet;
import java.util.Spliterator;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import sun.misc.SharedSecrets;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/util/concurrent/PriorityBlockingQueue.class */
public class PriorityBlockingQueue<E> extends AbstractQueue<E> implements BlockingQueue<E>, Serializable {
    private static final long serialVersionUID = 5595510919245408276L;
    private static final int DEFAULT_INITIAL_CAPACITY = 11;
    private static final int MAX_ARRAY_SIZE = 2147483639;
    private transient Object[] queue;
    private transient int size;
    private transient Comparator<? super E> comparator;
    private final ReentrantLock lock;
    private final Condition notEmpty;
    private volatile transient int allocationSpinLock;

    /* renamed from: q, reason: collision with root package name */
    private PriorityQueue<E> f12589q;
    private static final Unsafe UNSAFE;
    private static final long allocationSpinLockOffset;

    public PriorityBlockingQueue() {
        this(11, null);
    }

    public PriorityBlockingQueue(int i2) {
        this(i2, null);
    }

    public PriorityBlockingQueue(int i2, Comparator<? super E> comparator) {
        if (i2 < 1) {
            throw new IllegalArgumentException();
        }
        this.lock = new ReentrantLock();
        this.notEmpty = this.lock.newCondition();
        this.comparator = comparator;
        this.queue = new Object[i2];
    }

    public PriorityBlockingQueue(Collection<? extends E> collection) {
        this.lock = new ReentrantLock();
        this.notEmpty = this.lock.newCondition();
        boolean z2 = true;
        boolean z3 = true;
        if (collection instanceof SortedSet) {
            this.comparator = ((SortedSet) collection).comparator();
            z2 = false;
        } else if (collection instanceof PriorityBlockingQueue) {
            PriorityBlockingQueue priorityBlockingQueue = (PriorityBlockingQueue) collection;
            this.comparator = priorityBlockingQueue.comparator();
            z3 = false;
            if (priorityBlockingQueue.getClass() == PriorityBlockingQueue.class) {
                z2 = false;
            }
        }
        Object[] array = collection.toArray();
        int length = array.length;
        array = collection.getClass() != ArrayList.class ? Arrays.copyOf(array, length, Object[].class) : array;
        if (z3 && (length == 1 || this.comparator != null)) {
            for (int i2 = 0; i2 < length; i2++) {
                if (array[i2] == null) {
                    throw new NullPointerException();
                }
            }
        }
        this.queue = array;
        this.size = length;
        if (z2) {
            heapify();
        }
    }

    private void tryGrow(Object[] objArr, int i2) {
        this.lock.unlock();
        Object[] objArr2 = null;
        if (this.allocationSpinLock == 0 && UNSAFE.compareAndSwapInt(this, allocationSpinLockOffset, 0, 1)) {
            try {
                int i3 = i2 + (i2 < 64 ? i2 + 2 : i2 >> 1);
                if (i3 - MAX_ARRAY_SIZE > 0) {
                    int i4 = i2 + 1;
                    if (i4 < 0 || i4 > MAX_ARRAY_SIZE) {
                        throw new OutOfMemoryError();
                    }
                    i3 = MAX_ARRAY_SIZE;
                }
                if (i3 > i2 && this.queue == objArr) {
                    objArr2 = new Object[i3];
                }
            } finally {
                this.allocationSpinLock = 0;
            }
        }
        if (objArr2 == null) {
            Thread.yield();
        }
        this.lock.lock();
        if (objArr2 != null && this.queue == objArr) {
            this.queue = objArr2;
            System.arraycopy(objArr, 0, objArr2, 0, i2);
        }
    }

    private E dequeue() {
        int i2 = this.size - 1;
        if (i2 < 0) {
            return null;
        }
        Object[] objArr = this.queue;
        E e2 = (E) objArr[0];
        Object obj = objArr[i2];
        objArr[i2] = null;
        Comparator<? super E> comparator = this.comparator;
        if (comparator == null) {
            siftDownComparable(0, obj, objArr, i2);
        } else {
            siftDownUsingComparator(0, obj, objArr, i2, comparator);
        }
        this.size = i2;
        return e2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static <T> void siftUpComparable(int i2, T t2, Object[] objArr) {
        Comparable comparable = (Comparable) t2;
        while (i2 > 0) {
            int i3 = (i2 - 1) >>> 1;
            Object[] objArr2 = objArr[i3];
            if (comparable.compareTo(objArr2) >= 0) {
                break;
            }
            objArr[i2] = objArr2;
            i2 = i3;
        }
        objArr[i2] = comparable;
    }

    private static <T> void siftUpUsingComparator(int i2, T t2, Object[] objArr, Comparator<? super T> comparator) {
        while (i2 > 0) {
            int i3 = (i2 - 1) >>> 1;
            Object obj = objArr[i3];
            if (comparator.compare(t2, obj) >= 0) {
                break;
            }
            objArr[i2] = obj;
            i2 = i3;
        }
        objArr[i2] = t2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v11 */
    /* JADX WARN: Type inference failed for: r0v20, types: [java.lang.Comparable] */
    /* JADX WARN: Type inference failed for: r0v23 */
    /* JADX WARN: Type inference failed for: r1v11, types: [java.lang.Object] */
    private static <T> void siftDownComparable(int i2, T t2, Object[] objArr, int i3) {
        if (i3 > 0) {
            Comparable comparable = (Comparable) t2;
            int i4 = i3 >>> 1;
            while (i2 < i4) {
                int i5 = (i2 << 1) + 1;
                T t3 = objArr[i5];
                int i6 = i5 + 1;
                if (i6 < i3 && ((Comparable) t3).compareTo(objArr[i6]) > 0) {
                    i5 = i6;
                    t3 = objArr[i6];
                }
                if (comparable.compareTo(t3) <= 0) {
                    break;
                }
                objArr[i2] = t3;
                i2 = i5;
            }
            objArr[i2] = comparable;
        }
    }

    private static <T> void siftDownUsingComparator(int i2, T t2, Object[] objArr, int i3, Comparator<? super T> comparator) {
        if (i3 > 0) {
            int i4 = i3 >>> 1;
            while (i2 < i4) {
                int i5 = (i2 << 1) + 1;
                Object obj = objArr[i5];
                int i6 = i5 + 1;
                if (i6 < i3 && comparator.compare(obj, objArr[i6]) > 0) {
                    i5 = i6;
                    obj = objArr[i6];
                }
                if (comparator.compare(t2, obj) <= 0) {
                    break;
                }
                objArr[i2] = obj;
                i2 = i5;
            }
            objArr[i2] = t2;
        }
    }

    private void heapify() {
        Object[] objArr = this.queue;
        int i2 = this.size;
        int i3 = (i2 >>> 1) - 1;
        Comparator<? super E> comparator = this.comparator;
        if (comparator == null) {
            for (int i4 = i3; i4 >= 0; i4--) {
                siftDownComparable(i4, objArr[i4], objArr, i2);
            }
            return;
        }
        for (int i5 = i3; i5 >= 0; i5--) {
            siftDownUsingComparator(i5, objArr[i5], objArr, i2, comparator);
        }
    }

    @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(E e2) {
        return offer(e2);
    }

    @Override // java.util.Queue
    public boolean offer(E e2) {
        int i2;
        Object[] objArr;
        if (e2 == null) {
            throw new NullPointerException();
        }
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        while (true) {
            i2 = this.size;
            objArr = this.queue;
            int length = objArr.length;
            if (i2 >= length) {
                tryGrow(objArr, length);
            } else {
                try {
                    break;
                } catch (Throwable th) {
                    reentrantLock.unlock();
                    throw th;
                }
            }
        }
        Comparator<? super E> comparator = this.comparator;
        if (comparator == null) {
            siftUpComparable(i2, e2, objArr);
        } else {
            siftUpUsingComparator(i2, e2, objArr, comparator);
        }
        this.size = i2 + 1;
        this.notEmpty.signal();
        reentrantLock.unlock();
        return true;
    }

    @Override // java.util.concurrent.BlockingQueue
    public void put(E e2) {
        offer(e2);
    }

    @Override // java.util.concurrent.BlockingQueue
    public boolean offer(E e2, long j2, TimeUnit timeUnit) {
        return offer(e2);
    }

    @Override // java.util.Queue
    public E poll() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return dequeue();
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.concurrent.BlockingQueue
    /* renamed from: take */
    public E take2() throws InterruptedException {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lockInterruptibly();
        while (true) {
            try {
                E eDequeue = dequeue();
                if (eDequeue == null) {
                    this.notEmpty.await();
                } else {
                    return eDequeue;
                }
            } finally {
                reentrantLock.unlock();
            }
        }
    }

    @Override // java.util.concurrent.BlockingQueue
    /* renamed from: poll */
    public E poll2(long j2, TimeUnit timeUnit) throws InterruptedException {
        E eDequeue;
        long nanos = timeUnit.toNanos(j2);
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lockInterruptibly();
        while (true) {
            try {
                eDequeue = dequeue();
                if (eDequeue != null || nanos <= 0) {
                    break;
                }
                nanos = this.notEmpty.awaitNanos(nanos);
            } finally {
                reentrantLock.unlock();
            }
        }
        return eDequeue;
    }

    @Override // java.util.Queue
    public E peek() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return (E) (this.size == 0 ? null : this.queue[0]);
        } finally {
            reentrantLock.unlock();
        }
    }

    public Comparator<? super E> comparator() {
        return this.comparator;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return this.size;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.concurrent.BlockingQueue
    public int remainingCapacity() {
        return Integer.MAX_VALUE;
    }

    private int indexOf(Object obj) {
        if (obj != null) {
            Object[] objArr = this.queue;
            int i2 = this.size;
            for (int i3 = 0; i3 < i2; i3++) {
                if (obj.equals(objArr[i3])) {
                    return i3;
                }
            }
            return -1;
        }
        return -1;
    }

    private void removeAt(int i2) {
        Object[] objArr = this.queue;
        int i3 = this.size - 1;
        if (i3 == i2) {
            objArr[i2] = null;
        } else {
            Object obj = objArr[i3];
            objArr[i3] = null;
            Comparator<? super E> comparator = this.comparator;
            if (comparator == null) {
                siftDownComparable(i2, obj, objArr, i3);
            } else {
                siftDownUsingComparator(i2, obj, objArr, i3, comparator);
            }
            if (objArr[i2] == obj) {
                if (comparator == null) {
                    siftUpComparable(i2, obj, objArr);
                } else {
                    siftUpUsingComparator(i2, obj, objArr, comparator);
                }
            }
        }
        this.size = i3;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object obj) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            int iIndexOf = indexOf(obj);
            if (iIndexOf == -1) {
                return false;
            }
            removeAt(iIndexOf);
            reentrantLock.unlock();
            return true;
        } finally {
            reentrantLock.unlock();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x0026, code lost:
    
        removeAt(r8);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void removeEQ(java.lang.Object r5) {
        /*
            r4 = this;
            r0 = r4
            java.util.concurrent.locks.ReentrantLock r0 = r0.lock
            r6 = r0
            r0 = r6
            r0.lock()
            r0 = r4
            java.lang.Object[] r0 = r0.queue     // Catch: java.lang.Throwable -> L3c
            r7 = r0
            r0 = 0
            r8 = r0
            r0 = r4
            int r0 = r0.size     // Catch: java.lang.Throwable -> L3c
            r9 = r0
        L17:
            r0 = r8
            r1 = r9
            if (r0 >= r1) goto L35
            r0 = r5
            r1 = r7
            r2 = r8
            r1 = r1[r2]     // Catch: java.lang.Throwable -> L3c
            if (r0 != r1) goto L2f
            r0 = r4
            r1 = r8
            r0.removeAt(r1)     // Catch: java.lang.Throwable -> L3c
            goto L35
        L2f:
            int r8 = r8 + 1
            goto L17
        L35:
            r0 = r6
            r0.unlock()
            goto L45
        L3c:
            r10 = move-exception
            r0 = r6
            r0.unlock()
            r0 = r10
            throw r0
        L45:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.PriorityBlockingQueue.removeEQ(java.lang.Object):void");
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return indexOf(obj) != -1;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public Object[] toArray() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return Arrays.copyOf(this.queue, this.size);
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.AbstractCollection
    public String toString() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            int i2 = this.size;
            if (i2 == 0) {
                return "[]";
            }
            StringBuilder sb = new StringBuilder();
            sb.append('[');
            for (int i3 = 0; i3 < i2; i3++) {
                Object obj = this.queue[i3];
                sb.append(obj == this ? "(this Collection)" : obj);
                if (i3 != i2 - 1) {
                    sb.append(',').append(' ');
                }
            }
            String string = sb.append(']').toString();
            reentrantLock.unlock();
            return string;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.concurrent.BlockingQueue
    public int drainTo(Collection<? super E> collection) {
        return drainTo(collection, Integer.MAX_VALUE);
    }

    @Override // java.util.concurrent.BlockingQueue
    public int drainTo(Collection<? super E> collection, int i2) {
        if (collection == null) {
            throw new NullPointerException();
        }
        if (collection == this) {
            throw new IllegalArgumentException();
        }
        if (i2 <= 0) {
            return 0;
        }
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            int iMin = Math.min(this.size, i2);
            for (int i3 = 0; i3 < iMin; i3++) {
                collection.add(this.queue[0]);
                dequeue();
            }
            return iMin;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Object[] objArr = this.queue;
            int i2 = this.size;
            this.size = 0;
            for (int i3 = 0; i3 < i2; i3++) {
                objArr[i3] = null;
            }
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            int i2 = this.size;
            if (tArr.length < i2) {
                T[] tArr2 = (T[]) Arrays.copyOf(this.queue, this.size, tArr.getClass());
                reentrantLock.unlock();
                return tArr2;
            }
            System.arraycopy(this.queue, 0, tArr, 0, i2);
            if (tArr.length > i2) {
                tArr[i2] = null;
            }
            return tArr;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<E> iterator() {
        return new Itr(toArray());
    }

    /* loaded from: rt.jar:java/util/concurrent/PriorityBlockingQueue$Itr.class */
    final class Itr implements Iterator<E> {
        final Object[] array;
        int cursor;
        int lastRet = -1;

        Itr(Object[] objArr) {
            this.array = objArr;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.cursor < this.array.length;
        }

        @Override // java.util.Iterator
        public E next() {
            if (this.cursor >= this.array.length) {
                throw new NoSuchElementException();
            }
            this.lastRet = this.cursor;
            Object[] objArr = this.array;
            int i2 = this.cursor;
            this.cursor = i2 + 1;
            return (E) objArr[i2];
        }

        @Override // java.util.Iterator
        public void remove() {
            if (this.lastRet < 0) {
                throw new IllegalStateException();
            }
            PriorityBlockingQueue.this.removeEQ(this.array[this.lastRet]);
            this.lastRet = -1;
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        this.lock.lock();
        try {
            this.f12589q = new PriorityQueue<>(Math.max(this.size, 1), this.comparator);
            this.f12589q.addAll(this);
            objectOutputStream.defaultWriteObject();
        } finally {
            this.f12589q = null;
            this.lock.unlock();
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        try {
            objectInputStream.defaultReadObject();
            int size = this.f12589q.size();
            SharedSecrets.getJavaOISAccess().checkArray(objectInputStream, Object[].class, size);
            this.queue = new Object[size];
            this.comparator = this.f12589q.comparator();
            addAll(this.f12589q);
        } finally {
            this.f12589q = null;
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/PriorityBlockingQueue$PBQSpliterator.class */
    static final class PBQSpliterator<E> implements Spliterator<E> {
        final PriorityBlockingQueue<E> queue;
        Object[] array;
        int index;
        int fence;

        PBQSpliterator(PriorityBlockingQueue<E> priorityBlockingQueue, Object[] objArr, int i2, int i3) {
            this.queue = priorityBlockingQueue;
            this.array = objArr;
            this.index = i2;
            this.fence = i3;
        }

        final int getFence() {
            int i2 = this.fence;
            int i3 = i2;
            if (i2 < 0) {
                Object[] array = this.queue.toArray();
                this.array = array;
                int length = array.length;
                this.fence = length;
                i3 = length;
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
            PriorityBlockingQueue<E> priorityBlockingQueue = this.queue;
            Object[] objArr = this.array;
            this.index = i3;
            return new PBQSpliterator(priorityBlockingQueue, objArr, i2, i3);
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super E> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            Object[] objArr = this.array;
            Object[] objArr2 = objArr;
            if (objArr == null) {
                Object[] array = this.queue.toArray();
                objArr2 = array;
                this.fence = array.length;
            }
            int i2 = this.fence;
            if (i2 <= objArr2.length) {
                int i3 = this.index;
                int i4 = i3;
                if (i3 >= 0) {
                    this.index = i2;
                    if (i4 < i2) {
                        do {
                            consumer.accept(objArr2[i4]);
                            i4++;
                        } while (i4 < i2);
                    }
                }
            }
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super E> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            if (getFence() > this.index && this.index >= 0) {
                Object[] objArr = this.array;
                int i2 = this.index;
                this.index = i2 + 1;
                consumer.accept(objArr[i2]);
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

    @Override // java.util.Collection, java.lang.Iterable
    public Spliterator<E> spliterator() {
        return new PBQSpliterator(this, null, 0, -1);
    }

    static {
        try {
            UNSAFE = Unsafe.getUnsafe();
            allocationSpinLockOffset = UNSAFE.objectFieldOffset(PriorityBlockingQueue.class.getDeclaredField("allocationSpinLock"));
        } catch (Exception e2) {
            throw new Error(e2);
        }
    }
}
