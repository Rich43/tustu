package javax.net.ssl;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.SecureRandom;

/* loaded from: rt.jar:javax/net/ssl/SSLContextSpi.class */
public abstract class SSLContextSpi {
    protected abstract void engineInit(KeyManager[] keyManagerArr, TrustManager[] trustManagerArr, SecureRandom secureRandom) throws KeyManagementException;

    protected abstract SSLSocketFactory engineGetSocketFactory();

    protected abstract SSLServerSocketFactory engineGetServerSocketFactory();

    protected abstract SSLEngine engineCreateSSLEngine();

    protected abstract SSLEngine engineCreateSSLEngine(String str, int i2);

    protected abstract SSLSessionContext engineGetServerSessionContext();

    protected abstract SSLSessionContext engineGetClientSessionContext();

    private SSLSocket getDefaultSocket() {
        try {
            return (SSLSocket) engineGetSocketFactory().createSocket();
        } catch (IOException e2) {
            throw new UnsupportedOperationException("Could not obtain parameters", e2);
        }
    }

    protected SSLParameters engineGetDefaultSSLParameters() {
        return getDefaultSocket().getSSLParameters();
    }

    protected SSLParameters engineGetSupportedSSLParameters() {
        SSLSocket defaultSocket = getDefaultSocket();
        SSLParameters sSLParameters = new SSLParameters();
        sSLParameters.setCipherSuites(defaultSocket.getSupportedCipherSuites());
        sSLParameters.setProtocols(defaultSocket.getSupportedProtocols());
        return sSLParameters;
    }
}
