package java.rmi.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.rmi.MarshalException;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import sun.rmi.server.Util;
import sun.rmi.transport.ObjectTable;

/* loaded from: rt.jar:java/rmi/server/RemoteObject.class */
public abstract class RemoteObject implements Remote, Serializable {
    protected transient RemoteRef ref;
    private static final long serialVersionUID = -3215090123894869218L;

    protected RemoteObject() {
        this.ref = null;
    }

    protected RemoteObject(RemoteRef remoteRef) {
        this.ref = remoteRef;
    }

    public RemoteRef getRef() {
        return this.ref;
    }

    public static Remote toStub(Remote remote) throws NoSuchObjectException {
        if ((remote instanceof RemoteStub) || (remote != null && Proxy.isProxyClass(remote.getClass()) && (Proxy.getInvocationHandler(remote) instanceof RemoteObjectInvocationHandler))) {
            return remote;
        }
        return ObjectTable.getStub(remote);
    }

    public int hashCode() {
        return this.ref == null ? super.hashCode() : this.ref.remoteHashCode();
    }

    public boolean equals(Object obj) {
        if (obj instanceof RemoteObject) {
            if (this.ref == null) {
                return obj == this;
            }
            return this.ref.remoteEquals(((RemoteObject) obj).ref);
        }
        if (obj != null) {
            return obj.equals(this);
        }
        return false;
    }

    public String toString() {
        String unqualifiedName = Util.getUnqualifiedName(getClass());
        return this.ref == null ? unqualifiedName : unqualifiedName + "[" + this.ref.remoteToString() + "]";
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException, ClassNotFoundException {
        if (this.ref == null) {
            throw new MarshalException("Invalid remote object");
        }
        String refClass = this.ref.getRefClass(objectOutputStream);
        if (refClass == null || refClass.length() == 0) {
            objectOutputStream.writeUTF("");
            objectOutputStream.writeObject(this.ref);
        } else {
            objectOutputStream.writeUTF(refClass);
            this.ref.writeExternal(objectOutputStream);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        String utf = objectInputStream.readUTF();
        if (utf == null || utf.length() == 0) {
            this.ref = (RemoteRef) objectInputStream.readObject();
            return;
        }
        String str = "sun.rmi.server." + utf;
        try {
            this.ref = (RemoteRef) Class.forName(str).newInstance();
            this.ref.readExternal(objectInputStream);
        } catch (ClassCastException e2) {
            throw new ClassNotFoundException(str, e2);
        } catch (IllegalAccessException e3) {
            throw new ClassNotFoundException(str, e3);
        } catch (InstantiationException e4) {
            throw new ClassNotFoundException(str, e4);
        }
    }
}
