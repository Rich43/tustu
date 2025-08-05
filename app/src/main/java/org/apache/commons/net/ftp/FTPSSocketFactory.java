package org.apache.commons.net.ftp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/ftp/FTPSSocketFactory.class */
public class FTPSSocketFactory extends SocketFactory {
    private final SSLContext context;

    public FTPSSocketFactory(SSLContext context) {
        this.context = context;
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket() throws IOException {
        return this.context.getSocketFactory().createSocket();
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(String address, int port) throws IOException {
        return this.context.getSocketFactory().createSocket(address, port);
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(InetAddress address, int port) throws IOException {
        return this.context.getSocketFactory().createSocket(address, port);
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(String address, int port, InetAddress localAddress, int localPort) throws IOException {
        return this.context.getSocketFactory().createSocket(address, port, localAddress, localPort);
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        return this.context.getSocketFactory().createSocket(address, port, localAddress, localPort);
    }

    @Deprecated
    public ServerSocket createServerSocket(int port) throws IOException {
        return init(this.context.getServerSocketFactory().createServerSocket(port));
    }

    @Deprecated
    public ServerSocket createServerSocket(int port, int backlog) throws IOException {
        return init(this.context.getServerSocketFactory().createServerSocket(port, backlog));
    }

    @Deprecated
    public ServerSocket createServerSocket(int port, int backlog, InetAddress ifAddress) throws IOException {
        return init(this.context.getServerSocketFactory().createServerSocket(port, backlog, ifAddress));
    }

    @Deprecated
    public ServerSocket init(ServerSocket socket) throws IOException {
        ((SSLServerSocket) socket).setUseClientMode(true);
        return socket;
    }
}
