package bH;

import java.awt.Window;

/* loaded from: TunerStudioMS.jar:bH/J.class */
public class J {
    public static void a(Window window) {
        try {
            Class<?> cls = Class.forName("com.apple.eawt.FullScreenUtilities");
            cls.getMethod("setWindowCanFullScreen", Window.class, Boolean.TYPE).invoke(cls, window, true);
        } catch (Exception e2) {
            C.a(e2);
        }
    }

    public static void b(Window window) {
        try {
            Class<?> cls = Class.forName("com.apple.eawt.Application");
            cls.getMethod("requestToggleFullScreen", Window.class).invoke(cls.getMethod("getApplication", new Class[0]).invoke(cls, new Object[0]), window);
        } catch (Exception e2) {
            C.a(e2);
        }
    }
}
