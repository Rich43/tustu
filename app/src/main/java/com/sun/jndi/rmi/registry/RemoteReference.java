package com.sun.jndi.rmi.registry;

import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.naming.NamingException;
import javax.naming.Reference;

/* loaded from: rt.jar:com/sun/jndi/rmi/registry/RemoteReference.class */
public interface RemoteReference extends Remote {
    Reference getReference() throws NamingException, RemoteException;
}
