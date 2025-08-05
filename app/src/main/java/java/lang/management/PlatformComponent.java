package java.lang.management;

import com.sun.management.HotSpotDiagnosticMXBean;
import com.sun.management.UnixOperatingSystemMXBean;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import sun.management.ManagementFactoryHelper;
import sun.management.Util;

/* loaded from: rt.jar:java/lang/management/PlatformComponent.class */
enum PlatformComponent {
    CLASS_LOADING("java.lang.management.ClassLoadingMXBean", "java.lang", "ClassLoading", defaultKeyProperties(), true, new MXBeanFetcher<ClassLoadingMXBean>() { // from class: java.lang.management.PlatformComponent.1
        @Override // java.lang.management.PlatformComponent.MXBeanFetcher
        public List<ClassLoadingMXBean> getMXBeans() {
            return Collections.singletonList(ManagementFactoryHelper.getClassLoadingMXBean());
        }
    }, new PlatformComponent[0]),
    COMPILATION("java.lang.management.CompilationMXBean", "java.lang", "Compilation", defaultKeyProperties(), true, new MXBeanFetcher<CompilationMXBean>() { // from class: java.lang.management.PlatformComponent.2
        @Override // java.lang.management.PlatformComponent.MXBeanFetcher
        public List<CompilationMXBean> getMXBeans() {
            CompilationMXBean compilationMXBean = ManagementFactoryHelper.getCompilationMXBean();
            if (compilationMXBean == null) {
                return Collections.emptyList();
            }
            return Collections.singletonList(compilationMXBean);
        }
    }, new PlatformComponent[0]),
    MEMORY("java.lang.management.MemoryMXBean", "java.lang", "Memory", defaultKeyProperties(), true, new MXBeanFetcher<MemoryMXBean>() { // from class: java.lang.management.PlatformComponent.3
        @Override // java.lang.management.PlatformComponent.MXBeanFetcher
        public List<MemoryMXBean> getMXBeans() {
            return Collections.singletonList(ManagementFactoryHelper.getMemoryMXBean());
        }
    }, new PlatformComponent[0]),
    GARBAGE_COLLECTOR("java.lang.management.GarbageCollectorMXBean", "java.lang", "GarbageCollector", keyProperties("name"), false, new MXBeanFetcher<GarbageCollectorMXBean>() { // from class: java.lang.management.PlatformComponent.4
        @Override // java.lang.management.PlatformComponent.MXBeanFetcher
        public List<GarbageCollectorMXBean> getMXBeans() {
            return ManagementFactoryHelper.getGarbageCollectorMXBeans();
        }
    }, new PlatformComponent[0]),
    MEMORY_MANAGER("java.lang.management.MemoryManagerMXBean", "java.lang", "MemoryManager", keyProperties("name"), false, new MXBeanFetcher<MemoryManagerMXBean>() { // from class: java.lang.management.PlatformComponent.5
        @Override // java.lang.management.PlatformComponent.MXBeanFetcher
        public List<MemoryManagerMXBean> getMXBeans() {
            return ManagementFactoryHelper.getMemoryManagerMXBeans();
        }
    }, GARBAGE_COLLECTOR),
    MEMORY_POOL("java.lang.management.MemoryPoolMXBean", "java.lang", "MemoryPool", keyProperties("name"), false, new MXBeanFetcher<MemoryPoolMXBean>() { // from class: java.lang.management.PlatformComponent.6
        @Override // java.lang.management.PlatformComponent.MXBeanFetcher
        public List<MemoryPoolMXBean> getMXBeans() {
            return ManagementFactoryHelper.getMemoryPoolMXBeans();
        }
    }, new PlatformComponent[0]),
    OPERATING_SYSTEM("java.lang.management.OperatingSystemMXBean", "java.lang", "OperatingSystem", defaultKeyProperties(), true, new MXBeanFetcher<OperatingSystemMXBean>() { // from class: java.lang.management.PlatformComponent.7
        @Override // java.lang.management.PlatformComponent.MXBeanFetcher
        public List<OperatingSystemMXBean> getMXBeans() {
            return Collections.singletonList(ManagementFactoryHelper.getOperatingSystemMXBean());
        }
    }, new PlatformComponent[0]),
    RUNTIME("java.lang.management.RuntimeMXBean", "java.lang", "Runtime", defaultKeyProperties(), true, new MXBeanFetcher<RuntimeMXBean>() { // from class: java.lang.management.PlatformComponent.8
        @Override // java.lang.management.PlatformComponent.MXBeanFetcher
        public List<RuntimeMXBean> getMXBeans() {
            return Collections.singletonList(ManagementFactoryHelper.getRuntimeMXBean());
        }
    }, new PlatformComponent[0]),
    THREADING("java.lang.management.ThreadMXBean", "java.lang", "Threading", defaultKeyProperties(), true, new MXBeanFetcher<ThreadMXBean>() { // from class: java.lang.management.PlatformComponent.9
        @Override // java.lang.management.PlatformComponent.MXBeanFetcher
        public List<ThreadMXBean> getMXBeans() {
            return Collections.singletonList(ManagementFactoryHelper.getThreadMXBean());
        }
    }, new PlatformComponent[0]),
    LOGGING("java.lang.management.PlatformLoggingMXBean", "java.util.logging", "Logging", defaultKeyProperties(), true, new MXBeanFetcher<PlatformLoggingMXBean>() { // from class: java.lang.management.PlatformComponent.10
        @Override // java.lang.management.PlatformComponent.MXBeanFetcher
        public List<PlatformLoggingMXBean> getMXBeans() {
            PlatformLoggingMXBean platformLoggingMXBean = ManagementFactoryHelper.getPlatformLoggingMXBean();
            if (platformLoggingMXBean == null) {
                return Collections.emptyList();
            }
            return Collections.singletonList(platformLoggingMXBean);
        }
    }, new PlatformComponent[0]),
    BUFFER_POOL("java.lang.management.BufferPoolMXBean", "java.nio", "BufferPool", keyProperties("name"), false, new MXBeanFetcher<BufferPoolMXBean>() { // from class: java.lang.management.PlatformComponent.11
        @Override // java.lang.management.PlatformComponent.MXBeanFetcher
        public List<BufferPoolMXBean> getMXBeans() {
            return ManagementFactoryHelper.getBufferPoolMXBeans();
        }
    }, new PlatformComponent[0]),
    SUN_GARBAGE_COLLECTOR("com.sun.management.GarbageCollectorMXBean", "java.lang", "GarbageCollector", keyProperties("name"), false, new MXBeanFetcher<com.sun.management.GarbageCollectorMXBean>() { // from class: java.lang.management.PlatformComponent.12
        @Override // java.lang.management.PlatformComponent.MXBeanFetcher
        public List<com.sun.management.GarbageCollectorMXBean> getMXBeans() {
            return PlatformComponent.getGcMXBeanList(com.sun.management.GarbageCollectorMXBean.class);
        }
    }, new PlatformComponent[0]),
    SUN_OPERATING_SYSTEM("com.sun.management.OperatingSystemMXBean", "java.lang", "OperatingSystem", defaultKeyProperties(), true, new MXBeanFetcher<com.sun.management.OperatingSystemMXBean>() { // from class: java.lang.management.PlatformComponent.13
        @Override // java.lang.management.PlatformComponent.MXBeanFetcher
        public List<com.sun.management.OperatingSystemMXBean> getMXBeans() {
            return PlatformComponent.getOSMXBeanList(com.sun.management.OperatingSystemMXBean.class);
        }
    }, new PlatformComponent[0]),
    SUN_UNIX_OPERATING_SYSTEM("com.sun.management.UnixOperatingSystemMXBean", "java.lang", "OperatingSystem", defaultKeyProperties(), true, new MXBeanFetcher<UnixOperatingSystemMXBean>() { // from class: java.lang.management.PlatformComponent.14
        @Override // java.lang.management.PlatformComponent.MXBeanFetcher
        public List<UnixOperatingSystemMXBean> getMXBeans() {
            return PlatformComponent.getOSMXBeanList(UnixOperatingSystemMXBean.class);
        }
    }, new PlatformComponent[0]),
    HOTSPOT_DIAGNOSTIC("com.sun.management.HotSpotDiagnosticMXBean", "com.sun.management", "HotSpotDiagnostic", defaultKeyProperties(), true, new MXBeanFetcher<HotSpotDiagnosticMXBean>() { // from class: java.lang.management.PlatformComponent.15
        @Override // java.lang.management.PlatformComponent.MXBeanFetcher
        public List<HotSpotDiagnosticMXBean> getMXBeans() {
            return Collections.singletonList(ManagementFactoryHelper.getDiagnosticMXBean());
        }
    }, new PlatformComponent[0]);

    private final String mxbeanInterfaceName;
    private final String domain;
    private final String type;
    private final Set<String> keyProperties;
    private final MXBeanFetcher<?> fetcher;
    private final PlatformComponent[] subComponents;
    private final boolean singleton;
    private static Set<String> defaultKeyProps;
    private static Map<String, PlatformComponent> enumMap;
    private static final long serialVersionUID = 6992337162326171013L;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: rt.jar:java/lang/management/PlatformComponent$MXBeanFetcher.class */
    interface MXBeanFetcher<T extends PlatformManagedObject> {
        List<T> getMXBeans();
    }

    static {
        $assertionsDisabled = !PlatformComponent.class.desiredAssertionStatus();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T extends GarbageCollectorMXBean> List<T> getGcMXBeanList(Class<T> cls) {
        List<GarbageCollectorMXBean> garbageCollectorMXBeans = ManagementFactoryHelper.getGarbageCollectorMXBeans();
        ArrayList arrayList = new ArrayList(garbageCollectorMXBeans.size());
        for (GarbageCollectorMXBean garbageCollectorMXBean : garbageCollectorMXBeans) {
            if (cls.isInstance(garbageCollectorMXBean)) {
                arrayList.add(cls.cast(garbageCollectorMXBean));
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T extends OperatingSystemMXBean> List<T> getOSMXBeanList(Class<T> cls) {
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactoryHelper.getOperatingSystemMXBean();
        if (cls.isInstance(operatingSystemMXBean)) {
            return Collections.singletonList(cls.cast(operatingSystemMXBean));
        }
        return Collections.emptyList();
    }

    PlatformComponent(String str, String str2, String str3, Set set, boolean z2, MXBeanFetcher mXBeanFetcher, PlatformComponent... platformComponentArr) {
        this.mxbeanInterfaceName = str;
        this.domain = str2;
        this.type = str3;
        this.keyProperties = set;
        this.singleton = z2;
        this.fetcher = mXBeanFetcher;
        this.subComponents = platformComponentArr;
    }

    private static Set<String> defaultKeyProperties() {
        if (defaultKeyProps == null) {
            defaultKeyProps = Collections.singleton("type");
        }
        return defaultKeyProps;
    }

    private static Set<String> keyProperties(String... strArr) {
        HashSet hashSet = new HashSet();
        hashSet.add("type");
        for (String str : strArr) {
            hashSet.add(str);
        }
        return hashSet;
    }

    boolean isSingleton() {
        return this.singleton;
    }

    String getMXBeanInterfaceName() {
        return this.mxbeanInterfaceName;
    }

    Class<? extends PlatformManagedObject> getMXBeanInterface() {
        try {
            return Class.forName(this.mxbeanInterfaceName, false, PlatformManagedObject.class.getClassLoader());
        } catch (ClassNotFoundException e2) {
            throw new AssertionError(e2);
        }
    }

    <T extends PlatformManagedObject> List<T> getMXBeans(Class<T> cls) {
        return this.fetcher.getMXBeans();
    }

    <T extends PlatformManagedObject> T getSingletonMXBean(Class<T> cls) {
        if (!this.singleton) {
            throw new IllegalArgumentException(this.mxbeanInterfaceName + " can have zero or more than one instances");
        }
        List<T> mXBeans = getMXBeans(cls);
        if (!$assertionsDisabled && mXBeans.size() != 1) {
            throw new AssertionError();
        }
        if (mXBeans.isEmpty()) {
            return null;
        }
        return mXBeans.get(0);
    }

    <T extends PlatformManagedObject> T getSingletonMXBean(MBeanServerConnection mBeanServerConnection, Class<T> cls) throws IOException {
        if (!this.singleton) {
            throw new IllegalArgumentException(this.mxbeanInterfaceName + " can have zero or more than one instances");
        }
        if ($assertionsDisabled || this.keyProperties.size() == 1) {
            return (T) ManagementFactory.newPlatformMXBeanProxy(mBeanServerConnection, this.domain + ":type=" + this.type, cls);
        }
        throw new AssertionError();
    }

    <T extends PlatformManagedObject> List<T> getMXBeans(MBeanServerConnection mBeanServerConnection, Class<T> cls) throws IOException {
        ArrayList arrayList = new ArrayList();
        Iterator<ObjectName> it = getObjectNames(mBeanServerConnection).iterator();
        while (it.hasNext()) {
            arrayList.add(ManagementFactory.newPlatformMXBeanProxy(mBeanServerConnection, it.next().getCanonicalName(), cls));
        }
        return arrayList;
    }

    private Set<ObjectName> getObjectNames(MBeanServerConnection mBeanServerConnection) throws IOException {
        String str = this.domain + ":type=" + this.type;
        if (this.keyProperties.size() > 1) {
            str = str + ",*";
        }
        Set<ObjectName> setQueryNames = mBeanServerConnection.queryNames(Util.newObjectName(str), null);
        for (PlatformComponent platformComponent : this.subComponents) {
            setQueryNames.addAll(platformComponent.getObjectNames(mBeanServerConnection));
        }
        return setQueryNames;
    }

    private static synchronized void ensureInitialized() {
        if (enumMap == null) {
            enumMap = new HashMap();
            for (PlatformComponent platformComponent : values()) {
                enumMap.put(platformComponent.getMXBeanInterfaceName(), platformComponent);
            }
        }
    }

    static boolean isPlatformMXBean(String str) {
        ensureInitialized();
        return enumMap.containsKey(str);
    }

    static <T extends PlatformManagedObject> PlatformComponent getPlatformComponent(Class<T> cls) {
        ensureInitialized();
        PlatformComponent platformComponent = enumMap.get(cls.getName());
        if (platformComponent != null && platformComponent.getMXBeanInterface() == cls) {
            return platformComponent;
        }
        return null;
    }
}
