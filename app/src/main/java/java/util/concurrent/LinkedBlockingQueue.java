package java.util.concurrent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/* loaded from: rt.jar:java/util/concurrent/LinkedBlockingQueue.class */
public class LinkedBlockingQueue<E> extends AbstractQueue<E> implements BlockingQueue<E>, Serializable {
    private static final long serialVersionUID = -6903933977591709194L;
    private final int capacity;
    private final AtomicInteger count;
    transient Node<E> head;
    private transient Node<E> last;
    private final ReentrantLock takeLock;
    private final Condition notEmpty;
    private final ReentrantLock putLock;
    private final Condition notFull;

    /* loaded from: rt.jar:java/util/concurrent/LinkedBlockingQueue$Node.class */
    static class Node<E> {
        E item;
        Node<E> next;

        Node(E e2) {
            this.item = e2;
        }
    }

    private void signalNotEmpty() {
        ReentrantLock reentrantLock = this.takeLock;
        reentrantLock.lock();
        try {
            this.notEmpty.signal();
        } finally {
            reentrantLock.unlock();
        }
    }

    private void signalNotFull() {
        ReentrantLock reentrantLock = this.putLock;
        reentrantLock.lock();
        try {
            this.notFull.signal();
        } finally {
            reentrantLock.unlock();
        }
    }

    private void enqueue(Node<E> node) {
        this.last.next = node;
        this.last = node;
    }

    private E dequeue() {
        Node<E> node = this.head;
        Node<E> node2 = node.next;
        node.next = node;
        this.head = node2;
        E e2 = node2.item;
        node2.item = null;
        return e2;
    }

    void fullyLock() {
        this.putLock.lock();
        this.takeLock.lock();
    }

    void fullyUnlock() {
        this.takeLock.unlock();
        this.putLock.unlock();
    }

    public LinkedBlockingQueue() {
        this(Integer.MAX_VALUE);
    }

    public LinkedBlockingQueue(int i2) {
        this.count = new AtomicInteger();
        this.takeLock = new ReentrantLock();
        this.notEmpty = this.takeLock.newCondition();
        this.putLock = new ReentrantLock();
        this.notFull = this.putLock.newCondition();
        if (i2 <= 0) {
            throw new IllegalArgumentException();
        }
        this.capacity = i2;
        Node<E> node = new Node<>(null);
        this.head = node;
        this.last = node;
    }

    public LinkedBlockingQueue(Collection<? extends E> collection) {
        this(Integer.MAX_VALUE);
        ReentrantLock reentrantLock = this.putLock;
        reentrantLock.lock();
        try {
            int i2 = 0;
            for (E e2 : collection) {
                if (e2 == null) {
                    throw new NullPointerException();
                }
                if (i2 == this.capacity) {
                    throw new IllegalStateException("Queue full");
                }
                enqueue(new Node<>(e2));
                i2++;
            }
            this.count.set(i2);
            reentrantLock.unlock();
        } catch (Throwable th) {
            reentrantLock.unlock();
            throw th;
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.count.get();
    }

    @Override // java.util.concurrent.BlockingQueue
    public int remainingCapacity() {
        return this.capacity - this.count.get();
    }

    @Override // java.util.concurrent.BlockingQueue
    public void put(E e2) throws InterruptedException {
        if (e2 == null) {
            throw new NullPointerException();
        }
        Node<E> node = new Node<>(e2);
        ReentrantLock reentrantLock = this.putLock;
        AtomicInteger atomicInteger = this.count;
        reentrantLock.lockInterruptibly();
        while (atomicInteger.get() == this.capacity) {
            try {
                this.notFull.await();
            } finally {
                reentrantLock.unlock();
            }
        }
        enqueue(node);
        int andIncrement = atomicInteger.getAndIncrement();
        if (andIncrement + 1 < this.capacity) {
            this.notFull.signal();
        }
        if (andIncrement == 0) {
            signalNotEmpty();
        }
    }

    @Override // java.util.concurrent.BlockingQueue
    public boolean offer(E e2, long j2, TimeUnit timeUnit) throws InterruptedException {
        if (e2 == null) {
            throw new NullPointerException();
        }
        long nanos = timeUnit.toNanos(j2);
        ReentrantLock reentrantLock = this.putLock;
        AtomicInteger atomicInteger = this.count;
        reentrantLock.lockInterruptibly();
        while (atomicInteger.get() == this.capacity) {
            try {
                if (nanos <= 0) {
                    return false;
                }
                nanos = this.notFull.awaitNanos(nanos);
            } finally {
                reentrantLock.unlock();
            }
        }
        enqueue(new Node<>(e2));
        int andIncrement = atomicInteger.getAndIncrement();
        if (andIncrement + 1 < this.capacity) {
            this.notFull.signal();
        }
        reentrantLock.unlock();
        if (andIncrement == 0) {
            signalNotEmpty();
            return true;
        }
        return true;
    }

    @Override // java.util.Queue
    public boolean offer(E e2) {
        if (e2 == null) {
            throw new NullPointerException();
        }
        AtomicInteger atomicInteger = this.count;
        if (atomicInteger.get() == this.capacity) {
            return false;
        }
        int andIncrement = -1;
        Node<E> node = new Node<>(e2);
        ReentrantLock reentrantLock = this.putLock;
        reentrantLock.lock();
        try {
            if (atomicInteger.get() < this.capacity) {
                enqueue(node);
                andIncrement = atomicInteger.getAndIncrement();
                if (andIncrement + 1 < this.capacity) {
                    this.notFull.signal();
                }
            }
            if (andIncrement == 0) {
                signalNotEmpty();
            }
            return andIncrement >= 0;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.concurrent.BlockingQueue
    /* renamed from: take */
    public E take2() throws InterruptedException {
        AtomicInteger atomicInteger = this.count;
        ReentrantLock reentrantLock = this.takeLock;
        reentrantLock.lockInterruptibly();
        while (atomicInteger.get() == 0) {
            try {
                this.notEmpty.await();
            } finally {
                reentrantLock.unlock();
            }
        }
        E eDequeue = dequeue();
        int andDecrement = atomicInteger.getAndDecrement();
        if (andDecrement > 1) {
            this.notEmpty.signal();
        }
        if (andDecrement == this.capacity) {
            signalNotFull();
        }
        return eDequeue;
    }

    @Override // java.util.concurrent.BlockingQueue
    /* renamed from: poll */
    public E poll2(long j2, TimeUnit timeUnit) throws InterruptedException {
        long nanos = timeUnit.toNanos(j2);
        AtomicInteger atomicInteger = this.count;
        ReentrantLock reentrantLock = this.takeLock;
        reentrantLock.lockInterruptibly();
        while (atomicInteger.get() == 0) {
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
        int andDecrement = atomicInteger.getAndDecrement();
        if (andDecrement > 1) {
            this.notEmpty.signal();
        }
        reentrantLock.unlock();
        if (andDecrement == this.capacity) {
            signalNotFull();
        }
        return eDequeue;
    }

    @Override // java.util.Queue
    public E poll() {
        AtomicInteger atomicInteger = this.count;
        if (atomicInteger.get() == 0) {
            return null;
        }
        E eDequeue = null;
        int andDecrement = -1;
        ReentrantLock reentrantLock = this.takeLock;
        reentrantLock.lock();
        try {
            if (atomicInteger.get() > 0) {
                eDequeue = dequeue();
                andDecrement = atomicInteger.getAndDecrement();
                if (andDecrement > 1) {
                    this.notEmpty.signal();
                }
            }
            if (andDecrement == this.capacity) {
                signalNotFull();
            }
            return eDequeue;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // java.util.Queue
    public E peek() {
        if (this.count.get() == 0) {
            return null;
        }
        ReentrantLock reentrantLock = this.takeLock;
        reentrantLock.lock();
        try {
            Node<E> node = this.head.next;
            if (node == null) {
                return null;
            }
            E e2 = node.item;
            reentrantLock.unlock();
            return e2;
        } finally {
            reentrantLock.unlock();
        }
    }

    void unlink(Node<E> node, Node<E> node2) {
        node.item = null;
        node2.next = node.next;
        if (this.last == node) {
            this.last = node2;
        }
        if (this.count.getAndDecrement() == this.capacity) {
            this.notFull.signal();
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object obj) {
        if (obj == null) {
            return false;
        }
        fullyLock();
        try {
            Node<E> node = this.head;
            for (Node<E> node2 = node.next; node2 != null; node2 = node2.next) {
                if (!obj.equals(node2.item)) {
                    node = node2;
                } else {
                    unlink(node2, node);
                    fullyUnlock();
                    return true;
                }
            }
            return false;
        } finally {
            fullyUnlock();
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        if (obj == null) {
            return false;
        }
        fullyLock();
        try {
            for (Node<E> node = this.head.next; node != null; node = node.next) {
                if (obj.equals(node.item)) {
                    return true;
                }
            }
            fullyUnlock();
            return false;
        } finally {
            fullyUnlock();
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public Object[] toArray() {
        fullyLock();
        try {
            Object[] objArr = new Object[this.count.get()];
            int i2 = 0;
            for (Node<E> node = this.head.next; node != null; node = node.next) {
                int i3 = i2;
                i2++;
                objArr[i3] = node.item;
            }
            return objArr;
        } finally {
            fullyUnlock();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v19 */
    /* JADX WARN: Type inference failed for: r0v27, types: [java.lang.Object[]] */
    @Override // java.util.AbstractCollection, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        fullyLock();
        try {
            int i2 = this.count.get();
            if (tArr.length < i2) {
                tArr = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), i2);
            }
            int i3 = 0;
            for (Node<E> node = this.head.next; node != null; node = node.next) {
                int i4 = i3;
                i3++;
                tArr[i4] = node.item;
            }
            if (tArr.length > i3) {
                tArr[i3] = null;
            }
            return tArr;
        } finally {
            fullyUnlock();
        }
    }

    @Override // java.util.AbstractCollection
    public String toString() {
        fullyLock();
        try {
            Node<E> node = this.head.next;
            if (node == null) {
                return "[]";
            }
            StringBuilder sb = new StringBuilder();
            sb.append('[');
            while (true) {
                E e2 = node.item;
                sb.append(e2 == this ? "(this Collection)" : e2);
                node = node.next;
                if (node == null) {
                    String string = sb.append(']').toString();
                    fullyUnlock();
                    return string;
                }
                sb.append(',').append(' ');
            }
        } finally {
            fullyUnlock();
        }
    }

    @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        fullyLock();
        try {
            Node<E> node = this.head;
            while (true) {
                Node<E> node2 = node.next;
                if (node2 == null) {
                    break;
                }
                node.next = node;
                node2.item = null;
                node = node2;
            }
            this.head = this.last;
            if (this.count.getAndSet(0) == this.capacity) {
                this.notFull.signal();
            }
        } finally {
            fullyUnlock();
        }
    }

    @Override // java.util.concurrent.BlockingQueue
    public int drainTo(Collection<? super E> collection) {
        return drainTo(collection, Integer.MAX_VALUE);
    }

    /* JADX WARN: Finally extract failed */
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
        boolean z2 = false;
        ReentrantLock reentrantLock = this.takeLock;
        reentrantLock.lock();
        try {
            int iMin = Math.min(i2, this.count.get());
            Node<E> node = this.head;
            int i3 = 0;
            while (i3 < iMin) {
                try {
                    Node<E> node2 = node.next;
                    collection.add(node2.item);
                    node2.item = null;
                    node.next = node;
                    node = node2;
                    i3++;
                } finally {
                    if (i3 > 0) {
                        this.head = node;
                        boolean z3 = this.count.getAndAdd(-i3) == this.capacity;
                    }
                }
            }
            reentrantLock.unlock();
            if (z2) {
                signalNotFull();
            }
            return iMin;
        } catch (Throwable th) {
            reentrantLock.unlock();
            if (0 != 0) {
                signalNotFull();
            }
            throw th;
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<E> iterator() {
        return new Itr();
    }

    /* loaded from: rt.jar:java/util/concurrent/LinkedBlockingQueue$Itr.class */
    private class Itr implements Iterator<E> {
        private Node<E> current;
        private Node<E> lastRet;
        private E currentElement;

        Itr() {
            LinkedBlockingQueue.this.fullyLock();
            try {
                this.current = LinkedBlockingQueue.this.head.next;
                if (this.current != null) {
                    this.currentElement = this.current.item;
                }
            } finally {
                LinkedBlockingQueue.this.fullyUnlock();
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.current != null;
        }

        private Node<E> nextNode(Node<E> node) {
            Node<E> node2;
            while (true) {
                node2 = node.next;
                if (node2 == node) {
                    return LinkedBlockingQueue.this.head.next;
                }
                if (node2 == null || node2.item != null) {
                    break;
                }
                node = node2;
            }
            return node2;
        }

        @Override // java.util.Iterator
        public E next() {
            LinkedBlockingQueue.this.fullyLock();
            try {
                if (this.current == null) {
                    throw new NoSuchElementException();
                }
                E e2 = this.currentElement;
                this.lastRet = this.current;
                this.current = nextNode(this.current);
                this.currentElement = this.current == null ? null : this.current.item;
                return e2;
            } finally {
                LinkedBlockingQueue.this.fullyUnlock();
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:12:0x0036, code lost:
        
            r4.this$0.unlink(r7, r6);
         */
        @Override // java.util.Iterator
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void remove() {
            /*
                r4 = this;
                r0 = r4
                java.util.concurrent.LinkedBlockingQueue$Node<E> r0 = r0.lastRet
                if (r0 != 0) goto Lf
                java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
                r1 = r0
                r1.<init>()
                throw r0
            Lf:
                r0 = r4
                java.util.concurrent.LinkedBlockingQueue r0 = java.util.concurrent.LinkedBlockingQueue.this
                r0.fullyLock()
                r0 = r4
                java.util.concurrent.LinkedBlockingQueue$Node<E> r0 = r0.lastRet     // Catch: java.lang.Throwable -> L56
                r5 = r0
                r0 = r4
                r1 = 0
                r0.lastRet = r1     // Catch: java.lang.Throwable -> L56
                r0 = r4
                java.util.concurrent.LinkedBlockingQueue r0 = java.util.concurrent.LinkedBlockingQueue.this     // Catch: java.lang.Throwable -> L56
                java.util.concurrent.LinkedBlockingQueue$Node<E> r0 = r0.head     // Catch: java.lang.Throwable -> L56
                r6 = r0
                r0 = r6
                java.util.concurrent.LinkedBlockingQueue$Node<E> r0 = r0.next     // Catch: java.lang.Throwable -> L56
                r7 = r0
            L2d:
                r0 = r7
                if (r0 == 0) goto L4c
                r0 = r7
                r1 = r5
                if (r0 != r1) goto L42
                r0 = r4
                java.util.concurrent.LinkedBlockingQueue r0 = java.util.concurrent.LinkedBlockingQueue.this     // Catch: java.lang.Throwable -> L56
                r1 = r7
                r2 = r6
                r0.unlink(r1, r2)     // Catch: java.lang.Throwable -> L56
                goto L4c
            L42:
                r0 = r7
                r6 = r0
                r0 = r7
                java.util.concurrent.LinkedBlockingQueue$Node<E> r0 = r0.next     // Catch: java.lang.Throwable -> L56
                r7 = r0
                goto L2d
            L4c:
                r0 = r4
                java.util.concurrent.LinkedBlockingQueue r0 = java.util.concurrent.LinkedBlockingQueue.this
                r0.fullyUnlock()
                goto L62
            L56:
                r8 = move-exception
                r0 = r4
                java.util.concurrent.LinkedBlockingQueue r0 = java.util.concurrent.LinkedBlockingQueue.this
                r0.fullyUnlock()
                r0 = r8
                throw r0
            L62:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.LinkedBlockingQueue.Itr.remove():void");
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/LinkedBlockingQueue$LBQSpliterator.class */
    static final class LBQSpliterator<E> implements Spliterator<E> {
        static final int MAX_BATCH = 33554432;
        final LinkedBlockingQueue<E> queue;
        Node<E> current;
        int batch;
        boolean exhausted;
        long est;

        /*  JADX ERROR: Failed to decode insn: 0x00BE: MOVE_MULTI
            java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
            	at java.base/java.lang.System.arraycopy(Native Method)
            	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
            	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
            	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
            	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
            	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
            	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:464)
            	at jadx.core.ProcessClass.process(ProcessClass.java:69)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
            	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
            	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
            */
        @Override // java.util.Spliterator
        public java.util.Spliterator<E> trySplit() {
            /*
                Method dump skipped, instructions count: 229
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.LinkedBlockingQueue.LBQSpliterator.trySplit():java.util.Spliterator");
        }

        LBQSpliterator(LinkedBlockingQueue<E> linkedBlockingQueue) {
            this.queue = linkedBlockingQueue;
            this.est = linkedBlockingQueue.size();
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.est;
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super E> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            LinkedBlockingQueue<E> linkedBlockingQueue = this.queue;
            if (!this.exhausted) {
                this.exhausted = true;
                Node<E> node = this.current;
                do {
                    E e2 = null;
                    linkedBlockingQueue.fullyLock();
                    if (node == null) {
                        try {
                            node = linkedBlockingQueue.head.next;
                        } finally {
                            linkedBlockingQueue.fullyUnlock();
                        }
                    }
                    while (node != null) {
                        e2 = node.item;
                        node = node.next;
                        if (e2 != null) {
                            break;
                        }
                    }
                    if (e2 != null) {
                        consumer.accept((Object) e2);
                    }
                } while (node != null);
            }
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super E> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            LinkedBlockingQueue<E> linkedBlockingQueue = this.queue;
            if (!this.exhausted) {
                E e2 = null;
                linkedBlockingQueue.fullyLock();
                try {
                    if (this.current == null) {
                        this.current = linkedBlockingQueue.head.next;
                    }
                    while (this.current != null) {
                        e2 = this.current.item;
                        this.current = this.current.next;
                        if (e2 != null) {
                            break;
                        }
                    }
                    if (this.current == null) {
                        this.exhausted = true;
                    }
                    if (e2 != null) {
                        consumer.accept((Object) e2);
                        return true;
                    }
                    return false;
                } finally {
                    linkedBlockingQueue.fullyUnlock();
                }
            }
            return false;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return 4368;
        }
    }

    @Override // java.util.Collection, java.lang.Iterable
    public Spliterator<E> spliterator() {
        return new LBQSpliterator(this);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        fullyLock();
        try {
            objectOutputStream.defaultWriteObject();
            for (Node<E> node = this.head.next; node != null; node = node.next) {
                objectOutputStream.writeObject(node.item);
            }
            objectOutputStream.writeObject(null);
        } finally {
            fullyUnlock();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.count.set(0);
        Node<E> node = new Node<>(null);
        this.head = node;
        this.last = node;
        while (true) {
            Object object = objectInputStream.readObject();
            if (object != null) {
                add(object);
            } else {
                return;
            }
        }
    }
}
