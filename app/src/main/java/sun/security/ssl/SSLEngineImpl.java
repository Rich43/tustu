package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLKeyException;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLProtocolException;
import javax.net.ssl.SSLSession;

/* loaded from: jsse.jar:sun/security/ssl/SSLEngineImpl.class */
final class SSLEngineImpl extends SSLEngine implements SSLTransport {
    private final SSLContextImpl sslContext;
    final TransportContext conContext;

    SSLEngineImpl(SSLContextImpl sSLContextImpl) {
        this(sSLContextImpl, null, -1);
    }

    SSLEngineImpl(SSLContextImpl sSLContextImpl, String str, int i2) {
        super(str, i2);
        this.sslContext = sSLContextImpl;
        HandshakeHash handshakeHash = new HandshakeHash();
        this.conContext = new TransportContext(sSLContextImpl, this, new SSLEngineInputRecord(handshakeHash), new SSLEngineOutputRecord(handshakeHash));
        if (str != null) {
            this.conContext.sslConfig.serverNames = Utilities.addToSNIServerNameList(this.conContext.sslConfig.serverNames, str);
        }
    }

    @Override // javax.net.ssl.SSLEngine
    public synchronized void beginHandshake() throws SSLException {
        if (this.conContext.isUnsureMode) {
            throw new IllegalStateException("Client/Server mode has not yet been set.");
        }
        try {
            this.conContext.kickstart();
        } catch (IOException e2) {
            throw this.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Couldn't kickstart handshaking", e2);
        } catch (Exception e3) {
            throw this.conContext.fatal(Alert.INTERNAL_ERROR, "Fail to begin handshake", e3);
        }
    }

    @Override // javax.net.ssl.SSLEngine
    public synchronized SSLEngineResult wrap(ByteBuffer[] byteBufferArr, int i2, int i3, ByteBuffer byteBuffer) throws SSLException {
        return wrap(byteBufferArr, i2, i3, new ByteBuffer[]{byteBuffer}, 0, 1);
    }

    public synchronized SSLEngineResult wrap(ByteBuffer[] byteBufferArr, int i2, int i3, ByteBuffer[] byteBufferArr2, int i4, int i5) throws SSLException {
        if (this.conContext.isUnsureMode) {
            throw new IllegalStateException("Client/Server mode has not yet been set.");
        }
        checkTaskThrown();
        checkParams(byteBufferArr, i2, i3, byteBufferArr2, i4, i5);
        try {
            return writeRecord(byteBufferArr, i2, i3, byteBufferArr2, i4, i5);
        } catch (SSLProtocolException e2) {
            throw this.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e2);
        } catch (IOException e3) {
            throw this.conContext.fatal(Alert.INTERNAL_ERROR, "problem wrapping app data", e3);
        } catch (Exception e4) {
            throw this.conContext.fatal(Alert.INTERNAL_ERROR, "Fail to wrap application data", e4);
        }
    }

    private SSLEngineResult writeRecord(ByteBuffer[] byteBufferArr, int i2, int i3, ByteBuffer[] byteBufferArr2, int i4, int i5) throws IOException {
        SSLEngineResult.HandshakeStatus handshakeStatus;
        if (isOutboundDone()) {
            return new SSLEngineResult(SSLEngineResult.Status.CLOSED, getHandshakeStatus(), 0, 0);
        }
        HandshakeContext handshakeContext = this.conContext.handshakeContext;
        SSLEngineResult.HandshakeStatus handshakeStatus2 = null;
        if (!this.conContext.isNegotiated && !this.conContext.isBroken && !this.conContext.isInboundClosed() && !this.conContext.isOutboundClosed()) {
            this.conContext.kickstart();
            handshakeStatus2 = getHandshakeStatus();
            if (handshakeStatus2 == SSLEngineResult.HandshakeStatus.NEED_UNWRAP) {
                return new SSLEngineResult(SSLEngineResult.Status.OK, handshakeStatus2, 0, 0);
            }
        }
        if (handshakeStatus2 == null) {
            handshakeStatus2 = getHandshakeStatus();
        }
        if (handshakeStatus2 == SSLEngineResult.HandshakeStatus.NEED_TASK) {
            return new SSLEngineResult(SSLEngineResult.Status.OK, handshakeStatus2, 0, 0);
        }
        int iRemaining = 0;
        for (int i6 = i4; i6 < i4 + i5; i6++) {
            iRemaining += byteBufferArr2[i6].remaining();
        }
        if (iRemaining < this.conContext.conSession.getPacketBufferSize()) {
            return new SSLEngineResult(SSLEngineResult.Status.BUFFER_OVERFLOW, getHandshakeStatus(), 0, 0);
        }
        int iRemaining2 = 0;
        for (int i7 = i2; i7 < i2 + i3; i7++) {
            iRemaining2 += byteBufferArr[i7].remaining();
        }
        Ciphertext ciphertextEncode = null;
        try {
            if (!this.conContext.outputRecord.isEmpty()) {
                ciphertextEncode = encode(null, 0, 0, byteBufferArr2, i4, i5);
            }
            if (ciphertextEncode == null && iRemaining2 != 0) {
                ciphertextEncode = encode(byteBufferArr, i2, i3, byteBufferArr2, i4, i5);
            }
            SSLEngineResult.Status status = isOutboundDone() ? SSLEngineResult.Status.CLOSED : SSLEngineResult.Status.OK;
            if (ciphertextEncode != null && ciphertextEncode.handshakeStatus != null) {
                handshakeStatus = ciphertextEncode.handshakeStatus;
            } else {
                handshakeStatus = getHandshakeStatus();
                if (ciphertextEncode == null && !this.conContext.isNegotiated && this.conContext.isInboundClosed() && handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_WRAP) {
                    status = SSLEngineResult.Status.CLOSED;
                }
            }
            int iRemaining3 = iRemaining2;
            for (int i8 = i2; i8 < i2 + i3; i8++) {
                iRemaining3 -= byteBufferArr[i8].remaining();
            }
            int iRemaining4 = iRemaining;
            for (int i9 = i4; i9 < i4 + i5; i9++) {
                iRemaining4 -= byteBufferArr2[i9].remaining();
            }
            return new SSLEngineResult(status, handshakeStatus, iRemaining3, iRemaining4);
        } catch (IOException e2) {
            if (e2 instanceof SSLException) {
                throw e2;
            }
            throw new SSLException("Write problems", e2);
        }
    }

    private Ciphertext encode(ByteBuffer[] byteBufferArr, int i2, int i3, ByteBuffer[] byteBufferArr2, int i4, int i5) throws IOException {
        try {
            Ciphertext ciphertextEncode = this.conContext.outputRecord.encode(byteBufferArr, i2, i3, byteBufferArr2, i4, i5);
            if (ciphertextEncode == null) {
                return null;
            }
            SSLEngineResult.HandshakeStatus handshakeStatusTryToFinishHandshake = tryToFinishHandshake(ciphertextEncode.contentType);
            if (handshakeStatusTryToFinishHandshake == null) {
                handshakeStatusTryToFinishHandshake = this.conContext.getHandshakeStatus();
            }
            if (this.conContext.outputRecord.seqNumIsHuge() || this.conContext.outputRecord.writeCipher.atKeyLimit()) {
                handshakeStatusTryToFinishHandshake = tryKeyUpdate(handshakeStatusTryToFinishHandshake);
            }
            ciphertextEncode.handshakeStatus = handshakeStatusTryToFinishHandshake;
            return ciphertextEncode;
        } catch (SSLHandshakeException e2) {
            throw this.conContext.fatal(Alert.HANDSHAKE_FAILURE, e2);
        } catch (IOException e3) {
            throw this.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e3);
        }
    }

    private SSLEngineResult.HandshakeStatus tryToFinishHandshake(byte b2) {
        SSLEngineResult.HandshakeStatus handshakeStatusFinishHandshake = null;
        if (b2 == ContentType.HANDSHAKE.id && this.conContext.outputRecord.isEmpty()) {
            if (this.conContext.handshakeContext == null) {
                handshakeStatusFinishHandshake = SSLEngineResult.HandshakeStatus.FINISHED;
            } else if (this.conContext.isPostHandshakeContext()) {
                handshakeStatusFinishHandshake = this.conContext.finishPostHandshake();
            } else if (this.conContext.handshakeContext.handshakeFinished) {
                handshakeStatusFinishHandshake = this.conContext.finishHandshake();
            }
        }
        return handshakeStatusFinishHandshake;
    }

    private SSLEngineResult.HandshakeStatus tryKeyUpdate(SSLEngineResult.HandshakeStatus handshakeStatus) throws IOException {
        if (this.conContext.handshakeContext == null && !this.conContext.isOutboundClosed() && !this.conContext.isBroken) {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                SSLLogger.finest("trigger key update", new Object[0]);
            }
            beginHandshake();
            return this.conContext.getHandshakeStatus();
        }
        return handshakeStatus;
    }

    private static void checkParams(ByteBuffer[] byteBufferArr, int i2, int i3, ByteBuffer[] byteBufferArr2, int i4, int i5) {
        if (byteBufferArr == null || byteBufferArr2 == null) {
            throw new IllegalArgumentException("source or destination buffer is null");
        }
        if (i4 < 0 || i5 < 0 || i4 > byteBufferArr2.length - i5) {
            throw new IndexOutOfBoundsException("index out of bound of the destination buffers");
        }
        if (i2 < 0 || i3 < 0 || i2 > byteBufferArr.length - i3) {
            throw new IndexOutOfBoundsException("index out of bound of the source buffers");
        }
        for (int i6 = i4; i6 < i4 + i5; i6++) {
            if (byteBufferArr2[i6] == null) {
                throw new IllegalArgumentException("destination buffer[" + i6 + "] == null");
            }
            if (byteBufferArr2[i6].isReadOnly()) {
                throw new ReadOnlyBufferException();
            }
        }
        for (int i7 = i2; i7 < i2 + i3; i7++) {
            if (byteBufferArr[i7] == null) {
                throw new IllegalArgumentException("source buffer[" + i7 + "] == null");
            }
        }
    }

    @Override // javax.net.ssl.SSLEngine
    public synchronized SSLEngineResult unwrap(ByteBuffer byteBuffer, ByteBuffer[] byteBufferArr, int i2, int i3) throws SSLException {
        return unwrap(new ByteBuffer[]{byteBuffer}, 0, 1, byteBufferArr, i2, i3);
    }

    public synchronized SSLEngineResult unwrap(ByteBuffer[] byteBufferArr, int i2, int i3, ByteBuffer[] byteBufferArr2, int i4, int i5) throws SSLException {
        if (this.conContext.isUnsureMode) {
            throw new IllegalStateException("Client/Server mode has not yet been set.");
        }
        checkTaskThrown();
        checkParams(byteBufferArr, i2, i3, byteBufferArr2, i4, i5);
        try {
            return readRecord(byteBufferArr, i2, i3, byteBufferArr2, i4, i5);
        } catch (SSLProtocolException e2) {
            throw this.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e2.getMessage(), e2);
        } catch (IOException e3) {
            throw this.conContext.fatal(Alert.INTERNAL_ERROR, "problem unwrapping net record", e3);
        } catch (Exception e4) {
            throw this.conContext.fatal(Alert.INTERNAL_ERROR, "Fail to unwrap network record", e4);
        }
    }

    private SSLEngineResult readRecord(ByteBuffer[] byteBufferArr, int i2, int i3, ByteBuffer[] byteBufferArr2, int i4, int i5) throws IOException {
        SSLEngineResult.HandshakeStatus handshakeStatus;
        if (isInboundDone()) {
            return new SSLEngineResult(SSLEngineResult.Status.CLOSED, getHandshakeStatus(), 0, 0);
        }
        SSLEngineResult.HandshakeStatus handshakeStatus2 = null;
        if (!this.conContext.isNegotiated && !this.conContext.isBroken && !this.conContext.isInboundClosed() && !this.conContext.isOutboundClosed()) {
            this.conContext.kickstart();
            handshakeStatus2 = getHandshakeStatus();
            if (handshakeStatus2 == SSLEngineResult.HandshakeStatus.NEED_WRAP) {
                return new SSLEngineResult(SSLEngineResult.Status.OK, handshakeStatus2, 0, 0);
            }
        }
        if (handshakeStatus2 == null) {
            handshakeStatus2 = getHandshakeStatus();
        }
        if (handshakeStatus2 == SSLEngineResult.HandshakeStatus.NEED_TASK) {
            return new SSLEngineResult(SSLEngineResult.Status.OK, handshakeStatus2, 0, 0);
        }
        int iRemaining = 0;
        for (int i6 = i2; i6 < i2 + i3; i6++) {
            iRemaining += byteBufferArr[i6].remaining();
        }
        if (iRemaining == 0) {
            return new SSLEngineResult(SSLEngineResult.Status.BUFFER_UNDERFLOW, handshakeStatus2, 0, 0);
        }
        int iBytesInCompletePacket = this.conContext.inputRecord.bytesInCompletePacket(byteBufferArr, i2, i3);
        if (iBytesInCompletePacket > this.conContext.conSession.getPacketBufferSize()) {
            if (iBytesInCompletePacket <= 33093) {
                this.conContext.conSession.expandBufferSizes();
            }
            int packetBufferSize = this.conContext.conSession.getPacketBufferSize();
            if (iBytesInCompletePacket > packetBufferSize) {
                throw new SSLProtocolException("Input record too big: max = " + packetBufferSize + " len = " + iBytesInCompletePacket);
            }
        }
        int iRemaining2 = 0;
        for (int i7 = i4; i7 < i4 + i5; i7++) {
            iRemaining2 += byteBufferArr2[i7].remaining();
        }
        if (this.conContext.isNegotiated && this.conContext.inputRecord.estimateFragmentSize(iBytesInCompletePacket) > iRemaining2) {
            return new SSLEngineResult(SSLEngineResult.Status.BUFFER_OVERFLOW, handshakeStatus2, 0, 0);
        }
        if (iBytesInCompletePacket == -1 || iRemaining < iBytesInCompletePacket) {
            return new SSLEngineResult(SSLEngineResult.Status.BUFFER_UNDERFLOW, handshakeStatus2, 0, 0);
        }
        try {
            Plaintext plaintextDecode = decode(byteBufferArr, i2, i3, byteBufferArr2, i4, i5);
            SSLEngineResult.Status status = isInboundDone() ? SSLEngineResult.Status.CLOSED : SSLEngineResult.Status.OK;
            if (plaintextDecode.handshakeStatus != null) {
                handshakeStatus = plaintextDecode.handshakeStatus;
            } else {
                handshakeStatus = getHandshakeStatus();
            }
            int iRemaining3 = iRemaining;
            for (int i8 = i2; i8 < i2 + i3; i8++) {
                iRemaining3 -= byteBufferArr[i8].remaining();
            }
            int iRemaining4 = iRemaining2;
            for (int i9 = i4; i9 < i4 + i5; i9++) {
                iRemaining4 -= byteBufferArr2[i9].remaining();
            }
            return new SSLEngineResult(status, handshakeStatus, iRemaining3, iRemaining4);
        } catch (IOException e2) {
            if (e2 instanceof SSLException) {
                throw e2;
            }
            throw new SSLException("readRecord", e2);
        }
    }

    private Plaintext decode(ByteBuffer[] byteBufferArr, int i2, int i3, ByteBuffer[] byteBufferArr2, int i4, int i5) throws IOException {
        Plaintext plaintextDecode = SSLTransport.decode(this.conContext, byteBufferArr, i2, i3, byteBufferArr2, i4, i5);
        if (plaintextDecode != Plaintext.PLAINTEXT_NULL) {
            SSLEngineResult.HandshakeStatus handshakeStatusTryToFinishHandshake = tryToFinishHandshake(plaintextDecode.contentType);
            if (handshakeStatusTryToFinishHandshake == null) {
                plaintextDecode.handshakeStatus = this.conContext.getHandshakeStatus();
            } else {
                plaintextDecode.handshakeStatus = handshakeStatusTryToFinishHandshake;
            }
            if (this.conContext.inputRecord.seqNumIsHuge() || this.conContext.inputRecord.readCipher.atKeyLimit()) {
                plaintextDecode.handshakeStatus = tryKeyUpdate(plaintextDecode.handshakeStatus);
            }
        }
        return plaintextDecode;
    }

    @Override // javax.net.ssl.SSLEngine
    public synchronized Runnable getDelegatedTask() {
        if (this.conContext.handshakeContext != null && !this.conContext.handshakeContext.taskDelegated && !this.conContext.handshakeContext.delegatedActions.isEmpty()) {
            this.conContext.handshakeContext.taskDelegated = true;
            return new DelegatedTask(this);
        }
        return null;
    }

    @Override // javax.net.ssl.SSLEngine
    public synchronized void closeInbound() throws SSLException {
        if (isInboundDone()) {
            return;
        }
        if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
            SSLLogger.finest("Closing inbound of SSLEngine", new Object[0]);
        }
        if (!this.conContext.isInputCloseNotified && (this.conContext.isNegotiated || this.conContext.handshakeContext != null)) {
            throw this.conContext.fatal(Alert.INTERNAL_ERROR, "closing inbound before receiving peer's close_notify");
        }
        this.conContext.closeInbound();
    }

    @Override // javax.net.ssl.SSLEngine
    public synchronized boolean isInboundDone() {
        return this.conContext.isInboundClosed();
    }

    @Override // javax.net.ssl.SSLEngine
    public synchronized void closeOutbound() {
        if (this.conContext.isOutboundClosed()) {
            return;
        }
        if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
            SSLLogger.finest("Closing outbound of SSLEngine", new Object[0]);
        }
        this.conContext.closeOutbound();
    }

    @Override // javax.net.ssl.SSLEngine
    public synchronized boolean isOutboundDone() {
        return this.conContext.isOutboundDone();
    }

    @Override // javax.net.ssl.SSLEngine
    public String[] getSupportedCipherSuites() {
        return CipherSuite.namesOf(this.sslContext.getSupportedCipherSuites());
    }

    @Override // javax.net.ssl.SSLEngine
    public synchronized String[] getEnabledCipherSuites() {
        return CipherSuite.namesOf(this.conContext.sslConfig.enabledCipherSuites);
    }

    @Override // javax.net.ssl.SSLEngine
    public synchronized void setEnabledCipherSuites(String[] strArr) {
        this.conContext.sslConfig.enabledCipherSuites = CipherSuite.validValuesOf(strArr);
    }

    @Override // javax.net.ssl.SSLEngine
    public String[] getSupportedProtocols() {
        return ProtocolVersion.toStringArray(this.sslContext.getSupportedProtocolVersions());
    }

    @Override // javax.net.ssl.SSLEngine
    public synchronized String[] getEnabledProtocols() {
        return ProtocolVersion.toStringArray(this.conContext.sslConfig.enabledProtocols);
    }

    @Override // javax.net.ssl.SSLEngine
    public synchronized void setEnabledProtocols(String[] strArr) {
        if (strArr == null) {
            throw new IllegalArgumentException("Protocols cannot be null");
        }
        this.conContext.sslConfig.enabledProtocols = ProtocolVersion.namesOf(strArr);
    }

    @Override // javax.net.ssl.SSLEngine
    public synchronized SSLSession getSession() {
        return this.conContext.conSession;
    }

    @Override // javax.net.ssl.SSLEngine
    public synchronized SSLSession getHandshakeSession() {
        if (this.conContext.handshakeContext == null) {
            return null;
        }
        return this.conContext.handshakeContext.handshakeSession;
    }

    @Override // javax.net.ssl.SSLEngine
    public synchronized SSLEngineResult.HandshakeStatus getHandshakeStatus() {
        return this.conContext.getHandshakeStatus();
    }

    @Override // javax.net.ssl.SSLEngine
    public synchronized void setUseClientMode(boolean z2) {
        this.conContext.setUseClientMode(z2);
    }

    @Override // javax.net.ssl.SSLEngine
    public synchronized boolean getUseClientMode() {
        return this.conContext.sslConfig.isClientMode;
    }

    @Override // javax.net.ssl.SSLEngine
    public synchronized void setNeedClientAuth(boolean z2) {
        this.conContext.sslConfig.clientAuthType = z2 ? ClientAuthType.CLIENT_AUTH_REQUIRED : ClientAuthType.CLIENT_AUTH_NONE;
    }

    @Override // javax.net.ssl.SSLEngine
    public synchronized boolean getNeedClientAuth() {
        return this.conContext.sslConfig.clientAuthType == ClientAuthType.CLIENT_AUTH_REQUIRED;
    }

    @Override // javax.net.ssl.SSLEngine
    public synchronized void setWantClientAuth(boolean z2) {
        this.conContext.sslConfig.clientAuthType = z2 ? ClientAuthType.CLIENT_AUTH_REQUESTED : ClientAuthType.CLIENT_AUTH_NONE;
    }

    @Override // javax.net.ssl.SSLEngine
    public synchronized boolean getWantClientAuth() {
        return this.conContext.sslConfig.clientAuthType == ClientAuthType.CLIENT_AUTH_REQUESTED;
    }

    @Override // javax.net.ssl.SSLEngine
    public synchronized void setEnableSessionCreation(boolean z2) {
        this.conContext.sslConfig.enableSessionCreation = z2;
    }

    @Override // javax.net.ssl.SSLEngine
    public synchronized boolean getEnableSessionCreation() {
        return this.conContext.sslConfig.enableSessionCreation;
    }

    @Override // javax.net.ssl.SSLEngine
    public synchronized SSLParameters getSSLParameters() {
        return this.conContext.sslConfig.getSSLParameters();
    }

    @Override // javax.net.ssl.SSLEngine
    public synchronized void setSSLParameters(SSLParameters sSLParameters) {
        this.conContext.sslConfig.setSSLParameters(sSLParameters);
        if (this.conContext.sslConfig.maximumPacketSize != 0) {
            this.conContext.outputRecord.changePacketSize(this.conContext.sslConfig.maximumPacketSize);
        }
    }

    @Override // javax.net.ssl.SSLEngine
    public synchronized String getApplicationProtocol() {
        return this.conContext.applicationProtocol;
    }

    @Override // javax.net.ssl.SSLEngine
    public synchronized String getHandshakeApplicationProtocol() {
        if (this.conContext.handshakeContext == null) {
            return null;
        }
        return this.conContext.handshakeContext.applicationProtocol;
    }

    @Override // javax.net.ssl.SSLEngine
    public synchronized void setHandshakeApplicationProtocolSelector(BiFunction<SSLEngine, List<String>, String> biFunction) {
        this.conContext.sslConfig.engineAPSelector = biFunction;
    }

    @Override // javax.net.ssl.SSLEngine
    public synchronized BiFunction<SSLEngine, List<String>, String> getHandshakeApplicationProtocolSelector() {
        return this.conContext.sslConfig.engineAPSelector;
    }

    @Override // sun.security.ssl.SSLTransport
    public boolean useDelegatedTask() {
        return true;
    }

    private synchronized void checkTaskThrown() throws SSLException {
        Exception exc = null;
        HandshakeContext handshakeContext = this.conContext.handshakeContext;
        if (handshakeContext != null && handshakeContext.delegatedThrown != null) {
            exc = handshakeContext.delegatedThrown;
            handshakeContext.delegatedThrown = null;
        }
        if (this.conContext.delegatedThrown != null) {
            if (exc != null) {
                if (this.conContext.delegatedThrown == exc) {
                    this.conContext.delegatedThrown = null;
                }
            } else {
                exc = this.conContext.delegatedThrown;
                this.conContext.delegatedThrown = null;
            }
        }
        if (exc == null) {
            return;
        }
        if (exc instanceof SSLException) {
            throw ((SSLException) exc);
        }
        if (exc instanceof RuntimeException) {
            throw ((RuntimeException) exc);
        }
        throw getTaskThrown(exc);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static SSLException getTaskThrown(Exception exc) {
        String message = exc.getMessage();
        if (message == null) {
            message = "Delegated task threw Exception or Error";
        }
        if (exc instanceof RuntimeException) {
            throw new RuntimeException(message, exc);
        }
        if (exc instanceof SSLHandshakeException) {
            return (SSLHandshakeException) new SSLHandshakeException(message).initCause(exc);
        }
        if (exc instanceof SSLKeyException) {
            return (SSLKeyException) new SSLKeyException(message).initCause(exc);
        }
        if (exc instanceof SSLPeerUnverifiedException) {
            return (SSLPeerUnverifiedException) new SSLPeerUnverifiedException(message).initCause(exc);
        }
        if (exc instanceof SSLProtocolException) {
            return (SSLProtocolException) new SSLProtocolException(message).initCause(exc);
        }
        if (exc instanceof SSLException) {
            return (SSLException) exc;
        }
        return new SSLException(message, exc);
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLEngineImpl$DelegatedTask.class */
    private static class DelegatedTask implements Runnable {
        private final SSLEngineImpl engine;

        DelegatedTask(SSLEngineImpl sSLEngineImpl) {
            this.engine = sSLEngineImpl;
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (this.engine) {
                HandshakeContext handshakeContext = this.engine.conContext.handshakeContext;
                if (handshakeContext == null || handshakeContext.delegatedActions.isEmpty()) {
                    return;
                }
                try {
                    AccessController.doPrivileged(new DelegatedAction(handshakeContext), this.engine.conContext.acc);
                } catch (RuntimeException e2) {
                    if (this.engine.conContext.delegatedThrown == null) {
                        this.engine.conContext.delegatedThrown = e2;
                    }
                    HandshakeContext handshakeContext2 = this.engine.conContext.handshakeContext;
                    if (handshakeContext2 != null) {
                        handshakeContext2.delegatedThrown = e2;
                    } else if (this.engine.conContext.closeReason != null) {
                        this.engine.conContext.closeReason = e2;
                    }
                } catch (PrivilegedActionException e3) {
                    Exception exception = e3.getException();
                    if (this.engine.conContext.delegatedThrown == null) {
                        this.engine.conContext.delegatedThrown = exception;
                    }
                    HandshakeContext handshakeContext3 = this.engine.conContext.handshakeContext;
                    if (handshakeContext3 != null) {
                        handshakeContext3.delegatedThrown = exception;
                    } else if (this.engine.conContext.closeReason != null) {
                        this.engine.conContext.closeReason = SSLEngineImpl.getTaskThrown(exception);
                    }
                }
                HandshakeContext handshakeContext4 = this.engine.conContext.handshakeContext;
                if (handshakeContext4 != null) {
                    handshakeContext4.taskDelegated = false;
                }
            }
        }

        /* loaded from: jsse.jar:sun/security/ssl/SSLEngineImpl$DelegatedTask$DelegatedAction.class */
        private static class DelegatedAction implements PrivilegedExceptionAction<Void> {
            final HandshakeContext context;

            DelegatedAction(HandshakeContext handshakeContext) {
                this.context = handshakeContext;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedExceptionAction
            public Void run() throws Exception {
                while (!this.context.delegatedActions.isEmpty()) {
                    Map.Entry<Byte, ByteBuffer> entryPoll = this.context.delegatedActions.poll();
                    if (entryPoll != null) {
                        this.context.dispatch(entryPoll.getKey().byteValue(), entryPoll.getValue());
                    }
                }
                return null;
            }
        }
    }
}
