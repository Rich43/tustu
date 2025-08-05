package com.sun.jmx.mbeanserver;

import javax.management.DynamicMBean;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/* loaded from: rt.jar:com/sun/jmx/mbeanserver/DynamicMBean2.class */
public interface DynamicMBean2 extends DynamicMBean {
    Object getResource();

    String getClassName();

    void preRegister2(MBeanServer mBeanServer, ObjectName objectName) throws Exception;

    void registerFailed();
}
