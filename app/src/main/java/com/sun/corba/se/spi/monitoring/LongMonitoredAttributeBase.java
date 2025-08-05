package com.sun.corba.se.spi.monitoring;

/* loaded from: rt.jar:com/sun/corba/se/spi/monitoring/LongMonitoredAttributeBase.class */
public abstract class LongMonitoredAttributeBase extends MonitoredAttributeBase {
    public LongMonitoredAttributeBase(String str, String str2) {
        super(str);
        setMonitoredAttributeInfo(MonitoringFactories.getMonitoredAttributeInfoFactory().createMonitoredAttributeInfo(str2, Long.class, false, false));
    }
}
