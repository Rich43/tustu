package sun.net.httpserver;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: rt.jar:sun/net/httpserver/FixedLengthInputStream.class */
class FixedLengthInputStream extends LeftOverInputStream {
    private long remaining;

    FixedLengthInputStream(ExchangeImpl exchangeImpl, InputStream inputStream, long j2) {
        super(exchangeImpl, inputStream);
        if (j2 < 0) {
            throw new IllegalArgumentException("Content-Length: " + j2);
        }
        this.remaining = j2;
    }

    @Override // sun.net.httpserver.LeftOverInputStream
    protected int readImpl(byte[] bArr, int i2, int i3) throws IOException {
        this.eof = this.remaining == 0;
        if (this.eof) {
            return -1;
        }
        if (i3 > this.remaining) {
            i3 = (int) this.remaining;
        }
        int i4 = this.in.read(bArr, i2, i3);
        if (i4 > -1) {
            this.remaining -= i4;
            if (this.remaining == 0) {
                this.f13582t.getServerImpl().requestCompleted(this.f13582t.getConnection());
            }
        }
        if (i4 < 0 && !this.eof) {
            throw new IOException("connection closed before all data received");
        }
        return i4;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int available() throws IOException {
        if (this.eof) {
            return 0;
        }
        int iAvailable = this.in.available();
        return ((long) iAvailable) < this.remaining ? iAvailable : (int) this.remaining;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public boolean markSupported() {
        return false;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public void mark(int i2) {
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public void reset() throws IOException {
        throw new IOException("mark/reset not supported");
    }
}
