package sun.nio.ch;

import java.io.IOException;
import java.nio.channels.AsynchronousChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* loaded from: rt.jar:sun/nio/ch/PendingFuture.class */
final class PendingFuture<V, A> implements Future<V> {
    private static final CancellationException CANCELLED = new CancellationException();
    private final AsynchronousChannel channel;
    private final CompletionHandler<V, ? super A> handler;
    private final A attachment;
    private volatile boolean haveResult;
    private volatile V result;
    private volatile Throwable exc;
    private CountDownLatch latch;
    private Future<?> timeoutTask;
    private volatile Object context;

    PendingFuture(AsynchronousChannel asynchronousChannel, CompletionHandler<V, ? super A> completionHandler, A a2, Object obj) {
        this.channel = asynchronousChannel;
        this.handler = completionHandler;
        this.attachment = a2;
        this.context = obj;
    }

    PendingFuture(AsynchronousChannel asynchronousChannel, CompletionHandler<V, ? super A> completionHandler, A a2) {
        this.channel = asynchronousChannel;
        this.handler = completionHandler;
        this.attachment = a2;
    }

    PendingFuture(AsynchronousChannel asynchronousChannel) {
        this(asynchronousChannel, null, null);
    }

    PendingFuture(AsynchronousChannel asynchronousChannel, Object obj) {
        this(asynchronousChannel, null, null, obj);
    }

    AsynchronousChannel channel() {
        return this.channel;
    }

    CompletionHandler<V, ? super A> handler() {
        return this.handler;
    }

    A attachment() {
        return this.attachment;
    }

    void setContext(Object obj) {
        this.context = obj;
    }

    Object getContext() {
        return this.context;
    }

    void setTimeoutTask(Future<?> future) {
        synchronized (this) {
            if (this.haveResult) {
                future.cancel(false);
            } else {
                this.timeoutTask = future;
            }
        }
    }

    private boolean prepareForWait() {
        synchronized (this) {
            if (this.haveResult) {
                return false;
            }
            if (this.latch == null) {
                this.latch = new CountDownLatch(1);
            }
            return true;
        }
    }

    void setResult(V v2) {
        synchronized (this) {
            if (this.haveResult) {
                return;
            }
            this.result = v2;
            this.haveResult = true;
            if (this.timeoutTask != null) {
                this.timeoutTask.cancel(false);
            }
            if (this.latch != null) {
                this.latch.countDown();
            }
        }
    }

    void setFailure(Throwable th) {
        if (!(th instanceof IOException) && !(th instanceof SecurityException)) {
            th = new IOException(th);
        }
        synchronized (this) {
            if (this.haveResult) {
                return;
            }
            this.exc = th;
            this.haveResult = true;
            if (this.timeoutTask != null) {
                this.timeoutTask.cancel(false);
            }
            if (this.latch != null) {
                this.latch.countDown();
            }
        }
    }

    void setResult(V v2, Throwable th) {
        if (th == null) {
            setResult(v2);
        } else {
            setFailure(th);
        }
    }

    @Override // java.util.concurrent.Future
    public V get() throws ExecutionException, InterruptedException {
        if (!this.haveResult && prepareForWait()) {
            this.latch.await();
        }
        if (this.exc != null) {
            if (this.exc == CANCELLED) {
                throw new CancellationException();
            }
            throw new ExecutionException(this.exc);
        }
        return this.result;
    }

    @Override // java.util.concurrent.Future
    public V get(long j2, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
        if (!this.haveResult && prepareForWait() && !this.latch.await(j2, timeUnit)) {
            throw new TimeoutException();
        }
        if (this.exc != null) {
            if (this.exc == CANCELLED) {
                throw new CancellationException();
            }
            throw new ExecutionException(this.exc);
        }
        return this.result;
    }

    Throwable exception() {
        if (this.exc != CANCELLED) {
            return this.exc;
        }
        return null;
    }

    V value() {
        return this.result;
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return this.exc == CANCELLED;
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        return this.haveResult;
    }

    @Override // java.util.concurrent.Future
    public boolean cancel(boolean z2) {
        synchronized (this) {
            if (this.haveResult) {
                return false;
            }
            if (channel() instanceof Cancellable) {
                ((Cancellable) channel()).onCancel(this);
            }
            this.exc = CANCELLED;
            this.haveResult = true;
            if (this.timeoutTask != null) {
                this.timeoutTask.cancel(false);
            }
            if (z2) {
                try {
                    channel().close();
                } catch (IOException e2) {
                }
            }
            if (this.latch != null) {
                this.latch.countDown();
                return true;
            }
            return true;
        }
    }
}
