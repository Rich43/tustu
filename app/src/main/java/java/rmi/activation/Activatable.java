package java.rmi.activation;

import java.rmi.MarshalledObject;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.RemoteServer;
import sun.rmi.server.ActivatableRef;
import sun.rmi.server.ActivatableServerRef;
import sun.rmi.transport.ObjectTable;

/* loaded from: rt.jar:java/rmi/activation/Activatable.class */
public abstract class Activatable extends RemoteServer {
    private ActivationID id;
    private static final long serialVersionUID = -3120617863591563455L;

    protected Activatable(String str, MarshalledObject<?> marshalledObject, boolean z2, int i2) throws ActivationException, RemoteException {
        this.id = exportObject(this, str, marshalledObject, z2, i2);
    }

    protected Activatable(String str, MarshalledObject<?> marshalledObject, boolean z2, int i2, RMIClientSocketFactory rMIClientSocketFactory, RMIServerSocketFactory rMIServerSocketFactory) throws ActivationException, RemoteException {
        this.id = exportObject(this, str, marshalledObject, z2, i2, rMIClientSocketFactory, rMIServerSocketFactory);
    }

    protected Activatable(ActivationID activationID, int i2) throws RemoteException {
        this.id = activationID;
        exportObject(this, activationID, i2);
    }

    protected Activatable(ActivationID activationID, int i2, RMIClientSocketFactory rMIClientSocketFactory, RMIServerSocketFactory rMIServerSocketFactory) throws RemoteException {
        this.id = activationID;
        exportObject(this, activationID, i2, rMIClientSocketFactory, rMIServerSocketFactory);
    }

    protected ActivationID getID() {
        return this.id;
    }

    public static Remote register(ActivationDesc activationDesc) throws ActivationException, RemoteException {
        return ActivatableRef.getStub(activationDesc, ActivationGroup.getSystem().registerObject(activationDesc));
    }

    public static boolean inactive(ActivationID activationID) throws ActivationException, RemoteException {
        return ActivationGroup.currentGroup().inactiveObject(activationID);
    }

    public static void unregister(ActivationID activationID) throws ActivationException, RemoteException {
        ActivationGroup.getSystem().unregisterObject(activationID);
    }

    public static ActivationID exportObject(Remote remote, String str, MarshalledObject<?> marshalledObject, boolean z2, int i2) throws ActivationException, RemoteException {
        return exportObject(remote, str, marshalledObject, z2, i2, null, null);
    }

    public static ActivationID exportObject(Remote remote, String str, MarshalledObject<?> marshalledObject, boolean z2, int i2, RMIClientSocketFactory rMIClientSocketFactory, RMIServerSocketFactory rMIServerSocketFactory) throws ActivationException, RemoteException {
        ActivationDesc activationDesc = new ActivationDesc(remote.getClass().getName(), str, marshalledObject, z2);
        ActivationSystem system = ActivationGroup.getSystem();
        ActivationID activationIDRegisterObject = system.registerObject(activationDesc);
        try {
            exportObject(remote, activationIDRegisterObject, i2, rMIClientSocketFactory, rMIServerSocketFactory);
            ActivationGroup.currentGroup().activeObject(activationIDRegisterObject, remote);
            return activationIDRegisterObject;
        } catch (RemoteException e2) {
            try {
                system.unregisterObject(activationIDRegisterObject);
            } catch (Exception e3) {
            }
            throw e2;
        }
    }

    public static Remote exportObject(Remote remote, ActivationID activationID, int i2) throws RemoteException {
        return exportObject(remote, new ActivatableServerRef(activationID, i2));
    }

    public static Remote exportObject(Remote remote, ActivationID activationID, int i2, RMIClientSocketFactory rMIClientSocketFactory, RMIServerSocketFactory rMIServerSocketFactory) throws RemoteException {
        return exportObject(remote, new ActivatableServerRef(activationID, i2, rMIClientSocketFactory, rMIServerSocketFactory));
    }

    public static boolean unexportObject(Remote remote, boolean z2) throws NoSuchObjectException {
        return ObjectTable.unexportObject(remote, z2);
    }

    private static Remote exportObject(Remote remote, ActivatableServerRef activatableServerRef) throws RemoteException {
        if (remote instanceof Activatable) {
            ((Activatable) remote).ref = activatableServerRef;
        }
        return activatableServerRef.exportObject(remote, null, false);
    }
}
