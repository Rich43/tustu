package java.util.concurrent;

@FunctionalInterface
/* loaded from: rt.jar:java/util/concurrent/Callable.class */
public interface Callable<V> {
    V call() throws Exception;
}
