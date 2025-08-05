package java.rmi.activation;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.rmi.MarshalledObject;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.rmi.server.RemoteObject;
import java.rmi.server.RemoteObjectInvocationHandler;
import java.rmi.server.RemoteRef;
import java.rmi.server.UID;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permissions;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;

/* loaded from: rt.jar:java/rmi/activation/ActivationID.class */
public class ActivationID implements Serializable {
    private transient Activator activator;
    private transient UID uid = new UID();
    private static final long serialVersionUID = -4608673054848209235L;
    private static final AccessControlContext NOPERMS_ACC = new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(null, new Permissions())});

    public ActivationID(Activator activator) {
        this.activator = activator;
    }

    public Remote activate(boolean z2) throws ActivationException, RemoteException {
        try {
            final MarshalledObject<? extends Remote> marshalledObjectActivate = this.activator.activate(this, z2);
            return (Remote) AccessController.doPrivileged(new PrivilegedExceptionAction<Remote>() { // from class: java.rmi.activation.ActivationID.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Remote run() throws IOException, ClassNotFoundException {
                    return (Remote) marshalledObjectActivate.get();
                }
            }, NOPERMS_ACC);
        } catch (PrivilegedActionException e2) {
            Exception exception = e2.getException();
            if (exception instanceof RemoteException) {
                throw ((RemoteException) exception);
            }
            throw new UnmarshalException("activation failed", exception);
        }
    }

    public int hashCode() {
        return this.uid.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj instanceof ActivationID) {
            ActivationID activationID = (ActivationID) obj;
            return this.uid.equals(activationID.uid) && this.activator.equals(activationID.activator);
        }
        return false;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException, ClassNotFoundException, IllegalArgumentException {
        RemoteRef ref;
        objectOutputStream.writeObject(this.uid);
        if (this.activator instanceof RemoteObject) {
            ref = ((RemoteObject) this.activator).getRef();
        } else if (Proxy.isProxyClass(this.activator.getClass())) {
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(this.activator);
            if (!(invocationHandler instanceof RemoteObjectInvocationHandler)) {
                throw new InvalidObjectException("unexpected invocation handler");
            }
            ref = ((RemoteObjectInvocationHandler) invocationHandler).getRef();
        } else {
            throw new InvalidObjectException("unexpected activator type");
        }
        objectOutputStream.writeUTF(ref.getRefClass(objectOutputStream));
        ref.writeExternal(objectOutputStream);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.uid = (UID) objectInputStream.readObject();
        try {
            RemoteRef remoteRef = (RemoteRef) Class.forName("sun.rmi.server." + objectInputStream.readUTF()).asSubclass(RemoteRef.class).newInstance();
            remoteRef.readExternal(objectInputStream);
            this.activator = (Activator) Proxy.newProxyInstance(null, new Class[]{Activator.class}, new RemoteObjectInvocationHandler(remoteRef));
        } catch (IllegalAccessException e2) {
            throw ((IOException) new InvalidObjectException("Unable to create remote reference").initCause(e2));
        } catch (InstantiationException e3) {
            throw ((IOException) new InvalidObjectException("Unable to create remote reference").initCause(e3));
        }
    }
}
