package com.sun.corba.se.impl.monitoring;

import com.sun.corba.se.spi.monitoring.MonitoredObject;
import com.sun.corba.se.spi.monitoring.MonitoringFactories;
import com.sun.corba.se.spi.monitoring.MonitoringManager;

/* loaded from: rt.jar:com/sun/corba/se/impl/monitoring/MonitoringManagerImpl.class */
public class MonitoringManagerImpl implements MonitoringManager {
    private final MonitoredObject rootMonitoredObject;

    MonitoringManagerImpl(String str, String str2) {
        this.rootMonitoredObject = MonitoringFactories.getMonitoredObjectFactory().createMonitoredObject(str, str2);
    }

    @Override // com.sun.corba.se.spi.monitoring.MonitoringManager
    public void clearState() {
        this.rootMonitoredObject.clearState();
    }

    @Override // com.sun.corba.se.spi.monitoring.MonitoringManager
    public MonitoredObject getRootMonitoredObject() {
        return this.rootMonitoredObject;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        MonitoringFactories.getMonitoringManagerFactory().remove(this.rootMonitoredObject.getName());
    }
}
