package java.rmi.dgc;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ObjID;

/* loaded from: rt.jar:java/rmi/dgc/DGC.class */
public interface DGC extends Remote {
    Lease dirty(ObjID[] objIDArr, long j2, Lease lease) throws RemoteException;

    void clean(ObjID[] objIDArr, long j2, VMID vmid, boolean z2) throws RemoteException;
}
