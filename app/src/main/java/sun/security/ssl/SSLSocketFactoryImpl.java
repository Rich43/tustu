package sun.security.ssl;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import javax.net.ssl.SSLSocketFactory;
import sun.security.ssl.SSLContextImpl;

/* loaded from: jsse.jar:sun/security/ssl/SSLSocketFactoryImpl.class */
public final class SSLSocketFactoryImpl extends SSLSocketFactory {
    private final SSLContextImpl context;

    public SSLSocketFactoryImpl() throws Exception {
        this.context = SSLContextImpl.DefaultSSLContext.getDefaultImpl();
    }

    SSLSocketFactoryImpl(SSLContextImpl sSLContextImpl) {
        this.context = sSLContextImpl;
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket() {
        return new SSLSocketImpl(this.context);
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(String str, int i2) throws IOException {
        return new SSLSocketImpl(this.context, str, i2);
    }

    @Override // javax.net.ssl.SSLSocketFactory
    public Socket createSocket(Socket socket, String str, int i2, boolean z2) throws IOException {
        return new SSLSocketImpl(this.context, socket, str, i2, z2);
    }

    @Override // javax.net.ssl.SSLSocketFactory
    public Socket createSocket(Socket socket, InputStream inputStream, boolean z2) throws IOException {
        if (socket == null) {
            throw new NullPointerException("the existing socket cannot be null");
        }
        return new SSLSocketImpl(this.context, socket, inputStream, z2);
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(InetAddress inetAddress, int i2) throws IOException {
        return new SSLSocketImpl(this.context, inetAddress, i2);
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(String str, int i2, InetAddress inetAddress, int i3) throws IOException {
        return new SSLSocketImpl(this.context, str, i2, inetAddress, i3);
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(InetAddress inetAddress, int i2, InetAddress inetAddress2, int i3) throws IOException {
        return new SSLSocketImpl(this.context, inetAddress, i2, inetAddress2, i3);
    }

    @Override // javax.net.ssl.SSLSocketFactory
    public String[] getDefaultCipherSuites() {
        return CipherSuite.namesOf(this.context.getDefaultCipherSuites(false));
    }

    @Override // javax.net.ssl.SSLSocketFactory
    public String[] getSupportedCipherSuites() {
        return CipherSuite.namesOf(this.context.getSupportedCipherSuites());
    }
}
