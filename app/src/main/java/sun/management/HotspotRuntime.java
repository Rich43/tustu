package sun.management;

import java.util.List;
import sun.management.counter.Counter;

/* loaded from: rt.jar:sun/management/HotspotRuntime.class */
class HotspotRuntime implements HotspotRuntimeMBean {
    private VMManagement jvm;
    private static final String JAVA_RT = "java.rt.";
    private static final String COM_SUN_RT = "com.sun.rt.";
    private static final String SUN_RT = "sun.rt.";
    private static final String JAVA_PROPERTY = "java.property.";
    private static final String COM_SUN_PROPERTY = "com.sun.property.";
    private static final String SUN_PROPERTY = "sun.property.";
    private static final String RT_COUNTER_NAME_PATTERN = "java.rt.|com.sun.rt.|sun.rt.|java.property.|com.sun.property.|sun.property.";

    HotspotRuntime(VMManagement vMManagement) {
        this.jvm = vMManagement;
    }

    @Override // sun.management.HotspotRuntimeMBean
    public long getSafepointCount() {
        return this.jvm.getSafepointCount();
    }

    @Override // sun.management.HotspotRuntimeMBean
    public long getTotalSafepointTime() {
        return this.jvm.getTotalSafepointTime();
    }

    @Override // sun.management.HotspotRuntimeMBean
    public long getSafepointSyncTime() {
        return this.jvm.getSafepointSyncTime();
    }

    @Override // sun.management.HotspotRuntimeMBean
    public List<Counter> getInternalRuntimeCounters() {
        return this.jvm.getInternalCounters(RT_COUNTER_NAME_PATTERN);
    }
}
