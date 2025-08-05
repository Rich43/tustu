package sun.rmi.transport.proxy;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: rt.jar:sun/rmi/transport/proxy/HttpSendOutputStream.class */
class HttpSendOutputStream extends FilterOutputStream {
    HttpSendSocket owner;

    public HttpSendOutputStream(OutputStream outputStream, HttpSendSocket httpSendSocket) throws IOException {
        super(outputStream);
        this.owner = httpSendSocket;
    }

    public void deactivate() {
        this.out = null;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int i2) throws IOException {
        if (this.out == null) {
            this.out = this.owner.writeNotify();
        }
        this.out.write(i2);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) throws IOException {
        if (i3 == 0) {
            return;
        }
        if (this.out == null) {
            this.out = this.owner.writeNotify();
        }
        this.out.write(bArr, i2, i3);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        if (this.out != null) {
            this.out.flush();
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        flush();
        this.owner.close();
    }
}
