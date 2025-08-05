package com.sun.corba.se.impl.transport;

import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.pept.transport.Acceptor;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.transport.ORBSocketFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;

/* loaded from: rt.jar:com/sun/corba/se/impl/transport/DefaultSocketFactoryImpl.class */
public class DefaultSocketFactoryImpl implements ORBSocketFactory {
    private ORB orb;
    private static final boolean keepAlive = ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: com.sun.corba.se.impl.transport.DefaultSocketFactoryImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public Boolean run2() {
            String property = System.getProperty("com.sun.CORBA.transport.enableTcpKeepAlive");
            if (property != null) {
                return new Boolean(!"false".equalsIgnoreCase(property));
            }
            return Boolean.FALSE;
        }
    })).booleanValue();

    @Override // com.sun.corba.se.spi.transport.ORBSocketFactory
    public void setORB(ORB orb) {
        this.orb = orb;
    }

    @Override // com.sun.corba.se.spi.transport.ORBSocketFactory
    public ServerSocket createServerSocket(String str, InetSocketAddress inetSocketAddress) throws IOException {
        ServerSocket serverSocket;
        if (this.orb.getORBData().acceptorSocketType().equals(ORBConstants.SOCKETCHANNEL)) {
            serverSocket = ServerSocketChannel.open().socket();
        } else {
            serverSocket = new ServerSocket();
        }
        serverSocket.bind(inetSocketAddress);
        return serverSocket;
    }

    @Override // com.sun.corba.se.spi.transport.ORBSocketFactory
    public Socket createSocket(String str, InetSocketAddress inetSocketAddress) throws IOException {
        Socket socket;
        if (this.orb.getORBData().connectionSocketType().equals(ORBConstants.SOCKETCHANNEL)) {
            socket = SocketChannel.open(inetSocketAddress).socket();
        } else {
            socket = new Socket(inetSocketAddress.getHostName(), inetSocketAddress.getPort());
        }
        socket.setTcpNoDelay(true);
        if (keepAlive) {
            socket.setKeepAlive(true);
        }
        return socket;
    }

    @Override // com.sun.corba.se.spi.transport.ORBSocketFactory
    public void setAcceptedSocketOptions(Acceptor acceptor, ServerSocket serverSocket, Socket socket) throws SocketException {
        socket.setTcpNoDelay(true);
        if (keepAlive) {
            socket.setKeepAlive(true);
        }
    }
}
