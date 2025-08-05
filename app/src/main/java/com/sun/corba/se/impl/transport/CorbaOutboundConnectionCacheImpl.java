package com.sun.corba.se.impl.transport;

import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.pept.transport.Connection;
import com.sun.corba.se.pept.transport.ContactInfo;
import com.sun.corba.se.pept.transport.OutboundConnectionCache;
import com.sun.corba.se.spi.monitoring.LongMonitoredAttributeBase;
import com.sun.corba.se.spi.monitoring.MonitoredObject;
import com.sun.corba.se.spi.monitoring.MonitoringConstants;
import com.sun.corba.se.spi.monitoring.MonitoringFactories;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.transport.CorbaContactInfo;
import java.util.Collection;
import java.util.Hashtable;

/* loaded from: rt.jar:com/sun/corba/se/impl/transport/CorbaOutboundConnectionCacheImpl.class */
public class CorbaOutboundConnectionCacheImpl extends CorbaConnectionCacheBase implements OutboundConnectionCache {
    protected Hashtable connectionCache;

    public CorbaOutboundConnectionCacheImpl(ORB orb, ContactInfo contactInfo) {
        super(orb, contactInfo.getConnectionCacheType(), ((CorbaContactInfo) contactInfo).getMonitoringName());
        this.connectionCache = new Hashtable();
    }

    @Override // com.sun.corba.se.pept.transport.OutboundConnectionCache
    public Connection get(ContactInfo contactInfo) {
        Connection connection;
        if (this.orb.transportDebugFlag) {
            dprint(".get: " + ((Object) contactInfo) + " " + contactInfo.hashCode());
        }
        synchronized (backingStore()) {
            dprintStatistics();
            connection = (Connection) this.connectionCache.get(contactInfo);
        }
        return connection;
    }

    @Override // com.sun.corba.se.pept.transport.OutboundConnectionCache
    public void put(ContactInfo contactInfo, Connection connection) {
        if (this.orb.transportDebugFlag) {
            dprint(".put: " + ((Object) contactInfo) + " " + contactInfo.hashCode() + " " + ((Object) connection));
        }
        synchronized (backingStore()) {
            this.connectionCache.put(contactInfo, connection);
            connection.setConnectionCache(this);
            dprintStatistics();
        }
    }

    @Override // com.sun.corba.se.pept.transport.OutboundConnectionCache
    public void remove(ContactInfo contactInfo) {
        if (this.orb.transportDebugFlag) {
            dprint(".remove: " + ((Object) contactInfo) + " " + contactInfo.hashCode());
        }
        synchronized (backingStore()) {
            if (contactInfo != null) {
                this.connectionCache.remove(contactInfo);
                dprintStatistics();
            } else {
                dprintStatistics();
            }
        }
    }

    @Override // com.sun.corba.se.impl.transport.CorbaConnectionCacheBase
    public Collection values() {
        return this.connectionCache.values();
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
        MonitoredObject child2 = child.getChild(MonitoringConstants.OUTBOUND_CONNECTION_MONITORING_ROOT);
        if (child2 == null) {
            child2 = MonitoringFactories.getMonitoredObjectFactory().createMonitoredObject(MonitoringConstants.OUTBOUND_CONNECTION_MONITORING_ROOT, MonitoringConstants.OUTBOUND_CONNECTION_MONITORING_ROOT_DESCRIPTION);
            child.addChild(child2);
        }
        MonitoredObject child3 = child2.getChild(getMonitoringName());
        if (child3 == null) {
            child3 = MonitoringFactories.getMonitoredObjectFactory().createMonitoredObject(getMonitoringName(), MonitoringConstants.CONNECTION_MONITORING_DESCRIPTION);
            child2.addChild(child3);
        }
        child3.addAttribute(new LongMonitoredAttributeBase(MonitoringConstants.CONNECTION_TOTAL_NUMBER_OF_CONNECTIONS, MonitoringConstants.CONNECTION_TOTAL_NUMBER_OF_CONNECTIONS_DESCRIPTION) { // from class: com.sun.corba.se.impl.transport.CorbaOutboundConnectionCacheImpl.1
            @Override // com.sun.corba.se.spi.monitoring.MonitoredAttributeBase, com.sun.corba.se.spi.monitoring.MonitoredAttribute
            public Object getValue() {
                return new Long(CorbaOutboundConnectionCacheImpl.this.numberOfConnections());
            }
        });
        child3.addAttribute(new LongMonitoredAttributeBase(MonitoringConstants.CONNECTION_NUMBER_OF_IDLE_CONNECTIONS, MonitoringConstants.CONNECTION_NUMBER_OF_IDLE_CONNECTIONS_DESCRIPTION) { // from class: com.sun.corba.se.impl.transport.CorbaOutboundConnectionCacheImpl.2
            @Override // com.sun.corba.se.spi.monitoring.MonitoredAttributeBase, com.sun.corba.se.spi.monitoring.MonitoredAttribute
            public Object getValue() {
                return new Long(CorbaOutboundConnectionCacheImpl.this.numberOfIdleConnections());
            }
        });
        child3.addAttribute(new LongMonitoredAttributeBase(MonitoringConstants.CONNECTION_NUMBER_OF_BUSY_CONNECTIONS, MonitoringConstants.CONNECTION_NUMBER_OF_BUSY_CONNECTIONS_DESCRIPTION) { // from class: com.sun.corba.se.impl.transport.CorbaOutboundConnectionCacheImpl.3
            @Override // com.sun.corba.se.spi.monitoring.MonitoredAttributeBase, com.sun.corba.se.spi.monitoring.MonitoredAttribute
            public Object getValue() {
                return new Long(CorbaOutboundConnectionCacheImpl.this.numberOfBusyConnections());
            }
        });
    }

    @Override // com.sun.corba.se.impl.transport.CorbaConnectionCacheBase
    protected void dprint(String str) {
        ORBUtility.dprint("CorbaOutboundConnectionCacheImpl", str);
    }
}
