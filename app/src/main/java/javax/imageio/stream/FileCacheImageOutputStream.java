package javax.imageio.stream;

import com.sun.imageio.stream.StreamCloser;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import net.lingala.zip4j.util.InternalZipConstants;

/* loaded from: rt.jar:javax/imageio/stream/FileCacheImageOutputStream.class */
public class FileCacheImageOutputStream extends ImageOutputStreamImpl {
    private OutputStream stream;
    private File cacheFile;
    private RandomAccessFile cache;
    private long maxStreamPos = 0;
    private final StreamCloser.CloseAction closeAction;

    public FileCacheImageOutputStream(OutputStream outputStream, File file) throws IOException {
        if (outputStream == null) {
            throw new IllegalArgumentException("stream == null!");
        }
        if (file != null && !file.isDirectory()) {
            throw new IllegalArgumentException("Not a directory!");
        }
        this.stream = outputStream;
        if (file == null) {
            this.cacheFile = Files.createTempFile("imageio", ".tmp", new FileAttribute[0]).toFile();
        } else {
            this.cacheFile = Files.createTempFile(file.toPath(), "imageio", ".tmp", new FileAttribute[0]).toFile();
        }
        this.cache = new RandomAccessFile(this.cacheFile, InternalZipConstants.WRITE_MODE);
        this.closeAction = StreamCloser.createCloseAction(this);
        StreamCloser.addToQueue(this.closeAction);
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl, javax.imageio.stream.ImageInputStream
    public int read() throws IOException {
        checkClosed();
        this.bitOffset = 0;
        int i2 = this.cache.read();
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
        int i4 = this.cache.read(bArr, i2, i3);
        if (i4 != -1) {
            this.streamPos += i4;
        }
        return i4;
    }

    @Override // javax.imageio.stream.ImageOutputStreamImpl, javax.imageio.stream.ImageOutputStream, java.io.DataOutput
    public void write(int i2) throws IOException {
        flushBits();
        this.cache.write(i2);
        this.streamPos++;
        this.maxStreamPos = Math.max(this.maxStreamPos, this.streamPos);
    }

    @Override // javax.imageio.stream.ImageOutputStreamImpl, javax.imageio.stream.ImageOutputStream, java.io.DataOutput
    public void write(byte[] bArr, int i2, int i3) throws IOException {
        flushBits();
        this.cache.write(bArr, i2, i3);
        this.streamPos += i3;
        this.maxStreamPos = Math.max(this.maxStreamPos, this.streamPos);
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl, javax.imageio.stream.ImageInputStream
    public long length() {
        try {
            checkClosed();
            return this.cache.length();
        } catch (IOException e2) {
            return -1L;
        }
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl, javax.imageio.stream.ImageInputStream
    public void seek(long j2) throws IOException {
        checkClosed();
        if (j2 < this.flushedPos) {
            throw new IndexOutOfBoundsException();
        }
        this.cache.seek(j2);
        this.streamPos = this.cache.getFilePointer();
        this.maxStreamPos = Math.max(this.maxStreamPos, this.streamPos);
        this.bitOffset = 0;
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl, javax.imageio.stream.ImageInputStream
    public boolean isCached() {
        return true;
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl, javax.imageio.stream.ImageInputStream
    public boolean isCachedFile() {
        return true;
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl, javax.imageio.stream.ImageInputStream
    public boolean isCachedMemory() {
        return false;
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl, javax.imageio.stream.ImageInputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.maxStreamPos = this.cache.length();
        seek(this.maxStreamPos);
        flushBefore(this.maxStreamPos);
        super.close();
        this.cache.close();
        this.cache = null;
        this.cacheFile.delete();
        this.cacheFile = null;
        this.stream.flush();
        this.stream = null;
        StreamCloser.removeFromQueue(this.closeAction);
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl, javax.imageio.stream.ImageInputStream, javax.imageio.stream.ImageOutputStream
    public void flushBefore(long j2) throws IOException {
        long j3 = this.flushedPos;
        super.flushBefore(j2);
        long j4 = this.flushedPos - j3;
        if (j4 > 0) {
            byte[] bArr = new byte[512];
            this.cache.seek(j3);
            while (j4 > 0) {
                int iMin = (int) Math.min(j4, 512);
                this.cache.readFully(bArr, 0, iMin);
                this.stream.write(bArr, 0, iMin);
                j4 -= iMin;
            }
            this.stream.flush();
        }
    }
}
