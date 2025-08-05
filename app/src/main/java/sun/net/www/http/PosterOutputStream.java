package sun.net.www.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:sun/net/www/http/PosterOutputStream.class */
public class PosterOutputStream extends ByteArrayOutputStream {
    private boolean closed;

    public PosterOutputStream() {
        super(256);
    }

    @Override // java.io.ByteArrayOutputStream, java.io.OutputStream
    public synchronized void write(int i2) {
        if (this.closed) {
            return;
        }
        super.write(i2);
    }

    @Override // java.io.ByteArrayOutputStream, java.io.OutputStream
    public synchronized void write(byte[] bArr, int i2, int i3) {
        if (this.closed) {
            return;
        }
        super.write(bArr, i2, i3);
    }

    @Override // java.io.ByteArrayOutputStream
    public synchronized void reset() {
        if (this.closed) {
            return;
        }
        super.reset();
    }

    @Override // java.io.ByteArrayOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        this.closed = true;
        super.close();
    }
}
