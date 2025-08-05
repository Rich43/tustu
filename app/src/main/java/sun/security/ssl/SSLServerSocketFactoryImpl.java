package sun.security.ssl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import sun.security.ssl.SSLContextImpl;

/* loaded from: jsse.jar:sun/security/ssl/SSLServerSocketFactoryImpl.class */
public final class SSLServerSocketFactoryImpl extends SSLServerSocketFactory {
    private static final int DEFAULT_BACKLOG = 50;
    private final SSLContextImpl context;

    public SSLServerSocketFactoryImpl() throws Exception {
        this.context = SSLContextImpl.DefaultSSLContext.getDefaultImpl();
    }

    SSLServerSocketFactoryImpl(SSLContextImpl sSLContextImpl) {
        this.context = sSLContextImpl;
    }

    @Override // javax.net.ServerSocketFactory
    public ServerSocket createServerSocket() throws IOException {
        return new SSLServerSocketImpl(this.context);
    }

    @Override // javax.net.ServerSocketFactory
    public ServerSocket createServerSocket(int i2) throws IOException {
        return new SSLServerSocketImpl(this.context, i2, 50);
    }

    @Override // javax.net.ServerSocketFactory
    public ServerSocket createServerSocket(int i2, int i3) throws IOException {
        return new SSLServerSocketImpl(this.context, i2, i3);
    }

    @Override // javax.net.ServerSocketFactory
    public ServerSocket createServerSocket(int i2, int i3, InetAddress inetAddress) throws IOException {
        return new SSLServerSocketImpl(this.context, i2, i3, inetAddress);
    }

    @Override // javax.net.ssl.SSLServerSocketFactory
    public String[] getDefaultCipherSuites() {
        return CipherSuite.namesOf(this.context.getDefaultCipherSuites(true));
    }

    @Override // javax.net.ssl.SSLServerSocketFactory
    public String[] getSupportedCipherSuites() {
        return CipherSuite.namesOf(this.context.getSupportedCipherSuites());
    }
}
