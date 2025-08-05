package sun.security.ssl;

import java.io.IOException;
import java.security.AccessController;
import java.security.AlgorithmConstraints;
import sun.security.action.GetLongAction;
import sun.security.ssl.CertificateMessage;
import sun.security.ssl.StatusResponseManager;
import sun.security.util.LegacyAlgorithmConstraints;

/* loaded from: jsse.jar:sun/security/ssl/ServerHandshakeContext.class */
class ServerHandshakeContext extends HandshakeContext {
    static final boolean rejectClientInitiatedRenego = Utilities.getBooleanProperty("jdk.tls.rejectClientInitiatedRenegotiation", false);
    static final AlgorithmConstraints legacyAlgorithmConstraints = new LegacyAlgorithmConstraints(LegacyAlgorithmConstraints.PROPERTY_TLS_LEGACY_ALGS, new SSLAlgorithmDecomposer());
    SSLPossession interimAuthn;
    StatusResponseManager.StaplingParameters stapleParams;
    CertificateMessage.CertificateEntry currentCertEntry;
    private static final long DEFAULT_STATUS_RESP_DELAY = 5000;
    final long statusRespTimeout;

    ServerHandshakeContext(SSLContextImpl sSLContextImpl, TransportContext transportContext) throws IOException {
        super(sSLContextImpl, transportContext);
        long jLongValue = ((Long) AccessController.doPrivileged(new GetLongAction("jdk.tls.stapling.responseTimeout", 5000L))).longValue();
        this.statusRespTimeout = jLongValue >= 0 ? jLongValue : 5000L;
        this.handshakeConsumers.put(Byte.valueOf(SSLHandshake.CLIENT_HELLO.id), SSLHandshake.CLIENT_HELLO);
    }

    @Override // sun.security.ssl.HandshakeContext
    void kickstart() throws IOException {
        if (!this.conContext.isNegotiated || this.kickstartMessageDelivered) {
            return;
        }
        SSLHandshake.kickstart(this);
        this.kickstartMessageDelivered = true;
    }
}
