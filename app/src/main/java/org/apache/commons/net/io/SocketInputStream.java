package org.apache.commons.net.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/io/SocketInputStream.class */
public class SocketInputStream extends FilterInputStream {
    private final Socket __socket;

    public SocketInputStream(Socket socket, InputStream stream) {
        super(stream);
        this.__socket = socket;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        super.close();
        this.__socket.close();
    }
}
