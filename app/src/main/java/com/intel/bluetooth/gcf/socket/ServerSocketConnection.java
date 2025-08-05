package com.intel.bluetooth.gcf.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import javax.microedition.io.StreamConnection;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/gcf/socket/ServerSocketConnection.class */
public class ServerSocketConnection implements javax.microedition.io.ServerSocketConnection {
    private ServerSocket serverSocket;

    public ServerSocketConnection(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    @Override // javax.microedition.io.ServerSocketConnection
    public String getLocalAddress() throws IOException {
        InetAddress localHost = InetAddress.getLocalHost();
        return localHost.getHostAddress();
    }

    @Override // javax.microedition.io.ServerSocketConnection
    public int getLocalPort() throws IOException {
        return this.serverSocket.getLocalPort();
    }

    @Override // javax.microedition.io.StreamConnectionNotifier
    public StreamConnection acceptAndOpen() throws IOException {
        return new SocketConnection(this.serverSocket.accept());
    }

    @Override // javax.microedition.io.Connection
    public void close() throws IOException {
        this.serverSocket.close();
    }
}
