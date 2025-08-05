package javax.management.remote;

import com.sun.jmx.remote.util.ClassLogger;
import com.sun.jmx.remote.util.EnvHelp;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.management.MBeanServer;

/* loaded from: rt.jar:javax/management/remote/JMXConnectorServerFactory.class */
public class JMXConnectorServerFactory {
    public static final String DEFAULT_CLASS_LOADER = "jmx.remote.default.class.loader";
    public static final String DEFAULT_CLASS_LOADER_NAME = "jmx.remote.default.class.loader.name";
    public static final String PROTOCOL_PROVIDER_PACKAGES = "jmx.remote.protocol.provider.pkgs";
    public static final String PROTOCOL_PROVIDER_CLASS_LOADER = "jmx.remote.protocol.provider.class.loader";
    private static final String PROTOCOL_PROVIDER_DEFAULT_PACKAGE = "com.sun.jmx.remote.protocol";
    private static final ClassLogger logger = new ClassLogger("javax.management.remote.misc", "JMXConnectorServerFactory");

    private JMXConnectorServerFactory() {
    }

    private static JMXConnectorServer getConnectorServerAsService(ClassLoader classLoader, JMXServiceURL jMXServiceURL, Map<String, ?> map, MBeanServer mBeanServer) throws IOException {
        Iterator providerIterator = JMXConnectorFactory.getProviderIterator(JMXConnectorServerProvider.class, classLoader);
        IOException iOException = null;
        while (providerIterator.hasNext()) {
            try {
                return ((JMXConnectorServerProvider) providerIterator.next()).newJMXConnectorServer(jMXServiceURL, map, mBeanServer);
            } catch (JMXProviderException e2) {
                throw e2;
            } catch (Exception e3) {
                if (logger.traceOn()) {
                    logger.trace("getConnectorAsService", "URL[" + ((Object) jMXServiceURL) + "] Service provider exception: " + ((Object) e3));
                }
                if (!(e3 instanceof MalformedURLException) && iOException == null) {
                    if (e3 instanceof IOException) {
                        iOException = (IOException) e3;
                    } else {
                        iOException = (IOException) EnvHelp.initCause(new IOException(e3.getMessage()), e3);
                    }
                }
            }
        }
        if (iOException == null) {
            return null;
        }
        throw iOException;
    }

    public static JMXConnectorServer newJMXConnectorServer(JMXServiceURL jMXServiceURL, Map<String, ?> map, MBeanServer mBeanServer) throws IOException {
        HashMap map2;
        if (map == null) {
            map2 = new HashMap();
        } else {
            EnvHelp.checkAttributes(map);
            map2 = new HashMap(map);
        }
        ClassLoader classLoaderResolveClassLoader = JMXConnectorFactory.resolveClassLoader(map2);
        String protocol = jMXServiceURL.getProtocol();
        JMXConnectorServerProvider jMXConnectorServerProvider = (JMXConnectorServerProvider) JMXConnectorFactory.getProvider(jMXServiceURL, map2, "ServerProvider", JMXConnectorServerProvider.class, classLoaderResolveClassLoader);
        IOException iOException = null;
        if (jMXConnectorServerProvider == null) {
            if (classLoaderResolveClassLoader != null) {
                try {
                    JMXConnectorServer connectorServerAsService = getConnectorServerAsService(classLoaderResolveClassLoader, jMXServiceURL, map2, mBeanServer);
                    if (connectorServerAsService != null) {
                        return connectorServerAsService;
                    }
                } catch (JMXProviderException e2) {
                    throw e2;
                } catch (IOException e3) {
                    iOException = e3;
                }
            }
            jMXConnectorServerProvider = (JMXConnectorServerProvider) JMXConnectorFactory.getProvider(protocol, PROTOCOL_PROVIDER_DEFAULT_PACKAGE, JMXConnectorFactory.class.getClassLoader(), "ServerProvider", JMXConnectorServerProvider.class);
        }
        if (jMXConnectorServerProvider == null) {
            MalformedURLException malformedURLException = new MalformedURLException("Unsupported protocol: " + protocol);
            if (iOException == null) {
                throw malformedURLException;
            }
            throw ((MalformedURLException) EnvHelp.initCause(malformedURLException, iOException));
        }
        return jMXConnectorServerProvider.newJMXConnectorServer(jMXServiceURL, Collections.unmodifiableMap(map2), mBeanServer);
    }
}
