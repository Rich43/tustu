package sun.security.ssl;

import java.io.IOException;
import java.security.AlgorithmConstraints;
import java.security.CryptoPrimitive;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.ECPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPublicKeySpec;
import java.util.EnumSet;
import java.util.Iterator;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLHandshakeException;
import sun.security.ssl.CipherSuite;
import sun.security.ssl.SupportedGroupsExtension;
import sun.security.ssl.X509Authentication;
import sun.security.util.ECUtil;

/* loaded from: jsse.jar:sun/security/ssl/ECDHKeyExchange.class */
final class ECDHKeyExchange {
    static final SSLPossessionGenerator poGenerator = new ECDHEPossessionGenerator();
    static final SSLKeyAgreementGenerator ecdheKAGenerator = new ECDHEKAGenerator();
    static final SSLKeyAgreementGenerator ecdhKAGenerator = new ECDHKAGenerator();

    ECDHKeyExchange() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/ECDHKeyExchange$ECDHECredentials.class */
    static final class ECDHECredentials implements SSLCredentials {
        final ECPublicKey popPublicKey;
        final SupportedGroupsExtension.NamedGroup namedGroup;

        ECDHECredentials(ECPublicKey eCPublicKey, SupportedGroupsExtension.NamedGroup namedGroup) {
            this.popPublicKey = eCPublicKey;
            this.namedGroup = namedGroup;
        }

        static ECDHECredentials valueOf(SupportedGroupsExtension.NamedGroup namedGroup, byte[] bArr) throws GeneralSecurityException, IOException {
            ECParameterSpec eCParameterSpec;
            if (namedGroup.type != SupportedGroupsExtension.NamedGroupType.NAMED_GROUP_ECDHE) {
                throw new RuntimeException("Credentials decoding:  Not ECDHE named group");
            }
            if (bArr == null || bArr.length == 0 || (eCParameterSpec = JsseJce.getECParameterSpec(namedGroup.oid)) == null) {
                return null;
            }
            return new ECDHECredentials((ECPublicKey) JsseJce.getKeyFactory("EC").generatePublic(new ECPublicKeySpec(JsseJce.decodePoint(bArr, eCParameterSpec.getCurve()), eCParameterSpec)), namedGroup);
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ECDHKeyExchange$ECDHEPossession.class */
    static final class ECDHEPossession implements SSLPossession {
        final PrivateKey privateKey;
        final ECPublicKey publicKey;
        final SupportedGroupsExtension.NamedGroup namedGroup;

        ECDHEPossession(SupportedGroupsExtension.NamedGroup namedGroup, SecureRandom secureRandom) {
            try {
                KeyPairGenerator keyPairGenerator = JsseJce.getKeyPairGenerator("EC");
                keyPairGenerator.initialize((ECGenParameterSpec) namedGroup.getParameterSpec(), secureRandom);
                KeyPair keyPairGenerateKeyPair = keyPairGenerator.generateKeyPair();
                this.privateKey = keyPairGenerateKeyPair.getPrivate();
                this.publicKey = (ECPublicKey) keyPairGenerateKeyPair.getPublic();
                this.namedGroup = namedGroup;
            } catch (GeneralSecurityException e2) {
                throw new RuntimeException("Could not generate ECDH keypair", e2);
            }
        }

        ECDHEPossession(ECDHECredentials eCDHECredentials, SecureRandom secureRandom) {
            ECParameterSpec params = eCDHECredentials.popPublicKey.getParams();
            try {
                KeyPairGenerator keyPairGenerator = JsseJce.getKeyPairGenerator("EC");
                keyPairGenerator.initialize(params, secureRandom);
                KeyPair keyPairGenerateKeyPair = keyPairGenerator.generateKeyPair();
                this.privateKey = keyPairGenerateKeyPair.getPrivate();
                this.publicKey = (ECPublicKey) keyPairGenerateKeyPair.getPublic();
                this.namedGroup = eCDHECredentials.namedGroup;
            } catch (GeneralSecurityException e2) {
                throw new RuntimeException("Could not generate ECDH keypair", e2);
            }
        }

        @Override // sun.security.ssl.SSLPossession
        public byte[] encode() {
            return ECUtil.encodePoint(this.publicKey.getW(), this.publicKey.getParams().getCurve());
        }

        SecretKey getAgreedSecret(PublicKey publicKey) throws IllegalStateException, SSLHandshakeException {
            try {
                KeyAgreement keyAgreement = JsseJce.getKeyAgreement("ECDH");
                keyAgreement.init(this.privateKey);
                keyAgreement.doPhase(publicKey, true);
                return keyAgreement.generateSecret("TlsPremasterSecret");
            } catch (GeneralSecurityException e2) {
                throw ((SSLHandshakeException) new SSLHandshakeException("Could not generate secret").initCause(e2));
            }
        }

        SecretKey getAgreedSecret(byte[] bArr) throws SSLHandshakeException {
            try {
                ECParameterSpec params = this.publicKey.getParams();
                return getAgreedSecret(JsseJce.getKeyFactory("EC").generatePublic(new ECPublicKeySpec(JsseJce.decodePoint(bArr, params.getCurve()), params)));
            } catch (IOException | GeneralSecurityException e2) {
                throw ((SSLHandshakeException) new SSLHandshakeException("Could not generate secret").initCause(e2));
            }
        }

        void checkConstraints(AlgorithmConstraints algorithmConstraints, byte[] bArr) throws SSLHandshakeException {
            try {
                ECParameterSpec params = this.publicKey.getParams();
                if (!algorithmConstraints.permits(EnumSet.of(CryptoPrimitive.KEY_AGREEMENT), (ECPublicKey) JsseJce.getKeyFactory("EC").generatePublic(new ECPublicKeySpec(JsseJce.decodePoint(bArr, params.getCurve()), params)))) {
                    throw new SSLHandshakeException("ECPublicKey does not comply to algorithm constraints");
                }
            } catch (IOException | GeneralSecurityException e2) {
                throw ((SSLHandshakeException) new SSLHandshakeException("Could not generate ECPublicKey").initCause(e2));
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ECDHKeyExchange$ECDHEPossessionGenerator.class */
    private static final class ECDHEPossessionGenerator implements SSLPossessionGenerator {
        private ECDHEPossessionGenerator() {
        }

        @Override // sun.security.ssl.SSLPossessionGenerator
        public SSLPossession createPossession(HandshakeContext handshakeContext) {
            SupportedGroupsExtension.NamedGroup preferredGroup;
            if (handshakeContext.clientRequestedNamedGroups != null && !handshakeContext.clientRequestedNamedGroups.isEmpty()) {
                preferredGroup = SupportedGroupsExtension.SupportedGroups.getPreferredGroup(handshakeContext.negotiatedProtocol, handshakeContext.algorithmConstraints, SupportedGroupsExtension.NamedGroupType.NAMED_GROUP_ECDHE, handshakeContext.clientRequestedNamedGroups);
            } else {
                preferredGroup = SupportedGroupsExtension.SupportedGroups.getPreferredGroup(handshakeContext.negotiatedProtocol, handshakeContext.algorithmConstraints, SupportedGroupsExtension.NamedGroupType.NAMED_GROUP_ECDHE);
            }
            if (preferredGroup != null) {
                return new ECDHEPossession(preferredGroup, handshakeContext.sslContext.getSecureRandom());
            }
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ECDHKeyExchange$ECDHKAGenerator.class */
    private static final class ECDHKAGenerator implements SSLKeyAgreementGenerator {
        private ECDHKAGenerator() {
        }

        @Override // sun.security.ssl.SSLKeyAgreementGenerator
        public SSLKeyDerivation createKeyDerivation(HandshakeContext handshakeContext) throws IOException {
            if (handshakeContext instanceof ServerHandshakeContext) {
                return createServerKeyDerivation((ServerHandshakeContext) handshakeContext);
            }
            return createClientKeyDerivation((ClientHandshakeContext) handshakeContext);
        }

        private SSLKeyDerivation createServerKeyDerivation(ServerHandshakeContext serverHandshakeContext) throws IOException {
            ECParameterSpec eCParameterSpec;
            X509Authentication.X509Possession x509Possession = null;
            ECDHECredentials eCDHECredentials = null;
            Iterator<SSLPossession> it = serverHandshakeContext.handshakePossessions.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                SSLPossession next = it.next();
                if ((next instanceof X509Authentication.X509Possession) && (eCParameterSpec = ((X509Authentication.X509Possession) next).getECParameterSpec()) != null) {
                    SupportedGroupsExtension.NamedGroup namedGroupValueOf = SupportedGroupsExtension.NamedGroup.valueOf(eCParameterSpec);
                    if (namedGroupValueOf == null) {
                        throw serverHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Unsupported EC server cert for ECDH key exchange");
                    }
                    Iterator<SSLCredentials> it2 = serverHandshakeContext.handshakeCredentials.iterator();
                    while (true) {
                        if (!it2.hasNext()) {
                            break;
                        }
                        SSLCredentials next2 = it2.next();
                        if ((next2 instanceof ECDHECredentials) && namedGroupValueOf.equals(((ECDHECredentials) next2).namedGroup)) {
                            eCDHECredentials = (ECDHECredentials) next2;
                            break;
                        }
                    }
                    if (eCDHECredentials != null) {
                        x509Possession = (X509Authentication.X509Possession) next;
                        break;
                    }
                }
            }
            if (x509Possession == null || eCDHECredentials == null) {
                throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "No sufficient ECDHE key agreement parameters negotiated");
            }
            return new ECDHEKAKeyDerivation(serverHandshakeContext, x509Possession.popPrivateKey, eCDHECredentials.popPublicKey);
        }

        private SSLKeyDerivation createClientKeyDerivation(ClientHandshakeContext clientHandshakeContext) throws IOException {
            ECDHEPossession eCDHEPossession = null;
            X509Authentication.X509Credentials x509Credentials = null;
            Iterator<SSLPossession> it = clientHandshakeContext.handshakePossessions.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                SSLPossession next = it.next();
                if (next instanceof ECDHEPossession) {
                    SupportedGroupsExtension.NamedGroup namedGroup = ((ECDHEPossession) next).namedGroup;
                    Iterator<SSLCredentials> it2 = clientHandshakeContext.handshakeCredentials.iterator();
                    while (true) {
                        if (!it2.hasNext()) {
                            break;
                        }
                        SSLCredentials next2 = it2.next();
                        if (next2 instanceof X509Authentication.X509Credentials) {
                            PublicKey publicKey = ((X509Authentication.X509Credentials) next2).popPublicKey;
                            if (publicKey.getAlgorithm().equals("EC")) {
                                SupportedGroupsExtension.NamedGroup namedGroupValueOf = SupportedGroupsExtension.NamedGroup.valueOf(((ECPublicKey) publicKey).getParams());
                                if (namedGroupValueOf == null) {
                                    throw clientHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Unsupported EC server cert for ECDH key exchange");
                                }
                                if (namedGroup.equals(namedGroupValueOf)) {
                                    x509Credentials = (X509Authentication.X509Credentials) next2;
                                    break;
                                }
                            } else {
                                continue;
                            }
                        }
                    }
                    if (x509Credentials != null) {
                        eCDHEPossession = (ECDHEPossession) next;
                        break;
                    }
                }
            }
            if (eCDHEPossession == null || x509Credentials == null) {
                throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "No sufficient ECDH key agreement parameters negotiated");
            }
            return new ECDHEKAKeyDerivation(clientHandshakeContext, eCDHEPossession.privateKey, x509Credentials.popPublicKey);
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ECDHKeyExchange$ECDHEKAGenerator.class */
    private static final class ECDHEKAGenerator implements SSLKeyAgreementGenerator {
        private ECDHEKAGenerator() {
        }

        @Override // sun.security.ssl.SSLKeyAgreementGenerator
        public SSLKeyDerivation createKeyDerivation(HandshakeContext handshakeContext) throws IOException {
            ECDHEPossession eCDHEPossession = null;
            ECDHECredentials eCDHECredentials = null;
            Iterator<SSLPossession> it = handshakeContext.handshakePossessions.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                SSLPossession next = it.next();
                if (next instanceof ECDHEPossession) {
                    SupportedGroupsExtension.NamedGroup namedGroup = ((ECDHEPossession) next).namedGroup;
                    Iterator<SSLCredentials> it2 = handshakeContext.handshakeCredentials.iterator();
                    while (true) {
                        if (!it2.hasNext()) {
                            break;
                        }
                        SSLCredentials next2 = it2.next();
                        if ((next2 instanceof ECDHECredentials) && namedGroup.equals(((ECDHECredentials) next2).namedGroup)) {
                            eCDHECredentials = (ECDHECredentials) next2;
                            break;
                        }
                    }
                    if (eCDHECredentials != null) {
                        eCDHEPossession = (ECDHEPossession) next;
                        break;
                    }
                }
            }
            if (eCDHEPossession == null || eCDHECredentials == null) {
                throw handshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "No sufficient ECDHE key agreement parameters negotiated");
            }
            return new ECDHEKAKeyDerivation(handshakeContext, eCDHEPossession.privateKey, eCDHECredentials.popPublicKey);
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ECDHKeyExchange$ECDHEKAKeyDerivation.class */
    private static final class ECDHEKAKeyDerivation implements SSLKeyDerivation {
        private final HandshakeContext context;
        private final PrivateKey localPrivateKey;
        private final PublicKey peerPublicKey;

        ECDHEKAKeyDerivation(HandshakeContext handshakeContext, PrivateKey privateKey, PublicKey publicKey) {
            this.context = handshakeContext;
            this.localPrivateKey = privateKey;
            this.peerPublicKey = publicKey;
        }

        @Override // sun.security.ssl.SSLKeyDerivation
        public SecretKey deriveKey(String str, AlgorithmParameterSpec algorithmParameterSpec) throws IOException {
            if (!this.context.negotiatedProtocol.useTLS13PlusSpec()) {
                return t12DeriveKey(str, algorithmParameterSpec);
            }
            return t13DeriveKey(str, algorithmParameterSpec);
        }

        private SecretKey t12DeriveKey(String str, AlgorithmParameterSpec algorithmParameterSpec) throws IllegalStateException, IOException {
            try {
                KeyAgreement keyAgreement = JsseJce.getKeyAgreement("ECDH");
                keyAgreement.init(this.localPrivateKey);
                keyAgreement.doPhase(this.peerPublicKey, true);
                SecretKey secretKeyGenerateSecret = keyAgreement.generateSecret("TlsPremasterSecret");
                SSLMasterKeyDerivation sSLMasterKeyDerivationValueOf = SSLMasterKeyDerivation.valueOf(this.context.negotiatedProtocol);
                if (sSLMasterKeyDerivationValueOf == null) {
                    throw new SSLHandshakeException("No expected master key derivation for protocol: " + this.context.negotiatedProtocol.name);
                }
                return sSLMasterKeyDerivationValueOf.createKeyDerivation(this.context, secretKeyGenerateSecret).deriveKey("MasterSecret", algorithmParameterSpec);
            } catch (GeneralSecurityException e2) {
                throw ((SSLHandshakeException) new SSLHandshakeException("Could not generate secret").initCause(e2));
            }
        }

        private SecretKey t13DeriveKey(String str, AlgorithmParameterSpec algorithmParameterSpec) throws IllegalStateException, IOException {
            try {
                KeyAgreement keyAgreement = JsseJce.getKeyAgreement("ECDH");
                keyAgreement.init(this.localPrivateKey);
                keyAgreement.doPhase(this.peerPublicKey, true);
                SecretKey secretKeyGenerateSecret = keyAgreement.generateSecret("TlsPremasterSecret");
                CipherSuite.HashAlg hashAlg = this.context.negotiatedCipherSuite.hashAlg;
                SSLKeyDerivation sSLSecretDerivation = this.context.handshakeKeyDerivation;
                HKDF hkdf = new HKDF(hashAlg.name);
                if (sSLSecretDerivation == null) {
                    byte[] bArr = new byte[hashAlg.hashLength];
                    sSLSecretDerivation = new SSLSecretDerivation(this.context, hkdf.extract(bArr, new SecretKeySpec(bArr, "TlsPreSharedSecret"), "TlsEarlySecret"));
                }
                return hkdf.extract(sSLSecretDerivation.deriveKey("TlsSaltSecret", null), secretKeyGenerateSecret, str);
            } catch (GeneralSecurityException e2) {
                throw ((SSLHandshakeException) new SSLHandshakeException("Could not generate secret").initCause(e2));
            }
        }
    }
}
