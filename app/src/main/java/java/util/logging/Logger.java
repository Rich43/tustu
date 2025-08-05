package java.util.logging;

import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;
import java.util.logging.LogManager;
import java.util.logging.LogManager.LoggerWeakRef;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;

/* loaded from: rt.jar:java/util/logging/Logger.class */
public class Logger {
    private static final Handler[] emptyHandlers;
    private static final int offValue;
    static final String SYSTEM_LOGGER_RB_NAME = "sun.util.logging.resources.logging";
    private static final LoggerBundle SYSTEM_BUNDLE;
    private static final LoggerBundle NO_RESOURCE_BUNDLE;
    private volatile LogManager manager;
    private String name;
    private final CopyOnWriteArrayList<Handler> handlers;
    private volatile LoggerBundle loggerBundle;
    private volatile boolean useParentHandlers;
    private volatile Filter filter;
    private boolean anonymous;
    private ResourceBundle catalog;
    private String catalogName;
    private Locale catalogLocale;
    private static final Object treeLock;
    private volatile Logger parent;
    private ArrayList<LogManager.LoggerWeakRef> kids;
    private volatile Level levelObject;
    private volatile int levelValue;
    private WeakReference<ClassLoader> callersClassLoaderRef;
    private final boolean isSystemLogger;
    public static final String GLOBAL_LOGGER_NAME = "global";

    @Deprecated
    public static final Logger global;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Logger.class.desiredAssertionStatus();
        emptyHandlers = new Handler[0];
        offValue = Level.OFF.intValue();
        SYSTEM_BUNDLE = new LoggerBundle(SYSTEM_LOGGER_RB_NAME, null);
        NO_RESOURCE_BUNDLE = new LoggerBundle(null, null);
        treeLock = new Object();
        global = new Logger(GLOBAL_LOGGER_NAME);
    }

    /* loaded from: rt.jar:java/util/logging/Logger$LoggerBundle.class */
    private static final class LoggerBundle {
        final String resourceBundleName;
        final ResourceBundle userBundle;

        private LoggerBundle(String str, ResourceBundle resourceBundle) {
            this.resourceBundleName = str;
            this.userBundle = resourceBundle;
        }

        boolean isSystemBundle() {
            return Logger.SYSTEM_LOGGER_RB_NAME.equals(this.resourceBundleName);
        }

        static LoggerBundle get(String str, ResourceBundle resourceBundle) {
            if (str == null && resourceBundle == null) {
                return Logger.NO_RESOURCE_BUNDLE;
            }
            if (Logger.SYSTEM_LOGGER_RB_NAME.equals(str) && resourceBundle == null) {
                return Logger.SYSTEM_BUNDLE;
            }
            return new LoggerBundle(str, resourceBundle);
        }
    }

    public static final Logger getGlobal() {
        LogManager.getLogManager();
        return global;
    }

    protected Logger(String str, String str2) {
        this(str, str2, null, LogManager.getLogManager(), false);
    }

    Logger(String str, String str2, Class<?> cls, LogManager logManager, boolean z2) throws SecurityException {
        this.handlers = new CopyOnWriteArrayList<>();
        this.loggerBundle = NO_RESOURCE_BUNDLE;
        this.useParentHandlers = true;
        this.manager = logManager;
        this.isSystemLogger = z2;
        setupResourceInfo(str2, cls);
        this.name = str;
        this.levelValue = Level.INFO.intValue();
    }

    private void setCallersClassLoaderRef(Class<?> cls) {
        ClassLoader classLoader = cls != null ? cls.getClassLoader() : null;
        if (classLoader != null) {
            this.callersClassLoaderRef = new WeakReference<>(classLoader);
        }
    }

    private ClassLoader getCallersClassLoader() {
        if (this.callersClassLoaderRef != null) {
            return this.callersClassLoaderRef.get();
        }
        return null;
    }

    private Logger(String str) {
        this.handlers = new CopyOnWriteArrayList<>();
        this.loggerBundle = NO_RESOURCE_BUNDLE;
        this.useParentHandlers = true;
        this.name = str;
        this.isSystemLogger = true;
        this.levelValue = Level.INFO.intValue();
    }

    void setLogManager(LogManager logManager) {
        this.manager = logManager;
    }

    private void checkPermission() throws SecurityException {
        if (!this.anonymous) {
            if (this.manager == null) {
                this.manager = LogManager.getLogManager();
            }
            this.manager.checkPermission();
        }
    }

    /* loaded from: rt.jar:java/util/logging/Logger$SystemLoggerHelper.class */
    private static class SystemLoggerHelper {
        static boolean disableCallerCheck = getBooleanProperty("sun.util.logging.disableCallerCheck");

        private SystemLoggerHelper() {
        }

        private static boolean getBooleanProperty(final String str) {
            return Boolean.valueOf((String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: java.util.logging.Logger.SystemLoggerHelper.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public String run() {
                    return System.getProperty(str);
                }
            })).booleanValue();
        }
    }

    private static Logger demandLogger(String str, String str2, Class<?> cls) {
        LogManager logManager = LogManager.getLogManager();
        if (System.getSecurityManager() != null && !SystemLoggerHelper.disableCallerCheck && cls.getClassLoader() == null) {
            return logManager.demandSystemLogger(str, str2);
        }
        return logManager.demandLogger(str, str2, cls);
    }

    @CallerSensitive
    public static Logger getLogger(String str) {
        return demandLogger(str, null, Reflection.getCallerClass());
    }

    @CallerSensitive
    public static Logger getLogger(String str, String str2) throws SecurityException {
        Class<?> callerClass = Reflection.getCallerClass();
        Logger loggerDemandLogger = demandLogger(str, str2, callerClass);
        loggerDemandLogger.setupResourceInfo(str2, callerClass);
        return loggerDemandLogger;
    }

    static Logger getPlatformLogger(String str) {
        return LogManager.getLogManager().demandSystemLogger(str, SYSTEM_LOGGER_RB_NAME);
    }

    public static Logger getAnonymousLogger() {
        return getAnonymousLogger(null);
    }

    @CallerSensitive
    public static Logger getAnonymousLogger(String str) {
        LogManager logManager = LogManager.getLogManager();
        logManager.drainLoggerRefQueueBounded();
        Logger logger = new Logger(null, str, Reflection.getCallerClass(), logManager, false);
        logger.anonymous = true;
        logger.doSetParent(logManager.getLogger(""));
        return logger;
    }

    public ResourceBundle getResourceBundle() {
        return findResourceBundle(getResourceBundleName(), true);
    }

    public String getResourceBundleName() {
        return this.loggerBundle.resourceBundleName;
    }

    public void setFilter(Filter filter) throws SecurityException {
        checkPermission();
        this.filter = filter;
    }

    public Filter getFilter() {
        return this.filter;
    }

    public void log(LogRecord logRecord) {
        Handler[] handlers;
        if (!isLoggable(logRecord.getLevel())) {
            return;
        }
        Filter filter = this.filter;
        if (filter != null && !filter.isLoggable(logRecord)) {
            return;
        }
        Logger parent = this;
        while (true) {
            Logger logger = parent;
            if (logger != null) {
                if (this.isSystemLogger) {
                    handlers = logger.accessCheckedHandlers();
                } else {
                    handlers = logger.getHandlers();
                }
                for (Handler handler : handlers) {
                    handler.publish(logRecord);
                }
                if (this.isSystemLogger ? logger.useParentHandlers : logger.getUseParentHandlers()) {
                    parent = this.isSystemLogger ? logger.parent : logger.getParent();
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private void doLog(LogRecord logRecord) {
        logRecord.setLoggerName(this.name);
        LoggerBundle effectiveLoggerBundle = getEffectiveLoggerBundle();
        ResourceBundle resourceBundle = effectiveLoggerBundle.userBundle;
        String str = effectiveLoggerBundle.resourceBundleName;
        if (str != null && resourceBundle != null) {
            logRecord.setResourceBundleName(str);
            logRecord.setResourceBundle(resourceBundle);
        }
        log(logRecord);
    }

    public void log(Level level, String str) {
        if (!isLoggable(level)) {
            return;
        }
        doLog(new LogRecord(level, str));
    }

    public void log(Level level, Supplier<String> supplier) {
        if (!isLoggable(level)) {
            return;
        }
        doLog(new LogRecord(level, supplier.get()));
    }

    public void log(Level level, String str, Object obj) {
        if (!isLoggable(level)) {
            return;
        }
        LogRecord logRecord = new LogRecord(level, str);
        logRecord.setParameters(new Object[]{obj});
        doLog(logRecord);
    }

    public void log(Level level, String str, Object[] objArr) {
        if (!isLoggable(level)) {
            return;
        }
        LogRecord logRecord = new LogRecord(level, str);
        logRecord.setParameters(objArr);
        doLog(logRecord);
    }

    public void log(Level level, String str, Throwable th) {
        if (!isLoggable(level)) {
            return;
        }
        LogRecord logRecord = new LogRecord(level, str);
        logRecord.setThrown(th);
        doLog(logRecord);
    }

    public void log(Level level, Throwable th, Supplier<String> supplier) {
        if (!isLoggable(level)) {
            return;
        }
        LogRecord logRecord = new LogRecord(level, supplier.get());
        logRecord.setThrown(th);
        doLog(logRecord);
    }

    public void logp(Level level, String str, String str2, String str3) {
        if (!isLoggable(level)) {
            return;
        }
        LogRecord logRecord = new LogRecord(level, str3);
        logRecord.setSourceClassName(str);
        logRecord.setSourceMethodName(str2);
        doLog(logRecord);
    }

    public void logp(Level level, String str, String str2, Supplier<String> supplier) {
        if (!isLoggable(level)) {
            return;
        }
        LogRecord logRecord = new LogRecord(level, supplier.get());
        logRecord.setSourceClassName(str);
        logRecord.setSourceMethodName(str2);
        doLog(logRecord);
    }

    public void logp(Level level, String str, String str2, String str3, Object obj) {
        if (!isLoggable(level)) {
            return;
        }
        LogRecord logRecord = new LogRecord(level, str3);
        logRecord.setSourceClassName(str);
        logRecord.setSourceMethodName(str2);
        logRecord.setParameters(new Object[]{obj});
        doLog(logRecord);
    }

    public void logp(Level level, String str, String str2, String str3, Object[] objArr) {
        if (!isLoggable(level)) {
            return;
        }
        LogRecord logRecord = new LogRecord(level, str3);
        logRecord.setSourceClassName(str);
        logRecord.setSourceMethodName(str2);
        logRecord.setParameters(objArr);
        doLog(logRecord);
    }

    public void logp(Level level, String str, String str2, String str3, Throwable th) {
        if (!isLoggable(level)) {
            return;
        }
        LogRecord logRecord = new LogRecord(level, str3);
        logRecord.setSourceClassName(str);
        logRecord.setSourceMethodName(str2);
        logRecord.setThrown(th);
        doLog(logRecord);
    }

    public void logp(Level level, String str, String str2, Throwable th, Supplier<String> supplier) {
        if (!isLoggable(level)) {
            return;
        }
        LogRecord logRecord = new LogRecord(level, supplier.get());
        logRecord.setSourceClassName(str);
        logRecord.setSourceMethodName(str2);
        logRecord.setThrown(th);
        doLog(logRecord);
    }

    private void doLog(LogRecord logRecord, String str) {
        logRecord.setLoggerName(this.name);
        if (str != null) {
            logRecord.setResourceBundleName(str);
            logRecord.setResourceBundle(findResourceBundle(str, false));
        }
        log(logRecord);
    }

    private void doLog(LogRecord logRecord, ResourceBundle resourceBundle) {
        logRecord.setLoggerName(this.name);
        if (resourceBundle != null) {
            logRecord.setResourceBundleName(resourceBundle.getBaseBundleName());
            logRecord.setResourceBundle(resourceBundle);
        }
        log(logRecord);
    }

    @Deprecated
    public void logrb(Level level, String str, String str2, String str3, String str4) {
        if (!isLoggable(level)) {
            return;
        }
        LogRecord logRecord = new LogRecord(level, str4);
        logRecord.setSourceClassName(str);
        logRecord.setSourceMethodName(str2);
        doLog(logRecord, str3);
    }

    @Deprecated
    public void logrb(Level level, String str, String str2, String str3, String str4, Object obj) {
        if (!isLoggable(level)) {
            return;
        }
        LogRecord logRecord = new LogRecord(level, str4);
        logRecord.setSourceClassName(str);
        logRecord.setSourceMethodName(str2);
        logRecord.setParameters(new Object[]{obj});
        doLog(logRecord, str3);
    }

    @Deprecated
    public void logrb(Level level, String str, String str2, String str3, String str4, Object[] objArr) {
        if (!isLoggable(level)) {
            return;
        }
        LogRecord logRecord = new LogRecord(level, str4);
        logRecord.setSourceClassName(str);
        logRecord.setSourceMethodName(str2);
        logRecord.setParameters(objArr);
        doLog(logRecord, str3);
    }

    public void logrb(Level level, String str, String str2, ResourceBundle resourceBundle, String str3, Object... objArr) {
        if (!isLoggable(level)) {
            return;
        }
        LogRecord logRecord = new LogRecord(level, str3);
        logRecord.setSourceClassName(str);
        logRecord.setSourceMethodName(str2);
        if (objArr != null && objArr.length != 0) {
            logRecord.setParameters(objArr);
        }
        doLog(logRecord, resourceBundle);
    }

    @Deprecated
    public void logrb(Level level, String str, String str2, String str3, String str4, Throwable th) {
        if (!isLoggable(level)) {
            return;
        }
        LogRecord logRecord = new LogRecord(level, str4);
        logRecord.setSourceClassName(str);
        logRecord.setSourceMethodName(str2);
        logRecord.setThrown(th);
        doLog(logRecord, str3);
    }

    public void logrb(Level level, String str, String str2, ResourceBundle resourceBundle, String str3, Throwable th) {
        if (!isLoggable(level)) {
            return;
        }
        LogRecord logRecord = new LogRecord(level, str3);
        logRecord.setSourceClassName(str);
        logRecord.setSourceMethodName(str2);
        logRecord.setThrown(th);
        doLog(logRecord, resourceBundle);
    }

    public void entering(String str, String str2) {
        logp(Level.FINER, str, str2, "ENTRY");
    }

    public void entering(String str, String str2, Object obj) {
        logp(Level.FINER, str, str2, "ENTRY {0}", obj);
    }

    public void entering(String str, String str2, Object[] objArr) {
        String str3 = "ENTRY";
        if (objArr == null) {
            logp(Level.FINER, str, str2, str3);
            return;
        }
        if (isLoggable(Level.FINER)) {
            for (int i2 = 0; i2 < objArr.length; i2++) {
                str3 = str3 + " {" + i2 + "}";
            }
            logp(Level.FINER, str, str2, str3, objArr);
        }
    }

    public void exiting(String str, String str2) {
        logp(Level.FINER, str, str2, "RETURN");
    }

    public void exiting(String str, String str2, Object obj) {
        logp(Level.FINER, str, str2, "RETURN {0}", obj);
    }

    public void throwing(String str, String str2, Throwable th) {
        if (!isLoggable(Level.FINER)) {
            return;
        }
        LogRecord logRecord = new LogRecord(Level.FINER, "THROW");
        logRecord.setSourceClassName(str);
        logRecord.setSourceMethodName(str2);
        logRecord.setThrown(th);
        doLog(logRecord);
    }

    public void severe(String str) {
        log(Level.SEVERE, str);
    }

    public void warning(String str) {
        log(Level.WARNING, str);
    }

    public void info(String str) {
        log(Level.INFO, str);
    }

    public void config(String str) {
        log(Level.CONFIG, str);
    }

    public void fine(String str) {
        log(Level.FINE, str);
    }

    public void finer(String str) {
        log(Level.FINER, str);
    }

    public void finest(String str) {
        log(Level.FINEST, str);
    }

    public void severe(Supplier<String> supplier) {
        log(Level.SEVERE, supplier);
    }

    public void warning(Supplier<String> supplier) {
        log(Level.WARNING, supplier);
    }

    public void info(Supplier<String> supplier) {
        log(Level.INFO, supplier);
    }

    public void config(Supplier<String> supplier) {
        log(Level.CONFIG, supplier);
    }

    public void fine(Supplier<String> supplier) {
        log(Level.FINE, supplier);
    }

    public void finer(Supplier<String> supplier) {
        log(Level.FINER, supplier);
    }

    public void finest(Supplier<String> supplier) {
        log(Level.FINEST, supplier);
    }

    public void setLevel(Level level) throws SecurityException {
        checkPermission();
        synchronized (treeLock) {
            this.levelObject = level;
            updateEffectiveLevel();
        }
    }

    final boolean isLevelInitialized() {
        return this.levelObject != null;
    }

    public Level getLevel() {
        return this.levelObject;
    }

    public boolean isLoggable(Level level) {
        if (level.intValue() < this.levelValue || this.levelValue == offValue) {
            return false;
        }
        return true;
    }

    public String getName() {
        return this.name;
    }

    public void addHandler(Handler handler) throws SecurityException {
        handler.getClass();
        checkPermission();
        this.handlers.add(handler);
    }

    public void removeHandler(Handler handler) throws SecurityException {
        checkPermission();
        if (handler == null) {
            return;
        }
        this.handlers.remove(handler);
    }

    public Handler[] getHandlers() {
        return accessCheckedHandlers();
    }

    Handler[] accessCheckedHandlers() {
        return (Handler[]) this.handlers.toArray(emptyHandlers);
    }

    public void setUseParentHandlers(boolean z2) throws SecurityException {
        checkPermission();
        this.useParentHandlers = z2;
    }

    public boolean getUseParentHandlers() {
        return this.useParentHandlers;
    }

    private static ResourceBundle findSystemResourceBundle(final Locale locale) {
        return (ResourceBundle) AccessController.doPrivileged(new PrivilegedAction<ResourceBundle>() { // from class: java.util.logging.Logger.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public ResourceBundle run() {
                try {
                    return ResourceBundle.getBundle(Logger.SYSTEM_LOGGER_RB_NAME, locale);
                } catch (MissingResourceException e2) {
                    throw new InternalError(e2.toString());
                }
            }
        });
    }

    private synchronized ResourceBundle findResourceBundle(String str, boolean z2) {
        ClassLoader callersClassLoader;
        if (str == null) {
            return null;
        }
        Locale locale = Locale.getDefault();
        LoggerBundle loggerBundle = this.loggerBundle;
        if (loggerBundle.userBundle != null && str.equals(loggerBundle.resourceBundleName)) {
            return loggerBundle.userBundle;
        }
        if (this.catalog != null && locale.equals(this.catalogLocale) && str.equals(this.catalogName)) {
            return this.catalog;
        }
        if (str.equals(SYSTEM_LOGGER_RB_NAME)) {
            this.catalog = findSystemResourceBundle(locale);
            this.catalogName = str;
            this.catalogLocale = locale;
            return this.catalog;
        }
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader == null) {
            contextClassLoader = ClassLoader.getSystemClassLoader();
        }
        try {
            this.catalog = ResourceBundle.getBundle(str, locale, contextClassLoader);
            this.catalogName = str;
            this.catalogLocale = locale;
            return this.catalog;
        } catch (MissingResourceException e2) {
            if (!z2 || (callersClassLoader = getCallersClassLoader()) == null || callersClassLoader == contextClassLoader) {
                return null;
            }
            try {
                this.catalog = ResourceBundle.getBundle(str, locale, callersClassLoader);
                this.catalogName = str;
                this.catalogLocale = locale;
                return this.catalog;
            } catch (MissingResourceException e3) {
                return null;
            }
        }
    }

    private synchronized void setupResourceInfo(String str, Class<?> cls) throws SecurityException {
        LoggerBundle loggerBundle = this.loggerBundle;
        if (loggerBundle.resourceBundleName != null) {
            if (loggerBundle.resourceBundleName.equals(str)) {
                return;
            } else {
                throw new IllegalArgumentException(loggerBundle.resourceBundleName + " != " + str);
            }
        }
        if (str == null) {
            return;
        }
        setCallersClassLoaderRef(cls);
        if (this.isSystemLogger && getCallersClassLoader() != null) {
            checkPermission();
        }
        if (findResourceBundle(str, true) == null) {
            this.callersClassLoaderRef = null;
            throw new MissingResourceException("Can't find " + str + " bundle", str, "");
        }
        if (!$assertionsDisabled && loggerBundle.userBundle != null) {
            throw new AssertionError();
        }
        this.loggerBundle = LoggerBundle.get(str, null);
    }

    public void setResourceBundle(ResourceBundle resourceBundle) throws SecurityException {
        checkPermission();
        String baseBundleName = resourceBundle.getBaseBundleName();
        if (baseBundleName == null || baseBundleName.isEmpty()) {
            throw new IllegalArgumentException("resource bundle must have a name");
        }
        synchronized (this) {
            LoggerBundle loggerBundle = this.loggerBundle;
            if (!(loggerBundle.resourceBundleName == null || loggerBundle.resourceBundleName.equals(baseBundleName))) {
                throw new IllegalArgumentException("can't replace resource bundle");
            }
            this.loggerBundle = LoggerBundle.get(baseBundleName, resourceBundle);
        }
    }

    public Logger getParent() {
        return this.parent;
    }

    public void setParent(Logger logger) {
        if (logger == null) {
            throw new NullPointerException();
        }
        if (this.manager == null) {
            this.manager = LogManager.getLogManager();
        }
        this.manager.checkPermission();
        doSetParent(logger);
    }

    private void doSetParent(Logger logger) {
        synchronized (treeLock) {
            LogManager.LoggerWeakRef loggerWeakRef = null;
            if (this.parent != null) {
                Iterator<LogManager.LoggerWeakRef> it = this.parent.kids.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    loggerWeakRef = it.next();
                    if (loggerWeakRef.get() == this) {
                        it.remove();
                        break;
                    }
                    loggerWeakRef = null;
                }
            }
            this.parent = logger;
            if (this.parent.kids == null) {
                this.parent.kids = new ArrayList<>(2);
            }
            if (loggerWeakRef == null) {
                LogManager logManager = this.manager;
                logManager.getClass();
                loggerWeakRef = logManager.new LoggerWeakRef(this);
            }
            loggerWeakRef.setParentRef(new WeakReference<>(this.parent));
            this.parent.kids.add(loggerWeakRef);
            updateEffectiveLevel();
        }
    }

    final void removeChildLogger(LogManager.LoggerWeakRef loggerWeakRef) {
        synchronized (treeLock) {
            Iterator<LogManager.LoggerWeakRef> it = this.kids.iterator();
            while (it.hasNext()) {
                if (it.next() == loggerWeakRef) {
                    it.remove();
                    return;
                }
            }
        }
    }

    private void updateEffectiveLevel() {
        int iIntValue;
        if (this.levelObject != null) {
            iIntValue = this.levelObject.intValue();
        } else if (this.parent != null) {
            iIntValue = this.parent.levelValue;
        } else {
            iIntValue = Level.INFO.intValue();
        }
        if (this.levelValue == iIntValue) {
            return;
        }
        this.levelValue = iIntValue;
        if (this.kids != null) {
            for (int i2 = 0; i2 < this.kids.size(); i2++) {
                Logger logger = this.kids.get(i2).get();
                if (logger != null) {
                    logger.updateEffectiveLevel();
                }
            }
        }
    }

    private LoggerBundle getEffectiveLoggerBundle() {
        String resourceBundleName;
        LoggerBundle loggerBundle = this.loggerBundle;
        if (loggerBundle.isSystemBundle()) {
            return SYSTEM_BUNDLE;
        }
        ResourceBundle resourceBundle = getResourceBundle();
        if (resourceBundle != null && resourceBundle == loggerBundle.userBundle) {
            return loggerBundle;
        }
        if (resourceBundle != null) {
            return LoggerBundle.get(getResourceBundleName(), resourceBundle);
        }
        Logger parent = this.parent;
        while (true) {
            Logger logger = parent;
            if (logger != null) {
                LoggerBundle loggerBundle2 = logger.loggerBundle;
                if (loggerBundle2.isSystemBundle()) {
                    return SYSTEM_BUNDLE;
                }
                if (loggerBundle2.userBundle != null) {
                    return loggerBundle2;
                }
                if (this.isSystemLogger) {
                    resourceBundleName = logger.isSystemLogger ? loggerBundle2.resourceBundleName : null;
                } else {
                    resourceBundleName = logger.getResourceBundleName();
                }
                String str = resourceBundleName;
                if (str != null) {
                    return LoggerBundle.get(str, findResourceBundle(str, true));
                }
                parent = this.isSystemLogger ? logger.parent : logger.getParent();
            } else {
                return NO_RESOURCE_BUNDLE;
            }
        }
    }
}
