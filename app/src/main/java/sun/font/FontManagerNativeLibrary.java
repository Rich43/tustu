package sun.font;

import java.security.AccessController;
import java.security.PrivilegedAction;

/* loaded from: rt.jar:sun/font/FontManagerNativeLibrary.class */
public class FontManagerNativeLibrary {
    static {
        AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.font.FontManagerNativeLibrary.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                System.loadLibrary("awt");
                if (FontUtilities.isOpenJDK && System.getProperty("os.name").startsWith("Windows")) {
                    System.loadLibrary("freetype");
                }
                System.loadLibrary("fontmanager");
                return null;
            }
        });
    }

    public static void load() {
    }
}
