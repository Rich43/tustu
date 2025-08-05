package sun.security.ssl;

import java.io.IOException;
import java.security.cert.X509Certificate;
import sun.security.ssl.ClientHello;

/* loaded from: jsse.jar:sun/security/ssl/ClientHandshakeContext.class */
class ClientHandshakeContext extends HandshakeContext {
    static final boolean allowUnsafeServerCertChange = Utilities.getBooleanProperty("jdk.tls.allowUnsafeServerCertChange", false);
    X509Certificate[] reservedServerCerts;
    X509Certificate[] deferredCerts;
    ClientHello.ClientHelloMessage initialClientHelloMsg;
    boolean receivedCertReq;
    byte[] pskIdentity;

    ClientHandshakeContext(SSLContextImpl sSLContextImpl, TransportContext transportContext) throws IOException {
        super(sSLContextImpl, transportContext);
        this.reservedServerCerts = null;
        this.initialClientHelloMsg = null;
        this.receivedCertReq = false;
    }

    @Override // sun.security.ssl.HandshakeContext
    void kickstart() throws IOException {
        if (this.kickstartMessageDelivered) {
            return;
        }
        SSLHandshake.kickstart(this);
        this.kickstartMessageDelivered = true;
    }
}
