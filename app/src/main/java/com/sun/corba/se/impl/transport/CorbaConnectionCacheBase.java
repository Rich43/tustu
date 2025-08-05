package com.sun.corba.se.impl.transport;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.pept.transport.Connection;
import com.sun.corba.se.pept.transport.ConnectionCache;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.transport.CorbaConnection;
import com.sun.corba.se.spi.transport.CorbaConnectionCache;
import java.util.Collection;
import java.util.Iterator;

/* loaded from: rt.jar:com/sun/corba/se/impl/transport/CorbaConnectionCacheBase.class */
public abstract class CorbaConnectionCacheBase implements ConnectionCache, CorbaConnectionCache {
    protected ORB orb;
    protected long timestamp = 0;
    protected String cacheType;
    protected String monitoringName;
    protected ORBUtilSystemException wrapper;

    public abstract Collection values();

    protected abstract Object backingStore();

    protected abstract void registerWithMonitoring();

    protected CorbaConnectionCacheBase(ORB orb, String str, String str2) {
        this.orb = orb;
        this.cacheType = str;
        this.monitoringName = str2;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_TRANSPORT);
        registerWithMonitoring();
        dprintCreation();
    }

    @Override // com.sun.corba.se.pept.transport.ConnectionCache
    public String getCacheType() {
        return this.cacheType;
    }

    @Override // com.sun.corba.se.pept.transport.ConnectionCache
    public synchronized void stampTime(Connection connection) {
        long j2 = this.timestamp;
        this.timestamp = j2 + 1;
        connection.setTimeStamp(j2);
    }

    @Override // com.sun.corba.se.pept.transport.ConnectionCache
    public long numberOfConnections() {
        long size;
        synchronized (backingStore()) {
            size = values().size();
        }
        return size;
    }

    @Override // com.sun.corba.se.pept.transport.ConnectionCache
    public void close() {
        synchronized (backingStore()) {
            Iterator it = values().iterator();
            while (it.hasNext()) {
                ((CorbaConnection) it.next()).closeConnectionResources();
            }
        }
    }

    @Override // com.sun.corba.se.pept.transport.ConnectionCache
    public long numberOfIdleConnections() {
        long j2 = 0;
        synchronized (backingStore()) {
            Iterator it = values().iterator();
            while (it.hasNext()) {
                if (!((Connection) it.next()).isBusy()) {
                    j2++;
                }
            }
        }
        return j2;
    }

    @Override // com.sun.corba.se.pept.transport.ConnectionCache
    public long numberOfBusyConnections() {
        long j2 = 0;
        synchronized (backingStore()) {
            Iterator it = values().iterator();
            while (it.hasNext()) {
                if (((Connection) it.next()).isBusy()) {
                    j2++;
                }
            }
        }
        return j2;
    }

    @Override // com.sun.corba.se.pept.transport.ConnectionCache
    public synchronized boolean reclaim() {
        try {
            long jNumberOfConnections = numberOfConnections();
            if (this.orb.transportDebugFlag) {
                dprint(".reclaim->: " + jNumberOfConnections + " (" + this.orb.getORBData().getHighWaterMark() + "/" + this.orb.getORBData().getLowWaterMark() + "/" + this.orb.getORBData().getNumberToReclaim() + ")");
            }
            if (jNumberOfConnections <= this.orb.getORBData().getHighWaterMark() || jNumberOfConnections < this.orb.getORBData().getLowWaterMark()) {
                return false;
            }
            synchronized (backingStore()) {
                for (int i2 = 0; i2 < this.orb.getORBData().getNumberToReclaim(); i2++) {
                    Connection connection = null;
                    long timeStamp = Long.MAX_VALUE;
                    for (Connection connection2 : values()) {
                        if (!connection2.isBusy() && connection2.getTimeStamp() < timeStamp) {
                            connection = connection2;
                            timeStamp = connection2.getTimeStamp();
                        }
                    }
                    if (connection == null) {
                        if (this.orb.transportDebugFlag) {
                            dprint(".reclaim<-: " + numberOfConnections());
                        }
                        return false;
                    }
                    try {
                        if (this.orb.transportDebugFlag) {
                            dprint(".reclaim: closing: " + ((Object) connection));
                        }
                        connection.close();
                    } catch (Exception e2) {
                    }
                }
                if (this.orb.transportDebugFlag) {
                    dprint(".reclaim: connections reclaimed (" + (jNumberOfConnections - numberOfConnections()) + ")");
                }
                if (this.orb.transportDebugFlag) {
                    dprint(".reclaim<-: " + numberOfConnections());
                }
                return true;
            }
        } finally {
            if (this.orb.transportDebugFlag) {
                dprint(".reclaim<-: " + numberOfConnections());
            }
        }
    }

    @Override // com.sun.corba.se.spi.transport.CorbaConnectionCache
    public String getMonitoringName() {
        return this.monitoringName;
    }

    protected void dprintCreation() {
        if (this.orb.transportDebugFlag) {
            dprint(".constructor: cacheType: " + getCacheType() + " monitoringName: " + getMonitoringName());
        }
    }

    protected void dprintStatistics() {
        if (this.orb.transportDebugFlag) {
            dprint(".stats: " + numberOfConnections() + "/total " + numberOfBusyConnections() + "/busy " + numberOfIdleConnections() + "/idle (" + this.orb.getORBData().getHighWaterMark() + "/" + this.orb.getORBData().getLowWaterMark() + "/" + this.orb.getORBData().getNumberToReclaim() + ")");
        }
    }

    protected void dprint(String str) {
        ORBUtility.dprint("CorbaConnectionCacheBase", str);
    }
}
