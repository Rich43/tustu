package com.sun.corba.se.impl.monitoring;

import com.sun.corba.se.spi.monitoring.MonitoredObject;
import com.sun.corba.se.spi.monitoring.MonitoredObjectFactory;

/* loaded from: rt.jar:com/sun/corba/se/impl/monitoring/MonitoredObjectFactoryImpl.class */
public class MonitoredObjectFactoryImpl implements MonitoredObjectFactory {
    @Override // com.sun.corba.se.spi.monitoring.MonitoredObjectFactory
    public MonitoredObject createMonitoredObject(String str, String str2) {
        return new MonitoredObjectImpl(str, str2);
    }
}
