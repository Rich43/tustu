package jdk.jfr.internal;

/* loaded from: jfr.jar:jdk/jfr/internal/LogLevel.class */
public enum LogLevel {
    TRACE(1),
    DEBUG(2),
    INFO(3),
    WARN(4),
    ERROR(5);

    final int level;

    LogLevel(int i2) {
        this.level = i2;
    }
}
