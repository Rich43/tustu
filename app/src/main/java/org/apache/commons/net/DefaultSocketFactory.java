package org.apache.commons.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ServerSocket;
import java.net.Socket;
import javax.net.SocketFactory;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/DefaultSocketFactory.class */
public class DefaultSocketFactory extends SocketFactory {
    private final Proxy connProxy;

    public DefaultSocketFactory() {
        this(null);
    }

    public DefaultSocketFactory(Proxy proxy) {
        this.connProxy = proxy;
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket() throws IOException {
        if (this.connProxy != null) {
            return new Socket(this.connProxy);
        }
        return new Socket();
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(String host, int port) throws IOException {
        if (this.connProxy != null) {
            Socket s2 = new Socket(this.connProxy);
            s2.connect(new InetSocketAddress(host, port));
            return s2;
        }
        return new Socket(host, port);
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(InetAddress address, int port) throws IOException {
        if (this.connProxy != null) {
            Socket s2 = new Socket(this.connProxy);
            s2.connect(new InetSocketAddress(address, port));
            return s2;
        }
        return new Socket(address, port);
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(String host, int port, InetAddress localAddr, int localPort) throws IOException {
        if (this.connProxy != null) {
            Socket s2 = new Socket(this.connProxy);
            s2.bind(new InetSocketAddress(localAddr, localPort));
            s2.connect(new InetSocketAddress(host, port));
            return s2;
        }
        return new Socket(host, port, localAddr, localPort);
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(InetAddress address, int port, InetAddress localAddr, int localPort) throws IOException {
        if (this.connProxy != null) {
            Socket s2 = new Socket(this.connProxy);
            s2.bind(new InetSocketAddress(localAddr, localPort));
            s2.connect(new InetSocketAddress(address, port));
            return s2;
        }
        return new Socket(address, port, localAddr, localPort);
    }

    public ServerSocket createServerSocket(int port) throws IOException {
        return new ServerSocket(port);
    }

    public ServerSocket createServerSocket(int port, int backlog) throws IOException {
        return new ServerSocket(port, backlog);
    }

    public ServerSocket createServerSocket(int port, int backlog, InetAddress bindAddr) throws IOException {
        return new ServerSocket(port, backlog, bindAddr);
    }
}
