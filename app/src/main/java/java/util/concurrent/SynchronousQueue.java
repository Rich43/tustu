package java.util.concurrent;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/util/concurrent/SynchronousQueue.class */
public class SynchronousQueue<E> extends AbstractQueue<E> implements BlockingQueue<E>, Serializable {
    private static final long serialVersionUID = -3223113410248163686L;
    static final int NCPUS = Runtime.getRuntime().availableProcessors();
    static final int maxTimedSpins;
    static final int maxUntimedSpins;
    static final long spinForTimeoutThreshold = 1000;
    private volatile transient Transferer<E> transferer;
    private ReentrantLock qlock;
    private WaitQueue waitingProducers;
    private WaitQueue waitingConsumers;

    /* loaded from: rt.jar:java/util/concurrent/SynchronousQueue$Transferer.class */
    static abstract class Transferer<E> {
        abstract E transfer(E e2, boolean z2, long j2);

        Transferer() {
        }
    }

    static {
        maxTimedSpins = NCPUS < 2 ? 0 : 32;
        maxUntimedSpins = maxTimedSpins * 16;
    }

    /* loaded from: rt.jar:java/util/concurrent/SynchronousQueue$TransferStack.class */
    static final class TransferStack<E> extends Transferer<E> {
        static final int REQUEST = 0;
        static final int DATA = 1;
        static final int FULFILLING = 2;
        volatile SNode head;
        private static final Unsafe UNSAFE;
        private static final long headOffset;

        TransferStack() {
        }

        static boolean isFulfilling(int i2) {
            return (i2 & 2) != 0;
        }

        /* loaded from: rt.jar:java/util/concurrent/SynchronousQueue$TransferStack$SNode.class */
        static final class SNode {
            volatile SNode next;
            volatile SNode match;
            volatile Thread waiter;
            Object item;
            int mode;
            private static final Unsafe UNSAFE;
            private static final long matchOffset;
            private static final long nextOffset;

            SNode(Object obj) {
                this.item = obj;
            }

            boolean casNext(SNode sNode, SNode sNode2) {
                return sNode == this.next && UNSAFE.compareAndSwapObject(this, nextOffset, sNode, sNode2);
            }

            boolean tryMatch(SNode sNode) {
                if (this.match != null || !UNSAFE.compareAndSwapObject(this, matchOffset, null, sNode)) {
                    return this.match == sNode;
                }
                Thread thread = this.waiter;
                if (thread != null) {
                    this.waiter = null;
                    LockSupport.unpark(thread);
                    return true;
                }
                return true;
            }

            void tryCancel() {
                UNSAFE.compareAndSwapObject(this, matchOffset, null, this);
            }

            boolean isCancelled() {
                return this.match == this;
            }

            static {
                try {
                    UNSAFE = Unsafe.getUnsafe();
                    matchOffset = UNSAFE.objectFieldOffset(SNode.class.getDeclaredField(Constants.ATTRNAME_MATCH));
                    nextOffset = UNSAFE.objectFieldOffset(SNode.class.getDeclaredField(com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.NEXT));
                } catch (Exception e2) {
                    throw new Error(e2);
                }
            }
        }

        boolean casHead(SNode sNode, SNode sNode2) {
            return sNode == this.head && UNSAFE.compareAndSwapObject(this, headOffset, sNode, sNode2);
        }

        static SNode snode(SNode sNode, Object obj, SNode sNode2, int i2) {
            if (sNode == null) {
                sNode = new SNode(obj);
            }
            sNode.mode = i2;
            sNode.next = sNode2;
            return sNode;
        }

        @Override // java.util.concurrent.SynchronousQueue.Transferer
        E transfer(E e2, boolean z2, long j2) {
            SNode sNode = null;
            int i2 = e2 == null ? 0 : 1;
            while (true) {
                SNode sNode2 = this.head;
                if (sNode2 == null || sNode2.mode == i2) {
                    if (z2 && j2 <= 0) {
                        if (sNode2 != null && sNode2.isCancelled()) {
                            casHead(sNode2, sNode2.next);
                        } else {
                            return null;
                        }
                    } else {
                        SNode snode = snode(sNode, e2, sNode2, i2);
                        sNode = snode;
                        if (casHead(sNode2, snode)) {
                            SNode sNodeAwaitFulfill = awaitFulfill(sNode, z2, j2);
                            if (sNodeAwaitFulfill == sNode) {
                                clean(sNode);
                                return null;
                            }
                            SNode sNode3 = this.head;
                            if (sNode3 != null && sNode3.next == sNode) {
                                casHead(sNode3, sNode.next);
                            }
                            return i2 == 0 ? (E) sNodeAwaitFulfill.item : (E) sNode.item;
                        }
                    }
                } else if (!isFulfilling(sNode2.mode)) {
                    if (sNode2.isCancelled()) {
                        casHead(sNode2, sNode2.next);
                    } else {
                        SNode snode2 = snode(sNode, e2, sNode2, 2 | i2);
                        sNode = snode2;
                        if (casHead(sNode2, snode2)) {
                            while (true) {
                                SNode sNode4 = sNode.next;
                                if (sNode4 == null) {
                                    casHead(sNode, null);
                                    sNode = null;
                                    break;
                                }
                                SNode sNode5 = sNode4.next;
                                if (sNode4.tryMatch(sNode)) {
                                    casHead(sNode, sNode5);
                                    return i2 == 0 ? (E) sNode4.item : (E) sNode.item;
                                }
                                sNode.casNext(sNode4, sNode5);
                            }
                        } else {
                            continue;
                        }
                    }
                } else {
                    SNode sNode6 = sNode2.next;
                    if (sNode6 == null) {
                        casHead(sNode2, null);
                    } else {
                        SNode sNode7 = sNode6.next;
                        if (sNode6.tryMatch(sNode2)) {
                            casHead(sNode2, sNode7);
                        } else {
                            sNode2.casNext(sNode6, sNode7);
                        }
                    }
                }
            }
        }

        SNode awaitFulfill(SNode sNode, boolean z2, long j2) {
            long jNanoTime = z2 ? System.nanoTime() + j2 : 0L;
            Thread threadCurrentThread = Thread.currentThread();
            int i2 = shouldSpin(sNode) ? z2 ? SynchronousQueue.maxTimedSpins : SynchronousQueue.maxUntimedSpins : 0;
            while (true) {
                if (threadCurrentThread.isInterrupted()) {
                    sNode.tryCancel();
                }
                SNode sNode2 = sNode.match;
                if (sNode2 != null) {
                    return sNode2;
                }
                if (z2) {
                    j2 = jNanoTime - System.nanoTime();
                    if (j2 <= 0) {
                        sNode.tryCancel();
                    }
                }
                if (i2 > 0) {
                    i2 = shouldSpin(sNode) ? i2 - 1 : 0;
                } else if (sNode.waiter == null) {
                    sNode.waiter = threadCurrentThread;
                } else if (!z2) {
                    LockSupport.park(this);
                } else if (j2 > 1000) {
                    LockSupport.parkNanos(this, j2);
                }
            }
        }

        boolean shouldSpin(SNode sNode) {
            SNode sNode2 = this.head;
            return sNode2 == sNode || sNode2 == null || isFulfilling(sNode2.mode);
        }

        void clean(SNode sNode) {
            SNode sNode2;
            sNode.item = null;
            sNode.waiter = null;
            SNode sNode3 = sNode.next;
            if (sNode3 != null && sNode3.isCancelled()) {
                sNode3 = sNode3.next;
            }
            while (true) {
                SNode sNode4 = this.head;
                sNode2 = sNode4;
                if (sNode4 == null || sNode2 == sNode3 || !sNode2.isCancelled()) {
                    break;
                } else {
                    casHead(sNode2, sNode2.next);
                }
            }
            while (sNode2 != null && sNode2 != sNode3) {
                SNode sNode5 = sNode2.next;
                if (sNode5 != null && sNode5.isCancelled()) {
                    sNode2.casNext(sNode5, sNode5.next);
                } else {
                    sNode2 = sNode5;
                }
            }
        }

        static {
            try {
                UNSAFE = Unsafe.getUnsafe();
                headOffset = UNSAFE.objectFieldOffset(TransferStack.class.getDeclaredField("head"));
            } catch (Exception e2) {
                throw new Error(e2);
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/SynchronousQueue$TransferQueue.class */
    static final class TransferQueue<E> extends Transferer<E> {
        volatile transient QNode head;
        volatile transient QNode tail;
        volatile transient QNode cleanMe;
        private static final Unsafe UNSAFE;
        private static final long headOffset;
        private static final long tailOffset;
        private static final long cleanMeOffset;

        /* loaded from: rt.jar:java/util/concurrent/SynchronousQueue$TransferQueue$QNode.class */
        static final class QNode {
            volatile QNode next;
            volatile Object item;
            volatile Thread waiter;
            final boolean isData;
            private static final Unsafe UNSAFE;
            private static final long itemOffset;
            private static final long nextOffset;

            QNode(Object obj, boolean z2) {
                this.item = obj;
                this.isData = z2;
            }

            boolean casNext(QNode qNode, QNode qNode2) {
                return this.next == qNode && UNSAFE.compareAndSwapObject(this, nextOffset, qNode, qNode2);
            }

            boolean casItem(Object obj, Object obj2) {
                return this.item == obj && UNSAFE.compareAndSwapObject(this, itemOffset, obj, obj2);
            }

            void tryCancel(Object obj) {
                UNSAFE.compareAndSwapObject(this, itemOffset, obj, this);
            }

            boolean isCancelled() {
                return this.item == this;
            }

            boolean isOffList() {
                return this.next == this;
            }

            static {
                try {
                    UNSAFE = Unsafe.getUnsafe();
                    itemOffset = UNSAFE.objectFieldOffset(QNode.class.getDeclaredField("item"));
                    nextOffset = UNSAFE.objectFieldOffset(QNode.class.getDeclaredField(com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.NEXT));
                } catch (Exception e2) {
                    throw new Error(e2);
                }
            }
        }

        TransferQueue() {
            QNode qNode = new QNode(null, false);
            this.head = qNode;
            this.tail = qNode;
        }

        void advanceHead(QNode qNode, QNode qNode2) {
            if (qNode == this.head && UNSAFE.compareAndSwapObject(this, headOffset, qNode, qNode2)) {
                qNode.next = qNode;
            }
        }

        void advanceTail(QNode qNode, QNode qNode2) {
            if (this.tail == qNode) {
                UNSAFE.compareAndSwapObject(this, tailOffset, qNode, qNode2);
            }
        }

        boolean casCleanMe(QNode qNode, QNode qNode2) {
            return this.cleanMe == qNode && UNSAFE.compareAndSwapObject(this, cleanMeOffset, qNode, qNode2);
        }

        @Override // java.util.concurrent.SynchronousQueue.Transferer
        E transfer(E e2, boolean z2, long j2) {
            QNode qNode = null;
            boolean z3 = e2 != null;
            while (true) {
                QNode qNode2 = this.tail;
                QNode qNode3 = this.head;
                if (qNode2 != null && qNode3 != null) {
                    if (qNode3 == qNode2 || qNode2.isData == z3) {
                        QNode qNode4 = qNode2.next;
                        if (qNode2 != this.tail) {
                            continue;
                        } else if (qNode4 != null) {
                            advanceTail(qNode2, qNode4);
                        } else {
                            if (z2 && j2 <= 0) {
                                return null;
                            }
                            if (qNode == null) {
                                qNode = new QNode(e2, z3);
                            }
                            if (qNode2.casNext(null, qNode)) {
                                advanceTail(qNode2, qNode);
                                E e3 = (E) awaitFulfill(qNode, e2, z2, j2);
                                if (e3 == qNode) {
                                    clean(qNode2, qNode);
                                    return null;
                                }
                                if (!qNode.isOffList()) {
                                    advanceHead(qNode2, qNode);
                                    if (e3 != null) {
                                        qNode.item = qNode;
                                    }
                                    qNode.waiter = null;
                                }
                                return e3 != null ? e3 : e2;
                            }
                        }
                    } else {
                        QNode qNode5 = qNode3.next;
                        if (qNode2 == this.tail && qNode5 != null && qNode3 == this.head) {
                            E e4 = (E) qNode5.item;
                            if (z3 == (e4 != null) || e4 == qNode5 || !qNode5.casItem(e4, e2)) {
                                advanceHead(qNode3, qNode5);
                            } else {
                                advanceHead(qNode3, qNode5);
                                LockSupport.unpark(qNode5.waiter);
                                return e4 != null ? e4 : e2;
                            }
                        }
                    }
                }
            }
        }

        Object awaitFulfill(QNode qNode, E e2, boolean z2, long j2) {
            long jNanoTime = z2 ? System.nanoTime() + j2 : 0L;
            Thread threadCurrentThread = Thread.currentThread();
            int i2 = this.head.next == qNode ? z2 ? SynchronousQueue.maxTimedSpins : SynchronousQueue.maxUntimedSpins : 0;
            while (true) {
                if (threadCurrentThread.isInterrupted()) {
                    qNode.tryCancel(e2);
                }
                Object obj = qNode.item;
                if (obj != e2) {
                    return obj;
                }
                if (z2) {
                    j2 = jNanoTime - System.nanoTime();
                    if (j2 <= 0) {
                        qNode.tryCancel(e2);
                    }
                }
                if (i2 > 0) {
                    i2--;
                } else if (qNode.waiter == null) {
                    qNode.waiter = threadCurrentThread;
                } else if (!z2) {
                    LockSupport.park(this);
                } else if (j2 > 1000) {
                    LockSupport.parkNanos(this, j2);
                }
            }
        }

        void clean(QNode qNode, QNode qNode2) {
            QNode qNode3;
            QNode qNode4;
            qNode2.waiter = null;
            while (qNode.next == qNode2) {
                QNode qNode5 = this.head;
                QNode qNode6 = qNode5.next;
                if (qNode6 != null && qNode6.isCancelled()) {
                    advanceHead(qNode5, qNode6);
                } else {
                    QNode qNode7 = this.tail;
                    if (qNode7 == qNode5) {
                        return;
                    }
                    QNode qNode8 = qNode7.next;
                    if (qNode7 != this.tail) {
                        continue;
                    } else if (qNode8 != null) {
                        advanceTail(qNode7, qNode8);
                    } else {
                        if (qNode2 != qNode7 && ((qNode4 = qNode2.next) == qNode2 || qNode.casNext(qNode2, qNode4))) {
                            return;
                        }
                        QNode qNode9 = this.cleanMe;
                        if (qNode9 != null) {
                            QNode qNode10 = qNode9.next;
                            if (qNode10 == null || qNode10 == qNode9 || !qNode10.isCancelled() || (qNode10 != qNode7 && (qNode3 = qNode10.next) != null && qNode3 != qNode10 && qNode9.casNext(qNode10, qNode3))) {
                                casCleanMe(qNode9, null);
                            }
                            if (qNode9 == qNode) {
                                return;
                            }
                        } else if (casCleanMe(null, qNode)) {
                            return;
                        }
                    }
                }
            }
        }

        static {
            try {
                UNSAFE = Unsafe.getUnsafe();
                headOffset = UNSAFE.objectFieldOffset(TransferQueue.class.getDeclaredField("head"));
                tailOffset = UNSAFE.objectFieldOffset(TransferQueue.class.getDeclaredField("tail"));
                cleanMeOffset = UNSAFE.objectFieldOffset(TransferQueue.class.getDeclaredField("cleanMe"));
            } catch (Exception e2) {
                throw new Error(e2);
            }
        }
    }

    public SynchronousQueue() {
        this(false);
    }

    public SynchronousQueue(boolean z2) {
        this.transferer = z2 ? new TransferQueue<>() : new TransferStack<>();
    }

    @Override // java.util.concurrent.BlockingQueue
    public void put(E e2) throws InterruptedException {
        if (e2 == null) {
            throw new NullPointerException();
        }
        if (this.transferer.transfer(e2, false, 0L) == null) {
            Thread.interrupted();
            throw new InterruptedException();
        }
    }

    @Override // java.util.concurrent.BlockingQueue
    public boolean offer(E e2, long j2, TimeUnit timeUnit) throws InterruptedException {
        if (e2 == null) {
            throw new NullPointerException();
        }
        if (this.transferer.transfer(e2, true, timeUnit.toNanos(j2)) != null) {
            return true;
        }
        if (!Thread.interrupted()) {
            return false;
        }
        throw new InterruptedException();
    }

    @Override // java.util.Queue
    public boolean offer(E e2) {
        if (e2 == null) {
            throw new NullPointerException();
        }
        return this.transferer.transfer(e2, true, 0L) != null;
    }

    @Override // java.util.concurrent.BlockingQueue
    /* renamed from: take */
    public E take2() throws InterruptedException {
        E eTransfer = this.transferer.transfer(null, false, 0L);
        if (eTransfer != null) {
            return eTransfer;
        }
        Thread.interrupted();
        throw new InterruptedException();
    }

    @Override // java.util.concurrent.BlockingQueue
    /* renamed from: poll */
    public E poll2(long j2, TimeUnit timeUnit) throws InterruptedException {
        E eTransfer = this.transferer.transfer(null, true, timeUnit.toNanos(j2));
        if (eTransfer != null || !Thread.interrupted()) {
            return eTransfer;
        }
        throw new InterruptedException();
    }

    @Override // java.util.Queue
    public E poll() {
        return this.transferer.transfer(null, true, 0L);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean isEmpty() {
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return 0;
    }

    @Override // java.util.concurrent.BlockingQueue
    public int remainingCapacity() {
        return 0;
    }

    @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object obj) {
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean containsAll(Collection<?> collection) {
        return collection.isEmpty();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean removeAll(Collection<?> collection) {
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean retainAll(Collection<?> collection) {
        return false;
    }

    @Override // java.util.Queue
    public E peek() {
        return null;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<E> iterator() {
        return Collections.emptyIterator();
    }

    @Override // java.util.Collection, java.lang.Iterable
    public Spliterator<E> spliterator() {
        return Spliterators.emptySpliterator();
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public Object[] toArray() {
        return new Object[0];
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        if (tArr.length > 0) {
            tArr[0] = null;
        }
        return tArr;
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

    /* loaded from: rt.jar:java/util/concurrent/SynchronousQueue$WaitQueue.class */
    static class WaitQueue implements Serializable {
        WaitQueue() {
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/SynchronousQueue$LifoWaitQueue.class */
    static class LifoWaitQueue extends WaitQueue {
        private static final long serialVersionUID = -3633113410248163686L;

        LifoWaitQueue() {
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/SynchronousQueue$FifoWaitQueue.class */
    static class FifoWaitQueue extends WaitQueue {
        private static final long serialVersionUID = -3623113410248163686L;

        FifoWaitQueue() {
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (this.transferer instanceof TransferQueue) {
            this.qlock = new ReentrantLock(true);
            this.waitingProducers = new FifoWaitQueue();
            this.waitingConsumers = new FifoWaitQueue();
        } else {
            this.qlock = new ReentrantLock();
            this.waitingProducers = new LifoWaitQueue();
            this.waitingConsumers = new LifoWaitQueue();
        }
        objectOutputStream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.waitingProducers instanceof FifoWaitQueue) {
            this.transferer = new TransferQueue();
        } else {
            this.transferer = new TransferStack();
        }
    }

    static long objectFieldOffset(Unsafe unsafe, String str, Class<?> cls) {
        try {
            return unsafe.objectFieldOffset(cls.getDeclaredField(str));
        } catch (NoSuchFieldException e2) {
            NoSuchFieldError noSuchFieldError = new NoSuchFieldError(str);
            noSuchFieldError.initCause(e2);
            throw noSuchFieldError;
        }
    }
}
