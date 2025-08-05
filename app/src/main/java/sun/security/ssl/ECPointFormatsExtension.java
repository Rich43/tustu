package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.MessageFormat;
import java.util.Locale;
import javax.net.ssl.SSLProtocolException;
import sun.security.ssl.SSLExtension;
import sun.security.ssl.SSLHandshake;
import sun.security.ssl.SupportedGroupsExtension;

/* loaded from: jsse.jar:sun/security/ssl/ECPointFormatsExtension.class */
final class ECPointFormatsExtension {
    static final HandshakeProducer chNetworkProducer = new CHECPointFormatsProducer();
    static final SSLExtension.ExtensionConsumer chOnLoadConsumer = new CHECPointFormatsConsumer();
    static final SSLExtension.ExtensionConsumer shOnLoadConsumer = new SHECPointFormatsConsumer();
    static final SSLStringizer epfStringizer = new ECPointFormatsStringizer();

    ECPointFormatsExtension() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/ECPointFormatsExtension$ECPointFormatsSpec.class */
    static class ECPointFormatsSpec implements SSLExtension.SSLExtensionSpec {
        static final ECPointFormatsSpec DEFAULT = new ECPointFormatsSpec(new byte[]{ECPointFormat.UNCOMPRESSED.id});
        final byte[] formats;

        ECPointFormatsSpec(byte[] bArr) {
            this.formats = bArr;
        }

        private ECPointFormatsSpec(ByteBuffer byteBuffer) throws IOException {
            if (!byteBuffer.hasRemaining()) {
                throw new SSLProtocolException("Invalid ec_point_formats extension: insufficient data");
            }
            this.formats = Record.getBytes8(byteBuffer);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean hasUncompressedFormat() {
            for (byte b2 : this.formats) {
                if (b2 == ECPointFormat.UNCOMPRESSED.id) {
                    return true;
                }
            }
            return false;
        }

        public String toString() {
            MessageFormat messageFormat = new MessageFormat("\"formats\": '['{0}']'", Locale.ENGLISH);
            if (this.formats == null || this.formats.length == 0) {
                return messageFormat.format(new Object[]{"<no EC point format specified>"});
            }
            StringBuilder sb = new StringBuilder(512);
            boolean z2 = true;
            for (byte b2 : this.formats) {
                if (z2) {
                    z2 = false;
                } else {
                    sb.append(", ");
                }
                sb.append(ECPointFormat.nameOf(b2));
            }
            return messageFormat.format(new Object[]{sb.toString()});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ECPointFormatsExtension$ECPointFormatsStringizer.class */
    private static final class ECPointFormatsStringizer implements SSLStringizer {
        private ECPointFormatsStringizer() {
        }

        @Override // sun.security.ssl.SSLStringizer
        public String toString(ByteBuffer byteBuffer) {
            try {
                return new ECPointFormatsSpec(byteBuffer).toString();
            } catch (IOException e2) {
                return e2.getMessage();
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ECPointFormatsExtension$ECPointFormat.class */
    private enum ECPointFormat {
        UNCOMPRESSED((byte) 0, "uncompressed"),
        ANSIX962_COMPRESSED_PRIME((byte) 1, "ansiX962_compressed_prime"),
        FMT_ANSIX962_COMPRESSED_CHAR2((byte) 2, "ansiX962_compressed_char2");

        final byte id;
        final String name;

        ECPointFormat(byte b2, String str) {
            this.id = b2;
            this.name = str;
        }

        static String nameOf(int i2) {
            for (ECPointFormat eCPointFormat : values()) {
                if (eCPointFormat.id == i2) {
                    return eCPointFormat.name;
                }
            }
            return "UNDEFINED-EC-POINT-FORMAT(" + i2 + ")";
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ECPointFormatsExtension$CHECPointFormatsProducer.class */
    private static final class CHECPointFormatsProducer implements HandshakeProducer {
        private CHECPointFormatsProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (!clientHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_EC_POINT_FORMATS)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable ec_point_formats extension", new Object[0]);
                    return null;
                }
                return null;
            }
            if (SupportedGroupsExtension.NamedGroupType.NAMED_GROUP_ECDHE.isSupported(clientHandshakeContext.activeCipherSuites)) {
                byte[] bArr = {1, 0};
                clientHandshakeContext.handshakeExtensions.put(SSLExtension.CH_EC_POINT_FORMATS, ECPointFormatsSpec.DEFAULT);
                return bArr;
            }
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Need no ec_point_formats extension", new Object[0]);
                return null;
            }
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ECPointFormatsExtension$CHECPointFormatsConsumer.class */
    private static final class CHECPointFormatsConsumer implements SSLExtension.ExtensionConsumer {
        private CHECPointFormatsConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (!serverHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_EC_POINT_FORMATS)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable ec_point_formats extension", new Object[0]);
                    return;
                }
                return;
            }
            try {
                ECPointFormatsSpec eCPointFormatsSpec = new ECPointFormatsSpec(byteBuffer);
                if (!eCPointFormatsSpec.hasUncompressedFormat()) {
                    throw serverHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Invalid ec_point_formats extension data: peer does not support uncompressed points");
                }
                serverHandshakeContext.handshakeExtensions.put(SSLExtension.CH_EC_POINT_FORMATS, eCPointFormatsSpec);
            } catch (IOException e2) {
                throw serverHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e2);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ECPointFormatsExtension$SHECPointFormatsConsumer.class */
    private static final class SHECPointFormatsConsumer implements SSLExtension.ExtensionConsumer {
        private SHECPointFormatsConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (((ECPointFormatsSpec) clientHandshakeContext.handshakeExtensions.get(SSLExtension.CH_EC_POINT_FORMATS)) == null) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unexpected ec_point_formats extension in ServerHello");
            }
            try {
                ECPointFormatsSpec eCPointFormatsSpec = new ECPointFormatsSpec(byteBuffer);
                if (!eCPointFormatsSpec.hasUncompressedFormat()) {
                    throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Invalid ec_point_formats extension data: peer does not support uncompressed points");
                }
                clientHandshakeContext.handshakeExtensions.put(SSLExtension.CH_EC_POINT_FORMATS, eCPointFormatsSpec);
            } catch (IOException e2) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e2);
            }
        }
    }
}
