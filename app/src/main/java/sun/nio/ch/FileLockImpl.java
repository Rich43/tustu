package sun.nio.ch;

import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.Channel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/* loaded from: rt.jar:sun/nio/ch/FileLockImpl.class */
public class FileLockImpl extends FileLock {
    private volatile boolean valid;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !FileLockImpl.class.desiredAssertionStatus();
    }

    FileLockImpl(FileChannel fileChannel, long j2, long j3, boolean z2) {
        super(fileChannel, j2, j3, z2);
        this.valid = true;
    }

    FileLockImpl(AsynchronousFileChannel asynchronousFileChannel, long j2, long j3, boolean z2) {
        super(asynchronousFileChannel, j2, j3, z2);
        this.valid = true;
    }

    @Override // java.nio.channels.FileLock
    public boolean isValid() {
        return this.valid;
    }

    void invalidate() {
        if (!$assertionsDisabled && !Thread.holdsLock(this)) {
            throw new AssertionError();
        }
        this.valid = false;
    }

    @Override // java.nio.channels.FileLock
    public synchronized void release() throws IOException {
        Channel channelAcquiredBy = acquiredBy();
        if (!channelAcquiredBy.isOpen()) {
            throw new ClosedChannelException();
        }
        if (this.valid) {
            if (channelAcquiredBy instanceof FileChannelImpl) {
                ((FileChannelImpl) channelAcquiredBy).release(this);
            } else if (channelAcquiredBy instanceof AsynchronousFileChannelImpl) {
                ((AsynchronousFileChannelImpl) channelAcquiredBy).release(this);
            } else {
                throw new AssertionError();
            }
            this.valid = false;
        }
    }
}
