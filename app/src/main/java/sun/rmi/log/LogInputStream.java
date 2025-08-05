package sun.rmi.log;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: rt.jar:sun/rmi/log/LogInputStream.class */
public class LogInputStream extends InputStream {
    private InputStream in;
    private int length;

    public LogInputStream(InputStream inputStream, int i2) throws IOException {
        this.in = inputStream;
        this.length = i2;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.length == 0) {
            return -1;
        }
        int i2 = this.in.read();
        this.length = i2 != -1 ? this.length - 1 : 0;
        return i2;
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr) throws IOException {
        return read(bArr, 0, bArr.length);
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        if (this.length == 0) {
            return -1;
        }
        int i4 = this.in.read(bArr, i2, this.length < i3 ? this.length : i3);
        this.length = i4 != -1 ? this.length - i4 : 0;
        return i4;
    }

    @Override // java.io.InputStream
    public long skip(long j2) throws IOException {
        if (j2 > 2147483647L) {
            throw new IOException("Too many bytes to skip - " + j2);
        }
        if (this.length == 0) {
            return 0L;
        }
        long jSkip = this.in.skip(((long) this.length) < j2 ? this.length : j2);
        this.length = (int) (this.length - jSkip);
        return jSkip;
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        int iAvailable = this.in.available();
        return this.length < iAvailable ? this.length : iAvailable;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.length = 0;
    }

    protected void finalize() throws IOException {
        close();
    }
}
