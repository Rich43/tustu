package sun.nio.ch;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.net.StandardSocketOptions;
import java.nio.channels.AlreadyBoundException;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.CompletionHandler;
import java.nio.channels.NetworkChannel;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.swing.JInternalFrame;
import org.icepdf.core.util.PdfOps;
import sun.net.ExtendedOptionsHelper;
import sun.net.NetHooks;

/* loaded from: rt.jar:sun/nio/ch/AsynchronousServerSocketChannelImpl.class */
abstract class AsynchronousServerSocketChannelImpl extends AsynchronousServerSocketChannel implements Cancellable, Groupable {
    protected final FileDescriptor fd;
    protected volatile InetSocketAddress localAddress;
    private final Object stateLock;
    private ReadWriteLock closeLock;
    private volatile boolean open;
    private volatile boolean acceptKilled;
    private boolean isReuseAddress;

    abstract void implClose() throws IOException;

    abstract Future<AsynchronousSocketChannel> implAccept(Object obj, CompletionHandler<AsynchronousSocketChannel, Object> completionHandler);

    @Override // java.nio.channels.AsynchronousServerSocketChannel, java.nio.channels.NetworkChannel
    public /* bridge */ /* synthetic */ NetworkChannel setOption(SocketOption socketOption, Object obj) throws IOException {
        return setOption((SocketOption<SocketOption>) socketOption, (SocketOption) obj);
    }

    AsynchronousServerSocketChannelImpl(AsynchronousChannelGroupImpl asynchronousChannelGroupImpl) {
        super(asynchronousChannelGroupImpl.provider());
        this.localAddress = null;
        this.stateLock = new Object();
        this.closeLock = new ReentrantReadWriteLock();
        this.open = true;
        this.fd = Net.serverSocket(true);
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

    @Override // java.nio.channels.AsynchronousServerSocketChannel
    public final Future<AsynchronousSocketChannel> accept() {
        return implAccept(null, null);
    }

    @Override // java.nio.channels.AsynchronousServerSocketChannel
    public final <A> void accept(A a2, CompletionHandler<AsynchronousSocketChannel, ? super A> completionHandler) {
        if (completionHandler == null) {
            throw new NullPointerException("'handler' is null");
        }
        implAccept(a2, completionHandler);
    }

    final boolean isAcceptKilled() {
        return this.acceptKilled;
    }

    @Override // sun.nio.ch.Cancellable
    public final void onCancel(PendingFuture<?, ?> pendingFuture) {
        this.acceptKilled = true;
    }

    @Override // java.nio.channels.AsynchronousServerSocketChannel
    public final AsynchronousServerSocketChannel bind(SocketAddress socketAddress, int i2) throws IOException {
        InetSocketAddress inetSocketAddress = socketAddress == null ? new InetSocketAddress(0) : Net.checkAddress(socketAddress);
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkListen(inetSocketAddress.getPort());
        }
        try {
            begin();
            synchronized (this.stateLock) {
                if (this.localAddress != null) {
                    throw new AlreadyBoundException();
                }
                NetHooks.beforeTcpBind(this.fd, inetSocketAddress.getAddress(), inetSocketAddress.getPort());
                Net.bind(this.fd, inetSocketAddress.getAddress(), inetSocketAddress.getPort());
                Net.listen(this.fd, i2 < 1 ? 50 : i2);
                this.localAddress = Net.localAddress(this.fd);
            }
            return this;
        } finally {
            end();
        }
    }

    @Override // java.nio.channels.AsynchronousServerSocketChannel, java.nio.channels.NetworkChannel
    public final SocketAddress getLocalAddress() throws IOException {
        if (!isOpen()) {
            throw new ClosedChannelException();
        }
        return Net.getRevealedLocalAddress(this.localAddress);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.nio.channels.AsynchronousServerSocketChannel, java.nio.channels.NetworkChannel
    public final <T> AsynchronousServerSocketChannel setOption(SocketOption<T> socketOption, T t2) throws IOException {
        if (socketOption == null) {
            throw new NullPointerException();
        }
        if (!supportedOptions().contains(socketOption)) {
            throw new UnsupportedOperationException(PdfOps.SINGLE_QUOTE_TOKEN + ((Object) socketOption) + "' not supported");
        }
        try {
            begin();
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

    /* loaded from: rt.jar:sun/nio/ch/AsynchronousServerSocketChannelImpl$DefaultOptionsHolder.class */
    private static class DefaultOptionsHolder {
        static final Set<SocketOption<?>> defaultOptions = defaultOptions();

        private DefaultOptionsHolder() {
        }

        private static Set<SocketOption<?>> defaultOptions() {
            HashSet hashSet = new HashSet(2);
            hashSet.add(StandardSocketOptions.SO_RCVBUF);
            hashSet.add(StandardSocketOptions.SO_REUSEADDR);
            hashSet.addAll(ExtendedOptionsHelper.keepAliveOptions());
            return Collections.unmodifiableSet(hashSet);
        }
    }

    @Override // java.nio.channels.NetworkChannel
    public final Set<SocketOption<?>> supportedOptions() {
        return DefaultOptionsHolder.defaultOptions;
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append('[');
        if (!isOpen()) {
            sb.append(JInternalFrame.IS_CLOSED_PROPERTY);
        } else if (this.localAddress == null) {
            sb.append("unbound");
        } else {
            sb.append(Net.getRevealedLocalAddressAsString(this.localAddress));
        }
        sb.append(']');
        return sb.toString();
    }
}
