package sun.net.httpserver;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: rt.jar:sun/net/httpserver/FixedLengthOutputStream.class */
class FixedLengthOutputStream extends FilterOutputStream {
    private long remaining;
    private boolean eof;
    private boolean closed;

    /* renamed from: t, reason: collision with root package name */
    ExchangeImpl f13580t;

    FixedLengthOutputStream(ExchangeImpl exchangeImpl, OutputStream outputStream, long j2) {
        super(outputStream);
        this.eof = false;
        this.closed = false;
        if (j2 < 0) {
            throw new IllegalArgumentException("Content-Length: " + j2);
        }
        this.f13580t = exchangeImpl;
        this.remaining = j2;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int i2) throws IOException {
        if (this.closed) {
            throw new IOException("stream closed");
        }
        this.eof = this.remaining == 0;
        if (this.eof) {
            throw new StreamClosedException();
        }
        this.out.write(i2);
        this.remaining--;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) throws IOException {
        if (this.closed) {
            throw new IOException("stream closed");
        }
        this.eof = this.remaining == 0;
        if (this.eof) {
            throw new StreamClosedException();
        }
        if (i3 > this.remaining) {
            throw new IOException("too many bytes to write to stream");
        }
        this.out.write(bArr, i2, i3);
        this.remaining -= i3;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.closed) {
            return;
        }
        this.closed = true;
        if (this.remaining > 0) {
            this.f13580t.close();
            throw new IOException("insufficient bytes written to stream");
        }
        flush();
        this.eof = true;
        LeftOverInputStream originalInputStream = this.f13580t.getOriginalInputStream();
        if (!originalInputStream.isClosed()) {
            try {
                originalInputStream.close();
            } catch (IOException e2) {
            }
        }
        this.f13580t.getHttpContext().getServerImpl().addEvent(new WriteFinishedEvent(this.f13580t));
    }
}
