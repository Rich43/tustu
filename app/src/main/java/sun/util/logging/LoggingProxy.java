package sun.util.logging;

import java.util.List;

/* loaded from: rt.jar:sun/util/logging/LoggingProxy.class */
public interface LoggingProxy {
    Object getLogger(String str);

    Object getLevel(Object obj);

    void setLevel(Object obj, Object obj2);

    boolean isLoggable(Object obj, Object obj2);

    void log(Object obj, Object obj2, String str);

    void log(Object obj, Object obj2, String str, Throwable th);

    void log(Object obj, Object obj2, String str, Object... objArr);

    List<String> getLoggerNames();

    String getLoggerLevel(String str);

    void setLoggerLevel(String str, String str2);

    String getParentLoggerName(String str);

    Object parseLevel(String str);

    String getLevelName(Object obj);

    int getLevelValue(Object obj);

    String getProperty(String str);
}
