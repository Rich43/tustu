package java.util.logging;

import java.util.List;
import org.icepdf.core.util.PdfOps;
import sun.util.logging.LoggingProxy;

/* loaded from: rt.jar:java/util/logging/LoggingProxyImpl.class */
class LoggingProxyImpl implements LoggingProxy {
    static final LoggingProxy INSTANCE = new LoggingProxyImpl();

    private LoggingProxyImpl() {
    }

    @Override // sun.util.logging.LoggingProxy
    public Object getLogger(String str) {
        return Logger.getPlatformLogger(str);
    }

    @Override // sun.util.logging.LoggingProxy
    public Object getLevel(Object obj) {
        return ((Logger) obj).getLevel();
    }

    @Override // sun.util.logging.LoggingProxy
    public void setLevel(Object obj, Object obj2) throws SecurityException {
        ((Logger) obj).setLevel((Level) obj2);
    }

    @Override // sun.util.logging.LoggingProxy
    public boolean isLoggable(Object obj, Object obj2) {
        return ((Logger) obj).isLoggable((Level) obj2);
    }

    @Override // sun.util.logging.LoggingProxy
    public void log(Object obj, Object obj2, String str) {
        ((Logger) obj).log((Level) obj2, str);
    }

    @Override // sun.util.logging.LoggingProxy
    public void log(Object obj, Object obj2, String str, Throwable th) {
        ((Logger) obj).log((Level) obj2, str, th);
    }

    @Override // sun.util.logging.LoggingProxy
    public void log(Object obj, Object obj2, String str, Object... objArr) {
        ((Logger) obj).log((Level) obj2, str, objArr);
    }

    @Override // sun.util.logging.LoggingProxy
    public List<String> getLoggerNames() {
        return LogManager.getLoggingMXBean().getLoggerNames();
    }

    @Override // sun.util.logging.LoggingProxy
    public String getLoggerLevel(String str) {
        return LogManager.getLoggingMXBean().getLoggerLevel(str);
    }

    @Override // sun.util.logging.LoggingProxy
    public void setLoggerLevel(String str, String str2) {
        LogManager.getLoggingMXBean().setLoggerLevel(str, str2);
    }

    @Override // sun.util.logging.LoggingProxy
    public String getParentLoggerName(String str) {
        return LogManager.getLoggingMXBean().getParentLoggerName(str);
    }

    @Override // sun.util.logging.LoggingProxy
    public Object parseLevel(String str) {
        Level levelFindLevel = Level.findLevel(str);
        if (levelFindLevel == null) {
            throw new IllegalArgumentException("Unknown level \"" + str + PdfOps.DOUBLE_QUOTE__TOKEN);
        }
        return levelFindLevel;
    }

    @Override // sun.util.logging.LoggingProxy
    public String getLevelName(Object obj) {
        return ((Level) obj).getLevelName();
    }

    @Override // sun.util.logging.LoggingProxy
    public int getLevelValue(Object obj) {
        return ((Level) obj).intValue();
    }

    @Override // sun.util.logging.LoggingProxy
    public String getProperty(String str) {
        return LogManager.getLogManager().getProperty(str);
    }
}
