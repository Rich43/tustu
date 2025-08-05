package sun.management.jmxremote;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.server.RMIServerSocketFactory;
import java.util.Enumeration;

/* loaded from: rt.jar:sun/management/jmxremote/LocalRMIServerSocketFactory.class */
public final class LocalRMIServerSocketFactory implements RMIServerSocketFactory {
    @Override // java.rmi.server.RMIServerSocketFactory
    public ServerSocket createServerSocket(int i2) throws IOException {
        return new ServerSocket(i2) { // from class: sun.management.jmxremote.LocalRMIServerSocketFactory.1
            @Override // java.net.ServerSocket
            public Socket accept() throws IOException {
                Socket socketAccept = super.accept();
                InetAddress inetAddress = socketAccept.getInetAddress();
                if (inetAddress == null) {
                    String str = "";
                    if (socketAccept.isClosed()) {
                        str = " Socket is closed.";
                    } else if (!socketAccept.isConnected()) {
                        str = " Socket is not connected";
                    }
                    try {
                        socketAccept.close();
                    } catch (Exception e2) {
                    }
                    throw new IOException("The server sockets created using the LocalRMIServerSocketFactory only accept connections from clients running on the host where the RMI remote objects have been exported. Couldn't determine client address." + str);
                }
                if (inetAddress.isLoopbackAddress()) {
                    return socketAccept;
                }
                try {
                    Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
                    while (networkInterfaces.hasMoreElements()) {
                        Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement2().getInetAddresses();
                        while (inetAddresses.hasMoreElements()) {
                            if (inetAddresses.nextElement2().equals(inetAddress)) {
                                return socketAccept;
                            }
                        }
                    }
                    try {
                        socketAccept.close();
                    } catch (IOException e3) {
                    }
                    throw new IOException("The server sockets created using the LocalRMIServerSocketFactory only accept connections from clients running on the host where the RMI remote objects have been exported.");
                } catch (SocketException e4) {
                    try {
                        socketAccept.close();
                    } catch (IOException e5) {
                    }
                    throw new IOException("The server sockets created using the LocalRMIServerSocketFactory only accept connections from clients running on the host where the RMI remote objects have been exported.", e4);
                }
            }
        };
    }

    public boolean equals(Object obj) {
        return obj instanceof LocalRMIServerSocketFactory;
    }

    public int hashCode() {
        return getClass().hashCode();
    }
}
