package javax.rmi.CORBA;

import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/* loaded from: rt.jar:javax/rmi/CORBA/PortableRemoteObjectDelegate.class */
public interface PortableRemoteObjectDelegate {
    void exportObject(Remote remote) throws RemoteException;

    Remote toStub(Remote remote) throws NoSuchObjectException;

    void unexportObject(Remote remote) throws NoSuchObjectException;

    Object narrow(Object obj, Class cls) throws ClassCastException;

    void connect(Remote remote, Remote remote2) throws RemoteException;
}
