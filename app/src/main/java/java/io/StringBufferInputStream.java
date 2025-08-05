package java.io;

@Deprecated
/* loaded from: rt.jar:java/io/StringBufferInputStream.class */
public class StringBufferInputStream extends InputStream {
    protected String buffer;
    protected int pos;
    protected int count;

    public StringBufferInputStream(String str) {
        this.buffer = str;
        this.count = str.length();
    }

    @Override // java.io.InputStream
    public synchronized int read() {
        if (this.pos >= this.count) {
            return -1;
        }
        String str = this.buffer;
        int i2 = this.pos;
        this.pos = i2 + 1;
        return str.charAt(i2) & 255;
    }

    @Override // java.io.InputStream
    public synchronized int read(byte[] bArr, int i2, int i3) {
        if (bArr == null) {
            throw new NullPointerException();
        }
        if (i2 < 0 || i2 > bArr.length || i3 < 0 || i2 + i3 > bArr.length || i2 + i3 < 0) {
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
        String str = this.buffer;
        int i5 = i3;
        while (true) {
            i5--;
            if (i5 >= 0) {
                int i6 = i2;
                i2++;
                int i7 = this.pos;
                this.pos = i7 + 1;
                bArr[i6] = (byte) str.charAt(i7);
            } else {
                return i3;
            }
        }
    }

    @Override // java.io.InputStream
    public synchronized long skip(long j2) {
        if (j2 < 0) {
            return 0L;
        }
        if (j2 > this.count - this.pos) {
            j2 = this.count - this.pos;
        }
        this.pos = (int) (this.pos + j2);
        return j2;
    }

    @Override // java.io.InputStream
    public synchronized int available() {
        return this.count - this.pos;
    }

    @Override // java.io.InputStream
    public synchronized void reset() {
        this.pos = 0;
    }
}
