package sun.security.ssl;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import sun.security.internal.spec.TlsMasterSecretParameterSpec;
import sun.security.ssl.CipherSuite;

/* loaded from: jsse.jar:sun/security/ssl/SSLMasterKeyDerivation.class */
enum SSLMasterKeyDerivation implements SSLKeyDerivationGenerator {
    SSL30("kdf_ssl30"),
    TLS10("kdf_tls10"),
    TLS12("kdf_tls12");

    final String name;

    SSLMasterKeyDerivation(String str) {
        this.name = str;
    }

    static SSLMasterKeyDerivation valueOf(ProtocolVersion protocolVersion) {
        switch (protocolVersion) {
            case SSL30:
                return SSL30;
            case TLS10:
            case TLS11:
                return TLS10;
            case TLS12:
                return TLS12;
            default:
                return null;
        }
    }

    @Override // sun.security.ssl.SSLKeyDerivationGenerator
    public SSLKeyDerivation createKeyDerivation(HandshakeContext handshakeContext, SecretKey secretKey) throws IOException {
        return new LegacyMasterKeyDerivation(handshakeContext, secretKey);
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLMasterKeyDerivation$LegacyMasterKeyDerivation.class */
    private static final class LegacyMasterKeyDerivation implements SSLKeyDerivation {
        final HandshakeContext context;
        final SecretKey preMasterSecret;

        LegacyMasterKeyDerivation(HandshakeContext handshakeContext, SecretKey secretKey) {
            this.context = handshakeContext;
            this.preMasterSecret = secretKey;
        }

        @Override // sun.security.ssl.SSLKeyDerivation
        public SecretKey deriveKey(String str, AlgorithmParameterSpec algorithmParameterSpec) throws IOException {
            String str2;
            CipherSuite.HashAlg hashAlg;
            TlsMasterSecretParameterSpec tlsMasterSecretParameterSpec;
            CipherSuite cipherSuite = this.context.negotiatedCipherSuite;
            ProtocolVersion protocolVersion = this.context.negotiatedProtocol;
            byte b2 = protocolVersion.major;
            byte b3 = protocolVersion.minor;
            if (protocolVersion.id >= ProtocolVersion.TLS12.id) {
                str2 = "SunTls12MasterSecret";
                hashAlg = cipherSuite.hashAlg;
            } else {
                str2 = "SunTlsMasterSecret";
                hashAlg = CipherSuite.HashAlg.H_NONE;
            }
            if (this.context.handshakeSession.useExtendedMasterSecret) {
                str2 = "SunTlsExtendedMasterSecret";
                this.context.handshakeHash.utilize();
                tlsMasterSecretParameterSpec = new TlsMasterSecretParameterSpec(this.preMasterSecret, b2 & 255, b3 & 255, this.context.handshakeHash.digest(), hashAlg.name, hashAlg.hashLength, hashAlg.blockSize);
            } else {
                tlsMasterSecretParameterSpec = new TlsMasterSecretParameterSpec(this.preMasterSecret, b2 & 255, b3 & 255, this.context.clientHelloRandom.randomBytes, this.context.serverHelloRandom.randomBytes, hashAlg.name, hashAlg.hashLength, hashAlg.blockSize);
            }
            try {
                KeyGenerator keyGenerator = JsseJce.getKeyGenerator(str2);
                keyGenerator.init(tlsMasterSecretParameterSpec);
                return keyGenerator.generateKey();
            } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException e2) {
                if (SSLLogger.isOn && SSLLogger.isOn("handshake")) {
                    SSLLogger.fine("RSA master secret generation error.", e2);
                }
                throw new ProviderException(e2);
            }
        }
    }
}
