package com.sun.corba.se.spi.monitoring;

/* loaded from: rt.jar:com/sun/corba/se/spi/monitoring/MonitoredAttributeInfo.class */
public interface MonitoredAttributeInfo {
    boolean isWritable();

    boolean isStatistic();

    Class type();

    String getDescription();
}
