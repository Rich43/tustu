package I;

import G.C0048ah;
import G.C0051ak;
import G.C0094c;
import G.C0113cs;
import G.C0128k;
import bH.C1008p;
import java.util.ArrayList;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;

/* loaded from: TunerStudioMS.jar:I/h.class */
public class h {

    /* renamed from: a, reason: collision with root package name */
    public static String f1363a = "gpsSpeedMPH";

    /* renamed from: b, reason: collision with root package name */
    public static String f1364b = "gpsSpeedKPH";

    /* renamed from: c, reason: collision with root package name */
    public static String f1365c = "gpsSpeed";

    /* renamed from: d, reason: collision with root package name */
    public static String f1366d = "gpsAltitude";

    /* renamed from: e, reason: collision with root package name */
    public static String f1367e = "gpsAltitudeFeet";

    /* renamed from: f, reason: collision with root package name */
    public static String f1368f = "latitude";

    /* renamed from: g, reason: collision with root package name */
    public static String f1369g = "longitude";

    /* renamed from: h, reason: collision with root package name */
    public static String f1370h = "gpsHeading";

    /* renamed from: i, reason: collision with root package name */
    public static String f1371i = "gpsAccuracy";

    /* renamed from: j, reason: collision with root package name */
    public static String f1372j = "gpsPositionX";

    /* renamed from: k, reason: collision with root package name */
    public static String f1373k = "gpsPositionY";

    /* renamed from: l, reason: collision with root package name */
    public static String f1374l = "gpsTime";

    /* renamed from: m, reason: collision with root package name */
    public static String f1375m = "GPS_Update";

    /* renamed from: n, reason: collision with root package name */
    public static String f1376n = "gpsActive";

    /* renamed from: o, reason: collision with root package name */
    public static String f1377o = "gpsUsingExternal";

    /* renamed from: p, reason: collision with root package name */
    public static String f1378p = "gpsUpdateRate";

    /* renamed from: q, reason: collision with root package name */
    public static String f1379q = "gpsSatellites";

    /* renamed from: r, reason: collision with root package name */
    public static String f1380r = "gpsHasFix";

    public static void a() {
        C0113cs.a().d(f1363a);
        C0113cs.a().d(f1364b);
        C0113cs.a().d(f1365c);
        C0113cs.a().d(f1366d);
        C0113cs.a().d(f1367e);
        C0113cs.a().d(f1368f);
        C0113cs.a().d(f1369g);
        C0113cs.a().d(f1370h);
        C0113cs.a().d(f1371i);
        C0113cs.a().d(f1372j);
        C0113cs.a().d(f1373k);
        C0113cs.a().d(f1374l);
        C0113cs.a().d(f1375m);
        C0113cs.a().d(f1376n);
        C0113cs.a().d(f1380r);
        if (C1008p.b()) {
            C0113cs.a().d(f1377o);
        }
    }

    public static void a(ArrayList arrayList) {
        C0048ah c0048ah = new C0048ah();
        c0048ah.v(f1363a + "Gauge");
        c0048ah.a(f1363a);
        c0048ah.a(0.0d);
        c0048ah.b(160.0d);
        c0048ah.d(75.0d);
        c0048ah.e(140.0d);
        c0048ah.c(-1.0d);
        c0048ah.h(-2.0d);
        c0048ah.c("Speed");
        c0048ah.b("MPH");
        c0048ah.g(0.0d);
        c0048ah.f(0.0d);
        c0048ah.d("GPS");
        arrayList.add(c0048ah);
        C0048ah c0048ah2 = new C0048ah();
        c0048ah2.v(f1364b + "Gauge");
        c0048ah2.a(f1364b);
        c0048ah2.a(0.0d);
        c0048ah2.b(240.0d);
        c0048ah2.d(140.0d);
        c0048ah2.e(220.0d);
        c0048ah2.c(-1.0d);
        c0048ah2.h(-2.0d);
        c0048ah2.c("Speed");
        c0048ah2.b("KPH");
        c0048ah2.g(0.0d);
        c0048ah2.f(0.0d);
        c0048ah2.d("GPS");
        arrayList.add(c0048ah2);
        C0048ah c0048ah3 = new C0048ah();
        c0048ah3.v(f1366d + "Gauge");
        c0048ah3.a(f1366d);
        c0048ah3.a(0.0d);
        c0048ah3.b(2000.0d);
        c0048ah3.d(5000.0d);
        c0048ah3.e(20000.0d);
        c0048ah3.c(-100.0d);
        c0048ah3.h(-200.0d);
        c0048ah3.c("Altitude");
        c0048ah3.b(PdfOps.m_TOKEN);
        c0048ah3.g(0.0d);
        c0048ah3.f(0.0d);
        c0048ah3.d("GPS");
        arrayList.add(c0048ah3);
        C0048ah c0048ah4 = new C0048ah();
        c0048ah4.v(f1367e + "Gauge");
        c0048ah4.a(f1367e);
        c0048ah4.a(0.0d);
        c0048ah4.b(2000.0d);
        c0048ah4.d(5000.0d);
        c0048ah4.e(20000.0d);
        c0048ah4.c(-100.0d);
        c0048ah4.h(-200.0d);
        c0048ah4.c("Altitude");
        c0048ah4.b("ft");
        c0048ah4.g(0.0d);
        c0048ah4.f(0.0d);
        c0048ah4.d("GPS");
        arrayList.add(c0048ah4);
        C0048ah c0048ah5 = new C0048ah();
        c0048ah5.v(f1369g + "Gauge");
        c0048ah5.a(f1369g);
        c0048ah5.a(-180.0d);
        c0048ah5.b(180.0d);
        c0048ah5.d(5000.0d);
        c0048ah5.e(20000.0d);
        c0048ah5.c(-200.0d);
        c0048ah5.h(-200.0d);
        c0048ah5.c("Longitude");
        c0048ah5.b("°");
        c0048ah5.g(0.0d);
        c0048ah5.f(7.0d);
        c0048ah5.d("GPS");
        arrayList.add(c0048ah5);
        C0048ah c0048ah6 = new C0048ah();
        c0048ah6.v(f1368f + "Gauge");
        c0048ah6.a(f1368f);
        c0048ah6.a(-90.0d);
        c0048ah6.b(90.0d);
        c0048ah6.d(5000.0d);
        c0048ah6.e(20000.0d);
        c0048ah6.c(-200.0d);
        c0048ah6.h(-200.0d);
        c0048ah6.c("Latitude");
        c0048ah6.b("°");
        c0048ah6.g(0.0d);
        c0048ah6.f(7.0d);
        c0048ah6.d("GPS");
        arrayList.add(c0048ah6);
        C0048ah c0048ah7 = new C0048ah();
        c0048ah7.v(f1370h + "Gauge");
        c0048ah7.a(f1370h);
        c0048ah7.a(0.0d);
        c0048ah7.b(360.0d);
        c0048ah7.d(5000.0d);
        c0048ah7.e(20000.0d);
        c0048ah7.c(-200.0d);
        c0048ah7.h(-200.0d);
        c0048ah7.c("GPS Bearing");
        c0048ah7.b("°");
        c0048ah7.g(0.0d);
        c0048ah7.f(0.0d);
        c0048ah7.d("GPS");
        arrayList.add(c0048ah7);
        C0048ah c0048ah8 = new C0048ah();
        c0048ah8.v(f1371i + "Gauge");
        c0048ah8.a(f1371i);
        c0048ah8.a(0.0d);
        c0048ah8.b(360.0d);
        c0048ah8.d(40.0d);
        c0048ah8.e(100.0d);
        c0048ah8.c(-200.0d);
        c0048ah8.h(-200.0d);
        c0048ah8.c("GPS Accuracy");
        c0048ah8.b(PdfOps.m_TOKEN);
        c0048ah8.g(0.0d);
        c0048ah8.f(0.0d);
        c0048ah8.d("GPS");
        arrayList.add(c0048ah8);
        C0048ah c0048ah9 = new C0048ah();
        c0048ah9.v(f1372j + "Gauge");
        c0048ah9.a(f1372j);
        c0048ah9.a(-10000.0d);
        c0048ah9.b(10000.0d);
        c0048ah9.d(Double.MAX_VALUE);
        c0048ah9.e(Double.MAX_VALUE);
        c0048ah9.c(Double.MIN_VALUE);
        c0048ah9.h(Double.MIN_VALUE);
        c0048ah9.c("GPS Position X");
        c0048ah9.b(PdfOps.m_TOKEN);
        c0048ah9.g(0.0d);
        c0048ah9.f(0.0d);
        c0048ah9.d("GPS");
        arrayList.add(c0048ah9);
        C0048ah c0048ah10 = new C0048ah();
        c0048ah10.v(f1373k + "Gauge");
        c0048ah10.a(f1373k);
        c0048ah10.a(-10000.0d);
        c0048ah10.b(10000.0d);
        c0048ah10.d(Double.MAX_VALUE);
        c0048ah10.e(Double.MAX_VALUE);
        c0048ah10.c(Double.MIN_VALUE);
        c0048ah10.h(Double.MIN_VALUE);
        c0048ah10.c("GPS Position Y");
        c0048ah10.b(PdfOps.m_TOKEN);
        c0048ah10.g(0.0d);
        c0048ah10.f(0.0d);
        c0048ah10.d("GPS");
        arrayList.add(c0048ah10);
        C0048ah c0048ah11 = new C0048ah();
        c0048ah11.v(f1374l + "Gauge");
        c0048ah11.a(f1374l);
        c0048ah11.a(1.3E12d);
        c0048ah11.b(1.4E12d);
        c0048ah11.d(Double.MAX_VALUE);
        c0048ah11.e(Double.MAX_VALUE);
        c0048ah11.c(Double.MIN_VALUE);
        c0048ah11.h(Double.MIN_VALUE);
        c0048ah11.c("GPS Time");
        c0048ah11.b("ms");
        c0048ah11.g(0.0d);
        c0048ah11.f(0.0d);
        c0048ah11.d("GPS");
        arrayList.add(c0048ah11);
        C0048ah c0048ah12 = new C0048ah();
        c0048ah12.v(f1378p + "Gauge");
        c0048ah12.a(f1378p);
        c0048ah12.a(0.0d);
        c0048ah12.b(20.0d);
        c0048ah12.d(Double.MAX_VALUE);
        c0048ah12.e(Double.MAX_VALUE);
        c0048ah12.c(Double.MIN_VALUE);
        c0048ah12.h(Double.MIN_VALUE);
        c0048ah12.c("GPS Update Rate");
        c0048ah12.b("/s");
        c0048ah12.g(0.0d);
        c0048ah12.f(1.0d);
        c0048ah12.d("GPS");
        arrayList.add(c0048ah12);
        C0048ah c0048ah13 = new C0048ah();
        c0048ah13.v(f1379q + "Gauge");
        c0048ah13.a(f1379q);
        c0048ah13.a(0.0d);
        c0048ah13.b(20.0d);
        c0048ah13.d(Double.MAX_VALUE);
        c0048ah13.e(Double.MAX_VALUE);
        c0048ah13.c(Double.MIN_VALUE);
        c0048ah13.h(Double.MIN_VALUE);
        c0048ah13.c("GPS Satellites");
        c0048ah13.b(FXMLLoader.CONTROLLER_METHOD_PREFIX);
        c0048ah13.g(0.0d);
        c0048ah13.f(0.0d);
        c0048ah13.d("GPS");
        arrayList.add(c0048ah13);
    }

    public static void b(ArrayList arrayList) {
        C0051ak c0051ak = new C0051ak();
        c0051ak.v("gpsUpdate");
        c0051ak.a(new C0094c("GPS Update"));
        c0051ak.b(new C0094c(""));
        c0051ak.a(C0128k.f1268s);
        c0051ak.b(C0128k.f1276A);
        c0051ak.c(C0128k.f1258i);
        c0051ak.d(C0128k.f1252c);
        c0051ak.a(f1375m);
        arrayList.add(c0051ak);
        C0051ak c0051ak2 = new C0051ak();
        c0051ak2.v("gpsActive");
        c0051ak2.a(new C0094c("GPS Active"));
        c0051ak2.b(new C0094c("GPS Not Active"));
        c0051ak2.a(C0128k.f1268s);
        c0051ak2.b(C0128k.f1250a);
        c0051ak2.c(C0128k.f1258i);
        c0051ak2.d(C0128k.f1252c);
        c0051ak2.a(f1376n);
        arrayList.add(c0051ak2);
        C0051ak c0051ak3 = new C0051ak();
        c0051ak3.v("gpsHasFix");
        c0051ak3.a(new C0094c("GPS Fix"));
        c0051ak3.b(new C0094c("GPS Fix"));
        c0051ak3.a(C0128k.f1268s);
        c0051ak3.b(C0128k.f1250a);
        c0051ak3.c(C0128k.f1258i);
        c0051ak3.d(C0128k.f1252c);
        c0051ak3.a(f1380r);
        arrayList.add(c0051ak3);
        if (C1008p.b()) {
            C0051ak c0051ak4 = new C0051ak();
            c0051ak4.v("gpsuseingExternal");
            c0051ak4.a(new C0094c("External GPS"));
            c0051ak4.b(new C0094c("Internal GPS"));
            c0051ak4.a(C0128k.f1268s);
            c0051ak4.b(C0128k.f1250a);
            c0051ak4.c(C0128k.f1258i);
            c0051ak4.d(C0128k.f1252c);
            c0051ak4.a(f1377o);
            arrayList.add(c0051ak4);
        }
    }
}
