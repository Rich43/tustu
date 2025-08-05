package bh;

import G.C0076bi;
import G.R;
import java.util.Iterator;

/* renamed from: bh.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bh/a.class */
public class C1141a {
    public static String a(R r2) {
        String strA = a(r2, "ve");
        if (strA == null) {
            strA = a(r2, "fuel");
        }
        if (strA == null) {
            strA = c(r2);
        }
        return strA;
    }

    public static String b(R r2) {
        String strA = a(r2, "spark");
        if (strA == null) {
            strA = a(r2, "adv");
        }
        if (strA == null) {
            strA = c(r2);
        }
        return strA;
    }

    private static String a(R r2, String str) {
        Iterator itO = r2.o();
        while (itO.hasNext()) {
            C0076bi c0076bi = (C0076bi) itO.next();
            if (c0076bi.a().toLowerCase().startsWith(str.toLowerCase())) {
                return c0076bi.a();
            }
        }
        return null;
    }

    private static String c(R r2) {
        try {
            return ((C0076bi) r2.o().next()).a();
        } catch (Exception e2) {
            return null;
        }
    }
}
