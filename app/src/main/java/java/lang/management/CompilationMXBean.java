package java.lang.management;

/* loaded from: rt.jar:java/lang/management/CompilationMXBean.class */
public interface CompilationMXBean extends PlatformManagedObject {
    String getName();

    boolean isCompilationTimeMonitoringSupported();

    long getTotalCompilationTime();
}
