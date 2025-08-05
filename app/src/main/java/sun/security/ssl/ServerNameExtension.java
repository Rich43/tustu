package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import javax.net.ssl.SNIHostName;
import javax.net.ssl.SNIMatcher;
import javax.net.ssl.SNIServerName;
import javax.net.ssl.SSLProtocolException;
import sun.security.ssl.SSLExtension;
import sun.security.ssl.SSLHandshake;

/* loaded from: jsse.jar:sun/security/ssl/ServerNameExtension.class */
final class ServerNameExtension {
    static final HandshakeProducer chNetworkProducer = new CHServerNameProducer();
    static final SSLExtension.ExtensionConsumer chOnLoadConsumer = new CHServerNameConsumer();
    static final SSLStringizer chStringizer = new CHServerNamesStringizer();
    static final HandshakeProducer shNetworkProducer = new SHServerNameProducer();
    static final SSLExtension.ExtensionConsumer shOnLoadConsumer = new SHServerNameConsumer();
    static final SSLStringizer shStringizer = new SHServerNamesStringizer();
    static final HandshakeProducer eeNetworkProducer = new EEServerNameProducer();
    static final SSLExtension.ExtensionConsumer eeOnLoadConsumer = new EEServerNameConsumer();

    ServerNameExtension() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/ServerNameExtension$CHServerNamesSpec.class */
    static final class CHServerNamesSpec implements SSLExtension.SSLExtensionSpec {
        static final int NAME_HEADER_LENGTH = 3;
        final List<SNIServerName> serverNames;

        private CHServerNamesSpec(List<SNIServerName> list) {
            this.serverNames = Collections.unmodifiableList(new ArrayList(list));
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v32, types: [javax.net.ssl.SNIHostName] */
        private CHServerNamesSpec(ByteBuffer byteBuffer) throws IOException {
            UnknownServerName unknownServerName;
            if (byteBuffer.remaining() < 2) {
                throw new SSLProtocolException("Invalid server_name extension: insufficient data");
            }
            int int16 = Record.getInt16(byteBuffer);
            if (int16 == 0 || int16 != byteBuffer.remaining()) {
                throw new SSLProtocolException("Invalid server_name extension: incomplete data");
            }
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            while (byteBuffer.hasRemaining()) {
                int int8 = Record.getInt8(byteBuffer);
                byte[] bytes16 = Record.getBytes16(byteBuffer);
                if (int8 == 0) {
                    if (bytes16.length == 0) {
                        throw new SSLProtocolException("Empty HostName in server_name extension");
                    }
                    try {
                        unknownServerName = new SNIHostName(bytes16);
                    } catch (IllegalArgumentException e2) {
                        throw ((SSLProtocolException) new SSLProtocolException("Illegal server name, type=host_name(" + int8 + "), name=" + new String(bytes16, StandardCharsets.UTF_8) + ", value={" + Utilities.toHexString(bytes16) + "}").initCause(e2));
                    }
                } else {
                    try {
                        unknownServerName = new UnknownServerName(int8, bytes16);
                    } catch (IllegalArgumentException e3) {
                        throw ((SSLProtocolException) new SSLProtocolException("Illegal server name, type=(" + int8 + "), value={" + Utilities.toHexString(bytes16) + "}").initCause(e3));
                    }
                }
                if (linkedHashMap.put(Integer.valueOf(unknownServerName.getType()), unknownServerName) != null) {
                    throw new SSLProtocolException("Duplicated server name of type " + unknownServerName.getType());
                }
            }
            this.serverNames = new ArrayList(linkedHashMap.values());
        }

        public String toString() {
            if (this.serverNames == null || this.serverNames.isEmpty()) {
                return "<no server name indicator specified>";
            }
            StringBuilder sb = new StringBuilder(512);
            Iterator<SNIServerName> it = this.serverNames.iterator();
            while (it.hasNext()) {
                sb.append(it.next().toString());
                sb.append("\n");
            }
            return sb.toString();
        }

        /* loaded from: jsse.jar:sun/security/ssl/ServerNameExtension$CHServerNamesSpec$UnknownServerName.class */
        private static class UnknownServerName extends SNIServerName {
            UnknownServerName(int i2, byte[] bArr) {
                super(i2, bArr);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ServerNameExtension$CHServerNamesStringizer.class */
    private static final class CHServerNamesStringizer implements SSLStringizer {
        private CHServerNamesStringizer() {
        }

        @Override // sun.security.ssl.SSLStringizer
        public String toString(ByteBuffer byteBuffer) {
            try {
                return new CHServerNamesSpec(byteBuffer).toString();
            } catch (IOException e2) {
                return e2.getMessage();
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ServerNameExtension$CHServerNameProducer.class */
    private static final class CHServerNameProducer implements HandshakeProducer {
        private CHServerNameProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            List<SNIServerName> requestedServerNames;
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (!clientHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_SERVER_NAME)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.warning("Ignore unavailable server_name extension", new Object[0]);
                    return null;
                }
                return null;
            }
            if (clientHandshakeContext.isResumption && clientHandshakeContext.resumingSession != null) {
                requestedServerNames = clientHandshakeContext.resumingSession.getRequestedServerNames();
            } else {
                requestedServerNames = clientHandshakeContext.sslConfig.serverNames;
            }
            if (requestedServerNames != null && !requestedServerNames.isEmpty()) {
                int length = 0;
                Iterator<SNIServerName> it = requestedServerNames.iterator();
                while (it.hasNext()) {
                    length = length + 3 + it.next().getEncoded().length;
                }
                byte[] bArr = new byte[length + 2];
                ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
                Record.putInt16(byteBufferWrap, length);
                for (SNIServerName sNIServerName : requestedServerNames) {
                    Record.putInt8(byteBufferWrap, sNIServerName.getType());
                    Record.putBytes16(byteBufferWrap, sNIServerName.getEncoded());
                }
                clientHandshakeContext.requestedServerNames = requestedServerNames;
                clientHandshakeContext.handshakeExtensions.put(SSLExtension.CH_SERVER_NAME, new CHServerNamesSpec(requestedServerNames));
                return bArr;
            }
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.warning("Unable to indicate server name", new Object[0]);
                return null;
            }
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ServerNameExtension$CHServerNameConsumer.class */
    private static final class CHServerNameConsumer implements SSLExtension.ExtensionConsumer {
        private CHServerNameConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (!serverHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_SERVER_NAME)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable extension: " + SSLExtension.CH_SERVER_NAME.name, new Object[0]);
                    return;
                }
                return;
            }
            try {
                CHServerNamesSpec cHServerNamesSpec = new CHServerNamesSpec(byteBuffer);
                serverHandshakeContext.handshakeExtensions.put(SSLExtension.CH_SERVER_NAME, cHServerNamesSpec);
                SNIServerName sNIServerNameChooseSni = null;
                if (!serverHandshakeContext.sslConfig.sniMatchers.isEmpty()) {
                    sNIServerNameChooseSni = chooseSni(serverHandshakeContext.sslConfig.sniMatchers, cHServerNamesSpec.serverNames);
                    if (sNIServerNameChooseSni != null) {
                        if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                            SSLLogger.fine("server name indication (" + ((Object) sNIServerNameChooseSni) + ") is accepted", new Object[0]);
                        }
                    } else {
                        throw serverHandshakeContext.conContext.fatal(Alert.UNRECOGNIZED_NAME, "Unrecognized server name indication");
                    }
                } else if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("no server name matchers, ignore server name indication", new Object[0]);
                }
                if (serverHandshakeContext.isResumption && serverHandshakeContext.resumingSession != null && !Objects.equals(sNIServerNameChooseSni, serverHandshakeContext.resumingSession.serverNameIndication)) {
                    serverHandshakeContext.isResumption = false;
                    serverHandshakeContext.resumingSession = null;
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                        SSLLogger.fine("abort session resumption, different server name indication used", new Object[0]);
                    }
                }
                serverHandshakeContext.requestedServerNames = cHServerNamesSpec.serverNames;
                serverHandshakeContext.negotiatedServerName = sNIServerNameChooseSni;
            } catch (IOException e2) {
                throw serverHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e2);
            }
        }

        private static SNIServerName chooseSni(Collection<SNIMatcher> collection, List<SNIServerName> list) {
            if (list != null && !list.isEmpty()) {
                for (SNIMatcher sNIMatcher : collection) {
                    int type = sNIMatcher.getType();
                    Iterator<SNIServerName> it = list.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            SNIServerName next = it.next();
                            if (next.getType() == type) {
                                if (sNIMatcher.matches(next)) {
                                    return next;
                                }
                            }
                        }
                    }
                }
                return null;
            }
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ServerNameExtension$SHServerNamesSpec.class */
    static final class SHServerNamesSpec implements SSLExtension.SSLExtensionSpec {
        static final SHServerNamesSpec DEFAULT = new SHServerNamesSpec();

        private SHServerNamesSpec() {
        }

        private SHServerNamesSpec(ByteBuffer byteBuffer) throws IOException {
            if (byteBuffer.remaining() != 0) {
                throw new SSLProtocolException("Invalid ServerHello server_name extension: not empty");
            }
        }

        public String toString() {
            return "<empty extension_data field>";
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ServerNameExtension$SHServerNamesStringizer.class */
    private static final class SHServerNamesStringizer implements SSLStringizer {
        private SHServerNamesStringizer() {
        }

        @Override // sun.security.ssl.SSLStringizer
        public String toString(ByteBuffer byteBuffer) {
            try {
                return new SHServerNamesSpec(byteBuffer).toString();
            } catch (IOException e2) {
                return e2.getMessage();
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ServerNameExtension$SHServerNameProducer.class */
    private static final class SHServerNameProducer implements HandshakeProducer {
        private SHServerNameProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (((CHServerNamesSpec) serverHandshakeContext.handshakeExtensions.get(SSLExtension.CH_SERVER_NAME)) == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.finest("Ignore unavailable extension: " + SSLExtension.SH_SERVER_NAME.name, new Object[0]);
                    return null;
                }
                return null;
            }
            if (serverHandshakeContext.isResumption || serverHandshakeContext.negotiatedServerName == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.finest("No expected server name indication response", new Object[0]);
                    return null;
                }
                return null;
            }
            serverHandshakeContext.handshakeExtensions.put(SSLExtension.SH_SERVER_NAME, SHServerNamesSpec.DEFAULT);
            return new byte[0];
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ServerNameExtension$SHServerNameConsumer.class */
    private static final class SHServerNameConsumer implements SSLExtension.ExtensionConsumer {
        private SHServerNameConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            CHServerNamesSpec cHServerNamesSpec = (CHServerNamesSpec) clientHandshakeContext.handshakeExtensions.get(SSLExtension.CH_SERVER_NAME);
            if (cHServerNamesSpec == null) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unexpected ServerHello server_name extension");
            }
            if (byteBuffer.remaining() != 0) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Invalid ServerHello server_name extension");
            }
            clientHandshakeContext.handshakeExtensions.put(SSLExtension.SH_SERVER_NAME, SHServerNamesSpec.DEFAULT);
            clientHandshakeContext.negotiatedServerName = cHServerNamesSpec.serverNames.get(0);
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ServerNameExtension$EEServerNameProducer.class */
    private static final class EEServerNameProducer implements HandshakeProducer {
        private EEServerNameProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (((CHServerNamesSpec) serverHandshakeContext.handshakeExtensions.get(SSLExtension.CH_SERVER_NAME)) == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.finest("Ignore unavailable extension: " + SSLExtension.EE_SERVER_NAME.name, new Object[0]);
                    return null;
                }
                return null;
            }
            if (serverHandshakeContext.isResumption || serverHandshakeContext.negotiatedServerName == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.finest("No expected server name indication response", new Object[0]);
                    return null;
                }
                return null;
            }
            serverHandshakeContext.handshakeExtensions.put(SSLExtension.EE_SERVER_NAME, SHServerNamesSpec.DEFAULT);
            return new byte[0];
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ServerNameExtension$EEServerNameConsumer.class */
    private static final class EEServerNameConsumer implements SSLExtension.ExtensionConsumer {
        private EEServerNameConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            CHServerNamesSpec cHServerNamesSpec = (CHServerNamesSpec) clientHandshakeContext.handshakeExtensions.get(SSLExtension.CH_SERVER_NAME);
            if (cHServerNamesSpec == null) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unexpected EncryptedExtensions server_name extension");
            }
            if (byteBuffer.remaining() != 0) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Invalid EncryptedExtensions server_name extension");
            }
            clientHandshakeContext.handshakeExtensions.put(SSLExtension.EE_SERVER_NAME, SHServerNamesSpec.DEFAULT);
            clientHandshakeContext.negotiatedServerName = cHServerNamesSpec.serverNames.get(0);
        }
    }
}
