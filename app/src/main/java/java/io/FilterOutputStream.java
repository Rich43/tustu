package java.io;

/* loaded from: rt.jar:java/io/FilterOutputStream.class */
public class FilterOutputStream extends OutputStream {
    protected OutputStream out;

    public FilterOutputStream(OutputStream outputStream) {
        this.out = outputStream;
    }

    @Override // java.io.OutputStream
    public void write(int i2) throws IOException {
        this.out.write(i2);
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr) throws IOException {
        write(bArr, 0, bArr.length);
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) throws IOException {
        if ((i2 | i3 | (bArr.length - (i3 + i2)) | (i2 + i3)) < 0) {
            throw new IndexOutOfBoundsException();
        }
        for (int i4 = 0; i4 < i3; i4++) {
            write(bArr[i2 + i4]);
        }
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        this.out.flush();
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        OutputStream outputStream = this.out;
        Throwable th = null;
        try {
            try {
                flush();
                if (outputStream != null) {
                    if (0 != 0) {
                        try {
                            outputStream.close();
                            return;
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                            return;
                        }
                    }
                    outputStream.close();
                }
            } catch (Throwable th3) {
                if (outputStream != null) {
                    if (th != null) {
                        try {
                            outputStream.close();
                        } catch (Throwable th4) {
                            th.addSuppressed(th4);
                        }
                    } else {
                        outputStream.close();
                    }
                }
                throw th3;
            }
        } catch (Throwable th5) {
            th = th5;
            throw th5;
        }
    }
}
