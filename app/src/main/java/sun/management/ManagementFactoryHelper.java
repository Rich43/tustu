package sun.management;

import com.sun.management.DiagnosticCommandMBean;
import com.sun.management.HotSpotDiagnosticMXBean;
import java.lang.Thread;
import java.lang.management.BufferPoolMXBean;
import java.lang.management.ClassLoadingMXBean;
import java.lang.management.CompilationMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryManagerMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.PlatformLoggingMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.management.DynamicMBean;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.RuntimeOperationsException;
import sun.misc.JavaNioAccess;
import sun.misc.SharedSecrets;
import sun.misc.VM;
import sun.nio.ch.FileChannelImpl;
import sun.util.logging.LoggingSupport;

/* loaded from: rt.jar:sun/management/ManagementFactoryHelper.class */
public class ManagementFactoryHelper {
    private static VMManagement jvm;
    private static final String BUFFER_POOL_MXBEAN_NAME = "java.nio:type=BufferPool";
    private static final String HOTSPOT_CLASS_LOADING_MBEAN_NAME = "sun.management:type=HotspotClassLoading";
    private static final String HOTSPOT_COMPILATION_MBEAN_NAME = "sun.management:type=HotspotCompilation";
    private static final String HOTSPOT_MEMORY_MBEAN_NAME = "sun.management:type=HotspotMemory";
    private static final String HOTSPOT_RUNTIME_MBEAN_NAME = "sun.management:type=HotspotRuntime";
    private static final String HOTSPOT_THREAD_MBEAN_NAME = "sun.management:type=HotspotThreading";
    static final String HOTSPOT_DIAGNOSTIC_COMMAND_MBEAN_NAME = "com.sun.management:type=DiagnosticCommand";
    private static final int JMM_THREAD_STATE_FLAG_MASK = -1048576;
    private static final int JMM_THREAD_STATE_FLAG_SUSPENDED = 1048576;
    private static final int JMM_THREAD_STATE_FLAG_NATIVE = 4194304;
    private static ClassLoadingImpl classMBean = null;
    private static MemoryImpl memoryMBean = null;
    private static ThreadImpl threadMBean = null;
    private static RuntimeImpl runtimeMBean = null;
    private static CompilationImpl compileMBean = null;
    private static OperatingSystemImpl osMBean = null;
    private static List<BufferPoolMXBean> bufferPools = null;
    private static HotSpotDiagnostic hsDiagMBean = null;
    private static HotspotRuntime hsRuntimeMBean = null;
    private static HotspotClassLoading hsClassMBean = null;
    private static HotspotThread hsThreadMBean = null;
    private static HotspotCompilation hsCompileMBean = null;
    private static HotspotMemory hsMemoryMBean = null;
    private static DiagnosticCommandImpl hsDiagCommandMBean = null;

    /* loaded from: rt.jar:sun/management/ManagementFactoryHelper$LoggingMXBean.class */
    public interface LoggingMXBean extends PlatformLoggingMXBean, java.util.logging.LoggingMXBean {
    }

    private ManagementFactoryHelper() {
    }

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.management.ManagementFactoryHelper.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                System.loadLibrary("management");
                return null;
            }
        });
        jvm = new VMManagementImpl();
    }

    public static synchronized ClassLoadingMXBean getClassLoadingMXBean() {
        if (classMBean == null) {
            classMBean = new ClassLoadingImpl(jvm);
        }
        return classMBean;
    }

    public static synchronized MemoryMXBean getMemoryMXBean() {
        if (memoryMBean == null) {
            memoryMBean = new MemoryImpl(jvm);
        }
        return memoryMBean;
    }

    public static synchronized ThreadMXBean getThreadMXBean() {
        if (threadMBean == null) {
            threadMBean = new ThreadImpl(jvm);
        }
        return threadMBean;
    }

    public static synchronized RuntimeMXBean getRuntimeMXBean() {
        if (runtimeMBean == null) {
            runtimeMBean = new RuntimeImpl(jvm);
        }
        return runtimeMBean;
    }

    public static synchronized CompilationMXBean getCompilationMXBean() {
        if (compileMBean == null && jvm.getCompilerName() != null) {
            compileMBean = new CompilationImpl(jvm);
        }
        return compileMBean;
    }

    public static synchronized OperatingSystemMXBean getOperatingSystemMXBean() {
        if (osMBean == null) {
            osMBean = new OperatingSystemImpl(jvm);
        }
        return osMBean;
    }

    public static List<MemoryPoolMXBean> getMemoryPoolMXBeans() {
        MemoryPoolMXBean[] memoryPools = MemoryImpl.getMemoryPools();
        ArrayList arrayList = new ArrayList(memoryPools.length);
        for (MemoryPoolMXBean memoryPoolMXBean : memoryPools) {
            arrayList.add(memoryPoolMXBean);
        }
        return arrayList;
    }

    public static List<MemoryManagerMXBean> getMemoryManagerMXBeans() {
        MemoryManagerMXBean[] memoryManagers = MemoryImpl.getMemoryManagers();
        ArrayList arrayList = new ArrayList(memoryManagers.length);
        for (MemoryManagerMXBean memoryManagerMXBean : memoryManagers) {
            arrayList.add(memoryManagerMXBean);
        }
        return arrayList;
    }

    public static List<GarbageCollectorMXBean> getGarbageCollectorMXBeans() {
        MemoryManagerMXBean[] memoryManagers = MemoryImpl.getMemoryManagers();
        ArrayList arrayList = new ArrayList(memoryManagers.length);
        for (MemoryManagerMXBean memoryManagerMXBean : memoryManagers) {
            if (GarbageCollectorMXBean.class.isInstance(memoryManagerMXBean)) {
                arrayList.add(GarbageCollectorMXBean.class.cast(memoryManagerMXBean));
            }
        }
        return arrayList;
    }

    public static PlatformLoggingMXBean getPlatformLoggingMXBean() {
        if (LoggingSupport.isAvailable()) {
            return PlatformLoggingImpl.instance;
        }
        return null;
    }

    /* loaded from: rt.jar:sun/management/ManagementFactoryHelper$PlatformLoggingImpl.class */
    static class PlatformLoggingImpl implements LoggingMXBean {
        static final PlatformLoggingMXBean instance = new PlatformLoggingImpl();
        static final String LOGGING_MXBEAN_NAME = "java.util.logging:type=Logging";
        private volatile ObjectName objname;

        PlatformLoggingImpl() {
        }

        @Override // java.lang.management.PlatformManagedObject
        public ObjectName getObjectName() {
            ObjectName objectNameNewObjectName = this.objname;
            if (objectNameNewObjectName == null) {
                synchronized (this) {
                    objectNameNewObjectName = this.objname;
                    if (objectNameNewObjectName == null) {
                        objectNameNewObjectName = Util.newObjectName("java.util.logging:type=Logging");
                        this.objname = objectNameNewObjectName;
                    }
                }
            }
            return objectNameNewObjectName;
        }

        @Override // java.lang.management.PlatformLoggingMXBean, java.util.logging.LoggingMXBean
        public List<String> getLoggerNames() {
            return LoggingSupport.getLoggerNames();
        }

        @Override // java.lang.management.PlatformLoggingMXBean, java.util.logging.LoggingMXBean
        public String getLoggerLevel(String str) {
            return LoggingSupport.getLoggerLevel(str);
        }

        @Override // java.lang.management.PlatformLoggingMXBean, java.util.logging.LoggingMXBean
        public void setLoggerLevel(String str, String str2) {
            LoggingSupport.setLoggerLevel(str, str2);
        }

        @Override // java.lang.management.PlatformLoggingMXBean, java.util.logging.LoggingMXBean
        public String getParentLoggerName(String str) {
            return LoggingSupport.getParentLoggerName(str);
        }
    }

    public static synchronized List<BufferPoolMXBean> getBufferPoolMXBeans() {
        if (bufferPools == null) {
            bufferPools = new ArrayList(2);
            bufferPools.add(createBufferPoolMXBean(SharedSecrets.getJavaNioAccess().getDirectBufferPool()));
            bufferPools.add(createBufferPoolMXBean(FileChannelImpl.getMappedBufferPool()));
        }
        return bufferPools;
    }

    private static BufferPoolMXBean createBufferPoolMXBean(final JavaNioAccess.BufferPool bufferPool) {
        return new BufferPoolMXBean() { // from class: sun.management.ManagementFactoryHelper.1
            private volatile ObjectName objname;

            @Override // java.lang.management.PlatformManagedObject
            public ObjectName getObjectName() {
                ObjectName objectNameNewObjectName = this.objname;
                if (objectNameNewObjectName == null) {
                    synchronized (this) {
                        objectNameNewObjectName = this.objname;
                        if (objectNameNewObjectName == null) {
                            objectNameNewObjectName = Util.newObjectName("java.nio:type=BufferPool,name=" + bufferPool.getName());
                            this.objname = objectNameNewObjectName;
                        }
                    }
                }
                return objectNameNewObjectName;
            }

            @Override // java.lang.management.BufferPoolMXBean
            public String getName() {
                return bufferPool.getName();
            }

            @Override // java.lang.management.BufferPoolMXBean
            public long getCount() {
                return bufferPool.getCount();
            }

            @Override // java.lang.management.BufferPoolMXBean
            public long getTotalCapacity() {
                return bufferPool.getTotalCapacity();
            }

            @Override // java.lang.management.BufferPoolMXBean
            public long getMemoryUsed() {
                return bufferPool.getMemoryUsed();
            }
        };
    }

    public static synchronized HotSpotDiagnosticMXBean getDiagnosticMXBean() {
        if (hsDiagMBean == null) {
            hsDiagMBean = new HotSpotDiagnostic();
        }
        return hsDiagMBean;
    }

    public static synchronized HotspotRuntimeMBean getHotspotRuntimeMBean() {
        if (hsRuntimeMBean == null) {
            hsRuntimeMBean = new HotspotRuntime(jvm);
        }
        return hsRuntimeMBean;
    }

    public static synchronized HotspotClassLoadingMBean getHotspotClassLoadingMBean() {
        if (hsClassMBean == null) {
            hsClassMBean = new HotspotClassLoading(jvm);
        }
        return hsClassMBean;
    }

    public static synchronized HotspotThreadMBean getHotspotThreadMBean() {
        if (hsThreadMBean == null) {
            hsThreadMBean = new HotspotThread(jvm);
        }
        return hsThreadMBean;
    }

    public static synchronized HotspotMemoryMBean getHotspotMemoryMBean() {
        if (hsMemoryMBean == null) {
            hsMemoryMBean = new HotspotMemory(jvm);
        }
        return hsMemoryMBean;
    }

    public static synchronized DiagnosticCommandMBean getDiagnosticCommandMBean() {
        if (hsDiagCommandMBean == null && jvm.isRemoteDiagnosticCommandsSupported()) {
            hsDiagCommandMBean = new DiagnosticCommandImpl(jvm);
        }
        return hsDiagCommandMBean;
    }

    public static synchronized HotspotCompilationMBean getHotspotCompilationMBean() {
        if (hsCompileMBean == null) {
            hsCompileMBean = new HotspotCompilation(jvm);
        }
        return hsCompileMBean;
    }

    private static void addMBean(final MBeanServer mBeanServer, final Object obj, String str) {
        try {
            final ObjectName objectNameNewObjectName = Util.newObjectName(str);
            AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: sun.management.ManagementFactoryHelper.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Void run() throws NotCompliantMBeanException, MBeanRegistrationException {
                    try {
                        mBeanServer.registerMBean(obj, objectNameNewObjectName);
                        return null;
                    } catch (InstanceAlreadyExistsException e2) {
                        return null;
                    }
                }
            });
        } catch (PrivilegedActionException e2) {
            throw Util.newException(e2.getException());
        }
    }

    public static HashMap<ObjectName, DynamicMBean> getPlatformDynamicMBeans() {
        HashMap<ObjectName, DynamicMBean> map = new HashMap<>();
        DiagnosticCommandMBean diagnosticCommandMBean = getDiagnosticCommandMBean();
        if (diagnosticCommandMBean != null) {
            map.put(Util.newObjectName(HOTSPOT_DIAGNOSTIC_COMMAND_MBEAN_NAME), diagnosticCommandMBean);
        }
        return map;
    }

    static void registerInternalMBeans(MBeanServer mBeanServer) {
        addMBean(mBeanServer, getHotspotClassLoadingMBean(), HOTSPOT_CLASS_LOADING_MBEAN_NAME);
        addMBean(mBeanServer, getHotspotMemoryMBean(), HOTSPOT_MEMORY_MBEAN_NAME);
        addMBean(mBeanServer, getHotspotRuntimeMBean(), HOTSPOT_RUNTIME_MBEAN_NAME);
        addMBean(mBeanServer, getHotspotThreadMBean(), HOTSPOT_THREAD_MBEAN_NAME);
        if (getCompilationMXBean() != null) {
            addMBean(mBeanServer, getHotspotCompilationMBean(), HOTSPOT_COMPILATION_MBEAN_NAME);
        }
    }

    private static void unregisterMBean(final MBeanServer mBeanServer, String str) {
        try {
            final ObjectName objectNameNewObjectName = Util.newObjectName(str);
            AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: sun.management.ManagementFactoryHelper.3
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Void run() throws MBeanRegistrationException, RuntimeOperationsException {
                    try {
                        mBeanServer.unregisterMBean(objectNameNewObjectName);
                        return null;
                    } catch (InstanceNotFoundException e2) {
                        return null;
                    }
                }
            });
        } catch (PrivilegedActionException e2) {
            throw Util.newException(e2.getException());
        }
    }

    static void unregisterInternalMBeans(MBeanServer mBeanServer) {
        unregisterMBean(mBeanServer, HOTSPOT_CLASS_LOADING_MBEAN_NAME);
        unregisterMBean(mBeanServer, HOTSPOT_MEMORY_MBEAN_NAME);
        unregisterMBean(mBeanServer, HOTSPOT_RUNTIME_MBEAN_NAME);
        unregisterMBean(mBeanServer, HOTSPOT_THREAD_MBEAN_NAME);
        if (getCompilationMXBean() != null) {
            unregisterMBean(mBeanServer, HOTSPOT_COMPILATION_MBEAN_NAME);
        }
    }

    public static boolean isThreadSuspended(int i2) {
        return (i2 & 1048576) != 0;
    }

    public static boolean isThreadRunningNative(int i2) {
        return (i2 & 4194304) != 0;
    }

    public static Thread.State toThreadState(int i2) {
        return VM.toThreadState(i2 & 1048575);
    }
}
