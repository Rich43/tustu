package java.util.concurrent;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: rt.jar:java/util/concurrent/ArrayBlockingQueue.class */
public class ArrayBlockingQueue<E> extends AbstractQueue<E> implements BlockingQueue<E>, Serializable {
    private static final long serialVersionUID = -817911632652898426L;
    final Object[] items;
    int takeIndex;
    int putIndex;
    int count;
    final ReentrantLock lock;
    private final Condition notEmpty;
    private final Condition notFull;
    transient ArrayBlockingQueue<E>.Itrs itrs;

    final int dec(int i2) {
        return (i2 == 0 ? this.items.length : i2) - 1;
    }

    final E itemAt(int i2) {
        return (E) this.items[i2];
    }

    private static void checkNotNull(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
    }

    private void enqueue(E e2) {
        Object[] objArr = this.items;
        objArr[this.putIndex] = e2;
        int i2 = this.putIndex + 1;
        this.putIndex = i2;
        if (i2 == objArr.length) {
            this.putIndex = 0;
        }
        this.count++;
        this.notEmpty.signal();
    }

    private E dequeue() {
        Object[] objArr = this.items;
        E e2 = (E) objArr[this.takeIndex];
        objArr[this.takeIndex] = null;
        int i2 = this.takeIndex + 1;
        this.takeIndex = i2;
        if (i2 == objArr.length) {
            this.takeIndex = 0;
        }
        this.count--;
        if (this.itrs != null) {
            this.itrs.elementDequeued();
        }
        this.notFull.signal();
        return e2;
    }

    void removeAt(int i2) {
        int i3;
        Object[] objArr = this.items;
        if (i2 == this.takeIndex) {
            objArr[this.takeIndex] = null;
            int i4 = this.takeIndex + 1;
            this.takeIndex = i4;
            if (i4 == objArr.length) {
                this.takeIndex = 0;
            }
            this.count--;
            if (this.itrs != null) {
                this.itrs.elementDequeued();
            }
        } else {
            int i5 = this.putIndex;
            int i6 = i2;
            while (true) {
                i3 = i6;
                int i7 = i3 + 1;
                if (i7 == objArr.length) {
                    i7 = 0;
                }
                if (i7 == i5) {
                    break;
                }
                objArr[i3] = objArr[i7];
                i6 = i7;
            }
            objArr[i3] = null;
            this.putIndex = i3;
            this.count--;
            if (this.itrs != null) {
                this.itrs.removedAt(i2);
            }
        }
        this.notFull.signal();
    }

    public ArrayBlockingQueue(int i2) {
        this(i2, false);
    }

    public ArrayBlockingQueue(int i2, boolean z2) {
        this.itrs = null;
        if (i2 <= 0) {
            throw new IllegalArgumentException();
        }
        this.items = new Object[i2];
        this.lock = new ReentrantLock(z2);
        this.notEmpty = this.lock.newCondition();
        this.notFull = this.lock.newCondition();
    }

    public ArrayBlockingQueue(int i2, boolean z2, Collection<? extends E> collection) {
        this(i2, z2);
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        int i3 = 0;
        try {
            try {
                for (E e2 : collection) {
                    checkNotNull(e2);
                    int i4 = i3;
                    i3++;
                    this.items[i4] = e2;
                }
                this.count = i3;
                this.putIndex = i3 == i2 ? 0 : i3;
                reentrantLock.unlock();
            } catch (ArrayIndexOutOfBoundsException e3) {
                throw new IllegalArgumentException();
            }
        } catch (Throwable th) {
            reentrantLock.unlock();
            throw th;
        }
    }

    @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(E e2) {
        return super.add(e2);
    }

    @Override // java.util.Queue
    public boolean offer(E e2) {
        checkNotNull(e2);
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            if (this.count == this.items.length) {
                return false;
            }
            enqueue(e2);
            reentrantLock.unlock();
            return true;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.concurrent.BlockingQueue
    public void put(E e2) throws InterruptedException {
        checkNotNull(e2);
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lockInterruptibly();
        while (this.count == this.items.length) {
            try {
                this.notFull.await();
            } finally {
                reentrantLock.unlock();
            }
        }
        enqueue(e2);
    }

    @Override // java.util.concurrent.BlockingQueue
    public boolean offer(E e2, long j2, TimeUnit timeUnit) throws InterruptedException {
        checkNotNull(e2);
        long nanos = timeUnit.toNanos(j2);
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lockInterruptibly();
        while (this.count == this.items.length) {
            try {
                if (nanos <= 0) {
                    return false;
                }
                nanos = this.notFull.awaitNanos(nanos);
            } finally {
                reentrantLock.unlock();
            }
        }
        enqueue(e2);
        reentrantLock.unlock();
        return true;
    }

    @Override // java.util.Queue
    public E poll() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return this.count == 0 ? null : dequeue();
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.concurrent.BlockingQueue
    /* renamed from: take */
    public E take2() throws InterruptedException {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lockInterruptibly();
        while (this.count == 0) {
            try {
                this.notEmpty.await();
            } finally {
                reentrantLock.unlock();
            }
        }
        return dequeue();
    }

    @Override // java.util.concurrent.BlockingQueue
    /* renamed from: poll */
    public E poll2(long j2, TimeUnit timeUnit) throws InterruptedException {
        long nanos = timeUnit.toNanos(j2);
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lockInterruptibly();
        while (this.count == 0) {
            try {
                if (nanos <= 0) {
                    return null;
                }
                nanos = this.notEmpty.awaitNanos(nanos);
            } finally {
                reentrantLock.unlock();
            }
        }
        E eDequeue = dequeue();
        reentrantLock.unlock();
        return eDequeue;
    }

    @Override // java.util.Queue
    public E peek() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return itemAt(this.takeIndex);
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return this.count;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.concurrent.BlockingQueue
    public int remainingCapacity() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return this.items.length - this.count;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object obj) {
        if (obj == null) {
            return false;
        }
        Object[] objArr = this.items;
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            if (this.count > 0) {
                int i2 = this.putIndex;
                int i3 = this.takeIndex;
                while (!obj.equals(objArr[i3])) {
                    i3++;
                    if (i3 == objArr.length) {
                        i3 = 0;
                    }
                    if (i3 == i2) {
                    }
                }
                removeAt(i3);
                reentrantLock.unlock();
                return true;
            }
            return false;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        if (obj == null) {
            return false;
        }
        Object[] objArr = this.items;
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            if (this.count > 0) {
                int i2 = this.putIndex;
                int i3 = this.takeIndex;
                while (!obj.equals(objArr[i3])) {
                    i3++;
                    if (i3 == objArr.length) {
                        i3 = 0;
                    }
                    if (i3 == i2) {
                    }
                }
                return true;
            }
            reentrantLock.unlock();
            return false;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public Object[] toArray() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            int i2 = this.count;
            Object[] objArr = new Object[i2];
            int length = this.items.length - this.takeIndex;
            if (i2 <= length) {
                System.arraycopy(this.items, this.takeIndex, objArr, 0, i2);
            } else {
                System.arraycopy(this.items, this.takeIndex, objArr, 0, length);
                System.arraycopy(this.items, 0, objArr, length, i2 - length);
            }
            return objArr;
        } finally {
            reentrantLock.unlock();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v28, types: [java.lang.Object[]] */
    @Override // java.util.AbstractCollection, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        Object[] objArr = this.items;
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            int i2 = this.count;
            int length = tArr.length;
            if (length < i2) {
                tArr = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), i2);
            }
            int length2 = objArr.length - this.takeIndex;
            if (i2 <= length2) {
                System.arraycopy(objArr, this.takeIndex, tArr, 0, i2);
            } else {
                System.arraycopy(objArr, this.takeIndex, tArr, 0, length2);
                System.arraycopy(objArr, 0, tArr, length2, i2 - length2);
            }
            if (length > i2) {
                tArr[i2] = null;
            }
            return tArr;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.AbstractCollection
    public String toString() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            int i2 = this.count;
            if (i2 == 0) {
                return "[]";
            }
            Object[] objArr = this.items;
            StringBuilder sb = new StringBuilder();
            sb.append('[');
            int i3 = this.takeIndex;
            while (true) {
                Object obj = objArr[i3];
                sb.append(obj == this ? "(this Collection)" : obj);
                i2--;
                if (i2 == 0) {
                    String string = sb.append(']').toString();
                    reentrantLock.unlock();
                    return string;
                }
                sb.append(',').append(' ');
                i3++;
                if (i3 == objArr.length) {
                    i3 = 0;
                }
            }
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        Object[] objArr = this.items;
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            int i2 = this.count;
            if (i2 > 0) {
                int i3 = this.putIndex;
                int i4 = this.takeIndex;
                do {
                    objArr[i4] = null;
                    i4++;
                    if (i4 == objArr.length) {
                        i4 = 0;
                    }
                } while (i4 != i3);
                this.takeIndex = i3;
                this.count = 0;
                if (this.itrs != null) {
                    this.itrs.queueIsEmpty();
                }
                while (i2 > 0) {
                    if (!reentrantLock.hasWaiters(this.notFull)) {
                        break;
                    }
                    this.notFull.signal();
                    i2--;
                }
            }
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.concurrent.BlockingQueue
    public int drainTo(Collection<? super E> collection) {
        return drainTo(collection, Integer.MAX_VALUE);
    }

    /* JADX WARN: Incorrect condition in loop: B:29:0x00aa */
    @Override // java.util.concurrent.BlockingQueue
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int drainTo(java.util.Collection<? super E> r5, int r6) {
        /*
            Method dump skipped, instructions count: 315
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ArrayBlockingQueue.drainTo(java.util.Collection, int):int");
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<E> iterator() {
        return new Itr();
    }

    /* loaded from: rt.jar:java/util/concurrent/ArrayBlockingQueue$Itrs.class */
    class Itrs {
        private ArrayBlockingQueue<E>.Itrs.Node head;
        private static final int SHORT_SWEEP_PROBES = 4;
        private static final int LONG_SWEEP_PROBES = 16;
        int cycles = 0;
        private ArrayBlockingQueue<E>.Itrs.Node sweeper = null;

        /* loaded from: rt.jar:java/util/concurrent/ArrayBlockingQueue$Itrs$Node.class */
        private class Node extends WeakReference<ArrayBlockingQueue<E>.Itr> {
            ArrayBlockingQueue<E>.Itrs.Node next;

            Node(ArrayBlockingQueue<E>.Itr itr, ArrayBlockingQueue<E>.Itrs.Node node) {
                super(itr);
                this.next = node;
            }
        }

        Itrs(ArrayBlockingQueue<E>.Itr itr) {
            register(itr);
        }

        void doSomeSweeping(boolean z2) {
            ArrayBlockingQueue<E>.Itrs.Node node;
            ArrayBlockingQueue<E>.Itrs.Node node2;
            boolean z3;
            int i2 = z2 ? 16 : 4;
            ArrayBlockingQueue<E>.Itrs.Node node3 = this.sweeper;
            if (node3 == null) {
                node = null;
                node2 = this.head;
                z3 = true;
            } else {
                node = node3;
                node2 = node.next;
                z3 = false;
            }
            while (i2 > 0) {
                if (node2 == null) {
                    if (z3) {
                        break;
                    }
                    node = null;
                    node2 = this.head;
                    z3 = true;
                }
                ArrayBlockingQueue<E>.Itr itr = node2.get();
                ArrayBlockingQueue<E>.Itrs.Node node4 = node2.next;
                if (itr == null || itr.isDetached()) {
                    i2 = 16;
                    node2.clear();
                    node2.next = null;
                    if (node == null) {
                        this.head = node4;
                        if (node4 == null) {
                            ArrayBlockingQueue.this.itrs = null;
                            return;
                        }
                    } else {
                        node.next = node4;
                    }
                } else {
                    node = node2;
                }
                node2 = node4;
                i2--;
            }
            this.sweeper = node2 == null ? null : node;
        }

        void register(ArrayBlockingQueue<E>.Itr itr) {
            this.head = new Node(itr, this.head);
        }

        void takeIndexWrapped() {
            this.cycles++;
            ArrayBlockingQueue<E>.Itrs.Node node = null;
            ArrayBlockingQueue<E>.Itrs.Node node2 = this.head;
            while (true) {
                ArrayBlockingQueue<E>.Itrs.Node node3 = node2;
                if (node3 == null) {
                    break;
                }
                ArrayBlockingQueue<E>.Itr itr = node3.get();
                ArrayBlockingQueue<E>.Itrs.Node node4 = node3.next;
                if (itr == null || itr.takeIndexWrapped()) {
                    node3.clear();
                    node3.next = null;
                    if (node == null) {
                        this.head = node4;
                    } else {
                        node.next = node4;
                    }
                } else {
                    node = node3;
                }
                node2 = node4;
            }
            if (this.head == null) {
                ArrayBlockingQueue.this.itrs = null;
            }
        }

        void removedAt(int i2) {
            ArrayBlockingQueue<E>.Itrs.Node node = null;
            ArrayBlockingQueue<E>.Itrs.Node node2 = this.head;
            while (true) {
                ArrayBlockingQueue<E>.Itrs.Node node3 = node2;
                if (node3 == null) {
                    break;
                }
                ArrayBlockingQueue<E>.Itr itr = node3.get();
                ArrayBlockingQueue<E>.Itrs.Node node4 = node3.next;
                if (itr == null || itr.removedAt(i2)) {
                    node3.clear();
                    node3.next = null;
                    if (node == null) {
                        this.head = node4;
                    } else {
                        node.next = node4;
                    }
                } else {
                    node = node3;
                }
                node2 = node4;
            }
            if (this.head == null) {
                ArrayBlockingQueue.this.itrs = null;
            }
        }

        void queueIsEmpty() {
            ArrayBlockingQueue<E>.Itrs.Node node = this.head;
            while (true) {
                ArrayBlockingQueue<E>.Itrs.Node node2 = node;
                if (node2 != null) {
                    ArrayBlockingQueue<E>.Itr itr = node2.get();
                    if (itr != null) {
                        node2.clear();
                        itr.shutdown();
                    }
                    node = node2.next;
                } else {
                    this.head = null;
                    ArrayBlockingQueue.this.itrs = null;
                    return;
                }
            }
        }

        void elementDequeued() {
            if (ArrayBlockingQueue.this.count == 0) {
                queueIsEmpty();
            } else if (ArrayBlockingQueue.this.takeIndex == 0) {
                takeIndexWrapped();
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/ArrayBlockingQueue$Itr.class */
    private class Itr implements Iterator<E> {
        private int cursor;
        private E nextItem;
        private int nextIndex;
        private E lastItem;
        private int lastRet = -1;
        private int prevTakeIndex;
        private int prevCycles;
        private static final int NONE = -1;
        private static final int REMOVED = -2;
        private static final int DETACHED = -3;

        Itr() {
            ReentrantLock reentrantLock = ArrayBlockingQueue.this.lock;
            reentrantLock.lock();
            try {
                if (ArrayBlockingQueue.this.count == 0) {
                    this.cursor = -1;
                    this.nextIndex = -1;
                    this.prevTakeIndex = -3;
                } else {
                    int i2 = ArrayBlockingQueue.this.takeIndex;
                    this.prevTakeIndex = i2;
                    this.nextIndex = i2;
                    this.nextItem = (E) ArrayBlockingQueue.this.itemAt(i2);
                    this.cursor = incCursor(i2);
                    if (ArrayBlockingQueue.this.itrs == null) {
                        ArrayBlockingQueue.this.itrs = new Itrs(this);
                    } else {
                        ArrayBlockingQueue.this.itrs.register(this);
                        ArrayBlockingQueue.this.itrs.doSomeSweeping(false);
                    }
                    this.prevCycles = ArrayBlockingQueue.this.itrs.cycles;
                }
            } finally {
                reentrantLock.unlock();
            }
        }

        boolean isDetached() {
            return this.prevTakeIndex < 0;
        }

        private int incCursor(int i2) {
            int i3 = i2 + 1;
            if (i3 == ArrayBlockingQueue.this.items.length) {
                i3 = 0;
            }
            if (i3 == ArrayBlockingQueue.this.putIndex) {
                i3 = -1;
            }
            return i3;
        }

        private boolean invalidated(int i2, int i3, long j2, int i4) {
            if (i2 < 0) {
                return false;
            }
            int i5 = i2 - i3;
            if (i5 < 0) {
                i5 += i4;
            }
            return j2 > ((long) i5);
        }

        private void incorporateDequeues() {
            int i2 = ArrayBlockingQueue.this.itrs.cycles;
            int i3 = ArrayBlockingQueue.this.takeIndex;
            int i4 = this.prevCycles;
            int i5 = this.prevTakeIndex;
            if (i2 != i4 || i3 != i5) {
                int length = ArrayBlockingQueue.this.items.length;
                long j2 = ((i2 - i4) * length) + (i3 - i5);
                if (invalidated(this.lastRet, i5, j2, length)) {
                    this.lastRet = -2;
                }
                if (invalidated(this.nextIndex, i5, j2, length)) {
                    this.nextIndex = -2;
                }
                if (invalidated(this.cursor, i5, j2, length)) {
                    this.cursor = i3;
                }
                if (this.cursor < 0 && this.nextIndex < 0 && this.lastRet < 0) {
                    detach();
                } else {
                    this.prevCycles = i2;
                    this.prevTakeIndex = i3;
                }
            }
        }

        private void detach() {
            if (this.prevTakeIndex >= 0) {
                this.prevTakeIndex = -3;
                ArrayBlockingQueue.this.itrs.doSomeSweeping(true);
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.nextItem != null) {
                return true;
            }
            noNext();
            return false;
        }

        private void noNext() {
            ReentrantLock reentrantLock = ArrayBlockingQueue.this.lock;
            reentrantLock.lock();
            try {
                if (!isDetached()) {
                    incorporateDequeues();
                    if (this.lastRet >= 0) {
                        this.lastItem = (E) ArrayBlockingQueue.this.itemAt(this.lastRet);
                        detach();
                    }
                }
            } finally {
                reentrantLock.unlock();
            }
        }

        @Override // java.util.Iterator
        public E next() {
            E e2 = this.nextItem;
            if (e2 == null) {
                throw new NoSuchElementException();
            }
            ReentrantLock reentrantLock = ArrayBlockingQueue.this.lock;
            reentrantLock.lock();
            try {
                if (!isDetached()) {
                    incorporateDequeues();
                }
                this.lastRet = this.nextIndex;
                int i2 = this.cursor;
                if (i2 >= 0) {
                    ArrayBlockingQueue arrayBlockingQueue = ArrayBlockingQueue.this;
                    this.nextIndex = i2;
                    this.nextItem = (E) arrayBlockingQueue.itemAt(i2);
                    this.cursor = incCursor(i2);
                } else {
                    this.nextIndex = -1;
                    this.nextItem = null;
                }
                return e2;
            } finally {
                reentrantLock.unlock();
            }
        }

        @Override // java.util.Iterator
        public void remove() {
            ReentrantLock reentrantLock = ArrayBlockingQueue.this.lock;
            reentrantLock.lock();
            try {
                if (!isDetached()) {
                    incorporateDequeues();
                }
                int i2 = this.lastRet;
                this.lastRet = -1;
                if (i2 >= 0) {
                    if (!isDetached()) {
                        ArrayBlockingQueue.this.removeAt(i2);
                    } else {
                        E e2 = this.lastItem;
                        this.lastItem = null;
                        if (ArrayBlockingQueue.this.itemAt(i2) == e2) {
                            ArrayBlockingQueue.this.removeAt(i2);
                        }
                    }
                } else if (i2 == -1) {
                    throw new IllegalStateException();
                }
                if (this.cursor < 0 && this.nextIndex < 0) {
                    detach();
                }
            } finally {
                reentrantLock.unlock();
            }
        }

        void shutdown() {
            this.cursor = -1;
            if (this.nextIndex >= 0) {
                this.nextIndex = -2;
            }
            if (this.lastRet >= 0) {
                this.lastRet = -2;
                this.lastItem = null;
            }
            this.prevTakeIndex = -3;
        }

        private int distance(int i2, int i3, int i4) {
            int i5 = i2 - i3;
            if (i5 < 0) {
                i5 += i4;
            }
            return i5;
        }

        boolean removedAt(int i2) {
            if (isDetached()) {
                return true;
            }
            int i3 = ArrayBlockingQueue.this.itrs.cycles;
            int i4 = ArrayBlockingQueue.this.takeIndex;
            int i5 = this.prevCycles;
            int i6 = this.prevTakeIndex;
            int length = ArrayBlockingQueue.this.items.length;
            int i7 = i3 - i5;
            if (i2 < i4) {
                i7++;
            }
            int i8 = (i7 * length) + (i2 - i6);
            int i9 = this.cursor;
            if (i9 >= 0) {
                int iDistance = distance(i9, i6, length);
                if (iDistance == i8) {
                    if (i9 == ArrayBlockingQueue.this.putIndex) {
                        i9 = -1;
                        this.cursor = -1;
                    }
                } else if (iDistance > i8) {
                    int iDec = ArrayBlockingQueue.this.dec(i9);
                    i9 = iDec;
                    this.cursor = iDec;
                }
            }
            int i10 = this.lastRet;
            if (i10 >= 0) {
                int iDistance2 = distance(i10, i6, length);
                if (iDistance2 == i8) {
                    i10 = -2;
                    this.lastRet = -2;
                } else if (iDistance2 > i8) {
                    int iDec2 = ArrayBlockingQueue.this.dec(i10);
                    i10 = iDec2;
                    this.lastRet = iDec2;
                }
            }
            int i11 = this.nextIndex;
            if (i11 >= 0) {
                int iDistance3 = distance(i11, i6, length);
                if (iDistance3 == i8) {
                    this.nextIndex = -2;
                    return false;
                }
                if (iDistance3 > i8) {
                    this.nextIndex = ArrayBlockingQueue.this.dec(i11);
                    return false;
                }
                return false;
            }
            if (i9 < 0 && i11 < 0 && i10 < 0) {
                this.prevTakeIndex = -3;
                return true;
            }
            return false;
        }

        boolean takeIndexWrapped() {
            if (isDetached()) {
                return true;
            }
            if (ArrayBlockingQueue.this.itrs.cycles - this.prevCycles > 1) {
                shutdown();
                return true;
            }
            return false;
        }
    }

    @Override // java.util.Collection, java.lang.Iterable
    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, 4368);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.items.length == 0 || this.takeIndex < 0 || this.takeIndex >= this.items.length || this.putIndex < 0 || this.putIndex >= this.items.length || this.count < 0 || this.count > this.items.length || Math.floorMod(this.putIndex - this.takeIndex, this.items.length) != Math.floorMod(this.count, this.items.length)) {
            throw new InvalidObjectException("invariants violated");
        }
    }
}
