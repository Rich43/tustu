package java.util.concurrent.locks;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/util/concurrent/locks/StampedLock.class */
public class StampedLock implements Serializable {
    private static final long serialVersionUID = -6001602636862214147L;
    private static final int NCPU = Runtime.getRuntime().availableProcessors();
    private static final int SPINS;
    private static final int HEAD_SPINS;
    private static final int MAX_HEAD_SPINS;
    private static final int OVERFLOW_YIELD_RATE = 7;
    private static final int LG_READERS = 7;
    private static final long RUNIT = 1;
    private static final long WBIT = 128;
    private static final long RBITS = 127;
    private static final long RFULL = 126;
    private static final long ABITS = 255;
    private static final long SBITS = -128;
    private static final long ORIGIN = 256;
    private static final long INTERRUPTED = 1;
    private static final int WAITING = -1;
    private static final int CANCELLED = 1;
    private static final int RMODE = 0;
    private static final int WMODE = 1;
    private volatile transient WNode whead;
    private volatile transient WNode wtail;
    transient ReadLockView readLockView;
    transient WriteLockView writeLockView;
    transient ReadWriteLockView readWriteLockView;
    private volatile transient long state = 256;
    private transient int readerOverflow;

    /* renamed from: U, reason: collision with root package name */
    private static final Unsafe f12594U;
    private static final long STATE;
    private static final long WHEAD;
    private static final long WTAIL;
    private static final long WNEXT;
    private static final long WSTATUS;
    private static final long WCOWAIT;
    private static final long PARKBLOCKER;

    static {
        SPINS = NCPU > 1 ? 64 : 0;
        HEAD_SPINS = NCPU > 1 ? 1024 : 0;
        MAX_HEAD_SPINS = NCPU > 1 ? 65536 : 0;
        try {
            f12594U = Unsafe.getUnsafe();
            STATE = f12594U.objectFieldOffset(StampedLock.class.getDeclaredField("state"));
            WHEAD = f12594U.objectFieldOffset(StampedLock.class.getDeclaredField("whead"));
            WTAIL = f12594U.objectFieldOffset(StampedLock.class.getDeclaredField("wtail"));
            WSTATUS = f12594U.objectFieldOffset(WNode.class.getDeclaredField("status"));
            WNEXT = f12594U.objectFieldOffset(WNode.class.getDeclaredField(Constants.NEXT));
            WCOWAIT = f12594U.objectFieldOffset(WNode.class.getDeclaredField("cowait"));
            PARKBLOCKER = f12594U.objectFieldOffset(Thread.class.getDeclaredField("parkBlocker"));
        } catch (Exception e2) {
            throw new Error(e2);
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/locks/StampedLock$WNode.class */
    static final class WNode {
        volatile WNode prev;
        volatile WNode next;
        volatile WNode cowait;
        volatile Thread thread;
        volatile int status;
        final int mode;

        WNode(int i2, WNode wNode) {
            this.mode = i2;
            this.prev = wNode;
        }
    }

    /* JADX WARN: Type inference failed for: r0v7, types: [long, sun.misc.Unsafe] */
    public long writeLock() {
        long j2 = this.state;
        if ((j2 & ABITS) == 0) {
            ?? r0 = f12594U;
            if (r0.compareAndSwapLong(this, STATE, j2, j2 + 128)) {
                return r0;
            }
        }
        return acquireWrite(false, 0L);
    }

    /* JADX WARN: Type inference failed for: r0v6, types: [long, sun.misc.Unsafe] */
    public long tryWriteLock() {
        long j2 = this.state;
        if ((j2 & ABITS) == 0) {
            ?? r0 = f12594U;
            if (r0.compareAndSwapLong(this, STATE, j2, j2 + 128)) {
                return r0;
            }
        }
        return 0L;
    }

    public long tryWriteLock(long j2, TimeUnit timeUnit) throws InterruptedException {
        long nanos = timeUnit.toNanos(j2);
        if (!Thread.interrupted()) {
            long jTryWriteLock = tryWriteLock();
            if (jTryWriteLock != 0) {
                return jTryWriteLock;
            }
            if (nanos <= 0) {
                return 0L;
            }
            long jNanoTime = System.nanoTime() + nanos;
            long j3 = jNanoTime;
            if (jNanoTime == 0) {
                j3 = 1;
            }
            long jAcquireWrite = acquireWrite(true, j3);
            if (jAcquireWrite != 1) {
                return jAcquireWrite;
            }
        }
        throw new InterruptedException();
    }

    public long writeLockInterruptibly() throws InterruptedException {
        if (!Thread.interrupted()) {
            long jAcquireWrite = acquireWrite(true, 0L);
            if (jAcquireWrite != 1) {
                return jAcquireWrite;
            }
        }
        throw new InterruptedException();
    }

    /* JADX WARN: Type inference failed for: r0v9, types: [long, sun.misc.Unsafe] */
    public long readLock() {
        long j2 = this.state;
        if (this.whead == this.wtail && (j2 & ABITS) < RFULL) {
            ?? r0 = f12594U;
            if (r0.compareAndSwapLong(this, STATE, j2, j2 + 1)) {
                return r0;
            }
        }
        return acquireRead(false, 0L);
    }

    /* JADX WARN: Type inference failed for: r0v8, types: [long, sun.misc.Unsafe] */
    public long tryReadLock() {
        while (true) {
            long j2 = this.state;
            long j3 = j2 & ABITS;
            if (j3 == 128) {
                return 0L;
            }
            if (j3 < RFULL) {
                ?? r0 = f12594U;
                if (r0.compareAndSwapLong(this, STATE, j2, j2 + 1)) {
                    return r0;
                }
            } else {
                long jTryIncReaderOverflow = tryIncReaderOverflow(j2);
                if (jTryIncReaderOverflow != 0) {
                    return jTryIncReaderOverflow;
                }
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v27, types: [long, sun.misc.Unsafe] */
    public long tryReadLock(long j2, TimeUnit timeUnit) throws InterruptedException {
        long nanos = timeUnit.toNanos(j2);
        if (!Thread.interrupted()) {
            long j3 = this.state;
            long j4 = j3 & ABITS;
            if (j4 != 128) {
                if (j4 < RFULL) {
                    ?? r0 = f12594U;
                    if (r0.compareAndSwapLong(this, STATE, j3, j3 + 1)) {
                        return r0;
                    }
                } else {
                    long jTryIncReaderOverflow = tryIncReaderOverflow(j3);
                    if (jTryIncReaderOverflow != 0) {
                        return jTryIncReaderOverflow;
                    }
                }
            }
            if (nanos <= 0) {
                return 0L;
            }
            long jNanoTime = System.nanoTime() + nanos;
            long j5 = jNanoTime;
            if (jNanoTime == 0) {
                j5 = 1;
            }
            long jAcquireRead = acquireRead(true, j5);
            if (jAcquireRead != 1) {
                return jAcquireRead;
            }
        }
        throw new InterruptedException();
    }

    public long readLockInterruptibly() throws InterruptedException {
        if (!Thread.interrupted()) {
            long jAcquireRead = acquireRead(true, 0L);
            if (jAcquireRead != 1) {
                return jAcquireRead;
            }
        }
        throw new InterruptedException();
    }

    public long tryOptimisticRead() {
        long j2 = this.state;
        if ((j2 & 128) == 0) {
            return j2 & SBITS;
        }
        return 0L;
    }

    public boolean validate(long j2) {
        f12594U.loadFence();
        return (j2 & SBITS) == (this.state & SBITS);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void unlockWrite(long j2) {
        if (this.state != j2 || (j2 & 128) == 0) {
            throw new IllegalMonitorStateException();
        }
        this.state = j2 + 128 == 0 ? 256L : this;
        WNode wNode = this.whead;
        if (wNode != null && wNode.status != 0) {
            release(wNode);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x003a, code lost:
    
        throw new java.lang.IllegalMonitorStateException();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void unlockRead(long r12) {
        /*
            r11 = this;
        L0:
            r0 = r11
            long r0 = r0.state
            r1 = r0; r0 = r0; 
            r14 = r1
            r1 = -128(0xffffffffffffff80, double:NaN)
            long r0 = r0 & r1
            r1 = r12
            r2 = -128(0xffffffffffffff80, double:NaN)
            long r1 = r1 & r2
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 != 0) goto L33
            r0 = r12
            r1 = 255(0xff, double:1.26E-321)
            long r0 = r0 & r1
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 == 0) goto L33
            r0 = r14
            r1 = 255(0xff, double:1.26E-321)
            long r0 = r0 & r1
            r1 = r0; r1 = r0; 
            r16 = r1
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 == 0) goto L33
            r0 = r16
            r1 = 128(0x80, double:6.3E-322)
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 != 0) goto L3b
        L33:
            java.lang.IllegalMonitorStateException r0 = new java.lang.IllegalMonitorStateException
            r1 = r0
            r1.<init>()
            throw r0
        L3b:
            r0 = r16
            r1 = 126(0x7e, double:6.23E-322)
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 >= 0) goto L77
            sun.misc.Unsafe r0 = java.util.concurrent.locks.StampedLock.f12594U
            r1 = r11
            long r2 = java.util.concurrent.locks.StampedLock.STATE
            r3 = r14
            r4 = r14
            r5 = 1
            long r4 = r4 - r5
            boolean r0 = r0.compareAndSwapLong(r1, r2, r3, r4)
            if (r0 == 0) goto L0
            r0 = r16
            r1 = 1
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 != 0) goto L84
            r0 = r11
            java.util.concurrent.locks.StampedLock$WNode r0 = r0.whead
            r1 = r0
            r18 = r1
            if (r0 == 0) goto L84
            r0 = r18
            int r0 = r0.status
            if (r0 == 0) goto L84
            r0 = r11
            r1 = r18
            r0.release(r1)
            goto L84
        L77:
            r0 = r11
            r1 = r14
            long r0 = r0.tryDecReaderOverflow(r1)
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 == 0) goto L0
            goto L84
        L84:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.StampedLock.unlockRead(long):void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:44:0x00d1, code lost:
    
        throw new java.lang.IllegalMonitorStateException();
     */
    /* JADX WARN: Multi-variable type inference failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void unlock(long r12) {
        /*
            Method dump skipped, instructions count: 210
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.StampedLock.unlock(long):void");
    }

    /* JADX WARN: Type inference failed for: r0v13, types: [long, sun.misc.Unsafe] */
    /* JADX WARN: Type inference failed for: r0v28, types: [long, sun.misc.Unsafe] */
    public long tryConvertToWriteLock(long j2) {
        long j3 = j2 & ABITS;
        while (true) {
            long j4 = this.state;
            if ((j4 & SBITS) == (j2 & SBITS)) {
                long j5 = j4 & ABITS;
                if (j5 == 0) {
                    if (j3 == 0) {
                        ?? r0 = f12594U;
                        if (r0.compareAndSwapLong(this, STATE, j4, j4 + 128)) {
                            return r0;
                        }
                    } else {
                        return 0L;
                    }
                } else {
                    if (j5 == 128) {
                        if (j3 == j5) {
                            return j2;
                        }
                        return 0L;
                    }
                    if (j5 == 1 && j3 != 0) {
                        ?? r02 = f12594U;
                        if (r02.compareAndSwapLong(this, STATE, j4, (j4 - 1) + 128)) {
                            return r02;
                        }
                    } else {
                        return 0L;
                    }
                }
            } else {
                return 0L;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v34, types: [long, sun.misc.Unsafe] */
    public long tryConvertToReadLock(long j2) {
        long j3 = j2 & ABITS;
        while (true) {
            long j4 = this.state;
            if ((j4 & SBITS) == (j2 & SBITS)) {
                long j5 = j4 & ABITS;
                if (j5 != 0) {
                    if (j5 != 128) {
                        if (j3 != 0 && j3 < 128) {
                            return j2;
                        }
                        return 0L;
                    }
                    if (j3 == j5) {
                        this.state = j4 + 129;
                        WNode wNode = this.whead;
                        if (wNode != null && wNode.status != 0) {
                            release(wNode);
                        }
                        return this;
                    }
                    return 0L;
                }
                if (j3 != 0) {
                    return 0L;
                }
                if (j5 < RFULL) {
                    ?? r0 = f12594U;
                    if (r0.compareAndSwapLong(this, STATE, j4, j4 + 1)) {
                        return r0;
                    }
                } else {
                    long jTryIncReaderOverflow = tryIncReaderOverflow(j4);
                    if (jTryIncReaderOverflow != 0) {
                        return jTryIncReaderOverflow;
                    }
                }
            } else {
                return 0L;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v35, types: [long, sun.misc.Unsafe] */
    public long tryConvertToOptimisticRead(long j2) {
        WNode wNode;
        long j3 = j2 & ABITS;
        f12594U.loadFence();
        while (true) {
            long j4 = this.state;
            if ((j4 & SBITS) == (j2 & SBITS)) {
                long j5 = j4 & ABITS;
                if (j5 == 0) {
                    if (j3 == 0) {
                        return j4;
                    }
                    return 0L;
                }
                if (j5 == 128) {
                    if (j3 == j5) {
                        this.state = j4 + 128 == 0 ? 256L : this;
                        WNode wNode2 = this.whead;
                        if (wNode2 != null && wNode2.status != 0) {
                            release(wNode2);
                        }
                        return this;
                    }
                    return 0L;
                }
                if (j3 == 0 || j3 >= 128) {
                    return 0L;
                }
                if (j5 < RFULL) {
                    ?? r0 = f12594U;
                    if (r0.compareAndSwapLong(this, STATE, j4, j4 - 1)) {
                        if (j5 == 1 && (wNode = this.whead) != null && wNode.status != 0) {
                            release(wNode);
                        }
                        return r0 & SBITS;
                    }
                } else {
                    long jTryDecReaderOverflow = tryDecReaderOverflow(j4);
                    if (jTryDecReaderOverflow != 0) {
                        return jTryDecReaderOverflow & SBITS;
                    }
                }
            } else {
                return 0L;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public boolean tryUnlockWrite() {
        long j2 = this.state;
        if ((j2 & 128) != 0) {
            this.state = j2 + 128 == 0 ? 256L : this;
            WNode wNode = this.whead;
            if (wNode != null && wNode.status != 0) {
                release(wNode);
                return true;
            }
            return true;
        }
        return false;
    }

    public boolean tryUnlockRead() {
        WNode wNode;
        while (true) {
            long j2 = this.state;
            long j3 = j2 & ABITS;
            if (j3 == 0 || j3 >= 128) {
                return false;
            }
            if (j3 < RFULL) {
                if (f12594U.compareAndSwapLong(this, STATE, j2, j2 - 1)) {
                    if (j3 == 1 && (wNode = this.whead) != null && wNode.status != 0) {
                        release(wNode);
                        return true;
                    }
                    return true;
                }
            } else if (tryDecReaderOverflow(j2) != 0) {
                return true;
            }
        }
    }

    private int getReadLockCount(long j2) {
        long j3 = j2 & RBITS;
        long j4 = j3;
        if (j3 >= RFULL) {
            j4 = RFULL + this.readerOverflow;
        }
        return (int) j4;
    }

    public boolean isWriteLocked() {
        return (this.state & 128) != 0;
    }

    public boolean isReadLocked() {
        return (this.state & RBITS) != 0;
    }

    public int getReadLockCount() {
        return getReadLockCount(this.state);
    }

    public String toString() {
        String str;
        long j2 = this.state;
        StringBuilder sbAppend = new StringBuilder().append(super.toString());
        if ((j2 & ABITS) == 0) {
            str = "[Unlocked]";
        } else {
            str = (j2 & 128) != 0 ? "[Write-locked]" : "[Read-locks:" + getReadLockCount(j2) + "]";
        }
        return sbAppend.append(str).toString();
    }

    public Lock asReadLock() {
        ReadLockView readLockView = this.readLockView;
        if (readLockView != null) {
            return readLockView;
        }
        ReadLockView readLockView2 = new ReadLockView();
        this.readLockView = readLockView2;
        return readLockView2;
    }

    public Lock asWriteLock() {
        WriteLockView writeLockView = this.writeLockView;
        if (writeLockView != null) {
            return writeLockView;
        }
        WriteLockView writeLockView2 = new WriteLockView();
        this.writeLockView = writeLockView2;
        return writeLockView2;
    }

    public ReadWriteLock asReadWriteLock() {
        ReadWriteLockView readWriteLockView = this.readWriteLockView;
        if (readWriteLockView != null) {
            return readWriteLockView;
        }
        ReadWriteLockView readWriteLockView2 = new ReadWriteLockView();
        this.readWriteLockView = readWriteLockView2;
        return readWriteLockView2;
    }

    /* loaded from: rt.jar:java/util/concurrent/locks/StampedLock$ReadLockView.class */
    final class ReadLockView implements Lock {
        ReadLockView() {
        }

        @Override // java.util.concurrent.locks.Lock
        public void lock() {
            StampedLock.this.readLock();
        }

        @Override // java.util.concurrent.locks.Lock
        public void lockInterruptibly() throws InterruptedException {
            StampedLock.this.readLockInterruptibly();
        }

        @Override // java.util.concurrent.locks.Lock
        public boolean tryLock() {
            return StampedLock.this.tryReadLock() != 0;
        }

        @Override // java.util.concurrent.locks.Lock
        public boolean tryLock(long j2, TimeUnit timeUnit) throws InterruptedException {
            return StampedLock.this.tryReadLock(j2, timeUnit) != 0;
        }

        @Override // java.util.concurrent.locks.Lock
        public void unlock() {
            StampedLock.this.unstampedUnlockRead();
        }

        @Override // java.util.concurrent.locks.Lock
        public Condition newCondition() {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/locks/StampedLock$WriteLockView.class */
    final class WriteLockView implements Lock {
        WriteLockView() {
        }

        @Override // java.util.concurrent.locks.Lock
        public void lock() {
            StampedLock.this.writeLock();
        }

        @Override // java.util.concurrent.locks.Lock
        public void lockInterruptibly() throws InterruptedException {
            StampedLock.this.writeLockInterruptibly();
        }

        @Override // java.util.concurrent.locks.Lock
        public boolean tryLock() {
            return StampedLock.this.tryWriteLock() != 0;
        }

        @Override // java.util.concurrent.locks.Lock
        public boolean tryLock(long j2, TimeUnit timeUnit) throws InterruptedException {
            return StampedLock.this.tryWriteLock(j2, timeUnit) != 0;
        }

        @Override // java.util.concurrent.locks.Lock
        public void unlock() {
            StampedLock.this.unstampedUnlockWrite();
        }

        @Override // java.util.concurrent.locks.Lock
        public Condition newCondition() {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/locks/StampedLock$ReadWriteLockView.class */
    final class ReadWriteLockView implements ReadWriteLock {
        ReadWriteLockView() {
        }

        @Override // java.util.concurrent.locks.ReadWriteLock
        public Lock readLock() {
            return StampedLock.this.asReadLock();
        }

        @Override // java.util.concurrent.locks.ReadWriteLock
        public Lock writeLock() {
            return StampedLock.this.asWriteLock();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    final void unstampedUnlockWrite() {
        long j2 = this.state;
        if ((j2 & 128) == 0) {
            throw new IllegalMonitorStateException();
        }
        this.state = j2 + 128 == 0 ? 256L : this;
        WNode wNode = this.whead;
        if (wNode != null && wNode.status != 0) {
            release(wNode);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x0020, code lost:
    
        throw new java.lang.IllegalMonitorStateException();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    final void unstampedUnlockRead() {
        /*
            r11 = this;
        L0:
            r0 = r11
            long r0 = r0.state
            r1 = r0; r0 = r0; 
            r12 = r1
            r1 = 255(0xff, double:1.26E-321)
            long r0 = r0 & r1
            r1 = r0; r1 = r0; 
            r14 = r1
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 == 0) goto L19
            r0 = r14
            r1 = 128(0x80, double:6.3E-322)
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 < 0) goto L21
        L19:
            java.lang.IllegalMonitorStateException r0 = new java.lang.IllegalMonitorStateException
            r1 = r0
            r1.<init>()
            throw r0
        L21:
            r0 = r14
            r1 = 126(0x7e, double:6.23E-322)
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 >= 0) goto L5b
            sun.misc.Unsafe r0 = java.util.concurrent.locks.StampedLock.f12594U
            r1 = r11
            long r2 = java.util.concurrent.locks.StampedLock.STATE
            r3 = r12
            r4 = r12
            r5 = 1
            long r4 = r4 - r5
            boolean r0 = r0.compareAndSwapLong(r1, r2, r3, r4)
            if (r0 == 0) goto L68
            r0 = r14
            r1 = 1
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 != 0) goto L6b
            r0 = r11
            java.util.concurrent.locks.StampedLock$WNode r0 = r0.whead
            r1 = r0
            r16 = r1
            if (r0 == 0) goto L6b
            r0 = r16
            int r0 = r0.status
            if (r0 == 0) goto L6b
            r0 = r11
            r1 = r16
            r0.release(r1)
            goto L6b
        L5b:
            r0 = r11
            r1 = r12
            long r0 = r0.tryDecReaderOverflow(r1)
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 == 0) goto L68
            goto L6b
        L68:
            goto L0
        L6b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.StampedLock.unstampedUnlockRead():void");
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.state = 256L;
    }

    private long tryIncReaderOverflow(long j2) {
        if ((j2 & ABITS) == RFULL) {
            if (f12594U.compareAndSwapLong(this, STATE, j2, j2 | RBITS)) {
                this.readerOverflow++;
                this.state = j2;
                return j2;
            }
            return 0L;
        }
        if ((LockSupport.nextSecondarySeed() & 7) == 0) {
            Thread.yield();
            return 0L;
        }
        return 0L;
    }

    private long tryDecReaderOverflow(long j2) {
        long j3;
        if ((j2 & ABITS) == RFULL) {
            if (f12594U.compareAndSwapLong(this, STATE, j2, j2 | RBITS)) {
                int i2 = this.readerOverflow;
                if (i2 > 0) {
                    this.readerOverflow = i2 - 1;
                    j3 = j2;
                } else {
                    j3 = j2 - 1;
                }
                this.state = j3;
                return j3;
            }
            return 0L;
        }
        if ((LockSupport.nextSecondarySeed() & 7) == 0) {
            Thread.yield();
            return 0L;
        }
        return 0L;
    }

    private void release(WNode wNode) {
        Thread thread;
        if (wNode != null) {
            f12594U.compareAndSwapInt(wNode, WSTATUS, -1, 0);
            WNode wNode2 = wNode.next;
            WNode wNode3 = wNode2;
            if (wNode2 == null || wNode3.status == 1) {
                WNode wNode4 = this.wtail;
                while (true) {
                    WNode wNode5 = wNode4;
                    if (wNode5 == null || wNode5 == wNode) {
                        break;
                    }
                    if (wNode5.status <= 0) {
                        wNode3 = wNode5;
                    }
                    wNode4 = wNode5.prev;
                }
            }
            if (wNode3 != null && (thread = wNode3.thread) != null) {
                f12594U.unpark(thread);
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v7, types: [long, sun.misc.Unsafe] */
    /* JADX WARN: Type inference failed for: r0v98, types: [long, sun.misc.Unsafe] */
    private long acquireWrite(boolean z2, long j2) {
        Thread thread;
        long j3;
        WNode wNode = null;
        int i2 = -1;
        while (true) {
            long j4 = this.state;
            long j5 = j4 & ABITS;
            if (j5 == 0) {
                ?? r0 = f12594U;
                if (r0.compareAndSwapLong(this, STATE, j4, j4 + 128)) {
                    return r0;
                }
            } else if (i2 < 0) {
                i2 = (j5 == 128 && this.wtail == this.whead) ? SPINS : 0;
            } else if (i2 > 0) {
                if (LockSupport.nextSecondarySeed() >= 0) {
                    i2--;
                }
            } else {
                WNode wNode2 = this.wtail;
                WNode wNode3 = wNode2;
                if (wNode2 == null) {
                    WNode wNode4 = new WNode(1, null);
                    if (f12594U.compareAndSwapObject(this, WHEAD, null, wNode4)) {
                        this.wtail = wNode4;
                    }
                } else if (wNode == null) {
                    wNode = new WNode(1, wNode3);
                } else if (wNode.prev != wNode3) {
                    wNode.prev = wNode3;
                } else if (f12594U.compareAndSwapObject(this, WTAIL, wNode3, wNode)) {
                    wNode3.next = wNode;
                    int i3 = -1;
                    while (true) {
                        WNode wNode5 = this.whead;
                        if (wNode5 == wNode3) {
                            if (i3 < 0) {
                                i3 = HEAD_SPINS;
                            } else if (i3 < MAX_HEAD_SPINS) {
                                i3 <<= 1;
                            }
                            int i4 = i3;
                            while (true) {
                                long j6 = this.state;
                                if ((j6 & ABITS) == 0) {
                                    ?? r02 = f12594U;
                                    if (r02.compareAndSwapLong(this, STATE, j6, j6 + 128)) {
                                        this.whead = wNode;
                                        wNode.prev = null;
                                        return r02;
                                    }
                                } else if (LockSupport.nextSecondarySeed() >= 0) {
                                    i4--;
                                    if (i4 <= 0) {
                                        break;
                                    }
                                } else {
                                    continue;
                                }
                            }
                        } else if (wNode5 != null) {
                            while (true) {
                                WNode wNode6 = wNode5.cowait;
                                if (wNode6 == null) {
                                    break;
                                }
                                if (f12594U.compareAndSwapObject(wNode5, WCOWAIT, wNode6, wNode6.cowait) && (thread = wNode6.thread) != null) {
                                    f12594U.unpark(thread);
                                }
                            }
                        }
                        if (this.whead == wNode5) {
                            WNode wNode7 = wNode.prev;
                            if (wNode7 != wNode3) {
                                if (wNode7 != null) {
                                    wNode3 = wNode7;
                                    wNode7.next = wNode;
                                }
                            } else {
                                int i5 = wNode3.status;
                                if (i5 == 0) {
                                    f12594U.compareAndSwapInt(wNode3, WSTATUS, 0, -1);
                                } else if (i5 == 1) {
                                    WNode wNode8 = wNode3.prev;
                                    if (wNode8 != null) {
                                        wNode.prev = wNode8;
                                        wNode8.next = wNode;
                                    }
                                } else {
                                    if (j2 == 0) {
                                        j3 = 0;
                                    } else {
                                        long jNanoTime = j2 - System.nanoTime();
                                        j3 = jNanoTime;
                                        if (jNanoTime <= 0) {
                                            return cancelWaiter(wNode, wNode, false);
                                        }
                                    }
                                    Thread threadCurrentThread = Thread.currentThread();
                                    f12594U.putObject(threadCurrentThread, PARKBLOCKER, this);
                                    wNode.thread = threadCurrentThread;
                                    if (wNode3.status < 0 && ((wNode3 != wNode5 || (this.state & ABITS) != 0) && this.whead == wNode5 && wNode.prev == wNode3)) {
                                        f12594U.park(false, j3);
                                    }
                                    wNode.thread = null;
                                    f12594U.putObject(threadCurrentThread, PARKBLOCKER, (Object) null);
                                    if (z2 && Thread.interrupted()) {
                                        return cancelWaiter(wNode, wNode, true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:137:0x032f, code lost:
    
        r11.whead = r15;
        r15.prev = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:138:0x033b, code lost:
    
        r0 = r15.cowait;
     */
    /* JADX WARN: Code restructure failed: missing block: B:139:0x0343, code lost:
    
        if (r0 == null) goto L273;
     */
    /* JADX WARN: Code restructure failed: missing block: B:141:0x0358, code lost:
    
        if (java.util.concurrent.locks.StampedLock.f12594U.compareAndSwapObject(r15, java.util.concurrent.locks.StampedLock.WCOWAIT, r0, r0.cowait) == false) goto L277;
     */
    /* JADX WARN: Code restructure failed: missing block: B:142:0x035b, code lost:
    
        r0 = r0.thread;
     */
    /* JADX WARN: Code restructure failed: missing block: B:143:0x0363, code lost:
    
        if (r0 == null) goto L278;
     */
    /* JADX WARN: Code restructure failed: missing block: B:144:0x0366, code lost:
    
        java.util.concurrent.locks.StampedLock.f12594U.unpark(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:146:0x0373, code lost:
    
        return r27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x005e, code lost:
    
        return r23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x01ec, code lost:
    
        return r26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x021d, code lost:
    
        r15 = null;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:232:0x0068 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:239:0x0017 A[SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r0v126, types: [sun.misc.Unsafe] */
    /* JADX WARN: Type inference failed for: r0v198, types: [sun.misc.Unsafe] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private long acquireRead(boolean r12, long r13) {
        /*
            Method dump skipped, instructions count: 1235
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.locks.StampedLock.acquireRead(boolean, long):long");
    }

    private long cancelWaiter(WNode wNode, WNode wNode2, boolean z2) {
        WNode wNode3;
        WNode wNode4;
        WNode wNode5;
        Thread thread;
        if (wNode != null && wNode2 != null) {
            wNode.status = 1;
            WNode wNode6 = wNode2;
            while (true) {
                WNode wNode7 = wNode6;
                WNode wNode8 = wNode7.cowait;
                if (wNode8 == null) {
                    break;
                }
                if (wNode8.status == 1) {
                    f12594U.compareAndSwapObject(wNode7, WCOWAIT, wNode8, wNode8.cowait);
                    wNode6 = wNode2;
                } else {
                    wNode6 = wNode8;
                }
            }
            if (wNode2 == wNode) {
                WNode wNode9 = wNode2.cowait;
                while (true) {
                    WNode wNode10 = wNode9;
                    if (wNode10 == null) {
                        break;
                    }
                    Thread thread2 = wNode10.thread;
                    if (thread2 != null) {
                        f12594U.unpark(thread2);
                    }
                    wNode9 = wNode10.cowait;
                }
                WNode wNode11 = wNode.prev;
                while (true) {
                    WNode wNode12 = wNode11;
                    if (wNode12 == null) {
                        break;
                    }
                    do {
                        WNode wNode13 = wNode.next;
                        wNode3 = wNode13;
                        if (wNode13 != null && wNode3.status != 1) {
                            break;
                        }
                        WNode wNode14 = null;
                        WNode wNode15 = this.wtail;
                        while (true) {
                            WNode wNode16 = wNode15;
                            if (wNode16 == null || wNode16 == wNode) {
                                break;
                            }
                            if (wNode16.status != 1) {
                                wNode14 = wNode16;
                            }
                            wNode15 = wNode16.prev;
                        }
                        if (wNode3 == wNode14) {
                            break;
                        }
                        wNode4 = wNode14;
                        wNode3 = wNode4;
                    } while (!f12594U.compareAndSwapObject(wNode, WNEXT, wNode3, wNode4));
                    if (wNode3 == null && wNode == this.wtail) {
                        f12594U.compareAndSwapObject(this, WTAIL, wNode, wNode12);
                    }
                    if (wNode12.next == wNode) {
                        f12594U.compareAndSwapObject(wNode12, WNEXT, wNode, wNode3);
                    }
                    if (wNode3 != null && (thread = wNode3.thread) != null) {
                        wNode3.thread = null;
                        f12594U.unpark(thread);
                    }
                    if (wNode12.status != 1 || (wNode5 = wNode12.prev) == null) {
                        break;
                    }
                    wNode.prev = wNode5;
                    f12594U.compareAndSwapObject(wNode5, WNEXT, wNode12, wNode3);
                    wNode11 = wNode5;
                }
            }
        }
        while (true) {
            WNode wNode17 = this.whead;
            if (wNode17 == null) {
                break;
            }
            WNode wNode18 = wNode17.next;
            WNode wNode19 = wNode18;
            if (wNode18 == null || wNode19.status == 1) {
                WNode wNode20 = this.wtail;
                while (true) {
                    WNode wNode21 = wNode20;
                    if (wNode21 == null || wNode21 == wNode17) {
                        break;
                    }
                    if (wNode21.status <= 0) {
                        wNode19 = wNode21;
                    }
                    wNode20 = wNode21.prev;
                }
            }
            if (wNode17 == this.whead) {
                if (wNode19 != null && wNode17.status == 0) {
                    long j2 = this.state;
                    if ((j2 & ABITS) != 128 && (j2 == 0 || wNode19.mode == 0)) {
                        release(wNode17);
                    }
                }
            }
        }
        return (z2 || Thread.interrupted()) ? 1L : 0L;
    }
}
