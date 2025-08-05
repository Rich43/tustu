package com.sun.corba.se.spi.monitoring;

/* loaded from: rt.jar:com/sun/corba/se/spi/monitoring/MonitoringManagerFactory.class */
public interface MonitoringManagerFactory {
    MonitoringManager createMonitoringManager(String str, String str2);

    void remove(String str);
}
