package com.sun.media.jfxmedia.logging;

import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import java.util.jar.Pack200;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: jfxrt.jar:com/sun/media/jfxmedia/logging/Logger.class */
public class Logger {
    public static final int OFF = Integer.MAX_VALUE;
    public static final int ERROR = 4;
    public static final int WARNING = 3;
    public static final int INFO = 2;
    public static final int DEBUG = 1;
    private static int currentLevel = Integer.MAX_VALUE;
    private static long startTime = 0;
    private static final Object lock = new Object();

    private static native boolean nativeInit();

    private static native void nativeSetNativeLevel(int i2);

    static {
        startLogger();
    }

    private static void startLogger() {
        Integer logLevel;
        try {
            String level = System.getProperty("jfxmedia.loglevel", "off").toLowerCase();
            if (level.equals(TransformerFactoryImpl.DEBUG)) {
                logLevel = 1;
            } else if (level.equals("warning")) {
                logLevel = 3;
            } else if (level.equals(Pack200.Packer.ERROR)) {
                logLevel = 4;
            } else if (level.equals("info")) {
                logLevel = 2;
            } else {
                logLevel = Integer.MAX_VALUE;
            }
            setLevel(logLevel.intValue());
            startTime = System.currentTimeMillis();
        } catch (Exception e2) {
        }
        if (canLog(1)) {
            logMsg(1, "Logger initialized");
        }
    }

    private Logger() {
    }

    public static boolean initNative() {
        if (nativeInit()) {
            nativeSetNativeLevel(currentLevel);
            return true;
        }
        return false;
    }

    public static void setLevel(int level) {
        currentLevel = level;
        try {
            nativeSetNativeLevel(level);
        } catch (UnsatisfiedLinkError e2) {
        }
    }

    public static boolean canLog(int level) {
        if (level < currentLevel) {
            return false;
        }
        return true;
    }

    public static void logMsg(int level, String msg) {
        synchronized (lock) {
            if (level < currentLevel) {
                return;
            }
            if (level == 4) {
                System.err.println("Error (" + getTimestamp() + "): " + msg);
            } else if (level == 3) {
                System.err.println("Warning (" + getTimestamp() + "): " + msg);
            } else if (level == 2) {
                System.out.println("Info (" + getTimestamp() + "): " + msg);
            } else if (level == 1) {
                System.out.println("Debug (" + getTimestamp() + "): " + msg);
            }
        }
    }

    public static void logMsg(int level, String sourceClass, String sourceMethod, String msg) {
        synchronized (lock) {
            if (level < currentLevel) {
                return;
            }
            logMsg(level, sourceClass + CallSiteDescriptor.TOKEN_DELIMITER + sourceMethod + "() " + msg);
        }
    }

    private static String getTimestamp() {
        long elapsed = System.currentTimeMillis() - startTime;
        long elapsedHours = elapsed / 3600000;
        long elapsedMinutes = (elapsed - (((elapsedHours * 60) * 60) * 1000)) / 60000;
        long elapsedSeconds = ((elapsed - (((elapsedHours * 60) * 60) * 1000)) - ((elapsedMinutes * 60) * 1000)) / 1000;
        long elapsedMillis = ((elapsed - (((elapsedHours * 60) * 60) * 1000)) - ((elapsedMinutes * 60) * 1000)) - (elapsedSeconds * 1000);
        return String.format("%d:%02d:%02d:%03d", Long.valueOf(elapsedHours), Long.valueOf(elapsedMinutes), Long.valueOf(elapsedSeconds), Long.valueOf(elapsedMillis));
    }
}
