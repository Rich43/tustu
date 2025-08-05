package com.sun.jndi.rmi.registry;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javax.naming.NamingException;
import javax.naming.Reference;

/* loaded from: rt.jar:com/sun/jndi/rmi/registry/ReferenceWrapper.class */
public class ReferenceWrapper extends UnicastRemoteObject implements RemoteReference {
    protected Reference wrappee;
    private static final long serialVersionUID = 6078186197417641456L;

    public ReferenceWrapper(Reference reference) throws NamingException, RemoteException {
        this.wrappee = reference;
    }

    @Override // com.sun.jndi.rmi.registry.RemoteReference
    public Reference getReference() throws RemoteException {
        return this.wrappee;
    }
}
