package java.rmi.registry;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.ObjID;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import sun.rmi.registry.RegistryImpl;
import sun.rmi.server.UnicastRef;
import sun.rmi.server.UnicastRef2;
import sun.rmi.server.Util;
import sun.rmi.transport.Endpoint;
import sun.rmi.transport.LiveRef;
import sun.rmi.transport.tcp.TCPEndpoint;

/* loaded from: rt.jar:java/rmi/registry/LocateRegistry.class */
public final class LocateRegistry {
    private LocateRegistry() {
    }

    public static Registry getRegistry() throws RemoteException {
        return getRegistry(null, Registry.REGISTRY_PORT);
    }

    public static Registry getRegistry(int i2) throws RemoteException {
        return getRegistry(null, i2);
    }

    public static Registry getRegistry(String str) throws RemoteException {
        return getRegistry(str, Registry.REGISTRY_PORT);
    }

    public static Registry getRegistry(String str, int i2) throws RemoteException {
        return getRegistry(str, i2, null);
    }

    public static Registry getRegistry(String str, int i2, RMIClientSocketFactory rMIClientSocketFactory) throws RemoteException {
        if (i2 <= 0) {
            i2 = 1099;
        }
        if (str == null || str.length() == 0) {
            try {
                str = InetAddress.getLocalHost().getHostAddress();
            } catch (Exception e2) {
                str = "";
            }
        }
        LiveRef liveRef = new LiveRef(new ObjID(0), (Endpoint) new TCPEndpoint(str, i2, rMIClientSocketFactory, null), false);
        return (Registry) Util.createProxy(RegistryImpl.class, rMIClientSocketFactory == null ? new UnicastRef(liveRef) : new UnicastRef2(liveRef), false);
    }

    public static Registry createRegistry(int i2) throws RemoteException {
        return new RegistryImpl(i2);
    }

    public static Registry createRegistry(int i2, RMIClientSocketFactory rMIClientSocketFactory, RMIServerSocketFactory rMIServerSocketFactory) throws RemoteException {
        return new RegistryImpl(i2, rMIClientSocketFactory, rMIServerSocketFactory);
    }
}
