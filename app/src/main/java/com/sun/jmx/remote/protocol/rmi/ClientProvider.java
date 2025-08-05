package com.sun.jmx.remote.protocol.rmi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorProvider;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnector;

/* loaded from: rt.jar:com/sun/jmx/remote/protocol/rmi/ClientProvider.class */
public class ClientProvider implements JMXConnectorProvider {
    @Override // javax.management.remote.JMXConnectorProvider
    public JMXConnector newJMXConnector(JMXServiceURL jMXServiceURL, Map<String, ?> map) throws IOException {
        if (!jMXServiceURL.getProtocol().equals("rmi")) {
            throw new MalformedURLException("Protocol not rmi: " + jMXServiceURL.getProtocol());
        }
        return new RMIConnector(jMXServiceURL, map);
    }
}
