package sun.applet;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.cert.Certificate;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.NoSuchElementException;
import sun.awt.AppContext;
import sun.misc.IOUtils;
import sun.net.www.ParseUtil;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:sun/applet/AppletClassLoader.class */
public class AppletClassLoader extends URLClassLoader {
    private URL base;
    private CodeSource codesource;
    private AccessControlContext acc;
    private boolean exceptionStatus;
    private final Object threadGroupSynchronizer;
    private final Object grabReleaseSynchronizer;
    private boolean codebaseLookup;
    private volatile boolean allowRecursiveDirectoryRead;
    private Object syncResourceAsStream;
    private Object syncResourceAsStreamFromJar;
    private boolean resourceAsStreamInCall;
    private boolean resourceAsStreamFromJarInCall;
    private AppletThreadGroup threadGroup;
    private AppContext appContext;
    int usageCount;
    private HashMap jdk11AppletInfo;
    private HashMap jdk12AppletInfo;
    private static AppletMessageHandler mh = new AppletMessageHandler("appletclassloader");

    protected AppletClassLoader(URL url) {
        super(new URL[0]);
        this.exceptionStatus = false;
        this.threadGroupSynchronizer = new Object();
        this.grabReleaseSynchronizer = new Object();
        this.codebaseLookup = true;
        this.allowRecursiveDirectoryRead = true;
        this.syncResourceAsStream = new Object();
        this.syncResourceAsStreamFromJar = new Object();
        this.resourceAsStreamInCall = false;
        this.resourceAsStreamFromJarInCall = false;
        this.usageCount = 0;
        this.jdk11AppletInfo = new HashMap();
        this.jdk12AppletInfo = new HashMap();
        this.base = url;
        this.codesource = new CodeSource(url, (Certificate[]) null);
        this.acc = AccessController.getContext();
    }

    public void disableRecursiveDirectoryRead() {
        this.allowRecursiveDirectoryRead = false;
    }

    void setCodebaseLookup(boolean z2) {
        this.codebaseLookup = z2;
    }

    URL getBaseURL() {
        return this.base;
    }

    @Override // java.net.URLClassLoader, javax.management.loading.MLetMBean
    public URL[] getURLs() {
        URL[] uRLs = super.getURLs();
        URL[] urlArr = new URL[uRLs.length + 1];
        System.arraycopy(uRLs, 0, urlArr, 0, uRLs.length);
        urlArr[urlArr.length - 1] = this.base;
        return urlArr;
    }

    protected void addJar(String str) throws IOException {
        try {
            addURL(new URL(this.base, str));
        } catch (MalformedURLException e2) {
            throw new IllegalArgumentException("name");
        }
    }

    @Override // java.lang.ClassLoader
    public synchronized Class loadClass(String str, boolean z2) throws ClassNotFoundException {
        SecurityManager securityManager;
        int iLastIndexOf = str.lastIndexOf(46);
        if (iLastIndexOf != -1 && (securityManager = System.getSecurityManager()) != null) {
            securityManager.checkPackageAccess(str.substring(0, iLastIndexOf));
        }
        try {
            return super.loadClass(str, z2);
        } catch (ClassNotFoundException e2) {
            throw e2;
        } catch (Error e3) {
            throw e3;
        } catch (RuntimeException e4) {
            throw e4;
        }
    }

    @Override // java.net.URLClassLoader, java.lang.ClassLoader
    protected Class findClass(String str) throws ClassNotFoundException {
        int iIndexOf = str.indexOf(";");
        String strSubstring = "";
        if (iIndexOf != -1) {
            strSubstring = str.substring(iIndexOf, str.length());
            str = str.substring(0, iIndexOf);
        }
        try {
            return super.findClass(str);
        } catch (ClassNotFoundException e2) {
            if (!this.codebaseLookup) {
                throw new ClassNotFoundException(str);
            }
            final String string = new StringBuffer(ParseUtil.encodePath(str.replace('.', '/'), false)).append(".class").append(strSubstring).toString();
            try {
                byte[] bArr = (byte[]) AccessController.doPrivileged(new PrivilegedExceptionAction() { // from class: sun.applet.AppletClassLoader.1
                    @Override // java.security.PrivilegedExceptionAction
                    public Object run() throws IOException {
                        try {
                            URL url = new URL(AppletClassLoader.this.base, string);
                            if (AppletClassLoader.this.base.getProtocol().equals(url.getProtocol()) && AppletClassLoader.this.base.getHost().equals(url.getHost()) && AppletClassLoader.this.base.getPort() == url.getPort()) {
                                return AppletClassLoader.getBytes(url);
                            }
                            return null;
                        } catch (Exception e3) {
                            return null;
                        }
                    }
                }, this.acc);
                if (bArr != null) {
                    return defineClass(str, bArr, 0, bArr.length, this.codesource);
                }
                throw new ClassNotFoundException(str);
            } catch (PrivilegedActionException e3) {
                throw new ClassNotFoundException(str, e3.getException());
            }
        }
    }

    @Override // java.net.URLClassLoader, java.security.SecureClassLoader
    protected PermissionCollection getPermissions(CodeSource codeSource) {
        Permission permission;
        Permission permission2;
        int iLastIndexOf;
        PermissionCollection permissions = super.getPermissions(codeSource);
        URL location = codeSource.getLocation();
        String strDecode = null;
        try {
            permission = location.openConnection().getPermission();
        } catch (IOException e2) {
            permission = null;
        }
        if (permission instanceof FilePermission) {
            strDecode = permission.getName();
        } else if (permission == null && location.getProtocol().equals(DeploymentDescriptorParser.ATTR_FILE)) {
            strDecode = ParseUtil.decode(location.getFile().replace('/', File.separatorChar));
        }
        if (strDecode != null) {
            String str = strDecode;
            if (!strDecode.endsWith(File.separator) && (iLastIndexOf = strDecode.lastIndexOf(File.separatorChar)) != -1) {
                permissions.add(new FilePermission(strDecode.substring(0, iLastIndexOf + 1) + LanguageTag.SEP, "read"));
            }
            boolean zIsDirectory = new File(str).isDirectory();
            if (this.allowRecursiveDirectoryRead && (zIsDirectory || str.toLowerCase().endsWith(".jar") || str.toLowerCase().endsWith(".zip"))) {
                try {
                    permission2 = this.base.openConnection().getPermission();
                } catch (IOException e3) {
                    permission2 = null;
                }
                if (permission2 instanceof FilePermission) {
                    String name = permission2.getName();
                    if (name.endsWith(File.separator)) {
                        name = name + LanguageTag.SEP;
                    }
                    permissions.add(new FilePermission(name, "read"));
                } else if (permission2 == null && this.base.getProtocol().equals(DeploymentDescriptorParser.ATTR_FILE)) {
                    String strDecode2 = ParseUtil.decode(this.base.getFile().replace('/', File.separatorChar));
                    if (strDecode2.endsWith(File.separator)) {
                        strDecode2 = strDecode2 + LanguageTag.SEP;
                    }
                    permissions.add(new FilePermission(strDecode2, "read"));
                }
            }
        }
        return permissions;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static byte[] getBytes(URL url) throws IOException {
        URLConnection uRLConnectionOpenConnection = url.openConnection();
        if ((uRLConnectionOpenConnection instanceof HttpURLConnection) && ((HttpURLConnection) uRLConnectionOpenConnection).getResponseCode() >= 400) {
            throw new IOException("open HTTP connection failed.");
        }
        int contentLength = uRLConnectionOpenConnection.getContentLength();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(uRLConnectionOpenConnection.getInputStream());
        try {
            byte[] allBytes = IOUtils.readAllBytes(bufferedInputStream);
            if (contentLength != -1 && allBytes.length != contentLength) {
                throw new EOFException("Expected:" + contentLength + ", read:" + allBytes.length);
            }
            return allBytes;
        } finally {
            bufferedInputStream.close();
        }
    }

    @Override // java.net.URLClassLoader, java.lang.ClassLoader
    public InputStream getResourceAsStream(String str) {
        InputStream resourceAsStream;
        if (str == null) {
            throw new NullPointerException("name");
        }
        try {
            synchronized (this.syncResourceAsStream) {
                this.resourceAsStreamInCall = true;
                resourceAsStream = super.getResourceAsStream(str);
                this.resourceAsStreamInCall = false;
            }
            if (this.codebaseLookup && resourceAsStream == null) {
                resourceAsStream = new URL(this.base, ParseUtil.encodePath(str, false)).openStream();
            }
            return resourceAsStream;
        } catch (Exception e2) {
            return null;
        }
    }

    public InputStream getResourceAsStreamFromJar(String str) {
        InputStream resourceAsStream;
        if (str == null) {
            throw new NullPointerException("name");
        }
        try {
            synchronized (this.syncResourceAsStreamFromJar) {
                this.resourceAsStreamFromJarInCall = true;
                resourceAsStream = super.getResourceAsStream(str);
                this.resourceAsStreamFromJarInCall = false;
            }
            return resourceAsStream;
        } catch (Exception e2) {
            return null;
        }
    }

    @Override // java.net.URLClassLoader, java.lang.ClassLoader
    public URL findResource(String str) {
        boolean z2;
        boolean z3;
        URL urlFindResource = super.findResource(str);
        if (str.startsWith("META-INF/")) {
            return urlFindResource;
        }
        if (!this.codebaseLookup) {
            return urlFindResource;
        }
        if (urlFindResource == null) {
            synchronized (this.syncResourceAsStreamFromJar) {
                z2 = this.resourceAsStreamFromJarInCall;
            }
            if (z2) {
                return null;
            }
            synchronized (this.syncResourceAsStream) {
                z3 = this.resourceAsStreamInCall;
            }
            if (!z3) {
                try {
                    urlFindResource = new URL(this.base, ParseUtil.encodePath(str, false));
                    if (!resourceExists(urlFindResource)) {
                        urlFindResource = null;
                    }
                } catch (Exception e2) {
                    urlFindResource = null;
                }
            }
        }
        return urlFindResource;
    }

    private boolean resourceExists(URL url) {
        boolean z2 = true;
        try {
            URLConnection uRLConnectionOpenConnection = url.openConnection();
            if (uRLConnectionOpenConnection instanceof HttpURLConnection) {
                HttpURLConnection httpURLConnection = (HttpURLConnection) uRLConnectionOpenConnection;
                httpURLConnection.setRequestMethod("HEAD");
                int responseCode = httpURLConnection.getResponseCode();
                if (responseCode == 200) {
                    return true;
                }
                if (responseCode >= 400) {
                    return false;
                }
            } else {
                uRLConnectionOpenConnection.getInputStream().close();
            }
        } catch (Exception e2) {
            z2 = false;
        }
        return z2;
    }

    @Override // java.net.URLClassLoader, java.lang.ClassLoader
    public Enumeration findResources(String str) throws IOException {
        final Enumeration<URL> enumerationFindResources = super.findResources(str);
        if (str.startsWith("META-INF/")) {
            return enumerationFindResources;
        }
        if (!this.codebaseLookup) {
            return enumerationFindResources;
        }
        URL url = new URL(this.base, ParseUtil.encodePath(str, false));
        if (!resourceExists(url)) {
            url = null;
        }
        final URL url2 = url;
        return new Enumeration() { // from class: sun.applet.AppletClassLoader.2
            private boolean done;

            @Override // java.util.Enumeration
            /* renamed from: nextElement */
            public Object nextElement2() {
                if (!this.done) {
                    if (enumerationFindResources.hasMoreElements()) {
                        return enumerationFindResources.nextElement2();
                    }
                    this.done = true;
                    if (url2 != null) {
                        return url2;
                    }
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                return !this.done && (enumerationFindResources.hasMoreElements() || url2 != null);
            }
        };
    }

    Class loadCode(String str) throws ClassNotFoundException {
        String strReplace = str.replace('/', '.').replace(File.separatorChar, '.');
        String strSubstring = null;
        int iIndexOf = strReplace.indexOf(";");
        if (iIndexOf != -1) {
            strSubstring = strReplace.substring(iIndexOf, strReplace.length());
            strReplace = strReplace.substring(0, iIndexOf);
        }
        String string = strReplace;
        if (strReplace.endsWith(".class") || strReplace.endsWith(".java")) {
            strReplace = strReplace.substring(0, strReplace.lastIndexOf(46));
        }
        if (strSubstring != null) {
            try {
                strReplace = new StringBuffer(strReplace).append(strSubstring).toString();
            } catch (ClassNotFoundException e2) {
                if (strSubstring != null) {
                    string = new StringBuffer(string).append(strSubstring).toString();
                }
                return loadClass(string);
            }
        }
        return loadClass(strReplace);
    }

    public ThreadGroup getThreadGroup() {
        AppletThreadGroup appletThreadGroup;
        synchronized (this.threadGroupSynchronizer) {
            if (this.threadGroup == null || this.threadGroup.isDestroyed()) {
                AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.applet.AppletClassLoader.3
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public Object run2() {
                        AppletClassLoader.this.threadGroup = new AppletThreadGroup(((Object) AppletClassLoader.this.base) + "-threadGroup");
                        AppContextCreator appContextCreator = new AppContextCreator(AppletClassLoader.this.threadGroup);
                        appContextCreator.setContextClassLoader(AppletClassLoader.this);
                        appContextCreator.start();
                        try {
                            synchronized (appContextCreator.syncObject) {
                                while (!appContextCreator.created) {
                                    appContextCreator.syncObject.wait();
                                }
                            }
                        } catch (InterruptedException e2) {
                        }
                        AppletClassLoader.this.appContext = appContextCreator.appContext;
                        return null;
                    }
                });
            }
            appletThreadGroup = this.threadGroup;
        }
        return appletThreadGroup;
    }

    public AppContext getAppContext() {
        return this.appContext;
    }

    public void grab() {
        synchronized (this.grabReleaseSynchronizer) {
            this.usageCount++;
        }
        getThreadGroup();
    }

    protected void setExceptionStatus() {
        this.exceptionStatus = true;
    }

    public boolean getExceptionStatus() {
        return this.exceptionStatus;
    }

    protected void release() {
        AppContext appContextResetAppContext = null;
        synchronized (this.grabReleaseSynchronizer) {
            if (this.usageCount > 1) {
                this.usageCount--;
            } else {
                synchronized (this.threadGroupSynchronizer) {
                    appContextResetAppContext = resetAppContext();
                }
            }
        }
        if (appContextResetAppContext != null) {
            try {
                appContextResetAppContext.dispose();
            } catch (IllegalThreadStateException e2) {
            }
        }
    }

    protected AppContext resetAppContext() {
        AppContext appContext;
        synchronized (this.threadGroupSynchronizer) {
            appContext = this.appContext;
            this.usageCount = 0;
            this.appContext = null;
            this.threadGroup = null;
        }
        return appContext;
    }

    void setJDK11Target(Class cls, boolean z2) {
        this.jdk11AppletInfo.put(cls.toString(), Boolean.valueOf(z2));
    }

    void setJDK12Target(Class cls, boolean z2) {
        this.jdk12AppletInfo.put(cls.toString(), Boolean.valueOf(z2));
    }

    Boolean isJDK11Target(Class cls) {
        return (Boolean) this.jdk11AppletInfo.get(cls.toString());
    }

    Boolean isJDK12Target(Class cls) {
        return (Boolean) this.jdk12AppletInfo.get(cls.toString());
    }

    private static void printError(String str, Throwable th) {
        String message = null;
        if (th == null) {
            message = mh.getMessage("filenotfound", str);
        } else if (th instanceof IOException) {
            message = mh.getMessage("fileioexception", str);
        } else if (th instanceof ClassFormatError) {
            message = mh.getMessage("fileformat", str);
        } else if (th instanceof ThreadDeath) {
            message = mh.getMessage("filedeath", str);
        } else if (th instanceof Error) {
            message = mh.getMessage("fileerror", th.toString(), str);
        }
        if (message != null) {
            System.err.println(message);
        }
    }
}
