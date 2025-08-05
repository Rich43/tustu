package sun.management;

import java.util.List;
import sun.management.counter.Counter;

/* loaded from: rt.jar:sun/management/HotspotClassLoading.class */
class HotspotClassLoading implements HotspotClassLoadingMBean {
    private VMManagement jvm;
    private static final String JAVA_CLS = "java.cls.";
    private static final String COM_SUN_CLS = "com.sun.cls.";
    private static final String SUN_CLS = "sun.cls.";
    private static final String CLS_COUNTER_NAME_PATTERN = "java.cls.|com.sun.cls.|sun.cls.";

    HotspotClassLoading(VMManagement vMManagement) {
        this.jvm = vMManagement;
    }

    @Override // sun.management.HotspotClassLoadingMBean
    public long getLoadedClassSize() {
        return this.jvm.getLoadedClassSize();
    }

    @Override // sun.management.HotspotClassLoadingMBean
    public long getUnloadedClassSize() {
        return this.jvm.getUnloadedClassSize();
    }

    @Override // sun.management.HotspotClassLoadingMBean
    public long getClassLoadingTime() {
        return this.jvm.getClassLoadingTime();
    }

    @Override // sun.management.HotspotClassLoadingMBean
    public long getMethodDataSize() {
        return this.jvm.getMethodDataSize();
    }

    @Override // sun.management.HotspotClassLoadingMBean
    public long getInitializedClassCount() {
        return this.jvm.getInitializedClassCount();
    }

    @Override // sun.management.HotspotClassLoadingMBean
    public long getClassInitializationTime() {
        return this.jvm.getClassInitializationTime();
    }

    @Override // sun.management.HotspotClassLoadingMBean
    public long getClassVerificationTime() {
        return this.jvm.getClassVerificationTime();
    }

    @Override // sun.management.HotspotClassLoadingMBean
    public List<Counter> getInternalClassLoadingCounters() {
        return this.jvm.getInternalCounters(CLS_COUNTER_NAME_PATTERN);
    }
}
