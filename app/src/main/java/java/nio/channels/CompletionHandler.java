package java.nio.channels;

/* loaded from: rt.jar:java/nio/channels/CompletionHandler.class */
public interface CompletionHandler<V, A> {
    void completed(V v2, A a2);

    void failed(Throwable th, A a2);
}
