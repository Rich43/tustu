package javax.management.remote;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;

/* loaded from: rt.jar:javax/management/remote/JMXConnectorServer.class */
public abstract class JMXConnectorServer extends NotificationBroadcasterSupport implements JMXConnectorServerMBean, MBeanRegistration, JMXAddressable {
    public static final String AUTHENTICATOR = "jmx.remote.authenticator";
    private MBeanServer mbeanServer;
    private ObjectName myName;
    private final List<String> connectionIds;
    private static final int[] sequenceNumberLock = new int[0];
    private static long sequenceNumber;

    public JMXConnectorServer() {
        this(null);
    }

    public JMXConnectorServer(MBeanServer mBeanServer) {
        this.mbeanServer = null;
        this.connectionIds = new ArrayList();
        this.mbeanServer = mBeanServer;
    }

    public synchronized MBeanServer getMBeanServer() {
        return this.mbeanServer;
    }

    @Override // javax.management.remote.JMXConnectorServerMBean
    public synchronized void setMBeanServerForwarder(MBeanServerForwarder mBeanServerForwarder) {
        if (mBeanServerForwarder == null) {
            throw new IllegalArgumentException("Invalid null argument: mbsf");
        }
        if (this.mbeanServer != null) {
            mBeanServerForwarder.setMBeanServer(this.mbeanServer);
        }
        this.mbeanServer = mBeanServerForwarder;
    }

    @Override // javax.management.remote.JMXConnectorServerMBean
    public String[] getConnectionIds() {
        String[] strArr;
        synchronized (this.connectionIds) {
            strArr = (String[]) this.connectionIds.toArray(new String[this.connectionIds.size()]);
        }
        return strArr;
    }

    @Override // javax.management.remote.JMXConnectorServerMBean
    public JMXConnector toJMXConnector(Map<String, ?> map) throws IOException {
        if (isActive()) {
            return JMXConnectorFactory.newJMXConnector(getAddress(), map);
        }
        throw new IllegalStateException("Connector is not active");
    }

    @Override // javax.management.NotificationBroadcasterSupport, javax.management.NotificationBroadcaster
    public MBeanNotificationInfo[] getNotificationInfo() {
        return new MBeanNotificationInfo[]{new MBeanNotificationInfo(new String[]{JMXConnectionNotification.OPENED, JMXConnectionNotification.CLOSED, JMXConnectionNotification.FAILED}, JMXConnectionNotification.class.getName(), "A client connection has been opened or closed")};
    }

    protected void connectionOpened(String str, String str2, Object obj) {
        if (str == null) {
            throw new NullPointerException("Illegal null argument");
        }
        synchronized (this.connectionIds) {
            this.connectionIds.add(str);
        }
        sendNotification(JMXConnectionNotification.OPENED, str, str2, obj);
    }

    protected void connectionClosed(String str, String str2, Object obj) {
        if (str == null) {
            throw new NullPointerException("Illegal null argument");
        }
        synchronized (this.connectionIds) {
            this.connectionIds.remove(str);
        }
        sendNotification(JMXConnectionNotification.CLOSED, str, str2, obj);
    }

    protected void connectionFailed(String str, String str2, Object obj) {
        if (str == null) {
            throw new NullPointerException("Illegal null argument");
        }
        synchronized (this.connectionIds) {
            this.connectionIds.remove(str);
        }
        sendNotification(JMXConnectionNotification.FAILED, str, str2, obj);
    }

    private void sendNotification(String str, String str2, String str3, Object obj) {
        sendNotification(new JMXConnectionNotification(str, getNotificationSource(), str2, nextSequenceNumber(), str3, obj));
    }

    private synchronized Object getNotificationSource() {
        if (this.myName != null) {
            return this.myName;
        }
        return this;
    }

    private static long nextSequenceNumber() {
        long j2;
        synchronized (sequenceNumberLock) {
            j2 = sequenceNumber;
            sequenceNumber = j2 + 1;
        }
        return j2;
    }

    @Override // javax.management.MBeanRegistration
    public synchronized ObjectName preRegister(MBeanServer mBeanServer, ObjectName objectName) {
        if (mBeanServer == null || objectName == null) {
            throw new NullPointerException("Null MBeanServer or ObjectName");
        }
        if (this.mbeanServer == null) {
            this.mbeanServer = mBeanServer;
            this.myName = objectName;
        }
        return objectName;
    }

    @Override // javax.management.MBeanRegistration
    public void postRegister(Boolean bool) {
    }

    @Override // javax.management.MBeanRegistration
    public synchronized void preDeregister() throws Exception {
        if (this.myName != null && isActive()) {
            stop();
            this.myName = null;
        }
    }

    @Override // javax.management.MBeanRegistration
    public void postDeregister() {
        this.myName = null;
    }
}
