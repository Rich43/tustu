package sun.nio.ch;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.DirectByteBufferR;
import java.nio.channels.AlreadyConnectedException;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.CompletionHandler;
import java.nio.channels.ConnectionPendingException;
import java.nio.channels.InterruptedByTimeoutException;
import java.nio.channels.ShutdownChannelGroupException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import sun.misc.Unsafe;
import sun.nio.ch.Iocp;

/* loaded from: rt.jar:sun/nio/ch/WindowsAsynchronousSocketChannelImpl.class */
class WindowsAsynchronousSocketChannelImpl extends AsynchronousSocketChannelImpl implements Iocp.OverlappedChannel {
    private static final int OFFSETOF_LEN = 0;
    private static final int MAX_WSABUF = 16;
    final long handle;
    private final Iocp iocp;
    private final int completionKey;
    private final PendingIoCache ioCache;
    private final long readBufferArray;
    private final long writeBufferArray;
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static int addressSize = unsafe.addressSize();
    private static final int SIZEOF_WSABUF = dependsArch(8, 16);
    private static final int OFFSETOF_BUF = dependsArch(4, 8);
    private static final int SIZEOF_WSABUFARRAY = 16 * SIZEOF_WSABUF;

    private static native void initIDs();

    /* JADX INFO: Access modifiers changed from: private */
    public static native int connect0(long j2, boolean z2, InetAddress inetAddress, int i2, long j3) throws IOException;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void updateConnectContext(long j2) throws IOException;

    /* JADX INFO: Access modifiers changed from: private */
    public static native int read0(long j2, int i2, long j3, long j4) throws IOException;

    /* JADX INFO: Access modifiers changed from: private */
    public static native int write0(long j2, int i2, long j3, long j4) throws IOException;

    private static native void shutdown0(long j2, int i2) throws IOException;

    private static native void closesocket0(long j2) throws IOException;

    static {
        IOUtil.load();
        initIDs();
    }

    private static int dependsArch(int i2, int i3) {
        return addressSize == 4 ? i2 : i3;
    }

    WindowsAsynchronousSocketChannelImpl(Iocp iocp, boolean z2) throws IOException {
        super(iocp);
        long jFdVal = IOUtil.fdVal(this.fd);
        int iAssociate = 0;
        try {
            iAssociate = iocp.associate(this, jFdVal);
        } catch (IOException e2) {
            closesocket0(jFdVal);
            throw e2;
        } catch (ShutdownChannelGroupException e3) {
            if (z2) {
                closesocket0(jFdVal);
                throw e3;
            }
        }
        this.handle = jFdVal;
        this.iocp = iocp;
        this.completionKey = iAssociate;
        this.ioCache = new PendingIoCache();
        this.readBufferArray = unsafe.allocateMemory(SIZEOF_WSABUFARRAY);
        this.writeBufferArray = unsafe.allocateMemory(SIZEOF_WSABUFARRAY);
    }

    WindowsAsynchronousSocketChannelImpl(Iocp iocp) throws IOException {
        this(iocp, true);
    }

    @Override // sun.nio.ch.Groupable
    public AsynchronousChannelGroupImpl group() {
        return this.iocp;
    }

    @Override // sun.nio.ch.Iocp.OverlappedChannel
    public <V, A> PendingFuture<V, A> getByOverlapped(long j2) {
        return this.ioCache.remove(j2);
    }

    long handle() {
        return this.handle;
    }

    void setConnected(InetSocketAddress inetSocketAddress, InetSocketAddress inetSocketAddress2) {
        synchronized (this.stateLock) {
            this.state = 2;
            this.localAddress = inetSocketAddress;
            this.remoteAddress = inetSocketAddress2;
        }
    }

    @Override // sun.nio.ch.AsynchronousSocketChannelImpl
    void implClose() throws IOException {
        closesocket0(this.handle);
        this.ioCache.close();
        unsafe.freeMemory(this.readBufferArray);
        unsafe.freeMemory(this.writeBufferArray);
        if (this.completionKey != 0) {
            this.iocp.disassociate(this.completionKey);
        }
    }

    @Override // sun.nio.ch.Cancellable
    public void onCancel(PendingFuture<?, ?> pendingFuture) {
        if (pendingFuture.getContext() instanceof ConnectTask) {
            killConnect();
        }
        if (pendingFuture.getContext() instanceof ReadTask) {
            killReading();
        }
        if (pendingFuture.getContext() instanceof WriteTask) {
            killWriting();
        }
    }

    /* loaded from: rt.jar:sun/nio/ch/WindowsAsynchronousSocketChannelImpl$ConnectTask.class */
    private class ConnectTask<A> implements Runnable, Iocp.ResultHandler {
        private final InetSocketAddress remote;
        private final PendingFuture<Void, A> result;

        ConnectTask(InetSocketAddress inetSocketAddress, PendingFuture<Void, A> pendingFuture) {
            this.remote = inetSocketAddress;
            this.result = pendingFuture;
        }

        private void closeChannel() {
            try {
                WindowsAsynchronousSocketChannelImpl.this.close();
            } catch (IOException e2) {
            }
        }

        private IOException toIOException(Throwable th) {
            if (th instanceof IOException) {
                if (th instanceof ClosedChannelException) {
                    th = new AsynchronousCloseException();
                }
                return (IOException) th;
            }
            return new IOException(th);
        }

        private void afterConnect() throws IOException {
            WindowsAsynchronousSocketChannelImpl.updateConnectContext(WindowsAsynchronousSocketChannelImpl.this.handle);
            synchronized (WindowsAsynchronousSocketChannelImpl.this.stateLock) {
                WindowsAsynchronousSocketChannelImpl.this.state = 2;
                WindowsAsynchronousSocketChannelImpl.this.remoteAddress = this.remote;
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            Throwable th = null;
            try {
                try {
                    WindowsAsynchronousSocketChannelImpl.this.begin();
                } catch (Throwable th2) {
                    if (0 != 0) {
                        WindowsAsynchronousSocketChannelImpl.this.ioCache.remove(0L);
                    }
                    th = th2;
                    WindowsAsynchronousSocketChannelImpl.this.end();
                }
                synchronized (this.result) {
                    if (WindowsAsynchronousSocketChannelImpl.connect0(WindowsAsynchronousSocketChannelImpl.this.handle, Net.isIPv6Available(), this.remote.getAddress(), this.remote.getPort(), WindowsAsynchronousSocketChannelImpl.this.ioCache.add(this.result)) == -2) {
                        WindowsAsynchronousSocketChannelImpl.this.end();
                        return;
                    }
                    afterConnect();
                    this.result.setResult(null);
                    WindowsAsynchronousSocketChannelImpl.this.end();
                    if (th != null) {
                        closeChannel();
                        this.result.setFailure(toIOException(th));
                    }
                    Invoker.invoke(this.result);
                }
            } catch (Throwable th3) {
                WindowsAsynchronousSocketChannelImpl.this.end();
                throw th3;
            }
        }

        @Override // sun.nio.ch.Iocp.ResultHandler
        public void completed(int i2, boolean z2) {
            Throwable th = null;
            try {
                WindowsAsynchronousSocketChannelImpl.this.begin();
                afterConnect();
                this.result.setResult(null);
                WindowsAsynchronousSocketChannelImpl.this.end();
            } catch (Throwable th2) {
                th = th2;
                WindowsAsynchronousSocketChannelImpl.this.end();
            }
            if (th != null) {
                closeChannel();
                this.result.setFailure(toIOException(th));
            }
            if (z2) {
                Invoker.invokeUnchecked(this.result);
            } else {
                Invoker.invoke(this.result);
            }
        }

        @Override // sun.nio.ch.Iocp.ResultHandler
        public void failed(int i2, IOException iOException) {
            if (WindowsAsynchronousSocketChannelImpl.this.isOpen()) {
                closeChannel();
                this.result.setFailure(iOException);
            } else {
                this.result.setFailure(new AsynchronousCloseException());
            }
            Invoker.invoke(this.result);
        }
    }

    private void doPrivilegedBind(final SocketAddress socketAddress) throws IOException {
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: sun.nio.ch.WindowsAsynchronousSocketChannelImpl.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Void run() throws IOException {
                    WindowsAsynchronousSocketChannelImpl.this.bind(socketAddress);
                    return null;
                }
            });
        } catch (PrivilegedActionException e2) {
            throw ((IOException) e2.getException());
        }
    }

    @Override // sun.nio.ch.AsynchronousSocketChannelImpl
    <A> Future<Void> implConnect(SocketAddress socketAddress, A a2, CompletionHandler<Void, ? super A> completionHandler) {
        if (!isOpen()) {
            ClosedChannelException closedChannelException = new ClosedChannelException();
            if (completionHandler == null) {
                return CompletedFuture.withFailure(closedChannelException);
            }
            Invoker.invoke(this, completionHandler, a2, null, closedChannelException);
            return null;
        }
        InetSocketAddress inetSocketAddressCheckAddress = Net.checkAddress(socketAddress);
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkConnect(inetSocketAddressCheckAddress.getAddress().getHostAddress(), inetSocketAddressCheckAddress.getPort());
        }
        IOException iOException = null;
        synchronized (this.stateLock) {
            if (this.state == 2) {
                throw new AlreadyConnectedException();
            }
            if (this.state == 1) {
                throw new ConnectionPendingException();
            }
            if (this.localAddress == null) {
                try {
                    InetSocketAddress inetSocketAddress = new InetSocketAddress(0);
                    if (securityManager == null) {
                        bind((SocketAddress) inetSocketAddress);
                    } else {
                        doPrivilegedBind(inetSocketAddress);
                    }
                } catch (IOException e2) {
                    iOException = e2;
                }
            }
            if (iOException == null) {
                this.state = 1;
            }
        }
        if (iOException != null) {
            try {
                close();
            } catch (IOException e3) {
            }
            if (completionHandler == null) {
                return CompletedFuture.withFailure(iOException);
            }
            Invoker.invoke(this, completionHandler, a2, null, iOException);
            return null;
        }
        PendingFuture pendingFuture = new PendingFuture(this, completionHandler, a2);
        ConnectTask connectTask = new ConnectTask(inetSocketAddressCheckAddress, pendingFuture);
        pendingFuture.setContext(connectTask);
        if (Iocp.supportsThreadAgnosticIo()) {
            connectTask.run();
        } else {
            Invoker.invokeOnThreadInThreadPool(this, connectTask);
        }
        return pendingFuture;
    }

    /* loaded from: rt.jar:sun/nio/ch/WindowsAsynchronousSocketChannelImpl$ReadTask.class */
    private class ReadTask<V, A> implements Runnable, Iocp.ResultHandler {
        private final ByteBuffer[] bufs;
        private final int numBufs;
        private final boolean scatteringRead;
        private final PendingFuture<V, A> result;
        private ByteBuffer[] shadow;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !WindowsAsynchronousSocketChannelImpl.class.desiredAssertionStatus();
        }

        ReadTask(ByteBuffer[] byteBufferArr, boolean z2, PendingFuture<V, A> pendingFuture) {
            this.bufs = byteBufferArr;
            this.numBufs = byteBufferArr.length > 16 ? 16 : byteBufferArr.length;
            this.scatteringRead = z2;
            this.result = pendingFuture;
        }

        /* JADX WARN: Multi-variable type inference failed */
        void prepareBuffers() {
            long jAddress;
            this.shadow = new ByteBuffer[this.numBufs];
            long j2 = WindowsAsynchronousSocketChannelImpl.this.readBufferArray;
            for (int i2 = 0; i2 < this.numBufs; i2++) {
                ByteBuffer byteBuffer = this.bufs[i2];
                int iPosition = byteBuffer.position();
                int iLimit = byteBuffer.limit();
                if (!$assertionsDisabled && iPosition > iLimit) {
                    throw new AssertionError();
                }
                int i3 = iPosition <= iLimit ? iLimit - iPosition : 0;
                if (!(byteBuffer instanceof DirectBuffer)) {
                    Object temporaryDirectBuffer = Util.getTemporaryDirectBuffer(i3);
                    this.shadow[i2] = temporaryDirectBuffer;
                    jAddress = ((DirectBuffer) temporaryDirectBuffer).address();
                } else {
                    this.shadow[i2] = byteBuffer;
                    jAddress = ((DirectBuffer) byteBuffer).address() + iPosition;
                }
                WindowsAsynchronousSocketChannelImpl.unsafe.putAddress(j2 + WindowsAsynchronousSocketChannelImpl.OFFSETOF_BUF, jAddress);
                WindowsAsynchronousSocketChannelImpl.unsafe.putInt(j2 + 0, i3);
                j2 += WindowsAsynchronousSocketChannelImpl.SIZEOF_WSABUF;
            }
        }

        void updateBuffers(int i2) {
            int i3 = 0;
            while (true) {
                if (i3 >= this.numBufs) {
                    break;
                }
                ByteBuffer byteBuffer = this.shadow[i3];
                int iPosition = byteBuffer.position();
                int iRemaining = byteBuffer.remaining();
                if (i2 >= iRemaining) {
                    i2 -= iRemaining;
                    try {
                        byteBuffer.position(iPosition + iRemaining);
                    } catch (IllegalArgumentException e2) {
                    }
                    i3++;
                } else if (i2 > 0) {
                    if (!$assertionsDisabled && iPosition + i2 >= 2147483647L) {
                        throw new AssertionError();
                    }
                    try {
                        byteBuffer.position(iPosition + i2);
                    } catch (IllegalArgumentException e3) {
                    }
                }
            }
            for (int i4 = 0; i4 < this.numBufs; i4++) {
                if (!(this.bufs[i4] instanceof DirectBuffer)) {
                    this.shadow[i4].flip();
                    try {
                        this.bufs[i4].put(this.shadow[i4]);
                    } catch (BufferOverflowException e4) {
                    }
                }
            }
        }

        void releaseBuffers() {
            for (int i2 = 0; i2 < this.numBufs; i2++) {
                if (!(this.bufs[i2] instanceof DirectBuffer)) {
                    Util.releaseTemporaryDirectBuffer(this.shadow[i2]);
                }
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            long jAdd;
            int i2;
            try {
                try {
                    WindowsAsynchronousSocketChannelImpl.this.begin();
                    prepareBuffers();
                    jAdd = WindowsAsynchronousSocketChannelImpl.this.ioCache.add(this.result);
                    i2 = WindowsAsynchronousSocketChannelImpl.read0(WindowsAsynchronousSocketChannelImpl.this.handle, this.numBufs, WindowsAsynchronousSocketChannelImpl.this.readBufferArray, jAdd);
                } catch (Throwable th) {
                    th = th;
                    WindowsAsynchronousSocketChannelImpl.this.enableReading();
                    if (th instanceof ClosedChannelException) {
                        th = new AsynchronousCloseException();
                    }
                    if (!(th instanceof IOException)) {
                        th = new IOException(th);
                    }
                    this.result.setFailure(th);
                    if (0 == 0) {
                        if (0 != 0) {
                            WindowsAsynchronousSocketChannelImpl.this.ioCache.remove(0L);
                        }
                        if (0 != 0) {
                            releaseBuffers();
                        }
                    }
                    WindowsAsynchronousSocketChannelImpl.this.end();
                }
                if (i2 == -2) {
                    if (1 == 0) {
                        if (jAdd != 0) {
                            WindowsAsynchronousSocketChannelImpl.this.ioCache.remove(jAdd);
                        }
                        if (1 != 0) {
                            releaseBuffers();
                        }
                    }
                    WindowsAsynchronousSocketChannelImpl.this.end();
                    return;
                }
                if (i2 != -1) {
                    throw new InternalError("Read completed immediately");
                }
                WindowsAsynchronousSocketChannelImpl.this.enableReading();
                if (this.scatteringRead) {
                    this.result.setResult(-1L);
                } else {
                    this.result.setResult(-1);
                }
                if (0 == 0) {
                    if (jAdd != 0) {
                        WindowsAsynchronousSocketChannelImpl.this.ioCache.remove(jAdd);
                    }
                    if (1 != 0) {
                        releaseBuffers();
                    }
                }
                WindowsAsynchronousSocketChannelImpl.this.end();
                Invoker.invoke(this.result);
            } catch (Throwable th2) {
                if (0 == 0) {
                    if (0 != 0) {
                        WindowsAsynchronousSocketChannelImpl.this.ioCache.remove(0L);
                    }
                    if (0 != 0) {
                        releaseBuffers();
                    }
                }
                WindowsAsynchronousSocketChannelImpl.this.end();
                throw th2;
            }
        }

        @Override // sun.nio.ch.Iocp.ResultHandler
        public void completed(int i2, boolean z2) {
            if (i2 == 0) {
                i2 = -1;
            } else {
                updateBuffers(i2);
            }
            releaseBuffers();
            synchronized (this.result) {
                if (this.result.isDone()) {
                    return;
                }
                WindowsAsynchronousSocketChannelImpl.this.enableReading();
                if (this.scatteringRead) {
                    this.result.setResult(Long.valueOf(i2));
                } else {
                    this.result.setResult(Integer.valueOf(i2));
                }
                if (z2) {
                    Invoker.invokeUnchecked(this.result);
                } else {
                    Invoker.invoke(this.result);
                }
            }
        }

        @Override // sun.nio.ch.Iocp.ResultHandler
        public void failed(int i2, IOException iOException) {
            releaseBuffers();
            if (!WindowsAsynchronousSocketChannelImpl.this.isOpen()) {
                iOException = new AsynchronousCloseException();
            }
            synchronized (this.result) {
                if (this.result.isDone()) {
                    return;
                }
                WindowsAsynchronousSocketChannelImpl.this.enableReading();
                this.result.setFailure(iOException);
                Invoker.invoke(this.result);
            }
        }

        void timeout() {
            synchronized (this.result) {
                if (this.result.isDone()) {
                    return;
                }
                WindowsAsynchronousSocketChannelImpl.this.enableReading(true);
                this.result.setFailure(new InterruptedByTimeoutException());
                Invoker.invoke(this.result);
            }
        }
    }

    @Override // sun.nio.ch.AsynchronousSocketChannelImpl
    <V extends Number, A> Future<V> implRead(boolean z2, ByteBuffer byteBuffer, ByteBuffer[] byteBufferArr, long j2, TimeUnit timeUnit, A a2, CompletionHandler<V, ? super A> completionHandler) {
        ByteBuffer[] byteBufferArr2;
        PendingFuture pendingFuture = new PendingFuture(this, completionHandler, a2);
        if (z2) {
            byteBufferArr2 = byteBufferArr;
        } else {
            byteBufferArr2 = new ByteBuffer[]{byteBuffer};
        }
        final ReadTask readTask = new ReadTask(byteBufferArr2, z2, pendingFuture);
        pendingFuture.setContext(readTask);
        if (j2 > 0) {
            pendingFuture.setTimeoutTask(this.iocp.schedule(new Runnable() { // from class: sun.nio.ch.WindowsAsynchronousSocketChannelImpl.2
                @Override // java.lang.Runnable
                public void run() {
                    readTask.timeout();
                }
            }, j2, timeUnit));
        }
        if (Iocp.supportsThreadAgnosticIo()) {
            readTask.run();
        } else {
            Invoker.invokeOnThreadInThreadPool(this, readTask);
        }
        return pendingFuture;
    }

    /* loaded from: rt.jar:sun/nio/ch/WindowsAsynchronousSocketChannelImpl$WriteTask.class */
    private class WriteTask<V, A> implements Runnable, Iocp.ResultHandler {
        private final ByteBuffer[] bufs;
        private final int numBufs;
        private final boolean gatheringWrite;
        private final PendingFuture<V, A> result;
        private ByteBuffer[] shadow;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !WindowsAsynchronousSocketChannelImpl.class.desiredAssertionStatus();
        }

        WriteTask(ByteBuffer[] byteBufferArr, boolean z2, PendingFuture<V, A> pendingFuture) {
            this.bufs = byteBufferArr;
            this.numBufs = byteBufferArr.length > 16 ? 16 : byteBufferArr.length;
            this.gatheringWrite = z2;
            this.result = pendingFuture;
        }

        /* JADX WARN: Multi-variable type inference failed */
        void prepareBuffers() {
            long jAddress;
            this.shadow = new ByteBuffer[this.numBufs];
            long j2 = WindowsAsynchronousSocketChannelImpl.this.writeBufferArray;
            for (int i2 = 0; i2 < this.numBufs; i2++) {
                DirectByteBufferR directByteBufferR = this.bufs[i2];
                int iPosition = directByteBufferR.position();
                int iLimit = directByteBufferR.limit();
                if (!$assertionsDisabled && iPosition > iLimit) {
                    throw new AssertionError();
                }
                int i3 = iPosition <= iLimit ? iLimit - iPosition : 0;
                if (!(directByteBufferR instanceof DirectBuffer)) {
                    ByteBuffer temporaryDirectBuffer = Util.getTemporaryDirectBuffer(i3);
                    temporaryDirectBuffer.put(directByteBufferR);
                    temporaryDirectBuffer.flip();
                    directByteBufferR.position(iPosition);
                    this.shadow[i2] = temporaryDirectBuffer;
                    jAddress = ((DirectBuffer) temporaryDirectBuffer).address();
                } else {
                    this.shadow[i2] = directByteBufferR;
                    jAddress = directByteBufferR.address() + iPosition;
                }
                WindowsAsynchronousSocketChannelImpl.unsafe.putAddress(j2 + WindowsAsynchronousSocketChannelImpl.OFFSETOF_BUF, jAddress);
                WindowsAsynchronousSocketChannelImpl.unsafe.putInt(j2 + 0, i3);
                j2 += WindowsAsynchronousSocketChannelImpl.SIZEOF_WSABUF;
            }
        }

        void updateBuffers(int i2) {
            for (int i3 = 0; i3 < this.numBufs; i3++) {
                ByteBuffer byteBuffer = this.bufs[i3];
                int iPosition = byteBuffer.position();
                int iLimit = byteBuffer.limit();
                int i4 = iPosition <= iLimit ? iLimit - iPosition : iLimit;
                if (i2 >= i4) {
                    i2 -= i4;
                    try {
                        byteBuffer.position(iPosition + i4);
                    } catch (IllegalArgumentException e2) {
                    }
                } else {
                    if (i2 > 0) {
                        if (!$assertionsDisabled && iPosition + i2 >= 2147483647L) {
                            throw new AssertionError();
                        }
                        try {
                            byteBuffer.position(iPosition + i2);
                            return;
                        } catch (IllegalArgumentException e3) {
                            return;
                        }
                    }
                    return;
                }
            }
        }

        void releaseBuffers() {
            for (int i2 = 0; i2 < this.numBufs; i2++) {
                if (!(this.bufs[i2] instanceof DirectBuffer)) {
                    Util.releaseTemporaryDirectBuffer(this.shadow[i2]);
                }
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                try {
                    WindowsAsynchronousSocketChannelImpl.this.begin();
                    prepareBuffers();
                    long jAdd = WindowsAsynchronousSocketChannelImpl.this.ioCache.add(this.result);
                    int iWrite0 = WindowsAsynchronousSocketChannelImpl.write0(WindowsAsynchronousSocketChannelImpl.this.handle, this.numBufs, WindowsAsynchronousSocketChannelImpl.this.writeBufferArray, jAdd);
                    if (iWrite0 != -2) {
                        if (iWrite0 == -1) {
                            throw new ClosedChannelException();
                        }
                        throw new InternalError("Write completed immediately");
                    }
                    if (1 == 0) {
                        if (jAdd != 0) {
                            WindowsAsynchronousSocketChannelImpl.this.ioCache.remove(jAdd);
                        }
                        if (1 != 0) {
                            releaseBuffers();
                        }
                    }
                    WindowsAsynchronousSocketChannelImpl.this.end();
                } catch (Throwable th) {
                    th = th;
                    WindowsAsynchronousSocketChannelImpl.this.enableWriting();
                    if (0 == 0 && (th instanceof ClosedChannelException)) {
                        th = new AsynchronousCloseException();
                    }
                    if (!(th instanceof IOException)) {
                        th = new IOException(th);
                    }
                    this.result.setFailure(th);
                    if (0 == 0) {
                        if (0 != 0) {
                            WindowsAsynchronousSocketChannelImpl.this.ioCache.remove(0L);
                        }
                        if (0 != 0) {
                            releaseBuffers();
                        }
                    }
                    WindowsAsynchronousSocketChannelImpl.this.end();
                    Invoker.invoke(this.result);
                }
            } catch (Throwable th2) {
                if (0 == 0) {
                    if (0 != 0) {
                        WindowsAsynchronousSocketChannelImpl.this.ioCache.remove(0L);
                    }
                    if (0 != 0) {
                        releaseBuffers();
                    }
                }
                WindowsAsynchronousSocketChannelImpl.this.end();
                throw th2;
            }
        }

        @Override // sun.nio.ch.Iocp.ResultHandler
        public void completed(int i2, boolean z2) {
            updateBuffers(i2);
            releaseBuffers();
            synchronized (this.result) {
                if (this.result.isDone()) {
                    return;
                }
                WindowsAsynchronousSocketChannelImpl.this.enableWriting();
                if (this.gatheringWrite) {
                    this.result.setResult(Long.valueOf(i2));
                } else {
                    this.result.setResult(Integer.valueOf(i2));
                }
                if (z2) {
                    Invoker.invokeUnchecked(this.result);
                } else {
                    Invoker.invoke(this.result);
                }
            }
        }

        @Override // sun.nio.ch.Iocp.ResultHandler
        public void failed(int i2, IOException iOException) {
            releaseBuffers();
            if (!WindowsAsynchronousSocketChannelImpl.this.isOpen()) {
                iOException = new AsynchronousCloseException();
            }
            synchronized (this.result) {
                if (this.result.isDone()) {
                    return;
                }
                WindowsAsynchronousSocketChannelImpl.this.enableWriting();
                this.result.setFailure(iOException);
                Invoker.invoke(this.result);
            }
        }

        void timeout() {
            synchronized (this.result) {
                if (this.result.isDone()) {
                    return;
                }
                WindowsAsynchronousSocketChannelImpl.this.enableWriting(true);
                this.result.setFailure(new InterruptedByTimeoutException());
                Invoker.invoke(this.result);
            }
        }
    }

    @Override // sun.nio.ch.AsynchronousSocketChannelImpl
    <V extends Number, A> Future<V> implWrite(boolean z2, ByteBuffer byteBuffer, ByteBuffer[] byteBufferArr, long j2, TimeUnit timeUnit, A a2, CompletionHandler<V, ? super A> completionHandler) {
        ByteBuffer[] byteBufferArr2;
        PendingFuture pendingFuture = new PendingFuture(this, completionHandler, a2);
        if (z2) {
            byteBufferArr2 = byteBufferArr;
        } else {
            byteBufferArr2 = new ByteBuffer[]{byteBuffer};
        }
        final WriteTask writeTask = new WriteTask(byteBufferArr2, z2, pendingFuture);
        pendingFuture.setContext(writeTask);
        if (j2 > 0) {
            pendingFuture.setTimeoutTask(this.iocp.schedule(new Runnable() { // from class: sun.nio.ch.WindowsAsynchronousSocketChannelImpl.3
                @Override // java.lang.Runnable
                public void run() {
                    writeTask.timeout();
                }
            }, j2, timeUnit));
        }
        if (Iocp.supportsThreadAgnosticIo()) {
            writeTask.run();
        } else {
            Invoker.invokeOnThreadInThreadPool(this, writeTask);
        }
        return pendingFuture;
    }
}
