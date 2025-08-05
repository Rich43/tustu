package jdk.jfr.internal;

import java.util.function.Supplier;

/* loaded from: jfr.jar:jdk/jfr/internal/Logger.class */
public final class Logger {
    private static final int MAX_SIZE = 10000;

    static {
        JVMSupport.tryToInitializeJVM();
    }

    public static void log(LogTag logTag, LogLevel logLevel, String str) {
        if (shouldLog(logTag, logLevel)) {
            logInternal(logTag, logLevel, str);
        }
    }

    public static void log(LogTag logTag, LogLevel logLevel, Supplier<String> supplier) {
        if (shouldLog(logTag, logLevel)) {
            logInternal(logTag, logLevel, supplier.get());
        }
    }

    private static void logInternal(LogTag logTag, LogLevel logLevel, String str) {
        if (str == null || str.length() < 10000) {
            JVM.log(logTag.id, logLevel.level, str);
        } else {
            JVM.log(logTag.id, logLevel.level, str.substring(0, 10000));
        }
    }

    public static boolean shouldLog(LogTag logTag, LogLevel logLevel) {
        return JVM.shouldLog(logLevel.level);
    }
}
