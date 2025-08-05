package sun.nio.ch;

import java.nio.channels.AsynchronousCloseException;
import java.util.HashMap;
import java.util.Map;
import sun.misc.Unsafe;
import sun.nio.ch.Iocp;

/* loaded from: rt.jar:sun/nio/ch/PendingIoCache.class */
class PendingIoCache {
    private static final Unsafe unsafe;
    private static final int addressSize;
    private static final int SIZEOF_OVERLAPPED;
    private boolean closed;
    private boolean closePending;
    private final Map<Long, PendingFuture> pendingIoMap = new HashMap();
    private long[] overlappedCache = new long[4];
    private int overlappedCacheCount = 0;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !PendingIoCache.class.desiredAssertionStatus();
        unsafe = Unsafe.getUnsafe();
        addressSize = unsafe.addressSize();
        SIZEOF_OVERLAPPED = dependsArch(20, 32);
    }

    private static int dependsArch(int i2, int i3) {
        return addressSize == 4 ? i2 : i3;
    }

    PendingIoCache() {
    }

    long add(PendingFuture<?, ?> pendingFuture) {
        long jAllocateMemory;
        long j2;
        synchronized (this) {
            if (this.closed) {
                throw new AssertionError((Object) "Should not get here");
            }
            if (this.overlappedCacheCount > 0) {
                long[] jArr = this.overlappedCache;
                int i2 = this.overlappedCacheCount - 1;
                this.overlappedCacheCount = i2;
                jAllocateMemory = jArr[i2];
            } else {
                jAllocateMemory = unsafe.allocateMemory(SIZEOF_OVERLAPPED);
            }
            this.pendingIoMap.put(Long.valueOf(jAllocateMemory), pendingFuture);
            j2 = jAllocateMemory;
        }
        return j2;
    }

    <V, A> PendingFuture<V, A> remove(long j2) {
        PendingFuture<V, A> pendingFutureRemove;
        synchronized (this) {
            pendingFutureRemove = this.pendingIoMap.remove(Long.valueOf(j2));
            if (pendingFutureRemove != null) {
                if (this.overlappedCacheCount < this.overlappedCache.length) {
                    long[] jArr = this.overlappedCache;
                    int i2 = this.overlappedCacheCount;
                    this.overlappedCacheCount = i2 + 1;
                    jArr[i2] = j2;
                } else {
                    unsafe.freeMemory(j2);
                }
                if (this.closePending) {
                    notifyAll();
                }
            }
        }
        return pendingFutureRemove;
    }

    void close() {
        synchronized (this) {
            if (this.closed) {
                return;
            }
            if (!this.pendingIoMap.isEmpty()) {
                clearPendingIoMap();
            }
            while (this.overlappedCacheCount > 0) {
                Unsafe unsafe2 = unsafe;
                long[] jArr = this.overlappedCache;
                int i2 = this.overlappedCacheCount - 1;
                this.overlappedCacheCount = i2;
                unsafe2.freeMemory(jArr[i2]);
            }
            this.closed = true;
        }
    }

    private void clearPendingIoMap() {
        if (!$assertionsDisabled && !Thread.holdsLock(this)) {
            throw new AssertionError();
        }
        this.closePending = true;
        try {
            wait(50L);
        } catch (InterruptedException e2) {
            Thread.currentThread().interrupt();
        }
        this.closePending = false;
        if (this.pendingIoMap.isEmpty()) {
            return;
        }
        for (Long l2 : this.pendingIoMap.keySet()) {
            PendingFuture pendingFuture = this.pendingIoMap.get(l2);
            if (!$assertionsDisabled && pendingFuture.isDone()) {
                throw new AssertionError();
            }
            Iocp iocp = (Iocp) ((Groupable) pendingFuture.channel()).group();
            iocp.makeStale(l2);
            final Iocp.ResultHandler resultHandler = (Iocp.ResultHandler) pendingFuture.getContext();
            iocp.executeOnPooledThread(new Runnable() { // from class: sun.nio.ch.PendingIoCache.1
                @Override // java.lang.Runnable
                public void run() {
                    resultHandler.failed(-1, new AsynchronousCloseException());
                }
            });
        }
        this.pendingIoMap.clear();
    }
}
