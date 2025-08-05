package sun.security.ssl;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Iterator;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.net.ssl.SSLHandshakeException;
import sun.security.internal.spec.TlsRsaPremasterSecretParameterSpec;
import sun.security.util.KeyUtil;

/* loaded from: jsse.jar:sun/security/ssl/RSAKeyExchange.class */
final class RSAKeyExchange {
    static final SSLPossessionGenerator poGenerator = new EphemeralRSAPossessionGenerator();
    static final SSLKeyAgreementGenerator kaGenerator = new RSAKAGenerator();

    RSAKeyExchange() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/RSAKeyExchange$EphemeralRSAPossession.class */
    static final class EphemeralRSAPossession implements SSLPossession {
        final RSAPublicKey popPublicKey;
        final PrivateKey popPrivateKey;

        EphemeralRSAPossession(PrivateKey privateKey, RSAPublicKey rSAPublicKey) {
            this.popPublicKey = rSAPublicKey;
            this.popPrivateKey = privateKey;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/RSAKeyExchange$EphemeralRSACredentials.class */
    static final class EphemeralRSACredentials implements SSLCredentials {
        final RSAPublicKey popPublicKey;

        EphemeralRSACredentials(RSAPublicKey rSAPublicKey) {
            this.popPublicKey = rSAPublicKey;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/RSAKeyExchange$EphemeralRSAPossessionGenerator.class */
    private static final class EphemeralRSAPossessionGenerator implements SSLPossessionGenerator {
        private EphemeralRSAPossessionGenerator() {
        }

        @Override // sun.security.ssl.SSLPossessionGenerator
        public SSLPossession createPossession(HandshakeContext handshakeContext) {
            try {
                KeyPair rSAKeyPair = handshakeContext.sslContext.getEphemeralKeyManager().getRSAKeyPair(true, handshakeContext.sslContext.getSecureRandom());
                if (rSAKeyPair != null) {
                    return new EphemeralRSAPossession(rSAKeyPair.getPrivate(), (RSAPublicKey) rSAKeyPair.getPublic());
                }
                return null;
            } catch (RuntimeException e2) {
                return null;
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/RSAKeyExchange$RSAPremasterSecret.class */
    static final class RSAPremasterSecret implements SSLPossession, SSLCredentials {
        final SecretKey premasterSecret;

        RSAPremasterSecret(SecretKey secretKey) {
            this.premasterSecret = secretKey;
        }

        byte[] getEncoded(PublicKey publicKey, SecureRandom secureRandom) throws GeneralSecurityException {
            Cipher cipher = JsseJce.getCipher("RSA/ECB/PKCS1Padding");
            cipher.init(3, publicKey, secureRandom);
            return cipher.wrap(this.premasterSecret);
        }

        static RSAPremasterSecret createPremasterSecret(ClientHandshakeContext clientHandshakeContext) throws GeneralSecurityException {
            KeyGenerator keyGenerator = JsseJce.getKeyGenerator(clientHandshakeContext.negotiatedProtocol.useTLS12PlusSpec() ? "SunTls12RsaPremasterSecret" : "SunTlsRsaPremasterSecret");
            keyGenerator.init(new TlsRsaPremasterSecretParameterSpec(clientHandshakeContext.clientHelloVersion, clientHandshakeContext.negotiatedProtocol.id), clientHandshakeContext.sslContext.getSecureRandom());
            return new RSAPremasterSecret(keyGenerator.generateKey());
        }

        static RSAPremasterSecret decode(ServerHandshakeContext serverHandshakeContext, PrivateKey privateKey, byte[] bArr) throws GeneralSecurityException {
            boolean z2;
            SecretKey secretKeyGeneratePremasterSecret;
            byte[] bArrDoFinal = null;
            Cipher cipher = JsseJce.getCipher("RSA/ECB/PKCS1Padding");
            try {
                cipher.init(4, privateKey, new TlsRsaPremasterSecretParameterSpec(serverHandshakeContext.clientHelloVersion, serverHandshakeContext.negotiatedProtocol.id), serverHandshakeContext.sslContext.getSecureRandom());
                z2 = !KeyUtil.isOracleJCEProvider(cipher.getProvider().getName());
            } catch (UnsupportedOperationException | InvalidKeyException e2) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.warning("The Cipher provider " + safeProviderName(cipher) + " caused exception: " + e2.getMessage(), new Object[0]);
                }
                z2 = true;
            }
            if (z2) {
                Cipher cipher2 = JsseJce.getCipher("RSA/ECB/PKCS1Padding");
                cipher2.init(2, privateKey);
                boolean z3 = false;
                try {
                    bArrDoFinal = cipher2.doFinal(bArr);
                } catch (BadPaddingException e3) {
                    z3 = true;
                }
                secretKeyGeneratePremasterSecret = generatePremasterSecret(serverHandshakeContext.clientHelloVersion, serverHandshakeContext.negotiatedProtocol.id, KeyUtil.checkTlsPreMasterSecretKey(serverHandshakeContext.clientHelloVersion, serverHandshakeContext.negotiatedProtocol.id, serverHandshakeContext.sslContext.getSecureRandom(), bArrDoFinal, z3), serverHandshakeContext.sslContext.getSecureRandom());
            } else {
                secretKeyGeneratePremasterSecret = (SecretKey) cipher.unwrap(bArr, "TlsRsaPremasterSecret", 3);
            }
            return new RSAPremasterSecret(secretKeyGeneratePremasterSecret);
        }

        private static String safeProviderName(Cipher cipher) {
            try {
                return cipher.getProvider().toString();
            } catch (Exception e2) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Retrieving The Cipher provider name caused exception ", e2);
                }
                try {
                    return cipher.toString() + " (provider name not available)";
                } catch (Exception e3) {
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                        SSLLogger.fine("Retrieving The Cipher name caused exception ", e3);
                        return "(cipher/provider names not available)";
                    }
                    return "(cipher/provider names not available)";
                }
            }
        }

        private static SecretKey generatePremasterSecret(int i2, int i3, byte[] bArr, SecureRandom secureRandom) throws GeneralSecurityException {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Generating a premaster secret", new Object[0]);
            }
            try {
                KeyGenerator keyGenerator = JsseJce.getKeyGenerator(i2 >= ProtocolVersion.TLS12.id ? "SunTls12RsaPremasterSecret" : "SunTlsRsaPremasterSecret");
                keyGenerator.init(new TlsRsaPremasterSecretParameterSpec(i2, i3, bArr), secureRandom);
                return keyGenerator.generateKey();
            } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException e2) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("RSA premaster secret generation error:", new Object[0]);
                    e2.printStackTrace(System.out);
                }
                throw new GeneralSecurityException("Could not generate premaster secret", e2);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/RSAKeyExchange$RSAKAGenerator.class */
    private static final class RSAKAGenerator implements SSLKeyAgreementGenerator {
        private RSAKAGenerator() {
        }

        @Override // sun.security.ssl.SSLKeyAgreementGenerator
        public SSLKeyDerivation createKeyDerivation(HandshakeContext handshakeContext) throws IOException {
            RSAPremasterSecret rSAPremasterSecret = null;
            if (handshakeContext instanceof ClientHandshakeContext) {
                Iterator<SSLPossession> it = handshakeContext.handshakePossessions.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    SSLPossession next = it.next();
                    if (next instanceof RSAPremasterSecret) {
                        rSAPremasterSecret = (RSAPremasterSecret) next;
                        break;
                    }
                }
            } else {
                Iterator<SSLCredentials> it2 = handshakeContext.handshakeCredentials.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    SSLCredentials next2 = it2.next();
                    if (next2 instanceof RSAPremasterSecret) {
                        rSAPremasterSecret = (RSAPremasterSecret) next2;
                        break;
                    }
                }
            }
            if (rSAPremasterSecret == null) {
                throw handshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "No sufficient RSA key agreement parameters negotiated");
            }
            return new RSAKAKeyDerivation(handshakeContext, rSAPremasterSecret.premasterSecret);
        }

        /* loaded from: jsse.jar:sun/security/ssl/RSAKeyExchange$RSAKAGenerator$RSAKAKeyDerivation.class */
        private static final class RSAKAKeyDerivation implements SSLKeyDerivation {
            private final HandshakeContext context;
            private final SecretKey preMasterSecret;

            RSAKAKeyDerivation(HandshakeContext handshakeContext, SecretKey secretKey) {
                this.context = handshakeContext;
                this.preMasterSecret = secretKey;
            }

            @Override // sun.security.ssl.SSLKeyDerivation
            public SecretKey deriveKey(String str, AlgorithmParameterSpec algorithmParameterSpec) throws IOException {
                SSLMasterKeyDerivation sSLMasterKeyDerivationValueOf = SSLMasterKeyDerivation.valueOf(this.context.negotiatedProtocol);
                if (sSLMasterKeyDerivationValueOf == null) {
                    throw new SSLHandshakeException("No expected master key derivation for protocol: " + this.context.negotiatedProtocol.name);
                }
                return sSLMasterKeyDerivationValueOf.createKeyDerivation(this.context, this.preMasterSecret).deriveKey("MasterSecret", algorithmParameterSpec);
            }
        }
    }
}
