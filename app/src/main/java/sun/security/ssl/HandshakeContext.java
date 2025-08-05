package sun.security.ssl;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.security.AlgorithmConstraints;
import java.security.CryptoPrimitive;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import javax.crypto.SecretKey;
import javax.net.ssl.SNIServerName;
import javax.net.ssl.SSLHandshakeException;
import javax.security.auth.x500.X500Principal;
import sun.security.ssl.SSLExtension;
import sun.security.ssl.SupportedGroupsExtension;

/* loaded from: jsse.jar:sun/security/ssl/HandshakeContext.class */
abstract class HandshakeContext implements ConnectionContext {
    static final boolean allowUnsafeRenegotiation = Utilities.getBooleanProperty("sun.security.ssl.allowUnsafeRenegotiation", false);
    static final boolean allowLegacyHelloMessages = Utilities.getBooleanProperty("sun.security.ssl.allowLegacyHelloMessages", true);
    LinkedHashMap<Byte, SSLConsumer> handshakeConsumers;
    final HashMap<Byte, HandshakeProducer> handshakeProducers;
    final SSLContextImpl sslContext;
    final TransportContext conContext;
    final SSLConfiguration sslConfig;
    final List<ProtocolVersion> activeProtocols;
    final List<CipherSuite> activeCipherSuites;
    final AlgorithmConstraints algorithmConstraints;
    final ProtocolVersion maximumActiveProtocol;
    final HandshakeOutStream handshakeOutput;
    final HandshakeHash handshakeHash;
    SSLSessionImpl handshakeSession;
    boolean handshakeFinished;
    boolean kickstartMessageDelivered;
    boolean isResumption;
    SSLSessionImpl resumingSession;
    final Queue<Map.Entry<Byte, ByteBuffer>> delegatedActions;
    volatile boolean taskDelegated;
    volatile Exception delegatedThrown;
    ProtocolVersion negotiatedProtocol;
    CipherSuite negotiatedCipherSuite;
    final List<SSLPossession> handshakePossessions;
    final List<SSLCredentials> handshakeCredentials;
    SSLKeyDerivation handshakeKeyDerivation;
    SSLKeyExchange handshakeKeyExchange;
    SecretKey baseReadSecret;
    SecretKey baseWriteSecret;
    int clientHelloVersion;
    String applicationProtocol;
    RandomCookie clientHelloRandom;
    RandomCookie serverHelloRandom;
    byte[] certRequestContext;
    final Map<SSLExtension, SSLExtension.SSLExtensionSpec> handshakeExtensions;
    int maxFragmentLength;
    List<SignatureScheme> localSupportedSignAlgs;
    List<SignatureScheme> peerRequestedSignatureSchemes;
    List<SignatureScheme> peerRequestedCertSignSchemes;
    X500Principal[] peerSupportedAuthorities;
    List<SupportedGroupsExtension.NamedGroup> clientRequestedNamedGroups;
    SupportedGroupsExtension.NamedGroup serverSelectedNamedGroup;
    List<SNIServerName> requestedServerNames;
    SNIServerName negotiatedServerName;
    boolean staplingActive;

    abstract void kickstart() throws IOException;

    protected HandshakeContext(SSLContextImpl sSLContextImpl, TransportContext transportContext) throws IOException {
        this.taskDelegated = false;
        this.delegatedThrown = null;
        this.peerSupportedAuthorities = null;
        this.staplingActive = false;
        this.sslContext = sSLContextImpl;
        this.conContext = transportContext;
        this.sslConfig = (SSLConfiguration) transportContext.sslConfig.clone();
        this.algorithmConstraints = new SSLAlgorithmConstraints(this.sslConfig.userSpecifiedAlgorithmConstraints);
        this.activeProtocols = getActiveProtocols(this.sslConfig.enabledProtocols, this.sslConfig.enabledCipherSuites, this.algorithmConstraints);
        if (this.activeProtocols.isEmpty()) {
            throw new SSLHandshakeException("No appropriate protocol (protocol is disabled or cipher suites are inappropriate)");
        }
        ProtocolVersion protocolVersion = ProtocolVersion.NONE;
        for (ProtocolVersion protocolVersion2 : this.activeProtocols) {
            if (protocolVersion == ProtocolVersion.NONE || protocolVersion2.compare(protocolVersion) > 0) {
                protocolVersion = protocolVersion2;
            }
        }
        this.maximumActiveProtocol = protocolVersion;
        this.activeCipherSuites = getActiveCipherSuites(this.activeProtocols, this.sslConfig.enabledCipherSuites, this.algorithmConstraints);
        if (this.activeCipherSuites.isEmpty()) {
            throw new SSLHandshakeException("No appropriate cipher suite");
        }
        this.handshakeConsumers = new LinkedHashMap<>();
        this.handshakeProducers = new HashMap<>();
        this.handshakeHash = transportContext.inputRecord.handshakeHash;
        this.handshakeOutput = new HandshakeOutStream(transportContext.outputRecord);
        this.handshakeFinished = false;
        this.kickstartMessageDelivered = false;
        this.delegatedActions = new LinkedList();
        this.handshakeExtensions = new HashMap();
        this.handshakePossessions = new LinkedList();
        this.handshakeCredentials = new LinkedList();
        this.requestedServerNames = null;
        this.negotiatedServerName = null;
        this.negotiatedCipherSuite = transportContext.cipherSuite;
        initialize();
    }

    protected HandshakeContext(TransportContext transportContext) {
        this.taskDelegated = false;
        this.delegatedThrown = null;
        this.peerSupportedAuthorities = null;
        this.staplingActive = false;
        this.sslContext = transportContext.sslContext;
        this.conContext = transportContext;
        this.sslConfig = transportContext.sslConfig;
        this.negotiatedProtocol = transportContext.protocolVersion;
        this.negotiatedCipherSuite = transportContext.cipherSuite;
        this.handshakeOutput = new HandshakeOutStream(transportContext.outputRecord);
        this.delegatedActions = new LinkedList();
        this.handshakeConsumers = new LinkedHashMap<>();
        this.handshakeProducers = null;
        this.handshakeHash = null;
        this.activeProtocols = null;
        this.activeCipherSuites = null;
        this.algorithmConstraints = null;
        this.maximumActiveProtocol = null;
        this.handshakeExtensions = Collections.emptyMap();
        this.handshakePossessions = null;
        this.handshakeCredentials = null;
    }

    private void initialize() {
        ProtocolVersion protocolVersion;
        ProtocolVersion protocolVersion2;
        if (this.conContext.isNegotiated) {
            protocolVersion = this.conContext.protocolVersion;
            protocolVersion2 = this.conContext.protocolVersion;
        } else if (this.activeProtocols.contains(ProtocolVersion.SSL20Hello)) {
            protocolVersion = ProtocolVersion.SSL20Hello;
            if (this.maximumActiveProtocol.useTLS13PlusSpec()) {
                protocolVersion2 = this.maximumActiveProtocol;
            } else {
                protocolVersion2 = ProtocolVersion.SSL20Hello;
            }
        } else {
            protocolVersion = this.maximumActiveProtocol;
            protocolVersion2 = this.maximumActiveProtocol;
        }
        this.conContext.inputRecord.setHelloVersion(protocolVersion);
        this.conContext.outputRecord.setHelloVersion(protocolVersion2);
        if (!this.conContext.isNegotiated) {
            this.conContext.protocolVersion = this.maximumActiveProtocol;
        }
        this.conContext.outputRecord.setVersion(this.conContext.protocolVersion);
    }

    private static List<ProtocolVersion> getActiveProtocols(List<ProtocolVersion> list, List<CipherSuite> list2, AlgorithmConstraints algorithmConstraints) {
        boolean z2 = false;
        ArrayList arrayList = new ArrayList(4);
        for (ProtocolVersion protocolVersion : list) {
            if (!z2 && protocolVersion == ProtocolVersion.SSL20Hello) {
                z2 = true;
            } else if (algorithmConstraints.permits(EnumSet.of(CryptoPrimitive.KEY_AGREEMENT), protocolVersion.name, null)) {
                boolean z3 = false;
                EnumMap enumMap = new EnumMap(SupportedGroupsExtension.NamedGroupType.class);
                Iterator<CipherSuite> it = list2.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    CipherSuite next = it.next();
                    if (next.isAvailable() && next.supports(protocolVersion)) {
                        if (isActivatable(next, algorithmConstraints, enumMap)) {
                            arrayList.add(protocolVersion);
                            z3 = true;
                            break;
                        }
                    } else if (SSLLogger.isOn && SSLLogger.isOn("verbose")) {
                        SSLLogger.fine("Ignore unsupported cipher suite: " + ((Object) next) + " for " + ((Object) protocolVersion), new Object[0]);
                    }
                }
                if (!z3 && SSLLogger.isOn && SSLLogger.isOn("handshake")) {
                    SSLLogger.fine("No available cipher suite for " + ((Object) protocolVersion), new Object[0]);
                }
            }
        }
        if (!arrayList.isEmpty()) {
            if (z2) {
                arrayList.add(ProtocolVersion.SSL20Hello);
            }
            Collections.sort(arrayList);
        }
        return Collections.unmodifiableList(arrayList);
    }

    private static List<CipherSuite> getActiveCipherSuites(List<ProtocolVersion> list, List<CipherSuite> list2, AlgorithmConstraints algorithmConstraints) {
        LinkedList linkedList = new LinkedList();
        if (list != null && !list.isEmpty()) {
            EnumMap enumMap = new EnumMap(SupportedGroupsExtension.NamedGroupType.class);
            for (CipherSuite cipherSuite : list2) {
                if (cipherSuite.isAvailable()) {
                    boolean z2 = false;
                    Iterator<ProtocolVersion> it = list.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        if (cipherSuite.supports(it.next()) && isActivatable(cipherSuite, algorithmConstraints, enumMap)) {
                            linkedList.add(cipherSuite);
                            z2 = true;
                            break;
                        }
                    }
                    if (!z2 && SSLLogger.isOn && SSLLogger.isOn("verbose")) {
                        SSLLogger.finest("Ignore unsupported cipher suite: " + ((Object) cipherSuite), new Object[0]);
                    }
                }
            }
        }
        return Collections.unmodifiableList(linkedList);
    }

    static byte getHandshakeType(TransportContext transportContext, Plaintext plaintext) throws IOException {
        if (plaintext.contentType != ContentType.HANDSHAKE.id) {
            throw transportContext.fatal(Alert.INTERNAL_ERROR, "Unexpected operation for record: " + ((int) plaintext.contentType));
        }
        if (plaintext.fragment == null || plaintext.fragment.remaining() < 4) {
            throw transportContext.fatal(Alert.UNEXPECTED_MESSAGE, "Invalid handshake message: insufficient data");
        }
        byte int8 = (byte) Record.getInt8(plaintext.fragment);
        if (Record.getInt24(plaintext.fragment) != plaintext.fragment.remaining()) {
            throw transportContext.fatal(Alert.UNEXPECTED_MESSAGE, "Invalid handshake message: insufficient handshake body");
        }
        return int8;
    }

    void dispatch(byte b2, Plaintext plaintext) throws IOException {
        if (this.conContext.transport.useDelegatedTask()) {
            boolean z2 = !this.delegatedActions.isEmpty();
            if (z2 || (b2 != SSLHandshake.FINISHED.id && b2 != SSLHandshake.KEY_UPDATE.id && b2 != SSLHandshake.NEW_SESSION_TICKET.id)) {
                if (!z2) {
                    this.taskDelegated = false;
                    this.delegatedThrown = null;
                }
                ByteBuffer byteBufferWrap = ByteBuffer.wrap(new byte[plaintext.fragment.remaining()]);
                byteBufferWrap.put(plaintext.fragment);
                this.delegatedActions.add(new AbstractMap.SimpleImmutableEntry(Byte.valueOf(b2), (ByteBuffer) byteBufferWrap.rewind()));
                return;
            }
            dispatch(b2, plaintext.fragment);
            return;
        }
        dispatch(b2, plaintext.fragment);
    }

    void dispatch(byte b2, ByteBuffer byteBuffer) throws IOException {
        SSLConsumer sSLConsumer;
        if (b2 == SSLHandshake.HELLO_REQUEST.id) {
            sSLConsumer = SSLHandshake.HELLO_REQUEST;
        } else {
            sSLConsumer = this.handshakeConsumers.get(Byte.valueOf(b2));
        }
        if (sSLConsumer == null) {
            throw this.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unexpected handshake message: " + SSLHandshake.nameOf(b2));
        }
        try {
            sSLConsumer.consume(this, byteBuffer);
            this.handshakeHash.consume();
        } catch (UnsupportedOperationException e2) {
            throw this.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unsupported handshake message: " + SSLHandshake.nameOf(b2), e2);
        } catch (BufferOverflowException | BufferUnderflowException e3) {
            throw this.conContext.fatal(Alert.DECODE_ERROR, "Illegal handshake message: " + SSLHandshake.nameOf(b2), e3);
        }
    }

    boolean isNegotiable(CipherSuite cipherSuite) {
        return isNegotiable(this.activeCipherSuites, cipherSuite);
    }

    static final boolean isNegotiable(List<CipherSuite> list, CipherSuite cipherSuite) {
        return list.contains(cipherSuite) && cipherSuite.isNegotiable();
    }

    static final boolean isNegotiable(List<CipherSuite> list, ProtocolVersion protocolVersion, CipherSuite cipherSuite) {
        return list.contains(cipherSuite) && cipherSuite.isNegotiable() && cipherSuite.supports(protocolVersion);
    }

    boolean isNegotiable(ProtocolVersion protocolVersion) {
        return this.activeProtocols.contains(protocolVersion);
    }

    private static boolean isActivatable(CipherSuite cipherSuite, AlgorithmConstraints algorithmConstraints, Map<SupportedGroupsExtension.NamedGroupType, Boolean> map) {
        SupportedGroupsExtension.NamedGroupType namedGroupType;
        boolean zBooleanValue;
        if (algorithmConstraints.permits(EnumSet.of(CryptoPrimitive.KEY_AGREEMENT), cipherSuite.name, null)) {
            if (cipherSuite.keyExchange != null && (namedGroupType = cipherSuite.keyExchange.groupType) != SupportedGroupsExtension.NamedGroupType.NAMED_GROUP_NONE) {
                Boolean bool = map.get(namedGroupType);
                if (bool == null) {
                    zBooleanValue = SupportedGroupsExtension.SupportedGroups.isActivatable(algorithmConstraints, namedGroupType);
                    map.put(namedGroupType, Boolean.valueOf(zBooleanValue));
                    if (!zBooleanValue && SSLLogger.isOn && SSLLogger.isOn("verbose")) {
                        SSLLogger.fine("No activated named group", new Object[0]);
                    }
                } else {
                    zBooleanValue = bool.booleanValue();
                }
                if (!zBooleanValue && SSLLogger.isOn && SSLLogger.isOn("verbose")) {
                    SSLLogger.fine("No active named group, ignore " + ((Object) cipherSuite), new Object[0]);
                }
                return zBooleanValue;
            }
            return true;
        }
        if (SSLLogger.isOn && SSLLogger.isOn("verbose")) {
            SSLLogger.fine("Ignore disabled cipher suite: " + ((Object) cipherSuite), new Object[0]);
            return false;
        }
        return false;
    }

    List<SNIServerName> getRequestedServerNames() {
        if (this.requestedServerNames == null) {
            return Collections.emptyList();
        }
        return this.requestedServerNames;
    }
}
