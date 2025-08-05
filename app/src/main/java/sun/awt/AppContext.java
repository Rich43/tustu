package sun.awt;

import java.awt.EventQueue;
import java.awt.GraphicsEnvironment;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.Window;
import java.awt.event.InvocationEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.ref.SoftReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import sun.java2d.marlin.MarlinConst;
import sun.misc.JavaAWTAccess;
import sun.misc.SharedSecrets;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:sun/awt/AppContext.class */
public final class AppContext {
    private final ThreadGroup threadGroup;
    public static final String DISPOSED_PROPERTY_NAME = "disposed";
    public static final String GUI_DISPOSED = "guidisposed";
    private final ClassLoader contextClassLoader;
    private static final PlatformLogger log = PlatformLogger.getLogger("sun.awt.AppContext");
    public static final Object EVENT_QUEUE_KEY = new StringBuffer("EventQueue");
    public static final Object EVENT_QUEUE_LOCK_KEY = new StringBuilder("EventQueue.Lock");
    public static final Object EVENT_QUEUE_COND_KEY = new StringBuilder("EventQueue.Condition");
    private static final Map<ThreadGroup, AppContext> threadGroup2appContext = Collections.synchronizedMap(new IdentityHashMap());
    private static volatile AppContext mainAppContext = null;
    private static final Object getAppContextLock = new GetAppContextLock();
    private static final AtomicInteger numAppContexts = new AtomicInteger(0);
    private static final ThreadLocal<AppContext> threadAppContext = new ThreadLocal<>();
    private final Map<Object, Object> table = new HashMap();
    private PropertyChangeSupport changeSupport = null;
    private volatile State state = State.VALID;
    private long DISPOSAL_TIMEOUT = MarlinConst.statDump;
    private long THREAD_INTERRUPT_TIMEOUT = 1000;
    private MostRecentKeyValue mostRecentKeyValue = null;
    private MostRecentKeyValue shadowMostRecentKeyValue = null;

    /* loaded from: rt.jar:sun/awt/AppContext$State.class */
    private enum State {
        VALID,
        BEING_DISPOSED,
        DISPOSED
    }

    static {
        SharedSecrets.setJavaAWTAccess(new JavaAWTAccess() { // from class: sun.awt.AppContext.6
            private boolean hasRootThreadGroup(final AppContext appContext) {
                return ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: sun.awt.AppContext.6.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    public Boolean run() {
                        return Boolean.valueOf(appContext.threadGroup.getParent() == null);
                    }
                })).booleanValue();
            }

            @Override // sun.misc.JavaAWTAccess
            public Object getAppletContext() {
                if (AppContext.numAppContexts.get() == 0) {
                    return null;
                }
                AppContext executionAppContext = AppContext.getExecutionAppContext();
                if (AppContext.numAppContexts.get() > 0) {
                    executionAppContext = executionAppContext != null ? executionAppContext : AppContext.getAppContext();
                }
                if (executionAppContext == null || AppContext.mainAppContext == executionAppContext || (AppContext.mainAppContext == null && hasRootThreadGroup(executionAppContext))) {
                    return null;
                }
                return executionAppContext;
            }
        });
    }

    public static Set<AppContext> getAppContexts() {
        HashSet hashSet;
        synchronized (threadGroup2appContext) {
            hashSet = new HashSet(threadGroup2appContext.values());
        }
        return hashSet;
    }

    /* loaded from: rt.jar:sun/awt/AppContext$GetAppContextLock.class */
    private static class GetAppContextLock {
        private GetAppContextLock() {
        }
    }

    public boolean isDisposed() {
        return this.state == State.DISPOSED;
    }

    AppContext(ThreadGroup threadGroup) {
        numAppContexts.incrementAndGet();
        this.threadGroup = threadGroup;
        threadGroup2appContext.put(threadGroup, this);
        this.contextClassLoader = (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() { // from class: sun.awt.AppContext.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public ClassLoader run() {
                return Thread.currentThread().getContextClassLoader();
            }
        });
        ReentrantLock reentrantLock = new ReentrantLock();
        put(EVENT_QUEUE_LOCK_KEY, reentrantLock);
        put(EVENT_QUEUE_COND_KEY, reentrantLock.newCondition());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void initMainAppContext() {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.awt.AppContext.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Void run() {
                ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
                ThreadGroup parent = threadGroup.getParent();
                while (true) {
                    ThreadGroup threadGroup2 = parent;
                    if (threadGroup2 == null) {
                        AppContext unused = AppContext.mainAppContext = SunToolkit.createNewAppContext(threadGroup);
                        return null;
                    }
                    threadGroup = threadGroup2;
                    parent = threadGroup.getParent();
                }
            }
        });
    }

    public static final AppContext getAppContext() {
        if (numAppContexts.get() == 1 && mainAppContext != null) {
            return mainAppContext;
        }
        AppContext appContext = threadAppContext.get();
        if (null == appContext) {
            appContext = (AppContext) AccessController.doPrivileged(new PrivilegedAction<AppContext>() { // from class: sun.awt.AppContext.3
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public AppContext run() {
                    ThreadGroup threadGroup;
                    ThreadGroup threadGroup2 = Thread.currentThread().getThreadGroup();
                    ThreadGroup parent = threadGroup2;
                    synchronized (AppContext.getAppContextLock) {
                        if (AppContext.numAppContexts.get() == 0) {
                            if (System.getProperty("javaplugin.version") == null && System.getProperty("javawebstart.version") == null) {
                                AppContext.initMainAppContext();
                            } else if (System.getProperty("javafx.version") != null && parent.getParent() != null) {
                                SunToolkit.createNewAppContext();
                            }
                        }
                    }
                    Object obj = AppContext.threadGroup2appContext.get(parent);
                    while (true) {
                        AppContext appContext2 = (AppContext) obj;
                        if (appContext2 == null) {
                            parent = parent.getParent();
                            if (parent != null) {
                                obj = AppContext.threadGroup2appContext.get(parent);
                            } else {
                                SecurityManager securityManager = System.getSecurityManager();
                                if (securityManager != null && (threadGroup = securityManager.getThreadGroup()) != null) {
                                    return (AppContext) AppContext.threadGroup2appContext.get(threadGroup);
                                }
                                return null;
                            }
                        } else {
                            ThreadGroup parent2 = threadGroup2;
                            while (true) {
                                ThreadGroup threadGroup3 = parent2;
                                if (threadGroup3 != parent) {
                                    AppContext.threadGroup2appContext.put(threadGroup3, appContext2);
                                    parent2 = threadGroup3.getParent();
                                } else {
                                    AppContext.threadAppContext.set(appContext2);
                                    return appContext2;
                                }
                            }
                        }
                    }
                }
            });
        }
        return appContext;
    }

    public static final boolean isMainContext(AppContext appContext) {
        return appContext != null && appContext == mainAppContext;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final AppContext getExecutionAppContext() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null && (securityManager instanceof AWTSecurityManager)) {
            return ((AWTSecurityManager) securityManager).getAppContext();
        }
        return null;
    }

    public void dispose() throws IllegalThreadStateException {
        if (this.threadGroup.parentOf(Thread.currentThread().getThreadGroup())) {
            throw new IllegalThreadStateException("Current Thread is contained within AppContext to be disposed.");
        }
        synchronized (this) {
            if (this.state != State.VALID) {
                return;
            }
            this.state = State.BEING_DISPOSED;
            final PropertyChangeSupport propertyChangeSupport = this.changeSupport;
            if (propertyChangeSupport != null) {
                propertyChangeSupport.firePropertyChange(DISPOSED_PROPERTY_NAME, false, true);
            }
            final Object obj = new Object();
            Runnable runnable = new Runnable() { // from class: sun.awt.AppContext.4
                @Override // java.lang.Runnable
                public void run() {
                    for (Window window : Window.getOwnerlessWindows()) {
                        try {
                            window.dispose();
                        } catch (Throwable th) {
                            AppContext.log.finer("exception occurred while disposing app context", th);
                        }
                    }
                    AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.awt.AppContext.4.1
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedAction
                        public Void run() {
                            if (!GraphicsEnvironment.isHeadless() && SystemTray.isSupported()) {
                                SystemTray systemTray = SystemTray.getSystemTray();
                                for (TrayIcon trayIcon : systemTray.getTrayIcons()) {
                                    systemTray.remove(trayIcon);
                                }
                                return null;
                            }
                            return null;
                        }
                    });
                    if (propertyChangeSupport != null) {
                        propertyChangeSupport.firePropertyChange(AppContext.GUI_DISPOSED, false, true);
                    }
                    synchronized (obj) {
                        obj.notifyAll();
                    }
                }
            };
            synchronized (obj) {
                SunToolkit.postEvent(this, new InvocationEvent(Toolkit.getDefaultToolkit(), runnable));
                try {
                    obj.wait(this.DISPOSAL_TIMEOUT);
                } catch (InterruptedException e2) {
                }
            }
            Runnable runnable2 = new Runnable() { // from class: sun.awt.AppContext.5
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (obj) {
                        obj.notifyAll();
                    }
                }
            };
            synchronized (obj) {
                SunToolkit.postEvent(this, new InvocationEvent(Toolkit.getDefaultToolkit(), runnable2));
                try {
                    obj.wait(this.DISPOSAL_TIMEOUT);
                } catch (InterruptedException e3) {
                }
            }
            synchronized (this) {
                this.state = State.DISPOSED;
            }
            this.threadGroup.interrupt();
            long jCurrentTimeMillis = System.currentTimeMillis() + this.THREAD_INTERRUPT_TIMEOUT;
            while (this.threadGroup.activeCount() > 0 && System.currentTimeMillis() < jCurrentTimeMillis) {
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException e4) {
                }
            }
            this.threadGroup.stop();
            long jCurrentTimeMillis2 = System.currentTimeMillis() + this.THREAD_INTERRUPT_TIMEOUT;
            while (this.threadGroup.activeCount() > 0 && System.currentTimeMillis() < jCurrentTimeMillis2) {
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException e5) {
                }
            }
            int iActiveGroupCount = this.threadGroup.activeGroupCount();
            if (iActiveGroupCount > 0) {
                ThreadGroup[] threadGroupArr = new ThreadGroup[iActiveGroupCount];
                int iEnumerate = this.threadGroup.enumerate(threadGroupArr);
                for (int i2 = 0; i2 < iEnumerate; i2++) {
                    threadGroup2appContext.remove(threadGroupArr[i2]);
                }
            }
            threadGroup2appContext.remove(this.threadGroup);
            threadAppContext.set(null);
            try {
                this.threadGroup.destroy();
            } catch (IllegalThreadStateException e6) {
            }
            synchronized (this.table) {
                this.table.clear();
            }
            numAppContexts.decrementAndGet();
            this.mostRecentKeyValue = null;
        }
    }

    /* loaded from: rt.jar:sun/awt/AppContext$PostShutdownEventRunnable.class */
    static final class PostShutdownEventRunnable implements Runnable {
        private final AppContext appContext;

        public PostShutdownEventRunnable(AppContext appContext) {
            this.appContext = appContext;
        }

        @Override // java.lang.Runnable
        public void run() {
            EventQueue eventQueue = (EventQueue) this.appContext.get(AppContext.EVENT_QUEUE_KEY);
            if (eventQueue != null) {
                eventQueue.postEvent(AWTAutoShutdown.getShutdownEvent());
            }
        }
    }

    /* loaded from: rt.jar:sun/awt/AppContext$CreateThreadAction.class */
    static final class CreateThreadAction implements PrivilegedAction<Thread> {
        private final AppContext appContext;
        private final Runnable runnable;

        public CreateThreadAction(AppContext appContext, Runnable runnable) {
            this.appContext = appContext;
            this.runnable = runnable;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        public Thread run() {
            Thread thread = new Thread(this.appContext.getThreadGroup(), this.runnable);
            thread.setContextClassLoader(this.appContext.getContextClassLoader());
            thread.setPriority(6);
            thread.setDaemon(true);
            return thread;
        }
    }

    static void stopEventDispatchThreads() {
        for (AppContext appContext : getAppContexts()) {
            if (!appContext.isDisposed()) {
                PostShutdownEventRunnable postShutdownEventRunnable = new PostShutdownEventRunnable(appContext);
                if (appContext != getAppContext()) {
                    ((Thread) AccessController.doPrivileged(new CreateThreadAction(appContext, postShutdownEventRunnable))).start();
                } else {
                    postShutdownEventRunnable.run();
                }
            }
        }
    }

    public Object get(Object obj) {
        synchronized (this.table) {
            MostRecentKeyValue mostRecentKeyValue = this.mostRecentKeyValue;
            if (mostRecentKeyValue != null && mostRecentKeyValue.key == obj) {
                return mostRecentKeyValue.value;
            }
            Object obj2 = this.table.get(obj);
            if (this.mostRecentKeyValue == null) {
                this.mostRecentKeyValue = new MostRecentKeyValue(obj, obj2);
                this.shadowMostRecentKeyValue = new MostRecentKeyValue(obj, obj2);
            } else {
                MostRecentKeyValue mostRecentKeyValue2 = this.mostRecentKeyValue;
                this.shadowMostRecentKeyValue.setPair(obj, obj2);
                this.mostRecentKeyValue = this.shadowMostRecentKeyValue;
                this.shadowMostRecentKeyValue = mostRecentKeyValue2;
            }
            return obj2;
        }
    }

    public Object put(Object obj, Object obj2) {
        Object objPut;
        synchronized (this.table) {
            MostRecentKeyValue mostRecentKeyValue = this.mostRecentKeyValue;
            if (mostRecentKeyValue != null && mostRecentKeyValue.key == obj) {
                mostRecentKeyValue.value = obj2;
            }
            objPut = this.table.put(obj, obj2);
        }
        return objPut;
    }

    public Object remove(Object obj) {
        Object objRemove;
        synchronized (this.table) {
            MostRecentKeyValue mostRecentKeyValue = this.mostRecentKeyValue;
            if (mostRecentKeyValue != null && mostRecentKeyValue.key == obj) {
                mostRecentKeyValue.value = null;
            }
            objRemove = this.table.remove(obj);
        }
        return objRemove;
    }

    public ThreadGroup getThreadGroup() {
        return this.threadGroup;
    }

    public ClassLoader getContextClassLoader() {
        return this.contextClassLoader;
    }

    public String toString() {
        return getClass().getName() + "[threadGroup=" + this.threadGroup.getName() + "]";
    }

    public synchronized PropertyChangeListener[] getPropertyChangeListeners() {
        if (this.changeSupport == null) {
            return new PropertyChangeListener[0];
        }
        return this.changeSupport.getPropertyChangeListeners();
    }

    public synchronized void addPropertyChangeListener(String str, PropertyChangeListener propertyChangeListener) {
        if (propertyChangeListener == null) {
            return;
        }
        if (this.changeSupport == null) {
            this.changeSupport = new PropertyChangeSupport(this);
        }
        this.changeSupport.addPropertyChangeListener(str, propertyChangeListener);
    }

    public synchronized void removePropertyChangeListener(String str, PropertyChangeListener propertyChangeListener) {
        if (propertyChangeListener == null || this.changeSupport == null) {
            return;
        }
        this.changeSupport.removePropertyChangeListener(str, propertyChangeListener);
    }

    public synchronized PropertyChangeListener[] getPropertyChangeListeners(String str) {
        if (this.changeSupport == null) {
            return new PropertyChangeListener[0];
        }
        return this.changeSupport.getPropertyChangeListeners(str);
    }

    public static <T> T getSoftReferenceValue(Object obj, Supplier<T> supplier) {
        T t2;
        AppContext appContext = getAppContext();
        SoftReference softReference = (SoftReference) appContext.get(obj);
        if (softReference != null && (t2 = (T) softReference.get()) != null) {
            return t2;
        }
        T t3 = supplier.get();
        appContext.put(obj, new SoftReference(t3));
        return t3;
    }
}
