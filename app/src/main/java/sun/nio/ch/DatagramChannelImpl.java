package sun.nio.ch;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.PortUnreachableException;
import java.net.ProtocolFamily;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AlreadyBoundException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.MembershipKey;
import java.nio.channels.NetworkChannel;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.UnsupportedAddressTypeException;
import java.nio.channels.spi.SelectorProvider;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import jdk.net.ExtendedSocketOptions;
import org.icepdf.core.util.PdfOps;
import sun.net.ExtendedOptionsImpl;
import sun.net.ResourceManager;
import sun.nio.ch.MembershipKeyImpl;

/* loaded from: rt.jar:sun/nio/ch/DatagramChannelImpl.class */
class DatagramChannelImpl extends DatagramChannel implements SelChImpl {
    private static NativeDispatcher nd;
    private final FileDescriptor fd;
    private final int fdVal;
    private final ProtocolFamily family;
    private volatile long readerThread;
    private volatile long writerThread;
    private InetAddress cachedSenderInetAddress;
    private int cachedSenderPort;
    private final Object readLock;
    private final Object writeLock;
    private final Object stateLock;
    private static final int ST_UNINITIALIZED = -1;
    private static final int ST_UNCONNECTED = 0;
    private static final int ST_CONNECTED = 1;
    private static final int ST_KILLED = 2;
    private int state;
    private InetSocketAddress localAddress;
    private InetSocketAddress remoteAddress;
    private DatagramSocket socket;
    private MembershipRegistry registry;
    private boolean reuseAddressEmulated;
    private boolean isReuseAddress;
    private SocketAddress sender;
    static final /* synthetic */ boolean $assertionsDisabled;

    private static native void initIDs();

    private static native void disconnect0(FileDescriptor fileDescriptor, boolean z2) throws IOException;

    private native int receive0(FileDescriptor fileDescriptor, long j2, int i2, boolean z2) throws IOException;

    private native int send0(boolean z2, FileDescriptor fileDescriptor, long j2, int i2, InetAddress inetAddress, int i3) throws IOException;

    @Override // java.nio.channels.DatagramChannel, java.nio.channels.NetworkChannel
    public /* bridge */ /* synthetic */ NetworkChannel setOption(SocketOption socketOption, Object obj) throws IOException {
        return setOption((SocketOption<SocketOption>) socketOption, (SocketOption) obj);
    }

    static {
        $assertionsDisabled = !DatagramChannelImpl.class.desiredAssertionStatus();
        nd = new DatagramDispatcher();
        IOUtil.load();
        initIDs();
    }

    public DatagramChannelImpl(SelectorProvider selectorProvider) throws IOException {
        super(selectorProvider);
        this.readerThread = 0L;
        this.writerThread = 0L;
        this.readLock = new Object();
        this.writeLock = new Object();
        this.stateLock = new Object();
        this.state = -1;
        ResourceManager.beforeUdpCreate();
        try {
            this.family = Net.isIPv6Available() ? StandardProtocolFamily.INET6 : StandardProtocolFamily.INET;
            this.fd = Net.socket(this.family, false);
            this.fdVal = IOUtil.fdVal(this.fd);
            this.state = 0;
        } catch (IOException e2) {
            ResourceManager.afterUdpClose();
            throw e2;
        }
    }

    public DatagramChannelImpl(SelectorProvider selectorProvider, ProtocolFamily protocolFamily) throws IOException {
        super(selectorProvider);
        this.readerThread = 0L;
        this.writerThread = 0L;
        this.readLock = new Object();
        this.writeLock = new Object();
        this.stateLock = new Object();
        this.state = -1;
        if (protocolFamily != StandardProtocolFamily.INET && protocolFamily != StandardProtocolFamily.INET6) {
            if (protocolFamily == null) {
                throw new NullPointerException("'family' is null");
            }
            throw new UnsupportedOperationException("Protocol family not supported");
        }
        if (protocolFamily == StandardProtocolFamily.INET6 && !Net.isIPv6Available()) {
            throw new UnsupportedOperationException("IPv6 not available");
        }
        ResourceManager.beforeUdpCreate();
        try {
            this.family = protocolFamily;
            this.fd = Net.socket(protocolFamily, false);
            this.fdVal = IOUtil.fdVal(this.fd);
            this.state = 0;
        } catch (IOException e2) {
            ResourceManager.afterUdpClose();
            throw e2;
        }
    }

    public DatagramChannelImpl(SelectorProvider selectorProvider, FileDescriptor fileDescriptor) throws IOException {
        super(selectorProvider);
        this.readerThread = 0L;
        this.writerThread = 0L;
        this.readLock = new Object();
        this.writeLock = new Object();
        this.stateLock = new Object();
        this.state = -1;
        ResourceManager.beforeUdpCreate();
        this.family = Net.isIPv6Available() ? StandardProtocolFamily.INET6 : StandardProtocolFamily.INET;
        this.fd = fileDescriptor;
        this.fdVal = IOUtil.fdVal(fileDescriptor);
        this.state = 0;
        this.localAddress = Net.localAddress(fileDescriptor);
    }

    @Override // java.nio.channels.DatagramChannel
    public DatagramSocket socket() {
        DatagramSocket datagramSocket;
        synchronized (this.stateLock) {
            if (this.socket == null) {
                this.socket = DatagramSocketAdaptor.create(this);
            }
            datagramSocket = this.socket;
        }
        return datagramSocket;
    }

    @Override // java.nio.channels.DatagramChannel, java.nio.channels.NetworkChannel
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

    @Override // java.nio.channels.DatagramChannel
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
    @Override // java.nio.channels.DatagramChannel, java.nio.channels.NetworkChannel
    public <T> DatagramChannel setOption(SocketOption<T> socketOption, T t2) throws IOException {
        if (socketOption == null) {
            throw new NullPointerException();
        }
        if (!supportedOptions().contains(socketOption)) {
            throw new UnsupportedOperationException(PdfOps.SINGLE_QUOTE_TOKEN + ((Object) socketOption) + "' not supported");
        }
        synchronized (this.stateLock) {
            ensureOpen();
            if (socketOption == StandardSocketOptions.IP_TOS || socketOption == StandardSocketOptions.IP_MULTICAST_TTL || socketOption == StandardSocketOptions.IP_MULTICAST_LOOP) {
                Net.setSocketOption(this.fd, this.family, socketOption, t2);
                return this;
            }
            if (socketOption == StandardSocketOptions.IP_MULTICAST_IF) {
                if (t2 == 0) {
                    throw new IllegalArgumentException("Cannot set IP_MULTICAST_IF to 'null'");
                }
                NetworkInterface networkInterface = (NetworkInterface) t2;
                if (this.family == StandardProtocolFamily.INET6) {
                    int index = networkInterface.getIndex();
                    if (index == -1) {
                        throw new IOException("Network interface cannot be identified");
                    }
                    Net.setInterface6(this.fd, index);
                } else {
                    Inet4Address inet4AddressAnyInet4Address = Net.anyInet4Address(networkInterface);
                    if (inet4AddressAnyInet4Address == null) {
                        throw new IOException("Network interface not configured for IPv4");
                    }
                    Net.setInterface4(this.fd, Net.inet4AsInt(inet4AddressAnyInet4Address));
                }
                return this;
            }
            if (socketOption == StandardSocketOptions.SO_REUSEADDR && Net.useExclusiveBind() && this.localAddress != null) {
                this.reuseAddressEmulated = true;
                this.isReuseAddress = ((Boolean) t2).booleanValue();
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
            ensureOpen();
            if (socketOption == StandardSocketOptions.IP_TOS || socketOption == StandardSocketOptions.IP_MULTICAST_TTL || socketOption == StandardSocketOptions.IP_MULTICAST_LOOP) {
                return (T) Net.getSocketOption(this.fd, this.family, socketOption);
            }
            if (socketOption == StandardSocketOptions.IP_MULTICAST_IF) {
                if (this.family == StandardProtocolFamily.INET) {
                    int interface4 = Net.getInterface4(this.fd);
                    if (interface4 == 0) {
                        return null;
                    }
                    T t2 = (T) NetworkInterface.getByInetAddress(Net.inet4FromInt(interface4));
                    if (t2 == null) {
                        throw new IOException("Unable to map address to interface");
                    }
                    return t2;
                }
                int interface6 = Net.getInterface6(this.fd);
                if (interface6 == 0) {
                    return null;
                }
                T t3 = (T) NetworkInterface.getByIndex(interface6);
                if (t3 == null) {
                    throw new IOException("Unable to map index to interface");
                }
                return t3;
            }
            if (socketOption == StandardSocketOptions.SO_REUSEADDR && this.reuseAddressEmulated) {
                return (T) Boolean.valueOf(this.isReuseAddress);
            }
            return (T) Net.getSocketOption(this.fd, Net.UNSPEC, socketOption);
        }
    }

    /* loaded from: rt.jar:sun/nio/ch/DatagramChannelImpl$DefaultOptionsHolder.class */
    private static class DefaultOptionsHolder {
        static final Set<SocketOption<?>> defaultOptions = defaultOptions();

        private DefaultOptionsHolder() {
        }

        private static Set<SocketOption<?>> defaultOptions() {
            HashSet hashSet = new HashSet(8);
            hashSet.add(StandardSocketOptions.SO_SNDBUF);
            hashSet.add(StandardSocketOptions.SO_RCVBUF);
            hashSet.add(StandardSocketOptions.SO_REUSEADDR);
            hashSet.add(StandardSocketOptions.SO_BROADCAST);
            hashSet.add(StandardSocketOptions.IP_TOS);
            hashSet.add(StandardSocketOptions.IP_MULTICAST_IF);
            hashSet.add(StandardSocketOptions.IP_MULTICAST_TTL);
            hashSet.add(StandardSocketOptions.IP_MULTICAST_LOOP);
            if (ExtendedOptionsImpl.flowSupported()) {
                hashSet.add(ExtendedSocketOptions.SO_FLOW_SLA);
            }
            return Collections.unmodifiableSet(hashSet);
        }
    }

    @Override // java.nio.channels.NetworkChannel
    public final Set<SocketOption<?>> supportedOptions() {
        return DefaultOptionsHolder.defaultOptions;
    }

    private void ensureOpen() throws ClosedChannelException {
        if (!isOpen()) {
            throw new ClosedChannelException();
        }
    }

    @Override // java.nio.channels.DatagramChannel
    public SocketAddress receive(ByteBuffer byteBuffer) throws IOException {
        if (byteBuffer.isReadOnly()) {
            throw new IllegalArgumentException("Read-only buffer");
        }
        if (byteBuffer == null) {
            throw new NullPointerException();
        }
        synchronized (this.readLock) {
            ensureOpen();
            if (localAddress() == null) {
                bind((SocketAddress) null);
            }
            int iReceive = 0;
            ByteBuffer temporaryDirectBuffer = null;
            try {
                begin();
                if (!isOpen()) {
                    if (0 != 0) {
                        Util.releaseTemporaryDirectBuffer(null);
                    }
                    this.readerThread = 0L;
                    end(0 > 0 || 0 == -2);
                    if ($assertionsDisabled || IOStatus.check(0)) {
                        return null;
                    }
                    throw new AssertionError();
                }
                SecurityManager securityManager = System.getSecurityManager();
                this.readerThread = NativeThread.current();
                if (isConnected() || securityManager == null) {
                    do {
                        iReceive = receive(this.fd, byteBuffer);
                        if (iReceive != -3) {
                            break;
                        }
                    } while (isOpen());
                    if (iReceive == -2) {
                        if (0 != 0) {
                            Util.releaseTemporaryDirectBuffer(null);
                        }
                        this.readerThread = 0L;
                        end(iReceive > 0 || iReceive == -2);
                        if ($assertionsDisabled || IOStatus.check(iReceive)) {
                            return null;
                        }
                        throw new AssertionError();
                    }
                } else {
                    temporaryDirectBuffer = Util.getTemporaryDirectBuffer(byteBuffer.remaining());
                    while (true) {
                        iReceive = receive(this.fd, temporaryDirectBuffer);
                        if (iReceive != -3 || !isOpen()) {
                            if (iReceive == -2) {
                                if (temporaryDirectBuffer != null) {
                                    Util.releaseTemporaryDirectBuffer(temporaryDirectBuffer);
                                }
                                this.readerThread = 0L;
                                end(iReceive > 0 || iReceive == -2);
                                if ($assertionsDisabled || IOStatus.check(iReceive)) {
                                    return null;
                                }
                                throw new AssertionError();
                            }
                            InetSocketAddress inetSocketAddress = (InetSocketAddress) this.sender;
                            try {
                                securityManager.checkAccept(inetSocketAddress.getAddress().getHostAddress(), inetSocketAddress.getPort());
                                temporaryDirectBuffer.flip();
                                byteBuffer.put(temporaryDirectBuffer);
                                break;
                            } catch (SecurityException e2) {
                                temporaryDirectBuffer.clear();
                            }
                        }
                    }
                }
                SocketAddress socketAddress = this.sender;
                if (temporaryDirectBuffer != null) {
                    Util.releaseTemporaryDirectBuffer(temporaryDirectBuffer);
                }
                this.readerThread = 0L;
                end(iReceive > 0 || iReceive == -2);
                if ($assertionsDisabled || IOStatus.check(iReceive)) {
                    return socketAddress;
                }
                throw new AssertionError();
            } catch (Throwable th) {
                if (temporaryDirectBuffer != null) {
                    Util.releaseTemporaryDirectBuffer(temporaryDirectBuffer);
                }
                this.readerThread = 0L;
                end(iReceive > 0 || iReceive == -2);
                if ($assertionsDisabled || IOStatus.check(iReceive)) {
                    throw th;
                }
                throw new AssertionError();
            }
        }
    }

    private int receive(FileDescriptor fileDescriptor, ByteBuffer byteBuffer) throws IOException {
        int iPosition = byteBuffer.position();
        int iLimit = byteBuffer.limit();
        if (!$assertionsDisabled && iPosition > iLimit) {
            throw new AssertionError();
        }
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        if ((byteBuffer instanceof DirectBuffer) && i2 > 0) {
            return receiveIntoNativeBuffer(fileDescriptor, byteBuffer, i2, iPosition);
        }
        int iMax = Math.max(i2, 1);
        ByteBuffer temporaryDirectBuffer = Util.getTemporaryDirectBuffer(iMax);
        try {
            int iReceiveIntoNativeBuffer = receiveIntoNativeBuffer(fileDescriptor, temporaryDirectBuffer, iMax, 0);
            temporaryDirectBuffer.flip();
            if (iReceiveIntoNativeBuffer > 0 && i2 > 0) {
                byteBuffer.put(temporaryDirectBuffer);
            }
            return iReceiveIntoNativeBuffer;
        } finally {
            Util.releaseTemporaryDirectBuffer(temporaryDirectBuffer);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private int receiveIntoNativeBuffer(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, int i2, int i3) throws IOException {
        int iReceive0 = receive0(fileDescriptor, ((DirectBuffer) byteBuffer).address() + i3, i2, isConnected());
        if (iReceive0 > 0) {
            byteBuffer.position(i3 + iReceive0);
        }
        return iReceive0;
    }

    @Override // java.nio.channels.DatagramChannel
    public int send(ByteBuffer byteBuffer, SocketAddress socketAddress) throws IOException {
        int iSend;
        if (byteBuffer == null) {
            throw new NullPointerException();
        }
        synchronized (this.writeLock) {
            ensureOpen();
            InetSocketAddress inetSocketAddressCheckAddress = Net.checkAddress(socketAddress);
            InetAddress address = inetSocketAddressCheckAddress.getAddress();
            if (address == null) {
                throw new IOException("Target address not resolved");
            }
            synchronized (this.stateLock) {
                if (!isConnected()) {
                    if (socketAddress == null) {
                        throw new NullPointerException();
                    }
                    SecurityManager securityManager = System.getSecurityManager();
                    if (securityManager != null) {
                        if (address.isMulticastAddress()) {
                            securityManager.checkMulticast(address);
                        } else {
                            securityManager.checkConnect(address.getHostAddress(), inetSocketAddressCheckAddress.getPort());
                        }
                    }
                    try {
                        begin();
                        if (isOpen()) {
                            this.writerThread = NativeThread.current();
                            do {
                                iSend = send(this.fd, byteBuffer, inetSocketAddressCheckAddress);
                                if (iSend != -3) {
                                    break;
                                }
                            } while (isOpen());
                            synchronized (this.stateLock) {
                                if (isOpen() && this.localAddress == null) {
                                    this.localAddress = Net.localAddress(this.fd);
                                }
                            }
                            int iNormalize = IOStatus.normalize(iSend);
                            this.writerThread = 0L;
                            end(iSend > 0 || iSend == -2);
                            if ($assertionsDisabled || IOStatus.check(iSend)) {
                                return iNormalize;
                            }
                            throw new AssertionError();
                        }
                        this.writerThread = 0L;
                        end(0 > 0 || 0 == -2);
                        if ($assertionsDisabled || IOStatus.check(0)) {
                            return 0;
                        }
                        throw new AssertionError();
                    } catch (Throwable th) {
                        this.writerThread = 0L;
                        end(0 > 0 || 0 == -2);
                        if ($assertionsDisabled || IOStatus.check(0)) {
                            throw th;
                        }
                        throw new AssertionError();
                    }
                }
                if (!socketAddress.equals(this.remoteAddress)) {
                    throw new IllegalArgumentException("Connected address not equal to target address");
                }
                return write(byteBuffer);
            }
        }
    }

    private int send(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, InetSocketAddress inetSocketAddress) throws IOException {
        if (byteBuffer instanceof DirectBuffer) {
            return sendFromNativeBuffer(fileDescriptor, byteBuffer, inetSocketAddress);
        }
        int iPosition = byteBuffer.position();
        int iLimit = byteBuffer.limit();
        if (!$assertionsDisabled && iPosition > iLimit) {
            throw new AssertionError();
        }
        ByteBuffer temporaryDirectBuffer = Util.getTemporaryDirectBuffer(iPosition <= iLimit ? iLimit - iPosition : 0);
        try {
            temporaryDirectBuffer.put(byteBuffer);
            temporaryDirectBuffer.flip();
            byteBuffer.position(iPosition);
            int iSendFromNativeBuffer = sendFromNativeBuffer(fileDescriptor, temporaryDirectBuffer, inetSocketAddress);
            if (iSendFromNativeBuffer > 0) {
                byteBuffer.position(iPosition + iSendFromNativeBuffer);
            }
            return iSendFromNativeBuffer;
        } finally {
            Util.releaseTemporaryDirectBuffer(temporaryDirectBuffer);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private int sendFromNativeBuffer(FileDescriptor fileDescriptor, ByteBuffer byteBuffer, InetSocketAddress inetSocketAddress) throws IOException {
        int iSend0;
        int iPosition = byteBuffer.position();
        int iLimit = byteBuffer.limit();
        if (!$assertionsDisabled && iPosition > iLimit) {
            throw new AssertionError();
        }
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        try {
            iSend0 = send0(this.family != StandardProtocolFamily.INET, fileDescriptor, ((DirectBuffer) byteBuffer).address() + iPosition, i2, inetSocketAddress.getAddress(), inetSocketAddress.getPort());
        } catch (PortUnreachableException e2) {
            if (isConnected()) {
                throw e2;
            }
            iSend0 = i2;
        }
        if (iSend0 > 0) {
            byteBuffer.position(iPosition + iSend0);
        }
        return iSend0;
    }

    @Override // java.nio.channels.DatagramChannel, java.nio.channels.ReadableByteChannel
    public int read(ByteBuffer byteBuffer) throws IOException {
        if (byteBuffer == null) {
            throw new NullPointerException();
        }
        synchronized (this.readLock) {
            synchronized (this.stateLock) {
                ensureOpen();
                if (!isConnected()) {
                    throw new NotYetConnectedException();
                }
            }
            int i2 = 0;
            try {
                begin();
                if (isOpen()) {
                    this.readerThread = NativeThread.current();
                    do {
                        i2 = IOUtil.read(this.fd, byteBuffer, -1L, nd);
                        if (i2 != -3) {
                            break;
                        }
                    } while (isOpen());
                    int iNormalize = IOStatus.normalize(i2);
                    this.readerThread = 0L;
                    end(i2 > 0 || i2 == -2);
                    if ($assertionsDisabled || IOStatus.check(i2)) {
                        return iNormalize;
                    }
                    throw new AssertionError();
                }
                this.readerThread = 0L;
                end(0 > 0 || 0 == -2);
                if ($assertionsDisabled || IOStatus.check(0)) {
                    return 0;
                }
                throw new AssertionError();
            } catch (Throwable th) {
                this.readerThread = 0L;
                end(i2 > 0 || i2 == -2);
                if ($assertionsDisabled || IOStatus.check(i2)) {
                    throw th;
                }
                throw new AssertionError();
            }
        }
    }

    @Override // java.nio.channels.DatagramChannel, java.nio.channels.ScatteringByteChannel
    public long read(ByteBuffer[] byteBufferArr, int i2, int i3) throws IOException {
        if (i2 < 0 || i3 < 0 || i2 > byteBufferArr.length - i3) {
            throw new IndexOutOfBoundsException();
        }
        synchronized (this.readLock) {
            synchronized (this.stateLock) {
                ensureOpen();
                if (!isConnected()) {
                    throw new NotYetConnectedException();
                }
            }
            long j2 = 0;
            try {
                begin();
                if (isOpen()) {
                    this.readerThread = NativeThread.current();
                    do {
                        j2 = IOUtil.read(this.fd, byteBufferArr, i2, i3, nd);
                        if (j2 != -3) {
                            break;
                        }
                    } while (isOpen());
                    long jNormalize = IOStatus.normalize(j2);
                    this.readerThread = 0L;
                    end(j2 > 0 || j2 == -2);
                    if ($assertionsDisabled || IOStatus.check(j2)) {
                        return jNormalize;
                    }
                    throw new AssertionError();
                }
                this.readerThread = 0L;
                end(0 > 0 || 0 == -2);
                if ($assertionsDisabled || IOStatus.check(0L)) {
                    return 0L;
                }
                throw new AssertionError();
            } catch (Throwable th) {
                this.readerThread = 0L;
                end(j2 > 0 || j2 == -2);
                if ($assertionsDisabled || IOStatus.check(j2)) {
                    throw th;
                }
                throw new AssertionError();
            }
        }
    }

    @Override // java.nio.channels.DatagramChannel, java.nio.channels.WritableByteChannel
    public int write(ByteBuffer byteBuffer) throws IOException {
        if (byteBuffer == null) {
            throw new NullPointerException();
        }
        synchronized (this.writeLock) {
            synchronized (this.stateLock) {
                ensureOpen();
                if (!isConnected()) {
                    throw new NotYetConnectedException();
                }
            }
            int iWrite = 0;
            try {
                begin();
                if (isOpen()) {
                    this.writerThread = NativeThread.current();
                    do {
                        iWrite = IOUtil.write(this.fd, byteBuffer, -1L, nd);
                        if (iWrite != -3) {
                            break;
                        }
                    } while (isOpen());
                    int iNormalize = IOStatus.normalize(iWrite);
                    this.writerThread = 0L;
                    end(iWrite > 0 || iWrite == -2);
                    if ($assertionsDisabled || IOStatus.check(iWrite)) {
                        return iNormalize;
                    }
                    throw new AssertionError();
                }
                this.writerThread = 0L;
                end(0 > 0 || 0 == -2);
                if ($assertionsDisabled || IOStatus.check(0)) {
                    return 0;
                }
                throw new AssertionError();
            } catch (Throwable th) {
                this.writerThread = 0L;
                end(iWrite > 0 || iWrite == -2);
                if ($assertionsDisabled || IOStatus.check(iWrite)) {
                    throw th;
                }
                throw new AssertionError();
            }
        }
    }

    @Override // java.nio.channels.DatagramChannel, java.nio.channels.GatheringByteChannel
    public long write(ByteBuffer[] byteBufferArr, int i2, int i3) throws IOException {
        if (i2 < 0 || i3 < 0 || i2 > byteBufferArr.length - i3) {
            throw new IndexOutOfBoundsException();
        }
        synchronized (this.writeLock) {
            synchronized (this.stateLock) {
                ensureOpen();
                if (!isConnected()) {
                    throw new NotYetConnectedException();
                }
            }
            long jWrite = 0;
            try {
                begin();
                if (isOpen()) {
                    this.writerThread = NativeThread.current();
                    do {
                        jWrite = IOUtil.write(this.fd, byteBufferArr, i2, i3, nd);
                        if (jWrite != -3) {
                            break;
                        }
                    } while (isOpen());
                    long jNormalize = IOStatus.normalize(jWrite);
                    this.writerThread = 0L;
                    end(jWrite > 0 || jWrite == -2);
                    if ($assertionsDisabled || IOStatus.check(jWrite)) {
                        return jNormalize;
                    }
                    throw new AssertionError();
                }
                this.writerThread = 0L;
                end(0 > 0 || 0 == -2);
                if ($assertionsDisabled || IOStatus.check(0L)) {
                    return 0L;
                }
                throw new AssertionError();
            } catch (Throwable th) {
                this.writerThread = 0L;
                end(jWrite > 0 || jWrite == -2);
                if ($assertionsDisabled || IOStatus.check(jWrite)) {
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

    public SocketAddress localAddress() {
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

    @Override // java.nio.channels.DatagramChannel, java.nio.channels.NetworkChannel
    public DatagramChannel bind(SocketAddress socketAddress) throws IOException {
        InetSocketAddress inetSocketAddressCheckAddress;
        synchronized (this.readLock) {
            synchronized (this.writeLock) {
                synchronized (this.stateLock) {
                    ensureOpen();
                    if (this.localAddress != null) {
                        throw new AlreadyBoundException();
                    }
                    if (socketAddress == null) {
                        inetSocketAddressCheckAddress = this.family == StandardProtocolFamily.INET ? new InetSocketAddress(InetAddress.getByName("0.0.0.0"), 0) : new InetSocketAddress(0);
                    } else {
                        inetSocketAddressCheckAddress = Net.checkAddress(socketAddress);
                        if (this.family == StandardProtocolFamily.INET && !(inetSocketAddressCheckAddress.getAddress() instanceof Inet4Address)) {
                            throw new UnsupportedAddressTypeException();
                        }
                    }
                    SecurityManager securityManager = System.getSecurityManager();
                    if (securityManager != null) {
                        securityManager.checkListen(inetSocketAddressCheckAddress.getPort());
                    }
                    Net.bind(this.family, this.fd, inetSocketAddressCheckAddress.getAddress(), inetSocketAddressCheckAddress.getPort());
                    this.localAddress = Net.localAddress(this.fd);
                }
            }
        }
        return this;
    }

    @Override // java.nio.channels.DatagramChannel
    public boolean isConnected() {
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
            if (this.state != 0) {
                throw new IllegalStateException("Connect already invoked");
            }
        }
    }

    /* JADX WARN: Finally extract failed */
    @Override // java.nio.channels.DatagramChannel
    public DatagramChannel connect(SocketAddress socketAddress) throws IOException {
        synchronized (this.readLock) {
            synchronized (this.writeLock) {
                synchronized (this.stateLock) {
                    ensureOpenAndUnconnected();
                    InetSocketAddress inetSocketAddressCheckAddress = Net.checkAddress(socketAddress);
                    SecurityManager securityManager = System.getSecurityManager();
                    if (securityManager != null) {
                        securityManager.checkConnect(inetSocketAddressCheckAddress.getAddress().getHostAddress(), inetSocketAddressCheckAddress.getPort());
                    }
                    if (Net.connect(this.family, this.fd, inetSocketAddressCheckAddress.getAddress(), inetSocketAddressCheckAddress.getPort()) <= 0) {
                        throw new Error();
                    }
                    this.state = 1;
                    this.remoteAddress = inetSocketAddressCheckAddress;
                    this.sender = inetSocketAddressCheckAddress;
                    this.cachedSenderInetAddress = inetSocketAddressCheckAddress.getAddress();
                    this.cachedSenderPort = inetSocketAddressCheckAddress.getPort();
                    this.localAddress = Net.localAddress(this.fd);
                    synchronized (blockingLock()) {
                        boolean zIsBlocking = isBlocking();
                        try {
                            ByteBuffer byteBufferAllocate = ByteBuffer.allocate(1);
                            if (zIsBlocking) {
                                configureBlocking(false);
                            }
                            do {
                                byteBufferAllocate.clear();
                            } while (receive(byteBufferAllocate) != null);
                            if (zIsBlocking) {
                                configureBlocking(true);
                            }
                        } catch (Throwable th) {
                            if (zIsBlocking) {
                                configureBlocking(true);
                            }
                            throw th;
                        }
                    }
                }
            }
        }
        return this;
    }

    @Override // java.nio.channels.DatagramChannel
    public DatagramChannel disconnect() throws IOException {
        synchronized (this.readLock) {
            synchronized (this.writeLock) {
                synchronized (this.stateLock) {
                    if (!isConnected() || !isOpen()) {
                        return this;
                    }
                    InetSocketAddress inetSocketAddress = this.remoteAddress;
                    SecurityManager securityManager = System.getSecurityManager();
                    if (securityManager != null) {
                        securityManager.checkConnect(inetSocketAddress.getAddress().getHostAddress(), inetSocketAddress.getPort());
                    }
                    disconnect0(this.fd, this.family == StandardProtocolFamily.INET6);
                    this.remoteAddress = null;
                    this.state = 0;
                    this.localAddress = Net.localAddress(this.fd);
                    return this;
                }
            }
        }
    }

    private MembershipKey innerJoin(InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2) throws IOException {
        MembershipKeyImpl type4;
        if (!inetAddress.isMulticastAddress()) {
            throw new IllegalArgumentException("Group not a multicast address");
        }
        if (inetAddress instanceof Inet4Address) {
            if (this.family == StandardProtocolFamily.INET6 && !Net.canIPv6SocketJoinIPv4Group()) {
                throw new IllegalArgumentException("IPv6 socket cannot join IPv4 multicast group");
            }
        } else if (inetAddress instanceof Inet6Address) {
            if (this.family != StandardProtocolFamily.INET6) {
                throw new IllegalArgumentException("Only IPv6 sockets can join IPv6 multicast group");
            }
        } else {
            throw new IllegalArgumentException("Address type not supported");
        }
        if (inetAddress2 != null) {
            if (inetAddress2.isAnyLocalAddress()) {
                throw new IllegalArgumentException("Source address is a wildcard address");
            }
            if (inetAddress2.isMulticastAddress()) {
                throw new IllegalArgumentException("Source address is multicast address");
            }
            if (inetAddress2.getClass() != inetAddress.getClass()) {
                throw new IllegalArgumentException("Source address is different type to group");
            }
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkMulticast(inetAddress);
        }
        synchronized (this.stateLock) {
            if (!isOpen()) {
                throw new ClosedChannelException();
            }
            if (this.registry == null) {
                this.registry = new MembershipRegistry();
            } else {
                MembershipKey membershipKeyCheckMembership = this.registry.checkMembership(inetAddress, networkInterface, inetAddress2);
                if (membershipKeyCheckMembership != null) {
                    return membershipKeyCheckMembership;
                }
            }
            if (this.family == StandardProtocolFamily.INET6 && ((inetAddress instanceof Inet6Address) || Net.canJoin6WithIPv4Group())) {
                int index = networkInterface.getIndex();
                if (index == -1) {
                    throw new IOException("Network interface cannot be identified");
                }
                byte[] bArrInet6AsByteArray = Net.inet6AsByteArray(inetAddress);
                byte[] bArrInet6AsByteArray2 = inetAddress2 == null ? null : Net.inet6AsByteArray(inetAddress2);
                if (Net.join6(this.fd, bArrInet6AsByteArray, index, bArrInet6AsByteArray2) == -2) {
                    throw new UnsupportedOperationException();
                }
                type4 = new MembershipKeyImpl.Type6(this, inetAddress, networkInterface, inetAddress2, bArrInet6AsByteArray, index, bArrInet6AsByteArray2);
            } else {
                Inet4Address inet4AddressAnyInet4Address = Net.anyInet4Address(networkInterface);
                if (inet4AddressAnyInet4Address == null) {
                    throw new IOException("Network interface not configured for IPv4");
                }
                int iInet4AsInt = Net.inet4AsInt(inetAddress);
                int iInet4AsInt2 = Net.inet4AsInt(inet4AddressAnyInet4Address);
                int iInet4AsInt3 = inetAddress2 == null ? 0 : Net.inet4AsInt(inetAddress2);
                if (Net.join4(this.fd, iInet4AsInt, iInet4AsInt2, iInet4AsInt3) == -2) {
                    throw new UnsupportedOperationException();
                }
                type4 = new MembershipKeyImpl.Type4(this, inetAddress, networkInterface, inetAddress2, iInet4AsInt, iInet4AsInt2, iInet4AsInt3);
            }
            this.registry.add(type4);
            return type4;
        }
    }

    @Override // java.nio.channels.MulticastChannel
    public MembershipKey join(InetAddress inetAddress, NetworkInterface networkInterface) throws IOException {
        return innerJoin(inetAddress, networkInterface, null);
    }

    @Override // java.nio.channels.MulticastChannel
    public MembershipKey join(InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2) throws IOException {
        if (inetAddress2 == null) {
            throw new NullPointerException("source address is null");
        }
        return innerJoin(inetAddress, networkInterface, inetAddress2);
    }

    void drop(MembershipKeyImpl membershipKeyImpl) {
        if (!$assertionsDisabled && membershipKeyImpl.channel() != this) {
            throw new AssertionError();
        }
        synchronized (this.stateLock) {
            if (membershipKeyImpl.isValid()) {
                try {
                    if (membershipKeyImpl instanceof MembershipKeyImpl.Type6) {
                        MembershipKeyImpl.Type6 type6 = (MembershipKeyImpl.Type6) membershipKeyImpl;
                        Net.drop6(this.fd, type6.groupAddress(), type6.index(), type6.source());
                    } else {
                        MembershipKeyImpl.Type4 type4 = (MembershipKeyImpl.Type4) membershipKeyImpl;
                        Net.drop4(this.fd, type4.groupAddress(), type4.interfaceAddress(), type4.source());
                    }
                    membershipKeyImpl.invalidate();
                    this.registry.remove(membershipKeyImpl);
                } catch (IOException e2) {
                    throw new AssertionError(e2);
                }
            }
        }
    }

    void block(MembershipKeyImpl membershipKeyImpl, InetAddress inetAddress) throws IOException {
        int iBlock4;
        if (!$assertionsDisabled && membershipKeyImpl.channel() != this) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && membershipKeyImpl.sourceAddress() != null) {
            throw new AssertionError();
        }
        synchronized (this.stateLock) {
            if (!membershipKeyImpl.isValid()) {
                throw new IllegalStateException("key is no longer valid");
            }
            if (inetAddress.isAnyLocalAddress()) {
                throw new IllegalArgumentException("Source address is a wildcard address");
            }
            if (inetAddress.isMulticastAddress()) {
                throw new IllegalArgumentException("Source address is multicast address");
            }
            if (inetAddress.getClass() != membershipKeyImpl.group().getClass()) {
                throw new IllegalArgumentException("Source address is different type to group");
            }
            if (membershipKeyImpl instanceof MembershipKeyImpl.Type6) {
                MembershipKeyImpl.Type6 type6 = (MembershipKeyImpl.Type6) membershipKeyImpl;
                iBlock4 = Net.block6(this.fd, type6.groupAddress(), type6.index(), Net.inet6AsByteArray(inetAddress));
            } else {
                MembershipKeyImpl.Type4 type4 = (MembershipKeyImpl.Type4) membershipKeyImpl;
                iBlock4 = Net.block4(this.fd, type4.groupAddress(), type4.interfaceAddress(), Net.inet4AsInt(inetAddress));
            }
            if (iBlock4 == -2) {
                throw new UnsupportedOperationException();
            }
        }
    }

    void unblock(MembershipKeyImpl membershipKeyImpl, InetAddress inetAddress) {
        if (!$assertionsDisabled && membershipKeyImpl.channel() != this) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && membershipKeyImpl.sourceAddress() != null) {
            throw new AssertionError();
        }
        synchronized (this.stateLock) {
            if (!membershipKeyImpl.isValid()) {
                throw new IllegalStateException("key is no longer valid");
            }
            try {
                if (membershipKeyImpl instanceof MembershipKeyImpl.Type6) {
                    MembershipKeyImpl.Type6 type6 = (MembershipKeyImpl.Type6) membershipKeyImpl;
                    Net.unblock6(this.fd, type6.groupAddress(), type6.index(), Net.inet6AsByteArray(inetAddress));
                } else {
                    MembershipKeyImpl.Type4 type4 = (MembershipKeyImpl.Type4) membershipKeyImpl;
                    Net.unblock4(this.fd, type4.groupAddress(), type4.interfaceAddress(), Net.inet4AsInt(inetAddress));
                }
            } catch (IOException e2) {
                throw new AssertionError(e2);
            }
        }
    }

    @Override // java.nio.channels.spi.AbstractSelectableChannel
    protected void implCloseSelectableChannel() throws IOException {
        synchronized (this.stateLock) {
            if (this.state != 2) {
                nd.preClose(this.fd);
            }
            ResourceManager.afterUdpClose();
            if (this.registry != null) {
                this.registry.invalidateAll();
            }
            long j2 = this.readerThread;
            if (j2 != 0) {
                NativeThread.signal(j2);
            }
            long j3 = this.writerThread;
            if (j3 != 0) {
                NativeThread.signal(j3);
            }
            if (!isRegistered()) {
                kill();
            }
        }
    }

    @Override // sun.nio.ch.SelChImpl
    public void kill() throws IOException {
        synchronized (this.stateLock) {
            if (this.state == 2) {
                return;
            }
            if (this.state == -1) {
                this.state = 2;
            } else {
                if (!$assertionsDisabled && (isOpen() || isRegistered())) {
                    throw new AssertionError();
                }
                nd.close(this.fd);
                this.state = 2;
            }
        }
    }

    protected void finalize() throws IOException {
        if (this.fd != null) {
            close();
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
        if ((i2 & Net.POLLIN) != 0 && (iNioInterestOps & 1) != 0) {
            i4 |= 1;
        }
        if ((i2 & Net.POLLOUT) != 0 && (iNioInterestOps & 4) != 0) {
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
                    this.readerThread = 0L;
                    end(iPoll > 0);
                    return iPoll;
                }
            } finally {
                this.readerThread = 0L;
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
            i3 |= Net.POLLIN;
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
}
