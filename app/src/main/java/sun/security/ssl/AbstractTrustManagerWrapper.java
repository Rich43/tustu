package sun.security.ssl;

import java.net.Socket;
import java.security.AlgorithmConstraints;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.HashSet;
import javax.net.ssl.ExtendedSSLSession;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509TrustManager;
import sun.security.provider.certpath.AlgorithmChecker;
import sun.security.validator.Validator;

/* compiled from: SSLContextImpl.java */
/* loaded from: jsse.jar:sun/security/ssl/AbstractTrustManagerWrapper.class */
final class AbstractTrustManagerWrapper extends X509ExtendedTrustManager implements X509TrustManager {
    private final X509TrustManager tm;

    AbstractTrustManagerWrapper(X509TrustManager x509TrustManager) {
        this.tm = x509TrustManager;
    }

    @Override // javax.net.ssl.X509TrustManager
    public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        this.tm.checkClientTrusted(x509CertificateArr, str);
    }

    @Override // javax.net.ssl.X509TrustManager
    public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        this.tm.checkServerTrusted(x509CertificateArr, str);
    }

    @Override // javax.net.ssl.X509TrustManager
    public X509Certificate[] getAcceptedIssuers() {
        return this.tm.getAcceptedIssuers();
    }

    @Override // javax.net.ssl.X509ExtendedTrustManager
    public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str, Socket socket) throws CertificateException {
        this.tm.checkClientTrusted(x509CertificateArr, str);
        checkAdditionalTrust(x509CertificateArr, str, socket, true);
    }

    @Override // javax.net.ssl.X509ExtendedTrustManager
    public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str, Socket socket) throws CertificateException {
        this.tm.checkServerTrusted(x509CertificateArr, str);
        checkAdditionalTrust(x509CertificateArr, str, socket, false);
    }

    @Override // javax.net.ssl.X509ExtendedTrustManager
    public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str, SSLEngine sSLEngine) throws CertificateException {
        this.tm.checkClientTrusted(x509CertificateArr, str);
        checkAdditionalTrust(x509CertificateArr, str, sSLEngine, true);
    }

    @Override // javax.net.ssl.X509ExtendedTrustManager
    public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str, SSLEngine sSLEngine) throws CertificateException {
        this.tm.checkServerTrusted(x509CertificateArr, str);
        checkAdditionalTrust(x509CertificateArr, str, sSLEngine, false);
    }

    private void checkAdditionalTrust(X509Certificate[] x509CertificateArr, String str, Socket socket, boolean z2) throws CertificateException {
        SSLAlgorithmConstraints sSLAlgorithmConstraints;
        if (socket != null && socket.isConnected() && (socket instanceof SSLSocket)) {
            SSLSocket sSLSocket = (SSLSocket) socket;
            SSLSession handshakeSession = sSLSocket.getHandshakeSession();
            if (handshakeSession == null) {
                throw new CertificateException("No handshake session");
            }
            String endpointIdentificationAlgorithm = sSLSocket.getSSLParameters().getEndpointIdentificationAlgorithm();
            if (endpointIdentificationAlgorithm != null && !endpointIdentificationAlgorithm.isEmpty()) {
                X509TrustManagerImpl.checkIdentity(handshakeSession, x509CertificateArr, endpointIdentificationAlgorithm, z2);
            }
            if (ProtocolVersion.useTLS12PlusSpec(handshakeSession.getProtocol()) && (handshakeSession instanceof ExtendedSSLSession)) {
                sSLAlgorithmConstraints = new SSLAlgorithmConstraints(sSLSocket, ((ExtendedSSLSession) handshakeSession).getLocalSupportedSignatureAlgorithms(), true);
            } else {
                sSLAlgorithmConstraints = new SSLAlgorithmConstraints(sSLSocket, true);
            }
            checkAlgorithmConstraints(x509CertificateArr, sSLAlgorithmConstraints, z2);
        }
    }

    private void checkAdditionalTrust(X509Certificate[] x509CertificateArr, String str, SSLEngine sSLEngine, boolean z2) throws CertificateException {
        SSLAlgorithmConstraints sSLAlgorithmConstraints;
        if (sSLEngine != null) {
            SSLSession handshakeSession = sSLEngine.getHandshakeSession();
            if (handshakeSession == null) {
                throw new CertificateException("No handshake session");
            }
            String endpointIdentificationAlgorithm = sSLEngine.getSSLParameters().getEndpointIdentificationAlgorithm();
            if (endpointIdentificationAlgorithm != null && !endpointIdentificationAlgorithm.isEmpty()) {
                X509TrustManagerImpl.checkIdentity(handshakeSession, x509CertificateArr, endpointIdentificationAlgorithm, z2);
            }
            if (ProtocolVersion.useTLS12PlusSpec(handshakeSession.getProtocol()) && (handshakeSession instanceof ExtendedSSLSession)) {
                sSLAlgorithmConstraints = new SSLAlgorithmConstraints(sSLEngine, ((ExtendedSSLSession) handshakeSession).getLocalSupportedSignatureAlgorithms(), true);
            } else {
                sSLAlgorithmConstraints = new SSLAlgorithmConstraints(sSLEngine, true);
            }
            checkAlgorithmConstraints(x509CertificateArr, sSLAlgorithmConstraints, z2);
        }
    }

    private void checkAlgorithmConstraints(X509Certificate[] x509CertificateArr, AlgorithmConstraints algorithmConstraints, boolean z2) throws CertificateException {
        try {
            int length = x509CertificateArr.length - 1;
            HashSet hashSet = new HashSet();
            X509Certificate[] acceptedIssuers = this.tm.getAcceptedIssuers();
            if (acceptedIssuers != null && acceptedIssuers.length > 0) {
                Collections.addAll(hashSet, acceptedIssuers);
            }
            if (hashSet.contains(x509CertificateArr[length])) {
                length--;
            }
            if (length >= 0) {
                AlgorithmChecker algorithmChecker = new AlgorithmChecker(algorithmConstraints, z2 ? Validator.VAR_TLS_CLIENT : Validator.VAR_TLS_SERVER);
                algorithmChecker.init(false);
                for (int i2 = length; i2 >= 0; i2--) {
                    algorithmChecker.check(x509CertificateArr[i2], Collections.emptySet());
                }
            }
        } catch (CertPathValidatorException e2) {
            throw new CertificateException("Certificates do not conform to algorithm constraints", e2);
        }
    }
}
