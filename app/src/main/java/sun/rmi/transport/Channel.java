package sun.rmi.transport;

import java.rmi.RemoteException;

/* loaded from: rt.jar:sun/rmi/transport/Channel.class */
public interface Channel {
    Connection newConnection() throws RemoteException;

    Endpoint getEndpoint();

    void free(Connection connection, boolean z2) throws RemoteException;
}
