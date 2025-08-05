package com.sun.corba.se.spi.monitoring;

import java.io.Closeable;

/* loaded from: rt.jar:com/sun/corba/se/spi/monitoring/MonitoringManager.class */
public interface MonitoringManager extends Closeable {
    MonitoredObject getRootMonitoredObject();

    void clearState();
}
