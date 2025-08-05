package sun.security.ssl;

import java.io.IOException;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import sun.security.ssl.Alert;
import sun.security.ssl.SupportedGroupsExtension;

/* loaded from: jsse.jar:sun/security/ssl/TransportContext.class */
class TransportContext implements ConnectionContext {
    final SSLTransport transport;
    final Map<Byte, SSLConsumer> consumers;
    final AccessControlContext acc;
    final SSLContextImpl sslContext;
    final SSLConfiguration sslConfig;
    final InputRecord inputRecord;
    final OutputRecord outputRecord;
    boolean isUnsureMode;
    boolean isNegotiated;
    boolean isBroken;
    boolean isInputCloseNotified;
    boolean peerUserCanceled;
    Exception closeReason;
    Exception delegatedThrown;
    SSLSessionImpl conSession;
    ProtocolVersion protocolVersion;
    String applicationProtocol;
    HandshakeContext handshakeContext;
    boolean secureRenegotiation;
    byte[] clientVerifyData;
    byte[] serverVerifyData;
    List<SupportedGroupsExtension.NamedGroup> serverRequestedNamedGroups;
    CipherSuite cipherSuite;
    private static final byte[] emptyByteArray = new byte[0];

    TransportContext(SSLContextImpl sSLContextImpl, SSLTransport sSLTransport, InputRecord inputRecord, OutputRecord outputRecord) {
        this(sSLContextImpl, sSLTransport, new SSLConfiguration(sSLContextImpl, true), inputRecord, outputRecord, true);
    }

    TransportContext(SSLContextImpl sSLContextImpl, SSLTransport sSLTransport, InputRecord inputRecord, OutputRecord outputRecord, boolean z2) {
        this(sSLContextImpl, sSLTransport, new SSLConfiguration(sSLContextImpl, z2), inputRecord, outputRecord, false);
    }

    TransportContext(SSLContextImpl sSLContextImpl, SSLTransport sSLTransport, SSLConfiguration sSLConfiguration, InputRecord inputRecord, OutputRecord outputRecord) {
        this(sSLContextImpl, sSLTransport, (SSLConfiguration) sSLConfiguration.clone(), inputRecord, outputRecord, false);
    }

    private TransportContext(SSLContextImpl sSLContextImpl, SSLTransport sSLTransport, SSLConfiguration sSLConfiguration, InputRecord inputRecord, OutputRecord outputRecord, boolean z2) {
        this.isNegotiated = false;
        this.isBroken = false;
        this.isInputCloseNotified = false;
        this.peerUserCanceled = false;
        this.closeReason = null;
        this.delegatedThrown = null;
        this.applicationProtocol = null;
        this.handshakeContext = null;
        this.secureRenegotiation = false;
        this.transport = sSLTransport;
        this.sslContext = sSLContextImpl;
        this.inputRecord = inputRecord;
        this.outputRecord = outputRecord;
        this.sslConfig = sSLConfiguration;
        if (this.sslConfig.maximumPacketSize == 0) {
            this.sslConfig.maximumPacketSize = outputRecord.getMaxPacketSize();
        }
        this.isUnsureMode = z2;
        this.conSession = new SSLSessionImpl();
        this.protocolVersion = this.sslConfig.maximumProtocolVersion;
        this.clientVerifyData = emptyByteArray;
        this.serverVerifyData = emptyByteArray;
        this.acc = AccessController.getContext();
        this.consumers = new HashMap();
    }

    void dispatch(Plaintext plaintext) throws IOException {
        if (plaintext == null) {
            return;
        }
        ContentType contentTypeValueOf = ContentType.valueOf(plaintext.contentType);
        if (contentTypeValueOf == null) {
            throw fatal(Alert.UNEXPECTED_MESSAGE, "Unknown content type: " + ((int) plaintext.contentType));
        }
        switch (contentTypeValueOf) {
            case HANDSHAKE:
                byte handshakeType = HandshakeContext.getHandshakeType(this, plaintext);
                if (this.handshakeContext == null) {
                    if (handshakeType == SSLHandshake.KEY_UPDATE.id || handshakeType == SSLHandshake.NEW_SESSION_TICKET.id) {
                        if (!this.isNegotiated) {
                            throw fatal(Alert.UNEXPECTED_MESSAGE, "Unexpected unnegotiated post-handshake message: " + SSLHandshake.nameOf(handshakeType));
                        }
                        if (!PostHandshakeContext.isConsumable(this, handshakeType)) {
                            throw fatal(Alert.UNEXPECTED_MESSAGE, "Unexpected post-handshake message: " + SSLHandshake.nameOf(handshakeType));
                        }
                        this.handshakeContext = new PostHandshakeContext(this);
                    } else {
                        this.handshakeContext = this.sslConfig.isClientMode ? new ClientHandshakeContext(this.sslContext, this) : new ServerHandshakeContext(this.sslContext, this);
                    }
                }
                this.handshakeContext.dispatch(handshakeType, plaintext);
                return;
            case ALERT:
                Alert.alertConsumer.consume(this, plaintext.fragment);
                return;
            default:
                SSLConsumer sSLConsumer = this.consumers.get(Byte.valueOf(plaintext.contentType));
                if (sSLConsumer != null) {
                    sSLConsumer.consume(this, plaintext.fragment);
                    return;
                }
                throw fatal(Alert.UNEXPECTED_MESSAGE, "Unexpected content: " + ((int) plaintext.contentType));
        }
    }

    void kickstart() throws IOException {
        boolean z2;
        if (this.isUnsureMode) {
            throw new IllegalStateException("Client/Server mode not yet set.");
        }
        if (this.outputRecord.writeCipher.atKeyLimit()) {
            z2 = this.outputRecord.isClosed() || this.isBroken;
        } else {
            z2 = this.outputRecord.isClosed() || this.inputRecord.isClosed() || this.isBroken;
        }
        if (z2) {
            if (this.closeReason != null) {
                throw new SSLException("Cannot kickstart, the connection is broken or closed", this.closeReason);
            }
            throw new SSLException("Cannot kickstart, the connection is broken or closed");
        }
        if (this.handshakeContext == null) {
            if (this.isNegotiated && this.protocolVersion.useTLS13PlusSpec()) {
                this.handshakeContext = new PostHandshakeContext(this);
            } else {
                this.handshakeContext = this.sslConfig.isClientMode ? new ClientHandshakeContext(this.sslContext, this) : new ServerHandshakeContext(this.sslContext, this);
            }
        }
        if (this.isNegotiated || this.sslConfig.isClientMode) {
            this.handshakeContext.kickstart();
        }
    }

    boolean isPostHandshakeContext() {
        return this.handshakeContext != null && (this.handshakeContext instanceof PostHandshakeContext);
    }

    void warning(Alert alert) {
        if (this.isNegotiated || this.handshakeContext != null) {
            try {
                this.outputRecord.encodeAlert(Alert.Level.WARNING.level, alert.id);
            } catch (IOException e2) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                    SSLLogger.warning("Warning: failed to send warning alert " + ((Object) alert), e2);
                }
            }
        }
    }

    SSLException fatal(Alert alert, String str) throws SSLException {
        return fatal(alert, str, null);
    }

    SSLException fatal(Alert alert, Throwable th) throws SSLException {
        return fatal(alert, null, th);
    }

    SSLException fatal(Alert alert, String str, Throwable th) throws SSLException {
        return fatal(alert, str, false, th);
    }

    SSLException fatal(Alert alert, String str, boolean z2, Throwable th) throws SSLException {
        if (this.closeReason != null) {
            if (th == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                    SSLLogger.warning("Closed transport, general or untracked problem", new Object[0]);
                }
                throw alert.createSSLException("Closed transport, general or untracked problem");
            }
            if (th instanceof SSLException) {
                throw ((SSLException) th);
            }
            if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                SSLLogger.warning("Closed transport, unexpected rethrowing", th);
            }
            throw alert.createSSLException("Unexpected rethrowing", th);
        }
        if (str == null) {
            if (th == null) {
                str = "General/Untracked problem";
            } else {
                str = th.getMessage();
            }
        }
        if (th == null) {
            th = alert.createSSLException(str);
        }
        if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
            SSLLogger.severe("Fatal (" + ((Object) alert) + "): " + str, th);
        }
        if (th instanceof SSLException) {
            this.closeReason = (SSLException) th;
        } else {
            this.closeReason = alert.createSSLException(str, th);
        }
        try {
            this.inputRecord.close();
        } catch (IOException e2) {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                SSLLogger.warning("Fatal: input record closure failed", e2);
            }
            this.closeReason.addSuppressed(e2);
        }
        if (this.conSession != null) {
            this.conSession.invalidate();
        }
        if (this.handshakeContext != null && this.handshakeContext.handshakeSession != null) {
            this.handshakeContext.handshakeSession.invalidate();
        }
        if (!z2 && !isOutboundClosed() && !this.isBroken && (this.isNegotiated || this.handshakeContext != null)) {
            try {
                this.outputRecord.encodeAlert(Alert.Level.FATAL.level, alert.id);
            } catch (IOException e3) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                    SSLLogger.warning("Fatal: failed to send fatal alert " + ((Object) alert), e3);
                }
                this.closeReason.addSuppressed(e3);
            }
        }
        try {
            this.outputRecord.close();
        } catch (IOException e4) {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                SSLLogger.warning("Fatal: output record closure failed", e4);
            }
            this.closeReason.addSuppressed(e4);
        }
        if (this.handshakeContext != null) {
            this.handshakeContext = null;
        }
        try {
            try {
                this.transport.shutdown();
                this.isBroken = true;
            } catch (IOException e5) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                    SSLLogger.warning("Fatal: transport closure failed", e5);
                }
                this.closeReason.addSuppressed(e5);
                this.isBroken = true;
            }
            if (this.closeReason instanceof SSLException) {
                throw ((SSLException) this.closeReason);
            }
            throw ((RuntimeException) this.closeReason);
        } catch (Throwable th2) {
            this.isBroken = true;
            throw th2;
        }
    }

    void setUseClientMode(boolean z2) {
        if (this.handshakeContext != null || this.isNegotiated) {
            throw new IllegalArgumentException("Cannot change mode after SSL traffic has started");
        }
        if (this.sslConfig.isClientMode != z2) {
            if (this.sslContext.isDefaultProtocolVesions(this.sslConfig.enabledProtocols)) {
                this.sslConfig.enabledProtocols = this.sslContext.getDefaultProtocolVersions(!z2);
            }
            if (this.sslContext.isDefaultCipherSuiteList(this.sslConfig.enabledCipherSuites)) {
                this.sslConfig.enabledCipherSuites = this.sslContext.getDefaultCipherSuites(!z2);
            }
            this.sslConfig.toggleClientMode();
        }
        this.isUnsureMode = false;
    }

    boolean isOutboundDone() {
        return this.outputRecord.isClosed() && this.outputRecord.isEmpty();
    }

    boolean isOutboundClosed() {
        return this.outputRecord.isClosed();
    }

    boolean isInboundClosed() {
        return this.inputRecord.isClosed();
    }

    void closeInbound() throws SSLException {
        if (isInboundClosed()) {
            return;
        }
        try {
            if (!this.isInputCloseNotified) {
                initiateInboundClose();
            } else {
                passiveInboundClose();
            }
        } catch (IOException e2) {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                SSLLogger.warning("inbound closure failed", e2);
            }
        }
    }

    private void passiveInboundClose() throws IOException {
        ProtocolVersion protocolVersion;
        if (!isInboundClosed()) {
            this.inputRecord.close();
        }
        if (!isOutboundClosed()) {
            boolean z2 = SSLConfiguration.acknowledgeCloseNotify;
            if (!z2) {
                if (this.isNegotiated) {
                    if (!this.protocolVersion.useTLS13PlusSpec()) {
                        z2 = true;
                    }
                } else if (this.handshakeContext != null && ((protocolVersion = this.handshakeContext.negotiatedProtocol) == null || !protocolVersion.useTLS13PlusSpec())) {
                    z2 = true;
                }
            }
            if (z2) {
                synchronized (this.outputRecord) {
                    try {
                        warning(Alert.CLOSE_NOTIFY);
                        this.outputRecord.close();
                    } catch (Throwable th) {
                        this.outputRecord.close();
                        throw th;
                    }
                }
            }
        }
    }

    private void initiateInboundClose() throws IOException {
        if (!isInboundClosed()) {
            this.inputRecord.close();
        }
    }

    void closeOutbound() {
        if (isOutboundClosed()) {
            return;
        }
        try {
            initiateOutboundClose();
        } catch (IOException e2) {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                SSLLogger.warning("outbound closure failed", e2);
            }
        }
    }

    private void initiateOutboundClose() throws IOException {
        boolean z2 = false;
        if (!this.isNegotiated && this.handshakeContext != null && !this.peerUserCanceled) {
            z2 = true;
        }
        synchronized (this.outputRecord) {
            if (z2) {
                try {
                    warning(Alert.USER_CANCELED);
                    warning(Alert.CLOSE_NOTIFY);
                    this.outputRecord.close();
                } catch (Throwable th) {
                    this.outputRecord.close();
                    throw th;
                }
            } else {
                warning(Alert.CLOSE_NOTIFY);
                this.outputRecord.close();
            }
        }
    }

    SSLEngineResult.HandshakeStatus getHandshakeStatus() {
        if (!this.outputRecord.isEmpty()) {
            return SSLEngineResult.HandshakeStatus.NEED_WRAP;
        }
        if (isOutboundClosed() && isInboundClosed()) {
            return SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
        }
        if (this.handshakeContext != null) {
            if (!this.handshakeContext.delegatedActions.isEmpty()) {
                return SSLEngineResult.HandshakeStatus.NEED_TASK;
            }
            if (!isInboundClosed()) {
                return SSLEngineResult.HandshakeStatus.NEED_UNWRAP;
            }
            if (!isOutboundClosed()) {
                return SSLEngineResult.HandshakeStatus.NEED_WRAP;
            }
        }
        return SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
    }

    SSLEngineResult.HandshakeStatus finishHandshake() {
        if (this.protocolVersion.useTLS13PlusSpec()) {
            this.outputRecord.tc = this;
            this.inputRecord.tc = this;
            this.cipherSuite = this.handshakeContext.negotiatedCipherSuite;
            this.inputRecord.readCipher.baseSecret = this.handshakeContext.baseReadSecret;
            this.outputRecord.writeCipher.baseSecret = this.handshakeContext.baseWriteSecret;
        }
        this.handshakeContext = null;
        this.outputRecord.handshakeHash.finish();
        this.isNegotiated = true;
        if ((this.transport instanceof SSLSocket) && this.sslConfig.handshakeListeners != null && !this.sslConfig.handshakeListeners.isEmpty()) {
            new Thread(null, new NotifyHandshake(this.sslConfig.handshakeListeners, new HandshakeCompletedEvent((SSLSocket) this.transport, this.conSession)), "HandshakeCompletedNotify-Thread", 0L).start();
        }
        return SSLEngineResult.HandshakeStatus.FINISHED;
    }

    SSLEngineResult.HandshakeStatus finishPostHandshake() {
        this.handshakeContext = null;
        return SSLEngineResult.HandshakeStatus.FINISHED;
    }

    /* loaded from: jsse.jar:sun/security/ssl/TransportContext$NotifyHandshake.class */
    private static class NotifyHandshake implements Runnable {
        private final Set<Map.Entry<HandshakeCompletedListener, AccessControlContext>> targets;
        private final HandshakeCompletedEvent event;

        NotifyHandshake(Map<HandshakeCompletedListener, AccessControlContext> map, HandshakeCompletedEvent handshakeCompletedEvent) {
            this.targets = new HashSet(map.entrySet());
            this.event = handshakeCompletedEvent;
        }

        @Override // java.lang.Runnable
        public void run() {
            for (Map.Entry<HandshakeCompletedListener, AccessControlContext> entry : this.targets) {
                final HandshakeCompletedListener key = entry.getKey();
                AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.security.ssl.TransportContext.NotifyHandshake.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public Void run2() {
                        key.handshakeCompleted(NotifyHandshake.this.event);
                        return null;
                    }
                }, entry.getValue());
            }
        }
    }
}
