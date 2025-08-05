package java.lang.management;

import javax.management.openmbean.CompositeData;
import sun.management.MemoryNotifInfoCompositeData;

/* loaded from: rt.jar:java/lang/management/MemoryNotificationInfo.class */
public class MemoryNotificationInfo {
    private final String poolName;
    private final MemoryUsage usage;
    private final long count;
    public static final String MEMORY_THRESHOLD_EXCEEDED = "java.management.memory.threshold.exceeded";
    public static final String MEMORY_COLLECTION_THRESHOLD_EXCEEDED = "java.management.memory.collection.threshold.exceeded";

    public MemoryNotificationInfo(String str, MemoryUsage memoryUsage, long j2) {
        if (str == null) {
            throw new NullPointerException("Null poolName");
        }
        if (memoryUsage == null) {
            throw new NullPointerException("Null usage");
        }
        this.poolName = str;
        this.usage = memoryUsage;
        this.count = j2;
    }

    MemoryNotificationInfo(CompositeData compositeData) {
        MemoryNotifInfoCompositeData.validateCompositeData(compositeData);
        this.poolName = MemoryNotifInfoCompositeData.getPoolName(compositeData);
        this.usage = MemoryNotifInfoCompositeData.getUsage(compositeData);
        this.count = MemoryNotifInfoCompositeData.getCount(compositeData);
    }

    public String getPoolName() {
        return this.poolName;
    }

    public MemoryUsage getUsage() {
        return this.usage;
    }

    public long getCount() {
        return this.count;
    }

    public static MemoryNotificationInfo from(CompositeData compositeData) {
        if (compositeData == null) {
            return null;
        }
        if (compositeData instanceof MemoryNotifInfoCompositeData) {
            return ((MemoryNotifInfoCompositeData) compositeData).getMemoryNotifInfo();
        }
        return new MemoryNotificationInfo(compositeData);
    }
}
