package java.nio.channels;

import java.io.IOException;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:java/nio/channels/FileLock.class */
public abstract class FileLock implements AutoCloseable {
    private final Channel channel;
    private final long position;
    private final long size;
    private final boolean shared;

    public abstract boolean isValid();

    public abstract void release() throws IOException;

    protected FileLock(FileChannel fileChannel, long j2, long j3, boolean z2) {
        if (j2 < 0) {
            throw new IllegalArgumentException("Negative position");
        }
        if (j3 < 0) {
            throw new IllegalArgumentException("Negative size");
        }
        if (j2 + j3 < 0) {
            throw new IllegalArgumentException("Negative position + size");
        }
        this.channel = fileChannel;
        this.position = j2;
        this.size = j3;
        this.shared = z2;
    }

    protected FileLock(AsynchronousFileChannel asynchronousFileChannel, long j2, long j3, boolean z2) {
        if (j2 < 0) {
            throw new IllegalArgumentException("Negative position");
        }
        if (j3 < 0) {
            throw new IllegalArgumentException("Negative size");
        }
        if (j2 + j3 < 0) {
            throw new IllegalArgumentException("Negative position + size");
        }
        this.channel = asynchronousFileChannel;
        this.position = j2;
        this.size = j3;
        this.shared = z2;
    }

    public final FileChannel channel() {
        if (this.channel instanceof FileChannel) {
            return (FileChannel) this.channel;
        }
        return null;
    }

    public Channel acquiredBy() {
        return this.channel;
    }

    public final long position() {
        return this.position;
    }

    public final long size() {
        return this.size;
    }

    public final boolean isShared() {
        return this.shared;
    }

    public final boolean overlaps(long j2, long j3) {
        if (j2 + j3 <= this.position || this.position + this.size <= j2) {
            return false;
        }
        return true;
    }

    @Override // java.lang.AutoCloseable
    public final void close() throws IOException {
        release();
    }

    public final String toString() {
        return getClass().getName() + "[" + this.position + CallSiteDescriptor.TOKEN_DELIMITER + this.size + " " + (this.shared ? "shared" : "exclusive") + " " + (isValid() ? "valid" : "invalid") + "]";
    }
}
