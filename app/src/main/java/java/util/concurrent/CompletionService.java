package java.util.concurrent;

/* loaded from: rt.jar:java/util/concurrent/CompletionService.class */
public interface CompletionService<V> {
    Future<V> submit(Callable<V> callable);

    Future<V> submit(Runnable runnable, V v2);

    Future<V> take() throws InterruptedException;

    Future<V> poll();

    Future<V> poll(long j2, TimeUnit timeUnit) throws InterruptedException;
}
