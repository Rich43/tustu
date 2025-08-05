package org.bluecove.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

/* loaded from: bluecove-bluez-2.1.1.jar:org/bluecove/socket/LocalServerSocket.class */
public class LocalServerSocket extends ServerSocket {
    private LocalSocketImpl impl;

    public LocalServerSocket() throws IOException {
        this.impl = new LocalSocketImpl();
        this.impl.create(true);
    }

    public LocalServerSocket(SocketAddress endpoint) throws IOException {
        this();
        bind(endpoint);
    }

    public LocalServerSocket(SocketAddress endpoint, int backlog) throws IOException {
        this();
        bind(endpoint, backlog);
    }

    @Override // java.net.ServerSocket
    public void bind(SocketAddress endpoint, int backlog) throws IOException {
        this.impl.bind(endpoint);
        this.impl.listen(backlog);
    }

    @Override // java.net.ServerSocket
    public Socket accept() throws IOException {
        LocalSocketImpl clientImpl = new LocalSocketImpl();
        this.impl.accept(clientImpl);
        return new LocalSocket(clientImpl);
    }

    @Override // java.net.ServerSocket
    public SocketAddress getLocalSocketAddress() {
        return this.impl.getSocketAddress();
    }

    @Override // java.net.ServerSocket, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        SocketAddress endpoint = getLocalSocketAddress();
        this.impl.close();
        if (endpoint != null && !((LocalSocketAddress) endpoint).isAbstractNamespace()) {
            this.impl.unlink(((LocalSocketAddress) endpoint).getName());
        }
    }

    @Override // java.net.ServerSocket
    public boolean isBound() {
        return this.impl.isBound();
    }

    @Override // java.net.ServerSocket
    public boolean isClosed() {
        return this.impl.isClosed();
    }

    @Override // java.net.ServerSocket
    public String toString() {
        if (isBound()) {
            return "LocalServerSocket[" + ((Object) getLocalSocketAddress()) + "]";
        }
        return "LocalServerSocket[unbound]";
    }

    public void setReceiveCredentials(boolean on) throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is already closed");
        }
        this.impl.setOption(2, Integer.valueOf(on ? 1 : 0));
    }

    public boolean getReceiveCredentials() throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is already closed");
        }
        Object value = this.impl.getOption(2);
        return (value instanceof Integer) && ((Integer) value).intValue() > 0;
    }
}
