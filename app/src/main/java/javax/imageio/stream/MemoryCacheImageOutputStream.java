package javax.imageio.stream;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: rt.jar:javax/imageio/stream/MemoryCacheImageOutputStream.class */
public class MemoryCacheImageOutputStream extends ImageOutputStreamImpl {
    private OutputStream stream;
    private MemoryCache cache = new MemoryCache();

    public MemoryCacheImageOutputStream(OutputStream outputStream) {
        if (outputStream == null) {
            throw new IllegalArgumentException("stream == null!");
        }
        this.stream = outputStream;
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl, javax.imageio.stream.ImageInputStream
    public int read() throws IOException {
        checkClosed();
        this.bitOffset = 0;
        int i2 = this.cache.read(this.streamPos);
        if (i2 != -1) {
            this.streamPos++;
        }
        return i2;
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
        long length = this.cache.getLength() - this.streamPos;
        if (length <= 0) {
            return -1;
        }
        int iMin = (int) Math.min(length, i3);
        this.cache.read(bArr, i2, iMin, this.streamPos);
        this.streamPos += iMin;
        return iMin;
    }

    @Override // javax.imageio.stream.ImageOutputStreamImpl, javax.imageio.stream.ImageOutputStream, java.io.DataOutput
    public void write(int i2) throws IOException {
        flushBits();
        this.cache.write(i2, this.streamPos);
        this.streamPos++;
    }

    @Override // javax.imageio.stream.ImageOutputStreamImpl, javax.imageio.stream.ImageOutputStream, java.io.DataOutput
    public void write(byte[] bArr, int i2, int i3) throws IOException {
        flushBits();
        this.cache.write(bArr, i2, i3, this.streamPos);
        this.streamPos += i3;
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl, javax.imageio.stream.ImageInputStream
    public long length() {
        try {
            checkClosed();
            return this.cache.getLength();
        } catch (IOException e2) {
            return -1L;
        }
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
        long length = this.cache.getLength();
        seek(length);
        flushBefore(length);
        super.close();
        this.cache.reset();
        this.cache = null;
        this.stream = null;
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl, javax.imageio.stream.ImageInputStream, javax.imageio.stream.ImageOutputStream
    public void flushBefore(long j2) throws IOException {
        long j3 = this.flushedPos;
        super.flushBefore(j2);
        this.cache.writeToStream(this.stream, j3, this.flushedPos - j3);
        this.cache.disposeBefore(this.flushedPos);
        this.stream.flush();
    }
}
