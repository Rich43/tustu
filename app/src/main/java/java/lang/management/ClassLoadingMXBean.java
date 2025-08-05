package java.lang.management;

/* loaded from: rt.jar:java/lang/management/ClassLoadingMXBean.class */
public interface ClassLoadingMXBean extends PlatformManagedObject {
    long getTotalLoadedClassCount();

    int getLoadedClassCount();

    long getUnloadedClassCount();

    boolean isVerbose();

    void setVerbose(boolean z2);
}
