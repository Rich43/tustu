package javax.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/* compiled from: SocketFactory.java */
/* loaded from: rt.jar:javax/net/DefaultSocketFactory.class */
class DefaultSocketFactory extends SocketFactory {
    DefaultSocketFactory() {
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket() {
        return new Socket();
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(String str, int i2) throws IOException {
        return new Socket(str, i2);
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(InetAddress inetAddress, int i2) throws IOException {
        return new Socket(inetAddress, i2);
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(String str, int i2, InetAddress inetAddress, int i3) throws IOException {
        return new Socket(str, i2, inetAddress, i3);
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(InetAddress inetAddress, int i2, InetAddress inetAddress2, int i3) throws IOException {
        return new Socket(inetAddress, i2, inetAddress2, i3);
    }
}
