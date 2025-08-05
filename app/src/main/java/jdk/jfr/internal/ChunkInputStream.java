package jdk.jfr.internal;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: jfr.jar:jdk/jfr/internal/ChunkInputStream.class */
final class ChunkInputStream extends InputStream {
    private final Iterator<RepositoryChunk> chunks;
    private RepositoryChunk currentChunk;
    private InputStream stream;

    ChunkInputStream(List<RepositoryChunk> list) throws IOException {
        ArrayList arrayList = new ArrayList(list.size());
        for (RepositoryChunk repositoryChunk : list) {
            repositoryChunk.use();
            arrayList.add(repositoryChunk);
        }
        this.chunks = arrayList.iterator();
        nextStream();
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        if (this.stream != null) {
            return this.stream.available();
        }
        return 0;
    }

    private boolean nextStream() throws IOException {
        if (!nextChunk()) {
            return false;
        }
        this.stream = new BufferedInputStream(SecuritySupport.newFileInputStream(this.currentChunk.getFile()));
        return true;
    }

    private boolean nextChunk() {
        if (!this.chunks.hasNext()) {
            return false;
        }
        this.currentChunk = this.chunks.next();
        return true;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        do {
            if (this.stream != null) {
                int i2 = this.stream.read();
                if (i2 != -1) {
                    return i2;
                }
                this.stream.close();
                this.currentChunk.release();
                this.stream = null;
                this.currentChunk = null;
            }
        } while (nextStream());
        return -1;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.stream != null) {
            this.stream.close();
            this.stream = null;
        }
        while (this.currentChunk != null) {
            this.currentChunk.release();
            this.currentChunk = null;
            if (!nextChunk()) {
                return;
            }
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        close();
    }
}
