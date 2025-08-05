package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import sun.security.ssl.SSLHandshake;

/* loaded from: jsse.jar:sun/security/ssl/ServerKeyExchange.class */
final class ServerKeyExchange {
    static final SSLConsumer handshakeConsumer = new ServerKeyExchangeConsumer();
    static final HandshakeProducer handshakeProducer = new ServerKeyExchangeProducer();

    ServerKeyExchange() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/ServerKeyExchange$ServerKeyExchangeProducer.class */
    private static final class ServerKeyExchangeProducer implements HandshakeProducer {
        private ServerKeyExchangeProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            SSLKeyExchange sSLKeyExchangeValueOf = SSLKeyExchange.valueOf(serverHandshakeContext.negotiatedCipherSuite.keyExchange, serverHandshakeContext.negotiatedProtocol);
            if (sSLKeyExchangeValueOf != null) {
                for (Map.Entry<Byte, HandshakeProducer> entry : sSLKeyExchangeValueOf.getHandshakeProducers(serverHandshakeContext)) {
                    if (entry.getKey().byteValue() == SSLHandshake.SERVER_KEY_EXCHANGE.id) {
                        return entry.getValue().produce(connectionContext, handshakeMessage);
                    }
                }
            }
            throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "No ServerKeyExchange handshake message can be produced.");
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ServerKeyExchange$ServerKeyExchangeConsumer.class */
    private static final class ServerKeyExchangeConsumer implements SSLConsumer {
        private ServerKeyExchangeConsumer() {
        }

        @Override // sun.security.ssl.SSLConsumer
        public void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            clientHandshakeContext.handshakeConsumers.remove(Byte.valueOf(SSLHandshake.SERVER_KEY_EXCHANGE.id));
            if (clientHandshakeContext.receivedCertReq) {
                clientHandshakeContext.receivedCertReq = false;
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unexpected ServerKeyExchange handshake message");
            }
            if (clientHandshakeContext.handshakeConsumers.remove(Byte.valueOf(SSLHandshake.CERTIFICATE_STATUS.id)) != null) {
                CertificateStatus.handshakeAbsence.absent(connectionContext, null);
            }
            SSLKeyExchange sSLKeyExchangeValueOf = SSLKeyExchange.valueOf(clientHandshakeContext.negotiatedCipherSuite.keyExchange, clientHandshakeContext.negotiatedProtocol);
            if (sSLKeyExchangeValueOf != null) {
                for (Map.Entry<Byte, SSLConsumer> entry : sSLKeyExchangeValueOf.getHandshakeConsumers(clientHandshakeContext)) {
                    if (entry.getKey().byteValue() == SSLHandshake.SERVER_KEY_EXCHANGE.id) {
                        entry.getValue().consume(connectionContext, byteBuffer);
                        return;
                    }
                }
            }
            throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unexpected ServerKeyExchange handshake message.");
        }
    }
}
