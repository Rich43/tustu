package I;

import G.C0048ah;
import G.C0113cs;
import java.util.ArrayList;
import org.icepdf.core.util.PdfOps;

/* loaded from: TunerStudioMS.jar:I/a.class */
public class a {

    /* renamed from: a, reason: collision with root package name */
    public static String f1338a = "yAxisForce";

    /* renamed from: b, reason: collision with root package name */
    public static String f1339b = "xAxisForce";

    /* renamed from: c, reason: collision with root package name */
    public static String f1340c = "zAxisForce";

    /* renamed from: d, reason: collision with root package name */
    public static String f1341d = "accelForce";

    /* renamed from: e, reason: collision with root package name */
    public static String f1342e = "lateralForce";

    /* renamed from: f, reason: collision with root package name */
    public static String f1343f = "lateralAngle";

    /* renamed from: g, reason: collision with root package name */
    public static String f1344g = "turnForce";

    /* renamed from: h, reason: collision with root package name */
    public static String f1345h = "enhancedHeading";

    public static void a() {
        C0113cs.a().d(f1338a);
        C0113cs.a().d(f1338a);
        C0113cs.a().d(f1338a);
        C0113cs.a().d(f1341d);
        C0113cs.a().d(f1344g);
        C0113cs.a().d(f1342e);
        C0113cs.a().d(f1343f);
        C0113cs.a().d(f1345h);
    }

    public static void a(ArrayList arrayList) {
        C0048ah c0048ah = new C0048ah();
        c0048ah.v(f1339b + "Gauge");
        c0048ah.a(f1339b);
        c0048ah.a(-2.0d);
        c0048ah.b(2.0d);
        c0048ah.d(1.0d);
        c0048ah.e(1.5d);
        c0048ah.c(-1.0d);
        c0048ah.h(-1.5d);
        c0048ah.c("X Force");
        c0048ah.b(PdfOps.g_TOKEN);
        c0048ah.g(1.0d);
        c0048ah.f(2.0d);
        c0048ah.d("Accelerometer");
        arrayList.add(c0048ah);
        C0048ah c0048ah2 = new C0048ah();
        c0048ah2.v(f1338a + "Gauge");
        c0048ah2.a(f1338a);
        c0048ah2.a(-2.0d);
        c0048ah2.b(2.0d);
        c0048ah2.d(1.0d);
        c0048ah2.e(1.5d);
        c0048ah2.c(-1.0d);
        c0048ah2.h(-1.5d);
        c0048ah2.c("Y Force");
        c0048ah2.b(PdfOps.g_TOKEN);
        c0048ah2.g(1.0d);
        c0048ah2.f(2.0d);
        c0048ah2.d("Accelerometer");
        arrayList.add(c0048ah2);
        C0048ah c0048ah3 = new C0048ah();
        c0048ah3.v(f1340c + "Gauge");
        c0048ah3.a(f1340c);
        c0048ah3.a(-2.0d);
        c0048ah3.b(2.0d);
        c0048ah3.d(1.0d);
        c0048ah3.e(1.5d);
        c0048ah3.c(-1.0d);
        c0048ah3.h(-1.5d);
        c0048ah3.c("Z Force");
        c0048ah3.b(PdfOps.g_TOKEN);
        c0048ah3.g(1.0d);
        c0048ah3.f(2.0d);
        c0048ah3.d("Accelerometer");
        arrayList.add(c0048ah3);
        C0048ah c0048ah4 = new C0048ah();
        c0048ah4.v(f1341d + "Gauge");
        c0048ah4.a(f1341d);
        c0048ah4.a(-2.0d);
        c0048ah4.b(2.0d);
        c0048ah4.d(1.0d);
        c0048ah4.e(1.5d);
        c0048ah4.c(-1.0d);
        c0048ah4.h(-1.5d);
        c0048ah4.c("Accel Force");
        c0048ah4.b(PdfOps.g_TOKEN);
        c0048ah4.g(1.0d);
        c0048ah4.f(2.0d);
        c0048ah4.d("Accelerometer");
        arrayList.add(c0048ah4);
        C0048ah c0048ah5 = new C0048ah();
        c0048ah5.v(f1344g + "Gauge");
        c0048ah5.a(f1344g);
        c0048ah5.a(-2.0d);
        c0048ah5.b(2.0d);
        c0048ah5.d(1.5d);
        c0048ah5.e(2.0d);
        c0048ah5.c(-1.5d);
        c0048ah5.h(-2.0d);
        c0048ah5.c("Turn Force");
        c0048ah5.b(PdfOps.g_TOKEN);
        c0048ah5.g(1.0d);
        c0048ah5.f(2.0d);
        c0048ah5.d("Accelerometer");
        arrayList.add(c0048ah5);
        C0048ah c0048ah6 = new C0048ah();
        c0048ah6.v(f1342e + "Gauge");
        c0048ah6.a(f1342e);
        c0048ah6.a(-2.0d);
        c0048ah6.b(2.0d);
        c0048ah6.d(1.75d);
        c0048ah6.e(2.0d);
        c0048ah6.c(-1.75d);
        c0048ah6.h(-2.0d);
        c0048ah6.d("Accelerometer");
        c0048ah6.c("Lateral Force");
        c0048ah6.b(PdfOps.g_TOKEN);
        c0048ah6.g(1.0d);
        c0048ah6.f(2.0d);
        arrayList.add(c0048ah6);
        C0048ah c0048ah7 = new C0048ah();
        c0048ah7.v(f1343f + "Gauge");
        c0048ah7.a(f1343f);
        c0048ah7.a(0.0d);
        c0048ah7.b(360.0d);
        c0048ah7.d(1000.0d);
        c0048ah7.e(1000.0d);
        c0048ah7.c(-1.75d);
        c0048ah7.h(-2.0d);
        c0048ah7.c("Lateral Angle");
        c0048ah7.b("°");
        c0048ah7.g(1.0d);
        c0048ah7.f(2.0d);
        c0048ah7.d("Accelerometer");
        arrayList.add(c0048ah7);
        C0048ah c0048ah8 = new C0048ah();
        c0048ah8.v(f1345h + "Gauge");
        c0048ah8.a(f1345h);
        c0048ah8.a(0.0d);
        c0048ah8.b(360.0d);
        c0048ah8.d(1000.0d);
        c0048ah8.e(1000.0d);
        c0048ah8.c(-1.75d);
        c0048ah8.h(-2.0d);
        c0048ah8.c("Heading Enhanced");
        c0048ah8.b("°");
        c0048ah8.g(1.0d);
        c0048ah8.f(2.0d);
        c0048ah8.d("Accelerometer");
        arrayList.add(c0048ah8);
    }
}
