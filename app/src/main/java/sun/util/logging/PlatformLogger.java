package sun.util.logging;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import sun.misc.JavaLangAccess;
import sun.misc.SharedSecrets;

/* loaded from: rt.jar:sun/util/logging/PlatformLogger.class */
public class PlatformLogger {
    private static final int OFF = Integer.MAX_VALUE;
    private static final int SEVERE = 1000;
    private static final int WARNING = 900;
    private static final int INFO = 800;
    private static final int CONFIG = 700;
    private static final int FINE = 500;
    private static final int FINER = 400;
    private static final int FINEST = 300;
    private static final int ALL = Integer.MIN_VALUE;
    private static final Level DEFAULT_LEVEL = Level.INFO;
    private static boolean loggingEnabled = ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: sun.util.logging.PlatformLogger.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        public Boolean run() {
            return Boolean.valueOf((System.getProperty("java.util.logging.config.class") == null && System.getProperty("java.util.logging.config.file") == null) ? false : true);
        }
    })).booleanValue();
    private static Map<String, WeakReference<PlatformLogger>> loggers;
    private volatile LoggerProxy loggerProxy;
    private volatile JavaLoggerProxy javaLoggerProxy;

    /* loaded from: rt.jar:sun/util/logging/PlatformLogger$Level.class */
    public enum Level {
        ALL,
        FINEST,
        FINER,
        FINE,
        CONFIG,
        INFO,
        WARNING,
        SEVERE,
        OFF;

        Object javaLevel;
        private static final int[] LEVEL_VALUES = {Integer.MIN_VALUE, 300, 400, 500, 700, 800, 900, 1000, Integer.MAX_VALUE};

        public int intValue() {
            return LEVEL_VALUES[ordinal()];
        }

        static Level valueOf(int i2) {
            switch (i2) {
                case Integer.MIN_VALUE:
                    return ALL;
                case 300:
                    return FINEST;
                case 400:
                    return FINER;
                case 500:
                    return FINE;
                case 700:
                    return CONFIG;
                case 800:
                    return INFO;
                case 900:
                    return WARNING;
                case 1000:
                    return SEVERE;
                case Integer.MAX_VALUE:
                    return OFF;
                default:
                    int iBinarySearch = Arrays.binarySearch(LEVEL_VALUES, 0, LEVEL_VALUES.length - 2, i2);
                    return values()[iBinarySearch >= 0 ? iBinarySearch : (-iBinarySearch) - 1];
            }
        }
    }

    static {
        try {
            Class.forName("sun.util.logging.PlatformLogger$DefaultLoggerProxy", false, PlatformLogger.class.getClassLoader());
            Class.forName("sun.util.logging.PlatformLogger$JavaLoggerProxy", false, PlatformLogger.class.getClassLoader());
            loggers = new HashMap();
        } catch (ClassNotFoundException e2) {
            throw new InternalError(e2);
        }
    }

    public static synchronized PlatformLogger getLogger(String str) {
        PlatformLogger platformLogger = null;
        WeakReference<PlatformLogger> weakReference = loggers.get(str);
        if (weakReference != null) {
            platformLogger = weakReference.get();
        }
        if (platformLogger == null) {
            platformLogger = new PlatformLogger(str);
            loggers.put(str, new WeakReference<>(platformLogger));
        }
        return platformLogger;
    }

    public static synchronized void redirectPlatformLoggers() {
        if (loggingEnabled || !LoggingSupport.isAvailable()) {
            return;
        }
        loggingEnabled = true;
        Iterator<Map.Entry<String, WeakReference<PlatformLogger>>> it = loggers.entrySet().iterator();
        while (it.hasNext()) {
            PlatformLogger platformLogger = it.next().getValue().get();
            if (platformLogger != null) {
                platformLogger.redirectToJavaLoggerProxy();
            }
        }
    }

    private void redirectToJavaLoggerProxy() {
        DefaultLoggerProxy defaultLoggerProxy = (DefaultLoggerProxy) DefaultLoggerProxy.class.cast(this.loggerProxy);
        JavaLoggerProxy javaLoggerProxy = new JavaLoggerProxy(defaultLoggerProxy.name, defaultLoggerProxy.level);
        this.javaLoggerProxy = javaLoggerProxy;
        this.loggerProxy = javaLoggerProxy;
    }

    private PlatformLogger(String str) {
        if (loggingEnabled) {
            JavaLoggerProxy javaLoggerProxy = new JavaLoggerProxy(str);
            this.javaLoggerProxy = javaLoggerProxy;
            this.loggerProxy = javaLoggerProxy;
            return;
        }
        this.loggerProxy = new DefaultLoggerProxy(str);
    }

    public boolean isEnabled() {
        return this.loggerProxy.isEnabled();
    }

    public String getName() {
        return this.loggerProxy.name;
    }

    public boolean isLoggable(Level level) {
        if (level == null) {
            throw new NullPointerException();
        }
        JavaLoggerProxy javaLoggerProxy = this.javaLoggerProxy;
        return javaLoggerProxy != null ? javaLoggerProxy.isLoggable(level) : this.loggerProxy.isLoggable(level);
    }

    public Level level() {
        return this.loggerProxy.getLevel();
    }

    public void setLevel(Level level) {
        this.loggerProxy.setLevel(level);
    }

    public void severe(String str) {
        this.loggerProxy.doLog(Level.SEVERE, str);
    }

    public void severe(String str, Throwable th) {
        this.loggerProxy.doLog(Level.SEVERE, str, th);
    }

    public void severe(String str, Object... objArr) {
        this.loggerProxy.doLog(Level.SEVERE, str, objArr);
    }

    public void warning(String str) {
        this.loggerProxy.doLog(Level.WARNING, str);
    }

    public void warning(String str, Throwable th) {
        this.loggerProxy.doLog(Level.WARNING, str, th);
    }

    public void warning(String str, Object... objArr) {
        this.loggerProxy.doLog(Level.WARNING, str, objArr);
    }

    public void info(String str) {
        this.loggerProxy.doLog(Level.INFO, str);
    }

    public void info(String str, Throwable th) {
        this.loggerProxy.doLog(Level.INFO, str, th);
    }

    public void info(String str, Object... objArr) {
        this.loggerProxy.doLog(Level.INFO, str, objArr);
    }

    public void config(String str) {
        this.loggerProxy.doLog(Level.CONFIG, str);
    }

    public void config(String str, Throwable th) {
        this.loggerProxy.doLog(Level.CONFIG, str, th);
    }

    public void config(String str, Object... objArr) {
        this.loggerProxy.doLog(Level.CONFIG, str, objArr);
    }

    public void fine(String str) {
        this.loggerProxy.doLog(Level.FINE, str);
    }

    public void fine(String str, Throwable th) {
        this.loggerProxy.doLog(Level.FINE, str, th);
    }

    public void fine(String str, Object... objArr) {
        this.loggerProxy.doLog(Level.FINE, str, objArr);
    }

    public void finer(String str) {
        this.loggerProxy.doLog(Level.FINER, str);
    }

    public void finer(String str, Throwable th) {
        this.loggerProxy.doLog(Level.FINER, str, th);
    }

    public void finer(String str, Object... objArr) {
        this.loggerProxy.doLog(Level.FINER, str, objArr);
    }

    public void finest(String str) {
        this.loggerProxy.doLog(Level.FINEST, str);
    }

    public void finest(String str, Throwable th) {
        this.loggerProxy.doLog(Level.FINEST, str, th);
    }

    public void finest(String str, Object... objArr) {
        this.loggerProxy.doLog(Level.FINEST, str, objArr);
    }

    /* loaded from: rt.jar:sun/util/logging/PlatformLogger$LoggerProxy.class */
    private static abstract class LoggerProxy {
        final String name;

        abstract boolean isEnabled();

        abstract Level getLevel();

        abstract void setLevel(Level level);

        abstract void doLog(Level level, String str);

        abstract void doLog(Level level, String str, Throwable th);

        abstract void doLog(Level level, String str, Object... objArr);

        abstract boolean isLoggable(Level level);

        protected LoggerProxy(String str) {
            this.name = str;
        }
    }

    /* loaded from: rt.jar:sun/util/logging/PlatformLogger$DefaultLoggerProxy.class */
    private static final class DefaultLoggerProxy extends LoggerProxy {
        volatile Level effectiveLevel;
        volatile Level level;
        private static final String formatString = LoggingSupport.getSimpleFormat(false);
        private Date date;

        private static PrintStream outputStream() {
            return System.err;
        }

        DefaultLoggerProxy(String str) {
            super(str);
            this.date = new Date();
            this.effectiveLevel = deriveEffectiveLevel(null);
            this.level = null;
        }

        @Override // sun.util.logging.PlatformLogger.LoggerProxy
        boolean isEnabled() {
            return this.effectiveLevel != Level.OFF;
        }

        @Override // sun.util.logging.PlatformLogger.LoggerProxy
        Level getLevel() {
            return this.level;
        }

        @Override // sun.util.logging.PlatformLogger.LoggerProxy
        void setLevel(Level level) {
            if (this.level != level) {
                this.level = level;
                this.effectiveLevel = deriveEffectiveLevel(level);
            }
        }

        @Override // sun.util.logging.PlatformLogger.LoggerProxy
        void doLog(Level level, String str) {
            if (isLoggable(level)) {
                outputStream().print(format(level, str, null));
            }
        }

        @Override // sun.util.logging.PlatformLogger.LoggerProxy
        void doLog(Level level, String str, Throwable th) {
            if (isLoggable(level)) {
                outputStream().print(format(level, str, th));
            }
        }

        @Override // sun.util.logging.PlatformLogger.LoggerProxy
        void doLog(Level level, String str, Object... objArr) {
            if (isLoggable(level)) {
                outputStream().print(format(level, formatMessage(str, objArr), null));
            }
        }

        @Override // sun.util.logging.PlatformLogger.LoggerProxy
        boolean isLoggable(Level level) {
            Level level2 = this.effectiveLevel;
            return level.intValue() >= level2.intValue() && level2 != Level.OFF;
        }

        private Level deriveEffectiveLevel(Level level) {
            return level == null ? PlatformLogger.DEFAULT_LEVEL : level;
        }

        private String formatMessage(String str, Object... objArr) {
            if (objArr != null) {
                try {
                    if (objArr.length != 0) {
                        if (str.indexOf("{0") >= 0 || str.indexOf("{1") >= 0 || str.indexOf("{2") >= 0 || str.indexOf("{3") >= 0) {
                            return MessageFormat.format(str, objArr);
                        }
                        return str;
                    }
                } catch (Exception e2) {
                    return str;
                }
            }
            return str;
        }

        private synchronized String format(Level level, String str, Throwable th) {
            this.date.setTime(System.currentTimeMillis());
            String string = "";
            if (th != null) {
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                printWriter.println();
                th.printStackTrace(printWriter);
                printWriter.close();
                string = stringWriter.toString();
            }
            return String.format(formatString, this.date, getCallerInfo(), this.name, level.name(), str, string);
        }

        private String getCallerInfo() {
            String str = null;
            String methodName = null;
            JavaLangAccess javaLangAccess = SharedSecrets.getJavaLangAccess();
            Throwable th = new Throwable();
            int stackTraceDepth = javaLangAccess.getStackTraceDepth(th);
            boolean z2 = true;
            int i2 = 0;
            while (true) {
                if (i2 >= stackTraceDepth) {
                    break;
                }
                StackTraceElement stackTraceElement = javaLangAccess.getStackTraceElement(th, i2);
                String className = stackTraceElement.getClassName();
                if (z2) {
                    if (className.equals("sun.util.logging.PlatformLogger")) {
                        z2 = false;
                    }
                } else if (!className.equals("sun.util.logging.PlatformLogger")) {
                    str = className;
                    methodName = stackTraceElement.getMethodName();
                    break;
                }
                i2++;
            }
            if (str != null) {
                return str + " " + methodName;
            }
            return this.name;
        }
    }

    /* loaded from: rt.jar:sun/util/logging/PlatformLogger$JavaLoggerProxy.class */
    private static final class JavaLoggerProxy extends LoggerProxy {
        private final Object javaLogger;

        static {
            for (Level level : Level.values()) {
                level.javaLevel = LoggingSupport.parseLevel(level.name());
            }
        }

        JavaLoggerProxy(String str) {
            this(str, null);
        }

        JavaLoggerProxy(String str, Level level) {
            super(str);
            this.javaLogger = LoggingSupport.getLogger(str);
            if (level != null) {
                LoggingSupport.setLevel(this.javaLogger, level.javaLevel);
            }
        }

        @Override // sun.util.logging.PlatformLogger.LoggerProxy
        void doLog(Level level, String str) {
            LoggingSupport.log(this.javaLogger, level.javaLevel, str);
        }

        @Override // sun.util.logging.PlatformLogger.LoggerProxy
        void doLog(Level level, String str, Throwable th) {
            LoggingSupport.log(this.javaLogger, level.javaLevel, str, th);
        }

        @Override // sun.util.logging.PlatformLogger.LoggerProxy
        void doLog(Level level, String str, Object... objArr) {
            if (!isLoggable(level)) {
                return;
            }
            int length = objArr != null ? objArr.length : 0;
            String[] strArr = new String[length];
            for (int i2 = 0; i2 < length; i2++) {
                strArr[i2] = String.valueOf(objArr[i2]);
            }
            LoggingSupport.log(this.javaLogger, level.javaLevel, str, strArr);
        }

        @Override // sun.util.logging.PlatformLogger.LoggerProxy
        boolean isEnabled() {
            return LoggingSupport.isLoggable(this.javaLogger, Level.OFF.javaLevel);
        }

        @Override // sun.util.logging.PlatformLogger.LoggerProxy
        Level getLevel() {
            Object level = LoggingSupport.getLevel(this.javaLogger);
            if (level == null) {
                return null;
            }
            try {
                return Level.valueOf(LoggingSupport.getLevelName(level));
            } catch (IllegalArgumentException e2) {
                return Level.valueOf(LoggingSupport.getLevelValue(level));
            }
        }

        @Override // sun.util.logging.PlatformLogger.LoggerProxy
        void setLevel(Level level) {
            LoggingSupport.setLevel(this.javaLogger, level == null ? null : level.javaLevel);
        }

        @Override // sun.util.logging.PlatformLogger.LoggerProxy
        boolean isLoggable(Level level) {
            return LoggingSupport.isLoggable(this.javaLogger, level.javaLevel);
        }
    }
}
