package java.rmi.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

@Deprecated
/* loaded from: rt.jar:java/rmi/server/ServerRef.class */
public interface ServerRef extends RemoteRef {
    public static final long serialVersionUID = -4557750989390278438L;

    RemoteStub exportObject(Remote remote, Object obj) throws RemoteException;

    String getClientHost() throws ServerNotActiveException;
}
