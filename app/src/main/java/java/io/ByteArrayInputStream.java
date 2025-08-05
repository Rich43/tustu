package java.io;

/* loaded from: rt.jar:java/io/ByteArrayInputStream.class */
public class ByteArrayInputStream extends InputStream {
    protected byte[] buf;
    protected int pos;
    protected int mark;
    protected int count;

    public ByteArrayInputStream(byte[] bArr) {
        this.mark = 0;
        this.buf = bArr;
        this.pos = 0;
        this.count = bArr.length;
    }

    public ByteArrayInputStream(byte[] bArr, int i2, int i3) {
        this.mark = 0;
        this.buf = bArr;
        this.pos = i2;
        this.count = Math.min(i2 + i3, bArr.length);
        this.mark = i2;
    }

    @Override // java.io.InputStream
    public synchronized int read() {
        if (this.pos >= this.count) {
            return -1;
        }
        byte[] bArr = this.buf;
        int i2 = this.pos;
        this.pos = i2 + 1;
        return bArr[i2] & 255;
    }

    @Override // java.io.InputStream
    public synchronized int read(byte[] bArr, int i2, int i3) {
        if (bArr == null) {
            throw new NullPointerException();
        }
        if (i2 < 0 || i3 < 0 || i3 > bArr.length - i2) {
            throw new IndexOutOfBoundsException();
        }
        if (this.pos >= this.count) {
            return -1;
        }
        int i4 = this.count - this.pos;
        if (i3 > i4) {
            i3 = i4;
        }
        if (i3 <= 0) {
            return 0;
        }
        System.arraycopy(this.buf, this.pos, bArr, i2, i3);
        this.pos += i3;
        return i3;
    }

    @Override // java.io.InputStream
    public synchronized long skip(long j2) {
        long j3 = this.count - this.pos;
        if (j2 < j3) {
            j3 = j2 < 0 ? 0L : j2;
        }
        this.pos = (int) (this.pos + j3);
        return j3;
    }

    @Override // java.io.InputStream
    public synchronized int available() {
        return this.count - this.pos;
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return true;
    }

    @Override // java.io.InputStream
    public void mark(int i2) {
        this.mark = this.pos;
    }

    @Override // java.io.InputStream
    public synchronized void reset() {
        this.pos = this.mark;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
    }
}
