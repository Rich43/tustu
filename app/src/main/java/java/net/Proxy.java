package java.net;

/* loaded from: rt.jar:java/net/Proxy.class */
public class Proxy {
    private Type type;
    private SocketAddress sa;
    public static final Proxy NO_PROXY = new Proxy();

    /* loaded from: rt.jar:java/net/Proxy$Type.class */
    public enum Type {
        DIRECT,
        HTTP,
        SOCKS
    }

    private Proxy() {
        this.type = Type.DIRECT;
        this.sa = null;
    }

    public Proxy(Type type, SocketAddress socketAddress) {
        if (type == Type.DIRECT || !(socketAddress instanceof InetSocketAddress)) {
            throw new IllegalArgumentException("type " + ((Object) type) + " is not compatible with address " + ((Object) socketAddress));
        }
        this.type = type;
        this.sa = socketAddress;
    }

    public Type type() {
        return this.type;
    }

    public SocketAddress address() {
        return this.sa;
    }

    public String toString() {
        if (type() == Type.DIRECT) {
            return "DIRECT";
        }
        return ((Object) type()) + " @ " + ((Object) address());
    }

    public final boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Proxy)) {
            return false;
        }
        Proxy proxy = (Proxy) obj;
        if (proxy.type() == type()) {
            if (address() == null) {
                return proxy.address() == null;
            }
            return address().equals(proxy.address());
        }
        return false;
    }

    public final int hashCode() {
        if (address() == null) {
            return type().hashCode();
        }
        return type().hashCode() + address().hashCode();
    }
}
