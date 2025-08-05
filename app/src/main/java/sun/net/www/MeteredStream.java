package sun.net.www;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import sun.net.ProgressSource;
import sun.net.www.http.ChunkedInputStream;

/* loaded from: rt.jar:sun/net/www/MeteredStream.class */
public class MeteredStream extends FilterInputStream {
    protected boolean closed;
    protected long expected;
    protected long count;
    protected long markedCount;
    protected int markLimit;
    protected ProgressSource pi;

    public MeteredStream(InputStream inputStream, ProgressSource progressSource, long j2) {
        super(inputStream);
        this.closed = false;
        this.count = 0L;
        this.markedCount = 0L;
        this.markLimit = -1;
        this.pi = progressSource;
        this.expected = j2;
        if (progressSource != null) {
            progressSource.updateProgress(0L, j2);
        }
    }

    private final void justRead(long j2) throws IOException {
        if (j2 == -1) {
            if (!isMarked()) {
                close();
                return;
            }
            return;
        }
        this.count += j2;
        if (this.count - this.markedCount > this.markLimit) {
            this.markLimit = -1;
        }
        if (this.pi != null) {
            this.pi.updateProgress(this.count, this.expected);
        }
        if (!isMarked() && this.expected > 0 && this.count >= this.expected) {
            close();
        }
    }

    private boolean isMarked() {
        if (this.markLimit < 0 || this.count - this.markedCount > this.markLimit) {
            return false;
        }
        return true;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized int read() throws IOException {
        if (this.closed) {
            return -1;
        }
        int i2 = this.in.read();
        if (i2 != -1) {
            justRead(1L);
        } else {
            justRead(i2);
        }
        return i2;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized int read(byte[] bArr, int i2, int i3) throws IOException {
        if (this.closed) {
            return -1;
        }
        int i4 = this.in.read(bArr, i2, i3);
        justRead(i4);
        return i4;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized long skip(long j2) throws IOException {
        long jSkip;
        if (this.closed) {
            return 0L;
        }
        if (this.in instanceof ChunkedInputStream) {
            jSkip = this.in.skip(j2);
        } else {
            jSkip = this.in.skip(j2 > this.expected - this.count ? this.expected - this.count : j2);
        }
        justRead(jSkip);
        return jSkip;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.closed) {
            return;
        }
        if (this.pi != null) {
            this.pi.finishTracking();
        }
        this.closed = true;
        this.in.close();
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized int available() throws IOException {
        if (this.closed) {
            return 0;
        }
        return this.in.available();
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized void mark(int i2) {
        if (this.closed) {
            return;
        }
        super.mark(i2);
        this.markedCount = this.count;
        this.markLimit = i2;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized void reset() throws IOException {
        if (this.closed) {
            return;
        }
        if (!isMarked()) {
            throw new IOException("Resetting to an invalid mark");
        }
        this.count = this.markedCount;
        super.reset();
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public boolean markSupported() {
        if (this.closed) {
            return false;
        }
        return super.markSupported();
    }

    protected void finalize() throws Throwable {
        try {
            close();
            if (this.pi != null) {
                this.pi.close();
            }
        } finally {
            super.finalize();
        }
    }
}
