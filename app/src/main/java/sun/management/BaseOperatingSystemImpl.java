package sun.management;

import java.lang.management.OperatingSystemMXBean;
import javax.management.ObjectName;
import sun.misc.Unsafe;

/* loaded from: rt.jar:sun/management/BaseOperatingSystemImpl.class */
public class BaseOperatingSystemImpl implements OperatingSystemMXBean {
    private final VMManagement jvm;
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private double[] loadavg = new double[1];

    protected BaseOperatingSystemImpl(VMManagement vMManagement) {
        this.jvm = vMManagement;
    }

    @Override // java.lang.management.OperatingSystemMXBean
    public String getName() {
        return this.jvm.getOsName();
    }

    @Override // java.lang.management.OperatingSystemMXBean
    public String getArch() {
        return this.jvm.getOsArch();
    }

    @Override // java.lang.management.OperatingSystemMXBean
    public String getVersion() {
        return this.jvm.getOsVersion();
    }

    @Override // java.lang.management.OperatingSystemMXBean
    public int getAvailableProcessors() {
        return this.jvm.getAvailableProcessors();
    }

    @Override // java.lang.management.OperatingSystemMXBean
    public double getSystemLoadAverage() {
        if (unsafe.getLoadAverage(this.loadavg, 1) == 1) {
            return this.loadavg[0];
        }
        return -1.0d;
    }

    @Override // java.lang.management.PlatformManagedObject
    public ObjectName getObjectName() {
        return Util.newObjectName(java.lang.management.ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME);
    }
}
