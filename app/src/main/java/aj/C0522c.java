package aj;

import G.C0096cb;
import G.C0097cc;
import G.C0098cd;
import G.R;
import bH.C;
import java.awt.Event;
import java.awt.font.NumericShaper;
import java.util.ArrayList;

/* renamed from: aj.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aj/c.class */
public class C0522c {

    /* renamed from: a, reason: collision with root package name */
    public static String f4524a = "NextAddress";

    /* renamed from: b, reason: collision with root package name */
    public static String f4525b = "LastAddress";

    /* renamed from: c, reason: collision with root package name */
    public static String f4526c = "Units";

    public ArrayList a(R r2) {
        ArrayList arrayList = new ArrayList();
        String strI = r2.i();
        if (r2.z()) {
            if (r2.m(C0096cb.f1069e)) {
                arrayList.add(a());
            }
            if (r2.m(C0096cb.f1070f)) {
                C0096cb c0096cbA = a();
                a(c0096cbA);
                arrayList.add(c0096cbA);
            }
            if (r2.m(C0096cb.f1071g)) {
                arrayList.add(b());
            }
            if (r2.m(C0096cb.f1072h)) {
                C0096cb c0096cbB = b();
                a(c0096cbB);
                arrayList.add(c0096cbB);
            }
            if (r2.m(C0096cb.f1073i)) {
                arrayList.add(d());
            }
            if (r2.m(C0096cb.f1074j)) {
                C0096cb c0096cbD = d();
                a(c0096cbD);
                arrayList.add(c0096cbD);
            }
            if (r2.m(C0096cb.f1075k)) {
                arrayList.add(c());
            }
            if (r2.m(C0096cb.f1076l)) {
                C0096cb c0096cbC = c();
                a(c0096cbC);
                arrayList.add(c0096cbC);
            }
        } else {
            if (strI.startsWith("MS2Extra Rel 2.1") || strI.startsWith("MS2Extra Ser") || strI.startsWith("MS2Extra serial") || strI.startsWith("MS3") || strI.startsWith("MS2Extra 3") || strI.startsWith("MS2Extra comms") || strI.startsWith("MS2Extra Rel 3")) {
                arrayList.add(a(a(), strI));
                arrayList.add(a(b(), strI));
            }
            if (strI.startsWith("MS2Extra Rel 2") || strI.startsWith("MS2Extra Ser") || strI.startsWith("MS3") || strI.startsWith("MS2Extra 3") || strI.startsWith("MS2Extra comms") || strI.startsWith("MS2Extra Rel 3")) {
                arrayList.add(a(d(), strI));
                arrayList.add(a(c(), strI));
            }
            if (strI.startsWith("MSnS-extra") || strI.startsWith("MS1/Extra") || strI.startsWith("MS/Extra")) {
                arrayList.add(e());
                arrayList.add(f());
            }
        }
        return arrayList;
    }

    private C0096cb a(C0096cb c0096cb, String str) {
        if (str.startsWith("MS3")) {
            a(c0096cb);
        }
        return c0096cb;
    }

    private C0096cb a(C0096cb c0096cb) {
        int iB = c0096cb.b().b("ToothTime");
        int iB2 = iB == -1 ? c0096cb.b().b("TriggerTime") : iB;
        if (iB2 == -1) {
            C.b("Did not find Time Column, not adjusting scalar");
            return c0096cb;
        }
        c0096cb.b().a(iB2).a(0.001d);
        return c0096cb;
    }

    public C0096cb a() {
        C0096cb c0096cb = new C0096cb();
        try {
            c0096cb.a(C0096cb.f1065a);
        } catch (V.g e2) {
            e2.printStackTrace();
            C.c(e2.getMessage());
        }
        c0096cb.b("( status3 & 0x02 ) == 0x02");
        c0096cb.c("Composite Logger");
        c0096cb.d("r\\$tsCanId\\xf2\\x00\\x00\\x04\\x00");
        c0096cb.a(10000);
        C0098cd c0098cd = new C0098cd();
        c0098cd.d(3);
        c0098cd.f(1);
        C0097cc c0097cc = new C0097cc();
        c0097cc.b("PriLevel");
        c0097cc.d("PriLevel");
        c0097cc.a(1, 22);
        c0097cc.c("Flag");
        c0098cd.a(c0097cc);
        C0097cc c0097cc2 = new C0097cc();
        c0097cc2.b("SecLevel");
        c0097cc2.d("SecLevel");
        c0097cc2.a(1, 23);
        c0097cc2.c("Flag");
        c0098cd.a(c0097cc2);
        C0097cc c0097cc3 = new C0097cc();
        c0097cc3.b("Trigger");
        c0097cc3.d("Trigger");
        c0097cc3.a(1, 21);
        c0097cc3.c("Flag");
        c0098cd.a(c0097cc3);
        C0097cc c0097cc4 = new C0097cc();
        c0097cc4.b("Sync");
        c0097cc4.d("Sync");
        c0097cc4.a(1, 20);
        c0097cc4.c("Flag");
        c0098cd.a(c0097cc4);
        C0097cc c0097cc5 = new C0097cc();
        c0097cc5.b("ToothTime");
        c0097cc5.d("ToothTime");
        c0097cc5.a(6.6E-4d);
        c0097cc5.a(20, 0);
        c0097cc5.b(NumericShaper.ALL_RANGES);
        c0097cc5.c("ms");
        c0098cd.a(c0097cc5);
        c0096cb.a(c0098cd);
        return c0096cb;
    }

    public C0096cb b() {
        C0096cb c0096cb = new C0096cb();
        try {
            c0096cb.a(C0096cb.f1065a);
        } catch (V.g e2) {
            e2.printStackTrace();
            C.c(e2.getMessage());
        }
        c0096cb.b("( status3 & 0x02 ) == 0x02");
        c0096cb.c("Sync Error Logger");
        c0096cb.d("r\\$tsCanId\\xf3\\x00\\x00\\x04\\x00");
        c0096cb.a(1000000000);
        C0098cd c0098cd = new C0098cd();
        c0098cd.d(3);
        c0098cd.f(1);
        C0097cc c0097cc = new C0097cc();
        c0097cc.b("PriLevel");
        c0097cc.a(1, 22);
        c0097cc.c("Flag");
        c0098cd.a(c0097cc);
        C0097cc c0097cc2 = new C0097cc();
        c0097cc2.b("SecLevel");
        c0097cc2.a(1, 23);
        c0097cc2.c("Flag");
        c0098cd.a(c0097cc2);
        C0097cc c0097cc3 = new C0097cc();
        c0097cc3.b("Trigger");
        c0097cc3.a(1, 21);
        c0097cc3.c("Flag");
        c0098cd.a(c0097cc3);
        C0097cc c0097cc4 = new C0097cc();
        c0097cc4.b("Sync");
        c0097cc4.a(1, 20);
        c0097cc4.c("Flag");
        c0098cd.a(c0097cc4);
        C0097cc c0097cc5 = new C0097cc();
        c0097cc5.b("ToothTime");
        c0097cc5.a(6.6E-4d);
        c0097cc5.a(20, 0);
        c0097cc5.c("ms");
        c0098cd.a(c0097cc5);
        C0097cc c0097cc6 = new C0097cc();
        c0097cc6.b(f4525b);
        c0097cc6.a(1.0d);
        c0097cc6.a(Event.SCROLL_LOCK);
        c0097cc6.a(16, 0);
        c0098cd.b(c0097cc6);
        c0096cb.a(c0098cd);
        return c0096cb;
    }

    public C0096cb c() {
        C0096cb c0096cb = new C0096cb();
        try {
            c0096cb.a(C0096cb.f1067c);
        } catch (V.g e2) {
            e2.printStackTrace();
            C.c(e2.getMessage());
        }
        c0096cb.c("Trigger Logger");
        c0096cb.d("r\\$tsCanId\\xf1\\x00\\x00\\x04\\x00");
        C0098cd c0098cd = new C0098cd();
        c0098cd.d(3);
        c0098cd.f(1);
        new C0097cc();
        C0097cc c0097cc = new C0097cc();
        c0097cc.b("TriggerTime");
        c0097cc.a(6.6E-4d);
        c0097cc.a(20, 0);
        c0097cc.c("ms");
        c0098cd.a(c0097cc);
        c0096cb.a(c0098cd);
        return c0096cb;
    }

    public C0096cb d() {
        C0096cb c0096cb = new C0096cb();
        try {
            c0096cb.a(C0096cb.f1066b);
        } catch (V.g e2) {
            e2.printStackTrace();
            C.c(e2.getMessage());
        }
        c0096cb.c("Tooth Logger");
        c0096cb.d("r\\$tsCanId\\xf0\\x00\\x00\\x04\\x00");
        C0098cd c0098cd = new C0098cd();
        c0098cd.d(3);
        c0098cd.f(1);
        new C0097cc();
        C0097cc c0097cc = new C0097cc();
        c0097cc.b("ToothTime");
        c0097cc.a(6.6E-4d);
        c0097cc.a(20, 0);
        c0097cc.c("ms");
        c0098cd.a(c0097cc);
        c0096cb.a(c0098cd);
        return c0096cb;
    }

    public C0096cb e() {
        C0096cb c0096cb = new C0096cb();
        try {
            c0096cb.a(C0096cb.f1066b);
        } catch (V.g e2) {
            e2.printStackTrace();
            C.c(e2.getMessage());
        }
        c0096cb.c("Tooth Logger");
        c0096cb.b(9);
        c0096cb.d("V");
        C0098cd c0098cd = new C0098cd();
        c0098cd.d(2);
        C0097cc c0097cc = new C0097cc();
        c0097cc.b("ToothTime");
        c0097cc.a(1.0d);
        c0097cc.a(16, 0);
        c0097cc.c("us");
        c0098cd.a(c0097cc);
        c0096cb.a(c0098cd);
        c0098cd.f(2);
        C0097cc c0097cc2 = new C0097cc();
        c0097cc2.b(f4524a);
        c0097cc2.a(1.0d);
        c0097cc2.a(187);
        c0097cc2.a(8, 0);
        c0098cd.b(c0097cc2);
        C0097cc c0097cc3 = new C0097cc();
        c0097cc3.b(f4526c);
        c0097cc3.a(1.0d);
        c0097cc3.a(188);
        c0097cc3.a(8, 0);
        c0098cd.b(c0097cc3);
        return c0096cb;
    }

    public C0096cb f() {
        C0096cb c0096cb = new C0096cb();
        try {
            c0096cb.a(C0096cb.f1066b);
        } catch (V.g e2) {
            e2.printStackTrace();
            C.c(e2.getMessage());
        }
        c0096cb.c("Trigger Logger");
        c0096cb.b(10);
        c0096cb.d("V");
        C0098cd c0098cd = new C0098cd();
        c0098cd.d(2);
        c0098cd.f(2);
        new C0097cc();
        C0097cc c0097cc = new C0097cc();
        c0097cc.b("TriggerTime");
        c0097cc.a(1.0d);
        c0097cc.a(16, 0);
        c0097cc.c("us");
        c0098cd.a(c0097cc);
        c0096cb.a(c0098cd);
        C0097cc c0097cc2 = new C0097cc();
        c0097cc2.b(f4524a);
        c0097cc2.a(1.0d);
        c0097cc2.a(187);
        c0097cc2.a(8, 0);
        c0098cd.b(c0097cc2);
        C0097cc c0097cc3 = new C0097cc();
        c0097cc3.b(f4526c);
        c0097cc3.a(1.0d);
        c0097cc3.a(188);
        c0097cc3.a(8, 0);
        c0098cd.b(c0097cc3);
        return c0096cb;
    }
}
