package javax.management.remote.rmi;

import com.sun.jmx.remote.internal.IIOPHelper;
import com.sun.jmx.remote.security.MBeanServerFileAccessController;
import com.sun.jmx.remote.util.ClassLogger;
import com.sun.jmx.remote.util.EnvHelp;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.MBeanServerForwarder;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:javax/management/remote/rmi/RMIConnectorServer.class */
public class RMIConnectorServer extends JMXConnectorServer {
    public static final String JNDI_REBIND_ATTRIBUTE = "jmx.remote.jndi.rebind";
    public static final String RMI_CLIENT_SOCKET_FACTORY_ATTRIBUTE = "jmx.remote.rmi.client.socket.factory";
    public static final String RMI_SERVER_SOCKET_FACTORY_ATTRIBUTE = "jmx.remote.rmi.server.socket.factory";
    private JMXServiceURL address;
    private RMIServerImpl rmiServerImpl;
    private final Map<String, ?> attributes;
    private ClassLoader defaultClassLoader;
    private String boundJndiUrl;
    private static final int CREATED = 0;
    private static final int STARTED = 1;
    private static final int STOPPED = 2;
    private int state;
    private static final char[] intToAlpha = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    private static ClassLogger logger = new ClassLogger("javax.management.remote.rmi", "RMIConnectorServer");
    private static final Set<RMIConnectorServer> openedServers = new HashSet();

    public RMIConnectorServer(JMXServiceURL jMXServiceURL, Map<String, ?> map) throws IOException {
        this(jMXServiceURL, map, (MBeanServer) null);
    }

    public RMIConnectorServer(JMXServiceURL jMXServiceURL, Map<String, ?> map, MBeanServer mBeanServer) throws IOException {
        this(jMXServiceURL, map, (RMIServerImpl) null, mBeanServer);
    }

    public RMIConnectorServer(JMXServiceURL jMXServiceURL, Map<String, ?> map, RMIServerImpl rMIServerImpl, MBeanServer mBeanServer) throws IOException {
        super(mBeanServer);
        this.defaultClassLoader = null;
        this.state = 0;
        if (jMXServiceURL == null) {
            throw new IllegalArgumentException("Null JMXServiceURL");
        }
        if (rMIServerImpl == null) {
            String protocol = jMXServiceURL.getProtocol();
            if (protocol == null || (!protocol.equals("rmi") && !protocol.equals("iiop"))) {
                throw new MalformedURLException("Invalid protocol type: " + protocol);
            }
            String uRLPath = jMXServiceURL.getURLPath();
            if (!uRLPath.equals("") && !uRLPath.equals("/") && !uRLPath.startsWith("/jndi/")) {
                throw new MalformedURLException("URL path must be empty or start with /jndi/");
            }
        }
        if (map == null) {
            this.attributes = Collections.emptyMap();
        } else {
            EnvHelp.checkAttributes(map);
            this.attributes = Collections.unmodifiableMap(map);
        }
        this.address = jMXServiceURL;
        this.rmiServerImpl = rMIServerImpl;
    }

    @Override // javax.management.remote.JMXConnectorServer, javax.management.remote.JMXConnectorServerMBean
    public JMXConnector toJMXConnector(Map<String, ?> map) throws IOException {
        if (!isActive()) {
            throw new IllegalStateException("Connector is not active");
        }
        HashMap map2 = new HashMap(this.attributes == null ? Collections.emptyMap() : this.attributes);
        if (map != null) {
            EnvHelp.checkAttributes(map);
            map2.putAll(map);
        }
        return new RMIConnector((RMIServer) this.rmiServerImpl.toStub(), (Map<String, ?>) EnvHelp.filterAttributes(map2));
    }

    @Override // javax.management.remote.JMXConnectorServerMBean
    public synchronized void start() throws IOException {
        RMIServerImpl rMIServerImplNewServer;
        String str;
        boolean zTraceOn = logger.traceOn();
        if (this.state == 1) {
            if (zTraceOn) {
                logger.trace("start", "already started");
                return;
            }
            return;
        }
        if (this.state == 2) {
            if (zTraceOn) {
                logger.trace("start", "already stopped");
            }
            throw new IOException("The server has been stopped.");
        }
        if (getMBeanServer() == null) {
            throw new IllegalStateException("This connector server is not attached to an MBean server");
        }
        if (this.attributes != null && (str = (String) this.attributes.get("jmx.remote.x.access.file")) != null) {
            try {
                setMBeanServerForwarder(new MBeanServerFileAccessController(str));
            } catch (IOException e2) {
                throw ((IllegalArgumentException) EnvHelp.initCause(new IllegalArgumentException(e2.getMessage()), e2));
            }
        }
        if (zTraceOn) {
            try {
                logger.trace("start", "setting default class loader");
            } catch (InstanceNotFoundException e3) {
                throw ((IllegalArgumentException) EnvHelp.initCause(new IllegalArgumentException("ClassLoader not found: " + ((Object) e3)), e3));
            }
        }
        this.defaultClassLoader = EnvHelp.resolveServerClassLoader(this.attributes, getMBeanServer());
        if (zTraceOn) {
            logger.trace("start", "setting RMIServer object");
        }
        if (this.rmiServerImpl != null) {
            rMIServerImplNewServer = this.rmiServerImpl;
        } else {
            rMIServerImplNewServer = newServer();
        }
        rMIServerImplNewServer.setMBeanServer(getMBeanServer());
        rMIServerImplNewServer.setDefaultClassLoader(this.defaultClassLoader);
        rMIServerImplNewServer.setRMIConnectorServer(this);
        rMIServerImplNewServer.export();
        if (zTraceOn) {
            try {
                logger.trace("start", "getting RMIServer object to export");
            } catch (Exception e4) {
                try {
                    rMIServerImplNewServer.close();
                } catch (Exception e5) {
                }
                if (e4 instanceof RuntimeException) {
                    throw ((RuntimeException) e4);
                }
                if (e4 instanceof IOException) {
                    throw ((IOException) e4);
                }
                throw newIOException("Got unexpected exception while starting the connector server: " + ((Object) e4), e4);
            }
        }
        RMIServer rMIServerObjectToBind = objectToBind(rMIServerImplNewServer, this.attributes);
        if (this.address != null && this.address.getURLPath().startsWith("/jndi/")) {
            String strSubstring = this.address.getURLPath().substring(6);
            if (zTraceOn) {
                logger.trace("start", "Using external directory: " + strSubstring);
            }
            boolean zComputeBooleanFromString = EnvHelp.computeBooleanFromString((String) this.attributes.get(JNDI_REBIND_ATTRIBUTE));
            if (zTraceOn) {
                logger.trace("start", "jmx.remote.jndi.rebind=" + zComputeBooleanFromString);
            }
            if (zTraceOn) {
                try {
                    logger.trace("start", "binding to " + strSubstring);
                } catch (NamingException e6) {
                    throw newIOException("Cannot bind to URL [" + strSubstring + "]: " + ((Object) e6), e6);
                }
            }
            bind(strSubstring, EnvHelp.mapToHashtable(this.attributes), rMIServerObjectToBind, zComputeBooleanFromString);
            this.boundJndiUrl = strSubstring;
        } else {
            if (zTraceOn) {
                logger.trace("start", "Encoding URL");
            }
            encodeStubInAddress(rMIServerObjectToBind, this.attributes);
            if (zTraceOn) {
                logger.trace("start", "Encoded URL: " + ((Object) this.address));
            }
        }
        this.rmiServerImpl = rMIServerImplNewServer;
        synchronized (openedServers) {
            openedServers.add(this);
        }
        this.state = 1;
        if (zTraceOn) {
            logger.trace("start", "Connector Server Address = " + ((Object) this.address));
            logger.trace("start", "started.");
        }
    }

    @Override // javax.management.remote.JMXConnectorServerMBean
    public void stop() throws IOException {
        boolean zTraceOn = logger.traceOn();
        synchronized (this) {
            if (this.state == 2) {
                if (zTraceOn) {
                    logger.trace("stop", "already stopped.");
                }
                return;
            }
            if (this.state == 0 && zTraceOn) {
                logger.trace("stop", "not started yet.");
            }
            if (zTraceOn) {
                logger.trace("stop", "stopping.");
            }
            this.state = 2;
            synchronized (openedServers) {
                openedServers.remove(this);
            }
            IOException iOExceptionNewIOException = null;
            if (this.rmiServerImpl != null) {
                if (zTraceOn) {
                    try {
                        logger.trace("stop", "closing RMI server.");
                    } catch (IOException e2) {
                        if (zTraceOn) {
                            logger.trace("stop", "failed to close RMI server: " + ((Object) e2));
                        }
                        if (logger.debugOn()) {
                            logger.debug("stop", e2);
                        }
                        iOExceptionNewIOException = e2;
                    }
                }
                this.rmiServerImpl.close();
            }
            if (this.boundJndiUrl != null) {
                if (zTraceOn) {
                    try {
                        logger.trace("stop", "unbind from external directory: " + this.boundJndiUrl);
                    } catch (NamingException e3) {
                        if (zTraceOn) {
                            logger.trace("stop", "failed to unbind RMI server: " + ((Object) e3));
                        }
                        if (logger.debugOn()) {
                            logger.debug("stop", e3);
                        }
                        if (iOExceptionNewIOException == null) {
                            iOExceptionNewIOException = newIOException("Cannot bind to URL: " + ((Object) e3), e3);
                        }
                    }
                }
                InitialContext initialContext = new InitialContext((Hashtable<?, ?>) EnvHelp.mapToHashtable(this.attributes));
                initialContext.unbind(this.boundJndiUrl);
                initialContext.close();
            }
            if (iOExceptionNewIOException != null) {
                throw iOExceptionNewIOException;
            }
            if (zTraceOn) {
                logger.trace("stop", "stopped");
            }
        }
    }

    @Override // javax.management.remote.JMXConnectorServerMBean
    public synchronized boolean isActive() {
        return this.state == 1;
    }

    @Override // javax.management.remote.JMXConnectorServerMBean, javax.management.remote.JMXAddressable
    public JMXServiceURL getAddress() {
        if (!isActive()) {
            return null;
        }
        return this.address;
    }

    @Override // javax.management.remote.JMXConnectorServerMBean
    public Map<String, ?> getAttributes() {
        return Collections.unmodifiableMap(EnvHelp.filterAttributes(this.attributes));
    }

    @Override // javax.management.remote.JMXConnectorServer, javax.management.remote.JMXConnectorServerMBean
    public synchronized void setMBeanServerForwarder(MBeanServerForwarder mBeanServerForwarder) {
        super.setMBeanServerForwarder(mBeanServerForwarder);
        if (this.rmiServerImpl != null) {
            this.rmiServerImpl.setMBeanServer(getMBeanServer());
        }
    }

    @Override // javax.management.remote.JMXConnectorServer
    protected void connectionOpened(String str, String str2, Object obj) {
        super.connectionOpened(str, str2, obj);
    }

    @Override // javax.management.remote.JMXConnectorServer
    protected void connectionClosed(String str, String str2, Object obj) {
        super.connectionClosed(str, str2, obj);
    }

    @Override // javax.management.remote.JMXConnectorServer
    protected void connectionFailed(String str, String str2, Object obj) {
        super.connectionFailed(str, str2, obj);
    }

    void bind(String str, Hashtable<?, ?> hashtable, RMIServer rMIServer, boolean z2) throws MalformedURLException, NamingException {
        InitialContext initialContext = new InitialContext(hashtable);
        if (z2) {
            initialContext.rebind(str, rMIServer);
        } else {
            initialContext.bind(str, rMIServer);
        }
        initialContext.close();
    }

    RMIServerImpl newServer() throws IOException {
        int port;
        boolean zIsIiopURL = isIiopURL(this.address, true);
        if (this.address == null) {
            port = 0;
        } else {
            port = this.address.getPort();
        }
        if (zIsIiopURL) {
            return newIIOPServer(this.attributes);
        }
        return newJRMPServer(this.attributes, port);
    }

    private void encodeStubInAddress(RMIServer rMIServer, Map<String, ?> map) throws IOException {
        String protocol;
        String host;
        int port;
        if (this.address == null) {
            if (IIOPHelper.isStub(rMIServer)) {
                protocol = "iiop";
            } else {
                protocol = "rmi";
            }
            host = null;
            port = 0;
        } else {
            protocol = this.address.getProtocol();
            host = this.address.getHost().equals("") ? null : this.address.getHost();
            port = this.address.getPort();
        }
        this.address = new JMXServiceURL(protocol, host, port, encodeStub(rMIServer, map));
    }

    static boolean isIiopURL(JMXServiceURL jMXServiceURL, boolean z2) throws MalformedURLException {
        String protocol = jMXServiceURL.getProtocol();
        if (protocol.equals("rmi")) {
            return false;
        }
        if (protocol.equals("iiop")) {
            return true;
        }
        if (z2) {
            throw new MalformedURLException("URL must have protocol \"rmi\" or \"iiop\": \"" + protocol + PdfOps.DOUBLE_QUOTE__TOKEN);
        }
        return false;
    }

    static String encodeStub(RMIServer rMIServer, Map<String, ?> map) throws IOException {
        if (IIOPHelper.isStub(rMIServer)) {
            return "/ior/" + encodeIIOPStub(rMIServer, map);
        }
        return "/stub/" + encodeJRMPStub(rMIServer, map);
    }

    static String encodeJRMPStub(RMIServer rMIServer, Map<String, ?> map) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(rMIServer);
        objectOutputStream.close();
        return byteArrayToBase64(byteArrayOutputStream.toByteArray());
    }

    static String encodeIIOPStub(RMIServer rMIServer, Map<String, ?> map) throws IOException {
        try {
            return IIOPHelper.objectToString(IIOPHelper.getOrb(rMIServer), rMIServer);
        } catch (RuntimeException e2) {
            throw newIOException(e2.getMessage(), e2);
        }
    }

    private static RMIServer objectToBind(RMIServerImpl rMIServerImpl, Map<String, ?> map) throws IOException {
        return RMIConnector.connectStub((RMIServer) rMIServerImpl.toStub(), map);
    }

    private static RMIServerImpl newJRMPServer(Map<String, ?> map, int i2) throws IOException {
        return new RMIJRMPServerImpl(i2, (RMIClientSocketFactory) map.get(RMI_CLIENT_SOCKET_FACTORY_ATTRIBUTE), (RMIServerSocketFactory) map.get(RMI_SERVER_SOCKET_FACTORY_ATTRIBUTE), map);
    }

    private static RMIServerImpl newIIOPServer(Map<String, ?> map) throws IOException {
        return new RMIIIOPServerImpl(map);
    }

    private static String byteArrayToBase64(byte[] bArr) {
        int length = bArr.length;
        int i2 = length / 3;
        int i3 = length - (3 * i2);
        StringBuilder sb = new StringBuilder(4 * ((length + 2) / 3));
        int i4 = 0;
        for (int i5 = 0; i5 < i2; i5++) {
            int i6 = i4;
            int i7 = i4 + 1;
            int i8 = bArr[i6] & 255;
            int i9 = i7 + 1;
            int i10 = bArr[i7] & 255;
            i4 = i9 + 1;
            int i11 = bArr[i9] & 255;
            sb.append(intToAlpha[i8 >> 2]);
            sb.append(intToAlpha[((i8 << 4) & 63) | (i10 >> 4)]);
            sb.append(intToAlpha[((i10 << 2) & 63) | (i11 >> 6)]);
            sb.append(intToAlpha[i11 & 63]);
        }
        if (i3 != 0) {
            int i12 = i4;
            int i13 = i4 + 1;
            int i14 = bArr[i12] & 255;
            sb.append(intToAlpha[i14 >> 2]);
            if (i3 == 1) {
                sb.append(intToAlpha[(i14 << 4) & 63]);
                sb.append("==");
            } else {
                int i15 = i13 + 1;
                int i16 = bArr[i13] & 255;
                sb.append(intToAlpha[((i14 << 4) & 63) | (i16 >> 4)]);
                sb.append(intToAlpha[(i16 << 2) & 63]);
                sb.append('=');
            }
        }
        return sb.toString();
    }

    private static IOException newIOException(String str, Throwable th) {
        return (IOException) EnvHelp.initCause(new IOException(str), th);
    }
}
