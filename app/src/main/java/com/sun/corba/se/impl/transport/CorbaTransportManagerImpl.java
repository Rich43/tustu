package com.sun.corba.se.impl.transport;

import com.sun.corba.se.impl.oa.poa.Policies;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.pept.transport.Acceptor;
import com.sun.corba.se.pept.transport.ByteBufferPool;
import com.sun.corba.se.pept.transport.ConnectionCache;
import com.sun.corba.se.pept.transport.ContactInfo;
import com.sun.corba.se.pept.transport.InboundConnectionCache;
import com.sun.corba.se.pept.transport.OutboundConnectionCache;
import com.sun.corba.se.pept.transport.Selector;
import com.sun.corba.se.spi.ior.IORTemplate;
import com.sun.corba.se.spi.ior.ObjectAdapterId;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.transport.CorbaAcceptor;
import com.sun.corba.se.spi.transport.CorbaTransportManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: rt.jar:com/sun/corba/se/impl/transport/CorbaTransportManagerImpl.class */
public class CorbaTransportManagerImpl implements CorbaTransportManager {
    protected ORB orb;
    protected List acceptors = new ArrayList();
    protected Map outboundConnectionCaches = new HashMap();
    protected Map inboundConnectionCaches = new HashMap();
    protected Selector selector;

    public CorbaTransportManagerImpl(ORB orb) {
        this.orb = orb;
        this.selector = new SelectorImpl(orb);
    }

    @Override // com.sun.corba.se.pept.transport.TransportManager
    public ByteBufferPool getByteBufferPool(int i2) {
        throw new RuntimeException();
    }

    @Override // com.sun.corba.se.pept.transport.TransportManager
    public OutboundConnectionCache getOutboundConnectionCache(ContactInfo contactInfo) {
        OutboundConnectionCache connectionCache;
        OutboundConnectionCache corbaOutboundConnectionCacheImpl;
        synchronized (contactInfo) {
            if (contactInfo.getConnectionCache() == null) {
                synchronized (this.outboundConnectionCaches) {
                    corbaOutboundConnectionCacheImpl = (OutboundConnectionCache) this.outboundConnectionCaches.get(contactInfo.getConnectionCacheType());
                    if (corbaOutboundConnectionCacheImpl == null) {
                        corbaOutboundConnectionCacheImpl = new CorbaOutboundConnectionCacheImpl(this.orb, contactInfo);
                        this.outboundConnectionCaches.put(contactInfo.getConnectionCacheType(), corbaOutboundConnectionCacheImpl);
                    }
                }
                contactInfo.setConnectionCache(corbaOutboundConnectionCacheImpl);
            }
            connectionCache = contactInfo.getConnectionCache();
        }
        return connectionCache;
    }

    @Override // com.sun.corba.se.pept.transport.TransportManager
    public Collection getOutboundConnectionCaches() {
        return this.outboundConnectionCaches.values();
    }

    @Override // com.sun.corba.se.pept.transport.TransportManager
    public InboundConnectionCache getInboundConnectionCache(Acceptor acceptor) {
        InboundConnectionCache connectionCache;
        InboundConnectionCache corbaInboundConnectionCacheImpl;
        synchronized (acceptor) {
            if (acceptor.getConnectionCache() == null) {
                synchronized (this.inboundConnectionCaches) {
                    corbaInboundConnectionCacheImpl = (InboundConnectionCache) this.inboundConnectionCaches.get(acceptor.getConnectionCacheType());
                    if (corbaInboundConnectionCacheImpl == null) {
                        corbaInboundConnectionCacheImpl = new CorbaInboundConnectionCacheImpl(this.orb, acceptor);
                        this.inboundConnectionCaches.put(acceptor.getConnectionCacheType(), corbaInboundConnectionCacheImpl);
                    }
                }
                acceptor.setConnectionCache(corbaInboundConnectionCacheImpl);
            }
            connectionCache = acceptor.getConnectionCache();
        }
        return connectionCache;
    }

    @Override // com.sun.corba.se.pept.transport.TransportManager
    public Collection getInboundConnectionCaches() {
        return this.inboundConnectionCaches.values();
    }

    @Override // com.sun.corba.se.pept.transport.TransportManager
    public Selector getSelector(int i2) {
        return this.selector;
    }

    @Override // com.sun.corba.se.pept.transport.TransportManager
    public synchronized void registerAcceptor(Acceptor acceptor) {
        if (this.orb.transportDebugFlag) {
            dprint(".registerAcceptor->: " + ((Object) acceptor));
        }
        this.acceptors.add(acceptor);
        if (this.orb.transportDebugFlag) {
            dprint(".registerAcceptor<-: " + ((Object) acceptor));
        }
    }

    @Override // com.sun.corba.se.pept.transport.TransportManager
    public Collection getAcceptors() {
        return getAcceptors(null, null);
    }

    @Override // com.sun.corba.se.pept.transport.TransportManager
    public synchronized void unregisterAcceptor(Acceptor acceptor) {
        this.acceptors.remove(acceptor);
    }

    @Override // com.sun.corba.se.pept.transport.TransportManager
    public void close() {
        try {
            if (this.orb.transportDebugFlag) {
                dprint(".close->");
            }
            Iterator it = this.outboundConnectionCaches.values().iterator();
            while (it.hasNext()) {
                ((ConnectionCache) it.next()).close();
            }
            for (Object obj : this.inboundConnectionCaches.values()) {
                ((ConnectionCache) obj).close();
                unregisterAcceptor(((InboundConnectionCache) obj).getAcceptor());
            }
            getSelector(0).close();
        } finally {
            if (this.orb.transportDebugFlag) {
                dprint(".close<-");
            }
        }
    }

    @Override // com.sun.corba.se.spi.transport.CorbaTransportManager
    public Collection getAcceptors(String str, ObjectAdapterId objectAdapterId) {
        for (Acceptor acceptor : this.acceptors) {
            if (acceptor.initialize() && acceptor.shouldRegisterAcceptEvent()) {
                this.orb.getTransportManager().getSelector(0).registerForEvent(acceptor.getEventHandler());
            }
        }
        return this.acceptors;
    }

    @Override // com.sun.corba.se.spi.transport.CorbaTransportManager
    public void addToIORTemplate(IORTemplate iORTemplate, Policies policies, String str, String str2, ObjectAdapterId objectAdapterId) {
        Iterator it = getAcceptors(str2, objectAdapterId).iterator();
        while (it.hasNext()) {
            ((CorbaAcceptor) it.next()).addToIORTemplate(iORTemplate, policies, str);
        }
    }

    protected void dprint(String str) {
        ORBUtility.dprint("CorbaTransportManagerImpl", str);
    }
}
