package sun.java2d.marlin;

import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:sun/java2d/marlin/MarlinUtils.class */
public final class MarlinUtils {
    private static final PlatformLogger log;

    static {
        if (MarlinConst.useLogger) {
            log = PlatformLogger.getLogger("sun.java2d.marlin");
        } else {
            log = null;
        }
    }

    private MarlinUtils() {
    }

    public static void logInfo(String str) {
        if (MarlinConst.useLogger) {
            log.info(str);
        } else if (MarlinConst.enableLogs) {
            System.out.print("INFO: ");
            System.out.println(str);
        }
    }

    public static void logException(String str, Throwable th) {
        if (MarlinConst.useLogger) {
            log.warning(str, th);
        } else if (MarlinConst.enableLogs) {
            System.out.print("WARNING: ");
            System.out.println(str);
            th.printStackTrace(System.err);
        }
    }
}
