package sun.rmi.server;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.ConnectIOException;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.StubNotFoundException;
import java.rmi.UnmarshalException;
import java.rmi.activation.ActivateFailedException;
import java.rmi.activation.ActivationDesc;
import java.rmi.activation.ActivationException;
import java.rmi.activation.ActivationID;
import java.rmi.activation.UnknownObjectException;
import java.rmi.server.Operation;
import java.rmi.server.RMIClassLoader;
import java.rmi.server.RemoteCall;
import java.rmi.server.RemoteObject;
import java.rmi.server.RemoteObjectInvocationHandler;
import java.rmi.server.RemoteRef;
import java.rmi.server.RemoteStub;

/* loaded from: rt.jar:sun/rmi/server/ActivatableRef.class */
public class ActivatableRef implements RemoteRef {
    private static final long serialVersionUID = 7579060052569229166L;
    protected ActivationID id;
    protected RemoteRef ref;
    transient boolean force = false;
    private static final int MAX_RETRIES = 3;
    private static final String versionComplaint = "activation requires 1.2 stubs";
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ActivatableRef.class.desiredAssertionStatus();
    }

    public ActivatableRef() {
    }

    public ActivatableRef(ActivationID activationID, RemoteRef remoteRef) {
        this.id = activationID;
        this.ref = remoteRef;
    }

    public static Remote getStub(ActivationDesc activationDesc, ActivationID activationID) throws StubNotFoundException {
        String className = activationDesc.getClassName();
        try {
            return Util.createProxy(RMIClassLoader.loadClass(activationDesc.getLocation(), className), new ActivatableRef(activationID, null), false);
        } catch (ClassNotFoundException e2) {
            throw new StubNotFoundException("unable to load class: " + className, e2);
        } catch (IllegalArgumentException e3) {
            throw new StubNotFoundException("class implements an illegal remote interface", e3);
        } catch (MalformedURLException e4) {
            throw new StubNotFoundException("malformed URL", e4);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:56:0x00a8  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x010b A[SYNTHETIC] */
    @Override // java.rmi.server.RemoteRef
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.Object invoke(java.rmi.Remote r8, java.lang.reflect.Method r9, java.lang.Object[] r10, long r11) throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 276
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.rmi.server.ActivatableRef.invoke(java.rmi.Remote, java.lang.reflect.Method, java.lang.Object[], long):java.lang.Object");
    }

    private synchronized RemoteRef getRef() throws RemoteException {
        if (this.ref == null) {
            this.ref = activate(false);
        }
        return this.ref;
    }

    private RemoteRef activate(boolean z2) throws RemoteException {
        ActivatableRef activatableRef;
        if (!$assertionsDisabled && !Thread.holdsLock(this)) {
            throw new AssertionError();
        }
        this.ref = null;
        try {
            Remote remoteActivate = this.id.activate(z2);
            if (remoteActivate instanceof RemoteStub) {
                activatableRef = (ActivatableRef) ((RemoteStub) remoteActivate).getRef();
            } else {
                activatableRef = (ActivatableRef) ((RemoteObjectInvocationHandler) Proxy.getInvocationHandler(remoteActivate)).getRef();
            }
            this.ref = activatableRef.ref;
            return this.ref;
        } catch (ConnectException e2) {
            throw new ConnectException("activation failed", e2);
        } catch (RemoteException e3) {
            throw new ConnectIOException("activation failed", e3);
        } catch (UnknownObjectException e4) {
            throw new NoSuchObjectException("object not registered");
        } catch (ActivationException e5) {
            throw new ActivateFailedException("activation failed", e5);
        }
    }

    @Override // java.rmi.server.RemoteRef
    public synchronized RemoteCall newCall(RemoteObject remoteObject, Operation[] operationArr, int i2, long j2) throws RemoteException {
        throw new UnsupportedOperationException(versionComplaint);
    }

    @Override // java.rmi.server.RemoteRef
    public void invoke(RemoteCall remoteCall) throws Exception {
        throw new UnsupportedOperationException(versionComplaint);
    }

    @Override // java.rmi.server.RemoteRef
    public void done(RemoteCall remoteCall) throws RemoteException {
        throw new UnsupportedOperationException(versionComplaint);
    }

    @Override // java.rmi.server.RemoteRef
    public String getRefClass(ObjectOutput objectOutput) {
        return "ActivatableRef";
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        RemoteRef remoteRef = this.ref;
        objectOutput.writeObject(this.id);
        if (remoteRef == null) {
            objectOutput.writeUTF("");
        } else {
            objectOutput.writeUTF(remoteRef.getRefClass(objectOutput));
            remoteRef.writeExternal(objectOutput);
        }
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        this.id = (ActivationID) objectInput.readObject();
        this.ref = null;
        String utf = objectInput.readUTF();
        if (utf.equals("")) {
            return;
        }
        try {
            this.ref = (RemoteRef) Class.forName("sun.rmi.server." + utf).newInstance();
            this.ref.readExternal(objectInput);
        } catch (IllegalAccessException e2) {
            throw new UnmarshalException("Illegal access creating remote reference");
        } catch (InstantiationException e3) {
            throw new UnmarshalException("Unable to create remote reference", e3);
        }
    }

    @Override // java.rmi.server.RemoteRef
    public String remoteToString() {
        return Util.getUnqualifiedName(getClass()) + " [remoteRef: " + ((Object) this.ref) + "]";
    }

    @Override // java.rmi.server.RemoteRef
    public int remoteHashCode() {
        return this.id.hashCode();
    }

    @Override // java.rmi.server.RemoteRef
    public boolean remoteEquals(RemoteRef remoteRef) {
        if (remoteRef instanceof ActivatableRef) {
            return this.id.equals(((ActivatableRef) remoteRef).id);
        }
        return false;
    }
}
