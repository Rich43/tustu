package java.util.concurrent;

import java.util.Collection;
import java.util.List;

/* loaded from: rt.jar:java/util/concurrent/ExecutorService.class */
public interface ExecutorService extends Executor {
    void shutdown();

    List<Runnable> shutdownNow();

    boolean isShutdown();

    boolean isTerminated();

    boolean awaitTermination(long j2, TimeUnit timeUnit) throws InterruptedException;

    <T> Future<T> submit(Callable<T> callable);

    <T> Future<T> submit(Runnable runnable, T t2);

    Future<?> submit(Runnable runnable);

    <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection) throws InterruptedException;

    <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection, long j2, TimeUnit timeUnit) throws InterruptedException;

    <T> T invokeAny(Collection<? extends Callable<T>> collection) throws ExecutionException, InterruptedException;

    <T> T invokeAny(Collection<? extends Callable<T>> collection, long j2, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException;
}
