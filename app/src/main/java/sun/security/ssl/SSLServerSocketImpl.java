package sun.security.ssl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLServerSocket;

/* loaded from: jsse.jar:sun/security/ssl/SSLServerSocketImpl.class */
final class SSLServerSocketImpl extends SSLServerSocket {
    private final SSLContextImpl sslContext;
    private final SSLConfiguration sslConfig;

    SSLServerSocketImpl(SSLContextImpl sSLContextImpl) throws IOException {
        this.sslContext = sSLContextImpl;
        this.sslConfig = new SSLConfiguration(sSLContextImpl, false);
    }

    SSLServerSocketImpl(SSLContextImpl sSLContextImpl, int i2, int i3) throws IOException {
        super(i2, i3);
        this.sslContext = sSLContextImpl;
        this.sslConfig = new SSLConfiguration(sSLContextImpl, false);
    }

    SSLServerSocketImpl(SSLContextImpl sSLContextImpl, int i2, int i3, InetAddress inetAddress) throws IOException {
        super(i2, i3, inetAddress);
        this.sslContext = sSLContextImpl;
        this.sslConfig = new SSLConfiguration(sSLContextImpl, false);
    }

    @Override // javax.net.ssl.SSLServerSocket
    public synchronized String[] getEnabledCipherSuites() {
        return CipherSuite.namesOf(this.sslConfig.enabledCipherSuites);
    }

    @Override // javax.net.ssl.SSLServerSocket
    public synchronized void setEnabledCipherSuites(String[] strArr) {
        this.sslConfig.enabledCipherSuites = CipherSuite.validValuesOf(strArr);
    }

    @Override // javax.net.ssl.SSLServerSocket
    public String[] getSupportedCipherSuites() {
        return CipherSuite.namesOf(this.sslContext.getSupportedCipherSuites());
    }

    @Override // javax.net.ssl.SSLServerSocket
    public String[] getSupportedProtocols() {
        return ProtocolVersion.toStringArray(this.sslContext.getSupportedProtocolVersions());
    }

    @Override // javax.net.ssl.SSLServerSocket
    public synchronized String[] getEnabledProtocols() {
        return ProtocolVersion.toStringArray(this.sslConfig.enabledProtocols);
    }

    @Override // javax.net.ssl.SSLServerSocket
    public synchronized void setEnabledProtocols(String[] strArr) {
        if (strArr == null) {
            throw new IllegalArgumentException("Protocols cannot be null");
        }
        this.sslConfig.enabledProtocols = ProtocolVersion.namesOf(strArr);
    }

    @Override // javax.net.ssl.SSLServerSocket
    public synchronized void setNeedClientAuth(boolean z2) {
        this.sslConfig.clientAuthType = z2 ? ClientAuthType.CLIENT_AUTH_REQUIRED : ClientAuthType.CLIENT_AUTH_NONE;
    }

    @Override // javax.net.ssl.SSLServerSocket
    public synchronized boolean getNeedClientAuth() {
        return this.sslConfig.clientAuthType == ClientAuthType.CLIENT_AUTH_REQUIRED;
    }

    @Override // javax.net.ssl.SSLServerSocket
    public synchronized void setWantClientAuth(boolean z2) {
        this.sslConfig.clientAuthType = z2 ? ClientAuthType.CLIENT_AUTH_REQUESTED : ClientAuthType.CLIENT_AUTH_NONE;
    }

    @Override // javax.net.ssl.SSLServerSocket
    public synchronized boolean getWantClientAuth() {
        return this.sslConfig.clientAuthType == ClientAuthType.CLIENT_AUTH_REQUESTED;
    }

    @Override // javax.net.ssl.SSLServerSocket
    public synchronized void setUseClientMode(boolean z2) {
        if (this.sslConfig.isClientMode != z2) {
            if (this.sslContext.isDefaultProtocolVesions(this.sslConfig.enabledProtocols)) {
                this.sslConfig.enabledProtocols = this.sslContext.getDefaultProtocolVersions(!z2);
            }
            if (this.sslContext.isDefaultCipherSuiteList(this.sslConfig.enabledCipherSuites)) {
                this.sslConfig.enabledCipherSuites = this.sslContext.getDefaultCipherSuites(!z2);
            }
            this.sslConfig.toggleClientMode();
        }
    }

    @Override // javax.net.ssl.SSLServerSocket
    public synchronized boolean getUseClientMode() {
        return this.sslConfig.isClientMode;
    }

    @Override // javax.net.ssl.SSLServerSocket
    public synchronized void setEnableSessionCreation(boolean z2) {
        this.sslConfig.enableSessionCreation = z2;
    }

    @Override // javax.net.ssl.SSLServerSocket
    public synchronized boolean getEnableSessionCreation() {
        return this.sslConfig.enableSessionCreation;
    }

    @Override // javax.net.ssl.SSLServerSocket
    public synchronized SSLParameters getSSLParameters() {
        return this.sslConfig.getSSLParameters();
    }

    @Override // javax.net.ssl.SSLServerSocket
    public synchronized void setSSLParameters(SSLParameters sSLParameters) {
        this.sslConfig.setSSLParameters(sSLParameters);
    }

    @Override // java.net.ServerSocket
    public Socket accept() throws IOException {
        SSLSocketImpl sSLSocketImpl = new SSLSocketImpl(this.sslContext, this.sslConfig);
        implAccept(sSLSocketImpl);
        sSLSocketImpl.doneConnect();
        return sSLSocketImpl;
    }

    @Override // java.net.ServerSocket
    public String toString() {
        return "[SSL: " + super.toString() + "]";
    }
}
