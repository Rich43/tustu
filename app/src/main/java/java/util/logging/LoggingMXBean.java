package java.util.logging;

import java.util.List;

/* loaded from: rt.jar:java/util/logging/LoggingMXBean.class */
public interface LoggingMXBean {
    List<String> getLoggerNames();

    String getLoggerLevel(String str);

    void setLoggerLevel(String str, String str2);

    String getParentLoggerName(String str);
}
