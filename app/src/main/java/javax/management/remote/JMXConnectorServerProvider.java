package javax.management.remote;

import java.io.IOException;
import java.util.Map;
import javax.management.MBeanServer;

/* loaded from: rt.jar:javax/management/remote/JMXConnectorServerProvider.class */
public interface JMXConnectorServerProvider {
    JMXConnectorServer newJMXConnectorServer(JMXServiceURL jMXServiceURL, Map<String, ?> map, MBeanServer mBeanServer) throws IOException;
}
