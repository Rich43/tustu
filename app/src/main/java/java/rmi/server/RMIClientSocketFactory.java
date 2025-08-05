package java.rmi.server;

import java.io.IOException;
import java.net.Socket;

/* loaded from: rt.jar:java/rmi/server/RMIClientSocketFactory.class */
public interface RMIClientSocketFactory {
    Socket createSocket(String str, int i2) throws IOException;
}
