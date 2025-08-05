package com.sun.jmx.mbeanserver;

import java.lang.reflect.Method;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

/* loaded from: rt.jar:com/sun/jmx/mbeanserver/StandardMBeanSupport.class */
public class StandardMBeanSupport extends MBeanSupport<Method> {
    public <T> StandardMBeanSupport(T t2, Class<T> cls) throws NotCompliantMBeanException {
        super(t2, cls);
    }

    @Override // com.sun.jmx.mbeanserver.MBeanSupport
    MBeanIntrospector<Method> getMBeanIntrospector() {
        return StandardMBeanIntrospector.getInstance();
    }

    @Override // com.sun.jmx.mbeanserver.MBeanSupport
    Object getCookie() {
        return null;
    }

    @Override // com.sun.jmx.mbeanserver.MBeanSupport
    public void register(MBeanServer mBeanServer, ObjectName objectName) {
    }

    @Override // com.sun.jmx.mbeanserver.MBeanSupport
    public void unregister() {
    }

    @Override // com.sun.jmx.mbeanserver.MBeanSupport, javax.management.DynamicMBean
    public MBeanInfo getMBeanInfo() {
        MBeanInfo mBeanInfo = super.getMBeanInfo();
        if (StandardMBeanIntrospector.isDefinitelyImmutableInfo(getResource().getClass())) {
            return mBeanInfo;
        }
        return new MBeanInfo(mBeanInfo.getClassName(), mBeanInfo.getDescription(), mBeanInfo.getAttributes(), mBeanInfo.getConstructors(), mBeanInfo.getOperations(), MBeanIntrospector.findNotifications(getResource()), mBeanInfo.getDescriptor());
    }
}
