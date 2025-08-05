package java.rmi.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import sun.rmi.server.UnicastServerRef;
import sun.rmi.server.UnicastServerRef2;
import sun.rmi.transport.ObjectTable;

/* loaded from: rt.jar:java/rmi/server/UnicastRemoteObject.class */
public class UnicastRemoteObject extends RemoteServer {
    private int port;
    private RMIClientSocketFactory csf;
    private RMIServerSocketFactory ssf;
    private static final long serialVersionUID = 4974527148936298033L;

    protected UnicastRemoteObject() throws RemoteException {
        this(0);
    }

    protected UnicastRemoteObject(int i2) throws RemoteException {
        this.port = 0;
        this.csf = null;
        this.ssf = null;
        this.port = i2;
        exportObject(this, i2);
    }

    protected UnicastRemoteObject(int i2, RMIClientSocketFactory rMIClientSocketFactory, RMIServerSocketFactory rMIServerSocketFactory) throws RemoteException {
        this.port = 0;
        this.csf = null;
        this.ssf = null;
        this.port = i2;
        this.csf = rMIClientSocketFactory;
        this.ssf = rMIServerSocketFactory;
        exportObject(this, i2, rMIClientSocketFactory, rMIServerSocketFactory);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        reexport();
    }

    public Object clone() throws CloneNotSupportedException {
        try {
            UnicastRemoteObject unicastRemoteObject = (UnicastRemoteObject) super.clone();
            unicastRemoteObject.reexport();
            return unicastRemoteObject;
        } catch (RemoteException e2) {
            throw new ServerCloneException("Clone failed", e2);
        }
    }

    private void reexport() throws RemoteException {
        if (this.csf == null && this.ssf == null) {
            exportObject(this, this.port);
        } else {
            exportObject(this, this.port, this.csf, this.ssf);
        }
    }

    @Deprecated
    public static RemoteStub exportObject(Remote remote) throws RemoteException {
        return (RemoteStub) exportObject(remote, new UnicastServerRef(true));
    }

    public static Remote exportObject(Remote remote, int i2) throws RemoteException {
        return exportObject(remote, new UnicastServerRef(i2));
    }

    public static Remote exportObject(Remote remote, int i2, RMIClientSocketFactory rMIClientSocketFactory, RMIServerSocketFactory rMIServerSocketFactory) throws RemoteException {
        return exportObject(remote, new UnicastServerRef2(i2, rMIClientSocketFactory, rMIServerSocketFactory));
    }

    public static boolean unexportObject(Remote remote, boolean z2) throws NoSuchObjectException {
        return ObjectTable.unexportObject(remote, z2);
    }

    private static Remote exportObject(Remote remote, UnicastServerRef unicastServerRef) throws RemoteException {
        if (remote instanceof UnicastRemoteObject) {
            ((UnicastRemoteObject) remote).ref = unicastServerRef;
        }
        return unicastServerRef.exportObject(remote, null, false);
    }
}
