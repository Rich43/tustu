package javax.management.remote;

import java.io.IOException;
import java.util.Map;

/* loaded from: rt.jar:javax/management/remote/JMXConnectorServerMBean.class */
public interface JMXConnectorServerMBean {
    void start() throws IOException;

    void stop() throws IOException;

    boolean isActive();

    void setMBeanServerForwarder(MBeanServerForwarder mBeanServerForwarder);

    String[] getConnectionIds();

    JMXServiceURL getAddress();

    Map<String, ?> getAttributes();

    JMXConnector toJMXConnector(Map<String, ?> map) throws IOException;
}
