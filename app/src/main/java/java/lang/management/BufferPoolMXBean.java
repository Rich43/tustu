package java.lang.management;

/* loaded from: rt.jar:java/lang/management/BufferPoolMXBean.class */
public interface BufferPoolMXBean extends PlatformManagedObject {
    String getName();

    long getCount();

    long getTotalCapacity();

    long getMemoryUsed();
}
