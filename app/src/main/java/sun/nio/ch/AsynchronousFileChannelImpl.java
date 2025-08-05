package sun.nio.ch;

import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileLock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* loaded from: rt.jar:sun/nio/ch/AsynchronousFileChannelImpl.class */
abstract class AsynchronousFileChannelImpl extends AsynchronousFileChannel {
    protected final ReadWriteLock closeLock = new ReentrantReadWriteLock();
    protected volatile boolean closed;
    protected final FileDescriptor fdObj;
    protected final boolean reading;
    protected final boolean writing;
    protected final ExecutorService executor;
    private volatile FileLockTable fileLockTable;

    abstract <A> Future<FileLock> implLock(long j2, long j3, boolean z2, A a2, CompletionHandler<FileLock, ? super A> completionHandler);

    protected abstract void implRelease(FileLockImpl fileLockImpl) throws IOException;

    abstract <A> Future<Integer> implRead(ByteBuffer byteBuffer, long j2, A a2, CompletionHandler<Integer, ? super A> completionHandler);

    abstract <A> Future<Integer> implWrite(ByteBuffer byteBuffer, long j2, A a2, CompletionHandler<Integer, ? super A> completionHandler);

    protected AsynchronousFileChannelImpl(FileDescriptor fileDescriptor, boolean z2, boolean z3, ExecutorService executorService) {
        this.fdObj = fileDescriptor;
        this.reading = z2;
        this.writing = z3;
        this.executor = executorService;
    }

    final ExecutorService executor() {
        return this.executor;
    }

    @Override // java.nio.channels.Channel
    public final boolean isOpen() {
        return !this.closed;
    }

    protected final void begin() throws IOException {
        this.closeLock.readLock().lock();
        if (this.closed) {
            throw new ClosedChannelException();
        }
    }

    protected final void end() {
        this.closeLock.readLock().unlock();
    }

    protected final void end(boolean z2) throws IOException {
        end();
        if (!z2 && !isOpen()) {
            throw new AsynchronousCloseException();
        }
    }

    @Override // java.nio.channels.AsynchronousFileChannel
    public final Future<FileLock> lock(long j2, long j3, boolean z2) {
        return implLock(j2, j3, z2, null, null);
    }

    @Override // java.nio.channels.AsynchronousFileChannel
    public final <A> void lock(long j2, long j3, boolean z2, A a2, CompletionHandler<FileLock, ? super A> completionHandler) {
        if (completionHandler == null) {
            throw new NullPointerException("'handler' is null");
        }
        implLock(j2, j3, z2, a2, completionHandler);
    }

    final void ensureFileLockTableInitialized() throws IOException {
        if (this.fileLockTable == null) {
            synchronized (this) {
                if (this.fileLockTable == null) {
                    this.fileLockTable = FileLockTable.newSharedFileLockTable(this, this.fdObj);
                }
            }
        }
    }

    final void invalidateAllLocks() throws IOException {
        if (this.fileLockTable != null) {
            for (FileLock fileLock : this.fileLockTable.removeAll()) {
                synchronized (fileLock) {
                    if (fileLock.isValid()) {
                        FileLockImpl fileLockImpl = (FileLockImpl) fileLock;
                        implRelease(fileLockImpl);
                        fileLockImpl.invalidate();
                    }
                }
            }
        }
    }

    protected final FileLockImpl addToFileLockTable(long j2, long j3, boolean z2) {
        try {
            this.closeLock.readLock().lock();
            if (this.closed) {
                return null;
            }
            try {
                ensureFileLockTableInitialized();
                FileLockImpl fileLockImpl = new FileLockImpl(this, j2, j3, z2);
                this.fileLockTable.add(fileLockImpl);
                end();
                return fileLockImpl;
            } catch (IOException e2) {
                throw new AssertionError(e2);
            }
        } finally {
            end();
        }
    }

    protected final void removeFromFileLockTable(FileLockImpl fileLockImpl) {
        this.fileLockTable.remove(fileLockImpl);
    }

    final void release(FileLockImpl fileLockImpl) throws IOException {
        try {
            begin();
            implRelease(fileLockImpl);
            removeFromFileLockTable(fileLockImpl);
        } finally {
            end();
        }
    }

    @Override // java.nio.channels.AsynchronousFileChannel
    public final Future<Integer> read(ByteBuffer byteBuffer, long j2) {
        return implRead(byteBuffer, j2, null, null);
    }

    @Override // java.nio.channels.AsynchronousFileChannel
    public final <A> void read(ByteBuffer byteBuffer, long j2, A a2, CompletionHandler<Integer, ? super A> completionHandler) {
        if (completionHandler == null) {
            throw new NullPointerException("'handler' is null");
        }
        implRead(byteBuffer, j2, a2, completionHandler);
    }

    @Override // java.nio.channels.AsynchronousFileChannel
    public final Future<Integer> write(ByteBuffer byteBuffer, long j2) {
        return implWrite(byteBuffer, j2, null, null);
    }

    @Override // java.nio.channels.AsynchronousFileChannel
    public final <A> void write(ByteBuffer byteBuffer, long j2, A a2, CompletionHandler<Integer, ? super A> completionHandler) {
        if (completionHandler == null) {
            throw new NullPointerException("'handler' is null");
        }
        implWrite(byteBuffer, j2, a2, completionHandler);
    }
}
