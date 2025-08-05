package javax.net.ssl;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.function.BiFunction;
import javax.net.ssl.SSLEngineResult;

/* loaded from: rt.jar:javax/net/ssl/SSLEngine.class */
public abstract class SSLEngine {
    private String peerHost;
    private int peerPort;

    public abstract SSLEngineResult wrap(ByteBuffer[] byteBufferArr, int i2, int i3, ByteBuffer byteBuffer) throws SSLException;

    public abstract SSLEngineResult unwrap(ByteBuffer byteBuffer, ByteBuffer[] byteBufferArr, int i2, int i3) throws SSLException;

    public abstract Runnable getDelegatedTask();

    public abstract void closeInbound() throws SSLException;

    public abstract boolean isInboundDone();

    public abstract void closeOutbound();

    public abstract boolean isOutboundDone();

    public abstract String[] getSupportedCipherSuites();

    public abstract String[] getEnabledCipherSuites();

    public abstract void setEnabledCipherSuites(String[] strArr);

    public abstract String[] getSupportedProtocols();

    public abstract String[] getEnabledProtocols();

    public abstract void setEnabledProtocols(String[] strArr);

    public abstract SSLSession getSession();

    public abstract void beginHandshake() throws SSLException;

    public abstract SSLEngineResult.HandshakeStatus getHandshakeStatus();

    public abstract void setUseClientMode(boolean z2);

    public abstract boolean getUseClientMode();

    public abstract void setNeedClientAuth(boolean z2);

    public abstract boolean getNeedClientAuth();

    public abstract void setWantClientAuth(boolean z2);

    public abstract boolean getWantClientAuth();

    public abstract void setEnableSessionCreation(boolean z2);

    public abstract boolean getEnableSessionCreation();

    protected SSLEngine() {
        this.peerHost = null;
        this.peerPort = -1;
    }

    protected SSLEngine(String str, int i2) {
        this.peerHost = null;
        this.peerPort = -1;
        this.peerHost = str;
        this.peerPort = i2;
    }

    public String getPeerHost() {
        return this.peerHost;
    }

    public int getPeerPort() {
        return this.peerPort;
    }

    public SSLEngineResult wrap(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) throws SSLException {
        return wrap(new ByteBuffer[]{byteBuffer}, 0, 1, byteBuffer2);
    }

    public SSLEngineResult wrap(ByteBuffer[] byteBufferArr, ByteBuffer byteBuffer) throws SSLException {
        if (byteBufferArr == null) {
            throw new IllegalArgumentException("src == null");
        }
        return wrap(byteBufferArr, 0, byteBufferArr.length, byteBuffer);
    }

    public SSLEngineResult unwrap(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) throws SSLException {
        return unwrap(byteBuffer, new ByteBuffer[]{byteBuffer2}, 0, 1);
    }

    public SSLEngineResult unwrap(ByteBuffer byteBuffer, ByteBuffer[] byteBufferArr) throws SSLException {
        if (byteBufferArr == null) {
            throw new IllegalArgumentException("dsts == null");
        }
        return unwrap(byteBuffer, byteBufferArr, 0, byteBufferArr.length);
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

    public void setHandshakeApplicationProtocolSelector(BiFunction<SSLEngine, List<String>, String> biFunction) {
        throw new UnsupportedOperationException();
    }

    public BiFunction<SSLEngine, List<String>, String> getHandshakeApplicationProtocolSelector() {
        throw new UnsupportedOperationException();
    }
}
