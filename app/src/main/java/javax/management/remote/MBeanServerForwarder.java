package javax.management.remote;

import javax.management.MBeanServer;

/* loaded from: rt.jar:javax/management/remote/MBeanServerForwarder.class */
public interface MBeanServerForwarder extends MBeanServer {
    MBeanServer getMBeanServer();

    void setMBeanServer(MBeanServer mBeanServer);
}
