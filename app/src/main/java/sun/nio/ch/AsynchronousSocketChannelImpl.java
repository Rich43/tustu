package sun.nio.ch;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AlreadyBoundException;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.CompletionHandler;
import java.nio.channels.ConnectionPendingException;
import java.nio.channels.NetworkChannel;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.ReadPendingException;
import java.nio.channels.WritePendingException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.swing.JInternalFrame;
import jdk.net.ExtendedSocketOptions;
import org.icepdf.core.util.PdfOps;
import sun.net.ExtendedOptionsHelper;
import sun.net.ExtendedOptionsImpl;
import sun.net.NetHooks;

/* loaded from: rt.jar:sun/nio/ch/AsynchronousSocketChannelImpl.class */
abstract class AsynchronousSocketChannelImpl extends AsynchronousSocketChannel implements Cancellable, Groupable {
    protected final FileDescriptor fd;
    protected final Object stateLock;
    protected volatile InetSocketAddress localAddress;
    protected volatile InetSocketAddress remoteAddress;
    static final int ST_UNINITIALIZED = -1;
    static final int ST_UNCONNECTED = 0;
    static final int ST_PENDING = 1;
    static final int ST_CONNECTED = 2;
    protected volatile int state;
    private final Object readLock;
    private boolean reading;
    private boolean readShutdown;
    private boolean readKilled;
    private final Object writeLock;
    private boolean writing;
    private boolean writeShutdown;
    private boolean writeKilled;
    private final ReadWriteLock closeLock;
    private volatile boolean open;
    private boolean isReuseAddress;

    abstract void implClose() throws IOException;

    abstract <A> Future<Void> implConnect(SocketAddress socketAddress, A a2, CompletionHandler<Void, ? super A> completionHandler);

    abstract <V extends Number, A> Future<V> implRead(boolean z2, ByteBuffer byteBuffer, ByteBuffer[] byteBufferArr, long j2, TimeUnit timeUnit, A a2, CompletionHandler<V, ? super A> completionHandler);

    abstract <V extends Number, A> Future<V> implWrite(boolean z2, ByteBuffer byteBuffer, ByteBuffer[] byteBufferArr, long j2, TimeUnit timeUnit, A a2, CompletionHandler<V, ? super A> completionHandler);

    @Override // java.nio.channels.AsynchronousSocketChannel, java.nio.channels.NetworkChannel
    public /* bridge */ /* synthetic */ NetworkChannel setOption(SocketOption socketOption, Object obj) throws IOException {
        return setOption((SocketOption<SocketOption>) socketOption, (SocketOption) obj);
    }

    AsynchronousSocketChannelImpl(AsynchronousChannelGroupImpl asynchronousChannelGroupImpl) throws IOException {
        super(asynchronousChannelGroupImpl.provider());
        this.stateLock = new Object();
        this.localAddress = null;
        this.remoteAddress = null;
        this.state = -1;
        this.readLock = new Object();
        this.writeLock = new Object();
        this.closeLock = new ReentrantReadWriteLock();
        this.open = true;
        this.fd = Net.socket(true);
        this.state = 0;
    }

    AsynchronousSocketChannelImpl(AsynchronousChannelGroupImpl asynchronousChannelGroupImpl, FileDescriptor fileDescriptor, InetSocketAddress inetSocketAddress) throws IOException {
        super(asynchronousChannelGroupImpl.provider());
        this.stateLock = new Object();
        this.localAddress = null;
        this.remoteAddress = null;
        this.state = -1;
        this.readLock = new Object();
        this.writeLock = new Object();
        this.closeLock = new ReentrantReadWriteLock();
        this.open = true;
        this.fd = fileDescriptor;
        this.state = 2;
        this.localAddress = Net.localAddress(fileDescriptor);
        this.remoteAddress = inetSocketAddress;
    }

    @Override // java.nio.channels.Channel
    public final boolean isOpen() {
        return this.open;
    }

    final void begin() throws IOException {
        this.closeLock.readLock().lock();
        if (!isOpen()) {
            throw new ClosedChannelException();
        }
    }

    final void end() {
        this.closeLock.readLock().unlock();
    }

    @Override // java.nio.channels.AsynchronousChannel, java.nio.channels.Channel, java.io.Closeable, java.lang.AutoCloseable
    public final void close() throws IOException {
        this.closeLock.writeLock().lock();
        try {
            if (!this.open) {
                return;
            }
            this.open = false;
            implClose();
        } finally {
            this.closeLock.writeLock().unlock();
        }
    }

    final void enableReading(boolean z2) {
        synchronized (this.readLock) {
            this.reading = false;
            if (z2) {
                this.readKilled = true;
            }
        }
    }

    final void enableReading() {
        enableReading(false);
    }

    final void enableWriting(boolean z2) {
        synchronized (this.writeLock) {
            this.writing = false;
            if (z2) {
                this.writeKilled = true;
            }
        }
    }

    final void enableWriting() {
        enableWriting(false);
    }

    final void killReading() {
        synchronized (this.readLock) {
            this.readKilled = true;
        }
    }

    final void killWriting() {
        synchronized (this.writeLock) {
            this.writeKilled = true;
        }
    }

    final void killConnect() {
        killReading();
        killWriting();
    }

    @Override // java.nio.channels.AsynchronousSocketChannel
    public final Future<Void> connect(SocketAddress socketAddress) {
        return implConnect(socketAddress, null, null);
    }

    @Override // java.nio.channels.AsynchronousSocketChannel
    public final <A> void connect(SocketAddress socketAddress, A a2, CompletionHandler<Void, ? super A> completionHandler) {
        if (completionHandler == null) {
            throw new NullPointerException("'handler' is null");
        }
        implConnect(socketAddress, a2, completionHandler);
    }

    private <V extends Number, A> Future<V> read(boolean z2, ByteBuffer byteBuffer, ByteBuffer[] byteBufferArr, long j2, TimeUnit timeUnit, A a2, CompletionHandler<V, ? super A> completionHandler) {
        Object objValueOf;
        if (!isOpen()) {
            ClosedChannelException closedChannelException = new ClosedChannelException();
            if (completionHandler == null) {
                return CompletedFuture.withFailure(closedChannelException);
            }
            Invoker.invoke(this, completionHandler, a2, null, closedChannelException);
            return null;
        }
        if (this.remoteAddress == null) {
            throw new NotYetConnectedException();
        }
        boolean z3 = z2 || byteBuffer.hasRemaining();
        boolean z4 = false;
        synchronized (this.readLock) {
            if (this.readKilled) {
                throw new IllegalStateException("Reading not allowed due to timeout or cancellation");
            }
            if (this.reading) {
                throw new ReadPendingException();
            }
            if (this.readShutdown) {
                z4 = true;
            } else if (z3) {
                this.reading = true;
            }
        }
        if (z4 || !z3) {
            if (z2) {
                objValueOf = z4 ? -1L : 0L;
            } else {
                objValueOf = Integer.valueOf(z4 ? -1 : 0);
            }
            if (completionHandler == null) {
                return CompletedFuture.withResult(objValueOf);
            }
            Invoker.invoke(this, completionHandler, a2, objValueOf, null);
            return null;
        }
        return implRead(z2, byteBuffer, byteBufferArr, j2, timeUnit, a2, completionHandler);
    }

    @Override // java.nio.channels.AsynchronousSocketChannel, java.nio.channels.AsynchronousByteChannel
    public final Future<Integer> read(ByteBuffer byteBuffer) {
        if (byteBuffer.isReadOnly()) {
            throw new IllegalArgumentException("Read-only buffer");
        }
        return read(false, byteBuffer, (ByteBuffer[]) null, 0L, TimeUnit.MILLISECONDS, (TimeUnit) null, (CompletionHandler<V, ? super TimeUnit>) null);
    }

    @Override // java.nio.channels.AsynchronousSocketChannel
    public final <A> void read(ByteBuffer byteBuffer, long j2, TimeUnit timeUnit, A a2, CompletionHandler<Integer, ? super A> completionHandler) {
        if (completionHandler == null) {
            throw new NullPointerException("'handler' is null");
        }
        if (byteBuffer.isReadOnly()) {
            throw new IllegalArgumentException("Read-only buffer");
        }
        read(false, byteBuffer, (ByteBuffer[]) null, j2, timeUnit, (TimeUnit) a2, (CompletionHandler<V, ? super TimeUnit>) completionHandler);
    }

    @Override // java.nio.channels.AsynchronousSocketChannel
    public final <A> void read(ByteBuffer[] byteBufferArr, int i2, int i3, long j2, TimeUnit timeUnit, A a2, CompletionHandler<Long, ? super A> completionHandler) {
        if (completionHandler == null) {
            throw new NullPointerException("'handler' is null");
        }
        if (i2 < 0 || i3 < 0 || i2 > byteBufferArr.length - i3) {
            throw new IndexOutOfBoundsException();
        }
        ByteBuffer[] byteBufferArrSubsequence = Util.subsequence(byteBufferArr, i2, i3);
        for (ByteBuffer byteBuffer : byteBufferArrSubsequence) {
            if (byteBuffer.isReadOnly()) {
                throw new IllegalArgumentException("Read-only buffer");
            }
        }
        read(true, (ByteBuffer) null, byteBufferArrSubsequence, j2, timeUnit, (TimeUnit) a2, (CompletionHandler<V, ? super TimeUnit>) completionHandler);
    }

    private <V extends Number, A> Future<V> write(boolean z2, ByteBuffer byteBuffer, ByteBuffer[] byteBufferArr, long j2, TimeUnit timeUnit, A a2, CompletionHandler<V, ? super A> completionHandler) {
        boolean z3 = z2 || byteBuffer.hasRemaining();
        boolean z4 = false;
        if (isOpen()) {
            if (this.remoteAddress == null) {
                throw new NotYetConnectedException();
            }
            synchronized (this.writeLock) {
                if (this.writeKilled) {
                    throw new IllegalStateException("Writing not allowed due to timeout or cancellation");
                }
                if (this.writing) {
                    throw new WritePendingException();
                }
                if (this.writeShutdown) {
                    z4 = true;
                } else if (z3) {
                    this.writing = true;
                }
            }
        } else {
            z4 = true;
        }
        if (z4) {
            ClosedChannelException closedChannelException = new ClosedChannelException();
            if (completionHandler == null) {
                return CompletedFuture.withFailure(closedChannelException);
            }
            Invoker.invoke(this, completionHandler, a2, null, closedChannelException);
            return null;
        }
        if (!z3) {
            Object obj = z2 ? 0L : 0;
            if (completionHandler == null) {
                return CompletedFuture.withResult(obj);
            }
            Invoker.invoke(this, completionHandler, a2, obj, null);
            return null;
        }
        return implWrite(z2, byteBuffer, byteBufferArr, j2, timeUnit, a2, completionHandler);
    }

    @Override // java.nio.channels.AsynchronousSocketChannel, java.nio.channels.AsynchronousByteChannel
    public final Future<Integer> write(ByteBuffer byteBuffer) {
        return write(false, byteBuffer, (ByteBuffer[]) null, 0L, TimeUnit.MILLISECONDS, (TimeUnit) null, (CompletionHandler<V, ? super TimeUnit>) null);
    }

    @Override // java.nio.channels.AsynchronousSocketChannel
    public final <A> void write(ByteBuffer byteBuffer, long j2, TimeUnit timeUnit, A a2, CompletionHandler<Integer, ? super A> completionHandler) {
        if (completionHandler == null) {
            throw new NullPointerException("'handler' is null");
        }
        write(false, byteBuffer, (ByteBuffer[]) null, j2, timeUnit, (TimeUnit) a2, (CompletionHandler<V, ? super TimeUnit>) completionHandler);
    }

    @Override // java.nio.channels.AsynchronousSocketChannel
    public final <A> void write(ByteBuffer[] byteBufferArr, int i2, int i3, long j2, TimeUnit timeUnit, A a2, CompletionHandler<Long, ? super A> completionHandler) {
        if (completionHandler == null) {
            throw new NullPointerException("'handler' is null");
        }
        if (i2 < 0 || i3 < 0 || i2 > byteBufferArr.length - i3) {
            throw new IndexOutOfBoundsException();
        }
        write(true, (ByteBuffer) null, Util.subsequence(byteBufferArr, i2, i3), j2, timeUnit, (TimeUnit) a2, (CompletionHandler<V, ? super TimeUnit>) completionHandler);
    }

    @Override // java.nio.channels.AsynchronousSocketChannel, java.nio.channels.NetworkChannel
    public final AsynchronousSocketChannel bind(SocketAddress socketAddress) throws IOException {
        try {
            begin();
            synchronized (this.stateLock) {
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
            return this;
        } finally {
            end();
        }
    }

    @Override // java.nio.channels.AsynchronousSocketChannel, java.nio.channels.NetworkChannel
    public final SocketAddress getLocalAddress() throws IOException {
        if (!isOpen()) {
            throw new ClosedChannelException();
        }
        return Net.getRevealedLocalAddress(this.localAddress);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.nio.channels.AsynchronousSocketChannel, java.nio.channels.NetworkChannel
    public final <T> AsynchronousSocketChannel setOption(SocketOption<T> socketOption, T t2) throws IOException {
        if (socketOption == null) {
            throw new NullPointerException();
        }
        if (!supportedOptions().contains(socketOption)) {
            throw new UnsupportedOperationException(PdfOps.SINGLE_QUOTE_TOKEN + ((Object) socketOption) + "' not supported");
        }
        try {
            begin();
            if (this.writeShutdown) {
                throw new IOException("Connection has been shutdown for writing");
            }
            if (socketOption == StandardSocketOptions.SO_REUSEADDR && Net.useExclusiveBind()) {
                this.isReuseAddress = ((Boolean) t2).booleanValue();
            } else {
                Net.setSocketOption(this.fd, Net.UNSPEC, socketOption, t2);
            }
            return this;
        } finally {
            end();
        }
    }

    @Override // java.nio.channels.NetworkChannel
    public final <T> T getOption(SocketOption<T> socketOption) throws IOException {
        if (socketOption == null) {
            throw new NullPointerException();
        }
        if (!supportedOptions().contains(socketOption)) {
            throw new UnsupportedOperationException(PdfOps.SINGLE_QUOTE_TOKEN + ((Object) socketOption) + "' not supported");
        }
        try {
            begin();
            if (socketOption == StandardSocketOptions.SO_REUSEADDR && Net.useExclusiveBind()) {
                return (T) Boolean.valueOf(this.isReuseAddress);
            }
            return (T) Net.getSocketOption(this.fd, Net.UNSPEC, socketOption);
        } finally {
            end();
        }
    }

    /* loaded from: rt.jar:sun/nio/ch/AsynchronousSocketChannelImpl$DefaultOptionsHolder.class */
    private static class DefaultOptionsHolder {
        static final Set<SocketOption<?>> defaultOptions = defaultOptions();

        private DefaultOptionsHolder() {
        }

        private static Set<SocketOption<?>> defaultOptions() {
            HashSet hashSet = new HashSet(5);
            hashSet.add(StandardSocketOptions.SO_SNDBUF);
            hashSet.add(StandardSocketOptions.SO_RCVBUF);
            hashSet.add(StandardSocketOptions.SO_KEEPALIVE);
            hashSet.add(StandardSocketOptions.SO_REUSEADDR);
            hashSet.add(StandardSocketOptions.TCP_NODELAY);
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

    @Override // java.nio.channels.AsynchronousSocketChannel
    public final SocketAddress getRemoteAddress() throws IOException {
        if (!isOpen()) {
            throw new ClosedChannelException();
        }
        return this.remoteAddress;
    }

    @Override // java.nio.channels.AsynchronousSocketChannel
    public final AsynchronousSocketChannel shutdownInput() throws IOException {
        try {
            begin();
            if (this.remoteAddress == null) {
                throw new NotYetConnectedException();
            }
            synchronized (this.readLock) {
                if (!this.readShutdown) {
                    Net.shutdown(this.fd, 0);
                    this.readShutdown = true;
                }
            }
            return this;
        } finally {
            end();
        }
    }

    @Override // java.nio.channels.AsynchronousSocketChannel
    public final AsynchronousSocketChannel shutdownOutput() throws IOException {
        try {
            begin();
            if (this.remoteAddress == null) {
                throw new NotYetConnectedException();
            }
            synchronized (this.writeLock) {
                if (!this.writeShutdown) {
                    Net.shutdown(this.fd, 1);
                    this.writeShutdown = true;
                }
            }
            return this;
        } finally {
            end();
        }
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append('[');
        synchronized (this.stateLock) {
            if (!isOpen()) {
                sb.append(JInternalFrame.IS_CLOSED_PROPERTY);
            } else {
                switch (this.state) {
                    case 0:
                        sb.append("unconnected");
                        break;
                    case 1:
                        sb.append("connection-pending");
                        break;
                    case 2:
                        sb.append("connected");
                        if (this.readShutdown) {
                            sb.append(" ishut");
                        }
                        if (this.writeShutdown) {
                            sb.append(" oshut");
                            break;
                        }
                        break;
                }
                if (this.localAddress != null) {
                    sb.append(" local=");
                    sb.append(Net.getRevealedLocalAddressAsString(this.localAddress));
                }
                if (this.remoteAddress != null) {
                    sb.append(" remote=");
                    sb.append(this.remoteAddress.toString());
                }
            }
        }
        sb.append(']');
        return sb.toString();
    }
}
