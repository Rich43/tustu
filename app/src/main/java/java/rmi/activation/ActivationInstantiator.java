package java.rmi.activation;

import java.rmi.MarshalledObject;
import java.rmi.Remote;
import java.rmi.RemoteException;

/* loaded from: rt.jar:java/rmi/activation/ActivationInstantiator.class */
public interface ActivationInstantiator extends Remote {
    MarshalledObject<? extends Remote> newInstance(ActivationID activationID, ActivationDesc activationDesc) throws ActivationException, RemoteException;
}
