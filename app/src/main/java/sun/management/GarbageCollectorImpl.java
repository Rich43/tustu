package sun.management;

import com.sun.management.GarbageCollectionNotificationInfo;
import com.sun.management.GarbageCollectorMXBean;
import com.sun.management.GcInfo;
import java.lang.management.MemoryPoolMXBean;
import java.util.Iterator;
import java.util.List;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;

/* loaded from: rt.jar:sun/management/GarbageCollectorImpl.class */
class GarbageCollectorImpl extends MemoryManagerImpl implements GarbageCollectorMXBean {
    private String[] poolNames;
    private GcInfoBuilder gcInfoBuilder;
    private static final String notifName = "javax.management.Notification";
    private static final String[] gcNotifTypes = {GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION};
    private static long seqNumber = 0;

    @Override // java.lang.management.GarbageCollectorMXBean
    public native long getCollectionCount();

    @Override // java.lang.management.GarbageCollectorMXBean
    public native long getCollectionTime();

    native void setNotificationEnabled(GarbageCollectorMXBean garbageCollectorMXBean, boolean z2);

    GarbageCollectorImpl(String str) {
        super(str);
        this.poolNames = null;
    }

    synchronized String[] getAllPoolNames() {
        if (this.poolNames == null) {
            List<MemoryPoolMXBean> memoryPoolMXBeans = java.lang.management.ManagementFactory.getMemoryPoolMXBeans();
            this.poolNames = new String[memoryPoolMXBeans.size()];
            int i2 = 0;
            Iterator<MemoryPoolMXBean> it = memoryPoolMXBeans.iterator();
            while (it.hasNext()) {
                int i3 = i2;
                i2++;
                this.poolNames[i3] = it.next().getName();
            }
        }
        return this.poolNames;
    }

    private synchronized GcInfoBuilder getGcInfoBuilder() {
        if (this.gcInfoBuilder == null) {
            this.gcInfoBuilder = new GcInfoBuilder(this, getAllPoolNames());
        }
        return this.gcInfoBuilder;
    }

    @Override // com.sun.management.GarbageCollectorMXBean
    public GcInfo getLastGcInfo() {
        return getGcInfoBuilder().getLastGcInfo();
    }

    @Override // sun.management.MemoryManagerImpl, sun.management.NotificationEmitterSupport, javax.management.NotificationBroadcaster
    public MBeanNotificationInfo[] getNotificationInfo() {
        return new MBeanNotificationInfo[]{new MBeanNotificationInfo(gcNotifTypes, notifName, "GC Notification")};
    }

    private static long getNextSeqNumber() {
        long j2 = seqNumber + 1;
        seqNumber = j2;
        return j2;
    }

    void createGCNotification(long j2, String str, String str2, String str3, GcInfo gcInfo) {
        if (!hasListeners()) {
            return;
        }
        Notification notification = new Notification(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION, getObjectName(), getNextSeqNumber(), j2, str);
        notification.setUserData(GarbageCollectionNotifInfoCompositeData.toCompositeData(new GarbageCollectionNotificationInfo(str, str2, str3, gcInfo)));
        sendNotification(notification);
    }

    @Override // sun.management.NotificationEmitterSupport, javax.management.NotificationBroadcaster
    public synchronized void addNotificationListener(NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) {
        boolean zHasListeners = hasListeners();
        super.addNotificationListener(notificationListener, notificationFilter, obj);
        boolean zHasListeners2 = hasListeners();
        if (!zHasListeners && zHasListeners2) {
            setNotificationEnabled(this, true);
        }
    }

    @Override // sun.management.NotificationEmitterSupport, javax.management.NotificationBroadcaster
    public synchronized void removeNotificationListener(NotificationListener notificationListener) throws ListenerNotFoundException {
        boolean zHasListeners = hasListeners();
        super.removeNotificationListener(notificationListener);
        boolean zHasListeners2 = hasListeners();
        if (zHasListeners && !zHasListeners2) {
            setNotificationEnabled(this, false);
        }
    }

    @Override // sun.management.NotificationEmitterSupport, javax.management.NotificationEmitter
    public synchronized void removeNotificationListener(NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws ListenerNotFoundException {
        boolean zHasListeners = hasListeners();
        super.removeNotificationListener(notificationListener, notificationFilter, obj);
        boolean zHasListeners2 = hasListeners();
        if (zHasListeners && !zHasListeners2) {
            setNotificationEnabled(this, false);
        }
    }

    @Override // sun.management.MemoryManagerImpl, java.lang.management.PlatformManagedObject
    public ObjectName getObjectName() {
        return Util.newObjectName(java.lang.management.ManagementFactory.GARBAGE_COLLECTOR_MXBEAN_DOMAIN_TYPE, getName());
    }
}
