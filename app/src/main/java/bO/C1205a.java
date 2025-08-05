package bo;

import G.R;
import W.C0172ab;
import bH.C;
import r.C1798a;
import r.C1807j;

/* renamed from: bo.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bo/a.class */
public class C1205a {
    public static void a(R r2) {
        if ((C1798a.a().c(C1798a.f13387bq, C1798a.f13388br) && C1798a.a().c(C1798a.f13389bs, C1798a.f13390bt)) || r2.U()) {
            C.d("Not Loading default Tools because they were already loaded from the project ini(s)");
            return;
        }
        String strB = b(r2);
        if (strB != null) {
            new C0172ab().a(r2, strB, false);
        } else {
            C.d("Not Loading default Tools because non are defined for signature: " + r2.i());
        }
    }

    public static String b(R r2) {
        String strI = r2.i();
        String str = null;
        if (strI.startsWith("MS3")) {
            str = C1807j.f13495E;
        } else if (strI.startsWith("MSII Rev") || strI.startsWith("MS2Extra Rev 1") || (strI.startsWith("Monsterfirmware") && !strI.contains("pw"))) {
            str = C1807j.f13493C;
        } else if (strI.startsWith("MS2Extra")) {
            str = C1807j.f13494D;
        } else if (strI.startsWith("MS/Extra") || strI.startsWith("MS1/Extra") || strI.startsWith("MSnS-extra")) {
            str = C1807j.f13492B;
        } else if (strI.startsWith("20")) {
            str = C1807j.f13491A;
        }
        return str;
    }
}
