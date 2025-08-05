package javax.net.ssl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.function.BiFunction;

/* loaded from: rt.jar:javax/net/ssl/SSLSocket.class */
public abstract class SSLSocket extends Socket {
    public abstract String[] getSupportedCipherSuites();

    public abstract String[] getEnabledCipherSuites();

    public abstract void setEnabledCipherSuites(String[] strArr);

    public abstract String[] getSupportedProtocols();

    public abstract String[] getEnabledProtocols();

    public abstract void setEnabledProtocols(String[] strArr);

    public abstract SSLSession getSession();

    public abstract void addHandshakeCompletedListener(HandshakeCompletedListener handshakeCompletedListener);

    public abstract void removeHandshakeCompletedListener(HandshakeCompletedListener handshakeCompletedListener);

    public abstract void startHandshake() throws IOException;

    public abstract void setUseClientMode(boolean z2);

    public abstract boolean getUseClientMode();

    public abstract void setNeedClientAuth(boolean z2);

    public abstract boolean getNeedClientAuth();

    public abstract void setWantClientAuth(boolean z2);

    public abstract boolean getWantClientAuth();

    public abstract void setEnableSessionCreation(boolean z2);

    public abstract boolean getEnableSessionCreation();

    protected SSLSocket() {
    }

    protected SSLSocket(String str, int i2) throws IOException {
        super(str, i2);
    }

    protected SSLSocket(InetAddress inetAddress, int i2) throws IOException {
        super(inetAddress, i2);
    }

    protected SSLSocket(String str, int i2, InetAddress inetAddress, int i3) throws IOException {
        super(str, i2, inetAddress, i3);
    }

    protected SSLSocket(InetAddress inetAddress, int i2, InetAddress inetAddress2, int i3) throws IOException {
        super(inetAddress, i2, inetAddress2, i3);
    }

    public SSLSession getHandshakeSession() {
        throw new UnsupportedOperationException();
    }

    public SSLParameters getSSLParameters() {
        SSLParameters sSLParameters = new SSLParameters();
        sSLParameters.setCipherSuites(getEnabledCipherSuites());
        sSLParameters.setProtocols(getEnabledProtocols());
        if (getNeedClientAuth()) {
            sSLParameters.setNeedClientAuth(true);
        } else if (getWantClientAuth()) {
            sSLParameters.setWantClientAuth(true);
        }
        return sSLParameters;
    }

    public void setSSLParameters(SSLParameters sSLParameters) {
        String[] cipherSuites = sSLParameters.getCipherSuites();
        if (cipherSuites != null) {
            setEnabledCipherSuites(cipherSuites);
        }
        String[] protocols = sSLParameters.getProtocols();
        if (protocols != null) {
            setEnabledProtocols(protocols);
        }
        if (sSLParameters.getNeedClientAuth()) {
            setNeedClientAuth(true);
        } else if (sSLParameters.getWantClientAuth()) {
            setWantClientAuth(true);
        } else {
            setWantClientAuth(false);
        }
    }

    public String getApplicationProtocol() {
        throw new UnsupportedOperationException();
    }

    public String getHandshakeApplicationProtocol() {
        throw new UnsupportedOperationException();
    }

    public void setHandshakeApplicationProtocolSelector(BiFunction<SSLSocket, List<String>, String> biFunction) {
        throw new UnsupportedOperationException();
    }

    public BiFunction<SSLSocket, List<String>, String> getHandshakeApplicationProtocolSelector() {
        throw new UnsupportedOperationException();
    }
}
