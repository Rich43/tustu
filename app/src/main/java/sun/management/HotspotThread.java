package sun.management;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sun.management.counter.Counter;

/* loaded from: rt.jar:sun/management/HotspotThread.class */
class HotspotThread implements HotspotThreadMBean {
    private VMManagement jvm;
    private static final String JAVA_THREADS = "java.threads.";
    private static final String COM_SUN_THREADS = "com.sun.threads.";
    private static final String SUN_THREADS = "sun.threads.";
    private static final String THREADS_COUNTER_NAME_PATTERN = "java.threads.|com.sun.threads.|sun.threads.";

    @Override // sun.management.HotspotThreadMBean
    public native int getInternalThreadCount();

    public native int getInternalThreadTimes0(String[] strArr, long[] jArr);

    HotspotThread(VMManagement vMManagement) {
        this.jvm = vMManagement;
    }

    @Override // sun.management.HotspotThreadMBean
    public Map<String, Long> getInternalThreadCpuTimes() {
        int internalThreadCount = getInternalThreadCount();
        if (internalThreadCount == 0) {
            return Collections.emptyMap();
        }
        String[] strArr = new String[internalThreadCount];
        long[] jArr = new long[internalThreadCount];
        int internalThreadTimes0 = getInternalThreadTimes0(strArr, jArr);
        HashMap map = new HashMap(internalThreadTimes0);
        for (int i2 = 0; i2 < internalThreadTimes0; i2++) {
            map.put(strArr[i2], new Long(jArr[i2]));
        }
        return map;
    }

    @Override // sun.management.HotspotThreadMBean
    public List<Counter> getInternalThreadingCounters() {
        return this.jvm.getInternalCounters(THREADS_COUNTER_NAME_PATTERN);
    }
}
