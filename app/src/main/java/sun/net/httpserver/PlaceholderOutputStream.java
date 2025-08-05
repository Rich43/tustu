package sun.net.httpserver;

import java.io.IOException;
import java.io.OutputStream;

/* compiled from: ExchangeImpl.java */
/* loaded from: rt.jar:sun/net/httpserver/PlaceholderOutputStream.class */
class PlaceholderOutputStream extends OutputStream {
    OutputStream wrapped;

    PlaceholderOutputStream(OutputStream outputStream) {
        this.wrapped = outputStream;
    }

    void setWrappedStream(OutputStream outputStream) {
        this.wrapped = outputStream;
    }

    boolean isWrapped() {
        return this.wrapped != null;
    }

    private void checkWrap() throws IOException {
        if (this.wrapped == null) {
            throw new IOException("response headers not sent yet");
        }
    }

    @Override // java.io.OutputStream
    public void write(int i2) throws IOException {
        checkWrap();
        this.wrapped.write(i2);
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr) throws IOException {
        checkWrap();
        this.wrapped.write(bArr);
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) throws IOException {
        checkWrap();
        this.wrapped.write(bArr, i2, i3);
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        checkWrap();
        this.wrapped.flush();
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        checkWrap();
        this.wrapped.close();
    }
}
