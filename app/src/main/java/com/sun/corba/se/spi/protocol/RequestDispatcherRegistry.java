package com.sun.corba.se.spi.protocol;

import com.sun.corba.se.pept.protocol.ClientRequestDispatcher;
import com.sun.corba.se.spi.oa.ObjectAdapterFactory;
import java.util.Set;

/* loaded from: rt.jar:com/sun/corba/se/spi/protocol/RequestDispatcherRegistry.class */
public interface RequestDispatcherRegistry {
    void registerClientRequestDispatcher(ClientRequestDispatcher clientRequestDispatcher, int i2);

    ClientRequestDispatcher getClientRequestDispatcher(int i2);

    void registerLocalClientRequestDispatcherFactory(LocalClientRequestDispatcherFactory localClientRequestDispatcherFactory, int i2);

    LocalClientRequestDispatcherFactory getLocalClientRequestDispatcherFactory(int i2);

    void registerServerRequestDispatcher(CorbaServerRequestDispatcher corbaServerRequestDispatcher, int i2);

    CorbaServerRequestDispatcher getServerRequestDispatcher(int i2);

    void registerServerRequestDispatcher(CorbaServerRequestDispatcher corbaServerRequestDispatcher, String str);

    CorbaServerRequestDispatcher getServerRequestDispatcher(String str);

    void registerObjectAdapterFactory(ObjectAdapterFactory objectAdapterFactory, int i2);

    ObjectAdapterFactory getObjectAdapterFactory(int i2);

    Set<ObjectAdapterFactory> getObjectAdapterFactories();
}
