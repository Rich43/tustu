package sun.rmi.server;

import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.SocketPermission;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.rmi.server.LogStream;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.PropertyPermission;
import java.util.StringTokenizer;
import java.util.WeakHashMap;
import org.icepdf.core.util.PdfOps;
import sun.reflect.misc.ReflectUtil;
import sun.rmi.runtime.Log;
import sun.security.action.GetPropertyAction;
import sun.security.util.SecurityConstants;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:sun/rmi/server/LoaderHandler.class */
public final class LoaderHandler {
    static final int logLevel = LogStream.parseLevel((String) AccessController.doPrivileged(new GetPropertyAction("sun.rmi.loader.logLevel")));
    static final Log loaderLog = Log.getLog("sun.rmi.loader", "loader", logLevel);
    private static String codebaseProperty;
    private static URL[] codebaseURLs;
    private static final Map<ClassLoader, Void> codebaseLoaders;
    private static final HashMap<LoaderKey, LoaderEntry> loaderTable;
    private static final ReferenceQueue<Loader> refQueue;
    private static final Map<String, Object[]> pathToURLsCache;

    static {
        codebaseProperty = null;
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("java.rmi.server.codebase"));
        if (str != null && str.trim().length() > 0) {
            codebaseProperty = str;
        }
        codebaseURLs = null;
        codebaseLoaders = Collections.synchronizedMap(new IdentityHashMap(5));
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        while (true) {
            ClassLoader classLoader = systemClassLoader;
            if (classLoader != null) {
                codebaseLoaders.put(classLoader, null);
                systemClassLoader = classLoader.getParent();
            } else {
                loaderTable = new HashMap<>(5);
                refQueue = new ReferenceQueue<>();
                pathToURLsCache = new WeakHashMap(5);
                return;
            }
        }
    }

    private LoaderHandler() {
    }

    private static synchronized URL[] getDefaultCodebaseURLs() throws MalformedURLException {
        if (codebaseURLs == null) {
            if (codebaseProperty != null) {
                codebaseURLs = pathToURLs(codebaseProperty);
            } else {
                codebaseURLs = new URL[0];
            }
        }
        return codebaseURLs;
    }

    public static Class<?> loadClass(String str, String str2, ClassLoader classLoader) throws MalformedURLException, ClassNotFoundException {
        URL[] defaultCodebaseURLs;
        if (loaderLog.isLoggable(Log.BRIEF)) {
            loaderLog.log(Log.BRIEF, "name = \"" + str2 + "\", codebase = \"" + (str != null ? str : "") + PdfOps.DOUBLE_QUOTE__TOKEN + (classLoader != null ? ", defaultLoader = " + ((Object) classLoader) : ""));
        }
        if (str != null) {
            defaultCodebaseURLs = pathToURLs(str);
        } else {
            defaultCodebaseURLs = getDefaultCodebaseURLs();
        }
        if (classLoader != null) {
            try {
                Class<?> clsLoadClassForName = loadClassForName(str2, false, classLoader);
                if (loaderLog.isLoggable(Log.VERBOSE)) {
                    loaderLog.log(Log.VERBOSE, "class \"" + str2 + "\" found via defaultLoader, defined by " + ((Object) clsLoadClassForName.getClassLoader()));
                }
                return clsLoadClassForName;
            } catch (ClassNotFoundException e2) {
            }
        }
        return loadClass(defaultCodebaseURLs, str2);
    }

    public static String getClassAnnotation(Class<?> cls) {
        String name = cls.getName();
        int length = name.length();
        if (length > 0 && name.charAt(0) == '[') {
            int i2 = 1;
            while (length > i2 && name.charAt(i2) == '[') {
                i2++;
            }
            if (length > i2 && name.charAt(i2) != 'L') {
                return null;
            }
        }
        ClassLoader classLoader = cls.getClassLoader();
        if (classLoader == null || codebaseLoaders.containsKey(classLoader)) {
            return codebaseProperty;
        }
        String strUrlsToPath = null;
        if (classLoader instanceof Loader) {
            strUrlsToPath = ((Loader) classLoader).getClassAnnotation();
        } else if (classLoader instanceof URLClassLoader) {
            try {
                URL[] uRLs = ((URLClassLoader) classLoader).getURLs();
                if (uRLs != null) {
                    SecurityManager securityManager = System.getSecurityManager();
                    if (securityManager != null) {
                        Permissions permissions = new Permissions();
                        for (URL url : uRLs) {
                            Permission permission = url.openConnection().getPermission();
                            if (permission != null && !permissions.implies(permission)) {
                                securityManager.checkPermission(permission);
                                permissions.add(permission);
                            }
                        }
                    }
                    strUrlsToPath = urlsToPath(uRLs);
                }
            } catch (IOException | SecurityException e2) {
            }
        }
        if (strUrlsToPath != null) {
            return strUrlsToPath;
        }
        return codebaseProperty;
    }

    public static ClassLoader getClassLoader(String str) throws MalformedURLException {
        URL[] defaultCodebaseURLs;
        ClassLoader rMIContextClassLoader = getRMIContextClassLoader();
        if (str != null) {
            defaultCodebaseURLs = pathToURLs(str);
        } else {
            defaultCodebaseURLs = getDefaultCodebaseURLs();
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("getClassLoader"));
            Loader loaderLookupLoader = lookupLoader(defaultCodebaseURLs, rMIContextClassLoader);
            if (loaderLookupLoader != null) {
                loaderLookupLoader.checkPermissions();
            }
            return loaderLookupLoader;
        }
        return rMIContextClassLoader;
    }

    public static Object getSecurityContext(ClassLoader classLoader) {
        if (classLoader instanceof Loader) {
            URL[] uRLs = ((Loader) classLoader).getURLs();
            if (uRLs.length > 0) {
                return uRLs[0];
            }
            return null;
        }
        return null;
    }

    public static void registerCodebaseLoader(ClassLoader classLoader) {
        codebaseLoaders.put(classLoader, null);
    }

    private static Class<?> loadClass(URL[] urlArr, String str) throws ClassNotFoundException {
        ClassLoader rMIContextClassLoader = getRMIContextClassLoader();
        if (loaderLog.isLoggable(Log.VERBOSE)) {
            loaderLog.log(Log.VERBOSE, "(thread context class loader: " + ((Object) rMIContextClassLoader) + ")");
        }
        if (System.getSecurityManager() == null) {
            try {
                Class<?> cls = Class.forName(str, false, rMIContextClassLoader);
                if (loaderLog.isLoggable(Log.VERBOSE)) {
                    loaderLog.log(Log.VERBOSE, "class \"" + str + "\" found via thread context class loader (no security manager: codebase disabled), defined by " + ((Object) cls.getClassLoader()));
                }
                return cls;
            } catch (ClassNotFoundException e2) {
                if (loaderLog.isLoggable(Log.BRIEF)) {
                    loaderLog.log(Log.BRIEF, "class \"" + str + "\" not found via thread context class loader (no security manager: codebase disabled)", e2);
                }
                throw new ClassNotFoundException(e2.getMessage() + " (no security manager: RMI class loader disabled)", e2.getException());
            }
        }
        Loader loaderLookupLoader = lookupLoader(urlArr, rMIContextClassLoader);
        if (loaderLookupLoader != null) {
            try {
                loaderLookupLoader.checkPermissions();
            } catch (SecurityException e3) {
                try {
                    Class<?> clsLoadClassForName = loadClassForName(str, false, rMIContextClassLoader);
                    if (loaderLog.isLoggable(Log.VERBOSE)) {
                        loaderLog.log(Log.VERBOSE, "class \"" + str + "\" found via thread context class loader (access to codebase denied), defined by " + ((Object) clsLoadClassForName.getClassLoader()));
                    }
                    return clsLoadClassForName;
                } catch (ClassNotFoundException e4) {
                    if (loaderLog.isLoggable(Log.BRIEF)) {
                        loaderLog.log(Log.BRIEF, "class \"" + str + "\" not found via thread context class loader (access to codebase denied)", e3);
                    }
                    throw new ClassNotFoundException("access to class loader denied", e3);
                }
            }
        }
        try {
            Class<?> clsLoadClassForName2 = loadClassForName(str, false, loaderLookupLoader);
            if (loaderLog.isLoggable(Log.VERBOSE)) {
                loaderLog.log(Log.VERBOSE, "class \"" + str + "\" found via codebase, defined by " + ((Object) clsLoadClassForName2.getClassLoader()));
            }
            return clsLoadClassForName2;
        } catch (ClassNotFoundException e5) {
            if (loaderLog.isLoggable(Log.BRIEF)) {
                loaderLog.log(Log.BRIEF, "class \"" + str + "\" not found via codebase", e5);
            }
            throw e5;
        }
    }

    public static Class<?> loadProxyClass(String str, String[] strArr, ClassLoader classLoader) throws MalformedURLException, ClassNotFoundException {
        URL[] defaultCodebaseURLs;
        if (loaderLog.isLoggable(Log.BRIEF)) {
            loaderLog.log(Log.BRIEF, "interfaces = " + ((Object) Arrays.asList(strArr)) + ", codebase = \"" + (str != null ? str : "") + PdfOps.DOUBLE_QUOTE__TOKEN + (classLoader != null ? ", defaultLoader = " + ((Object) classLoader) : ""));
        }
        ClassLoader rMIContextClassLoader = getRMIContextClassLoader();
        if (loaderLog.isLoggable(Log.VERBOSE)) {
            loaderLog.log(Log.VERBOSE, "(thread context class loader: " + ((Object) rMIContextClassLoader) + ")");
        }
        if (str != null) {
            defaultCodebaseURLs = pathToURLs(str);
        } else {
            defaultCodebaseURLs = getDefaultCodebaseURLs();
        }
        if (System.getSecurityManager() == null) {
            try {
                Class<?> clsLoadProxyClass = loadProxyClass(strArr, classLoader, rMIContextClassLoader, false);
                if (loaderLog.isLoggable(Log.VERBOSE)) {
                    loaderLog.log(Log.VERBOSE, "(no security manager: codebase disabled) proxy class defined by " + ((Object) clsLoadProxyClass.getClassLoader()));
                }
                return clsLoadProxyClass;
            } catch (ClassNotFoundException e2) {
                if (loaderLog.isLoggable(Log.BRIEF)) {
                    loaderLog.log(Log.BRIEF, "(no security manager: codebase disabled) proxy class resolution failed", e2);
                }
                throw new ClassNotFoundException(e2.getMessage() + " (no security manager: RMI class loader disabled)", e2.getException());
            }
        }
        Loader loaderLookupLoader = lookupLoader(defaultCodebaseURLs, rMIContextClassLoader);
        if (loaderLookupLoader != null) {
            try {
                loaderLookupLoader.checkPermissions();
            } catch (SecurityException e3) {
                try {
                    Class<?> clsLoadProxyClass2 = loadProxyClass(strArr, classLoader, rMIContextClassLoader, false);
                    if (loaderLog.isLoggable(Log.VERBOSE)) {
                        loaderLog.log(Log.VERBOSE, "(access to codebase denied) proxy class defined by " + ((Object) clsLoadProxyClass2.getClassLoader()));
                    }
                    return clsLoadProxyClass2;
                } catch (ClassNotFoundException e4) {
                    if (loaderLog.isLoggable(Log.BRIEF)) {
                        loaderLog.log(Log.BRIEF, "(access to codebase denied) proxy class resolution failed", e3);
                    }
                    throw new ClassNotFoundException("access to class loader denied", e3);
                }
            }
        }
        try {
            Class<?> clsLoadProxyClass3 = loadProxyClass(strArr, classLoader, loaderLookupLoader, true);
            if (loaderLog.isLoggable(Log.VERBOSE)) {
                loaderLog.log(Log.VERBOSE, "proxy class defined by " + ((Object) clsLoadProxyClass3.getClassLoader()));
            }
            return clsLoadProxyClass3;
        } catch (ClassNotFoundException e5) {
            if (loaderLog.isLoggable(Log.BRIEF)) {
                loaderLog.log(Log.BRIEF, "proxy class resolution failed", e5);
            }
            throw e5;
        }
    }

    private static Class<?> loadProxyClass(String[] strArr, ClassLoader classLoader, ClassLoader classLoader2, boolean z2) throws ClassNotFoundException {
        Class[] clsArr = new Class[strArr.length];
        boolean[] zArr = {false};
        if (classLoader != null) {
            try {
                ClassLoader classLoaderLoadProxyInterfaces = loadProxyInterfaces(strArr, classLoader, clsArr, zArr);
                if (loaderLog.isLoggable(Log.VERBOSE)) {
                    ClassLoader[] classLoaderArr = new ClassLoader[clsArr.length];
                    for (int i2 = 0; i2 < classLoaderArr.length; i2++) {
                        classLoaderArr[i2] = clsArr[i2].getClassLoader();
                    }
                    loaderLog.log(Log.VERBOSE, "proxy interfaces found via defaultLoader, defined by " + ((Object) Arrays.asList(classLoaderArr)));
                }
                if (!zArr[0]) {
                    if (z2) {
                        try {
                            return Proxy.getProxyClass(classLoader2, clsArr);
                        } catch (IllegalArgumentException e2) {
                        }
                    }
                    classLoaderLoadProxyInterfaces = classLoader;
                }
                return loadProxyClass(classLoaderLoadProxyInterfaces, clsArr);
            } catch (ClassNotFoundException e3) {
            }
        }
        zArr[0] = false;
        ClassLoader classLoaderLoadProxyInterfaces2 = loadProxyInterfaces(strArr, classLoader2, clsArr, zArr);
        if (loaderLog.isLoggable(Log.VERBOSE)) {
            ClassLoader[] classLoaderArr2 = new ClassLoader[clsArr.length];
            for (int i3 = 0; i3 < classLoaderArr2.length; i3++) {
                classLoaderArr2[i3] = clsArr[i3].getClassLoader();
            }
            loaderLog.log(Log.VERBOSE, "proxy interfaces found via codebase, defined by " + ((Object) Arrays.asList(classLoaderArr2)));
        }
        if (!zArr[0]) {
            classLoaderLoadProxyInterfaces2 = classLoader2;
        }
        return loadProxyClass(classLoaderLoadProxyInterfaces2, clsArr);
    }

    private static Class<?> loadProxyClass(ClassLoader classLoader, Class<?>[] clsArr) throws ClassNotFoundException {
        try {
            return Proxy.getProxyClass(classLoader, clsArr);
        } catch (IllegalArgumentException e2) {
            throw new ClassNotFoundException("error creating dynamic proxy class", e2);
        }
    }

    private static ClassLoader loadProxyInterfaces(String[] strArr, ClassLoader classLoader, Class<?>[] clsArr, boolean[] zArr) throws ClassNotFoundException {
        ClassLoader classLoader2 = null;
        for (int i2 = 0; i2 < strArr.length; i2++) {
            Class<?> clsLoadClassForName = loadClassForName(strArr[i2], false, classLoader);
            clsArr[i2] = clsLoadClassForName;
            if (!Modifier.isPublic(clsLoadClassForName.getModifiers())) {
                ClassLoader classLoader3 = clsLoadClassForName.getClassLoader();
                if (loaderLog.isLoggable(Log.VERBOSE)) {
                    loaderLog.log(Log.VERBOSE, "non-public interface \"" + strArr[i2] + "\" defined by " + ((Object) classLoader3));
                }
                if (!zArr[0]) {
                    classLoader2 = classLoader3;
                    zArr[0] = true;
                } else if (classLoader3 != classLoader2) {
                    throw new IllegalAccessError("non-public interfaces defined in different class loaders");
                }
            }
        }
        return classLoader2;
    }

    private static URL[] pathToURLs(String str) throws MalformedURLException {
        synchronized (pathToURLsCache) {
            Object[] objArr = pathToURLsCache.get(str);
            if (objArr != null) {
                return (URL[]) objArr[0];
            }
            StringTokenizer stringTokenizer = new StringTokenizer(str);
            URL[] urlArr = new URL[stringTokenizer.countTokens()];
            int i2 = 0;
            while (stringTokenizer.hasMoreTokens()) {
                urlArr[i2] = new URL(stringTokenizer.nextToken());
                i2++;
            }
            synchronized (pathToURLsCache) {
                pathToURLsCache.put(str, new Object[]{urlArr, new SoftReference(str)});
            }
            return urlArr;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String urlsToPath(URL[] urlArr) {
        if (urlArr.length == 0) {
            return null;
        }
        if (urlArr.length == 1) {
            return urlArr[0].toExternalForm();
        }
        StringBuffer stringBuffer = new StringBuffer(urlArr[0].toExternalForm());
        for (int i2 = 1; i2 < urlArr.length; i2++) {
            stringBuffer.append(' ');
            stringBuffer.append(urlArr[i2].toExternalForm());
        }
        return stringBuffer.toString();
    }

    private static ClassLoader getRMIContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0050  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static sun.rmi.server.LoaderHandler.Loader lookupLoader(final java.net.URL[] r5, final java.lang.ClassLoader r6) {
        /*
            java.lang.Class<sun.rmi.server.LoaderHandler> r0 = sun.rmi.server.LoaderHandler.class
            r1 = r0
            r9 = r1
            monitor-enter(r0)
        L6:
            java.lang.ref.ReferenceQueue<sun.rmi.server.LoaderHandler$Loader> r0 = sun.rmi.server.LoaderHandler.refQueue     // Catch: java.lang.Throwable -> L95
            java.lang.ref.Reference r0 = r0.poll()     // Catch: java.lang.Throwable -> L95
            sun.rmi.server.LoaderHandler$LoaderEntry r0 = (sun.rmi.server.LoaderHandler.LoaderEntry) r0     // Catch: java.lang.Throwable -> L95
            r1 = r0
            r7 = r1
            if (r0 == 0) goto L29
            r0 = r7
            boolean r0 = r0.removed     // Catch: java.lang.Throwable -> L95
            if (r0 != 0) goto L6
            java.util.HashMap<sun.rmi.server.LoaderHandler$LoaderKey, sun.rmi.server.LoaderHandler$LoaderEntry> r0 = sun.rmi.server.LoaderHandler.loaderTable     // Catch: java.lang.Throwable -> L95
            r1 = r7
            sun.rmi.server.LoaderHandler$LoaderKey r1 = r1.key     // Catch: java.lang.Throwable -> L95
            java.lang.Object r0 = r0.remove(r1)     // Catch: java.lang.Throwable -> L95
            goto L6
        L29:
            sun.rmi.server.LoaderHandler$LoaderKey r0 = new sun.rmi.server.LoaderHandler$LoaderKey     // Catch: java.lang.Throwable -> L95
            r1 = r0
            r2 = r5
            r3 = r6
            r1.<init>(r2, r3)     // Catch: java.lang.Throwable -> L95
            r10 = r0
            java.util.HashMap<sun.rmi.server.LoaderHandler$LoaderKey, sun.rmi.server.LoaderHandler$LoaderEntry> r0 = sun.rmi.server.LoaderHandler.loaderTable     // Catch: java.lang.Throwable -> L95
            r1 = r10
            java.lang.Object r0 = r0.get(r1)     // Catch: java.lang.Throwable -> L95
            sun.rmi.server.LoaderHandler$LoaderEntry r0 = (sun.rmi.server.LoaderHandler.LoaderEntry) r0     // Catch: java.lang.Throwable -> L95
            r7 = r0
            r0 = r7
            if (r0 == 0) goto L50
            r0 = r7
            java.lang.Object r0 = r0.get()     // Catch: java.lang.Throwable -> L95
            sun.rmi.server.LoaderHandler$Loader r0 = (sun.rmi.server.LoaderHandler.Loader) r0     // Catch: java.lang.Throwable -> L95
            r1 = r0
            r8 = r1
            if (r0 != 0) goto L8f
        L50:
            r0 = r7
            if (r0 == 0) goto L62
            java.util.HashMap<sun.rmi.server.LoaderHandler$LoaderKey, sun.rmi.server.LoaderHandler$LoaderEntry> r0 = sun.rmi.server.LoaderHandler.loaderTable     // Catch: java.lang.Throwable -> L95
            r1 = r10
            java.lang.Object r0 = r0.remove(r1)     // Catch: java.lang.Throwable -> L95
            r0 = r7
            r1 = 1
            r0.removed = r1     // Catch: java.lang.Throwable -> L95
        L62:
            r0 = r5
            java.security.AccessControlContext r0 = getLoaderAccessControlContext(r0)     // Catch: java.lang.Throwable -> L95
            r11 = r0
            sun.rmi.server.LoaderHandler$1 r0 = new sun.rmi.server.LoaderHandler$1     // Catch: java.lang.Throwable -> L95
            r1 = r0
            r2 = r5
            r3 = r6
            r1.<init>()     // Catch: java.lang.Throwable -> L95
            r1 = r11
            java.lang.Object r0 = java.security.AccessController.doPrivileged(r0, r1)     // Catch: java.lang.Throwable -> L95
            sun.rmi.server.LoaderHandler$Loader r0 = (sun.rmi.server.LoaderHandler.Loader) r0     // Catch: java.lang.Throwable -> L95
            r8 = r0
            sun.rmi.server.LoaderHandler$LoaderEntry r0 = new sun.rmi.server.LoaderHandler$LoaderEntry     // Catch: java.lang.Throwable -> L95
            r1 = r0
            r2 = r10
            r3 = r8
            r1.<init>(r2, r3)     // Catch: java.lang.Throwable -> L95
            r7 = r0
            java.util.HashMap<sun.rmi.server.LoaderHandler$LoaderKey, sun.rmi.server.LoaderHandler$LoaderEntry> r0 = sun.rmi.server.LoaderHandler.loaderTable     // Catch: java.lang.Throwable -> L95
            r1 = r10
            r2 = r7
            java.lang.Object r0 = r0.put(r1, r2)     // Catch: java.lang.Throwable -> L95
        L8f:
            r0 = r9
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L95
            goto L9d
        L95:
            r12 = move-exception
            r0 = r9
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L95
            r0 = r12
            throw r0
        L9d:
            r0 = r8
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.rmi.server.LoaderHandler.lookupLoader(java.net.URL[], java.lang.ClassLoader):sun.rmi.server.LoaderHandler$Loader");
    }

    /* loaded from: rt.jar:sun/rmi/server/LoaderHandler$LoaderKey.class */
    private static class LoaderKey {
        private URL[] urls;
        private ClassLoader parent;
        private int hashValue;

        public LoaderKey(URL[] urlArr, ClassLoader classLoader) {
            this.urls = urlArr;
            this.parent = classLoader;
            if (classLoader != null) {
                this.hashValue = classLoader.hashCode();
            }
            for (URL url : urlArr) {
                this.hashValue ^= url.hashCode();
            }
        }

        public int hashCode() {
            return this.hashValue;
        }

        public boolean equals(Object obj) {
            if (obj instanceof LoaderKey) {
                LoaderKey loaderKey = (LoaderKey) obj;
                if (this.parent != loaderKey.parent) {
                    return false;
                }
                if (this.urls == loaderKey.urls) {
                    return true;
                }
                if (this.urls.length != loaderKey.urls.length) {
                    return false;
                }
                for (int i2 = 0; i2 < this.urls.length; i2++) {
                    if (!this.urls[i2].equals(loaderKey.urls[i2])) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
    }

    /* loaded from: rt.jar:sun/rmi/server/LoaderHandler$LoaderEntry.class */
    private static class LoaderEntry extends WeakReference<Loader> {
        public LoaderKey key;
        public boolean removed;

        public LoaderEntry(LoaderKey loaderKey, Loader loader) {
            super(loader, LoaderHandler.refQueue);
            this.removed = false;
            this.key = loaderKey;
        }
    }

    private static AccessControlContext getLoaderAccessControlContext(URL[] urlArr) {
        PermissionCollection permissionCollection = (PermissionCollection) AccessController.doPrivileged(new PrivilegedAction<PermissionCollection>() { // from class: sun.rmi.server.LoaderHandler.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public PermissionCollection run2() {
                CodeSource codeSource = new CodeSource((URL) null, (Certificate[]) null);
                Policy policy = Policy.getPolicy();
                if (policy != null) {
                    return policy.getPermissions(codeSource);
                }
                return new Permissions();
            }
        });
        permissionCollection.add(new RuntimePermission("createClassLoader"));
        permissionCollection.add(new PropertyPermission("java.*", "read"));
        addPermissionsForURLs(urlArr, permissionCollection, true);
        return new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(new CodeSource(urlArr.length > 0 ? urlArr[0] : null, (Certificate[]) null), permissionCollection)});
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void addPermissionsForURLs(URL[] urlArr, PermissionCollection permissionCollection, boolean z2) {
        for (URL url : urlArr) {
            try {
                URLConnection uRLConnectionOpenConnection = url.openConnection();
                Permission permission = uRLConnectionOpenConnection.getPermission();
                if (permission != null) {
                    if (permission instanceof FilePermission) {
                        String name = permission.getName();
                        int iLastIndexOf = name.lastIndexOf(File.separatorChar);
                        if (iLastIndexOf != -1) {
                            String strSubstring = name.substring(0, iLastIndexOf + 1);
                            if (strSubstring.endsWith(File.separator)) {
                                strSubstring = strSubstring + LanguageTag.SEP;
                            }
                            FilePermission filePermission = new FilePermission(strSubstring, "read");
                            if (!permissionCollection.implies(filePermission)) {
                                permissionCollection.add(filePermission);
                            }
                            permissionCollection.add(new FilePermission(strSubstring, "read"));
                        } else if (!permissionCollection.implies(permission)) {
                            permissionCollection.add(permission);
                        }
                    } else {
                        if (!permissionCollection.implies(permission)) {
                            permissionCollection.add(permission);
                        }
                        if (z2) {
                            URL jarFileURL = url;
                            URLConnection uRLConnectionOpenConnection2 = uRLConnectionOpenConnection;
                            while (uRLConnectionOpenConnection2 instanceof JarURLConnection) {
                                jarFileURL = ((JarURLConnection) uRLConnectionOpenConnection2).getJarFileURL();
                                uRLConnectionOpenConnection2 = jarFileURL.openConnection();
                            }
                            String host = jarFileURL.getHost();
                            if (host != null && permission.implies(new SocketPermission(host, SecurityConstants.SOCKET_RESOLVE_ACTION))) {
                                SocketPermission socketPermission = new SocketPermission(host, SecurityConstants.SOCKET_CONNECT_ACCEPT_ACTION);
                                if (!permissionCollection.implies(socketPermission)) {
                                    permissionCollection.add(socketPermission);
                                }
                            }
                        }
                    }
                }
            } catch (IOException e2) {
            }
        }
    }

    /* loaded from: rt.jar:sun/rmi/server/LoaderHandler$Loader.class */
    private static class Loader extends URLClassLoader {
        private ClassLoader parent;
        private String annotation;
        private Permissions permissions;

        private Loader(URL[] urlArr, ClassLoader classLoader) {
            super(urlArr, classLoader);
            this.parent = classLoader;
            this.permissions = new Permissions();
            LoaderHandler.addPermissionsForURLs(urlArr, this.permissions, false);
            this.annotation = LoaderHandler.urlsToPath(urlArr);
        }

        public String getClassAnnotation() {
            return this.annotation;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void checkPermissions() {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                Enumeration<Permission> enumerationElements = this.permissions.elements();
                while (enumerationElements.hasMoreElements()) {
                    securityManager.checkPermission(enumerationElements.nextElement2());
                }
            }
        }

        @Override // java.net.URLClassLoader, java.security.SecureClassLoader
        protected PermissionCollection getPermissions(CodeSource codeSource) {
            return super.getPermissions(codeSource);
        }

        public String toString() {
            return super.toString() + "[\"" + this.annotation + "\"]";
        }

        @Override // java.lang.ClassLoader
        protected Class<?> loadClass(String str, boolean z2) throws ClassNotFoundException {
            if (this.parent == null) {
                ReflectUtil.checkPackageAccess(str);
            }
            return super.loadClass(str, z2);
        }
    }

    private static Class<?> loadClassForName(String str, boolean z2, ClassLoader classLoader) throws ClassNotFoundException {
        if (classLoader == null) {
            ReflectUtil.checkPackageAccess(str);
        }
        return Class.forName(str, z2, classLoader);
    }
}
