package com.sun.corba.se.impl.monitoring;

import com.sun.corba.se.spi.monitoring.MonitoringManager;
import com.sun.corba.se.spi.monitoring.MonitoringManagerFactory;
import java.util.HashMap;

/* loaded from: rt.jar:com/sun/corba/se/impl/monitoring/MonitoringManagerFactoryImpl.class */
public class MonitoringManagerFactoryImpl implements MonitoringManagerFactory {
    private HashMap monitoringManagerTable = new HashMap();

    @Override // com.sun.corba.se.spi.monitoring.MonitoringManagerFactory
    public synchronized MonitoringManager createMonitoringManager(String str, String str2) {
        MonitoringManagerImpl monitoringManagerImpl = (MonitoringManagerImpl) this.monitoringManagerTable.get(str);
        if (monitoringManagerImpl == null) {
            monitoringManagerImpl = new MonitoringManagerImpl(str, str2);
            this.monitoringManagerTable.put(str, monitoringManagerImpl);
        }
        return monitoringManagerImpl;
    }

    @Override // com.sun.corba.se.spi.monitoring.MonitoringManagerFactory
    public synchronized void remove(String str) {
        this.monitoringManagerTable.remove(str);
    }
}
