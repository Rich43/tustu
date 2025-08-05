package sun.rmi.transport.proxy;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: rt.jar:sun/rmi/transport/proxy/HttpSendInputStream.class */
class HttpSendInputStream extends FilterInputStream {
    HttpSendSocket owner;

    public HttpSendInputStream(InputStream inputStream, HttpSendSocket httpSendSocket) throws IOException {
        super(inputStream);
        this.owner = httpSendSocket;
    }

    public void deactivate() {
        this.in = null;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        if (this.in == null) {
            this.in = this.owner.readNotify();
        }
        return this.in.read();
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        if (i3 == 0) {
            return 0;
        }
        if (this.in == null) {
            this.in = this.owner.readNotify();
        }
        return this.in.read(bArr, i2, i3);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public long skip(long j2) throws IOException {
        if (j2 == 0) {
            return 0L;
        }
        if (this.in == null) {
            this.in = this.owner.readNotify();
        }
        return this.in.skip(j2);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int available() throws IOException {
        if (this.in == null) {
            this.in = this.owner.readNotify();
        }
        return this.in.available();
    }

    @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.owner.close();
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized void mark(int i2) {
        if (this.in == null) {
            try {
                this.in = this.owner.readNotify();
            } catch (IOException e2) {
                return;
            }
        }
        this.in.mark(i2);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized void reset() throws IOException {
        if (this.in == null) {
            this.in = this.owner.readNotify();
        }
        this.in.reset();
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public boolean markSupported() {
        if (this.in == null) {
            try {
                this.in = this.owner.readNotify();
            } catch (IOException e2) {
                return false;
            }
        }
        return this.in.markSupported();
    }
}
