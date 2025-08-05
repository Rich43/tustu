package sun.management;

import java.lang.management.CompilationMXBean;
import javax.management.ObjectName;

/* loaded from: rt.jar:sun/management/CompilationImpl.class */
class CompilationImpl implements CompilationMXBean {
    private final VMManagement jvm;
    private final String name;

    CompilationImpl(VMManagement vMManagement) {
        this.jvm = vMManagement;
        this.name = this.jvm.getCompilerName();
        if (this.name == null) {
            throw new AssertionError((Object) "Null compiler name");
        }
    }

    @Override // java.lang.management.CompilationMXBean
    public String getName() {
        return this.name;
    }

    @Override // java.lang.management.CompilationMXBean
    public boolean isCompilationTimeMonitoringSupported() {
        return this.jvm.isCompilationTimeMonitoringSupported();
    }

    @Override // java.lang.management.CompilationMXBean
    public long getTotalCompilationTime() {
        if (!isCompilationTimeMonitoringSupported()) {
            throw new UnsupportedOperationException("Compilation time monitoring is not supported.");
        }
        return this.jvm.getTotalCompileTime();
    }

    @Override // java.lang.management.PlatformManagedObject
    public ObjectName getObjectName() {
        return Util.newObjectName(java.lang.management.ManagementFactory.COMPILATION_MXBEAN_NAME);
    }
}
