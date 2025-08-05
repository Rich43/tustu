package com.sun.corba.se.impl.monitoring;

import com.sun.corba.se.spi.monitoring.MonitoredAttributeInfo;
import com.sun.corba.se.spi.monitoring.MonitoredAttributeInfoFactory;

/* loaded from: rt.jar:com/sun/corba/se/impl/monitoring/MonitoredAttributeInfoFactoryImpl.class */
public class MonitoredAttributeInfoFactoryImpl implements MonitoredAttributeInfoFactory {
    @Override // com.sun.corba.se.spi.monitoring.MonitoredAttributeInfoFactory
    public MonitoredAttributeInfo createMonitoredAttributeInfo(String str, Class cls, boolean z2, boolean z3) {
        return new MonitoredAttributeInfoImpl(str, cls, z2, z3);
    }
}
