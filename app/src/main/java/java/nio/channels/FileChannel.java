package java.nio.channels;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.spi.AbstractInterruptibleChannel;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/* loaded from: rt.jar:java/nio/channels/FileChannel.class */
public abstract class FileChannel extends AbstractInterruptibleChannel implements SeekableByteChannel, GatheringByteChannel, ScatteringByteChannel {
    private static final FileAttribute<?>[] NO_ATTRIBUTES = new FileAttribute[0];

    public abstract int read(ByteBuffer byteBuffer) throws IOException;

    public abstract long read(ByteBuffer[] byteBufferArr, int i2, int i3) throws IOException;

    public abstract int write(ByteBuffer byteBuffer) throws IOException;

    public abstract long write(ByteBuffer[] byteBufferArr, int i2, int i3) throws IOException;

    public abstract long position() throws IOException;

    @Override // java.nio.channels.SeekableByteChannel
    public abstract FileChannel position(long j2) throws IOException;

    public abstract long size() throws IOException;

    @Override // java.nio.channels.SeekableByteChannel
    public abstract FileChannel truncate(long j2) throws IOException;

    public abstract void force(boolean z2) throws IOException;

    public abstract long transferTo(long j2, long j3, WritableByteChannel writableByteChannel) throws IOException;

    public abstract long transferFrom(ReadableByteChannel readableByteChannel, long j2, long j3) throws IOException;

    public abstract int read(ByteBuffer byteBuffer, long j2) throws IOException;

    public abstract int write(ByteBuffer byteBuffer, long j2) throws IOException;

    public abstract MappedByteBuffer map(MapMode mapMode, long j2, long j3) throws IOException;

    public abstract FileLock lock(long j2, long j3, boolean z2) throws IOException;

    public abstract FileLock tryLock(long j2, long j3, boolean z2) throws IOException;

    protected FileChannel() {
    }

    public static FileChannel open(Path path, Set<? extends OpenOption> set, FileAttribute<?>... fileAttributeArr) throws IOException {
        return path.getFileSystem().provider().newFileChannel(path, set, fileAttributeArr);
    }

    public static FileChannel open(Path path, OpenOption... openOptionArr) throws IOException {
        HashSet hashSet = new HashSet(openOptionArr.length);
        Collections.addAll(hashSet, openOptionArr);
        return open(path, hashSet, NO_ATTRIBUTES);
    }

    @Override // java.nio.channels.ScatteringByteChannel
    public final long read(ByteBuffer[] byteBufferArr) throws IOException {
        return read(byteBufferArr, 0, byteBufferArr.length);
    }

    @Override // java.nio.channels.GatheringByteChannel
    public final long write(ByteBuffer[] byteBufferArr) throws IOException {
        return write(byteBufferArr, 0, byteBufferArr.length);
    }

    /* loaded from: rt.jar:java/nio/channels/FileChannel$MapMode.class */
    public static class MapMode {
        public static final MapMode READ_ONLY = new MapMode("READ_ONLY");
        public static final MapMode READ_WRITE = new MapMode("READ_WRITE");
        public static final MapMode PRIVATE = new MapMode("PRIVATE");
        private final String name;

        private MapMode(String str) {
            this.name = str;
        }

        public String toString() {
            return this.name;
        }
    }

    public final FileLock lock() throws IOException {
        return lock(0L, Long.MAX_VALUE, false);
    }

    public final FileLock tryLock() throws IOException {
        return tryLock(0L, Long.MAX_VALUE, false);
    }
}
