package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLProtocolException;
import javax.security.auth.Subject;
import sun.security.ssl.CipherSuite;
import sun.security.ssl.SSLHandshake;
import sun.security.ssl.SupportedVersionsExtension;

/* loaded from: jsse.jar:sun/security/ssl/ClientHello.class */
final class ClientHello {
    static final SSLProducer kickstartProducer = new ClientHelloKickstartProducer();
    static final SSLConsumer handshakeConsumer = new ClientHelloConsumer();
    static final HandshakeProducer handshakeProducer = new ClientHelloProducer();
    private static final HandshakeConsumer t12HandshakeConsumer = new T12ClientHelloConsumer();
    private static final HandshakeConsumer t13HandshakeConsumer = new T13ClientHelloConsumer();

    ClientHello() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/ClientHello$ClientHelloMessage.class */
    static final class ClientHelloMessage extends SSLHandshake.HandshakeMessage {
        final int clientVersion;
        final RandomCookie clientRandom;
        final SessionId sessionId;
        final int[] cipherSuiteIds;
        final List<CipherSuite> cipherSuites;
        final byte[] compressionMethod;
        final SSLExtensions extensions;
        private static final byte[] NULL_COMPRESSION = {0};

        ClientHelloMessage(HandshakeContext handshakeContext, int i2, SessionId sessionId, List<CipherSuite> list, SecureRandom secureRandom) {
            super(handshakeContext);
            this.clientVersion = i2;
            this.clientRandom = new RandomCookie(secureRandom);
            this.sessionId = sessionId;
            this.cipherSuites = list;
            this.cipherSuiteIds = getCipherSuiteIds(list);
            this.extensions = new SSLExtensions(this);
            this.compressionMethod = NULL_COMPRESSION;
        }

        static void readPartial(TransportContext transportContext, ByteBuffer byteBuffer) throws IOException {
            Record.getInt16(byteBuffer);
            new RandomCookie(byteBuffer);
            Record.getBytes8(byteBuffer);
            Record.getBytes16(byteBuffer);
            Record.getBytes8(byteBuffer);
            if (byteBuffer.remaining() >= 2) {
                int int16 = Record.getInt16(byteBuffer);
                while (int16 > 0) {
                    int int162 = Record.getInt16(byteBuffer);
                    int int163 = Record.getInt16(byteBuffer);
                    int16 -= int163 + 4;
                    if (int162 == SSLExtension.CH_PRE_SHARED_KEY.id) {
                        if (int16 > 0) {
                            throw transportContext.fatal(Alert.ILLEGAL_PARAMETER, "pre_shared_key extension is not last");
                        }
                        Record.getBytes16(byteBuffer);
                        return;
                    }
                    byteBuffer.position(byteBuffer.position() + int163);
                }
            }
        }

        ClientHelloMessage(HandshakeContext handshakeContext, ByteBuffer byteBuffer, SSLExtension[] sSLExtensionArr) throws IOException {
            super(handshakeContext);
            this.clientVersion = ((byteBuffer.get() & 255) << 8) | (byteBuffer.get() & 255);
            this.clientRandom = new RandomCookie(byteBuffer);
            this.sessionId = new SessionId(Record.getBytes8(byteBuffer));
            try {
                this.sessionId.checkLength(this.clientVersion);
                byte[] bytes16 = Record.getBytes16(byteBuffer);
                if (bytes16.length == 0 || (bytes16.length & 1) != 0) {
                    throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Invalid ClientHello message");
                }
                this.cipherSuiteIds = new int[bytes16.length >> 1];
                int i2 = 0;
                int i3 = 0;
                while (i2 < bytes16.length) {
                    int i4 = i2;
                    int i5 = i2 + 1;
                    this.cipherSuiteIds[i3] = ((bytes16[i4] & 255) << 8) | (bytes16[i5] & 255);
                    i2 = i5 + 1;
                    i3++;
                }
                this.cipherSuites = getCipherSuites(this.cipherSuiteIds);
                this.compressionMethod = Record.getBytes8(byteBuffer);
                if (byteBuffer.hasRemaining()) {
                    this.extensions = new SSLExtensions(this, byteBuffer, sSLExtensionArr);
                } else {
                    this.extensions = new SSLExtensions(this);
                }
            } catch (SSLProtocolException e2) {
                throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, e2);
            }
        }

        byte[] getHeaderBytes() {
            HandshakeOutStream handshakeOutStream = new HandshakeOutStream(null);
            try {
                handshakeOutStream.putInt8((byte) ((this.clientVersion >>> 8) & 255));
                handshakeOutStream.putInt8((byte) (this.clientVersion & 255));
                handshakeOutStream.write(this.clientRandom.randomBytes, 0, 32);
                handshakeOutStream.putBytes8(this.sessionId.getId());
                handshakeOutStream.putBytes16(getEncodedCipherSuites());
                handshakeOutStream.putBytes8(this.compressionMethod);
            } catch (IOException e2) {
            }
            return handshakeOutStream.toByteArray();
        }

        private static int[] getCipherSuiteIds(List<CipherSuite> list) {
            if (list != null) {
                int[] iArr = new int[list.size()];
                int i2 = 0;
                Iterator<CipherSuite> it = list.iterator();
                while (it.hasNext()) {
                    int i3 = i2;
                    i2++;
                    iArr[i3] = it.next().id;
                }
                return iArr;
            }
            return new int[0];
        }

        private static List<CipherSuite> getCipherSuites(int[] iArr) {
            LinkedList linkedList = new LinkedList();
            for (int i2 : iArr) {
                CipherSuite cipherSuiteValueOf = CipherSuite.valueOf(i2);
                if (cipherSuiteValueOf != null) {
                    linkedList.add(cipherSuiteValueOf);
                }
            }
            return Collections.unmodifiableList(linkedList);
        }

        private List<String> getCipherSuiteNames() {
            LinkedList linkedList = new LinkedList();
            for (int i2 : this.cipherSuiteIds) {
                linkedList.add(CipherSuite.nameOf(i2) + "(" + Utilities.byte16HexString(i2) + ")");
            }
            return linkedList;
        }

        private byte[] getEncodedCipherSuites() {
            byte[] bArr = new byte[this.cipherSuiteIds.length << 1];
            int i2 = 0;
            for (int i3 : this.cipherSuiteIds) {
                int i4 = i2;
                int i5 = i2 + 1;
                bArr[i4] = (byte) (i3 >> 8);
                i2 = i5 + 1;
                bArr[i5] = (byte) i3;
            }
            return bArr;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public SSLHandshake handshakeType() {
            return SSLHandshake.CLIENT_HELLO;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public int messageLength() {
            return 38 + this.sessionId.length() + (this.cipherSuiteIds.length * 2) + this.compressionMethod.length + this.extensions.length();
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public void send(HandshakeOutStream handshakeOutStream) throws IOException {
            sendCore(handshakeOutStream);
            this.extensions.send(handshakeOutStream);
        }

        void sendCore(HandshakeOutStream handshakeOutStream) throws IOException {
            handshakeOutStream.putInt8((byte) (this.clientVersion >>> 8));
            handshakeOutStream.putInt8((byte) this.clientVersion);
            handshakeOutStream.write(this.clientRandom.randomBytes, 0, 32);
            handshakeOutStream.putBytes8(this.sessionId.getId());
            handshakeOutStream.putBytes16(getEncodedCipherSuites());
            handshakeOutStream.putBytes8(this.compressionMethod);
        }

        public String toString() {
            return new MessageFormat("\"ClientHello\": '{'\n  \"client version\"      : \"{0}\",\n  \"random\"              : \"{1}\",\n  \"session id\"          : \"{2}\",\n  \"cipher suites\"       : \"{3}\",\n  \"compression methods\" : \"{4}\",\n  \"extensions\"          : [\n{5}\n  ]\n'}'", Locale.ENGLISH).format(new Object[]{ProtocolVersion.nameOf(this.clientVersion), Utilities.toHexString(this.clientRandom.randomBytes), this.sessionId.toString(), getCipherSuiteNames().toString(), Utilities.toHexString(this.compressionMethod), Utilities.indent(Utilities.indent(this.extensions.toString()))});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ClientHello$ClientHelloKickstartProducer.class */
    private static final class ClientHelloKickstartProducer implements SSLProducer {
        private ClientHelloKickstartProducer() {
        }

        @Override // sun.security.ssl.SSLProducer
        public byte[] produce(ConnectionContext connectionContext) throws IOException {
            String str;
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            clientHandshakeContext.handshakeProducers.remove(Byte.valueOf(SSLHandshake.CLIENT_HELLO.id));
            SessionId sessionId = new SessionId(new byte[0]);
            List<CipherSuite> linkedList = clientHandshakeContext.activeCipherSuites;
            SSLSessionImpl sSLSessionImpl = ((SSLSessionContextImpl) clientHandshakeContext.sslContext.engineGetClientSessionContext()).get(clientHandshakeContext.conContext.transport.getPeerHost(), clientHandshakeContext.conContext.transport.getPeerPort());
            if (sSLSessionImpl != null) {
                if (!ClientHandshakeContext.allowUnsafeServerCertChange && sSLSessionImpl.isSessionResumption()) {
                    try {
                        clientHandshakeContext.reservedServerCerts = (X509Certificate[]) sSLSessionImpl.getPeerCertificates();
                    } catch (SSLPeerUnverifiedException e2) {
                    }
                }
                if (!sSLSessionImpl.isRejoinable()) {
                    sSLSessionImpl = null;
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake,verbose")) {
                        SSLLogger.finest("Can't resume, the session is not rejoinable", new Object[0]);
                    }
                }
            }
            CipherSuite suite = null;
            if (sSLSessionImpl != null) {
                suite = sSLSessionImpl.getSuite();
                if (!clientHandshakeContext.isNegotiable(suite)) {
                    sSLSessionImpl = null;
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake,verbose")) {
                        SSLLogger.finest("Can't resume, unavailable session cipher suite", new Object[0]);
                    }
                }
            }
            ProtocolVersion protocolVersion = null;
            if (sSLSessionImpl != null) {
                protocolVersion = sSLSessionImpl.getProtocolVersion();
                if (!clientHandshakeContext.isNegotiable(protocolVersion)) {
                    sSLSessionImpl = null;
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake,verbose")) {
                        SSLLogger.finest("Can't resume, unavailable protocol version", new Object[0]);
                    }
                }
            }
            if (sSLSessionImpl != null && !protocolVersion.useTLS13PlusSpec() && SSLConfiguration.useExtendedMasterSecret) {
                boolean zIsAvailable = clientHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_EXTENDED_MASTER_SECRET, protocolVersion);
                if (zIsAvailable && !sSLSessionImpl.useExtendedMasterSecret && !SSLConfiguration.allowLegacyResumption) {
                    sSLSessionImpl = null;
                }
                if (sSLSessionImpl != null && !ClientHandshakeContext.allowUnsafeServerCertChange && (((str = clientHandshakeContext.sslConfig.identificationProtocol) == null || str.isEmpty()) && (!zIsAvailable || !sSLSessionImpl.useExtendedMasterSecret))) {
                    sSLSessionImpl = null;
                }
            }
            String str2 = clientHandshakeContext.sslConfig.identificationProtocol;
            if (sSLSessionImpl != null && str2 != null) {
                String identificationProtocol = sSLSessionImpl.getIdentificationProtocol();
                if (!str2.equalsIgnoreCase(identificationProtocol)) {
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake,verbose")) {
                        SSLLogger.finest("Can't resume, endpoint id algorithm does not match, requested: " + str2 + ", cached: " + identificationProtocol, new Object[0]);
                    }
                    sSLSessionImpl = null;
                }
            }
            if (sSLSessionImpl != null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake,verbose")) {
                    SSLLogger.finest("Try resuming session", sSLSessionImpl);
                }
                if (!sSLSessionImpl.getProtocolVersion().useTLS13PlusSpec()) {
                    sessionId = sSLSessionImpl.getSessionId();
                }
                if (!clientHandshakeContext.sslConfig.enableSessionCreation) {
                    if (!clientHandshakeContext.conContext.isNegotiated && !protocolVersion.useTLS13PlusSpec() && linkedList.contains(CipherSuite.TLS_EMPTY_RENEGOTIATION_INFO_SCSV)) {
                        linkedList = Arrays.asList(suite, CipherSuite.TLS_EMPTY_RENEGOTIATION_INFO_SCSV);
                    } else {
                        linkedList = Arrays.asList(suite);
                    }
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake,verbose")) {
                        SSLLogger.finest("No new session is allowed, so try to resume the session cipher suite only", suite);
                    }
                }
                clientHandshakeContext.isResumption = true;
                clientHandshakeContext.resumingSession = sSLSessionImpl;
            }
            if (sSLSessionImpl == null && !clientHandshakeContext.sslConfig.enableSessionCreation) {
                throw new SSLHandshakeException("No new session is allowed and no existing session can be resumed");
            }
            if (sessionId.length() == 0 && clientHandshakeContext.maximumActiveProtocol.useTLS13PlusSpec() && SSLConfiguration.useCompatibilityMode) {
                sessionId = new SessionId(true, clientHandshakeContext.sslContext.getSecureRandom());
            }
            ProtocolVersion protocolVersion2 = ProtocolVersion.NONE;
            for (ProtocolVersion protocolVersion3 : clientHandshakeContext.activeProtocols) {
                if (protocolVersion2 == ProtocolVersion.NONE || protocolVersion3.compare(protocolVersion2) < 0) {
                    protocolVersion2 = protocolVersion3;
                }
            }
            if (!protocolVersion2.useTLS13PlusSpec() && clientHandshakeContext.conContext.secureRenegotiation && linkedList.contains(CipherSuite.TLS_EMPTY_RENEGOTIATION_INFO_SCSV)) {
                linkedList = new LinkedList(linkedList);
                linkedList.remove(CipherSuite.TLS_EMPTY_RENEGOTIATION_INFO_SCSV);
            }
            boolean z2 = false;
            Iterator<CipherSuite> it = linkedList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                if (clientHandshakeContext.isNegotiable(it.next())) {
                    z2 = true;
                    break;
                }
            }
            if (!z2) {
                throw new SSLHandshakeException("No negotiable cipher suite");
            }
            ProtocolVersion protocolVersion4 = clientHandshakeContext.maximumActiveProtocol;
            if (protocolVersion4.useTLS13PlusSpec()) {
                protocolVersion4 = ProtocolVersion.TLS12;
            }
            ClientHelloMessage clientHelloMessage = new ClientHelloMessage(clientHandshakeContext, protocolVersion4.id, sessionId, linkedList, clientHandshakeContext.sslContext.getSecureRandom());
            clientHandshakeContext.clientHelloRandom = clientHelloMessage.clientRandom;
            clientHandshakeContext.clientHelloVersion = protocolVersion4.id;
            clientHelloMessage.extensions.produce(clientHandshakeContext, clientHandshakeContext.sslConfig.getEnabledExtensions(SSLHandshake.CLIENT_HELLO, clientHandshakeContext.activeProtocols));
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Produced ClientHello handshake message", clientHelloMessage);
            }
            clientHelloMessage.write(clientHandshakeContext.handshakeOutput);
            clientHandshakeContext.handshakeOutput.flush();
            clientHandshakeContext.initialClientHelloMsg = clientHelloMessage;
            clientHandshakeContext.handshakeConsumers.put(Byte.valueOf(SSLHandshake.SERVER_HELLO.id), SSLHandshake.SERVER_HELLO);
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ClientHello$ClientHelloProducer.class */
    private static final class ClientHelloProducer implements HandshakeProducer {
        private ClientHelloProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            SSLHandshake sSLHandshakeHandshakeType = handshakeMessage.handshakeType();
            if (sSLHandshakeHandshakeType == null) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            switch (sSLHandshakeHandshakeType) {
                case HELLO_REQUEST:
                    try {
                        clientHandshakeContext.kickstart();
                        return null;
                    } catch (IOException e2) {
                        throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, e2);
                    }
                case HELLO_RETRY_REQUEST:
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                        SSLLogger.fine("Produced ClientHello(HRR) handshake message", clientHandshakeContext.initialClientHelloMsg);
                    }
                    clientHandshakeContext.initialClientHelloMsg.write(clientHandshakeContext.handshakeOutput);
                    clientHandshakeContext.handshakeOutput.flush();
                    clientHandshakeContext.conContext.consumers.putIfAbsent(Byte.valueOf(ContentType.CHANGE_CIPHER_SPEC.id), ChangeCipherSpec.t13Consumer);
                    clientHandshakeContext.handshakeConsumers.put(Byte.valueOf(SSLHandshake.SERVER_HELLO.id), SSLHandshake.SERVER_HELLO);
                    return null;
                default:
                    throw new UnsupportedOperationException("Not supported yet.");
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ClientHello$ClientHelloConsumer.class */
    private static final class ClientHelloConsumer implements SSLConsumer {
        private ClientHelloConsumer() {
        }

        @Override // sun.security.ssl.SSLConsumer
        public void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            serverHandshakeContext.handshakeConsumers.remove(Byte.valueOf(SSLHandshake.CLIENT_HELLO.id));
            if (!serverHandshakeContext.handshakeConsumers.isEmpty()) {
                throw serverHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "No more handshake message allowed in a ClientHello flight");
            }
            ClientHelloMessage clientHelloMessage = new ClientHelloMessage(serverHandshakeContext, byteBuffer, serverHandshakeContext.sslConfig.getEnabledExtensions(SSLHandshake.CLIENT_HELLO));
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Consuming ClientHello handshake message", clientHelloMessage);
            }
            serverHandshakeContext.clientHelloVersion = clientHelloMessage.clientVersion;
            onClientHello(serverHandshakeContext, clientHelloMessage);
        }

        private void onClientHello(ServerHandshakeContext serverHandshakeContext, ClientHelloMessage clientHelloMessage) throws IOException {
            ProtocolVersion protocolVersionNegotiateProtocol;
            clientHelloMessage.extensions.consumeOnLoad(serverHandshakeContext, new SSLExtension[]{SSLExtension.CH_SUPPORTED_VERSIONS});
            SupportedVersionsExtension.CHSupportedVersionsSpec cHSupportedVersionsSpec = (SupportedVersionsExtension.CHSupportedVersionsSpec) serverHandshakeContext.handshakeExtensions.get(SSLExtension.CH_SUPPORTED_VERSIONS);
            if (cHSupportedVersionsSpec != null) {
                protocolVersionNegotiateProtocol = negotiateProtocol(serverHandshakeContext, cHSupportedVersionsSpec.requestedProtocols);
            } else {
                protocolVersionNegotiateProtocol = negotiateProtocol(serverHandshakeContext, clientHelloMessage.clientVersion);
            }
            serverHandshakeContext.negotiatedProtocol = protocolVersionNegotiateProtocol;
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Negotiated protocol version: " + protocolVersionNegotiateProtocol.name, new Object[0]);
            }
            if (protocolVersionNegotiateProtocol.useTLS13PlusSpec()) {
                ClientHello.t13HandshakeConsumer.consume(serverHandshakeContext, clientHelloMessage);
            } else {
                ClientHello.t12HandshakeConsumer.consume(serverHandshakeContext, clientHelloMessage);
            }
        }

        private ProtocolVersion negotiateProtocol(ServerHandshakeContext serverHandshakeContext, int i2) throws SSLException {
            int i3 = i2;
            if (i3 > ProtocolVersion.TLS12.id) {
                i3 = ProtocolVersion.TLS12.id;
            }
            ProtocolVersion protocolVersionSelectedFrom = ProtocolVersion.selectedFrom(serverHandshakeContext.activeProtocols, i3);
            if (protocolVersionSelectedFrom == null || protocolVersionSelectedFrom == ProtocolVersion.NONE || protocolVersionSelectedFrom == ProtocolVersion.SSL20Hello) {
                throw serverHandshakeContext.conContext.fatal(Alert.PROTOCOL_VERSION, "Client requested protocol " + ProtocolVersion.nameOf(i2) + " is not enabled or supported in server context");
            }
            return protocolVersionSelectedFrom;
        }

        private ProtocolVersion negotiateProtocol(ServerHandshakeContext serverHandshakeContext, int[] iArr) throws SSLException {
            for (ProtocolVersion protocolVersion : serverHandshakeContext.activeProtocols) {
                if (protocolVersion != ProtocolVersion.SSL20Hello) {
                    for (int i2 : iArr) {
                        if (i2 != ProtocolVersion.SSL20Hello.id && protocolVersion.id == i2) {
                            return protocolVersion;
                        }
                    }
                }
            }
            throw serverHandshakeContext.conContext.fatal(Alert.PROTOCOL_VERSION, "The client supported protocol versions " + Arrays.toString(ProtocolVersion.toStringArray(iArr)) + " are not accepted by server preferences " + ((Object) serverHandshakeContext.activeProtocols));
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ClientHello$T12ClientHelloConsumer.class */
    private static final class T12ClientHelloConsumer implements HandshakeConsumer {
        private T12ClientHelloConsumer() {
        }

        @Override // sun.security.ssl.HandshakeConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            Subject subject;
            final ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            ClientHelloMessage clientHelloMessage = (ClientHelloMessage) handshakeMessage;
            if (serverHandshakeContext.conContext.isNegotiated) {
                if (!serverHandshakeContext.conContext.secureRenegotiation && !HandshakeContext.allowUnsafeRenegotiation) {
                    throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Unsafe renegotiation is not allowed");
                }
                if (ServerHandshakeContext.rejectClientInitiatedRenego && !serverHandshakeContext.kickstartMessageDelivered) {
                    throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Client initiated renegotiation is not allowed");
                }
            }
            if (clientHelloMessage.sessionId.length() != 0) {
                SSLSessionImpl sSLSessionImpl = ((SSLSessionContextImpl) serverHandshakeContext.sslContext.engineGetServerSessionContext()).get(clientHelloMessage.sessionId.getId());
                boolean z2 = sSLSessionImpl != null && sSLSessionImpl.isRejoinable();
                if (!z2 && SSLLogger.isOn && SSLLogger.isOn("ssl,handshake,verbose")) {
                    SSLLogger.finest("Can't resume, the existing session is not rejoinable", new Object[0]);
                }
                if (z2 && sSLSessionImpl.getProtocolVersion() != serverHandshakeContext.negotiatedProtocol) {
                    z2 = false;
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake,verbose")) {
                        SSLLogger.finest("Can't resume, not the same protocol version", new Object[0]);
                    }
                }
                if (z2 && serverHandshakeContext.sslConfig.clientAuthType == ClientAuthType.CLIENT_AUTH_REQUIRED) {
                    try {
                        sSLSessionImpl.getPeerPrincipal();
                    } catch (SSLPeerUnverifiedException e2) {
                        z2 = false;
                        if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake,verbose")) {
                            SSLLogger.finest("Can't resume, client authentication is required", new Object[0]);
                        }
                    }
                }
                if (z2) {
                    CipherSuite suite = sSLSessionImpl.getSuite();
                    if (!serverHandshakeContext.isNegotiable(suite) || !clientHelloMessage.cipherSuites.contains(suite)) {
                        z2 = false;
                        if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake,verbose")) {
                            SSLLogger.finest("Can't resume, the session cipher suite is absent", new Object[0]);
                        }
                    }
                }
                if (z2) {
                    CipherSuite suite2 = sSLSessionImpl.getSuite();
                    if (suite2.keyExchange == CipherSuite.KeyExchange.K_KRB5 || suite2.keyExchange == CipherSuite.KeyExchange.K_KRB5_EXPORT) {
                        Principal localPrincipal = sSLSessionImpl.getLocalPrincipal();
                        try {
                            subject = (Subject) AccessController.doPrivileged(new PrivilegedExceptionAction<Subject>() { // from class: sun.security.ssl.ClientHello.T12ClientHelloConsumer.1
                                /* JADX WARN: Can't rename method to resolve collision */
                                @Override // java.security.PrivilegedExceptionAction
                                public Subject run() throws Exception {
                                    return Krb5Helper.getServerSubject(serverHandshakeContext.conContext.acc);
                                }
                            });
                        } catch (PrivilegedActionException e3) {
                            subject = null;
                            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake,verbose")) {
                                SSLLogger.finest("Attempt to obtain subject failed!", new Object[0]);
                            }
                        }
                        if (subject != null) {
                            if (Krb5Helper.isRelated(subject, localPrincipal)) {
                                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake,verbose")) {
                                    SSLLogger.finest("Subject can provide creds for princ", new Object[0]);
                                }
                            } else {
                                z2 = false;
                                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake,verbose")) {
                                    SSLLogger.finest("Subject cannot provide creds for princ", new Object[0]);
                                }
                            }
                        } else {
                            z2 = false;
                            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake,verbose")) {
                                SSLLogger.finest("Kerberos credentials are not present in the current Subject; check if  javax.security.auth.useSubjectCredsOnly system property has been set to false", new Object[0]);
                            }
                        }
                    }
                }
                String str = serverHandshakeContext.sslConfig.identificationProtocol;
                if (z2 && str != null) {
                    String identificationProtocol = sSLSessionImpl.getIdentificationProtocol();
                    if (!str.equalsIgnoreCase(identificationProtocol)) {
                        if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake,verbose")) {
                            SSLLogger.finest("Can't resume, endpoint id algorithm does not match, requested: " + str + ", cached: " + identificationProtocol, new Object[0]);
                        }
                        z2 = false;
                    }
                }
                serverHandshakeContext.isResumption = z2;
                serverHandshakeContext.resumingSession = z2 ? sSLSessionImpl : null;
            }
            serverHandshakeContext.clientHelloRandom = clientHelloMessage.clientRandom;
            clientHelloMessage.extensions.consumeOnLoad(serverHandshakeContext, serverHandshakeContext.sslConfig.getEnabledExtensions(SSLHandshake.CLIENT_HELLO));
            if (!serverHandshakeContext.conContext.isNegotiated) {
                serverHandshakeContext.conContext.protocolVersion = serverHandshakeContext.negotiatedProtocol;
                serverHandshakeContext.conContext.outputRecord.setVersion(serverHandshakeContext.negotiatedProtocol);
            }
            serverHandshakeContext.handshakeProducers.put(Byte.valueOf(SSLHandshake.SERVER_HELLO.id), SSLHandshake.SERVER_HELLO);
            for (SSLHandshake sSLHandshake : new SSLHandshake[]{SSLHandshake.SERVER_HELLO, SSLHandshake.CERTIFICATE, SSLHandshake.CERTIFICATE_STATUS, SSLHandshake.SERVER_KEY_EXCHANGE, SSLHandshake.CERTIFICATE_REQUEST, SSLHandshake.SERVER_HELLO_DONE, SSLHandshake.FINISHED}) {
                HandshakeProducer handshakeProducerRemove = serverHandshakeContext.handshakeProducers.remove(Byte.valueOf(sSLHandshake.id));
                if (handshakeProducerRemove != null) {
                    handshakeProducerRemove.produce(connectionContext, clientHelloMessage);
                }
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ClientHello$T13ClientHelloConsumer.class */
    private static final class T13ClientHelloConsumer implements HandshakeConsumer {
        private T13ClientHelloConsumer() {
        }

        @Override // sun.security.ssl.HandshakeConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            ClientHelloMessage clientHelloMessage = (ClientHelloMessage) handshakeMessage;
            if (serverHandshakeContext.conContext.isNegotiated) {
                throw serverHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Received unexpected renegotiation handshake message");
            }
            serverHandshakeContext.conContext.consumers.putIfAbsent(Byte.valueOf(ContentType.CHANGE_CIPHER_SPEC.id), ChangeCipherSpec.t13Consumer);
            serverHandshakeContext.isResumption = true;
            clientHelloMessage.extensions.consumeOnLoad(serverHandshakeContext, new SSLExtension[]{SSLExtension.PSK_KEY_EXCHANGE_MODES, SSLExtension.CH_PRE_SHARED_KEY});
            clientHelloMessage.extensions.consumeOnLoad(serverHandshakeContext, serverHandshakeContext.sslConfig.getExclusiveExtensions(SSLHandshake.CLIENT_HELLO, Arrays.asList(SSLExtension.PSK_KEY_EXCHANGE_MODES, SSLExtension.CH_PRE_SHARED_KEY, SSLExtension.CH_SUPPORTED_VERSIONS)));
            if (!serverHandshakeContext.handshakeProducers.isEmpty()) {
                goHelloRetryRequest(serverHandshakeContext, clientHelloMessage);
            } else {
                goServerHello(serverHandshakeContext, clientHelloMessage);
            }
        }

        private void goHelloRetryRequest(ServerHandshakeContext serverHandshakeContext, ClientHelloMessage clientHelloMessage) throws IOException {
            HandshakeProducer handshakeProducerRemove = serverHandshakeContext.handshakeProducers.remove(Byte.valueOf(SSLHandshake.HELLO_RETRY_REQUEST.id));
            if (handshakeProducerRemove != null) {
                handshakeProducerRemove.produce(serverHandshakeContext, clientHelloMessage);
                if (!serverHandshakeContext.handshakeProducers.isEmpty()) {
                    throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "unknown handshake producers: " + ((Object) serverHandshakeContext.handshakeProducers));
                }
                return;
            }
            throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "No HelloRetryRequest producer: " + ((Object) serverHandshakeContext.handshakeProducers));
        }

        private void goServerHello(ServerHandshakeContext serverHandshakeContext, ClientHelloMessage clientHelloMessage) throws IOException {
            serverHandshakeContext.clientHelloRandom = clientHelloMessage.clientRandom;
            if (!serverHandshakeContext.conContext.isNegotiated) {
                serverHandshakeContext.conContext.protocolVersion = serverHandshakeContext.negotiatedProtocol;
                serverHandshakeContext.conContext.outputRecord.setVersion(serverHandshakeContext.negotiatedProtocol);
            }
            serverHandshakeContext.handshakeProducers.put(Byte.valueOf(SSLHandshake.SERVER_HELLO.id), SSLHandshake.SERVER_HELLO);
            for (SSLHandshake sSLHandshake : new SSLHandshake[]{SSLHandshake.SERVER_HELLO, SSLHandshake.ENCRYPTED_EXTENSIONS, SSLHandshake.CERTIFICATE_REQUEST, SSLHandshake.CERTIFICATE, SSLHandshake.CERTIFICATE_VERIFY, SSLHandshake.FINISHED}) {
                HandshakeProducer handshakeProducerRemove = serverHandshakeContext.handshakeProducers.remove(Byte.valueOf(sSLHandshake.id));
                if (handshakeProducerRemove != null) {
                    handshakeProducerRemove.produce(serverHandshakeContext, clientHelloMessage);
                }
            }
        }
    }
}
