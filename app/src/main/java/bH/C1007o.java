package bH;

import G.C0126i;
import G.aI;

/* renamed from: bH.o, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bH/o.class */
public class C1007o {

    /* renamed from: a, reason: collision with root package name */
    public static String f7057a = "+-/=&<>*^!%, [{()}]|\n\t";

    public static boolean a(String str, G.R r2) {
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        try {
            double dA = C0126i.a((aI) r2, str);
            return Double.isNaN(dA) || dA != 0.0d;
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new V.g("Unable to evaluate condition:'" + str + "'\n" + e2.getMessage() + " \nCheck Log for more detail.");
        }
    }

    public static double b(String str, G.R r2) throws V.g {
        if (str == null || str.trim().length() == 0) {
            return 0.0d;
        }
        try {
            return C0126i.a((aI) r2, str);
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new V.g("Unable to evaluate condition:'" + str + "'\n" + e2.getMessage() + " \nCheck Log for more detail.");
        }
    }
}
