package sun.management;

import java.util.List;
import sun.management.counter.Counter;

/* loaded from: rt.jar:sun/management/HotspotMemory.class */
class HotspotMemory implements HotspotMemoryMBean {
    private VMManagement jvm;
    private static final String JAVA_GC = "java.gc.";
    private static final String COM_SUN_GC = "com.sun.gc.";
    private static final String SUN_GC = "sun.gc.";
    private static final String GC_COUNTER_NAME_PATTERN = "java.gc.|com.sun.gc.|sun.gc.";

    HotspotMemory(VMManagement vMManagement) {
        this.jvm = vMManagement;
    }

    @Override // sun.management.HotspotMemoryMBean
    public List<Counter> getInternalMemoryCounters() {
        return this.jvm.getInternalCounters(GC_COUNTER_NAME_PATTERN);
    }
}
