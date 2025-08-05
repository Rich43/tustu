package sun.net;

import java.net.Proxy;
import java.net.SocketAddress;

/* loaded from: rt.jar:sun/net/SocksProxy.class */
public final class SocksProxy extends Proxy {
    private final int version;

    private SocksProxy(SocketAddress socketAddress, int i2) {
        super(Proxy.Type.SOCKS, socketAddress);
        this.version = i2;
    }

    public static SocksProxy create(SocketAddress socketAddress, int i2) {
        return new SocksProxy(socketAddress, i2);
    }

    public int protocolVersion() {
        return this.version;
    }
}
