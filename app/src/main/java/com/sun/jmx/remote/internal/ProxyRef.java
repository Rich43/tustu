package com.sun.jmx.remote.internal;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Method;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.Operation;
import java.rmi.server.RemoteCall;
import java.rmi.server.RemoteObject;
import java.rmi.server.RemoteRef;

/* loaded from: rt.jar:com/sun/jmx/remote/internal/ProxyRef.class */
public class ProxyRef implements RemoteRef {
    private static final long serialVersionUID = -6503061366316814723L;
    protected RemoteRef ref;

    public ProxyRef(RemoteRef remoteRef) {
        this.ref = remoteRef;
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        this.ref.readExternal(objectInput);
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        this.ref.writeExternal(objectOutput);
    }

    @Override // java.rmi.server.RemoteRef
    @Deprecated
    public void invoke(RemoteCall remoteCall) throws Exception {
        this.ref.invoke(remoteCall);
    }

    @Override // java.rmi.server.RemoteRef
    public Object invoke(Remote remote, Method method, Object[] objArr, long j2) throws Exception {
        return this.ref.invoke(remote, method, objArr, j2);
    }

    @Override // java.rmi.server.RemoteRef
    @Deprecated
    public void done(RemoteCall remoteCall) throws RemoteException {
        this.ref.done(remoteCall);
    }

    @Override // java.rmi.server.RemoteRef
    public String getRefClass(ObjectOutput objectOutput) {
        return this.ref.getRefClass(objectOutput);
    }

    @Override // java.rmi.server.RemoteRef
    @Deprecated
    public RemoteCall newCall(RemoteObject remoteObject, Operation[] operationArr, int i2, long j2) throws RemoteException {
        return this.ref.newCall(remoteObject, operationArr, i2, j2);
    }

    @Override // java.rmi.server.RemoteRef
    public boolean remoteEquals(RemoteRef remoteRef) {
        return this.ref.remoteEquals(remoteRef);
    }

    @Override // java.rmi.server.RemoteRef
    public int remoteHashCode() {
        return this.ref.remoteHashCode();
    }

    @Override // java.rmi.server.RemoteRef
    public String remoteToString() {
        return this.ref.remoteToString();
    }
}
