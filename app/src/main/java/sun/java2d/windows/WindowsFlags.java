package sun.java2d.windows;

import java.security.AccessController;
import java.security.PrivilegedAction;
import org.icepdf.core.util.PdfOps;
import sun.awt.windows.WToolkit;
import sun.java2d.opengl.WGLGraphicsConfig;

/* loaded from: rt.jar:sun/java2d/windows/WindowsFlags.class */
public class WindowsFlags {
    private static boolean gdiBlitEnabled;
    private static boolean d3dEnabled;
    private static boolean d3dVerbose;
    private static boolean d3dSet;
    private static boolean d3dOnScreenEnabled;
    private static boolean oglEnabled;
    private static boolean oglVerbose;
    private static boolean offscreenSharingEnabled;
    private static boolean accelReset;
    private static boolean checkRegistry;
    private static boolean disableRegistry;
    private static boolean magPresent;
    private static boolean setHighDPIAware;
    private static String javaVersion;

    private static native boolean initNativeFlags();

    static {
        WToolkit.loadLibraries();
        initJavaFlags();
        initNativeFlags();
    }

    public static void initFlags() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean getBooleanProp(String str, boolean z2) {
        String property = System.getProperty(str);
        boolean z3 = z2;
        if (property != null) {
            if (property.equals("true") || property.equals("t") || property.equals("True") || property.equals("T") || property.equals("")) {
                z3 = true;
            } else if (property.equals("false") || property.equals(PdfOps.f_TOKEN) || property.equals("False") || property.equals(PdfOps.F_TOKEN)) {
                z3 = false;
            }
        }
        return z3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isBooleanPropTrueVerbose(String str) {
        String property = System.getProperty(str);
        if (property != null) {
            if (property.equals("True") || property.equals("T")) {
                return true;
            }
            return false;
        }
        return false;
    }

    private static int getIntProp(String str, int i2) {
        String property = System.getProperty(str);
        int i3 = i2;
        if (property != null) {
            try {
                i3 = Integer.parseInt(property);
            } catch (NumberFormatException e2) {
            }
        }
        return i3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean getPropertySet(String str) {
        return System.getProperty(str) != null;
    }

    private static void initJavaFlags() {
        AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.java2d.windows.WindowsFlags.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                boolean unused = WindowsFlags.magPresent = WindowsFlags.getBooleanProp("javax.accessibility.screen_magnifier_present", false);
                boolean z2 = !WindowsFlags.getBooleanProp("sun.java2d.noddraw", WindowsFlags.magPresent);
                boolean unused2 = WindowsFlags.d3dEnabled = WindowsFlags.getBooleanProp("sun.java2d.d3d", z2 && WindowsFlags.getBooleanProp("sun.java2d.ddoffscreen", z2));
                boolean unused3 = WindowsFlags.d3dOnScreenEnabled = WindowsFlags.getBooleanProp("sun.java2d.d3d.onscreen", WindowsFlags.d3dEnabled);
                boolean unused4 = WindowsFlags.oglEnabled = WindowsFlags.getBooleanProp("sun.java2d.opengl", false);
                if (WindowsFlags.oglEnabled) {
                    boolean unused5 = WindowsFlags.oglVerbose = WindowsFlags.isBooleanPropTrueVerbose("sun.java2d.opengl");
                    if (WGLGraphicsConfig.isWGLAvailable()) {
                        boolean unused6 = WindowsFlags.d3dEnabled = false;
                    } else {
                        if (WindowsFlags.oglVerbose) {
                            System.out.println("Could not enable OpenGL pipeline (WGL not available)");
                        }
                        boolean unused7 = WindowsFlags.oglEnabled = false;
                    }
                }
                boolean unused8 = WindowsFlags.gdiBlitEnabled = WindowsFlags.getBooleanProp("sun.java2d.gdiBlit", true);
                boolean unused9 = WindowsFlags.d3dSet = WindowsFlags.getPropertySet("sun.java2d.d3d");
                if (WindowsFlags.d3dSet) {
                    boolean unused10 = WindowsFlags.d3dVerbose = WindowsFlags.isBooleanPropTrueVerbose("sun.java2d.d3d");
                }
                boolean unused11 = WindowsFlags.offscreenSharingEnabled = WindowsFlags.getBooleanProp("sun.java2d.offscreenSharing", false);
                boolean unused12 = WindowsFlags.accelReset = WindowsFlags.getBooleanProp("sun.java2d.accelReset", false);
                boolean unused13 = WindowsFlags.checkRegistry = WindowsFlags.getBooleanProp("sun.java2d.checkRegistry", false);
                boolean unused14 = WindowsFlags.disableRegistry = WindowsFlags.getBooleanProp("sun.java2d.disableRegistry", false);
                String unused15 = WindowsFlags.javaVersion = System.getProperty("java.version");
                if (WindowsFlags.javaVersion == null) {
                    String unused16 = WindowsFlags.javaVersion = "default";
                } else {
                    int iIndexOf = WindowsFlags.javaVersion.indexOf(45);
                    if (iIndexOf >= 0) {
                        String unused17 = WindowsFlags.javaVersion = WindowsFlags.javaVersion.substring(0, iIndexOf);
                    }
                }
                String property = System.getProperty("sun.java2d.dpiaware");
                if (property != null) {
                    boolean unused18 = WindowsFlags.setHighDPIAware = property.equalsIgnoreCase("true");
                    return null;
                }
                boolean unused19 = WindowsFlags.setHighDPIAware = System.getProperty("sun.java.launcher", "unknown").equalsIgnoreCase("SUN_STANDARD");
                return null;
            }
        });
    }

    public static boolean isD3DEnabled() {
        return d3dEnabled;
    }

    public static boolean isD3DSet() {
        return d3dSet;
    }

    public static boolean isD3DOnScreenEnabled() {
        return d3dOnScreenEnabled;
    }

    public static boolean isD3DVerbose() {
        return d3dVerbose;
    }

    public static boolean isGdiBlitEnabled() {
        return gdiBlitEnabled;
    }

    public static boolean isTranslucentAccelerationEnabled() {
        return d3dEnabled;
    }

    public static boolean isOffscreenSharingEnabled() {
        return offscreenSharingEnabled;
    }

    public static boolean isMagPresent() {
        return magPresent;
    }

    public static boolean isOGLEnabled() {
        return oglEnabled;
    }

    public static boolean isOGLVerbose() {
        return oglVerbose;
    }
}
