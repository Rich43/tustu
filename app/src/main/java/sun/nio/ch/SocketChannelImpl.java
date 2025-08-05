package sun.nio.ch;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AlreadyBoundException;
import java.nio.channels.AlreadyConnectedException;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ConnectionPendingException;
import java.nio.channels.NetworkChannel;
import java.nio.channels.NoConnectionPendingException;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JInternalFrame;
import jdk.net.ExtendedSocketOptions;
import org.icepdf.core.util.PdfOps;
import sun.net.ExtendedOptionsHelper;
import sun.net.ExtendedOptionsImpl;
import sun.net.NetHooks;

/* loaded from: rt.jar:sun/nio/ch/SocketChannelImpl.class */
class SocketChannelImpl extends SocketChannel implements SelChImpl {
    private static NativeDispatcher nd;
    private final FileDescriptor fd;
    private final int fdVal;
    private volatile long readerThread;
    private volatile long writerThread;
    private final Object readLock;
    private final Object writeLock;
    private final Object stateLock;
    private boolean isReuseAddress;
    private static final int ST_UNINITIALIZED = -1;
    private static final int ST_UNCONNECTED = 0;
    private static final int ST_PENDING = 1;
    private static final int ST_CONNECTED = 2;
    private static final int ST_KILLPENDING = 3;
    private static final int ST_KILLED = 4;
    private int state;
    private InetSocketAddress localAddress;
    private InetSocketAddress remoteAddress;
    private boolean isInputOpen;
    private boolean isOutputOpen;
    private boolean readyToConnect;
    private Socket socket;
    static final /* synthetic */ boolean $assertionsDisabled;

    private static native int checkConnect(FileDescriptor fileDescriptor, boolean z2, boolean z3) throws IOException;

    private static native int sendOutOfBandData(FileDescriptor fileDescriptor, byte b2) throws IOException;

    @Override // java.nio.channels.SocketChannel, java.nio.channels.NetworkChannel
    public /* bridge */ /* synthetic */ NetworkChannel setOption(SocketOption socketOption, Object obj) throws IOException {
        return setOption((SocketOption<SocketOption>) socketOption, (SocketOption) obj);
    }

    static {
        $assertionsDisabled = !SocketChannelImpl.class.desiredAssertionStatus();
        IOUtil.load();
        nd = new SocketDispatcher();
    }

    SocketChannelImpl(SelectorProvider selectorProvider) throws IOException {
        super(selectorProvider);
        this.readerThread = 0L;
        this.writerThread = 0L;
        this.readLock = new Object();
        this.writeLock = new Object();
        this.stateLock = new Object();
        this.state = -1;
        this.isInputOpen = true;
        this.isOutputOpen = true;
        this.readyToConnect = false;
        this.fd = Net.socket(true);
        this.fdVal = IOUtil.fdVal(this.fd);
        this.state = 0;
    }

    SocketChannelImpl(SelectorProvider selectorProvider, FileDescriptor fileDescriptor, boolean z2) throws IOException {
        super(selectorProvider);
        this.readerThread = 0L;
        this.writerThread = 0L;
        this.readLock = new Object();
        this.writeLock = new Object();
        this.stateLock = new Object();
        this.state = -1;
        this.isInputOpen = true;
        this.isOutputOpen = true;
        this.readyToConnect = false;
        this.fd = fileDescriptor;
        this.fdVal = IOUtil.fdVal(fileDescriptor);
        this.state = 0;
        if (z2) {
            this.localAddress = Net.localAddress(fileDescriptor);
        }
    }

    SocketChannelImpl(SelectorProvider selectorProvider, FileDescriptor fileDescriptor, InetSocketAddress inetSocketAddress) throws IOException {
        super(selectorProvider);
        this.readerThread = 0L;
        this.writerThread = 0L;
        this.readLock = new Object();
        this.writeLock = new Object();
        this.stateLock = new Object();
        this.state = -1;
        this.isInputOpen = true;
        this.isOutputOpen = true;
        this.readyToConnect = false;
        this.fd = fileDescriptor;
        this.fdVal = IOUtil.fdVal(fileDescriptor);
        this.state = 2;
        this.localAddress = Net.localAddress(fileDescriptor);
        this.remoteAddress = inetSocketAddress;
    }

    @Override // java.nio.channels.SocketChannel
    public Socket socket() {
        Socket socket;
        synchronized (this.stateLock) {
            if (this.socket == null) {
                this.socket = SocketAdaptor.create(this);
            }
            socket = this.socket;
        }
        return socket;
    }

    @Override // java.nio.channels.SocketChannel, java.nio.channels.NetworkChannel
    public SocketAddress getLocalAddress() throws IOException {
        InetSocketAddress revealedLocalAddress;
        synchronized (this.stateLock) {
            if (!isOpen()) {
                throw new ClosedChannelException();
            }
            revealedLocalAddress = Net.getRevealedLocalAddress(this.localAddress);
        }
        return revealedLocalAddress;
    }

    @Override // java.nio.channels.SocketChannel
    public SocketAddress getRemoteAddress() throws IOException {
        InetSocketAddress inetSocketAddress;
        synchronized (this.stateLock) {
            if (!isOpen()) {
                throw new ClosedChannelException();
            }
            inetSocketAddress = this.remoteAddress;
        }
        return inetSocketAddress;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.nio.channels.SocketChannel, java.nio.channels.NetworkChannel
    public <T> SocketChannel setOption(SocketOption<T> socketOption, T t2) throws IOException {
        if (socketOption == null) {
            throw new NullPointerException();
        }
        if (!supportedOptions().contains(socketOption)) {
            throw new UnsupportedOperationException(PdfOps.SINGLE_QUOTE_TOKEN + ((Object) socketOption) + "' not supported");
        }
        synchronized (this.stateLock) {
            if (!isOpen()) {
                throw new ClosedChannelException();
            }
            if (socketOption == StandardSocketOptions.IP_TOS) {
                Net.setSocketOption(this.fd, Net.isIPv6Available() ? StandardProtocolFamily.INET6 : StandardProtocolFamily.INET, socketOption, t2);
                return this;
            }
            if (socketOption == StandardSocketOptions.SO_REUSEADDR && Net.useExclusiveBind()) {
                this.isReuseAddress = ((Boolean) t2).booleanValue();
                return this;
            }
            Net.setSocketOption(this.fd, Net.UNSPEC, socketOption, t2);
            return this;
        }
    }

    @Override // java.nio.channels.NetworkChannel
    public <T> T getOption(SocketOption<T> socketOption) throws IOException {
        if (socketOption == null) {
            throw new NullPointerException();
        }
        if (!supportedOptions().contains(socketOption)) {
            throw new UnsupportedOperationException(PdfOps.SINGLE_QUOTE_TOKEN + ((Object) socketOption) + "' not supported");
        }
        synchronized (this.stateLock) {
            if (!isOpen()) {
                throw new ClosedChannelException();
            }
            if (socketOption == StandardSocketOptions.SO_REUSEADDR && Net.useExclusiveBind()) {
                return (T) Boolean.valueOf(this.isReuseAddress);
            }
            if (socketOption == StandardSocketOptions.IP_TOS) {
                return (T) Net.getSocketOption(this.fd, Net.isIPv6Available() ? StandardProtocolFamily.INET6 : StandardProtocolFamily.INET, socketOption);
            }
            return (T) Net.getSocketOption(this.fd, Net.UNSPEC, socketOption);
        }
    }

    /* loaded from: rt.jar:sun/nio/ch/SocketChannelImpl$DefaultOptionsHolder.class */
    private static class DefaultOptionsHolder {
        static final Set<SocketOption<?>> defaultOptions = defaultOptions();

        private DefaultOptionsHolder() {
        }

        private static Set<SocketOption<?>> defaultOptions() {
            HashSet hashSet = new HashSet(8);
            hashSet.add(StandardSocketOptions.SO_SNDBUF);
            hashSet.add(StandardSocketOptions.SO_RCVBUF);
            hashSet.add(StandardSocketOptions.SO_KEEPALIVE);
            hashSet.add(StandardSocketOptions.SO_REUSEADDR);
            hashSet.add(StandardSocketOptions.SO_LINGER);
            hashSet.add(StandardSocketOptions.TCP_NODELAY);
            hashSet.add(StandardSocketOptions.IP_TOS);
            hashSet.add(ExtendedSocketOption.SO_OOBINLINE);
            if (ExtendedOptionsImpl.flowSupported()) {
                hashSet.add(ExtendedSocketOptions.SO_FLOW_SLA);
            }
            hashSet.addAll(ExtendedOptionsHelper.keepAliveOptions());
            return Collections.unmodifiableSet(hashSet);
        }
    }

    @Override // java.nio.channels.NetworkChannel
    public final Set<SocketOption<?>> supportedOptions() {
        return DefaultOptionsHolder.defaultOptions;
    }

    private boolean ensureReadOpen() throws ClosedChannelException {
        synchronized (this.stateLock) {
            if (!isOpen()) {
                throw new ClosedChannelException();
            }
            if (!isConnected()) {
                throw new NotYetConnectedException();
            }
            if (!this.isInputOpen) {
                return false;
            }
            return true;
        }
    }

    private void ensureWriteOpen() throws ClosedChannelException {
        synchronized (this.stateLock) {
            if (!isOpen()) {
                throw new ClosedChannelException();
            }
            if (!this.isOutputOpen) {
                throw new ClosedChannelException();
            }
            if (!isConnected()) {
                throw new NotYetConnectedException();
            }
        }
    }

    private void readerCleanup() throws IOException {
        synchronized (this.stateLock) {
            this.readerThread = 0L;
            if (this.state == 3) {
                kill();
            }
        }
    }

    private void writerCleanup() throws IOException {
        synchronized (this.stateLock) {
            this.writerThread = 0L;
            if (this.state == 3) {
                kill();
            }
        }
    }

    @Override // java.nio.channels.SocketChannel, java.nio.channels.ReadableByteChannel
    public int read(ByteBuffer byteBuffer) throws IOException {
        int i2;
        if (byteBuffer == null) {
            throw new NullPointerException();
        }
        synchronized (this.readLock) {
            if (!ensureReadOpen()) {
                return -1;
            }
            try {
                begin();
                synchronized (this.stateLock) {
                    if (isOpen()) {
                        this.readerThread = NativeThread.current();
                        do {
                            i2 = IOUtil.read(this.fd, byteBuffer, -1L, nd);
                            if (i2 != -3) {
                                break;
                            }
                        } while (isOpen());
                        int iNormalize = IOStatus.normalize(i2);
                        readerCleanup();
                        end(i2 > 0 || i2 == -2);
                        synchronized (this.stateLock) {
                            if (i2 <= 0) {
                                if (!this.isInputOpen) {
                                    return -1;
                                }
                            }
                            if ($assertionsDisabled || IOStatus.check(i2)) {
                                return iNormalize;
                            }
                            throw new AssertionError();
                        }
                    }
                    readerCleanup();
                    end(0 > 0 || 0 == -2);
                    synchronized (this.stateLock) {
                        if (0 <= 0) {
                            if (!this.isInputOpen) {
                                return -1;
                            }
                        }
                        if ($assertionsDisabled || IOStatus.check(0)) {
                            return 0;
                        }
                        throw new AssertionError();
                    }
                }
            } catch (Throwable th) {
                readerCleanup();
                end(0 > 0 || 0 == -2);
                synchronized (this.stateLock) {
                    if (0 <= 0) {
                        if (!this.isInputOpen) {
                            return -1;
                        }
                    }
                    if ($assertionsDisabled || IOStatus.check(0)) {
                        throw th;
                    }
                    throw new AssertionError();
                }
            }
        }
    }

    @Override // java.nio.channels.SocketChannel, java.nio.channels.ScatteringByteChannel
    public long read(ByteBuffer[] byteBufferArr, int i2, int i3) throws IOException {
        long j2;
        if (i2 < 0 || i3 < 0 || i2 > byteBufferArr.length - i3) {
            throw new IndexOutOfBoundsException();
        }
        synchronized (this.readLock) {
            if (!ensureReadOpen()) {
                return -1L;
            }
            try {
                begin();
                synchronized (this.stateLock) {
                    if (isOpen()) {
                        this.readerThread = NativeThread.current();
                        do {
                            j2 = IOUtil.read(this.fd, byteBufferArr, i2, i3, nd);
                            if (j2 != -3) {
                                break;
                            }
                        } while (isOpen());
                        long jNormalize = IOStatus.normalize(j2);
                        readerCleanup();
                        end(j2 > 0 || j2 == -2);
                        synchronized (this.stateLock) {
                            if (j2 <= 0) {
                                if (!this.isInputOpen) {
                                    return -1L;
                                }
                            }
                            if ($assertionsDisabled || IOStatus.check(j2)) {
                                return jNormalize;
                            }
                            throw new AssertionError();
                        }
                    }
                    readerCleanup();
                    end(0 > 0 || 0 == -2);
                    synchronized (this.stateLock) {
                        if (0 <= 0) {
                            if (!this.isInputOpen) {
                                return -1L;
                            }
                        }
                        if ($assertionsDisabled || IOStatus.check(0L)) {
                            return 0L;
                        }
                        throw new AssertionError();
                    }
                }
            } catch (Throwable th) {
                readerCleanup();
                end(0 > 0 || 0 == -2);
                synchronized (this.stateLock) {
                    if (0 <= 0) {
                        if (!this.isInputOpen) {
                            return -1L;
                        }
                    }
                    if ($assertionsDisabled || IOStatus.check(0L)) {
                        throw th;
                    }
                    throw new AssertionError();
                }
            }
        }
    }

    @Override // java.nio.channels.SocketChannel, java.nio.channels.WritableByteChannel
    public int write(ByteBuffer byteBuffer) throws IOException {
        int iWrite;
        if (byteBuffer == null) {
            throw new NullPointerException();
        }
        synchronized (this.writeLock) {
            ensureWriteOpen();
            try {
                begin();
                synchronized (this.stateLock) {
                    if (isOpen()) {
                        this.writerThread = NativeThread.current();
                        do {
                            iWrite = IOUtil.write(this.fd, byteBuffer, -1L, nd);
                            if (iWrite != -3) {
                                break;
                            }
                        } while (isOpen());
                        int iNormalize = IOStatus.normalize(iWrite);
                        writerCleanup();
                        end(iWrite > 0 || iWrite == -2);
                        synchronized (this.stateLock) {
                            if (iWrite <= 0) {
                                if (!this.isOutputOpen) {
                                    throw new AsynchronousCloseException();
                                }
                            }
                        }
                        if ($assertionsDisabled || IOStatus.check(iWrite)) {
                            return iNormalize;
                        }
                        throw new AssertionError();
                    }
                    writerCleanup();
                    end(0 > 0 || 0 == -2);
                    synchronized (this.stateLock) {
                        if (0 <= 0) {
                            if (!this.isOutputOpen) {
                                throw new AsynchronousCloseException();
                            }
                        }
                    }
                    if ($assertionsDisabled || IOStatus.check(0)) {
                        return 0;
                    }
                    throw new AssertionError();
                }
            } catch (Throwable th) {
                writerCleanup();
                end(0 > 0 || 0 == -2);
                synchronized (this.stateLock) {
                    if (0 <= 0) {
                        if (!this.isOutputOpen) {
                            throw new AsynchronousCloseException();
                        }
                    }
                    if ($assertionsDisabled || IOStatus.check(0)) {
                        throw th;
                    }
                    throw new AssertionError();
                }
            }
        }
    }

    @Override // java.nio.channels.SocketChannel, java.nio.channels.GatheringByteChannel
    public long write(ByteBuffer[] byteBufferArr, int i2, int i3) throws IOException {
        long jWrite;
        if (i2 < 0 || i3 < 0 || i2 > byteBufferArr.length - i3) {
            throw new IndexOutOfBoundsException();
        }
        synchronized (this.writeLock) {
            ensureWriteOpen();
            try {
                begin();
                synchronized (this.stateLock) {
                    if (isOpen()) {
                        this.writerThread = NativeThread.current();
                        do {
                            jWrite = IOUtil.write(this.fd, byteBufferArr, i2, i3, nd);
                            if (jWrite != -3) {
                                break;
                            }
                        } while (isOpen());
                        long jNormalize = IOStatus.normalize(jWrite);
                        writerCleanup();
                        end(jWrite > 0 || jWrite == -2);
                        synchronized (this.stateLock) {
                            if (jWrite <= 0) {
                                if (!this.isOutputOpen) {
                                    throw new AsynchronousCloseException();
                                }
                            }
                        }
                        if ($assertionsDisabled || IOStatus.check(jWrite)) {
                            return jNormalize;
                        }
                        throw new AssertionError();
                    }
                    writerCleanup();
                    end(0 > 0 || 0 == -2);
                    synchronized (this.stateLock) {
                        if (0 <= 0) {
                            if (!this.isOutputOpen) {
                                throw new AsynchronousCloseException();
                            }
                        }
                    }
                    if ($assertionsDisabled || IOStatus.check(0L)) {
                        return 0L;
                    }
                    throw new AssertionError();
                }
            } catch (Throwable th) {
                writerCleanup();
                end(0 > 0 || 0 == -2);
                synchronized (this.stateLock) {
                    if (0 <= 0) {
                        if (!this.isOutputOpen) {
                            throw new AsynchronousCloseException();
                        }
                    }
                    if ($assertionsDisabled || IOStatus.check(0L)) {
                        throw th;
                    }
                    throw new AssertionError();
                }
            }
        }
    }

    int sendOutOfBandData(byte b2) throws IOException {
        int iSendOutOfBandData;
        synchronized (this.writeLock) {
            ensureWriteOpen();
            try {
                begin();
                synchronized (this.stateLock) {
                    if (isOpen()) {
                        this.writerThread = NativeThread.current();
                        do {
                            iSendOutOfBandData = sendOutOfBandData(this.fd, b2);
                            if (iSendOutOfBandData != -3) {
                                break;
                            }
                        } while (isOpen());
                        int iNormalize = IOStatus.normalize(iSendOutOfBandData);
                        writerCleanup();
                        end(iSendOutOfBandData > 0 || iSendOutOfBandData == -2);
                        synchronized (this.stateLock) {
                            if (iSendOutOfBandData <= 0) {
                                if (!this.isOutputOpen) {
                                    throw new AsynchronousCloseException();
                                }
                            }
                        }
                        if ($assertionsDisabled || IOStatus.check(iSendOutOfBandData)) {
                            return iNormalize;
                        }
                        throw new AssertionError();
                    }
                    writerCleanup();
                    end(0 > 0 || 0 == -2);
                    synchronized (this.stateLock) {
                        if (0 <= 0) {
                            if (!this.isOutputOpen) {
                                throw new AsynchronousCloseException();
                            }
                        }
                    }
                    if ($assertionsDisabled || IOStatus.check(0)) {
                        return 0;
                    }
                    throw new AssertionError();
                }
            } catch (Throwable th) {
                writerCleanup();
                end(0 > 0 || 0 == -2);
                synchronized (this.stateLock) {
                    if (0 <= 0) {
                        if (!this.isOutputOpen) {
                            throw new AsynchronousCloseException();
                        }
                    }
                    if ($assertionsDisabled || IOStatus.check(0)) {
                        throw th;
                    }
                    throw new AssertionError();
                }
            }
        }
    }

    @Override // java.nio.channels.spi.AbstractSelectableChannel
    protected void implConfigureBlocking(boolean z2) throws IOException {
        IOUtil.configureBlocking(this.fd, z2);
    }

    public InetSocketAddress localAddress() {
        InetSocketAddress inetSocketAddress;
        synchronized (this.stateLock) {
            inetSocketAddress = this.localAddress;
        }
        return inetSocketAddress;
    }

    public SocketAddress remoteAddress() {
        InetSocketAddress inetSocketAddress;
        synchronized (this.stateLock) {
            inetSocketAddress = this.remoteAddress;
        }
        return inetSocketAddress;
    }

    @Override // java.nio.channels.SocketChannel, java.nio.channels.NetworkChannel
    public SocketChannel bind(SocketAddress socketAddress) throws IOException {
        synchronized (this.readLock) {
            synchronized (this.writeLock) {
                synchronized (this.stateLock) {
                    if (!isOpen()) {
                        throw new ClosedChannelException();
                    }
                    if (this.state == 1) {
                        throw new ConnectionPendingException();
                    }
                    if (this.localAddress != null) {
                        throw new AlreadyBoundException();
                    }
                    InetSocketAddress inetSocketAddress = socketAddress == null ? new InetSocketAddress(0) : Net.checkAddress(socketAddress);
                    SecurityManager securityManager = System.getSecurityManager();
                    if (securityManager != null) {
                        securityManager.checkListen(inetSocketAddress.getPort());
                    }
                    NetHooks.beforeTcpBind(this.fd, inetSocketAddress.getAddress(), inetSocketAddress.getPort());
                    Net.bind(this.fd, inetSocketAddress.getAddress(), inetSocketAddress.getPort());
                    this.localAddress = Net.localAddress(this.fd);
                }
            }
        }
        return this;
    }

    @Override // java.nio.channels.SocketChannel
    public boolean isConnected() {
        boolean z2;
        synchronized (this.stateLock) {
            z2 = this.state == 2;
        }
        return z2;
    }

    @Override // java.nio.channels.SocketChannel
    public boolean isConnectionPending() {
        boolean z2;
        synchronized (this.stateLock) {
            z2 = this.state == 1;
        }
        return z2;
    }

    void ensureOpenAndUnconnected() throws IOException {
        synchronized (this.stateLock) {
            if (!isOpen()) {
                throw new ClosedChannelException();
            }
            if (this.state == 2) {
                throw new AlreadyConnectedException();
            }
            if (this.state == 1) {
                throw new ConnectionPendingException();
            }
        }
    }

    @Override // java.nio.channels.SocketChannel
    public boolean connect(SocketAddress socketAddress) throws IOException {
        int iConnect;
        synchronized (this.readLock) {
            synchronized (this.writeLock) {
                ensureOpenAndUnconnected();
                InetSocketAddress inetSocketAddressCheckAddress = Net.checkAddress(socketAddress);
                SecurityManager securityManager = System.getSecurityManager();
                if (securityManager != null) {
                    securityManager.checkConnect(inetSocketAddressCheckAddress.getAddress().getHostAddress(), inetSocketAddressCheckAddress.getPort());
                }
                synchronized (blockingLock()) {
                    try {
                        try {
                            begin();
                            synchronized (this.stateLock) {
                                if (isOpen()) {
                                    if (this.localAddress == null) {
                                        NetHooks.beforeTcpConnect(this.fd, inetSocketAddressCheckAddress.getAddress(), inetSocketAddressCheckAddress.getPort());
                                    }
                                    this.readerThread = NativeThread.current();
                                    do {
                                        InetAddress address = inetSocketAddressCheckAddress.getAddress();
                                        if (address.isAnyLocalAddress()) {
                                            address = InetAddress.getLocalHost();
                                        }
                                        iConnect = Net.connect(this.fd, address, inetSocketAddressCheckAddress.getPort());
                                        if (iConnect != -3) {
                                            break;
                                        }
                                    } while (isOpen());
                                    readerCleanup();
                                    end(iConnect > 0 || iConnect == -2);
                                    if (!$assertionsDisabled && !IOStatus.check(iConnect)) {
                                        throw new AssertionError();
                                    }
                                    synchronized (this.stateLock) {
                                        this.remoteAddress = inetSocketAddressCheckAddress;
                                        if (iConnect > 0) {
                                            this.state = 2;
                                            if (isOpen()) {
                                                this.localAddress = Net.localAddress(this.fd);
                                            }
                                            return true;
                                        }
                                        if (!isBlocking()) {
                                            this.state = 1;
                                        } else if (!$assertionsDisabled) {
                                            throw new AssertionError();
                                        }
                                        return false;
                                    }
                                }
                                readerCleanup();
                                end(0 > 0 || 0 == -2);
                                if ($assertionsDisabled || IOStatus.check(0)) {
                                    return false;
                                }
                                throw new AssertionError();
                            }
                        } catch (Throwable th) {
                            readerCleanup();
                            end(0 > 0 || 0 == -2);
                            if ($assertionsDisabled || IOStatus.check(0)) {
                                throw th;
                            }
                            throw new AssertionError();
                        }
                    } catch (IOException e2) {
                        close();
                        throw e2;
                    }
                }
            }
        }
    }

    @Override // java.nio.channels.SocketChannel
    public boolean finishConnect() throws IOException {
        int iCheckConnect;
        synchronized (this.readLock) {
            synchronized (this.writeLock) {
                synchronized (this.stateLock) {
                    if (!isOpen()) {
                        throw new ClosedChannelException();
                    }
                    if (this.state == 2) {
                        return true;
                    }
                    if (this.state != 1) {
                        throw new NoConnectionPendingException();
                    }
                    int i2 = 0;
                    try {
                        try {
                            begin();
                            synchronized (blockingLock()) {
                                synchronized (this.stateLock) {
                                    if (isOpen()) {
                                        this.readerThread = NativeThread.current();
                                        if (!isBlocking()) {
                                            do {
                                                iCheckConnect = checkConnect(this.fd, false, this.readyToConnect);
                                                if (iCheckConnect != -3) {
                                                    break;
                                                }
                                            } while (isOpen());
                                        } else {
                                            while (true) {
                                                iCheckConnect = checkConnect(this.fd, true, this.readyToConnect);
                                                if (iCheckConnect != 0 && (iCheckConnect != -3 || !isOpen())) {
                                                    break;
                                                }
                                            }
                                        }
                                        synchronized (this.stateLock) {
                                            this.readerThread = 0L;
                                            if (this.state == 3) {
                                                kill();
                                                iCheckConnect = 0;
                                            }
                                        }
                                        end(iCheckConnect > 0 || iCheckConnect == -2);
                                        if (!$assertionsDisabled && !IOStatus.check(iCheckConnect)) {
                                            throw new AssertionError();
                                        }
                                        if (iCheckConnect > 0) {
                                            synchronized (this.stateLock) {
                                                this.state = 2;
                                                if (isOpen()) {
                                                    this.localAddress = Net.localAddress(this.fd);
                                                }
                                            }
                                            return true;
                                        }
                                        return false;
                                    }
                                    synchronized (this.stateLock) {
                                        this.readerThread = 0L;
                                        if (this.state == 3) {
                                            kill();
                                            i2 = 0;
                                        }
                                    }
                                    end(i2 > 0 || i2 == -2);
                                    if ($assertionsDisabled || IOStatus.check(i2)) {
                                        return false;
                                    }
                                    throw new AssertionError();
                                }
                            }
                        } catch (Throwable th) {
                            synchronized (this.stateLock) {
                                this.readerThread = 0L;
                                if (this.state == 3) {
                                    kill();
                                    i2 = 0;
                                }
                                end(i2 > 0 || i2 == -2);
                                if ($assertionsDisabled || IOStatus.check(i2)) {
                                    throw th;
                                }
                                throw new AssertionError();
                            }
                        }
                    } catch (IOException e2) {
                        close();
                        throw e2;
                    }
                }
            }
        }
    }

    @Override // java.nio.channels.SocketChannel
    public SocketChannel shutdownInput() throws IOException {
        synchronized (this.stateLock) {
            if (!isOpen()) {
                throw new ClosedChannelException();
            }
            if (!isConnected()) {
                throw new NotYetConnectedException();
            }
            if (this.isInputOpen) {
                Net.shutdown(this.fd, 0);
                if (this.readerThread != 0) {
                    NativeThread.signal(this.readerThread);
                }
                this.isInputOpen = false;
            }
        }
        return this;
    }

    @Override // java.nio.channels.SocketChannel
    public SocketChannel shutdownOutput() throws IOException {
        synchronized (this.stateLock) {
            if (!isOpen()) {
                throw new ClosedChannelException();
            }
            if (!isConnected()) {
                throw new NotYetConnectedException();
            }
            if (this.isOutputOpen) {
                Net.shutdown(this.fd, 1);
                if (this.writerThread != 0) {
                    NativeThread.signal(this.writerThread);
                }
                this.isOutputOpen = false;
            }
        }
        return this;
    }

    public boolean isInputOpen() {
        boolean z2;
        synchronized (this.stateLock) {
            z2 = this.isInputOpen;
        }
        return z2;
    }

    public boolean isOutputOpen() {
        boolean z2;
        synchronized (this.stateLock) {
            z2 = this.isOutputOpen;
        }
        return z2;
    }

    @Override // java.nio.channels.spi.AbstractSelectableChannel
    protected void implCloseSelectableChannel() throws IOException {
        synchronized (this.stateLock) {
            this.isInputOpen = false;
            this.isOutputOpen = false;
            if (this.state != 4) {
                nd.preClose(this.fd);
            }
            if (this.readerThread != 0) {
                NativeThread.signal(this.readerThread);
            }
            if (this.writerThread != 0) {
                NativeThread.signal(this.writerThread);
            }
            if (!isRegistered()) {
                kill();
            }
        }
    }

    @Override // sun.nio.ch.SelChImpl
    public void kill() throws IOException {
        synchronized (this.stateLock) {
            if (this.state == 4) {
                return;
            }
            if (this.state == -1) {
                this.state = 4;
                return;
            }
            if (!$assertionsDisabled && (isOpen() || isRegistered())) {
                throw new AssertionError();
            }
            if (this.readerThread == 0 && this.writerThread == 0) {
                nd.close(this.fd);
                this.state = 4;
            } else {
                this.state = 3;
            }
        }
    }

    public boolean translateReadyOps(int i2, int i3, SelectionKeyImpl selectionKeyImpl) {
        int iNioInterestOps = selectionKeyImpl.nioInterestOps();
        int iNioReadyOps = selectionKeyImpl.nioReadyOps();
        int i4 = i3;
        if ((i2 & Net.POLLNVAL) != 0) {
            return false;
        }
        if ((i2 & (Net.POLLERR | Net.POLLHUP)) != 0) {
            selectionKeyImpl.nioReadyOps(iNioInterestOps);
            this.readyToConnect = true;
            return (iNioInterestOps & (iNioReadyOps ^ (-1))) != 0;
        }
        if ((i2 & Net.POLLIN) != 0 && (iNioInterestOps & 1) != 0 && this.state == 2) {
            i4 |= 1;
        }
        if ((i2 & Net.POLLCONN) != 0 && (iNioInterestOps & 8) != 0 && (this.state == 0 || this.state == 1)) {
            i4 |= 8;
            this.readyToConnect = true;
        }
        if ((i2 & Net.POLLOUT) != 0 && (iNioInterestOps & 4) != 0 && this.state == 2) {
            i4 |= 4;
        }
        selectionKeyImpl.nioReadyOps(i4);
        return (i4 & (iNioReadyOps ^ (-1))) != 0;
    }

    @Override // sun.nio.ch.SelChImpl
    public boolean translateAndUpdateReadyOps(int i2, SelectionKeyImpl selectionKeyImpl) {
        return translateReadyOps(i2, selectionKeyImpl.nioReadyOps(), selectionKeyImpl);
    }

    @Override // sun.nio.ch.SelChImpl
    public boolean translateAndSetReadyOps(int i2, SelectionKeyImpl selectionKeyImpl) {
        return translateReadyOps(i2, 0, selectionKeyImpl);
    }

    int poll(int i2, long j2) throws IOException {
        if (!$assertionsDisabled && (!Thread.holdsLock(blockingLock()) || isBlocking())) {
            throw new AssertionError();
        }
        synchronized (this.readLock) {
            try {
                begin();
                synchronized (this.stateLock) {
                    if (!isOpen()) {
                        return 0;
                    }
                    this.readerThread = NativeThread.current();
                    int iPoll = Net.poll(this.fd, i2, j2);
                    readerCleanup();
                    end(iPoll > 0);
                    return iPoll;
                }
            } finally {
                readerCleanup();
                end(0 > 0);
            }
        }
    }

    @Override // sun.nio.ch.SelChImpl
    public void translateAndSetInterestOps(int i2, SelectionKeyImpl selectionKeyImpl) {
        int i3 = 0;
        if ((i2 & 1) != 0) {
            i3 = 0 | Net.POLLIN;
        }
        if ((i2 & 4) != 0) {
            i3 |= Net.POLLOUT;
        }
        if ((i2 & 8) != 0) {
            i3 |= Net.POLLCONN;
        }
        selectionKeyImpl.selector.putEventOps(selectionKeyImpl, i3);
    }

    @Override // sun.nio.ch.SelChImpl
    public FileDescriptor getFD() {
        return this.fd;
    }

    @Override // sun.nio.ch.SelChImpl
    public int getFDVal() {
        return this.fdVal;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(getClass().getSuperclass().getName());
        stringBuffer.append('[');
        if (!isOpen()) {
            stringBuffer.append(JInternalFrame.IS_CLOSED_PROPERTY);
        } else {
            synchronized (this.stateLock) {
                switch (this.state) {
                    case 0:
                        stringBuffer.append("unconnected");
                        break;
                    case 1:
                        stringBuffer.append("connection-pending");
                        break;
                    case 2:
                        stringBuffer.append("connected");
                        if (!this.isInputOpen) {
                            stringBuffer.append(" ishut");
                        }
                        if (!this.isOutputOpen) {
                            stringBuffer.append(" oshut");
                            break;
                        }
                        break;
                }
                InetSocketAddress inetSocketAddressLocalAddress = localAddress();
                if (inetSocketAddressLocalAddress != null) {
                    stringBuffer.append(" local=");
                    stringBuffer.append(Net.getRevealedLocalAddressAsString(inetSocketAddressLocalAddress));
                }
                if (remoteAddress() != null) {
                    stringBuffer.append(" remote=");
                    stringBuffer.append(remoteAddress().toString());
                }
            }
        }
        stringBuffer.append(']');
        return stringBuffer.toString();
    }
}
