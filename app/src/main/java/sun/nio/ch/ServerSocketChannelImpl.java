package sun.nio.ch;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.channels.AlreadyBoundException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NetworkChannel;
import java.nio.channels.NotYetBoundException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JInternalFrame;
import org.icepdf.core.util.PdfOps;
import sun.net.ExtendedOptionsHelper;
import sun.net.NetHooks;

/* loaded from: rt.jar:sun/nio/ch/ServerSocketChannelImpl.class */
class ServerSocketChannelImpl extends ServerSocketChannel implements SelChImpl {
    private static NativeDispatcher nd;
    private final FileDescriptor fd;
    private final int fdVal;
    private volatile long thread;
    private final Object lock;
    private final Object stateLock;
    private static final int ST_UNINITIALIZED = -1;
    private static final int ST_INUSE = 0;
    private static final int ST_KILLED = 1;
    private int state;
    private InetSocketAddress localAddress;
    private boolean isReuseAddress;
    ServerSocket socket;
    static final /* synthetic */ boolean $assertionsDisabled;

    private native int accept0(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, InetSocketAddress[] inetSocketAddressArr) throws IOException;

    private static native void initIDs();

    @Override // java.nio.channels.ServerSocketChannel, java.nio.channels.NetworkChannel
    public /* bridge */ /* synthetic */ NetworkChannel setOption(SocketOption socketOption, Object obj) throws IOException {
        return setOption((SocketOption<SocketOption>) socketOption, (SocketOption) obj);
    }

    static {
        $assertionsDisabled = !ServerSocketChannelImpl.class.desiredAssertionStatus();
        IOUtil.load();
        initIDs();
        nd = new SocketDispatcher();
    }

    ServerSocketChannelImpl(SelectorProvider selectorProvider) throws IOException {
        super(selectorProvider);
        this.thread = 0L;
        this.lock = new Object();
        this.stateLock = new Object();
        this.state = -1;
        this.fd = Net.serverSocket(true);
        this.fdVal = IOUtil.fdVal(this.fd);
        this.state = 0;
    }

    ServerSocketChannelImpl(SelectorProvider selectorProvider, FileDescriptor fileDescriptor, boolean z2) throws IOException {
        super(selectorProvider);
        this.thread = 0L;
        this.lock = new Object();
        this.stateLock = new Object();
        this.state = -1;
        this.fd = fileDescriptor;
        this.fdVal = IOUtil.fdVal(fileDescriptor);
        this.state = 0;
        if (z2) {
            this.localAddress = Net.localAddress(fileDescriptor);
        }
    }

    @Override // java.nio.channels.ServerSocketChannel
    public ServerSocket socket() {
        ServerSocket serverSocket;
        synchronized (this.stateLock) {
            if (this.socket == null) {
                this.socket = ServerSocketAdaptor.create(this);
            }
            serverSocket = this.socket;
        }
        return serverSocket;
    }

    @Override // java.nio.channels.ServerSocketChannel, java.nio.channels.NetworkChannel
    public SocketAddress getLocalAddress() throws IOException {
        InetSocketAddress revealedLocalAddress;
        synchronized (this.stateLock) {
            if (!isOpen()) {
                throw new ClosedChannelException();
            }
            revealedLocalAddress = this.localAddress == null ? this.localAddress : Net.getRevealedLocalAddress(Net.asInetSocketAddress(this.localAddress));
        }
        return revealedLocalAddress;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.nio.channels.ServerSocketChannel, java.nio.channels.NetworkChannel
    public <T> ServerSocketChannel setOption(SocketOption<T> socketOption, T t2) throws IOException {
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
            } else {
                Net.setSocketOption(this.fd, Net.UNSPEC, socketOption, t2);
            }
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
            return (T) Net.getSocketOption(this.fd, Net.UNSPEC, socketOption);
        }
    }

    /* loaded from: rt.jar:sun/nio/ch/ServerSocketChannelImpl$DefaultOptionsHolder.class */
    private static class DefaultOptionsHolder {
        static final Set<SocketOption<?>> defaultOptions = defaultOptions();

        private DefaultOptionsHolder() {
        }

        private static Set<SocketOption<?>> defaultOptions() {
            HashSet hashSet = new HashSet(2);
            hashSet.add(StandardSocketOptions.SO_RCVBUF);
            hashSet.add(StandardSocketOptions.SO_REUSEADDR);
            hashSet.add(StandardSocketOptions.IP_TOS);
            hashSet.addAll(ExtendedOptionsHelper.keepAliveOptions());
            return Collections.unmodifiableSet(hashSet);
        }
    }

    @Override // java.nio.channels.NetworkChannel
    public final Set<SocketOption<?>> supportedOptions() {
        return DefaultOptionsHolder.defaultOptions;
    }

    public boolean isBound() {
        boolean z2;
        synchronized (this.stateLock) {
            z2 = this.localAddress != null;
        }
        return z2;
    }

    public InetSocketAddress localAddress() {
        InetSocketAddress inetSocketAddress;
        synchronized (this.stateLock) {
            inetSocketAddress = this.localAddress;
        }
        return inetSocketAddress;
    }

    @Override // java.nio.channels.ServerSocketChannel
    public ServerSocketChannel bind(SocketAddress socketAddress, int i2) throws IOException {
        synchronized (this.lock) {
            if (!isOpen()) {
                throw new ClosedChannelException();
            }
            if (isBound()) {
                throw new AlreadyBoundException();
            }
            InetSocketAddress inetSocketAddress = socketAddress == null ? new InetSocketAddress(0) : Net.checkAddress(socketAddress);
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkListen(inetSocketAddress.getPort());
            }
            NetHooks.beforeTcpBind(this.fd, inetSocketAddress.getAddress(), inetSocketAddress.getPort());
            Net.bind(this.fd, inetSocketAddress.getAddress(), inetSocketAddress.getPort());
            Net.listen(this.fd, i2 < 1 ? 50 : i2);
            synchronized (this.stateLock) {
                this.localAddress = Net.localAddress(this.fd);
            }
        }
        return this;
    }

    @Override // java.nio.channels.ServerSocketChannel
    public SocketChannel accept() throws IOException {
        synchronized (this.lock) {
            if (!isOpen()) {
                throw new ClosedChannelException();
            }
            if (!isBound()) {
                throw new NotYetBoundException();
            }
            int iAccept = 0;
            FileDescriptor fileDescriptor = new FileDescriptor();
            InetSocketAddress[] inetSocketAddressArr = new InetSocketAddress[1];
            try {
                begin();
                if (isOpen()) {
                    this.thread = NativeThread.current();
                    do {
                        iAccept = accept(this.fd, fileDescriptor, inetSocketAddressArr);
                        if (iAccept != -3) {
                            break;
                        }
                    } while (isOpen());
                    this.thread = 0L;
                    end(iAccept > 0);
                    if (!$assertionsDisabled && !IOStatus.check(iAccept)) {
                        throw new AssertionError();
                    }
                    if (iAccept < 1) {
                        return null;
                    }
                    IOUtil.configureBlocking(fileDescriptor, true);
                    InetSocketAddress inetSocketAddress = inetSocketAddressArr[0];
                    SocketChannelImpl socketChannelImpl = new SocketChannelImpl(provider(), fileDescriptor, inetSocketAddress);
                    SecurityManager securityManager = System.getSecurityManager();
                    if (securityManager != null) {
                        try {
                            securityManager.checkAccept(inetSocketAddress.getAddress().getHostAddress(), inetSocketAddress.getPort());
                        } catch (SecurityException e2) {
                            socketChannelImpl.close();
                            throw e2;
                        }
                    }
                    return socketChannelImpl;
                }
                this.thread = 0L;
                end(0 > 0);
                if ($assertionsDisabled || IOStatus.check(0)) {
                    return null;
                }
                throw new AssertionError();
            } catch (Throwable th) {
                this.thread = 0L;
                end(iAccept > 0);
                if ($assertionsDisabled || IOStatus.check(iAccept)) {
                    throw th;
                }
                throw new AssertionError();
            }
        }
    }

    @Override // java.nio.channels.spi.AbstractSelectableChannel
    protected void implConfigureBlocking(boolean z2) throws IOException {
        IOUtil.configureBlocking(this.fd, z2);
    }

    @Override // java.nio.channels.spi.AbstractSelectableChannel
    protected void implCloseSelectableChannel() throws IOException {
        synchronized (this.stateLock) {
            if (this.state != 1) {
                nd.preClose(this.fd);
            }
            long j2 = this.thread;
            if (j2 != 0) {
                NativeThread.signal(j2);
            }
            if (!isRegistered()) {
                kill();
            }
        }
    }

    @Override // sun.nio.ch.SelChImpl
    public void kill() throws IOException {
        synchronized (this.stateLock) {
            if (this.state == 1) {
                return;
            }
            if (this.state == -1) {
                this.state = 1;
            } else {
                if (!$assertionsDisabled && (isOpen() || isRegistered())) {
                    throw new AssertionError();
                }
                nd.close(this.fd);
                this.state = 1;
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
            return (iNioInterestOps & (iNioReadyOps ^ (-1))) != 0;
        }
        if ((i2 & Net.POLLIN) != 0 && (iNioInterestOps & 16) != 0) {
            i4 |= 16;
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
        synchronized (this.lock) {
            try {
                begin();
                synchronized (this.stateLock) {
                    if (!isOpen()) {
                        return 0;
                    }
                    this.thread = NativeThread.current();
                    int iPoll = Net.poll(this.fd, i2, j2);
                    this.thread = 0L;
                    end(iPoll > 0);
                    return iPoll;
                }
            } finally {
                this.thread = 0L;
                end(0 > 0);
            }
        }
    }

    @Override // sun.nio.ch.SelChImpl
    public void translateAndSetInterestOps(int i2, SelectionKeyImpl selectionKeyImpl) {
        int i3 = 0;
        if ((i2 & 16) != 0) {
            i3 = 0 | Net.POLLIN;
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
        stringBuffer.append(getClass().getName());
        stringBuffer.append('[');
        if (!isOpen()) {
            stringBuffer.append(JInternalFrame.IS_CLOSED_PROPERTY);
        } else {
            synchronized (this.stateLock) {
                InetSocketAddress inetSocketAddressLocalAddress = localAddress();
                if (inetSocketAddressLocalAddress == null) {
                    stringBuffer.append("unbound");
                } else {
                    stringBuffer.append(Net.getRevealedLocalAddressAsString(inetSocketAddressLocalAddress));
                }
            }
        }
        stringBuffer.append(']');
        return stringBuffer.toString();
    }

    private int accept(FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, InetSocketAddress[] inetSocketAddressArr) throws IOException {
        return accept0(fileDescriptor, fileDescriptor2, inetSocketAddressArr);
    }
}
