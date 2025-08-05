package java.lang.management;

/* loaded from: rt.jar:java/lang/management/MemoryPoolMXBean.class */
public interface MemoryPoolMXBean extends PlatformManagedObject {
    String getName();

    MemoryType getType();

    MemoryUsage getUsage();

    MemoryUsage getPeakUsage();

    void resetPeakUsage();

    boolean isValid();

    String[] getMemoryManagerNames();

    long getUsageThreshold();

    void setUsageThreshold(long j2);

    boolean isUsageThresholdExceeded();

    long getUsageThresholdCount();

    boolean isUsageThresholdSupported();

    long getCollectionUsageThreshold();

    void setCollectionUsageThreshold(long j2);

    boolean isCollectionUsageThresholdExceeded();

    long getCollectionUsageThresholdCount();

    MemoryUsage getCollectionUsage();

    boolean isCollectionUsageThresholdSupported();
}
