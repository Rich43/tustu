package sun.management;

import com.sun.management.OperatingSystemMXBean;

/* loaded from: rt.jar:sun/management/OperatingSystemImpl.class */
class OperatingSystemImpl extends BaseOperatingSystemImpl implements OperatingSystemMXBean {
    private static Object psapiLock = new Object();

    private native long getCommittedVirtualMemorySize0();

    private native long getFreePhysicalMemorySize0();

    private native long getFreeSwapSpaceSize0();

    private native double getProcessCpuLoad0();

    private native long getProcessCpuTime0();

    private native double getSystemCpuLoad0();

    private native long getTotalPhysicalMemorySize0();

    private native long getTotalSwapSpaceSize0();

    private static native void initialize0();

    static {
        initialize0();
    }

    OperatingSystemImpl(VMManagement vMManagement) {
        super(vMManagement);
    }

    @Override // com.sun.management.OperatingSystemMXBean
    public long getCommittedVirtualMemorySize() {
        long committedVirtualMemorySize0;
        synchronized (psapiLock) {
            committedVirtualMemorySize0 = getCommittedVirtualMemorySize0();
        }
        return committedVirtualMemorySize0;
    }

    @Override // com.sun.management.OperatingSystemMXBean
    public long getTotalSwapSpaceSize() {
        return getTotalSwapSpaceSize0();
    }

    @Override // com.sun.management.OperatingSystemMXBean
    public long getFreeSwapSpaceSize() {
        return getFreeSwapSpaceSize0();
    }

    @Override // com.sun.management.OperatingSystemMXBean
    public long getProcessCpuTime() {
        return getProcessCpuTime0();
    }

    @Override // com.sun.management.OperatingSystemMXBean
    public long getFreePhysicalMemorySize() {
        return getFreePhysicalMemorySize0();
    }

    @Override // com.sun.management.OperatingSystemMXBean
    public long getTotalPhysicalMemorySize() {
        return getTotalPhysicalMemorySize0();
    }

    @Override // com.sun.management.OperatingSystemMXBean
    public double getSystemCpuLoad() {
        return getSystemCpuLoad0();
    }

    @Override // com.sun.management.OperatingSystemMXBean
    public double getProcessCpuLoad() {
        return getProcessCpuLoad0();
    }
}
