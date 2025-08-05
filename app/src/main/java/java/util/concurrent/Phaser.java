package java.util.concurrent;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/util/concurrent/Phaser.class */
public class Phaser {
    private volatile long state;
    private static final int MAX_PARTIES = 65535;
    private static final int MAX_PHASE = Integer.MAX_VALUE;
    private static final int PARTIES_SHIFT = 16;
    private static final int PHASE_SHIFT = 32;
    private static final int UNARRIVED_MASK = 65535;
    private static final long PARTIES_MASK = 4294901760L;
    private static final long COUNTS_MASK = 4294967295L;
    private static final long TERMINATION_BIT = Long.MIN_VALUE;
    private static final int ONE_ARRIVAL = 1;
    private static final int ONE_PARTY = 65536;
    private static final int ONE_DEREGISTER = 65537;
    private static final int EMPTY = 1;
    private final Phaser parent;
    private final Phaser root;
    private final AtomicReference<QNode> evenQ;
    private final AtomicReference<QNode> oddQ;
    private static final int NCPU = Runtime.getRuntime().availableProcessors();
    static final int SPINS_PER_ARRIVAL;
    private static final Unsafe UNSAFE;
    private static final long stateOffset;

    private static int unarrivedOf(long j2) {
        int i2 = (int) j2;
        if (i2 == 1) {
            return 0;
        }
        return i2 & 65535;
    }

    private static int partiesOf(long j2) {
        return ((int) j2) >>> 16;
    }

    private static int phaseOf(long j2) {
        return (int) (j2 >>> 32);
    }

    private static int arrivedOf(long j2) {
        int i2 = (int) j2;
        if (i2 == 1) {
            return 0;
        }
        return (i2 >>> 16) - (i2 & 65535);
    }

    private AtomicReference<QNode> queueFor(int i2) {
        return (i2 & 1) == 0 ? this.evenQ : this.oddQ;
    }

    private String badArrive(long j2) {
        return "Attempted arrival of unregistered party for " + stateToString(j2);
    }

    private String badRegister(long j2) {
        return "Attempt to register more than 65535 parties for " + stateToString(j2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v19, types: [long, sun.misc.Unsafe] */
    private int doArrive(int i2) {
        long jReconcileState;
        int iDoArrive;
        int i3;
        ?? r0;
        long j2;
        Phaser phaser = this.root;
        do {
            jReconcileState = phaser == this ? this.state : reconcileState();
            iDoArrive = (int) (jReconcileState >>> 32);
            if (iDoArrive < 0) {
                return iDoArrive;
            }
            int i4 = (int) jReconcileState;
            i3 = i4 == 1 ? 0 : i4 & 65535;
            if (i3 <= 0) {
                throw new IllegalStateException(badArrive(jReconcileState));
            }
            r0 = UNSAFE;
        } while (!r0.compareAndSwapLong(this, stateOffset, jReconcileState, jReconcileState - i2));
        if (i3 == 1) {
            long j3 = r0 & PARTIES_MASK;
            int i5 = ((int) j3) >>> 16;
            if (phaser == this) {
                if (onAdvance(iDoArrive, i5)) {
                    j2 = j3 | Long.MIN_VALUE;
                } else if (i5 == 0) {
                    j2 = j3 | 1;
                } else {
                    j2 = j3 | i5;
                }
                UNSAFE.compareAndSwapLong(this, stateOffset, r0, j2 | (((iDoArrive + 1) & Integer.MAX_VALUE) << 32));
                releaseWaiters(iDoArrive);
            } else if (i5 == 0) {
                iDoArrive = this.parent.doArrive(65537);
                UNSAFE.compareAndSwapLong(this, stateOffset, r0, r0 | 1);
            } else {
                iDoArrive = this.parent.doArrive(1);
            }
        }
        return iDoArrive;
    }

    private int doRegister(int i2) {
        int iDoRegister;
        long j2 = (i2 << 16) | i2;
        Phaser phaser = this.parent;
        while (true) {
            long jReconcileState = phaser == null ? this.state : reconcileState();
            int i3 = (int) jReconcileState;
            int i4 = i3 >>> 16;
            int i5 = i3 & 65535;
            if (i2 > 65535 - i4) {
                throw new IllegalStateException(badRegister(jReconcileState));
            }
            iDoRegister = (int) (jReconcileState >>> 32);
            if (iDoRegister < 0) {
                break;
            }
            if (i3 != 1) {
                if (phaser == null || reconcileState() == jReconcileState) {
                    if (i5 == 0) {
                        this.root.internalAwaitAdvance(iDoRegister, null);
                    } else if (UNSAFE.compareAndSwapLong(this, stateOffset, jReconcileState, jReconcileState + j2)) {
                        break;
                    }
                }
            } else if (phaser == null) {
                if (UNSAFE.compareAndSwapLong(this, stateOffset, jReconcileState, (iDoRegister << 32) | j2)) {
                    break;
                }
            } else {
                synchronized (this) {
                    if (this.state == jReconcileState) {
                        iDoRegister = phaser.doRegister(1);
                        if (iDoRegister >= 0) {
                            while (!UNSAFE.compareAndSwapLong(this, stateOffset, jReconcileState, (iDoRegister << 32) | j2)) {
                                jReconcileState = this.state;
                                iDoRegister = (int) (this.root.state >>> 32);
                            }
                        }
                    }
                }
            }
        }
        return iDoRegister;
    }

    /* JADX WARN: Type inference failed for: r0v10, types: [sun.misc.Unsafe] */
    private long reconcileState() {
        long j2;
        Phaser phaser = this.root;
        long j3 = this.state;
        if (phaser != this) {
            while (true) {
                int i2 = (int) (phaser.state >>> 32);
                if (i2 == ((int) (j3 >>> 32))) {
                    break;
                }
                ?? r0 = UNSAFE;
                long j4 = stateOffset;
                long j5 = j3;
                long j6 = i2 << 32;
                if (i2 < 0) {
                    j2 = j3 & 4294967295L;
                } else {
                    int i3 = ((int) j3) >>> 16;
                    j2 = i3 == 0 ? 1L : (j3 & PARTIES_MASK) | i3;
                }
                j3 = r0;
                if (r0.compareAndSwapLong(this, j4, j5, j6 | j2)) {
                    break;
                }
                j3 = this.state;
            }
        }
        return j3;
    }

    public Phaser() {
        this(null, 0);
    }

    public Phaser(int i2) {
        this(null, i2);
    }

    public Phaser(Phaser phaser) {
        this(phaser, 0);
    }

    public Phaser(Phaser phaser, int i2) {
        if ((i2 >>> 16) != 0) {
            throw new IllegalArgumentException("Illegal number of parties");
        }
        int iDoRegister = 0;
        this.parent = phaser;
        if (phaser != null) {
            Phaser phaser2 = phaser.root;
            this.root = phaser2;
            this.evenQ = phaser2.evenQ;
            this.oddQ = phaser2.oddQ;
            if (i2 != 0) {
                iDoRegister = phaser.doRegister(1);
            }
        } else {
            this.root = this;
            this.evenQ = new AtomicReference<>();
            this.oddQ = new AtomicReference<>();
        }
        this.state = i2 == 0 ? 1L : (iDoRegister << 32) | (i2 << 16) | i2;
    }

    public int register() {
        return doRegister(1);
    }

    public int bulkRegister(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException();
        }
        if (i2 == 0) {
            return getPhase();
        }
        return doRegister(i2);
    }

    public int arrive() {
        return doArrive(1);
    }

    public int arriveAndDeregister() {
        return doArrive(65537);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v19, types: [long, sun.misc.Unsafe] */
    public int arriveAndAwaitAdvance() {
        long jReconcileState;
        int i2;
        int i3;
        ?? r0;
        long j2;
        Phaser phaser = this.root;
        do {
            jReconcileState = phaser == this ? this.state : reconcileState();
            i2 = (int) (jReconcileState >>> 32);
            if (i2 < 0) {
                return i2;
            }
            int i4 = (int) jReconcileState;
            i3 = i4 == 1 ? 0 : i4 & 65535;
            if (i3 <= 0) {
                throw new IllegalStateException(badArrive(jReconcileState));
            }
            r0 = UNSAFE;
        } while (!r0.compareAndSwapLong(this, stateOffset, jReconcileState, jReconcileState - 1));
        if (i3 > 1) {
            return phaser.internalAwaitAdvance(i2, null);
        }
        if (phaser != this) {
            return this.parent.arriveAndAwaitAdvance();
        }
        long j3 = r0 & PARTIES_MASK;
        int i5 = ((int) j3) >>> 16;
        if (onAdvance(i2, i5)) {
            j2 = j3 | Long.MIN_VALUE;
        } else if (i5 == 0) {
            j2 = j3 | 1;
        } else {
            j2 = j3 | i5;
        }
        int i6 = (i2 + 1) & Integer.MAX_VALUE;
        if (!UNSAFE.compareAndSwapLong(this, stateOffset, r0, j2 | (i6 << 32))) {
            return (int) (this.state >>> 32);
        }
        releaseWaiters(i2);
        return i6;
    }

    public int awaitAdvance(int i2) {
        Phaser phaser = this.root;
        int iReconcileState = (int) ((phaser == this ? this.state : reconcileState()) >>> 32);
        if (i2 < 0) {
            return i2;
        }
        if (iReconcileState == i2) {
            return phaser.internalAwaitAdvance(i2, null);
        }
        return iReconcileState;
    }

    public int awaitAdvanceInterruptibly(int i2) throws InterruptedException {
        Phaser phaser = this.root;
        int iReconcileState = (int) ((phaser == this ? this.state : reconcileState()) >>> 32);
        if (i2 < 0) {
            return i2;
        }
        if (iReconcileState == i2) {
            QNode qNode = new QNode(this, i2, true, false, 0L);
            iReconcileState = phaser.internalAwaitAdvance(i2, qNode);
            if (qNode.wasInterrupted) {
                throw new InterruptedException();
            }
        }
        return iReconcileState;
    }

    public int awaitAdvanceInterruptibly(int i2, long j2, TimeUnit timeUnit) throws InterruptedException, TimeoutException {
        long nanos = timeUnit.toNanos(j2);
        Phaser phaser = this.root;
        int iReconcileState = (int) ((phaser == this ? this.state : reconcileState()) >>> 32);
        if (i2 < 0) {
            return i2;
        }
        if (iReconcileState == i2) {
            QNode qNode = new QNode(this, i2, true, true, nanos);
            iReconcileState = phaser.internalAwaitAdvance(i2, qNode);
            if (qNode.wasInterrupted) {
                throw new InterruptedException();
            }
            if (iReconcileState == i2) {
                throw new TimeoutException();
            }
        }
        return iReconcileState;
    }

    public void forceTermination() {
        long j2;
        Phaser phaser = this.root;
        do {
            j2 = phaser.state;
            if (j2 < 0) {
                return;
            }
        } while (!UNSAFE.compareAndSwapLong(phaser, stateOffset, j2, j2 | Long.MIN_VALUE));
        releaseWaiters(0);
        releaseWaiters(1);
    }

    public final int getPhase() {
        return (int) (this.root.state >>> 32);
    }

    public int getRegisteredParties() {
        return partiesOf(this.state);
    }

    public int getArrivedParties() {
        return arrivedOf(reconcileState());
    }

    public int getUnarrivedParties() {
        return unarrivedOf(reconcileState());
    }

    public Phaser getParent() {
        return this.parent;
    }

    public Phaser getRoot() {
        return this.root;
    }

    public boolean isTerminated() {
        return this.root.state < 0;
    }

    protected boolean onAdvance(int i2, int i3) {
        return i3 == 0;
    }

    public String toString() {
        return stateToString(reconcileState());
    }

    private String stateToString(long j2) {
        return super.toString() + "[phase = " + phaseOf(j2) + " parties = " + partiesOf(j2) + " arrived = " + arrivedOf(j2) + "]";
    }

    private void releaseWaiters(int i2) {
        Thread thread;
        AtomicReference<QNode> atomicReference = (i2 & 1) == 0 ? this.evenQ : this.oddQ;
        while (true) {
            QNode qNode = atomicReference.get();
            if (qNode != null && qNode.phase != ((int) (this.root.state >>> 32))) {
                if (atomicReference.compareAndSet(qNode, qNode.next) && (thread = qNode.thread) != null) {
                    qNode.thread = null;
                    LockSupport.unpark(thread);
                }
            } else {
                return;
            }
        }
    }

    private int abortWait(int i2) {
        int i3;
        Thread thread;
        AtomicReference<QNode> atomicReference = (i2 & 1) == 0 ? this.evenQ : this.oddQ;
        while (true) {
            QNode qNode = atomicReference.get();
            i3 = (int) (this.root.state >>> 32);
            if (qNode == null || ((thread = qNode.thread) != null && qNode.phase == i3)) {
                break;
            }
            if (atomicReference.compareAndSet(qNode, qNode.next) && thread != null) {
                qNode.thread = null;
                LockSupport.unpark(thread);
            }
        }
        return i3;
    }

    static {
        SPINS_PER_ARRIVAL = NCPU < 2 ? 1 : 256;
        try {
            UNSAFE = Unsafe.getUnsafe();
            stateOffset = UNSAFE.objectFieldOffset(Phaser.class.getDeclaredField("state"));
        } catch (Exception e2) {
            throw new Error(e2);
        }
    }

    private int internalAwaitAdvance(int i2, QNode qNode) {
        int i3;
        releaseWaiters(i2 - 1);
        boolean zCompareAndSet = false;
        int i4 = 0;
        int i5 = SPINS_PER_ARRIVAL;
        while (true) {
            long j2 = this.state;
            int i6 = (int) (j2 >>> 32);
            i3 = i6;
            if (i6 != i2) {
                break;
            }
            if (qNode == null) {
                int i7 = ((int) j2) & 65535;
                if (i7 != i4) {
                    i4 = i7;
                    if (i7 < NCPU) {
                        i5 += SPINS_PER_ARRIVAL;
                    }
                }
                boolean zInterrupted = Thread.interrupted();
                if (!zInterrupted) {
                    i5--;
                    if (i5 < 0) {
                    }
                }
                qNode = new QNode(this, i2, false, false, 0L);
                qNode.wasInterrupted = zInterrupted;
            } else {
                if (qNode.isReleasable()) {
                    break;
                }
                if (!zCompareAndSet) {
                    AtomicReference<QNode> atomicReference = (i2 & 1) == 0 ? this.evenQ : this.oddQ;
                    QNode qNode2 = atomicReference.get();
                    qNode.next = qNode2;
                    if (qNode2 == null || qNode2.phase == i2) {
                        if (((int) (this.state >>> 32)) == i2) {
                            zCompareAndSet = atomicReference.compareAndSet(qNode2, qNode);
                        }
                    }
                } else {
                    try {
                        ForkJoinPool.managedBlock(qNode);
                    } catch (InterruptedException e2) {
                        qNode.wasInterrupted = true;
                    }
                }
            }
        }
        if (qNode != null) {
            if (qNode.thread != null) {
                qNode.thread = null;
            }
            if (qNode.wasInterrupted && !qNode.interruptible) {
                Thread.currentThread().interrupt();
            }
            if (i3 == i2) {
                int i8 = (int) (this.state >>> 32);
                i3 = i8;
                if (i8 == i2) {
                    return abortWait(i2);
                }
            }
        }
        releaseWaiters(i2);
        return i3;
    }

    /* loaded from: rt.jar:java/util/concurrent/Phaser$QNode.class */
    static final class QNode implements ForkJoinPool.ManagedBlocker {
        final Phaser phaser;
        final int phase;
        final boolean interruptible;
        final boolean timed;
        boolean wasInterrupted;
        long nanos;
        final long deadline;
        volatile Thread thread;
        QNode next;

        QNode(Phaser phaser, int i2, boolean z2, boolean z3, long j2) {
            this.phaser = phaser;
            this.phase = i2;
            this.interruptible = z2;
            this.nanos = j2;
            this.timed = z3;
            this.deadline = z3 ? System.nanoTime() + j2 : 0L;
            this.thread = Thread.currentThread();
        }

        @Override // java.util.concurrent.ForkJoinPool.ManagedBlocker
        public boolean isReleasable() {
            if (this.thread == null) {
                return true;
            }
            if (this.phaser.getPhase() != this.phase) {
                this.thread = null;
                return true;
            }
            if (Thread.interrupted()) {
                this.wasInterrupted = true;
            }
            if (this.wasInterrupted && this.interruptible) {
                this.thread = null;
                return true;
            }
            if (!this.timed) {
                return false;
            }
            if (this.nanos > 0) {
                this.nanos = this.deadline - System.nanoTime();
            }
            if (this.nanos <= 0) {
                this.thread = null;
                return true;
            }
            return false;
        }

        @Override // java.util.concurrent.ForkJoinPool.ManagedBlocker
        public boolean block() {
            if (isReleasable()) {
                return true;
            }
            if (!this.timed) {
                LockSupport.park(this);
            } else if (this.nanos > 0) {
                LockSupport.parkNanos(this, this.nanos);
            }
            return isReleasable();
        }
    }
}
