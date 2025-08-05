package java.util.concurrent;

/* loaded from: rt.jar:java/util/concurrent/Delayed.class */
public interface Delayed extends Comparable<Delayed> {
    long getDelay(TimeUnit timeUnit);
}
