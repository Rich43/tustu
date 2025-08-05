package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import sun.security.ssl.SSLHandshake;

/* loaded from: jsse.jar:sun/security/ssl/ClientKeyExchange.class */
final class ClientKeyExchange {
    static final SSLConsumer handshakeConsumer = new ClientKeyExchangeConsumer();
    static final HandshakeProducer handshakeProducer = new ClientKeyExchangeProducer();

    ClientKeyExchange() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/ClientKeyExchange$ClientKeyExchangeProducer.class */
    private static final class ClientKeyExchangeProducer implements HandshakeProducer {
        private ClientKeyExchangeProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            SSLKeyExchange sSLKeyExchangeValueOf = SSLKeyExchange.valueOf(clientHandshakeContext.negotiatedCipherSuite.keyExchange, clientHandshakeContext.negotiatedProtocol);
            if (sSLKeyExchangeValueOf != null) {
                for (Map.Entry<Byte, HandshakeProducer> entry : sSLKeyExchangeValueOf.getHandshakeProducers(clientHandshakeContext)) {
                    if (entry.getKey().byteValue() == SSLHandshake.CLIENT_KEY_EXCHANGE.id) {
                        return entry.getValue().produce(connectionContext, handshakeMessage);
                    }
                }
            }
            throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unexpected ClientKeyExchange handshake message.");
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ClientKeyExchange$ClientKeyExchangeConsumer.class */
    private static final class ClientKeyExchangeConsumer implements SSLConsumer {
        private ClientKeyExchangeConsumer() {
        }

        @Override // sun.security.ssl.SSLConsumer
        public void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            serverHandshakeContext.handshakeConsumers.remove(Byte.valueOf(SSLHandshake.CLIENT_KEY_EXCHANGE.id));
            if (serverHandshakeContext.handshakeConsumers.containsKey(Byte.valueOf(SSLHandshake.CERTIFICATE.id))) {
                throw serverHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unexpected ClientKeyExchange handshake message.");
            }
            SSLKeyExchange sSLKeyExchangeValueOf = SSLKeyExchange.valueOf(serverHandshakeContext.negotiatedCipherSuite.keyExchange, serverHandshakeContext.negotiatedProtocol);
            if (sSLKeyExchangeValueOf != null) {
                for (Map.Entry<Byte, SSLConsumer> entry : sSLKeyExchangeValueOf.getHandshakeConsumers(serverHandshakeContext)) {
                    if (entry.getKey().byteValue() == SSLHandshake.CLIENT_KEY_EXCHANGE.id) {
                        entry.getValue().consume(connectionContext, byteBuffer);
                        return;
                    }
                }
            }
            throw serverHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unexpected ClientKeyExchange handshake message.");
        }
    }
}
