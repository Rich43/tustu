package sun.nio.ch;

import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.FileLockInterruptionException;
import java.nio.channels.NonReadableChannelException;
import java.nio.channels.NonWritableChannelException;
import java.nio.channels.OverlappingFileLockException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.WritableByteChannel;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import sun.misc.Cleaner;
import sun.misc.JavaNioAccess;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/nio/ch/FileChannelImpl.class */
public class FileChannelImpl extends FileChannel {
    private static final long allocationGranularity;
    private final FileDispatcher nd;
    private final FileDescriptor fd;
    private final boolean writable;
    private final boolean readable;
    private final boolean append;
    private final Object parent;
    private final String path;
    private final NativeThreadSet threads = new NativeThreadSet(2);
    private final Object positionLock = new Object();
    private static volatile boolean transferSupported;
    private static volatile boolean pipeSupported;
    private static volatile boolean fileSupported;
    private static final long MAPPED_TRANSFER_SIZE = 8388608;
    private static final int TRANSFER_SIZE = 8192;
    private static final int MAP_RO = 0;
    private static final int MAP_RW = 1;
    private static final int MAP_PV = 2;
    private volatile FileLockTable fileLockTable;
    private static boolean isSharedFileLockTable;
    private static volatile boolean propertyChecked;
    static final /* synthetic */ boolean $assertionsDisabled;

    private native long map0(int i2, long j2, long j3) throws IOException;

    /* JADX INFO: Access modifiers changed from: private */
    public static native int unmap0(long j2, long j3);

    private native long transferTo0(FileDescriptor fileDescriptor, long j2, long j3, FileDescriptor fileDescriptor2);

    private static native long initIDs();

    static {
        $assertionsDisabled = !FileChannelImpl.class.desiredAssertionStatus();
        transferSupported = true;
        pipeSupported = true;
        fileSupported = true;
        IOUtil.load();
        allocationGranularity = initIDs();
    }

    private FileChannelImpl(FileDescriptor fileDescriptor, String str, boolean z2, boolean z3, boolean z4, Object obj) {
        this.fd = fileDescriptor;
        this.readable = z2;
        this.writable = z3;
        this.append = z4;
        this.parent = obj;
        this.path = str;
        this.nd = new FileDispatcherImpl(z4);
    }

    public static FileChannel open(FileDescriptor fileDescriptor, String str, boolean z2, boolean z3, Object obj) {
        return new FileChannelImpl(fileDescriptor, str, z2, z3, false, obj);
    }

    public static FileChannel open(FileDescriptor fileDescriptor, String str, boolean z2, boolean z3, boolean z4, Object obj) {
        return new FileChannelImpl(fileDescriptor, str, z2, z3, z4, obj);
    }

    private void ensureOpen() throws IOException {
        if (!isOpen()) {
            throw new ClosedChannelException();
        }
    }

    @Override // java.nio.channels.spi.AbstractInterruptibleChannel
    protected void implCloseChannel() throws IOException {
        if (this.fileLockTable != null) {
            for (FileLock fileLock : this.fileLockTable.removeAll()) {
                synchronized (fileLock) {
                    if (fileLock.isValid()) {
                        this.nd.release(this.fd, fileLock.position(), fileLock.size());
                        ((FileLockImpl) fileLock).invalidate();
                    }
                }
            }
        }
        this.threads.signalAndWait();
        if (this.parent != null) {
            ((Closeable) this.parent).close();
        } else {
            this.nd.close(this.fd);
        }
    }

    @Override // java.nio.channels.FileChannel, java.nio.channels.SeekableByteChannel, java.nio.channels.ReadableByteChannel
    public int read(ByteBuffer byteBuffer) throws IOException {
        ensureOpen();
        if (!this.readable) {
            throw new NonReadableChannelException();
        }
        synchronized (this.positionLock) {
            int i2 = 0;
            int iAdd = -1;
            try {
                begin();
                iAdd = this.threads.add();
                if (isOpen()) {
                    do {
                        i2 = IOUtil.read(this.fd, byteBuffer, -1L, this.nd);
                        if (i2 != -3) {
                            break;
                        }
                    } while (isOpen());
                    int iNormalize = IOStatus.normalize(i2);
                    this.threads.remove(iAdd);
                    end(i2 > 0);
                    if ($assertionsDisabled || IOStatus.check(i2)) {
                        return iNormalize;
                    }
                    throw new AssertionError();
                }
                this.threads.remove(iAdd);
                end(0 > 0);
                if ($assertionsDisabled || IOStatus.check(0)) {
                    return 0;
                }
                throw new AssertionError();
            } catch (Throwable th) {
                this.threads.remove(iAdd);
                end(i2 > 0);
                if ($assertionsDisabled || IOStatus.check(i2)) {
                    throw th;
                }
                throw new AssertionError();
            }
        }
    }

    @Override // java.nio.channels.FileChannel, java.nio.channels.ScatteringByteChannel
    public long read(ByteBuffer[] byteBufferArr, int i2, int i3) throws IOException {
        if (i2 < 0 || i3 < 0 || i2 > byteBufferArr.length - i3) {
            throw new IndexOutOfBoundsException();
        }
        ensureOpen();
        if (!this.readable) {
            throw new NonReadableChannelException();
        }
        synchronized (this.positionLock) {
            long j2 = 0;
            int iAdd = -1;
            try {
                begin();
                iAdd = this.threads.add();
                if (isOpen()) {
                    do {
                        j2 = IOUtil.read(this.fd, byteBufferArr, i2, i3, this.nd);
                        if (j2 != -3) {
                            break;
                        }
                    } while (isOpen());
                    long jNormalize = IOStatus.normalize(j2);
                    this.threads.remove(iAdd);
                    end(j2 > 0);
                    if ($assertionsDisabled || IOStatus.check(j2)) {
                        return jNormalize;
                    }
                    throw new AssertionError();
                }
                this.threads.remove(iAdd);
                end(0 > 0);
                if ($assertionsDisabled || IOStatus.check(0L)) {
                    return 0L;
                }
                throw new AssertionError();
            } catch (Throwable th) {
                this.threads.remove(iAdd);
                end(j2 > 0);
                if ($assertionsDisabled || IOStatus.check(j2)) {
                    throw th;
                }
                throw new AssertionError();
            }
        }
    }

    @Override // java.nio.channels.FileChannel, java.nio.channels.SeekableByteChannel, java.nio.channels.WritableByteChannel
    public int write(ByteBuffer byteBuffer) throws IOException {
        ensureOpen();
        if (!this.writable) {
            throw new NonWritableChannelException();
        }
        synchronized (this.positionLock) {
            int iWrite = 0;
            int iAdd = -1;
            try {
                begin();
                iAdd = this.threads.add();
                if (isOpen()) {
                    do {
                        iWrite = IOUtil.write(this.fd, byteBuffer, -1L, this.nd);
                        if (iWrite != -3) {
                            break;
                        }
                    } while (isOpen());
                    int iNormalize = IOStatus.normalize(iWrite);
                    this.threads.remove(iAdd);
                    end(iWrite > 0);
                    if ($assertionsDisabled || IOStatus.check(iWrite)) {
                        return iNormalize;
                    }
                    throw new AssertionError();
                }
                this.threads.remove(iAdd);
                end(0 > 0);
                if ($assertionsDisabled || IOStatus.check(0)) {
                    return 0;
                }
                throw new AssertionError();
            } catch (Throwable th) {
                this.threads.remove(iAdd);
                end(iWrite > 0);
                if ($assertionsDisabled || IOStatus.check(iWrite)) {
                    throw th;
                }
                throw new AssertionError();
            }
        }
    }

    @Override // java.nio.channels.FileChannel, java.nio.channels.GatheringByteChannel
    public long write(ByteBuffer[] byteBufferArr, int i2, int i3) throws IOException {
        if (i2 < 0 || i3 < 0 || i2 > byteBufferArr.length - i3) {
            throw new IndexOutOfBoundsException();
        }
        ensureOpen();
        if (!this.writable) {
            throw new NonWritableChannelException();
        }
        synchronized (this.positionLock) {
            long jWrite = 0;
            int iAdd = -1;
            try {
                begin();
                iAdd = this.threads.add();
                if (isOpen()) {
                    do {
                        jWrite = IOUtil.write(this.fd, byteBufferArr, i2, i3, this.nd);
                        if (jWrite != -3) {
                            break;
                        }
                    } while (isOpen());
                    long jNormalize = IOStatus.normalize(jWrite);
                    this.threads.remove(iAdd);
                    end(jWrite > 0);
                    if ($assertionsDisabled || IOStatus.check(jWrite)) {
                        return jNormalize;
                    }
                    throw new AssertionError();
                }
                this.threads.remove(iAdd);
                end(0 > 0);
                if ($assertionsDisabled || IOStatus.check(0L)) {
                    return 0L;
                }
                throw new AssertionError();
            } catch (Throwable th) {
                this.threads.remove(iAdd);
                end(jWrite > 0);
                if ($assertionsDisabled || IOStatus.check(jWrite)) {
                    throw th;
                }
                throw new AssertionError();
            }
        }
    }

    @Override // java.nio.channels.FileChannel, java.nio.channels.SeekableByteChannel
    public long position() throws IOException {
        ensureOpen();
        synchronized (this.positionLock) {
            long size = -1;
            int iAdd = -1;
            try {
                begin();
                iAdd = this.threads.add();
                if (isOpen()) {
                    do {
                        size = this.append ? this.nd.size(this.fd) : this.nd.seek(this.fd, -1L);
                        if (size != -3) {
                            break;
                        }
                    } while (isOpen());
                    long jNormalize = IOStatus.normalize(size);
                    this.threads.remove(iAdd);
                    end(size > -1);
                    if ($assertionsDisabled || IOStatus.check(size)) {
                        return jNormalize;
                    }
                    throw new AssertionError();
                }
                this.threads.remove(iAdd);
                end(-1 > -1);
                if ($assertionsDisabled || IOStatus.check(-1L)) {
                    return 0L;
                }
                throw new AssertionError();
            } catch (Throwable th) {
                this.threads.remove(iAdd);
                end(size > -1);
                if ($assertionsDisabled || IOStatus.check(size)) {
                    throw th;
                }
                throw new AssertionError();
            }
        }
    }

    @Override // java.nio.channels.FileChannel, java.nio.channels.SeekableByteChannel
    public FileChannel position(long j2) throws IOException {
        ensureOpen();
        if (j2 < 0) {
            throw new IllegalArgumentException();
        }
        synchronized (this.positionLock) {
            long jSeek = -1;
            int iAdd = -1;
            try {
                begin();
                iAdd = this.threads.add();
                if (!isOpen()) {
                    this.threads.remove(iAdd);
                    end(-1 > -1);
                    if ($assertionsDisabled || IOStatus.check(-1L)) {
                        return null;
                    }
                    throw new AssertionError();
                }
                do {
                    jSeek = this.nd.seek(this.fd, j2);
                    if (jSeek != -3) {
                        break;
                    }
                } while (isOpen());
                this.threads.remove(iAdd);
                end(jSeek > -1);
                if ($assertionsDisabled || IOStatus.check(jSeek)) {
                    return this;
                }
                throw new AssertionError();
            } catch (Throwable th) {
                this.threads.remove(iAdd);
                end(jSeek > -1);
                if ($assertionsDisabled || IOStatus.check(jSeek)) {
                    throw th;
                }
                throw new AssertionError();
            }
        }
    }

    @Override // java.nio.channels.FileChannel, java.nio.channels.SeekableByteChannel
    public long size() throws IOException {
        ensureOpen();
        synchronized (this.positionLock) {
            long size = -1;
            int iAdd = -1;
            try {
                begin();
                iAdd = this.threads.add();
                if (isOpen()) {
                    do {
                        size = this.nd.size(this.fd);
                        if (size != -3) {
                            break;
                        }
                    } while (isOpen());
                    long jNormalize = IOStatus.normalize(size);
                    this.threads.remove(iAdd);
                    end(size > -1);
                    if ($assertionsDisabled || IOStatus.check(size)) {
                        return jNormalize;
                    }
                    throw new AssertionError();
                }
                this.threads.remove(iAdd);
                end(-1 > -1);
                if ($assertionsDisabled || IOStatus.check(-1L)) {
                    return -1L;
                }
                throw new AssertionError();
            } catch (Throwable th) {
                this.threads.remove(iAdd);
                end(size > -1);
                if ($assertionsDisabled || IOStatus.check(size)) {
                    throw th;
                }
                throw new AssertionError();
            }
        }
    }

    @Override // java.nio.channels.FileChannel, java.nio.channels.SeekableByteChannel
    public FileChannel truncate(long j2) throws IOException {
        long size;
        long jSeek;
        ensureOpen();
        if (j2 < 0) {
            throw new IllegalArgumentException("Negative size");
        }
        if (!this.writable) {
            throw new NonWritableChannelException();
        }
        synchronized (this.positionLock) {
            int iTruncate = -1;
            try {
                begin();
                int iAdd = this.threads.add();
                if (!isOpen()) {
                    this.threads.remove(iAdd);
                    end(-1 > -1);
                    if ($assertionsDisabled || IOStatus.check(-1)) {
                        return null;
                    }
                    throw new AssertionError();
                }
                do {
                    size = this.nd.size(this.fd);
                    if (size != -3) {
                        break;
                    }
                } while (isOpen());
                if (!isOpen()) {
                    this.threads.remove(iAdd);
                    end(-1 > -1);
                    if ($assertionsDisabled || IOStatus.check(-1)) {
                        return null;
                    }
                    throw new AssertionError();
                }
                do {
                    jSeek = this.nd.seek(this.fd, -1L);
                    if (jSeek != -3) {
                        break;
                    }
                } while (isOpen());
                if (!isOpen()) {
                    this.threads.remove(iAdd);
                    end(-1 > -1);
                    if ($assertionsDisabled || IOStatus.check(-1)) {
                        return null;
                    }
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && jSeek < 0) {
                    throw new AssertionError();
                }
                if (j2 < size) {
                    do {
                        iTruncate = this.nd.truncate(this.fd, j2);
                        if (iTruncate != -3) {
                            break;
                        }
                    } while (isOpen());
                    if (!isOpen()) {
                        this.threads.remove(iAdd);
                        end(iTruncate > -1);
                        if ($assertionsDisabled || IOStatus.check(iTruncate)) {
                            return null;
                        }
                        throw new AssertionError();
                    }
                }
                if (jSeek > j2) {
                    jSeek = j2;
                }
                while (this.nd.seek(this.fd, jSeek) == -3 && isOpen()) {
                }
                this.threads.remove(iAdd);
                end(iTruncate > -1);
                if ($assertionsDisabled || IOStatus.check(iTruncate)) {
                    return this;
                }
                throw new AssertionError();
            } catch (Throwable th) {
                this.threads.remove(-1);
                end(-1 > -1);
                if ($assertionsDisabled || IOStatus.check(-1)) {
                    throw th;
                }
                throw new AssertionError();
            }
        }
    }

    @Override // java.nio.channels.FileChannel
    public void force(boolean z2) throws IOException {
        ensureOpen();
        int iForce = -1;
        int iAdd = -1;
        try {
            begin();
            iAdd = this.threads.add();
            if (!isOpen()) {
                this.threads.remove(iAdd);
                end(-1 > -1);
                if (!$assertionsDisabled && !IOStatus.check(-1)) {
                    throw new AssertionError();
                }
                return;
            }
            do {
                iForce = this.nd.force(this.fd, z2);
                if (iForce != -3) {
                    break;
                }
            } while (isOpen());
            this.threads.remove(iAdd);
            end(iForce > -1);
            if (!$assertionsDisabled && !IOStatus.check(iForce)) {
                throw new AssertionError();
            }
        } catch (Throwable th) {
            this.threads.remove(iAdd);
            end(iForce > -1);
            if (!$assertionsDisabled && !IOStatus.check(iForce)) {
                throw new AssertionError();
            }
            throw th;
        }
    }

    private long transferToDirectlyInternal(long j2, int i2, WritableByteChannel writableByteChannel, FileDescriptor fileDescriptor) throws IOException {
        long jTransferTo0;
        if (!$assertionsDisabled && this.nd.transferToDirectlyNeedsPositionLock() && !Thread.holdsLock(this.positionLock)) {
            throw new AssertionError();
        }
        try {
            begin();
            int iAdd = this.threads.add();
            if (!isOpen()) {
                this.threads.remove(iAdd);
                end(-1 > -1);
                return -1L;
            }
            do {
                jTransferTo0 = transferTo0(this.fd, j2, i2, fileDescriptor);
                if (jTransferTo0 != -3) {
                    break;
                }
            } while (isOpen());
            if (jTransferTo0 == -6) {
                if (writableByteChannel instanceof SinkChannelImpl) {
                    pipeSupported = false;
                }
                if (writableByteChannel instanceof FileChannelImpl) {
                    fileSupported = false;
                }
                this.threads.remove(iAdd);
                end(jTransferTo0 > -1);
                return -6L;
            }
            if (jTransferTo0 == -4) {
                transferSupported = false;
                this.threads.remove(iAdd);
                end(jTransferTo0 > -1);
                return -4L;
            }
            long jNormalize = IOStatus.normalize(jTransferTo0);
            this.threads.remove(iAdd);
            end(jTransferTo0 > -1);
            return jNormalize;
        } catch (Throwable th) {
            this.threads.remove(-1);
            end(-1 > -1);
            throw th;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private long transferToDirectly(long j2, int i2, WritableByteChannel writableByteChannel) throws IOException {
        long jTransferToDirectlyInternal;
        if (!transferSupported) {
            return -4L;
        }
        FileDescriptor fd = null;
        if (writableByteChannel instanceof FileChannelImpl) {
            if (!fileSupported) {
                return -6L;
            }
            fd = ((FileChannelImpl) writableByteChannel).fd;
        } else if (writableByteChannel instanceof SelChImpl) {
            if ((writableByteChannel instanceof SinkChannelImpl) && !pipeSupported) {
                return -6L;
            }
            if (!this.nd.canTransferToDirectly((SelectableChannel) writableByteChannel)) {
                return -6L;
            }
            fd = ((SelChImpl) writableByteChannel).getFD();
        }
        if (fd == null || IOUtil.fdVal(this.fd) == IOUtil.fdVal(fd)) {
            return -4L;
        }
        if (this.nd.transferToDirectlyNeedsPositionLock()) {
            synchronized (this.positionLock) {
                long jPosition = position();
                try {
                    jTransferToDirectlyInternal = transferToDirectlyInternal(j2, i2, writableByteChannel, fd);
                    position(jPosition);
                } catch (Throwable th) {
                    position(jPosition);
                    throw th;
                }
            }
            return jTransferToDirectlyInternal;
        }
        return transferToDirectlyInternal(j2, i2, writableByteChannel, fd);
    }

    /* JADX WARN: Code restructure failed: missing block: B:56:0x00dc, code lost:
    
        return r10 - r14;
     */
    /* JADX WARN: Finally extract failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private long transferToTrustedChannel(long r8, long r10, java.nio.channels.WritableByteChannel r12) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 221
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.ch.FileChannelImpl.transferToTrustedChannel(long, long, java.nio.channels.WritableByteChannel):long");
    }

    private long transferToArbitraryChannel(long j2, int i2, WritableByteChannel writableByteChannel) throws IOException {
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(Math.min(i2, 8192));
        long j3 = 0;
        long j4 = j2;
        while (j3 < i2) {
            try {
                byteBufferAllocate.limit(Math.min((int) (i2 - j3), 8192));
                int i3 = read(byteBufferAllocate, j4);
                if (i3 <= 0) {
                    break;
                }
                byteBufferAllocate.flip();
                int iWrite = writableByteChannel.write(byteBufferAllocate);
                j3 += iWrite;
                if (iWrite != i3) {
                    break;
                }
                j4 += iWrite;
                byteBufferAllocate.clear();
            } catch (IOException e2) {
                if (j3 > 0) {
                    return j3;
                }
                throw e2;
            }
        }
        return j3;
    }

    @Override // java.nio.channels.FileChannel
    public long transferTo(long j2, long j3, WritableByteChannel writableByteChannel) throws IOException {
        ensureOpen();
        if (!writableByteChannel.isOpen()) {
            throw new ClosedChannelException();
        }
        if (!this.readable) {
            throw new NonReadableChannelException();
        }
        if ((writableByteChannel instanceof FileChannelImpl) && !((FileChannelImpl) writableByteChannel).writable) {
            throw new NonWritableChannelException();
        }
        if (j2 < 0 || j3 < 0) {
            throw new IllegalArgumentException();
        }
        long size = size();
        if (j2 > size) {
            return 0L;
        }
        int iMin = (int) Math.min(j3, 2147483647L);
        if (size - j2 < iMin) {
            iMin = (int) (size - j2);
        }
        int i2 = iMin;
        long jTransferToDirectly = transferToDirectly(j2, i2, writableByteChannel);
        if (i2 >= 0) {
            return jTransferToDirectly;
        }
        long j4 = iMin;
        long jTransferToTrustedChannel = transferToTrustedChannel(j2, j4, writableByteChannel);
        if (j4 >= 0) {
            return jTransferToTrustedChannel;
        }
        return transferToArbitraryChannel(j2, iMin, writableByteChannel);
    }

    /* JADX WARN: Finally extract failed */
    private long transferFromFileChannel(FileChannelImpl fileChannelImpl, long j2, long j3) throws IOException {
        long j4;
        if (!fileChannelImpl.readable) {
            throw new NonReadableChannelException();
        }
        synchronized (fileChannelImpl.positionLock) {
            long jPosition = fileChannelImpl.position();
            long jMin = Math.min(j3, fileChannelImpl.size() - jPosition);
            long j5 = jMin;
            long j6 = jPosition;
            while (j5 > 0) {
                MappedByteBuffer map = fileChannelImpl.map(FileChannel.MapMode.READ_ONLY, j6, Math.min(j5, 8388608L));
                try {
                    try {
                        long jWrite = write(map, j2);
                        if (!$assertionsDisabled && jWrite <= 0) {
                            throw new AssertionError();
                        }
                        j6 += jWrite;
                        j2 += jWrite;
                        j5 -= jWrite;
                        unmap(map);
                    } catch (IOException e2) {
                        if (j5 == jMin) {
                            throw e2;
                        }
                        unmap(map);
                    }
                } catch (Throwable th) {
                    unmap(map);
                    throw th;
                }
            }
            j4 = jMin - j5;
            fileChannelImpl.position(jPosition + j4);
        }
        return j4;
    }

    private long transferFromArbitraryChannel(ReadableByteChannel readableByteChannel, long j2, long j3) throws IOException {
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate((int) Math.min(j3, 8192L));
        long j4 = 0;
        long j5 = j2;
        while (j4 < j3) {
            try {
                byteBufferAllocate.limit((int) Math.min(j3 - j4, 8192L));
                int i2 = readableByteChannel.read(byteBufferAllocate);
                if (i2 <= 0) {
                    break;
                }
                byteBufferAllocate.flip();
                int iWrite = write(byteBufferAllocate, j5);
                j4 += iWrite;
                if (iWrite != i2) {
                    break;
                }
                j5 += iWrite;
                byteBufferAllocate.clear();
            } catch (IOException e2) {
                if (j4 > 0) {
                    return j4;
                }
                throw e2;
            }
        }
        return j4;
    }

    @Override // java.nio.channels.FileChannel
    public long transferFrom(ReadableByteChannel readableByteChannel, long j2, long j3) throws IOException {
        ensureOpen();
        if (!readableByteChannel.isOpen()) {
            throw new ClosedChannelException();
        }
        if (!this.writable) {
            throw new NonWritableChannelException();
        }
        if (j2 < 0 || j3 < 0) {
            throw new IllegalArgumentException();
        }
        if (j2 > size()) {
            return 0L;
        }
        if (readableByteChannel instanceof FileChannelImpl) {
            return transferFromFileChannel((FileChannelImpl) readableByteChannel, j2, j3);
        }
        return transferFromArbitraryChannel(readableByteChannel, j2, j3);
    }

    @Override // java.nio.channels.FileChannel
    public int read(ByteBuffer byteBuffer, long j2) throws IOException {
        int internal;
        if (byteBuffer == null) {
            throw new NullPointerException();
        }
        if (j2 < 0) {
            throw new IllegalArgumentException("Negative position");
        }
        if (!this.readable) {
            throw new NonReadableChannelException();
        }
        ensureOpen();
        if (this.nd.needsPositionLock()) {
            synchronized (this.positionLock) {
                internal = readInternal(byteBuffer, j2);
            }
            return internal;
        }
        return readInternal(byteBuffer, j2);
    }

    private int readInternal(ByteBuffer byteBuffer, long j2) throws IOException {
        if (!$assertionsDisabled && this.nd.needsPositionLock() && !Thread.holdsLock(this.positionLock)) {
            throw new AssertionError();
        }
        int i2 = 0;
        int iAdd = -1;
        try {
            begin();
            iAdd = this.threads.add();
            if (isOpen()) {
                do {
                    i2 = IOUtil.read(this.fd, byteBuffer, j2, this.nd);
                    if (i2 != -3) {
                        break;
                    }
                } while (isOpen());
                int iNormalize = IOStatus.normalize(i2);
                this.threads.remove(iAdd);
                end(i2 > 0);
                if ($assertionsDisabled || IOStatus.check(i2)) {
                    return iNormalize;
                }
                throw new AssertionError();
            }
            this.threads.remove(iAdd);
            end(0 > 0);
            if ($assertionsDisabled || IOStatus.check(0)) {
                return -1;
            }
            throw new AssertionError();
        } catch (Throwable th) {
            this.threads.remove(iAdd);
            end(i2 > 0);
            if ($assertionsDisabled || IOStatus.check(i2)) {
                throw th;
            }
            throw new AssertionError();
        }
    }

    @Override // java.nio.channels.FileChannel
    public int write(ByteBuffer byteBuffer, long j2) throws IOException {
        int iWriteInternal;
        if (byteBuffer == null) {
            throw new NullPointerException();
        }
        if (j2 < 0) {
            throw new IllegalArgumentException("Negative position");
        }
        if (!this.writable) {
            throw new NonWritableChannelException();
        }
        ensureOpen();
        if (this.nd.needsPositionLock()) {
            synchronized (this.positionLock) {
                iWriteInternal = writeInternal(byteBuffer, j2);
            }
            return iWriteInternal;
        }
        return writeInternal(byteBuffer, j2);
    }

    private int writeInternal(ByteBuffer byteBuffer, long j2) throws IOException {
        if (!$assertionsDisabled && this.nd.needsPositionLock() && !Thread.holdsLock(this.positionLock)) {
            throw new AssertionError();
        }
        int iWrite = 0;
        int iAdd = -1;
        try {
            begin();
            iAdd = this.threads.add();
            if (isOpen()) {
                do {
                    iWrite = IOUtil.write(this.fd, byteBuffer, j2, this.nd);
                    if (iWrite != -3) {
                        break;
                    }
                } while (isOpen());
                int iNormalize = IOStatus.normalize(iWrite);
                this.threads.remove(iAdd);
                end(iWrite > 0);
                if ($assertionsDisabled || IOStatus.check(iWrite)) {
                    return iNormalize;
                }
                throw new AssertionError();
            }
            this.threads.remove(iAdd);
            end(0 > 0);
            if ($assertionsDisabled || IOStatus.check(0)) {
                return -1;
            }
            throw new AssertionError();
        } catch (Throwable th) {
            this.threads.remove(iAdd);
            end(iWrite > 0);
            if ($assertionsDisabled || IOStatus.check(iWrite)) {
                throw th;
            }
            throw new AssertionError();
        }
    }

    /* loaded from: rt.jar:sun/nio/ch/FileChannelImpl$Unmapper.class */
    private static class Unmapper implements Runnable {
        private static final NativeDispatcher nd;
        static volatile int count;
        static volatile long totalSize;
        static volatile long totalCapacity;
        private volatile long address;
        private final long size;
        private final int cap;
        private final FileDescriptor fd;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !FileChannelImpl.class.desiredAssertionStatus();
            nd = new FileDispatcherImpl();
        }

        private Unmapper(long j2, long j3, int i2, FileDescriptor fileDescriptor) {
            if (!$assertionsDisabled && j2 == 0) {
                throw new AssertionError();
            }
            this.address = j2;
            this.size = j3;
            this.cap = i2;
            this.fd = fileDescriptor;
            synchronized (Unmapper.class) {
                count++;
                totalSize += j3;
                totalCapacity += i2;
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.address != 0) {
                FileChannelImpl.unmap0(this.address, this.size);
                this.address = 0L;
                if (this.fd.valid()) {
                    try {
                        nd.close(this.fd);
                    } catch (IOException e2) {
                    }
                }
                synchronized (Unmapper.class) {
                    count--;
                    totalSize -= this.size;
                    totalCapacity -= this.cap;
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static void unmap(MappedByteBuffer mappedByteBuffer) {
        Cleaner cleaner = ((DirectBuffer) mappedByteBuffer).cleaner();
        if (cleaner != null) {
            cleaner.clean();
        }
    }

    /*  JADX ERROR: NullPointerException in pass: AttachTryCatchVisitor
        java.lang.NullPointerException: Cannot invoke "String.charAt(int)" because "obj" is null
        	at jadx.core.utils.Utils.cleanObjectName(Utils.java:41)
        	at jadx.core.dex.instructions.args.ArgType.object(ArgType.java:88)
        	at jadx.core.dex.info.ClassInfo.fromName(ClassInfo.java:42)
        	at jadx.core.dex.visitors.AttachTryCatchVisitor.convertToHandlers(AttachTryCatchVisitor.java:113)
        	at jadx.core.dex.visitors.AttachTryCatchVisitor.initTryCatches(AttachTryCatchVisitor.java:54)
        	at jadx.core.dex.visitors.AttachTryCatchVisitor.visit(AttachTryCatchVisitor.java:42)
        */
    @Override // java.nio.channels.FileChannel
    public java.nio.MappedByteBuffer map(java.nio.channels.FileChannel.MapMode r11, long r12, long r14) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 811
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.ch.FileChannelImpl.map(java.nio.channels.FileChannel$MapMode, long, long):java.nio.MappedByteBuffer");
    }

    public static JavaNioAccess.BufferPool getMappedBufferPool() {
        return new JavaNioAccess.BufferPool() { // from class: sun.nio.ch.FileChannelImpl.1
            @Override // sun.misc.JavaNioAccess.BufferPool
            public String getName() {
                return "mapped";
            }

            @Override // sun.misc.JavaNioAccess.BufferPool
            public long getCount() {
                return Unmapper.count;
            }

            @Override // sun.misc.JavaNioAccess.BufferPool
            public long getTotalCapacity() {
                return Unmapper.totalCapacity;
            }

            @Override // sun.misc.JavaNioAccess.BufferPool
            public long getMemoryUsed() {
                return Unmapper.totalSize;
            }
        };
    }

    private static boolean isSharedFileLockTable() {
        if (!propertyChecked) {
            synchronized (FileChannelImpl.class) {
                if (!propertyChecked) {
                    String str = (String) AccessController.doPrivileged(new GetPropertyAction("sun.nio.ch.disableSystemWideOverlappingFileLockCheck"));
                    isSharedFileLockTable = str == null || str.equals("false");
                    propertyChecked = true;
                }
            }
        }
        return isSharedFileLockTable;
    }

    private FileLockTable fileLockTable() throws IOException {
        if (this.fileLockTable == null) {
            synchronized (this) {
                if (this.fileLockTable == null) {
                    if (isSharedFileLockTable()) {
                        int iAdd = this.threads.add();
                        try {
                            ensureOpen();
                            this.fileLockTable = FileLockTable.newSharedFileLockTable(this, this.fd);
                            this.threads.remove(iAdd);
                        } catch (Throwable th) {
                            this.threads.remove(iAdd);
                            throw th;
                        }
                    } else {
                        this.fileLockTable = new SimpleFileLockTable();
                    }
                }
            }
        }
        return this.fileLockTable;
    }

    @Override // java.nio.channels.FileChannel
    public FileLock lock(long j2, long j3, boolean z2) throws OverlappingFileLockException, IOException {
        int iLock;
        ensureOpen();
        if (z2 && !this.readable) {
            throw new NonReadableChannelException();
        }
        if (!z2 && !this.writable) {
            throw new NonWritableChannelException();
        }
        FileLockImpl fileLockImpl = new FileLockImpl(this, j2, j3, z2);
        FileLockTable fileLockTable = fileLockTable();
        fileLockTable.add(fileLockImpl);
        boolean z3 = false;
        try {
            begin();
            int iAdd = this.threads.add();
            if (isOpen()) {
                do {
                    iLock = this.nd.lock(this.fd, true, j2, j3, z2);
                    if (iLock != 2) {
                        break;
                    }
                } while (isOpen());
                if (isOpen()) {
                    if (iLock == 1) {
                        if (!$assertionsDisabled && !z2) {
                            throw new AssertionError();
                        }
                        FileLockImpl fileLockImpl2 = new FileLockImpl((FileChannel) this, j2, j3, false);
                        fileLockTable.replace(fileLockImpl, fileLockImpl2);
                        fileLockImpl = fileLockImpl2;
                    }
                    z3 = true;
                }
                if (!z3) {
                    fileLockTable.remove(fileLockImpl);
                }
                this.threads.remove(iAdd);
                try {
                    end(z3);
                    return fileLockImpl;
                } catch (ClosedByInterruptException e2) {
                    throw new FileLockInterruptionException();
                }
            }
            if (0 == 0) {
                fileLockTable.remove(fileLockImpl);
            }
            this.threads.remove(iAdd);
            try {
                end(false);
                return null;
            } catch (ClosedByInterruptException e3) {
                throw new FileLockInterruptionException();
            }
        } catch (Throwable th) {
            if (0 == 0) {
                fileLockTable.remove(fileLockImpl);
            }
            this.threads.remove(-1);
            try {
                end(false);
                throw th;
            } catch (ClosedByInterruptException e4) {
                throw new FileLockInterruptionException();
            }
        }
    }

    @Override // java.nio.channels.FileChannel
    public FileLock tryLock(long j2, long j3, boolean z2) throws OverlappingFileLockException, IOException {
        ensureOpen();
        if (z2 && !this.readable) {
            throw new NonReadableChannelException();
        }
        if (!z2 && !this.writable) {
            throw new NonWritableChannelException();
        }
        FileLockImpl fileLockImpl = new FileLockImpl(this, j2, j3, z2);
        FileLockTable fileLockTable = fileLockTable();
        fileLockTable.add(fileLockImpl);
        int iAdd = this.threads.add();
        try {
            try {
                ensureOpen();
                int iLock = this.nd.lock(this.fd, false, j2, j3, z2);
                if (iLock == -1) {
                    fileLockTable.remove(fileLockImpl);
                    this.threads.remove(iAdd);
                    return null;
                }
                if (iLock != 1) {
                    return fileLockImpl;
                }
                if (!$assertionsDisabled && !z2) {
                    throw new AssertionError();
                }
                FileLockImpl fileLockImpl2 = new FileLockImpl((FileChannel) this, j2, j3, false);
                fileLockTable.replace(fileLockImpl, fileLockImpl2);
                this.threads.remove(iAdd);
                return fileLockImpl2;
            } catch (IOException e2) {
                fileLockTable.remove(fileLockImpl);
                throw e2;
            }
        } finally {
            this.threads.remove(iAdd);
        }
    }

    void release(FileLockImpl fileLockImpl) throws IOException {
        int iAdd = this.threads.add();
        try {
            ensureOpen();
            this.nd.release(this.fd, fileLockImpl.position(), fileLockImpl.size());
            if (!$assertionsDisabled && this.fileLockTable == null) {
                throw new AssertionError();
            }
            this.fileLockTable.remove(fileLockImpl);
        } finally {
            this.threads.remove(iAdd);
        }
    }

    /* loaded from: rt.jar:sun/nio/ch/FileChannelImpl$SimpleFileLockTable.class */
    private static class SimpleFileLockTable extends FileLockTable {
        private final List<FileLock> lockList = new ArrayList(2);
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !FileChannelImpl.class.desiredAssertionStatus();
        }

        private void checkList(long j2, long j3) throws OverlappingFileLockException {
            if (!$assertionsDisabled && !Thread.holdsLock(this.lockList)) {
                throw new AssertionError();
            }
            Iterator<FileLock> it = this.lockList.iterator();
            while (it.hasNext()) {
                if (it.next().overlaps(j2, j3)) {
                    throw new OverlappingFileLockException();
                }
            }
        }

        @Override // sun.nio.ch.FileLockTable
        public void add(FileLock fileLock) throws OverlappingFileLockException {
            synchronized (this.lockList) {
                checkList(fileLock.position(), fileLock.size());
                this.lockList.add(fileLock);
            }
        }

        @Override // sun.nio.ch.FileLockTable
        public void remove(FileLock fileLock) {
            synchronized (this.lockList) {
                this.lockList.remove(fileLock);
            }
        }

        @Override // sun.nio.ch.FileLockTable
        public List<FileLock> removeAll() {
            ArrayList arrayList;
            synchronized (this.lockList) {
                arrayList = new ArrayList(this.lockList);
                this.lockList.clear();
            }
            return arrayList;
        }

        @Override // sun.nio.ch.FileLockTable
        public void replace(FileLock fileLock, FileLock fileLock2) {
            synchronized (this.lockList) {
                this.lockList.remove(fileLock);
                this.lockList.add(fileLock2);
            }
        }
    }
}
