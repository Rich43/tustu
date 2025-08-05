package javax.management.remote.rmi;

import com.sun.jmx.remote.internal.ArrayNotificationBuffer;
import com.sun.jmx.remote.internal.NotificationBuffer;
import com.sun.jmx.remote.security.JMXPluggableAuthenticator;
import com.sun.jmx.remote.util.ClassLogger;
import java.io.Closeable;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.rmi.Remote;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.management.MBeanServer;
import javax.management.remote.JMXAuthenticator;
import javax.management.remote.JMXConnectorServer;
import javax.security.auth.Subject;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:javax/management/remote/rmi/RMIServerImpl.class */
public abstract class RMIServerImpl implements Closeable, RMIServer {
    private static final ClassLogger logger = new ClassLogger("javax.management.remote.rmi", "RMIServerImpl");
    private final List<WeakReference<RMIConnection>> clientList = new ArrayList();
    private ClassLoader cl;
    private MBeanServer mbeanServer;
    private final Map<String, ?> env;
    private RMIConnectorServer connServer;
    private static int connectionIdNumber;
    private NotificationBuffer notifBuffer;

    protected abstract void export() throws IOException;

    public abstract Remote toStub() throws IOException;

    protected abstract RMIConnection makeClient(String str, Subject subject) throws IOException;

    protected abstract void closeClient(RMIConnection rMIConnection) throws IOException;

    protected abstract String getProtocol();

    protected abstract void closeServer() throws IOException;

    public RMIServerImpl(Map<String, ?> map) {
        this.env = map == null ? Collections.emptyMap() : map;
    }

    void setRMIConnectorServer(RMIConnectorServer rMIConnectorServer) throws IOException {
        this.connServer = rMIConnectorServer;
    }

    public synchronized void setDefaultClassLoader(ClassLoader classLoader) {
        this.cl = classLoader;
    }

    public synchronized ClassLoader getDefaultClassLoader() {
        return this.cl;
    }

    public synchronized void setMBeanServer(MBeanServer mBeanServer) {
        this.mbeanServer = mBeanServer;
    }

    public synchronized MBeanServer getMBeanServer() {
        return this.mbeanServer;
    }

    @Override // javax.management.remote.rmi.RMIServer
    public String getVersion() {
        try {
            return "1.0 java_runtime_" + System.getProperty("java.runtime.version");
        } catch (SecurityException e2) {
            return "1.0 ";
        }
    }

    @Override // javax.management.remote.rmi.RMIServer
    public RMIConnection newClient(Object obj) throws IOException {
        return doNewClient(obj);
    }

    RMIConnection doNewClient(Object obj) throws IOException {
        boolean zTraceOn = logger.traceOn();
        if (zTraceOn) {
            logger.trace("newClient", "making new client");
        }
        if (getMBeanServer() == null) {
            throw new IllegalStateException("Not attached to an MBean server");
        }
        Subject subjectAuthenticate = null;
        JMXAuthenticator jMXPluggableAuthenticator = (JMXAuthenticator) this.env.get(JMXConnectorServer.AUTHENTICATOR);
        if (jMXPluggableAuthenticator == null && (this.env.get("jmx.remote.x.password.file") != null || this.env.get("jmx.remote.x.login.config") != null)) {
            jMXPluggableAuthenticator = new JMXPluggableAuthenticator(this.env);
        }
        if (jMXPluggableAuthenticator != null) {
            if (zTraceOn) {
                logger.trace("newClient", "got authenticator: " + jMXPluggableAuthenticator.getClass().getName());
            }
            try {
                subjectAuthenticate = jMXPluggableAuthenticator.authenticate(obj);
            } catch (SecurityException e2) {
                logger.trace("newClient", "Authentication failed: " + ((Object) e2));
                throw e2;
            }
        }
        if (zTraceOn) {
            if (subjectAuthenticate != null) {
                logger.trace("newClient", "subject is not null");
            } else {
                logger.trace("newClient", "no subject");
            }
        }
        String strMakeConnectionId = makeConnectionId(getProtocol(), subjectAuthenticate);
        if (zTraceOn) {
            logger.trace("newClient", "making new connection: " + strMakeConnectionId);
        }
        RMIConnection rMIConnectionMakeClient = makeClient(strMakeConnectionId, subjectAuthenticate);
        dropDeadReferences();
        WeakReference<RMIConnection> weakReference = new WeakReference<>(rMIConnectionMakeClient);
        synchronized (this.clientList) {
            this.clientList.add(weakReference);
        }
        this.connServer.connectionOpened(strMakeConnectionId, "Connection opened", null);
        synchronized (this.clientList) {
            if (!this.clientList.contains(weakReference)) {
                throw new IOException("The connection is refused.");
            }
        }
        if (zTraceOn) {
            logger.trace("newClient", "new connection done: " + strMakeConnectionId);
        }
        return rMIConnectionMakeClient;
    }

    protected void clientClosed(RMIConnection rMIConnection) throws IOException {
        boolean zDebugOn = logger.debugOn();
        if (zDebugOn) {
            logger.trace("clientClosed", "client=" + ((Object) rMIConnection));
        }
        if (rMIConnection == null) {
            throw new NullPointerException("Null client");
        }
        synchronized (this.clientList) {
            dropDeadReferences();
            Iterator<WeakReference<RMIConnection>> it = this.clientList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                } else if (it.next().get() == rMIConnection) {
                    it.remove();
                    break;
                }
            }
        }
        if (zDebugOn) {
            logger.trace("clientClosed", "closing client.");
        }
        closeClient(rMIConnection);
        if (zDebugOn) {
            logger.trace("clientClosed", "sending notif");
        }
        this.connServer.connectionClosed(rMIConnection.getConnectionId(), "Client connection closed", null);
        if (zDebugOn) {
            logger.trace("clientClosed", "done");
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:39:0x00fe, code lost:
    
        r0.close();
     */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0092 A[Catch: all -> 0x014b, TryCatch #2 {, blocks: (B:25:0x0080, B:26:0x008a, B:28:0x0092, B:29:0x00b5, B:32:0x00c3, B:34:0x00c7, B:35:0x00d2, B:37:0x00dc, B:39:0x00fe, B:43:0x010e, B:46:0x012e, B:53:0x0147), top: B:79:0x0080, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00c7 A[Catch: all -> 0x014b, TRY_ENTER, TryCatch #2 {, blocks: (B:25:0x0080, B:26:0x008a, B:28:0x0092, B:29:0x00b5, B:32:0x00c3, B:34:0x00c7, B:35:0x00d2, B:37:0x00dc, B:39:0x00fe, B:43:0x010e, B:46:0x012e, B:53:0x0147), top: B:79:0x0080, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x015d  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x016a  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x017a  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x00c1 A[EDGE_INSN: B:81:0x00c1->B:31:0x00c1 BREAK  A[LOOP:0: B:21:0x0074->B:54:0x0148], SYNTHETIC] */
    @Override // java.io.Closeable, java.lang.AutoCloseable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized void close() throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 393
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.management.remote.rmi.RMIServerImpl.close():void");
    }

    private static synchronized String makeConnectionId(String str, Subject subject) {
        connectionIdNumber++;
        String clientHost = "";
        try {
            clientHost = RemoteServer.getClientHost();
            if (clientHost.contains(CallSiteDescriptor.TOKEN_DELIMITER)) {
                clientHost = "[" + clientHost + "]";
            }
        } catch (ServerNotActiveException e2) {
            logger.trace("makeConnectionId", "getClientHost", e2);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str).append(CallSiteDescriptor.TOKEN_DELIMITER);
        if (clientHost.length() > 0) {
            sb.append("//").append(clientHost);
        }
        sb.append(" ");
        if (subject != null) {
            String str2 = "";
            Iterator<Principal> it = subject.getPrincipals().iterator();
            while (it.hasNext()) {
                sb.append(str2).append(it.next().getName().replace(' ', '_').replace(';', ':'));
                str2 = ";";
            }
        }
        sb.append(" ").append(connectionIdNumber);
        if (logger.traceOn()) {
            logger.trace("newConnectionId", "connectionId=" + ((Object) sb));
        }
        return sb.toString();
    }

    private void dropDeadReferences() {
        synchronized (this.clientList) {
            Iterator<WeakReference<RMIConnection>> it = this.clientList.iterator();
            while (it.hasNext()) {
                if (it.next().get() == null) {
                    it.remove();
                }
            }
        }
    }

    synchronized NotificationBuffer getNotifBuffer() {
        if (this.notifBuffer == null) {
            this.notifBuffer = ArrayNotificationBuffer.getNotificationBuffer(this.mbeanServer, this.env);
        }
        return this.notifBuffer;
    }
}
