package com.sun.jmx.remote.protocol.rmi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerProvider;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnectorServer;

/* loaded from: rt.jar:com/sun/jmx/remote/protocol/rmi/ServerProvider.class */
public class ServerProvider implements JMXConnectorServerProvider {
    @Override // javax.management.remote.JMXConnectorServerProvider
    public JMXConnectorServer newJMXConnectorServer(JMXServiceURL jMXServiceURL, Map<String, ?> map, MBeanServer mBeanServer) throws IOException {
        if (!jMXServiceURL.getProtocol().equals("rmi")) {
            throw new MalformedURLException("Protocol not rmi: " + jMXServiceURL.getProtocol());
        }
        return new RMIConnectorServer(jMXServiceURL, map, mBeanServer);
    }
}
