package java.io;

/* loaded from: rt.jar:java/io/InputStream.class */
public abstract class InputStream implements Closeable {
    private static final int MAX_SKIP_BUFFER_SIZE = 2048;

    public abstract int read() throws IOException;

    public int read(byte[] bArr) throws IOException {
        return read(bArr, 0, bArr.length);
    }

    public int read(byte[] bArr, int i2, int i3) throws IOException {
        if (bArr == null) {
            throw new NullPointerException();
        }
        if (i2 < 0 || i3 < 0 || i3 > bArr.length - i2) {
            throw new IndexOutOfBoundsException();
        }
        if (i3 == 0) {
            return 0;
        }
        int i4 = read();
        if (i4 == -1) {
            return -1;
        }
        bArr[i2] = (byte) i4;
        int i5 = 1;
        while (i5 < i3) {
            try {
                int i6 = read();
                if (i6 == -1) {
                    break;
                }
                bArr[i2 + i5] = (byte) i6;
                i5++;
            } catch (IOException e2) {
            }
        }
        return i5;
    }

    public long skip(long j2) throws IOException {
        int i2;
        long j3 = j2;
        if (j2 <= 0) {
            return 0L;
        }
        int iMin = (int) Math.min(2048L, j3);
        byte[] bArr = new byte[iMin];
        while (j3 > 0 && (i2 = read(bArr, 0, (int) Math.min(iMin, j3))) >= 0) {
            j3 -= i2;
        }
        return j2 - j3;
    }

    public int available() throws IOException {
        return 0;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
    }

    public synchronized void mark(int i2) {
    }

    public synchronized void reset() throws IOException {
        throw new IOException("mark/reset not supported");
    }

    public boolean markSupported() {
        return false;
    }
}
