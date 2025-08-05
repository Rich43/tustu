package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.ProviderException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLHandshakeException;
import sun.security.internal.spec.TlsKeyMaterialParameterSpec;
import sun.security.internal.spec.TlsKeyMaterialSpec;
import sun.security.ssl.CipherSuite;

/* loaded from: jsse.jar:sun/security/ssl/SSLTrafficKeyDerivation.class */
enum SSLTrafficKeyDerivation implements SSLKeyDerivationGenerator {
    SSL30("kdf_ssl30", new S30TrafficKeyDerivationGenerator()),
    TLS10("kdf_tls10", new T10TrafficKeyDerivationGenerator()),
    TLS12("kdf_tls12", new T12TrafficKeyDerivationGenerator()),
    TLS13("kdf_tls13", new T13TrafficKeyDerivationGenerator());

    final String name;
    final SSLKeyDerivationGenerator keyDerivationGenerator;

    SSLTrafficKeyDerivation(String str, SSLKeyDerivationGenerator sSLKeyDerivationGenerator) {
        this.name = str;
        this.keyDerivationGenerator = sSLKeyDerivationGenerator;
    }

    static SSLTrafficKeyDerivation valueOf(ProtocolVersion protocolVersion) {
        switch (protocolVersion) {
            case SSL30:
                return SSL30;
            case TLS10:
            case TLS11:
                return TLS10;
            case TLS12:
                return TLS12;
            case TLS13:
                return TLS13;
            default:
                return null;
        }
    }

    @Override // sun.security.ssl.SSLKeyDerivationGenerator
    public SSLKeyDerivation createKeyDerivation(HandshakeContext handshakeContext, SecretKey secretKey) throws IOException {
        return this.keyDerivationGenerator.createKeyDerivation(handshakeContext, secretKey);
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLTrafficKeyDerivation$S30TrafficKeyDerivationGenerator.class */
    private static final class S30TrafficKeyDerivationGenerator implements SSLKeyDerivationGenerator {
        private S30TrafficKeyDerivationGenerator() {
        }

        @Override // sun.security.ssl.SSLKeyDerivationGenerator
        public SSLKeyDerivation createKeyDerivation(HandshakeContext handshakeContext, SecretKey secretKey) throws IOException {
            return new LegacyTrafficKeyDerivation(handshakeContext, secretKey);
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLTrafficKeyDerivation$T10TrafficKeyDerivationGenerator.class */
    private static final class T10TrafficKeyDerivationGenerator implements SSLKeyDerivationGenerator {
        private T10TrafficKeyDerivationGenerator() {
        }

        @Override // sun.security.ssl.SSLKeyDerivationGenerator
        public SSLKeyDerivation createKeyDerivation(HandshakeContext handshakeContext, SecretKey secretKey) throws IOException {
            return new LegacyTrafficKeyDerivation(handshakeContext, secretKey);
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLTrafficKeyDerivation$T12TrafficKeyDerivationGenerator.class */
    private static final class T12TrafficKeyDerivationGenerator implements SSLKeyDerivationGenerator {
        private T12TrafficKeyDerivationGenerator() {
        }

        @Override // sun.security.ssl.SSLKeyDerivationGenerator
        public SSLKeyDerivation createKeyDerivation(HandshakeContext handshakeContext, SecretKey secretKey) throws IOException {
            return new LegacyTrafficKeyDerivation(handshakeContext, secretKey);
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLTrafficKeyDerivation$T13TrafficKeyDerivationGenerator.class */
    private static final class T13TrafficKeyDerivationGenerator implements SSLKeyDerivationGenerator {
        private T13TrafficKeyDerivationGenerator() {
        }

        @Override // sun.security.ssl.SSLKeyDerivationGenerator
        public SSLKeyDerivation createKeyDerivation(HandshakeContext handshakeContext, SecretKey secretKey) throws IOException {
            return new T13TrafficKeyDerivation(handshakeContext, secretKey);
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLTrafficKeyDerivation$T13TrafficKeyDerivation.class */
    static final class T13TrafficKeyDerivation implements SSLKeyDerivation {
        private final CipherSuite cs;
        private final SecretKey secret;

        T13TrafficKeyDerivation(HandshakeContext handshakeContext, SecretKey secretKey) {
            this.secret = secretKey;
            this.cs = handshakeContext.negotiatedCipherSuite;
        }

        @Override // sun.security.ssl.SSLKeyDerivation
        public SecretKey deriveKey(String str, AlgorithmParameterSpec algorithmParameterSpec) throws IOException {
            KeySchedule keyScheduleValueOf = KeySchedule.valueOf(str);
            try {
                return new HKDF(this.cs.hashAlg.name).expand(this.secret, createHkdfInfo(keyScheduleValueOf.label, keyScheduleValueOf.getKeyLength(this.cs)), keyScheduleValueOf.getKeyLength(this.cs), keyScheduleValueOf.getAlgorithm(this.cs, str));
            } catch (GeneralSecurityException e2) {
                throw ((SSLHandshakeException) new SSLHandshakeException("Could not generate secret").initCause(e2));
            }
        }

        private static byte[] createHkdfInfo(byte[] bArr, int i2) throws IOException {
            byte[] bArr2 = new byte[4 + bArr.length];
            ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr2);
            try {
                Record.putInt16(byteBufferWrap, i2);
                Record.putBytes8(byteBufferWrap, bArr);
                Record.putInt8(byteBufferWrap, 0);
                return bArr2;
            } catch (IOException e2) {
                throw new RuntimeException("Unexpected exception", e2);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLTrafficKeyDerivation$KeySchedule.class */
    private enum KeySchedule {
        TlsKey("key", false),
        TlsIv("iv", true),
        TlsUpdateNplus1("traffic upd", false);

        private final byte[] label;
        private final boolean isIv;

        KeySchedule(String str, boolean z2) {
            this.label = ("tls13 " + str).getBytes();
            this.isIv = z2;
        }

        int getKeyLength(CipherSuite cipherSuite) {
            if (this == TlsUpdateNplus1) {
                return cipherSuite.hashAlg.hashLength;
            }
            return this.isIv ? cipherSuite.bulkCipher.ivSize : cipherSuite.bulkCipher.keySize;
        }

        String getAlgorithm(CipherSuite cipherSuite, String str) {
            return this.isIv ? str : cipherSuite.bulkCipher.algorithm;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLTrafficKeyDerivation$LegacyTrafficKeyDerivation.class */
    static final class LegacyTrafficKeyDerivation implements SSLKeyDerivation {
        private final HandshakeContext context;
        private final SecretKey masterSecret;
        private final TlsKeyMaterialSpec keyMaterialSpec;

        LegacyTrafficKeyDerivation(HandshakeContext handshakeContext, SecretKey secretKey) {
            String str;
            CipherSuite.HashAlg hashAlg;
            this.context = handshakeContext;
            this.masterSecret = secretKey;
            CipherSuite cipherSuite = handshakeContext.negotiatedCipherSuite;
            ProtocolVersion protocolVersion = handshakeContext.negotiatedProtocol;
            int i2 = cipherSuite.macAlg.size;
            boolean z2 = cipherSuite.exportable;
            SSLCipher sSLCipher = cipherSuite.bulkCipher;
            int i3 = z2 ? sSLCipher.expandedKeySize : 0;
            byte b2 = protocolVersion.major;
            byte b3 = protocolVersion.minor;
            if (protocolVersion.id >= ProtocolVersion.TLS12.id) {
                str = "SunTls12KeyMaterial";
                hashAlg = cipherSuite.hashAlg;
            } else {
                str = "SunTlsKeyMaterial";
                hashAlg = CipherSuite.HashAlg.H_NONE;
            }
            int i4 = sSLCipher.ivSize;
            if (sSLCipher.cipherType == CipherType.AEAD_CIPHER) {
                i4 = sSLCipher.fixedIvSize;
            } else if (sSLCipher.cipherType == CipherType.BLOCK_CIPHER && protocolVersion.useTLS11PlusSpec()) {
                i4 = 0;
            }
            TlsKeyMaterialParameterSpec tlsKeyMaterialParameterSpec = new TlsKeyMaterialParameterSpec(secretKey, b2 & 255, b3 & 255, handshakeContext.clientHelloRandom.randomBytes, handshakeContext.serverHelloRandom.randomBytes, sSLCipher.algorithm, sSLCipher.keySize, i3, i4, i2, hashAlg.name, hashAlg.hashLength, hashAlg.blockSize);
            try {
                KeyGenerator keyGenerator = JsseJce.getKeyGenerator(str);
                keyGenerator.init(tlsKeyMaterialParameterSpec);
                this.keyMaterialSpec = (TlsKeyMaterialSpec) keyGenerator.generateKey();
            } catch (GeneralSecurityException e2) {
                throw new ProviderException(e2);
            }
        }

        SecretKey getTrafficKey(String str) {
            switch (str) {
                case "clientMacKey":
                    return this.keyMaterialSpec.getClientMacKey();
                case "serverMacKey":
                    return this.keyMaterialSpec.getServerMacKey();
                case "clientWriteKey":
                    return this.keyMaterialSpec.getClientCipherKey();
                case "serverWriteKey":
                    return this.keyMaterialSpec.getServerCipherKey();
                case "clientWriteIv":
                    IvParameterSpec clientIv = this.keyMaterialSpec.getClientIv();
                    if (clientIv == null) {
                        return null;
                    }
                    return new SecretKeySpec(clientIv.getIV(), "TlsIv");
                case "serverWriteIv":
                    IvParameterSpec serverIv = this.keyMaterialSpec.getServerIv();
                    if (serverIv == null) {
                        return null;
                    }
                    return new SecretKeySpec(serverIv.getIV(), "TlsIv");
                default:
                    return null;
            }
        }

        @Override // sun.security.ssl.SSLKeyDerivation
        public SecretKey deriveKey(String str, AlgorithmParameterSpec algorithmParameterSpec) throws IOException {
            return getTrafficKey(str);
        }
    }
}
