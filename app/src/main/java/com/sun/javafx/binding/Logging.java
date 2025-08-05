package com.sun.javafx.binding;

import sun.util.logging.PlatformLogger;

/* loaded from: jfxrt.jar:com/sun/javafx/binding/Logging.class */
public class Logging {

    /* loaded from: jfxrt.jar:com/sun/javafx/binding/Logging$LoggerHolder.class */
    private static class LoggerHolder {
        private static final PlatformLogger INSTANCE = PlatformLogger.getLogger("javafx.beans");

        private LoggerHolder() {
        }
    }

    public static PlatformLogger getLogger() {
        return LoggerHolder.INSTANCE;
    }
}
