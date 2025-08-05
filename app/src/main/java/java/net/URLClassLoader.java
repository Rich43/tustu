package java.net;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.Closeable;
import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.SecureClassLoader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.WeakHashMap;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import sun.misc.JavaNetAccess;
import sun.misc.PerfCounter;
import sun.misc.Resource;
import sun.misc.SharedSecrets;
import sun.misc.URLClassPath;
import sun.net.www.ParseUtil;
import sun.net.www.protocol.file.FileURLConnection;
import sun.security.util.SecurityConstants;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:java/net/URLClassLoader.class */
public class URLClassLoader extends SecureClassLoader implements Closeable {
    private final URLClassPath ucp;
    private final AccessControlContext acc;
    private WeakHashMap<Closeable, Void> closeables;

    public URLClassLoader(URL[] urlArr, ClassLoader classLoader) {
        super(classLoader);
        this.closeables = new WeakHashMap<>();
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkCreateClassLoader();
        }
        this.acc = AccessController.getContext();
        this.ucp = new URLClassPath(urlArr, this.acc);
    }

    URLClassLoader(URL[] urlArr, ClassLoader classLoader, AccessControlContext accessControlContext) {
        super(classLoader);
        this.closeables = new WeakHashMap<>();
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkCreateClassLoader();
        }
        this.acc = accessControlContext;
        this.ucp = new URLClassPath(urlArr, accessControlContext);
    }

    public URLClassLoader(URL[] urlArr) {
        this.closeables = new WeakHashMap<>();
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkCreateClassLoader();
        }
        this.acc = AccessController.getContext();
        this.ucp = new URLClassPath(urlArr, this.acc);
    }

    URLClassLoader(URL[] urlArr, AccessControlContext accessControlContext) {
        this.closeables = new WeakHashMap<>();
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkCreateClassLoader();
        }
        this.acc = accessControlContext;
        this.ucp = new URLClassPath(urlArr, accessControlContext);
    }

    public URLClassLoader(URL[] urlArr, ClassLoader classLoader, URLStreamHandlerFactory uRLStreamHandlerFactory) {
        super(classLoader);
        this.closeables = new WeakHashMap<>();
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkCreateClassLoader();
        }
        this.acc = AccessController.getContext();
        this.ucp = new URLClassPath(urlArr, uRLStreamHandlerFactory, this.acc);
    }

    @Override // java.lang.ClassLoader
    public InputStream getResourceAsStream(String str) {
        URL resource = getResource(str);
        if (resource == null) {
            return null;
        }
        try {
            URLConnection uRLConnectionOpenConnection = resource.openConnection();
            InputStream inputStream = uRLConnectionOpenConnection.getInputStream();
            if (uRLConnectionOpenConnection instanceof JarURLConnection) {
                JarFile jarFile = ((JarURLConnection) uRLConnectionOpenConnection).getJarFile();
                synchronized (this.closeables) {
                    if (!this.closeables.containsKey(jarFile)) {
                        this.closeables.put(jarFile, null);
                    }
                }
            } else if (uRLConnectionOpenConnection instanceof FileURLConnection) {
                synchronized (this.closeables) {
                    this.closeables.put(inputStream, null);
                }
            }
            return inputStream;
        } catch (IOException e2) {
            return null;
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("closeClassLoader"));
        }
        List<IOException> listCloseLoaders = this.ucp.closeLoaders();
        synchronized (this.closeables) {
            Iterator<Closeable> it = this.closeables.keySet().iterator();
            while (it.hasNext()) {
                try {
                    it.next().close();
                } catch (IOException e2) {
                    listCloseLoaders.add(e2);
                }
            }
            this.closeables.clear();
        }
        if (listCloseLoaders.isEmpty()) {
            return;
        }
        IOException iOExceptionRemove = listCloseLoaders.remove(0);
        Iterator<IOException> it2 = listCloseLoaders.iterator();
        while (it2.hasNext()) {
            iOExceptionRemove.addSuppressed(it2.next());
        }
        throw iOExceptionRemove;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addURL(URL url) {
        this.ucp.addURL(url);
    }

    public URL[] getURLs() {
        return this.ucp.getURLs();
    }

    @Override // java.lang.ClassLoader
    protected Class<?> findClass(final String str) throws ClassNotFoundException {
        try {
            Class<?> cls = (Class) AccessController.doPrivileged(new PrivilegedExceptionAction<Class<?>>() { // from class: java.net.URLClassLoader.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Class<?> run() throws ClassNotFoundException {
                    Resource resource = URLClassLoader.this.ucp.getResource(str.replace('.', '/').concat(".class"), false);
                    if (resource != null) {
                        try {
                            return URLClassLoader.this.defineClass(str, resource);
                        } catch (IOException e2) {
                            throw new ClassNotFoundException(str, e2);
                        } catch (ClassFormatError e3) {
                            if (resource.getDataError() != null) {
                                e3.addSuppressed(resource.getDataError());
                            }
                            throw e3;
                        }
                    }
                    return null;
                }
            }, this.acc);
            if (cls == null) {
                throw new ClassNotFoundException(str);
            }
            return cls;
        } catch (PrivilegedActionException e2) {
            throw ((ClassNotFoundException) e2.getException());
        }
    }

    private Package getAndVerifyPackage(String str, Manifest manifest, URL url) {
        Package r0 = getPackage(str);
        if (r0 != null) {
            if (r0.isSealed()) {
                if (!r0.isSealed(url)) {
                    throw new SecurityException("sealing violation: package " + str + " is sealed");
                }
            } else if (manifest != null && isSealed(str, manifest)) {
                throw new SecurityException("sealing violation: can't seal package " + str + ": already loaded");
            }
        }
        return r0;
    }

    private void definePackageInternal(String str, Manifest manifest, URL url) {
        if (getAndVerifyPackage(str, manifest, url) == null) {
            try {
                if (manifest != null) {
                    definePackage(str, manifest, url);
                } else {
                    definePackage(str, null, null, null, null, null, null, null);
                }
            } catch (IllegalArgumentException e2) {
                if (getAndVerifyPackage(str, manifest, url) == null) {
                    throw new AssertionError((Object) ("Cannot find package " + str));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Class<?> defineClass(String str, Resource resource) throws IOException {
        long jNanoTime = System.nanoTime();
        int iLastIndexOf = str.lastIndexOf(46);
        URL codeSourceURL = resource.getCodeSourceURL();
        if (iLastIndexOf != -1) {
            definePackageInternal(str.substring(0, iLastIndexOf), resource.getManifest(), codeSourceURL);
        }
        ByteBuffer byteBuffer = resource.getByteBuffer();
        if (byteBuffer != null) {
            CodeSource codeSource = new CodeSource(codeSourceURL, resource.getCodeSigners());
            PerfCounter.getReadClassBytesTime().addElapsedTimeFrom(jNanoTime);
            return defineClass(str, byteBuffer, codeSource);
        }
        byte[] bytes = resource.getBytes();
        CodeSource codeSource2 = new CodeSource(codeSourceURL, resource.getCodeSigners());
        PerfCounter.getReadClassBytesTime().addElapsedTimeFrom(jNanoTime);
        return defineClass(str, bytes, 0, bytes.length, codeSource2);
    }

    protected Package definePackage(String str, Manifest manifest, URL url) throws IllegalArgumentException {
        String value = null;
        String value2 = null;
        String value3 = null;
        String value4 = null;
        String value5 = null;
        String value6 = null;
        String value7 = null;
        URL url2 = null;
        Attributes trustedAttributes = SharedSecrets.javaUtilJarAccess().getTrustedAttributes(manifest, str.replace('.', '/').concat("/"));
        if (trustedAttributes != null) {
            value = trustedAttributes.getValue(Attributes.Name.SPECIFICATION_TITLE);
            value2 = trustedAttributes.getValue(Attributes.Name.SPECIFICATION_VERSION);
            value3 = trustedAttributes.getValue(Attributes.Name.SPECIFICATION_VENDOR);
            value4 = trustedAttributes.getValue(Attributes.Name.IMPLEMENTATION_TITLE);
            value5 = trustedAttributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
            value6 = trustedAttributes.getValue(Attributes.Name.IMPLEMENTATION_VENDOR);
            value7 = trustedAttributes.getValue(Attributes.Name.SEALED);
        }
        Attributes mainAttributes = manifest.getMainAttributes();
        if (mainAttributes != null) {
            if (value == null) {
                value = mainAttributes.getValue(Attributes.Name.SPECIFICATION_TITLE);
            }
            if (value2 == null) {
                value2 = mainAttributes.getValue(Attributes.Name.SPECIFICATION_VERSION);
            }
            if (value3 == null) {
                value3 = mainAttributes.getValue(Attributes.Name.SPECIFICATION_VENDOR);
            }
            if (value4 == null) {
                value4 = mainAttributes.getValue(Attributes.Name.IMPLEMENTATION_TITLE);
            }
            if (value5 == null) {
                value5 = mainAttributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
            }
            if (value6 == null) {
                value6 = mainAttributes.getValue(Attributes.Name.IMPLEMENTATION_VENDOR);
            }
            if (value7 == null) {
                value7 = mainAttributes.getValue(Attributes.Name.SEALED);
            }
        }
        if ("true".equalsIgnoreCase(value7)) {
            url2 = url;
        }
        return definePackage(str, value, value2, value3, value4, value5, value6, url2);
    }

    private boolean isSealed(String str, Manifest manifest) {
        Attributes mainAttributes;
        Attributes trustedAttributes = SharedSecrets.javaUtilJarAccess().getTrustedAttributes(manifest, str.replace('.', '/').concat("/"));
        String value = null;
        if (trustedAttributes != null) {
            value = trustedAttributes.getValue(Attributes.Name.SEALED);
        }
        if (value == null && (mainAttributes = manifest.getMainAttributes()) != null) {
            value = mainAttributes.getValue(Attributes.Name.SEALED);
        }
        return "true".equalsIgnoreCase(value);
    }

    @Override // java.lang.ClassLoader
    public URL findResource(final String str) {
        URL url = (URL) AccessController.doPrivileged(new PrivilegedAction<URL>() { // from class: java.net.URLClassLoader.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public URL run2() {
                return URLClassLoader.this.ucp.findResource(str, true);
            }
        }, this.acc);
        if (url != null) {
            return this.ucp.checkURL(url);
        }
        return null;
    }

    @Override // java.lang.ClassLoader
    public Enumeration<URL> findResources(String str) throws IOException {
        final Enumeration<URL> enumerationFindResources = this.ucp.findResources(str, true);
        return new Enumeration<URL>() { // from class: java.net.URLClassLoader.3
            private URL url = null;

            private boolean next() {
                if (this.url != null) {
                    return true;
                }
                do {
                    URL url = (URL) AccessController.doPrivileged(new PrivilegedAction<URL>() { // from class: java.net.URLClassLoader.3.1
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedAction
                        /* renamed from: run */
                        public URL run2() {
                            if (!enumerationFindResources.hasMoreElements()) {
                                return null;
                            }
                            return (URL) enumerationFindResources.nextElement2();
                        }
                    }, URLClassLoader.this.acc);
                    if (url == null) {
                        break;
                    }
                    this.url = URLClassLoader.this.ucp.checkURL(url);
                } while (this.url == null);
                return this.url != null;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Enumeration
            /* renamed from: nextElement */
            public URL nextElement2() {
                if (!next()) {
                    throw new NoSuchElementException();
                }
                URL url = this.url;
                this.url = null;
                return url;
            }

            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                return next();
            }
        };
    }

    @Override // java.security.SecureClassLoader
    protected PermissionCollection getPermissions(CodeSource codeSource) {
        Permission socketPermission;
        URLConnection uRLConnectionOpenConnection;
        PermissionCollection permissions = super.getPermissions(codeSource);
        URL location = codeSource.getLocation();
        try {
            uRLConnectionOpenConnection = location.openConnection();
            socketPermission = uRLConnectionOpenConnection.getPermission();
        } catch (IOException e2) {
            socketPermission = null;
            uRLConnectionOpenConnection = null;
        }
        if (socketPermission instanceof FilePermission) {
            String name = socketPermission.getName();
            if (name.endsWith(File.separator)) {
                socketPermission = new FilePermission(name + LanguageTag.SEP, "read");
            }
        } else if (socketPermission == null && location.getProtocol().equals(DeploymentDescriptorParser.ATTR_FILE)) {
            String strDecode = ParseUtil.decode(location.getFile().replace('/', File.separatorChar));
            if (strDecode.endsWith(File.separator)) {
                strDecode = strDecode + LanguageTag.SEP;
            }
            socketPermission = new FilePermission(strDecode, "read");
        } else {
            URL jarFileURL = location;
            if (uRLConnectionOpenConnection instanceof JarURLConnection) {
                jarFileURL = ((JarURLConnection) uRLConnectionOpenConnection).getJarFileURL();
            }
            String host = jarFileURL.getHost();
            if (host != null && host.length() > 0) {
                socketPermission = new SocketPermission(host, SecurityConstants.SOCKET_CONNECT_ACCEPT_ACTION);
            }
        }
        if (socketPermission != null) {
            final SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                final Permission permission = socketPermission;
                AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.net.URLClassLoader.4
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public Void run2() throws SecurityException {
                        securityManager.checkPermission(permission);
                        return null;
                    }
                }, this.acc);
            }
            permissions.add(socketPermission);
        }
        return permissions;
    }

    public static URLClassLoader newInstance(final URL[] urlArr, final ClassLoader classLoader) {
        final AccessControlContext context = AccessController.getContext();
        return (URLClassLoader) AccessController.doPrivileged(new PrivilegedAction<URLClassLoader>() { // from class: java.net.URLClassLoader.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public URLClassLoader run2() {
                return new FactoryURLClassLoader(urlArr, classLoader, context);
            }
        });
    }

    public static URLClassLoader newInstance(final URL[] urlArr) {
        final AccessControlContext context = AccessController.getContext();
        return (URLClassLoader) AccessController.doPrivileged(new PrivilegedAction<URLClassLoader>() { // from class: java.net.URLClassLoader.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public URLClassLoader run2() {
                return new FactoryURLClassLoader(urlArr, context);
            }
        });
    }

    static {
        SharedSecrets.setJavaNetAccess(new JavaNetAccess() { // from class: java.net.URLClassLoader.7
            @Override // sun.misc.JavaNetAccess
            public URLClassPath getURLClassPath(URLClassLoader uRLClassLoader) {
                return uRLClassLoader.ucp;
            }

            @Override // sun.misc.JavaNetAccess
            public String getOriginalHostName(InetAddress inetAddress) {
                return inetAddress.holder.getOriginalHostName();
            }
        });
        ClassLoader.registerAsParallelCapable();
    }
}
