package com.sun.corba.se.impl.javax.rmi;

import com.sun.corba.se.impl.util.RepositoryId;
import com.sun.corba.se.impl.util.Utility;
import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
import java.io.Externalizable;
import java.io.Serializable;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ExportException;
import java.rmi.server.RemoteObject;
import java.rmi.server.RemoteStub;
import java.rmi.server.UnicastRemoteObject;
import javax.rmi.CORBA.PortableRemoteObjectDelegate;
import javax.rmi.CORBA.Tie;
import javax.rmi.CORBA.Util;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.SystemException;

/* loaded from: rt.jar:com/sun/corba/se/impl/javax/rmi/PortableRemoteObject.class */
public class PortableRemoteObject implements PortableRemoteObjectDelegate {
    @Override // javax.rmi.CORBA.PortableRemoteObjectDelegate
    public void exportObject(Remote remote) throws RemoteException {
        if (remote == null) {
            throw new NullPointerException("invalid argument");
        }
        if (Util.getTie(remote) != null) {
            throw new ExportException(remote.getClass().getName() + " already exported");
        }
        Tie tieLoadTie = Utility.loadTie(remote);
        if (tieLoadTie != null) {
            Util.registerTarget(tieLoadTie, remote);
        } else {
            UnicastRemoteObject.exportObject(remote);
        }
    }

    @Override // javax.rmi.CORBA.PortableRemoteObjectDelegate
    public Remote toStub(Remote remote) throws NoSuchObjectException {
        Remote stub = null;
        if (remote == null) {
            throw new NullPointerException("invalid argument");
        }
        if (StubAdapter.isStub(remote)) {
            return remote;
        }
        if (remote instanceof RemoteStub) {
            return remote;
        }
        Tie tie = Util.getTie(remote);
        if (tie != null) {
            stub = Utility.loadStub(tie, null, null, true);
        } else if (Utility.loadTie(remote) == null) {
            stub = RemoteObject.toStub(remote);
        }
        if (stub == null) {
            throw new NoSuchObjectException("object not exported");
        }
        return stub;
    }

    @Override // javax.rmi.CORBA.PortableRemoteObjectDelegate
    public void unexportObject(Remote remote) throws NoSuchObjectException {
        if (remote == null) {
            throw new NullPointerException("invalid argument");
        }
        if (StubAdapter.isStub(remote) || (remote instanceof RemoteStub)) {
            throw new NoSuchObjectException("Can only unexport a server object.");
        }
        if (Util.getTie(remote) != null) {
            Util.unexportObject(remote);
        } else {
            if (Utility.loadTie(remote) == null) {
                UnicastRemoteObject.unexportObject(remote, true);
                return;
            }
            throw new NoSuchObjectException("Object not exported.");
        }
    }

    @Override // javax.rmi.CORBA.PortableRemoteObjectDelegate
    public Object narrow(Object obj, Class cls) throws ClassCastException {
        if (obj == null) {
            return null;
        }
        if (cls == null) {
            throw new NullPointerException("invalid argument");
        }
        try {
            if (cls.isAssignableFrom(obj.getClass())) {
                return obj;
            }
            if (cls.isInterface() && cls != Serializable.class && cls != Externalizable.class) {
                Object object = (Object) obj;
                if (object._is_a(RepositoryId.createForAnyType(cls))) {
                    return Utility.loadStub(object, cls);
                }
                throw new ClassCastException("Object is not of remote type " + cls.getName());
            }
            throw new ClassCastException("Class " + cls.getName() + " is not a valid remote interface");
        } catch (Exception e2) {
            ClassCastException classCastException = new ClassCastException();
            classCastException.initCause(e2);
            throw classCastException;
        }
    }

    @Override // javax.rmi.CORBA.PortableRemoteObjectDelegate
    public void connect(Remote remote, Remote remote2) throws RemoteException {
        if (remote == null || remote2 == null) {
            throw new NullPointerException("invalid argument");
        }
        ORB orb = null;
        try {
            if (StubAdapter.isStub(remote2)) {
                orb = StubAdapter.getORB(remote2);
            } else {
                Tie tie = Util.getTie(remote2);
                if (tie != null) {
                    orb = tie.orb();
                }
            }
            boolean z2 = false;
            Tie tie2 = null;
            if (StubAdapter.isStub(remote)) {
                z2 = true;
            } else {
                tie2 = Util.getTie(remote);
                if (tie2 != null) {
                    z2 = true;
                }
            }
            if (!z2) {
                if (orb != null) {
                    throw new RemoteException("'source' object exported to IIOP, 'target' is JRMP");
                }
            } else {
                if (orb == null) {
                    throw new RemoteException("'source' object is JRMP, 'target' is IIOP");
                }
                try {
                    if (tie2 != null) {
                        try {
                            if (tie2.orb() == orb) {
                            } else {
                                throw new RemoteException("'target' object was already connected");
                            }
                        } catch (SystemException e2) {
                            tie2.orb(orb);
                        }
                    } else {
                        StubAdapter.connect(remote, orb);
                    }
                } catch (SystemException e3) {
                    throw new RemoteException("'target' object was already connected", e3);
                }
            }
        } catch (SystemException e4) {
            throw new RemoteException("'source' object not connected", e4);
        }
    }
}
