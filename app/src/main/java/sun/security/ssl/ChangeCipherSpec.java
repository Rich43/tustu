package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.net.ssl.SSLException;
import sun.security.ssl.SSLCipher;
import sun.security.ssl.SSLHandshake;
import sun.security.ssl.SSLTrafficKeyDerivation;

/* loaded from: jsse.jar:sun/security/ssl/ChangeCipherSpec.class */
final class ChangeCipherSpec {
    static final SSLConsumer t10Consumer = new T10ChangeCipherSpecConsumer();
    static final HandshakeProducer t10Producer = new T10ChangeCipherSpecProducer();
    static final SSLConsumer t13Consumer = new T13ChangeCipherSpecConsumer();

    ChangeCipherSpec() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/ChangeCipherSpec$T10ChangeCipherSpecProducer.class */
    private static final class T10ChangeCipherSpecProducer implements HandshakeProducer {
        private T10ChangeCipherSpecProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            Authenticator authenticatorValueOf;
            HandshakeContext handshakeContext = (HandshakeContext) connectionContext;
            SSLKeyDerivation sSLKeyDerivation = handshakeContext.handshakeKeyDerivation;
            if (!(sSLKeyDerivation instanceof SSLTrafficKeyDerivation.LegacyTrafficKeyDerivation)) {
                throw new UnsupportedOperationException("Not supported.");
            }
            SSLTrafficKeyDerivation.LegacyTrafficKeyDerivation legacyTrafficKeyDerivation = (SSLTrafficKeyDerivation.LegacyTrafficKeyDerivation) sSLKeyDerivation;
            CipherSuite cipherSuite = handshakeContext.negotiatedCipherSuite;
            if (cipherSuite.bulkCipher.cipherType == CipherType.AEAD_CIPHER) {
                authenticatorValueOf = Authenticator.valueOf(handshakeContext.negotiatedProtocol);
            } else {
                try {
                    authenticatorValueOf = Authenticator.valueOf(handshakeContext.negotiatedProtocol, cipherSuite.macAlg, legacyTrafficKeyDerivation.getTrafficKey(handshakeContext.sslConfig.isClientMode ? "clientMacKey" : "serverMacKey"));
                } catch (InvalidKeyException | NoSuchAlgorithmException e2) {
                    throw new SSLException("Algorithm missing:  ", e2);
                }
            }
            SecretKey trafficKey = legacyTrafficKeyDerivation.getTrafficKey(handshakeContext.sslConfig.isClientMode ? "clientWriteKey" : "serverWriteKey");
            SecretKey trafficKey2 = legacyTrafficKeyDerivation.getTrafficKey(handshakeContext.sslConfig.isClientMode ? "clientWriteIv" : "serverWriteIv");
            try {
                SSLCipher.SSLWriteCipher sSLWriteCipherCreateWriteCipher = cipherSuite.bulkCipher.createWriteCipher(authenticatorValueOf, handshakeContext.negotiatedProtocol, trafficKey, trafficKey2 == null ? null : new IvParameterSpec(trafficKey2.getEncoded()), handshakeContext.sslContext.getSecureRandom());
                if (sSLWriteCipherCreateWriteCipher == null) {
                    throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Illegal cipher suite (" + ((Object) cipherSuite) + ") and protocol version (" + ((Object) handshakeContext.negotiatedProtocol) + ")");
                }
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Produced ChangeCipherSpec message", new Object[0]);
                }
                handshakeContext.conContext.outputRecord.changeWriteCiphers(sSLWriteCipherCreateWriteCipher, true);
                return null;
            } catch (GeneralSecurityException e3) {
                throw new SSLException("Algorithm missing:  ", e3);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ChangeCipherSpec$T10ChangeCipherSpecConsumer.class */
    private static final class T10ChangeCipherSpecConsumer implements SSLConsumer {
        private T10ChangeCipherSpecConsumer() {
        }

        @Override // sun.security.ssl.SSLConsumer
        public void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException {
            Authenticator authenticatorValueOf;
            TransportContext transportContext = (TransportContext) connectionContext;
            transportContext.consumers.remove(Byte.valueOf(ContentType.CHANGE_CIPHER_SPEC.id));
            if (byteBuffer.remaining() != 1 || byteBuffer.get() != 1) {
                throw transportContext.fatal(Alert.UNEXPECTED_MESSAGE, "Malformed or unexpected ChangeCipherSpec message");
            }
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Consuming ChangeCipherSpec message", new Object[0]);
            }
            if (transportContext.handshakeContext == null) {
                throw transportContext.fatal(Alert.HANDSHAKE_FAILURE, "Unexpected ChangeCipherSpec message");
            }
            HandshakeContext handshakeContext = transportContext.handshakeContext;
            if (handshakeContext.handshakeKeyDerivation == null) {
                throw transportContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unexpected ChangeCipherSpec message");
            }
            SSLKeyDerivation sSLKeyDerivation = handshakeContext.handshakeKeyDerivation;
            if (sSLKeyDerivation instanceof SSLTrafficKeyDerivation.LegacyTrafficKeyDerivation) {
                SSLTrafficKeyDerivation.LegacyTrafficKeyDerivation legacyTrafficKeyDerivation = (SSLTrafficKeyDerivation.LegacyTrafficKeyDerivation) sSLKeyDerivation;
                CipherSuite cipherSuite = handshakeContext.negotiatedCipherSuite;
                if (cipherSuite.bulkCipher.cipherType == CipherType.AEAD_CIPHER) {
                    authenticatorValueOf = Authenticator.valueOf(handshakeContext.negotiatedProtocol);
                } else {
                    try {
                        authenticatorValueOf = Authenticator.valueOf(handshakeContext.negotiatedProtocol, cipherSuite.macAlg, legacyTrafficKeyDerivation.getTrafficKey(handshakeContext.sslConfig.isClientMode ? "serverMacKey" : "clientMacKey"));
                    } catch (InvalidKeyException | NoSuchAlgorithmException e2) {
                        throw new SSLException("Algorithm missing:  ", e2);
                    }
                }
                SecretKey trafficKey = legacyTrafficKeyDerivation.getTrafficKey(handshakeContext.sslConfig.isClientMode ? "serverWriteKey" : "clientWriteKey");
                SecretKey trafficKey2 = legacyTrafficKeyDerivation.getTrafficKey(handshakeContext.sslConfig.isClientMode ? "serverWriteIv" : "clientWriteIv");
                try {
                    SSLCipher.SSLReadCipher sSLReadCipherCreateReadCipher = cipherSuite.bulkCipher.createReadCipher(authenticatorValueOf, handshakeContext.negotiatedProtocol, trafficKey, trafficKey2 == null ? null : new IvParameterSpec(trafficKey2.getEncoded()), handshakeContext.sslContext.getSecureRandom());
                    if (sSLReadCipherCreateReadCipher == null) {
                        throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Illegal cipher suite (" + ((Object) handshakeContext.negotiatedCipherSuite) + ") and protocol version (" + ((Object) handshakeContext.negotiatedProtocol) + ")");
                    }
                    transportContext.inputRecord.changeReadCiphers(sSLReadCipherCreateReadCipher);
                    return;
                } catch (GeneralSecurityException e3) {
                    throw new SSLException("Algorithm missing:  ", e3);
                }
            }
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ChangeCipherSpec$T13ChangeCipherSpecConsumer.class */
    private static final class T13ChangeCipherSpecConsumer implements SSLConsumer {
        private T13ChangeCipherSpecConsumer() {
        }

        @Override // sun.security.ssl.SSLConsumer
        public void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException {
            TransportContext transportContext = (TransportContext) connectionContext;
            transportContext.consumers.remove(Byte.valueOf(ContentType.CHANGE_CIPHER_SPEC.id));
            if (byteBuffer.remaining() != 1 || byteBuffer.get() != 1) {
                throw transportContext.fatal(Alert.UNEXPECTED_MESSAGE, "Malformed or unexpected ChangeCipherSpec message");
            }
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Consuming ChangeCipherSpec message", new Object[0]);
            }
        }
    }
}
