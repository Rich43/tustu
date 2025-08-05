package javax.management;

import com.sun.jmx.defaults.JmxProperties;
import com.sun.jmx.defaults.ServiceName;
import com.sun.jmx.mbeanserver.Util;
import java.net.InetAddress;
import java.net.UnknownHostException;

/* loaded from: rt.jar:javax/management/MBeanServerDelegate.class */
public class MBeanServerDelegate implements MBeanServerDelegateMBean, NotificationEmitter {
    private String mbeanServerId;
    private static long oldStamp = 0;
    private static final MBeanNotificationInfo[] notifsInfo;
    public static final ObjectName DELEGATE_NAME;
    private long sequenceNumber = 1;
    private final long stamp = getStamp();
    private final NotificationBroadcasterSupport broadcaster = new NotificationBroadcasterSupport();

    static {
        String[] strArr = {MBeanServerNotification.UNREGISTRATION_NOTIFICATION, MBeanServerNotification.REGISTRATION_NOTIFICATION};
        notifsInfo = new MBeanNotificationInfo[1];
        notifsInfo[0] = new MBeanNotificationInfo(strArr, "javax.management.MBeanServerNotification", "Notifications sent by the MBeanServerDelegate MBean");
        DELEGATE_NAME = Util.newObjectName(ServiceName.DELEGATE);
    }

    @Override // javax.management.MBeanServerDelegateMBean
    public synchronized String getMBeanServerId() {
        String hostName;
        if (this.mbeanServerId == null) {
            try {
                hostName = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e2) {
                JmxProperties.MISC_LOGGER.finest("Can't get local host name, using \"localhost\" instead. Cause is: " + ((Object) e2));
                hostName = "localhost";
            }
            this.mbeanServerId = hostName + "_" + this.stamp;
        }
        return this.mbeanServerId;
    }

    @Override // javax.management.MBeanServerDelegateMBean
    public String getSpecificationName() {
        return ServiceName.JMX_SPEC_NAME;
    }

    @Override // javax.management.MBeanServerDelegateMBean
    public String getSpecificationVersion() {
        return ServiceName.JMX_SPEC_VERSION;
    }

    @Override // javax.management.MBeanServerDelegateMBean
    public String getSpecificationVendor() {
        return "Oracle Corporation";
    }

    @Override // javax.management.MBeanServerDelegateMBean
    public String getImplementationName() {
        return ServiceName.JMX_IMPL_NAME;
    }

    @Override // javax.management.MBeanServerDelegateMBean
    public String getImplementationVersion() {
        try {
            return System.getProperty("java.runtime.version");
        } catch (SecurityException e2) {
            return "";
        }
    }

    @Override // javax.management.MBeanServerDelegateMBean
    public String getImplementationVendor() {
        return "Oracle Corporation";
    }

    @Override // javax.management.NotificationBroadcaster
    public MBeanNotificationInfo[] getNotificationInfo() {
        int length = notifsInfo.length;
        MBeanNotificationInfo[] mBeanNotificationInfoArr = new MBeanNotificationInfo[length];
        System.arraycopy(notifsInfo, 0, mBeanNotificationInfoArr, 0, length);
        return mBeanNotificationInfoArr;
    }

    @Override // javax.management.NotificationBroadcaster
    public synchronized void addNotificationListener(NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws IllegalArgumentException {
        this.broadcaster.addNotificationListener(notificationListener, notificationFilter, obj);
    }

    @Override // javax.management.NotificationEmitter
    public synchronized void removeNotificationListener(NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws ListenerNotFoundException {
        this.broadcaster.removeNotificationListener(notificationListener, notificationFilter, obj);
    }

    @Override // javax.management.NotificationBroadcaster
    public synchronized void removeNotificationListener(NotificationListener notificationListener) throws ListenerNotFoundException {
        this.broadcaster.removeNotificationListener(notificationListener);
    }

    public void sendNotification(Notification notification) {
        if (notification.getSequenceNumber() < 1) {
            synchronized (this) {
                long j2 = this.sequenceNumber;
                this.sequenceNumber = j2 + 1;
                notification.setSequenceNumber(j2);
            }
        }
        this.broadcaster.sendNotification(notification);
    }

    private static synchronized long getStamp() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (oldStamp >= jCurrentTimeMillis) {
            jCurrentTimeMillis = oldStamp + 1;
        }
        oldStamp = jCurrentTimeMillis;
        return jCurrentTimeMillis;
    }
}
