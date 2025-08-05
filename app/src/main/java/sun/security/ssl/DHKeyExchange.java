package sun.security.ssl;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.util.Iterator;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPublicKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLHandshakeException;
import sun.security.action.GetPropertyAction;
import sun.security.ssl.CipherSuite;
import sun.security.ssl.SupportedGroupsExtension;
import sun.security.ssl.X509Authentication;
import sun.security.util.KeyUtil;

/* loaded from: jsse.jar:sun/security/ssl/DHKeyExchange.class */
final class DHKeyExchange {
    static final SSLPossessionGenerator poGenerator = new DHEPossessionGenerator(false);
    static final SSLPossessionGenerator poExportableGenerator = new DHEPossessionGenerator(true);
    static final SSLKeyAgreementGenerator kaGenerator = new DHEKAGenerator();

    DHKeyExchange() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/DHKeyExchange$DHECredentials.class */
    static final class DHECredentials implements SSLCredentials {
        final DHPublicKey popPublicKey;
        final SupportedGroupsExtension.NamedGroup namedGroup;

        DHECredentials(DHPublicKey dHPublicKey, SupportedGroupsExtension.NamedGroup namedGroup) {
            this.popPublicKey = dHPublicKey;
            this.namedGroup = namedGroup;
        }

        static DHECredentials valueOf(SupportedGroupsExtension.NamedGroup namedGroup, byte[] bArr) throws GeneralSecurityException, IOException {
            DHParameterSpec dHParameterSpec;
            if (namedGroup.type != SupportedGroupsExtension.NamedGroupType.NAMED_GROUP_FFDHE) {
                throw new RuntimeException("Credentials decoding:  Not FFDHE named group");
            }
            if (bArr == null || bArr.length == 0 || (dHParameterSpec = (DHParameterSpec) namedGroup.getParameterSpec()) == null) {
                return null;
            }
            return new DHECredentials((DHPublicKey) JsseJce.getKeyFactory("DiffieHellman").generatePublic(new DHPublicKeySpec(new BigInteger(1, bArr), dHParameterSpec.getP(), dHParameterSpec.getG())), namedGroup);
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/DHKeyExchange$DHEPossession.class */
    static final class DHEPossession implements SSLPossession {
        final PrivateKey privateKey;
        final DHPublicKey publicKey;
        final SupportedGroupsExtension.NamedGroup namedGroup;

        DHEPossession(SupportedGroupsExtension.NamedGroup namedGroup, SecureRandom secureRandom) {
            try {
                KeyPairGenerator keyPairGenerator = JsseJce.getKeyPairGenerator("DiffieHellman");
                keyPairGenerator.initialize((DHParameterSpec) namedGroup.getParameterSpec(), secureRandom);
                KeyPair keyPairGenerateDHKeyPair = generateDHKeyPair(keyPairGenerator);
                if (keyPairGenerateDHKeyPair == null) {
                    throw new RuntimeException("Could not generate DH keypair");
                }
                this.privateKey = keyPairGenerateDHKeyPair.getPrivate();
                this.publicKey = (DHPublicKey) keyPairGenerateDHKeyPair.getPublic();
                this.namedGroup = namedGroup;
            } catch (GeneralSecurityException e2) {
                throw new RuntimeException("Could not generate DH keypair", e2);
            }
        }

        DHEPossession(int i2, SecureRandom secureRandom) {
            DHParameterSpec dHParameterSpec = PredefinedDHParameterSpecs.definedParams.get(Integer.valueOf(i2));
            try {
                KeyPairGenerator keyPairGenerator = JsseJce.getKeyPairGenerator("DiffieHellman");
                if (dHParameterSpec != null) {
                    keyPairGenerator.initialize(dHParameterSpec, secureRandom);
                } else {
                    keyPairGenerator.initialize(i2, secureRandom);
                }
                KeyPair keyPairGenerateDHKeyPair = generateDHKeyPair(keyPairGenerator);
                if (keyPairGenerateDHKeyPair == null) {
                    throw new RuntimeException("Could not generate DH keypair of " + i2 + " bits");
                }
                this.privateKey = keyPairGenerateDHKeyPair.getPrivate();
                this.publicKey = (DHPublicKey) keyPairGenerateDHKeyPair.getPublic();
                this.namedGroup = SupportedGroupsExtension.NamedGroup.valueOf(this.publicKey.getParams());
            } catch (GeneralSecurityException e2) {
                throw new RuntimeException("Could not generate DH keypair", e2);
            }
        }

        DHEPossession(DHECredentials dHECredentials, SecureRandom secureRandom) {
            try {
                KeyPairGenerator keyPairGenerator = JsseJce.getKeyPairGenerator("DiffieHellman");
                keyPairGenerator.initialize(dHECredentials.popPublicKey.getParams(), secureRandom);
                KeyPair keyPairGenerateDHKeyPair = generateDHKeyPair(keyPairGenerator);
                if (keyPairGenerateDHKeyPair == null) {
                    throw new RuntimeException("Could not generate DH keypair");
                }
                this.privateKey = keyPairGenerateDHKeyPair.getPrivate();
                this.publicKey = (DHPublicKey) keyPairGenerateDHKeyPair.getPublic();
                this.namedGroup = dHECredentials.namedGroup;
            } catch (GeneralSecurityException e2) {
                throw new RuntimeException("Could not generate DH keypair", e2);
            }
        }

        private KeyPair generateDHKeyPair(KeyPairGenerator keyPairGenerator) throws GeneralSecurityException {
            boolean z2 = !KeyUtil.isOracleJCEProvider(keyPairGenerator.getProvider().getName());
            boolean z3 = false;
            for (int i2 = 0; i2 <= 2; i2++) {
                KeyPair keyPairGenerateKeyPair = keyPairGenerator.generateKeyPair();
                if (z2) {
                    try {
                        KeyUtil.validate(getDHPublicKeySpec(keyPairGenerateKeyPair.getPublic()));
                    } catch (InvalidKeyException e2) {
                        if (z3) {
                            throw e2;
                        }
                        z3 = true;
                    }
                }
                return keyPairGenerateKeyPair;
            }
            return null;
        }

        private static DHPublicKeySpec getDHPublicKeySpec(PublicKey publicKey) {
            if (publicKey instanceof DHPublicKey) {
                DHPublicKey dHPublicKey = (DHPublicKey) publicKey;
                DHParameterSpec params = dHPublicKey.getParams();
                return new DHPublicKeySpec(dHPublicKey.getY(), params.getP(), params.getG());
            }
            try {
                return (DHPublicKeySpec) JsseJce.getKeyFactory("DiffieHellman").getKeySpec(publicKey, DHPublicKeySpec.class);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e2) {
                throw new RuntimeException("Unable to get DHPublicKeySpec", e2);
            }
        }

        @Override // sun.security.ssl.SSLPossession
        public byte[] encode() {
            byte[] byteArray = Utilities.toByteArray(this.publicKey.getY());
            int keySize = (KeyUtil.getKeySize(this.publicKey) + 7) >>> 3;
            if (keySize > 0 && byteArray.length < keySize) {
                byte[] bArr = new byte[keySize];
                System.arraycopy(byteArray, 0, bArr, keySize - byteArray.length, byteArray.length);
                byteArray = bArr;
            }
            return byteArray;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/DHKeyExchange$DHEPossessionGenerator.class */
    private static final class DHEPossessionGenerator implements SSLPossessionGenerator {
        private static final boolean useSmartEphemeralDHKeys;
        private static final boolean useLegacyEphemeralDHKeys;
        private static final int customizedDHKeySize;
        private final boolean exportable;

        static {
            String strPrivilegedGetProperty = GetPropertyAction.privilegedGetProperty("jdk.tls.ephemeralDHKeySize");
            if (strPrivilegedGetProperty == null || strPrivilegedGetProperty.isEmpty()) {
                useLegacyEphemeralDHKeys = false;
                useSmartEphemeralDHKeys = false;
                customizedDHKeySize = -1;
                return;
            }
            if ("matched".equals(strPrivilegedGetProperty)) {
                useLegacyEphemeralDHKeys = false;
                useSmartEphemeralDHKeys = true;
                customizedDHKeySize = -1;
            } else {
                if ("legacy".equals(strPrivilegedGetProperty)) {
                    useLegacyEphemeralDHKeys = true;
                    useSmartEphemeralDHKeys = false;
                    customizedDHKeySize = -1;
                    return;
                }
                useLegacyEphemeralDHKeys = false;
                useSmartEphemeralDHKeys = false;
                try {
                    customizedDHKeySize = Integer.parseUnsignedInt(strPrivilegedGetProperty);
                    if (customizedDHKeySize < 1024 || customizedDHKeySize > 8192 || (customizedDHKeySize & 63) != 0) {
                        throw new IllegalArgumentException("Unsupported customized DH key size: " + customizedDHKeySize + ". The key size must be multiple of 64, and range from 1024 to 8192 (inclusive)");
                    }
                } catch (NumberFormatException e2) {
                    throw new IllegalArgumentException("Invalid system property jdk.tls.ephemeralDHKeySize");
                }
            }
        }

        private DHEPossessionGenerator(boolean z2) {
            this.exportable = z2;
        }

        @Override // sun.security.ssl.SSLPossessionGenerator
        public SSLPossession createPossession(HandshakeContext handshakeContext) {
            SupportedGroupsExtension.NamedGroup preferredGroup;
            if (!useLegacyEphemeralDHKeys && handshakeContext.clientRequestedNamedGroups != null && !handshakeContext.clientRequestedNamedGroups.isEmpty() && (preferredGroup = SupportedGroupsExtension.SupportedGroups.getPreferredGroup(handshakeContext.negotiatedProtocol, handshakeContext.algorithmConstraints, SupportedGroupsExtension.NamedGroupType.NAMED_GROUP_FFDHE, handshakeContext.clientRequestedNamedGroups)) != null) {
                return new DHEPossession(preferredGroup, handshakeContext.sslContext.getSecureRandom());
            }
            int i2 = this.exportable ? 512 : 1024;
            if (!this.exportable) {
                if (useLegacyEphemeralDHKeys) {
                    i2 = 768;
                } else if (useSmartEphemeralDHKeys) {
                    PrivateKey privateKey = null;
                    ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) handshakeContext;
                    if (serverHandshakeContext.interimAuthn instanceof X509Authentication.X509Possession) {
                        privateKey = ((X509Authentication.X509Possession) serverHandshakeContext.interimAuthn).popPrivateKey;
                    }
                    if (privateKey != null) {
                        i2 = KeyUtil.getKeySize(privateKey) <= 1024 ? 1024 : 2048;
                    }
                } else if (customizedDHKeySize > 0) {
                    i2 = customizedDHKeySize;
                }
            }
            return new DHEPossession(i2, handshakeContext.sslContext.getSecureRandom());
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/DHKeyExchange$DHEKAGenerator.class */
    private static final class DHEKAGenerator implements SSLKeyAgreementGenerator {
        private static DHEKAGenerator instance = new DHEKAGenerator();

        private DHEKAGenerator() {
        }

        @Override // sun.security.ssl.SSLKeyAgreementGenerator
        public SSLKeyDerivation createKeyDerivation(HandshakeContext handshakeContext) throws IOException {
            DHEPossession dHEPossession = null;
            DHECredentials dHECredentials = null;
            Iterator<SSLPossession> it = handshakeContext.handshakePossessions.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                SSLPossession next = it.next();
                if (next instanceof DHEPossession) {
                    DHEPossession dHEPossession2 = (DHEPossession) next;
                    Iterator<SSLCredentials> it2 = handshakeContext.handshakeCredentials.iterator();
                    while (true) {
                        if (!it2.hasNext()) {
                            break;
                        }
                        SSLCredentials next2 = it2.next();
                        if (next2 instanceof DHECredentials) {
                            DHECredentials dHECredentials2 = (DHECredentials) next2;
                            if (dHEPossession2.namedGroup != null && dHECredentials2.namedGroup != null) {
                                if (dHEPossession2.namedGroup.equals(dHECredentials2.namedGroup)) {
                                    dHECredentials = (DHECredentials) next2;
                                    break;
                                }
                            } else {
                                DHParameterSpec params = dHEPossession2.publicKey.getParams();
                                DHParameterSpec params2 = dHECredentials2.popPublicKey.getParams();
                                if (params.getP().equals(params2.getP()) && params.getG().equals(params2.getG())) {
                                    dHECredentials = (DHECredentials) next2;
                                    break;
                                }
                            }
                        }
                    }
                    if (dHECredentials != null) {
                        dHEPossession = (DHEPossession) next;
                        break;
                    }
                }
            }
            if (dHEPossession == null || dHECredentials == null) {
                throw handshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "No sufficient DHE key agreement parameters negotiated");
            }
            return new DHEKAKeyDerivation(handshakeContext, dHEPossession.privateKey, dHECredentials.popPublicKey);
        }

        /* loaded from: jsse.jar:sun/security/ssl/DHKeyExchange$DHEKAGenerator$DHEKAKeyDerivation.class */
        private static final class DHEKAKeyDerivation implements SSLKeyDerivation {
            private final HandshakeContext context;
            private final PrivateKey localPrivateKey;
            private final PublicKey peerPublicKey;

            DHEKAKeyDerivation(HandshakeContext handshakeContext, PrivateKey privateKey, PublicKey publicKey) {
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
                    KeyAgreement keyAgreement = JsseJce.getKeyAgreement("DiffieHellman");
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
                    KeyAgreement keyAgreement = JsseJce.getKeyAgreement("DiffieHellman");
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
}
