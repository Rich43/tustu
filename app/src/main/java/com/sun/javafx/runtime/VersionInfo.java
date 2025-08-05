package com.sun.javafx.runtime;

import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:com/sun/javafx/runtime/VersionInfo.class */
public class VersionInfo {
    private static final String BUILD_TIMESTAMP = "Fri Jul 07 07:56:45 UTC 2023";
    private static final String HUDSON_JOB_NAME = ".";
    private static final String HUDSON_BUILD_NUMBER = "0000";
    private static final String PROMOTED_BUILD_NUMBER = "01";
    private static final String PRODUCT_NAME = "OpenJFX";
    private static final String RAW_VERSION = "8.0.382";
    private static final String RELEASE_MILESTONE = "";
    private static final String RELEASE_NAME = "8u382";
    private static final String VERSION;
    private static final String RUNTIME_VERSION;

    static {
        String tmpVersion;
        String tmpVersion2 = RAW_VERSION;
        if (getReleaseMilestone().length() > 0) {
            tmpVersion2 = tmpVersion2 + LanguageTag.SEP;
        }
        VERSION = tmpVersion2;
        if (getHudsonJobName().length() > 0) {
            tmpVersion = tmpVersion2 + "-b01";
        } else {
            tmpVersion = tmpVersion2 + " (Fri Jul 07 07:56:45 UTC 2023)";
        }
        RUNTIME_VERSION = tmpVersion;
    }

    public static synchronized void setupSystemProperties() {
        if (System.getProperty("javafx.version") == null) {
            System.setProperty("javafx.version", getVersion());
            System.setProperty("javafx.runtime.version", getRuntimeVersion());
        }
    }

    public static String getBuildTimestamp() {
        return BUILD_TIMESTAMP;
    }

    public static String getHudsonJobName() {
        if (".".equals("not_hudson")) {
            return "";
        }
        return ".";
    }

    public static String getHudsonBuildNumber() {
        return HUDSON_BUILD_NUMBER;
    }

    public static String getReleaseMilestone() {
        if ("".equals("fcs")) {
            return "";
        }
        return "";
    }

    public static String getVersion() {
        return VERSION;
    }

    public static String getRuntimeVersion() {
        return RUNTIME_VERSION;
    }
}
