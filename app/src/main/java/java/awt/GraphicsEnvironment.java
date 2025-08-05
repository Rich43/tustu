package java.awt;

import java.awt.image.BufferedImage;
import java.security.AccessController;
import java.util.Locale;
import sun.font.FontManagerFactory;
import sun.java2d.HeadlessGraphicsEnvironment;
import sun.java2d.SunGraphicsEnvironment;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:java/awt/GraphicsEnvironment.class */
public abstract class GraphicsEnvironment {
    private static GraphicsEnvironment localEnv;
    private static Boolean headless;
    private static Boolean defaultHeadless;

    public abstract GraphicsDevice[] getScreenDevices() throws HeadlessException;

    public abstract GraphicsDevice getDefaultScreenDevice() throws HeadlessException;

    public abstract Graphics2D createGraphics(BufferedImage bufferedImage);

    public abstract Font[] getAllFonts();

    public abstract String[] getAvailableFontFamilyNames();

    public abstract String[] getAvailableFontFamilyNames(Locale locale);

    protected GraphicsEnvironment() {
    }

    public static synchronized GraphicsEnvironment getLocalGraphicsEnvironment() {
        if (localEnv == null) {
            localEnv = createGE();
        }
        return localEnv;
    }

    private static GraphicsEnvironment createGE() {
        Class<?> cls;
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("java.awt.graphicsenv", null));
        try {
            try {
                cls = Class.forName(str);
            } catch (ClassNotFoundException e2) {
                throw new Error("Could not find class: " + str);
            } catch (IllegalAccessException e3) {
                throw new Error("Could not access Graphics Environment: " + str);
            } catch (InstantiationException e4) {
                throw new Error("Could not instantiate Graphics Environment: " + str);
            }
        } catch (ClassNotFoundException e5) {
            cls = Class.forName(str, true, ClassLoader.getSystemClassLoader());
        }
        GraphicsEnvironment headlessGraphicsEnvironment = (GraphicsEnvironment) cls.newInstance();
        if (isHeadless()) {
            headlessGraphicsEnvironment = new HeadlessGraphicsEnvironment(headlessGraphicsEnvironment);
        }
        return headlessGraphicsEnvironment;
    }

    public static boolean isHeadless() {
        return getHeadlessProperty();
    }

    static String getHeadlessMessage() {
        if (headless == null) {
            getHeadlessProperty();
        }
        if (defaultHeadless != Boolean.TRUE) {
            return null;
        }
        return "\nNo X11 DISPLAY variable was set, but this program performed an operation which requires it.";
    }

    private static boolean getHeadlessProperty() {
        if (headless == null) {
            AccessController.doPrivileged(() -> {
                String property = System.getProperty("java.awt.headless");
                if (property == null) {
                    if (System.getProperty("javaplugin.version") != null) {
                        Boolean bool = Boolean.FALSE;
                        defaultHeadless = bool;
                        headless = bool;
                        return null;
                    }
                    String property2 = System.getProperty("os.name");
                    if (property2.contains("OS X") && "sun.awt.HToolkit".equals(System.getProperty("awt.toolkit"))) {
                        Boolean bool2 = Boolean.TRUE;
                        defaultHeadless = bool2;
                        headless = bool2;
                        return null;
                    }
                    String str = System.getenv("DISPLAY");
                    Boolean boolValueOf = Boolean.valueOf(("Linux".equals(property2) || "SunOS".equals(property2) || "FreeBSD".equals(property2) || "NetBSD".equals(property2) || "OpenBSD".equals(property2) || "AIX".equals(property2)) && (str == null || str.trim().isEmpty()));
                    defaultHeadless = boolValueOf;
                    headless = boolValueOf;
                    return null;
                }
                headless = Boolean.valueOf(property);
                return null;
            });
        }
        return headless.booleanValue();
    }

    static void checkHeadless() throws HeadlessException {
        if (isHeadless()) {
            throw new HeadlessException();
        }
    }

    public boolean isHeadlessInstance() {
        return getHeadlessProperty();
    }

    public boolean registerFont(Font font) {
        if (font == null) {
            throw new NullPointerException("font cannot be null.");
        }
        return FontManagerFactory.getInstance().registerFont(font);
    }

    public void preferLocaleFonts() {
        FontManagerFactory.getInstance().preferLocaleFonts();
    }

    public void preferProportionalFonts() {
        FontManagerFactory.getInstance().preferProportionalFonts();
    }

    public Point getCenterPoint() throws HeadlessException {
        Rectangle usableBounds = SunGraphicsEnvironment.getUsableBounds(getDefaultScreenDevice());
        return new Point((usableBounds.width / 2) + usableBounds.f12372x, (usableBounds.height / 2) + usableBounds.f12373y);
    }

    public Rectangle getMaximumWindowBounds() throws HeadlessException {
        return SunGraphicsEnvironment.getUsableBounds(getDefaultScreenDevice());
    }
}
