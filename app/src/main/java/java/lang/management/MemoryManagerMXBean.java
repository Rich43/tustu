package java.lang.management;

/* loaded from: rt.jar:java/lang/management/MemoryManagerMXBean.class */
public interface MemoryManagerMXBean extends PlatformManagedObject {
    String getName();

    boolean isValid();

    String[] getMemoryPoolNames();
}
