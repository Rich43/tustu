package com.sun.jmx.remote.internal;

import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Properties;

/* loaded from: rt.jar:com/sun/jmx/remote/internal/IIOPProxy.class */
public interface IIOPProxy {
    boolean isStub(Object obj);

    Object getDelegate(Object obj);

    void setDelegate(Object obj, Object obj2);

    Object getOrb(Object obj);

    void connect(Object obj, Object obj2) throws RemoteException;

    boolean isOrb(Object obj);

    Object createOrb(String[] strArr, Properties properties);

    Object stringToObject(Object obj, String str);

    String objectToString(Object obj, Object obj2);

    <T> T narrow(Object obj, Class<T> cls);

    void exportObject(Remote remote) throws RemoteException;

    void unexportObject(Remote remote) throws NoSuchObjectException;

    Remote toStub(Remote remote) throws NoSuchObjectException;
}
