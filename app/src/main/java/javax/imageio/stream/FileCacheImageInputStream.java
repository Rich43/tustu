package javax.imageio.stream;

import com.sun.imageio.stream.StreamCloser;
import com.sun.imageio.stream.StreamFinalizer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import net.lingala.zip4j.util.InternalZipConstants;
import sun.java2d.Disposer;
import sun.java2d.DisposerRecord;

/* loaded from: rt.jar:javax/imageio/stream/FileCacheImageInputStream.class */
public class FileCacheImageInputStream extends ImageInputStreamImpl {
    private InputStream stream;
    private File cacheFile;
    private RandomAccessFile cache;
    private static final int BUFFER_LENGTH = 1024;
    private byte[] buf = new byte[1024];
    private long length = 0;
    private boolean foundEOF = false;
    private final Object disposerReferent;
    private final DisposerRecord disposerRecord;
    private final StreamCloser.CloseAction closeAction;

    public FileCacheImageInputStream(InputStream inputStream, File file) throws IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("stream == null!");
        }
        if (file != null && !file.isDirectory()) {
            throw new IllegalArgumentException("Not a directory!");
        }
        this.stream = inputStream;
        if (file == null) {
            this.cacheFile = Files.createTempFile("imageio", ".tmp", new FileAttribute[0]).toFile();
        } else {
            this.cacheFile = Files.createTempFile(file.toPath(), "imageio", ".tmp", new FileAttribute[0]).toFile();
        }
        this.cache = new RandomAccessFile(this.cacheFile, InternalZipConstants.WRITE_MODE);
        this.closeAction = StreamCloser.createCloseAction(this);
        StreamCloser.addToQueue(this.closeAction);
        this.disposerRecord = new StreamDisposerRecord(this.cacheFile, this.cache);
        if (getClass() == FileCacheImageInputStream.class) {
            this.disposerReferent = new Object();
            Disposer.addRecord(this.disposerReferent, this.disposerRecord);
        } else {
            this.disposerReferent = new StreamFinalizer(this);
        }
    }

    private long readUntil(long j2) throws IOException {
        if (j2 < this.length) {
            return j2;
        }
        if (this.foundEOF) {
            return this.length;
        }
        long j3 = j2 - this.length;
        this.cache.seek(this.length);
        while (j3 > 0) {
            int i2 = this.stream.read(this.buf, 0, (int) Math.min(j3, 1024L));
            if (i2 == -1) {
                this.foundEOF = true;
                return this.length;
            }
            this.cache.write(this.buf, 0, i2);
            j3 -= i2;
            this.length += i2;
        }
        return j2;
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl, javax.imageio.stream.ImageInputStream
    public int read() throws IOException {
        checkClosed();
        this.bitOffset = 0;
        long j2 = this.streamPos + 1;
        if (readUntil(j2) >= j2) {
            RandomAccessFile randomAccessFile = this.cache;
            long j3 = this.streamPos;
            this.streamPos = j3 + 1;
            randomAccessFile.seek(j3);
            return this.cache.read();
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
        int iMin = (int) Math.min(i3, readUntil(this.streamPos + i3) - this.streamPos);
        if (iMin > 0) {
            this.cache.seek(this.streamPos);
            this.cache.readFully(bArr, i2, iMin);
            this.streamPos += iMin;
            return iMin;
        }
        return -1;
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
        super.close();
        this.disposerRecord.dispose();
        this.stream = null;
        this.cache = null;
        this.cacheFile = null;
        StreamCloser.removeFromQueue(this.closeAction);
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl
    protected void finalize() throws Throwable {
    }

    /* loaded from: rt.jar:javax/imageio/stream/FileCacheImageInputStream$StreamDisposerRecord.class */
    private static class StreamDisposerRecord implements DisposerRecord {
        private File cacheFile;
        private RandomAccessFile cache;

        public StreamDisposerRecord(File file, RandomAccessFile randomAccessFile) {
            this.cacheFile = file;
            this.cache = randomAccessFile;
        }

        @Override // sun.java2d.DisposerRecord
        public synchronized void dispose() {
            if (this.cache != null) {
                try {
                    this.cache.close();
                } catch (IOException e2) {
                } finally {
                    this.cache = null;
                }
            }
            if (this.cacheFile != null) {
                this.cacheFile.delete();
                this.cacheFile = null;
            }
        }
    }
}
