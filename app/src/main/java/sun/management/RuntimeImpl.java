package sun.management;

import java.lang.management.RuntimeMXBean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.management.ObjectName;

/* loaded from: rt.jar:sun/management/RuntimeImpl.class */
class RuntimeImpl implements RuntimeMXBean {
    private final VMManagement jvm;
    private final long vmStartupTime;

    RuntimeImpl(VMManagement vMManagement) {
        this.jvm = vMManagement;
        this.vmStartupTime = this.jvm.getStartupTime();
    }

    @Override // java.lang.management.RuntimeMXBean
    public String getName() {
        return this.jvm.getVmId();
    }

    @Override // java.lang.management.RuntimeMXBean
    public String getManagementSpecVersion() {
        return this.jvm.getManagementVersion();
    }

    @Override // java.lang.management.RuntimeMXBean
    public String getVmName() {
        return this.jvm.getVmName();
    }

    @Override // java.lang.management.RuntimeMXBean
    public String getVmVendor() {
        return this.jvm.getVmVendor();
    }

    @Override // java.lang.management.RuntimeMXBean
    public String getVmVersion() {
        return this.jvm.getVmVersion();
    }

    @Override // java.lang.management.RuntimeMXBean
    public String getSpecName() {
        return this.jvm.getVmSpecName();
    }

    @Override // java.lang.management.RuntimeMXBean
    public String getSpecVendor() {
        return this.jvm.getVmSpecVendor();
    }

    @Override // java.lang.management.RuntimeMXBean
    public String getSpecVersion() {
        return this.jvm.getVmSpecVersion();
    }

    @Override // java.lang.management.RuntimeMXBean
    public String getClassPath() {
        return this.jvm.getClassPath();
    }

    @Override // java.lang.management.RuntimeMXBean
    public String getLibraryPath() {
        return this.jvm.getLibraryPath();
    }

    @Override // java.lang.management.RuntimeMXBean
    public String getBootClassPath() throws SecurityException {
        if (!isBootClassPathSupported()) {
            throw new UnsupportedOperationException("Boot class path mechanism is not supported");
        }
        Util.checkMonitorAccess();
        return this.jvm.getBootClassPath();
    }

    @Override // java.lang.management.RuntimeMXBean
    public List<String> getInputArguments() throws SecurityException {
        Util.checkMonitorAccess();
        return this.jvm.getVmArguments();
    }

    @Override // java.lang.management.RuntimeMXBean
    public long getUptime() {
        return this.jvm.getUptime();
    }

    @Override // java.lang.management.RuntimeMXBean
    public long getStartTime() {
        return this.vmStartupTime;
    }

    @Override // java.lang.management.RuntimeMXBean
    public boolean isBootClassPathSupported() {
        return this.jvm.isBootClassPathSupported();
    }

    @Override // java.lang.management.RuntimeMXBean
    public Map<String, String> getSystemProperties() {
        Properties properties = System.getProperties();
        HashMap map = new HashMap();
        for (String str : properties.stringPropertyNames()) {
            map.put(str, properties.getProperty(str));
        }
        return map;
    }

    @Override // java.lang.management.PlatformManagedObject
    public ObjectName getObjectName() {
        return Util.newObjectName(java.lang.management.ManagementFactory.RUNTIME_MXBEAN_NAME);
    }
}
