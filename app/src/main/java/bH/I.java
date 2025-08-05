package bH;

import com.sun.glass.ui.Platform;

/* loaded from: TunerStudioMS.jar:bH/I.class */
public class I {
    public static boolean a() {
        return System.getProperty("os.name", "Windows").startsWith(Platform.WINDOWS);
    }

    public static boolean b() {
        return System.getProperty("os.name", "").startsWith(Platform.MAC);
    }

    public static boolean c() {
        String property = System.getProperty("os.version", "");
        if (!property.contains(".")) {
            return false;
        }
        while (property.split(".").length > 2) {
            try {
                property = property.substring(0, property.lastIndexOf("."));
            } catch (Exception e2) {
                return false;
            }
        }
        double d2 = Double.parseDouble(property.substring(0, property.indexOf(".")));
        return b() && (d2 >= 11.0d || (d2 >= 10.0d && Double.parseDouble(property.substring(property.indexOf(".") + 1)) >= 7.0d));
    }

    public static boolean d() {
        return System.getProperty("os.name", "").startsWith("Linux");
    }

    public static boolean e() {
        String property = System.getProperty("os.arch", "");
        return d() && (property.contains("arm") || property.contains("aarch64"));
    }

    public static boolean f() {
        return System.getProperty("os.name", "Windows").startsWith(Platform.WINDOWS) && System.getProperty("os.version", "10").startsWith("5.1");
    }
}
