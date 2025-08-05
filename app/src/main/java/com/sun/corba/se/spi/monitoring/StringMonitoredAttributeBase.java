package com.sun.corba.se.spi.monitoring;

/* loaded from: rt.jar:com/sun/corba/se/spi/monitoring/StringMonitoredAttributeBase.class */
public abstract class StringMonitoredAttributeBase extends MonitoredAttributeBase {
    public StringMonitoredAttributeBase(String str, String str2) {
        super(str);
        setMonitoredAttributeInfo(MonitoringFactories.getMonitoredAttributeInfoFactory().createMonitoredAttributeInfo(str2, String.class, false, false));
    }
}
