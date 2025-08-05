package java.util.concurrent;

import java.util.concurrent.locks.LockSupport;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/util/concurrent/FutureTask.class */
public class FutureTask<V> implements RunnableFuture<V> {
    private volatile int state;
    private static final int NEW = 0;
    private static final int COMPLETING = 1;
    private static final int NORMAL = 2;
    private static final int EXCEPTIONAL = 3;
    private static final int CANCELLED = 4;
    private static final int INTERRUPTING = 5;
    private static final int INTERRUPTED = 6;
    private Callable<V> callable;
    private Object outcome;
    private volatile Thread runner;
    private volatile WaitNode waiters;
    private static final Unsafe UNSAFE;
    private static final long stateOffset;
    private static final long runnerOffset;
    private static final long waitersOffset;

    /* JADX WARN: Multi-variable type inference failed */
    private V report(int i2) throws ExecutionException {
        V v2 = (V) this.outcome;
        if (i2 == 2) {
            return v2;
        }
        if (i2 >= 4) {
            throw new CancellationException();
        }
        throw new ExecutionException((Throwable) v2);
    }

    public FutureTask(Callable<V> callable) {
        if (callable == null) {
            throw new NullPointerException();
        }
        this.callable = callable;
        this.state = 0;
    }

    public FutureTask(Runnable runnable, V v2) {
        this.callable = Executors.callable(runnable, v2);
        this.state = 0;
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return this.state >= 4;
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        return this.state != 0;
    }

    public boolean cancel(boolean z2) {
        if (this.state == 0) {
            if (!UNSAFE.compareAndSwapInt(this, stateOffset, 0, z2 ? 5 : 4)) {
                return false;
            }
            if (z2) {
                try {
                    try {
                        Thread thread = this.runner;
                        if (thread != null) {
                            thread.interrupt();
                        }
                        UNSAFE.putOrderedInt(this, stateOffset, 6);
                    } catch (Throwable th) {
                        UNSAFE.putOrderedInt(this, stateOffset, 6);
                        throw th;
                    }
                } finally {
                    finishCompletion();
                }
            }
            return true;
        }
        return false;
    }

    public V get() throws ExecutionException, InterruptedException {
        int iAwaitDone = this.state;
        if (iAwaitDone <= 1) {
            iAwaitDone = awaitDone(false, 0L);
        }
        return report(iAwaitDone);
    }

    @Override // java.util.concurrent.Future
    public V get(long j2, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
        if (timeUnit == null) {
            throw new NullPointerException();
        }
        int i2 = this.state;
        if (i2 <= 1) {
            int iAwaitDone = awaitDone(true, timeUnit.toNanos(j2));
            i2 = iAwaitDone;
            if (iAwaitDone <= 1) {
                throw new TimeoutException();
            }
        }
        return report(i2);
    }

    protected void done() {
    }

    protected void set(V v2) {
        if (UNSAFE.compareAndSwapInt(this, stateOffset, 0, 1)) {
            this.outcome = v2;
            UNSAFE.putOrderedInt(this, stateOffset, 2);
            finishCompletion();
        }
    }

    protected void setException(Throwable th) {
        if (UNSAFE.compareAndSwapInt(this, stateOffset, 0, 1)) {
            this.outcome = th;
            UNSAFE.putOrderedInt(this, stateOffset, 3);
            finishCompletion();
        }
    }

    @Override // java.util.concurrent.RunnableFuture, java.lang.Runnable
    public void run() {
        V vCall;
        boolean z2;
        if (this.state != 0 || !UNSAFE.compareAndSwapObject(this, runnerOffset, null, Thread.currentThread())) {
            return;
        }
        try {
            Callable<V> callable = this.callable;
            if (callable != null && this.state == 0) {
                try {
                    vCall = callable.call();
                    z2 = true;
                } catch (Throwable th) {
                    vCall = null;
                    z2 = false;
                    setException(th);
                }
                if (z2) {
                    set(vCall);
                }
            }
            this.runner = null;
            int i2 = this.state;
            if (i2 >= 5) {
                handlePossibleCancellationInterrupt(i2);
            }
        } catch (Throwable th2) {
            this.runner = null;
            int i3 = this.state;
            if (i3 >= 5) {
                handlePossibleCancellationInterrupt(i3);
            }
            throw th2;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean runAndReset() {
        if (this.state != 0 || !UNSAFE.compareAndSwapObject(this, runnerOffset, null, Thread.currentThread())) {
            return false;
        }
        boolean z2 = false;
        int i2 = this.state;
        try {
            Callable<V> callable = this.callable;
            if (callable != null && i2 == 0) {
                try {
                    callable.call();
                    z2 = true;
                } catch (Throwable th) {
                    setException(th);
                }
            }
            this.runner = null;
            int i3 = this.state;
            if (i3 >= 5) {
                handlePossibleCancellationInterrupt(i3);
            }
            return z2 && i3 == 0;
        } catch (Throwable th2) {
            this.runner = null;
            int i4 = this.state;
            if (i4 >= 5) {
                handlePossibleCancellationInterrupt(i4);
            }
            throw th2;
        }
    }

    private void handlePossibleCancellationInterrupt(int i2) {
        if (i2 == 5) {
            while (this.state == 5) {
                Thread.yield();
            }
        }
    }

    /* loaded from: rt.jar:java/util/concurrent/FutureTask$WaitNode.class */
    static final class WaitNode {
        volatile Thread thread = Thread.currentThread();
        volatile WaitNode next;

        WaitNode() {
        }
    }

    private void finishCompletion() {
        while (true) {
            WaitNode waitNode = this.waiters;
            WaitNode waitNode2 = waitNode;
            if (waitNode == null) {
                break;
            }
            if (UNSAFE.compareAndSwapObject(this, waitersOffset, waitNode2, null)) {
                while (true) {
                    Thread thread = waitNode2.thread;
                    if (thread != null) {
                        waitNode2.thread = null;
                        LockSupport.unpark(thread);
                    }
                    WaitNode waitNode3 = waitNode2.next;
                    if (waitNode3 == null) {
                        break;
                    }
                    waitNode2.next = null;
                    waitNode2 = waitNode3;
                }
            }
        }
        done();
        this.callable = null;
    }

    private int awaitDone(boolean z2, long j2) throws InterruptedException {
        long jNanoTime = z2 ? System.nanoTime() + j2 : 0L;
        WaitNode waitNode = null;
        boolean zCompareAndSwapObject = false;
        while (!Thread.interrupted()) {
            int i2 = this.state;
            if (i2 > 1) {
                if (waitNode != null) {
                    waitNode.thread = null;
                }
                return i2;
            }
            if (i2 == 1) {
                Thread.yield();
            } else if (waitNode == null) {
                waitNode = new WaitNode();
            } else if (!zCompareAndSwapObject) {
                Unsafe unsafe = UNSAFE;
                long j3 = waitersOffset;
                WaitNode waitNode2 = this.waiters;
                waitNode.next = waitNode2;
                zCompareAndSwapObject = unsafe.compareAndSwapObject(this, j3, waitNode2, waitNode);
            } else if (z2) {
                long jNanoTime2 = jNanoTime - System.nanoTime();
                if (jNanoTime2 <= 0) {
                    removeWaiter(waitNode);
                    return this.state;
                }
                LockSupport.parkNanos(this, jNanoTime2);
            } else {
                LockSupport.park(this);
            }
        }
        removeWaiter(waitNode);
        throw new InterruptedException();
    }

    private void removeWaiter(WaitNode waitNode) {
        if (waitNode != null) {
            waitNode.thread = null;
            while (true) {
                WaitNode waitNode2 = null;
                WaitNode waitNode3 = this.waiters;
                while (true) {
                    WaitNode waitNode4 = waitNode3;
                    if (waitNode4 != null) {
                        WaitNode waitNode5 = waitNode4.next;
                        if (waitNode4.thread != null) {
                            waitNode2 = waitNode4;
                        } else if (waitNode2 != null) {
                            waitNode2.next = waitNode5;
                            if (waitNode2.thread == null) {
                                break;
                            }
                        } else if (!UNSAFE.compareAndSwapObject(this, waitersOffset, waitNode4, waitNode5)) {
                            break;
                        }
                        waitNode3 = waitNode5;
                    } else {
                        return;
                    }
                }
            }
        }
    }

    static {
        try {
            UNSAFE = Unsafe.getUnsafe();
            stateOffset = UNSAFE.objectFieldOffset(FutureTask.class.getDeclaredField("state"));
            runnerOffset = UNSAFE.objectFieldOffset(FutureTask.class.getDeclaredField("runner"));
            waitersOffset = UNSAFE.objectFieldOffset(FutureTask.class.getDeclaredField("waiters"));
        } catch (Exception e2) {
            throw new Error(e2);
        }
    }
}
