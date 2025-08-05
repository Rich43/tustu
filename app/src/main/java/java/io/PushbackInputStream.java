package java.io;

/* loaded from: rt.jar:java/io/PushbackInputStream.class */
public class PushbackInputStream extends FilterInputStream {
    protected byte[] buf;
    protected int pos;

    private void ensureOpen() throws IOException {
        if (this.in == null) {
            throw new IOException("Stream closed");
        }
    }

    public PushbackInputStream(InputStream inputStream, int i2) {
        super(inputStream);
        if (i2 <= 0) {
            throw new IllegalArgumentException("size <= 0");
        }
        this.buf = new byte[i2];
        this.pos = i2;
    }

    public PushbackInputStream(InputStream inputStream) {
        this(inputStream, 1);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        ensureOpen();
        if (this.pos < this.buf.length) {
            byte[] bArr = this.buf;
            int i2 = this.pos;
            this.pos = i2 + 1;
            return bArr[i2] & 255;
        }
        return super.read();
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
        int length = this.buf.length - this.pos;
        if (length > 0) {
            if (i3 < length) {
                length = i3;
            }
            System.arraycopy(this.buf, this.pos, bArr, i2, length);
            this.pos += length;
            i2 += length;
            i3 -= length;
        }
        if (i3 > 0) {
            int i4 = super.read(bArr, i2, i3);
            if (i4 == -1) {
                if (length == 0) {
                    return -1;
                }
                return length;
            }
            return length + i4;
        }
        return length;
    }

    public void unread(int i2) throws IOException {
        ensureOpen();
        if (this.pos == 0) {
            throw new IOException("Push back buffer is full");
        }
        byte[] bArr = this.buf;
        int i3 = this.pos - 1;
        this.pos = i3;
        bArr[i3] = (byte) i2;
    }

    public void unread(byte[] bArr, int i2, int i3) throws IOException {
        ensureOpen();
        if (i3 > this.pos) {
            throw new IOException("Push back buffer is full");
        }
        this.pos -= i3;
        System.arraycopy(bArr, i2, this.buf, this.pos, i3);
    }

    public void unread(byte[] bArr) throws IOException {
        unread(bArr, 0, bArr.length);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int available() throws IOException {
        ensureOpen();
        int length = this.buf.length - this.pos;
        int iAvailable = super.available();
        if (length > Integer.MAX_VALUE - iAvailable) {
            return Integer.MAX_VALUE;
        }
        return length + iAvailable;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public long skip(long j2) throws IOException {
        ensureOpen();
        if (j2 <= 0) {
            return 0L;
        }
        long length = this.buf.length - this.pos;
        if (length > 0) {
            if (j2 < length) {
                length = j2;
            }
            this.pos = (int) (this.pos + length);
            j2 -= length;
        }
        if (j2 > 0) {
            length += super.skip(j2);
        }
        return length;
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

    @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        if (this.in == null) {
            return;
        }
        this.in.close();
        this.in = null;
        this.buf = null;
    }
}
