package com.sun.corba.se.impl.legacy.connection;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.iiop.IIOPAddress;
import com.sun.corba.se.spi.ior.iiop.IIOPProfileTemplate;
import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException;
import com.sun.corba.se.spi.legacy.connection.ORBSocketFactory;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.transport.SocketInfo;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/* loaded from: rt.jar:com/sun/corba/se/impl/legacy/connection/DefaultSocketFactory.class */
public class DefaultSocketFactory implements ORBSocketFactory {
    private ORB orb;
    private static ORBUtilSystemException wrapper = ORBUtilSystemException.get(CORBALogDomains.RPC_TRANSPORT);

    public void setORB(ORB orb) {
        this.orb = orb;
    }

    @Override // com.sun.corba.se.spi.legacy.connection.ORBSocketFactory
    public ServerSocket createServerSocket(String str, int i2) throws IOException {
        ServerSocket serverSocket;
        if (!str.equals("IIOP_CLEAR_TEXT")) {
            throw wrapper.defaultCreateServerSocketGivenNonIiopClearText(str);
        }
        if (this.orb.getORBData().acceptorSocketType().equals(ORBConstants.SOCKETCHANNEL)) {
            serverSocket = ServerSocketChannel.open().socket();
        } else {
            serverSocket = new ServerSocket();
        }
        serverSocket.bind(new InetSocketAddress(i2));
        return serverSocket;
    }

    @Override // com.sun.corba.se.spi.legacy.connection.ORBSocketFactory
    public SocketInfo getEndPointInfo(org.omg.CORBA.ORB orb, IOR ior, SocketInfo socketInfo) {
        IIOPAddress primaryAddress = ((IIOPProfileTemplate) ior.getProfile().getTaggedProfileTemplate()).getPrimaryAddress();
        return new EndPointInfoImpl("IIOP_CLEAR_TEXT", primaryAddress.getPort(), primaryAddress.getHost().toLowerCase());
    }

    @Override // com.sun.corba.se.spi.legacy.connection.ORBSocketFactory
    public Socket createSocket(SocketInfo socketInfo) throws GetEndPointInfoAgainException, IOException {
        Socket socket;
        if (this.orb.getORBData().acceptorSocketType().equals(ORBConstants.SOCKETCHANNEL)) {
            socket = SocketChannel.open(new InetSocketAddress(socketInfo.getHost(), socketInfo.getPort())).socket();
        } else {
            socket = new Socket(socketInfo.getHost(), socketInfo.getPort());
        }
        try {
            socket.setTcpNoDelay(true);
        } catch (Exception e2) {
        }
        return socket;
    }
}
