package java.util.concurrent;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/util/concurrent/ConcurrentLinkedQueue.class */
public class ConcurrentLinkedQueue<E> extends AbstractQueue<E> implements Queue<E>, Serializable {
    private static final long serialVersionUID = 196745693267521676L;
    private volatile transient Node<E> head;
    private volatile transient Node<E> tail;
    private static final Unsafe UNSAFE;
    private static final long headOffset;
    private static final long tailOffset;

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentLinkedQueue$Node.class */
    private static class Node<E> {
        volatile E item;
        volatile Node<E> next;
        private static final Unsafe UNSAFE;
        private static final long itemOffset;
        private static final long nextOffset;

        Node(E e2) {
            UNSAFE.putObject(this, itemOffset, e2);
        }

        boolean casItem(E e2, E e3) {
            return UNSAFE.compareAndSwapObject(this, itemOffset, e2, e3);
        }

        void lazySetNext(Node<E> node) {
            UNSAFE.putOrderedObject(this, nextOffset, node);
        }

        boolean casNext(Node<E> node, Node<E> node2) {
            return UNSAFE.compareAndSwapObject(this, nextOffset, node, node2);
        }

        static {
            try {
                UNSAFE = Unsafe.getUnsafe();
                itemOffset = UNSAFE.objectFieldOffset(Node.class.getDeclaredField("item"));
                nextOffset = UNSAFE.objectFieldOffset(Node.class.getDeclaredField(Constants.NEXT));
            } catch (Exception e2) {
                throw new Error(e2);
            }
        }
    }

    public ConcurrentLinkedQueue() {
        Node<E> node = new Node<>(null);
        this.tail = node;
        this.head = node;
    }

    public ConcurrentLinkedQueue(Collection<? extends E> collection) {
        Node<E> node = null;
        Node<E> node2 = null;
        for (E e2 : collection) {
            checkNotNull(e2);
            Node<E> node3 = new Node<>(e2);
            if (node == null) {
                node2 = node3;
                node = node3;
            } else {
                node2.lazySetNext(node3);
                node2 = node3;
            }
        }
        if (node == null) {
            Node<E> node4 = new Node<>(null);
            node2 = node4;
            node = node4;
        }
        this.head = node;
        this.tail = node2;
    }

    @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(E e2) {
        return offer(e2);
    }

    final void updateHead(Node<E> node, Node<E> node2) {
        if (node != node2 && casHead(node, node2)) {
            node.lazySetNext(node);
        }
    }

    final Node<E> succ(Node<E> node) {
        Node<E> node2 = node.next;
        return node == node2 ? this.head : node2;
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x006c A[PHI: r7
  0x006c: PHI (r7v4 java.util.concurrent.ConcurrentLinkedQueue$Node<E>) = (r7v1 java.util.concurrent.ConcurrentLinkedQueue$Node<E>), (r7v6 java.util.concurrent.ConcurrentLinkedQueue$Node<E>) binds: [B:20:0x005b, B:22:0x0065] A[DONT_GENERATE, DONT_INLINE]] */
    @Override // java.util.Queue
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean offer(E r5) {
        /*
            r4 = this;
            r0 = r5
            checkNotNull(r0)
            java.util.concurrent.ConcurrentLinkedQueue$Node r0 = new java.util.concurrent.ConcurrentLinkedQueue$Node
            r1 = r0
            r2 = r5
            r1.<init>(r2)
            r6 = r0
            r0 = r4
            java.util.concurrent.ConcurrentLinkedQueue$Node<E> r0 = r0.tail
            r7 = r0
            r0 = r7
            r8 = r0
        L15:
            r0 = r8
            java.util.concurrent.ConcurrentLinkedQueue$Node<E> r0 = r0.next
            r9 = r0
            r0 = r9
            if (r0 != 0) goto L3a
            r0 = r8
            r1 = 0
            r2 = r6
            boolean r0 = r0.casNext(r1, r2)
            if (r0 == 0) goto L70
            r0 = r8
            r1 = r7
            if (r0 == r1) goto L38
            r0 = r4
            r1 = r7
            r2 = r6
            boolean r0 = r0.casTail(r1, r2)
        L38:
            r0 = 1
            return r0
        L3a:
            r0 = r8
            r1 = r9
            if (r0 != r1) goto L58
            r0 = r7
            r1 = r4
            java.util.concurrent.ConcurrentLinkedQueue$Node<E> r1 = r1.tail
            r2 = r1
            r7 = r2
            if (r0 == r1) goto L4f
            r0 = r7
            goto L53
        L4f:
            r0 = r4
            java.util.concurrent.ConcurrentLinkedQueue$Node<E> r0 = r0.head
        L53:
            r8 = r0
            goto L70
        L58:
            r0 = r8
            r1 = r7
            if (r0 == r1) goto L6c
            r0 = r7
            r1 = r4
            java.util.concurrent.ConcurrentLinkedQueue$Node<E> r1 = r1.tail
            r2 = r1
            r7 = r2
            if (r0 == r1) goto L6c
            r0 = r7
            goto L6e
        L6c:
            r0 = r9
        L6e:
            r8 = r0
        L70:
            goto L15
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedQueue.offer(java.lang.Object):boolean");
    }

    @Override // java.util.Queue
    public E poll() {
        while (true) {
            Node<E> node = this.head;
            Node<E> node2 = node;
            while (true) {
                Node<E> node3 = node2;
                E e2 = node3.item;
                if (e2 != null && node3.casItem(e2, null)) {
                    if (node3 != node) {
                        Node<E> node4 = node3.next;
                        updateHead(node, node4 != null ? node4 : node3);
                    }
                    return e2;
                }
                Node<E> node5 = node3.next;
                if (node5 == null) {
                    updateHead(node, node3);
                    return null;
                }
                if (node3 == node5) {
                    break;
                }
                node2 = node5;
            }
        }
    }

    @Override // java.util.Queue
    public E peek() {
        Node<E> node;
        Node<E> node2;
        E e2;
        Node<E> node3;
        loop0: while (true) {
            node = this.head;
            Node<E> node4 = node;
            while (true) {
                node2 = node4;
                e2 = node2.item;
                if (e2 != null || (node3 = node2.next) == null) {
                    break loop0;
                }
                if (node2 == node3) {
                    break;
                }
                node4 = node3;
            }
        }
        updateHead(node, node2);
        return e2;
    }

    Node<E> first() {
        Node<E> node;
        Node<E> node2;
        boolean z2;
        Node<E> node3;
        loop0: while (true) {
            node = this.head;
            Node<E> node4 = node;
            while (true) {
                node2 = node4;
                z2 = node2.item != null;
                if (z2 || (node3 = node2.next) == null) {
                    break loop0;
                }
                if (node2 == node3) {
                    break;
                }
                node4 = node3;
            }
        }
        updateHead(node, node2);
        if (z2) {
            return node2;
        }
        return null;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean isEmpty() {
        return first() == null;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        int i2 = 0;
        Node<E> nodeFirst = first();
        while (true) {
            Node<E> node = nodeFirst;
            if (node == null) {
                break;
            }
            if (node.item != null) {
                i2++;
                if (i2 == Integer.MAX_VALUE) {
                    break;
                }
            }
            nodeFirst = succ(node);
        }
        return i2;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        if (obj == null) {
            return false;
        }
        Node<E> nodeFirst = first();
        while (true) {
            Node<E> node = nodeFirst;
            if (node != null) {
                E e2 = node.item;
                if (e2 == null || !obj.equals(e2)) {
                    nodeFirst = succ(node);
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x0059 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x005b A[SYNTHETIC] */
    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean remove(java.lang.Object r5) {
        /*
            r4 = this;
            r0 = r5
            if (r0 == 0) goto L64
            r0 = 0
            r7 = r0
            r0 = r4
            java.util.concurrent.ConcurrentLinkedQueue$Node r0 = r0.first()
            r8 = r0
        Lc:
            r0 = r8
            if (r0 == 0) goto L64
            r0 = 0
            r9 = r0
            r0 = r8
            E r0 = r0.item
            r10 = r0
            r0 = r10
            if (r0 == 0) goto L3d
            r0 = r5
            r1 = r10
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L33
            r0 = r4
            r1 = r8
            java.util.concurrent.ConcurrentLinkedQueue$Node r0 = r0.succ(r1)
            r6 = r0
            goto L5b
        L33:
            r0 = r8
            r1 = r10
            r2 = 0
            boolean r0 = r0.casItem(r1, r2)
            r9 = r0
        L3d:
            r0 = r4
            r1 = r8
            java.util.concurrent.ConcurrentLinkedQueue$Node r0 = r0.succ(r1)
            r6 = r0
            r0 = r7
            if (r0 == 0) goto L54
            r0 = r6
            if (r0 == 0) goto L54
            r0 = r7
            r1 = r8
            r2 = r6
            boolean r0 = r0.casNext(r1, r2)
        L54:
            r0 = r9
            if (r0 == 0) goto L5b
            r0 = 1
            return r0
        L5b:
            r0 = r8
            r7 = r0
            r0 = r6
            r8 = r0
            goto Lc
        L64:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedQueue.remove(java.lang.Object):boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:42:0x00d4 A[PHI: r8
  0x00d4: PHI (r8v6 java.util.concurrent.ConcurrentLinkedQueue$Node<E>) = (r8v2 java.util.concurrent.ConcurrentLinkedQueue$Node<E>), (r8v8 java.util.concurrent.ConcurrentLinkedQueue$Node<E>) binds: [B:38:0x00c0, B:40:0x00cc] A[DONT_GENERATE, DONT_INLINE]] */
    @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean addAll(java.util.Collection<? extends E> r5) {
        /*
            Method dump skipped, instructions count: 219
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.ConcurrentLinkedQueue.addAll(java.util.Collection):boolean");
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public Object[] toArray() {
        ArrayList arrayList = new ArrayList();
        Node<E> nodeFirst = first();
        while (true) {
            Node<E> node = nodeFirst;
            if (node != null) {
                E e2 = node.item;
                if (e2 != null) {
                    arrayList.add(e2);
                }
                nodeFirst = succ(node);
            } else {
                return arrayList.toArray();
            }
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        Node<E> node;
        int i2 = 0;
        Node<E> nodeFirst = first();
        while (true) {
            node = nodeFirst;
            if (node == null || i2 >= tArr.length) {
                break;
            }
            E e2 = node.item;
            if (e2 != null) {
                int i3 = i2;
                i2++;
                tArr[i3] = e2;
            }
            nodeFirst = succ(node);
        }
        if (node == null) {
            if (i2 < tArr.length) {
                tArr[i2] = 0;
            }
            return tArr;
        }
        ArrayList arrayList = new ArrayList();
        Node<E> nodeFirst2 = first();
        while (true) {
            Node<E> node2 = nodeFirst2;
            if (node2 != null) {
                E e3 = node2.item;
                if (e3 != null) {
                    arrayList.add(e3);
                }
                nodeFirst2 = succ(node2);
            } else {
                return (T[]) arrayList.toArray(tArr);
            }
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<E> iterator() {
        return new Itr();
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentLinkedQueue$Itr.class */
    private class Itr implements Iterator<E> {
        private Node<E> nextNode;
        private E nextItem;
        private Node<E> lastRet;

        Itr() {
            advance();
        }

        private E advance() {
            Node<E> node;
            Node<E> nodeSucc;
            this.lastRet = this.nextNode;
            E e2 = this.nextItem;
            if (this.nextNode == null) {
                nodeSucc = ConcurrentLinkedQueue.this.first();
                node = null;
            } else {
                node = this.nextNode;
                nodeSucc = ConcurrentLinkedQueue.this.succ(this.nextNode);
            }
            while (nodeSucc != null) {
                E e3 = nodeSucc.item;
                if (e3 != null) {
                    this.nextNode = nodeSucc;
                    this.nextItem = e3;
                    return e2;
                }
                Node<E> nodeSucc2 = ConcurrentLinkedQueue.this.succ(nodeSucc);
                if (node != null && nodeSucc2 != null) {
                    node.casNext(nodeSucc, nodeSucc2);
                }
                nodeSucc = nodeSucc2;
            }
            this.nextNode = null;
            this.nextItem = null;
            return e2;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.nextNode != null;
        }

        @Override // java.util.Iterator
        public E next() {
            if (this.nextNode == null) {
                throw new NoSuchElementException();
            }
            return (E) advance();
        }

        @Override // java.util.Iterator
        public void remove() {
            Node<E> node = this.lastRet;
            if (node == null) {
                throw new IllegalStateException();
            }
            node.item = null;
            this.lastRet = null;
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        Node<E> nodeFirst = first();
        while (true) {
            Node<E> node = nodeFirst;
            if (node != null) {
                E e2 = node.item;
                if (e2 != null) {
                    objectOutputStream.writeObject(e2);
                }
                nodeFirst = succ(node);
            } else {
                objectOutputStream.writeObject(null);
                return;
            }
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        Node<E> node = null;
        Node<E> node2 = null;
        while (true) {
            Object object = objectInputStream.readObject();
            if (object == null) {
                break;
            }
            Node<E> node3 = new Node<>(object);
            if (node == null) {
                node2 = node3;
                node = node3;
            } else {
                node2.lazySetNext(node3);
                node2 = node3;
            }
        }
        if (node == null) {
            Node<E> node4 = new Node<>(null);
            node2 = node4;
            node = node4;
        }
        this.head = node;
        this.tail = node2;
    }

    /* loaded from: rt.jar:java/util/concurrent/ConcurrentLinkedQueue$CLQSpliterator.class */
    static final class CLQSpliterator<E> implements Spliterator<E> {
        static final int MAX_BATCH = 33554432;
        final ConcurrentLinkedQueue<E> queue;
        Node<E> current;
        int batch;
        boolean exhausted;

        CLQSpliterator(ConcurrentLinkedQueue<E> concurrentLinkedQueue) {
            this.queue = concurrentLinkedQueue;
        }

        @Override // java.util.Spliterator
        public Spliterator<E> trySplit() {
            ConcurrentLinkedQueue<E> concurrentLinkedQueue = this.queue;
            int i2 = this.batch;
            int i3 = i2 <= 0 ? 1 : i2 >= 33554432 ? 33554432 : i2 + 1;
            if (this.exhausted) {
                return null;
            }
            Node<E> node = this.current;
            Node<E> nodeFirst = node;
            if (node == null) {
                Node<E> nodeFirst2 = concurrentLinkedQueue.first();
                nodeFirst = nodeFirst2;
                if (nodeFirst2 == null) {
                    return null;
                }
            }
            if (nodeFirst.next != null) {
                Object[] objArr = new Object[i3];
                int i4 = 0;
                do {
                    E e2 = nodeFirst.item;
                    objArr[i4] = e2;
                    if (e2 != null) {
                        i4++;
                    }
                    Node<E> node2 = nodeFirst;
                    Node<E> node3 = nodeFirst.next;
                    nodeFirst = node3;
                    if (node2 == node3) {
                        nodeFirst = concurrentLinkedQueue.first();
                    }
                    if (nodeFirst == null) {
                        break;
                    }
                } while (i4 < i3);
                Node<E> node4 = nodeFirst;
                this.current = node4;
                if (node4 == null) {
                    this.exhausted = true;
                }
                if (i4 > 0) {
                    this.batch = i4;
                    return Spliterators.spliterator(objArr, 0, i4, 4368);
                }
                return null;
            }
            return null;
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super E> consumer) {
            if (consumer == null) {
                throw new NullPointerException();
            }
            ConcurrentLinkedQueue<E> concurrentLinkedQueue = this.queue;
            if (!this.exhausted) {
                Node<E> node = this.current;
                Node<E> nodeFirst = node;
                if (node == null) {
                    Node<E> nodeFirst2 = concurrentLinkedQueue.first();
                    nodeFirst = nodeFirst2;
                    if (nodeFirst2 == null) {
                        return;
                    }
                }
                this.exhausted = true;
                do {
                    E e2 = nodeFirst.item;
                    Node<E> node2 = nodeFirst;
                    Node<E> node3 = nodeFirst.next;
                    nodeFirst = node3;
                    if (node2 == node3) {
                        nodeFirst = concurrentLinkedQueue.first();
                    }
                    if (e2 != null) {
                        consumer.accept(e2);
                    }
                } while (nodeFirst != null);
            }
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super E> consumer) {
            E e2;
            if (consumer == null) {
                throw new NullPointerException();
            }
            ConcurrentLinkedQueue<E> concurrentLinkedQueue = this.queue;
            if (!this.exhausted) {
                Node<E> node = this.current;
                Node<E> nodeFirst = node;
                if (node == null) {
                    Node<E> nodeFirst2 = concurrentLinkedQueue.first();
                    nodeFirst = nodeFirst2;
                    if (nodeFirst2 == null) {
                        return false;
                    }
                }
                do {
                    e2 = nodeFirst.item;
                    Node<E> node2 = nodeFirst;
                    Node<E> node3 = nodeFirst.next;
                    nodeFirst = node3;
                    if (node2 == node3) {
                        nodeFirst = concurrentLinkedQueue.first();
                    }
                    if (e2 != null) {
                        break;
                    }
                } while (nodeFirst != null);
                Node<E> node4 = nodeFirst;
                this.current = node4;
                if (node4 == null) {
                    this.exhausted = true;
                }
                if (e2 != null) {
                    consumer.accept(e2);
                    return true;
                }
                return false;
            }
            return false;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return Long.MAX_VALUE;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return 4368;
        }
    }

    @Override // java.util.Collection, java.lang.Iterable
    public Spliterator<E> spliterator() {
        return new CLQSpliterator(this);
    }

    private static void checkNotNull(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
    }

    private boolean casTail(Node<E> node, Node<E> node2) {
        return UNSAFE.compareAndSwapObject(this, tailOffset, node, node2);
    }

    private boolean casHead(Node<E> node, Node<E> node2) {
        return UNSAFE.compareAndSwapObject(this, headOffset, node, node2);
    }

    static {
        try {
            UNSAFE = Unsafe.getUnsafe();
            headOffset = UNSAFE.objectFieldOffset(ConcurrentLinkedQueue.class.getDeclaredField("head"));
            tailOffset = UNSAFE.objectFieldOffset(ConcurrentLinkedQueue.class.getDeclaredField("tail"));
        } catch (Exception e2) {
            throw new Error(e2);
        }
    }
}
