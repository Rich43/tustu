package com.sun.corba.se.impl.protocol;

import com.sun.corba.se.impl.orbutil.DenseIntMapImpl;
import com.sun.corba.se.pept.protocol.ClientRequestDispatcher;
import com.sun.corba.se.spi.oa.ObjectAdapterFactory;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.protocol.CorbaServerRequestDispatcher;
import com.sun.corba.se.spi.protocol.LocalClientRequestDispatcherFactory;
import com.sun.corba.se.spi.protocol.RequestDispatcherRegistry;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/RequestDispatcherRegistryImpl.class */
public class RequestDispatcherRegistryImpl implements RequestDispatcherRegistry {
    private ORB orb;
    protected int defaultId;
    private DenseIntMapImpl SDRegistry = new DenseIntMapImpl();
    private DenseIntMapImpl CSRegistry = new DenseIntMapImpl();
    private DenseIntMapImpl OAFRegistry = new DenseIntMapImpl();
    private DenseIntMapImpl LCSFRegistry = new DenseIntMapImpl();
    private Set objectAdapterFactories = new HashSet();
    private Set objectAdapterFactoriesView = Collections.unmodifiableSet(this.objectAdapterFactories);
    private Map stringToServerSubcontract = new HashMap();

    public RequestDispatcherRegistryImpl(ORB orb, int i2) {
        this.orb = orb;
        this.defaultId = i2;
    }

    @Override // com.sun.corba.se.spi.protocol.RequestDispatcherRegistry
    public synchronized void registerClientRequestDispatcher(ClientRequestDispatcher clientRequestDispatcher, int i2) {
        this.CSRegistry.set(i2, clientRequestDispatcher);
    }

    @Override // com.sun.corba.se.spi.protocol.RequestDispatcherRegistry
    public synchronized void registerLocalClientRequestDispatcherFactory(LocalClientRequestDispatcherFactory localClientRequestDispatcherFactory, int i2) {
        this.LCSFRegistry.set(i2, localClientRequestDispatcherFactory);
    }

    @Override // com.sun.corba.se.spi.protocol.RequestDispatcherRegistry
    public synchronized void registerServerRequestDispatcher(CorbaServerRequestDispatcher corbaServerRequestDispatcher, int i2) {
        this.SDRegistry.set(i2, corbaServerRequestDispatcher);
    }

    @Override // com.sun.corba.se.spi.protocol.RequestDispatcherRegistry
    public synchronized void registerServerRequestDispatcher(CorbaServerRequestDispatcher corbaServerRequestDispatcher, String str) {
        this.stringToServerSubcontract.put(str, corbaServerRequestDispatcher);
    }

    @Override // com.sun.corba.se.spi.protocol.RequestDispatcherRegistry
    public synchronized void registerObjectAdapterFactory(ObjectAdapterFactory objectAdapterFactory, int i2) {
        this.objectAdapterFactories.add(objectAdapterFactory);
        this.OAFRegistry.set(i2, objectAdapterFactory);
    }

    @Override // com.sun.corba.se.spi.protocol.RequestDispatcherRegistry
    public CorbaServerRequestDispatcher getServerRequestDispatcher(int i2) {
        CorbaServerRequestDispatcher corbaServerRequestDispatcher = (CorbaServerRequestDispatcher) this.SDRegistry.get(i2);
        if (corbaServerRequestDispatcher == null) {
            corbaServerRequestDispatcher = (CorbaServerRequestDispatcher) this.SDRegistry.get(this.defaultId);
        }
        return corbaServerRequestDispatcher;
    }

    @Override // com.sun.corba.se.spi.protocol.RequestDispatcherRegistry
    public CorbaServerRequestDispatcher getServerRequestDispatcher(String str) {
        CorbaServerRequestDispatcher corbaServerRequestDispatcher = (CorbaServerRequestDispatcher) this.stringToServerSubcontract.get(str);
        if (corbaServerRequestDispatcher == null) {
            corbaServerRequestDispatcher = (CorbaServerRequestDispatcher) this.SDRegistry.get(this.defaultId);
        }
        return corbaServerRequestDispatcher;
    }

    @Override // com.sun.corba.se.spi.protocol.RequestDispatcherRegistry
    public LocalClientRequestDispatcherFactory getLocalClientRequestDispatcherFactory(int i2) {
        LocalClientRequestDispatcherFactory localClientRequestDispatcherFactory = (LocalClientRequestDispatcherFactory) this.LCSFRegistry.get(i2);
        if (localClientRequestDispatcherFactory == null) {
            localClientRequestDispatcherFactory = (LocalClientRequestDispatcherFactory) this.LCSFRegistry.get(this.defaultId);
        }
        return localClientRequestDispatcherFactory;
    }

    @Override // com.sun.corba.se.spi.protocol.RequestDispatcherRegistry
    public ClientRequestDispatcher getClientRequestDispatcher(int i2) {
        ClientRequestDispatcher clientRequestDispatcher = (ClientRequestDispatcher) this.CSRegistry.get(i2);
        if (clientRequestDispatcher == null) {
            clientRequestDispatcher = (ClientRequestDispatcher) this.CSRegistry.get(this.defaultId);
        }
        return clientRequestDispatcher;
    }

    @Override // com.sun.corba.se.spi.protocol.RequestDispatcherRegistry
    public ObjectAdapterFactory getObjectAdapterFactory(int i2) {
        ObjectAdapterFactory objectAdapterFactory = (ObjectAdapterFactory) this.OAFRegistry.get(i2);
        if (objectAdapterFactory == null) {
            objectAdapterFactory = (ObjectAdapterFactory) this.OAFRegistry.get(this.defaultId);
        }
        return objectAdapterFactory;
    }

    @Override // com.sun.corba.se.spi.protocol.RequestDispatcherRegistry
    public Set getObjectAdapterFactories() {
        return this.objectAdapterFactoriesView;
    }
}
