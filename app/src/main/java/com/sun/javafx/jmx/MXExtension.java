package com.sun.javafx.jmx;

import com.sun.javafx.util.Logging;

/* loaded from: jfxrt.jar:com/sun/javafx/jmx/MXExtension.class */
public abstract class MXExtension {
    private static final String EXTENSION_CLASS_NAME = System.getProperty("javafx.debug.jmx.class", "com.oracle.javafx.jmx.MXExtensionImpl");

    public abstract void intialize() throws Exception;

    public static void initializeIfAvailable() {
        try {
            ClassLoader loader = MXExtension.class.getClassLoader();
            Class<?> mxExtensionClass = Class.forName(EXTENSION_CLASS_NAME, false, loader);
            if (!MXExtension.class.isAssignableFrom(mxExtensionClass)) {
                throw new IllegalArgumentException("Unrecognized MXExtension class: " + EXTENSION_CLASS_NAME);
            }
            MXExtension mxExtension = (MXExtension) mxExtensionClass.newInstance();
            mxExtension.intialize();
        } catch (Exception e2) {
            Logging.getJavaFXLogger().info("Failed to initialize management extension", e2);
        }
    }
}
