package sun.management;

import java.lang.management.ClassLoadingMXBean;
import javax.management.ObjectName;

/* loaded from: rt.jar:sun/management/ClassLoadingImpl.class */
class ClassLoadingImpl implements ClassLoadingMXBean {
    private final VMManagement jvm;

    static native void setVerboseClass(boolean z2);

    ClassLoadingImpl(VMManagement vMManagement) {
        this.jvm = vMManagement;
    }

    @Override // java.lang.management.ClassLoadingMXBean
    public long getTotalLoadedClassCount() {
        return this.jvm.getTotalClassCount();
    }

    @Override // java.lang.management.ClassLoadingMXBean
    public int getLoadedClassCount() {
        return this.jvm.getLoadedClassCount();
    }

    @Override // java.lang.management.ClassLoadingMXBean
    public long getUnloadedClassCount() {
        return this.jvm.getUnloadedClassCount();
    }

    @Override // java.lang.management.ClassLoadingMXBean
    public boolean isVerbose() {
        return this.jvm.getVerboseClass();
    }

    @Override // java.lang.management.ClassLoadingMXBean
    public void setVerbose(boolean z2) throws SecurityException {
        Util.checkControlAccess();
        setVerboseClass(z2);
    }

    @Override // java.lang.management.PlatformManagedObject
    public ObjectName getObjectName() {
        return Util.newObjectName(java.lang.management.ManagementFactory.CLASS_LOADING_MXBEAN_NAME);
    }
}
