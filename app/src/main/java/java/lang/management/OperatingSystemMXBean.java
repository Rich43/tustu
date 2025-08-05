package java.lang.management;

/* loaded from: rt.jar:java/lang/management/OperatingSystemMXBean.class */
public interface OperatingSystemMXBean extends PlatformManagedObject {
    String getName();

    String getArch();

    String getVersion();

    int getAvailableProcessors();

    double getSystemLoadAverage();
}
