package com.sun.jndi.rmi.registry;

import java.lang.reflect.Method;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.UnexpectedException;
import java.rmi.server.RemoteRef;
import java.rmi.server.RemoteStub;
import javax.naming.NamingException;
import javax.naming.Reference;

/* loaded from: rt.jar:com/sun/jndi/rmi/registry/ReferenceWrapper_Stub.class */
public final class ReferenceWrapper_Stub extends RemoteStub implements RemoteReference, Remote {
    private static final long serialVersionUID = 2;
    private static Method $method_getReference_0;
    static Class class$com$sun$jndi$rmi$registry$RemoteReference;

    static {
        Class clsClass$;
        try {
            if (class$com$sun$jndi$rmi$registry$RemoteReference != null) {
                clsClass$ = class$com$sun$jndi$rmi$registry$RemoteReference;
            } else {
                clsClass$ = class$("com.sun.jndi.rmi.registry.RemoteReference");
                class$com$sun$jndi$rmi$registry$RemoteReference = clsClass$;
            }
            $method_getReference_0 = clsClass$.getMethod("getReference", new Class[0]);
        } catch (NoSuchMethodException unused) {
            throw new NoSuchMethodError("stub class initialization failed");
        }
    }

    public ReferenceWrapper_Stub(RemoteRef remoteRef) {
        super(remoteRef);
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e2) {
            throw new NoClassDefFoundError(e2.getMessage());
        }
    }

    @Override // com.sun.jndi.rmi.registry.RemoteReference
    public Reference getReference() throws NamingException, RemoteException {
        try {
            return (Reference) this.ref.invoke(this, $method_getReference_0, null, 3529874867989176284L);
        } catch (RuntimeException e2) {
            throw e2;
        } catch (RemoteException e3) {
            throw e3;
        } catch (NamingException e4) {
            throw e4;
        } catch (Exception e5) {
            throw new UnexpectedException("undeclared checked exception", e5);
        }
    }
}
