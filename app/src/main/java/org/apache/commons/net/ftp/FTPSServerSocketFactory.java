package org.apache.commons.net.ftp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/ftp/FTPSServerSocketFactory.class */
public class FTPSServerSocketFactory extends ServerSocketFactory {
    private final SSLContext context;

    public FTPSServerSocketFactory(SSLContext context) {
        this.context = context;
    }

    @Override // javax.net.ServerSocketFactory
    public ServerSocket createServerSocket() throws IOException {
        return init(this.context.getServerSocketFactory().createServerSocket());
    }

    @Override // javax.net.ServerSocketFactory
    public ServerSocket createServerSocket(int port) throws IOException {
        return init(this.context.getServerSocketFactory().createServerSocket(port));
    }

    @Override // javax.net.ServerSocketFactory
    public ServerSocket createServerSocket(int port, int backlog) throws IOException {
        return init(this.context.getServerSocketFactory().createServerSocket(port, backlog));
    }

    @Override // javax.net.ServerSocketFactory
    public ServerSocket createServerSocket(int port, int backlog, InetAddress ifAddress) throws IOException {
        return init(this.context.getServerSocketFactory().createServerSocket(port, backlog, ifAddress));
    }

    public ServerSocket init(ServerSocket socket) {
        ((SSLServerSocket) socket).setUseClientMode(true);
        return socket;
    }
}
