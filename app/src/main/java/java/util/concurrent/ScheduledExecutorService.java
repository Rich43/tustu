package java.util.concurrent;

/* loaded from: rt.jar:java/util/concurrent/ScheduledExecutorService.class */
public interface ScheduledExecutorService extends ExecutorService {
    ScheduledFuture<?> schedule(Runnable runnable, long j2, TimeUnit timeUnit);

    <V> ScheduledFuture<V> schedule(Callable<V> callable, long j2, TimeUnit timeUnit);

    ScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long j2, long j3, TimeUnit timeUnit);

    ScheduledFuture<?> scheduleWithFixedDelay(Runnable runnable, long j2, long j3, TimeUnit timeUnit);
}
