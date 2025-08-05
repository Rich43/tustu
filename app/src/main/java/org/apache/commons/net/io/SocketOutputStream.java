package org.apache.commons.net.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/io/SocketOutputStream.class */
public class SocketOutputStream extends FilterOutputStream {
    private final Socket __socket;

    public SocketOutputStream(Socket socket, OutputStream stream) {
        super(stream);
        this.__socket = socket;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] buffer, int offset, int length) throws IOException {
        this.out.write(buffer, offset, length);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        super.close();
        this.__socket.close();
    }
}
