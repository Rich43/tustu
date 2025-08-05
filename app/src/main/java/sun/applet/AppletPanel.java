package sun.applet;

import java.applet.Applet;
import java.applet.AppletStub;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.KeyboardFocusManager;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.InvocationEvent;
import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.SocketPermission;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessControlContext;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.jar.Pack200;
import sun.awt.AWTAccessor;
import sun.awt.AppContext;
import sun.awt.EmbeddedFrame;
import sun.awt.SunToolkit;
import sun.misc.MessageUtils;
import sun.misc.PerformanceLogger;
import sun.misc.Queue;
import sun.security.util.SecurityConstants;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:sun/applet/AppletPanel.class */
public abstract class AppletPanel extends Panel implements AppletStub, Runnable {
    Applet applet;
    protected AppletClassLoader loader;
    public static final int APPLET_DISPOSE = 0;
    public static final int APPLET_LOAD = 1;
    public static final int APPLET_INIT = 2;
    public static final int APPLET_START = 3;
    public static final int APPLET_STOP = 4;
    public static final int APPLET_DESTROY = 5;
    public static final int APPLET_QUIT = 6;
    public static final int APPLET_ERROR = 7;
    public static final int APPLET_RESIZE = 51234;
    public static final int APPLET_LOADING = 51235;
    public static final int APPLET_LOADING_COMPLETED = 51236;
    protected int status;
    protected Thread handler;
    private AppletListener listeners;
    private static int threadGroupNumber = 0;
    private static HashMap classloaders = new HashMap();
    private static AppletMessageHandler amh = new AppletMessageHandler("appletpanel");
    protected boolean doInit = true;
    Dimension defaultAppletSize = new Dimension(10, 10);
    Dimension currentAppletSize = new Dimension(10, 10);
    MessageUtils mu = new MessageUtils();
    Thread loaderThread = null;
    boolean loadAbortRequest = false;
    private Queue queue = null;
    private EventQueue appEvtQ = null;
    private boolean jdk11Applet = false;
    private boolean jdk12Applet = false;

    protected abstract String getCode();

    protected abstract String getJarFiles();

    protected abstract String getSerializedObject();

    @Override // java.awt.Component
    public abstract int getWidth();

    @Override // java.awt.Component
    public abstract int getHeight();

    public abstract boolean hasInitialFocus();

    protected void setupAppletAppContext() {
    }

    synchronized void createAppletThread() {
        String str = "applet-" + getCode();
        this.loader = getClassLoader(getCodeBase(), getClassLoaderCacheKey());
        this.loader.grab();
        String parameter = getParameter("codebase_lookup");
        if (parameter != null && parameter.equals("false")) {
            this.loader.setCodebaseLookup(false);
        } else {
            this.loader.setCodebaseLookup(true);
        }
        this.handler = new Thread(this.loader.getThreadGroup(), this, "thread " + str);
        AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.applet.AppletPanel.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                AppletPanel.this.handler.setContextClassLoader(AppletPanel.this.loader);
                return null;
            }
        });
        this.handler.start();
    }

    void joinAppletThread() throws InterruptedException {
        if (this.handler != null) {
            this.handler.join();
            this.handler = null;
        }
    }

    void release() {
        if (this.loader != null) {
            this.loader.release();
            this.loader = null;
        }
    }

    public void init() {
        try {
            this.defaultAppletSize.width = getWidth();
            this.currentAppletSize.width = this.defaultAppletSize.width;
            this.defaultAppletSize.height = getHeight();
            this.currentAppletSize.height = this.defaultAppletSize.height;
        } catch (NumberFormatException e2) {
            this.status = 7;
            showAppletStatus("badattribute.exception");
            showAppletLog("badattribute.exception");
            showAppletException(e2);
        }
        setLayout(new BorderLayout());
        createAppletThread();
    }

    @Override // java.awt.Container, java.awt.Component
    public Dimension minimumSize() {
        return new Dimension(this.defaultAppletSize.width, this.defaultAppletSize.height);
    }

    @Override // java.awt.Container, java.awt.Component
    public Dimension preferredSize() {
        return new Dimension(this.currentAppletSize.width, this.currentAppletSize.height);
    }

    public synchronized void addAppletListener(AppletListener appletListener) {
        this.listeners = AppletEventMulticaster.add(this.listeners, appletListener);
    }

    public synchronized void removeAppletListener(AppletListener appletListener) {
        this.listeners = AppletEventMulticaster.remove(this.listeners, appletListener);
    }

    public void dispatchAppletEvent(int i2, Object obj) {
        if (this.listeners != null) {
            this.listeners.appletStateChanged(new AppletEvent(this, i2, obj));
        }
    }

    public void sendEvent(int i2) {
        synchronized (this) {
            if (this.queue == null) {
                this.queue = new Queue();
            }
            this.queue.enqueue(Integer.valueOf(i2));
            notifyAll();
        }
        if (i2 == 6) {
            try {
                joinAppletThread();
            } catch (InterruptedException e2) {
            }
            if (this.loader == null) {
                this.loader = getClassLoader(getCodeBase(), getClassLoaderCacheKey());
            }
            release();
        }
    }

    synchronized AppletEvent getNextEvent() throws InterruptedException {
        while (true) {
            if (this.queue == null || this.queue.isEmpty()) {
                wait();
            } else {
                return new AppletEvent(this, ((Integer) this.queue.dequeue()).intValue(), null);
            }
        }
    }

    boolean emptyEventQueue() {
        if (this.queue == null || this.queue.isEmpty()) {
            return true;
        }
        return false;
    }

    private void setExceptionStatus(AccessControlException accessControlException) {
        Permission permission = accessControlException.getPermission();
        if ((permission instanceof RuntimePermission) && permission.getName().startsWith("modifyThread")) {
            if (this.loader == null) {
                this.loader = getClassLoader(getCodeBase(), getClassLoaderCacheKey());
            }
            this.loader.setExceptionStatus();
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        Thread threadCurrentThread = Thread.currentThread();
        if (threadCurrentThread == this.loaderThread) {
            runLoader();
            return;
        }
        boolean z2 = false;
        while (!z2 && !threadCurrentThread.isInterrupted()) {
            try {
                try {
                    switch (getNextEvent().getID()) {
                        case 0:
                            if (this.status != 5 && this.status != 1) {
                                showAppletStatus("notdestroyed");
                                break;
                            } else {
                                this.status = 0;
                                try {
                                    final Applet applet = this.applet;
                                    AWTAccessor.getEventQueueAccessor().invokeAndWait(this.applet, new Runnable() { // from class: sun.applet.AppletPanel.5
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            AppletPanel.this.remove(applet);
                                        }
                                    });
                                } catch (InterruptedException e2) {
                                } catch (InvocationTargetException e3) {
                                }
                                this.applet = null;
                                showAppletStatus(AppContext.DISPOSED_PROPERTY_NAME);
                                z2 = true;
                                break;
                            }
                        case 1:
                            if (okToLoad() && this.loaderThread == null) {
                                setLoaderThread(new Thread(this));
                                this.loaderThread.start();
                                this.loaderThread.join();
                                setLoaderThread(null);
                                break;
                            }
                            break;
                        case 2:
                            if (this.status != 1 && this.status != 5) {
                                showAppletStatus("notloaded");
                                break;
                            } else {
                                this.applet.resize(this.defaultAppletSize);
                                if (this.doInit) {
                                    if (PerformanceLogger.loggingEnabled()) {
                                        PerformanceLogger.setTime("Applet Init");
                                        PerformanceLogger.outputLog();
                                    }
                                    this.applet.init();
                                }
                                Font font = getFont();
                                if (font == null || ("dialog".equals(font.getFamily().toLowerCase(Locale.ENGLISH)) && font.getSize() == 12 && font.getStyle() == 0)) {
                                    setFont(new Font(Font.DIALOG, 0, 12));
                                }
                                this.doInit = true;
                                try {
                                    AWTAccessor.getEventQueueAccessor().invokeAndWait(this.applet, new Runnable() { // from class: sun.applet.AppletPanel.2
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            this.validate();
                                        }
                                    });
                                } catch (InterruptedException e4) {
                                } catch (InvocationTargetException e5) {
                                }
                                this.status = 2;
                                showAppletStatus("inited");
                                break;
                            }
                        case 3:
                            if (this.status != 2 && this.status != 4) {
                                showAppletStatus("notinited");
                                break;
                            } else {
                                this.applet.resize(this.currentAppletSize);
                                this.applet.start();
                                try {
                                    final Applet applet2 = this.applet;
                                    AWTAccessor.getEventQueueAccessor().invokeAndWait(this.applet, new Runnable() { // from class: sun.applet.AppletPanel.3
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            this.validate();
                                            applet2.setVisible(true);
                                            if (AppletPanel.this.hasInitialFocus()) {
                                                AppletPanel.this.setDefaultFocus();
                                            }
                                        }
                                    });
                                } catch (InterruptedException e6) {
                                } catch (InvocationTargetException e7) {
                                }
                                this.status = 3;
                                showAppletStatus("started");
                                break;
                            }
                        case 4:
                            if (this.status != 3) {
                                showAppletStatus("notstarted");
                                break;
                            } else {
                                this.status = 4;
                                try {
                                    final Applet applet3 = this.applet;
                                    AWTAccessor.getEventQueueAccessor().invokeAndWait(this.applet, new Runnable() { // from class: sun.applet.AppletPanel.4
                                        @Override // java.lang.Runnable
                                        public void run() {
                                            applet3.setVisible(false);
                                        }
                                    });
                                } catch (InterruptedException e8) {
                                } catch (InvocationTargetException e9) {
                                }
                                try {
                                    this.applet.stop();
                                    showAppletStatus("stopped");
                                    break;
                                } catch (AccessControlException e10) {
                                    setExceptionStatus(e10);
                                    throw e10;
                                }
                            }
                        case 5:
                            if (this.status != 4 && this.status != 2) {
                                showAppletStatus("notstopped");
                                break;
                            } else {
                                this.status = 5;
                                try {
                                    this.applet.destroy();
                                    showAppletStatus("destroyed");
                                    break;
                                } catch (AccessControlException e11) {
                                    setExceptionStatus(e11);
                                    throw e11;
                                }
                            }
                            break;
                        case 6:
                            return;
                    }
                } catch (Error e12) {
                    this.status = 7;
                    if (e12.getMessage() != null) {
                        showAppletStatus("error2", e12.getClass().getName(), e12.getMessage());
                    } else {
                        showAppletStatus(Pack200.Packer.ERROR, e12.getClass().getName());
                    }
                    showAppletException(e12);
                } catch (Exception e13) {
                    this.status = 7;
                    if (e13.getMessage() != null) {
                        showAppletStatus("exception2", e13.getClass().getName(), e13.getMessage());
                    } else {
                        showAppletStatus("exception", e13.getClass().getName());
                    }
                    showAppletException(e13);
                } catch (ThreadDeath e14) {
                    showAppletStatus("death");
                    return;
                }
                clearLoadAbortRequest();
            } catch (InterruptedException e15) {
                showAppletStatus("bail");
                return;
            }
        }
    }

    private Component getMostRecentFocusOwnerForWindow(Window window) {
        Method method = (Method) AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.applet.AppletPanel.6
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                Method declaredMethod = null;
                try {
                    declaredMethod = KeyboardFocusManager.class.getDeclaredMethod("getMostRecentFocusOwner", Window.class);
                    declaredMethod.setAccessible(true);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                return declaredMethod;
            }
        });
        if (method != null) {
            try {
                return (Component) method.invoke(null, window);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return window.getMostRecentFocusOwner();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDefaultFocus() {
        Container defaultComponent = null;
        Container parent = getParent();
        if (parent != null) {
            if (parent instanceof Window) {
                defaultComponent = getMostRecentFocusOwnerForWindow((Window) parent);
                if (defaultComponent == parent || defaultComponent == null) {
                    defaultComponent = parent.getFocusTraversalPolicy().getInitialComponent((Window) parent);
                }
            } else if (parent.isFocusCycleRoot()) {
                defaultComponent = parent.getFocusTraversalPolicy().getDefaultComponent(parent);
            }
        }
        if (defaultComponent != null) {
            if (parent instanceof EmbeddedFrame) {
                ((EmbeddedFrame) parent).synthesizeWindowActivation(true);
            }
            defaultComponent.requestFocusInWindow();
        }
    }

    private void runLoader() {
        if (this.status != 0) {
            showAppletStatus("notdisposed");
            return;
        }
        dispatchAppletEvent(APPLET_LOADING, null);
        this.status = 1;
        this.loader = getClassLoader(getCodeBase(), getClassLoaderCacheKey());
        String code = getCode();
        setupAppletAppContext();
        try {
            loadJarFiles(this.loader);
            this.applet = createApplet(this.loader);
            if (this.applet != null) {
                this.applet.setStub(this);
                this.applet.hide();
                add(BorderLayout.CENTER, this.applet);
                showAppletStatus("loaded");
                validate();
            }
        } catch (ClassNotFoundException e2) {
            this.status = 7;
            showAppletStatus("notfound", code);
            showAppletLog("notfound", code);
            showAppletException(e2);
        } catch (ThreadDeath e3) {
            this.status = 7;
            showAppletStatus("death");
        } catch (Error e4) {
            this.status = 7;
            showAppletStatus(Pack200.Packer.ERROR, e4.getMessage());
            showAppletException(e4);
        } catch (IllegalAccessException e5) {
            this.status = 7;
            showAppletStatus("noconstruct", code);
            showAppletLog("noconstruct", code);
            showAppletException(e5);
        } catch (InstantiationException e6) {
            this.status = 7;
            showAppletStatus("nocreate", code);
            showAppletLog("nocreate", code);
            showAppletException(e6);
        } catch (Exception e7) {
            this.status = 7;
            showAppletStatus("exception", e7.getMessage());
            showAppletException(e7);
        } finally {
            dispatchAppletEvent(APPLET_LOADING_COMPLETED, null);
        }
    }

    protected Applet createApplet(AppletClassLoader appletClassLoader) throws IllegalAccessException, InterruptedException, InstantiationException, IOException, ClassNotFoundException {
        String serializedObject = getSerializedObject();
        String code = getCode();
        if (code != null && serializedObject != null) {
            System.err.println(amh.getMessage("runloader.err"));
            throw new InstantiationException("Either \"code\" or \"object\" should be specified, but not both.");
        }
        if (code == null && serializedObject == null) {
            this.status = 7;
            showAppletStatus("nocode");
            showAppletLog("nocode");
            repaint();
        }
        if (code != null) {
            this.applet = (Applet) appletClassLoader.loadCode(code).newInstance();
            this.doInit = true;
        } else {
            InputStream inputStream = (InputStream) AccessController.doPrivileged(() -> {
                return appletClassLoader.getResourceAsStream(serializedObject);
            });
            Throwable th = null;
            try {
                AppletObjectInputStream appletObjectInputStream = new AppletObjectInputStream(inputStream, appletClassLoader);
                Throwable th2 = null;
                try {
                    try {
                        this.applet = (Applet) appletObjectInputStream.readObject();
                        this.doInit = false;
                        if (appletObjectInputStream != null) {
                            if (0 != 0) {
                                try {
                                    appletObjectInputStream.close();
                                } catch (Throwable th3) {
                                    th2.addSuppressed(th3);
                                }
                            } else {
                                appletObjectInputStream.close();
                            }
                        }
                    } catch (Throwable th4) {
                        if (appletObjectInputStream != null) {
                            if (th2 != null) {
                                try {
                                    appletObjectInputStream.close();
                                } catch (Throwable th5) {
                                    th2.addSuppressed(th5);
                                }
                            } else {
                                appletObjectInputStream.close();
                            }
                        }
                        throw th4;
                    }
                } finally {
                }
            } finally {
                if (inputStream != null) {
                    if (0 != 0) {
                        try {
                            inputStream.close();
                        } catch (Throwable th6) {
                            th.addSuppressed(th6);
                        }
                    } else {
                        inputStream.close();
                    }
                }
            }
        }
        findAppletJDKLevel(this.applet);
        if (!Thread.interrupted()) {
            return this.applet;
        }
        try {
            this.status = 0;
            this.applet = null;
            showAppletStatus("death");
            Thread.currentThread().interrupt();
            return null;
        } catch (Throwable th7) {
            Thread.currentThread().interrupt();
            throw th7;
        }
    }

    protected void loadJarFiles(AppletClassLoader appletClassLoader) throws InterruptedException, IOException {
        String jarFiles = getJarFiles();
        if (jarFiles != null) {
            StringTokenizer stringTokenizer = new StringTokenizer(jarFiles, ",", false);
            while (stringTokenizer.hasMoreTokens()) {
                try {
                    appletClassLoader.addJar(stringTokenizer.nextToken().trim());
                } catch (IllegalArgumentException e2) {
                }
            }
        }
    }

    protected synchronized void stopLoading() {
        if (this.loaderThread != null) {
            this.loaderThread.interrupt();
        } else {
            setLoadAbortRequest();
        }
    }

    protected synchronized boolean okToLoad() {
        return !this.loadAbortRequest;
    }

    protected synchronized void clearLoadAbortRequest() {
        this.loadAbortRequest = false;
    }

    protected synchronized void setLoadAbortRequest() {
        this.loadAbortRequest = true;
    }

    private synchronized void setLoaderThread(Thread thread) {
        this.loaderThread = thread;
    }

    @Override // java.applet.AppletStub
    public boolean isActive() {
        return this.status == 3;
    }

    @Override // java.applet.AppletStub
    public void appletResize(int i2, int i3) {
        AppContext appContext;
        this.currentAppletSize.width = i2;
        this.currentAppletSize.height = i3;
        final Dimension dimension = new Dimension(this.currentAppletSize.width, this.currentAppletSize.height);
        if (this.loader != null && (appContext = this.loader.getAppContext()) != null) {
            this.appEvtQ = (EventQueue) appContext.get(AppContext.EVENT_QUEUE_KEY);
        }
        if (this.appEvtQ != null) {
            this.appEvtQ.postEvent(new InvocationEvent(Toolkit.getDefaultToolkit(), new Runnable() { // from class: sun.applet.AppletPanel.7
                @Override // java.lang.Runnable
                public void run() {
                    if (this != null) {
                        this.dispatchAppletEvent(AppletPanel.APPLET_RESIZE, dimension);
                    }
                }
            }));
        }
    }

    @Override // java.awt.Component
    public void setBounds(int i2, int i3, int i4, int i5) {
        super.setBounds(i2, i3, i4, i5);
        this.currentAppletSize.width = i4;
        this.currentAppletSize.height = i5;
    }

    public Applet getApplet() {
        return this.applet;
    }

    protected void showAppletStatus(String str) {
        getAppletContext().showStatus(amh.getMessage(str));
    }

    protected void showAppletStatus(String str, Object obj) {
        getAppletContext().showStatus(amh.getMessage(str, obj));
    }

    protected void showAppletStatus(String str, Object obj, Object obj2) {
        getAppletContext().showStatus(amh.getMessage(str, obj, obj2));
    }

    protected void showAppletLog(String str) {
        System.out.println(amh.getMessage(str));
    }

    protected void showAppletLog(String str, Object obj) {
        System.out.println(amh.getMessage(str, obj));
    }

    protected void showAppletException(Throwable th) {
        th.printStackTrace();
        repaint();
    }

    public String getClassLoaderCacheKey() {
        return getCodeBase().toString();
    }

    public static synchronized void flushClassLoader(String str) {
        classloaders.remove(str);
    }

    public static synchronized void flushClassLoaders() {
        classloaders = new HashMap();
    }

    protected AppletClassLoader createClassLoader(URL url) {
        return new AppletClassLoader(url);
    }

    synchronized AppletClassLoader getClassLoader(final URL url, final String str) {
        AppletClassLoader appletClassLoader = (AppletClassLoader) classloaders.get(str);
        if (appletClassLoader == null) {
            appletClassLoader = (AppletClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.applet.AppletPanel.8
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Object run2() {
                    AppletClassLoader appletClassLoaderCreateClassLoader = AppletPanel.this.createClassLoader(url);
                    synchronized (getClass()) {
                        AppletClassLoader appletClassLoader2 = (AppletClassLoader) AppletPanel.classloaders.get(str);
                        if (appletClassLoader2 == null) {
                            AppletPanel.classloaders.put(str, appletClassLoaderCreateClassLoader);
                            return appletClassLoaderCreateClassLoader;
                        }
                        return appletClassLoader2;
                    }
                }
            }, getAccessControlContext(url));
        }
        return appletClassLoader;
    }

    private AccessControlContext getAccessControlContext(URL url) {
        Permission permission;
        PermissionCollection permissions = (PermissionCollection) AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.applet.AppletPanel.9
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                Policy policy = Policy.getPolicy();
                if (policy != null) {
                    return policy.getPermissions(new CodeSource((URL) null, (Certificate[]) null));
                }
                return null;
            }
        });
        if (permissions == null) {
            permissions = new Permissions();
        }
        permissions.add(SecurityConstants.CREATE_CLASSLOADER_PERMISSION);
        URLConnection uRLConnectionOpenConnection = null;
        try {
            uRLConnectionOpenConnection = url.openConnection();
            permission = uRLConnectionOpenConnection.getPermission();
        } catch (IOException e2) {
            permission = null;
        }
        if (permission != null) {
            permissions.add(permission);
        }
        if (permission instanceof FilePermission) {
            String name = permission.getName();
            int iLastIndexOf = name.lastIndexOf(File.separatorChar);
            if (iLastIndexOf != -1) {
                String strSubstring = name.substring(0, iLastIndexOf + 1);
                if (strSubstring.endsWith(File.separator)) {
                    strSubstring = strSubstring + LanguageTag.SEP;
                }
                permissions.add(new FilePermission(strSubstring, "read"));
            }
        } else {
            URL jarFileURL = url;
            if (uRLConnectionOpenConnection instanceof JarURLConnection) {
                jarFileURL = ((JarURLConnection) uRLConnectionOpenConnection).getJarFileURL();
            }
            String host = jarFileURL.getHost();
            if (host != null && host.length() > 0) {
                permissions.add(new SocketPermission(host, SecurityConstants.SOCKET_CONNECT_ACCEPT_ACTION));
            }
        }
        return new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(new CodeSource(url, (Certificate[]) null), permissions)});
    }

    public Thread getAppletHandlerThread() {
        return this.handler;
    }

    public int getAppletWidth() {
        return this.currentAppletSize.width;
    }

    public int getAppletHeight() {
        return this.currentAppletSize.height;
    }

    public static void changeFrameAppContext(Frame frame, AppContext appContext) {
        AppContext appContextTargetToAppContext = SunToolkit.targetToAppContext(frame);
        if (appContextTargetToAppContext == appContext) {
            return;
        }
        synchronized (Window.class) {
            WeakReference weakReference = null;
            Vector vector = (Vector) appContextTargetToAppContext.get(Window.class);
            if (vector != null) {
                Iterator it = vector.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    WeakReference weakReference2 = (WeakReference) it.next();
                    if (weakReference2.get() == frame) {
                        weakReference = weakReference2;
                        break;
                    }
                }
                if (weakReference != null) {
                    vector.remove(weakReference);
                }
            }
            SunToolkit.insertTargetMapping(frame, appContext);
            Vector vector2 = (Vector) appContext.get(Window.class);
            if (vector2 == null) {
                vector2 = new Vector();
                appContext.put(Window.class, vector2);
            }
            vector2.add(weakReference);
        }
    }

    private void findAppletJDKLevel(Applet applet) {
        Class<?> cls = applet.getClass();
        synchronized (cls) {
            Boolean boolIsJDK11Target = this.loader.isJDK11Target(cls);
            Boolean boolIsJDK12Target = this.loader.isJDK12Target(cls);
            if (boolIsJDK11Target != null || boolIsJDK12Target != null) {
                this.jdk11Applet = boolIsJDK11Target == null ? false : boolIsJDK11Target.booleanValue();
                this.jdk12Applet = boolIsJDK12Target == null ? false : boolIsJDK12Target.booleanValue();
                return;
            }
            String str = cls.getName().replace('.', '/') + ".class";
            byte[] bArr = new byte[8];
            try {
                InputStream inputStream = (InputStream) AccessController.doPrivileged(() -> {
                    return this.loader.getResourceAsStream(str);
                });
                Throwable th = null;
                try {
                    try {
                        if (inputStream.read(bArr, 0, 8) != 8) {
                            if (inputStream != null) {
                                if (0 != 0) {
                                    try {
                                        inputStream.close();
                                    } catch (Throwable th2) {
                                        th.addSuppressed(th2);
                                    }
                                } else {
                                    inputStream.close();
                                }
                            }
                            return;
                        }
                        if (inputStream != null) {
                            if (0 != 0) {
                                try {
                                    inputStream.close();
                                } catch (Throwable th3) {
                                    th.addSuppressed(th3);
                                }
                            } else {
                                inputStream.close();
                            }
                        }
                        int i2 = readShort(bArr, 6);
                        if (i2 < 46) {
                            this.jdk11Applet = true;
                        } else if (i2 == 46) {
                            this.jdk12Applet = true;
                        }
                        this.loader.setJDK11Target(cls, this.jdk11Applet);
                        this.loader.setJDK12Target(cls, this.jdk12Applet);
                    } catch (Throwable th4) {
                        if (inputStream != null) {
                            if (th != null) {
                                try {
                                    inputStream.close();
                                } catch (Throwable th5) {
                                    th.addSuppressed(th5);
                                }
                            } else {
                                inputStream.close();
                            }
                        }
                        throw th4;
                    }
                } finally {
                }
            } catch (IOException e2) {
            }
        }
    }

    protected boolean isJDK11Applet() {
        return this.jdk11Applet;
    }

    protected boolean isJDK12Applet() {
        return this.jdk12Applet;
    }

    private int readShort(byte[] bArr, int i2) {
        return (readByte(bArr[i2]) << 8) | readByte(bArr[i2 + 1]);
    }

    private int readByte(byte b2) {
        return b2 & 255;
    }
}
