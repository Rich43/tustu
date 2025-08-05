package sun.net.httpserver;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: rt.jar:sun/net/httpserver/LeftOverInputStream.class */
abstract class LeftOverInputStream extends FilterInputStream {

    /* renamed from: t, reason: collision with root package name */
    ExchangeImpl f13582t;
    ServerImpl server;
    protected boolean closed;
    protected boolean eof;
    byte[] one;
    static final /* synthetic */ boolean $assertionsDisabled;

    protected abstract int readImpl(byte[] bArr, int i2, int i3) throws IOException;

    static {
        $assertionsDisabled = !LeftOverInputStream.class.desiredAssertionStatus();
    }

    public LeftOverInputStream(ExchangeImpl exchangeImpl, InputStream inputStream) {
        super(inputStream);
        this.closed = false;
        this.eof = false;
        this.one = new byte[1];
        this.f13582t = exchangeImpl;
        this.server = exchangeImpl.getServerImpl();
    }

    public boolean isDataBuffered() throws IOException {
        if ($assertionsDisabled || this.eof) {
            return super.available() > 0;
        }
        throw new AssertionError();
    }

    @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.closed) {
            return;
        }
        this.closed = true;
        if (!this.eof) {
            this.eof = drain(ServerConfig.getDrainAmount());
        }
    }

    public boolean isClosed() {
        return this.closed;
    }

    public boolean isEOF() {
        return this.eof;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized int read() throws IOException {
        if (this.closed) {
            throw new IOException("Stream is closed");
        }
        int impl = readImpl(this.one, 0, 1);
        if (impl == -1 || impl == 0) {
            return impl;
        }
        return this.one[0] & 255;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized int read(byte[] bArr, int i2, int i3) throws IOException {
        if (this.closed) {
            throw new IOException("Stream is closed");
        }
        return readImpl(bArr, i2, i3);
    }

    public boolean drain(long j2) throws IOException {
        byte[] bArr = new byte[2048];
        while (j2 > 0) {
            long impl = readImpl(bArr, 0, 2048);
            if (impl == -1) {
                this.eof = true;
                return true;
            }
            j2 -= impl;
        }
        return false;
    }
}
