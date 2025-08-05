package net.lingala.zip4j.io;

import java.io.IOException;
import java.io.InputStream;
import net.lingala.zip4j.exception.ZipException;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/io/ZipInputStream.class */
public class ZipInputStream extends InputStream {
    private BaseInputStream is;

    public ZipInputStream(BaseInputStream is) {
        this.is = is;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        int readByte = this.is.read();
        if (readByte != -1) {
            this.is.getUnzipEngine().updateCRC(readByte);
        }
        return readByte;
    }

    @Override // java.io.InputStream
    public int read(byte[] b2) throws IOException {
        return read(b2, 0, b2.length);
    }

    @Override // java.io.InputStream
    public int read(byte[] b2, int off, int len) throws IOException {
        int readLen = this.is.read(b2, off, len);
        if (readLen > 0 && this.is.getUnzipEngine() != null) {
            this.is.getUnzipEngine().updateCRC(b2, off, readLen);
        }
        return readLen;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        close(false);
    }

    public void close(boolean skipCRCCheck) throws IOException {
        try {
            this.is.close();
            if (!skipCRCCheck && this.is.getUnzipEngine() != null) {
                this.is.getUnzipEngine().checkCRC();
            }
        } catch (ZipException e2) {
            throw new IOException(e2.getMessage());
        }
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        return this.is.available();
    }

    @Override // java.io.InputStream
    public long skip(long n2) throws IOException {
        return this.is.skip(n2);
    }
}
