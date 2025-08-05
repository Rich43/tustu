package sun.security.ssl;

import java.security.Principal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECParameterSpec;
import java.util.AbstractMap;
import java.util.Map;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.X509ExtendedKeyManager;
import sun.security.ssl.SupportedGroupsExtension;

/* loaded from: jsse.jar:sun/security/ssl/X509Authentication.class */
enum X509Authentication implements SSLAuthentication {
    RSA("RSA", new X509PossessionGenerator(new String[]{"RSA"})),
    RSASSA_PSS("RSASSA-PSS", new X509PossessionGenerator(new String[]{"RSASSA-PSS"})),
    RSA_OR_PSS("RSA_OR_PSS", new X509PossessionGenerator(new String[]{"RSA", "RSASSA-PSS"})),
    DSA("DSA", new X509PossessionGenerator(new String[]{"DSA"})),
    EC("EC", new X509PossessionGenerator(new String[]{"EC"}));

    final String keyType;
    final SSLPossessionGenerator possessionGenerator;

    X509Authentication(String str, SSLPossessionGenerator sSLPossessionGenerator) {
        this.keyType = str;
        this.possessionGenerator = sSLPossessionGenerator;
    }

    static X509Authentication valueOf(SignatureScheme signatureScheme) {
        for (X509Authentication x509Authentication : values()) {
            if (x509Authentication.keyType.equals(signatureScheme.keyAlgorithm)) {
                return x509Authentication;
            }
        }
        return null;
    }

    @Override // sun.security.ssl.SSLPossessionGenerator
    public SSLPossession createPossession(HandshakeContext handshakeContext) {
        return this.possessionGenerator.createPossession(handshakeContext);
    }

    @Override // sun.security.ssl.SSLHandshakeBinding
    public SSLHandshake[] getRelatedHandshakers(HandshakeContext handshakeContext) {
        if (!handshakeContext.negotiatedProtocol.useTLS13PlusSpec()) {
            return new SSLHandshake[]{SSLHandshake.CERTIFICATE, SSLHandshake.CERTIFICATE_REQUEST};
        }
        return new SSLHandshake[0];
    }

    @Override // sun.security.ssl.SSLHandshakeBinding
    public Map.Entry<Byte, HandshakeProducer>[] getHandshakeProducers(HandshakeContext handshakeContext) {
        if (!handshakeContext.negotiatedProtocol.useTLS13PlusSpec()) {
            return new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(Byte.valueOf(SSLHandshake.CERTIFICATE.id), SSLHandshake.CERTIFICATE)};
        }
        return new Map.Entry[0];
    }

    /* loaded from: jsse.jar:sun/security/ssl/X509Authentication$X509Possession.class */
    static final class X509Possession implements SSLPossession {
        final X509Certificate[] popCerts;
        final PrivateKey popPrivateKey;

        X509Possession(PrivateKey privateKey, X509Certificate[] x509CertificateArr) {
            this.popCerts = x509CertificateArr;
            this.popPrivateKey = privateKey;
        }

        ECParameterSpec getECParameterSpec() {
            if (this.popPrivateKey == null || !"EC".equals(this.popPrivateKey.getAlgorithm())) {
                return null;
            }
            if (this.popPrivateKey instanceof ECKey) {
                return ((ECKey) this.popPrivateKey).getParams();
            }
            if (this.popCerts != null && this.popCerts.length != 0) {
                PublicKey publicKey = this.popCerts[0].getPublicKey();
                if (publicKey instanceof ECKey) {
                    return ((ECKey) publicKey).getParams();
                }
                return null;
            }
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/X509Authentication$X509Credentials.class */
    static final class X509Credentials implements SSLCredentials {
        final X509Certificate[] popCerts;
        final PublicKey popPublicKey;

        X509Credentials(PublicKey publicKey, X509Certificate[] x509CertificateArr) {
            this.popCerts = x509CertificateArr;
            this.popPublicKey = publicKey;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/X509Authentication$X509PossessionGenerator.class */
    static final class X509PossessionGenerator implements SSLPossessionGenerator {
        final String[] keyTypes;

        private X509PossessionGenerator(String[] strArr) {
            this.keyTypes = strArr;
        }

        @Override // sun.security.ssl.SSLPossessionGenerator
        public SSLPossession createPossession(HandshakeContext handshakeContext) {
            if (handshakeContext.sslConfig.isClientMode) {
                for (String str : this.keyTypes) {
                    SSLPossession sSLPossessionCreateClientPossession = createClientPossession((ClientHandshakeContext) handshakeContext, str);
                    if (sSLPossessionCreateClientPossession != null) {
                        return sSLPossessionCreateClientPossession;
                    }
                }
                return null;
            }
            for (String str2 : this.keyTypes) {
                SSLPossession sSLPossessionCreateServerPossession = createServerPossession((ServerHandshakeContext) handshakeContext, str2);
                if (sSLPossessionCreateServerPossession != null) {
                    return sSLPossessionCreateServerPossession;
                }
            }
            return null;
        }

        private SSLPossession createClientPossession(ClientHandshakeContext clientHandshakeContext, String str) {
            X509ExtendedKeyManager x509KeyManager = clientHandshakeContext.sslContext.getX509KeyManager();
            String strChooseEngineClientAlias = null;
            if (clientHandshakeContext.conContext.transport instanceof SSLSocketImpl) {
                strChooseEngineClientAlias = x509KeyManager.chooseClientAlias(new String[]{str}, clientHandshakeContext.peerSupportedAuthorities == null ? null : (Principal[]) clientHandshakeContext.peerSupportedAuthorities.clone(), (SSLSocket) clientHandshakeContext.conContext.transport);
            } else if (clientHandshakeContext.conContext.transport instanceof SSLEngineImpl) {
                strChooseEngineClientAlias = x509KeyManager.chooseEngineClientAlias(new String[]{str}, clientHandshakeContext.peerSupportedAuthorities == null ? null : (Principal[]) clientHandshakeContext.peerSupportedAuthorities.clone(), (SSLEngine) clientHandshakeContext.conContext.transport);
            }
            if (strChooseEngineClientAlias == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                    SSLLogger.finest("No X.509 cert selected for " + str, new Object[0]);
                    return null;
                }
                return null;
            }
            PrivateKey privateKey = x509KeyManager.getPrivateKey(strChooseEngineClientAlias);
            if (privateKey == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                    SSLLogger.finest(strChooseEngineClientAlias + " is not a private key entry", new Object[0]);
                    return null;
                }
                return null;
            }
            X509Certificate[] certificateChain = x509KeyManager.getCertificateChain(strChooseEngineClientAlias);
            if (certificateChain == null || certificateChain.length == 0) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                    SSLLogger.finest(strChooseEngineClientAlias + " is a private key entry with no cert chain stored", new Object[0]);
                    return null;
                }
                return null;
            }
            PublicKey publicKey = certificateChain[0].getPublicKey();
            if (!privateKey.getAlgorithm().equals(str) || !publicKey.getAlgorithm().equals(str)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                    SSLLogger.fine(strChooseEngineClientAlias + " private or public key is not of " + str + " algorithm", new Object[0]);
                    return null;
                }
                return null;
            }
            return new X509Possession(privateKey, certificateChain);
        }

        private SSLPossession createServerPossession(ServerHandshakeContext serverHandshakeContext, String str) {
            X509ExtendedKeyManager x509KeyManager = serverHandshakeContext.sslContext.getX509KeyManager();
            String strChooseEngineServerAlias = null;
            if (serverHandshakeContext.conContext.transport instanceof SSLSocketImpl) {
                strChooseEngineServerAlias = x509KeyManager.chooseServerAlias(str, serverHandshakeContext.peerSupportedAuthorities == null ? null : (Principal[]) serverHandshakeContext.peerSupportedAuthorities.clone(), (SSLSocket) serverHandshakeContext.conContext.transport);
            } else if (serverHandshakeContext.conContext.transport instanceof SSLEngineImpl) {
                strChooseEngineServerAlias = x509KeyManager.chooseEngineServerAlias(str, serverHandshakeContext.peerSupportedAuthorities == null ? null : (Principal[]) serverHandshakeContext.peerSupportedAuthorities.clone(), (SSLEngine) serverHandshakeContext.conContext.transport);
            }
            if (strChooseEngineServerAlias == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                    SSLLogger.finest("No X.509 cert selected for " + str, new Object[0]);
                    return null;
                }
                return null;
            }
            PrivateKey privateKey = x509KeyManager.getPrivateKey(strChooseEngineServerAlias);
            if (privateKey == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                    SSLLogger.finest(strChooseEngineServerAlias + " is not a private key entry", new Object[0]);
                    return null;
                }
                return null;
            }
            X509Certificate[] certificateChain = x509KeyManager.getCertificateChain(strChooseEngineServerAlias);
            if (certificateChain == null || certificateChain.length == 0) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                    SSLLogger.finest(strChooseEngineServerAlias + " is not a certificate entry", new Object[0]);
                    return null;
                }
                return null;
            }
            PublicKey publicKey = certificateChain[0].getPublicKey();
            if (!privateKey.getAlgorithm().equals(str) || !publicKey.getAlgorithm().equals(str)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                    SSLLogger.fine(strChooseEngineServerAlias + " private or public key is not of " + str + " algorithm", new Object[0]);
                    return null;
                }
                return null;
            }
            if (!serverHandshakeContext.negotiatedProtocol.useTLS13PlusSpec() && str.equals("EC")) {
                if (!(publicKey instanceof ECPublicKey)) {
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                        SSLLogger.warning(strChooseEngineServerAlias + " public key is not an instance of ECPublicKey", new Object[0]);
                        return null;
                    }
                    return null;
                }
                SupportedGroupsExtension.NamedGroup namedGroupValueOf = SupportedGroupsExtension.NamedGroup.valueOf(((ECPublicKey) publicKey).getParams());
                if (namedGroupValueOf == null || !SupportedGroupsExtension.SupportedGroups.isSupported(namedGroupValueOf) || (serverHandshakeContext.clientRequestedNamedGroups != null && !serverHandshakeContext.clientRequestedNamedGroups.contains(namedGroupValueOf))) {
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                        SSLLogger.warning("Unsupported named group (" + ((Object) namedGroupValueOf) + ") used in the " + strChooseEngineServerAlias + " certificate", new Object[0]);
                        return null;
                    }
                    return null;
                }
            }
            return new X509Possession(privateKey, certificateChain);
        }
    }
}
