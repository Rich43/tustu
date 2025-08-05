package sun.misc;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.nio.file.Paths;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.Vector;
import sun.net.www.ParseUtil;
import sun.nio.fs.DefaultFileSystemProvider;

/* loaded from: rt.jar:sun/misc/Launcher.class */
public class Launcher {
    private static URLStreamHandlerFactory factory = new Factory();
    private static Launcher launcher = new Launcher();
    private static String bootClassPath = System.getProperty("sun.boot.class.path");
    private ClassLoader loader;
    private static URLStreamHandler fileHandler;

    public static Launcher getLauncher() {
        return launcher;
    }

    public Launcher() {
        try {
            try {
                this.loader = AppClassLoader.getAppClassLoader(ExtClassLoader.getExtClassLoader());
                Thread.currentThread().setContextClassLoader(this.loader);
                String property = System.getProperty("java.security.manager");
                if (property != null) {
                    DefaultFileSystemProvider.create();
                    SecurityManager securityManager = null;
                    if ("".equals(property) || "default".equals(property)) {
                        securityManager = new SecurityManager();
                    } else {
                        try {
                            securityManager = (SecurityManager) this.loader.loadClass(property).newInstance();
                        } catch (ClassCastException e2) {
                        } catch (ClassNotFoundException e3) {
                        } catch (IllegalAccessException e4) {
                        } catch (InstantiationException e5) {
                        }
                    }
                    if (securityManager == null) {
                        throw new InternalError("Could not create SecurityManager: " + property);
                    }
                    System.setSecurityManager(securityManager);
                }
            } catch (IOException e6) {
                throw new InternalError("Could not create application class loader", e6);
            }
        } catch (IOException e7) {
            throw new InternalError("Could not create extension class loader", e7);
        }
    }

    public ClassLoader getClassLoader() {
        return this.loader;
    }

    /* loaded from: rt.jar:sun/misc/Launcher$ExtClassLoader.class */
    static class ExtClassLoader extends URLClassLoader {
        private static volatile ExtClassLoader instance;

        static {
            ClassLoader.registerAsParallelCapable();
            instance = null;
        }

        public static ExtClassLoader getExtClassLoader() throws IOException {
            if (instance == null) {
                synchronized (ExtClassLoader.class) {
                    if (instance == null) {
                        instance = createExtClassLoader();
                    }
                }
            }
            return instance;
        }

        private static ExtClassLoader createExtClassLoader() throws IOException {
            try {
                return (ExtClassLoader) AccessController.doPrivileged(new PrivilegedExceptionAction<ExtClassLoader>() { // from class: sun.misc.Launcher.ExtClassLoader.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public ExtClassLoader run() throws IOException {
                        File[] extDirs = ExtClassLoader.getExtDirs();
                        for (File file : extDirs) {
                            MetaIndex.registerDirectory(file);
                        }
                        return new ExtClassLoader(extDirs);
                    }
                });
            } catch (PrivilegedActionException e2) {
                throw ((IOException) e2.getException());
            }
        }

        void addExtURL(URL url) {
            super.addURL(url);
        }

        public ExtClassLoader(File[] fileArr) throws IOException {
            super(getExtURLs(fileArr), (ClassLoader) null, Launcher.factory);
            SharedSecrets.getJavaNetAccess().getURLClassPath(this).initLookupCache(this);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static File[] getExtDirs() {
            File[] fileArr;
            String property = System.getProperty("java.ext.dirs");
            if (property != null) {
                StringTokenizer stringTokenizer = new StringTokenizer(property, File.pathSeparator);
                int iCountTokens = stringTokenizer.countTokens();
                fileArr = new File[iCountTokens];
                for (int i2 = 0; i2 < iCountTokens; i2++) {
                    fileArr[i2] = new File(stringTokenizer.nextToken());
                }
            } else {
                fileArr = new File[0];
            }
            return fileArr;
        }

        private static URL[] getExtURLs(File[] fileArr) throws IOException {
            Vector vector = new Vector();
            for (int i2 = 0; i2 < fileArr.length; i2++) {
                String[] list = fileArr[i2].list();
                if (list != null) {
                    for (int i3 = 0; i3 < list.length; i3++) {
                        if (!list[i3].equals("meta-index")) {
                            vector.add(Launcher.getFileURL(new File(fileArr[i2], list[i3])));
                        }
                    }
                }
            }
            URL[] urlArr = new URL[vector.size()];
            vector.copyInto(urlArr);
            return urlArr;
        }

        @Override // java.lang.ClassLoader
        public String findLibrary(String str) {
            String strMapLibraryName = System.mapLibraryName(str);
            Object obj = null;
            for (URL url : super.getURLs()) {
                try {
                    File parentFile = Paths.get(url.toURI()).toFile().getParentFile();
                    if (parentFile != null && !parentFile.equals(obj)) {
                        String savedProperty = VM.getSavedProperty("os.arch");
                        if (savedProperty != null) {
                            File file = new File(new File(parentFile, savedProperty), strMapLibraryName);
                            if (file.exists()) {
                                return file.getAbsolutePath();
                            }
                        }
                        File file2 = new File(parentFile, strMapLibraryName);
                        if (file2.exists()) {
                            return file2.getAbsolutePath();
                        }
                    }
                    obj = parentFile;
                } catch (URISyntaxException e2) {
                }
            }
            return null;
        }

        private static AccessControlContext getContext(File[] fileArr) throws IOException {
            PathPermissions pathPermissions = new PathPermissions(fileArr);
            return new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(new CodeSource(pathPermissions.getCodeBase(), (Certificate[]) null), pathPermissions)});
        }
    }

    /* loaded from: rt.jar:sun/misc/Launcher$AppClassLoader.class */
    static class AppClassLoader extends URLClassLoader {
        final URLClassPath ucp;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Launcher.class.desiredAssertionStatus();
            ClassLoader.registerAsParallelCapable();
        }

        public static ClassLoader getAppClassLoader(final ClassLoader classLoader) throws IOException {
            final String property = System.getProperty("java.class.path");
            final File[] classPath = property == null ? new File[0] : Launcher.getClassPath(property);
            return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<AppClassLoader>() { // from class: sun.misc.Launcher.AppClassLoader.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public AppClassLoader run2() {
                    return new AppClassLoader(property == null ? new URL[0] : Launcher.pathToURLs(classPath), classLoader);
                }
            });
        }

        AppClassLoader(URL[] urlArr, ClassLoader classLoader) {
            super(urlArr, classLoader, Launcher.factory);
            this.ucp = SharedSecrets.getJavaNetAccess().getURLClassPath(this);
            this.ucp.initLookupCache(this);
        }

        @Override // java.lang.ClassLoader
        public Class<?> loadClass(String str, boolean z2) throws ClassNotFoundException {
            SecurityManager securityManager;
            int iLastIndexOf = str.lastIndexOf(46);
            if (iLastIndexOf != -1 && (securityManager = System.getSecurityManager()) != null) {
                securityManager.checkPackageAccess(str.substring(0, iLastIndexOf));
            }
            if (this.ucp.knownToNotExist(str)) {
                Class<?> clsFindLoadedClass = findLoadedClass(str);
                if (clsFindLoadedClass != null) {
                    if (z2) {
                        resolveClass(clsFindLoadedClass);
                    }
                    return clsFindLoadedClass;
                }
                throw new ClassNotFoundException(str);
            }
            return super.loadClass(str, z2);
        }

        @Override // java.net.URLClassLoader, java.security.SecureClassLoader
        protected PermissionCollection getPermissions(CodeSource codeSource) {
            PermissionCollection permissions = super.getPermissions(codeSource);
            permissions.add(new RuntimePermission("exitVM"));
            return permissions;
        }

        private void appendToClassPathForInstrumentation(String str) {
            if (!$assertionsDisabled && !Thread.holdsLock(this)) {
                throw new AssertionError();
            }
            super.addURL(Launcher.getFileURL(new File(str)));
        }

        private static AccessControlContext getContext(File[] fileArr) throws MalformedURLException {
            PathPermissions pathPermissions = new PathPermissions(fileArr);
            return new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(new CodeSource(pathPermissions.getCodeBase(), (Certificate[]) null), pathPermissions)});
        }
    }

    /* loaded from: rt.jar:sun/misc/Launcher$BootClassPathHolder.class */
    private static class BootClassPathHolder {
        static final URLClassPath bcp;

        private BootClassPathHolder() {
        }

        static {
            URL[] urlArr;
            if (Launcher.bootClassPath != null) {
                urlArr = (URL[]) AccessController.doPrivileged(new PrivilegedAction<URL[]>() { // from class: sun.misc.Launcher.BootClassPathHolder.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public URL[] run2() {
                        File[] classPath = Launcher.getClassPath(Launcher.bootClassPath);
                        int length = classPath.length;
                        HashSet hashSet = new HashSet();
                        for (int i2 = 0; i2 < length; i2++) {
                            File parentFile = classPath[i2];
                            if (!parentFile.isDirectory()) {
                                parentFile = parentFile.getParentFile();
                            }
                            if (parentFile != null && hashSet.add(parentFile)) {
                                MetaIndex.registerDirectory(parentFile);
                            }
                        }
                        return Launcher.pathToURLs(classPath);
                    }
                });
            } else {
                urlArr = new URL[0];
            }
            bcp = new URLClassPath(urlArr, Launcher.factory, null);
            bcp.initLookupCache(null);
        }
    }

    public static URLClassPath getBootstrapClassPath() {
        return BootClassPathHolder.bcp;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static URL[] pathToURLs(File[] fileArr) {
        URL[] urlArr = new URL[fileArr.length];
        for (int i2 = 0; i2 < fileArr.length; i2++) {
            urlArr[i2] = getFileURL(fileArr[i2]);
        }
        return urlArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static File[] getClassPath(String str) {
        File[] fileArr;
        int i2;
        int i3;
        if (str != null) {
            int i4 = 0;
            int i5 = 1;
            int i6 = 0;
            while (true) {
                int iIndexOf = str.indexOf(File.pathSeparator, i6);
                if (iIndexOf == -1) {
                    break;
                }
                i5++;
                i6 = iIndexOf + 1;
            }
            fileArr = new File[i5];
            int i7 = 0;
            while (true) {
                i2 = i7;
                int iIndexOf2 = str.indexOf(File.pathSeparator, i2);
                if (iIndexOf2 == -1) {
                    break;
                }
                if (iIndexOf2 - i2 > 0) {
                    int i8 = i4;
                    i4++;
                    fileArr[i8] = new File(str.substring(i2, iIndexOf2));
                } else {
                    int i9 = i4;
                    i4++;
                    fileArr[i9] = new File(".");
                }
                i7 = iIndexOf2 + 1;
            }
            if (i2 < str.length()) {
                int i10 = i4;
                i3 = i4 + 1;
                fileArr[i10] = new File(str.substring(i2));
            } else {
                int i11 = i4;
                i3 = i4 + 1;
                fileArr[i11] = new File(".");
            }
            if (i3 != i5) {
                File[] fileArr2 = new File[i3];
                System.arraycopy(fileArr, 0, fileArr2, 0, i3);
                fileArr = fileArr2;
            }
        } else {
            fileArr = new File[0];
        }
        return fileArr;
    }

    static URL getFileURL(File file) {
        try {
            file = file.getCanonicalFile();
        } catch (IOException e2) {
        }
        try {
            return ParseUtil.fileToEncodedURL(file);
        } catch (MalformedURLException e3) {
            throw new InternalError(e3);
        }
    }

    /* loaded from: rt.jar:sun/misc/Launcher$Factory.class */
    private static class Factory implements URLStreamHandlerFactory {
        private static String PREFIX = "sun.net.www.protocol";

        private Factory() {
        }

        @Override // java.net.URLStreamHandlerFactory
        public URLStreamHandler createURLStreamHandler(String str) {
            try {
                return (URLStreamHandler) Class.forName(PREFIX + "." + str + ".Handler").newInstance();
            } catch (ReflectiveOperationException e2) {
                throw new InternalError("could not load " + str + "system protocol handler", e2);
            }
        }
    }
}
