package java.io;

@Deprecated
/* loaded from: rt.jar:java/io/LineNumberInputStream.class */
public class LineNumberInputStream extends FilterInputStream {
    int pushBack;
    int lineNumber;
    int markLineNumber;
    int markPushBack;

    public LineNumberInputStream(InputStream inputStream) {
        super(inputStream);
        this.pushBack = -1;
        this.markPushBack = -1;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        int i2 = this.pushBack;
        if (i2 != -1) {
            this.pushBack = -1;
        } else {
            i2 = this.in.read();
        }
        switch (i2) {
            case 10:
                break;
            case 13:
                this.pushBack = this.in.read();
                if (this.pushBack == 10) {
                    this.pushBack = -1;
                    break;
                }
                break;
            default:
                return i2;
        }
        this.lineNumber++;
        return 10;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        if (bArr == null) {
            throw new NullPointerException();
        }
        if (i2 < 0 || i2 > bArr.length || i3 < 0 || i2 + i3 > bArr.length || i2 + i3 < 0) {
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
                if (bArr != null) {
                    bArr[i2 + i5] = (byte) i6;
                }
                i5++;
            } catch (IOException e2) {
            }
        }
        return i5;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public long skip(long j2) throws IOException {
        int i2;
        long j3 = j2;
        if (j2 <= 0) {
            return 0L;
        }
        byte[] bArr = new byte[2048];
        while (j3 > 0 && (i2 = read(bArr, 0, (int) Math.min(2048, j3))) >= 0) {
            j3 -= i2;
        }
        return j2 - j3;
    }

    public void setLineNumber(int i2) {
        this.lineNumber = i2;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int available() throws IOException {
        return this.pushBack == -1 ? super.available() / 2 : (super.available() / 2) + 1;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public void mark(int i2) {
        this.markLineNumber = this.lineNumber;
        this.markPushBack = this.pushBack;
        this.in.mark(i2);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public void reset() throws IOException {
        this.lineNumber = this.markLineNumber;
        this.pushBack = this.markPushBack;
        this.in.reset();
    }
}
