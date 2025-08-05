package sun.management;

import java.lang.management.MemoryManagerMXBean;
import java.lang.management.MemoryNotificationInfo;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;
import javax.management.ObjectName;

/* loaded from: rt.jar:sun/management/MemoryPoolImpl.class */
class MemoryPoolImpl implements MemoryPoolMXBean {
    private final String name;
    private final boolean isHeap;
    private final boolean collectionThresholdSupported;
    private final boolean usageThresholdSupported;
    private long usageThreshold;
    private long collectionThreshold;
    private boolean usageSensorRegistered;
    private boolean gcSensorRegistered;
    private Sensor usageSensor;
    private Sensor gcSensor;
    private final boolean isValid = true;
    private MemoryManagerMXBean[] managers = null;

    private native MemoryUsage getUsage0();

    private native MemoryUsage getPeakUsage0();

    private native MemoryUsage getCollectionUsage0();

    private native void setUsageThreshold0(long j2, long j3);

    private native void setCollectionThreshold0(long j2, long j3);

    private native void resetPeakUsage0();

    private native MemoryManagerMXBean[] getMemoryManagers0();

    private native void setPoolUsageSensor(Sensor sensor);

    private native void setPoolCollectionSensor(Sensor sensor);

    MemoryPoolImpl(String str, boolean z2, long j2, long j3) {
        this.name = str;
        this.isHeap = z2;
        this.usageThreshold = j2;
        this.collectionThreshold = j3;
        this.usageThresholdSupported = j2 >= 0;
        this.collectionThresholdSupported = j3 >= 0;
        this.usageSensor = new PoolSensor(this, str + " usage sensor");
        this.gcSensor = new CollectionSensor(this, str + " collection sensor");
        this.usageSensorRegistered = false;
        this.gcSensorRegistered = false;
    }

    @Override // java.lang.management.MemoryPoolMXBean
    public String getName() {
        return this.name;
    }

    @Override // java.lang.management.MemoryPoolMXBean
    public boolean isValid() {
        return this.isValid;
    }

    @Override // java.lang.management.MemoryPoolMXBean
    public MemoryType getType() {
        if (this.isHeap) {
            return MemoryType.HEAP;
        }
        return MemoryType.NON_HEAP;
    }

    @Override // java.lang.management.MemoryPoolMXBean
    public MemoryUsage getUsage() {
        return getUsage0();
    }

    @Override // java.lang.management.MemoryPoolMXBean
    public synchronized MemoryUsage getPeakUsage() {
        return getPeakUsage0();
    }

    @Override // java.lang.management.MemoryPoolMXBean
    public synchronized long getUsageThreshold() {
        if (!isUsageThresholdSupported()) {
            throw new UnsupportedOperationException("Usage threshold is not supported");
        }
        return this.usageThreshold;
    }

    @Override // java.lang.management.MemoryPoolMXBean
    public void setUsageThreshold(long j2) throws SecurityException {
        if (!isUsageThresholdSupported()) {
            throw new UnsupportedOperationException("Usage threshold is not supported");
        }
        Util.checkControlAccess();
        MemoryUsage usage0 = getUsage0();
        if (j2 < 0) {
            throw new IllegalArgumentException("Invalid threshold: " + j2);
        }
        if (usage0.getMax() != -1 && j2 > usage0.getMax()) {
            throw new IllegalArgumentException("Invalid threshold: " + j2 + " must be <= maxSize. Committed = " + usage0.getCommitted() + " Max = " + usage0.getMax());
        }
        synchronized (this) {
            if (!this.usageSensorRegistered) {
                this.usageSensorRegistered = true;
                setPoolUsageSensor(this.usageSensor);
            }
            setUsageThreshold0(this.usageThreshold, j2);
            this.usageThreshold = j2;
        }
    }

    private synchronized MemoryManagerMXBean[] getMemoryManagers() {
        if (this.managers == null) {
            this.managers = getMemoryManagers0();
        }
        return this.managers;
    }

    @Override // java.lang.management.MemoryPoolMXBean
    public String[] getMemoryManagerNames() {
        MemoryManagerMXBean[] memoryManagers = getMemoryManagers();
        String[] strArr = new String[memoryManagers.length];
        for (int i2 = 0; i2 < memoryManagers.length; i2++) {
            strArr[i2] = memoryManagers[i2].getName();
        }
        return strArr;
    }

    @Override // java.lang.management.MemoryPoolMXBean
    public void resetPeakUsage() throws SecurityException {
        Util.checkControlAccess();
        synchronized (this) {
            resetPeakUsage0();
        }
    }

    @Override // java.lang.management.MemoryPoolMXBean
    public boolean isUsageThresholdExceeded() {
        if (!isUsageThresholdSupported()) {
            throw new UnsupportedOperationException("Usage threshold is not supported");
        }
        if (this.usageThreshold == 0) {
            return false;
        }
        return getUsage0().getUsed() >= this.usageThreshold || this.usageSensor.isOn();
    }

    @Override // java.lang.management.MemoryPoolMXBean
    public long getUsageThresholdCount() {
        if (!isUsageThresholdSupported()) {
            throw new UnsupportedOperationException("Usage threshold is not supported");
        }
        return this.usageSensor.getCount();
    }

    @Override // java.lang.management.MemoryPoolMXBean
    public boolean isUsageThresholdSupported() {
        return this.usageThresholdSupported;
    }

    @Override // java.lang.management.MemoryPoolMXBean
    public synchronized long getCollectionUsageThreshold() {
        if (!isCollectionUsageThresholdSupported()) {
            throw new UnsupportedOperationException("CollectionUsage threshold is not supported");
        }
        return this.collectionThreshold;
    }

    @Override // java.lang.management.MemoryPoolMXBean
    public void setCollectionUsageThreshold(long j2) throws SecurityException {
        if (!isCollectionUsageThresholdSupported()) {
            throw new UnsupportedOperationException("CollectionUsage threshold is not supported");
        }
        Util.checkControlAccess();
        MemoryUsage usage0 = getUsage0();
        if (j2 < 0) {
            throw new IllegalArgumentException("Invalid threshold: " + j2);
        }
        if (usage0.getMax() != -1 && j2 > usage0.getMax()) {
            throw new IllegalArgumentException("Invalid threshold: " + j2 + " > max (" + usage0.getMax() + ").");
        }
        synchronized (this) {
            if (!this.gcSensorRegistered) {
                this.gcSensorRegistered = true;
                setPoolCollectionSensor(this.gcSensor);
            }
            setCollectionThreshold0(this.collectionThreshold, j2);
            this.collectionThreshold = j2;
        }
    }

    @Override // java.lang.management.MemoryPoolMXBean
    public boolean isCollectionUsageThresholdExceeded() {
        if (!isCollectionUsageThresholdSupported()) {
            throw new UnsupportedOperationException("CollectionUsage threshold is not supported");
        }
        if (this.collectionThreshold == 0) {
            return false;
        }
        MemoryUsage collectionUsage0 = getCollectionUsage0();
        return this.gcSensor.isOn() || (collectionUsage0 != null && collectionUsage0.getUsed() >= this.collectionThreshold);
    }

    @Override // java.lang.management.MemoryPoolMXBean
    public long getCollectionUsageThresholdCount() {
        if (!isCollectionUsageThresholdSupported()) {
            throw new UnsupportedOperationException("CollectionUsage threshold is not supported");
        }
        return this.gcSensor.getCount();
    }

    @Override // java.lang.management.MemoryPoolMXBean
    public MemoryUsage getCollectionUsage() {
        return getCollectionUsage0();
    }

    @Override // java.lang.management.MemoryPoolMXBean
    public boolean isCollectionUsageThresholdSupported() {
        return this.collectionThresholdSupported;
    }

    /* loaded from: rt.jar:sun/management/MemoryPoolImpl$PoolSensor.class */
    class PoolSensor extends Sensor {
        MemoryPoolImpl pool;

        PoolSensor(MemoryPoolImpl memoryPoolImpl, String str) {
            super(str);
            this.pool = memoryPoolImpl;
        }

        @Override // sun.management.Sensor
        void triggerAction(MemoryUsage memoryUsage) {
            MemoryImpl.createNotification(MemoryNotificationInfo.MEMORY_THRESHOLD_EXCEEDED, this.pool.getName(), memoryUsage, getCount());
        }

        @Override // sun.management.Sensor
        void triggerAction() {
        }

        @Override // sun.management.Sensor
        void clearAction() {
        }
    }

    /* loaded from: rt.jar:sun/management/MemoryPoolImpl$CollectionSensor.class */
    class CollectionSensor extends Sensor {
        MemoryPoolImpl pool;

        CollectionSensor(MemoryPoolImpl memoryPoolImpl, String str) {
            super(str);
            this.pool = memoryPoolImpl;
        }

        @Override // sun.management.Sensor
        void triggerAction(MemoryUsage memoryUsage) {
            MemoryImpl.createNotification(MemoryNotificationInfo.MEMORY_COLLECTION_THRESHOLD_EXCEEDED, this.pool.getName(), memoryUsage, MemoryPoolImpl.this.gcSensor.getCount());
        }

        @Override // sun.management.Sensor
        void triggerAction() {
        }

        @Override // sun.management.Sensor
        void clearAction() {
        }
    }

    @Override // java.lang.management.PlatformManagedObject
    public ObjectName getObjectName() {
        return Util.newObjectName(java.lang.management.ManagementFactory.MEMORY_POOL_MXBEAN_DOMAIN_TYPE, getName());
    }
}
