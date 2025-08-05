package sun.rmi.transport.proxy;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: rt.jar:sun/rmi/transport/proxy/HttpOutputStream.class */
class HttpOutputStream extends ByteArrayOutputStream {
    protected OutputStream out;
    boolean responseSent = false;
    private static byte[] emptyData = {0};

    public HttpOutputStream(OutputStream outputStream) {
        this.out = outputStream;
    }

    @Override // java.io.ByteArrayOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        if (!this.responseSent) {
            if (size() == 0) {
                write(emptyData);
            }
            DataOutputStream dataOutputStream = new DataOutputStream(this.out);
            dataOutputStream.writeBytes("Content-type: application/octet-stream\r\n");
            dataOutputStream.writeBytes("Content-length: " + size() + "\r\n");
            dataOutputStream.writeBytes("\r\n");
            writeTo(dataOutputStream);
            dataOutputStream.flush();
            reset();
            this.responseSent = true;
        }
    }
}
