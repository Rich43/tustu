package sun.rmi.transport.proxy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.rmi.server.RMISocketFactory;

/* loaded from: rt.jar:sun/rmi/transport/proxy/RMIHttpToPortSocketFactory.class */
public class RMIHttpToPortSocketFactory extends RMISocketFactory {
    @Override // java.rmi.server.RMISocketFactory, java.rmi.server.RMIClientSocketFactory
    public Socket createSocket(String str, int i2) throws IOException {
        return new HttpSendSocket(str, i2, new URL("http", str, i2, "/"));
    }

    @Override // java.rmi.server.RMISocketFactory, java.rmi.server.RMIServerSocketFactory
    public ServerSocket createServerSocket(int i2) throws IOException {
        return new HttpAwareServerSocket(i2);
    }
}
