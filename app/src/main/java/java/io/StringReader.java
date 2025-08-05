package java.io;

/* loaded from: rt.jar:java/io/StringReader.class */
public class StringReader extends Reader {
    private String str;
    private int length;
    private int next = 0;
    private int mark = 0;

    public StringReader(String str) {
        this.str = str;
        this.length = str.length();
    }

    private void ensureOpen() throws IOException {
        if (this.str == null) {
            throw new IOException("Stream closed");
        }
    }

    @Override // java.io.Reader
    public int read() throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            if (this.next >= this.length) {
                return -1;
            }
            String str = this.str;
            int i2 = this.next;
            this.next = i2 + 1;
            return str.charAt(i2);
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
            if (this.next >= this.length) {
                return -1;
            }
            int iMin = Math.min(this.length - this.next, i3);
            this.str.getChars(this.next, this.next + iMin, cArr, i2);
            this.next += iMin;
            return iMin;
        }
    }

    @Override // java.io.Reader
    public long skip(long j2) throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            if (this.next >= this.length) {
                return 0L;
            }
            long jMax = Math.max(-this.next, Math.min(this.length - this.next, j2));
            this.next = (int) (this.next + jMax);
            return jMax;
        }
    }

    @Override // java.io.Reader
    public boolean ready() throws IOException {
        synchronized (this.lock) {
            ensureOpen();
        }
        return true;
    }

    @Override // java.io.Reader
    public boolean markSupported() {
        return true;
    }

    @Override // java.io.Reader
    public void mark(int i2) throws IOException {
        if (i2 < 0) {
            throw new IllegalArgumentException("Read-ahead limit < 0");
        }
        synchronized (this.lock) {
            ensureOpen();
            this.mark = this.next;
        }
    }

    @Override // java.io.Reader
    public void reset() throws IOException {
        synchronized (this.lock) {
            ensureOpen();
            this.next = this.mark;
        }
    }

    @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.str = null;
    }
}
