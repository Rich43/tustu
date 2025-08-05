package java.rmi.server;

import java.io.Externalizable;
import java.io.ObjectOutput;
import java.lang.reflect.Method;
import java.rmi.Remote;
import java.rmi.RemoteException;

/* loaded from: rt.jar:java/rmi/server/RemoteRef.class */
public interface RemoteRef extends Externalizable {
    public static final long serialVersionUID = 3632638527362204081L;
    public static final String packagePrefix = "sun.rmi.server";

    Object invoke(Remote remote, Method method, Object[] objArr, long j2) throws Exception;

    @Deprecated
    RemoteCall newCall(RemoteObject remoteObject, Operation[] operationArr, int i2, long j2) throws RemoteException;

    @Deprecated
    void invoke(RemoteCall remoteCall) throws Exception;

    @Deprecated
    void done(RemoteCall remoteCall) throws RemoteException;

    String getRefClass(ObjectOutput objectOutput);

    int remoteHashCode();

    boolean remoteEquals(RemoteRef remoteRef);

    String remoteToString();
}
