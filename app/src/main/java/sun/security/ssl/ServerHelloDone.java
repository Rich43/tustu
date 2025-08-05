package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import sun.security.ssl.SSLHandshake;

/* loaded from: jsse.jar:sun/security/ssl/ServerHelloDone.class */
final class ServerHelloDone {
    static final SSLConsumer handshakeConsumer = new ServerHelloDoneConsumer();
    static final HandshakeProducer handshakeProducer = new ServerHelloDoneProducer();

    ServerHelloDone() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/ServerHelloDone$ServerHelloDoneMessage.class */
    static final class ServerHelloDoneMessage extends SSLHandshake.HandshakeMessage {
        ServerHelloDoneMessage(HandshakeContext handshakeContext) {
            super(handshakeContext);
        }

        ServerHelloDoneMessage(HandshakeContext handshakeContext, ByteBuffer byteBuffer) throws IOException {
            super(handshakeContext);
            if (byteBuffer.hasRemaining()) {
                throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Error parsing ServerHelloDone message: not empty");
            }
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public SSLHandshake handshakeType() {
            return SSLHandshake.SERVER_HELLO_DONE;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public int messageLength() {
            return 0;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public void send(HandshakeOutStream handshakeOutStream) throws IOException {
        }

        public String toString() {
            return "<empty>";
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ServerHelloDone$ServerHelloDoneProducer.class */
    private static final class ServerHelloDoneProducer implements HandshakeProducer {
        private ServerHelloDoneProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            ServerHelloDoneMessage serverHelloDoneMessage = new ServerHelloDoneMessage(serverHandshakeContext);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Produced ServerHelloDone handshake message", serverHelloDoneMessage);
            }
            serverHelloDoneMessage.write(serverHandshakeContext.handshakeOutput);
            serverHandshakeContext.handshakeOutput.flush();
            serverHandshakeContext.handshakeConsumers.put(Byte.valueOf(SSLHandshake.CLIENT_KEY_EXCHANGE.id), SSLHandshake.CLIENT_KEY_EXCHANGE);
            serverHandshakeContext.conContext.consumers.put(Byte.valueOf(ContentType.CHANGE_CIPHER_SPEC.id), ChangeCipherSpec.t10Consumer);
            serverHandshakeContext.handshakeConsumers.put(Byte.valueOf(SSLHandshake.FINISHED.id), SSLHandshake.FINISHED);
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ServerHelloDone$ServerHelloDoneConsumer.class */
    private static final class ServerHelloDoneConsumer implements SSLConsumer {
        private ServerHelloDoneConsumer() {
        }

        @Override // sun.security.ssl.SSLConsumer
        public void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (clientHandshakeContext.handshakeConsumers.remove(Byte.valueOf(SSLHandshake.CERTIFICATE_STATUS.id)) != null) {
                CertificateStatus.handshakeAbsence.absent(connectionContext, null);
            }
            clientHandshakeContext.handshakeConsumers.clear();
            ServerHelloDoneMessage serverHelloDoneMessage = new ServerHelloDoneMessage(clientHandshakeContext, byteBuffer);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Consuming ServerHelloDone handshake message", serverHelloDoneMessage);
            }
            clientHandshakeContext.handshakeProducers.put(Byte.valueOf(SSLHandshake.CLIENT_KEY_EXCHANGE.id), SSLHandshake.CLIENT_KEY_EXCHANGE);
            clientHandshakeContext.handshakeProducers.put(Byte.valueOf(SSLHandshake.FINISHED.id), SSLHandshake.FINISHED);
            for (SSLHandshake sSLHandshake : new SSLHandshake[]{SSLHandshake.CERTIFICATE, SSLHandshake.CLIENT_KEY_EXCHANGE, SSLHandshake.CERTIFICATE_VERIFY, SSLHandshake.FINISHED}) {
                HandshakeProducer handshakeProducerRemove = clientHandshakeContext.handshakeProducers.remove(Byte.valueOf(sSLHandshake.id));
                if (handshakeProducerRemove != null) {
                    handshakeProducerRemove.produce(connectionContext, null);
                }
            }
        }
    }
}
