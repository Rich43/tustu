package java.lang.management;

/* loaded from: rt.jar:java/lang/management/GarbageCollectorMXBean.class */
public interface GarbageCollectorMXBean extends MemoryManagerMXBean {
    long getCollectionCount();

    long getCollectionTime();
}
