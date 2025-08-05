package com.sun.corba.se.spi.legacy.connection;

import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.transport.SocketInfo;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.omg.CORBA.ORB;

/* loaded from: rt.jar:com/sun/corba/se/spi/legacy/connection/ORBSocketFactory.class */
public interface ORBSocketFactory {
    public static final String IIOP_CLEAR_TEXT = "IIOP_CLEAR_TEXT";

    ServerSocket createServerSocket(String str, int i2) throws IOException;

    SocketInfo getEndPointInfo(ORB orb, IOR ior, SocketInfo socketInfo);

    Socket createSocket(SocketInfo socketInfo) throws GetEndPointInfoAgainException, IOException;
}
