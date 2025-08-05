package com.sun.jmx.remote.internal;

import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;

/* loaded from: rt.jar:com/sun/jmx/remote/internal/RMIExporter.class */
public interface RMIExporter {
    public static final String EXPORTER_ATTRIBUTE = "com.sun.jmx.remote.rmi.exporter";

    Remote exportObject(Remote remote, int i2, RMIClientSocketFactory rMIClientSocketFactory, RMIServerSocketFactory rMIServerSocketFactory) throws RemoteException;

    boolean unexportObject(Remote remote, boolean z2) throws NoSuchObjectException;
}
