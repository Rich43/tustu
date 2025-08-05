package com.sun.jmx.remote.protocol.iiop;

import com.sun.jmx.remote.internal.IIOPProxy;
import java.io.SerializablePermission;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permissions;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;
import java.util.Properties;
import javax.rmi.CORBA.Stub;
import javax.rmi.PortableRemoteObject;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.portable.Delegate;

/* loaded from: rt.jar:com/sun/jmx/remote/protocol/iiop/IIOPProxyImpl.class */
public class IIOPProxyImpl implements IIOPProxy {
    private static final AccessControlContext STUB_ACC;

    static {
        Permissions permissions = new Permissions();
        permissions.add(new SerializablePermission("enableSubclassImplementation"));
        STUB_ACC = new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(null, permissions)});
    }

    @Override // com.sun.jmx.remote.internal.IIOPProxy
    public boolean isStub(Object obj) {
        return obj instanceof Stub;
    }

    @Override // com.sun.jmx.remote.internal.IIOPProxy
    public Object getDelegate(Object obj) {
        return ((Stub) obj)._get_delegate();
    }

    @Override // com.sun.jmx.remote.internal.IIOPProxy
    public void setDelegate(Object obj, Object obj2) {
        ((Stub) obj)._set_delegate((Delegate) obj2);
    }

    @Override // com.sun.jmx.remote.internal.IIOPProxy
    public Object getOrb(Object obj) {
        try {
            return ((Stub) obj)._orb();
        } catch (BAD_OPERATION e2) {
            throw new UnsupportedOperationException(e2);
        }
    }

    @Override // com.sun.jmx.remote.internal.IIOPProxy
    public void connect(Object obj, Object obj2) throws RemoteException {
        ((Stub) obj).connect((ORB) obj2);
    }

    @Override // com.sun.jmx.remote.internal.IIOPProxy
    public boolean isOrb(Object obj) {
        return obj instanceof ORB;
    }

    @Override // com.sun.jmx.remote.internal.IIOPProxy
    public Object createOrb(String[] strArr, Properties properties) {
        return ORB.init(strArr, properties);
    }

    @Override // com.sun.jmx.remote.internal.IIOPProxy
    public Object stringToObject(Object obj, String str) {
        return ((ORB) obj).string_to_object(str);
    }

    @Override // com.sun.jmx.remote.internal.IIOPProxy
    public String objectToString(Object obj, Object obj2) {
        return ((ORB) obj).object_to_string((Object) obj2);
    }

    @Override // com.sun.jmx.remote.internal.IIOPProxy
    public <T> T narrow(Object obj, Class<T> cls) {
        return (T) PortableRemoteObject.narrow(obj, cls);
    }

    @Override // com.sun.jmx.remote.internal.IIOPProxy
    public void exportObject(Remote remote) throws RemoteException {
        PortableRemoteObject.exportObject(remote);
    }

    @Override // com.sun.jmx.remote.internal.IIOPProxy
    public void unexportObject(Remote remote) throws NoSuchObjectException {
        PortableRemoteObject.unexportObject(remote);
    }

    @Override // com.sun.jmx.remote.internal.IIOPProxy
    public Remote toStub(final Remote remote) throws NoSuchObjectException {
        if (System.getSecurityManager() == null) {
            return PortableRemoteObject.toStub(remote);
        }
        try {
            return (Remote) AccessController.doPrivileged(new PrivilegedExceptionAction<Remote>() { // from class: com.sun.jmx.remote.protocol.iiop.IIOPProxyImpl.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Remote run() throws Exception {
                    return PortableRemoteObject.toStub(remote);
                }
            }, STUB_ACC);
        } catch (PrivilegedActionException e2) {
            if (e2.getException() instanceof NoSuchObjectException) {
                throw ((NoSuchObjectException) e2.getException());
            }
            throw new RuntimeException("Unexpected exception type", e2.getException());
        }
    }
}
