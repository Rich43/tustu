package sun.management;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.lingala.zip4j.util.InternalZipConstants;
import sun.management.counter.Counter;
import sun.management.counter.perf.PerfInstrumentation;
import sun.misc.Perf;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/management/VMManagementImpl.class */
class VMManagementImpl implements VMManagement {
    private static String version = getVersion0();
    private static boolean compTimeMonitoringSupport;
    private static boolean threadContentionMonitoringSupport;
    private static boolean currentThreadCpuTimeSupport;
    private static boolean otherThreadCpuTimeSupport;
    private static boolean bootClassPathSupport;
    private static boolean objectMonitorUsageSupport;
    private static boolean synchronizerUsageSupport;
    private static boolean threadAllocatedMemorySupport;
    private static boolean gcNotificationSupport;
    private static boolean remoteDiagnosticCommandsSupport;
    private List<String> vmArgs = null;
    private PerfInstrumentation perfInstr = null;
    private boolean noPerfData = false;

    private static native String getVersion0();

    private static native void initOptionalSupportFields();

    @Override // sun.management.VMManagement
    public native boolean isThreadContentionMonitoringEnabled();

    @Override // sun.management.VMManagement
    public native boolean isThreadCpuTimeEnabled();

    @Override // sun.management.VMManagement
    public native boolean isThreadAllocatedMemoryEnabled();

    @Override // sun.management.VMManagement
    public native long getTotalClassCount();

    @Override // sun.management.VMManagement
    public native long getUnloadedClassCount();

    @Override // sun.management.VMManagement
    public native boolean getVerboseClass();

    @Override // sun.management.VMManagement
    public native boolean getVerboseGC();

    private native int getProcessId();

    public native String[] getVmArguments0();

    @Override // sun.management.VMManagement
    public native long getStartupTime();

    private native long getUptime0();

    @Override // sun.management.VMManagement
    public native int getAvailableProcessors();

    @Override // sun.management.VMManagement
    public native long getTotalCompileTime();

    @Override // sun.management.VMManagement
    public native long getTotalThreadCount();

    @Override // sun.management.VMManagement
    public native int getLiveThreadCount();

    @Override // sun.management.VMManagement
    public native int getPeakThreadCount();

    @Override // sun.management.VMManagement
    public native int getDaemonThreadCount();

    @Override // sun.management.VMManagement
    public native long getSafepointCount();

    @Override // sun.management.VMManagement
    public native long getTotalSafepointTime();

    @Override // sun.management.VMManagement
    public native long getSafepointSyncTime();

    @Override // sun.management.VMManagement
    public native long getTotalApplicationNonStoppedTime();

    @Override // sun.management.VMManagement
    public native long getLoadedClassSize();

    @Override // sun.management.VMManagement
    public native long getUnloadedClassSize();

    @Override // sun.management.VMManagement
    public native long getClassLoadingTime();

    @Override // sun.management.VMManagement
    public native long getMethodDataSize();

    @Override // sun.management.VMManagement
    public native long getInitializedClassCount();

    @Override // sun.management.VMManagement
    public native long getClassInitializationTime();

    @Override // sun.management.VMManagement
    public native long getClassVerificationTime();

    VMManagementImpl() {
    }

    static {
        if (version == null) {
            throw new AssertionError((Object) "Invalid Management Version");
        }
        initOptionalSupportFields();
    }

    @Override // sun.management.VMManagement
    public boolean isCompilationTimeMonitoringSupported() {
        return compTimeMonitoringSupport;
    }

    @Override // sun.management.VMManagement
    public boolean isThreadContentionMonitoringSupported() {
        return threadContentionMonitoringSupport;
    }

    @Override // sun.management.VMManagement
    public boolean isCurrentThreadCpuTimeSupported() {
        return currentThreadCpuTimeSupport;
    }

    @Override // sun.management.VMManagement
    public boolean isOtherThreadCpuTimeSupported() {
        return otherThreadCpuTimeSupport;
    }

    @Override // sun.management.VMManagement
    public boolean isBootClassPathSupported() {
        return bootClassPathSupport;
    }

    @Override // sun.management.VMManagement
    public boolean isObjectMonitorUsageSupported() {
        return objectMonitorUsageSupport;
    }

    @Override // sun.management.VMManagement
    public boolean isSynchronizerUsageSupported() {
        return synchronizerUsageSupport;
    }

    @Override // sun.management.VMManagement
    public boolean isThreadAllocatedMemorySupported() {
        return threadAllocatedMemorySupport;
    }

    @Override // sun.management.VMManagement
    public boolean isGcNotificationSupported() {
        return gcNotificationSupport;
    }

    @Override // sun.management.VMManagement
    public boolean isRemoteDiagnosticCommandsSupported() {
        return remoteDiagnosticCommandsSupport;
    }

    @Override // sun.management.VMManagement
    public int getLoadedClassCount() {
        return (int) (getTotalClassCount() - getUnloadedClassCount());
    }

    @Override // sun.management.VMManagement
    public String getManagementVersion() {
        return version;
    }

    @Override // sun.management.VMManagement
    public String getVmId() {
        int processId = getProcessId();
        String hostName = "localhost";
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e2) {
        }
        return processId + "@" + hostName;
    }

    @Override // sun.management.VMManagement
    public String getVmName() {
        return System.getProperty("java.vm.name");
    }

    @Override // sun.management.VMManagement
    public String getVmVendor() {
        return System.getProperty("java.vm.vendor");
    }

    @Override // sun.management.VMManagement
    public String getVmVersion() {
        return System.getProperty("java.vm.version");
    }

    @Override // sun.management.VMManagement
    public String getVmSpecName() {
        return System.getProperty("java.vm.specification.name");
    }

    @Override // sun.management.VMManagement
    public String getVmSpecVendor() {
        return System.getProperty("java.vm.specification.vendor");
    }

    @Override // sun.management.VMManagement
    public String getVmSpecVersion() {
        return System.getProperty("java.vm.specification.version");
    }

    @Override // sun.management.VMManagement
    public String getClassPath() {
        return System.getProperty("java.class.path");
    }

    @Override // sun.management.VMManagement
    public String getLibraryPath() {
        return System.getProperty("java.library.path");
    }

    @Override // sun.management.VMManagement
    public String getBootClassPath() {
        return (String) AccessController.doPrivileged(new GetPropertyAction("sun.boot.class.path"));
    }

    @Override // sun.management.VMManagement
    public long getUptime() {
        return getUptime0();
    }

    @Override // sun.management.VMManagement
    public synchronized List<String> getVmArguments() {
        if (this.vmArgs == null) {
            String[] vmArguments0 = getVmArguments0();
            this.vmArgs = Collections.unmodifiableList((vmArguments0 == null || vmArguments0.length == 0) ? Collections.emptyList() : Arrays.asList(vmArguments0));
        }
        return this.vmArgs;
    }

    @Override // sun.management.VMManagement
    public String getCompilerName() {
        return (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: sun.management.VMManagementImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                return System.getProperty("sun.management.compiler");
            }
        });
    }

    @Override // sun.management.VMManagement
    public String getOsName() {
        return System.getProperty("os.name");
    }

    @Override // sun.management.VMManagement
    public String getOsArch() {
        return System.getProperty("os.arch");
    }

    @Override // sun.management.VMManagement
    public String getOsVersion() {
        return System.getProperty("os.version");
    }

    private synchronized PerfInstrumentation getPerfInstrumentation() {
        ByteBuffer byteBufferAttach;
        if (this.noPerfData || this.perfInstr != null) {
            return this.perfInstr;
        }
        try {
            byteBufferAttach = ((Perf) AccessController.doPrivileged(new Perf.GetPerfAction())).attach(0, InternalZipConstants.READ_MODE);
        } catch (IOException e2) {
            throw new AssertionError(e2);
        } catch (IllegalArgumentException e3) {
            this.noPerfData = true;
        }
        if (byteBufferAttach.capacity() == 0) {
            this.noPerfData = true;
            return null;
        }
        this.perfInstr = new PerfInstrumentation(byteBufferAttach);
        return this.perfInstr;
    }

    @Override // sun.management.VMManagement
    public List<Counter> getInternalCounters(String str) {
        PerfInstrumentation perfInstrumentation = getPerfInstrumentation();
        if (perfInstrumentation != null) {
            return perfInstrumentation.findByPattern(str);
        }
        return Collections.emptyList();
    }
}
