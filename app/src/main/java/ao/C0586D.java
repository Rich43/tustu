package ao;

import W.C0184j;
import W.C0188n;

/* renamed from: ao.D, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/D.class */
public class C0586D {
    public static C0184j a(C0188n c0188n) {
        C0184j c0184jA = c0188n.a(h.g.a().a("Field.TP"));
        if (c0184jA != null) {
            return c0184jA;
        }
        C0184j c0184jA2 = c0188n.a(h.g.a().a("TPS"));
        if (c0184jA2 != null) {
            return c0184jA2;
        }
        C0184j c0184jA3 = c0188n.a("TPS");
        if (c0184jA3 != null) {
            return c0184jA3;
        }
        return null;
    }

    public static C0184j b(C0188n c0188n) {
        C0184j c0184jA = c0188n.a(h.g.a().a("VSS2"));
        if (c0184jA != null && c0184jA.g() > c0184jA.h()) {
            bH.C.d("findMphColumn, Using VSS2");
            return c0184jA;
        }
        C0184j c0184jA2 = c0188n.a(h.g.a().a("SmoothMPH"));
        if (c0184jA2 != null) {
            bH.C.d("findMphColumn, Using SmoothMPH");
            return c0184jA2;
        }
        C0184j c0184jA3 = c0188n.a("MPH");
        if (c0184jA3 != null) {
            bH.C.d("findMphColumn, Using MPH");
            return c0184jA3;
        }
        C0184j c0184jA4 = c0188n.a(h.g.a().a("Field.speedMPH"));
        if (c0184jA4 == null) {
            return null;
        }
        bH.C.d("findMphColumn, Using " + c0184jA4.a());
        return c0184jA4;
    }
}
