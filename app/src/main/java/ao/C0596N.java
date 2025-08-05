package ao;

import W.C0188n;
import org.apache.commons.math3.geometry.VectorFormat;

/* renamed from: ao.N, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/N.class */
public class C0596N {
    public static boolean a(String str) {
        if (str.trim().equals("")) {
            throw new C0657bu("Expression can not be empty");
        }
        C0188n c0188nR = C0804hg.a().r();
        if (c0188nR == null || c0188nR.d() <= 0) {
            throw new C0745fb();
        }
        String str2 = str;
        while (str2.indexOf(VectorFormat.DEFAULT_PREFIX) > 0) {
            try {
                str2 = str2.substring(0, str2.indexOf(VectorFormat.DEFAULT_PREFIX)) + "1.0" + str2.substring(str2.indexOf("}", str2.indexOf(VectorFormat.DEFAULT_PREFIX)) + 1);
            } catch (Exception e2) {
                throw new C0657bu("Invalid Formula:" + str + "\nPlease check your syntax.");
            }
        }
        return true;
    }
}
