package java.util.concurrent;

/* loaded from: rt.jar:java/util/concurrent/TransferQueue.class */
public interface TransferQueue<E> extends BlockingQueue<E> {
    boolean tryTransfer(E e2);

    void transfer(E e2) throws InterruptedException;

    boolean tryTransfer(E e2, long j2, TimeUnit timeUnit) throws InterruptedException;

    boolean hasWaitingConsumer();

    int getWaitingConsumerCount();
}
