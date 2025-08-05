package sun.security.ssl;

import java.security.AlgorithmConstraints;
import java.security.AlgorithmParameters;
import java.security.CryptoPrimitive;
import java.security.Key;
import java.util.Set;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSocket;
import sun.security.util.DisabledAlgorithmConstraints;

/* loaded from: jsse.jar:sun/security/ssl/SSLAlgorithmConstraints.class */
final class SSLAlgorithmConstraints implements AlgorithmConstraints {
    private final AlgorithmConstraints userSpecifiedConstraints;
    private final AlgorithmConstraints peerSpecifiedConstraints;
    private final boolean enabledX509DisabledAlgConstraints;
    private static final AlgorithmConstraints tlsDisabledAlgConstraints = new DisabledAlgorithmConstraints(DisabledAlgorithmConstraints.PROPERTY_TLS_DISABLED_ALGS, new SSLAlgorithmDecomposer());
    private static final AlgorithmConstraints x509DisabledAlgConstraints = new DisabledAlgorithmConstraints(DisabledAlgorithmConstraints.PROPERTY_CERTPATH_DISABLED_ALGS, new SSLAlgorithmDecomposer(true));
    static final AlgorithmConstraints DEFAULT = new SSLAlgorithmConstraints(null);
    static final AlgorithmConstraints DEFAULT_SSL_ONLY = new SSLAlgorithmConstraints((SSLSocket) null, false);

    SSLAlgorithmConstraints(AlgorithmConstraints algorithmConstraints) {
        this.userSpecifiedConstraints = algorithmConstraints;
        this.peerSpecifiedConstraints = null;
        this.enabledX509DisabledAlgConstraints = true;
    }

    SSLAlgorithmConstraints(SSLSocket sSLSocket, boolean z2) {
        this.userSpecifiedConstraints = getUserSpecifiedConstraints(sSLSocket);
        this.peerSpecifiedConstraints = null;
        this.enabledX509DisabledAlgConstraints = z2;
    }

    SSLAlgorithmConstraints(SSLEngine sSLEngine, boolean z2) {
        this.userSpecifiedConstraints = getUserSpecifiedConstraints(sSLEngine);
        this.peerSpecifiedConstraints = null;
        this.enabledX509DisabledAlgConstraints = z2;
    }

    SSLAlgorithmConstraints(SSLSocket sSLSocket, String[] strArr, boolean z2) {
        this.userSpecifiedConstraints = getUserSpecifiedConstraints(sSLSocket);
        this.peerSpecifiedConstraints = new SupportedSignatureAlgorithmConstraints(strArr);
        this.enabledX509DisabledAlgConstraints = z2;
    }

    SSLAlgorithmConstraints(SSLEngine sSLEngine, String[] strArr, boolean z2) {
        this.userSpecifiedConstraints = getUserSpecifiedConstraints(sSLEngine);
        this.peerSpecifiedConstraints = new SupportedSignatureAlgorithmConstraints(strArr);
        this.enabledX509DisabledAlgConstraints = z2;
    }

    private static AlgorithmConstraints getUserSpecifiedConstraints(SSLEngine sSLEngine) {
        HandshakeContext handshakeContext;
        if (sSLEngine != null) {
            if ((sSLEngine instanceof SSLEngineImpl) && (handshakeContext = ((SSLEngineImpl) sSLEngine).conContext.handshakeContext) != null) {
                return handshakeContext.sslConfig.userSpecifiedAlgorithmConstraints;
            }
            return sSLEngine.getSSLParameters().getAlgorithmConstraints();
        }
        return null;
    }

    private static AlgorithmConstraints getUserSpecifiedConstraints(SSLSocket sSLSocket) {
        HandshakeContext handshakeContext;
        if (sSLSocket != null) {
            if ((sSLSocket instanceof SSLSocketImpl) && (handshakeContext = ((SSLSocketImpl) sSLSocket).conContext.handshakeContext) != null) {
                return handshakeContext.sslConfig.userSpecifiedAlgorithmConstraints;
            }
            return sSLSocket.getSSLParameters().getAlgorithmConstraints();
        }
        return null;
    }

    @Override // java.security.AlgorithmConstraints
    public boolean permits(Set<CryptoPrimitive> set, String str, AlgorithmParameters algorithmParameters) {
        boolean zPermits = true;
        if (this.peerSpecifiedConstraints != null) {
            zPermits = this.peerSpecifiedConstraints.permits(set, str, algorithmParameters);
        }
        if (zPermits && this.userSpecifiedConstraints != null) {
            zPermits = this.userSpecifiedConstraints.permits(set, str, algorithmParameters);
        }
        if (zPermits) {
            zPermits = tlsDisabledAlgConstraints.permits(set, str, algorithmParameters);
        }
        if (zPermits && this.enabledX509DisabledAlgConstraints) {
            zPermits = x509DisabledAlgConstraints.permits(set, str, algorithmParameters);
        }
        return zPermits;
    }

    @Override // java.security.AlgorithmConstraints
    public boolean permits(Set<CryptoPrimitive> set, Key key) {
        boolean zPermits = true;
        if (this.peerSpecifiedConstraints != null) {
            zPermits = this.peerSpecifiedConstraints.permits(set, key);
        }
        if (zPermits && this.userSpecifiedConstraints != null) {
            zPermits = this.userSpecifiedConstraints.permits(set, key);
        }
        if (zPermits) {
            zPermits = tlsDisabledAlgConstraints.permits(set, key);
        }
        if (zPermits && this.enabledX509DisabledAlgConstraints) {
            zPermits = x509DisabledAlgConstraints.permits(set, key);
        }
        return zPermits;
    }

    @Override // java.security.AlgorithmConstraints
    public boolean permits(Set<CryptoPrimitive> set, String str, Key key, AlgorithmParameters algorithmParameters) {
        boolean zPermits = true;
        if (this.peerSpecifiedConstraints != null) {
            zPermits = this.peerSpecifiedConstraints.permits(set, str, key, algorithmParameters);
        }
        if (zPermits && this.userSpecifiedConstraints != null) {
            zPermits = this.userSpecifiedConstraints.permits(set, str, key, algorithmParameters);
        }
        if (zPermits) {
            zPermits = tlsDisabledAlgConstraints.permits(set, str, key, algorithmParameters);
        }
        if (zPermits && this.enabledX509DisabledAlgConstraints) {
            zPermits = x509DisabledAlgConstraints.permits(set, str, key, algorithmParameters);
        }
        return zPermits;
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLAlgorithmConstraints$SupportedSignatureAlgorithmConstraints.class */
    private static class SupportedSignatureAlgorithmConstraints implements AlgorithmConstraints {
        private String[] supportedAlgorithms;

        SupportedSignatureAlgorithmConstraints(String[] strArr) {
            if (strArr != null) {
                this.supportedAlgorithms = (String[]) strArr.clone();
            } else {
                this.supportedAlgorithms = null;
            }
        }

        @Override // java.security.AlgorithmConstraints
        public boolean permits(Set<CryptoPrimitive> set, String str, AlgorithmParameters algorithmParameters) {
            if (str == null || str.isEmpty()) {
                throw new IllegalArgumentException("No algorithm name specified");
            }
            if (set == null || set.isEmpty()) {
                throw new IllegalArgumentException("No cryptographic primitive specified");
            }
            if (this.supportedAlgorithms == null || this.supportedAlgorithms.length == 0) {
                return false;
            }
            int iIndexOf = str.indexOf("and");
            if (iIndexOf > 0) {
                str = str.substring(0, iIndexOf);
            }
            for (String str2 : this.supportedAlgorithms) {
                if (str.equalsIgnoreCase(str2)) {
                    return true;
                }
            }
            return false;
        }

        @Override // java.security.AlgorithmConstraints
        public final boolean permits(Set<CryptoPrimitive> set, Key key) {
            return true;
        }

        @Override // java.security.AlgorithmConstraints
        public final boolean permits(Set<CryptoPrimitive> set, String str, Key key, AlgorithmParameters algorithmParameters) {
            if (str == null || str.isEmpty()) {
                throw new IllegalArgumentException("No algorithm name specified");
            }
            return permits(set, str, algorithmParameters);
        }
    }
}
