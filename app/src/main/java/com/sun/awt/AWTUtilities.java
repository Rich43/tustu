package com.sun.awt;

import java.awt.Component;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.Window;
import sun.awt.AWTAccessor;
import sun.awt.SunToolkit;

/* loaded from: rt.jar:com/sun/awt/AWTUtilities.class */
public final class AWTUtilities {

    /* loaded from: rt.jar:com/sun/awt/AWTUtilities$Translucency.class */
    public enum Translucency {
        PERPIXEL_TRANSPARENT,
        TRANSLUCENT,
        PERPIXEL_TRANSLUCENT
    }

    private AWTUtilities() {
    }

    public static boolean isTranslucencySupported(Translucency translucency) {
        switch (translucency) {
            case PERPIXEL_TRANSPARENT:
                return isWindowShapingSupported();
            case TRANSLUCENT:
                return isWindowOpacitySupported();
            case PERPIXEL_TRANSLUCENT:
                return isWindowTranslucencySupported();
            default:
                return false;
        }
    }

    private static boolean isWindowOpacitySupported() {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        if (!(defaultToolkit instanceof SunToolkit)) {
            return false;
        }
        return ((SunToolkit) defaultToolkit).isWindowOpacitySupported();
    }

    public static void setWindowOpacity(Window window, float f2) {
        if (window == null) {
            throw new NullPointerException("The window argument should not be null.");
        }
        AWTAccessor.getWindowAccessor().setOpacity(window, f2);
    }

    public static float getWindowOpacity(Window window) {
        if (window == null) {
            throw new NullPointerException("The window argument should not be null.");
        }
        return AWTAccessor.getWindowAccessor().getOpacity(window);
    }

    public static boolean isWindowShapingSupported() {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        if (!(defaultToolkit instanceof SunToolkit)) {
            return false;
        }
        return ((SunToolkit) defaultToolkit).isWindowShapingSupported();
    }

    public static Shape getWindowShape(Window window) {
        if (window == null) {
            throw new NullPointerException("The window argument should not be null.");
        }
        return AWTAccessor.getWindowAccessor().getShape(window);
    }

    public static void setWindowShape(Window window, Shape shape) {
        if (window == null) {
            throw new NullPointerException("The window argument should not be null.");
        }
        AWTAccessor.getWindowAccessor().setShape(window, shape);
    }

    private static boolean isWindowTranslucencySupported() throws HeadlessException {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        if (!(defaultToolkit instanceof SunToolkit) || !((SunToolkit) defaultToolkit).isWindowTranslucencySupported()) {
            return false;
        }
        GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        if (isTranslucencyCapable(localGraphicsEnvironment.getDefaultScreenDevice().getDefaultConfiguration())) {
            return true;
        }
        for (GraphicsDevice graphicsDevice : localGraphicsEnvironment.getScreenDevices()) {
            for (GraphicsConfiguration graphicsConfiguration : graphicsDevice.getConfigurations()) {
                if (isTranslucencyCapable(graphicsConfiguration)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void setWindowOpaque(Window window, boolean z2) {
        if (window == null) {
            throw new NullPointerException("The window argument should not be null.");
        }
        if (!z2 && !isTranslucencySupported(Translucency.PERPIXEL_TRANSLUCENT)) {
            throw new UnsupportedOperationException("The PERPIXEL_TRANSLUCENT translucency kind is not supported");
        }
        AWTAccessor.getWindowAccessor().setOpaque(window, z2);
    }

    public static boolean isWindowOpaque(Window window) {
        if (window == null) {
            throw new NullPointerException("The window argument should not be null.");
        }
        return window.isOpaque();
    }

    public static boolean isTranslucencyCapable(GraphicsConfiguration graphicsConfiguration) {
        if (graphicsConfiguration == null) {
            throw new NullPointerException("The gc argument should not be null");
        }
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        if (!(defaultToolkit instanceof SunToolkit)) {
            return false;
        }
        return ((SunToolkit) defaultToolkit).isTranslucencyCapable(graphicsConfiguration);
    }

    public static void setComponentMixingCutoutShape(Component component, Shape shape) {
        if (component == null) {
            throw new NullPointerException("The component argument should not be null.");
        }
        AWTAccessor.getComponentAccessor().setMixingCutoutShape(component, shape);
    }
}
