package sun.rmi.transport.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketImpl;
import java.security.AccessController;
import java.security.PrivilegedAction;

/* loaded from: rt.jar:sun/rmi/transport/proxy/WrappedSocket.class */
class WrappedSocket extends Socket {
    protected Socket socket;
    protected InputStream in;
    protected OutputStream out;

    public WrappedSocket(Socket socket, InputStream inputStream, OutputStream outputStream) throws IOException {
        super((SocketImpl) null);
        this.in = null;
        this.out = null;
        this.socket = socket;
        this.in = inputStream;
        this.out = outputStream;
    }

    @Override // java.net.Socket
    public InetAddress getInetAddress() {
        return this.socket.getInetAddress();
    }

    @Override // java.net.Socket
    public InetAddress getLocalAddress() {
        return (InetAddress) AccessController.doPrivileged(new PrivilegedAction<InetAddress>() { // from class: sun.rmi.transport.proxy.WrappedSocket.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public InetAddress run2() {
                return WrappedSocket.this.socket.getLocalAddress();
            }
        });
    }

    @Override // java.net.Socket
    public int getPort() {
        return this.socket.getPort();
    }

    @Override // java.net.Socket
    public int getLocalPort() {
        return this.socket.getLocalPort();
    }

    @Override // java.net.Socket
    public InputStream getInputStream() throws IOException {
        if (this.in == null) {
            this.in = this.socket.getInputStream();
        }
        return this.in;
    }

    @Override // java.net.Socket
    public OutputStream getOutputStream() throws IOException {
        if (this.out == null) {
            this.out = this.socket.getOutputStream();
        }
        return this.out;
    }

    @Override // java.net.Socket
    public void setTcpNoDelay(boolean z2) throws SocketException {
        this.socket.setTcpNoDelay(z2);
    }

    @Override // java.net.Socket
    public boolean getTcpNoDelay() throws SocketException {
        return this.socket.getTcpNoDelay();
    }

    @Override // java.net.Socket
    public void setSoLinger(boolean z2, int i2) throws SocketException {
        this.socket.setSoLinger(z2, i2);
    }

    @Override // java.net.Socket
    public int getSoLinger() throws SocketException {
        return this.socket.getSoLinger();
    }

    @Override // java.net.Socket
    public synchronized void setSoTimeout(int i2) throws SocketException {
        this.socket.setSoTimeout(i2);
    }

    @Override // java.net.Socket
    public synchronized int getSoTimeout() throws SocketException {
        return this.socket.getSoTimeout();
    }

    @Override // java.net.Socket, java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        this.socket.close();
    }

    @Override // java.net.Socket
    public String toString() {
        return "Wrapped" + this.socket.toString();
    }
}
