package java.util.concurrent;

/* loaded from: rt.jar:java/util/concurrent/RunnableFuture.class */
public interface RunnableFuture<V> extends Runnable, Future<V> {
    @Override // java.lang.Runnable
    void run();
}
