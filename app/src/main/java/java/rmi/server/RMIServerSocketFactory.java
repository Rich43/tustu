package java.rmi.server;

import java.io.IOException;
import java.net.ServerSocket;

/* loaded from: rt.jar:java/rmi/server/RMIServerSocketFactory.class */
public interface RMIServerSocketFactory {
    ServerSocket createServerSocket(int i2) throws IOException;
}
