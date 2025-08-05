package sun.rmi.log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

/* loaded from: rt.jar:sun/rmi/log/LogOutputStream.class */
public class LogOutputStream extends OutputStream {
    private RandomAccessFile raf;

    public LogOutputStream(RandomAccessFile randomAccessFile) throws IOException {
        this.raf = randomAccessFile;
    }

    @Override // java.io.OutputStream
    public void write(int i2) throws IOException {
        this.raf.write(i2);
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr) throws IOException {
        this.raf.write(bArr);
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) throws IOException {
        this.raf.write(bArr, i2, i3);
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public final void close() throws IOException {
    }
}
