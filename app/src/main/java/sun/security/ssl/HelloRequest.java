package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import sun.security.ssl.SSLHandshake;

/* loaded from: jsse.jar:sun/security/ssl/HelloRequest.class */
final class HelloRequest {
    static final SSLProducer kickstartProducer = new HelloRequestKickstartProducer();
    static final SSLConsumer handshakeConsumer = new HelloRequestConsumer();
    static final HandshakeProducer handshakeProducer = new HelloRequestProducer();

    HelloRequest() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/HelloRequest$HelloRequestMessage.class */
    static final class HelloRequestMessage extends SSLHandshake.HandshakeMessage {
        HelloRequestMessage(HandshakeContext handshakeContext) {
            super(handshakeContext);
        }

        HelloRequestMessage(HandshakeContext handshakeContext, ByteBuffer byteBuffer) throws IOException {
            super(handshakeContext);
            if (byteBuffer.hasRemaining()) {
                throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Error parsing HelloRequest message: not empty");
            }
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public SSLHandshake handshakeType() {
            return SSLHandshake.HELLO_REQUEST;
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

    /* loaded from: jsse.jar:sun/security/ssl/HelloRequest$HelloRequestKickstartProducer.class */
    private static final class HelloRequestKickstartProducer implements SSLProducer {
        private HelloRequestKickstartProducer() {
        }

        @Override // sun.security.ssl.SSLProducer
        public byte[] produce(ConnectionContext connectionContext) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            HelloRequestMessage helloRequestMessage = new HelloRequestMessage(serverHandshakeContext);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Produced HelloRequest handshake message", helloRequestMessage);
            }
            helloRequestMessage.write(serverHandshakeContext.handshakeOutput);
            serverHandshakeContext.handshakeOutput.flush();
            serverHandshakeContext.handshakeConsumers.put(Byte.valueOf(SSLHandshake.CLIENT_HELLO.id), SSLHandshake.CLIENT_HELLO);
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/HelloRequest$HelloRequestProducer.class */
    private static final class HelloRequestProducer implements HandshakeProducer {
        private HelloRequestProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            HelloRequestMessage helloRequestMessage = new HelloRequestMessage(serverHandshakeContext);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Produced HelloRequest handshake message", helloRequestMessage);
            }
            helloRequestMessage.write(serverHandshakeContext.handshakeOutput);
            serverHandshakeContext.handshakeOutput.flush();
            serverHandshakeContext.handshakeConsumers.put(Byte.valueOf(SSLHandshake.CLIENT_HELLO.id), SSLHandshake.CLIENT_HELLO);
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/HelloRequest$HelloRequestConsumer.class */
    private static final class HelloRequestConsumer implements SSLConsumer {
        private HelloRequestConsumer() {
        }

        @Override // sun.security.ssl.SSLConsumer
        public void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            HelloRequestMessage helloRequestMessage = new HelloRequestMessage(clientHandshakeContext, byteBuffer);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Consuming HelloRequest handshake message", helloRequestMessage);
            }
            if (!clientHandshakeContext.kickstartMessageDelivered) {
                if (!clientHandshakeContext.conContext.secureRenegotiation && !HandshakeContext.allowUnsafeRenegotiation) {
                    throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Unsafe renegotiation is not allowed");
                }
                if (!clientHandshakeContext.conContext.secureRenegotiation && SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.warning("Continue with insecure renegotiation", new Object[0]);
                }
                clientHandshakeContext.handshakeProducers.put(Byte.valueOf(SSLHandshake.CLIENT_HELLO.id), SSLHandshake.CLIENT_HELLO);
                SSLHandshake.CLIENT_HELLO.produce(connectionContext, helloRequestMessage);
                return;
            }
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Ingore HelloRequest, handshaking is in progress", new Object[0]);
            }
        }
    }
}
