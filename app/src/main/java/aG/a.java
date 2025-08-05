package aG;

import G.C0130m;
import G.F;
import bH.C0995c;

/* loaded from: TunerStudioMS.jar:aG/a.class */
public class a {
    public static C0130m a(F f2, int i2) {
        int i3 = i2 * 52;
        int[] iArrA = C0995c.a(i3, new int[2], false);
        C0130m c0130mA = C0130m.a(f2, new int[]{251, iArrA[0], iArrA[1]});
        c0130mA.b(i3 + 1);
        c0130mA.v("Read Original Replay");
        c0130mA.i(250);
        c0130mA.a(3000);
        return c0130mA;
    }

    public static C0130m a(F f2, int i2, int i3, int i4) {
        int[] iArrA = C0995c.a(i3, new int[2], false);
        C0130m c0130mA = C0130m.a(f2, new int[]{i2, iArrA[0], iArrA[1]});
        c0130mA.b(i4 + 1);
        c0130mA.v("Read Extended Replay");
        c0130mA.i(50);
        c0130mA.a(2000);
        return c0130mA;
    }
}
