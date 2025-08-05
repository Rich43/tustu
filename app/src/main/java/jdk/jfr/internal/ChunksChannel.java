package jdk.jfr.internal;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: jfr.jar:jdk/jfr/internal/ChunksChannel.class */
final class ChunksChannel implements ReadableByteChannel {
    private final Iterator<RepositoryChunk> chunks;
    private RepositoryChunk current;
    private ReadableByteChannel channel;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ChunksChannel.class.desiredAssertionStatus();
    }

    public ChunksChannel(List<RepositoryChunk> list) throws IOException {
        if (list.isEmpty()) {
            throw new FileNotFoundException("No chunks");
        }
        ArrayList arrayList = new ArrayList(list.size());
        for (RepositoryChunk repositoryChunk : list) {
            repositoryChunk.use();
            arrayList.add(repositoryChunk);
        }
        this.chunks = arrayList.iterator();
        nextChannel();
    }

    private boolean nextChunk() {
        if (!this.chunks.hasNext()) {
            return false;
        }
        this.current = this.chunks.next();
        return true;
    }

    private boolean nextChannel() throws IOException {
        if (!nextChunk()) {
            return false;
        }
        this.channel = this.current.newChannel();
        return true;
    }

    @Override // java.nio.channels.ReadableByteChannel
    public int read(ByteBuffer byteBuffer) throws IOException {
        do {
            if (this.channel != null) {
                if (!$assertionsDisabled && this.current == null) {
                    throw new AssertionError();
                }
                int i2 = this.channel.read(byteBuffer);
                if (i2 != -1) {
                    return i2;
                }
                this.channel.close();
                this.current.release();
                this.channel = null;
                this.current = null;
            }
        } while (nextChannel());
        return -1;
    }

    public long transferTo(FileChannel fileChannel) throws IOException {
        long j2 = 0;
        do {
            if (this.channel != null) {
                if (!$assertionsDisabled && this.current == null) {
                    throw new AssertionError();
                }
                long size = this.current.getSize();
                while (true) {
                    long j3 = size;
                    if (j3 > 0) {
                        long jTransferFrom = fileChannel.transferFrom(this.channel, j2, Math.min(j3, 1048576L));
                        if (jTransferFrom == 0) {
                            return fileChannel.size();
                        }
                        j2 += jTransferFrom;
                        size = j3 - jTransferFrom;
                    } else {
                        this.channel.close();
                        this.current.release();
                        this.channel = null;
                        this.current = null;
                        break;
                    }
                }
            }
        } while (nextChannel());
        return fileChannel.size();
    }

    @Override // java.nio.channels.Channel, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.channel != null) {
            this.channel.close();
            this.channel = null;
        }
        while (this.current != null) {
            this.current.release();
            this.current = null;
            if (!nextChunk()) {
                return;
            }
        }
    }

    @Override // java.nio.channels.Channel
    public boolean isOpen() {
        return this.channel != null;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        close();
    }
}
