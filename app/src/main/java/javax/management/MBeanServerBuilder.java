package javax.management;

import com.sun.jmx.mbeanserver.JmxMBeanServer;

/* loaded from: rt.jar:javax/management/MBeanServerBuilder.class */
public class MBeanServerBuilder {
    public MBeanServerDelegate newMBeanServerDelegate() {
        return JmxMBeanServer.newMBeanServerDelegate();
    }

    public MBeanServer newMBeanServer(String str, MBeanServer mBeanServer, MBeanServerDelegate mBeanServerDelegate) {
        return JmxMBeanServer.newMBeanServer(str, mBeanServer, mBeanServerDelegate, false);
    }
}
