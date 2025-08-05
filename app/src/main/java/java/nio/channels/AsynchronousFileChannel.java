package java.nio.channels;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/* loaded from: rt.jar:java/nio/channels/AsynchronousFileChannel.class */
public abstract class AsynchronousFileChannel implements AsynchronousChannel {
    private static final FileAttribute<?>[] NO_ATTRIBUTES = new FileAttribute[0];

    public abstract long size() throws IOException;

    public abstract AsynchronousFileChannel truncate(long j2) throws IOException;

    public abstract void force(boolean z2) throws IOException;

    public abstract <A> void lock(long j2, long j3, boolean z2, A a2, CompletionHandler<FileLock, ? super A> completionHandler);

    public abstract Future<FileLock> lock(long j2, long j3, boolean z2);

    public abstract FileLock tryLock(long j2, long j3, boolean z2) throws IOException;

    public abstract <A> void read(ByteBuffer byteBuffer, long j2, A a2, CompletionHandler<Integer, ? super A> completionHandler);

    public abstract Future<Integer> read(ByteBuffer byteBuffer, long j2);

    public abstract <A> void write(ByteBuffer byteBuffer, long j2, A a2, CompletionHandler<Integer, ? super A> completionHandler);

    public abstract Future<Integer> write(ByteBuffer byteBuffer, long j2);

    protected AsynchronousFileChannel() {
    }

    public static AsynchronousFileChannel open(Path path, Set<? extends OpenOption> set, ExecutorService executorService, FileAttribute<?>... fileAttributeArr) throws IOException {
        return path.getFileSystem().provider().newAsynchronousFileChannel(path, set, executorService, fileAttributeArr);
    }

    public static AsynchronousFileChannel open(Path path, OpenOption... openOptionArr) throws IOException {
        HashSet hashSet = new HashSet(openOptionArr.length);
        Collections.addAll(hashSet, openOptionArr);
        return open(path, hashSet, null, NO_ATTRIBUTES);
    }

    public final <A> void lock(A a2, CompletionHandler<FileLock, ? super A> completionHandler) {
        lock(0L, Long.MAX_VALUE, false, a2, completionHandler);
    }

    public final Future<FileLock> lock() {
        return lock(0L, Long.MAX_VALUE, false);
    }

    public final FileLock tryLock() throws IOException {
        return tryLock(0L, Long.MAX_VALUE, false);
    }
}
