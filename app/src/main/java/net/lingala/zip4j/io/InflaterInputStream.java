package net.lingala.zip4j.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import net.lingala.zip4j.unzip.UnzipEngine;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/io/InflaterInputStream.class */
public class InflaterInputStream extends PartInputStream {
    private Inflater inflater;
    private byte[] buff;
    private byte[] oneByteBuff;
    private UnzipEngine unzipEngine;
    private long bytesWritten;
    private long uncompressedSize;

    public InflaterInputStream(RandomAccessFile raf, long start, long len, UnzipEngine unzipEngine) {
        super(raf, start, len, unzipEngine);
        this.oneByteBuff = new byte[1];
        this.inflater = new Inflater(true);
        this.buff = new byte[4096];
        this.unzipEngine = unzipEngine;
        this.bytesWritten = 0L;
        this.uncompressedSize = unzipEngine.getFileHeader().getUncompressedSize();
    }

    @Override // net.lingala.zip4j.io.PartInputStream, net.lingala.zip4j.io.BaseInputStream, java.io.InputStream
    public int read() throws IOException {
        if (read(this.oneByteBuff, 0, 1) == -1) {
            return -1;
        }
        return this.oneByteBuff[0] & 255;
    }

    @Override // net.lingala.zip4j.io.PartInputStream, java.io.InputStream
    public int read(byte[] b2) throws IOException {
        if (b2 == null) {
            throw new NullPointerException("input buffer is null");
        }
        return read(b2, 0, b2.length);
    }

    @Override // net.lingala.zip4j.io.PartInputStream, java.io.InputStream
    public int read(byte[] b2, int off, int len) throws IOException {
        if (b2 == null) {
            throw new NullPointerException("input buffer is null");
        }
        if (off < 0 || len < 0 || len > b2.length - off) {
            throw new IndexOutOfBoundsException();
        }
        if (len == 0) {
            return 0;
        }
        try {
            if (this.bytesWritten >= this.uncompressedSize) {
                return -1;
            }
            while (true) {
                int n2 = this.inflater.inflate(b2, off, len);
                if (n2 == 0) {
                    if (this.inflater.finished() || this.inflater.needsDictionary()) {
                        return -1;
                    }
                    if (this.inflater.needsInput()) {
                        fill();
                    }
                } else {
                    this.bytesWritten += n2;
                    return n2;
                }
            }
        } catch (DataFormatException e2) {
            String s2 = "Invalid ZLIB data format";
            if (e2.getMessage() != null) {
                s2 = e2.getMessage();
            }
            if (this.unzipEngine != null && this.unzipEngine.getLocalFileHeader().isEncrypted() && this.unzipEngine.getLocalFileHeader().getEncryptionMethod() == 0) {
                s2 = new StringBuffer(String.valueOf(s2)).append(" - Wrong Password?").toString();
            }
            throw new IOException(s2);
        }
    }

    private void fill() throws IOException {
        int len = super.read(this.buff, 0, this.buff.length);
        if (len == -1) {
            throw new EOFException("Unexpected end of ZLIB input stream");
        }
        this.inflater.setInput(this.buff, 0, len);
    }

    @Override // net.lingala.zip4j.io.PartInputStream, java.io.InputStream
    public long skip(long n2) throws IOException {
        if (n2 < 0) {
            throw new IllegalArgumentException("negative skip length");
        }
        int max = (int) Math.min(n2, 2147483647L);
        int total = 0;
        byte[] b2 = new byte[512];
        while (total < max) {
            int len = max - total;
            if (len > b2.length) {
                len = b2.length;
            }
            int len2 = read(b2, 0, len);
            if (len2 == -1) {
                break;
            }
            total += len2;
        }
        return total;
    }

    @Override // net.lingala.zip4j.io.PartInputStream, net.lingala.zip4j.io.BaseInputStream
    public void seek(long pos) throws IOException {
        super.seek(pos);
    }

    @Override // net.lingala.zip4j.io.PartInputStream, net.lingala.zip4j.io.BaseInputStream, java.io.InputStream
    public int available() {
        return this.inflater.finished() ? 0 : 1;
    }

    @Override // net.lingala.zip4j.io.PartInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.inflater.end();
        super.close();
    }

    @Override // net.lingala.zip4j.io.PartInputStream, net.lingala.zip4j.io.BaseInputStream
    public UnzipEngine getUnzipEngine() {
        return super.getUnzipEngine();
    }
}
