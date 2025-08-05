package java.util.zip;

import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: rt.jar:java/util/zip/InflaterInputStream.class */
public class InflaterInputStream extends FilterInputStream {
    protected Inflater inf;
    protected byte[] buf;
    protected int len;
    private boolean closed;
    private boolean reachEOF;
    boolean usesDefaultInflater;
    private byte[] singleByteBuf;

    /* renamed from: b, reason: collision with root package name */
    private byte[] f12622b;

    private void ensureOpen() throws IOException {
        if (this.closed) {
            throw new IOException("Stream closed");
        }
    }

    public InflaterInputStream(InputStream inputStream, Inflater inflater, int i2) {
        super(inputStream);
        this.closed = false;
        this.reachEOF = false;
        this.usesDefaultInflater = false;
        this.singleByteBuf = new byte[1];
        this.f12622b = new byte[512];
        if (inputStream == null || inflater == null) {
            throw new NullPointerException();
        }
        if (i2 <= 0) {
            throw new IllegalArgumentException("buffer size <= 0");
        }
        this.inf = inflater;
        this.buf = new byte[i2];
    }

    public InflaterInputStream(InputStream inputStream, Inflater inflater) {
        this(inputStream, inflater, 512);
    }

    public InflaterInputStream(InputStream inputStream) {
        this(inputStream, new Inflater());
        this.usesDefaultInflater = true;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        ensureOpen();
        if (read(this.singleByteBuf, 0, 1) == -1) {
            return -1;
        }
        return Byte.toUnsignedInt(this.singleByteBuf[0]);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        ensureOpen();
        if (bArr == null) {
            throw new NullPointerException();
        }
        if (i2 < 0 || i3 < 0 || i3 > bArr.length - i2) {
            throw new IndexOutOfBoundsException();
        }
        if (i3 == 0) {
            return 0;
        }
        while (true) {
            try {
                int iInflate = this.inf.inflate(bArr, i2, i3);
                if (iInflate == 0) {
                    if (this.inf.finished() || this.inf.needsDictionary()) {
                        break;
                    }
                    if (this.inf.needsInput()) {
                        fill();
                    }
                } else {
                    return iInflate;
                }
            } catch (DataFormatException e2) {
                String message = e2.getMessage();
                throw new ZipException(message != null ? message : "Invalid ZLIB data format");
            }
        }
        this.reachEOF = true;
        return -1;
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
    public long skip(long j2) throws IOException {
        int i2;
        if (j2 < 0) {
            throw new IllegalArgumentException("negative skip length");
        }
        ensureOpen();
        int iMin = (int) Math.min(j2, 2147483647L);
        int i3 = 0;
        while (true) {
            i2 = i3;
            if (i2 >= iMin) {
                break;
            }
            int length = iMin - i2;
            if (length > this.f12622b.length) {
                length = this.f12622b.length;
            }
            int i4 = read(this.f12622b, 0, length);
            if (i4 == -1) {
                this.reachEOF = true;
                break;
            }
            i3 = i2 + i4;
        }
        return i2;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.closed) {
            if (this.usesDefaultInflater) {
                this.inf.end();
            }
            this.in.close();
            this.closed = true;
        }
    }

    protected void fill() throws IOException {
        ensureOpen();
        this.len = this.in.read(this.buf, 0, this.buf.length);
        if (this.len == -1) {
            throw new EOFException("Unexpected end of ZLIB input stream");
        }
        this.inf.setInput(this.buf, 0, this.len);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public boolean markSupported() {
        return false;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized void mark(int i2) {
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized void reset() throws IOException {
        throw new IOException("mark/reset not supported");
    }
}
