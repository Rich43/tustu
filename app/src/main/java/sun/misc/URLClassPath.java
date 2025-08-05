package sun.misc;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.SocketPermission;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import java.security.AccessControlContext;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.CodeSigner;
import java.security.Permission;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.CRC32;
import sun.net.util.URLUtil;
import sun.net.www.ParseUtil;
import sun.security.action.GetPropertyAction;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:sun/misc/URLClassPath.class */
public class URLClassPath {
    static final String USER_AGENT_JAVA_VERSION = "UA-Java-Version";
    static final String JAVA_VERSION = (String) AccessController.doPrivileged(new GetPropertyAction("java.version"));
    private static final boolean DEBUG;
    private static final boolean DEBUG_LOOKUP_CACHE;
    private static final boolean DISABLE_JAR_CHECKING;
    private static final boolean DISABLE_ACC_CHECKING;
    private static final boolean DISABLE_CP_URL_CHECK;
    private static final boolean DEBUG_CP_URL_CHECK;
    private ArrayList<URL> path;
    Stack<URL> urls;
    ArrayList<Loader> loaders;
    HashMap<String, Loader> lmap;
    private URLStreamHandler jarHandler;
    private boolean closed;
    private final AccessControlContext acc;
    private static volatile boolean lookupCacheEnabled;
    private URL[] lookupCacheURLs;
    private ClassLoader lookupCacheLoader;

    private static native URL[] getLookupCacheURLs(ClassLoader classLoader);

    private static native int[] getLookupCacheForClassLoader(ClassLoader classLoader, String str);

    private static native boolean knownToNotExist0(ClassLoader classLoader, String str);

    static {
        DEBUG = AccessController.doPrivileged(new GetPropertyAction("sun.misc.URLClassPath.debug")) != null;
        DEBUG_LOOKUP_CACHE = AccessController.doPrivileged(new GetPropertyAction("sun.misc.URLClassPath.debugLookupCache")) != null;
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("sun.misc.URLClassPath.disableJarChecking"));
        DISABLE_JAR_CHECKING = str != null ? str.equals("true") || str.equals("") : false;
        String str2 = (String) AccessController.doPrivileged(new GetPropertyAction("jdk.net.URLClassPath.disableRestrictedPermissions"));
        DISABLE_ACC_CHECKING = str2 != null ? str2.equals("true") || str2.equals("") : false;
        String str3 = (String) AccessController.doPrivileged(new GetPropertyAction("jdk.net.URLClassPath.disableClassPathURLCheck", "true"));
        DISABLE_CP_URL_CHECK = str3 != null ? str3.equals("true") || str3.isEmpty() : false;
        DEBUG_CP_URL_CHECK = TransformerFactoryImpl.DEBUG.equals(str3);
        lookupCacheEnabled = "true".equals(VM.getSavedProperty("sun.cds.enableSharedLookupCache"));
    }

    public URLClassPath(URL[] urlArr, URLStreamHandlerFactory uRLStreamHandlerFactory, AccessControlContext accessControlContext) {
        this.path = new ArrayList<>();
        this.urls = new Stack<>();
        this.loaders = new ArrayList<>();
        this.lmap = new HashMap<>();
        this.closed = false;
        for (URL url : urlArr) {
            this.path.add(url);
        }
        push(urlArr);
        if (uRLStreamHandlerFactory != null) {
            this.jarHandler = uRLStreamHandlerFactory.createURLStreamHandler("jar");
        }
        if (DISABLE_ACC_CHECKING) {
            this.acc = null;
        } else {
            this.acc = accessControlContext;
        }
    }

    public URLClassPath(URL[] urlArr) {
        this(urlArr, null, null);
    }

    public URLClassPath(URL[] urlArr, AccessControlContext accessControlContext) {
        this(urlArr, null, accessControlContext);
    }

    public synchronized List<IOException> closeLoaders() {
        if (this.closed) {
            return Collections.emptyList();
        }
        LinkedList linkedList = new LinkedList();
        Iterator<Loader> it = this.loaders.iterator();
        while (it.hasNext()) {
            try {
                it.next().close();
            } catch (IOException e2) {
                linkedList.add(e2);
            }
        }
        this.closed = true;
        return linkedList;
    }

    public synchronized void addURL(URL url) {
        if (this.closed) {
            return;
        }
        synchronized (this.urls) {
            if (url != null) {
                if (!this.path.contains(url)) {
                    this.urls.add(0, url);
                    this.path.add(url);
                    if (this.lookupCacheURLs != null) {
                        disableAllLookupCaches();
                    }
                }
            }
        }
    }

    public URL[] getURLs() {
        URL[] urlArr;
        synchronized (this.urls) {
            urlArr = (URL[]) this.path.toArray(new URL[this.path.size()]);
        }
        return urlArr;
    }

    public URL findResource(String str, boolean z2) {
        int[] lookupCache = getLookupCache(str);
        int i2 = 0;
        while (true) {
            Loader nextLoader = getNextLoader(lookupCache, i2);
            if (nextLoader != null) {
                URL urlFindResource = nextLoader.findResource(str, z2);
                if (urlFindResource == null) {
                    i2++;
                } else {
                    return urlFindResource;
                }
            } else {
                return null;
            }
        }
    }

    public Resource getResource(String str, boolean z2) {
        if (DEBUG) {
            System.err.println("URLClassPath.getResource(\"" + str + "\")");
        }
        int[] lookupCache = getLookupCache(str);
        int i2 = 0;
        while (true) {
            Loader nextLoader = getNextLoader(lookupCache, i2);
            if (nextLoader != null) {
                Resource resource = nextLoader.getResource(str, z2);
                if (resource == null) {
                    i2++;
                } else {
                    return resource;
                }
            } else {
                return null;
            }
        }
    }

    public Enumeration<URL> findResources(final String str, final boolean z2) {
        return new Enumeration<URL>() { // from class: sun.misc.URLClassPath.1
            private int[] cache;
            private int index = 0;
            private URL url = null;

            {
                this.cache = URLClassPath.this.getLookupCache(str);
            }

            private boolean next() {
                if (this.url != null) {
                    return true;
                }
                do {
                    URLClassPath uRLClassPath = URLClassPath.this;
                    int[] iArr = this.cache;
                    int i2 = this.index;
                    this.index = i2 + 1;
                    Loader nextLoader = uRLClassPath.getNextLoader(iArr, i2);
                    if (nextLoader != null) {
                        this.url = nextLoader.findResource(str, z2);
                    } else {
                        return false;
                    }
                } while (this.url == null);
                return true;
            }

            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                return next();
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
        };
    }

    public Resource getResource(String str) {
        return getResource(str, true);
    }

    public Enumeration<Resource> getResources(final String str, final boolean z2) {
        return new Enumeration<Resource>() { // from class: sun.misc.URLClassPath.2
            private int[] cache;
            private int index = 0;
            private Resource res = null;

            {
                this.cache = URLClassPath.this.getLookupCache(str);
            }

            private boolean next() {
                if (this.res != null) {
                    return true;
                }
                do {
                    URLClassPath uRLClassPath = URLClassPath.this;
                    int[] iArr = this.cache;
                    int i2 = this.index;
                    this.index = i2 + 1;
                    Loader nextLoader = uRLClassPath.getNextLoader(iArr, i2);
                    if (nextLoader != null) {
                        this.res = nextLoader.getResource(str, z2);
                    } else {
                        return false;
                    }
                } while (this.res == null);
                return true;
            }

            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                return next();
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Enumeration
            /* renamed from: nextElement */
            public Resource nextElement2() {
                if (!next()) {
                    throw new NoSuchElementException();
                }
                Resource resource = this.res;
                this.res = null;
                return resource;
            }
        };
    }

    public Enumeration<Resource> getResources(String str) {
        return getResources(str, true);
    }

    synchronized void initLookupCache(ClassLoader classLoader) {
        URL[] lookupCacheURLs = getLookupCacheURLs(classLoader);
        this.lookupCacheURLs = lookupCacheURLs;
        if (lookupCacheURLs != null) {
            this.lookupCacheLoader = classLoader;
        } else {
            disableAllLookupCaches();
        }
    }

    static void disableAllLookupCaches() {
        lookupCacheEnabled = false;
    }

    synchronized boolean knownToNotExist(String str) {
        if (this.lookupCacheURLs != null && lookupCacheEnabled) {
            return knownToNotExist0(this.lookupCacheLoader, str);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized int[] getLookupCache(String str) {
        if (this.lookupCacheURLs == null || !lookupCacheEnabled) {
            return null;
        }
        int[] lookupCacheForClassLoader = getLookupCacheForClassLoader(this.lookupCacheLoader, str);
        if (lookupCacheForClassLoader != null && lookupCacheForClassLoader.length > 0) {
            int i2 = lookupCacheForClassLoader[lookupCacheForClassLoader.length - 1];
            if (!ensureLoaderOpened(i2)) {
                if (DEBUG_LOOKUP_CACHE) {
                    System.out.println("Expanded loaders FAILED " + this.loaders.size() + " for maxindex=" + i2);
                    return null;
                }
                return null;
            }
        }
        return lookupCacheForClassLoader;
    }

    private boolean ensureLoaderOpened(int i2) {
        if (this.loaders.size() <= i2) {
            if (getLoader(i2) == null || !lookupCacheEnabled) {
                return false;
            }
            if (DEBUG_LOOKUP_CACHE) {
                System.out.println("Expanded loaders " + this.loaders.size() + " to index=" + i2);
                return true;
            }
            return true;
        }
        return true;
    }

    private synchronized void validateLookupCache(int i2, String str) {
        if (this.lookupCacheURLs != null && lookupCacheEnabled) {
            if (i2 < this.lookupCacheURLs.length && str.equals(URLUtil.urlNoFragString(this.lookupCacheURLs[i2]))) {
                return;
            }
            if (DEBUG || DEBUG_LOOKUP_CACHE) {
                System.out.println("WARNING: resource lookup cache invalidated for lookupCacheLoader at " + i2);
            }
            disableAllLookupCaches();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized Loader getNextLoader(int[] iArr, int i2) {
        if (this.closed) {
            return null;
        }
        if (iArr != null) {
            if (i2 < iArr.length) {
                Loader loader = this.loaders.get(iArr[i2]);
                if (DEBUG_LOOKUP_CACHE) {
                    System.out.println("HASCACHE: Loading from : " + iArr[i2] + " = " + ((Object) loader.getBaseURL()));
                }
                return loader;
            }
            return null;
        }
        return getLoader(i2);
    }

    private synchronized Loader getLoader(int i2) {
        URL urlPop;
        if (this.closed) {
            return null;
        }
        while (this.loaders.size() < i2 + 1) {
            synchronized (this.urls) {
                if (this.urls.empty()) {
                    return null;
                }
                urlPop = this.urls.pop();
            }
            String strUrlNoFragString = URLUtil.urlNoFragString(urlPop);
            if (!this.lmap.containsKey(strUrlNoFragString)) {
                try {
                    Loader loader = getLoader(urlPop);
                    URL[] classPath = loader.getClassPath();
                    if (classPath != null) {
                        push(classPath);
                    }
                    validateLookupCache(this.loaders.size(), strUrlNoFragString);
                    this.loaders.add(loader);
                    this.lmap.put(strUrlNoFragString, loader);
                } catch (IOException e2) {
                } catch (SecurityException e3) {
                    if (DEBUG) {
                        System.err.println("Failed to access " + ((Object) urlPop) + ", " + ((Object) e3));
                    }
                }
            }
        }
        if (DEBUG_LOOKUP_CACHE) {
            System.out.println("NOCACHE: Loading from : " + i2);
        }
        return this.loaders.get(i2);
    }

    private Loader getLoader(final URL url) throws IOException {
        try {
            return (Loader) AccessController.doPrivileged(new PrivilegedExceptionAction<Loader>() { // from class: sun.misc.URLClassPath.3
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Loader run() throws IOException {
                    String file = url.getFile();
                    if (file != null && file.endsWith("/")) {
                        if (DeploymentDescriptorParser.ATTR_FILE.equals(url.getProtocol())) {
                            return new FileLoader(url);
                        }
                        return new Loader(url);
                    }
                    return new JarLoader(url, URLClassPath.this.jarHandler, URLClassPath.this.lmap, URLClassPath.this.acc);
                }
            }, this.acc);
        } catch (PrivilegedActionException e2) {
            throw ((IOException) e2.getException());
        }
    }

    private void push(URL[] urlArr) {
        synchronized (this.urls) {
            for (int length = urlArr.length - 1; length >= 0; length--) {
                this.urls.push(urlArr[length]);
            }
        }
    }

    public static URL[] pathToURLs(String str) {
        StringTokenizer stringTokenizer = new StringTokenizer(str, File.pathSeparator);
        URL[] urlArr = new URL[stringTokenizer.countTokens()];
        int i2 = 0;
        while (stringTokenizer.hasMoreTokens()) {
            File file = new File(stringTokenizer.nextToken());
            try {
                file = new File(file.getCanonicalPath());
            } catch (IOException e2) {
            }
            try {
                int i3 = i2;
                i2++;
                urlArr[i3] = ParseUtil.fileToEncodedURL(file);
            } catch (IOException e3) {
            }
        }
        if (urlArr.length != i2) {
            URL[] urlArr2 = new URL[i2];
            System.arraycopy(urlArr, 0, urlArr2, 0, i2);
            urlArr = urlArr2;
        }
        return urlArr;
    }

    public URL checkURL(URL url) {
        try {
            check(url);
            return url;
        } catch (Exception e2) {
            return null;
        }
    }

    static void check(URL url) throws IOException {
        URLConnection uRLConnectionOpenConnection;
        Permission permission;
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null && (permission = (uRLConnectionOpenConnection = url.openConnection()).getPermission()) != null) {
            try {
                securityManager.checkPermission(permission);
            } catch (SecurityException e2) {
                if ((permission instanceof FilePermission) && permission.getActions().indexOf("read") != -1) {
                    securityManager.checkRead(permission.getName());
                    return;
                }
                if ((permission instanceof SocketPermission) && permission.getActions().indexOf(SecurityConstants.SOCKET_CONNECT_ACTION) != -1) {
                    URL jarFileURL = url;
                    if (uRLConnectionOpenConnection instanceof JarURLConnection) {
                        jarFileURL = ((JarURLConnection) uRLConnectionOpenConnection).getJarFileURL();
                    }
                    securityManager.checkConnect(jarFileURL.getHost(), jarFileURL.getPort());
                    return;
                }
                throw e2;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: rt.jar:sun/misc/URLClassPath$Loader.class */
    static class Loader implements Closeable {
        private final URL base;
        private JarFile jarfile;

        Loader(URL url) {
            this.base = url;
        }

        URL getBaseURL() {
            return this.base;
        }

        URL findResource(String str, boolean z2) {
            try {
                URL url = new URL(this.base, ParseUtil.encodePath(str, false));
                if (z2) {
                    try {
                        URLClassPath.check(url);
                    } catch (Exception e2) {
                        return null;
                    }
                }
                URLConnection uRLConnectionOpenConnection = url.openConnection();
                if (uRLConnectionOpenConnection instanceof HttpURLConnection) {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) uRLConnectionOpenConnection;
                    httpURLConnection.setRequestMethod("HEAD");
                    if (httpURLConnection.getResponseCode() >= 400) {
                        return null;
                    }
                } else {
                    uRLConnectionOpenConnection.setUseCaches(false);
                    uRLConnectionOpenConnection.getInputStream().close();
                }
                return url;
            } catch (MalformedURLException e3) {
                throw new IllegalArgumentException("name");
            }
        }

        Resource getResource(final String str, boolean z2) {
            try {
                final URL url = new URL(this.base, ParseUtil.encodePath(str, false));
                if (z2) {
                    try {
                        URLClassPath.check(url);
                    } catch (Exception e2) {
                        return null;
                    }
                }
                final URLConnection uRLConnectionOpenConnection = url.openConnection();
                uRLConnectionOpenConnection.getInputStream();
                if (uRLConnectionOpenConnection instanceof JarURLConnection) {
                    this.jarfile = JarLoader.checkJar(((JarURLConnection) uRLConnectionOpenConnection).getJarFile());
                }
                return new Resource() { // from class: sun.misc.URLClassPath.Loader.1
                    @Override // sun.misc.Resource
                    public String getName() {
                        return str;
                    }

                    @Override // sun.misc.Resource
                    public URL getURL() {
                        return url;
                    }

                    @Override // sun.misc.Resource
                    public URL getCodeSourceURL() {
                        return Loader.this.base;
                    }

                    @Override // sun.misc.Resource
                    public InputStream getInputStream() throws IOException {
                        return uRLConnectionOpenConnection.getInputStream();
                    }

                    @Override // sun.misc.Resource
                    public int getContentLength() throws IOException {
                        return uRLConnectionOpenConnection.getContentLength();
                    }
                };
            } catch (MalformedURLException e3) {
                throw new IllegalArgumentException("name");
            }
        }

        Resource getResource(String str) {
            return getResource(str, true);
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (this.jarfile != null) {
                this.jarfile.close();
            }
        }

        URL[] getClassPath() throws IOException {
            return null;
        }
    }

    /* loaded from: rt.jar:sun/misc/URLClassPath$JarLoader.class */
    static class JarLoader extends Loader {
        private JarFile jar;
        private final URL csu;
        private JarIndex index;
        private MetaIndex metaIndex;
        private URLStreamHandler handler;
        private final HashMap<String, Loader> lmap;
        private final AccessControlContext acc;
        private boolean closed;
        private static final JavaUtilZipFileAccess zipAccess = SharedSecrets.getJavaUtilZipFileAccess();

        JarLoader(URL url, URLStreamHandler uRLStreamHandler, HashMap<String, Loader> map, AccessControlContext accessControlContext) throws IOException {
            super(new URL("jar", "", -1, ((Object) url) + "!/", uRLStreamHandler));
            this.closed = false;
            this.csu = url;
            this.handler = uRLStreamHandler;
            this.lmap = map;
            this.acc = accessControlContext;
            if (!isOptimizable(url)) {
                ensureOpen();
                return;
            }
            String file = url.getFile();
            if (file != null) {
                File file2 = new File(ParseUtil.decode(file));
                this.metaIndex = MetaIndex.forJar(file2);
                if (this.metaIndex != null && !file2.exists()) {
                    this.metaIndex = null;
                }
            }
            if (this.metaIndex == null) {
                ensureOpen();
            }
        }

        @Override // sun.misc.URLClassPath.Loader, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (!this.closed) {
                this.closed = true;
                ensureOpen();
                this.jar.close();
            }
        }

        JarFile getJarFile() {
            return this.jar;
        }

        private boolean isOptimizable(URL url) {
            return DeploymentDescriptorParser.ATTR_FILE.equals(url.getProtocol());
        }

        private void ensureOpen() throws IOException {
            if (this.jar == null) {
                try {
                    AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: sun.misc.URLClassPath.JarLoader.1
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedExceptionAction
                        public Void run() throws IOException {
                            if (URLClassPath.DEBUG) {
                                System.err.println("Opening " + ((Object) JarLoader.this.csu));
                                Thread.dumpStack();
                            }
                            JarLoader.this.jar = JarLoader.this.getJarFile(JarLoader.this.csu);
                            JarLoader.this.index = JarIndex.getJarIndex(JarLoader.this.jar, JarLoader.this.metaIndex);
                            if (JarLoader.this.index != null) {
                                for (String str : JarLoader.this.index.getJarFiles()) {
                                    try {
                                        String strUrlNoFragString = URLUtil.urlNoFragString(new URL(JarLoader.this.csu, str));
                                        if (!JarLoader.this.lmap.containsKey(strUrlNoFragString)) {
                                            JarLoader.this.lmap.put(strUrlNoFragString, null);
                                        }
                                    } catch (MalformedURLException e2) {
                                    }
                                }
                                return null;
                            }
                            return null;
                        }
                    }, this.acc);
                } catch (PrivilegedActionException e2) {
                    throw ((IOException) e2.getException());
                }
            }
        }

        static JarFile checkJar(JarFile jarFile) throws IOException {
            if (System.getSecurityManager() != null && !URLClassPath.DISABLE_JAR_CHECKING && !zipAccess.startsWithLocHeader(jarFile)) {
                IOException iOException = new IOException("Invalid Jar file");
                try {
                    jarFile.close();
                } catch (IOException e2) {
                    iOException.addSuppressed(e2);
                }
                throw iOException;
            }
            return jarFile;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public JarFile getJarFile(URL url) throws IOException {
            if (isOptimizable(url)) {
                FileURLMapper fileURLMapper = new FileURLMapper(url);
                if (!fileURLMapper.exists()) {
                    throw new FileNotFoundException(fileURLMapper.getPath());
                }
                return checkJar(new JarFile(fileURLMapper.getPath()));
            }
            URLConnection uRLConnectionOpenConnection = getBaseURL().openConnection();
            uRLConnectionOpenConnection.setRequestProperty(URLClassPath.USER_AGENT_JAVA_VERSION, URLClassPath.JAVA_VERSION);
            return checkJar(((JarURLConnection) uRLConnectionOpenConnection).getJarFile());
        }

        JarIndex getIndex() {
            try {
                ensureOpen();
                return this.index;
            } catch (IOException e2) {
                throw new InternalError(e2);
            }
        }

        Resource checkResource(final String str, boolean z2, final JarEntry jarEntry) {
            try {
                final URL url = new URL(getBaseURL(), ParseUtil.encodePath(str, false));
                if (z2) {
                    URLClassPath.check(url);
                }
                return new Resource() { // from class: sun.misc.URLClassPath.JarLoader.2
                    private Exception dataError = null;

                    @Override // sun.misc.Resource
                    public String getName() {
                        return str;
                    }

                    @Override // sun.misc.Resource
                    public URL getURL() {
                        return url;
                    }

                    @Override // sun.misc.Resource
                    public URL getCodeSourceURL() {
                        return JarLoader.this.csu;
                    }

                    @Override // sun.misc.Resource
                    public InputStream getInputStream() throws IOException {
                        return JarLoader.this.jar.getInputStream(jarEntry);
                    }

                    @Override // sun.misc.Resource
                    public int getContentLength() {
                        return (int) jarEntry.getSize();
                    }

                    @Override // sun.misc.Resource
                    public Manifest getManifest() throws IOException {
                        SharedSecrets.javaUtilJarAccess().ensureInitialization(JarLoader.this.jar);
                        return JarLoader.this.jar.getManifest();
                    }

                    @Override // sun.misc.Resource
                    public Certificate[] getCertificates() {
                        return jarEntry.getCertificates();
                    }

                    @Override // sun.misc.Resource
                    public CodeSigner[] getCodeSigners() {
                        return jarEntry.getCodeSigners();
                    }

                    @Override // sun.misc.Resource
                    public Exception getDataError() {
                        return this.dataError;
                    }

                    @Override // sun.misc.Resource
                    public byte[] getBytes() throws IOException {
                        byte[] bytes = super.getBytes();
                        CRC32 crc32 = new CRC32();
                        crc32.update(bytes);
                        if (crc32.getValue() != jarEntry.getCrc()) {
                            this.dataError = new IOException("CRC error while extracting entry from JAR file");
                        }
                        return bytes;
                    }
                };
            } catch (MalformedURLException e2) {
                return null;
            } catch (IOException e3) {
                return null;
            } catch (AccessControlException e4) {
                return null;
            }
        }

        boolean validIndex(String str) {
            String strSubstring = str;
            int iLastIndexOf = str.lastIndexOf("/");
            if (iLastIndexOf != -1) {
                strSubstring = str.substring(0, iLastIndexOf);
            }
            Enumeration<JarEntry> enumerationEntries = this.jar.entries();
            while (enumerationEntries.hasMoreElements()) {
                String name = enumerationEntries.nextElement2().getName();
                int iLastIndexOf2 = name.lastIndexOf("/");
                if (iLastIndexOf2 != -1) {
                    name = name.substring(0, iLastIndexOf2);
                }
                if (name.equals(strSubstring)) {
                    return true;
                }
            }
            return false;
        }

        @Override // sun.misc.URLClassPath.Loader
        URL findResource(String str, boolean z2) {
            Resource resource = getResource(str, z2);
            if (resource != null) {
                return resource.getURL();
            }
            return null;
        }

        @Override // sun.misc.URLClassPath.Loader
        Resource getResource(String str, boolean z2) {
            if (this.metaIndex != null && !this.metaIndex.mayContain(str)) {
                return null;
            }
            try {
                ensureOpen();
                JarEntry jarEntry = this.jar.getJarEntry(str);
                if (jarEntry != null) {
                    return checkResource(str, z2, jarEntry);
                }
                if (this.index == null) {
                    return null;
                }
                return getResource(str, z2, new HashSet());
            } catch (IOException e2) {
                throw new InternalError(e2);
            }
        }

        Resource getResource(String str, boolean z2, Set<String> set) {
            JarLoader jarLoader;
            boolean z3;
            Resource resource;
            int i2 = 0;
            LinkedList<String> linkedList = this.index.get(str);
            LinkedList<String> linkedList2 = linkedList;
            if (linkedList == null) {
                return null;
            }
            do {
                int size = linkedList2.size();
                String[] strArr = (String[]) linkedList2.toArray(new String[size]);
                while (i2 < size) {
                    int i3 = i2;
                    i2++;
                    String str2 = strArr[i3];
                    try {
                        final URL url = new URL(this.csu, str2);
                        String strUrlNoFragString = URLUtil.urlNoFragString(url);
                        JarLoader jarLoader2 = (JarLoader) this.lmap.get(strUrlNoFragString);
                        jarLoader = jarLoader2;
                        if (jarLoader2 == null) {
                            jarLoader = (JarLoader) AccessController.doPrivileged(new PrivilegedExceptionAction<JarLoader>() { // from class: sun.misc.URLClassPath.JarLoader.3
                                /* JADX WARN: Can't rename method to resolve collision */
                                @Override // java.security.PrivilegedExceptionAction
                                public JarLoader run() throws IOException {
                                    return new JarLoader(url, JarLoader.this.handler, JarLoader.this.lmap, JarLoader.this.acc);
                                }
                            }, this.acc);
                            JarIndex index = jarLoader.getIndex();
                            if (index != null) {
                                int iLastIndexOf = str2.lastIndexOf("/");
                                index.merge(this.index, iLastIndexOf == -1 ? null : str2.substring(0, iLastIndexOf + 1));
                            }
                            this.lmap.put(strUrlNoFragString, jarLoader);
                        }
                        z3 = !set.add(URLUtil.urlNoFragString(url));
                        if (!z3) {
                            try {
                                jarLoader.ensureOpen();
                                JarEntry jarEntry = jarLoader.jar.getJarEntry(str);
                                if (jarEntry != null) {
                                    return jarLoader.checkResource(str, z2, jarEntry);
                                }
                                if (!jarLoader.validIndex(str)) {
                                    throw new InvalidJarIndexException("Invalid index");
                                }
                            } catch (IOException e2) {
                                throw new InternalError(e2);
                            }
                        }
                    } catch (MalformedURLException e3) {
                    } catch (PrivilegedActionException e4) {
                    }
                    if (!z3 && jarLoader != this && jarLoader.getIndex() != null && (resource = jarLoader.getResource(str, z2, set)) != null) {
                        return resource;
                    }
                }
                linkedList2 = this.index.get(str);
            } while (i2 < linkedList2.size());
            return null;
        }

        @Override // sun.misc.URLClassPath.Loader
        URL[] getClassPath() throws IOException {
            Manifest manifest;
            Attributes mainAttributes;
            String value;
            if (this.index != null || this.metaIndex != null) {
                return null;
            }
            ensureOpen();
            parseExtensionsDependencies();
            if (SharedSecrets.javaUtilJarAccess().jarFileHasClassPathAttribute(this.jar) && (manifest = this.jar.getManifest()) != null && (mainAttributes = manifest.getMainAttributes()) != null && (value = mainAttributes.getValue(Attributes.Name.CLASS_PATH)) != null) {
                return parseClassPath(this.csu, value);
            }
            return null;
        }

        private void parseExtensionsDependencies() throws IOException {
            ExtensionDependency.checkExtensionsDependencies(this.jar);
        }

        private URL[] parseClassPath(URL url, String str) throws MalformedURLException {
            StringTokenizer stringTokenizer = new StringTokenizer(str);
            URL[] urlArr = new URL[stringTokenizer.countTokens()];
            int i2 = 0;
            while (stringTokenizer.hasMoreTokens()) {
                String strNextToken = stringTokenizer.nextToken();
                URL url2 = URLClassPath.DISABLE_CP_URL_CHECK ? new URL(url, strNextToken) : tryResolve(url, strNextToken);
                if (url2 == null) {
                    if (URLClassPath.DEBUG_CP_URL_CHECK) {
                        System.err.println("Class-Path entry: \"" + strNextToken + "\" ignored in JAR file " + ((Object) url));
                    }
                } else {
                    urlArr[i2] = url2;
                    i2++;
                }
            }
            if (i2 == 0) {
                urlArr = null;
            } else if (i2 != urlArr.length) {
                urlArr = (URL[]) Arrays.copyOf(urlArr, i2);
            }
            return urlArr;
        }

        static URL tryResolve(URL url, String str) throws MalformedURLException {
            if (DeploymentDescriptorParser.ATTR_FILE.equalsIgnoreCase(url.getProtocol())) {
                return tryResolveFile(url, str);
            }
            return tryResolveNonFile(url, str);
        }

        static URL tryResolveFile(URL url, String str) throws MalformedURLException {
            boolean zEqualsIgnoreCase;
            int iIndexOf = str.indexOf(58);
            if (iIndexOf >= 0) {
                zEqualsIgnoreCase = DeploymentDescriptorParser.ATTR_FILE.equalsIgnoreCase(str.substring(0, iIndexOf));
            } else {
                zEqualsIgnoreCase = true;
            }
            if (zEqualsIgnoreCase) {
                return new URL(url, str);
            }
            return null;
        }

        static URL tryResolveNonFile(URL url, String str) throws MalformedURLException {
            String strReplace = str.replace(File.separatorChar, '/');
            if (isRelative(strReplace)) {
                URL url2 = new URL(url, strReplace);
                String path = url.getPath();
                String path2 = url2.getPath();
                int iLastIndexOf = path.lastIndexOf(47);
                if (iLastIndexOf == -1) {
                    iLastIndexOf = path.length() - 1;
                }
                if (path2.regionMatches(0, path, 0, iLastIndexOf + 1) && path2.indexOf(Constants.ATTRVAL_PARENT, iLastIndexOf) == -1) {
                    return url2;
                }
                return null;
            }
            return null;
        }

        static boolean isRelative(String str) {
            try {
                return !URI.create(str).isAbsolute();
            } catch (IllegalArgumentException e2) {
                return false;
            }
        }
    }

    /* loaded from: rt.jar:sun/misc/URLClassPath$FileLoader.class */
    private static class FileLoader extends Loader {
        private File dir;

        FileLoader(URL url) throws IOException {
            super(url);
            if (!DeploymentDescriptorParser.ATTR_FILE.equals(url.getProtocol())) {
                throw new IllegalArgumentException("url");
            }
            this.dir = new File(ParseUtil.decode(url.getFile().replace('/', File.separatorChar))).getCanonicalFile();
        }

        @Override // sun.misc.URLClassPath.Loader
        URL findResource(String str, boolean z2) {
            Resource resource = getResource(str, z2);
            if (resource != null) {
                return resource.getURL();
            }
            return null;
        }

        @Override // sun.misc.URLClassPath.Loader
        Resource getResource(final String str, boolean z2) {
            File file;
            try {
                URL url = new URL(getBaseURL(), ".");
                final URL url2 = new URL(getBaseURL(), ParseUtil.encodePath(str, false));
                if (!url2.getFile().startsWith(url.getFile())) {
                    return null;
                }
                if (z2) {
                    URLClassPath.check(url2);
                }
                if (str.indexOf(Constants.ATTRVAL_PARENT) != -1) {
                    file = new File(this.dir, str.replace('/', File.separatorChar)).getCanonicalFile();
                    if (!file.getPath().startsWith(this.dir.getPath())) {
                        return null;
                    }
                } else {
                    file = new File(this.dir, str.replace('/', File.separatorChar));
                }
                if (file.exists()) {
                    final File file2 = file;
                    return new Resource() { // from class: sun.misc.URLClassPath.FileLoader.1
                        @Override // sun.misc.Resource
                        public String getName() {
                            return str;
                        }

                        @Override // sun.misc.Resource
                        public URL getURL() {
                            return url2;
                        }

                        @Override // sun.misc.Resource
                        public URL getCodeSourceURL() {
                            return FileLoader.this.getBaseURL();
                        }

                        @Override // sun.misc.Resource
                        public InputStream getInputStream() throws IOException {
                            return new FileInputStream(file2);
                        }

                        @Override // sun.misc.Resource
                        public int getContentLength() throws IOException {
                            return (int) file2.length();
                        }
                    };
                }
                return null;
            } catch (Exception e2) {
                return null;
            }
        }
    }
}
