package javax.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

/* compiled from: ServerSocketFactory.java */
/* loaded from: rt.jar:javax/net/DefaultServerSocketFactory.class */
class DefaultServerSocketFactory extends ServerSocketFactory {
    DefaultServerSocketFactory() {
    }

    @Override // javax.net.ServerSocketFactory
    public ServerSocket createServerSocket() throws IOException {
        return new ServerSocket();
    }

    @Override // javax.net.ServerSocketFactory
    public ServerSocket createServerSocket(int i2) throws IOException {
        return new ServerSocket(i2);
    }

    @Override // javax.net.ServerSocketFactory
    public ServerSocket createServerSocket(int i2, int i3) throws IOException {
        return new ServerSocket(i2, i3);
    }

    @Override // javax.net.ServerSocketFactory
    public ServerSocket createServerSocket(int i2, int i3, InetAddress inetAddress) throws IOException {
        return new ServerSocket(i2, i3, inetAddress);
    }
}
