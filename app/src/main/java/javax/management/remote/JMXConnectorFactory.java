package javax.management.remote;

import com.sun.jmx.mbeanserver.Util;
import com.sun.jmx.remote.util.ClassLogger;
import com.sun.jmx.remote.util.EnvHelp;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.StringTokenizer;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:javax/management/remote/JMXConnectorFactory.class */
public class JMXConnectorFactory {
    public static final String DEFAULT_CLASS_LOADER = "jmx.remote.default.class.loader";
    public static final String PROTOCOL_PROVIDER_PACKAGES = "jmx.remote.protocol.provider.pkgs";
    public static final String PROTOCOL_PROVIDER_CLASS_LOADER = "jmx.remote.protocol.provider.class.loader";
    private static final String PROTOCOL_PROVIDER_DEFAULT_PACKAGE = "com.sun.jmx.remote.protocol";
    private static final ClassLogger logger = new ClassLogger("javax.management.remote.misc", "JMXConnectorFactory");

    private JMXConnectorFactory() {
    }

    public static JMXConnector connect(JMXServiceURL jMXServiceURL) throws IOException {
        return connect(jMXServiceURL, null);
    }

    public static JMXConnector connect(JMXServiceURL jMXServiceURL, Map<String, ?> map) throws IOException {
        if (jMXServiceURL == null) {
            throw new NullPointerException("Null JMXServiceURL");
        }
        JMXConnector jMXConnectorNewJMXConnector = newJMXConnector(jMXServiceURL, map);
        jMXConnectorNewJMXConnector.connect(map);
        return jMXConnectorNewJMXConnector;
    }

    private static <K, V> Map<K, V> newHashMap() {
        return new HashMap();
    }

    private static <K> Map<K, Object> newHashMap(Map<K, ?> map) {
        return new HashMap(map);
    }

    public static JMXConnector newJMXConnector(JMXServiceURL jMXServiceURL, Map<String, ?> map) throws IOException {
        Map mapNewHashMap;
        if (map == null) {
            mapNewHashMap = newHashMap();
        } else {
            EnvHelp.checkAttributes(map);
            mapNewHashMap = newHashMap(map);
        }
        ClassLoader classLoaderResolveClassLoader = resolveClassLoader(mapNewHashMap);
        String protocol = jMXServiceURL.getProtocol();
        JMXConnectorProvider jMXConnectorProvider = (JMXConnectorProvider) getProvider(jMXServiceURL, (Map<String, Object>) mapNewHashMap, "ClientProvider", JMXConnectorProvider.class, classLoaderResolveClassLoader);
        IOException iOException = null;
        if (jMXConnectorProvider == null) {
            if (classLoaderResolveClassLoader != null) {
                try {
                    JMXConnector connectorAsService = getConnectorAsService(classLoaderResolveClassLoader, jMXServiceURL, mapNewHashMap);
                    if (connectorAsService != null) {
                        return connectorAsService;
                    }
                } catch (JMXProviderException e2) {
                    throw e2;
                } catch (IOException e3) {
                    iOException = e3;
                }
            }
            jMXConnectorProvider = (JMXConnectorProvider) getProvider(protocol, PROTOCOL_PROVIDER_DEFAULT_PACKAGE, JMXConnectorFactory.class.getClassLoader(), "ClientProvider", JMXConnectorProvider.class);
        }
        if (jMXConnectorProvider == null) {
            MalformedURLException malformedURLException = new MalformedURLException("Unsupported protocol: " + protocol);
            if (iOException == null) {
                throw malformedURLException;
            }
            throw ((MalformedURLException) EnvHelp.initCause(malformedURLException, iOException));
        }
        return jMXConnectorProvider.newJMXConnector(jMXServiceURL, Collections.unmodifiableMap(mapNewHashMap));
    }

    private static String resolvePkgs(Map<String, ?> map) throws JMXProviderException {
        Object objDoPrivileged = null;
        if (map != null) {
            objDoPrivileged = map.get("jmx.remote.protocol.provider.pkgs");
        }
        if (objDoPrivileged == null) {
            objDoPrivileged = AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: javax.management.remote.JMXConnectorFactory.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public String run2() {
                    return System.getProperty("jmx.remote.protocol.provider.pkgs");
                }
            });
        }
        if (objDoPrivileged == null) {
            return null;
        }
        if (!(objDoPrivileged instanceof String)) {
            throw new JMXProviderException("Value of jmx.remote.protocol.provider.pkgs parameter is not a String: " + objDoPrivileged.getClass().getName());
        }
        String str = (String) objDoPrivileged;
        if (str.trim().equals("")) {
            return null;
        }
        if (str.startsWith(CallSiteDescriptor.OPERATOR_DELIMITER) || str.endsWith(CallSiteDescriptor.OPERATOR_DELIMITER) || str.indexOf("||") >= 0) {
            throw new JMXProviderException("Value of jmx.remote.protocol.provider.pkgs contains an empty element: " + str);
        }
        return str;
    }

    static <T> T getProvider(JMXServiceURL jMXServiceURL, Map<String, Object> map, String str, Class<T> cls, ClassLoader classLoader) throws IOException {
        String protocol = jMXServiceURL.getProtocol();
        String strResolvePkgs = resolvePkgs(map);
        Object provider = null;
        if (strResolvePkgs != null) {
            provider = getProvider(protocol, strResolvePkgs, classLoader, str, cls);
            if (provider != null) {
                map.put("jmx.remote.protocol.provider.class.loader", classLoader != provider.getClass().getClassLoader() ? wrap(classLoader) : classLoader);
            }
        }
        return (T) provider;
    }

    static <T> Iterator<T> getProviderIterator(Class<T> cls, ClassLoader classLoader) {
        return ServiceLoader.load(cls, classLoader).iterator();
    }

    private static ClassLoader wrap(final ClassLoader classLoader) {
        if (classLoader != null) {
            return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() { // from class: javax.management.remote.JMXConnectorFactory.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public ClassLoader run2() {
                    return new ClassLoader(classLoader) { // from class: javax.management.remote.JMXConnectorFactory.2.1
                        @Override // java.lang.ClassLoader
                        protected Class<?> loadClass(String str, boolean z2) throws ClassNotFoundException {
                            ReflectUtil.checkPackageAccess(str);
                            return super.loadClass(str, z2);
                        }
                    };
                }
            });
        }
        return null;
    }

    private static JMXConnector getConnectorAsService(ClassLoader classLoader, JMXServiceURL jMXServiceURL, Map<String, ?> map) throws IOException {
        Iterator providerIterator = getProviderIterator(JMXConnectorProvider.class, classLoader);
        IOException iOException = null;
        while (providerIterator.hasNext()) {
            try {
                return ((JMXConnectorProvider) providerIterator.next()).newJMXConnector(jMXServiceURL, map);
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

    static <T> T getProvider(String str, String str2, ClassLoader classLoader, String str3, Class<T> cls) throws IOException {
        StringTokenizer stringTokenizer = new StringTokenizer(str2, CallSiteDescriptor.OPERATOR_DELIMITER);
        while (stringTokenizer.hasMoreTokens()) {
            String str4 = stringTokenizer.nextToken() + "." + protocol2package(str) + "." + str3;
            try {
                Class<?> cls2 = Class.forName(str4, true, classLoader);
                if (!cls.isAssignableFrom(cls2)) {
                    throw new JMXProviderException("Provider class does not implement " + cls.getName() + ": " + cls2.getName());
                }
                try {
                    return (T) ((Class) Util.cast(cls2)).newInstance();
                } catch (Exception e2) {
                    throw new JMXProviderException("Exception when instantiating provider [" + str4 + "]", e2);
                }
            } catch (ClassNotFoundException e3) {
            }
        }
        return null;
    }

    static ClassLoader resolveClassLoader(Map<String, ?> map) {
        ClassLoader contextClassLoader = null;
        if (map != null) {
            try {
                contextClassLoader = (ClassLoader) map.get("jmx.remote.protocol.provider.class.loader");
            } catch (ClassCastException e2) {
                throw new IllegalArgumentException("The ClassLoader supplied in the environment map using the jmx.remote.protocol.provider.class.loader attribute is not an instance of java.lang.ClassLoader");
            }
        }
        if (contextClassLoader == null) {
            contextClassLoader = Thread.currentThread().getContextClassLoader();
        }
        return contextClassLoader;
    }

    private static String protocol2package(String str) {
        return str.replace('+', '.').replace('-', '_');
    }
}
