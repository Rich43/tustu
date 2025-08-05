package javax.management.remote.rmi;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/* loaded from: rt.jar:javax/management/remote/rmi/RMIServer.class */
public interface RMIServer extends Remote {
    String getVersion() throws RemoteException;

    RMIConnection newClient(Object obj) throws IOException;
}
