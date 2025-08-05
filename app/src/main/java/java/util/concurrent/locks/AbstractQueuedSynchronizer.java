package java.util.concurrent.locks;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/util/concurrent/locks/AbstractQueuedSynchronizer.class */
public abstract class AbstractQueuedSynchronizer extends AbstractOwnableSynchronizer implements Serializable {
    private static final long serialVersionUID = 7373984972572414691L;
    private volatile transient Node head;
    private volatile transient Node tail;
    private volatile int state;
    static final long spinForTimeoutThreshold = 1000;
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final long stateOffset;
    private static final long headOffset;
    private static final long tailOffset;
    private static final long waitStatusOffset;
    private static final long nextOffset;

    protected AbstractQueuedSynchronizer() {
    }

    /* loaded from: rt.jar:java/util/concurrent/locks/AbstractQueuedSynchronizer$Node.class */
    static final class Node {
        static final Node SHARED = new Node();
        static final Node EXCLUSIVE = null;
        static final int CANCELLED = 1;
        static final int SIGNAL = -1;
        static final int CONDITION = -2;
        static final int PROPAGATE = -3;
        volatile int waitStatus;
        volatile Node prev;
        volatile Node next;
        volatile Thread thread;
        Node nextWaiter;

        final boolean isShared() {
            return this.nextWaiter == SHARED;
        }

        final Node predecessor() throws NullPointerException {
            Node node = this.prev;
            if (node == null) {
                throw new NullPointerException();
            }
            return node;
        }

        Node() {
        }

        Node(Thread thread, Node node) {
            this.nextWaiter = node;
            this.thread = thread;
        }

        Node(Thread thread, int i2) {
            this.waitStatus = i2;
            this.thread = thread;
        }
    }

    protected final int getState() {
        return this.state;
    }

    protected final void setState(int i2) {
        this.state = i2;
    }

    protected final boolean compareAndSetState(int i2, int i3) {
        return unsafe.compareAndSwapInt(this, stateOffset, i2, i3);
    }

    private Node enq(Node node) {
        while (true) {
            Node node2 = this.tail;
            if (node2 == null) {
                if (compareAndSetHead(new Node())) {
                    this.tail = this.head;
                }
            } else {
                node.prev = node2;
                if (compareAndSetTail(node2, node)) {
                    node2.next = node;
                    return node2;
                }
            }
        }
    }

    private Node addWaiter(Node node) {
        Node node2 = new Node(Thread.currentThread(), node);
        Node node3 = this.tail;
        if (node3 != null) {
            node2.prev = node3;
            if (compareAndSetTail(node3, node2)) {
                node3.next = node2;
                return node2;
            }
        }
        enq(node2);
        return node2;
    }

    private void setHead(Node node) {
        this.head = node;
        node.thread = null;
        node.prev = null;
    }

    private void unparkSuccessor(Node node) {
        int i2 = node.waitStatus;
        if (i2 < 0) {
            compareAndSetWaitStatus(node, i2, 0);
        }
        Node node2 = node.next;
        if (node2 == null || node2.waitStatus > 0) {
            node2 = null;
            Node node3 = this.tail;
            while (true) {
                Node node4 = node3;
                if (node4 == null || node4 == node) {
                    break;
                }
                if (node4.waitStatus <= 0) {
                    node2 = node4;
                }
                node3 = node4.prev;
            }
        }
        if (node2 != null) {
            LockSupport.unpark(node2.thread);
        }
    }

    private void doReleaseShared() {
        while (true) {
            Node node = this.head;
            if (node != null && node != this.tail) {
                int i2 = node.waitStatus;
                if (i2 == -1) {
                    if (compareAndSetWaitStatus(node, -1, 0)) {
                        unparkSuccessor(node);
                    } else {
                        continue;
                    }
                } else if (i2 != 0 || compareAndSetWaitStatus(node, 0, -3)) {
                }
            }
            if (node == this.head) {
                return;
            }
        }
    }

    private void setHeadAndPropagate(Node node, int i2) {
        Node node2;
        Node node3 = this.head;
        setHead(node);
        if (i2 > 0 || node3 == null || node3.waitStatus < 0 || (node2 = this.head) == null || node2.waitStatus < 0) {
            Node node4 = node.next;
            if (node4 == null || node4.isShared()) {
                doReleaseShared();
            }
        }
    }

    private void cancelAcquire(Node node) {
        int i2;
        if (node == null) {
            return;
        }
        node.thread = null;
        Node node2 = node.prev;
        while (node2.waitStatus > 0) {
            Node node3 = node2.prev;
            node2 = node3;
            node.prev = node3;
        }
        Node node4 = node2.next;
        node.waitStatus = 1;
        if (node == this.tail && compareAndSetTail(node, node2)) {
            compareAndSetNext(node2, node4, null);
            return;
        }
        if (node2 != this.head && (((i2 = node2.waitStatus) == -1 || (i2 <= 0 && compareAndSetWaitStatus(node2, i2, -1))) && node2.thread != null)) {
            Node node5 = node.next;
            if (node5 != null && node5.waitStatus <= 0) {
                compareAndSetNext(node2, node4, node5);
            }
        } else {
            unparkSuccessor(node);
        }
        node.next = node;
    }

    private static boolean shouldParkAfterFailedAcquire(Node node, Node node2) {
        int i2 = node.waitStatus;
        if (i2 == -1) {
            return true;
        }
        if (i2 > 0) {
            do {
                Node node3 = node.prev;
                node = node3;
                node2.prev = node3;
            } while (node.waitStatus > 0);
            node.next = node2;
            return false;
        }
        compareAndSetWaitStatus(node, i2, -1);
        return false;
    }

    static void selfInterrupt() {
        Thread.currentThread().interrupt();
    }

    private final boolean parkAndCheckInterrupt() {
        LockSupport.park(this);
        return Thread.interrupted();
    }

    final boolean acquireQueued(Node node, int i2) {
        Node nodePredecessor;
        boolean z2 = true;
        boolean z3 = false;
        while (true) {
            try {
                nodePredecessor = node.predecessor();
                if (nodePredecessor == this.head && tryAcquire(i2)) {
                    break;
                }
                if (shouldParkAfterFailedAcquire(nodePredecessor, node) && parkAndCheckInterrupt()) {
                    z3 = true;
                }
            } catch (Throwable th) {
                if (z2) {
                    cancelAcquire(node);
                }
                throw th;
            }
        }
        setHead(node);
        nodePredecessor.next = null;
        z2 = false;
        boolean z4 = z3;
        if (0 != 0) {
            cancelAcquire(node);
        }
        return z4;
    }

    private void doAcquireInterruptibly(int i2) throws InterruptedException {
        Node nodeAddWaiter = addWaiter(Node.EXCLUSIVE);
        while (true) {
            try {
                Node nodePredecessor = nodeAddWaiter.predecessor();
                if (nodePredecessor == this.head && tryAcquire(i2)) {
                    setHead(nodeAddWaiter);
                    nodePredecessor.next = null;
                    if (0 == 0) {
                        return;
                    }
                    cancelAcquire(nodeAddWaiter);
                    return;
                }
                if (shouldParkAfterFailedAcquire(nodePredecessor, nodeAddWaiter) && parkAndCheckInterrupt()) {
                    throw new InterruptedException();
                }
            } catch (Throwable th) {
                if (1 != 0) {
                    cancelAcquire(nodeAddWaiter);
                }
                throw th;
            }
        }
    }

    private boolean doAcquireNanos(int i2, long j2) throws InterruptedException {
        if (j2 <= 0) {
            return false;
        }
        long jNanoTime = System.nanoTime() + j2;
        Node nodeAddWaiter = addWaiter(Node.EXCLUSIVE);
        do {
            try {
                Node nodePredecessor = nodeAddWaiter.predecessor();
                if (nodePredecessor == this.head && tryAcquire(i2)) {
                    setHead(nodeAddWaiter);
                    nodePredecessor.next = null;
                    if (0 != 0) {
                        cancelAcquire(nodeAddWaiter);
                    }
                    return true;
                }
                long jNanoTime2 = jNanoTime - System.nanoTime();
                if (jNanoTime2 <= 0) {
                    return false;
                }
                if (shouldParkAfterFailedAcquire(nodePredecessor, nodeAddWaiter) && jNanoTime2 > 1000) {
                    LockSupport.parkNanos(this, jNanoTime2);
                }
            } finally {
                if (1 != 0) {
                    cancelAcquire(nodeAddWaiter);
                }
            }
        } while (!Thread.interrupted());
        throw new InterruptedException();
    }

    private void doAcquireShared(int i2) {
        Node nodePredecessor;
        int iTryAcquireShared;
        Node nodeAddWaiter = addWaiter(Node.SHARED);
        boolean z2 = false;
        while (true) {
            try {
                nodePredecessor = nodeAddWaiter.predecessor();
                if (nodePredecessor == this.head && (iTryAcquireShared = tryAcquireShared(i2)) >= 0) {
                    break;
                } else if (shouldParkAfterFailedAcquire(nodePredecessor, nodeAddWaiter) && parkAndCheckInterrupt()) {
                    z2 = true;
                }
            } catch (Throwable th) {
                if (1 != 0) {
                    cancelAcquire(nodeAddWaiter);
                }
                throw th;
            }
        }
        setHeadAndPropagate(nodeAddWaiter, iTryAcquireShared);
        nodePredecessor.next = null;
        if (z2) {
            selfInterrupt();
        }
        if (0 == 0) {
            return;
        }
        cancelAcquire(nodeAddWaiter);
    }

    private void doAcquireSharedInterruptibly(int i2) throws InterruptedException {
        int iTryAcquireShared;
        Node nodeAddWaiter = addWaiter(Node.SHARED);
        while (true) {
            try {
                Node nodePredecessor = nodeAddWaiter.predecessor();
                if (nodePredecessor == this.head && (iTryAcquireShared = tryAcquireShared(i2)) >= 0) {
                    setHeadAndPropagate(nodeAddWaiter, iTryAcquireShared);
                    nodePredecessor.next = null;
                    if (0 == 0) {
                        return;
                    }
                    cancelAcquire(nodeAddWaiter);
                    return;
                }
                if (shouldParkAfterFailedAcquire(nodePredecessor, nodeAddWaiter) && parkAndCheckInterrupt()) {
                    throw new InterruptedException();
                }
            } catch (Throwable th) {
                if (1 != 0) {
                    cancelAcquire(nodeAddWaiter);
                }
                throw th;
            }
        }
    }

    private boolean doAcquireSharedNanos(int i2, long j2) throws InterruptedException {
        int iTryAcquireShared;
        if (j2 <= 0) {
            return false;
        }
        long jNanoTime = System.nanoTime() + j2;
        Node nodeAddWaiter = addWaiter(Node.SHARED);
        do {
            try {
                Node nodePredecessor = nodeAddWaiter.predecessor();
                if (nodePredecessor == this.head && (iTryAcquireShared = tryAcquireShared(i2)) >= 0) {
                    setHeadAndPropagate(nodeAddWaiter, iTryAcquireShared);
                    nodePredecessor.next = null;
                    if (0 != 0) {
                        cancelAcquire(nodeAddWaiter);
                    }
                    return true;
                }
                long jNanoTime2 = jNanoTime - System.nanoTime();
                if (jNanoTime2 <= 0) {
                    return false;
                }
                if (shouldParkAfterFailedAcquire(nodePredecessor, nodeAddWaiter) && jNanoTime2 > 1000) {
                    LockSupport.parkNanos(this, jNanoTime2);
                }
            } finally {
                if (1 != 0) {
                    cancelAcquire(nodeAddWaiter);
                }
            }
        } while (!Thread.interrupted());
        throw new InterruptedException();
    }

    protected boolean tryAcquire(int i2) {
        throw new UnsupportedOperationException();
    }

    protected boolean tryRelease(int i2) {
        throw new UnsupportedOperationException();
    }

    protected int tryAcquireShared(int i2) {
        throw new UnsupportedOperationException();
    }

    protected boolean tryReleaseShared(int i2) {
        throw new UnsupportedOperationException();
    }

    protected boolean isHeldExclusively() {
        throw new UnsupportedOperationException();
    }

    public final void acquire(int i2) {
        if (!tryAcquire(i2) && acquireQueued(addWaiter(Node.EXCLUSIVE), i2)) {
            selfInterrupt();
        }
    }

    public final void acquireInterruptibly(int i2) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        if (!tryAcquire(i2)) {
            doAcquireInterruptibly(i2);
        }
    }

    public final boolean tryAcquireNanos(int i2, long j2) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        return tryAcquire(i2) || doAcquireNanos(i2, j2);
    }

    public final boolean release(int i2) {
        if (tryRelease(i2)) {
            Node node = this.head;
            if (node != null && node.waitStatus != 0) {
                unparkSuccessor(node);
                return true;
            }
            return true;
        }
        return false;
    }

    public final void acquireShared(int i2) {
        if (tryAcquireShared(i2) < 0) {
            doAcquireShared(i2);
        }
    }

    public final void acquireSharedInterruptibly(int i2) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        if (tryAcquireShared(i2) < 0) {
            doAcquireSharedInterruptibly(i2);
        }
    }

    public final boolean tryAcquireSharedNanos(int i2, long j2) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        return tryAcquireShared(i2) >= 0 || doAcquireSharedNanos(i2, j2);
    }

    public final boolean releaseShared(int i2) {
        if (tryReleaseShared(i2)) {
            doReleaseShared();
            return true;
        }
        return false;
    }

    public final boolean hasQueuedThreads() {
        return this.head != this.tail;
    }

    public final boolean hasContended() {
        return this.head != null;
    }

    public final Thread getFirstQueuedThread() {
        if (this.head == this.tail) {
            return null;
        }
        return fullGetFirstQueuedThread();
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0049, code lost:
    
        if (r0 != null) goto L18;
     */
    /* JADX WARN: Removed duplicated region for block: B:10:0x0026  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.Thread fullGetFirstQueuedThread() {
        /*
            r3 = this;
            r0 = r3
            java.util.concurrent.locks.AbstractQueuedSynchronizer$Node r0 = r0.head
            r1 = r0
            r4 = r1
            if (r0 == 0) goto L26
            r0 = r4
            java.util.concurrent.locks.AbstractQueuedSynchronizer$Node r0 = r0.next
            r1 = r0
            r5 = r1
            if (r0 == 0) goto L26
            r0 = r5
            java.util.concurrent.locks.AbstractQueuedSynchronizer$Node r0 = r0.prev
            r1 = r3
            java.util.concurrent.locks.AbstractQueuedSynchronizer$Node r1 = r1.head
            if (r0 != r1) goto L26
            r0 = r5
            java.lang.Thread r0 = r0.thread
            r1 = r0
            r6 = r1
            if (r0 != 0) goto L4c
        L26:
            r0 = r3
            java.util.concurrent.locks.AbstractQueuedSynchronizer$Node r0 = r0.head
            r1 = r0
            r4 = r1
            if (r0 == 0) goto L4e
            r0 = r4
            java.util.concurrent.locks.AbstractQueuedSynchronizer$Node r0 = r0.next
            r1 = r0
            r5 = r1
            if (r0 == 0) goto L4e
            r0 = r5
            java.util.concurrent.locks.AbstractQueuedSynchronizer$Node r0 = r0.prev
            r1 = r3
            java.util.concurrent.locks.AbstractQueuedSynchronizer$Node r1 = r1.head
            if (r0 != r1) goto L4e
            r0 = r5
            java.lang.Thread r0 = r0.thread
            r1 = r0
            r6 = r1
            if (r0 == 0) goto L4e
        L4c:
            r0 = r6
            return r0
        L4e:
            r0 = r3
            java.util.concurrent.locks.AbstractQueuedSynchronizer$Node r0 = r0.tail
            r7 = r0
            r0 = 0
            r8 = r0
        L57:
            r0 = r7
            if (r0 == 0) goto L7f
            r0 = r7
            r1 = r3
            java.util.concurrent.locks.AbstractQueuedSynchronizer$Node r1 = r1.head
            if (r0 == r1) goto L7f
            r0 = r7
            java.lang.Thread r0 = r0.thread
            r9 = r0
            r0 = r9
            if (r0 == 0) goto L75
            r0 = r9
            r8 = r0
        L75:
            r0 = r7
            java.util.concurrent.locks.AbstractQueuedSynchronizer$Node r0 = r0.prev
            r7 = r0
            goto L57
        L7f:
            r0 = r8
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.AbstractQueuedSynchronizer.fullGetFirstQueuedThread():java.lang.Thread");
    }

    public final boolean isQueued(Thread thread) {
        if (thread == null) {
            throw new NullPointerException();
        }
        Node node = this.tail;
        while (true) {
            Node node2 = node;
            if (node2 != null) {
                if (node2.thread != thread) {
                    node = node2.prev;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    final boolean apparentlyFirstQueuedIsExclusive() {
        Node node;
        Node node2 = this.head;
        return (node2 == null || (node = node2.next) == null || node.isShared() || node.thread == null) ? false : true;
    }

    public final boolean hasQueuedPredecessors() {
        Node node;
        Node node2 = this.tail;
        Node node3 = this.head;
        return node3 != node2 && ((node = node3.next) == null || node.thread != Thread.currentThread());
    }

    public final int getQueueLength() {
        int i2 = 0;
        Node node = this.tail;
        while (true) {
            Node node2 = node;
            if (node2 != null) {
                if (node2.thread != null) {
                    i2++;
                }
                node = node2.prev;
            } else {
                return i2;
            }
        }
    }

    public final Collection<Thread> getQueuedThreads() {
        ArrayList arrayList = new ArrayList();
        Node node = this.tail;
        while (true) {
            Node node2 = node;
            if (node2 != null) {
                Thread thread = node2.thread;
                if (thread != null) {
                    arrayList.add(thread);
                }
                node = node2.prev;
            } else {
                return arrayList;
            }
        }
    }

    public final Collection<Thread> getExclusiveQueuedThreads() {
        Thread thread;
        ArrayList arrayList = new ArrayList();
        Node node = this.tail;
        while (true) {
            Node node2 = node;
            if (node2 != null) {
                if (!node2.isShared() && (thread = node2.thread) != null) {
                    arrayList.add(thread);
                }
                node = node2.prev;
            } else {
                return arrayList;
            }
        }
    }

    public final Collection<Thread> getSharedQueuedThreads() {
        Thread thread;
        ArrayList arrayList = new ArrayList();
        Node node = this.tail;
        while (true) {
            Node node2 = node;
            if (node2 != null) {
                if (node2.isShared() && (thread = node2.thread) != null) {
                    arrayList.add(thread);
                }
                node = node2.prev;
            } else {
                return arrayList;
            }
        }
    }

    public String toString() {
        return super.toString() + "[State = " + getState() + ", " + (hasQueuedThreads() ? "non" : "") + "empty queue]";
    }

    final boolean isOnSyncQueue(Node node) {
        if (node.waitStatus == -2 || node.prev == null) {
            return false;
        }
        if (node.next != null) {
            return true;
        }
        return findNodeFromTail(node);
    }

    private boolean findNodeFromTail(Node node) {
        Node node2 = this.tail;
        while (true) {
            Node node3 = node2;
            if (node3 == node) {
                return true;
            }
            if (node3 == null) {
                return false;
            }
            node2 = node3.prev;
        }
    }

    final boolean transferForSignal(Node node) {
        if (!compareAndSetWaitStatus(node, -2, 0)) {
            return false;
        }
        Node nodeEnq = enq(node);
        int i2 = nodeEnq.waitStatus;
        if (i2 > 0 || !compareAndSetWaitStatus(nodeEnq, i2, -1)) {
            LockSupport.unpark(node.thread);
            return true;
        }
        return true;
    }

    final boolean transferAfterCancelledWait(Node node) {
        if (compareAndSetWaitStatus(node, -2, 0)) {
            enq(node);
            return true;
        }
        while (!isOnSyncQueue(node)) {
            Thread.yield();
        }
        return false;
    }

    final int fullyRelease(Node node) {
        try {
            int state = getState();
            if (!release(state)) {
                throw new IllegalMonitorStateException();
            }
            if (0 != 0) {
                node.waitStatus = 1;
            }
            return state;
        } catch (Throwable th) {
            if (1 != 0) {
                node.waitStatus = 1;
            }
            throw th;
        }
    }

    public final boolean owns(ConditionObject conditionObject) {
        return conditionObject.isOwnedBy(this);
    }

    public final boolean hasWaiters(ConditionObject conditionObject) {
        if (!owns(conditionObject)) {
            throw new IllegalArgumentException("Not owner");
        }
        return conditionObject.hasWaiters();
    }

    public final int getWaitQueueLength(ConditionObject conditionObject) {
        if (!owns(conditionObject)) {
            throw new IllegalArgumentException("Not owner");
        }
        return conditionObject.getWaitQueueLength();
    }

    public final Collection<Thread> getWaitingThreads(ConditionObject conditionObject) {
        if (!owns(conditionObject)) {
            throw new IllegalArgumentException("Not owner");
        }
        return conditionObject.getWaitingThreads();
    }

    /* loaded from: rt.jar:java/util/concurrent/locks/AbstractQueuedSynchronizer$ConditionObject.class */
    public class ConditionObject implements Condition, Serializable {
        private static final long serialVersionUID = 1173984872572414699L;
        private transient Node firstWaiter;
        private transient Node lastWaiter;
        private static final int REINTERRUPT = 1;
        private static final int THROW_IE = -1;

        public ConditionObject() {
        }

        private Node addConditionWaiter() {
            Node node = this.lastWaiter;
            if (node != null && node.waitStatus != -2) {
                unlinkCancelledWaiters();
                node = this.lastWaiter;
            }
            Node node2 = new Node(Thread.currentThread(), -2);
            if (node == null) {
                this.firstWaiter = node2;
            } else {
                node.nextWaiter = node2;
            }
            this.lastWaiter = node2;
            return node2;
        }

        private void doSignal(Node node) {
            Node node2;
            do {
                Node node3 = node.nextWaiter;
                this.firstWaiter = node3;
                if (node3 == null) {
                    this.lastWaiter = null;
                }
                node.nextWaiter = null;
                if (AbstractQueuedSynchronizer.this.transferForSignal(node)) {
                    return;
                }
                node2 = this.firstWaiter;
                node = node2;
            } while (node2 != null);
        }

        private void doSignalAll(Node node) {
            this.firstWaiter = null;
            this.lastWaiter = null;
            do {
                Node node2 = node.nextWaiter;
                node.nextWaiter = null;
                AbstractQueuedSynchronizer.this.transferForSignal(node);
                node = node2;
            } while (node != null);
        }

        private void unlinkCancelledWaiters() {
            Node node = this.firstWaiter;
            Node node2 = null;
            while (node != null) {
                Node node3 = node.nextWaiter;
                if (node.waitStatus != -2) {
                    node.nextWaiter = null;
                    if (node2 == null) {
                        this.firstWaiter = node3;
                    } else {
                        node2.nextWaiter = node3;
                    }
                    if (node3 == null) {
                        this.lastWaiter = node2;
                    }
                } else {
                    node2 = node;
                }
                node = node3;
            }
        }

        @Override // java.util.concurrent.locks.Condition
        public final void signal() {
            if (!AbstractQueuedSynchronizer.this.isHeldExclusively()) {
                throw new IllegalMonitorStateException();
            }
            Node node = this.firstWaiter;
            if (node != null) {
                doSignal(node);
            }
        }

        @Override // java.util.concurrent.locks.Condition
        public final void signalAll() {
            if (!AbstractQueuedSynchronizer.this.isHeldExclusively()) {
                throw new IllegalMonitorStateException();
            }
            Node node = this.firstWaiter;
            if (node != null) {
                doSignalAll(node);
            }
        }

        @Override // java.util.concurrent.locks.Condition
        public final void awaitUninterruptibly() {
            Node nodeAddConditionWaiter = addConditionWaiter();
            int iFullyRelease = AbstractQueuedSynchronizer.this.fullyRelease(nodeAddConditionWaiter);
            boolean z2 = false;
            while (!AbstractQueuedSynchronizer.this.isOnSyncQueue(nodeAddConditionWaiter)) {
                LockSupport.park(this);
                if (Thread.interrupted()) {
                    z2 = true;
                }
            }
            if (AbstractQueuedSynchronizer.this.acquireQueued(nodeAddConditionWaiter, iFullyRelease) || z2) {
                AbstractQueuedSynchronizer.selfInterrupt();
            }
        }

        private int checkInterruptWhileWaiting(Node node) {
            if (Thread.interrupted()) {
                return AbstractQueuedSynchronizer.this.transferAfterCancelledWait(node) ? -1 : 1;
            }
            return 0;
        }

        private void reportInterruptAfterWait(int i2) throws InterruptedException {
            if (i2 == -1) {
                throw new InterruptedException();
            }
            if (i2 == 1) {
                AbstractQueuedSynchronizer.selfInterrupt();
            }
        }

        @Override // java.util.concurrent.locks.Condition
        public final void await() throws InterruptedException {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            Node nodeAddConditionWaiter = addConditionWaiter();
            int iFullyRelease = AbstractQueuedSynchronizer.this.fullyRelease(nodeAddConditionWaiter);
            int i2 = 0;
            while (!AbstractQueuedSynchronizer.this.isOnSyncQueue(nodeAddConditionWaiter)) {
                LockSupport.park(this);
                int iCheckInterruptWhileWaiting = checkInterruptWhileWaiting(nodeAddConditionWaiter);
                i2 = iCheckInterruptWhileWaiting;
                if (iCheckInterruptWhileWaiting != 0) {
                    break;
                }
            }
            if (AbstractQueuedSynchronizer.this.acquireQueued(nodeAddConditionWaiter, iFullyRelease) && i2 != -1) {
                i2 = 1;
            }
            if (nodeAddConditionWaiter.nextWaiter != null) {
                unlinkCancelledWaiters();
            }
            if (i2 != 0) {
                reportInterruptAfterWait(i2);
            }
        }

        @Override // java.util.concurrent.locks.Condition
        public final long awaitNanos(long j2) throws InterruptedException {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            Node nodeAddConditionWaiter = addConditionWaiter();
            int iFullyRelease = AbstractQueuedSynchronizer.this.fullyRelease(nodeAddConditionWaiter);
            long jNanoTime = System.nanoTime() + j2;
            int i2 = 0;
            while (true) {
                if (AbstractQueuedSynchronizer.this.isOnSyncQueue(nodeAddConditionWaiter)) {
                    break;
                }
                if (j2 <= 0) {
                    AbstractQueuedSynchronizer.this.transferAfterCancelledWait(nodeAddConditionWaiter);
                    break;
                }
                if (j2 >= 1000) {
                    LockSupport.parkNanos(this, j2);
                }
                int iCheckInterruptWhileWaiting = checkInterruptWhileWaiting(nodeAddConditionWaiter);
                i2 = iCheckInterruptWhileWaiting;
                if (iCheckInterruptWhileWaiting != 0) {
                    break;
                }
                j2 = jNanoTime - System.nanoTime();
            }
            if (AbstractQueuedSynchronizer.this.acquireQueued(nodeAddConditionWaiter, iFullyRelease) && i2 != -1) {
                i2 = 1;
            }
            if (nodeAddConditionWaiter.nextWaiter != null) {
                unlinkCancelledWaiters();
            }
            if (i2 != 0) {
                reportInterruptAfterWait(i2);
            }
            return jNanoTime - System.nanoTime();
        }

        @Override // java.util.concurrent.locks.Condition
        public final boolean awaitUntil(Date date) throws InterruptedException {
            long time = date.getTime();
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            Node nodeAddConditionWaiter = addConditionWaiter();
            int iFullyRelease = AbstractQueuedSynchronizer.this.fullyRelease(nodeAddConditionWaiter);
            boolean zTransferAfterCancelledWait = false;
            int i2 = 0;
            while (true) {
                if (AbstractQueuedSynchronizer.this.isOnSyncQueue(nodeAddConditionWaiter)) {
                    break;
                }
                if (System.currentTimeMillis() > time) {
                    zTransferAfterCancelledWait = AbstractQueuedSynchronizer.this.transferAfterCancelledWait(nodeAddConditionWaiter);
                    break;
                }
                LockSupport.parkUntil(this, time);
                int iCheckInterruptWhileWaiting = checkInterruptWhileWaiting(nodeAddConditionWaiter);
                i2 = iCheckInterruptWhileWaiting;
                if (iCheckInterruptWhileWaiting != 0) {
                    break;
                }
            }
            if (AbstractQueuedSynchronizer.this.acquireQueued(nodeAddConditionWaiter, iFullyRelease) && i2 != -1) {
                i2 = 1;
            }
            if (nodeAddConditionWaiter.nextWaiter != null) {
                unlinkCancelledWaiters();
            }
            if (i2 != 0) {
                reportInterruptAfterWait(i2);
            }
            return !zTransferAfterCancelledWait;
        }

        @Override // java.util.concurrent.locks.Condition
        public final boolean await(long j2, TimeUnit timeUnit) throws InterruptedException {
            long nanos = timeUnit.toNanos(j2);
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            Node nodeAddConditionWaiter = addConditionWaiter();
            int iFullyRelease = AbstractQueuedSynchronizer.this.fullyRelease(nodeAddConditionWaiter);
            long jNanoTime = System.nanoTime() + nanos;
            boolean zTransferAfterCancelledWait = false;
            int i2 = 0;
            while (true) {
                if (AbstractQueuedSynchronizer.this.isOnSyncQueue(nodeAddConditionWaiter)) {
                    break;
                }
                if (nanos <= 0) {
                    zTransferAfterCancelledWait = AbstractQueuedSynchronizer.this.transferAfterCancelledWait(nodeAddConditionWaiter);
                    break;
                }
                if (nanos >= 1000) {
                    LockSupport.parkNanos(this, nanos);
                }
                int iCheckInterruptWhileWaiting = checkInterruptWhileWaiting(nodeAddConditionWaiter);
                i2 = iCheckInterruptWhileWaiting;
                if (iCheckInterruptWhileWaiting != 0) {
                    break;
                }
                nanos = jNanoTime - System.nanoTime();
            }
            if (AbstractQueuedSynchronizer.this.acquireQueued(nodeAddConditionWaiter, iFullyRelease) && i2 != -1) {
                i2 = 1;
            }
            if (nodeAddConditionWaiter.nextWaiter != null) {
                unlinkCancelledWaiters();
            }
            if (i2 != 0) {
                reportInterruptAfterWait(i2);
            }
            return !zTransferAfterCancelledWait;
        }

        final boolean isOwnedBy(AbstractQueuedSynchronizer abstractQueuedSynchronizer) {
            return abstractQueuedSynchronizer == AbstractQueuedSynchronizer.this;
        }

        protected final boolean hasWaiters() {
            if (!AbstractQueuedSynchronizer.this.isHeldExclusively()) {
                throw new IllegalMonitorStateException();
            }
            Node node = this.firstWaiter;
            while (true) {
                Node node2 = node;
                if (node2 != null) {
                    if (node2.waitStatus != -2) {
                        node = node2.nextWaiter;
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }

        protected final int getWaitQueueLength() {
            if (!AbstractQueuedSynchronizer.this.isHeldExclusively()) {
                throw new IllegalMonitorStateException();
            }
            int i2 = 0;
            Node node = this.firstWaiter;
            while (true) {
                Node node2 = node;
                if (node2 != null) {
                    if (node2.waitStatus == -2) {
                        i2++;
                    }
                    node = node2.nextWaiter;
                } else {
                    return i2;
                }
            }
        }

        protected final Collection<Thread> getWaitingThreads() {
            Thread thread;
            if (!AbstractQueuedSynchronizer.this.isHeldExclusively()) {
                throw new IllegalMonitorStateException();
            }
            ArrayList arrayList = new ArrayList();
            Node node = this.firstWaiter;
            while (true) {
                Node node2 = node;
                if (node2 != null) {
                    if (node2.waitStatus == -2 && (thread = node2.thread) != null) {
                        arrayList.add(thread);
                    }
                    node = node2.nextWaiter;
                } else {
                    return arrayList;
                }
            }
        }
    }

    static {
        try {
            stateOffset = unsafe.objectFieldOffset(AbstractQueuedSynchronizer.class.getDeclaredField("state"));
            headOffset = unsafe.objectFieldOffset(AbstractQueuedSynchronizer.class.getDeclaredField("head"));
            tailOffset = unsafe.objectFieldOffset(AbstractQueuedSynchronizer.class.getDeclaredField("tail"));
            waitStatusOffset = unsafe.objectFieldOffset(Node.class.getDeclaredField("waitStatus"));
            nextOffset = unsafe.objectFieldOffset(Node.class.getDeclaredField(Constants.NEXT));
        } catch (Exception e2) {
            throw new Error(e2);
        }
    }

    private final boolean compareAndSetHead(Node node) {
        return unsafe.compareAndSwapObject(this, headOffset, null, node);
    }

    private final boolean compareAndSetTail(Node node, Node node2) {
        return unsafe.compareAndSwapObject(this, tailOffset, node, node2);
    }

    private static final boolean compareAndSetWaitStatus(Node node, int i2, int i3) {
        return unsafe.compareAndSwapInt(node, waitStatusOffset, i2, i3);
    }

    private static final boolean compareAndSetNext(Node node, Node node2, Node node3) {
        return unsafe.compareAndSwapObject(node, nextOffset, node2, node3);
    }
}
