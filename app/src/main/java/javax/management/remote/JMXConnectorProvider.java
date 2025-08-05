package javax.management.remote;

import java.io.IOException;
import java.util.Map;

/* loaded from: rt.jar:javax/management/remote/JMXConnectorProvider.class */
public interface JMXConnectorProvider {
    JMXConnector newJMXConnector(JMXServiceURL jMXServiceURL, Map<String, ?> map) throws IOException;
}
