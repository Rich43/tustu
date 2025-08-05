package com.sun.javafx.stage;

import java.security.AccessControlContext;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.stage.Screen;
import javafx.stage.Window;

/* loaded from: jfxrt.jar:com/sun/javafx/stage/WindowHelper.class */
public final class WindowHelper {
    private static WindowAccessor windowAccessor;

    /* loaded from: jfxrt.jar:com/sun/javafx/stage/WindowHelper$WindowAccessor.class */
    public interface WindowAccessor {
        void notifyLocationChanged(Window window, double d2, double d3);

        void notifySizeChanged(Window window, double d2, double d3);

        void notifyScreenChanged(Window window, Object obj, Object obj2);

        float getUIScale(Window window);

        float getRenderScale(Window window);

        ReadOnlyObjectProperty<Screen> screenProperty(Window window);

        AccessControlContext getAccessControlContext(Window window);
    }

    static {
        forceInit(Window.class);
    }

    private WindowHelper() {
    }

    public static void notifyLocationChanged(Window window, double x2, double y2) {
        windowAccessor.notifyLocationChanged(window, x2, y2);
    }

    public static void notifySizeChanged(Window window, double width, double height) {
        windowAccessor.notifySizeChanged(window, width, height);
    }

    static AccessControlContext getAccessControlContext(Window window) {
        return windowAccessor.getAccessControlContext(window);
    }

    public static void setWindowAccessor(WindowAccessor newAccessor) {
        if (windowAccessor != null) {
            throw new IllegalStateException();
        }
        windowAccessor = newAccessor;
    }

    public static WindowAccessor getWindowAccessor() {
        return windowAccessor;
    }

    private static void forceInit(Class<?> classToInit) {
        try {
            Class.forName(classToInit.getName(), true, classToInit.getClassLoader());
        } catch (ClassNotFoundException e2) {
            throw new AssertionError(e2);
        }
    }
}
