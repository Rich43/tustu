package sun.net.httpserver;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: rt.jar:sun/net/httpserver/UndefLengthOutputStream.class */
class UndefLengthOutputStream extends FilterOutputStream {
    private boolean closed;

    /* renamed from: t, reason: collision with root package name */
    ExchangeImpl f13583t;

    UndefLengthOutputStream(ExchangeImpl exchangeImpl, OutputStream outputStream) {
        super(outputStream);
        this.closed = false;
        this.f13583t = exchangeImpl;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int i2) throws IOException {
        if (this.closed) {
            throw new IOException("stream closed");
        }
        this.out.write(i2);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) throws IOException {
        if (this.closed) {
            throw new IOException("stream closed");
        }
        this.out.write(bArr, i2, i3);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.closed) {
            return;
        }
        this.closed = true;
        flush();
        LeftOverInputStream originalInputStream = this.f13583t.getOriginalInputStream();
        if (!originalInputStream.isClosed()) {
            try {
                originalInputStream.close();
            } catch (IOException e2) {
            }
        }
        this.f13583t.getHttpContext().getServerImpl().addEvent(new WriteFinishedEvent(this.f13583t));
    }
}
