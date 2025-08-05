package sun.rmi.transport.proxy;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

/* loaded from: rt.jar:sun/rmi/transport/proxy/HttpReceiveSocket.class */
public class HttpReceiveSocket extends WrappedSocket implements RMISocketInfo {
    private boolean headerSent;

    @Override // sun.rmi.transport.proxy.WrappedSocket, java.net.Socket
    public /* bridge */ /* synthetic */ int getSoTimeout() throws SocketException {
        return super.getSoTimeout();
    }

    @Override // sun.rmi.transport.proxy.WrappedSocket, java.net.Socket
    public /* bridge */ /* synthetic */ void setSoTimeout(int i2) throws SocketException {
        super.setSoTimeout(i2);
    }

    @Override // sun.rmi.transport.proxy.WrappedSocket, java.net.Socket
    public /* bridge */ /* synthetic */ int getSoLinger() throws SocketException {
        return super.getSoLinger();
    }

    @Override // sun.rmi.transport.proxy.WrappedSocket, java.net.Socket
    public /* bridge */ /* synthetic */ void setSoLinger(boolean z2, int i2) throws SocketException {
        super.setSoLinger(z2, i2);
    }

    @Override // sun.rmi.transport.proxy.WrappedSocket, java.net.Socket
    public /* bridge */ /* synthetic */ boolean getTcpNoDelay() throws SocketException {
        return super.getTcpNoDelay();
    }

    @Override // sun.rmi.transport.proxy.WrappedSocket, java.net.Socket
    public /* bridge */ /* synthetic */ void setTcpNoDelay(boolean z2) throws SocketException {
        super.setTcpNoDelay(z2);
    }

    @Override // sun.rmi.transport.proxy.WrappedSocket, java.net.Socket
    public /* bridge */ /* synthetic */ InputStream getInputStream() throws IOException {
        return super.getInputStream();
    }

    @Override // sun.rmi.transport.proxy.WrappedSocket, java.net.Socket
    public /* bridge */ /* synthetic */ int getLocalPort() {
        return super.getLocalPort();
    }

    @Override // sun.rmi.transport.proxy.WrappedSocket, java.net.Socket
    public /* bridge */ /* synthetic */ int getPort() {
        return super.getPort();
    }

    @Override // sun.rmi.transport.proxy.WrappedSocket, java.net.Socket
    public /* bridge */ /* synthetic */ InetAddress getLocalAddress() {
        return super.getLocalAddress();
    }

    public HttpReceiveSocket(Socket socket, InputStream inputStream, OutputStream outputStream) throws IOException {
        super(socket, inputStream, outputStream);
        this.headerSent = false;
        this.in = new HttpInputStream(inputStream != null ? inputStream : socket.getInputStream());
        this.out = outputStream != null ? outputStream : socket.getOutputStream();
    }

    @Override // sun.rmi.transport.proxy.RMISocketInfo
    public boolean isReusable() {
        return false;
    }

    @Override // sun.rmi.transport.proxy.WrappedSocket, java.net.Socket
    public InetAddress getInetAddress() {
        return null;
    }

    @Override // sun.rmi.transport.proxy.WrappedSocket, java.net.Socket
    public OutputStream getOutputStream() throws IOException {
        if (!this.headerSent) {
            DataOutputStream dataOutputStream = new DataOutputStream(this.out);
            dataOutputStream.writeBytes("HTTP/1.0 200 OK\r\n");
            dataOutputStream.flush();
            this.headerSent = true;
            this.out = new HttpOutputStream(this.out);
        }
        return this.out;
    }

    @Override // sun.rmi.transport.proxy.WrappedSocket, java.net.Socket, java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        getOutputStream().close();
        this.socket.close();
    }

    @Override // sun.rmi.transport.proxy.WrappedSocket, java.net.Socket
    public String toString() {
        return "HttpReceive" + this.socket.toString();
    }
}
