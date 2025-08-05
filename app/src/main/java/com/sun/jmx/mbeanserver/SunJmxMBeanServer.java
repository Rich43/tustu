package com.sun.jmx.mbeanserver;

import javax.management.MBeanServer;
import javax.management.MBeanServerDelegate;

/* loaded from: rt.jar:com/sun/jmx/mbeanserver/SunJmxMBeanServer.class */
public interface SunJmxMBeanServer extends MBeanServer {
    MBeanInstantiator getMBeanInstantiator();

    boolean interceptorsEnabled();

    MBeanServer getMBeanServerInterceptor();

    void setMBeanServerInterceptor(MBeanServer mBeanServer);

    MBeanServerDelegate getMBeanServerDelegate();
}
