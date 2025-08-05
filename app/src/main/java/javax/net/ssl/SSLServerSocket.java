package javax.net.ssl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

/* loaded from: rt.jar:javax/net/ssl/SSLServerSocket.class */
public abstract class SSLServerSocket extends ServerSocket {
    public abstract String[] getEnabledCipherSuites();

    public abstract void setEnabledCipherSuites(String[] strArr);

    public abstract String[] getSupportedCipherSuites();

    public abstract String[] getSupportedProtocols();

    public abstract String[] getEnabledProtocols();

    public abstract void setEnabledProtocols(String[] strArr);

    public abstract void setNeedClientAuth(boolean z2);

    public abstract boolean getNeedClientAuth();

    public abstract void setWantClientAuth(boolean z2);

    public abstract boolean getWantClientAuth();

    public abstract void setUseClientMode(boolean z2);

    public abstract boolean getUseClientMode();

    public abstract void setEnableSessionCreation(boolean z2);

    public abstract boolean getEnableSessionCreation();

    protected SSLServerSocket() throws IOException {
    }

    protected SSLServerSocket(int i2) throws IOException {
        super(i2);
    }

    protected SSLServerSocket(int i2, int i3) throws IOException {
        super(i2, i3);
    }

    protected SSLServerSocket(int i2, int i3, InetAddress inetAddress) throws IOException {
        super(i2, i3, inetAddress);
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
}
