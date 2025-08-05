package java.io;

import java.nio.CharBuffer;

/* loaded from: rt.jar:java/io/Reader.class */
public abstract class Reader implements Readable, Closeable {
    protected Object lock;
    private static final int maxSkipBufferSize = 8192;
    private char[] skipBuffer;

    public abstract int read(char[] cArr, int i2, int i3) throws IOException;

    public abstract void close() throws IOException;

    protected Reader() {
        this.skipBuffer = null;
        this.lock = this;
    }

    protected Reader(Object obj) {
        this.skipBuffer = null;
        if (obj == null) {
            throw new NullPointerException();
        }
        this.lock = obj;
    }

    @Override // java.lang.Readable
    public int read(CharBuffer charBuffer) throws IOException {
        int iRemaining = charBuffer.remaining();
        char[] cArr = new char[iRemaining];
        int i2 = read(cArr, 0, iRemaining);
        if (i2 > 0) {
            charBuffer.put(cArr, 0, i2);
        }
        return i2;
    }

    public int read() throws IOException {
        char[] cArr = new char[1];
        if (read(cArr, 0, 1) == -1) {
            return -1;
        }
        return cArr[0];
    }

    public int read(char[] cArr) throws IOException {
        return read(cArr, 0, cArr.length);
    }

    public long skip(long j2) throws IOException {
        long j3;
        int i2;
        if (j2 < 0) {
            throw new IllegalArgumentException("skip value is negative");
        }
        int iMin = (int) Math.min(j2, 8192L);
        synchronized (this.lock) {
            if (this.skipBuffer == null || this.skipBuffer.length < iMin) {
                this.skipBuffer = new char[iMin];
            }
            long j4 = j2;
            while (j4 > 0 && (i2 = read(this.skipBuffer, 0, (int) Math.min(j4, iMin))) != -1) {
                j4 -= i2;
            }
            j3 = j2 - j4;
        }
        return j3;
    }

    public boolean ready() throws IOException {
        return false;
    }

    public boolean markSupported() {
        return false;
    }

    public void mark(int i2) throws IOException {
        throw new IOException("mark() not supported");
    }

    public void reset() throws IOException {
        throw new IOException("reset() not supported");
    }
}
