package bH;

import com.sun.glass.ui.Platform;

/* renamed from: bH.p, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bH/p.class */
public class C1008p {
    public static boolean a() {
        return System.getProperty("os.name", "Windows").startsWith(Platform.WINDOWS);
    }

    public static boolean b() {
        return System.getProperty("os.name", "").startsWith("Android");
    }

    public static float c() {
        String property = System.getProperty("java.specification.version");
        try {
            return Float.parseFloat(property);
        } catch (Exception e2) {
            C.a("Failed to get Java Spec from: " + property);
            return Float.NaN;
        }
    }
}
