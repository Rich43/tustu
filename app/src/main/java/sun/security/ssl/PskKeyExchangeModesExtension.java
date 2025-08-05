package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.MessageFormat;
import java.util.Locale;
import javax.net.ssl.SSLProtocolException;
import sun.security.ssl.SSLExtension;
import sun.security.ssl.SSLHandshake;

/* loaded from: jsse.jar:sun/security/ssl/PskKeyExchangeModesExtension.class */
final class PskKeyExchangeModesExtension {
    static final HandshakeProducer chNetworkProducer = new PskKeyExchangeModesProducer();
    static final SSLExtension.ExtensionConsumer chOnLoadConsumer = new PskKeyExchangeModesConsumer();
    static final HandshakeAbsence chOnLoadAbsence = new PskKeyExchangeModesOnLoadAbsence();
    static final HandshakeAbsence chOnTradeAbsence = new PskKeyExchangeModesOnTradeAbsence();
    static final SSLStringizer pkemStringizer = new PskKeyExchangeModesStringizer();

    PskKeyExchangeModesExtension() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/PskKeyExchangeModesExtension$PskKeyExchangeMode.class */
    enum PskKeyExchangeMode {
        PSK_KE((byte) 0, "psk_ke"),
        PSK_DHE_KE((byte) 1, "psk_dhe_ke");

        final byte id;
        final String name;

        PskKeyExchangeMode(byte b2, String str) {
            this.id = b2;
            this.name = str;
        }

        static PskKeyExchangeMode valueOf(byte b2) {
            for (PskKeyExchangeMode pskKeyExchangeMode : values()) {
                if (pskKeyExchangeMode.id == b2) {
                    return pskKeyExchangeMode;
                }
            }
            return null;
        }

        static String nameOf(byte b2) {
            for (PskKeyExchangeMode pskKeyExchangeMode : values()) {
                if (pskKeyExchangeMode.id == b2) {
                    return pskKeyExchangeMode.name;
                }
            }
            return "<UNKNOWN PskKeyExchangeMode TYPE: " + (b2 & 255) + ">";
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/PskKeyExchangeModesExtension$PskKeyExchangeModesSpec.class */
    static final class PskKeyExchangeModesSpec implements SSLExtension.SSLExtensionSpec {
        private static final PskKeyExchangeModesSpec DEFAULT = new PskKeyExchangeModesSpec(new byte[]{PskKeyExchangeMode.PSK_DHE_KE.id});
        final byte[] modes;

        PskKeyExchangeModesSpec(byte[] bArr) {
            this.modes = bArr;
        }

        PskKeyExchangeModesSpec(ByteBuffer byteBuffer) throws IOException {
            if (byteBuffer.remaining() < 2) {
                throw new SSLProtocolException("Invalid psk_key_exchange_modes extension: insufficient data");
            }
            this.modes = Record.getBytes8(byteBuffer);
        }

        boolean contains(PskKeyExchangeMode pskKeyExchangeMode) {
            if (this.modes != null) {
                for (byte b2 : this.modes) {
                    if (pskKeyExchangeMode.id == b2) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        }

        public String toString() {
            MessageFormat messageFormat = new MessageFormat("\"ke_modes\": '['{0}']'", Locale.ENGLISH);
            if (this.modes == null || this.modes.length == 0) {
                return messageFormat.format(new Object[]{"<no PSK key exchange modes specified>"});
            }
            StringBuilder sb = new StringBuilder(64);
            boolean z2 = true;
            for (byte b2 : this.modes) {
                if (z2) {
                    z2 = false;
                } else {
                    sb.append(", ");
                }
                sb.append(PskKeyExchangeMode.nameOf(b2));
            }
            return messageFormat.format(new Object[]{sb.toString()});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/PskKeyExchangeModesExtension$PskKeyExchangeModesStringizer.class */
    private static final class PskKeyExchangeModesStringizer implements SSLStringizer {
        private PskKeyExchangeModesStringizer() {
        }

        @Override // sun.security.ssl.SSLStringizer
        public String toString(ByteBuffer byteBuffer) {
            try {
                return new PskKeyExchangeModesSpec(byteBuffer).toString();
            } catch (IOException e2) {
                return e2.getMessage();
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/PskKeyExchangeModesExtension$PskKeyExchangeModesConsumer.class */
    private static final class PskKeyExchangeModesConsumer implements SSLExtension.ExtensionConsumer {
        private PskKeyExchangeModesConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (!serverHandshakeContext.sslConfig.isAvailable(SSLExtension.PSK_KEY_EXCHANGE_MODES)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable psk_key_exchange_modes extension", new Object[0]);
                }
                if (serverHandshakeContext.isResumption && serverHandshakeContext.resumingSession != null) {
                    serverHandshakeContext.isResumption = false;
                    serverHandshakeContext.resumingSession = null;
                    return;
                }
                return;
            }
            try {
                PskKeyExchangeModesSpec pskKeyExchangeModesSpec = new PskKeyExchangeModesSpec(byteBuffer);
                serverHandshakeContext.handshakeExtensions.put(SSLExtension.PSK_KEY_EXCHANGE_MODES, pskKeyExchangeModesSpec);
                if (serverHandshakeContext.isResumption && !pskKeyExchangeModesSpec.contains(PskKeyExchangeMode.PSK_DHE_KE)) {
                    serverHandshakeContext.isResumption = false;
                    serverHandshakeContext.resumingSession = null;
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                        SSLLogger.fine("abort session resumption, no supported psk_dhe_ke PSK key exchange mode", new Object[0]);
                    }
                }
            } catch (IOException e2) {
                throw serverHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e2);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/PskKeyExchangeModesExtension$PskKeyExchangeModesProducer.class */
    private static final class PskKeyExchangeModesProducer implements HandshakeProducer {
        private PskKeyExchangeModesProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (!clientHandshakeContext.sslConfig.isAvailable(SSLExtension.PSK_KEY_EXCHANGE_MODES)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.warning("Ignore unavailable psk_key_exchange_modes extension", new Object[0]);
                    return null;
                }
                return null;
            }
            byte[] bArr = {1, 1};
            clientHandshakeContext.handshakeExtensions.put(SSLExtension.PSK_KEY_EXCHANGE_MODES, PskKeyExchangeModesSpec.DEFAULT);
            return bArr;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/PskKeyExchangeModesExtension$PskKeyExchangeModesOnLoadAbsence.class */
    private static final class PskKeyExchangeModesOnLoadAbsence implements HandshakeAbsence {
        private PskKeyExchangeModesOnLoadAbsence() {
        }

        @Override // sun.security.ssl.HandshakeAbsence
        public void absent(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (serverHandshakeContext.isResumption) {
                serverHandshakeContext.isResumption = false;
                serverHandshakeContext.resumingSession = null;
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("abort session resumption, no supported psk_dhe_ke PSK key exchange mode", new Object[0]);
                }
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/PskKeyExchangeModesExtension$PskKeyExchangeModesOnTradeAbsence.class */
    private static final class PskKeyExchangeModesOnTradeAbsence implements HandshakeAbsence {
        private PskKeyExchangeModesOnTradeAbsence() {
        }

        @Override // sun.security.ssl.HandshakeAbsence
        public void absent(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (serverHandshakeContext.handshakeExtensions.get(SSLExtension.CH_PRE_SHARED_KEY) != null) {
                throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "pre_shared_key key extension is offered without a psk_key_exchange_modes extension");
            }
        }
    }
}
