package I;

import G.C0048ah;
import G.C0113cs;
import java.util.ArrayList;

/* loaded from: TunerStudioMS.jar:I/j.class */
public class j {

    /* renamed from: a, reason: collision with root package name */
    public static String f1381a = "magneticCompass";

    public static void a() {
        C0113cs.a().d(f1381a);
    }

    public static void a(ArrayList arrayList) {
        C0048ah c0048ah = new C0048ah();
        c0048ah.v(f1381a + "Gauge");
        c0048ah.a(f1381a);
        c0048ah.a(0.0d);
        c0048ah.b(360.0d);
        c0048ah.d(1000.0d);
        c0048ah.e(1000.0d);
        c0048ah.c(-1.75d);
        c0048ah.h(-2.0d);
        c0048ah.c("Direction");
        c0048ah.b("°");
        c0048ah.g(0.0d);
        c0048ah.f(1.0d);
        arrayList.add(c0048ah);
    }
}
