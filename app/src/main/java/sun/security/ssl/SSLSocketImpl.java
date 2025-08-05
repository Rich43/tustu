package sun.security.ssl;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLProtocolException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import sun.misc.SharedSecrets;

/* loaded from: jsse.jar:sun/security/ssl/SSLSocketImpl.class */
public final class SSLSocketImpl extends BaseSSLSocketImpl implements SSLTransport {
    final SSLContextImpl sslContext;
    final TransportContext conContext;
    private final AppInputStream appInput;
    private final AppOutputStream appOutput;
    private String peerHost;
    private boolean autoClose;
    private boolean isConnected;
    private volatile boolean tlsIsClosed;
    private final ReentrantLock socketLock;
    private final ReentrantLock handshakeLock;
    private static final boolean trustNameService = Utilities.getBooleanProperty("jdk.tls.trustNameService", false);
    private static final int DEFAULT_SKIP_TIMEOUT = 1;

    @Override // sun.security.ssl.BaseSSLSocketImpl, java.net.Socket
    public /* bridge */ /* synthetic */ void setSoTimeout(int i2) throws SocketException {
        super.setSoTimeout(i2);
    }

    @Override // sun.security.ssl.BaseSSLSocketImpl, java.net.Socket
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    @Override // sun.security.ssl.BaseSSLSocketImpl, java.net.Socket
    public /* bridge */ /* synthetic */ void setPerformancePreferences(int i2, int i3, int i4) {
        super.setPerformancePreferences(i2, i3, i4);
    }

    @Override // sun.security.ssl.BaseSSLSocketImpl, java.net.Socket
    public /* bridge */ /* synthetic */ SocketAddress getRemoteSocketAddress() {
        return super.getRemoteSocketAddress();
    }

    @Override // sun.security.ssl.BaseSSLSocketImpl, java.net.Socket
    public /* bridge */ /* synthetic */ SocketAddress getLocalSocketAddress() {
        return super.getLocalSocketAddress();
    }

    @Override // sun.security.ssl.BaseSSLSocketImpl, java.net.Socket
    public /* bridge */ /* synthetic */ void bind(SocketAddress socketAddress) throws IOException {
        super.bind(socketAddress);
    }

    SSLSocketImpl(SSLContextImpl sSLContextImpl) {
        this.appInput = new AppInputStream();
        this.appOutput = new AppOutputStream();
        this.isConnected = false;
        this.tlsIsClosed = false;
        this.socketLock = new ReentrantLock();
        this.handshakeLock = new ReentrantLock();
        this.sslContext = sSLContextImpl;
        HandshakeHash handshakeHash = new HandshakeHash();
        this.conContext = new TransportContext(sSLContextImpl, (SSLTransport) this, (InputRecord) new SSLSocketInputRecord(handshakeHash), (OutputRecord) new SSLSocketOutputRecord(handshakeHash), true);
    }

    SSLSocketImpl(SSLContextImpl sSLContextImpl, SSLConfiguration sSLConfiguration) {
        this.appInput = new AppInputStream();
        this.appOutput = new AppOutputStream();
        this.isConnected = false;
        this.tlsIsClosed = false;
        this.socketLock = new ReentrantLock();
        this.handshakeLock = new ReentrantLock();
        this.sslContext = sSLContextImpl;
        HandshakeHash handshakeHash = new HandshakeHash();
        this.conContext = new TransportContext(sSLContextImpl, this, sSLConfiguration, new SSLSocketInputRecord(handshakeHash), new SSLSocketOutputRecord(handshakeHash));
    }

    SSLSocketImpl(SSLContextImpl sSLContextImpl, String str, int i2) throws IOException {
        this.appInput = new AppInputStream();
        this.appOutput = new AppOutputStream();
        this.isConnected = false;
        this.tlsIsClosed = false;
        this.socketLock = new ReentrantLock();
        this.handshakeLock = new ReentrantLock();
        this.sslContext = sSLContextImpl;
        HandshakeHash handshakeHash = new HandshakeHash();
        this.conContext = new TransportContext(sSLContextImpl, (SSLTransport) this, (InputRecord) new SSLSocketInputRecord(handshakeHash), (OutputRecord) new SSLSocketOutputRecord(handshakeHash), true);
        this.peerHost = str;
        connect(str != null ? new InetSocketAddress(str, i2) : new InetSocketAddress(InetAddress.getByName(null), i2), 0);
    }

    SSLSocketImpl(SSLContextImpl sSLContextImpl, InetAddress inetAddress, int i2) throws IOException {
        this.appInput = new AppInputStream();
        this.appOutput = new AppOutputStream();
        this.isConnected = false;
        this.tlsIsClosed = false;
        this.socketLock = new ReentrantLock();
        this.handshakeLock = new ReentrantLock();
        this.sslContext = sSLContextImpl;
        HandshakeHash handshakeHash = new HandshakeHash();
        this.conContext = new TransportContext(sSLContextImpl, (SSLTransport) this, (InputRecord) new SSLSocketInputRecord(handshakeHash), (OutputRecord) new SSLSocketOutputRecord(handshakeHash), true);
        connect(new InetSocketAddress(inetAddress, i2), 0);
    }

    SSLSocketImpl(SSLContextImpl sSLContextImpl, String str, int i2, InetAddress inetAddress, int i3) throws IOException {
        this.appInput = new AppInputStream();
        this.appOutput = new AppOutputStream();
        this.isConnected = false;
        this.tlsIsClosed = false;
        this.socketLock = new ReentrantLock();
        this.handshakeLock = new ReentrantLock();
        this.sslContext = sSLContextImpl;
        HandshakeHash handshakeHash = new HandshakeHash();
        this.conContext = new TransportContext(sSLContextImpl, (SSLTransport) this, (InputRecord) new SSLSocketInputRecord(handshakeHash), (OutputRecord) new SSLSocketOutputRecord(handshakeHash), true);
        this.peerHost = str;
        bind(new InetSocketAddress(inetAddress, i3));
        connect(str != null ? new InetSocketAddress(str, i2) : new InetSocketAddress(InetAddress.getByName(null), i2), 0);
    }

    SSLSocketImpl(SSLContextImpl sSLContextImpl, InetAddress inetAddress, int i2, InetAddress inetAddress2, int i3) throws IOException {
        this.appInput = new AppInputStream();
        this.appOutput = new AppOutputStream();
        this.isConnected = false;
        this.tlsIsClosed = false;
        this.socketLock = new ReentrantLock();
        this.handshakeLock = new ReentrantLock();
        this.sslContext = sSLContextImpl;
        HandshakeHash handshakeHash = new HandshakeHash();
        this.conContext = new TransportContext(sSLContextImpl, (SSLTransport) this, (InputRecord) new SSLSocketInputRecord(handshakeHash), (OutputRecord) new SSLSocketOutputRecord(handshakeHash), true);
        bind(new InetSocketAddress(inetAddress2, i3));
        connect(new InetSocketAddress(inetAddress, i2), 0);
    }

    SSLSocketImpl(SSLContextImpl sSLContextImpl, Socket socket, InputStream inputStream, boolean z2) throws IOException {
        super(socket, inputStream);
        this.appInput = new AppInputStream();
        this.appOutput = new AppOutputStream();
        this.isConnected = false;
        this.tlsIsClosed = false;
        this.socketLock = new ReentrantLock();
        this.handshakeLock = new ReentrantLock();
        if (!socket.isConnected()) {
            throw new SocketException("Underlying socket is not connected");
        }
        this.sslContext = sSLContextImpl;
        HandshakeHash handshakeHash = new HandshakeHash();
        this.conContext = new TransportContext(sSLContextImpl, (SSLTransport) this, (InputRecord) new SSLSocketInputRecord(handshakeHash), (OutputRecord) new SSLSocketOutputRecord(handshakeHash), false);
        this.autoClose = z2;
        doneConnect();
    }

    SSLSocketImpl(SSLContextImpl sSLContextImpl, Socket socket, String str, int i2, boolean z2) throws IOException {
        super(socket);
        this.appInput = new AppInputStream();
        this.appOutput = new AppOutputStream();
        this.isConnected = false;
        this.tlsIsClosed = false;
        this.socketLock = new ReentrantLock();
        this.handshakeLock = new ReentrantLock();
        if (!socket.isConnected()) {
            throw new SocketException("Underlying socket is not connected");
        }
        this.sslContext = sSLContextImpl;
        HandshakeHash handshakeHash = new HandshakeHash();
        this.conContext = new TransportContext(sSLContextImpl, (SSLTransport) this, (InputRecord) new SSLSocketInputRecord(handshakeHash), (OutputRecord) new SSLSocketOutputRecord(handshakeHash), true);
        this.peerHost = str;
        this.autoClose = z2;
        doneConnect();
    }

    @Override // java.net.Socket
    public void connect(SocketAddress socketAddress, int i2) throws IOException {
        if (isLayered()) {
            throw new SocketException("Already connected");
        }
        if (!(socketAddress instanceof InetSocketAddress)) {
            throw new SocketException("Cannot handle non-Inet socket addresses.");
        }
        super.connect(socketAddress, i2);
        doneConnect();
    }

    @Override // javax.net.ssl.SSLSocket
    public String[] getSupportedCipherSuites() {
        return CipherSuite.namesOf(this.sslContext.getSupportedCipherSuites());
    }

    @Override // javax.net.ssl.SSLSocket
    public String[] getEnabledCipherSuites() {
        this.socketLock.lock();
        try {
            return CipherSuite.namesOf(this.conContext.sslConfig.enabledCipherSuites);
        } finally {
            this.socketLock.unlock();
        }
    }

    @Override // javax.net.ssl.SSLSocket
    public void setEnabledCipherSuites(String[] strArr) {
        this.socketLock.lock();
        try {
            this.conContext.sslConfig.enabledCipherSuites = CipherSuite.validValuesOf(strArr);
        } finally {
            this.socketLock.unlock();
        }
    }

    @Override // javax.net.ssl.SSLSocket
    public String[] getSupportedProtocols() {
        return ProtocolVersion.toStringArray(this.sslContext.getSupportedProtocolVersions());
    }

    @Override // javax.net.ssl.SSLSocket
    public String[] getEnabledProtocols() {
        this.socketLock.lock();
        try {
            return ProtocolVersion.toStringArray(this.conContext.sslConfig.enabledProtocols);
        } finally {
            this.socketLock.unlock();
        }
    }

    @Override // javax.net.ssl.SSLSocket
    public void setEnabledProtocols(String[] strArr) {
        if (strArr == null) {
            throw new IllegalArgumentException("Protocols cannot be null");
        }
        this.socketLock.lock();
        try {
            this.conContext.sslConfig.enabledProtocols = ProtocolVersion.namesOf(strArr);
        } finally {
            this.socketLock.unlock();
        }
    }

    @Override // javax.net.ssl.SSLSocket
    public SSLSession getSession() {
        try {
            ensureNegotiated();
            return this.conContext.conSession;
        } catch (IOException e2) {
            if (SSLLogger.isOn && SSLLogger.isOn("handshake")) {
                SSLLogger.severe("handshake failed", e2);
            }
            return new SSLSessionImpl();
        }
    }

    @Override // javax.net.ssl.SSLSocket
    public SSLSession getHandshakeSession() {
        this.socketLock.lock();
        try {
            return this.conContext.handshakeContext == null ? null : this.conContext.handshakeContext.handshakeSession;
        } finally {
            this.socketLock.unlock();
        }
    }

    @Override // javax.net.ssl.SSLSocket
    public void addHandshakeCompletedListener(HandshakeCompletedListener handshakeCompletedListener) {
        if (handshakeCompletedListener == null) {
            throw new IllegalArgumentException("listener is null");
        }
        this.socketLock.lock();
        try {
            this.conContext.sslConfig.addHandshakeCompletedListener(handshakeCompletedListener);
        } finally {
            this.socketLock.unlock();
        }
    }

    @Override // javax.net.ssl.SSLSocket
    public void removeHandshakeCompletedListener(HandshakeCompletedListener handshakeCompletedListener) {
        if (handshakeCompletedListener == null) {
            throw new IllegalArgumentException("listener is null");
        }
        this.socketLock.lock();
        try {
            this.conContext.sslConfig.removeHandshakeCompletedListener(handshakeCompletedListener);
        } finally {
            this.socketLock.unlock();
        }
    }

    @Override // javax.net.ssl.SSLSocket
    public void startHandshake() throws IOException {
        if (!this.isConnected) {
            throw new SocketException("Socket is not connected");
        }
        if (this.conContext.isBroken || this.conContext.isInboundClosed() || this.conContext.isOutboundClosed()) {
            throw new SocketException("Socket has been closed or broken");
        }
        this.handshakeLock.lock();
        try {
            if (this.conContext.isBroken || this.conContext.isInboundClosed() || this.conContext.isOutboundClosed()) {
                throw new SocketException("Socket has been closed or broken");
            }
            try {
                this.conContext.kickstart();
                if (!this.conContext.isNegotiated) {
                    readHandshakeRecord();
                }
            } catch (IOException e2) {
                throw this.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Couldn't kickstart handshaking", e2);
            } catch (Exception e3) {
                handleException(e3);
            }
        } finally {
            this.handshakeLock.unlock();
        }
    }

    @Override // javax.net.ssl.SSLSocket
    public void setUseClientMode(boolean z2) {
        this.socketLock.lock();
        try {
            this.conContext.setUseClientMode(z2);
        } finally {
            this.socketLock.unlock();
        }
    }

    @Override // javax.net.ssl.SSLSocket
    public boolean getUseClientMode() {
        this.socketLock.lock();
        try {
            return this.conContext.sslConfig.isClientMode;
        } finally {
            this.socketLock.unlock();
        }
    }

    @Override // javax.net.ssl.SSLSocket
    public void setNeedClientAuth(boolean z2) {
        this.socketLock.lock();
        try {
            this.conContext.sslConfig.clientAuthType = z2 ? ClientAuthType.CLIENT_AUTH_REQUIRED : ClientAuthType.CLIENT_AUTH_NONE;
        } finally {
            this.socketLock.unlock();
        }
    }

    @Override // javax.net.ssl.SSLSocket
    public boolean getNeedClientAuth() {
        this.socketLock.lock();
        try {
            return this.conContext.sslConfig.clientAuthType == ClientAuthType.CLIENT_AUTH_REQUIRED;
        } finally {
            this.socketLock.unlock();
        }
    }

    @Override // javax.net.ssl.SSLSocket
    public void setWantClientAuth(boolean z2) {
        this.socketLock.lock();
        try {
            this.conContext.sslConfig.clientAuthType = z2 ? ClientAuthType.CLIENT_AUTH_REQUESTED : ClientAuthType.CLIENT_AUTH_NONE;
        } finally {
            this.socketLock.unlock();
        }
    }

    @Override // javax.net.ssl.SSLSocket
    public boolean getWantClientAuth() {
        this.socketLock.lock();
        try {
            return this.conContext.sslConfig.clientAuthType == ClientAuthType.CLIENT_AUTH_REQUESTED;
        } finally {
            this.socketLock.unlock();
        }
    }

    @Override // javax.net.ssl.SSLSocket
    public void setEnableSessionCreation(boolean z2) {
        this.socketLock.lock();
        try {
            this.conContext.sslConfig.enableSessionCreation = z2;
        } finally {
            this.socketLock.unlock();
        }
    }

    @Override // javax.net.ssl.SSLSocket
    public boolean getEnableSessionCreation() {
        this.socketLock.lock();
        try {
            return this.conContext.sslConfig.enableSessionCreation;
        } finally {
            this.socketLock.unlock();
        }
    }

    @Override // java.net.Socket
    public boolean isClosed() {
        return this.tlsIsClosed;
    }

    @Override // sun.security.ssl.BaseSSLSocketImpl, java.net.Socket, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (isClosed()) {
            return;
        }
        if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
            SSLLogger.fine("duplex close of SSLSocket", new Object[0]);
        }
        try {
            try {
                if (isConnected()) {
                    if (!isOutputShutdown()) {
                        duplexCloseOutput();
                    }
                    if (!isInputShutdown()) {
                        duplexCloseInput();
                    }
                }
                try {
                    if (isClosed()) {
                        return;
                    }
                    closeSocket(false);
                } catch (IOException e2) {
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                        SSLLogger.warning("SSLSocket close failed", e2);
                    }
                } finally {
                }
            } catch (IOException e3) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                    SSLLogger.warning("SSLSocket duplex close failed", e3);
                }
                try {
                    if (isClosed()) {
                        return;
                    }
                    closeSocket(false);
                } catch (IOException e4) {
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                        SSLLogger.warning("SSLSocket close failed", e4);
                    }
                } finally {
                }
            }
        } catch (Throwable th) {
            if (!isClosed()) {
                try {
                    try {
                        closeSocket(false);
                    } catch (IOException e5) {
                        if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                            SSLLogger.warning("SSLSocket close failed", e5);
                        }
                        this.tlsIsClosed = true;
                    }
                } catch (Throwable th2) {
                    throw th2;
                }
            }
            throw th;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x00a3 A[DONT_GENERATE] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void duplexCloseOutput() throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 240
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.ssl.SSLSocketImpl.duplexCloseOutput():void");
    }

    private void duplexCloseInput() throws IOException {
        boolean z2 = false;
        if (this.conContext.isNegotiated && !this.conContext.protocolVersion.useTLS13PlusSpec()) {
            z2 = true;
        }
        bruteForceCloseInput(z2);
    }

    private void bruteForceCloseInput(boolean z2) throws IOException {
        boolean zIsInputShutdown;
        if (z2) {
            try {
                shutdown();
                if (!zIsInputShutdown) {
                    return;
                } else {
                    return;
                }
            } finally {
                if (!isInputShutdown()) {
                    shutdownInput(false);
                }
            }
        }
        if (!this.conContext.isInboundClosed()) {
            InputRecord inputRecord = this.conContext.inputRecord;
            Throwable th = null;
            try {
                try {
                    this.appInput.deplete();
                    if (inputRecord != null) {
                        if (0 != 0) {
                            try {
                                inputRecord.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        } else {
                            inputRecord.close();
                        }
                    }
                } catch (Throwable th3) {
                    th = th3;
                    throw th3;
                }
            } catch (Throwable th4) {
                if (inputRecord != null) {
                    if (th != null) {
                        try {
                            inputRecord.close();
                        } catch (Throwable th5) {
                            th.addSuppressed(th5);
                        }
                    } else {
                        inputRecord.close();
                    }
                }
                throw th4;
            }
        }
        if ((this.autoClose || !isLayered()) && !super.isInputShutdown()) {
            super.shutdownInput();
        }
    }

    @Override // sun.security.ssl.BaseSSLSocketImpl, java.net.Socket
    public void shutdownInput() throws IOException {
        shutdownInput(true);
    }

    private void shutdownInput(boolean z2) throws IOException {
        boolean z3;
        boolean zIsLayered;
        if (isInputShutdown()) {
            return;
        }
        if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
            SSLLogger.fine("close inbound of SSLSocket", new Object[0]);
        }
        if (z2) {
            try {
                if (!this.conContext.isInputCloseNotified && (this.conContext.isNegotiated || this.conContext.handshakeContext != null)) {
                    throw new SSLException("closing inbound before receiving peer's close_notify");
                }
            } finally {
                this.conContext.closeInbound();
                if ((this.autoClose || !isLayered()) && !super.isInputShutdown()) {
                    super.shutdownInput();
                }
            }
        }
        if (!z3) {
            if (zIsLayered) {
                return;
            }
        }
    }

    @Override // sun.security.ssl.BaseSSLSocketImpl, java.net.Socket
    public boolean isInputShutdown() {
        return this.conContext.isInboundClosed() && ((!this.autoClose && isLayered()) || super.isInputShutdown());
    }

    @Override // sun.security.ssl.BaseSSLSocketImpl, java.net.Socket
    public void shutdownOutput() throws IOException {
        if (isOutputShutdown()) {
            return;
        }
        if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
            SSLLogger.fine("close outbound of SSLSocket", new Object[0]);
        }
        this.conContext.closeOutbound();
        if ((this.autoClose || !isLayered()) && !super.isOutputShutdown()) {
            super.shutdownOutput();
        }
    }

    @Override // sun.security.ssl.BaseSSLSocketImpl, java.net.Socket
    public boolean isOutputShutdown() {
        return this.conContext.isOutboundClosed() && ((!this.autoClose && isLayered()) || super.isOutputShutdown());
    }

    @Override // sun.security.ssl.BaseSSLSocketImpl, java.net.Socket
    public InputStream getInputStream() throws IOException {
        this.socketLock.lock();
        try {
            if (isClosed()) {
                throw new SocketException("Socket is closed");
            }
            if (!this.isConnected) {
                throw new SocketException("Socket is not connected");
            }
            if (this.conContext.isInboundClosed() || isInputShutdown()) {
                throw new SocketException("Socket input is already shutdown");
            }
            return this.appInput;
        } finally {
            this.socketLock.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ensureNegotiated() throws IOException {
        if (this.conContext.isNegotiated || this.conContext.isBroken || this.conContext.isInboundClosed() || this.conContext.isOutboundClosed()) {
            return;
        }
        this.handshakeLock.lock();
        try {
            if (this.conContext.isNegotiated || this.conContext.isBroken || this.conContext.isInboundClosed() || this.conContext.isOutboundClosed()) {
                return;
            }
            startHandshake();
        } finally {
            this.handshakeLock.unlock();
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLSocketImpl$AppInputStream.class */
    private class AppInputStream extends InputStream {
        private volatile boolean isClosing;
        private volatile boolean hasDepleted;
        private final byte[] oneByte = new byte[1];
        private final ReentrantLock readLock = new ReentrantLock();
        private volatile boolean appDataIsAvailable = false;
        private ByteBuffer buffer = ByteBuffer.allocate(4096);

        AppInputStream() {
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            if (!this.appDataIsAvailable || checkEOF()) {
                return 0;
            }
            return this.buffer.remaining();
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            if (read(this.oneByte, 0, 1) <= 0) {
                return -1;
            }
            return this.oneByte[0] & 255;
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr, int i2, int i3) throws IOException {
            if (bArr == null) {
                throw new NullPointerException("the target buffer is null");
            }
            if (i2 < 0 || i3 < 0 || i3 > bArr.length - i2) {
                throw new IndexOutOfBoundsException("buffer length: " + bArr.length + ", offset; " + i2 + ", bytes to read:" + i3);
            }
            if (i3 == 0) {
                return 0;
            }
            if (checkEOF()) {
                return -1;
            }
            if (!SSLSocketImpl.this.conContext.isNegotiated && !SSLSocketImpl.this.conContext.isBroken && !SSLSocketImpl.this.conContext.isInboundClosed() && !SSLSocketImpl.this.conContext.isOutboundClosed()) {
                SSLSocketImpl.this.ensureNegotiated();
            }
            if (!SSLSocketImpl.this.conContext.isNegotiated || SSLSocketImpl.this.conContext.isBroken || SSLSocketImpl.this.conContext.isInboundClosed()) {
                throw new SocketException("Connection or inbound has closed");
            }
            if (this.hasDepleted) {
                if (!SSLLogger.isOn || !SSLLogger.isOn("ssl")) {
                    return -1;
                }
                SSLLogger.fine("The input stream has been depleted", new Object[0]);
                return -1;
            }
            this.readLock.lock();
            try {
                if (SSLSocketImpl.this.conContext.isBroken || SSLSocketImpl.this.conContext.isInboundClosed()) {
                    throw new SocketException("Connection or inbound has closed");
                }
                if (this.hasDepleted) {
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                        SSLLogger.fine("The input stream is closing", new Object[0]);
                    }
                    try {
                        if (this.isClosing) {
                            readLockedDeplete();
                        }
                        return -1;
                    } finally {
                        this.readLock.unlock();
                    }
                }
                int iAvailable = available();
                if (iAvailable > 0) {
                    int iMin = Math.min(iAvailable, i3);
                    this.buffer.get(bArr, i2, iMin);
                    try {
                        if (this.isClosing) {
                            readLockedDeplete();
                        }
                        this.readLock.unlock();
                        return iMin;
                    } finally {
                        this.readLock.unlock();
                    }
                }
                this.appDataIsAvailable = false;
                try {
                    ByteBuffer applicationRecord = SSLSocketImpl.this.readApplicationRecord(this.buffer);
                    if (applicationRecord == null) {
                        try {
                            if (this.isClosing) {
                                readLockedDeplete();
                            }
                            this.readLock.unlock();
                            return -1;
                        } finally {
                            this.readLock.unlock();
                        }
                    }
                    this.buffer = applicationRecord;
                    applicationRecord.flip();
                    int iMin2 = Math.min(i3, applicationRecord.remaining());
                    this.buffer.get(bArr, i2, iMin2);
                    this.appDataIsAvailable = true;
                    try {
                        if (this.isClosing) {
                            readLockedDeplete();
                        }
                        this.readLock.unlock();
                        return iMin2;
                    } finally {
                        this.readLock.unlock();
                    }
                } catch (Exception e2) {
                    SSLSocketImpl.this.handleException(e2);
                    try {
                        if (this.isClosing) {
                            readLockedDeplete();
                        }
                        this.readLock.unlock();
                        return -1;
                    } finally {
                        this.readLock.unlock();
                    }
                }
            } catch (Throwable th) {
                try {
                    if (this.isClosing) {
                        readLockedDeplete();
                    }
                    this.readLock.unlock();
                    throw th;
                } finally {
                    this.readLock.unlock();
                }
            }
        }

        @Override // java.io.InputStream
        public long skip(long j2) throws IOException {
            byte[] bArr = new byte[256];
            long j3 = 0;
            this.readLock.lock();
            while (j2 > 0) {
                try {
                    int i2 = read(bArr, 0, (int) Math.min(j2, bArr.length));
                    if (i2 <= 0) {
                        break;
                    }
                    j2 -= i2;
                    j3 += i2;
                } finally {
                    this.readLock.unlock();
                }
            }
            return j3;
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                SSLLogger.finest("Closing input stream", new Object[0]);
            }
            try {
                SSLSocketImpl.this.close();
            } catch (IOException e2) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                    SSLLogger.warning("input stream close failed", e2);
                }
            }
        }

        private boolean checkEOF() throws IOException {
            if (SSLSocketImpl.this.conContext.isInboundClosed()) {
                return true;
            }
            if (SSLSocketImpl.this.conContext.isInputCloseNotified || SSLSocketImpl.this.conContext.isBroken) {
                if (SSLSocketImpl.this.conContext.closeReason == null) {
                    return true;
                }
                throw new SSLException("Connection has closed: " + ((Object) SSLSocketImpl.this.conContext.closeReason), SSLSocketImpl.this.conContext.closeReason);
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void deplete() {
            if (SSLSocketImpl.this.conContext.isInboundClosed() || this.isClosing) {
                return;
            }
            this.isClosing = true;
            if (this.readLock.tryLock()) {
                try {
                    readLockedDeplete();
                } finally {
                    this.readLock.unlock();
                }
            }
        }

        private void readLockedDeplete() {
            if (this.hasDepleted || SSLSocketImpl.this.conContext.isInboundClosed() || !(SSLSocketImpl.this.conContext.inputRecord instanceof SSLSocketInputRecord)) {
                return;
            }
            try {
                ((SSLSocketInputRecord) SSLSocketImpl.this.conContext.inputRecord).deplete(SSLSocketImpl.this.conContext.isNegotiated && SSLSocketImpl.this.getSoTimeout() > 0);
            } catch (Exception e2) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                    SSLLogger.warning("input stream close depletion failed", e2);
                }
            } finally {
                this.hasDepleted = true;
            }
        }
    }

    @Override // sun.security.ssl.BaseSSLSocketImpl, java.net.Socket
    public OutputStream getOutputStream() throws IOException {
        this.socketLock.lock();
        try {
            if (isClosed()) {
                throw new SocketException("Socket is closed");
            }
            if (!this.isConnected) {
                throw new SocketException("Socket is not connected");
            }
            if (this.conContext.isOutboundDone() || isOutputShutdown()) {
                throw new SocketException("Socket output is already shutdown");
            }
            return this.appOutput;
        } finally {
            this.socketLock.unlock();
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLSocketImpl$AppOutputStream.class */
    private class AppOutputStream extends OutputStream {
        private final byte[] oneByte;

        private AppOutputStream() {
            this.oneByte = new byte[1];
        }

        @Override // java.io.OutputStream
        public void write(int i2) throws IOException {
            this.oneByte[0] = (byte) i2;
            write(this.oneByte, 0, 1);
        }

        @Override // java.io.OutputStream
        public void write(byte[] bArr, int i2, int i3) throws IOException {
            if (bArr == null) {
                throw new NullPointerException("the source buffer is null");
            }
            if (i2 < 0 || i3 < 0 || i3 > bArr.length - i2) {
                throw new IndexOutOfBoundsException("buffer length: " + bArr.length + ", offset; " + i2 + ", bytes to read:" + i3);
            }
            if (i3 == 0) {
                return;
            }
            if (!SSLSocketImpl.this.conContext.isNegotiated && !SSLSocketImpl.this.conContext.isBroken && !SSLSocketImpl.this.conContext.isInboundClosed() && !SSLSocketImpl.this.conContext.isOutboundClosed()) {
                SSLSocketImpl.this.ensureNegotiated();
            }
            if (!SSLSocketImpl.this.conContext.isNegotiated || SSLSocketImpl.this.conContext.isBroken || SSLSocketImpl.this.conContext.isOutboundClosed()) {
                throw new SocketException("Connection or outbound has closed");
            }
            try {
                SSLSocketImpl.this.conContext.outputRecord.deliver(bArr, i2, i3);
                if (SSLSocketImpl.this.conContext.outputRecord.seqNumIsHuge() || SSLSocketImpl.this.conContext.outputRecord.writeCipher.atKeyLimit()) {
                    SSLSocketImpl.this.tryKeyUpdate();
                }
            } catch (SSLHandshakeException e2) {
                throw SSLSocketImpl.this.conContext.fatal(Alert.HANDSHAKE_FAILURE, e2);
            } catch (SSLException e3) {
                throw SSLSocketImpl.this.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e3);
            }
        }

        @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                SSLLogger.finest("Closing output stream", new Object[0]);
            }
            try {
                SSLSocketImpl.this.close();
            } catch (IOException e2) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                    SSLLogger.warning("output stream close failed", e2);
                }
            }
        }
    }

    @Override // javax.net.ssl.SSLSocket
    public SSLParameters getSSLParameters() {
        this.socketLock.lock();
        try {
            return this.conContext.sslConfig.getSSLParameters();
        } finally {
            this.socketLock.unlock();
        }
    }

    @Override // javax.net.ssl.SSLSocket
    public void setSSLParameters(SSLParameters sSLParameters) {
        this.socketLock.lock();
        try {
            this.conContext.sslConfig.setSSLParameters(sSLParameters);
            if (this.conContext.sslConfig.maximumPacketSize != 0) {
                this.conContext.outputRecord.changePacketSize(this.conContext.sslConfig.maximumPacketSize);
            }
        } finally {
            this.socketLock.unlock();
        }
    }

    @Override // javax.net.ssl.SSLSocket
    public String getApplicationProtocol() {
        this.socketLock.lock();
        try {
            return this.conContext.applicationProtocol;
        } finally {
            this.socketLock.unlock();
        }
    }

    @Override // javax.net.ssl.SSLSocket
    public String getHandshakeApplicationProtocol() {
        this.socketLock.lock();
        try {
            if (this.conContext.handshakeContext != null) {
                return this.conContext.handshakeContext.applicationProtocol;
            }
            return null;
        } finally {
            this.socketLock.unlock();
        }
    }

    @Override // javax.net.ssl.SSLSocket
    public void setHandshakeApplicationProtocolSelector(BiFunction<SSLSocket, List<String>, String> biFunction) {
        this.socketLock.lock();
        try {
            this.conContext.sslConfig.socketAPSelector = biFunction;
        } finally {
            this.socketLock.unlock();
        }
    }

    @Override // javax.net.ssl.SSLSocket
    public BiFunction<SSLSocket, List<String>, String> getHandshakeApplicationProtocolSelector() {
        this.socketLock.lock();
        try {
            return this.conContext.sslConfig.socketAPSelector;
        } finally {
            this.socketLock.unlock();
        }
    }

    private int readHandshakeRecord() throws IOException {
        while (!this.conContext.isInboundClosed()) {
            try {
                if (decode(null).contentType == ContentType.HANDSHAKE.id && this.conContext.isNegotiated) {
                    return 0;
                }
            } catch (SSLException e2) {
                throw e2;
            } catch (IOException e3) {
                if (!(e3 instanceof SSLException)) {
                    throw new SSLException("readHandshakeRecord", e3);
                }
                throw e3;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ByteBuffer readApplicationRecord(ByteBuffer byteBuffer) throws Throwable {
        while (!this.conContext.isInboundClosed()) {
            byteBuffer.clear();
            int iBytesInCompletePacket = this.conContext.inputRecord.bytesInCompletePacket();
            if (iBytesInCompletePacket < 0) {
                handleEOF(null);
                return null;
            }
            if (iBytesInCompletePacket > 33093) {
                throw new SSLProtocolException("Illegal packet size: " + iBytesInCompletePacket);
            }
            if (iBytesInCompletePacket > byteBuffer.remaining()) {
                byteBuffer = ByteBuffer.allocate(iBytesInCompletePacket);
            }
            try {
                this.socketLock.lock();
                try {
                    Plaintext plaintextDecode = decode(byteBuffer);
                    this.socketLock.unlock();
                    if (plaintextDecode.contentType == ContentType.APPLICATION_DATA.id && byteBuffer.position() > 0) {
                        return byteBuffer;
                    }
                } finally {
                }
            } catch (SSLException e2) {
                throw e2;
            } catch (IOException e3) {
                if (!(e3 instanceof SSLException)) {
                    throw new SSLException("readApplicationRecord", e3);
                }
                throw e3;
            }
        }
        return null;
    }

    private Plaintext decode(ByteBuffer byteBuffer) throws Throwable {
        Plaintext plaintextHandleEOF;
        try {
            if (byteBuffer == null) {
                plaintextHandleEOF = SSLTransport.decode(this.conContext, null, 0, 0, null, 0, 0);
            } else {
                plaintextHandleEOF = SSLTransport.decode(this.conContext, null, 0, 0, new ByteBuffer[]{byteBuffer}, 0, 1);
            }
        } catch (EOFException e2) {
            plaintextHandleEOF = handleEOF(e2);
        }
        if (plaintextHandleEOF != Plaintext.PLAINTEXT_NULL && (this.conContext.inputRecord.seqNumIsHuge() || this.conContext.inputRecord.readCipher.atKeyLimit())) {
            tryKeyUpdate();
        }
        return plaintextHandleEOF;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryKeyUpdate() throws IOException {
        if (this.conContext.handshakeContext == null && !this.conContext.isOutboundClosed() && !this.conContext.isBroken) {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                SSLLogger.finest("trigger key update", new Object[0]);
            }
            startHandshake();
        }
    }

    void doneConnect() throws IOException {
        this.socketLock.lock();
        try {
            if (this.peerHost == null || this.peerHost.isEmpty()) {
                useImplicitHost(trustNameService && this.conContext.sslConfig.isClientMode);
            } else {
                this.conContext.sslConfig.serverNames = Utilities.addToSNIServerNameList(this.conContext.sslConfig.serverNames, this.peerHost);
            }
            this.conContext.inputRecord.setReceiverStream(super.getInputStream());
            OutputStream outputStream = super.getOutputStream();
            this.conContext.inputRecord.setDeliverStream(outputStream);
            this.conContext.outputRecord.setDeliverStream(outputStream);
            this.isConnected = true;
        } finally {
            this.socketLock.unlock();
        }
    }

    private void useImplicitHost(boolean z2) {
        InetAddress inetAddress = getInetAddress();
        if (inetAddress == null) {
            return;
        }
        String originalHostName = SharedSecrets.getJavaNetAccess().getOriginalHostName(inetAddress);
        if (originalHostName != null && !originalHostName.isEmpty()) {
            this.peerHost = originalHostName;
            if (this.conContext.sslConfig.serverNames.isEmpty() && !this.conContext.sslConfig.noSniExtension) {
                this.conContext.sslConfig.serverNames = Utilities.addToSNIServerNameList(this.conContext.sslConfig.serverNames, this.peerHost);
                return;
            }
            return;
        }
        if (!z2) {
            this.peerHost = inetAddress.getHostAddress();
        } else {
            this.peerHost = getInetAddress().getHostName();
        }
    }

    public void setHost(String str) {
        this.socketLock.lock();
        try {
            this.peerHost = str;
            this.conContext.sslConfig.serverNames = Utilities.addToSNIServerNameList(this.conContext.sslConfig.serverNames, str);
        } finally {
            this.socketLock.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleException(Exception exc) throws IOException {
        Alert alert;
        if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
            SSLLogger.warning("handling exception", exc);
        }
        if (exc instanceof InterruptedIOException) {
            throw ((IOException) exc);
        }
        if (exc instanceof SSLException) {
            if (exc instanceof SSLHandshakeException) {
                alert = Alert.HANDSHAKE_FAILURE;
            } else {
                alert = Alert.UNEXPECTED_MESSAGE;
            }
        } else if (exc instanceof IOException) {
            alert = Alert.UNEXPECTED_MESSAGE;
        } else {
            alert = Alert.INTERNAL_ERROR;
        }
        throw this.conContext.fatal(alert, exc);
    }

    private Plaintext handleEOF(EOFException eOFException) throws Throwable {
        Throwable sSLProtocolException;
        if (requireCloseNotify || this.conContext.handshakeContext != null) {
            if (this.conContext.handshakeContext != null) {
                sSLProtocolException = new SSLHandshakeException("Remote host terminated the handshake");
            } else {
                sSLProtocolException = new SSLProtocolException("Remote host terminated the connection");
            }
            if (eOFException != null) {
                sSLProtocolException.initCause(eOFException);
            }
            throw sSLProtocolException;
        }
        this.conContext.isInputCloseNotified = true;
        shutdownInput();
        return Plaintext.PLAINTEXT_NULL;
    }

    @Override // sun.security.ssl.SSLTransport
    public String getPeerHost() {
        return this.peerHost;
    }

    @Override // sun.security.ssl.SSLTransport
    public int getPeerPort() {
        return getPort();
    }

    @Override // sun.security.ssl.SSLTransport
    public boolean useDelegatedTask() {
        return false;
    }

    @Override // sun.security.ssl.SSLTransport
    public void shutdown() throws IOException {
        if (!isClosed()) {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                SSLLogger.fine("close the underlying socket", new Object[0]);
            }
            try {
                if (this.conContext.isInputCloseNotified) {
                    closeSocket(false);
                } else {
                    closeSocket(true);
                }
            } finally {
                this.tlsIsClosed = true;
            }
        }
    }

    private void closeSocket(boolean z2) throws IOException {
        if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
            SSLLogger.fine("close the SSL connection " + (z2 ? "(initiative)" : "(passive)"), new Object[0]);
        }
        if (this.autoClose || !isLayered()) {
            if ((this.conContext.inputRecord instanceof SSLSocketInputRecord) && this.isConnected && this.appInput.readLock.tryLock()) {
                int soTimeout = getSoTimeout();
                if (soTimeout == 0) {
                    try {
                        setSoTimeout(1);
                    } catch (SocketTimeoutException e2) {
                        if (soTimeout == 0) {
                            setSoTimeout(soTimeout);
                        }
                        this.appInput.readLock.unlock();
                    } catch (Throwable th) {
                        if (soTimeout == 0) {
                            setSoTimeout(soTimeout);
                        }
                        this.appInput.readLock.unlock();
                        throw th;
                    }
                }
                ((SSLSocketInputRecord) this.conContext.inputRecord).deplete(false);
                if (soTimeout == 0) {
                    setSoTimeout(soTimeout);
                }
                this.appInput.readLock.unlock();
            }
            super.close();
            return;
        }
        if (z2 && !this.conContext.isInboundClosed() && !isInputShutdown()) {
            waitForClose();
        }
    }

    private void waitForClose() throws IOException {
        if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
            SSLLogger.fine("wait for close_notify or alert", new Object[0]);
        }
        this.appInput.readLock.lock();
        while (!this.conContext.isInboundClosed()) {
            try {
                try {
                    Plaintext plaintextDecode = decode(null);
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                        SSLLogger.finest("discard plaintext while waiting for close", plaintextDecode);
                    }
                } catch (Exception e2) {
                    handleException(e2);
                }
            } finally {
                this.appInput.readLock.unlock();
            }
        }
    }
}
