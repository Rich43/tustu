package jdk.management.jfr;

import java.lang.management.ManagementPermission;
import java.security.Permission;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import jdk.jfr.internal.management.ManagementSupport;

/* loaded from: jfr.jar:jdk/management/jfr/MBeanUtils.class */
final class MBeanUtils {
    private static final Permission monitor = new ManagementPermission("monitor");
    private static final Permission control = new ManagementPermission("control");

    MBeanUtils() {
    }

    static ObjectName createObjectName() {
        try {
            return new ObjectName(FlightRecorderMXBean.MXBEAN_NAME);
        } catch (MalformedObjectNameException e2) {
            throw new Error("Can't happen", e2);
        }
    }

    static void checkControl() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(control);
        }
    }

    static void checkMonitor() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(monitor);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    static <T, R> List<R> transformList(List<T> list, Function<T, R> function) {
        return (List) list.stream().map(function).collect(Collectors.toList());
    }

    static boolean booleanValue(String str) {
        if ("true".equals(str)) {
            return true;
        }
        if ("false".equals(str)) {
            return false;
        }
        throw new IllegalArgumentException("Value must be true or false.");
    }

    static Duration duration(String str) throws NumberFormatException {
        if (str == null) {
            return null;
        }
        long timespan = ManagementSupport.parseTimespan(str);
        if (timespan == 0) {
            return null;
        }
        return Duration.ofNanos(timespan);
    }

    public static Instant parseTimestamp(String str, Instant instant) {
        if (str == null) {
            return instant;
        }
        try {
            return Instant.parse(str);
        } catch (DateTimeParseException e2) {
            try {
                return Instant.ofEpochMilli(Long.parseLong(str));
            } catch (NumberFormatException | DateTimeException e3) {
                throw new IllegalArgumentException("Not a valid timestamp " + str);
            }
        }
    }

    static Long size(String str) throws NumberFormatException {
        long j2 = Long.parseLong(str);
        if (j2 < 0) {
            throw new IllegalArgumentException("Negative size not allowed");
        }
        return Long.valueOf(j2);
    }

    public static int parseBlockSize(String str, int i2) throws NumberFormatException {
        if (str == null) {
            return i2;
        }
        int i3 = Integer.parseInt(str);
        if (i3 < 1) {
            throw new IllegalArgumentException("Block size must be at least 1 byte");
        }
        return i3;
    }
}
