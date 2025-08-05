package sun.nio.ch;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AcceptPendingException;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.CompletionHandler;
import java.nio.channels.NotYetBoundException;
import java.nio.channels.ShutdownChannelGroupException;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import sun.misc.Unsafe;
import sun.nio.ch.Iocp;

/* loaded from: rt.jar:sun/nio/ch/WindowsAsynchronousServerSocketChannelImpl.class */
class WindowsAsynchronousServerSocketChannelImpl extends AsynchronousServerSocketChannelImpl implements Iocp.OverlappedChannel {
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final int DATA_BUFFER_SIZE = 88;
    private final long handle;
    private final int completionKey;
    private final Iocp iocp;
    private final PendingIoCache ioCache;
    private final long dataBuffer;
    private AtomicBoolean accepting;

    private static native void initIDs();

    /* JADX INFO: Access modifiers changed from: private */
    public static native int accept0(long j2, long j3, long j4, long j5) throws IOException;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void updateAcceptContext(long j2, long j3) throws IOException;

    private static native void closesocket0(long j2) throws IOException;

    static {
        IOUtil.load();
        initIDs();
    }

    WindowsAsynchronousServerSocketChannelImpl(Iocp iocp) throws IOException {
        super(iocp);
        this.accepting = new AtomicBoolean();
        long jFdVal = IOUtil.fdVal(this.fd);
        try {
            int iAssociate = iocp.associate(this, jFdVal);
            this.handle = jFdVal;
            this.completionKey = iAssociate;
            this.iocp = iocp;
            this.ioCache = new PendingIoCache();
            this.dataBuffer = unsafe.allocateMemory(88L);
        } catch (IOException e2) {
            closesocket0(jFdVal);
            throw e2;
        }
    }

    @Override // sun.nio.ch.Iocp.OverlappedChannel
    public <V, A> PendingFuture<V, A> getByOverlapped(long j2) {
        return this.ioCache.remove(j2);
    }

    @Override // sun.nio.ch.AsynchronousServerSocketChannelImpl
    void implClose() throws IOException {
        closesocket0(this.handle);
        this.ioCache.close();
        this.iocp.disassociate(this.completionKey);
        unsafe.freeMemory(this.dataBuffer);
    }

    @Override // sun.nio.ch.Groupable
    public AsynchronousChannelGroupImpl group() {
        return this.iocp;
    }

    /* loaded from: rt.jar:sun/nio/ch/WindowsAsynchronousServerSocketChannelImpl$AcceptTask.class */
    private class AcceptTask implements Runnable, Iocp.ResultHandler {
        private final WindowsAsynchronousSocketChannelImpl channel;
        private final AccessControlContext acc;
        private final PendingFuture<AsynchronousSocketChannel, Object> result;

        AcceptTask(WindowsAsynchronousSocketChannelImpl windowsAsynchronousSocketChannelImpl, AccessControlContext accessControlContext, PendingFuture<AsynchronousSocketChannel, Object> pendingFuture) {
            this.channel = windowsAsynchronousSocketChannelImpl;
            this.acc = accessControlContext;
            this.result = pendingFuture;
        }

        void enableAccept() {
            WindowsAsynchronousServerSocketChannelImpl.this.accepting.set(false);
        }

        void closeChildChannel() {
            try {
                this.channel.close();
            } catch (IOException e2) {
            }
        }

        void finishAccept() throws IOException {
            WindowsAsynchronousServerSocketChannelImpl.updateAcceptContext(WindowsAsynchronousServerSocketChannelImpl.this.handle, this.channel.handle());
            InetSocketAddress inetSocketAddressLocalAddress = Net.localAddress(this.channel.fd);
            final InetSocketAddress inetSocketAddressRemoteAddress = Net.remoteAddress(this.channel.fd);
            this.channel.setConnected(inetSocketAddressLocalAddress, inetSocketAddressRemoteAddress);
            if (this.acc != null) {
                AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.nio.ch.WindowsAsynchronousServerSocketChannelImpl.AcceptTask.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public Void run2() {
                        System.getSecurityManager().checkAccept(inetSocketAddressRemoteAddress.getAddress().getHostAddress(), inetSocketAddressRemoteAddress.getPort());
                        return null;
                    }
                }, this.acc);
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                try {
                    WindowsAsynchronousServerSocketChannelImpl.this.begin();
                } catch (Throwable th) {
                    th = th;
                    if (0 != 0) {
                        WindowsAsynchronousServerSocketChannelImpl.this.ioCache.remove(0L);
                    }
                    closeChildChannel();
                    if (th instanceof ClosedChannelException) {
                        th = new AsynchronousCloseException();
                    }
                    if (!(th instanceof IOException) && !(th instanceof SecurityException)) {
                        th = new IOException(th);
                    }
                    enableAccept();
                    this.result.setFailure(th);
                    WindowsAsynchronousServerSocketChannelImpl.this.end();
                }
                try {
                    this.channel.begin();
                    synchronized (this.result) {
                        if (WindowsAsynchronousServerSocketChannelImpl.accept0(WindowsAsynchronousServerSocketChannelImpl.this.handle, this.channel.handle(), WindowsAsynchronousServerSocketChannelImpl.this.ioCache.add(this.result), WindowsAsynchronousServerSocketChannelImpl.this.dataBuffer) == -2) {
                            WindowsAsynchronousServerSocketChannelImpl.this.end();
                            return;
                        }
                        finishAccept();
                        enableAccept();
                        this.result.setResult(this.channel);
                        this.channel.end();
                        WindowsAsynchronousServerSocketChannelImpl.this.end();
                        if (this.result.isCancelled()) {
                            closeChildChannel();
                        }
                        Invoker.invokeIndirectly(this.result);
                    }
                } finally {
                    this.channel.end();
                }
            } catch (Throwable th2) {
                WindowsAsynchronousServerSocketChannelImpl.this.end();
                throw th2;
            }
        }

        /* JADX WARN: Finally extract failed */
        @Override // sun.nio.ch.Iocp.ResultHandler
        public void completed(int i2, boolean z2) {
            try {
            } catch (Throwable th) {
                th = th;
                enableAccept();
                closeChildChannel();
                if (th instanceof ClosedChannelException) {
                    th = new AsynchronousCloseException();
                }
                if (!(th instanceof IOException) && !(th instanceof SecurityException)) {
                    th = new IOException(th);
                }
                this.result.setFailure(th);
            }
            if (WindowsAsynchronousServerSocketChannelImpl.this.iocp.isShutdown()) {
                throw new IOException(new ShutdownChannelGroupException());
            }
            try {
                WindowsAsynchronousServerSocketChannelImpl.this.begin();
                try {
                    this.channel.begin();
                    finishAccept();
                    this.channel.end();
                    WindowsAsynchronousServerSocketChannelImpl.this.end();
                    enableAccept();
                    this.result.setResult(this.channel);
                    if (this.result.isCancelled()) {
                        closeChildChannel();
                    }
                    Invoker.invokeIndirectly(this.result);
                } catch (Throwable th2) {
                    this.channel.end();
                    throw th2;
                }
            } catch (Throwable th3) {
                WindowsAsynchronousServerSocketChannelImpl.this.end();
                throw th3;
            }
        }

        @Override // sun.nio.ch.Iocp.ResultHandler
        public void failed(int i2, IOException iOException) {
            enableAccept();
            closeChildChannel();
            if (WindowsAsynchronousServerSocketChannelImpl.this.isOpen()) {
                this.result.setFailure(iOException);
            } else {
                this.result.setFailure(new AsynchronousCloseException());
            }
            Invoker.invokeIndirectly(this.result);
        }
    }

    @Override // sun.nio.ch.AsynchronousServerSocketChannelImpl
    Future<AsynchronousSocketChannel> implAccept(Object obj, CompletionHandler<AsynchronousSocketChannel, Object> completionHandler) {
        if (!isOpen()) {
            ClosedChannelException closedChannelException = new ClosedChannelException();
            if (completionHandler == null) {
                return CompletedFuture.withFailure(closedChannelException);
            }
            Invoker.invokeIndirectly(this, completionHandler, obj, (Object) null, closedChannelException);
            return null;
        }
        if (isAcceptKilled()) {
            throw new RuntimeException("Accept not allowed due to cancellation");
        }
        if (this.localAddress == null) {
            throw new NotYetBoundException();
        }
        WindowsAsynchronousSocketChannelImpl windowsAsynchronousSocketChannelImpl = null;
        IOException iOException = null;
        try {
            begin();
            windowsAsynchronousSocketChannelImpl = new WindowsAsynchronousSocketChannelImpl(this.iocp, false);
            end();
        } catch (IOException e2) {
            iOException = e2;
            end();
        } catch (Throwable th) {
            end();
            throw th;
        }
        if (iOException != null) {
            if (completionHandler == null) {
                return CompletedFuture.withFailure(iOException);
            }
            Invoker.invokeIndirectly(this, completionHandler, obj, (Object) null, iOException);
            return null;
        }
        AccessControlContext context = System.getSecurityManager() == null ? null : AccessController.getContext();
        PendingFuture pendingFuture = new PendingFuture(this, completionHandler, obj);
        AcceptTask acceptTask = new AcceptTask(windowsAsynchronousSocketChannelImpl, context, pendingFuture);
        pendingFuture.setContext(acceptTask);
        if (!this.accepting.compareAndSet(false, true)) {
            throw new AcceptPendingException();
        }
        if (Iocp.supportsThreadAgnosticIo()) {
            acceptTask.run();
        } else {
            Invoker.invokeOnThreadInThreadPool(this, acceptTask);
        }
        return pendingFuture;
    }
}
