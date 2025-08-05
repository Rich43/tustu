package java.net;

/* loaded from: rt.jar:java/net/SocketOption.class */
public interface SocketOption<T> {
    String name();

    Class<T> type();
}
