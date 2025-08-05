package com.sun.corba.se.spi.monitoring;

/* loaded from: rt.jar:com/sun/corba/se/spi/monitoring/MonitoredAttribute.class */
public interface MonitoredAttribute {
    MonitoredAttributeInfo getAttributeInfo();

    void setValue(Object obj);

    Object getValue();

    String getName();

    void clearState();
}
