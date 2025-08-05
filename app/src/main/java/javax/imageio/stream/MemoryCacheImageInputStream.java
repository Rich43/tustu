package javax.imageio.stream;

import com.sun.imageio.stream.StreamFinalizer;
import java.io.IOException;
import java.io.InputStream;
import sun.java2d.Disposer;
import sun.java2d.DisposerRecord;

/* loaded from: rt.jar:javax/imageio/stream/MemoryCacheImageInputStream.class */
public class MemoryCacheImageInputStream extends ImageInputStreamImpl {
    private InputStream stream;
    private MemoryCache cache = new MemoryCache();
    private final Object disposerReferent;
    private final DisposerRecord disposerRecord;

    public MemoryCacheImageInputStream(InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("stream == null!");
        }
        this.stream = inputStream;
        this.disposerRecord = new StreamDisposerRecord(this.cache);
        if (getClass() == MemoryCacheImageInputStream.class) {
            this.disposerReferent = new Object();
            Disposer.addRecord(this.disposerReferent, this.disposerRecord);
        } else {
            this.disposerReferent = new StreamFinalizer(this);
        }
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl, javax.imageio.stream.ImageInputStream
    public int read() throws IOException {
        checkClosed();
        this.bitOffset = 0;
        if (this.cache.loadFromStream(this.stream, this.streamPos + 1) >= this.streamPos + 1) {
            MemoryCache memoryCache = this.cache;
            long j2 = this.streamPos;
            this.streamPos = j2 + 1;
            return memoryCache.read(j2);
        }
        return -1;
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl, javax.imageio.stream.ImageInputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        checkClosed();
        if (bArr == null) {
            throw new NullPointerException("b == null!");
        }
        if (i2 < 0 || i3 < 0 || i2 + i3 > bArr.length || i2 + i3 < 0) {
            throw new IndexOutOfBoundsException("off < 0 || len < 0 || off+len > b.length || off+len < 0!");
        }
        this.bitOffset = 0;
        if (i3 == 0) {
            return 0;
        }
        int iLoadFromStream = (int) (this.cache.loadFromStream(this.stream, this.streamPos + i3) - this.streamPos);
        if (iLoadFromStream > 0) {
            this.cache.read(bArr, i2, iLoadFromStream, this.streamPos);
            this.streamPos += iLoadFromStream;
            return iLoadFromStream;
        }
        return -1;
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl, javax.imageio.stream.ImageInputStream, javax.imageio.stream.ImageOutputStream
    public void flushBefore(long j2) throws IOException {
        super.flushBefore(j2);
        this.cache.disposeBefore(j2);
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl, javax.imageio.stream.ImageInputStream
    public boolean isCached() {
        return true;
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl, javax.imageio.stream.ImageInputStream
    public boolean isCachedFile() {
        return false;
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl, javax.imageio.stream.ImageInputStream
    public boolean isCachedMemory() {
        return true;
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl, javax.imageio.stream.ImageInputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        super.close();
        this.disposerRecord.dispose();
        this.stream = null;
        this.cache = null;
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl
    protected void finalize() throws Throwable {
    }

    /* loaded from: rt.jar:javax/imageio/stream/MemoryCacheImageInputStream$StreamDisposerRecord.class */
    private static class StreamDisposerRecord implements DisposerRecord {
        private MemoryCache cache;

        public StreamDisposerRecord(MemoryCache memoryCache) {
            this.cache = memoryCache;
        }

        @Override // sun.java2d.DisposerRecord
        public synchronized void dispose() {
            if (this.cache != null) {
                this.cache.reset();
                this.cache = null;
            }
        }
    }
}
