package au;

import G.C0096cb;
import G.C0097cc;
import G.C0098cd;
import V.g;
import aO.j;
import bH.C;
import java.awt.Event;
import java.awt.font.NumericShaper;
import java.util.ArrayList;

/* renamed from: au.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:au/b.class */
public class C0860b implements j {

    /* renamed from: a, reason: collision with root package name */
    public static String f6277a = "NextAddress";

    /* renamed from: b, reason: collision with root package name */
    public static String f6278b = "LastAddress";

    /* renamed from: c, reason: collision with root package name */
    public static String f6279c = "Units";

    @Override // aO.j
    public ArrayList a(String str) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(a(a(), str));
        arrayList.add(a(b(), str));
        arrayList.add(a(d(), str));
        arrayList.add(a(c(), str));
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
        } catch (g e2) {
            e2.printStackTrace();
            C.c(e2.getMessage());
        }
        c0096cb.b("( status3 & 0x02 ) == 0x02");
        c0096cb.c("Composite Logger");
        c0096cb.d("r\\x00\\xf2\\x00\\x00\\x04\\x00");
        c0096cb.a(10000);
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
        } catch (g e2) {
            e2.printStackTrace();
            C.c(e2.getMessage());
        }
        c0096cb.b("( status3 & 0x02 ) == 0x02");
        c0096cb.c("Sync Error Logger");
        c0096cb.d("r\\x00\\xf3\\x00\\x00\\x04\\x00");
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
        c0097cc6.b(f6278b);
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
        } catch (g e2) {
            e2.printStackTrace();
            C.c(e2.getMessage());
        }
        c0096cb.c("Trigger Logger");
        c0096cb.d("r\\x00\\xf1\\x00\\x00\\x04\\x00");
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
        } catch (g e2) {
            e2.printStackTrace();
            C.c(e2.getMessage());
        }
        c0096cb.c("Tooth Logger");
        c0096cb.d("r\\x00\\xf0\\x00\\x00\\x04\\x00");
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
}
