package java.util.logging;

import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.Permission;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.WeakHashMap;
import org.icepdf.core.util.PdfOps;
import sun.misc.JavaAWTAccess;
import sun.misc.SharedSecrets;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:java/util/logging/LogManager.class */
public class LogManager {
    private static final LogManager manager;
    private volatile Properties props;
    private static final Level defaultLevel;
    private final Map<Object, Integer> listenerMap;
    private final LoggerContext systemContext;
    private final LoggerContext userContext;
    private volatile Logger rootLogger;
    private volatile boolean readPrimordialConfiguration;
    private boolean initializedGlobalHandlers;
    private boolean deathImminent;
    private boolean initializedCalled;
    private volatile boolean initializationDone;
    private WeakHashMap<Object, LoggerContext> contextsMap;
    private final ReferenceQueue<Logger> loggerRefQueue;
    private static final int MAX_ITERATIONS = 400;
    private final Permission controlPermission;
    private static LoggingMXBean loggingMXBean;
    public static final String LOGGING_MXBEAN_NAME = "java.util.logging:type=Logging";
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !LogManager.class.desiredAssertionStatus();
        defaultLevel = Level.INFO;
        manager = (LogManager) AccessController.doPrivileged(new PrivilegedAction<LogManager>() { // from class: java.util.logging.LogManager.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public LogManager run2() {
                LogManager logManager = null;
                String property = null;
                try {
                    property = System.getProperty("java.util.logging.manager");
                    if (property != null) {
                        try {
                            logManager = (LogManager) ClassLoader.getSystemClassLoader().loadClass(property).newInstance();
                        } catch (ClassNotFoundException e2) {
                            logManager = (LogManager) Thread.currentThread().getContextClassLoader().loadClass(property).newInstance();
                        }
                    }
                } catch (Exception e3) {
                    System.err.println("Could not load Logmanager \"" + property + PdfOps.DOUBLE_QUOTE__TOKEN);
                    e3.printStackTrace();
                }
                if (logManager == null) {
                    logManager = new LogManager();
                }
                return logManager;
            }
        });
        loggingMXBean = null;
    }

    /* loaded from: rt.jar:java/util/logging/LogManager$Cleaner.class */
    private class Cleaner extends Thread {
        private Cleaner() {
            setContextClassLoader(null);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() throws SecurityException {
            LogManager logManager = LogManager.manager;
            synchronized (LogManager.this) {
                LogManager.this.deathImminent = true;
                LogManager.this.initializedGlobalHandlers = true;
            }
            LogManager.this.reset();
        }
    }

    protected LogManager() {
        this(checkSubclassPermissions());
    }

    private LogManager(Void r7) {
        this.props = new Properties();
        this.listenerMap = new HashMap();
        this.systemContext = new SystemLoggerContext();
        this.userContext = new LoggerContext();
        this.initializedGlobalHandlers = true;
        this.initializedCalled = false;
        this.initializationDone = false;
        this.contextsMap = null;
        this.loggerRefQueue = new ReferenceQueue<>();
        this.controlPermission = new LoggingPermission("control", null);
        try {
            Runtime.getRuntime().addShutdownHook(new Cleaner());
        } catch (IllegalStateException e2) {
        }
    }

    private static Void checkSubclassPermissions() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("shutdownHooks"));
            securityManager.checkPermission(new RuntimePermission("setContextClassLoader"));
            return null;
        }
        return null;
    }

    final void ensureLogManagerInitialized() {
        if (this.initializationDone || this != manager) {
            return;
        }
        synchronized (this) {
            boolean z2 = this.initializedCalled;
            if (!$assertionsDisabled && !this.initializedCalled && this.initializationDone) {
                throw new AssertionError((Object) "Initialization can't be done if initialized has not been called!");
            }
            if (z2 || this.initializationDone) {
                return;
            }
            this.initializedCalled = true;
            try {
                AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: java.util.logging.LogManager.2
                    static final /* synthetic */ boolean $assertionsDisabled;

                    static {
                        $assertionsDisabled = !LogManager.class.desiredAssertionStatus();
                    }

                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public Object run2() throws SecurityException {
                        if (!$assertionsDisabled && LogManager.this.rootLogger != null) {
                            throw new AssertionError();
                        }
                        if (!$assertionsDisabled && (!LogManager.this.initializedCalled || LogManager.this.initializationDone)) {
                            throw new AssertionError();
                        }
                        this.readPrimordialConfiguration();
                        LogManager logManager = this;
                        LogManager logManager2 = this;
                        logManager2.getClass();
                        logManager.rootLogger = new RootLogger();
                        this.addLogger(this.rootLogger);
                        if (!this.rootLogger.isLevelInitialized()) {
                            this.rootLogger.setLevel(LogManager.defaultLevel);
                        }
                        this.addLogger(Logger.global);
                        return null;
                    }
                });
                this.initializationDone = true;
            } catch (Throwable th) {
                this.initializationDone = true;
                throw th;
            }
        }
    }

    public static LogManager getLogManager() {
        if (manager != null) {
            manager.ensureLogManagerInitialized();
        }
        return manager;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void readPrimordialConfiguration() {
        if (!this.readPrimordialConfiguration) {
            synchronized (this) {
                if (!this.readPrimordialConfiguration) {
                    if (System.out == null) {
                        return;
                    }
                    this.readPrimordialConfiguration = true;
                    try {
                        AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() { // from class: java.util.logging.LogManager.3
                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // java.security.PrivilegedExceptionAction
                            public Void run() throws Exception {
                                LogManager.this.readConfiguration();
                                PlatformLogger.redirectPlatformLoggers();
                                return null;
                            }
                        });
                    } catch (Exception e2) {
                        if (!$assertionsDisabled) {
                            throw new AssertionError((Object) ("Exception raised while reading logging configuration: " + ((Object) e2)));
                        }
                    }
                }
            }
        }
    }

    @Deprecated
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) throws SecurityException {
        PropertyChangeListener propertyChangeListener2 = (PropertyChangeListener) Objects.requireNonNull(propertyChangeListener);
        checkPermission();
        synchronized (this.listenerMap) {
            Integer num = this.listenerMap.get(propertyChangeListener2);
            this.listenerMap.put(propertyChangeListener2, Integer.valueOf(num == null ? 1 : num.intValue() + 1));
        }
    }

    @Deprecated
    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) throws SecurityException {
        checkPermission();
        if (propertyChangeListener != null) {
            synchronized (this.listenerMap) {
                Integer num = this.listenerMap.get(propertyChangeListener);
                if (num != null) {
                    int iIntValue = num.intValue();
                    if (iIntValue == 1) {
                        this.listenerMap.remove(propertyChangeListener);
                    } else {
                        if (!$assertionsDisabled && iIntValue <= 1) {
                            throw new AssertionError();
                        }
                        this.listenerMap.put(propertyChangeListener, Integer.valueOf(iIntValue - 1));
                    }
                }
            }
        }
    }

    private LoggerContext getUserContext() {
        Object appletContext;
        LoggerContext loggerContext = null;
        SecurityManager securityManager = System.getSecurityManager();
        JavaAWTAccess javaAWTAccess = SharedSecrets.getJavaAWTAccess();
        if (securityManager != null && javaAWTAccess != null && (appletContext = javaAWTAccess.getAppletContext()) != null) {
            synchronized (javaAWTAccess) {
                if (this.contextsMap == null) {
                    this.contextsMap = new WeakHashMap<>();
                }
                loggerContext = this.contextsMap.get(appletContext);
                if (loggerContext == null) {
                    loggerContext = new LoggerContext();
                    this.contextsMap.put(appletContext, loggerContext);
                }
            }
        }
        return loggerContext != null ? loggerContext : this.userContext;
    }

    final LoggerContext getSystemContext() {
        return this.systemContext;
    }

    private List<LoggerContext> contexts() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(getSystemContext());
        arrayList.add(getUserContext());
        return arrayList;
    }

    Logger demandLogger(String str, String str2, Class<?> cls) {
        Logger logger = getLogger(str);
        if (logger == null) {
            Logger logger2 = new Logger(str, str2, cls, this, false);
            while (!addLogger(logger2)) {
                logger = getLogger(str);
                if (logger != null) {
                }
            }
            return logger2;
        }
        return logger;
    }

    Logger demandSystemLogger(String str, String str2) {
        Logger logger;
        final Logger loggerDemandLogger = getSystemContext().demandLogger(str, str2);
        do {
            if (addLogger(loggerDemandLogger)) {
                logger = loggerDemandLogger;
            } else {
                logger = getLogger(str);
            }
        } while (logger == null);
        if (logger != loggerDemandLogger && loggerDemandLogger.accessCheckedHandlers().length == 0) {
            final Logger logger2 = logger;
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.util.logging.LogManager.4
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() throws SecurityException {
                    for (Handler handler : logger2.accessCheckedHandlers()) {
                        loggerDemandLogger.addHandler(handler);
                    }
                    return null;
                }
            });
        }
        return loggerDemandLogger;
    }

    /* loaded from: rt.jar:java/util/logging/LogManager$LoggerContext.class */
    class LoggerContext {
        private final Hashtable<String, LoggerWeakRef> namedLoggers;
        private final LogNode root;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !LogManager.class.desiredAssertionStatus();
        }

        private LoggerContext() {
            this.namedLoggers = new Hashtable<>();
            this.root = new LogNode(null, this);
        }

        final boolean requiresDefaultLoggers() {
            boolean z2 = getOwner() == LogManager.manager;
            if (z2) {
                getOwner().ensureLogManagerInitialized();
            }
            return z2;
        }

        final LogManager getOwner() {
            return LogManager.this;
        }

        final Logger getRootLogger() {
            return getOwner().rootLogger;
        }

        final Logger getGlobalLogger() {
            return Logger.global;
        }

        Logger demandLogger(String str, String str2) {
            return getOwner().demandLogger(str, str2, null);
        }

        private void ensureInitialized() throws SecurityException {
            if (requiresDefaultLoggers()) {
                ensureDefaultLogger(getRootLogger());
                ensureDefaultLogger(getGlobalLogger());
            }
        }

        synchronized Logger findLogger(String str) throws SecurityException {
            ensureInitialized();
            LoggerWeakRef loggerWeakRef = this.namedLoggers.get(str);
            if (loggerWeakRef == null) {
                return null;
            }
            Logger logger = loggerWeakRef.get();
            if (logger == null) {
                loggerWeakRef.dispose();
            }
            return logger;
        }

        private void ensureAllDefaultLoggers(Logger logger) {
            if (requiresDefaultLoggers()) {
                String name = logger.getName();
                if (!name.isEmpty()) {
                    ensureDefaultLogger(getRootLogger());
                    if (!Logger.GLOBAL_LOGGER_NAME.equals(name)) {
                        ensureDefaultLogger(getGlobalLogger());
                    }
                }
            }
        }

        private void ensureDefaultLogger(Logger logger) throws SecurityException {
            if (!requiresDefaultLoggers() || logger == null || (logger != Logger.global && logger != LogManager.this.rootLogger)) {
                if (!$assertionsDisabled && logger != null) {
                    throw new AssertionError();
                }
            } else if (!this.namedLoggers.containsKey(logger.getName())) {
                addLocalLogger(logger, false);
            }
        }

        boolean addLocalLogger(Logger logger) {
            return addLocalLogger(logger, requiresDefaultLoggers());
        }

        synchronized boolean addLocalLogger(Logger logger, boolean z2) throws SecurityException {
            if (z2) {
                ensureAllDefaultLoggers(logger);
            }
            String name = logger.getName();
            if (name == null) {
                throw new NullPointerException();
            }
            LoggerWeakRef loggerWeakRef = this.namedLoggers.get(name);
            if (loggerWeakRef != null) {
                if (loggerWeakRef.get() == null) {
                    loggerWeakRef.dispose();
                } else {
                    return false;
                }
            }
            LogManager owner = getOwner();
            logger.setLogManager(owner);
            owner.getClass();
            LoggerWeakRef loggerWeakRef2 = owner.new LoggerWeakRef(logger);
            this.namedLoggers.put(name, loggerWeakRef2);
            Level levelProperty = owner.getLevelProperty(name + ".level", null);
            if (levelProperty != null && !logger.isLevelInitialized()) {
                LogManager.doSetLevel(logger, levelProperty);
            }
            processParentHandlers(logger, name);
            LogNode node = getNode(name);
            node.loggerRef = loggerWeakRef2;
            Logger logger2 = null;
            LogNode logNode = node.parent;
            while (true) {
                LogNode logNode2 = logNode;
                if (logNode2 == null) {
                    break;
                }
                LoggerWeakRef loggerWeakRef3 = logNode2.loggerRef;
                if (loggerWeakRef3 != null) {
                    logger2 = loggerWeakRef3.get();
                    if (logger2 != null) {
                        break;
                    }
                }
                logNode = logNode2.parent;
            }
            if (logger2 != null) {
                LogManager.doSetParent(logger, logger2);
            }
            node.walkAndSetParent(logger);
            loggerWeakRef2.setNode(node);
            return true;
        }

        synchronized void removeLoggerRef(String str, LoggerWeakRef loggerWeakRef) {
            this.namedLoggers.remove(str, loggerWeakRef);
        }

        synchronized Enumeration<String> getLoggerNames() throws SecurityException {
            ensureInitialized();
            return this.namedLoggers.keys();
        }

        private void processParentHandlers(final Logger logger, final String str) {
            final LogManager owner = getOwner();
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.util.logging.LogManager.LoggerContext.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() throws SecurityException {
                    if (logger != owner.rootLogger && !owner.getBooleanProperty(str + ".useParentHandlers", true)) {
                        logger.setUseParentHandlers(false);
                        return null;
                    }
                    return null;
                }
            });
            int i2 = 1;
            while (true) {
                int iIndexOf = str.indexOf(".", i2);
                if (iIndexOf >= 0) {
                    String strSubstring = str.substring(0, iIndexOf);
                    if (owner.getProperty(strSubstring + ".level") != null || owner.getProperty(strSubstring + ".handlers") != null) {
                        demandLogger(strSubstring, null);
                    }
                    i2 = iIndexOf + 1;
                } else {
                    return;
                }
            }
        }

        LogNode getNode(String str) {
            String strSubstring;
            if (str == null || str.equals("")) {
                return this.root;
            }
            LogNode logNode = this.root;
            while (true) {
                LogNode logNode2 = logNode;
                if (str.length() > 0) {
                    int iIndexOf = str.indexOf(".");
                    if (iIndexOf > 0) {
                        strSubstring = str.substring(0, iIndexOf);
                        str = str.substring(iIndexOf + 1);
                    } else {
                        strSubstring = str;
                        str = "";
                    }
                    if (logNode2.children == null) {
                        logNode2.children = new HashMap<>();
                    }
                    LogNode logNode3 = logNode2.children.get(strSubstring);
                    if (logNode3 == null) {
                        logNode3 = new LogNode(logNode2, this);
                        logNode2.children.put(strSubstring, logNode3);
                    }
                    logNode = logNode3;
                } else {
                    return logNode2;
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/logging/LogManager$SystemLoggerContext.class */
    final class SystemLoggerContext extends LoggerContext {
        SystemLoggerContext() {
            super();
        }

        @Override // java.util.logging.LogManager.LoggerContext
        Logger demandLogger(String str, String str2) {
            Logger loggerFindLogger = findLogger(str);
            if (loggerFindLogger == null) {
                Logger logger = new Logger(str, str2, null, getOwner(), true);
                do {
                    if (addLocalLogger(logger)) {
                        loggerFindLogger = logger;
                    } else {
                        loggerFindLogger = findLogger(str);
                    }
                } while (loggerFindLogger == null);
            }
            return loggerFindLogger;
        }
    }

    private void loadLoggerHandlers(final Logger logger, String str, final String str2) {
        AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: java.util.logging.LogManager.5
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                for (String str3 : LogManager.this.parseClassNames(str2)) {
                    try {
                        Handler handler = (Handler) ClassLoader.getSystemClassLoader().loadClass(str3).newInstance();
                        String property = LogManager.this.getProperty(str3 + ".level");
                        if (property != null) {
                            Level levelFindLevel = Level.findLevel(property);
                            if (levelFindLevel != null) {
                                handler.setLevel(levelFindLevel);
                            } else {
                                System.err.println("Can't set level for " + str3);
                            }
                        }
                        logger.addHandler(handler);
                    } catch (Exception e2) {
                        System.err.println("Can't load log handler \"" + str3 + PdfOps.DOUBLE_QUOTE__TOKEN);
                        System.err.println("" + ((Object) e2));
                        e2.printStackTrace();
                    }
                }
                return null;
            }
        });
    }

    /* loaded from: rt.jar:java/util/logging/LogManager$LoggerWeakRef.class */
    final class LoggerWeakRef extends WeakReference<Logger> {
        private String name;
        private LogNode node;
        private WeakReference<Logger> parentRef;
        private boolean disposed;

        LoggerWeakRef(Logger logger) {
            super(logger, LogManager.this.loggerRefQueue);
            this.disposed = false;
            this.name = logger.getName();
        }

        void dispose() {
            synchronized (this) {
                if (this.disposed) {
                    return;
                }
                this.disposed = true;
                LogNode logNode = this.node;
                if (logNode != null) {
                    synchronized (logNode.context) {
                        logNode.context.removeLoggerRef(this.name, this);
                        this.name = null;
                        if (logNode.loggerRef == this) {
                            logNode.loggerRef = null;
                        }
                        this.node = null;
                    }
                }
                if (this.parentRef != null) {
                    Logger logger = this.parentRef.get();
                    if (logger != null) {
                        logger.removeChildLogger(this);
                    }
                    this.parentRef = null;
                }
            }
        }

        void setNode(LogNode logNode) {
            this.node = logNode;
        }

        void setParentRef(WeakReference<Logger> weakReference) {
            this.parentRef = weakReference;
        }
    }

    final void drainLoggerRefQueueBounded() {
        LoggerWeakRef loggerWeakRef;
        for (int i2 = 0; i2 < 400 && this.loggerRefQueue != null && (loggerWeakRef = (LoggerWeakRef) this.loggerRefQueue.poll()) != null; i2++) {
            loggerWeakRef.dispose();
        }
    }

    public boolean addLogger(Logger logger) {
        String name = logger.getName();
        if (name == null) {
            throw new NullPointerException();
        }
        drainLoggerRefQueueBounded();
        if (getUserContext().addLocalLogger(logger)) {
            loadLoggerHandlers(logger, name, name + ".handlers");
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void doSetLevel(final Logger logger, final Level level) throws SecurityException {
        if (System.getSecurityManager() == null) {
            logger.setLevel(level);
        } else {
            AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: java.util.logging.LogManager.6
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Object run2() throws SecurityException {
                    logger.setLevel(level);
                    return null;
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void doSetParent(final Logger logger, final Logger logger2) {
        if (System.getSecurityManager() == null) {
            logger.setParent(logger2);
        } else {
            AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: java.util.logging.LogManager.7
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Object run2() {
                    logger.setParent(logger2);
                    return null;
                }
            });
        }
    }

    public Logger getLogger(String str) {
        return getUserContext().findLogger(str);
    }

    public Enumeration<String> getLoggerNames() {
        return getUserContext().getLoggerNames();
    }

    public void readConfiguration() throws IOException, SecurityException {
        checkPermission();
        String property = System.getProperty("java.util.logging.config.class");
        try {
            if (property != null) {
                try {
                    ClassLoader.getSystemClassLoader().loadClass(property).newInstance();
                    return;
                } catch (ClassNotFoundException e2) {
                    Thread.currentThread().getContextClassLoader().loadClass(property).newInstance();
                    return;
                }
            }
        } catch (Exception e3) {
            System.err.println("Logging configuration class \"" + property + "\" failed");
            System.err.println("" + ((Object) e3));
        }
        String property2 = System.getProperty("java.util.logging.config.file");
        if (property2 == null) {
            String property3 = System.getProperty("java.home");
            if (property3 == null) {
                throw new Error("Can't find java.home ??");
            }
            property2 = new File(new File(property3, "lib"), "logging.properties").getCanonicalPath();
        }
        FileInputStream fileInputStream = new FileInputStream(property2);
        Throwable th = null;
        try {
            try {
                readConfiguration(new BufferedInputStream(fileInputStream));
                if (fileInputStream != null) {
                    if (0 != 0) {
                        try {
                            fileInputStream.close();
                            return;
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                            return;
                        }
                    }
                    fileInputStream.close();
                }
            } catch (Throwable th3) {
                th = th3;
                throw th3;
            }
        } catch (Throwable th4) {
            if (fileInputStream != null) {
                if (th != null) {
                    try {
                        fileInputStream.close();
                    } catch (Throwable th5) {
                        th.addSuppressed(th5);
                    }
                } else {
                    fileInputStream.close();
                }
            }
            throw th4;
        }
    }

    public void reset() throws SecurityException {
        checkPermission();
        synchronized (this) {
            this.props = new Properties();
            this.initializedGlobalHandlers = true;
        }
        for (LoggerContext loggerContext : contexts()) {
            Enumeration<String> loggerNames = loggerContext.getLoggerNames();
            while (loggerNames.hasMoreElements()) {
                Logger loggerFindLogger = loggerContext.findLogger(loggerNames.nextElement2());
                if (loggerFindLogger != null) {
                    resetLogger(loggerFindLogger);
                }
            }
        }
    }

    private void resetLogger(Logger logger) throws SecurityException {
        for (Handler handler : logger.getHandlers()) {
            logger.removeHandler(handler);
            try {
                handler.close();
            } catch (Exception e2) {
            }
        }
        String name = logger.getName();
        if (name != null && name.equals("")) {
            logger.setLevel(defaultLevel);
        } else {
            logger.setLevel(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String[] parseClassNames(String str) {
        String property = getProperty(str);
        if (property == null) {
            return new String[0];
        }
        String strTrim = property.trim();
        int i2 = 0;
        ArrayList arrayList = new ArrayList();
        while (i2 < strTrim.length()) {
            int i3 = i2;
            while (i3 < strTrim.length() && !Character.isWhitespace(strTrim.charAt(i3)) && strTrim.charAt(i3) != ',') {
                i3++;
            }
            String strSubstring = strTrim.substring(i2, i3);
            i2 = i3 + 1;
            String strTrim2 = strSubstring.trim();
            if (strTrim2.length() != 0) {
                arrayList.add(strTrim2);
            }
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    public void readConfiguration(InputStream inputStream) throws SecurityException, IOException, IllegalArgumentException {
        checkPermission();
        reset();
        this.props.load(inputStream);
        for (String str : parseClassNames("config")) {
            try {
                ClassLoader.getSystemClassLoader().loadClass(str).newInstance();
            } catch (Exception e2) {
                System.err.println("Can't load config class \"" + str + PdfOps.DOUBLE_QUOTE__TOKEN);
                System.err.println("" + ((Object) e2));
            }
        }
        setLevelsOnExistingLoggers();
        HashMap map = null;
        synchronized (this.listenerMap) {
            if (!this.listenerMap.isEmpty()) {
                map = new HashMap(this.listenerMap);
            }
        }
        if (map != null) {
            if (!$assertionsDisabled && !Beans.isBeansPresent()) {
                throw new AssertionError();
            }
            Object objNewPropertyChangeEvent = Beans.newPropertyChangeEvent(LogManager.class, null, null, null);
            for (Map.Entry entry : map.entrySet()) {
                Object key = entry.getKey();
                int iIntValue = ((Integer) entry.getValue()).intValue();
                for (int i2 = 0; i2 < iIntValue; i2++) {
                    Beans.invokePropertyChange(key, objNewPropertyChangeEvent);
                }
            }
        }
        synchronized (this) {
            this.initializedGlobalHandlers = false;
        }
    }

    public String getProperty(String str) {
        return this.props.getProperty(str);
    }

    String getStringProperty(String str, String str2) {
        String property = getProperty(str);
        if (property == null) {
            return str2;
        }
        return property.trim();
    }

    int getIntProperty(String str, int i2) {
        String property = getProperty(str);
        if (property == null) {
            return i2;
        }
        try {
            return Integer.parseInt(property.trim());
        } catch (Exception e2) {
            return i2;
        }
    }

    boolean getBooleanProperty(String str, boolean z2) {
        String property = getProperty(str);
        if (property == null) {
            return z2;
        }
        String lowerCase = property.toLowerCase();
        if (lowerCase.equals("true") || lowerCase.equals("1")) {
            return true;
        }
        if (lowerCase.equals("false") || lowerCase.equals("0")) {
            return false;
        }
        return z2;
    }

    Level getLevelProperty(String str, Level level) {
        String property = getProperty(str);
        if (property == null) {
            return level;
        }
        Level levelFindLevel = Level.findLevel(property.trim());
        return levelFindLevel != null ? levelFindLevel : level;
    }

    Filter getFilterProperty(String str, Filter filter) {
        String property = getProperty(str);
        if (property != null) {
            try {
                return (Filter) ClassLoader.getSystemClassLoader().loadClass(property).newInstance();
            } catch (Exception e2) {
            }
        }
        return filter;
    }

    Formatter getFormatterProperty(String str, Formatter formatter) {
        String property = getProperty(str);
        if (property != null) {
            try {
                return (Formatter) ClassLoader.getSystemClassLoader().loadClass(property).newInstance();
            } catch (Exception e2) {
            }
        }
        return formatter;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void initializeGlobalHandlers() {
        if (this.initializedGlobalHandlers) {
            return;
        }
        this.initializedGlobalHandlers = true;
        if (this.deathImminent) {
            return;
        }
        loadLoggerHandlers(this.rootLogger, null, "handlers");
    }

    void checkPermission() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(this.controlPermission);
        }
    }

    public void checkAccess() throws SecurityException {
        checkPermission();
    }

    /* loaded from: rt.jar:java/util/logging/LogManager$LogNode.class */
    private static class LogNode {
        HashMap<String, LogNode> children;
        LoggerWeakRef loggerRef;
        LogNode parent;
        final LoggerContext context;

        LogNode(LogNode logNode, LoggerContext loggerContext) {
            this.parent = logNode;
            this.context = loggerContext;
        }

        void walkAndSetParent(Logger logger) {
            if (this.children == null) {
                return;
            }
            for (LogNode logNode : this.children.values()) {
                LoggerWeakRef loggerWeakRef = logNode.loggerRef;
                Logger logger2 = loggerWeakRef == null ? null : loggerWeakRef.get();
                if (logger2 != null) {
                    LogManager.doSetParent(logger2, logger);
                } else {
                    logNode.walkAndSetParent(logger);
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/logging/LogManager$RootLogger.class */
    private final class RootLogger extends Logger {
        private RootLogger() {
            super("", null, null, LogManager.this, true);
        }

        @Override // java.util.logging.Logger
        public void log(LogRecord logRecord) {
            LogManager.this.initializeGlobalHandlers();
            super.log(logRecord);
        }

        @Override // java.util.logging.Logger
        public void addHandler(Handler handler) throws SecurityException {
            LogManager.this.initializeGlobalHandlers();
            super.addHandler(handler);
        }

        @Override // java.util.logging.Logger
        public void removeHandler(Handler handler) throws SecurityException {
            LogManager.this.initializeGlobalHandlers();
            super.removeHandler(handler);
        }

        @Override // java.util.logging.Logger
        Handler[] accessCheckedHandlers() {
            LogManager.this.initializeGlobalHandlers();
            return super.accessCheckedHandlers();
        }
    }

    private synchronized void setLevelsOnExistingLoggers() throws SecurityException {
        Enumeration<?> enumerationPropertyNames = this.props.propertyNames();
        while (enumerationPropertyNames.hasMoreElements()) {
            String str = (String) enumerationPropertyNames.nextElement2();
            if (str.endsWith(".level")) {
                String strSubstring = str.substring(0, str.length() - 6);
                Level levelProperty = getLevelProperty(str, null);
                if (levelProperty == null) {
                    System.err.println("Bad level value for property: " + str);
                } else {
                    Iterator<LoggerContext> it = contexts().iterator();
                    while (it.hasNext()) {
                        Logger loggerFindLogger = it.next().findLogger(strSubstring);
                        if (loggerFindLogger != null) {
                            loggerFindLogger.setLevel(levelProperty);
                        }
                    }
                }
            }
        }
    }

    public static synchronized LoggingMXBean getLoggingMXBean() {
        if (loggingMXBean == null) {
            loggingMXBean = new Logging();
        }
        return loggingMXBean;
    }

    /* loaded from: rt.jar:java/util/logging/LogManager$Beans.class */
    private static class Beans {
        private static final Class<?> propertyChangeListenerClass = getClass("java.beans.PropertyChangeListener");
        private static final Class<?> propertyChangeEventClass = getClass("java.beans.PropertyChangeEvent");
        private static final Method propertyChangeMethod = getMethod(propertyChangeListenerClass, "propertyChange", propertyChangeEventClass);
        private static final Constructor<?> propertyEventCtor = getConstructor(propertyChangeEventClass, Object.class, String.class, Object.class, Object.class);

        private Beans() {
        }

        private static Class<?> getClass(String str) {
            try {
                return Class.forName(str, true, Beans.class.getClassLoader());
            } catch (ClassNotFoundException e2) {
                return null;
            }
        }

        private static Constructor<?> getConstructor(Class<?> cls, Class<?>... clsArr) {
            if (cls == null) {
                return null;
            }
            try {
                return cls.getDeclaredConstructor(clsArr);
            } catch (NoSuchMethodException e2) {
                throw new AssertionError(e2);
            }
        }

        private static Method getMethod(Class<?> cls, String str, Class<?>... clsArr) {
            if (cls == null) {
                return null;
            }
            try {
                return cls.getMethod(str, clsArr);
            } catch (NoSuchMethodException e2) {
                throw new AssertionError(e2);
            }
        }

        static boolean isBeansPresent() {
            return (propertyChangeListenerClass == null || propertyChangeEventClass == null) ? false : true;
        }

        static Object newPropertyChangeEvent(Object obj, String str, Object obj2, Object obj3) {
            try {
                return propertyEventCtor.newInstance(obj, str, obj2, obj3);
            } catch (IllegalAccessException | InstantiationException e2) {
                throw new AssertionError(e2);
            } catch (InvocationTargetException e3) {
                Throwable cause = e3.getCause();
                if (cause instanceof Error) {
                    throw ((Error) cause);
                }
                if (cause instanceof RuntimeException) {
                    throw ((RuntimeException) cause);
                }
                throw new AssertionError(e3);
            }
        }

        static void invokePropertyChange(Object obj, Object obj2) throws IllegalArgumentException {
            try {
                propertyChangeMethod.invoke(obj, obj2);
            } catch (IllegalAccessException e2) {
                throw new AssertionError(e2);
            } catch (InvocationTargetException e3) {
                Throwable cause = e3.getCause();
                if (cause instanceof Error) {
                    throw ((Error) cause);
                }
                if (cause instanceof RuntimeException) {
                    throw ((RuntimeException) cause);
                }
                throw new AssertionError(e3);
            }
        }
    }
}
