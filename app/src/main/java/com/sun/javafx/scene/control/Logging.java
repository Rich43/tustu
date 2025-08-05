package com.sun.javafx.scene.control;

import sun.util.logging.PlatformLogger;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/Logging.class */
public class Logging {
    private static PlatformLogger controlsLogger = null;

    public static final PlatformLogger getControlsLogger() {
        if (controlsLogger == null) {
            controlsLogger = PlatformLogger.getLogger("javafx.scene.control");
        }
        return controlsLogger;
    }
}
