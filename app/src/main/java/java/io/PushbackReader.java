package java.io;

/* loaded from: rt.jar:java/io/PushbackReader.class */
public class PushbackReader extends FilterReader {
    private char[] buf;
    private int pos;

    public PushbackReader(Reader reader, int i2) {
        super(reader);
        if (i2 <= 0) {
            throw new IllegalArgumentException("size <= 0");
        }
        this.buf = new char[i2];
        this.pos = i2;
    }

    public PushbackReader(Reader reader) {
        this(reader, 1);
    }

    private void ensureOpen() throws IOException {
        if (this.buf == null) {
            throw new IOException("Stream closed");
        }
    }

    @Override // java.io.FilterReader, java.io.Reader
    public int read() throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            if (this.pos < this.buf.length) {
                char[] cArr = this.buf;
                int i2 = this.pos;
                this.pos = i2 + 1;
                return cArr[i2];
            }
            return super.read();
        }
    }

    @Override // java.io.FilterReader, java.io.Reader
    public int read(char[] cArr, int i2, int i3) throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            try {
                if (i3 <= 0) {
                    if (i3 < 0) {
                        throw new IndexOutOfBoundsException();
                    }
                    if (i2 < 0 || i2 > cArr.length) {
                        throw new IndexOutOfBoundsException();
                    }
                    return 0;
                }
                int length = this.buf.length - this.pos;
                if (length > 0) {
                    if (i3 < length) {
                        length = i3;
                    }
                    System.arraycopy(this.buf, this.pos, cArr, i2, length);
                    this.pos += length;
                    i2 += length;
                    i3 -= length;
                }
                if (i3 > 0) {
                    int i4 = super.read(cArr, i2, i3);
                    if (i4 == -1) {
                        return length == 0 ? -1 : length;
                    }
                    return length + i4;
                }
                return length;
            } catch (ArrayIndexOutOfBoundsException e2) {
                throw new IndexOutOfBoundsException();
            }
        }
    }

    public void unread(int i2) throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            if (this.pos == 0) {
                throw new IOException("Pushback buffer overflow");
            }
            char[] cArr = this.buf;
            int i3 = this.pos - 1;
            this.pos = i3;
            cArr[i3] = (char) i2;
        }
    }

    public void unread(char[] cArr, int i2, int i3) throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            if (i3 > this.pos) {
                throw new IOException("Pushback buffer overflow");
            }
            this.pos -= i3;
            System.arraycopy(cArr, i2, this.buf, this.pos, i3);
        }
    }

    public void unread(char[] cArr) throws IOException {
        unread(cArr, 0, cArr.length);
    }

    @Override // java.io.FilterReader, java.io.Reader
    public boolean ready() throws IOException {
        boolean z2;
        synchronized (this.lock) {
            ensureOpen();
            z2 = this.pos < this.buf.length || super.ready();
        }
        return z2;
    }

    @Override // java.io.FilterReader, java.io.Reader
    public void mark(int i2) throws IOException {
        throw new IOException("mark/reset not supported");
    }

    @Override // java.io.FilterReader, java.io.Reader
    public void reset() throws IOException {
        throw new IOException("mark/reset not supported");
    }

    @Override // java.io.FilterReader, java.io.Reader
    public boolean markSupported() {
        return false;
    }

    @Override // java.io.FilterReader, java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        super.close();
        this.buf = null;
    }

    @Override // java.io.FilterReader, java.io.Reader
    public long skip(long j2) throws IOException {
        if (j2 < 0) {
            throw new IllegalArgumentException("skip value is negative");
        }
        synchronized (this.lock) {
            ensureOpen();
            int length = this.buf.length - this.pos;
            if (length > 0) {
                if (j2 <= length) {
                    this.pos = (int) (this.pos + j2);
                    return j2;
                }
                this.pos = this.buf.length;
                j2 -= length;
            }
            return length + super.skip(j2);
        }
    }
}
