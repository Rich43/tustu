package sun.rmi.transport.proxy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.RMISocketFactory;

/* loaded from: rt.jar:sun/rmi/transport/proxy/RMIDirectSocketFactory.class */
public class RMIDirectSocketFactory extends RMISocketFactory {
    @Override // java.rmi.server.RMISocketFactory, java.rmi.server.RMIClientSocketFactory
    public Socket createSocket(String str, int i2) throws IOException {
        return new Socket(str, i2);
    }

    @Override // java.rmi.server.RMISocketFactory, java.rmi.server.RMIServerSocketFactory
    public ServerSocket createServerSocket(int i2) throws IOException {
        return new ServerSocket(i2);
    }
}
