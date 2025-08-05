package java.io;

/* loaded from: rt.jar:java/io/CharArrayReader.class */
public class CharArrayReader extends Reader {
    protected char[] buf;
    protected int pos;
    protected int markedPos;
    protected int count;

    public CharArrayReader(char[] cArr) {
        this.markedPos = 0;
        this.buf = cArr;
        this.pos = 0;
        this.count = cArr.length;
    }

    public CharArrayReader(char[] cArr, int i2, int i3) {
        this.markedPos = 0;
        if (i2 < 0 || i2 > cArr.length || i3 < 0 || i2 + i3 < 0) {
            throw new IllegalArgumentException();
        }
        this.buf = cArr;
        this.pos = i2;
        this.count = Math.min(i2 + i3, cArr.length);
        this.markedPos = i2;
    }

    private void ensureOpen() throws IOException {
        if (this.buf == null) {
            throw new IOException("Stream closed");
        }
    }

    @Override // java.io.Reader
    public int read() throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            if (this.pos >= this.count) {
                return -1;
            }
            char[] cArr = this.buf;
            int i2 = this.pos;
            this.pos = i2 + 1;
            return cArr[i2];
        }
    }

    @Override // java.io.Reader
    public int read(char[] cArr, int i2, int i3) throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            if (i2 < 0 || i2 > cArr.length || i3 < 0 || i2 + i3 > cArr.length || i2 + i3 < 0) {
                throw new IndexOutOfBoundsException();
            }
            if (i3 == 0) {
                return 0;
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
            System.arraycopy(this.buf, this.pos, cArr, i2, i3);
            this.pos += i3;
            return i3;
        }
    }

    @Override // java.io.Reader
    public long skip(long j2) throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            long j3 = this.count - this.pos;
            if (j2 > j3) {
                j2 = j3;
            }
            if (j2 < 0) {
                return 0L;
            }
            this.pos = (int) (this.pos + j2);
            return j2;
        }
    }

    @Override // java.io.Reader
    public boolean ready() throws IOException {
        boolean z2;
        synchronized (this.lock) {
            ensureOpen();
            z2 = this.count - this.pos > 0;
        }
        return z2;
    }

    @Override // java.io.Reader
    public boolean markSupported() {
        return true;
    }

    @Override // java.io.Reader
    public void mark(int i2) throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            this.markedPos = this.pos;
        }
    }

    @Override // java.io.Reader
    public void reset() throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            this.pos = this.markedPos;
        }
    }

    @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.buf = null;
    }
}
