package sun.java2d.marlin;

import java.security.AccessController;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/java2d/marlin/MarlinProperties.class */
public final class MarlinProperties {
    private MarlinProperties() {
    }

    public static boolean isUseThreadLocal() {
        return getBoolean("sun.java2d.renderer.useThreadLocal", "true");
    }

    public static int getInitialImageSize() {
        return getInteger("sun.java2d.renderer.pixelsize", 2048, 64, 32768);
    }

    public static int getSubPixel_Log2_X() {
        return getInteger("sun.java2d.renderer.subPixel_log2_X", 3, 1, 8);
    }

    public static int getSubPixel_Log2_Y() {
        return getInteger("sun.java2d.renderer.subPixel_log2_Y", 3, 1, 8);
    }

    public static int getTileSize_Log2() {
        return getInteger("sun.java2d.renderer.tileSize_log2", 5, 3, 8);
    }

    public static int getBlockSize_Log2() {
        return getInteger("sun.java2d.renderer.blockSize_log2", 5, 3, 8);
    }

    public static boolean isForceRLE() {
        return getBoolean("sun.java2d.renderer.forceRLE", "false");
    }

    public static boolean isForceNoRLE() {
        return getBoolean("sun.java2d.renderer.forceNoRLE", "false");
    }

    public static boolean isUseTileFlags() {
        return getBoolean("sun.java2d.renderer.useTileFlags", "true");
    }

    public static boolean isUseTileFlagsWithHeuristics() {
        return isUseTileFlags() && getBoolean("sun.java2d.renderer.useTileFlags.useHeuristics", "true");
    }

    public static int getRLEMinWidth() {
        return getInteger("sun.java2d.renderer.rleMinWidth", 64, 0, Integer.MAX_VALUE);
    }

    public static boolean isUseSimplifier() {
        return getBoolean("sun.java2d.renderer.useSimplifier", "false");
    }

    public static boolean isDoStats() {
        return getBoolean("sun.java2d.renderer.doStats", "false");
    }

    public static boolean isDoMonitors() {
        return getBoolean("sun.java2d.renderer.doMonitors", "false");
    }

    public static boolean isDoChecks() {
        return getBoolean("sun.java2d.renderer.doChecks", "false");
    }

    public static boolean isLoggingEnabled() {
        return getBoolean("sun.java2d.renderer.log", "false");
    }

    public static boolean isUseLogger() {
        return getBoolean("sun.java2d.renderer.useLogger", "false");
    }

    public static boolean isLogCreateContext() {
        return getBoolean("sun.java2d.renderer.logCreateContext", "false");
    }

    public static boolean isLogUnsafeMalloc() {
        return getBoolean("sun.java2d.renderer.logUnsafeMalloc", "false");
    }

    static boolean getBoolean(String str, String str2) {
        return Boolean.valueOf((String) AccessController.doPrivileged(new GetPropertyAction(str, str2))).booleanValue();
    }

    static int getInteger(String str, int i2, int i3, int i4) {
        String str2 = (String) AccessController.doPrivileged(new GetPropertyAction(str));
        int iIntValue = i2;
        if (str2 != null) {
            try {
                iIntValue = Integer.decode(str2).intValue();
            } catch (NumberFormatException e2) {
                MarlinUtils.logInfo("Invalid integer value for " + str + " = " + str2);
            }
        }
        if (iIntValue < i3 || iIntValue > i4) {
            MarlinUtils.logInfo("Invalid value for " + str + " = " + iIntValue + "; expected value in range[" + i3 + ", " + i4 + "] !");
            iIntValue = i2;
        }
        return iIntValue;
    }
}
