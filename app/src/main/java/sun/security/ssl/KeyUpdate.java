package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.text.MessageFormat;
import java.util.Locale;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import sun.security.ssl.SSLCipher;
import sun.security.ssl.SSLHandshake;

/* loaded from: jsse.jar:sun/security/ssl/KeyUpdate.class */
final class KeyUpdate {
    static final SSLProducer kickstartProducer = new KeyUpdateKickstartProducer();
    static final SSLConsumer handshakeConsumer = new KeyUpdateConsumer();
    static final HandshakeProducer handshakeProducer = new KeyUpdateProducer();

    KeyUpdate() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/KeyUpdate$KeyUpdateMessage.class */
    static final class KeyUpdateMessage extends SSLHandshake.HandshakeMessage {
        private final KeyUpdateRequest status;

        KeyUpdateMessage(PostHandshakeContext postHandshakeContext, KeyUpdateRequest keyUpdateRequest) {
            super(postHandshakeContext);
            this.status = keyUpdateRequest;
        }

        KeyUpdateMessage(PostHandshakeContext postHandshakeContext, ByteBuffer byteBuffer) throws IOException {
            super(postHandshakeContext);
            if (byteBuffer.remaining() != 1) {
                throw postHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "KeyUpdate has an unexpected length of " + byteBuffer.remaining());
            }
            byte b2 = byteBuffer.get();
            this.status = KeyUpdateRequest.valueOf(b2);
            if (this.status == null) {
                throw postHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Invalid KeyUpdate message value: " + KeyUpdateRequest.nameOf(b2));
            }
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public SSLHandshake handshakeType() {
            return SSLHandshake.KEY_UPDATE;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public int messageLength() {
            return 1;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public void send(HandshakeOutStream handshakeOutStream) throws IOException {
            handshakeOutStream.putInt8(this.status.id);
        }

        public String toString() {
            return new MessageFormat("\"KeyUpdate\": '{'\n  \"request_update\": {0}\n'}'", Locale.ENGLISH).format(new Object[]{this.status.name});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/KeyUpdate$KeyUpdateRequest.class */
    enum KeyUpdateRequest {
        NOTREQUESTED((byte) 0, "update_not_requested"),
        REQUESTED((byte) 1, "update_requested");

        final byte id;
        final String name;

        KeyUpdateRequest(byte b2, String str) {
            this.id = b2;
            this.name = str;
        }

        static KeyUpdateRequest valueOf(byte b2) {
            for (KeyUpdateRequest keyUpdateRequest : values()) {
                if (keyUpdateRequest.id == b2) {
                    return keyUpdateRequest;
                }
            }
            return null;
        }

        static String nameOf(byte b2) {
            for (KeyUpdateRequest keyUpdateRequest : values()) {
                if (keyUpdateRequest.id == b2) {
                    return keyUpdateRequest.name;
                }
            }
            return "<UNKNOWN KeyUpdateRequest TYPE: " + (b2 & 255) + ">";
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/KeyUpdate$KeyUpdateKickstartProducer.class */
    private static final class KeyUpdateKickstartProducer implements SSLProducer {
        private KeyUpdateKickstartProducer() {
        }

        @Override // sun.security.ssl.SSLProducer
        public byte[] produce(ConnectionContext connectionContext) throws IOException {
            PostHandshakeContext postHandshakeContext = (PostHandshakeContext) connectionContext;
            return KeyUpdate.handshakeProducer.produce(connectionContext, new KeyUpdateMessage(postHandshakeContext, postHandshakeContext.conContext.isInboundClosed() ? KeyUpdateRequest.NOTREQUESTED : KeyUpdateRequest.REQUESTED));
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/KeyUpdate$KeyUpdateConsumer.class */
    private static final class KeyUpdateConsumer implements SSLConsumer {
        private KeyUpdateConsumer() {
        }

        @Override // sun.security.ssl.SSLConsumer
        public void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException {
            PostHandshakeContext postHandshakeContext = (PostHandshakeContext) connectionContext;
            KeyUpdateMessage keyUpdateMessage = new KeyUpdateMessage(postHandshakeContext, byteBuffer);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Consuming KeyUpdate post-handshake message", keyUpdateMessage);
            }
            SSLTrafficKeyDerivation sSLTrafficKeyDerivationValueOf = SSLTrafficKeyDerivation.valueOf(postHandshakeContext.conContext.protocolVersion);
            if (sSLTrafficKeyDerivationValueOf == null) {
                throw postHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Not supported key derivation: " + ((Object) postHandshakeContext.conContext.protocolVersion));
            }
            SSLKeyDerivation sSLKeyDerivationCreateKeyDerivation = sSLTrafficKeyDerivationValueOf.createKeyDerivation(postHandshakeContext, postHandshakeContext.conContext.inputRecord.readCipher.baseSecret);
            if (sSLKeyDerivationCreateKeyDerivation == null) {
                throw postHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "no key derivation");
            }
            SecretKey secretKeyDeriveKey = sSLKeyDerivationCreateKeyDerivation.deriveKey("TlsUpdateNplus1", null);
            SSLKeyDerivation sSLKeyDerivationCreateKeyDerivation2 = sSLTrafficKeyDerivationValueOf.createKeyDerivation(postHandshakeContext, secretKeyDeriveKey);
            try {
                SSLCipher.SSLReadCipher sSLReadCipherCreateReadCipher = postHandshakeContext.negotiatedCipherSuite.bulkCipher.createReadCipher(Authenticator.valueOf(postHandshakeContext.conContext.protocolVersion), postHandshakeContext.conContext.protocolVersion, sSLKeyDerivationCreateKeyDerivation2.deriveKey("TlsKey", null), new IvParameterSpec(sSLKeyDerivationCreateKeyDerivation2.deriveKey("TlsIv", null).getEncoded()), postHandshakeContext.sslContext.getSecureRandom());
                if (sSLReadCipherCreateReadCipher == null) {
                    throw postHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Illegal cipher suite (" + ((Object) postHandshakeContext.negotiatedCipherSuite) + ") and protocol version (" + ((Object) postHandshakeContext.negotiatedProtocol) + ")");
                }
                sSLReadCipherCreateReadCipher.baseSecret = secretKeyDeriveKey;
                postHandshakeContext.conContext.inputRecord.changeReadCiphers(sSLReadCipherCreateReadCipher);
                if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                    SSLLogger.fine("KeyUpdate: read key updated", new Object[0]);
                }
                if (keyUpdateMessage.status == KeyUpdateRequest.REQUESTED) {
                    KeyUpdate.handshakeProducer.produce(postHandshakeContext, new KeyUpdateMessage(postHandshakeContext, KeyUpdateRequest.NOTREQUESTED));
                } else {
                    postHandshakeContext.conContext.finishPostHandshake();
                }
            } catch (GeneralSecurityException e2) {
                throw postHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Failure to derive read secrets", e2);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/KeyUpdate$KeyUpdateProducer.class */
    private static final class KeyUpdateProducer implements HandshakeProducer {
        private KeyUpdateProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            PostHandshakeContext postHandshakeContext = (PostHandshakeContext) connectionContext;
            KeyUpdateMessage keyUpdateMessage = (KeyUpdateMessage) handshakeMessage;
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Produced KeyUpdate post-handshake message", keyUpdateMessage);
            }
            SSLTrafficKeyDerivation sSLTrafficKeyDerivationValueOf = SSLTrafficKeyDerivation.valueOf(postHandshakeContext.conContext.protocolVersion);
            if (sSLTrafficKeyDerivationValueOf == null) {
                throw postHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Not supported key derivation: " + ((Object) postHandshakeContext.conContext.protocolVersion));
            }
            SSLKeyDerivation sSLKeyDerivationCreateKeyDerivation = sSLTrafficKeyDerivationValueOf.createKeyDerivation(postHandshakeContext, postHandshakeContext.conContext.outputRecord.writeCipher.baseSecret);
            if (sSLKeyDerivationCreateKeyDerivation == null) {
                throw postHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "no key derivation");
            }
            SecretKey secretKeyDeriveKey = sSLKeyDerivationCreateKeyDerivation.deriveKey("TlsUpdateNplus1", null);
            SSLKeyDerivation sSLKeyDerivationCreateKeyDerivation2 = sSLTrafficKeyDerivationValueOf.createKeyDerivation(postHandshakeContext, secretKeyDeriveKey);
            try {
                SSLCipher.SSLWriteCipher sSLWriteCipherCreateWriteCipher = postHandshakeContext.negotiatedCipherSuite.bulkCipher.createWriteCipher(Authenticator.valueOf(postHandshakeContext.conContext.protocolVersion), postHandshakeContext.conContext.protocolVersion, sSLKeyDerivationCreateKeyDerivation2.deriveKey("TlsKey", null), new IvParameterSpec(sSLKeyDerivationCreateKeyDerivation2.deriveKey("TlsIv", null).getEncoded()), postHandshakeContext.sslContext.getSecureRandom());
                if (sSLWriteCipherCreateWriteCipher == null) {
                    throw postHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Illegal cipher suite (" + ((Object) postHandshakeContext.negotiatedCipherSuite) + ") and protocol version (" + ((Object) postHandshakeContext.negotiatedProtocol) + ")");
                }
                sSLWriteCipherCreateWriteCipher.baseSecret = secretKeyDeriveKey;
                postHandshakeContext.conContext.outputRecord.changeWriteCiphers(sSLWriteCipherCreateWriteCipher, keyUpdateMessage.status.id);
                if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                    SSLLogger.fine("KeyUpdate: write key updated", new Object[0]);
                }
                postHandshakeContext.conContext.finishPostHandshake();
                return null;
            } catch (GeneralSecurityException e2) {
                throw postHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Failure to derive write secrets", e2);
            }
        }
    }
}
