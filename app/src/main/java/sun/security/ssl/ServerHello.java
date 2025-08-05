package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.AlgorithmConstraints;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLProtocolException;
import javax.security.auth.Subject;
import sun.security.ssl.CipherSuite;
import sun.security.ssl.ClientHello;
import sun.security.ssl.SSLCipher;
import sun.security.ssl.SSLHandshake;
import sun.security.ssl.SupportedVersionsExtension;

/* loaded from: jsse.jar:sun/security/ssl/ServerHello.class */
final class ServerHello {
    static final SSLConsumer handshakeConsumer = new ServerHelloConsumer();
    static final HandshakeProducer t12HandshakeProducer = new T12ServerHelloProducer();
    static final HandshakeProducer t13HandshakeProducer = new T13ServerHelloProducer();
    static final HandshakeProducer hrrHandshakeProducer = new T13HelloRetryRequestProducer();
    static final HandshakeProducer hrrReproducer = new T13HelloRetryRequestReproducer();
    private static final HandshakeConsumer t12HandshakeConsumer = new T12ServerHelloConsumer();
    private static final HandshakeConsumer t13HandshakeConsumer = new T13ServerHelloConsumer();
    private static final HandshakeConsumer t13HrrHandshakeConsumer = new T13HelloRetryRequestConsumer();

    ServerHello() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/ServerHello$ServerHelloMessage.class */
    static final class ServerHelloMessage extends SSLHandshake.HandshakeMessage {
        final ProtocolVersion serverVersion;
        final RandomCookie serverRandom;
        final SessionId sessionId;
        final CipherSuite cipherSuite;
        final byte compressionMethod;
        final SSLExtensions extensions;
        final ClientHello.ClientHelloMessage clientHello;
        final ByteBuffer handshakeRecord;

        ServerHelloMessage(HandshakeContext handshakeContext, ProtocolVersion protocolVersion, SessionId sessionId, CipherSuite cipherSuite, RandomCookie randomCookie, ClientHello.ClientHelloMessage clientHelloMessage) {
            super(handshakeContext);
            this.serverVersion = protocolVersion;
            this.serverRandom = randomCookie;
            this.sessionId = sessionId;
            this.cipherSuite = cipherSuite;
            this.compressionMethod = (byte) 0;
            this.extensions = new SSLExtensions(this);
            this.clientHello = clientHelloMessage;
            this.handshakeRecord = null;
        }

        ServerHelloMessage(HandshakeContext handshakeContext, ByteBuffer byteBuffer) throws IOException {
            SSLExtension[] enabledExtensions;
            super(handshakeContext);
            this.handshakeRecord = byteBuffer.duplicate();
            byte b2 = byteBuffer.get();
            byte b3 = byteBuffer.get();
            this.serverVersion = ProtocolVersion.valueOf(b2, b3);
            if (this.serverVersion == null) {
                throw handshakeContext.conContext.fatal(Alert.PROTOCOL_VERSION, "Unsupported protocol version: " + ProtocolVersion.nameOf(b2, b3));
            }
            this.serverRandom = new RandomCookie(byteBuffer);
            this.sessionId = new SessionId(Record.getBytes8(byteBuffer));
            try {
                this.sessionId.checkLength(this.serverVersion.id);
                int int16 = Record.getInt16(byteBuffer);
                this.cipherSuite = CipherSuite.valueOf(int16);
                if (this.cipherSuite == null || !handshakeContext.isNegotiable(this.cipherSuite)) {
                    throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Server selected improper ciphersuite " + CipherSuite.nameOf(int16));
                }
                this.compressionMethod = byteBuffer.get();
                if (this.compressionMethod != 0) {
                    throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "compression type not supported, " + ((int) this.compressionMethod));
                }
                if (this.serverRandom.isHelloRetryRequest()) {
                    enabledExtensions = handshakeContext.sslConfig.getEnabledExtensions(SSLHandshake.HELLO_RETRY_REQUEST);
                } else {
                    enabledExtensions = handshakeContext.sslConfig.getEnabledExtensions(SSLHandshake.SERVER_HELLO);
                }
                if (byteBuffer.hasRemaining()) {
                    this.extensions = new SSLExtensions(this, byteBuffer, enabledExtensions);
                } else {
                    this.extensions = new SSLExtensions(this);
                }
                this.clientHello = null;
            } catch (SSLProtocolException e2) {
                throw this.handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, e2);
            }
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public SSLHandshake handshakeType() {
            return this.serverRandom.isHelloRetryRequest() ? SSLHandshake.HELLO_RETRY_REQUEST : SSLHandshake.SERVER_HELLO;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public int messageLength() {
            return 38 + this.sessionId.length() + this.extensions.length();
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public void send(HandshakeOutStream handshakeOutStream) throws IOException {
            handshakeOutStream.putInt8(this.serverVersion.major);
            handshakeOutStream.putInt8(this.serverVersion.minor);
            handshakeOutStream.write(this.serverRandom.randomBytes);
            handshakeOutStream.putBytes8(this.sessionId.getId());
            handshakeOutStream.putInt8((this.cipherSuite.id >> 8) & 255);
            handshakeOutStream.putInt8(this.cipherSuite.id & 255);
            handshakeOutStream.putInt8(this.compressionMethod);
            this.extensions.send(handshakeOutStream);
        }

        public String toString() {
            MessageFormat messageFormat = new MessageFormat("\"{0}\": '{'\n  \"server version\"      : \"{1}\",\n  \"random\"              : \"{2}\",\n  \"session id\"          : \"{3}\",\n  \"cipher suite\"        : \"{4}\",\n  \"compression methods\" : \"{5}\",\n  \"extensions\"          : [\n{6}\n  ]\n'}'", Locale.ENGLISH);
            Object[] objArr = new Object[7];
            objArr[0] = this.serverRandom.isHelloRetryRequest() ? "HelloRetryRequest" : "ServerHello";
            objArr[1] = this.serverVersion.name;
            objArr[2] = Utilities.toHexString(this.serverRandom.randomBytes);
            objArr[3] = this.sessionId.toString();
            objArr[4] = this.cipherSuite.name + "(" + Utilities.byte16HexString(this.cipherSuite.id) + ")";
            objArr[5] = Utilities.toHexString(this.compressionMethod);
            objArr[6] = Utilities.indent(this.extensions.toString(), "    ");
            return messageFormat.format(objArr);
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ServerHello$T12ServerHelloProducer.class */
    private static final class T12ServerHelloProducer implements HandshakeProducer {
        private T12ServerHelloProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            ClientHello.ClientHelloMessage clientHelloMessage = (ClientHello.ClientHelloMessage) handshakeMessage;
            if (!serverHandshakeContext.isResumption || serverHandshakeContext.resumingSession == null) {
                if (!serverHandshakeContext.sslConfig.enableSessionCreation) {
                    throw new SSLException("Not resumption, and no new session is allowed");
                }
                if (serverHandshakeContext.localSupportedSignAlgs == null) {
                    serverHandshakeContext.localSupportedSignAlgs = SignatureScheme.getSupportedAlgorithms(serverHandshakeContext.sslConfig, serverHandshakeContext.algorithmConstraints, serverHandshakeContext.activeProtocols);
                }
                SSLSessionImpl sSLSessionImpl = new SSLSessionImpl(serverHandshakeContext, CipherSuite.C_NULL);
                sSLSessionImpl.setMaximumPacketSize(serverHandshakeContext.sslConfig.maximumPacketSize);
                serverHandshakeContext.handshakeSession = sSLSessionImpl;
                clientHelloMessage.extensions.consumeOnTrade(serverHandshakeContext, serverHandshakeContext.sslConfig.getEnabledExtensions(SSLHandshake.CLIENT_HELLO, serverHandshakeContext.negotiatedProtocol));
                KeyExchangeProperties keyExchangePropertiesChooseCipherSuite = chooseCipherSuite(serverHandshakeContext, clientHelloMessage);
                if (keyExchangePropertiesChooseCipherSuite == null) {
                    throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "no cipher suites in common");
                }
                serverHandshakeContext.negotiatedCipherSuite = keyExchangePropertiesChooseCipherSuite.cipherSuite;
                serverHandshakeContext.handshakeKeyExchange = keyExchangePropertiesChooseCipherSuite.keyExchange;
                serverHandshakeContext.handshakeSession.setSuite(keyExchangePropertiesChooseCipherSuite.cipherSuite);
                serverHandshakeContext.handshakePossessions.addAll(Arrays.asList(keyExchangePropertiesChooseCipherSuite.possessions));
                serverHandshakeContext.handshakeHash.determine(serverHandshakeContext.negotiatedProtocol, serverHandshakeContext.negotiatedCipherSuite);
                serverHandshakeContext.stapleParams = StatusResponseManager.processStapling(serverHandshakeContext);
                serverHandshakeContext.staplingActive = serverHandshakeContext.stapleParams != null;
                SSLKeyExchange sSLKeyExchange = keyExchangePropertiesChooseCipherSuite.keyExchange;
                if (sSLKeyExchange != null) {
                    for (Map.Entry<Byte, HandshakeProducer> entry : sSLKeyExchange.getHandshakeProducers(serverHandshakeContext)) {
                        serverHandshakeContext.handshakeProducers.put(entry.getKey(), entry.getValue());
                    }
                }
                if (sSLKeyExchange != null && serverHandshakeContext.sslConfig.clientAuthType != ClientAuthType.CLIENT_AUTH_NONE && !serverHandshakeContext.negotiatedCipherSuite.isAnonymous()) {
                    SSLHandshake[] relatedHandshakers = sSLKeyExchange.getRelatedHandshakers(serverHandshakeContext);
                    int length = relatedHandshakers.length;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= length) {
                            break;
                        }
                        if (relatedHandshakers[i2] != SSLHandshake.CERTIFICATE) {
                            i2++;
                        } else {
                            serverHandshakeContext.handshakeProducers.put(Byte.valueOf(SSLHandshake.CERTIFICATE_REQUEST.id), SSLHandshake.CERTIFICATE_REQUEST);
                            break;
                        }
                    }
                }
                serverHandshakeContext.handshakeProducers.put(Byte.valueOf(SSLHandshake.SERVER_HELLO_DONE.id), SSLHandshake.SERVER_HELLO_DONE);
            } else {
                serverHandshakeContext.handshakeSession = serverHandshakeContext.resumingSession;
                serverHandshakeContext.negotiatedProtocol = serverHandshakeContext.resumingSession.getProtocolVersion();
                serverHandshakeContext.negotiatedCipherSuite = serverHandshakeContext.resumingSession.getSuite();
                serverHandshakeContext.handshakeHash.determine(serverHandshakeContext.negotiatedProtocol, serverHandshakeContext.negotiatedCipherSuite);
            }
            ServerHelloMessage serverHelloMessage = new ServerHelloMessage(serverHandshakeContext, serverHandshakeContext.negotiatedProtocol, serverHandshakeContext.handshakeSession.getSessionId(), serverHandshakeContext.negotiatedCipherSuite, new RandomCookie(serverHandshakeContext), clientHelloMessage);
            serverHandshakeContext.serverHelloRandom = serverHelloMessage.serverRandom;
            serverHelloMessage.extensions.produce(serverHandshakeContext, serverHandshakeContext.sslConfig.getEnabledExtensions(SSLHandshake.SERVER_HELLO, serverHandshakeContext.negotiatedProtocol));
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Produced ServerHello handshake message", serverHelloMessage);
            }
            serverHelloMessage.write(serverHandshakeContext.handshakeOutput);
            serverHandshakeContext.handshakeOutput.flush();
            if (serverHandshakeContext.isResumption && serverHandshakeContext.resumingSession != null) {
                SSLTrafficKeyDerivation sSLTrafficKeyDerivationValueOf = SSLTrafficKeyDerivation.valueOf(serverHandshakeContext.negotiatedProtocol);
                if (sSLTrafficKeyDerivationValueOf == null) {
                    throw serverHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Not supported key derivation: " + ((Object) serverHandshakeContext.negotiatedProtocol));
                }
                serverHandshakeContext.handshakeKeyDerivation = sSLTrafficKeyDerivationValueOf.createKeyDerivation(serverHandshakeContext, serverHandshakeContext.resumingSession.getMasterSecret());
                serverHandshakeContext.handshakeProducers.put(Byte.valueOf(SSLHandshake.FINISHED.id), SSLHandshake.FINISHED);
                return null;
            }
            return null;
        }

        private static KeyExchangeProperties chooseCipherSuite(ServerHandshakeContext serverHandshakeContext, ClientHello.ClientHelloMessage clientHelloMessage) throws IOException {
            List<CipherSuite> list;
            List<CipherSuite> list2;
            SSLPossession[] sSLPossessionArrCreatePossessions;
            if (serverHandshakeContext.sslConfig.preferLocalCipherSuites) {
                list = serverHandshakeContext.activeCipherSuites;
                list2 = clientHelloMessage.cipherSuites;
            } else {
                list = clientHelloMessage.cipherSuites;
                list2 = serverHandshakeContext.activeCipherSuites;
            }
            LinkedList<CipherSuite> linkedList = new LinkedList();
            for (CipherSuite cipherSuite : list) {
                if (HandshakeContext.isNegotiable(list2, serverHandshakeContext.negotiatedProtocol, cipherSuite) && (serverHandshakeContext.sslConfig.clientAuthType != ClientAuthType.CLIENT_AUTH_REQUIRED || (cipherSuite.keyExchange != CipherSuite.KeyExchange.K_DH_ANON && cipherSuite.keyExchange != CipherSuite.KeyExchange.K_ECDH_ANON))) {
                    SSLKeyExchange sSLKeyExchangeValueOf = SSLKeyExchange.valueOf(cipherSuite.keyExchange, serverHandshakeContext.negotiatedProtocol);
                    if (sSLKeyExchangeValueOf == null) {
                        continue;
                    } else if (!ServerHandshakeContext.legacyAlgorithmConstraints.permits(null, cipherSuite.name, null)) {
                        linkedList.add(cipherSuite);
                    } else {
                        SSLPossession[] sSLPossessionArrCreatePossessions2 = sSLKeyExchangeValueOf.createPossessions(serverHandshakeContext);
                        if (sSLPossessionArrCreatePossessions2 != null && sSLPossessionArrCreatePossessions2.length != 0) {
                            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                                SSLLogger.fine("use cipher suite " + cipherSuite.name, new Object[0]);
                            }
                            return new KeyExchangeProperties(cipherSuite, sSLKeyExchangeValueOf, sSLPossessionArrCreatePossessions2);
                        }
                    }
                }
            }
            for (CipherSuite cipherSuite2 : linkedList) {
                SSLKeyExchange sSLKeyExchangeValueOf2 = SSLKeyExchange.valueOf(cipherSuite2.keyExchange, serverHandshakeContext.negotiatedProtocol);
                if (sSLKeyExchangeValueOf2 != null && (sSLPossessionArrCreatePossessions = sSLKeyExchangeValueOf2.createPossessions(serverHandshakeContext)) != null && sSLPossessionArrCreatePossessions.length != 0) {
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                        SSLLogger.warning("use legacy cipher suite " + cipherSuite2.name, new Object[0]);
                    }
                    return new KeyExchangeProperties(cipherSuite2, sSLKeyExchangeValueOf2, sSLPossessionArrCreatePossessions);
                }
            }
            throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "no cipher suites in common");
        }

        /* loaded from: jsse.jar:sun/security/ssl/ServerHello$T12ServerHelloProducer$KeyExchangeProperties.class */
        private static final class KeyExchangeProperties {
            final CipherSuite cipherSuite;
            final SSLKeyExchange keyExchange;
            final SSLPossession[] possessions;

            private KeyExchangeProperties(CipherSuite cipherSuite, SSLKeyExchange sSLKeyExchange, SSLPossession[] sSLPossessionArr) {
                this.cipherSuite = cipherSuite;
                this.keyExchange = sSLKeyExchange;
                this.possessions = sSLPossessionArr;
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ServerHello$T13ServerHelloProducer.class */
    private static final class T13ServerHelloProducer implements HandshakeProducer {
        private T13ServerHelloProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            ClientHello.ClientHelloMessage clientHelloMessage = (ClientHello.ClientHelloMessage) handshakeMessage;
            if (!serverHandshakeContext.isResumption || serverHandshakeContext.resumingSession == null) {
                if (!serverHandshakeContext.sslConfig.enableSessionCreation) {
                    throw new SSLException("Not resumption, and no new session is allowed");
                }
                if (serverHandshakeContext.localSupportedSignAlgs == null) {
                    serverHandshakeContext.localSupportedSignAlgs = SignatureScheme.getSupportedAlgorithms(serverHandshakeContext.sslConfig, serverHandshakeContext.algorithmConstraints, serverHandshakeContext.activeProtocols);
                }
                SSLSessionImpl sSLSessionImpl = new SSLSessionImpl(serverHandshakeContext, CipherSuite.C_NULL);
                sSLSessionImpl.setMaximumPacketSize(serverHandshakeContext.sslConfig.maximumPacketSize);
                serverHandshakeContext.handshakeSession = sSLSessionImpl;
                clientHelloMessage.extensions.consumeOnTrade(serverHandshakeContext, serverHandshakeContext.sslConfig.getEnabledExtensions(SSLHandshake.CLIENT_HELLO, serverHandshakeContext.negotiatedProtocol));
                CipherSuite cipherSuiteChooseCipherSuite = chooseCipherSuite(serverHandshakeContext, clientHelloMessage);
                if (cipherSuiteChooseCipherSuite == null) {
                    throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "no cipher suites in common");
                }
                serverHandshakeContext.negotiatedCipherSuite = cipherSuiteChooseCipherSuite;
                serverHandshakeContext.handshakeSession.setSuite(cipherSuiteChooseCipherSuite);
                serverHandshakeContext.handshakeHash.determine(serverHandshakeContext.negotiatedProtocol, serverHandshakeContext.negotiatedCipherSuite);
            } else {
                serverHandshakeContext.handshakeSession = serverHandshakeContext.resumingSession;
                clientHelloMessage.extensions.consumeOnTrade(serverHandshakeContext, serverHandshakeContext.sslConfig.getEnabledExtensions(SSLHandshake.CLIENT_HELLO, serverHandshakeContext.negotiatedProtocol));
                serverHandshakeContext.negotiatedProtocol = serverHandshakeContext.resumingSession.getProtocolVersion();
                serverHandshakeContext.negotiatedCipherSuite = serverHandshakeContext.resumingSession.getSuite();
                serverHandshakeContext.handshakeHash.determine(serverHandshakeContext.negotiatedProtocol, serverHandshakeContext.negotiatedCipherSuite);
                ServerHello.setUpPskKD(serverHandshakeContext, serverHandshakeContext.resumingSession.consumePreSharedKey());
            }
            serverHandshakeContext.handshakeProducers.put(Byte.valueOf(SSLHandshake.ENCRYPTED_EXTENSIONS.id), SSLHandshake.ENCRYPTED_EXTENSIONS);
            serverHandshakeContext.handshakeProducers.put(Byte.valueOf(SSLHandshake.FINISHED.id), SSLHandshake.FINISHED);
            ServerHelloMessage serverHelloMessage = new ServerHelloMessage(serverHandshakeContext, ProtocolVersion.TLS12, clientHelloMessage.sessionId, serverHandshakeContext.negotiatedCipherSuite, new RandomCookie(serverHandshakeContext), clientHelloMessage);
            serverHandshakeContext.serverHelloRandom = serverHelloMessage.serverRandom;
            serverHelloMessage.extensions.produce(serverHandshakeContext, serverHandshakeContext.sslConfig.getEnabledExtensions(SSLHandshake.SERVER_HELLO, serverHandshakeContext.negotiatedProtocol));
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Produced ServerHello handshake message", serverHelloMessage);
            }
            serverHelloMessage.write(serverHandshakeContext.handshakeOutput);
            serverHandshakeContext.handshakeOutput.flush();
            serverHandshakeContext.handshakeHash.update();
            SSLKeyExchange sSLKeyExchange = serverHandshakeContext.handshakeKeyExchange;
            if (sSLKeyExchange == null) {
                throw serverHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Not negotiated key shares");
            }
            SecretKey secretKeyDeriveKey = sSLKeyExchange.createKeyDerivation(serverHandshakeContext).deriveKey("TlsHandshakeSecret", null);
            SSLTrafficKeyDerivation sSLTrafficKeyDerivationValueOf = SSLTrafficKeyDerivation.valueOf(serverHandshakeContext.negotiatedProtocol);
            if (sSLTrafficKeyDerivationValueOf == null) {
                throw serverHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Not supported key derivation: " + ((Object) serverHandshakeContext.negotiatedProtocol));
            }
            SSLSecretDerivation sSLSecretDerivation = new SSLSecretDerivation(serverHandshakeContext, secretKeyDeriveKey);
            SecretKey secretKeyDeriveKey2 = sSLSecretDerivation.deriveKey("TlsClientHandshakeTrafficSecret", null);
            SSLKeyDerivation sSLKeyDerivationCreateKeyDerivation = sSLTrafficKeyDerivationValueOf.createKeyDerivation(serverHandshakeContext, secretKeyDeriveKey2);
            try {
                SSLCipher.SSLReadCipher sSLReadCipherCreateReadCipher = serverHandshakeContext.negotiatedCipherSuite.bulkCipher.createReadCipher(Authenticator.valueOf(serverHandshakeContext.negotiatedProtocol), serverHandshakeContext.negotiatedProtocol, sSLKeyDerivationCreateKeyDerivation.deriveKey("TlsKey", null), new IvParameterSpec(sSLKeyDerivationCreateKeyDerivation.deriveKey("TlsIv", null).getEncoded()), serverHandshakeContext.sslContext.getSecureRandom());
                if (sSLReadCipherCreateReadCipher == null) {
                    throw serverHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Illegal cipher suite (" + ((Object) serverHandshakeContext.negotiatedCipherSuite) + ") and protocol version (" + ((Object) serverHandshakeContext.negotiatedProtocol) + ")");
                }
                serverHandshakeContext.baseReadSecret = secretKeyDeriveKey2;
                serverHandshakeContext.conContext.inputRecord.changeReadCiphers(sSLReadCipherCreateReadCipher);
                SecretKey secretKeyDeriveKey3 = sSLSecretDerivation.deriveKey("TlsServerHandshakeTrafficSecret", null);
                SSLKeyDerivation sSLKeyDerivationCreateKeyDerivation2 = sSLTrafficKeyDerivationValueOf.createKeyDerivation(serverHandshakeContext, secretKeyDeriveKey3);
                try {
                    SSLCipher.SSLWriteCipher sSLWriteCipherCreateWriteCipher = serverHandshakeContext.negotiatedCipherSuite.bulkCipher.createWriteCipher(Authenticator.valueOf(serverHandshakeContext.negotiatedProtocol), serverHandshakeContext.negotiatedProtocol, sSLKeyDerivationCreateKeyDerivation2.deriveKey("TlsKey", null), new IvParameterSpec(sSLKeyDerivationCreateKeyDerivation2.deriveKey("TlsIv", null).getEncoded()), serverHandshakeContext.sslContext.getSecureRandom());
                    if (sSLWriteCipherCreateWriteCipher == null) {
                        throw serverHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Illegal cipher suite (" + ((Object) serverHandshakeContext.negotiatedCipherSuite) + ") and protocol version (" + ((Object) serverHandshakeContext.negotiatedProtocol) + ")");
                    }
                    serverHandshakeContext.baseWriteSecret = secretKeyDeriveKey3;
                    serverHandshakeContext.conContext.outputRecord.changeWriteCiphers(sSLWriteCipherCreateWriteCipher, clientHelloMessage.sessionId.length() != 0);
                    serverHandshakeContext.handshakeKeyDerivation = sSLSecretDerivation;
                    return null;
                } catch (GeneralSecurityException e2) {
                    throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Missing cipher algorithm", e2);
                }
            } catch (GeneralSecurityException e3) {
                throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Missing cipher algorithm", e3);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static CipherSuite chooseCipherSuite(ServerHandshakeContext serverHandshakeContext, ClientHello.ClientHelloMessage clientHelloMessage) throws IOException {
            List<CipherSuite> list;
            List<CipherSuite> list2;
            if (serverHandshakeContext.sslConfig.preferLocalCipherSuites) {
                list = serverHandshakeContext.activeCipherSuites;
                list2 = clientHelloMessage.cipherSuites;
            } else {
                list = clientHelloMessage.cipherSuites;
                list2 = serverHandshakeContext.activeCipherSuites;
            }
            CipherSuite cipherSuite = null;
            AlgorithmConstraints algorithmConstraints = ServerHandshakeContext.legacyAlgorithmConstraints;
            for (CipherSuite cipherSuite2 : list) {
                if (HandshakeContext.isNegotiable(list2, serverHandshakeContext.negotiatedProtocol, cipherSuite2)) {
                    if (cipherSuite == null && !algorithmConstraints.permits(null, cipherSuite2.name, null)) {
                        cipherSuite = cipherSuite2;
                    } else {
                        if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                            SSLLogger.fine("use cipher suite " + cipherSuite2.name, new Object[0]);
                        }
                        return cipherSuite2;
                    }
                }
            }
            if (cipherSuite != null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.warning("use legacy cipher suite " + cipherSuite.name, new Object[0]);
                }
                return cipherSuite;
            }
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ServerHello$T13HelloRetryRequestProducer.class */
    private static final class T13HelloRetryRequestProducer implements HandshakeProducer {
        private T13HelloRetryRequestProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            ClientHello.ClientHelloMessage clientHelloMessage = (ClientHello.ClientHelloMessage) handshakeMessage;
            CipherSuite cipherSuiteChooseCipherSuite = T13ServerHelloProducer.chooseCipherSuite(serverHandshakeContext, clientHelloMessage);
            if (cipherSuiteChooseCipherSuite == null) {
                throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "no cipher suites in common for hello retry request");
            }
            ServerHelloMessage serverHelloMessage = new ServerHelloMessage(serverHandshakeContext, ProtocolVersion.TLS12, clientHelloMessage.sessionId, cipherSuiteChooseCipherSuite, RandomCookie.hrrRandom, clientHelloMessage);
            serverHandshakeContext.negotiatedCipherSuite = cipherSuiteChooseCipherSuite;
            serverHandshakeContext.handshakeHash.determine(serverHandshakeContext.negotiatedProtocol, serverHandshakeContext.negotiatedCipherSuite);
            serverHelloMessage.extensions.produce(serverHandshakeContext, serverHandshakeContext.sslConfig.getEnabledExtensions(SSLHandshake.HELLO_RETRY_REQUEST, serverHandshakeContext.negotiatedProtocol));
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Produced HelloRetryRequest handshake message", serverHelloMessage);
            }
            serverHelloMessage.write(serverHandshakeContext.handshakeOutput);
            serverHandshakeContext.handshakeOutput.flush();
            serverHandshakeContext.handshakeHash.finish();
            serverHandshakeContext.handshakeExtensions.clear();
            serverHandshakeContext.handshakeConsumers.put(Byte.valueOf(SSLHandshake.CLIENT_HELLO.id), SSLHandshake.CLIENT_HELLO);
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ServerHello$T13HelloRetryRequestReproducer.class */
    private static final class T13HelloRetryRequestReproducer implements HandshakeProducer {
        private T13HelloRetryRequestReproducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            ClientHello.ClientHelloMessage clientHelloMessage = (ClientHello.ClientHelloMessage) handshakeMessage;
            ServerHelloMessage serverHelloMessage = new ServerHelloMessage(serverHandshakeContext, ProtocolVersion.TLS12, clientHelloMessage.sessionId, serverHandshakeContext.negotiatedCipherSuite, RandomCookie.hrrRandom, clientHelloMessage);
            serverHelloMessage.extensions.produce(serverHandshakeContext, serverHandshakeContext.sslConfig.getEnabledExtensions(SSLHandshake.MESSAGE_HASH, serverHandshakeContext.negotiatedProtocol));
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Reproduced HelloRetryRequest handshake message", serverHelloMessage);
            }
            HandshakeOutStream handshakeOutStream = new HandshakeOutStream(null);
            serverHelloMessage.write(handshakeOutStream);
            return handshakeOutStream.toByteArray();
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ServerHello$ServerHelloConsumer.class */
    private static final class ServerHelloConsumer implements SSLConsumer {
        private ServerHelloConsumer() {
        }

        @Override // sun.security.ssl.SSLConsumer
        public void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            clientHandshakeContext.handshakeConsumers.remove(Byte.valueOf(SSLHandshake.SERVER_HELLO.id));
            if (!clientHandshakeContext.handshakeConsumers.isEmpty()) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "No more message expected before ServerHello is processed");
            }
            ServerHelloMessage serverHelloMessage = new ServerHelloMessage(clientHandshakeContext, byteBuffer);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Consuming ServerHello handshake message", serverHelloMessage);
            }
            if (serverHelloMessage.serverRandom.isHelloRetryRequest()) {
                onHelloRetryRequest(clientHandshakeContext, serverHelloMessage);
            } else {
                onServerHello(clientHandshakeContext, serverHelloMessage);
            }
        }

        private void onHelloRetryRequest(ClientHandshakeContext clientHandshakeContext, ServerHelloMessage serverHelloMessage) throws IOException {
            ProtocolVersion protocolVersionValueOf;
            serverHelloMessage.extensions.consumeOnLoad(clientHandshakeContext, new SSLExtension[]{SSLExtension.HRR_SUPPORTED_VERSIONS});
            SupportedVersionsExtension.SHSupportedVersionsSpec sHSupportedVersionsSpec = (SupportedVersionsExtension.SHSupportedVersionsSpec) clientHandshakeContext.handshakeExtensions.get(SSLExtension.HRR_SUPPORTED_VERSIONS);
            if (sHSupportedVersionsSpec != null) {
                protocolVersionValueOf = ProtocolVersion.valueOf(sHSupportedVersionsSpec.selectedVersion);
            } else {
                protocolVersionValueOf = serverHelloMessage.serverVersion;
            }
            if (!clientHandshakeContext.activeProtocols.contains(protocolVersionValueOf)) {
                throw clientHandshakeContext.conContext.fatal(Alert.PROTOCOL_VERSION, "The server selected protocol version " + ((Object) protocolVersionValueOf) + " is not accepted by client preferences " + ((Object) clientHandshakeContext.activeProtocols));
            }
            if (!protocolVersionValueOf.useTLS13PlusSpec()) {
                throw clientHandshakeContext.conContext.fatal(Alert.PROTOCOL_VERSION, "Unexpected HelloRetryRequest for " + protocolVersionValueOf.name);
            }
            clientHandshakeContext.negotiatedProtocol = protocolVersionValueOf;
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Negotiated protocol version: " + protocolVersionValueOf.name, new Object[0]);
            }
            clientHandshakeContext.handshakePossessions.clear();
            ServerHello.t13HrrHandshakeConsumer.consume(clientHandshakeContext, serverHelloMessage);
        }

        private void onServerHello(ClientHandshakeContext clientHandshakeContext, ServerHelloMessage serverHelloMessage) throws IOException {
            ProtocolVersion protocolVersionValueOf;
            serverHelloMessage.extensions.consumeOnLoad(clientHandshakeContext, new SSLExtension[]{SSLExtension.SH_SUPPORTED_VERSIONS});
            SupportedVersionsExtension.SHSupportedVersionsSpec sHSupportedVersionsSpec = (SupportedVersionsExtension.SHSupportedVersionsSpec) clientHandshakeContext.handshakeExtensions.get(SSLExtension.SH_SUPPORTED_VERSIONS);
            if (sHSupportedVersionsSpec != null) {
                protocolVersionValueOf = ProtocolVersion.valueOf(sHSupportedVersionsSpec.selectedVersion);
            } else {
                protocolVersionValueOf = serverHelloMessage.serverVersion;
            }
            if (!clientHandshakeContext.activeProtocols.contains(protocolVersionValueOf)) {
                throw clientHandshakeContext.conContext.fatal(Alert.PROTOCOL_VERSION, "The server selected protocol version " + ((Object) protocolVersionValueOf) + " is not accepted by client preferences " + ((Object) clientHandshakeContext.activeProtocols));
            }
            clientHandshakeContext.negotiatedProtocol = protocolVersionValueOf;
            if (!clientHandshakeContext.conContext.isNegotiated) {
                clientHandshakeContext.conContext.protocolVersion = clientHandshakeContext.negotiatedProtocol;
                clientHandshakeContext.conContext.outputRecord.setVersion(clientHandshakeContext.negotiatedProtocol);
            }
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Negotiated protocol version: " + protocolVersionValueOf.name, new Object[0]);
            }
            if (serverHelloMessage.serverRandom.isVersionDowngrade(clientHandshakeContext)) {
                throw clientHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "A potential protocol version downgrade attack");
            }
            if (protocolVersionValueOf.useTLS13PlusSpec()) {
                ServerHello.t13HandshakeConsumer.consume(clientHandshakeContext, serverHelloMessage);
            } else {
                clientHandshakeContext.handshakePossessions.clear();
                ServerHello.t12HandshakeConsumer.consume(clientHandshakeContext, serverHelloMessage);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ServerHello$T12ServerHelloConsumer.class */
    private static final class T12ServerHelloConsumer implements HandshakeConsumer {
        private T12ServerHelloConsumer() {
        }

        @Override // sun.security.ssl.HandshakeConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            Subject subject;
            final ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            ServerHelloMessage serverHelloMessage = (ServerHelloMessage) handshakeMessage;
            if (!clientHandshakeContext.isNegotiable(serverHelloMessage.serverVersion)) {
                throw clientHandshakeContext.conContext.fatal(Alert.PROTOCOL_VERSION, "Server chose " + ((Object) serverHelloMessage.serverVersion) + ", but that protocol version is not enabled or not supported by the client.");
            }
            clientHandshakeContext.negotiatedCipherSuite = serverHelloMessage.cipherSuite;
            clientHandshakeContext.handshakeHash.determine(clientHandshakeContext.negotiatedProtocol, clientHandshakeContext.negotiatedCipherSuite);
            clientHandshakeContext.serverHelloRandom = serverHelloMessage.serverRandom;
            if (clientHandshakeContext.negotiatedCipherSuite.keyExchange == null) {
                throw clientHandshakeContext.conContext.fatal(Alert.PROTOCOL_VERSION, "TLS 1.2 or prior version does not support the server cipher suite: " + clientHandshakeContext.negotiatedCipherSuite.name);
            }
            serverHelloMessage.extensions.consumeOnLoad(clientHandshakeContext, new SSLExtension[]{SSLExtension.SH_RENEGOTIATION_INFO});
            if (clientHandshakeContext.resumingSession != null) {
                if (serverHelloMessage.sessionId.equals(clientHandshakeContext.resumingSession.getSessionId())) {
                    CipherSuite suite = clientHandshakeContext.resumingSession.getSuite();
                    if (clientHandshakeContext.negotiatedCipherSuite != suite) {
                        throw clientHandshakeContext.conContext.fatal(Alert.PROTOCOL_VERSION, "Server returned wrong cipher suite for session");
                    }
                    if (clientHandshakeContext.negotiatedProtocol != clientHandshakeContext.resumingSession.getProtocolVersion()) {
                        throw clientHandshakeContext.conContext.fatal(Alert.PROTOCOL_VERSION, "Server resumed with wrong protocol version");
                    }
                    if (suite.keyExchange == CipherSuite.KeyExchange.K_KRB5 || suite.keyExchange == CipherSuite.KeyExchange.K_KRB5_EXPORT) {
                        Principal localPrincipal = clientHandshakeContext.resumingSession.getLocalPrincipal();
                        try {
                            subject = (Subject) AccessController.doPrivileged(new PrivilegedExceptionAction<Subject>() { // from class: sun.security.ssl.ServerHello.T12ServerHelloConsumer.1
                                /* JADX WARN: Can't rename method to resolve collision */
                                @Override // java.security.PrivilegedExceptionAction
                                public Subject run() throws Exception {
                                    return Krb5Helper.getClientSubject(clientHandshakeContext.conContext.acc);
                                }
                            });
                        } catch (PrivilegedActionException e2) {
                            subject = null;
                            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake,verbose")) {
                                SSLLogger.fine("Attempt to obtain subject failed!", new Object[0]);
                            }
                        }
                        if (subject != null) {
                            if (!subject.getPrincipals(Principal.class).contains(localPrincipal)) {
                                throw new SSLProtocolException("Server resumed session with wrong subject identity");
                            }
                            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake,verbose")) {
                                SSLLogger.fine("Subject identity is same", new Object[0]);
                            }
                        } else {
                            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake,verbose")) {
                                SSLLogger.fine("Kerberos credentials are not present in the current Subject; check if javax.security.auth.useSubjectCredsOnly system property has been set to false", new Object[0]);
                            }
                            throw new SSLProtocolException("Server resumed session with no subject");
                        }
                    }
                    clientHandshakeContext.isResumption = true;
                    clientHandshakeContext.resumingSession.setAsSessionResumption(true);
                    clientHandshakeContext.handshakeSession = clientHandshakeContext.resumingSession;
                } else {
                    if (clientHandshakeContext.resumingSession != null) {
                        clientHandshakeContext.resumingSession.invalidate();
                        clientHandshakeContext.resumingSession = null;
                    }
                    clientHandshakeContext.isResumption = false;
                    if (!clientHandshakeContext.sslConfig.enableSessionCreation) {
                        throw clientHandshakeContext.conContext.fatal(Alert.PROTOCOL_VERSION, "New session creation is disabled");
                    }
                }
            }
            SSLExtension[] enabledExtensions = clientHandshakeContext.sslConfig.getEnabledExtensions(SSLHandshake.SERVER_HELLO);
            serverHelloMessage.extensions.consumeOnLoad(clientHandshakeContext, enabledExtensions);
            if (!clientHandshakeContext.isResumption) {
                if (clientHandshakeContext.resumingSession != null) {
                    clientHandshakeContext.resumingSession.invalidate();
                    clientHandshakeContext.resumingSession = null;
                }
                if (!clientHandshakeContext.sslConfig.enableSessionCreation) {
                    throw clientHandshakeContext.conContext.fatal(Alert.PROTOCOL_VERSION, "New session creation is disabled");
                }
                clientHandshakeContext.handshakeSession = new SSLSessionImpl(clientHandshakeContext, clientHandshakeContext.negotiatedCipherSuite, serverHelloMessage.sessionId);
                clientHandshakeContext.handshakeSession.setMaximumPacketSize(clientHandshakeContext.sslConfig.maximumPacketSize);
            }
            serverHelloMessage.extensions.consumeOnTrade(clientHandshakeContext, enabledExtensions);
            if (clientHandshakeContext.isResumption) {
                SSLTrafficKeyDerivation sSLTrafficKeyDerivationValueOf = SSLTrafficKeyDerivation.valueOf(clientHandshakeContext.negotiatedProtocol);
                if (sSLTrafficKeyDerivationValueOf == null) {
                    throw clientHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Not supported key derivation: " + ((Object) clientHandshakeContext.negotiatedProtocol));
                }
                clientHandshakeContext.handshakeKeyDerivation = sSLTrafficKeyDerivationValueOf.createKeyDerivation(clientHandshakeContext, clientHandshakeContext.resumingSession.getMasterSecret());
                clientHandshakeContext.conContext.consumers.putIfAbsent(Byte.valueOf(ContentType.CHANGE_CIPHER_SPEC.id), ChangeCipherSpec.t10Consumer);
                clientHandshakeContext.handshakeConsumers.put(Byte.valueOf(SSLHandshake.FINISHED.id), SSLHandshake.FINISHED);
                return;
            }
            SSLKeyExchange sSLKeyExchangeValueOf = SSLKeyExchange.valueOf(clientHandshakeContext.negotiatedCipherSuite.keyExchange, clientHandshakeContext.negotiatedProtocol);
            clientHandshakeContext.handshakeKeyExchange = sSLKeyExchangeValueOf;
            if (sSLKeyExchangeValueOf != null) {
                for (SSLHandshake sSLHandshake : sSLKeyExchangeValueOf.getRelatedHandshakers(clientHandshakeContext)) {
                    clientHandshakeContext.handshakeConsumers.put(Byte.valueOf(sSLHandshake.id), sSLHandshake);
                }
            }
            clientHandshakeContext.handshakeConsumers.put(Byte.valueOf(SSLHandshake.SERVER_HELLO_DONE.id), SSLHandshake.SERVER_HELLO_DONE);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void setUpPskKD(HandshakeContext handshakeContext, SecretKey secretKey) throws SSLHandshakeException {
        if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
            SSLLogger.fine("Using PSK to derive early secret", new Object[0]);
        }
        try {
            CipherSuite.HashAlg hashAlg = handshakeContext.negotiatedCipherSuite.hashAlg;
            handshakeContext.handshakeKeyDerivation = new SSLSecretDerivation(handshakeContext, new HKDF(hashAlg.name).extract(new byte[hashAlg.hashLength], secretKey, "TlsEarlySecret"));
        } catch (GeneralSecurityException e2) {
            throw ((SSLHandshakeException) new SSLHandshakeException("Could not generate secret").initCause(e2));
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ServerHello$T13ServerHelloConsumer.class */
    private static final class T13ServerHelloConsumer implements HandshakeConsumer {
        private T13ServerHelloConsumer() {
        }

        @Override // sun.security.ssl.HandshakeConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            ServerHelloMessage serverHelloMessage = (ServerHelloMessage) handshakeMessage;
            if (serverHelloMessage.serverVersion != ProtocolVersion.TLS12) {
                throw clientHandshakeContext.conContext.fatal(Alert.PROTOCOL_VERSION, "The ServerHello.legacy_version field is not TLS 1.2");
            }
            clientHandshakeContext.negotiatedCipherSuite = serverHelloMessage.cipherSuite;
            clientHandshakeContext.handshakeHash.determine(clientHandshakeContext.negotiatedProtocol, clientHandshakeContext.negotiatedCipherSuite);
            clientHandshakeContext.serverHelloRandom = serverHelloMessage.serverRandom;
            SSLExtension[] enabledExtensions = clientHandshakeContext.sslConfig.getEnabledExtensions(SSLHandshake.SERVER_HELLO);
            serverHelloMessage.extensions.consumeOnLoad(clientHandshakeContext, enabledExtensions);
            if (!clientHandshakeContext.isResumption) {
                if (clientHandshakeContext.resumingSession != null) {
                    clientHandshakeContext.resumingSession.invalidate();
                    clientHandshakeContext.resumingSession = null;
                }
                if (!clientHandshakeContext.sslConfig.enableSessionCreation) {
                    throw clientHandshakeContext.conContext.fatal(Alert.PROTOCOL_VERSION, "New session creation is disabled");
                }
                clientHandshakeContext.handshakeSession = new SSLSessionImpl(clientHandshakeContext, clientHandshakeContext.negotiatedCipherSuite, serverHelloMessage.sessionId);
                clientHandshakeContext.handshakeSession.setMaximumPacketSize(clientHandshakeContext.sslConfig.maximumPacketSize);
            } else {
                SecretKey secretKeyConsumePreSharedKey = clientHandshakeContext.resumingSession.consumePreSharedKey();
                if (secretKeyConsumePreSharedKey == null) {
                    throw clientHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "No PSK available. Unable to resume.");
                }
                clientHandshakeContext.handshakeSession = clientHandshakeContext.resumingSession;
                ServerHello.setUpPskKD(clientHandshakeContext, secretKeyConsumePreSharedKey);
            }
            serverHelloMessage.extensions.consumeOnTrade(clientHandshakeContext, enabledExtensions);
            clientHandshakeContext.handshakeHash.update();
            SSLKeyExchange sSLKeyExchange = clientHandshakeContext.handshakeKeyExchange;
            if (sSLKeyExchange == null) {
                throw clientHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Not negotiated key shares");
            }
            SecretKey secretKeyDeriveKey = sSLKeyExchange.createKeyDerivation(clientHandshakeContext).deriveKey("TlsHandshakeSecret", null);
            SSLTrafficKeyDerivation sSLTrafficKeyDerivationValueOf = SSLTrafficKeyDerivation.valueOf(clientHandshakeContext.negotiatedProtocol);
            if (sSLTrafficKeyDerivationValueOf == null) {
                throw clientHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Not supported key derivation: " + ((Object) clientHandshakeContext.negotiatedProtocol));
            }
            SSLSecretDerivation sSLSecretDerivation = new SSLSecretDerivation(clientHandshakeContext, secretKeyDeriveKey);
            SecretKey secretKeyDeriveKey2 = sSLSecretDerivation.deriveKey("TlsServerHandshakeTrafficSecret", null);
            SSLKeyDerivation sSLKeyDerivationCreateKeyDerivation = sSLTrafficKeyDerivationValueOf.createKeyDerivation(clientHandshakeContext, secretKeyDeriveKey2);
            try {
                SSLCipher.SSLReadCipher sSLReadCipherCreateReadCipher = clientHandshakeContext.negotiatedCipherSuite.bulkCipher.createReadCipher(Authenticator.valueOf(clientHandshakeContext.negotiatedProtocol), clientHandshakeContext.negotiatedProtocol, sSLKeyDerivationCreateKeyDerivation.deriveKey("TlsKey", null), new IvParameterSpec(sSLKeyDerivationCreateKeyDerivation.deriveKey("TlsIv", null).getEncoded()), clientHandshakeContext.sslContext.getSecureRandom());
                if (sSLReadCipherCreateReadCipher == null) {
                    throw clientHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Illegal cipher suite (" + ((Object) clientHandshakeContext.negotiatedCipherSuite) + ") and protocol version (" + ((Object) clientHandshakeContext.negotiatedProtocol) + ")");
                }
                clientHandshakeContext.baseReadSecret = secretKeyDeriveKey2;
                clientHandshakeContext.conContext.inputRecord.changeReadCiphers(sSLReadCipherCreateReadCipher);
                SecretKey secretKeyDeriveKey3 = sSLSecretDerivation.deriveKey("TlsClientHandshakeTrafficSecret", null);
                SSLKeyDerivation sSLKeyDerivationCreateKeyDerivation2 = sSLTrafficKeyDerivationValueOf.createKeyDerivation(clientHandshakeContext, secretKeyDeriveKey3);
                try {
                    SSLCipher.SSLWriteCipher sSLWriteCipherCreateWriteCipher = clientHandshakeContext.negotiatedCipherSuite.bulkCipher.createWriteCipher(Authenticator.valueOf(clientHandshakeContext.negotiatedProtocol), clientHandshakeContext.negotiatedProtocol, sSLKeyDerivationCreateKeyDerivation2.deriveKey("TlsKey", null), new IvParameterSpec(sSLKeyDerivationCreateKeyDerivation2.deriveKey("TlsIv", null).getEncoded()), clientHandshakeContext.sslContext.getSecureRandom());
                    if (sSLWriteCipherCreateWriteCipher == null) {
                        throw clientHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Illegal cipher suite (" + ((Object) clientHandshakeContext.negotiatedCipherSuite) + ") and protocol version (" + ((Object) clientHandshakeContext.negotiatedProtocol) + ")");
                    }
                    clientHandshakeContext.baseWriteSecret = secretKeyDeriveKey3;
                    clientHandshakeContext.conContext.outputRecord.changeWriteCiphers(sSLWriteCipherCreateWriteCipher, serverHelloMessage.sessionId.length() != 0);
                    clientHandshakeContext.handshakeKeyDerivation = sSLSecretDerivation;
                    clientHandshakeContext.conContext.consumers.putIfAbsent(Byte.valueOf(ContentType.CHANGE_CIPHER_SPEC.id), ChangeCipherSpec.t13Consumer);
                    clientHandshakeContext.handshakeConsumers.put(Byte.valueOf(SSLHandshake.ENCRYPTED_EXTENSIONS.id), SSLHandshake.ENCRYPTED_EXTENSIONS);
                    clientHandshakeContext.handshakeConsumers.put(Byte.valueOf(SSLHandshake.CERTIFICATE_REQUEST.id), SSLHandshake.CERTIFICATE_REQUEST);
                    clientHandshakeContext.handshakeConsumers.put(Byte.valueOf(SSLHandshake.CERTIFICATE.id), SSLHandshake.CERTIFICATE);
                    clientHandshakeContext.handshakeConsumers.put(Byte.valueOf(SSLHandshake.CERTIFICATE_VERIFY.id), SSLHandshake.CERTIFICATE_VERIFY);
                    clientHandshakeContext.handshakeConsumers.put(Byte.valueOf(SSLHandshake.FINISHED.id), SSLHandshake.FINISHED);
                } catch (GeneralSecurityException e2) {
                    throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Missing cipher algorithm", e2);
                }
            } catch (GeneralSecurityException e3) {
                throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Missing cipher algorithm", e3);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ServerHello$T13HelloRetryRequestConsumer.class */
    private static final class T13HelloRetryRequestConsumer implements HandshakeConsumer {
        private T13HelloRetryRequestConsumer() {
        }

        @Override // sun.security.ssl.HandshakeConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            ServerHelloMessage serverHelloMessage = (ServerHelloMessage) handshakeMessage;
            if (serverHelloMessage.serverVersion != ProtocolVersion.TLS12) {
                throw clientHandshakeContext.conContext.fatal(Alert.PROTOCOL_VERSION, "The HelloRetryRequest.legacy_version is not TLS 1.2");
            }
            clientHandshakeContext.negotiatedCipherSuite = serverHelloMessage.cipherSuite;
            SSLExtension[] enabledExtensions = clientHandshakeContext.sslConfig.getEnabledExtensions(SSLHandshake.HELLO_RETRY_REQUEST);
            serverHelloMessage.extensions.consumeOnLoad(clientHandshakeContext, enabledExtensions);
            serverHelloMessage.extensions.consumeOnTrade(clientHandshakeContext, enabledExtensions);
            clientHandshakeContext.handshakeHash.finish();
            HandshakeOutStream handshakeOutStream = new HandshakeOutStream(null);
            try {
                clientHandshakeContext.initialClientHelloMsg.write(handshakeOutStream);
                clientHandshakeContext.handshakeHash.deliver(handshakeOutStream.toByteArray());
                clientHandshakeContext.handshakeHash.determine(clientHandshakeContext.negotiatedProtocol, clientHandshakeContext.negotiatedCipherSuite);
                byte[] bArrDigest = clientHandshakeContext.handshakeHash.digest();
                int i2 = clientHandshakeContext.negotiatedCipherSuite.hashAlg.hashLength;
                byte[] bArr = new byte[4 + i2];
                bArr[0] = SSLHandshake.MESSAGE_HASH.id;
                bArr[1] = 0;
                bArr[2] = 0;
                bArr[3] = (byte) (i2 & 255);
                System.arraycopy(bArrDigest, 0, bArr, 4, i2);
                clientHandshakeContext.handshakeHash.finish();
                clientHandshakeContext.handshakeHash.deliver(bArr);
                int iRemaining = serverHelloMessage.handshakeRecord.remaining();
                byte[] bArr2 = new byte[4 + iRemaining];
                bArr2[0] = SSLHandshake.HELLO_RETRY_REQUEST.id;
                bArr2[1] = (byte) ((iRemaining >> 16) & 255);
                bArr2[2] = (byte) ((iRemaining >> 8) & 255);
                bArr2[3] = (byte) (iRemaining & 255);
                serverHelloMessage.handshakeRecord.duplicate().get(bArr2, 4, iRemaining);
                clientHandshakeContext.handshakeHash.receive(bArr2);
                clientHandshakeContext.initialClientHelloMsg.extensions.reproduce(clientHandshakeContext, new SSLExtension[]{SSLExtension.CH_COOKIE, SSLExtension.CH_KEY_SHARE, SSLExtension.CH_PRE_SHARED_KEY});
                SSLHandshake.CLIENT_HELLO.produce(connectionContext, serverHelloMessage);
            } catch (IOException e2) {
                throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Failed to construct message hash", e2);
            }
        }
    }
}
