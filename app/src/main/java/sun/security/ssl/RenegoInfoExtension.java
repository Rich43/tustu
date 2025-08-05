package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import javax.net.ssl.SSLProtocolException;
import sun.security.ssl.ClientHello;
import sun.security.ssl.SSLExtension;
import sun.security.ssl.SSLHandshake;
import sun.security.util.ByteArrays;

/* loaded from: jsse.jar:sun/security/ssl/RenegoInfoExtension.class */
final class RenegoInfoExtension {
    static final HandshakeProducer chNetworkProducer = new CHRenegotiationInfoProducer();
    static final SSLExtension.ExtensionConsumer chOnLoadConsumer = new CHRenegotiationInfoConsumer();
    static final HandshakeAbsence chOnLoadAbsence = new CHRenegotiationInfoAbsence();
    static final HandshakeProducer shNetworkProducer = new SHRenegotiationInfoProducer();
    static final SSLExtension.ExtensionConsumer shOnLoadConsumer = new SHRenegotiationInfoConsumer();
    static final HandshakeAbsence shOnLoadAbsence = new SHRenegotiationInfoAbsence();
    static final SSLStringizer rniStringizer = new RenegotiationInfoStringizer();

    RenegoInfoExtension() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/RenegoInfoExtension$RenegotiationInfoSpec.class */
    static final class RenegotiationInfoSpec implements SSLExtension.SSLExtensionSpec {
        static final RenegotiationInfoSpec NOMINAL = new RenegotiationInfoSpec(new byte[0]);
        private final byte[] renegotiatedConnection;

        private RenegotiationInfoSpec(byte[] bArr) {
            this.renegotiatedConnection = Arrays.copyOf(bArr, bArr.length);
        }

        private RenegotiationInfoSpec(ByteBuffer byteBuffer) throws IOException {
            if (!byteBuffer.hasRemaining() || byteBuffer.remaining() < 1) {
                throw new SSLProtocolException("Invalid renegotiation_info extension data: insufficient data");
            }
            this.renegotiatedConnection = Record.getBytes8(byteBuffer);
        }

        public String toString() {
            MessageFormat messageFormat = new MessageFormat("\"renegotiated connection\": '['{0}']'", Locale.ENGLISH);
            if (this.renegotiatedConnection.length == 0) {
                return messageFormat.format(new Object[]{"<no renegotiated connection>"});
            }
            return messageFormat.format(new Object[]{Utilities.toHexString(this.renegotiatedConnection)});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/RenegoInfoExtension$RenegotiationInfoStringizer.class */
    private static final class RenegotiationInfoStringizer implements SSLStringizer {
        private RenegotiationInfoStringizer() {
        }

        @Override // sun.security.ssl.SSLStringizer
        public String toString(ByteBuffer byteBuffer) {
            try {
                return new RenegotiationInfoSpec(byteBuffer).toString();
            } catch (IOException e2) {
                return e2.getMessage();
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/RenegoInfoExtension$CHRenegotiationInfoProducer.class */
    private static final class CHRenegotiationInfoProducer implements HandshakeProducer {
        private CHRenegotiationInfoProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (!clientHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_RENEGOTIATION_INFO)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable renegotiation_info extension", new Object[0]);
                    return null;
                }
                return null;
            }
            if (!clientHandshakeContext.conContext.isNegotiated) {
                if (clientHandshakeContext.activeCipherSuites.contains(CipherSuite.TLS_EMPTY_RENEGOTIATION_INFO_SCSV)) {
                    return null;
                }
                byte[] bArr = {0};
                clientHandshakeContext.handshakeExtensions.put(SSLExtension.CH_RENEGOTIATION_INFO, RenegotiationInfoSpec.NOMINAL);
                return bArr;
            }
            if (clientHandshakeContext.conContext.secureRenegotiation) {
                byte[] bArr2 = new byte[clientHandshakeContext.conContext.clientVerifyData.length + 1];
                Record.putBytes8(ByteBuffer.wrap(bArr2), clientHandshakeContext.conContext.clientVerifyData);
                clientHandshakeContext.handshakeExtensions.put(SSLExtension.CH_RENEGOTIATION_INFO, RenegotiationInfoSpec.NOMINAL);
                return bArr2;
            }
            if (HandshakeContext.allowUnsafeRenegotiation) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.warning("Using insecure renegotiation", new Object[0]);
                    return null;
                }
                return null;
            }
            throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "insecure renegotiation is not allowed");
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/RenegoInfoExtension$CHRenegotiationInfoConsumer.class */
    private static final class CHRenegotiationInfoConsumer implements SSLExtension.ExtensionConsumer {
        private CHRenegotiationInfoConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (!serverHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_RENEGOTIATION_INFO)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable extension: " + SSLExtension.CH_RENEGOTIATION_INFO.name, new Object[0]);
                    return;
                }
                return;
            }
            try {
                RenegotiationInfoSpec renegotiationInfoSpec = new RenegotiationInfoSpec(byteBuffer);
                if (!serverHandshakeContext.conContext.isNegotiated) {
                    if (renegotiationInfoSpec.renegotiatedConnection.length != 0) {
                        throw serverHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Invalid renegotiation_info extension data: not empty");
                    }
                    serverHandshakeContext.conContext.secureRenegotiation = true;
                } else {
                    if (!serverHandshakeContext.conContext.secureRenegotiation) {
                        throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "The renegotiation_info is present in a insecure renegotiation");
                    }
                    if (!MessageDigest.isEqual(serverHandshakeContext.conContext.clientVerifyData, renegotiationInfoSpec.renegotiatedConnection)) {
                        throw serverHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Invalid renegotiation_info extension data: incorrect verify data in ClientHello");
                    }
                }
                serverHandshakeContext.handshakeExtensions.put(SSLExtension.CH_RENEGOTIATION_INFO, RenegotiationInfoSpec.NOMINAL);
            } catch (IOException e2) {
                throw serverHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e2);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/RenegoInfoExtension$CHRenegotiationInfoAbsence.class */
    private static final class CHRenegotiationInfoAbsence implements HandshakeAbsence {
        private CHRenegotiationInfoAbsence() {
        }

        @Override // sun.security.ssl.HandshakeAbsence
        public void absent(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            ClientHello.ClientHelloMessage clientHelloMessage = (ClientHello.ClientHelloMessage) handshakeMessage;
            if (!serverHandshakeContext.conContext.isNegotiated) {
                for (int i2 : clientHelloMessage.cipherSuiteIds) {
                    if (i2 == CipherSuite.TLS_EMPTY_RENEGOTIATION_INFO_SCSV.id) {
                        if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                            SSLLogger.finest("Safe renegotiation, using the SCSV signgling", new Object[0]);
                        }
                        serverHandshakeContext.conContext.secureRenegotiation = true;
                        return;
                    }
                }
                if (!HandshakeContext.allowLegacyHelloMessages) {
                    throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Failed to negotiate the use of secure renegotiation");
                }
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.warning("Warning: No renegotiation indication in ClientHello, allow legacy ClientHello", new Object[0]);
                }
                serverHandshakeContext.conContext.secureRenegotiation = false;
                return;
            }
            if (serverHandshakeContext.conContext.secureRenegotiation) {
                throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Inconsistent secure renegotiation indication");
            }
            if (HandshakeContext.allowUnsafeRenegotiation) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.warning("Using insecure renegotiation", new Object[0]);
                    return;
                }
                return;
            }
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Terminate insecure renegotiation", new Object[0]);
            }
            throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Unsafe renegotiation is not allowed");
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/RenegoInfoExtension$SHRenegotiationInfoProducer.class */
    private static final class SHRenegotiationInfoProducer implements HandshakeProducer {
        private SHRenegotiationInfoProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (((RenegotiationInfoSpec) serverHandshakeContext.handshakeExtensions.get(SSLExtension.CH_RENEGOTIATION_INFO)) == null && !serverHandshakeContext.conContext.secureRenegotiation) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.finest("Ignore unavailable renegotiation_info extension", new Object[0]);
                    return null;
                }
                return null;
            }
            if (!serverHandshakeContext.conContext.secureRenegotiation) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.finest("No secure renegotiation has been negotiated", new Object[0]);
                    return null;
                }
                return null;
            }
            if (!serverHandshakeContext.conContext.isNegotiated) {
                byte[] bArr = {0};
                serverHandshakeContext.handshakeExtensions.put(SSLExtension.SH_RENEGOTIATION_INFO, RenegotiationInfoSpec.NOMINAL);
                return bArr;
            }
            int length = serverHandshakeContext.conContext.clientVerifyData.length + serverHandshakeContext.conContext.serverVerifyData.length;
            byte[] bArr2 = new byte[length + 1];
            ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr2);
            Record.putInt8(byteBufferWrap, length);
            byteBufferWrap.put(serverHandshakeContext.conContext.clientVerifyData);
            byteBufferWrap.put(serverHandshakeContext.conContext.serverVerifyData);
            serverHandshakeContext.handshakeExtensions.put(SSLExtension.SH_RENEGOTIATION_INFO, RenegotiationInfoSpec.NOMINAL);
            return bArr2;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/RenegoInfoExtension$SHRenegotiationInfoConsumer.class */
    private static final class SHRenegotiationInfoConsumer implements SSLExtension.ExtensionConsumer {
        private SHRenegotiationInfoConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (((RenegotiationInfoSpec) clientHandshakeContext.handshakeExtensions.get(SSLExtension.CH_RENEGOTIATION_INFO)) == null && !clientHandshakeContext.activeCipherSuites.contains(CipherSuite.TLS_EMPTY_RENEGOTIATION_INFO_SCSV)) {
                throw clientHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Missing renegotiation_info and SCSV detected in ClientHello");
            }
            try {
                RenegotiationInfoSpec renegotiationInfoSpec = new RenegotiationInfoSpec(byteBuffer);
                if (!clientHandshakeContext.conContext.isNegotiated) {
                    if (renegotiationInfoSpec.renegotiatedConnection.length != 0) {
                        throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Invalid renegotiation_info in ServerHello: not empty renegotiated_connection");
                    }
                    clientHandshakeContext.conContext.secureRenegotiation = true;
                } else {
                    int length = clientHandshakeContext.conContext.clientVerifyData.length + clientHandshakeContext.conContext.serverVerifyData.length;
                    if (renegotiationInfoSpec.renegotiatedConnection.length != length) {
                        throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Invalid renegotiation_info in ServerHello: invalid renegotiated_connection length (" + renegotiationInfoSpec.renegotiatedConnection.length + ")");
                    }
                    byte[] bArr = clientHandshakeContext.conContext.clientVerifyData;
                    if (!ByteArrays.isEqual(renegotiationInfoSpec.renegotiatedConnection, 0, bArr.length, bArr, 0, bArr.length)) {
                        throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Invalid renegotiation_info in ServerHello: unmatched client_verify_data value");
                    }
                    byte[] bArr2 = clientHandshakeContext.conContext.serverVerifyData;
                    if (!ByteArrays.isEqual(renegotiationInfoSpec.renegotiatedConnection, bArr.length, length, bArr2, 0, bArr2.length)) {
                        throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Invalid renegotiation_info in ServerHello: unmatched server_verify_data value");
                    }
                }
                clientHandshakeContext.handshakeExtensions.put(SSLExtension.SH_RENEGOTIATION_INFO, RenegotiationInfoSpec.NOMINAL);
            } catch (IOException e2) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e2);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/RenegoInfoExtension$SHRenegotiationInfoAbsence.class */
    private static final class SHRenegotiationInfoAbsence implements HandshakeAbsence {
        private SHRenegotiationInfoAbsence() {
        }

        @Override // sun.security.ssl.HandshakeAbsence
        public void absent(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (((RenegotiationInfoSpec) clientHandshakeContext.handshakeExtensions.get(SSLExtension.CH_RENEGOTIATION_INFO)) == null && !clientHandshakeContext.activeCipherSuites.contains(CipherSuite.TLS_EMPTY_RENEGOTIATION_INFO_SCSV)) {
                throw clientHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Missing renegotiation_info and SCSV detected in ClientHello");
            }
            if (!clientHandshakeContext.conContext.isNegotiated) {
                if (!HandshakeContext.allowLegacyHelloMessages) {
                    throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Failed to negotiate the use of secure renegotiation");
                }
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.warning("Warning: No renegotiation indication in ServerHello, allow legacy ServerHello", new Object[0]);
                }
                clientHandshakeContext.conContext.secureRenegotiation = false;
                return;
            }
            if (clientHandshakeContext.conContext.secureRenegotiation) {
                throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Inconsistent secure renegotiation indication");
            }
            if (HandshakeContext.allowUnsafeRenegotiation) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.warning("Using insecure renegotiation", new Object[0]);
                    return;
                }
                return;
            }
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Terminate insecure renegotiation", new Object[0]);
            }
            throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Unsafe renegotiation is not allowed");
        }
    }
}
