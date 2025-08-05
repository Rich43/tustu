package java.rmi.activation;

import java.rmi.Remote;
import java.rmi.RemoteException;

/* loaded from: rt.jar:java/rmi/activation/ActivationSystem.class */
public interface ActivationSystem extends Remote {
    public static final int SYSTEM_PORT = 1098;

    ActivationID registerObject(ActivationDesc activationDesc) throws ActivationException, RemoteException;

    void unregisterObject(ActivationID activationID) throws ActivationException, RemoteException;

    ActivationGroupID registerGroup(ActivationGroupDesc activationGroupDesc) throws ActivationException, RemoteException;

    ActivationMonitor activeGroup(ActivationGroupID activationGroupID, ActivationInstantiator activationInstantiator, long j2) throws ActivationException, RemoteException;

    void unregisterGroup(ActivationGroupID activationGroupID) throws ActivationException, RemoteException;

    void shutdown() throws RemoteException;

    ActivationDesc setActivationDesc(ActivationID activationID, ActivationDesc activationDesc) throws ActivationException, RemoteException;

    ActivationGroupDesc setActivationGroupDesc(ActivationGroupID activationGroupID, ActivationGroupDesc activationGroupDesc) throws ActivationException, RemoteException;

    ActivationDesc getActivationDesc(ActivationID activationID) throws ActivationException, RemoteException;

    ActivationGroupDesc getActivationGroupDesc(ActivationGroupID activationGroupID) throws ActivationException, RemoteException;
}
