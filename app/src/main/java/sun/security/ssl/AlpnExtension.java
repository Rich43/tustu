package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.AccessController;
import java.security.Security;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLProtocolException;
import javax.net.ssl.SSLSocket;
import sun.security.ssl.SSLExtension;
import sun.security.ssl.SSLHandshake;

/* loaded from: jsse.jar:sun/security/ssl/AlpnExtension.class */
final class AlpnExtension {
    static final HandshakeProducer chNetworkProducer = new CHAlpnProducer();
    static final SSLExtension.ExtensionConsumer chOnLoadConsumer = new CHAlpnConsumer();
    static final HandshakeAbsence chOnLoadAbsence = new CHAlpnAbsence();
    static final HandshakeProducer shNetworkProducer = new SHAlpnProducer();
    static final SSLExtension.ExtensionConsumer shOnLoadConsumer = new SHAlpnConsumer();
    static final HandshakeAbsence shOnLoadAbsence = new SHAlpnAbsence();
    static final HandshakeProducer eeNetworkProducer = new SHAlpnProducer();
    static final SSLExtension.ExtensionConsumer eeOnLoadConsumer = new SHAlpnConsumer();
    static final HandshakeAbsence eeOnLoadAbsence = new SHAlpnAbsence();
    static final SSLStringizer alpnStringizer = new AlpnStringizer();
    static final Charset alpnCharset;

    AlpnExtension() {
    }

    static {
        String str = (String) AccessController.doPrivileged(() -> {
            return Security.getProperty("jdk.tls.alpnCharset");
        });
        if (str == null || str.length() == 0) {
            str = "ISO_8859_1";
        }
        alpnCharset = Charset.forName(str);
    }

    /* loaded from: jsse.jar:sun/security/ssl/AlpnExtension$AlpnSpec.class */
    static final class AlpnSpec implements SSLExtension.SSLExtensionSpec {
        final List<String> applicationProtocols;

        private AlpnSpec(String[] strArr) {
            this.applicationProtocols = Collections.unmodifiableList(Arrays.asList(strArr));
        }

        private AlpnSpec(ByteBuffer byteBuffer) throws IOException {
            if (byteBuffer.remaining() < 2) {
                throw new SSLProtocolException("Invalid application_layer_protocol_negotiation: insufficient data (length=" + byteBuffer.remaining() + ")");
            }
            int int16 = Record.getInt16(byteBuffer);
            if (int16 < 2 || int16 != byteBuffer.remaining()) {
                throw new SSLProtocolException("Invalid application_layer_protocol_negotiation: incorrect list length (length=" + int16 + ")");
            }
            LinkedList linkedList = new LinkedList();
            while (byteBuffer.hasRemaining()) {
                byte[] bytes8 = Record.getBytes8(byteBuffer);
                if (bytes8.length == 0) {
                    throw new SSLProtocolException("Invalid application_layer_protocol_negotiation extension: empty application protocol name");
                }
                linkedList.add(new String(bytes8, AlpnExtension.alpnCharset));
            }
            this.applicationProtocols = Collections.unmodifiableList(linkedList);
        }

        public String toString() {
            return this.applicationProtocols.toString();
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/AlpnExtension$AlpnStringizer.class */
    private static final class AlpnStringizer implements SSLStringizer {
        private AlpnStringizer() {
        }

        @Override // sun.security.ssl.SSLStringizer
        public String toString(ByteBuffer byteBuffer) {
            try {
                return new AlpnSpec(byteBuffer).toString();
            } catch (IOException e2) {
                return e2.getMessage();
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/AlpnExtension$CHAlpnProducer.class */
    private static final class CHAlpnProducer implements HandshakeProducer {
        static final int MAX_AP_LENGTH = 255;
        static final int MAX_AP_LIST_LENGTH = 65535;

        private CHAlpnProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (!clientHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_ALPN)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.info("Ignore client unavailable extension: " + SSLExtension.CH_ALPN.name, new Object[0]);
                }
                clientHandshakeContext.applicationProtocol = "";
                clientHandshakeContext.conContext.applicationProtocol = "";
                return null;
            }
            String[] strArr = clientHandshakeContext.sslConfig.applicationProtocols;
            if (strArr == null || strArr.length == 0) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.info("No available application protocols", new Object[0]);
                    return null;
                }
                return null;
            }
            int i2 = 0;
            for (String str : strArr) {
                int length = str.getBytes(AlpnExtension.alpnCharset).length;
                if (length == 0) {
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                        SSLLogger.severe("Application protocol name cannot be empty", new Object[0]);
                    }
                    throw clientHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Application protocol name cannot be empty");
                }
                if (length <= 255) {
                    i2 += length + 1;
                    if (i2 > 65535) {
                        if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                            SSLLogger.severe("The configured application protocols (" + Arrays.toString(strArr) + ") exceed the size limit (65535 bytes)", new Object[0]);
                        }
                        throw clientHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "The configured application protocols (" + Arrays.toString(strArr) + ") exceed the size limit (65535 bytes)");
                    }
                } else {
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                        SSLLogger.severe("Application protocol name (" + str + ") exceeds the size limit (255 bytes)", new Object[0]);
                    }
                    throw clientHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Application protocol name (" + str + ") exceeds the size limit (255 bytes)");
                }
            }
            byte[] bArr = new byte[i2 + 2];
            ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
            Record.putInt16(byteBufferWrap, i2);
            for (String str2 : strArr) {
                Record.putBytes8(byteBufferWrap, str2.getBytes(AlpnExtension.alpnCharset));
            }
            clientHandshakeContext.handshakeExtensions.put(SSLExtension.CH_ALPN, new AlpnSpec(clientHandshakeContext.sslConfig.applicationProtocols));
            return bArr;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/AlpnExtension$CHAlpnConsumer.class */
    private static final class CHAlpnConsumer implements SSLExtension.ExtensionConsumer {
        private CHAlpnConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            boolean z2;
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (!serverHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_ALPN)) {
                serverHandshakeContext.applicationProtocol = "";
                serverHandshakeContext.conContext.applicationProtocol = "";
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.info("Ignore server unavailable extension: " + SSLExtension.CH_ALPN.name, new Object[0]);
                    return;
                }
                return;
            }
            if (serverHandshakeContext.conContext.transport instanceof SSLEngine) {
                z2 = serverHandshakeContext.sslConfig.engineAPSelector == null;
            } else {
                z2 = serverHandshakeContext.sslConfig.socketAPSelector == null;
            }
            boolean z3 = serverHandshakeContext.sslConfig.applicationProtocols == null || serverHandshakeContext.sslConfig.applicationProtocols.length == 0;
            if (z2 && z3) {
                serverHandshakeContext.applicationProtocol = "";
                serverHandshakeContext.conContext.applicationProtocol = "";
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore server unenabled extension: " + SSLExtension.CH_ALPN.name, new Object[0]);
                    return;
                }
                return;
            }
            try {
                AlpnSpec alpnSpec = new AlpnSpec(byteBuffer);
                if (z2) {
                    List<String> list = alpnSpec.applicationProtocols;
                    boolean z4 = false;
                    String[] strArr = serverHandshakeContext.sslConfig.applicationProtocols;
                    int length = strArr.length;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= length) {
                            break;
                        }
                        String str = strArr[i2];
                        if (!list.contains(str)) {
                            i2++;
                        } else {
                            serverHandshakeContext.applicationProtocol = str;
                            serverHandshakeContext.conContext.applicationProtocol = str;
                            z4 = true;
                            break;
                        }
                    }
                    if (!z4) {
                        throw serverHandshakeContext.conContext.fatal(Alert.NO_APPLICATION_PROTOCOL, "No matching application layer protocol values");
                    }
                }
                serverHandshakeContext.handshakeExtensions.put(SSLExtension.CH_ALPN, alpnSpec);
            } catch (IOException e2) {
                throw serverHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e2);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/AlpnExtension$CHAlpnAbsence.class */
    private static final class CHAlpnAbsence implements HandshakeAbsence {
        private CHAlpnAbsence() {
        }

        @Override // sun.security.ssl.HandshakeAbsence
        public void absent(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            serverHandshakeContext.applicationProtocol = "";
            serverHandshakeContext.conContext.applicationProtocol = "";
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/AlpnExtension$SHAlpnProducer.class */
    private static final class SHAlpnProducer implements HandshakeProducer {
        private SHAlpnProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            AlpnSpec alpnSpec = (AlpnSpec) serverHandshakeContext.handshakeExtensions.get(SSLExtension.CH_ALPN);
            if (alpnSpec == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable extension: " + SSLExtension.SH_ALPN.name, new Object[0]);
                }
                serverHandshakeContext.applicationProtocol = "";
                serverHandshakeContext.conContext.applicationProtocol = "";
                return null;
            }
            List<String> list = alpnSpec.applicationProtocols;
            if (serverHandshakeContext.conContext.transport instanceof SSLEngine) {
                if (serverHandshakeContext.sslConfig.engineAPSelector != null) {
                    serverHandshakeContext.applicationProtocol = serverHandshakeContext.sslConfig.engineAPSelector.apply((SSLEngine) serverHandshakeContext.conContext.transport, list);
                    if (serverHandshakeContext.applicationProtocol == null || (!serverHandshakeContext.applicationProtocol.isEmpty() && !list.contains(serverHandshakeContext.applicationProtocol))) {
                        throw serverHandshakeContext.conContext.fatal(Alert.NO_APPLICATION_PROTOCOL, "No matching application layer protocol values");
                    }
                }
            } else if (serverHandshakeContext.sslConfig.socketAPSelector != null) {
                serverHandshakeContext.applicationProtocol = serverHandshakeContext.sslConfig.socketAPSelector.apply((SSLSocket) serverHandshakeContext.conContext.transport, list);
                if (serverHandshakeContext.applicationProtocol == null || (!serverHandshakeContext.applicationProtocol.isEmpty() && !list.contains(serverHandshakeContext.applicationProtocol))) {
                    throw serverHandshakeContext.conContext.fatal(Alert.NO_APPLICATION_PROTOCOL, "No matching application layer protocol values");
                }
            }
            if (serverHandshakeContext.applicationProtocol == null || serverHandshakeContext.applicationProtocol.isEmpty()) {
                serverHandshakeContext.applicationProtocol = "";
                serverHandshakeContext.conContext.applicationProtocol = "";
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.warning("Ignore, no negotiated application layer protocol", new Object[0]);
                    return null;
                }
                return null;
            }
            byte[] bytes = serverHandshakeContext.applicationProtocol.getBytes(AlpnExtension.alpnCharset);
            int length = bytes.length + 1;
            byte[] bArr = new byte[length + 2];
            ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
            Record.putInt16(byteBufferWrap, length);
            Record.putBytes8(byteBufferWrap, bytes);
            serverHandshakeContext.conContext.applicationProtocol = serverHandshakeContext.applicationProtocol;
            serverHandshakeContext.handshakeExtensions.remove(SSLExtension.CH_ALPN);
            return bArr;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/AlpnExtension$SHAlpnConsumer.class */
    private static final class SHAlpnConsumer implements SSLExtension.ExtensionConsumer {
        private SHAlpnConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            AlpnSpec alpnSpec = (AlpnSpec) clientHandshakeContext.handshakeExtensions.get(SSLExtension.CH_ALPN);
            if (alpnSpec == null || alpnSpec.applicationProtocols == null || alpnSpec.applicationProtocols.isEmpty()) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unexpected " + SSLExtension.CH_ALPN.name + " extension");
            }
            try {
                AlpnSpec alpnSpec2 = new AlpnSpec(byteBuffer);
                if (alpnSpec2.applicationProtocols.size() != 1) {
                    throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Invalid " + SSLExtension.CH_ALPN.name + " extension: Only one application protocol name is allowed in ServerHello message");
                }
                if (!alpnSpec.applicationProtocols.containsAll(alpnSpec2.applicationProtocols)) {
                    throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Invalid " + SSLExtension.CH_ALPN.name + " extension: Only client specified application protocol is allowed in ServerHello message");
                }
                clientHandshakeContext.applicationProtocol = alpnSpec2.applicationProtocols.get(0);
                clientHandshakeContext.conContext.applicationProtocol = clientHandshakeContext.applicationProtocol;
                clientHandshakeContext.handshakeExtensions.remove(SSLExtension.CH_ALPN);
            } catch (IOException e2) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e2);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/AlpnExtension$SHAlpnAbsence.class */
    private static final class SHAlpnAbsence implements HandshakeAbsence {
        private SHAlpnAbsence() {
        }

        @Override // sun.security.ssl.HandshakeAbsence
        public void absent(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            clientHandshakeContext.applicationProtocol = "";
            clientHandshakeContext.conContext.applicationProtocol = "";
        }
    }
}
