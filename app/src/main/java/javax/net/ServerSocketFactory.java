package javax.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;

/* loaded from: rt.jar:javax/net/ServerSocketFactory.class */
public abstract class ServerSocketFactory {
    private static ServerSocketFactory theFactory;

    public abstract ServerSocket createServerSocket(int i2) throws IOException;

    public abstract ServerSocket createServerSocket(int i2, int i3) throws IOException;

    public abstract ServerSocket createServerSocket(int i2, int i3, InetAddress inetAddress) throws IOException;

    protected ServerSocketFactory() {
    }

    public static ServerSocketFactory getDefault() {
        synchronized (ServerSocketFactory.class) {
            if (theFactory == null) {
                theFactory = new DefaultServerSocketFactory();
            }
        }
        return theFactory;
    }

    public ServerSocket createServerSocket() throws IOException {
        throw new SocketException("Unbound server sockets not implemented");
    }
}
