package sun.nio.ch;

import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileLock;
import java.nio.channels.NonReadableChannelException;
import java.nio.channels.NonWritableChannelException;
import java.util.concurrent.Future;
import sun.misc.JavaIOFileDescriptorAccess;
import sun.misc.SharedSecrets;
import sun.nio.ch.Iocp;

/* loaded from: rt.jar:sun/nio/ch/WindowsAsynchronousFileChannelImpl.class */
public class WindowsAsynchronousFileChannelImpl extends AsynchronousFileChannelImpl implements Iocp.OverlappedChannel, Groupable {
    private static final JavaIOFileDescriptorAccess fdAccess;
    private static final int ERROR_HANDLE_EOF = 38;
    private static final FileDispatcher nd;
    private final long handle;
    private final int completionKey;
    private final Iocp iocp;
    private final boolean isDefaultIocp;
    private final PendingIoCache ioCache;
    static final int NO_LOCK = -1;
    static final int LOCKED = 0;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* JADX INFO: Access modifiers changed from: private */
    public static native int readFile(long j2, long j3, int i2, long j4, long j5) throws IOException;

    /* JADX INFO: Access modifiers changed from: private */
    public static native int writeFile(long j2, long j3, int i2, long j4, long j5) throws IOException;

    /* JADX INFO: Access modifiers changed from: private */
    public static native int lockFile(long j2, long j3, long j4, boolean z2, long j5) throws IOException;

    private static native void close0(long j2);

    static {
        $assertionsDisabled = !WindowsAsynchronousFileChannelImpl.class.desiredAssertionStatus();
        fdAccess = SharedSecrets.getJavaIOFileDescriptorAccess();
        nd = new FileDispatcherImpl();
        IOUtil.load();
    }

    /* loaded from: rt.jar:sun/nio/ch/WindowsAsynchronousFileChannelImpl$DefaultIocpHolder.class */
    private static class DefaultIocpHolder {
        static final Iocp defaultIocp = defaultIocp();

        private DefaultIocpHolder() {
        }

        private static Iocp defaultIocp() {
            try {
                return new Iocp(null, ThreadPool.createDefault()).start();
            } catch (IOException e2) {
                throw new InternalError(e2);
            }
        }
    }

    private WindowsAsynchronousFileChannelImpl(FileDescriptor fileDescriptor, boolean z2, boolean z3, Iocp iocp, boolean z4) throws IOException {
        super(fileDescriptor, z2, z3, iocp.executor());
        this.handle = fdAccess.getHandle(fileDescriptor);
        this.iocp = iocp;
        this.isDefaultIocp = z4;
        this.ioCache = new PendingIoCache();
        this.completionKey = iocp.associate(this, this.handle);
    }

    public static AsynchronousFileChannel open(FileDescriptor fileDescriptor, boolean z2, boolean z3, ThreadPool threadPool) throws IOException {
        Iocp iocpStart;
        boolean z4;
        if (threadPool == null) {
            iocpStart = DefaultIocpHolder.defaultIocp;
            z4 = true;
        } else {
            iocpStart = new Iocp(null, threadPool).start();
            z4 = false;
        }
        try {
            return new WindowsAsynchronousFileChannelImpl(fileDescriptor, z2, z3, iocpStart, z4);
        } catch (IOException e2) {
            if (!z4) {
                iocpStart.implClose();
            }
            throw e2;
        }
    }

    @Override // sun.nio.ch.Iocp.OverlappedChannel
    public <V, A> PendingFuture<V, A> getByOverlapped(long j2) {
        return this.ioCache.remove(j2);
    }

    @Override // java.nio.channels.AsynchronousChannel, java.nio.channels.Channel, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.closeLock.writeLock().lock();
        try {
            if (this.closed) {
                return;
            }
            this.closed = true;
            invalidateAllLocks();
            close0(this.handle);
            this.ioCache.close();
            this.iocp.disassociate(this.completionKey);
            if (!this.isDefaultIocp) {
                this.iocp.detachFromThreadPool();
            }
        } finally {
            this.closeLock.writeLock().unlock();
        }
    }

    @Override // sun.nio.ch.Groupable
    public AsynchronousChannelGroupImpl group() {
        return this.iocp;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static IOException toIOException(Throwable th) {
        if (th instanceof IOException) {
            if (th instanceof ClosedChannelException) {
                th = new AsynchronousCloseException();
            }
            return (IOException) th;
        }
        return new IOException(th);
    }

    @Override // java.nio.channels.AsynchronousFileChannel
    public long size() throws IOException {
        try {
            begin();
            return nd.size(this.fdObj);
        } finally {
            end();
        }
    }

    @Override // java.nio.channels.AsynchronousFileChannel
    public AsynchronousFileChannel truncate(long j2) throws IOException {
        if (j2 < 0) {
            throw new IllegalArgumentException("Negative size");
        }
        if (!this.writing) {
            throw new NonWritableChannelException();
        }
        try {
            begin();
            if (j2 > nd.size(this.fdObj)) {
                return this;
            }
            nd.truncate(this.fdObj, j2);
            end();
            return this;
        } finally {
            end();
        }
    }

    @Override // java.nio.channels.AsynchronousFileChannel
    public void force(boolean z2) throws IOException {
        try {
            begin();
            nd.force(this.fdObj, z2);
        } finally {
            end();
        }
    }

    /* loaded from: rt.jar:sun/nio/ch/WindowsAsynchronousFileChannelImpl$LockTask.class */
    private class LockTask<A> implements Runnable, Iocp.ResultHandler {
        private final long position;
        private final FileLockImpl fli;
        private final PendingFuture<FileLock, A> result;

        LockTask(long j2, FileLockImpl fileLockImpl, PendingFuture<FileLock, A> pendingFuture) {
            this.position = j2;
            this.fli = fileLockImpl;
            this.result = pendingFuture;
        }

        @Override // java.lang.Runnable
        public void run() {
            long jAdd;
            try {
                try {
                    WindowsAsynchronousFileChannelImpl.this.begin();
                    jAdd = WindowsAsynchronousFileChannelImpl.this.ioCache.add(this.result);
                } catch (Throwable th) {
                    WindowsAsynchronousFileChannelImpl.this.removeFromFileLockTable(this.fli);
                    this.result.setFailure(WindowsAsynchronousFileChannelImpl.toIOException(th));
                    if (0 == 0 && 0 != 0) {
                        WindowsAsynchronousFileChannelImpl.this.ioCache.remove(0L);
                    }
                    WindowsAsynchronousFileChannelImpl.this.end();
                }
                synchronized (this.result) {
                    if (WindowsAsynchronousFileChannelImpl.lockFile(WindowsAsynchronousFileChannelImpl.this.handle, this.position, this.fli.size(), this.fli.isShared(), jAdd) == -2) {
                        if (1 == 0 && jAdd != 0) {
                            WindowsAsynchronousFileChannelImpl.this.ioCache.remove(jAdd);
                        }
                        WindowsAsynchronousFileChannelImpl.this.end();
                        return;
                    }
                    this.result.setResult(this.fli);
                    if (0 == 0 && jAdd != 0) {
                        WindowsAsynchronousFileChannelImpl.this.ioCache.remove(jAdd);
                    }
                    WindowsAsynchronousFileChannelImpl.this.end();
                    Invoker.invoke(this.result);
                }
            } catch (Throwable th2) {
                if (0 == 0 && 0 != 0) {
                    WindowsAsynchronousFileChannelImpl.this.ioCache.remove(0L);
                }
                WindowsAsynchronousFileChannelImpl.this.end();
                throw th2;
            }
        }

        @Override // sun.nio.ch.Iocp.ResultHandler
        public void completed(int i2, boolean z2) {
            this.result.setResult(this.fli);
            if (z2) {
                Invoker.invokeUnchecked(this.result);
            } else {
                Invoker.invoke(this.result);
            }
        }

        @Override // sun.nio.ch.Iocp.ResultHandler
        public void failed(int i2, IOException iOException) {
            WindowsAsynchronousFileChannelImpl.this.removeFromFileLockTable(this.fli);
            if (WindowsAsynchronousFileChannelImpl.this.isOpen()) {
                this.result.setFailure(iOException);
            } else {
                this.result.setFailure(new AsynchronousCloseException());
            }
            Invoker.invoke(this.result);
        }
    }

    @Override // sun.nio.ch.AsynchronousFileChannelImpl
    <A> Future<FileLock> implLock(long j2, long j3, boolean z2, A a2, CompletionHandler<FileLock, ? super A> completionHandler) {
        if (z2 && !this.reading) {
            throw new NonReadableChannelException();
        }
        if (!z2 && !this.writing) {
            throw new NonWritableChannelException();
        }
        FileLockImpl fileLockImplAddToFileLockTable = addToFileLockTable(j2, j3, z2);
        if (fileLockImplAddToFileLockTable == null) {
            ClosedChannelException closedChannelException = new ClosedChannelException();
            if (completionHandler == null) {
                return CompletedFuture.withFailure(closedChannelException);
            }
            Invoker.invoke(this, completionHandler, a2, null, closedChannelException);
            return null;
        }
        PendingFuture pendingFuture = new PendingFuture(this, completionHandler, a2);
        LockTask lockTask = new LockTask(j2, fileLockImplAddToFileLockTable, pendingFuture);
        pendingFuture.setContext(lockTask);
        if (Iocp.supportsThreadAgnosticIo()) {
            lockTask.run();
        } else {
            boolean z3 = false;
            try {
                Invoker.invokeOnThreadInThreadPool(this, lockTask);
                z3 = true;
                if (1 == 0) {
                    removeFromFileLockTable(fileLockImplAddToFileLockTable);
                }
            } catch (Throwable th) {
                if (!z3) {
                    removeFromFileLockTable(fileLockImplAddToFileLockTable);
                }
                throw th;
            }
        }
        return pendingFuture;
    }

    @Override // java.nio.channels.AsynchronousFileChannel
    public FileLock tryLock(long j2, long j3, boolean z2) throws IOException {
        if (z2 && !this.reading) {
            throw new NonReadableChannelException();
        }
        if (!z2 && !this.writing) {
            throw new NonWritableChannelException();
        }
        FileLockImpl fileLockImplAddToFileLockTable = addToFileLockTable(j2, j3, z2);
        if (fileLockImplAddToFileLockTable == null) {
            throw new ClosedChannelException();
        }
        try {
            begin();
            if (nd.lock(this.fdObj, false, j2, j3, z2) == -1) {
                return null;
            }
            if (1 == 0) {
                removeFromFileLockTable(fileLockImplAddToFileLockTable);
            }
            end();
            return fileLockImplAddToFileLockTable;
        } finally {
            if (0 == 0) {
                removeFromFileLockTable(fileLockImplAddToFileLockTable);
            }
            end();
        }
    }

    @Override // sun.nio.ch.AsynchronousFileChannelImpl
    protected void implRelease(FileLockImpl fileLockImpl) throws IOException {
        nd.release(this.fdObj, fileLockImpl.position(), fileLockImpl.size());
    }

    /* loaded from: rt.jar:sun/nio/ch/WindowsAsynchronousFileChannelImpl$ReadTask.class */
    private class ReadTask<A> implements Runnable, Iocp.ResultHandler {
        private final ByteBuffer dst;
        private final int pos;
        private final int rem;
        private final long position;
        private final PendingFuture<Integer, A> result;
        private volatile ByteBuffer buf;

        ReadTask(ByteBuffer byteBuffer, int i2, int i3, long j2, PendingFuture<Integer, A> pendingFuture) {
            this.dst = byteBuffer;
            this.pos = i2;
            this.rem = i3;
            this.position = j2;
            this.result = pendingFuture;
        }

        void releaseBufferIfSubstituted() {
            if (this.buf != this.dst) {
                Util.releaseTemporaryDirectBuffer(this.buf);
            }
        }

        void updatePosition(int i2) {
            if (i2 > 0) {
                if (this.buf == this.dst) {
                    try {
                        this.dst.position(this.pos + i2);
                    } catch (IllegalArgumentException e2) {
                    }
                } else {
                    this.buf.position(i2).flip();
                    try {
                        this.dst.put(this.buf);
                    } catch (BufferOverflowException e3) {
                    }
                }
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            long jAddress;
            long jAdd;
            int file;
            if (this.dst instanceof DirectBuffer) {
                this.buf = this.dst;
                jAddress = ((DirectBuffer) this.dst).address() + this.pos;
            } else {
                this.buf = Util.getTemporaryDirectBuffer(this.rem);
                jAddress = ((DirectBuffer) this.buf).address();
            }
            try {
                try {
                    WindowsAsynchronousFileChannelImpl.this.begin();
                    jAdd = WindowsAsynchronousFileChannelImpl.this.ioCache.add(this.result);
                    file = WindowsAsynchronousFileChannelImpl.readFile(WindowsAsynchronousFileChannelImpl.this.handle, jAddress, this.rem, this.position, jAdd);
                } catch (Throwable th) {
                    this.result.setFailure(WindowsAsynchronousFileChannelImpl.toIOException(th));
                    if (0 == 0) {
                        if (0 != 0) {
                            WindowsAsynchronousFileChannelImpl.this.ioCache.remove(0L);
                        }
                        releaseBufferIfSubstituted();
                    }
                    WindowsAsynchronousFileChannelImpl.this.end();
                }
                if (file == -2) {
                    if (1 == 0) {
                        if (jAdd != 0) {
                            WindowsAsynchronousFileChannelImpl.this.ioCache.remove(jAdd);
                        }
                        releaseBufferIfSubstituted();
                    }
                    WindowsAsynchronousFileChannelImpl.this.end();
                    return;
                }
                if (file != -1) {
                    throw new InternalError("Unexpected result: " + file);
                }
                this.result.setResult(Integer.valueOf(file));
                if (0 == 0) {
                    if (jAdd != 0) {
                        WindowsAsynchronousFileChannelImpl.this.ioCache.remove(jAdd);
                    }
                    releaseBufferIfSubstituted();
                }
                WindowsAsynchronousFileChannelImpl.this.end();
                Invoker.invoke(this.result);
            } catch (Throwable th2) {
                if (0 == 0) {
                    if (0 != 0) {
                        WindowsAsynchronousFileChannelImpl.this.ioCache.remove(0L);
                    }
                    releaseBufferIfSubstituted();
                }
                WindowsAsynchronousFileChannelImpl.this.end();
                throw th2;
            }
        }

        @Override // sun.nio.ch.Iocp.ResultHandler
        public void completed(int i2, boolean z2) {
            updatePosition(i2);
            releaseBufferIfSubstituted();
            this.result.setResult(Integer.valueOf(i2));
            if (z2) {
                Invoker.invokeUnchecked(this.result);
            } else {
                Invoker.invoke(this.result);
            }
        }

        @Override // sun.nio.ch.Iocp.ResultHandler
        public void failed(int i2, IOException iOException) {
            if (i2 == 38) {
                completed(-1, false);
                return;
            }
            releaseBufferIfSubstituted();
            if (WindowsAsynchronousFileChannelImpl.this.isOpen()) {
                this.result.setFailure(iOException);
            } else {
                this.result.setFailure(new AsynchronousCloseException());
            }
            Invoker.invoke(this.result);
        }
    }

    @Override // sun.nio.ch.AsynchronousFileChannelImpl
    <A> Future<Integer> implRead(ByteBuffer byteBuffer, long j2, A a2, CompletionHandler<Integer, ? super A> completionHandler) {
        if (!this.reading) {
            throw new NonReadableChannelException();
        }
        if (j2 < 0) {
            throw new IllegalArgumentException("Negative position");
        }
        if (byteBuffer.isReadOnly()) {
            throw new IllegalArgumentException("Read-only buffer");
        }
        if (!isOpen()) {
            ClosedChannelException closedChannelException = new ClosedChannelException();
            if (completionHandler == null) {
                return CompletedFuture.withFailure(closedChannelException);
            }
            Invoker.invoke(this, completionHandler, a2, null, closedChannelException);
            return null;
        }
        int iPosition = byteBuffer.position();
        int iLimit = byteBuffer.limit();
        if (!$assertionsDisabled && iPosition > iLimit) {
            throw new AssertionError();
        }
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        if (i2 == 0) {
            if (completionHandler == null) {
                return CompletedFuture.withResult(0);
            }
            Invoker.invoke(this, completionHandler, a2, 0, null);
            return null;
        }
        PendingFuture pendingFuture = new PendingFuture(this, completionHandler, a2);
        ReadTask readTask = new ReadTask(byteBuffer, iPosition, i2, j2, pendingFuture);
        pendingFuture.setContext(readTask);
        if (Iocp.supportsThreadAgnosticIo()) {
            readTask.run();
        } else {
            Invoker.invokeOnThreadInThreadPool(this, readTask);
        }
        return pendingFuture;
    }

    /* loaded from: rt.jar:sun/nio/ch/WindowsAsynchronousFileChannelImpl$WriteTask.class */
    private class WriteTask<A> implements Runnable, Iocp.ResultHandler {
        private final ByteBuffer src;
        private final int pos;
        private final int rem;
        private final long position;
        private final PendingFuture<Integer, A> result;
        private volatile ByteBuffer buf;

        WriteTask(ByteBuffer byteBuffer, int i2, int i3, long j2, PendingFuture<Integer, A> pendingFuture) {
            this.src = byteBuffer;
            this.pos = i2;
            this.rem = i3;
            this.position = j2;
            this.result = pendingFuture;
        }

        void releaseBufferIfSubstituted() {
            if (this.buf != this.src) {
                Util.releaseTemporaryDirectBuffer(this.buf);
            }
        }

        void updatePosition(int i2) {
            if (i2 > 0) {
                try {
                    this.src.position(this.pos + i2);
                } catch (IllegalArgumentException e2) {
                }
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            long jAddress;
            long jAdd = 0;
            if (this.src instanceof DirectBuffer) {
                this.buf = this.src;
                jAddress = ((DirectBuffer) this.src).address() + this.pos;
            } else {
                this.buf = Util.getTemporaryDirectBuffer(this.rem);
                this.buf.put(this.src);
                this.buf.flip();
                this.src.position(this.pos);
                jAddress = ((DirectBuffer) this.buf).address();
            }
            try {
                try {
                    WindowsAsynchronousFileChannelImpl.this.begin();
                    jAdd = WindowsAsynchronousFileChannelImpl.this.ioCache.add(this.result);
                    int iWriteFile = WindowsAsynchronousFileChannelImpl.writeFile(WindowsAsynchronousFileChannelImpl.this.handle, jAddress, this.rem, this.position, jAdd);
                    if (iWriteFile == -2) {
                    } else {
                        throw new InternalError("Unexpected result: " + iWriteFile);
                    }
                } catch (Throwable th) {
                    this.result.setFailure(WindowsAsynchronousFileChannelImpl.toIOException(th));
                    if (jAdd != 0) {
                        WindowsAsynchronousFileChannelImpl.this.ioCache.remove(jAdd);
                    }
                    releaseBufferIfSubstituted();
                    WindowsAsynchronousFileChannelImpl.this.end();
                    Invoker.invoke(this.result);
                }
            } finally {
                WindowsAsynchronousFileChannelImpl.this.end();
            }
        }

        @Override // sun.nio.ch.Iocp.ResultHandler
        public void completed(int i2, boolean z2) {
            updatePosition(i2);
            releaseBufferIfSubstituted();
            this.result.setResult(Integer.valueOf(i2));
            if (z2) {
                Invoker.invokeUnchecked(this.result);
            } else {
                Invoker.invoke(this.result);
            }
        }

        @Override // sun.nio.ch.Iocp.ResultHandler
        public void failed(int i2, IOException iOException) {
            releaseBufferIfSubstituted();
            if (WindowsAsynchronousFileChannelImpl.this.isOpen()) {
                this.result.setFailure(iOException);
            } else {
                this.result.setFailure(new AsynchronousCloseException());
            }
            Invoker.invoke(this.result);
        }
    }

    @Override // sun.nio.ch.AsynchronousFileChannelImpl
    <A> Future<Integer> implWrite(ByteBuffer byteBuffer, long j2, A a2, CompletionHandler<Integer, ? super A> completionHandler) {
        if (!this.writing) {
            throw new NonWritableChannelException();
        }
        if (j2 < 0) {
            throw new IllegalArgumentException("Negative position");
        }
        if (!isOpen()) {
            ClosedChannelException closedChannelException = new ClosedChannelException();
            if (completionHandler == null) {
                return CompletedFuture.withFailure(closedChannelException);
            }
            Invoker.invoke(this, completionHandler, a2, null, closedChannelException);
            return null;
        }
        int iPosition = byteBuffer.position();
        int iLimit = byteBuffer.limit();
        if (!$assertionsDisabled && iPosition > iLimit) {
            throw new AssertionError();
        }
        int i2 = iPosition <= iLimit ? iLimit - iPosition : 0;
        if (i2 == 0) {
            if (completionHandler == null) {
                return CompletedFuture.withResult(0);
            }
            Invoker.invoke(this, completionHandler, a2, 0, null);
            return null;
        }
        PendingFuture pendingFuture = new PendingFuture(this, completionHandler, a2);
        WriteTask writeTask = new WriteTask(byteBuffer, iPosition, i2, j2, pendingFuture);
        pendingFuture.setContext(writeTask);
        if (Iocp.supportsThreadAgnosticIo()) {
            writeTask.run();
        } else {
            Invoker.invokeOnThreadInThreadPool(this, writeTask);
        }
        return pendingFuture;
    }
}
