package java.lang;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import sun.misc.CompoundEnumeration;
import sun.misc.Launcher;
import sun.misc.PerfCounter;
import sun.misc.Resource;
import sun.misc.URLClassPath;
import sun.misc.VM;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;
import sun.reflect.misc.ReflectUtil;
import sun.security.util.SecurityConstants;
import sun.security.x509.X509CertImpl;

/* loaded from: rt.jar:java/lang/ClassLoader.class */
public abstract class ClassLoader {
    private final ClassLoader parent;
    private final ConcurrentHashMap<String, Object> parallelLockMap;
    private final Map<String, Certificate[]> package2certs;
    private static final Certificate[] nocerts;
    private final Vector<Class<?>> classes;
    private final ProtectionDomain defaultDomain;
    private final HashMap<String, Package> packages;
    private static ClassLoader scl;
    private static boolean sclSet;
    private static Vector<String> loadedLibraryNames;
    private static Vector<NativeLibrary> systemNativeLibraries;
    private Vector<NativeLibrary> nativeLibraries;
    private static Stack<NativeLibrary> nativeLibraryContext;
    private static String[] usr_paths;
    private static String[] sys_paths;
    final Object assertionLock;
    private boolean defaultAssertionStatus;
    private Map<String, Boolean> packageAssertionStatus;
    Map<String, Boolean> classAssertionStatus;

    private static native void registerNatives();

    private native Class<?> defineClass0(String str, byte[] bArr, int i2, int i3, ProtectionDomain protectionDomain);

    private native Class<?> defineClass1(String str, byte[] bArr, int i2, int i3, ProtectionDomain protectionDomain, String str2);

    private native Class<?> defineClass2(String str, ByteBuffer byteBuffer, int i2, int i3, ProtectionDomain protectionDomain, String str2);

    private native void resolveClass0(Class<?> cls);

    private native Class<?> findBootstrapClass(String str);

    private final native Class<?> findLoadedClass0(String str);

    private static native String findBuiltinLib(String str);

    private static native AssertionStatusDirectives retrieveDirectives();

    static {
        registerNatives();
        nocerts = new Certificate[0];
        loadedLibraryNames = new Vector<>();
        systemNativeLibraries = new Vector<>();
        nativeLibraryContext = new Stack<>();
    }

    /* loaded from: rt.jar:java/lang/ClassLoader$ParallelLoaders.class */
    private static class ParallelLoaders {
        private static final Set<Class<? extends ClassLoader>> loaderTypes = Collections.newSetFromMap(new WeakHashMap());

        private ParallelLoaders() {
        }

        static {
            synchronized (loaderTypes) {
                loaderTypes.add(ClassLoader.class);
            }
        }

        static boolean register(Class<? extends ClassLoader> cls) {
            synchronized (loaderTypes) {
                if (loaderTypes.contains(cls.getSuperclass())) {
                    loaderTypes.add(cls);
                    return true;
                }
                return false;
            }
        }

        static boolean isRegistered(Class<? extends ClassLoader> cls) {
            boolean zContains;
            synchronized (loaderTypes) {
                zContains = loaderTypes.contains(cls);
            }
            return zContains;
        }
    }

    void addClass(Class<?> cls) {
        this.classes.addElement(cls);
    }

    private static Void checkCreateClassLoader() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkCreateClassLoader();
            return null;
        }
        return null;
    }

    private ClassLoader(Void r9, ClassLoader classLoader) {
        this.classes = new Vector<>();
        this.defaultDomain = new ProtectionDomain(new CodeSource((URL) null, (Certificate[]) null), null, this, null);
        this.packages = new HashMap<>();
        this.nativeLibraries = new Vector<>();
        this.defaultAssertionStatus = false;
        this.packageAssertionStatus = null;
        this.classAssertionStatus = null;
        this.parent = classLoader;
        if (ParallelLoaders.isRegistered(getClass())) {
            this.parallelLockMap = new ConcurrentHashMap<>();
            this.package2certs = new ConcurrentHashMap();
            this.assertionLock = new Object();
        } else {
            this.parallelLockMap = null;
            this.package2certs = new Hashtable();
            this.assertionLock = this;
        }
    }

    protected ClassLoader(ClassLoader classLoader) {
        this(checkCreateClassLoader(), classLoader);
    }

    protected ClassLoader() {
        this(checkCreateClassLoader(), getSystemClassLoader());
    }

    public Class<?> loadClass(String str) throws ClassNotFoundException {
        return loadClass(str, false);
    }

    protected Class<?> loadClass(String str, boolean z2) throws ClassNotFoundException {
        Class<?> cls;
        synchronized (getClassLoadingLock(str)) {
            Class<?> clsFindLoadedClass = findLoadedClass(str);
            if (clsFindLoadedClass == null) {
                long jNanoTime = System.nanoTime();
                try {
                    if (this.parent != null) {
                        clsFindLoadedClass = this.parent.loadClass(str, false);
                    } else {
                        clsFindLoadedClass = findBootstrapClassOrNull(str);
                    }
                } catch (ClassNotFoundException e2) {
                }
                if (clsFindLoadedClass == null) {
                    long jNanoTime2 = System.nanoTime();
                    clsFindLoadedClass = findClass(str);
                    PerfCounter.getParentDelegationTime().addTime(jNanoTime2 - jNanoTime);
                    PerfCounter.getFindClassTime().addElapsedTimeFrom(jNanoTime2);
                    PerfCounter.getFindClasses().increment();
                }
            }
            if (z2) {
                resolveClass(clsFindLoadedClass);
            }
            cls = clsFindLoadedClass;
        }
        return cls;
    }

    protected Object getClassLoadingLock(String str) {
        Object objPutIfAbsent = this;
        if (this.parallelLockMap != null) {
            Object obj = new Object();
            objPutIfAbsent = this.parallelLockMap.putIfAbsent(str, obj);
            if (objPutIfAbsent == null) {
                objPutIfAbsent = obj;
            }
        }
        return objPutIfAbsent;
    }

    private Class<?> loadClassInternal(String str) throws ClassNotFoundException {
        Class<?> clsLoadClass;
        if (this.parallelLockMap == null) {
            synchronized (this) {
                clsLoadClass = loadClass(str);
            }
            return clsLoadClass;
        }
        return loadClass(str);
    }

    private void checkPackageAccess(Class<?> cls, ProtectionDomain protectionDomain) {
        final SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            if (ReflectUtil.isNonPublicProxyClass(cls)) {
                for (Class<?> cls2 : cls.getInterfaces()) {
                    checkPackageAccess(cls2, protectionDomain);
                }
                return;
            }
            final String name = cls.getName();
            final int iLastIndexOf = name.lastIndexOf(46);
            if (iLastIndexOf != -1) {
                AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.lang.ClassLoader.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    public Void run() {
                        securityManager.checkPackageAccess(name.substring(0, iLastIndexOf));
                        return null;
                    }
                }, new AccessControlContext(new ProtectionDomain[]{protectionDomain}));
            }
        }
    }

    protected Class<?> findClass(String str) throws ClassNotFoundException {
        throw new ClassNotFoundException(str);
    }

    @Deprecated
    protected final Class<?> defineClass(byte[] bArr, int i2, int i3) throws ClassFormatError {
        return defineClass(null, bArr, i2, i3, null);
    }

    protected final Class<?> defineClass(String str, byte[] bArr, int i2, int i3) throws ClassFormatError {
        return defineClass(str, bArr, i2, i3, null);
    }

    private ProtectionDomain preDefineClass(String str, ProtectionDomain protectionDomain) {
        if (!checkName(str)) {
            throw new NoClassDefFoundError("IllegalName: " + str);
        }
        if (str != null && str.startsWith("java.")) {
            throw new SecurityException("Prohibited package name: " + str.substring(0, str.lastIndexOf(46)));
        }
        if (protectionDomain == null) {
            protectionDomain = this.defaultDomain;
        }
        if (str != null) {
            checkCerts(str, protectionDomain.getCodeSource());
        }
        return protectionDomain;
    }

    private String defineClassSourceLocation(ProtectionDomain protectionDomain) {
        CodeSource codeSource = protectionDomain.getCodeSource();
        String string = null;
        if (codeSource != null && codeSource.getLocation() != null) {
            string = codeSource.getLocation().toString();
        }
        return string;
    }

    private void postDefineClass(Class<?> cls, ProtectionDomain protectionDomain) {
        Certificate[] certificates;
        if (protectionDomain.getCodeSource() != null && (certificates = protectionDomain.getCodeSource().getCertificates()) != null) {
            setSigners(cls, certificates);
        }
    }

    protected final Class<?> defineClass(String str, byte[] bArr, int i2, int i3, ProtectionDomain protectionDomain) throws ClassFormatError {
        ProtectionDomain protectionDomainPreDefineClass = preDefineClass(str, protectionDomain);
        Class<?> clsDefineClass1 = defineClass1(str, bArr, i2, i3, protectionDomainPreDefineClass, defineClassSourceLocation(protectionDomainPreDefineClass));
        postDefineClass(clsDefineClass1, protectionDomainPreDefineClass);
        return clsDefineClass1;
    }

    protected final Class<?> defineClass(String str, ByteBuffer byteBuffer, ProtectionDomain protectionDomain) throws ClassFormatError {
        int iRemaining = byteBuffer.remaining();
        if (!byteBuffer.isDirect()) {
            if (byteBuffer.hasArray()) {
                return defineClass(str, byteBuffer.array(), byteBuffer.position() + byteBuffer.arrayOffset(), iRemaining, protectionDomain);
            }
            byte[] bArr = new byte[iRemaining];
            byteBuffer.get(bArr);
            return defineClass(str, bArr, 0, iRemaining, protectionDomain);
        }
        ProtectionDomain protectionDomainPreDefineClass = preDefineClass(str, protectionDomain);
        Class<?> clsDefineClass2 = defineClass2(str, byteBuffer, byteBuffer.position(), iRemaining, protectionDomainPreDefineClass, defineClassSourceLocation(protectionDomainPreDefineClass));
        postDefineClass(clsDefineClass2, protectionDomainPreDefineClass);
        return clsDefineClass2;
    }

    private boolean checkName(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        if (str.indexOf(47) == -1) {
            if (!VM.allowArraySyntax() && str.charAt(0) == '[') {
                return false;
            }
            return true;
        }
        return false;
    }

    private void checkCerts(String str, CodeSource codeSource) {
        Certificate[] certificateArr;
        int iLastIndexOf = str.lastIndexOf(46);
        String strSubstring = iLastIndexOf == -1 ? "" : str.substring(0, iLastIndexOf);
        Certificate[] certificates = null;
        if (codeSource != null) {
            certificates = codeSource.getCertificates();
        }
        if (this.parallelLockMap == null) {
            synchronized (this) {
                certificateArr = this.package2certs.get(strSubstring);
                if (certificateArr == null) {
                    this.package2certs.put(strSubstring, certificates == null ? nocerts : certificates);
                }
            }
        } else {
            certificateArr = (Certificate[]) ((ConcurrentHashMap) this.package2certs).putIfAbsent(strSubstring, certificates == null ? nocerts : certificates);
        }
        if (certificateArr != null && !compareCerts(certificateArr, certificates)) {
            throw new SecurityException("class \"" + str + "\"'s signer information does not match signer information of other classes in the same package");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean compareCerts(Certificate[] certificateArr, Certificate[] certificateArr2) {
        if (certificateArr2 == 0 || certificateArr2.length == 0) {
            return certificateArr.length == 0;
        }
        if (certificateArr2.length != certificateArr.length) {
            return false;
        }
        for (X509CertImpl x509CertImpl : certificateArr2) {
            boolean z2 = false;
            int i2 = 0;
            while (true) {
                if (i2 >= certificateArr.length) {
                    break;
                }
                if (!x509CertImpl.equals(certificateArr[i2])) {
                    i2++;
                } else {
                    z2 = true;
                    break;
                }
            }
            if (!z2) {
                return false;
            }
        }
        for (X509CertImpl x509CertImpl2 : certificateArr) {
            boolean z3 = false;
            int i3 = 0;
            while (true) {
                if (i3 >= certificateArr2.length) {
                    break;
                }
                if (!x509CertImpl2.equals(certificateArr2[i3])) {
                    i3++;
                } else {
                    z3 = true;
                    break;
                }
            }
            if (!z3) {
                return false;
            }
        }
        return true;
    }

    protected final void resolveClass(Class<?> cls) {
        resolveClass0(cls);
    }

    protected final Class<?> findSystemClass(String str) throws ClassNotFoundException {
        ClassLoader systemClassLoader = getSystemClassLoader();
        if (systemClassLoader == null) {
            if (!checkName(str)) {
                throw new ClassNotFoundException(str);
            }
            Class<?> clsFindBootstrapClass = findBootstrapClass(str);
            if (clsFindBootstrapClass == null) {
                throw new ClassNotFoundException(str);
            }
            return clsFindBootstrapClass;
        }
        return systemClassLoader.loadClass(str);
    }

    private Class<?> findBootstrapClassOrNull(String str) {
        if (checkName(str)) {
            return findBootstrapClass(str);
        }
        return null;
    }

    protected final Class<?> findLoadedClass(String str) {
        if (!checkName(str)) {
            return null;
        }
        return findLoadedClass0(str);
    }

    protected final void setSigners(Class<?> cls, Object[] objArr) {
        cls.setSigners(objArr);
    }

    public URL getResource(String str) {
        URL bootstrapResource;
        if (this.parent != null) {
            bootstrapResource = this.parent.getResource(str);
        } else {
            bootstrapResource = getBootstrapResource(str);
        }
        if (bootstrapResource == null) {
            bootstrapResource = findResource(str);
        }
        return bootstrapResource;
    }

    public Enumeration<URL> getResources(String str) throws IOException {
        Enumeration[] enumerationArr = new Enumeration[2];
        if (this.parent != null) {
            enumerationArr[0] = this.parent.getResources(str);
        } else {
            enumerationArr[0] = getBootstrapResources(str);
        }
        enumerationArr[1] = findResources(str);
        return new CompoundEnumeration(enumerationArr);
    }

    protected URL findResource(String str) {
        return null;
    }

    protected Enumeration<URL> findResources(String str) throws IOException {
        return Collections.emptyEnumeration();
    }

    @CallerSensitive
    protected static boolean registerAsParallelCapable() {
        return ParallelLoaders.register(Reflection.getCallerClass().asSubclass(ClassLoader.class));
    }

    public static URL getSystemResource(String str) {
        ClassLoader systemClassLoader = getSystemClassLoader();
        if (systemClassLoader == null) {
            return getBootstrapResource(str);
        }
        return systemClassLoader.getResource(str);
    }

    public static Enumeration<URL> getSystemResources(String str) throws IOException {
        ClassLoader systemClassLoader = getSystemClassLoader();
        if (systemClassLoader == null) {
            return getBootstrapResources(str);
        }
        return systemClassLoader.getResources(str);
    }

    private static URL getBootstrapResource(String str) {
        Resource resource = getBootstrapClassPath().getResource(str);
        if (resource != null) {
            return resource.getURL();
        }
        return null;
    }

    private static Enumeration<URL> getBootstrapResources(String str) throws IOException {
        final Enumeration<Resource> resources = getBootstrapClassPath().getResources(str);
        return new Enumeration<URL>() { // from class: java.lang.ClassLoader.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Enumeration
            public URL nextElement() {
                return ((Resource) resources.nextElement()).getURL();
            }

            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                return resources.hasMoreElements();
            }
        };
    }

    static URLClassPath getBootstrapClassPath() {
        return Launcher.getBootstrapClassPath();
    }

    public InputStream getResourceAsStream(String str) {
        URL resource = getResource(str);
        if (resource == null) {
            return null;
        }
        try {
            return resource.openStream();
        } catch (IOException e2) {
            return null;
        }
    }

    public static InputStream getSystemResourceAsStream(String str) {
        URL systemResource = getSystemResource(str);
        if (systemResource == null) {
            return null;
        }
        try {
            return systemResource.openStream();
        } catch (IOException e2) {
            return null;
        }
    }

    @CallerSensitive
    public final ClassLoader getParent() {
        if (this.parent == null) {
            return null;
        }
        if (System.getSecurityManager() != null) {
            checkClassLoaderPermission(this.parent, Reflection.getCallerClass());
        }
        return this.parent;
    }

    @CallerSensitive
    public static ClassLoader getSystemClassLoader() {
        initSystemClassLoader();
        if (scl == null) {
            return null;
        }
        if (System.getSecurityManager() != null) {
            checkClassLoaderPermission(scl, Reflection.getCallerClass());
        }
        return scl;
    }

    private static synchronized void initSystemClassLoader() {
        if (!sclSet) {
            if (scl != null) {
                throw new IllegalStateException("recursive invocation");
            }
            Launcher launcher = Launcher.getLauncher();
            if (launcher != null) {
                Throwable cause = null;
                scl = launcher.getClassLoader();
                try {
                    scl = (ClassLoader) AccessController.doPrivileged(new SystemClassLoaderAction(scl));
                } catch (PrivilegedActionException e2) {
                    cause = e2.getCause();
                    if (cause instanceof InvocationTargetException) {
                        cause = cause.getCause();
                    }
                }
                if (cause != null) {
                    if (cause instanceof Error) {
                        throw ((Error) cause);
                    }
                    throw new Error(cause);
                }
            }
            sclSet = true;
        }
    }

    boolean isAncestor(ClassLoader classLoader) {
        ClassLoader classLoader2 = this;
        do {
            classLoader2 = classLoader2.parent;
            if (classLoader == classLoader2) {
                return true;
            }
        } while (classLoader2 != null);
        return false;
    }

    private static boolean needsClassLoaderPermissionCheck(ClassLoader classLoader, ClassLoader classLoader2) {
        return (classLoader == classLoader2 || classLoader == null || classLoader2.isAncestor(classLoader)) ? false : true;
    }

    static ClassLoader getClassLoader(Class<?> cls) {
        if (cls == null) {
            return null;
        }
        return cls.getClassLoader0();
    }

    static void checkClassLoaderPermission(ClassLoader classLoader, Class<?> cls) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null && needsClassLoaderPermissionCheck(getClassLoader(cls), classLoader)) {
            securityManager.checkPermission(SecurityConstants.GET_CLASSLOADER_PERMISSION);
        }
    }

    protected Package definePackage(String str, String str2, String str3, String str4, String str5, String str6, String str7, URL url) throws IllegalArgumentException {
        Package r0;
        synchronized (this.packages) {
            if (getPackage(str) != null) {
                throw new IllegalArgumentException(str);
            }
            r0 = new Package(str, str2, str3, str4, str5, str6, str7, url, this);
            this.packages.put(str, r0);
        }
        return r0;
    }

    protected Package getPackage(String str) {
        Package systemPackage;
        synchronized (this.packages) {
            systemPackage = this.packages.get(str);
        }
        if (systemPackage == null) {
            if (this.parent != null) {
                systemPackage = this.parent.getPackage(str);
            } else {
                systemPackage = Package.getSystemPackage(str);
            }
            if (systemPackage != null) {
                synchronized (this.packages) {
                    Package r0 = this.packages.get(str);
                    if (r0 == null) {
                        this.packages.put(str, systemPackage);
                    } else {
                        systemPackage = r0;
                    }
                }
            }
        }
        return systemPackage;
    }

    protected Package[] getPackages() {
        HashMap map;
        Package[] systemPackages;
        synchronized (this.packages) {
            map = new HashMap(this.packages);
        }
        if (this.parent != null) {
            systemPackages = this.parent.getPackages();
        } else {
            systemPackages = Package.getSystemPackages();
        }
        if (systemPackages != null) {
            for (int i2 = 0; i2 < systemPackages.length; i2++) {
                String name = systemPackages[i2].getName();
                if (map.get(name) == 0) {
                    map.put(name, systemPackages[i2]);
                }
            }
        }
        return (Package[]) map.values().toArray(new Package[map.size()]);
    }

    protected String findLibrary(String str) {
        return null;
    }

    /* loaded from: rt.jar:java/lang/ClassLoader$NativeLibrary.class */
    static class NativeLibrary {
        long handle;
        private int jniVersion;
        private final Class<?> fromClass;
        String name;
        boolean isBuiltin;
        boolean loaded;

        native void load(String str, boolean z2);

        native long find(String str);

        native void unload(String str, boolean z2);

        public NativeLibrary(Class<?> cls, String str, boolean z2) {
            this.name = str;
            this.fromClass = cls;
            this.isBuiltin = z2;
        }

        protected void finalize() {
            synchronized (ClassLoader.loadedLibraryNames) {
                if (this.fromClass.getClassLoader() != null && this.loaded) {
                    int size = ClassLoader.loadedLibraryNames.size();
                    int i2 = 0;
                    while (true) {
                        if (i2 >= size) {
                            break;
                        }
                        if (this.name.equals(ClassLoader.loadedLibraryNames.elementAt(i2))) {
                            ClassLoader.loadedLibraryNames.removeElementAt(i2);
                            break;
                        }
                        i2++;
                    }
                    ClassLoader.nativeLibraryContext.push(this);
                    try {
                        unload(this.name, this.isBuiltin);
                        ClassLoader.nativeLibraryContext.pop();
                    } catch (Throwable th) {
                        ClassLoader.nativeLibraryContext.pop();
                        throw th;
                    }
                }
            }
        }

        static Class<?> getFromClass() {
            return ((NativeLibrary) ClassLoader.nativeLibraryContext.peek()).fromClass;
        }
    }

    private static String[] initializePath(String str) {
        String property = System.getProperty(str, "");
        String str2 = File.pathSeparator;
        int length = property.length();
        int i2 = 0;
        for (int iIndexOf = property.indexOf(str2); iIndexOf >= 0; iIndexOf = property.indexOf(str2, iIndexOf + 1)) {
            i2++;
        }
        String[] strArr = new String[i2 + 1];
        int i3 = 0;
        int i4 = 0;
        int iIndexOf2 = property.indexOf(str2);
        while (true) {
            int i5 = iIndexOf2;
            if (i5 >= 0) {
                if (i5 - i3 > 0) {
                    int i6 = i4;
                    i4++;
                    strArr[i6] = property.substring(i3, i5);
                } else if (i5 - i3 == 0) {
                    int i7 = i4;
                    i4++;
                    strArr[i7] = ".";
                }
                i3 = i5 + 1;
                iIndexOf2 = property.indexOf(str2, i3);
            } else {
                strArr[i4] = property.substring(i3, length);
                return strArr;
            }
        }
    }

    static void loadLibrary(Class<?> cls, String str, boolean z2) {
        String strFindLibrary;
        ClassLoader classLoader = cls == null ? null : cls.getClassLoader();
        if (sys_paths == null) {
            usr_paths = initializePath("java.library.path");
            sys_paths = initializePath("sun.boot.library.path");
        }
        if (z2) {
            if (loadLibrary0(cls, new File(str))) {
                return;
            } else {
                throw new UnsatisfiedLinkError("Can't load library: " + str);
            }
        }
        if (classLoader != null && (strFindLibrary = classLoader.findLibrary(str)) != null) {
            File file = new File(strFindLibrary);
            if (!file.isAbsolute()) {
                throw new UnsatisfiedLinkError("ClassLoader.findLibrary failed to return an absolute path: " + strFindLibrary);
            }
            if (loadLibrary0(cls, file)) {
                return;
            } else {
                throw new UnsatisfiedLinkError("Can't load " + strFindLibrary);
            }
        }
        for (int i2 = 0; i2 < sys_paths.length; i2++) {
            File file2 = new File(sys_paths[i2], System.mapLibraryName(str));
            if (loadLibrary0(cls, file2)) {
                return;
            }
            File fileMapAlternativeName = ClassLoaderHelper.mapAlternativeName(file2);
            if (fileMapAlternativeName != null && loadLibrary0(cls, fileMapAlternativeName)) {
                return;
            }
        }
        if (classLoader != null) {
            for (int i3 = 0; i3 < usr_paths.length; i3++) {
                File file3 = new File(usr_paths[i3], System.mapLibraryName(str));
                if (loadLibrary0(cls, file3)) {
                    return;
                }
                File fileMapAlternativeName2 = ClassLoaderHelper.mapAlternativeName(file3);
                if (fileMapAlternativeName2 != null && loadLibrary0(cls, fileMapAlternativeName2)) {
                    return;
                }
            }
        }
        throw new UnsatisfiedLinkError("no " + str + " in java.library.path");
    }

    private static boolean loadLibrary0(Class<?> cls, final File file) {
        String strFindBuiltinLib = findBuiltinLib(file.getName());
        boolean z2 = strFindBuiltinLib != null;
        if (!z2) {
            if (!(AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: java.lang.ClassLoader.3
                @Override // java.security.PrivilegedAction
                public Object run() {
                    if (file.exists()) {
                        return Boolean.TRUE;
                    }
                    return null;
                }
            }) != null)) {
                return false;
            }
            try {
                strFindBuiltinLib = file.getCanonicalPath();
            } catch (IOException e2) {
                return false;
            }
        }
        ClassLoader classLoader = cls == null ? null : cls.getClassLoader();
        Vector<NativeLibrary> vector = classLoader != null ? classLoader.nativeLibraries : systemNativeLibraries;
        synchronized (vector) {
            int size = vector.size();
            for (int i2 = 0; i2 < size; i2++) {
                if (strFindBuiltinLib.equals(vector.elementAt(i2).name)) {
                    return true;
                }
            }
            synchronized (loadedLibraryNames) {
                if (loadedLibraryNames.contains(strFindBuiltinLib)) {
                    throw new UnsatisfiedLinkError("Native Library " + strFindBuiltinLib + " already loaded in another classloader");
                }
                int size2 = nativeLibraryContext.size();
                for (int i3 = 0; i3 < size2; i3++) {
                    NativeLibrary nativeLibraryElementAt = nativeLibraryContext.elementAt(i3);
                    if (strFindBuiltinLib.equals(nativeLibraryElementAt.name)) {
                        if (classLoader == nativeLibraryElementAt.fromClass.getClassLoader()) {
                            return true;
                        }
                        throw new UnsatisfiedLinkError("Native Library " + strFindBuiltinLib + " is being loaded in another classloader");
                    }
                }
                NativeLibrary nativeLibrary = new NativeLibrary(cls, strFindBuiltinLib, z2);
                nativeLibraryContext.push(nativeLibrary);
                try {
                    nativeLibrary.load(strFindBuiltinLib, z2);
                    nativeLibraryContext.pop();
                    if (nativeLibrary.loaded) {
                        loadedLibraryNames.addElement(strFindBuiltinLib);
                        vector.addElement(nativeLibrary);
                        return true;
                    }
                    return false;
                } catch (Throwable th) {
                    nativeLibraryContext.pop();
                    throw th;
                }
            }
        }
    }

    static long findNative(ClassLoader classLoader, String str) {
        Vector<NativeLibrary> vector = classLoader != null ? classLoader.nativeLibraries : systemNativeLibraries;
        synchronized (vector) {
            int size = vector.size();
            for (int i2 = 0; i2 < size; i2++) {
                long jFind = vector.elementAt(i2).find(str);
                if (jFind != 0) {
                    return jFind;
                }
            }
            return 0L;
        }
    }

    public void setDefaultAssertionStatus(boolean z2) {
        synchronized (this.assertionLock) {
            if (this.classAssertionStatus == null) {
                initializeJavaAssertionMaps();
            }
            this.defaultAssertionStatus = z2;
        }
    }

    public void setPackageAssertionStatus(String str, boolean z2) {
        synchronized (this.assertionLock) {
            if (this.packageAssertionStatus == null) {
                initializeJavaAssertionMaps();
            }
            this.packageAssertionStatus.put(str, Boolean.valueOf(z2));
        }
    }

    public void setClassAssertionStatus(String str, boolean z2) {
        synchronized (this.assertionLock) {
            if (this.classAssertionStatus == null) {
                initializeJavaAssertionMaps();
            }
            this.classAssertionStatus.put(str, Boolean.valueOf(z2));
        }
    }

    public void clearAssertionStatus() {
        synchronized (this.assertionLock) {
            this.classAssertionStatus = new HashMap();
            this.packageAssertionStatus = new HashMap();
            this.defaultAssertionStatus = false;
        }
    }

    boolean desiredAssertionStatus(String str) {
        Boolean bool;
        synchronized (this.assertionLock) {
            Boolean bool2 = this.classAssertionStatus.get(str);
            if (bool2 != null) {
                return bool2.booleanValue();
            }
            int iLastIndexOf = str.lastIndexOf(".");
            if (iLastIndexOf < 0 && (bool = this.packageAssertionStatus.get(null)) != null) {
                return bool.booleanValue();
            }
            while (iLastIndexOf > 0) {
                str = str.substring(0, iLastIndexOf);
                Boolean bool3 = this.packageAssertionStatus.get(str);
                if (bool3 != null) {
                    return bool3.booleanValue();
                }
                iLastIndexOf = str.lastIndexOf(".", iLastIndexOf - 1);
            }
            return this.defaultAssertionStatus;
        }
    }

    private void initializeJavaAssertionMaps() {
        this.classAssertionStatus = new HashMap();
        this.packageAssertionStatus = new HashMap();
        AssertionStatusDirectives assertionStatusDirectivesRetrieveDirectives = retrieveDirectives();
        for (int i2 = 0; i2 < assertionStatusDirectivesRetrieveDirectives.classes.length; i2++) {
            this.classAssertionStatus.put(assertionStatusDirectivesRetrieveDirectives.classes[i2], Boolean.valueOf(assertionStatusDirectivesRetrieveDirectives.classEnabled[i2]));
        }
        for (int i3 = 0; i3 < assertionStatusDirectivesRetrieveDirectives.packages.length; i3++) {
            this.packageAssertionStatus.put(assertionStatusDirectivesRetrieveDirectives.packages[i3], Boolean.valueOf(assertionStatusDirectivesRetrieveDirectives.packageEnabled[i3]));
        }
        this.defaultAssertionStatus = assertionStatusDirectivesRetrieveDirectives.deflt;
    }
}
