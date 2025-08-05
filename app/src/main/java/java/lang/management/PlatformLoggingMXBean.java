package java.lang.management;

import java.util.List;

/* loaded from: rt.jar:java/lang/management/PlatformLoggingMXBean.class */
public interface PlatformLoggingMXBean extends PlatformManagedObject {
    List<String> getLoggerNames();

    String getLoggerLevel(String str);

    void setLoggerLevel(String str, String str2);

    String getParentLoggerName(String str);
}
