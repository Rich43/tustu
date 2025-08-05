package sun.security.ssl;

import java.net.Socket;
import java.security.AlgorithmConstraints;
import java.security.cert.CertificateException;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.ExtendedSSLSession;
import javax.net.ssl.SNIHostName;
import javax.net.ssl.SNIServerName;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509TrustManager;
import sun.security.util.AnchorCertificates;
import sun.security.util.HostnameChecker;
import sun.security.validator.Validator;

/* loaded from: jsse.jar:sun/security/ssl/X509TrustManagerImpl.class */
final class X509TrustManagerImpl extends X509ExtendedTrustManager implements X509TrustManager {
    private final String validatorType;
    private final Collection<X509Certificate> trustedCerts;
    private final PKIXBuilderParameters pkixParams;
    private volatile Validator clientValidator;
    private volatile Validator serverValidator;

    X509TrustManagerImpl(String str, Collection<X509Certificate> collection) {
        this.validatorType = str;
        this.pkixParams = null;
        collection = collection == null ? Collections.emptySet() : collection;
        this.trustedCerts = collection;
        if (SSLLogger.isOn && SSLLogger.isOn("ssl,trustmanager")) {
            SSLLogger.fine("adding as trusted certificates", collection.toArray(new X509Certificate[0]));
        }
    }

    X509TrustManagerImpl(String str, PKIXBuilderParameters pKIXBuilderParameters) {
        this.validatorType = str;
        this.pkixParams = pKIXBuilderParameters;
        Validator validator = getValidator(Validator.VAR_TLS_SERVER);
        this.trustedCerts = validator.getTrustedCertificates();
        this.serverValidator = validator;
        if (SSLLogger.isOn && SSLLogger.isOn("ssl,trustmanager")) {
            SSLLogger.fine("adding as trusted certificates", this.trustedCerts.toArray(new X509Certificate[0]));
        }
    }

    @Override // javax.net.ssl.X509TrustManager
    public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        checkTrusted(x509CertificateArr, str, (Socket) null, true);
    }

    @Override // javax.net.ssl.X509TrustManager
    public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        checkTrusted(x509CertificateArr, str, (Socket) null, false);
    }

    @Override // javax.net.ssl.X509TrustManager
    public X509Certificate[] getAcceptedIssuers() {
        X509Certificate[] x509CertificateArr = new X509Certificate[this.trustedCerts.size()];
        this.trustedCerts.toArray(x509CertificateArr);
        return x509CertificateArr;
    }

    @Override // javax.net.ssl.X509ExtendedTrustManager
    public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str, Socket socket) throws CertificateException {
        checkTrusted(x509CertificateArr, str, socket, true);
    }

    @Override // javax.net.ssl.X509ExtendedTrustManager
    public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str, Socket socket) throws CertificateException {
        checkTrusted(x509CertificateArr, str, socket, false);
    }

    @Override // javax.net.ssl.X509ExtendedTrustManager
    public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str, SSLEngine sSLEngine) throws CertificateException {
        checkTrusted(x509CertificateArr, str, sSLEngine, true);
    }

    @Override // javax.net.ssl.X509ExtendedTrustManager
    public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str, SSLEngine sSLEngine) throws CertificateException {
        checkTrusted(x509CertificateArr, str, sSLEngine, false);
    }

    private Validator checkTrustedInit(X509Certificate[] x509CertificateArr, String str, boolean z2) {
        Validator validator;
        if (x509CertificateArr == null || x509CertificateArr.length == 0) {
            throw new IllegalArgumentException("null or zero-length certificate chain");
        }
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("null or zero-length authentication type");
        }
        if (z2) {
            validator = this.clientValidator;
            if (validator == null) {
                synchronized (this) {
                    validator = this.clientValidator;
                    if (validator == null) {
                        validator = getValidator(Validator.VAR_TLS_CLIENT);
                        this.clientValidator = validator;
                    }
                }
            }
        } else {
            validator = this.serverValidator;
            if (validator == null) {
                synchronized (this) {
                    validator = this.serverValidator;
                    if (validator == null) {
                        validator = getValidator(Validator.VAR_TLS_SERVER);
                        this.serverValidator = validator;
                    }
                }
            }
        }
        return validator;
    }

    private void checkTrusted(X509Certificate[] x509CertificateArr, String str, Socket socket, boolean z2) throws CertificateException {
        X509Certificate[] x509CertificateArrValidate;
        SSLAlgorithmConstraints sSLAlgorithmConstraints;
        Validator validatorCheckTrustedInit = checkTrustedInit(x509CertificateArr, str, z2);
        if (socket != null && socket.isConnected() && (socket instanceof SSLSocket)) {
            SSLSocket sSLSocket = (SSLSocket) socket;
            SSLSession handshakeSession = sSLSocket.getHandshakeSession();
            if (handshakeSession == null) {
                throw new CertificateException("No handshake session");
            }
            boolean z3 = handshakeSession instanceof ExtendedSSLSession;
            if (z3 && ProtocolVersion.useTLS12PlusSpec(handshakeSession.getProtocol())) {
                sSLAlgorithmConstraints = new SSLAlgorithmConstraints(sSLSocket, ((ExtendedSSLSession) handshakeSession).getLocalSupportedSignatureAlgorithms(), false);
            } else {
                sSLAlgorithmConstraints = new SSLAlgorithmConstraints(sSLSocket, false);
            }
            List<byte[]> listEmptyList = Collections.emptyList();
            if (!z2 && z3 && (handshakeSession instanceof SSLSessionImpl)) {
                listEmptyList = ((SSLSessionImpl) handshakeSession).getStatusResponses();
            }
            x509CertificateArrValidate = validate(validatorCheckTrustedInit, x509CertificateArr, listEmptyList, sSLAlgorithmConstraints, z2 ? null : str);
            String endpointIdentificationAlgorithm = sSLSocket.getSSLParameters().getEndpointIdentificationAlgorithm();
            if (endpointIdentificationAlgorithm != null && !endpointIdentificationAlgorithm.isEmpty()) {
                checkIdentity(handshakeSession, x509CertificateArrValidate, endpointIdentificationAlgorithm, z2);
            }
        } else {
            x509CertificateArrValidate = validate(validatorCheckTrustedInit, x509CertificateArr, Collections.emptyList(), null, z2 ? null : str);
        }
        if (SSLLogger.isOn && SSLLogger.isOn("ssl,trustmanager")) {
            SSLLogger.fine("Found trusted certificate", x509CertificateArrValidate[x509CertificateArrValidate.length - 1]);
        }
    }

    private void checkTrusted(X509Certificate[] x509CertificateArr, String str, SSLEngine sSLEngine, boolean z2) throws CertificateException {
        X509Certificate[] x509CertificateArrValidate;
        SSLAlgorithmConstraints sSLAlgorithmConstraints;
        Validator validatorCheckTrustedInit = checkTrustedInit(x509CertificateArr, str, z2);
        if (sSLEngine != null) {
            SSLSession handshakeSession = sSLEngine.getHandshakeSession();
            if (handshakeSession == null) {
                throw new CertificateException("No handshake session");
            }
            boolean z3 = handshakeSession instanceof ExtendedSSLSession;
            if (z3 && ProtocolVersion.useTLS12PlusSpec(handshakeSession.getProtocol())) {
                sSLAlgorithmConstraints = new SSLAlgorithmConstraints(sSLEngine, ((ExtendedSSLSession) handshakeSession).getLocalSupportedSignatureAlgorithms(), false);
            } else {
                sSLAlgorithmConstraints = new SSLAlgorithmConstraints(sSLEngine, false);
            }
            List<byte[]> listEmptyList = Collections.emptyList();
            if (!z2 && z3 && (handshakeSession instanceof SSLSessionImpl)) {
                listEmptyList = ((SSLSessionImpl) handshakeSession).getStatusResponses();
            }
            x509CertificateArrValidate = validate(validatorCheckTrustedInit, x509CertificateArr, listEmptyList, sSLAlgorithmConstraints, z2 ? null : str);
            String endpointIdentificationAlgorithm = sSLEngine.getSSLParameters().getEndpointIdentificationAlgorithm();
            if (endpointIdentificationAlgorithm != null && !endpointIdentificationAlgorithm.isEmpty()) {
                checkIdentity(handshakeSession, x509CertificateArrValidate, endpointIdentificationAlgorithm, z2);
            }
        } else {
            x509CertificateArrValidate = validate(validatorCheckTrustedInit, x509CertificateArr, Collections.emptyList(), null, z2 ? null : str);
        }
        if (SSLLogger.isOn && SSLLogger.isOn("ssl,trustmanager")) {
            SSLLogger.fine("Found trusted certificate", x509CertificateArrValidate[x509CertificateArrValidate.length - 1]);
        }
    }

    private Validator getValidator(String str) {
        Validator validator;
        if (this.pkixParams == null) {
            validator = Validator.getInstance(this.validatorType, str, this.trustedCerts);
        } else {
            validator = Validator.getInstance(this.validatorType, str, this.pkixParams);
        }
        return validator;
    }

    private static X509Certificate[] validate(Validator validator, X509Certificate[] x509CertificateArr, List<byte[]> list, AlgorithmConstraints algorithmConstraints, String str) throws CertificateException {
        Object objBeginFipsProvider = JsseJce.beginFipsProvider();
        try {
            X509Certificate[] x509CertificateArrValidate = validator.validate(x509CertificateArr, null, list, algorithmConstraints, str);
            JsseJce.endFipsProvider(objBeginFipsProvider);
            return x509CertificateArrValidate;
        } catch (Throwable th) {
            JsseJce.endFipsProvider(objBeginFipsProvider);
            throw th;
        }
    }

    private static String getHostNameInSNI(List<SNIServerName> list) {
        SNIHostName sNIHostName = null;
        Iterator<SNIServerName> it = list.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            SNIServerName next = it.next();
            if (next.getType() == 0) {
                if (next instanceof SNIHostName) {
                    sNIHostName = (SNIHostName) next;
                } else {
                    try {
                        sNIHostName = new SNIHostName(next.getEncoded());
                    } catch (IllegalArgumentException e2) {
                        if (SSLLogger.isOn && SSLLogger.isOn("ssl,trustmanager")) {
                            SSLLogger.fine("Illegal server name: " + ((Object) next), new Object[0]);
                        }
                    }
                }
            }
        }
        if (sNIHostName != null) {
            return sNIHostName.getAsciiName();
        }
        return null;
    }

    static List<SNIServerName> getRequestedServerNames(Socket socket) {
        if (socket != null && socket.isConnected() && (socket instanceof SSLSocket)) {
            return getRequestedServerNames(((SSLSocket) socket).getHandshakeSession());
        }
        return Collections.emptyList();
    }

    static List<SNIServerName> getRequestedServerNames(SSLEngine sSLEngine) {
        if (sSLEngine != null) {
            return getRequestedServerNames(sSLEngine.getHandshakeSession());
        }
        return Collections.emptyList();
    }

    private static List<SNIServerName> getRequestedServerNames(SSLSession sSLSession) {
        if (sSLSession != null && (sSLSession instanceof ExtendedSSLSession)) {
            return ((ExtendedSSLSession) sSLSession).getRequestedServerNames();
        }
        return Collections.emptyList();
    }

    static void checkIdentity(SSLSession sSLSession, X509Certificate[] x509CertificateArr, String str, boolean z2) throws CertificateException {
        String hostNameInSNI;
        boolean zContains = AnchorCertificates.contains(x509CertificateArr[x509CertificateArr.length - 1]);
        boolean z3 = false;
        String peerHost = sSLSession.getPeerHost();
        if (peerHost != null && peerHost.endsWith(".")) {
            peerHost = peerHost.substring(0, peerHost.length() - 1);
        }
        if (!z2 && (hostNameInSNI = getHostNameInSNI(getRequestedServerNames(sSLSession))) != null) {
            try {
                checkIdentity(hostNameInSNI, x509CertificateArr[0], str, zContains);
                z3 = true;
            } catch (CertificateException e2) {
                if (hostNameInSNI.equalsIgnoreCase(peerHost)) {
                    throw e2;
                }
            }
        }
        if (!z3) {
            checkIdentity(peerHost, x509CertificateArr[0], str, zContains);
        }
    }

    static void checkIdentity(String str, X509Certificate x509Certificate, String str2) throws CertificateException {
        checkIdentity(str, x509Certificate, str2, false);
    }

    private static void checkIdentity(String str, X509Certificate x509Certificate, String str2, boolean z2) throws CertificateException {
        if (str2 != null && !str2.isEmpty()) {
            if (str != null && str.startsWith("[") && str.endsWith("]")) {
                str = str.substring(1, str.length() - 1);
            }
            if (str2.equalsIgnoreCase("HTTPS")) {
                HostnameChecker.getInstance((byte) 1).match(str, x509Certificate, z2);
            } else {
                if (str2.equalsIgnoreCase("LDAP") || str2.equalsIgnoreCase("LDAPS")) {
                    HostnameChecker.getInstance((byte) 2).match(str, x509Certificate, z2);
                    return;
                }
                throw new CertificateException("Unknown identification algorithm: " + str2);
            }
        }
    }
}
