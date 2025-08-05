package com.sun.corba.se.spi.transport;

import com.sun.corba.se.pept.transport.Acceptor;
import com.sun.corba.se.spi.orb.ORB;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/* loaded from: rt.jar:com/sun/corba/se/spi/transport/ORBSocketFactory.class */
public interface ORBSocketFactory {
    void setORB(ORB orb);

    ServerSocket createServerSocket(String str, InetSocketAddress inetSocketAddress) throws IOException;

    Socket createSocket(String str, InetSocketAddress inetSocketAddress) throws IOException;

    void setAcceptedSocketOptions(Acceptor acceptor, ServerSocket serverSocket, Socket socket) throws SocketException;
}
