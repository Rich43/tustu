package javax.net.ssl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;

/* compiled from: SSLServerSocketFactory.java */
/* loaded from: rt.jar:javax/net/ssl/DefaultSSLServerSocketFactory.class */
class DefaultSSLServerSocketFactory extends SSLServerSocketFactory {
    private final Exception reason;

    DefaultSSLServerSocketFactory(Exception exc) {
        this.reason = exc;
    }

    private ServerSocket throwException() throws SocketException {
        throw ((SocketException) new SocketException(this.reason.toString()).initCause(this.reason));
    }

    @Override // javax.net.ServerSocketFactory
    public ServerSocket createServerSocket() throws IOException {
        return throwException();
    }

    @Override // javax.net.ServerSocketFactory
    public ServerSocket createServerSocket(int i2) throws IOException {
        return throwException();
    }

    @Override // javax.net.ServerSocketFactory
    public ServerSocket createServerSocket(int i2, int i3) throws IOException {
        return throwException();
    }

    @Override // javax.net.ServerSocketFactory
    public ServerSocket createServerSocket(int i2, int i3, InetAddress inetAddress) throws IOException {
        return throwException();
    }

    @Override // javax.net.ssl.SSLServerSocketFactory
    public String[] getDefaultCipherSuites() {
        return new String[0];
    }

    @Override // javax.net.ssl.SSLServerSocketFactory
    public String[] getSupportedCipherSuites() {
        return new String[0];
    }
}
