package java.rmi.activation;

import java.rmi.MarshalledObject;
import java.rmi.Remote;
import java.rmi.RemoteException;

/* loaded from: rt.jar:java/rmi/activation/Activator.class */
public interface Activator extends Remote {
    MarshalledObject<? extends Remote> activate(ActivationID activationID, boolean z2) throws ActivationException, RemoteException;
}
