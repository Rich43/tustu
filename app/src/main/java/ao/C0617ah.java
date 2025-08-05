package ao;

import java.util.ArrayList;
import k.C1753a;

/* renamed from: ao.ah, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/ah.class */
public class C0617ah {
    public static boolean a(String str) {
        return !h.i.e(c(str), "").equals("");
    }

    public static boolean b(String str) {
        return h.i.c(c(str)) != null;
    }

    public static String c(String str) {
        return "APPEND_FIELD_" + str;
    }

    public static String[] a(String str, String str2) {
        try {
            String[] strArrB = new C1753a(str2).b();
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 < strArrB.length; i2++) {
                if (b(strArrB[i2]) && !a(strArrB[i2])) {
                    arrayList.add(strArrB[i2]);
                } else if (!strArrB[i2].equals(str) && b(strArrB[i2])) {
                    String[] strArrA = a(strArrB[i2], h.i.c(c(strArrB[i2])));
                    if (strArrA != null && strArrA.length > 0) {
                        int i3 = 0;
                        while (i2 < strArrA.length) {
                            if (!arrayList.contains(strArrA[i2])) {
                                arrayList.add(strArrA[i2]);
                            }
                            i3++;
                        }
                    }
                }
            }
            return (String[]) arrayList.toArray(new String[arrayList.size()]);
        } catch (ax.U e2) {
            e2.printStackTrace();
            return new String[0];
        }
    }
}
