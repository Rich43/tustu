package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import javax.net.ssl.SSLProtocolException;
import sun.security.ssl.SSLExtension;
import sun.security.ssl.SSLHandshake;

/* loaded from: jsse.jar:sun/security/ssl/ExtendedMasterSecretExtension.class */
final class ExtendedMasterSecretExtension {
    static final HandshakeProducer chNetworkProducer = new CHExtendedMasterSecretProducer();
    static final SSLExtension.ExtensionConsumer chOnLoadConsumer = new CHExtendedMasterSecretConsumer();
    static final HandshakeAbsence chOnLoadAbsence = new CHExtendedMasterSecretAbsence();
    static final HandshakeProducer shNetworkProducer = new SHExtendedMasterSecretProducer();
    static final SSLExtension.ExtensionConsumer shOnLoadConsumer = new SHExtendedMasterSecretConsumer();
    static final HandshakeAbsence shOnLoadAbsence = new SHExtendedMasterSecretAbsence();
    static final SSLStringizer emsStringizer = new ExtendedMasterSecretStringizer();

    ExtendedMasterSecretExtension() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/ExtendedMasterSecretExtension$ExtendedMasterSecretSpec.class */
    static final class ExtendedMasterSecretSpec implements SSLExtension.SSLExtensionSpec {
        static final ExtendedMasterSecretSpec NOMINAL = new ExtendedMasterSecretSpec();

        private ExtendedMasterSecretSpec() {
        }

        private ExtendedMasterSecretSpec(ByteBuffer byteBuffer) throws IOException {
            if (byteBuffer.hasRemaining()) {
                throw new SSLProtocolException("Invalid extended_master_secret extension data: not empty");
            }
        }

        public String toString() {
            return "<empty>";
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ExtendedMasterSecretExtension$ExtendedMasterSecretStringizer.class */
    private static final class ExtendedMasterSecretStringizer implements SSLStringizer {
        private ExtendedMasterSecretStringizer() {
        }

        @Override // sun.security.ssl.SSLStringizer
        public String toString(ByteBuffer byteBuffer) {
            try {
                return new ExtendedMasterSecretSpec(byteBuffer).toString();
            } catch (IOException e2) {
                return e2.getMessage();
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ExtendedMasterSecretExtension$CHExtendedMasterSecretProducer.class */
    private static final class CHExtendedMasterSecretProducer implements HandshakeProducer {
        private CHExtendedMasterSecretProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (!clientHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_EXTENDED_MASTER_SECRET) || !SSLConfiguration.useExtendedMasterSecret || !clientHandshakeContext.conContext.protocolVersion.useTLS10PlusSpec()) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable extended_master_secret extension", new Object[0]);
                    return null;
                }
                return null;
            }
            if (clientHandshakeContext.handshakeSession == null || clientHandshakeContext.handshakeSession.useExtendedMasterSecret) {
                byte[] bArr = new byte[0];
                clientHandshakeContext.handshakeExtensions.put(SSLExtension.CH_EXTENDED_MASTER_SECRET, ExtendedMasterSecretSpec.NOMINAL);
                return bArr;
            }
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ExtendedMasterSecretExtension$CHExtendedMasterSecretConsumer.class */
    private static final class CHExtendedMasterSecretConsumer implements SSLExtension.ExtensionConsumer {
        private CHExtendedMasterSecretConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (!serverHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_EXTENDED_MASTER_SECRET) || !SSLConfiguration.useExtendedMasterSecret || !serverHandshakeContext.negotiatedProtocol.useTLS10PlusSpec()) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable extension: " + SSLExtension.CH_EXTENDED_MASTER_SECRET.name, new Object[0]);
                    return;
                }
                return;
            }
            try {
                new ExtendedMasterSecretSpec(byteBuffer);
                if (serverHandshakeContext.isResumption && serverHandshakeContext.resumingSession != null && !serverHandshakeContext.resumingSession.useExtendedMasterSecret) {
                    serverHandshakeContext.isResumption = false;
                    serverHandshakeContext.resumingSession = null;
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                        SSLLogger.fine("abort session resumption which did not use Extended Master Secret extension", new Object[0]);
                    }
                }
                serverHandshakeContext.handshakeExtensions.put(SSLExtension.CH_EXTENDED_MASTER_SECRET, ExtendedMasterSecretSpec.NOMINAL);
            } catch (IOException e2) {
                throw serverHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e2);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ExtendedMasterSecretExtension$CHExtendedMasterSecretAbsence.class */
    private static final class CHExtendedMasterSecretAbsence implements HandshakeAbsence {
        private CHExtendedMasterSecretAbsence() {
        }

        @Override // sun.security.ssl.HandshakeAbsence
        public void absent(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (!serverHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_EXTENDED_MASTER_SECRET) || !SSLConfiguration.useExtendedMasterSecret) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable extension: " + SSLExtension.CH_EXTENDED_MASTER_SECRET.name, new Object[0]);
                    return;
                }
                return;
            }
            if (serverHandshakeContext.negotiatedProtocol.useTLS10PlusSpec() && !SSLConfiguration.allowLegacyMasterSecret) {
                throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Extended Master Secret extension is required");
            }
            if (serverHandshakeContext.isResumption && serverHandshakeContext.resumingSession != null) {
                if (serverHandshakeContext.resumingSession.useExtendedMasterSecret) {
                    throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Missing Extended Master Secret extension on session resumption");
                }
                if (!SSLConfiguration.allowLegacyResumption) {
                    throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Missing Extended Master Secret extension on session resumption");
                }
                serverHandshakeContext.isResumption = false;
                serverHandshakeContext.resumingSession = null;
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("abort session resumption, missing Extended Master Secret extension", new Object[0]);
                }
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ExtendedMasterSecretExtension$SHExtendedMasterSecretProducer.class */
    private static final class SHExtendedMasterSecretProducer implements HandshakeProducer {
        private SHExtendedMasterSecretProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (serverHandshakeContext.handshakeSession.useExtendedMasterSecret) {
                byte[] bArr = new byte[0];
                serverHandshakeContext.handshakeExtensions.put(SSLExtension.SH_EXTENDED_MASTER_SECRET, ExtendedMasterSecretSpec.NOMINAL);
                return bArr;
            }
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ExtendedMasterSecretExtension$SHExtendedMasterSecretConsumer.class */
    private static final class SHExtendedMasterSecretConsumer implements SSLExtension.ExtensionConsumer {
        private SHExtendedMasterSecretConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (((ExtendedMasterSecretSpec) clientHandshakeContext.handshakeExtensions.get(SSLExtension.CH_EXTENDED_MASTER_SECRET)) == null) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNSUPPORTED_EXTENSION, "Server sent the extended_master_secret extension improperly");
            }
            try {
                new ExtendedMasterSecretSpec(byteBuffer);
                if (clientHandshakeContext.isResumption && clientHandshakeContext.resumingSession != null && !clientHandshakeContext.resumingSession.useExtendedMasterSecret) {
                    throw clientHandshakeContext.conContext.fatal(Alert.UNSUPPORTED_EXTENSION, "Server sent an unexpected extended_master_secret extension on session resumption");
                }
                clientHandshakeContext.handshakeExtensions.put(SSLExtension.SH_EXTENDED_MASTER_SECRET, ExtendedMasterSecretSpec.NOMINAL);
            } catch (IOException e2) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e2);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ExtendedMasterSecretExtension$SHExtendedMasterSecretAbsence.class */
    private static final class SHExtendedMasterSecretAbsence implements HandshakeAbsence {
        private SHExtendedMasterSecretAbsence() {
        }

        @Override // sun.security.ssl.HandshakeAbsence
        public void absent(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (SSLConfiguration.useExtendedMasterSecret && !SSLConfiguration.allowLegacyMasterSecret) {
                throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Extended Master Secret extension is required");
            }
            if (clientHandshakeContext.isResumption && clientHandshakeContext.resumingSession != null) {
                if (clientHandshakeContext.resumingSession.useExtendedMasterSecret) {
                    throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Missing Extended Master Secret extension on session resumption");
                }
                if (SSLConfiguration.useExtendedMasterSecret && !SSLConfiguration.allowLegacyResumption && clientHandshakeContext.negotiatedProtocol.useTLS10PlusSpec()) {
                    throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Extended Master Secret extension is required");
                }
            }
        }
    }
}
