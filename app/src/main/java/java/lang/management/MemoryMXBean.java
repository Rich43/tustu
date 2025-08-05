package java.lang.management;

/* loaded from: rt.jar:java/lang/management/MemoryMXBean.class */
public interface MemoryMXBean extends PlatformManagedObject {
    int getObjectPendingFinalizationCount();

    MemoryUsage getHeapMemoryUsage();

    MemoryUsage getNonHeapMemoryUsage();

    boolean isVerbose();

    void setVerbose(boolean z2);

    void gc();
}
