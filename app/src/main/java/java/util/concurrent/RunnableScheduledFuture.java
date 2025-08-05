package java.util.concurrent;

/* loaded from: rt.jar:java/util/concurrent/RunnableScheduledFuture.class */
public interface RunnableScheduledFuture<V> extends RunnableFuture<V>, ScheduledFuture<V> {
    boolean isPeriodic();
}
