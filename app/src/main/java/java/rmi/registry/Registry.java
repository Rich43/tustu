package java.rmi.registry;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/* loaded from: rt.jar:java/rmi/registry/Registry.class */
public interface Registry extends Remote {
    public static final int REGISTRY_PORT = 1099;

    Remote lookup(String str) throws NotBoundException, RemoteException;

    void bind(String str, Remote remote) throws AlreadyBoundException, RemoteException;

    void unbind(String str) throws NotBoundException, RemoteException;

    void rebind(String str, Remote remote) throws RemoteException;

    String[] list() throws RemoteException;
}
