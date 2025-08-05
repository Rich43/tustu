package sun.nio.ch;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/* loaded from: rt.jar:sun/nio/ch/CompletedFuture.class */
final class CompletedFuture<V> implements Future<V> {
    private final V result;
    private final Throwable exc;

    private CompletedFuture(V v2, Throwable th) {
        this.result = v2;
        this.exc = th;
    }

    static <V> CompletedFuture<V> withResult(V v2) {
        return new CompletedFuture<>(v2, null);
    }

    static <V> CompletedFuture<V> withFailure(Throwable th) {
        if (!(th instanceof IOException) && !(th instanceof SecurityException)) {
            th = new IOException(th);
        }
        return new CompletedFuture<>(null, th);
    }

    static <V> CompletedFuture<V> withResult(V v2, Throwable th) {
        if (th == null) {
            return withResult(v2);
        }
        return withFailure(th);
    }

    @Override // java.util.concurrent.Future
    public V get() throws ExecutionException {
        if (this.exc != null) {
            throw new ExecutionException(this.exc);
        }
        return this.result;
    }

    @Override // java.util.concurrent.Future
    public V get(long j2, TimeUnit timeUnit) throws ExecutionException {
        if (timeUnit == null) {
            throw new NullPointerException();
        }
        if (this.exc != null) {
            throw new ExecutionException(this.exc);
        }
        return this.result;
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return false;
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        return true;
    }

    @Override // java.util.concurrent.Future
    public boolean cancel(boolean z2) {
        return false;
    }
}
