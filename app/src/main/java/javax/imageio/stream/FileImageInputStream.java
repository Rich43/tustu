package javax.imageio.stream;

import com.sun.imageio.stream.CloseableDisposerRecord;
import com.sun.imageio.stream.StreamFinalizer;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import net.lingala.zip4j.util.InternalZipConstants;
import sun.java2d.Disposer;

/* loaded from: rt.jar:javax/imageio/stream/FileImageInputStream.class */
public class FileImageInputStream extends ImageInputStreamImpl {
    private RandomAccessFile raf;
    private final Object disposerReferent;
    private final CloseableDisposerRecord disposerRecord;

    public FileImageInputStream(File file) throws IOException {
        this(file == null ? null : new RandomAccessFile(file, InternalZipConstants.READ_MODE));
    }

    public FileImageInputStream(RandomAccessFile randomAccessFile) {
        if (randomAccessFile == null) {
            throw new IllegalArgumentException("raf == null!");
        }
        this.raf = randomAccessFile;
        this.disposerRecord = new CloseableDisposerRecord(randomAccessFile);
        if (getClass() == FileImageInputStream.class) {
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
        int i2 = this.raf.read();
        if (i2 != -1) {
            this.streamPos++;
        }
        return i2;
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl, javax.imageio.stream.ImageInputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        checkClosed();
        this.bitOffset = 0;
        int i4 = this.raf.read(bArr, i2, i3);
        if (i4 != -1) {
            this.streamPos += i4;
        }
        return i4;
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl, javax.imageio.stream.ImageInputStream
    public long length() {
        try {
            checkClosed();
            return this.raf.length();
        } catch (IOException e2) {
            return -1L;
        }
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl, javax.imageio.stream.ImageInputStream
    public void seek(long j2) throws IOException {
        checkClosed();
        if (j2 < this.flushedPos) {
            throw new IndexOutOfBoundsException("pos < flushedPos!");
        }
        this.bitOffset = 0;
        this.raf.seek(j2);
        this.streamPos = this.raf.getFilePointer();
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl, javax.imageio.stream.ImageInputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        super.close();
        this.disposerRecord.dispose();
        this.raf = null;
    }

    @Override // javax.imageio.stream.ImageInputStreamImpl
    protected void finalize() throws Throwable {
    }
}
