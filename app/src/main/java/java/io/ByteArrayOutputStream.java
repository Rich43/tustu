package java.io;

import java.util.Arrays;

/* loaded from: rt.jar:java/io/ByteArrayOutputStream.class */
public class ByteArrayOutputStream extends OutputStream {
    protected byte[] buf;
    protected int count;
    private static final int MAX_ARRAY_SIZE = 2147483639;

    public ByteArrayOutputStream() {
        this(32);
    }

    public ByteArrayOutputStream(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("Negative initial size: " + i2);
        }
        this.buf = new byte[i2];
    }

    private void ensureCapacity(int i2) {
        if (i2 - this.buf.length > 0) {
            grow(i2);
        }
    }

    private void grow(int i2) {
        int length = this.buf.length << 1;
        if (length - i2 < 0) {
            length = i2;
        }
        if (length - MAX_ARRAY_SIZE > 0) {
            length = hugeCapacity(i2);
        }
        this.buf = Arrays.copyOf(this.buf, length);
    }

    private static int hugeCapacity(int i2) {
        if (i2 < 0) {
            throw new OutOfMemoryError();
        }
        if (i2 > MAX_ARRAY_SIZE) {
            return Integer.MAX_VALUE;
        }
        return MAX_ARRAY_SIZE;
    }

    @Override // java.io.OutputStream
    public synchronized void write(int i2) {
        ensureCapacity(this.count + 1);
        this.buf[this.count] = (byte) i2;
        this.count++;
    }

    @Override // java.io.OutputStream
    public synchronized void write(byte[] bArr, int i2, int i3) {
        if (i2 < 0 || i2 > bArr.length || i3 < 0 || (i2 + i3) - bArr.length > 0) {
            throw new IndexOutOfBoundsException();
        }
        ensureCapacity(this.count + i3);
        System.arraycopy(bArr, i2, this.buf, this.count, i3);
        this.count += i3;
    }

    public synchronized void writeTo(OutputStream outputStream) throws IOException {
        outputStream.write(this.buf, 0, this.count);
    }

    public synchronized void reset() {
        this.count = 0;
    }

    public synchronized byte[] toByteArray() {
        return Arrays.copyOf(this.buf, this.count);
    }

    public synchronized int size() {
        return this.count;
    }

    public synchronized String toString() {
        return new String(this.buf, 0, this.count);
    }

    public synchronized String toString(String str) throws UnsupportedEncodingException {
        return new String(this.buf, 0, this.count, str);
    }

    @Deprecated
    public synchronized String toString(int i2) {
        return new String(this.buf, i2, 0, this.count);
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
    }
}
