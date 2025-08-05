package bI;

import W.C0178d;
import bH.C;
import bH.C1008p;
import java.io.File;

/* loaded from: TunerStudioMS.jar:bI/d.class */
public class d {
    public static String a(byte[] bArr) {
        float fC = C1008p.c();
        if (!Float.isNaN(fC) && fC >= 1.8f) {
            try {
                Class<?> cls = Class.forName("java.util.Base64");
                return (String) Class.forName("java.util.Base64$Encoder").getMethod("encodeToString", byte[].class).invoke(cls.getMethod("getEncoder", new Class[0]).invoke(cls, new Object[0]), bArr);
            } catch (Exception e2) {
                C.d("Could not Base64 encode using 1.8 method, using legacy.");
            }
        }
        return a.a(bArr);
    }

    public static String a(File file) {
        float fC = C1008p.c();
        if (!Float.isNaN(fC) && fC >= 1.8f) {
            byte[] bArrA = C0178d.a(file);
            try {
                Class<?> cls = Class.forName("java.util.Base64");
                return (String) Class.forName("java.util.Base64$Encoder").getMethod("encodeToString", byte[].class).invoke(cls.getMethod("getEncoder", new Class[0]).invoke(cls, new Object[0]), bArrA);
            } catch (Exception e2) {
                C.d("Could not Base64 encode using 1.8 method, using legacy.");
            }
        }
        return a.b(file.getAbsolutePath());
    }

    public static byte[] a(String str) {
        float fC = C1008p.c();
        if (!Float.isNaN(fC) && fC >= 1.8f) {
            try {
                Class<?> cls = Class.forName("java.util.Base64");
                return (byte[]) Class.forName("java.util.Base64$Decoder").getMethod("decode", String.class).invoke(cls.getMethod("getDecoder", new Class[0]).invoke(cls, new Object[0]), str);
            } catch (Exception e2) {
                C.d("Could not Base64 decode using 1.8 method, using legacy.");
            }
        }
        return a.a(str);
    }

    public static void a(String str, File file) {
        float fC = C1008p.c();
        if (!Float.isNaN(fC) && fC >= 1.8f) {
            try {
                Class<?> cls = Class.forName("java.util.Base64");
                C0178d.a(file, (byte[]) Class.forName("java.util.Base64$Decoder").getMethod("decode", String.class).invoke(cls.getMethod("getDecoder", new Class[0]).invoke(cls, new Object[0]), str));
                return;
            } catch (Exception e2) {
                C.d("Could not Base64 decode using 1.8 method, using legacy.");
            }
        }
        a.a(str, file.getAbsolutePath());
    }
}
