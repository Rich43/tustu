package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.MessageFormat;
import java.util.Locale;
import sun.security.ssl.SSLHandshake;

/* loaded from: jsse.jar:sun/security/ssl/EncryptedExtensions.class */
final class EncryptedExtensions {
    static final HandshakeProducer handshakeProducer = new EncryptedExtensionsProducer();
    static final SSLConsumer handshakeConsumer = new EncryptedExtensionsConsumer();

    EncryptedExtensions() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/EncryptedExtensions$EncryptedExtensionsMessage.class */
    static final class EncryptedExtensionsMessage extends SSLHandshake.HandshakeMessage {
        private final SSLExtensions extensions;

        EncryptedExtensionsMessage(HandshakeContext handshakeContext) throws IOException {
            super(handshakeContext);
            this.extensions = new SSLExtensions(this);
        }

        EncryptedExtensionsMessage(HandshakeContext handshakeContext, ByteBuffer byteBuffer) throws IOException {
            super(handshakeContext);
            if (byteBuffer.remaining() < 2) {
                throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Invalid EncryptedExtensions handshake message: no sufficient data");
            }
            this.extensions = new SSLExtensions(this, byteBuffer, handshakeContext.sslConfig.getEnabledExtensions(SSLHandshake.ENCRYPTED_EXTENSIONS));
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        SSLHandshake handshakeType() {
            return SSLHandshake.ENCRYPTED_EXTENSIONS;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        int messageLength() {
            int length = this.extensions.length();
            if (length == 0) {
                length = 2;
            }
            return length;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        void send(HandshakeOutStream handshakeOutStream) throws IOException {
            if (this.extensions.length() == 0) {
                handshakeOutStream.putInt16(0);
            } else {
                this.extensions.send(handshakeOutStream);
            }
        }

        public String toString() {
            return new MessageFormat("\"EncryptedExtensions\": [\n{0}\n]", Locale.ENGLISH).format(new Object[]{Utilities.indent(this.extensions.toString())});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/EncryptedExtensions$EncryptedExtensionsProducer.class */
    private static final class EncryptedExtensionsProducer implements HandshakeProducer {
        private EncryptedExtensionsProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            EncryptedExtensionsMessage encryptedExtensionsMessage = new EncryptedExtensionsMessage(serverHandshakeContext);
            encryptedExtensionsMessage.extensions.produce(serverHandshakeContext, serverHandshakeContext.sslConfig.getEnabledExtensions(SSLHandshake.ENCRYPTED_EXTENSIONS, serverHandshakeContext.negotiatedProtocol));
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Produced EncryptedExtensions message", encryptedExtensionsMessage);
            }
            encryptedExtensionsMessage.write(serverHandshakeContext.handshakeOutput);
            serverHandshakeContext.handshakeOutput.flush();
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/EncryptedExtensions$EncryptedExtensionsConsumer.class */
    private static final class EncryptedExtensionsConsumer implements SSLConsumer {
        private EncryptedExtensionsConsumer() {
        }

        @Override // sun.security.ssl.SSLConsumer
        public void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            clientHandshakeContext.handshakeConsumers.remove(Byte.valueOf(SSLHandshake.ENCRYPTED_EXTENSIONS.id));
            EncryptedExtensionsMessage encryptedExtensionsMessage = new EncryptedExtensionsMessage(clientHandshakeContext, byteBuffer);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Consuming EncryptedExtensions handshake message", encryptedExtensionsMessage);
            }
            SSLExtension[] enabledExtensions = clientHandshakeContext.sslConfig.getEnabledExtensions(SSLHandshake.ENCRYPTED_EXTENSIONS);
            encryptedExtensionsMessage.extensions.consumeOnLoad(clientHandshakeContext, enabledExtensions);
            encryptedExtensionsMessage.extensions.consumeOnTrade(clientHandshakeContext, enabledExtensions);
        }
    }
}
