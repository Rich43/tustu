package sun.management;

import java.lang.management.MemoryManagerMXBean;
import java.lang.management.MemoryPoolMXBean;
import javax.management.MBeanNotificationInfo;
import javax.management.ObjectName;

/* loaded from: rt.jar:sun/management/MemoryManagerImpl.class */
class MemoryManagerImpl extends NotificationEmitterSupport implements MemoryManagerMXBean {
    private final String name;
    private MBeanNotificationInfo[] notifInfo = null;
    private final boolean isValid = true;
    private MemoryPoolMXBean[] pools = null;

    private native MemoryPoolMXBean[] getMemoryPools0();

    MemoryManagerImpl(String str) {
        this.name = str;
    }

    @Override // java.lang.management.MemoryManagerMXBean
    public String getName() {
        return this.name;
    }

    @Override // java.lang.management.MemoryManagerMXBean
    public boolean isValid() {
        return this.isValid;
    }

    @Override // java.lang.management.MemoryManagerMXBean
    public String[] getMemoryPoolNames() {
        MemoryPoolMXBean[] memoryPools = getMemoryPools();
        String[] strArr = new String[memoryPools.length];
        for (int i2 = 0; i2 < memoryPools.length; i2++) {
            strArr[i2] = memoryPools[i2].getName();
        }
        return strArr;
    }

    synchronized MemoryPoolMXBean[] getMemoryPools() {
        if (this.pools == null) {
            this.pools = getMemoryPools0();
        }
        return this.pools;
    }

    @Override // sun.management.NotificationEmitterSupport, javax.management.NotificationBroadcaster
    public MBeanNotificationInfo[] getNotificationInfo() {
        synchronized (this) {
            if (this.notifInfo == null) {
                this.notifInfo = new MBeanNotificationInfo[0];
            }
        }
        return this.notifInfo;
    }

    public ObjectName getObjectName() {
        return Util.newObjectName(java.lang.management.ManagementFactory.MEMORY_MANAGER_MXBEAN_DOMAIN_TYPE, getName());
    }
}
