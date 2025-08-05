package javax.rmi.ssl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RMIServerSocketFactory;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/* loaded from: rt.jar:javax/rmi/ssl/SslRMIServerSocketFactory.class */
public class SslRMIServerSocketFactory implements RMIServerSocketFactory {
    private static SSLSocketFactory defaultSSLSocketFactory = null;
    private final String[] enabledCipherSuites;
    private final String[] enabledProtocols;
    private final boolean needClientAuth;
    private List<String> enabledCipherSuitesList;
    private List<String> enabledProtocolsList;
    private SSLContext context;

    public SslRMIServerSocketFactory() {
        this(null, null, false);
    }

    public SslRMIServerSocketFactory(String[] strArr, String[] strArr2, boolean z2) throws IllegalArgumentException {
        this(null, strArr, strArr2, z2);
    }

    public SslRMIServerSocketFactory(SSLContext sSLContext, String[] strArr, String[] strArr2, boolean z2) throws IllegalArgumentException {
        this.enabledCipherSuites = strArr == null ? null : (String[]) strArr.clone();
        this.enabledProtocols = strArr2 == null ? null : (String[]) strArr2.clone();
        this.needClientAuth = z2;
        this.context = sSLContext;
        SSLSocketFactory defaultSSLSocketFactory2 = sSLContext == null ? getDefaultSSLSocketFactory() : sSLContext.getSocketFactory();
        SSLSocket sSLSocket = null;
        if (this.enabledCipherSuites != null || this.enabledProtocols != null) {
            try {
                sSLSocket = (SSLSocket) defaultSSLSocketFactory2.createSocket();
            } catch (Exception e2) {
                throw ((IllegalArgumentException) new IllegalArgumentException("Unable to check if the cipher suites and protocols to enable are supported").initCause(e2));
            }
        }
        if (this.enabledCipherSuites != null) {
            sSLSocket.setEnabledCipherSuites(this.enabledCipherSuites);
            this.enabledCipherSuitesList = Arrays.asList(this.enabledCipherSuites);
        }
        if (this.enabledProtocols != null) {
            sSLSocket.setEnabledProtocols(this.enabledProtocols);
            this.enabledProtocolsList = Arrays.asList(this.enabledProtocols);
        }
    }

    public final String[] getEnabledCipherSuites() {
        if (this.enabledCipherSuites == null) {
            return null;
        }
        return (String[]) this.enabledCipherSuites.clone();
    }

    public final String[] getEnabledProtocols() {
        if (this.enabledProtocols == null) {
            return null;
        }
        return (String[]) this.enabledProtocols.clone();
    }

    public final boolean getNeedClientAuth() {
        return this.needClientAuth;
    }

    @Override // java.rmi.server.RMIServerSocketFactory
    public ServerSocket createServerSocket(int i2) throws IOException {
        final SSLSocketFactory defaultSSLSocketFactory2 = this.context == null ? getDefaultSSLSocketFactory() : this.context.getSocketFactory();
        return new ServerSocket(i2) { // from class: javax.rmi.ssl.SslRMIServerSocketFactory.1
            @Override // java.net.ServerSocket
            public Socket accept() throws IOException {
                Socket socketAccept = super.accept();
                SSLSocket sSLSocket = (SSLSocket) defaultSSLSocketFactory2.createSocket(socketAccept, socketAccept.getInetAddress().getHostName(), socketAccept.getPort(), true);
                sSLSocket.setUseClientMode(false);
                if (SslRMIServerSocketFactory.this.enabledCipherSuites != null) {
                    sSLSocket.setEnabledCipherSuites(SslRMIServerSocketFactory.this.enabledCipherSuites);
                }
                if (SslRMIServerSocketFactory.this.enabledProtocols != null) {
                    sSLSocket.setEnabledProtocols(SslRMIServerSocketFactory.this.enabledProtocols);
                }
                sSLSocket.setNeedClientAuth(SslRMIServerSocketFactory.this.needClientAuth);
                return sSLSocket;
            }
        };
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SslRMIServerSocketFactory)) {
            return false;
        }
        SslRMIServerSocketFactory sslRMIServerSocketFactory = (SslRMIServerSocketFactory) obj;
        return getClass().equals(sslRMIServerSocketFactory.getClass()) && checkParameters(sslRMIServerSocketFactory);
    }

    private boolean checkParameters(SslRMIServerSocketFactory sslRMIServerSocketFactory) {
        if (this.context == null) {
            if (sslRMIServerSocketFactory.context != null) {
                return false;
            }
        } else if (!this.context.equals(sslRMIServerSocketFactory.context)) {
            return false;
        }
        if (this.needClientAuth != sslRMIServerSocketFactory.needClientAuth) {
            return false;
        }
        if (this.enabledCipherSuites == null && sslRMIServerSocketFactory.enabledCipherSuites != null) {
            return false;
        }
        if (this.enabledCipherSuites != null && sslRMIServerSocketFactory.enabledCipherSuites == null) {
            return false;
        }
        if (this.enabledCipherSuites != null && sslRMIServerSocketFactory.enabledCipherSuites != null) {
            if (!this.enabledCipherSuitesList.equals(Arrays.asList(sslRMIServerSocketFactory.enabledCipherSuites))) {
                return false;
            }
        }
        if (this.enabledProtocols == null && sslRMIServerSocketFactory.enabledProtocols != null) {
            return false;
        }
        if (this.enabledProtocols != null && sslRMIServerSocketFactory.enabledProtocols == null) {
            return false;
        }
        if (this.enabledProtocols != null && sslRMIServerSocketFactory.enabledProtocols != null) {
            if (!this.enabledProtocolsList.equals(Arrays.asList(sslRMIServerSocketFactory.enabledProtocols))) {
                return false;
            }
            return true;
        }
        return true;
    }

    public int hashCode() {
        return getClass().hashCode() + (this.context == null ? 0 : this.context.hashCode()) + (this.needClientAuth ? Boolean.TRUE.hashCode() : Boolean.FALSE.hashCode()) + (this.enabledCipherSuites == null ? 0 : this.enabledCipherSuitesList.hashCode()) + (this.enabledProtocols == null ? 0 : this.enabledProtocolsList.hashCode());
    }

    private static synchronized SSLSocketFactory getDefaultSSLSocketFactory() {
        if (defaultSSLSocketFactory == null) {
            defaultSSLSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        }
        return defaultSSLSocketFactory;
    }
}
