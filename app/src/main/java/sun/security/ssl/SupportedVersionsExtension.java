package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.MessageFormat;
import java.util.Locale;
import javax.net.ssl.SSLProtocolException;
import sun.security.ssl.SSLExtension;
import sun.security.ssl.SSLHandshake;

/* loaded from: jsse.jar:sun/security/ssl/SupportedVersionsExtension.class */
final class SupportedVersionsExtension {
    static final HandshakeProducer chNetworkProducer = new CHSupportedVersionsProducer();
    static final SSLExtension.ExtensionConsumer chOnLoadConsumer = new CHSupportedVersionsConsumer();
    static final SSLStringizer chStringizer = new CHSupportedVersionsStringizer();
    static final HandshakeProducer shNetworkProducer = new SHSupportedVersionsProducer();
    static final SSLExtension.ExtensionConsumer shOnLoadConsumer = new SHSupportedVersionsConsumer();
    static final SSLStringizer shStringizer = new SHSupportedVersionsStringizer();
    static final HandshakeProducer hrrNetworkProducer = new HRRSupportedVersionsProducer();
    static final SSLExtension.ExtensionConsumer hrrOnLoadConsumer = new HRRSupportedVersionsConsumer();
    static final HandshakeProducer hrrReproducer = new HRRSupportedVersionsReproducer();
    static final SSLStringizer hrrStringizer = new SHSupportedVersionsStringizer();

    SupportedVersionsExtension() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/SupportedVersionsExtension$CHSupportedVersionsSpec.class */
    static final class CHSupportedVersionsSpec implements SSLExtension.SSLExtensionSpec {
        final int[] requestedProtocols;

        private CHSupportedVersionsSpec(int[] iArr) {
            this.requestedProtocols = iArr;
        }

        private CHSupportedVersionsSpec(ByteBuffer byteBuffer) throws IOException {
            if (byteBuffer.remaining() < 3) {
                throw new SSLProtocolException("Invalid supported_versions extension: insufficient data");
            }
            byte[] bytes8 = Record.getBytes8(byteBuffer);
            if (byteBuffer.hasRemaining()) {
                throw new SSLProtocolException("Invalid supported_versions extension: unknown extra data");
            }
            if (bytes8 == null || bytes8.length == 0 || (bytes8.length & 1) != 0) {
                throw new SSLProtocolException("Invalid supported_versions extension: incomplete data");
            }
            int[] iArr = new int[bytes8.length >> 1];
            int i2 = 0;
            int i3 = 0;
            while (i2 < bytes8.length) {
                int i4 = i2;
                int i5 = i2 + 1;
                byte b2 = bytes8[i4];
                i2 = i5 + 1;
                int i6 = i3;
                i3++;
                iArr[i6] = ((b2 & 255) << 8) | (bytes8[i5] & 255);
            }
            this.requestedProtocols = iArr;
        }

        public String toString() {
            MessageFormat messageFormat = new MessageFormat("\"versions\": '['{0}']'", Locale.ENGLISH);
            if (this.requestedProtocols == null || this.requestedProtocols.length == 0) {
                return messageFormat.format(new Object[]{"<no supported version specified>"});
            }
            StringBuilder sb = new StringBuilder(512);
            boolean z2 = true;
            for (int i2 : this.requestedProtocols) {
                if (z2) {
                    z2 = false;
                } else {
                    sb.append(", ");
                }
                sb.append(ProtocolVersion.nameOf(i2));
            }
            return messageFormat.format(new Object[]{sb.toString()});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SupportedVersionsExtension$CHSupportedVersionsStringizer.class */
    private static final class CHSupportedVersionsStringizer implements SSLStringizer {
        private CHSupportedVersionsStringizer() {
        }

        @Override // sun.security.ssl.SSLStringizer
        public String toString(ByteBuffer byteBuffer) {
            try {
                return new CHSupportedVersionsSpec(byteBuffer).toString();
            } catch (IOException e2) {
                return e2.getMessage();
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SupportedVersionsExtension$CHSupportedVersionsProducer.class */
    private static final class CHSupportedVersionsProducer implements HandshakeProducer {
        private CHSupportedVersionsProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (!clientHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_SUPPORTED_VERSIONS)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable extension: " + SSLExtension.CH_SUPPORTED_VERSIONS.name, new Object[0]);
                    return null;
                }
                return null;
            }
            int[] iArr = new int[clientHandshakeContext.activeProtocols.size()];
            int length = iArr.length * 2;
            byte[] bArr = new byte[length + 1];
            bArr[0] = (byte) (length & 255);
            int i2 = 0;
            int i3 = 1;
            for (ProtocolVersion protocolVersion : clientHandshakeContext.activeProtocols) {
                int i4 = i2;
                i2++;
                iArr[i4] = protocolVersion.id;
                int i5 = i3;
                int i6 = i3 + 1;
                bArr[i5] = protocolVersion.major;
                i3 = i6 + 1;
                bArr[i6] = protocolVersion.minor;
            }
            clientHandshakeContext.handshakeExtensions.put(SSLExtension.CH_SUPPORTED_VERSIONS, new CHSupportedVersionsSpec(iArr));
            return bArr;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SupportedVersionsExtension$CHSupportedVersionsConsumer.class */
    private static final class CHSupportedVersionsConsumer implements SSLExtension.ExtensionConsumer {
        private CHSupportedVersionsConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (!serverHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_SUPPORTED_VERSIONS)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable extension: " + SSLExtension.CH_SUPPORTED_VERSIONS.name, new Object[0]);
                    return;
                }
                return;
            }
            try {
                serverHandshakeContext.handshakeExtensions.put(SSLExtension.CH_SUPPORTED_VERSIONS, new CHSupportedVersionsSpec(byteBuffer));
            } catch (IOException e2) {
                throw serverHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e2);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SupportedVersionsExtension$SHSupportedVersionsSpec.class */
    static final class SHSupportedVersionsSpec implements SSLExtension.SSLExtensionSpec {
        final int selectedVersion;

        private SHSupportedVersionsSpec(ProtocolVersion protocolVersion) {
            this.selectedVersion = protocolVersion.id;
        }

        private SHSupportedVersionsSpec(ByteBuffer byteBuffer) throws IOException {
            if (byteBuffer.remaining() != 2) {
                throw new SSLProtocolException("Invalid supported_versions: insufficient data");
            }
            this.selectedVersion = ((byteBuffer.get() & 255) << 8) | (byteBuffer.get() & 255);
        }

        public String toString() {
            return new MessageFormat("\"selected version\": '['{0}']'", Locale.ENGLISH).format(new Object[]{ProtocolVersion.nameOf(this.selectedVersion)});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SupportedVersionsExtension$SHSupportedVersionsStringizer.class */
    private static final class SHSupportedVersionsStringizer implements SSLStringizer {
        private SHSupportedVersionsStringizer() {
        }

        @Override // sun.security.ssl.SSLStringizer
        public String toString(ByteBuffer byteBuffer) {
            try {
                return new SHSupportedVersionsSpec(byteBuffer).toString();
            } catch (IOException e2) {
                return e2.getMessage();
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SupportedVersionsExtension$SHSupportedVersionsProducer.class */
    private static final class SHSupportedVersionsProducer implements HandshakeProducer {
        private SHSupportedVersionsProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (((CHSupportedVersionsSpec) serverHandshakeContext.handshakeExtensions.get(SSLExtension.CH_SUPPORTED_VERSIONS)) == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.warning("Ignore unavailable supported_versions extension", new Object[0]);
                    return null;
                }
                return null;
            }
            if (!serverHandshakeContext.sslConfig.isAvailable(SSLExtension.SH_SUPPORTED_VERSIONS)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable extension: " + SSLExtension.SH_SUPPORTED_VERSIONS.name, new Object[0]);
                    return null;
                }
                return null;
            }
            byte[] bArr = {serverHandshakeContext.negotiatedProtocol.major, serverHandshakeContext.negotiatedProtocol.minor};
            serverHandshakeContext.handshakeExtensions.put(SSLExtension.SH_SUPPORTED_VERSIONS, new SHSupportedVersionsSpec(serverHandshakeContext.negotiatedProtocol));
            return bArr;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SupportedVersionsExtension$SHSupportedVersionsConsumer.class */
    private static final class SHSupportedVersionsConsumer implements SSLExtension.ExtensionConsumer {
        private SHSupportedVersionsConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (!clientHandshakeContext.sslConfig.isAvailable(SSLExtension.SH_SUPPORTED_VERSIONS)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable extension: " + SSLExtension.SH_SUPPORTED_VERSIONS.name, new Object[0]);
                    return;
                }
                return;
            }
            try {
                clientHandshakeContext.handshakeExtensions.put(SSLExtension.SH_SUPPORTED_VERSIONS, new SHSupportedVersionsSpec(byteBuffer));
            } catch (IOException e2) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e2);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SupportedVersionsExtension$HRRSupportedVersionsProducer.class */
    private static final class HRRSupportedVersionsProducer implements HandshakeProducer {
        private HRRSupportedVersionsProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (!serverHandshakeContext.sslConfig.isAvailable(SSLExtension.HRR_SUPPORTED_VERSIONS)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable extension: " + SSLExtension.HRR_SUPPORTED_VERSIONS.name, new Object[0]);
                    return null;
                }
                return null;
            }
            byte[] bArr = {serverHandshakeContext.negotiatedProtocol.major, serverHandshakeContext.negotiatedProtocol.minor};
            serverHandshakeContext.handshakeExtensions.put(SSLExtension.HRR_SUPPORTED_VERSIONS, new SHSupportedVersionsSpec(serverHandshakeContext.negotiatedProtocol));
            return bArr;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SupportedVersionsExtension$HRRSupportedVersionsConsumer.class */
    private static final class HRRSupportedVersionsConsumer implements SSLExtension.ExtensionConsumer {
        private HRRSupportedVersionsConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (!clientHandshakeContext.sslConfig.isAvailable(SSLExtension.HRR_SUPPORTED_VERSIONS)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable extension: " + SSLExtension.HRR_SUPPORTED_VERSIONS.name, new Object[0]);
                    return;
                }
                return;
            }
            try {
                clientHandshakeContext.handshakeExtensions.put(SSLExtension.HRR_SUPPORTED_VERSIONS, new SHSupportedVersionsSpec(byteBuffer));
            } catch (IOException e2) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e2);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SupportedVersionsExtension$HRRSupportedVersionsReproducer.class */
    private static final class HRRSupportedVersionsReproducer implements HandshakeProducer {
        private HRRSupportedVersionsReproducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (!serverHandshakeContext.sslConfig.isAvailable(SSLExtension.HRR_SUPPORTED_VERSIONS)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("[Reproduce] Ignore unavailable extension: " + SSLExtension.HRR_SUPPORTED_VERSIONS.name, new Object[0]);
                    return null;
                }
                return null;
            }
            return new byte[]{serverHandshakeContext.negotiatedProtocol.major, serverHandshakeContext.negotiatedProtocol.minor};
        }
    }
}
