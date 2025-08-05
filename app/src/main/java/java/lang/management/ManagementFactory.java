package java.lang.management;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.management.DynamicMBean;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.JMX;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerFactory;
import javax.management.MBeanServerPermission;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.NotificationEmitter;
import javax.management.ObjectName;
import javax.management.StandardEmitterMBean;
import javax.management.StandardMBean;
import sun.management.ExtendedPlatformComponent;
import sun.management.ManagementFactoryHelper;
import sun.misc.VM;

/* loaded from: rt.jar:java/lang/management/ManagementFactory.class */
public class ManagementFactory {
    public static final String CLASS_LOADING_MXBEAN_NAME = "java.lang:type=ClassLoading";
    public static final String COMPILATION_MXBEAN_NAME = "java.lang:type=Compilation";
    public static final String MEMORY_MXBEAN_NAME = "java.lang:type=Memory";
    public static final String OPERATING_SYSTEM_MXBEAN_NAME = "java.lang:type=OperatingSystem";
    public static final String RUNTIME_MXBEAN_NAME = "java.lang:type=Runtime";
    public static final String THREAD_MXBEAN_NAME = "java.lang:type=Threading";
    public static final String GARBAGE_COLLECTOR_MXBEAN_DOMAIN_TYPE = "java.lang:type=GarbageCollector";
    public static final String MEMORY_MANAGER_MXBEAN_DOMAIN_TYPE = "java.lang:type=MemoryManager";
    public static final String MEMORY_POOL_MXBEAN_DOMAIN_TYPE = "java.lang:type=MemoryPool";
    private static MBeanServer platformMBeanServer;
    private static final String NOTIF_EMITTER = "javax.management.NotificationEmitter";

    private ManagementFactory() {
    }

    public static ClassLoadingMXBean getClassLoadingMXBean() {
        return ManagementFactoryHelper.getClassLoadingMXBean();
    }

    public static MemoryMXBean getMemoryMXBean() {
        return ManagementFactoryHelper.getMemoryMXBean();
    }

    public static ThreadMXBean getThreadMXBean() {
        return ManagementFactoryHelper.getThreadMXBean();
    }

    public static RuntimeMXBean getRuntimeMXBean() {
        return ManagementFactoryHelper.getRuntimeMXBean();
    }

    public static CompilationMXBean getCompilationMXBean() {
        return ManagementFactoryHelper.getCompilationMXBean();
    }

    public static OperatingSystemMXBean getOperatingSystemMXBean() {
        return ManagementFactoryHelper.getOperatingSystemMXBean();
    }

    public static List<MemoryPoolMXBean> getMemoryPoolMXBeans() {
        return ManagementFactoryHelper.getMemoryPoolMXBeans();
    }

    public static List<MemoryManagerMXBean> getMemoryManagerMXBeans() {
        return ManagementFactoryHelper.getMemoryManagerMXBeans();
    }

    public static List<GarbageCollectorMXBean> getGarbageCollectorMXBeans() {
        return ManagementFactoryHelper.getGarbageCollectorMXBeans();
    }

    public static synchronized MBeanServer getPlatformMBeanServer() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new MBeanServerPermission("createMBeanServer"));
        }
        if (platformMBeanServer == null) {
            platformMBeanServer = MBeanServerFactory.createMBeanServer();
            for (PlatformComponent platformComponent : PlatformComponent.values()) {
                for (PlatformManagedObject platformManagedObject : platformComponent.getMXBeans(platformComponent.getMXBeanInterface())) {
                    if (!platformMBeanServer.isRegistered(platformManagedObject.getObjectName())) {
                        addMXBean(platformMBeanServer, platformManagedObject);
                    }
                }
            }
            for (Map.Entry<ObjectName, DynamicMBean> entry : ManagementFactoryHelper.getPlatformDynamicMBeans().entrySet()) {
                addDynamicMBean(platformMBeanServer, entry.getValue(), entry.getKey());
            }
            for (PlatformManagedObject platformManagedObject2 : ExtendedPlatformComponent.getMXBeans()) {
                if (!platformMBeanServer.isRegistered(platformManagedObject2.getObjectName())) {
                    addMXBean(platformMBeanServer, platformManagedObject2);
                }
            }
        }
        return platformMBeanServer;
    }

    public static <T> T newPlatformMXBeanProxy(MBeanServerConnection mBeanServerConnection, String str, final Class<T> cls) throws IOException {
        if (!VM.isSystemDomainLoader((ClassLoader) AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() { // from class: java.lang.management.ManagementFactory.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public ClassLoader run2() {
                return cls.getClassLoader();
            }
        }))) {
            throw new IllegalArgumentException(str + " is not a platform MXBean");
        }
        try {
            ObjectName objectName = new ObjectName(str);
            if (!mBeanServerConnection.isInstanceOf(objectName, cls.getName())) {
                throw new IllegalArgumentException(str + " is not an instance of " + ((Object) cls));
            }
            return (T) JMX.newMXBeanProxy(mBeanServerConnection, objectName, cls, mBeanServerConnection.isInstanceOf(objectName, NOTIF_EMITTER));
        } catch (InstanceNotFoundException | MalformedObjectNameException e2) {
            throw new IllegalArgumentException(e2);
        }
    }

    public static <T extends PlatformManagedObject> T getPlatformMXBean(Class<T> cls) {
        PlatformComponent platformComponent = PlatformComponent.getPlatformComponent(cls);
        if (platformComponent == null) {
            T t2 = (T) ExtendedPlatformComponent.getMXBean(cls);
            if (t2 != null) {
                return t2;
            }
            throw new IllegalArgumentException(cls.getName() + " is not a platform management interface");
        }
        if (!platformComponent.isSingleton()) {
            throw new IllegalArgumentException(cls.getName() + " can have zero or more than one instances");
        }
        return (T) platformComponent.getSingletonMXBean(cls);
    }

    public static <T extends PlatformManagedObject> List<T> getPlatformMXBeans(Class<T> cls) {
        PlatformComponent platformComponent = PlatformComponent.getPlatformComponent(cls);
        if (platformComponent == null) {
            PlatformManagedObject mXBean = ExtendedPlatformComponent.getMXBean(cls);
            if (mXBean != null) {
                return Collections.singletonList(mXBean);
            }
            throw new IllegalArgumentException(cls.getName() + " is not a platform management interface");
        }
        return Collections.unmodifiableList(platformComponent.getMXBeans(cls));
    }

    public static <T extends PlatformManagedObject> T getPlatformMXBean(MBeanServerConnection mBeanServerConnection, Class<T> cls) throws IOException {
        PlatformComponent platformComponent = PlatformComponent.getPlatformComponent(cls);
        if (platformComponent == null) {
            PlatformManagedObject mXBean = ExtendedPlatformComponent.getMXBean(cls);
            if (mXBean != null) {
                return (T) newPlatformMXBeanProxy(mBeanServerConnection, mXBean.getObjectName().getCanonicalName(), cls);
            }
            throw new IllegalArgumentException(cls.getName() + " is not a platform management interface");
        }
        if (!platformComponent.isSingleton()) {
            throw new IllegalArgumentException(cls.getName() + " can have zero or more than one instances");
        }
        return (T) platformComponent.getSingletonMXBean(mBeanServerConnection, cls);
    }

    public static <T extends PlatformManagedObject> List<T> getPlatformMXBeans(MBeanServerConnection mBeanServerConnection, Class<T> cls) throws IOException {
        PlatformComponent platformComponent = PlatformComponent.getPlatformComponent(cls);
        if (platformComponent == null) {
            PlatformManagedObject mXBean = ExtendedPlatformComponent.getMXBean(cls);
            if (mXBean != null) {
                return Collections.singletonList((PlatformManagedObject) newPlatformMXBeanProxy(mBeanServerConnection, mXBean.getObjectName().getCanonicalName(), cls));
            }
            throw new IllegalArgumentException(cls.getName() + " is not a platform management interface");
        }
        return Collections.unmodifiableList(platformComponent.getMXBeans(mBeanServerConnection, cls));
    }

    public static Set<Class<? extends PlatformManagedObject>> getPlatformManagementInterfaces() {
        HashSet hashSet = new HashSet();
        for (PlatformComponent platformComponent : PlatformComponent.values()) {
            hashSet.add(platformComponent.getMXBeanInterface());
        }
        return Collections.unmodifiableSet(hashSet);
    }

    private static void addMXBean(final MBeanServer mBeanServer, final PlatformManagedObject platformManagedObject) {
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: java.lang.management.ManagementFactory.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Void run() throws InstanceAlreadyExistsException, NotCompliantMBeanException, MBeanRegistrationException {
                    Object standardMBean;
                    if (platformManagedObject instanceof DynamicMBean) {
                        standardMBean = (DynamicMBean) DynamicMBean.class.cast(platformManagedObject);
                    } else if (platformManagedObject instanceof NotificationEmitter) {
                        standardMBean = new StandardEmitterMBean(platformManagedObject, null, true, (NotificationEmitter) platformManagedObject);
                    } else {
                        standardMBean = new StandardMBean(platformManagedObject, null, true);
                    }
                    mBeanServer.registerMBean(standardMBean, platformManagedObject.getObjectName());
                    return null;
                }
            });
        } catch (PrivilegedActionException e2) {
            throw new RuntimeException(e2.getException());
        }
    }

    private static void addDynamicMBean(final MBeanServer mBeanServer, final DynamicMBean dynamicMBean, final ObjectName objectName) {
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: java.lang.management.ManagementFactory.3
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Void run() throws InstanceAlreadyExistsException, NotCompliantMBeanException, MBeanRegistrationException {
                    mBeanServer.registerMBean(dynamicMBean, objectName);
                    return null;
                }
            });
        } catch (PrivilegedActionException e2) {
            throw new RuntimeException(e2.getException());
        }
    }
}
