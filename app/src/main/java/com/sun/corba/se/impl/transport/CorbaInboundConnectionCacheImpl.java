package com.sun.corba.se.impl.transport;

import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.pept.transport.Acceptor;
import com.sun.corba.se.pept.transport.Connection;
import com.sun.corba.se.pept.transport.InboundConnectionCache;
import com.sun.corba.se.spi.monitoring.LongMonitoredAttributeBase;
import com.sun.corba.se.spi.monitoring.MonitoredObject;
import com.sun.corba.se.spi.monitoring.MonitoringConstants;
import com.sun.corba.se.spi.monitoring.MonitoringFactories;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.transport.CorbaAcceptor;
import java.util.ArrayList;
import java.util.Collection;

/* loaded from: rt.jar:com/sun/corba/se/impl/transport/CorbaInboundConnectionCacheImpl.class */
public class CorbaInboundConnectionCacheImpl extends CorbaConnectionCacheBase implements InboundConnectionCache {
    protected Collection connectionCache;
    private Acceptor acceptor;

    public CorbaInboundConnectionCacheImpl(ORB orb, Acceptor acceptor) {
        super(orb, acceptor.getConnectionCacheType(), ((CorbaAcceptor) acceptor).getMonitoringName());
        this.connectionCache = new ArrayList();
        this.acceptor = acceptor;
        if (orb.transportDebugFlag) {
            dprint(": " + ((Object) acceptor));
        }
    }

    @Override // com.sun.corba.se.impl.transport.CorbaConnectionCacheBase, com.sun.corba.se.pept.transport.ConnectionCache
    public void close() {
        super.close();
        if (this.orb.transportDebugFlag) {
            dprint(".close: " + ((Object) this.acceptor));
        }
        this.acceptor.close();
    }

    @Override // com.sun.corba.se.pept.transport.InboundConnectionCache
    public Connection get(Acceptor acceptor) {
        throw this.wrapper.methodShouldNotBeCalled();
    }

    @Override // com.sun.corba.se.pept.transport.InboundConnectionCache
    public Acceptor getAcceptor() {
        return this.acceptor;
    }

    @Override // com.sun.corba.se.pept.transport.InboundConnectionCache
    public void put(Acceptor acceptor, Connection connection) {
        if (this.orb.transportDebugFlag) {
            dprint(".put: " + ((Object) acceptor) + " " + ((Object) connection));
        }
        synchronized (backingStore()) {
            this.connectionCache.add(connection);
            connection.setConnectionCache(this);
            dprintStatistics();
        }
    }

    @Override // com.sun.corba.se.pept.transport.InboundConnectionCache
    public void remove(Connection connection) {
        if (this.orb.transportDebugFlag) {
            dprint(".remove: " + ((Object) connection));
        }
        synchronized (backingStore()) {
            this.connectionCache.remove(connection);
            dprintStatistics();
        }
    }

    @Override // com.sun.corba.se.impl.transport.CorbaConnectionCacheBase
    public Collection values() {
        return this.connectionCache;
    }

    @Override // com.sun.corba.se.impl.transport.CorbaConnectionCacheBase
    protected Object backingStore() {
        return this.connectionCache;
    }

    @Override // com.sun.corba.se.impl.transport.CorbaConnectionCacheBase
    protected void registerWithMonitoring() {
        MonitoredObject rootMonitoredObject = this.orb.getMonitoringManager().getRootMonitoredObject();
        MonitoredObject child = rootMonitoredObject.getChild(MonitoringConstants.CONNECTION_MONITORING_ROOT);
        if (child == null) {
            child = MonitoringFactories.getMonitoredObjectFactory().createMonitoredObject(MonitoringConstants.CONNECTION_MONITORING_ROOT, MonitoringConstants.CONNECTION_MONITORING_ROOT_DESCRIPTION);
            rootMonitoredObject.addChild(child);
        }
        MonitoredObject child2 = child.getChild(MonitoringConstants.INBOUND_CONNECTION_MONITORING_ROOT);
        if (child2 == null) {
            child2 = MonitoringFactories.getMonitoredObjectFactory().createMonitoredObject(MonitoringConstants.INBOUND_CONNECTION_MONITORING_ROOT, MonitoringConstants.INBOUND_CONNECTION_MONITORING_ROOT_DESCRIPTION);
            child.addChild(child2);
        }
        MonitoredObject child3 = child2.getChild(getMonitoringName());
        if (child3 == null) {
            child3 = MonitoringFactories.getMonitoredObjectFactory().createMonitoredObject(getMonitoringName(), MonitoringConstants.CONNECTION_MONITORING_DESCRIPTION);
            child2.addChild(child3);
        }
        child3.addAttribute(new LongMonitoredAttributeBase(MonitoringConstants.CONNECTION_TOTAL_NUMBER_OF_CONNECTIONS, MonitoringConstants.CONNECTION_TOTAL_NUMBER_OF_CONNECTIONS_DESCRIPTION) { // from class: com.sun.corba.se.impl.transport.CorbaInboundConnectionCacheImpl.1
            @Override // com.sun.corba.se.spi.monitoring.MonitoredAttributeBase, com.sun.corba.se.spi.monitoring.MonitoredAttribute
            public Object getValue() {
                return new Long(CorbaInboundConnectionCacheImpl.this.numberOfConnections());
            }
        });
        child3.addAttribute(new LongMonitoredAttributeBase(MonitoringConstants.CONNECTION_NUMBER_OF_IDLE_CONNECTIONS, MonitoringConstants.CONNECTION_NUMBER_OF_IDLE_CONNECTIONS_DESCRIPTION) { // from class: com.sun.corba.se.impl.transport.CorbaInboundConnectionCacheImpl.2
            @Override // com.sun.corba.se.spi.monitoring.MonitoredAttributeBase, com.sun.corba.se.spi.monitoring.MonitoredAttribute
            public Object getValue() {
                return new Long(CorbaInboundConnectionCacheImpl.this.numberOfIdleConnections());
            }
        });
        child3.addAttribute(new LongMonitoredAttributeBase(MonitoringConstants.CONNECTION_NUMBER_OF_BUSY_CONNECTIONS, MonitoringConstants.CONNECTION_NUMBER_OF_BUSY_CONNECTIONS_DESCRIPTION) { // from class: com.sun.corba.se.impl.transport.CorbaInboundConnectionCacheImpl.3
            @Override // com.sun.corba.se.spi.monitoring.MonitoredAttributeBase, com.sun.corba.se.spi.monitoring.MonitoredAttribute
            public Object getValue() {
                return new Long(CorbaInboundConnectionCacheImpl.this.numberOfBusyConnections());
            }
        });
    }

    @Override // com.sun.corba.se.impl.transport.CorbaConnectionCacheBase
    protected void dprint(String str) {
        ORBUtility.dprint("CorbaInboundConnectionCacheImpl", str);
    }
}
