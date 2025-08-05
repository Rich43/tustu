package java.util.zip;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: rt.jar:java/util/zip/DeflaterInputStream.class */
public class DeflaterInputStream extends FilterInputStream {
    protected final Deflater def;
    protected final byte[] buf;
    private byte[] rbuf;
    private boolean usesDefaultDeflater;
    private boolean reachEOF;

    private void ensureOpen() throws IOException {
        if (this.in == null) {
            throw new IOException("Stream closed");
        }
    }

    public DeflaterInputStream(InputStream inputStream) {
        this(inputStream, new Deflater());
        this.usesDefaultDeflater = true;
    }

    public DeflaterInputStream(InputStream inputStream, Deflater deflater) {
        this(inputStream, deflater, 512);
    }

    public DeflaterInputStream(InputStream inputStream, Deflater deflater, int i2) {
        super(inputStream);
        this.rbuf = new byte[1];
        this.usesDefaultDeflater = false;
        this.reachEOF = false;
        if (inputStream == null) {
            throw new NullPointerException("Null input");
        }
        if (deflater == null) {
            throw new NullPointerException("Null deflater");
        }
        if (i2 < 1) {
            throw new IllegalArgumentException("Buffer size < 1");
        }
        this.def = deflater;
        this.buf = new byte[i2];
    }

    @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.in != null) {
            try {
                if (this.usesDefaultDeflater) {
                    this.def.end();
                }
                this.in.close();
            } finally {
                this.in = null;
            }
        }
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        if (read(this.rbuf, 0, 1) <= 0) {
            return -1;
        }
        return this.rbuf[0] & 255;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        ensureOpen();
        if (bArr == null) {
            throw new NullPointerException("Null buffer for read");
        }
        if (i2 < 0 || i3 < 0 || i3 > bArr.length - i2) {
            throw new IndexOutOfBoundsException();
        }
        if (i3 == 0) {
            return 0;
        }
        int i4 = 0;
        while (i3 > 0 && !this.def.finished()) {
            if (this.def.needsInput()) {
                int i5 = this.in.read(this.buf, 0, this.buf.length);
                if (i5 < 0) {
                    this.def.finish();
                } else if (i5 > 0) {
                    this.def.setInput(this.buf, 0, i5);
                }
            }
            int iDeflate = this.def.deflate(bArr, i2, i3);
            i4 += iDeflate;
            i2 += iDeflate;
            i3 -= iDeflate;
        }
        if (i4 == 0 && this.def.finished()) {
            this.reachEOF = true;
            i4 = -1;
        }
        return i4;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public long skip(long j2) throws IOException {
        if (j2 < 0) {
            throw new IllegalArgumentException("negative skip length");
        }
        ensureOpen();
        if (this.rbuf.length < 512) {
            this.rbuf = new byte[512];
        }
        int iMin = (int) Math.min(j2, 2147483647L);
        long j3 = 0;
        while (iMin > 0) {
            int i2 = read(this.rbuf, 0, iMin <= this.rbuf.length ? iMin : this.rbuf.length);
            if (i2 < 0) {
                break;
            }
            j3 += i2;
            iMin -= i2;
        }
        return j3;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int available() throws IOException {
        ensureOpen();
        if (this.reachEOF) {
            return 0;
        }
        return 1;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public boolean markSupported() {
        return false;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public void mark(int i2) {
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public void reset() throws IOException {
        throw new IOException("mark/reset not supported");
    }
}
