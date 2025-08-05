package org.icepdf.core.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.lingala.zip4j.util.InternalZipConstants;

/* loaded from: icepdf-core.jar:org/icepdf/core/io/RandomAccessFileInputStream.class */
public class RandomAccessFileInputStream extends InputStream implements SeekableInput {
    private static final Logger logger = Logger.getLogger(RandomAccessFileInputStream.class.toString());
    private RandomAccessFile m_RandomAccessFile;
    private final ReentrantLock lock = new ReentrantLock();
    private long m_lMarkPosition = 0;

    public static RandomAccessFileInputStream build(File file) throws FileNotFoundException {
        RandomAccessFile raf = new RandomAccessFile(file, InternalZipConstants.READ_MODE);
        return new RandomAccessFileInputStream(raf);
    }

    protected RandomAccessFileInputStream(RandomAccessFile raf) {
        this.m_RandomAccessFile = raf;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        return this.m_RandomAccessFile.read();
    }

    @Override // java.io.InputStream
    public int read(byte[] buffer) throws IOException {
        return this.m_RandomAccessFile.read(buffer);
    }

    @Override // java.io.InputStream
    public int read(byte[] buffer, int offset, int length) throws IOException {
        return this.m_RandomAccessFile.read(buffer, offset, length);
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.m_RandomAccessFile.close();
    }

    @Override // java.io.InputStream
    public int available() {
        try {
            return (int) this.m_RandomAccessFile.getFilePointer();
        } catch (IOException e2) {
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.WARNING, "Error calling available", (Throwable) e2);
                return 0;
            }
            return 0;
        }
    }

    @Override // java.io.InputStream
    public void mark(int readLimit) {
        try {
            this.m_lMarkPosition = this.m_RandomAccessFile.getFilePointer();
        } catch (IOException e2) {
            throw new RuntimeException(e2.toString());
        }
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return true;
    }

    @Override // java.io.InputStream
    public void reset() throws IOException {
        this.m_RandomAccessFile.seek(this.m_lMarkPosition);
    }

    @Override // java.io.InputStream
    public long skip(long n2) throws IOException {
        int nn = (int) (n2 & (-1));
        return this.m_RandomAccessFile.skipBytes(nn);
    }

    @Override // org.icepdf.core.io.SeekableInput
    public void seekAbsolute(long absolutePosition) throws IOException {
        this.m_RandomAccessFile.seek(absolutePosition);
    }

    @Override // org.icepdf.core.io.SeekableInput
    public void seekRelative(long relativeOffset) throws IOException {
        long pos = this.m_RandomAccessFile.getFilePointer() + relativeOffset;
        if (pos < 0) {
            pos = 0;
        }
        this.m_RandomAccessFile.seek(pos);
    }

    @Override // org.icepdf.core.io.SeekableInput
    public void seekEnd() throws IOException {
        long end = this.m_RandomAccessFile.length();
        seekAbsolute(end);
    }

    @Override // org.icepdf.core.io.SeekableInput
    public long getAbsolutePosition() throws IOException {
        return this.m_RandomAccessFile.getFilePointer();
    }

    @Override // org.icepdf.core.io.SeekableInput
    public long getLength() throws IOException {
        return this.m_RandomAccessFile.length();
    }

    @Override // org.icepdf.core.io.SeekableInput
    public InputStream getInputStream() {
        return this;
    }

    @Override // org.icepdf.core.io.SeekableInput
    public void beginThreadAccess() {
        this.lock.lock();
    }

    @Override // org.icepdf.core.io.SeekableInput
    public void endThreadAccess() {
        this.lock.unlock();
    }
}
