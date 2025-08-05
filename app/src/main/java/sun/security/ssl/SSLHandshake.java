package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.AbstractMap;
import java.util.Map;
import javax.net.ssl.SSLException;

/* loaded from: jsse.jar:sun/security/ssl/SSLHandshake.class */
enum SSLHandshake implements SSLConsumer, HandshakeProducer {
    HELLO_REQUEST((byte) 0, "hello_request", new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(HelloRequest.handshakeConsumer, ProtocolVersion.PROTOCOLS_TO_12)}, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(HelloRequest.handshakeProducer, ProtocolVersion.PROTOCOLS_TO_12)}),
    CLIENT_HELLO((byte) 1, "client_hello", new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(ClientHello.handshakeConsumer, ProtocolVersion.PROTOCOLS_TO_13)}, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(ClientHello.handshakeProducer, ProtocolVersion.PROTOCOLS_TO_13)}),
    SERVER_HELLO((byte) 2, "server_hello", new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(ServerHello.handshakeConsumer, ProtocolVersion.PROTOCOLS_TO_13)}, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(ServerHello.t12HandshakeProducer, ProtocolVersion.PROTOCOLS_TO_12), new AbstractMap.SimpleImmutableEntry(ServerHello.t13HandshakeProducer, ProtocolVersion.PROTOCOLS_OF_13)}),
    HELLO_RETRY_REQUEST((byte) 2, "hello_retry_request", new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(ServerHello.handshakeConsumer, ProtocolVersion.PROTOCOLS_TO_13)}, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(ServerHello.hrrHandshakeProducer, ProtocolVersion.PROTOCOLS_OF_13)}),
    NEW_SESSION_TICKET((byte) 4, "new_session_ticket", new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(NewSessionTicket.handshakeConsumer, ProtocolVersion.PROTOCOLS_OF_13)}, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(NewSessionTicket.handshakeProducer, ProtocolVersion.PROTOCOLS_OF_13)}),
    END_OF_EARLY_DATA((byte) 5, "end_of_early_data"),
    ENCRYPTED_EXTENSIONS((byte) 8, "encrypted_extensions", new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(EncryptedExtensions.handshakeConsumer, ProtocolVersion.PROTOCOLS_OF_13)}, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(EncryptedExtensions.handshakeProducer, ProtocolVersion.PROTOCOLS_OF_13)}),
    CERTIFICATE((byte) 11, "certificate", new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(CertificateMessage.t12HandshakeConsumer, ProtocolVersion.PROTOCOLS_TO_12), new AbstractMap.SimpleImmutableEntry(CertificateMessage.t13HandshakeConsumer, ProtocolVersion.PROTOCOLS_OF_13)}, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(CertificateMessage.t12HandshakeProducer, ProtocolVersion.PROTOCOLS_TO_12), new AbstractMap.SimpleImmutableEntry(CertificateMessage.t13HandshakeProducer, ProtocolVersion.PROTOCOLS_OF_13)}),
    SERVER_KEY_EXCHANGE((byte) 12, "server_key_exchange", new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(ServerKeyExchange.handshakeConsumer, ProtocolVersion.PROTOCOLS_TO_12)}, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(ServerKeyExchange.handshakeProducer, ProtocolVersion.PROTOCOLS_TO_12)}),
    CERTIFICATE_REQUEST((byte) 13, "certificate_request", new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(CertificateRequest.t10HandshakeConsumer, ProtocolVersion.PROTOCOLS_TO_11), new AbstractMap.SimpleImmutableEntry(CertificateRequest.t12HandshakeConsumer, ProtocolVersion.PROTOCOLS_OF_12), new AbstractMap.SimpleImmutableEntry(CertificateRequest.t13HandshakeConsumer, ProtocolVersion.PROTOCOLS_OF_13)}, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(CertificateRequest.t10HandshakeProducer, ProtocolVersion.PROTOCOLS_TO_11), new AbstractMap.SimpleImmutableEntry(CertificateRequest.t12HandshakeProducer, ProtocolVersion.PROTOCOLS_OF_12), new AbstractMap.SimpleImmutableEntry(CertificateRequest.t13HandshakeProducer, ProtocolVersion.PROTOCOLS_OF_13)}),
    SERVER_HELLO_DONE((byte) 14, "server_hello_done", new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(ServerHelloDone.handshakeConsumer, ProtocolVersion.PROTOCOLS_TO_12)}, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(ServerHelloDone.handshakeProducer, ProtocolVersion.PROTOCOLS_TO_12)}),
    CERTIFICATE_VERIFY((byte) 15, "certificate_verify", new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(CertificateVerify.s30HandshakeConsumer, ProtocolVersion.PROTOCOLS_OF_30), new AbstractMap.SimpleImmutableEntry(CertificateVerify.t10HandshakeConsumer, ProtocolVersion.PROTOCOLS_10_11), new AbstractMap.SimpleImmutableEntry(CertificateVerify.t12HandshakeConsumer, ProtocolVersion.PROTOCOLS_OF_12), new AbstractMap.SimpleImmutableEntry(CertificateVerify.t13HandshakeConsumer, ProtocolVersion.PROTOCOLS_OF_13)}, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(CertificateVerify.s30HandshakeProducer, ProtocolVersion.PROTOCOLS_OF_30), new AbstractMap.SimpleImmutableEntry(CertificateVerify.t10HandshakeProducer, ProtocolVersion.PROTOCOLS_10_11), new AbstractMap.SimpleImmutableEntry(CertificateVerify.t12HandshakeProducer, ProtocolVersion.PROTOCOLS_OF_12), new AbstractMap.SimpleImmutableEntry(CertificateVerify.t13HandshakeProducer, ProtocolVersion.PROTOCOLS_OF_13)}),
    CLIENT_KEY_EXCHANGE((byte) 16, "client_key_exchange", new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(ClientKeyExchange.handshakeConsumer, ProtocolVersion.PROTOCOLS_TO_12)}, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(ClientKeyExchange.handshakeProducer, ProtocolVersion.PROTOCOLS_TO_12)}),
    FINISHED((byte) 20, "finished", new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(Finished.t12HandshakeConsumer, ProtocolVersion.PROTOCOLS_TO_12), new AbstractMap.SimpleImmutableEntry(Finished.t13HandshakeConsumer, ProtocolVersion.PROTOCOLS_OF_13)}, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(Finished.t12HandshakeProducer, ProtocolVersion.PROTOCOLS_TO_12), new AbstractMap.SimpleImmutableEntry(Finished.t13HandshakeProducer, ProtocolVersion.PROTOCOLS_OF_13)}),
    CERTIFICATE_URL((byte) 21, "certificate_url"),
    CERTIFICATE_STATUS((byte) 22, "certificate_status", new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(CertificateStatus.handshakeConsumer, ProtocolVersion.PROTOCOLS_TO_12)}, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(CertificateStatus.handshakeProducer, ProtocolVersion.PROTOCOLS_TO_12)}, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(CertificateStatus.handshakeAbsence, ProtocolVersion.PROTOCOLS_TO_12)}),
    SUPPLEMENTAL_DATA((byte) 23, "supplemental_data"),
    KEY_UPDATE((byte) 24, "key_update", new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(KeyUpdate.handshakeConsumer, ProtocolVersion.PROTOCOLS_OF_13)}, new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(KeyUpdate.handshakeProducer, ProtocolVersion.PROTOCOLS_OF_13)}),
    MESSAGE_HASH((byte) -2, "message_hash"),
    NOT_APPLICABLE((byte) -1, "not_applicable");

    final byte id;
    final String name;
    final Map.Entry<SSLConsumer, ProtocolVersion[]>[] handshakeConsumers;
    final Map.Entry<HandshakeProducer, ProtocolVersion[]>[] handshakeProducers;
    final Map.Entry<HandshakeAbsence, ProtocolVersion[]>[] handshakeAbsences;

    SSLHandshake(byte b2, String str) {
        this(b2, str, new Map.Entry[0], new Map.Entry[0], new Map.Entry[0]);
    }

    SSLHandshake(byte b2, String str, Map.Entry[] entryArr, Map.Entry[] entryArr2) {
        this(b2, str, entryArr, entryArr2, new Map.Entry[0]);
    }

    SSLHandshake(byte b2, String str, Map.Entry[] entryArr, Map.Entry[] entryArr2, Map.Entry[] entryArr3) {
        this.id = b2;
        this.name = str;
        this.handshakeConsumers = entryArr;
        this.handshakeProducers = entryArr2;
        this.handshakeAbsences = entryArr3;
    }

    @Override // sun.security.ssl.SSLConsumer
    public void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException {
        SSLConsumer handshakeConsumer = getHandshakeConsumer(connectionContext);
        if (handshakeConsumer != null) {
            handshakeConsumer.consume(connectionContext, byteBuffer);
            return;
        }
        throw new UnsupportedOperationException("Unsupported handshake consumer: " + this.name);
    }

    private SSLConsumer getHandshakeConsumer(ConnectionContext connectionContext) {
        ProtocolVersion protocolVersion;
        if (this.handshakeConsumers.length == 0) {
            return null;
        }
        HandshakeContext handshakeContext = (HandshakeContext) connectionContext;
        if (handshakeContext.negotiatedProtocol == null || handshakeContext.negotiatedProtocol == ProtocolVersion.NONE) {
            if (handshakeContext.conContext.isNegotiated && handshakeContext.conContext.protocolVersion != ProtocolVersion.NONE) {
                protocolVersion = handshakeContext.conContext.protocolVersion;
            } else {
                protocolVersion = handshakeContext.maximumActiveProtocol;
            }
        } else {
            protocolVersion = handshakeContext.negotiatedProtocol;
        }
        for (Map.Entry<SSLConsumer, ProtocolVersion[]> entry : this.handshakeConsumers) {
            for (ProtocolVersion protocolVersion2 : entry.getValue()) {
                if (protocolVersion == protocolVersion2) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    @Override // sun.security.ssl.HandshakeProducer
    public byte[] produce(ConnectionContext connectionContext, HandshakeMessage handshakeMessage) throws IOException {
        HandshakeProducer handshakeProducer = getHandshakeProducer(connectionContext);
        if (handshakeProducer != null) {
            return handshakeProducer.produce(connectionContext, handshakeMessage);
        }
        throw new UnsupportedOperationException("Unsupported handshake producer: " + this.name);
    }

    private HandshakeProducer getHandshakeProducer(ConnectionContext connectionContext) {
        ProtocolVersion protocolVersion;
        if (this.handshakeConsumers.length == 0) {
            return null;
        }
        HandshakeContext handshakeContext = (HandshakeContext) connectionContext;
        if (handshakeContext.negotiatedProtocol == null || handshakeContext.negotiatedProtocol == ProtocolVersion.NONE) {
            if (handshakeContext.conContext.isNegotiated && handshakeContext.conContext.protocolVersion != ProtocolVersion.NONE) {
                protocolVersion = handshakeContext.conContext.protocolVersion;
            } else {
                protocolVersion = handshakeContext.maximumActiveProtocol;
            }
        } else {
            protocolVersion = handshakeContext.negotiatedProtocol;
        }
        for (Map.Entry<HandshakeProducer, ProtocolVersion[]> entry : this.handshakeProducers) {
            for (ProtocolVersion protocolVersion2 : entry.getValue()) {
                if (protocolVersion == protocolVersion2) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.name;
    }

    static String nameOf(byte b2) {
        for (SSLHandshake sSLHandshake : values()) {
            if (sSLHandshake.id == b2) {
                return sSLHandshake.name;
            }
        }
        return "UNKNOWN-HANDSHAKE-MESSAGE(" + ((int) b2) + ")";
    }

    static boolean isKnown(byte b2) {
        for (SSLHandshake sSLHandshake : values()) {
            if (sSLHandshake.id == b2 && b2 != NOT_APPLICABLE.id) {
                return true;
            }
        }
        return false;
    }

    static final void kickstart(HandshakeContext handshakeContext) throws IOException {
        if (handshakeContext instanceof ClientHandshakeContext) {
            if (handshakeContext.conContext.isNegotiated && handshakeContext.conContext.protocolVersion.useTLS13PlusSpec()) {
                KeyUpdate.kickstartProducer.produce(handshakeContext);
                return;
            } else {
                ClientHello.kickstartProducer.produce(handshakeContext);
                return;
            }
        }
        if (handshakeContext.conContext.protocolVersion.useTLS13PlusSpec()) {
            KeyUpdate.kickstartProducer.produce(handshakeContext);
        } else {
            HelloRequest.kickstartProducer.produce(handshakeContext);
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLHandshake$HandshakeMessage.class */
    static abstract class HandshakeMessage {
        final HandshakeContext handshakeContext;

        abstract SSLHandshake handshakeType();

        abstract int messageLength();

        abstract void send(HandshakeOutStream handshakeOutStream) throws IOException;

        HandshakeMessage(HandshakeContext handshakeContext) {
            this.handshakeContext = handshakeContext;
        }

        void write(HandshakeOutStream handshakeOutStream) throws IOException {
            int iMessageLength = messageLength();
            if (iMessageLength >= 16777216) {
                throw new SSLException("Handshake message is overflow, type = " + ((Object) handshakeType()) + ", len = " + iMessageLength);
            }
            handshakeOutStream.write(handshakeType().id);
            handshakeOutStream.putInt24(iMessageLength);
            send(handshakeOutStream);
            handshakeOutStream.complete();
        }
    }
}
