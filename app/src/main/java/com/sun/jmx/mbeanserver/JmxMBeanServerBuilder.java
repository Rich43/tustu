package com.sun.jmx.mbeanserver;

import javax.management.MBeanServer;
import javax.management.MBeanServerBuilder;
import javax.management.MBeanServerDelegate;

/* loaded from: rt.jar:com/sun/jmx/mbeanserver/JmxMBeanServerBuilder.class */
public class JmxMBeanServerBuilder extends MBeanServerBuilder {
    @Override // javax.management.MBeanServerBuilder
    public MBeanServerDelegate newMBeanServerDelegate() {
        return JmxMBeanServer.newMBeanServerDelegate();
    }

    @Override // javax.management.MBeanServerBuilder
    public MBeanServer newMBeanServer(String str, MBeanServer mBeanServer, MBeanServerDelegate mBeanServerDelegate) {
        return JmxMBeanServer.newMBeanServer(str, mBeanServer, mBeanServerDelegate, true);
    }
}
