package sun.management;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javax.management.remote.JMXConnectorServer;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;
import sun.management.jdp.JdpController;
import sun.management.jdp.JdpException;
import sun.management.jmxremote.ConnectorBootstrap;
import sun.misc.VMSupport;

/* loaded from: rt.jar:sun/management/Agent.class */
public class Agent {
    private static Properties mgmtProps;
    private static ResourceBundle messageRB;
    private static final String CONFIG_FILE = "com.sun.management.config.file";
    private static final String SNMP_PORT = "com.sun.management.snmp.port";
    private static final String JMXREMOTE = "com.sun.management.jmxremote";
    private static final String JMXREMOTE_PORT = "com.sun.management.jmxremote.port";
    private static final String RMI_PORT = "com.sun.management.jmxremote.rmi.port";
    private static final String ENABLE_THREAD_CONTENTION_MONITORING = "com.sun.management.enableThreadContentionMonitoring";
    private static final String LOCAL_CONNECTOR_ADDRESS_PROP = "com.sun.management.jmxremote.localConnectorAddress";
    private static final String SNMP_ADAPTOR_BOOTSTRAP_CLASS_NAME = "sun.management.snmp.AdaptorBootstrap";
    private static final String JDP_DEFAULT_ADDRESS = "224.0.23.178";
    private static final int JDP_DEFAULT_PORT = 7095;
    private static JMXConnectorServer jmxServer = null;

    private static Properties parseString(String str) {
        Properties properties = new Properties();
        if (str != null && !str.trim().equals("")) {
            for (String str2 : str.split(",")) {
                String[] strArrSplit = str2.split("=", 2);
                String strTrim = strArrSplit[0].trim();
                String strTrim2 = strArrSplit.length > 1 ? strArrSplit[1].trim() : "";
                if (!strTrim.startsWith("com.sun.management.")) {
                    error(AgentConfigurationError.INVALID_OPTION, strTrim);
                }
                properties.setProperty(strTrim, strTrim2);
            }
        }
        return properties;
    }

    public static void premain(String str) throws Exception {
        agentmain(str);
    }

    public static void agentmain(String str) throws Exception {
        if (str == null || str.length() == 0) {
            str = JMXREMOTE;
        }
        Properties string = parseString(str);
        Properties properties = new Properties();
        readConfiguration(string.getProperty("com.sun.management.config.file"), properties);
        properties.putAll(string);
        startAgent(properties);
    }

    private static synchronized void startLocalManagementAgent() {
        Properties agentProperties = VMSupport.getAgentProperties();
        if (agentProperties.get(LOCAL_CONNECTOR_ADDRESS_PROP) == null) {
            String string = ConnectorBootstrap.startLocalConnectorServer().getAddress().toString();
            agentProperties.put(LOCAL_CONNECTOR_ADDRESS_PROP, string);
            try {
                ConnectorAddressLink.export(string);
            } catch (Exception e2) {
                warning(AgentConfigurationError.EXPORT_ADDRESS_FAILED, e2.getMessage());
            }
        }
    }

    private static synchronized void startRemoteManagementAgent(String str) throws Exception {
        if (jmxServer != null) {
            throw new RuntimeException(getText(AgentConfigurationError.INVALID_STATE, "Agent already started"));
        }
        try {
            Properties string = parseString(str);
            Properties properties = new Properties();
            readConfiguration(System.getProperty("com.sun.management.config.file"), properties);
            Properties properties2 = System.getProperties();
            synchronized (properties2) {
                properties.putAll(properties2);
            }
            String property = string.getProperty("com.sun.management.config.file");
            if (property != null) {
                readConfiguration(property, properties);
            }
            properties.putAll(string);
            if (properties.getProperty(ENABLE_THREAD_CONTENTION_MONITORING) != null) {
                java.lang.management.ManagementFactory.getThreadMXBean().setThreadContentionMonitoringEnabled(true);
            }
            String property2 = properties.getProperty("com.sun.management.jmxremote.port");
            if (property2 != null) {
                jmxServer = ConnectorBootstrap.startRemoteConnectorServer(property2, properties);
                startDiscoveryService(properties);
                return;
            }
            throw new AgentConfigurationError(AgentConfigurationError.INVALID_JMXREMOTE_PORT, "No port specified");
        } catch (AgentConfigurationError e2) {
            error(e2);
        }
    }

    private static synchronized void stopRemoteManagementAgent() throws Exception {
        JdpController.stopDiscoveryService();
        if (jmxServer != null) {
            ConnectorBootstrap.unexportRegistry();
            jmxServer.stop();
            jmxServer = null;
        }
    }

    private static void startAgent(Properties properties) throws Exception {
        String property = properties.getProperty(SNMP_PORT);
        String property2 = properties.getProperty(JMXREMOTE);
        String property3 = properties.getProperty("com.sun.management.jmxremote.port");
        if (properties.getProperty(ENABLE_THREAD_CONTENTION_MONITORING) != null) {
            java.lang.management.ManagementFactory.getThreadMXBean().setThreadContentionMonitoringEnabled(true);
        }
        if (property != null) {
            try {
                loadSnmpAgent(property, properties);
            } catch (Exception e2) {
                error(e2);
                return;
            } catch (AgentConfigurationError e3) {
                error(e3);
                return;
            }
        }
        if (property2 != null || property3 != null) {
            if (property3 != null) {
                jmxServer = ConnectorBootstrap.startRemoteConnectorServer(property3, properties);
                startDiscoveryService(properties);
            }
            startLocalManagementAgent();
        }
    }

    private static void startDiscoveryService(Properties properties) throws IOException {
        boolean z2;
        String str;
        String property = properties.getProperty("com.sun.management.jdp.port");
        String property2 = properties.getProperty("com.sun.management.jdp.address");
        String property3 = properties.getProperty("com.sun.management.jmxremote.autodiscovery");
        if (property3 == null) {
            z2 = property != null;
        } else {
            try {
                z2 = Boolean.parseBoolean(property3);
            } catch (NumberFormatException e2) {
                throw new AgentConfigurationError("Couldn't parse autodiscovery argument");
            }
        }
        if (z2) {
            try {
                InetAddress byName = property2 == null ? InetAddress.getByName(JDP_DEFAULT_ADDRESS) : InetAddress.getByName(property2);
                int i2 = JDP_DEFAULT_PORT;
                if (property != null) {
                    try {
                        i2 = Integer.parseInt(property);
                    } catch (NumberFormatException e3) {
                        throw new AgentConfigurationError("Couldn't parse JDP port argument");
                    }
                }
                String property4 = properties.getProperty("com.sun.management.jmxremote.port");
                String property5 = properties.getProperty("com.sun.management.jmxremote.rmi.port");
                String host = jmxServer.getAddress().getHost();
                if (property5 != null) {
                    str = String.format("service:jmx:rmi://%s:%s/jndi/rmi://%s:%s/jmxrmi", host, property5, host, property4);
                } else {
                    str = String.format("service:jmx:rmi:///jndi/rmi://%s:%s/jmxrmi", host, property4);
                }
                try {
                    JdpController.startDiscoveryService(byName, i2, properties.getProperty("com.sun.management.jdp.name"), str);
                } catch (JdpException e4) {
                    throw new AgentConfigurationError("Couldn't start JDP service", e4);
                }
            } catch (UnknownHostException e5) {
                throw new AgentConfigurationError("Unable to broadcast to requested address", e5);
            }
        }
    }

    public static Properties loadManagementProperties() {
        Properties properties = new Properties();
        readConfiguration(System.getProperty("com.sun.management.config.file"), properties);
        Properties properties2 = System.getProperties();
        synchronized (properties2) {
            properties.putAll(properties2);
        }
        return properties;
    }

    public static synchronized Properties getManagementProperties() {
        if (mgmtProps == null) {
            String property = System.getProperty("com.sun.management.config.file");
            String property2 = System.getProperty(SNMP_PORT);
            String property3 = System.getProperty(JMXREMOTE);
            String property4 = System.getProperty("com.sun.management.jmxremote.port");
            if (property == null && property2 == null && property3 == null && property4 == null) {
                return null;
            }
            mgmtProps = loadManagementProperties();
        }
        return mgmtProps;
    }

    private static void loadSnmpAgent(String str, Properties properties) {
        try {
            Class.forName(SNMP_ADAPTOR_BOOTSTRAP_CLASS_NAME, true, null).getMethod(FXMLLoader.INITIALIZE_METHOD_NAME, String.class, Properties.class).invoke(null, str, properties);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException e2) {
            throw new UnsupportedOperationException("Unsupported management property: com.sun.management.snmp.port", e2);
        } catch (InvocationTargetException e3) {
            Throwable cause = e3.getCause();
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            }
            if (cause instanceof Error) {
                throw ((Error) cause);
            }
            throw new UnsupportedOperationException("Unsupported management property: com.sun.management.snmp.port", cause);
        }
    }

    private static void readConfiguration(String str, Properties properties) {
        if (str == null) {
            String property = System.getProperty("java.home");
            if (property == null) {
                throw new Error("Can't find java.home ??");
            }
            StringBuffer stringBuffer = new StringBuffer(property);
            stringBuffer.append(File.separator).append("lib");
            stringBuffer.append(File.separator).append("management");
            stringBuffer.append(File.separator).append(ConnectorBootstrap.DefaultValues.CONFIG_FILE_NAME);
            str = stringBuffer.toString();
        }
        File file = new File(str);
        if (!file.exists()) {
            error(AgentConfigurationError.CONFIG_FILE_NOT_FOUND, str);
        }
        FileInputStream fileInputStream = null;
        try {
            try {
                try {
                    fileInputStream = new FileInputStream(file);
                    properties.load(new BufferedInputStream(fileInputStream));
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e2) {
                            error(AgentConfigurationError.CONFIG_FILE_CLOSE_FAILED, str);
                        }
                    }
                } catch (FileNotFoundException e3) {
                    error(AgentConfigurationError.CONFIG_FILE_OPEN_FAILED, e3.getMessage());
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e4) {
                            error(AgentConfigurationError.CONFIG_FILE_CLOSE_FAILED, str);
                        }
                    }
                } catch (IOException e5) {
                    error(AgentConfigurationError.CONFIG_FILE_OPEN_FAILED, e5.getMessage());
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e6) {
                            error(AgentConfigurationError.CONFIG_FILE_CLOSE_FAILED, str);
                        }
                    }
                }
            } catch (SecurityException e7) {
                error(AgentConfigurationError.CONFIG_FILE_ACCESS_DENIED, str);
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e8) {
                        error(AgentConfigurationError.CONFIG_FILE_CLOSE_FAILED, str);
                    }
                }
            }
        } catch (Throwable th) {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e9) {
                    error(AgentConfigurationError.CONFIG_FILE_CLOSE_FAILED, str);
                }
            }
            throw th;
        }
    }

    public static void startAgent() throws Exception {
        String property = System.getProperty("com.sun.management.agent.class");
        if (property == null) {
            Properties managementProperties = getManagementProperties();
            if (managementProperties != null) {
                startAgent(managementProperties);
                return;
            }
            return;
        }
        String[] strArrSplit = property.split(CallSiteDescriptor.TOKEN_DELIMITER);
        if (strArrSplit.length < 1 || strArrSplit.length > 2) {
            error(AgentConfigurationError.AGENT_CLASS_INVALID, PdfOps.DOUBLE_QUOTE__TOKEN + property + PdfOps.DOUBLE_QUOTE__TOKEN);
        }
        String str = strArrSplit[0];
        String str2 = strArrSplit.length == 2 ? strArrSplit[1] : null;
        if (str == null || str.length() == 0) {
            error(AgentConfigurationError.AGENT_CLASS_INVALID, PdfOps.DOUBLE_QUOTE__TOKEN + property + PdfOps.DOUBLE_QUOTE__TOKEN);
        }
        if (str != null) {
            try {
                ClassLoader.getSystemClassLoader().loadClass(str).getMethod("premain", String.class).invoke(null, str2);
            } catch (ClassNotFoundException e2) {
                error(AgentConfigurationError.AGENT_CLASS_NOT_FOUND, PdfOps.DOUBLE_QUOTE__TOKEN + str + PdfOps.DOUBLE_QUOTE__TOKEN);
            } catch (NoSuchMethodException e3) {
                error(AgentConfigurationError.AGENT_CLASS_PREMAIN_NOT_FOUND, PdfOps.DOUBLE_QUOTE__TOKEN + str + PdfOps.DOUBLE_QUOTE__TOKEN);
            } catch (SecurityException e4) {
                error(AgentConfigurationError.AGENT_CLASS_ACCESS_DENIED);
            } catch (Exception e5) {
                error(AgentConfigurationError.AGENT_CLASS_FAILED, e5.getCause() == null ? e5.getMessage() : e5.getCause().getMessage());
            }
        }
    }

    public static void error(String str) {
        String text = getText(str);
        System.err.print(getText("agent.err.error") + ": " + text);
        throw new RuntimeException(text);
    }

    public static void error(String str, String str2) {
        String text = getText(str);
        System.err.print(getText("agent.err.error") + ": " + text);
        System.err.println(": " + str2);
        throw new RuntimeException(text + ": " + str2);
    }

    public static void error(Exception exc) {
        exc.printStackTrace();
        System.err.println(getText(AgentConfigurationError.AGENT_EXCEPTION) + ": " + exc.toString());
        throw new RuntimeException(exc);
    }

    public static void error(AgentConfigurationError agentConfigurationError) {
        String text = getText(agentConfigurationError.getError());
        String[] params = agentConfigurationError.getParams();
        System.err.print(getText("agent.err.error") + ": " + text);
        if (params != null && params.length != 0) {
            StringBuffer stringBuffer = new StringBuffer(params[0]);
            for (int i2 = 1; i2 < params.length; i2++) {
                stringBuffer.append(" " + params[i2]);
            }
            System.err.println(": " + ((Object) stringBuffer));
        }
        agentConfigurationError.printStackTrace();
        throw new RuntimeException(agentConfigurationError);
    }

    public static void warning(String str, String str2) {
        System.err.print(getText("agent.err.warning") + ": " + getText(str));
        System.err.println(": " + str2);
    }

    private static void initResource() {
        try {
            messageRB = ResourceBundle.getBundle("sun.management.resources.agent");
        } catch (MissingResourceException e2) {
            throw new Error("Fatal: Resource for management agent is missing");
        }
    }

    public static String getText(String str) {
        if (messageRB == null) {
            initResource();
        }
        try {
            return messageRB.getString(str);
        } catch (MissingResourceException e2) {
            return "Missing management agent resource bundle: key = \"" + str + PdfOps.DOUBLE_QUOTE__TOKEN;
        }
    }

    public static String getText(String str, String... strArr) {
        if (messageRB == null) {
            initResource();
        }
        String string = messageRB.getString(str);
        if (string == null) {
            string = "missing resource key: key = \"" + str + "\", arguments = \"{0}\", \"{1}\", \"{2}\"";
        }
        return MessageFormat.format(string, strArr);
    }
}
