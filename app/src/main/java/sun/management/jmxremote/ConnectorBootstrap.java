package sun.management.jmxremote;

import com.sun.jmx.remote.internal.RMIExporter;
import com.sun.jmx.remote.security.JMXPluggableAuthenticator;
import com.sun.jmx.remote.util.ClassLogger;
import com.sun.jmx.remote.util.EnvHelp;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.RemoteObject;
import java.rmi.server.UnicastRemoteObject;
import java.security.KeyStore;
import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import javax.management.MBeanServer;
import javax.management.remote.JMXAuthenticator;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnectorServer;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;
import javax.security.auth.Subject;
import sun.management.Agent;
import sun.management.AgentConfigurationError;
import sun.management.ConnectorAddressLink;
import sun.management.FileSystem;
import sun.rmi.server.UnicastRef;
import sun.rmi.server.UnicastServerRef;
import sun.rmi.server.UnicastServerRef2;

/* loaded from: rt.jar:sun/management/jmxremote/ConnectorBootstrap.class */
public final class ConnectorBootstrap {
    private static Registry registry = null;
    private static final ClassLogger log = new ClassLogger(ConnectorBootstrap.class.getPackage().getName(), "ConnectorBootstrap");

    /* loaded from: rt.jar:sun/management/jmxremote/ConnectorBootstrap$DefaultValues.class */
    public interface DefaultValues {
        public static final String PORT = "0";
        public static final String CONFIG_FILE_NAME = "management.properties";
        public static final String USE_SSL = "true";
        public static final String USE_LOCAL_ONLY = "true";
        public static final String USE_REGISTRY_SSL = "false";
        public static final String USE_AUTHENTICATION = "true";
        public static final String PASSWORD_FILE_NAME = "jmxremote.password";
        public static final String ACCESS_FILE_NAME = "jmxremote.access";
        public static final String SSL_NEED_CLIENT_AUTH = "false";
    }

    /* loaded from: rt.jar:sun/management/jmxremote/ConnectorBootstrap$PropertyNames.class */
    public interface PropertyNames {
        public static final String PORT = "com.sun.management.jmxremote.port";
        public static final String HOST = "com.sun.management.jmxremote.host";
        public static final String RMI_PORT = "com.sun.management.jmxremote.rmi.port";
        public static final String LOCAL_PORT = "com.sun.management.jmxremote.local.port";
        public static final String CONFIG_FILE_NAME = "com.sun.management.config.file";
        public static final String USE_LOCAL_ONLY = "com.sun.management.jmxremote.local.only";
        public static final String USE_SSL = "com.sun.management.jmxremote.ssl";
        public static final String USE_REGISTRY_SSL = "com.sun.management.jmxremote.registry.ssl";
        public static final String USE_AUTHENTICATION = "com.sun.management.jmxremote.authenticate";
        public static final String PASSWORD_FILE_NAME = "com.sun.management.jmxremote.password.file";
        public static final String ACCESS_FILE_NAME = "com.sun.management.jmxremote.access.file";
        public static final String LOGIN_CONFIG_NAME = "com.sun.management.jmxremote.login.config";
        public static final String SSL_ENABLED_CIPHER_SUITES = "com.sun.management.jmxremote.ssl.enabled.cipher.suites";
        public static final String SSL_ENABLED_PROTOCOLS = "com.sun.management.jmxremote.ssl.enabled.protocols";
        public static final String SSL_NEED_CLIENT_AUTH = "com.sun.management.jmxremote.ssl.need.client.auth";
        public static final String SSL_CONFIG_FILE_NAME = "com.sun.management.jmxremote.ssl.config.file";
    }

    /* loaded from: rt.jar:sun/management/jmxremote/ConnectorBootstrap$JMXConnectorServerData.class */
    private static class JMXConnectorServerData {
        JMXConnectorServer jmxConnectorServer;
        JMXServiceURL jmxRemoteURL;

        public JMXConnectorServerData(JMXConnectorServer jMXConnectorServer, JMXServiceURL jMXServiceURL) {
            this.jmxConnectorServer = jMXConnectorServer;
            this.jmxRemoteURL = jMXServiceURL;
        }
    }

    /* loaded from: rt.jar:sun/management/jmxremote/ConnectorBootstrap$PermanentExporter.class */
    private static class PermanentExporter implements RMIExporter {
        Remote firstExported;

        private PermanentExporter() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v11, types: [sun.rmi.server.UnicastServerRef] */
        @Override // com.sun.jmx.remote.internal.RMIExporter
        public Remote exportObject(Remote remote, int i2, RMIClientSocketFactory rMIClientSocketFactory, RMIServerSocketFactory rMIServerSocketFactory) throws RemoteException {
            UnicastServerRef2 unicastServerRef2;
            synchronized (this) {
                if (this.firstExported == null) {
                    this.firstExported = remote;
                }
            }
            if (rMIClientSocketFactory == null && rMIServerSocketFactory == null) {
                unicastServerRef2 = new UnicastServerRef(i2);
            } else {
                unicastServerRef2 = new UnicastServerRef2(i2, rMIClientSocketFactory, rMIServerSocketFactory);
            }
            return unicastServerRef2.exportObject(remote, null, true);
        }

        @Override // com.sun.jmx.remote.internal.RMIExporter
        public boolean unexportObject(Remote remote, boolean z2) throws NoSuchObjectException {
            return UnicastRemoteObject.unexportObject(remote, z2);
        }
    }

    /* loaded from: rt.jar:sun/management/jmxremote/ConnectorBootstrap$AccessFileCheckerAuthenticator.class */
    private static class AccessFileCheckerAuthenticator implements JMXAuthenticator {
        private final Map<String, Object> environment;
        private final Properties properties;
        private final String accessFile;

        public AccessFileCheckerAuthenticator(Map<String, Object> map) throws IOException {
            this.environment = map;
            this.accessFile = (String) map.get("jmx.remote.x.access.file");
            this.properties = propertiesFromFile(this.accessFile);
        }

        @Override // javax.management.remote.JMXAuthenticator
        public Subject authenticate(Object obj) {
            Subject subjectAuthenticate = new JMXPluggableAuthenticator(this.environment).authenticate(obj);
            checkAccessFileEntries(subjectAuthenticate);
            return subjectAuthenticate;
        }

        private void checkAccessFileEntries(Subject subject) {
            if (subject == null) {
                throw new SecurityException("Access denied! No matching entries found in the access file [" + this.accessFile + "] as the authenticated Subject is null");
            }
            Set<Principal> principals = subject.getPrincipals();
            Iterator<Principal> it = principals.iterator();
            while (it.hasNext()) {
                if (this.properties.containsKey(it.next().getName())) {
                    return;
                }
            }
            HashSet hashSet = new HashSet();
            Iterator<Principal> it2 = principals.iterator();
            while (it2.hasNext()) {
                hashSet.add(it2.next().getName());
            }
            throw new SecurityException("Access denied! No entries found in the access file [" + this.accessFile + "] for any of the authenticated identities " + ((Object) hashSet));
        }

        private static Properties propertiesFromFile(String str) throws IOException {
            Properties properties = new Properties();
            if (str == null) {
                return properties;
            }
            FileInputStream fileInputStream = new FileInputStream(str);
            Throwable th = null;
            try {
                try {
                    properties.load(fileInputStream);
                    if (fileInputStream != null) {
                        if (0 != 0) {
                            try {
                                fileInputStream.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        } else {
                            fileInputStream.close();
                        }
                    }
                    return properties;
                } finally {
                }
            } catch (Throwable th3) {
                if (fileInputStream != null) {
                    if (th != null) {
                        try {
                            fileInputStream.close();
                        } catch (Throwable th4) {
                            th.addSuppressed(th4);
                        }
                    } else {
                        fileInputStream.close();
                    }
                }
                throw th3;
            }
        }
    }

    public static void unexportRegistry() {
        try {
            if (registry != null) {
                UnicastRemoteObject.unexportObject(registry, true);
                registry = null;
            }
        } catch (NoSuchObjectException e2) {
        }
    }

    public static synchronized JMXConnectorServer initialize() {
        Properties propertiesLoadManagementProperties = Agent.loadManagementProperties();
        if (propertiesLoadManagementProperties == null) {
            return null;
        }
        return startRemoteConnectorServer(propertiesLoadManagementProperties.getProperty(PropertyNames.PORT), propertiesLoadManagementProperties);
    }

    public static synchronized JMXConnectorServer initialize(String str, Properties properties) {
        return startRemoteConnectorServer(str, properties);
    }

    public static synchronized JMXConnectorServer startRemoteConnectorServer(String str, Properties properties) {
        String str2;
        try {
            int i2 = Integer.parseInt(str);
            if (i2 < 0) {
                throw new AgentConfigurationError(AgentConfigurationError.INVALID_JMXREMOTE_PORT, str);
            }
            int i3 = 0;
            String property = properties.getProperty(PropertyNames.RMI_PORT);
            if (property != null) {
                try {
                    i3 = Integer.parseInt(property);
                } catch (NumberFormatException e2) {
                    throw new AgentConfigurationError(AgentConfigurationError.INVALID_JMXREMOTE_RMI_PORT, e2, property);
                }
            }
            if (i3 < 0) {
                throw new AgentConfigurationError(AgentConfigurationError.INVALID_JMXREMOTE_RMI_PORT, property);
            }
            String property2 = properties.getProperty(PropertyNames.USE_AUTHENTICATION, "true");
            boolean zBooleanValue = Boolean.valueOf(property2).booleanValue();
            String property3 = properties.getProperty(PropertyNames.USE_SSL, "true");
            boolean zBooleanValue2 = Boolean.valueOf(property3).booleanValue();
            String property4 = properties.getProperty(PropertyNames.USE_REGISTRY_SSL, "false");
            boolean zBooleanValue3 = Boolean.valueOf(property4).booleanValue();
            String property5 = properties.getProperty(PropertyNames.SSL_ENABLED_CIPHER_SUITES);
            String[] strArr = null;
            if (property5 != null) {
                StringTokenizer stringTokenizer = new StringTokenizer(property5, ",");
                int iCountTokens = stringTokenizer.countTokens();
                strArr = new String[iCountTokens];
                for (int i4 = 0; i4 < iCountTokens; i4++) {
                    strArr[i4] = stringTokenizer.nextToken();
                }
            }
            String property6 = properties.getProperty(PropertyNames.SSL_ENABLED_PROTOCOLS);
            String[] strArr2 = null;
            if (property6 != null) {
                StringTokenizer stringTokenizer2 = new StringTokenizer(property6, ",");
                int iCountTokens2 = stringTokenizer2.countTokens();
                strArr2 = new String[iCountTokens2];
                for (int i5 = 0; i5 < iCountTokens2; i5++) {
                    strArr2[i5] = stringTokenizer2.nextToken();
                }
            }
            String property7 = properties.getProperty(PropertyNames.SSL_NEED_CLIENT_AUTH, "false");
            boolean zBooleanValue4 = Boolean.valueOf(property7).booleanValue();
            String property8 = properties.getProperty(PropertyNames.SSL_CONFIG_FILE_NAME);
            String property9 = null;
            String property10 = null;
            String property11 = null;
            if (zBooleanValue) {
                property9 = properties.getProperty(PropertyNames.LOGIN_CONFIG_NAME);
                if (property9 == null) {
                    property10 = properties.getProperty(PropertyNames.PASSWORD_FILE_NAME, getDefaultFileName(DefaultValues.PASSWORD_FILE_NAME));
                    checkPasswordFile(property10);
                }
                property11 = properties.getProperty(PropertyNames.ACCESS_FILE_NAME, getDefaultFileName(DefaultValues.ACCESS_FILE_NAME));
                checkAccessFile(property11);
            }
            String property12 = properties.getProperty(PropertyNames.HOST);
            if (log.debugOn()) {
                ClassLogger classLogger = log;
                StringBuilder sbAppend = new StringBuilder().append(Agent.getText("jmxremote.ConnectorBootstrap.starting")).append("\n\t").append(PropertyNames.PORT).append("=").append(i2).append(property12 == null ? "" : "\n\tcom.sun.management.jmxremote.host=" + property12).append("\n\t").append(PropertyNames.RMI_PORT).append("=").append(i3).append("\n\t").append(PropertyNames.USE_SSL).append("=").append(zBooleanValue2).append("\n\t").append(PropertyNames.USE_REGISTRY_SSL).append("=").append(zBooleanValue3).append("\n\t").append(PropertyNames.SSL_CONFIG_FILE_NAME).append("=").append(property8).append("\n\t").append(PropertyNames.SSL_ENABLED_CIPHER_SUITES).append("=").append(property5).append("\n\t").append(PropertyNames.SSL_ENABLED_PROTOCOLS).append("=").append(property6).append("\n\t").append(PropertyNames.SSL_NEED_CLIENT_AUTH).append("=").append(zBooleanValue4).append("\n\t").append(PropertyNames.USE_AUTHENTICATION).append("=").append(zBooleanValue);
                if (zBooleanValue) {
                    str2 = property9 == null ? "\n\tcom.sun.management.jmxremote.password.file=" + property10 : "\n\tcom.sun.management.jmxremote.login.config=" + property9;
                } else {
                    str2 = "\n\t" + Agent.getText("jmxremote.ConnectorBootstrap.noAuthentication");
                }
                classLogger.debug("startRemoteConnectorServer", sbAppend.append(str2).append(zBooleanValue ? "\n\tcom.sun.management.jmxremote.access.file=" + property11 : "").append("").toString());
            }
            try {
                JMXConnectorServerData jMXConnectorServerDataExportMBeanServer = exportMBeanServer(ManagementFactory.getPlatformMBeanServer(), i2, i3, zBooleanValue2, zBooleanValue3, property8, strArr, strArr2, zBooleanValue4, zBooleanValue, property9, property10, property11, property12);
                JMXConnectorServer jMXConnectorServer = jMXConnectorServerDataExportMBeanServer.jmxConnectorServer;
                JMXServiceURL jMXServiceURL = jMXConnectorServerDataExportMBeanServer.jmxRemoteURL;
                log.config("startRemoteConnectorServer", Agent.getText("jmxremote.ConnectorBootstrap.ready", jMXServiceURL.toString()));
                try {
                    HashMap map = new HashMap();
                    map.put("remoteAddress", jMXServiceURL.toString());
                    map.put("authenticate", property2);
                    map.put("ssl", property3);
                    map.put("sslRegistry", property4);
                    map.put("sslNeedClientAuth", property7);
                    ConnectorAddressLink.exportRemote(map);
                } catch (Exception e3) {
                    log.debug("startRemoteConnectorServer", e3);
                }
                return jMXConnectorServer;
            } catch (Exception e4) {
                throw new AgentConfigurationError(AgentConfigurationError.AGENT_EXCEPTION, e4, e4.toString());
            }
        } catch (NumberFormatException e5) {
            throw new AgentConfigurationError(AgentConfigurationError.INVALID_JMXREMOTE_PORT, e5, str);
        }
    }

    public static JMXConnectorServer startLocalConnectorServer() {
        System.setProperty("java.rmi.server.randomIDs", "true");
        HashMap map = new HashMap();
        map.put(RMIExporter.EXPORTER_ATTRIBUTE, new PermanentExporter());
        map.put(EnvHelp.CREDENTIAL_TYPES, new String[]{String[].class.getName(), String.class.getName()});
        String hostAddress = "localhost";
        InetAddress byName = null;
        try {
            byName = InetAddress.getByName(hostAddress);
            hostAddress = byName.getHostAddress();
        } catch (UnknownHostException e2) {
        }
        if (byName == null || !byName.isLoopbackAddress()) {
            hostAddress = "127.0.0.1";
        }
        MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
        try {
            Properties managementProperties = Agent.getManagementProperties();
            if (managementProperties == null) {
                managementProperties = new Properties();
            }
            int i2 = 0;
            String property = managementProperties.getProperty(PropertyNames.LOCAL_PORT);
            if (property != null) {
                try {
                    i2 = Integer.parseInt(property);
                } catch (NumberFormatException e3) {
                    throw new AgentConfigurationError(AgentConfigurationError.INVALID_JMXREMOTE_LOCAL_PORT, e3, property);
                }
            }
            if (i2 < 0) {
                throw new AgentConfigurationError(AgentConfigurationError.INVALID_JMXREMOTE_LOCAL_PORT, property);
            }
            try {
                JMXServiceURL jMXServiceURL = new JMXServiceURL("rmi", hostAddress, i2);
                if (Boolean.valueOf(managementProperties.getProperty(PropertyNames.USE_LOCAL_ONLY, "true")).booleanValue()) {
                    map.put(RMIConnectorServer.RMI_SERVER_SOCKET_FACTORY_ATTRIBUTE, new LocalRMIServerSocketFactory());
                }
                JMXConnectorServer jMXConnectorServerNewJMXConnectorServer = JMXConnectorServerFactory.newJMXConnectorServer(jMXServiceURL, map, platformMBeanServer);
                jMXConnectorServerNewJMXConnectorServer.start();
                return jMXConnectorServerNewJMXConnectorServer;
            } catch (Exception e4) {
                throw new AgentConfigurationError(AgentConfigurationError.AGENT_EXCEPTION, e4, e4.toString());
            }
        } catch (Exception e5) {
            throw new AgentConfigurationError(AgentConfigurationError.AGENT_EXCEPTION, e5, e5.toString());
        }
    }

    private static void checkPasswordFile(String str) {
        if (str == null || str.length() == 0) {
            throw new AgentConfigurationError(AgentConfigurationError.PASSWORD_FILE_NOT_SET);
        }
        File file = new File(str);
        if (!file.exists()) {
            throw new AgentConfigurationError(AgentConfigurationError.PASSWORD_FILE_NOT_FOUND, str);
        }
        if (!file.canRead()) {
            throw new AgentConfigurationError(AgentConfigurationError.PASSWORD_FILE_NOT_READABLE, str);
        }
        FileSystem fileSystemOpen = FileSystem.open();
        try {
            if (fileSystemOpen.supportsFileSecurity(file) && !fileSystemOpen.isAccessUserOnly(file)) {
                log.config("startRemoteConnectorServer", Agent.getText("jmxremote.ConnectorBootstrap.password.readonly", str));
                throw new AgentConfigurationError(AgentConfigurationError.PASSWORD_FILE_ACCESS_NOT_RESTRICTED, str);
            }
        } catch (IOException e2) {
            throw new AgentConfigurationError(AgentConfigurationError.PASSWORD_FILE_READ_FAILED, e2, str);
        }
    }

    private static void checkAccessFile(String str) {
        if (str == null || str.length() == 0) {
            throw new AgentConfigurationError(AgentConfigurationError.ACCESS_FILE_NOT_SET);
        }
        File file = new File(str);
        if (!file.exists()) {
            throw new AgentConfigurationError(AgentConfigurationError.ACCESS_FILE_NOT_FOUND, str);
        }
        if (!file.canRead()) {
            throw new AgentConfigurationError(AgentConfigurationError.ACCESS_FILE_NOT_READABLE, str);
        }
    }

    private static void checkRestrictedFile(String str) {
        if (str == null || str.length() == 0) {
            throw new AgentConfigurationError(AgentConfigurationError.FILE_NOT_SET);
        }
        File file = new File(str);
        if (!file.exists()) {
            throw new AgentConfigurationError(AgentConfigurationError.FILE_NOT_FOUND, str);
        }
        if (!file.canRead()) {
            throw new AgentConfigurationError(AgentConfigurationError.FILE_NOT_READABLE, str);
        }
        FileSystem fileSystemOpen = FileSystem.open();
        try {
            if (fileSystemOpen.supportsFileSecurity(file) && !fileSystemOpen.isAccessUserOnly(file)) {
                log.config("startRemoteConnectorServer", Agent.getText("jmxremote.ConnectorBootstrap.file.readonly", str));
                throw new AgentConfigurationError(AgentConfigurationError.FILE_ACCESS_NOT_RESTRICTED, str);
            }
        } catch (IOException e2) {
            throw new AgentConfigurationError(AgentConfigurationError.FILE_READ_FAILED, e2, str);
        }
    }

    private static String getDefaultFileName(String str) {
        String str2 = File.separator;
        return System.getProperty("java.home") + str2 + "lib" + str2 + "management" + str2 + str;
    }

    private static SslRMIServerSocketFactory createSslRMIServerSocketFactory(String str, String[] strArr, String[] strArr2, boolean z2, String str2) {
        FileInputStream fileInputStream;
        if (str == null) {
            return new HostAwareSslSocketFactory(strArr, strArr2, z2, str2);
        }
        checkRestrictedFile(str);
        try {
            Properties properties = new Properties();
            FileInputStream fileInputStream2 = new FileInputStream(str);
            Throwable th = null;
            try {
                try {
                    properties.load(new BufferedInputStream(fileInputStream2));
                    if (fileInputStream2 != null) {
                        if (0 != 0) {
                            try {
                                fileInputStream2.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        } else {
                            fileInputStream2.close();
                        }
                    }
                    String property = properties.getProperty("javax.net.ssl.keyStore");
                    String property2 = properties.getProperty("javax.net.ssl.keyStorePassword", "");
                    String property3 = properties.getProperty("javax.net.ssl.trustStore");
                    String property4 = properties.getProperty("javax.net.ssl.trustStorePassword", "");
                    char[] charArray = null;
                    if (property2.length() != 0) {
                        charArray = property2.toCharArray();
                    }
                    char[] charArray2 = null;
                    if (property4.length() != 0) {
                        charArray2 = property4.toCharArray();
                    }
                    KeyStore keyStore = null;
                    if (property != null) {
                        keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                        fileInputStream = new FileInputStream(property);
                        Throwable th3 = null;
                        try {
                            try {
                                keyStore.load(fileInputStream, charArray);
                                if (fileInputStream != null) {
                                    if (0 != 0) {
                                        try {
                                            fileInputStream.close();
                                        } catch (Throwable th4) {
                                            th3.addSuppressed(th4);
                                        }
                                    } else {
                                        fileInputStream.close();
                                    }
                                }
                            } finally {
                            }
                        } finally {
                        }
                    }
                    KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                    keyManagerFactory.init(keyStore, charArray);
                    KeyStore keyStore2 = null;
                    if (property3 != null) {
                        keyStore2 = KeyStore.getInstance(KeyStore.getDefaultType());
                        fileInputStream = new FileInputStream(property3);
                        Throwable th5 = null;
                        try {
                            try {
                                keyStore2.load(fileInputStream, charArray2);
                                if (fileInputStream != null) {
                                    if (0 != 0) {
                                        try {
                                            fileInputStream.close();
                                        } catch (Throwable th6) {
                                            th5.addSuppressed(th6);
                                        }
                                    } else {
                                        fileInputStream.close();
                                    }
                                }
                            } finally {
                            }
                        } finally {
                        }
                    }
                    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                    trustManagerFactory.init(keyStore2);
                    SSLContext sSLContext = SSLContext.getInstance("SSL");
                    sSLContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
                    return new HostAwareSslSocketFactory(sSLContext, strArr, strArr2, z2, str2);
                } finally {
                }
            } finally {
            }
        } catch (Exception e2) {
            throw new AgentConfigurationError(AgentConfigurationError.AGENT_EXCEPTION, e2, e2.toString());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v45, types: [sun.management.jmxremote.ConnectorBootstrap$HostAwareSocketFactory] */
    private static JMXConnectorServerData exportMBeanServer(MBeanServer mBeanServer, int i2, int i3, boolean z2, boolean z3, String str, String[] strArr, String[] strArr2, boolean z4, boolean z5, String str2, String str3, String str4, String str5) throws IOException {
        System.setProperty("java.rmi.server.randomIDs", "true");
        JMXServiceURL jMXServiceURL = new JMXServiceURL("rmi", str5, i3);
        HashMap map = new HashMap();
        PermanentExporter permanentExporter = new PermanentExporter();
        map.put(RMIExporter.EXPORTER_ATTRIBUTE, permanentExporter);
        map.put(EnvHelp.CREDENTIAL_TYPES, new String[]{String[].class.getName(), String.class.getName()});
        boolean z6 = (str5 == null || z2) ? false : true;
        if (z5) {
            if (str2 != null) {
                map.put("jmx.remote.x.login.config", str2);
            }
            if (str3 != null) {
                map.put("jmx.remote.x.password.file", str3);
            }
            map.put("jmx.remote.x.access.file", str4);
            if (map.get("jmx.remote.x.password.file") != null || map.get("jmx.remote.x.login.config") != null) {
                map.put(JMXConnectorServer.AUTHENTICATOR, new AccessFileCheckerAuthenticator(map));
            }
        }
        SslRMIClientSocketFactory sslRMIClientSocketFactory = null;
        SslRMIServerSocketFactory sslRMIServerSocketFactoryCreateSslRMIServerSocketFactory = null;
        if (z2 || z3) {
            sslRMIClientSocketFactory = new SslRMIClientSocketFactory();
            sslRMIServerSocketFactoryCreateSslRMIServerSocketFactory = createSslRMIServerSocketFactory(str, strArr, strArr2, z4, str5);
        }
        if (z2) {
            map.put(RMIConnectorServer.RMI_CLIENT_SOCKET_FACTORY_ATTRIBUTE, sslRMIClientSocketFactory);
            map.put(RMIConnectorServer.RMI_SERVER_SOCKET_FACTORY_ATTRIBUTE, sslRMIServerSocketFactoryCreateSslRMIServerSocketFactory);
        }
        if (z6) {
            sslRMIServerSocketFactoryCreateSslRMIServerSocketFactory = new HostAwareSocketFactory(str5);
            map.put(RMIConnectorServer.RMI_SERVER_SOCKET_FACTORY_ATTRIBUTE, sslRMIServerSocketFactoryCreateSslRMIServerSocketFactory);
        }
        JMXConnectorServer jMXConnectorServerNewJMXConnectorServer = null;
        try {
            jMXConnectorServerNewJMXConnectorServer = JMXConnectorServerFactory.newJMXConnectorServer(jMXServiceURL, map, mBeanServer);
            jMXConnectorServerNewJMXConnectorServer.start();
            if (z3 || z6) {
                registry = new SingleEntryRegistry(i2, sslRMIClientSocketFactory, sslRMIServerSocketFactoryCreateSslRMIServerSocketFactory, "jmxrmi", permanentExporter.firstExported);
            } else {
                registry = new SingleEntryRegistry(i2, "jmxrmi", permanentExporter.firstExported);
            }
            return new JMXConnectorServerData(jMXConnectorServerNewJMXConnectorServer, new JMXServiceURL(String.format("service:jmx:rmi:///jndi/rmi://%s:%d/jmxrmi", jMXServiceURL.getHost(), Integer.valueOf(((UnicastRef) ((RemoteObject) registry).getRef()).getLiveRef().getPort()))));
        } catch (IOException e2) {
            if (jMXConnectorServerNewJMXConnectorServer == null || jMXConnectorServerNewJMXConnectorServer.getAddress() == null) {
                throw new AgentConfigurationError(AgentConfigurationError.CONNECTOR_SERVER_IO_ERROR, e2, jMXServiceURL.toString());
            }
            throw new AgentConfigurationError(AgentConfigurationError.CONNECTOR_SERVER_IO_ERROR, e2, jMXConnectorServerNewJMXConnectorServer.getAddress().toString());
        }
    }

    private ConnectorBootstrap() {
    }

    /* loaded from: rt.jar:sun/management/jmxremote/ConnectorBootstrap$HostAwareSocketFactory.class */
    private static class HostAwareSocketFactory implements RMIServerSocketFactory {
        private final String bindAddress;

        private HostAwareSocketFactory(String str) {
            this.bindAddress = str;
        }

        @Override // java.rmi.server.RMIServerSocketFactory
        public ServerSocket createServerSocket(int i2) throws IOException {
            if (this.bindAddress == null) {
                return new ServerSocket(i2);
            }
            try {
                return new ServerSocket(i2, 0, InetAddress.getByName(this.bindAddress));
            } catch (UnknownHostException e2) {
                return new ServerSocket(i2);
            }
        }
    }

    /* loaded from: rt.jar:sun/management/jmxremote/ConnectorBootstrap$HostAwareSslSocketFactory.class */
    private static class HostAwareSslSocketFactory extends SslRMIServerSocketFactory {
        private final String bindAddress;
        private final String[] enabledCipherSuites;
        private final String[] enabledProtocols;
        private final boolean needClientAuth;
        private final SSLContext context;

        private HostAwareSslSocketFactory(String[] strArr, String[] strArr2, boolean z2, String str) throws IllegalArgumentException {
            this((SSLContext) null, strArr, strArr2, z2, str);
        }

        private HostAwareSslSocketFactory(SSLContext sSLContext, String[] strArr, String[] strArr2, boolean z2, String str) throws IllegalArgumentException {
            this.context = sSLContext;
            this.bindAddress = str;
            this.enabledProtocols = strArr2;
            this.enabledCipherSuites = strArr;
            this.needClientAuth = z2;
            checkValues(sSLContext, strArr, strArr2);
        }

        @Override // javax.rmi.ssl.SslRMIServerSocketFactory, java.rmi.server.RMIServerSocketFactory
        public ServerSocket createServerSocket(int i2) throws IOException {
            if (this.bindAddress != null) {
                try {
                    return new SslServerSocket(i2, 0, InetAddress.getByName(this.bindAddress), this.context, this.enabledCipherSuites, this.enabledProtocols, this.needClientAuth);
                } catch (UnknownHostException e2) {
                    return new SslServerSocket(i2, this.context, this.enabledCipherSuites, this.enabledProtocols, this.needClientAuth);
                }
            }
            return new SslServerSocket(i2, this.context, this.enabledCipherSuites, this.enabledProtocols, this.needClientAuth);
        }

        private static void checkValues(SSLContext sSLContext, String[] strArr, String[] strArr2) throws IllegalArgumentException {
            SSLSocketFactory socketFactory = sSLContext == null ? (SSLSocketFactory) SSLSocketFactory.getDefault() : sSLContext.getSocketFactory();
            SSLSocket sSLSocket = null;
            if (strArr != null || strArr2 != null) {
                try {
                    sSLSocket = (SSLSocket) socketFactory.createSocket();
                } catch (Exception e2) {
                    throw ((IllegalArgumentException) new IllegalArgumentException("Unable to check if the cipher suites and protocols to enable are supported").initCause(e2));
                }
            }
            if (strArr != null) {
                sSLSocket.setEnabledCipherSuites(strArr);
            }
            if (strArr2 != null) {
                sSLSocket.setEnabledProtocols(strArr2);
            }
        }
    }

    /* loaded from: rt.jar:sun/management/jmxremote/ConnectorBootstrap$SslServerSocket.class */
    private static class SslServerSocket extends ServerSocket {
        private static SSLSocketFactory defaultSSLSocketFactory;
        private final String[] enabledCipherSuites;
        private final String[] enabledProtocols;
        private final boolean needClientAuth;
        private final SSLContext context;

        private SslServerSocket(int i2, SSLContext sSLContext, String[] strArr, String[] strArr2, boolean z2) throws IOException {
            super(i2);
            this.enabledProtocols = strArr2;
            this.enabledCipherSuites = strArr;
            this.needClientAuth = z2;
            this.context = sSLContext;
        }

        private SslServerSocket(int i2, int i3, InetAddress inetAddress, SSLContext sSLContext, String[] strArr, String[] strArr2, boolean z2) throws IOException {
            super(i2, i3, inetAddress);
            this.enabledProtocols = strArr2;
            this.enabledCipherSuites = strArr;
            this.needClientAuth = z2;
            this.context = sSLContext;
        }

        @Override // java.net.ServerSocket
        public Socket accept() throws IOException {
            SSLSocketFactory defaultSSLSocketFactory2 = this.context == null ? getDefaultSSLSocketFactory() : this.context.getSocketFactory();
            Socket socketAccept = super.accept();
            SSLSocket sSLSocket = (SSLSocket) defaultSSLSocketFactory2.createSocket(socketAccept, socketAccept.getInetAddress().getHostName(), socketAccept.getPort(), true);
            sSLSocket.setUseClientMode(false);
            if (this.enabledCipherSuites != null) {
                sSLSocket.setEnabledCipherSuites(this.enabledCipherSuites);
            }
            if (this.enabledProtocols != null) {
                sSLSocket.setEnabledProtocols(this.enabledProtocols);
            }
            sSLSocket.setNeedClientAuth(this.needClientAuth);
            return sSLSocket;
        }

        private static synchronized SSLSocketFactory getDefaultSSLSocketFactory() {
            if (defaultSSLSocketFactory == null) {
                defaultSSLSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
                return defaultSSLSocketFactory;
            }
            return defaultSSLSocketFactory;
        }
    }
}
