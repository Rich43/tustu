package java.rmi.activation;

import java.rmi.MarshalledObject;
import java.rmi.Remote;
import java.rmi.RemoteException;

/* loaded from: rt.jar:java/rmi/activation/ActivationMonitor.class */
public interface ActivationMonitor extends Remote {
    void inactiveObject(ActivationID activationID) throws UnknownObjectException, RemoteException;

    void activeObject(ActivationID activationID, MarshalledObject<? extends Remote> marshalledObject) throws UnknownObjectException, RemoteException;

    void inactiveGroup(ActivationGroupID activationGroupID, long j2) throws UnknownGroupException, RemoteException;
}
