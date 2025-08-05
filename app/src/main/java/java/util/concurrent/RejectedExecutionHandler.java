package java.util.concurrent;

/* loaded from: rt.jar:java/util/concurrent/RejectedExecutionHandler.class */
public interface RejectedExecutionHandler {
    void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor);
}
