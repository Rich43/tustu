package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import javax.net.ssl.SSLProtocolException;
import sun.security.ssl.SSLExtension;
import sun.security.ssl.SSLHandshake;

/* loaded from: jsse.jar:sun/security/ssl/MaxFragExtension.class */
final class MaxFragExtension {
    static final HandshakeProducer chNetworkProducer = new CHMaxFragmentLengthProducer();
    static final SSLExtension.ExtensionConsumer chOnLoadConsumer = new CHMaxFragmentLengthConsumer();
    static final HandshakeProducer shNetworkProducer = new SHMaxFragmentLengthProducer();
    static final SSLExtension.ExtensionConsumer shOnLoadConsumer = new SHMaxFragmentLengthConsumer();
    static final HandshakeConsumer shOnTradeConsumer = new SHMaxFragmentLengthUpdate();
    static final HandshakeProducer eeNetworkProducer = new EEMaxFragmentLengthProducer();
    static final SSLExtension.ExtensionConsumer eeOnLoadConsumer = new EEMaxFragmentLengthConsumer();
    static final HandshakeConsumer eeOnTradeConsumer = new EEMaxFragmentLengthUpdate();
    static final SSLStringizer maxFragLenStringizer = new MaxFragLenStringizer();

    MaxFragExtension() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/MaxFragExtension$MaxFragLenSpec.class */
    static final class MaxFragLenSpec implements SSLExtension.SSLExtensionSpec {
        byte id;

        private MaxFragLenSpec(byte b2) {
            this.id = b2;
        }

        private MaxFragLenSpec(ByteBuffer byteBuffer) throws IOException {
            if (byteBuffer.remaining() != 1) {
                throw new SSLProtocolException("Invalid max_fragment_length extension data");
            }
            this.id = byteBuffer.get();
        }

        public String toString() {
            return MaxFragLenEnum.nameOf(this.id);
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/MaxFragExtension$MaxFragLenStringizer.class */
    private static final class MaxFragLenStringizer implements SSLStringizer {
        private MaxFragLenStringizer() {
        }

        @Override // sun.security.ssl.SSLStringizer
        public String toString(ByteBuffer byteBuffer) {
            try {
                return new MaxFragLenSpec(byteBuffer).toString();
            } catch (IOException e2) {
                return e2.getMessage();
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/MaxFragExtension$MaxFragLenEnum.class */
    enum MaxFragLenEnum {
        MFL_512((byte) 1, 512, "2^9"),
        MFL_1024((byte) 2, 1024, "2^10"),
        MFL_2048((byte) 3, 2048, "2^11"),
        MFL_4096((byte) 4, 4096, "2^12");

        final byte id;
        final int fragmentSize;
        final String description;

        MaxFragLenEnum(byte b2, int i2, String str) {
            this.id = b2;
            this.fragmentSize = i2;
            this.description = str;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static MaxFragLenEnum valueOf(byte b2) {
            for (MaxFragLenEnum maxFragLenEnum : values()) {
                if (maxFragLenEnum.id == b2) {
                    return maxFragLenEnum;
                }
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static String nameOf(byte b2) {
            for (MaxFragLenEnum maxFragLenEnum : values()) {
                if (maxFragLenEnum.id == b2) {
                    return maxFragLenEnum.description;
                }
            }
            return "UNDEFINED-MAX-FRAGMENT-LENGTH(" + ((int) b2) + ")";
        }

        static MaxFragLenEnum valueOf(int i2) {
            if (i2 <= 0) {
                return null;
            }
            if (i2 < 1024) {
                return MFL_512;
            }
            if (i2 < 2048) {
                return MFL_1024;
            }
            if (i2 < 4096) {
                return MFL_2048;
            }
            if (i2 == 4096) {
                return MFL_4096;
            }
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/MaxFragExtension$CHMaxFragmentLengthProducer.class */
    private static final class CHMaxFragmentLengthProducer implements HandshakeProducer {
        private CHMaxFragmentLengthProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            int negotiatedMaxFragSize;
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (!clientHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_MAX_FRAGMENT_LENGTH)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable max_fragment_length extension", new Object[0]);
                    return null;
                }
                return null;
            }
            if (clientHandshakeContext.isResumption && clientHandshakeContext.resumingSession != null) {
                negotiatedMaxFragSize = clientHandshakeContext.resumingSession.getNegotiatedMaxFragSize();
            } else if (clientHandshakeContext.sslConfig.maximumPacketSize != 0) {
                negotiatedMaxFragSize = clientHandshakeContext.sslConfig.maximumPacketSize - SSLRecord.maxPlaintextPlusSize;
            } else {
                negotiatedMaxFragSize = -1;
            }
            MaxFragLenEnum maxFragLenEnumValueOf = MaxFragLenEnum.valueOf(negotiatedMaxFragSize);
            if (maxFragLenEnumValueOf != null) {
                clientHandshakeContext.handshakeExtensions.put(SSLExtension.CH_MAX_FRAGMENT_LENGTH, new MaxFragLenSpec(maxFragLenEnumValueOf.id));
                return new byte[]{maxFragLenEnumValueOf.id};
            }
            clientHandshakeContext.maxFragmentLength = -1;
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("No available max_fragment_length extension can be used for fragment size of " + negotiatedMaxFragSize + "bytes", new Object[0]);
                return null;
            }
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/MaxFragExtension$CHMaxFragmentLengthConsumer.class */
    private static final class CHMaxFragmentLengthConsumer implements SSLExtension.ExtensionConsumer {
        private CHMaxFragmentLengthConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (!serverHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_MAX_FRAGMENT_LENGTH)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable max_fragment_length extension", new Object[0]);
                    return;
                }
                return;
            }
            try {
                MaxFragLenSpec maxFragLenSpec = new MaxFragLenSpec(byteBuffer);
                MaxFragLenEnum maxFragLenEnumValueOf = MaxFragLenEnum.valueOf(maxFragLenSpec.id);
                if (maxFragLenEnumValueOf == null) {
                    throw serverHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "the requested maximum fragment length is other than the allowed values");
                }
                serverHandshakeContext.maxFragmentLength = maxFragLenEnumValueOf.fragmentSize;
                serverHandshakeContext.handshakeExtensions.put(SSLExtension.CH_MAX_FRAGMENT_LENGTH, maxFragLenSpec);
            } catch (IOException e2) {
                throw serverHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e2);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/MaxFragExtension$SHMaxFragmentLengthProducer.class */
    private static final class SHMaxFragmentLengthProducer implements HandshakeProducer {
        private SHMaxFragmentLengthProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            MaxFragLenSpec maxFragLenSpec = (MaxFragLenSpec) serverHandshakeContext.handshakeExtensions.get(SSLExtension.CH_MAX_FRAGMENT_LENGTH);
            if (maxFragLenSpec == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.finest("Ignore unavailable max_fragment_length extension", new Object[0]);
                    return null;
                }
                return null;
            }
            if (serverHandshakeContext.maxFragmentLength > 0 && serverHandshakeContext.sslConfig.maximumPacketSize != 0 && serverHandshakeContext.negotiatedCipherSuite.calculatePacketSize(serverHandshakeContext.maxFragmentLength, serverHandshakeContext.negotiatedProtocol) > serverHandshakeContext.sslConfig.maximumPacketSize) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Abort the maximum fragment length negotiation, may overflow the maximum packet size limit.", new Object[0]);
                }
                serverHandshakeContext.maxFragmentLength = -1;
            }
            if (serverHandshakeContext.maxFragmentLength > 0) {
                serverHandshakeContext.handshakeSession.setNegotiatedMaxFragSize(serverHandshakeContext.maxFragmentLength);
                serverHandshakeContext.conContext.inputRecord.changeFragmentSize(serverHandshakeContext.maxFragmentLength);
                serverHandshakeContext.conContext.outputRecord.changeFragmentSize(serverHandshakeContext.maxFragmentLength);
                serverHandshakeContext.handshakeExtensions.put(SSLExtension.SH_MAX_FRAGMENT_LENGTH, maxFragLenSpec);
                return new byte[]{maxFragLenSpec.id};
            }
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/MaxFragExtension$SHMaxFragmentLengthConsumer.class */
    private static final class SHMaxFragmentLengthConsumer implements SSLExtension.ExtensionConsumer {
        private SHMaxFragmentLengthConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            MaxFragLenSpec maxFragLenSpec = (MaxFragLenSpec) clientHandshakeContext.handshakeExtensions.get(SSLExtension.CH_MAX_FRAGMENT_LENGTH);
            if (maxFragLenSpec == null) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unexpected max_fragment_length extension in ServerHello");
            }
            try {
                MaxFragLenSpec maxFragLenSpec2 = new MaxFragLenSpec(byteBuffer);
                if (maxFragLenSpec2.id == maxFragLenSpec.id) {
                    MaxFragLenEnum maxFragLenEnumValueOf = MaxFragLenEnum.valueOf(maxFragLenSpec2.id);
                    if (maxFragLenEnumValueOf == null) {
                        throw clientHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "the requested maximum fragment length is other than the allowed values");
                    }
                    clientHandshakeContext.maxFragmentLength = maxFragLenEnumValueOf.fragmentSize;
                    clientHandshakeContext.handshakeExtensions.put(SSLExtension.SH_MAX_FRAGMENT_LENGTH, maxFragLenSpec2);
                    return;
                }
                throw clientHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "The maximum fragment length response is not requested");
            } catch (IOException e2) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e2);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/MaxFragExtension$SHMaxFragmentLengthUpdate.class */
    private static final class SHMaxFragmentLengthUpdate implements HandshakeConsumer {
        private SHMaxFragmentLengthUpdate() {
        }

        @Override // sun.security.ssl.HandshakeConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (((MaxFragLenSpec) clientHandshakeContext.handshakeExtensions.get(SSLExtension.SH_MAX_FRAGMENT_LENGTH)) == null) {
                return;
            }
            if (clientHandshakeContext.maxFragmentLength > 0 && clientHandshakeContext.sslConfig.maximumPacketSize != 0 && clientHandshakeContext.negotiatedCipherSuite.calculatePacketSize(clientHandshakeContext.maxFragmentLength, clientHandshakeContext.negotiatedProtocol) > clientHandshakeContext.sslConfig.maximumPacketSize) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Abort the maximum fragment length negotiation, may overflow the maximum packet size limit.", new Object[0]);
                }
                clientHandshakeContext.maxFragmentLength = -1;
            }
            if (clientHandshakeContext.maxFragmentLength > 0) {
                clientHandshakeContext.handshakeSession.setNegotiatedMaxFragSize(clientHandshakeContext.maxFragmentLength);
                clientHandshakeContext.conContext.inputRecord.changeFragmentSize(clientHandshakeContext.maxFragmentLength);
                clientHandshakeContext.conContext.outputRecord.changeFragmentSize(clientHandshakeContext.maxFragmentLength);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/MaxFragExtension$EEMaxFragmentLengthProducer.class */
    private static final class EEMaxFragmentLengthProducer implements HandshakeProducer {
        private EEMaxFragmentLengthProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            MaxFragLenSpec maxFragLenSpec = (MaxFragLenSpec) serverHandshakeContext.handshakeExtensions.get(SSLExtension.CH_MAX_FRAGMENT_LENGTH);
            if (maxFragLenSpec == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.finest("Ignore unavailable max_fragment_length extension", new Object[0]);
                    return null;
                }
                return null;
            }
            if (serverHandshakeContext.maxFragmentLength > 0 && serverHandshakeContext.sslConfig.maximumPacketSize != 0 && serverHandshakeContext.negotiatedCipherSuite.calculatePacketSize(serverHandshakeContext.maxFragmentLength, serverHandshakeContext.negotiatedProtocol) > serverHandshakeContext.sslConfig.maximumPacketSize) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Abort the maximum fragment length negotiation, may overflow the maximum packet size limit.", new Object[0]);
                }
                serverHandshakeContext.maxFragmentLength = -1;
            }
            if (serverHandshakeContext.maxFragmentLength > 0) {
                serverHandshakeContext.handshakeSession.setNegotiatedMaxFragSize(serverHandshakeContext.maxFragmentLength);
                serverHandshakeContext.conContext.inputRecord.changeFragmentSize(serverHandshakeContext.maxFragmentLength);
                serverHandshakeContext.conContext.outputRecord.changeFragmentSize(serverHandshakeContext.maxFragmentLength);
                serverHandshakeContext.handshakeExtensions.put(SSLExtension.EE_MAX_FRAGMENT_LENGTH, maxFragLenSpec);
                return new byte[]{maxFragLenSpec.id};
            }
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/MaxFragExtension$EEMaxFragmentLengthConsumer.class */
    private static final class EEMaxFragmentLengthConsumer implements SSLExtension.ExtensionConsumer {
        private EEMaxFragmentLengthConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            MaxFragLenSpec maxFragLenSpec = (MaxFragLenSpec) clientHandshakeContext.handshakeExtensions.get(SSLExtension.CH_MAX_FRAGMENT_LENGTH);
            if (maxFragLenSpec == null) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unexpected max_fragment_length extension in ServerHello");
            }
            try {
                MaxFragLenSpec maxFragLenSpec2 = new MaxFragLenSpec(byteBuffer);
                if (maxFragLenSpec2.id == maxFragLenSpec.id) {
                    MaxFragLenEnum maxFragLenEnumValueOf = MaxFragLenEnum.valueOf(maxFragLenSpec2.id);
                    if (maxFragLenEnumValueOf == null) {
                        throw clientHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "the requested maximum fragment length is other than the allowed values");
                    }
                    clientHandshakeContext.maxFragmentLength = maxFragLenEnumValueOf.fragmentSize;
                    clientHandshakeContext.handshakeExtensions.put(SSLExtension.EE_MAX_FRAGMENT_LENGTH, maxFragLenSpec2);
                    return;
                }
                throw clientHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "The maximum fragment length response is not requested");
            } catch (IOException e2) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e2);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/MaxFragExtension$EEMaxFragmentLengthUpdate.class */
    private static final class EEMaxFragmentLengthUpdate implements HandshakeConsumer {
        private EEMaxFragmentLengthUpdate() {
        }

        @Override // sun.security.ssl.HandshakeConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (((MaxFragLenSpec) clientHandshakeContext.handshakeExtensions.get(SSLExtension.EE_MAX_FRAGMENT_LENGTH)) == null) {
                return;
            }
            if (clientHandshakeContext.maxFragmentLength > 0 && clientHandshakeContext.sslConfig.maximumPacketSize != 0 && clientHandshakeContext.negotiatedCipherSuite.calculatePacketSize(clientHandshakeContext.maxFragmentLength, clientHandshakeContext.negotiatedProtocol) > clientHandshakeContext.sslConfig.maximumPacketSize) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Abort the maximum fragment length negotiation, may overflow the maximum packet size limit.", new Object[0]);
                }
                clientHandshakeContext.maxFragmentLength = -1;
            }
            if (clientHandshakeContext.maxFragmentLength > 0) {
                clientHandshakeContext.handshakeSession.setNegotiatedMaxFragSize(clientHandshakeContext.maxFragmentLength);
                clientHandshakeContext.conContext.inputRecord.changeFragmentSize(clientHandshakeContext.maxFragmentLength);
                clientHandshakeContext.conContext.outputRecord.changeFragmentSize(clientHandshakeContext.maxFragmentLength);
            }
        }
    }
}
