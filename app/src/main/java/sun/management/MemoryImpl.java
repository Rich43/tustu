package sun.management;

import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryManagerMXBean;
import java.lang.management.MemoryNotificationInfo;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.ObjectName;
import sun.misc.VM;

/* loaded from: rt.jar:sun/management/MemoryImpl.class */
class MemoryImpl extends NotificationEmitterSupport implements MemoryMXBean {
    private final VMManagement jvm;
    private static final String notifName = "javax.management.Notification";
    private static MemoryPoolMXBean[] pools = null;
    private static MemoryManagerMXBean[] mgrs = null;
    private static final String[] notifTypes = {MemoryNotificationInfo.MEMORY_THRESHOLD_EXCEEDED, MemoryNotificationInfo.MEMORY_COLLECTION_THRESHOLD_EXCEEDED};
    private static final String[] notifMsgs = {"Memory usage exceeds usage threshold", "Memory usage exceeds collection usage threshold"};
    private static long seqNumber = 0;

    private static native MemoryPoolMXBean[] getMemoryPools0();

    private static native MemoryManagerMXBean[] getMemoryManagers0();

    private native MemoryUsage getMemoryUsage0(boolean z2);

    private native void setVerboseGC(boolean z2);

    MemoryImpl(VMManagement vMManagement) {
        this.jvm = vMManagement;
    }

    @Override // java.lang.management.MemoryMXBean
    public int getObjectPendingFinalizationCount() {
        return VM.getFinalRefCount();
    }

    @Override // java.lang.management.MemoryMXBean
    public void gc() {
        Runtime.getRuntime().gc();
    }

    @Override // java.lang.management.MemoryMXBean
    public MemoryUsage getHeapMemoryUsage() {
        return getMemoryUsage0(true);
    }

    @Override // java.lang.management.MemoryMXBean
    public MemoryUsage getNonHeapMemoryUsage() {
        return getMemoryUsage0(false);
    }

    @Override // java.lang.management.MemoryMXBean
    public boolean isVerbose() {
        return this.jvm.getVerboseGC();
    }

    @Override // java.lang.management.MemoryMXBean
    public void setVerbose(boolean z2) throws SecurityException {
        Util.checkControlAccess();
        setVerboseGC(z2);
    }

    static synchronized MemoryPoolMXBean[] getMemoryPools() {
        if (pools == null) {
            pools = getMemoryPools0();
        }
        return pools;
    }

    static synchronized MemoryManagerMXBean[] getMemoryManagers() {
        if (mgrs == null) {
            mgrs = getMemoryManagers0();
        }
        return mgrs;
    }

    @Override // sun.management.NotificationEmitterSupport, javax.management.NotificationBroadcaster
    public MBeanNotificationInfo[] getNotificationInfo() {
        return new MBeanNotificationInfo[]{new MBeanNotificationInfo(notifTypes, notifName, "Memory Notification")};
    }

    private static String getNotifMsg(String str) {
        for (int i2 = 0; i2 < notifTypes.length; i2++) {
            if (str == notifTypes[i2]) {
                return notifMsgs[i2];
            }
        }
        return "Unknown message";
    }

    private static long getNextSeqNumber() {
        long j2 = seqNumber + 1;
        seqNumber = j2;
        return j2;
    }

    static void createNotification(String str, String str2, MemoryUsage memoryUsage, long j2) {
        MemoryImpl memoryImpl = (MemoryImpl) java.lang.management.ManagementFactory.getMemoryMXBean();
        if (!memoryImpl.hasListeners()) {
            return;
        }
        Notification notification = new Notification(str, memoryImpl.getObjectName(), getNextSeqNumber(), System.currentTimeMillis(), getNotifMsg(str));
        notification.setUserData(MemoryNotifInfoCompositeData.toCompositeData(new MemoryNotificationInfo(str2, memoryUsage, j2)));
        memoryImpl.sendNotification(notification);
    }

    @Override // java.lang.management.PlatformManagedObject
    public ObjectName getObjectName() {
        return Util.newObjectName(java.lang.management.ManagementFactory.MEMORY_MXBEAN_NAME);
    }
}
