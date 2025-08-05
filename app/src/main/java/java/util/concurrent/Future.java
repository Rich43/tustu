package java.util.concurrent;

/* loaded from: rt.jar:java/util/concurrent/Future.class */
public interface Future<V> {
    boolean cancel(boolean z2);

    boolean isCancelled();

    boolean isDone();

    V get() throws ExecutionException, InterruptedException;

    V get(long j2, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException;
}
