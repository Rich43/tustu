package java.util.concurrent;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Consumer;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/util/concurrent/LinkedTransferQueue.class */
public class LinkedTransferQueue<E> extends AbstractQueue<E> implements TransferQueue<E>, Serializable {
    private static final long serialVersionUID = -3223113410248163686L;
    private static final boolean MP;
    private static final int FRONT_SPINS = 128;
    private static final int CHAINED_SPINS = 64;
    static final int SWEEP_THRESHOLD = 32;
    volatile transient Node head;
    private volatile transient Node tail;
    private volatile transient int sweepVotes;
    private static final int NOW = 0;
    private static final int ASYNC = 1;
    private static final int SYNC = 2;
    private static final int TIMED = 3;
    private static final Unsafe UNSAFE;
    private static final long headOffset;
    private static final long tailOffset;
    private static final long sweepVotesOffset;

    static {
        MP = Runtime.getRuntime().availableProcessors() > 1;
        try {
            UNSAFE = Unsafe.getUnsafe();
            headOffset = UNSAFE.objectFieldOffset(LinkedTransferQueue.class.getDeclaredField("head"));
            tailOffset = UNSAFE.objectFieldOffset(LinkedTransferQueue.class.getDeclaredField("tail"));
            sweepVotesOffset = UNSAFE.objectFieldOffset(LinkedTransferQueue.class.getDeclaredField("sweepVotes"));
        } catch (Exception e2) {
            throw new Error(e2);
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/LinkedTransferQueue$Node.class */
    static final class Node {
        final boolean isData;
        volatile Object item;
        volatile Node next;
        volatile Thread waiter;
        private static final long serialVersionUID = -3375979862319811754L;
        private static final Unsafe UNSAFE;
        private static final long itemOffset;
        private static final long nextOffset;
        private static final long waiterOffset;

        final boolean casNext(Node node, Node node2) {
            return UNSAFE.compareAndSwapObject(this, nextOffset, node, node2);
        }

        final boolean casItem(Object obj, Object obj2) {
            return UNSAFE.compareAndSwapObject(this, itemOffset, obj, obj2);
        }

        Node(Object obj, boolean z2) {
            UNSAFE.putObject(this, itemOffset, obj);
            this.isData = z2;
        }

        final void forgetNext() {
            UNSAFE.putObject(this, nextOffset, this);
        }

        final void forgetContents() {
            UNSAFE.putObject(this, itemOffset, this);
            UNSAFE.putObject(this, waiterOffset, (Object) null);
        }

        final boolean isMatched() {
            Object obj = this.item;
            if (obj != this) {
                if ((obj == null) != this.isData) {
                    return false;
                }
            }
            return true;
        }

        final boolean isUnmatchedRequest() {
            return !this.isData && this.item == null;
        }

        final boolean cannotPrecede(boolean z2) {
            Object obj;
            boolean z3 = this.isData;
            if (z3 != z2 && (obj = this.item) != this) {
                if ((obj != null) == z3) {
                    return true;
                }
            }
            return false;
        }

        final boolean tryMatchData() {
            Object obj = this.item;
            if (obj != null && obj != this && casItem(obj, null)) {
                LockSupport.unpark(this.waiter);
                return true;
            }
            return false;
        }

        static {
            try {
                UNSAFE = Unsafe.getUnsafe();
                itemOffset = UNSAFE.objectFieldOffset(Node.class.getDeclaredField("item"));
                nextOffset = UNSAFE.objectFieldOffset(Node.class.getDeclaredField(Constants.NEXT));
                waiterOffset = UNSAFE.objectFieldOffset(Node.class.getDeclaredField("waiter"));
            } catch (Exception e2) {
                throw new Error(e2);
            }
        }
    }

    private boolean casTail(Node node, Node node2) {
        return UNSAFE.compareAndSwapObject(this, tailOffset, node, node2);
    }

    private boolean casHead(Node node, Node node2) {
        return UNSAFE.compareAndSwapObject(this, headOffset, node, node2);
    }

    private boolean casSweepVotes(int i2, int i3) {
        return UNSAFE.compareAndSwapInt(this, sweepVotesOffset, i2, i3);
    }

    /* JADX WARN: Multi-variable type inference failed */
    static <E> E cast(Object obj) {
        return obj;
    }

    /* JADX WARN: Code restructure failed: missing block: B:53:0x00e3, code lost:
    
        if (r11 == 0) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00e8, code lost:
    
        if (r14 != null) goto L57;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00eb, code lost:
    
        r14 = new java.util.concurrent.LinkedTransferQueue.Node(r9, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x00f6, code lost:
    
        r0 = tryAppend(r14, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x0101, code lost:
    
        if (r0 != null) goto L72;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0109, code lost:
    
        if (r11 == 1) goto L68;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x010c, code lost:
    
        r1 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0114, code lost:
    
        if (r11 != 3) goto L65;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0117, code lost:
    
        r4 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x011b, code lost:
    
        r4 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0121, code lost:
    
        return awaitMatch(r1, r0, r9, r4, r12);
     */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0092  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private E xfer(E r9, boolean r10, int r11, long r12) {
        /*
            Method dump skipped, instructions count: 292
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.LinkedTransferQueue.xfer(java.lang.Object, boolean, int, long):java.lang.Object");
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0050  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.util.concurrent.LinkedTransferQueue.Node tryAppend(java.util.concurrent.LinkedTransferQueue.Node r5, boolean r6) {
        /*
            r4 = this;
            r0 = r4
            java.util.concurrent.LinkedTransferQueue$Node r0 = r0.tail
            r7 = r0
            r0 = r7
            r8 = r0
        L8:
            r0 = r8
            if (r0 != 0) goto L22
            r0 = r4
            java.util.concurrent.LinkedTransferQueue$Node r0 = r0.head
            r1 = r0
            r8 = r1
            if (r0 != 0) goto L22
            r0 = r4
            r1 = 0
            r2 = r5
            boolean r0 = r0.casHead(r1, r2)
            if (r0 == 0) goto Lb3
            r0 = r5
            return r0
        L22:
            r0 = r8
            r1 = r6
            boolean r0 = r0.cannotPrecede(r1)
            if (r0 == 0) goto L2d
            r0 = 0
            return r0
        L2d:
            r0 = r8
            java.util.concurrent.LinkedTransferQueue$Node r0 = r0.next
            r1 = r0
            r9 = r1
            if (r0 == 0) goto L62
            r0 = r8
            r1 = r7
            if (r0 == r1) goto L50
            r0 = r7
            r1 = r4
            java.util.concurrent.LinkedTransferQueue$Node r1 = r1.tail
            r2 = r1
            r10 = r2
            if (r0 == r1) goto L50
            r0 = r10
            r1 = r0
            r7 = r1
            goto L5d
        L50:
            r0 = r8
            r1 = r9
            if (r0 == r1) goto L5c
            r0 = r9
            goto L5d
        L5c:
            r0 = 0
        L5d:
            r8 = r0
            goto Lb3
        L62:
            r0 = r8
            r1 = 0
            r2 = r5
            boolean r0 = r0.casNext(r1, r2)
            if (r0 != 0) goto L76
            r0 = r8
            java.util.concurrent.LinkedTransferQueue$Node r0 = r0.next
            r8 = r0
            goto Lb3
        L76:
            r0 = r8
            r1 = r7
            if (r0 == r1) goto Lb0
        L7c:
            r0 = r4
            java.util.concurrent.LinkedTransferQueue$Node r0 = r0.tail
            r1 = r7
            if (r0 != r1) goto L8d
            r0 = r4
            r1 = r7
            r2 = r5
            boolean r0 = r0.casTail(r1, r2)
            if (r0 != 0) goto Lb0
        L8d:
            r0 = r4
            java.util.concurrent.LinkedTransferQueue$Node r0 = r0.tail
            r1 = r0
            r7 = r1
            if (r0 == 0) goto Lb0
            r0 = r7
            java.util.concurrent.LinkedTransferQueue$Node r0 = r0.next
            r1 = r0
            r5 = r1
            if (r0 == 0) goto Lb0
            r0 = r5
            java.util.concurrent.LinkedTransferQueue$Node r0 = r0.next
            r1 = r0
            r5 = r1
            if (r0 == 0) goto Lb0
            r0 = r5
            r1 = r7
            if (r0 == r1) goto Lb0
            goto L7c
        Lb0:
            r0 = r8
            return r0
        Lb3:
            goto L8
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.LinkedTransferQueue.tryAppend(java.util.concurrent.LinkedTransferQueue$Node, boolean):java.util.concurrent.LinkedTransferQueue$Node");
    }

    private E awaitMatch(Node node, Node node2, E e2, boolean z2, long j2) {
        long jNanoTime = z2 ? System.nanoTime() + j2 : 0L;
        Thread threadCurrentThread = Thread.currentThread();
        int i2 = -1;
        ThreadLocalRandom threadLocalRandomCurrent = null;
        while (true) {
            Object obj = node.item;
            if (obj != e2) {
                node.forgetContents();
                return (E) cast(obj);
            }
            if ((threadCurrentThread.isInterrupted() || (z2 && j2 <= 0)) && node.casItem(e2, node)) {
                unsplice(node2, node);
                return e2;
            }
            if (i2 < 0) {
                int iSpinsFor = spinsFor(node2, node.isData);
                i2 = iSpinsFor;
                if (iSpinsFor > 0) {
                    threadLocalRandomCurrent = ThreadLocalRandom.current();
                }
            } else if (i2 > 0) {
                i2--;
                if (threadLocalRandomCurrent.nextInt(64) == 0) {
                    Thread.yield();
                }
            } else if (node.waiter == null) {
                node.waiter = threadCurrentThread;
            } else if (z2) {
                j2 = jNanoTime - System.nanoTime();
                if (j2 > 0) {
                    LockSupport.parkNanos(this, j2);
                }
            } else {
                LockSupport.park(this);
            }
        }
    }

    private static int spinsFor(Node node, boolean z2) {
        if (MP && node != null) {
            if (node.isData != z2) {
                return 192;
            }
            if (node.isMatched()) {
                return 128;
            }
            if (node.waiter == null) {
                return 64;
            }
            return 0;
        }
        return 0;
    }

    final Node succ(Node node) {
        Node node2 = node.next;
        return node == node2 ? this.head : node2;
    }

    private Node firstOfMode(boolean z2) {
        Node nodeSucc = this.head;
        while (true) {
            Node node = nodeSucc;
            if (node != null) {
                if (node.isMatched()) {
                    nodeSucc = succ(node);
                } else {
                    if (node.isData == z2) {
                        return node;
                    }
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    final Node firstDataNode() {
        Node node = this.head;
        while (node != null) {
            Object obj = node.item;
            if (node.isData) {
                if (obj != null && obj != node) {
                    return node;
                }
            } else if (obj == null) {
                return null;
            }
            Node node2 = node;
            Node node3 = node.next;
            node = node3;
            if (node2 == node3) {
                node = this.head;
            }
        }
        return null;
    }

    private E firstDataItem() {
        Node nodeSucc = this.head;
        while (true) {
            Node node = nodeSucc;
            if (node != null) {
                Object obj = node.item;
                if (node.isData) {
                    if (obj != null && obj != node) {
                        return (E) cast(obj);
                    }
                } else if (obj == null) {
                    return null;
                }
                nodeSucc = succ(node);
            } else {
                return null;
            }
        }
    }

    private int countOfMode(boolean z2) {
        int i2 = 0;
        Node node = this.head;
        while (true) {
            Node node2 = node;
            if (node2 == null) {
                break;
            }
            if (!node2.isMatched()) {
                if (node2.isData != z2) {
                    return 0;
                }
                i2++;
                if (i2 == Integer.MAX_VALUE) {
                    break;
                }
            }
            Node node3 = node2.next;
            if (node3 != node2) {
                node = node3;
            } else {
                i2 = 0;
                node = this.head;
            }
        }
        return i2;
    }

    /* loaded from: rt.jar:java/util/concurrent/LinkedTransferQueue$Itr.class */
    final class Itr implements Iterator<E> {
        private Node nextNode;
        private E nextItem;
        private Node lastRet;
        private Node lastPred;

        private void advance(Node node) {
            Node node2;
            Node node3 = this.lastRet;
            if (node3 != null && !node3.isMatched()) {
                this.lastPred = node3;
            } else {
                Node node4 = this.lastPred;
                if (node4 == null || node4.isMatched()) {
                    this.lastPred = null;
                } else {
                    while (true) {
                        Node node5 = node4.next;
                        if (node5 == null || node5 == node4 || !node5.isMatched() || (node2 = node5.next) == null || node2 == node5) {
                            break;
                        } else {
                            node4.casNext(node5, node2);
                        }
                    }
                }
            }
            this.lastRet = node;
            Node node6 = node;
            while (true) {
                Node node7 = node6 == null ? LinkedTransferQueue.this.head : node6.next;
                if (node7 == null) {
                    break;
                }
                if (node7 == node6) {
                    node6 = null;
                } else {
                    Object obj = node7.item;
                    if (node7.isData) {
                        if (obj != null && obj != node7) {
                            this.nextItem = (E) LinkedTransferQueue.cast(obj);
                            this.nextNode = node7;
                            return;
                        }
                    } else {
                        if (obj != null) {
                            break;
                            break;
                        }
                        break;
                    }
                    if (node6 == null) {
                        node6 = node7;
                    } else {
                        Node node8 = node7.next;
                        if (node8 == null) {
                            break;
                        } else if (node7 == node8) {
                            node6 = null;
                        } else {
                            node6.casNext(node7, node8);
                        }
                    }
                }
            }
            this.nextNode = null;
            this.nextItem = null;
        }

        Itr() {
            advance(null);
        }

        @Override // java.util.Iterator
        public final boolean hasNext() {
            return this.nextNode != null;
        }

        @Override // java.util.Iterator
        public final E next() {
            Node node = this.nextNode;
            if (node == null) {
                throw new NoSuchElementException();
            }
            E e2 = this.nextItem;
            advance(node);
            return e2;
        }

        @Override // java.util.Iterator
        public final void remove() {
            Node node = this.lastRet;
            if (node == null) {
                throw new IllegalStateException();
            }
            this.lastRet = null;
            if (node.tryMatchData()) {
                LinkedTransferQueue.this.unsplice(this.lastPred, node);
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/LinkedTransferQueue$LTQSpliterator.class */
    static final class LTQSpliterator<E> implements Spliterator<E> {
        static final int MAX_BATCH = 33554432;
        final LinkedTransferQueue<E> queue;
        Node current;
        int batch;
        boolean exhausted;

        LTQSpliterator(LinkedTransferQueue<E> linkedTransferQueue) {
            this.queue = linkedTransferQueue;
        }

        @Override // java.util.Spliterator
        public Spliterator<E> trySplit() {
            LinkedTransferQueue<E> linkedTransferQueue = this.queue;
            int i2 = this.batch;
            int i3 = i2 <= 0 ? 1 : i2 >= 33554432 ? 33554432 : i2 + 1;
            if (this.exhausted) {
                return null;
            }
            Node node = this.current;
            Node nodeFirstDataNode = node;
            if (node == null) {
                Node nodeFirstDataNode2 = linkedTransferQueue.firstDataNode();
                nodeFirstDataNode = nodeFirstDataNode2;
                if (nodeFirstDataNode2 == null) {
                    return null;
                }
            }
            if (nodeFirstDataNode.next != null) {
                Object[] objArr = new Object[i3];
                int i4 = 0;
                do {
                    Object obj = nodeFirstDataNode.item;
                    if (obj != nodeFirstDataNode) {
                        objArr[i4] = obj;
                        if (obj != null) {
                            i4++;
                        }
                    }
                    Node node2 = nodeFirstDataNode;
                    Node node3 = nodeFirstDataNode.next;
                    nodeFirstDataNode = node3;
                    if (node2 == node3) {
                        nodeFirstDataNode = linkedTransferQueue.firstDataNode();
                    }
                    if (nodeFirstDataNode == null || i4 >= i3) {
                        break;
                    }
                } while (nodeFirstDataNode.isData);
                Node node4 = nodeFirstDataNode;
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
            LinkedTransferQueue<E> linkedTransferQueue = this.queue;
            if (!this.exhausted) {
                Node node = this.current;
                Node nodeFirstDataNode = node;
                if (node == null) {
                    Node nodeFirstDataNode2 = linkedTransferQueue.firstDataNode();
                    nodeFirstDataNode = nodeFirstDataNode2;
                    if (nodeFirstDataNode2 == null) {
                        return;
                    }
                }
                this.exhausted = true;
                do {
                    Node node2 = (Object) nodeFirstDataNode.item;
                    if (node2 != null && node2 != nodeFirstDataNode) {
                        consumer.accept(node2);
                    }
                    Node node3 = nodeFirstDataNode;
                    Node node4 = nodeFirstDataNode.next;
                    nodeFirstDataNode = node4;
                    if (node3 == node4) {
                        nodeFirstDataNode = linkedTransferQueue.firstDataNode();
                    }
                    if (nodeFirstDataNode == null) {
                        return;
                    }
                } while (nodeFirstDataNode.isData);
            }
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super E> consumer) {
            Object obj;
            if (consumer == null) {
                throw new NullPointerException();
            }
            LinkedTransferQueue<E> linkedTransferQueue = this.queue;
            if (!this.exhausted) {
                Node node = this.current;
                Node nodeFirstDataNode = node;
                if (node == null) {
                    Node nodeFirstDataNode2 = linkedTransferQueue.firstDataNode();
                    nodeFirstDataNode = nodeFirstDataNode2;
                    if (nodeFirstDataNode2 == null) {
                        return false;
                    }
                }
                do {
                    Object obj2 = nodeFirstDataNode.item;
                    obj = obj2;
                    if (obj2 == nodeFirstDataNode) {
                        obj = null;
                    }
                    Node node2 = nodeFirstDataNode;
                    Node node3 = nodeFirstDataNode.next;
                    nodeFirstDataNode = node3;
                    if (node2 == node3) {
                        nodeFirstDataNode = linkedTransferQueue.firstDataNode();
                    }
                    if (obj != null || nodeFirstDataNode == null) {
                        break;
                    }
                } while (nodeFirstDataNode.isData);
                Node node4 = nodeFirstDataNode;
                this.current = node4;
                if (node4 == null) {
                    this.exhausted = true;
                }
                if (obj != null) {
                    consumer.accept(obj);
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
        return new LTQSpliterator(this);
    }

    final void unsplice(Node node, Node node2) {
        node2.forgetContents();
        if (node != null && node != node2 && node.next == node2) {
            Node node3 = node2.next;
            if (node3 != null && (node3 == node2 || !node.casNext(node2, node3) || !node.isMatched())) {
                return;
            }
            while (true) {
                Node node4 = this.head;
                if (node4 == node || node4 == node2 || node4 == null) {
                    return;
                }
                if (node4.isMatched()) {
                    Node node5 = node4.next;
                    if (node5 == null) {
                        return;
                    }
                    if (node5 != node4 && casHead(node4, node5)) {
                        node4.forgetNext();
                    }
                } else {
                    if (node.next == node || node2.next == node2) {
                        return;
                    }
                    while (true) {
                        int i2 = this.sweepVotes;
                        if (i2 < 32) {
                            if (casSweepVotes(i2, i2 + 1)) {
                                return;
                            }
                        } else if (casSweepVotes(i2, 0)) {
                            sweep();
                            return;
                        }
                    }
                }
            }
        }
    }

    private void sweep() {
        Node node;
        Node node2 = this.head;
        while (node2 != null && (node = node2.next) != null) {
            if (!node.isMatched()) {
                node2 = node;
            } else {
                Node node3 = node.next;
                if (node3 != null) {
                    if (node == node3) {
                        node2 = this.head;
                    } else {
                        node2.casNext(node, node3);
                    }
                } else {
                    return;
                }
            }
        }
    }

    private boolean findAndRemove(Object obj) {
        if (obj != null) {
            Node node = null;
            Node node2 = this.head;
            while (node2 != null) {
                Object obj2 = node2.item;
                if (node2.isData) {
                    if (obj2 != null && obj2 != node2 && obj.equals(obj2) && node2.tryMatchData()) {
                        unsplice(node, node2);
                        return true;
                    }
                } else if (obj2 == null) {
                    return false;
                }
                node = node2;
                Node node3 = node2.next;
                node2 = node3;
                if (node3 == node) {
                    node = null;
                    node2 = this.head;
                }
            }
            return false;
        }
        return false;
    }

    public LinkedTransferQueue() {
    }

    public LinkedTransferQueue(Collection<? extends E> collection) {
        this();
        addAll(collection);
    }

    @Override // java.util.concurrent.BlockingQueue
    public void put(E e2) {
        xfer(e2, true, 1, 0L);
    }

    @Override // java.util.concurrent.BlockingQueue
    public boolean offer(E e2, long j2, TimeUnit timeUnit) {
        xfer(e2, true, 1, 0L);
        return true;
    }

    @Override // java.util.Queue
    public boolean offer(E e2) {
        xfer(e2, true, 1, 0L);
        return true;
    }

    @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(E e2) {
        xfer(e2, true, 1, 0L);
        return true;
    }

    @Override // java.util.concurrent.TransferQueue
    public boolean tryTransfer(E e2) {
        return xfer(e2, true, 0, 0L) == null;
    }

    @Override // java.util.concurrent.TransferQueue
    public void transfer(E e2) throws InterruptedException {
        if (xfer(e2, true, 2, 0L) != null) {
            Thread.interrupted();
            throw new InterruptedException();
        }
    }

    @Override // java.util.concurrent.TransferQueue
    public boolean tryTransfer(E e2, long j2, TimeUnit timeUnit) throws InterruptedException {
        if (xfer(e2, true, 3, timeUnit.toNanos(j2)) == null) {
            return true;
        }
        if (!Thread.interrupted()) {
            return false;
        }
        throw new InterruptedException();
    }

    @Override // java.util.concurrent.BlockingQueue
    /* renamed from: take */
    public E take2() throws InterruptedException {
        E eXfer = xfer(null, false, 2, 0L);
        if (eXfer != null) {
            return eXfer;
        }
        Thread.interrupted();
        throw new InterruptedException();
    }

    @Override // java.util.concurrent.BlockingQueue
    /* renamed from: poll */
    public E poll2(long j2, TimeUnit timeUnit) throws InterruptedException {
        E eXfer = xfer(null, false, 3, timeUnit.toNanos(j2));
        if (eXfer != null || !Thread.interrupted()) {
            return eXfer;
        }
        throw new InterruptedException();
    }

    @Override // java.util.Queue
    public E poll() {
        return xfer(null, false, 0, 0L);
    }

    @Override // java.util.concurrent.BlockingQueue
    public int drainTo(Collection<? super E> collection) {
        if (collection == null) {
            throw new NullPointerException();
        }
        if (collection == this) {
            throw new IllegalArgumentException();
        }
        int i2 = 0;
        while (true) {
            E ePoll = poll();
            if (ePoll != null) {
                collection.add(ePoll);
                i2++;
            } else {
                return i2;
            }
        }
    }

    @Override // java.util.concurrent.BlockingQueue
    public int drainTo(Collection<? super E> collection, int i2) {
        E ePoll;
        if (collection == null) {
            throw new NullPointerException();
        }
        if (collection == this) {
            throw new IllegalArgumentException();
        }
        int i3 = 0;
        while (i3 < i2 && (ePoll = poll()) != null) {
            collection.add(ePoll);
            i3++;
        }
        return i3;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override // java.util.Queue
    public E peek() {
        return firstDataItem();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean isEmpty() {
        Node nodeSucc = this.head;
        while (true) {
            Node node = nodeSucc;
            if (node != null) {
                if (!node.isMatched()) {
                    return !node.isData;
                }
                nodeSucc = succ(node);
            } else {
                return true;
            }
        }
    }

    @Override // java.util.concurrent.TransferQueue
    public boolean hasWaitingConsumer() {
        return firstOfMode(false) != null;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return countOfMode(true);
    }

    @Override // java.util.concurrent.TransferQueue
    public int getWaitingConsumerCount() {
        return countOfMode(false);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object obj) {
        return findAndRemove(obj);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        if (obj == null) {
            return false;
        }
        Node nodeSucc = this.head;
        while (true) {
            Node node = nodeSucc;
            if (node != null) {
                Object obj2 = node.item;
                if (node.isData) {
                    if (obj2 != null && obj2 != node && obj.equals(obj2)) {
                        return true;
                    }
                } else if (obj2 == null) {
                    return false;
                }
                nodeSucc = succ(node);
            } else {
                return false;
            }
        }
    }

    @Override // java.util.concurrent.BlockingQueue
    public int remainingCapacity() {
        return Integer.MAX_VALUE;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            objectOutputStream.writeObject(it.next());
        }
        objectOutputStream.writeObject(null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        while (true) {
            Object object = objectInputStream.readObject();
            if (object != null) {
                offer(object);
            } else {
                return;
            }
        }
    }
}
