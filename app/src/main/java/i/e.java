package I;

import G.C0051ak;
import G.C0094c;
import G.C0113cs;
import G.C0128k;
import java.util.ArrayList;

/* loaded from: TunerStudioMS.jar:I/e.class */
public class e {

    /* renamed from: a, reason: collision with root package name */
    public static String f1356a = "controllerConnecting";

    /* renamed from: b, reason: collision with root package name */
    public static String f1357b = "controllerConnected";

    public static void a() {
        C0113cs.a().d(f1356a);
        C0113cs.a().d(f1357b);
    }

    public static void a(ArrayList arrayList) {
        C0051ak c0051ak = new C0051ak();
        c0051ak.v(f1356a + "Indicator");
        c0051ak.a(new C0094c("Connecting"));
        c0051ak.b(new C0094c(""));
        c0051ak.a(C0128k.f1268s);
        c0051ak.b(C0128k.f1251b);
        c0051ak.c(C0128k.f1258i);
        c0051ak.d(C0128k.f1252c);
        c0051ak.a(f1356a);
        arrayList.add(c0051ak);
        C0051ak c0051ak2 = new C0051ak();
        c0051ak2.v(f1357b + "Indicator");
        c0051ak2.a(new C0094c("Connected"));
        c0051ak2.b(new C0094c("Not Connected"));
        c0051ak2.a(C0128k.f1268s);
        c0051ak2.b(C0128k.f1251b);
        c0051ak2.c(C0128k.f1258i);
        c0051ak2.d(C0128k.f1252c);
        c0051ak2.a(f1357b);
        arrayList.add(c0051ak2);
    }
}
