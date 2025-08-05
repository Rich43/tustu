package sun.font;

import java.security.AccessController;
import java.security.PrivilegedAction;

/* loaded from: rt.jar:sun/font/FontManagerFactory.class */
public final class FontManagerFactory {
    private static FontManager instance = null;
    private static final String DEFAULT_CLASS;

    static {
        if (FontUtilities.isWindows) {
            DEFAULT_CLASS = "sun.awt.Win32FontManager";
        } else if (FontUtilities.isMacOSX) {
            DEFAULT_CLASS = "sun.font.CFontManager";
        } else {
            DEFAULT_CLASS = "sun.awt.X11FontManager";
        }
    }

    public static synchronized FontManager getInstance() {
        if (instance != null) {
            return instance;
        }
        AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.font.FontManagerFactory.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                try {
                    FontManager unused = FontManagerFactory.instance = (FontManager) Class.forName(System.getProperty("sun.font.fontmanager", FontManagerFactory.DEFAULT_CLASS), true, ClassLoader.getSystemClassLoader()).newInstance();
                    return null;
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e2) {
                    throw new InternalError(e2);
                }
            }
        });
        return instance;
    }
}
