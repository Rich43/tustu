package I;

import G.C0048ah;
import bH.C1008p;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.fxml.FXMLLoader;

/* loaded from: TunerStudioMS.jar:I/c.class */
public class c {

    /* renamed from: b, reason: collision with root package name */
    private static c f1347b = null;

    /* renamed from: a, reason: collision with root package name */
    boolean f1348a = false;

    /* renamed from: c, reason: collision with root package name */
    private boolean f1349c;

    /* renamed from: d, reason: collision with root package name */
    private boolean f1350d;

    /* renamed from: e, reason: collision with root package name */
    private boolean f1351e;

    /* renamed from: f, reason: collision with root package name */
    private boolean f1352f;

    /* renamed from: g, reason: collision with root package name */
    private ArrayList f1353g;

    private c() {
        this.f1349c = C1008p.b() || this.f1348a;
        this.f1350d = C1008p.b() || this.f1348a;
        this.f1351e = C1008p.b() || this.f1348a;
        this.f1352f = true;
        this.f1353g = new ArrayList();
    }

    public static c a() {
        if (f1347b == null) {
            f1347b = new c();
        }
        return f1347b;
    }

    public Iterator b() {
        if (this.f1353g.isEmpty()) {
            this.f1353g = e();
        }
        return this.f1353g.iterator();
    }

    private ArrayList e() {
        C0048ah c0048ah = new C0048ah();
        c0048ah.v(f.f1358a + "Gauge");
        c0048ah.a(f.f1358a);
        c0048ah.a(0.0d);
        c0048ah.b(100.0d);
        c0048ah.d(75.0d);
        c0048ah.e(90.0d);
        c0048ah.c(10.0d);
        c0048ah.h(5.0d);
        c0048ah.c("Runtime Data Rate");
        c0048ah.b("/sec");
        c0048ah.g(1.0d);
        c0048ah.f(1.0d);
        this.f1353g.add(c0048ah);
        C0048ah c0048ah2 = new C0048ah();
        c0048ah2.v("dataLogRecordCountGauge");
        c0048ah2.a("dataLogRecordCount");
        c0048ah2.a(0.0d);
        c0048ah2.b(50000.0d);
        c0048ah2.d(200000.0d);
        c0048ah2.e(250000.0d);
        c0048ah2.c(0.0d);
        c0048ah2.h(0.0d);
        c0048ah2.c("Records Logged");
        c0048ah2.b("Records");
        c0048ah2.g(0.0d);
        c0048ah2.f(0.0d);
        this.f1353g.add(c0048ah2);
        C0048ah c0048ah3 = new C0048ah();
        c0048ah3.v("interrogationProgressGauge");
        c0048ah3.a("interrogationProgress");
        c0048ah3.a(0.0d);
        c0048ah3.b(100.0d);
        c0048ah3.d(200000.0d);
        c0048ah3.e(250000.0d);
        c0048ah3.c(0.0d);
        c0048ah3.h(0.0d);
        c0048ah3.c("Interrogation Progress");
        c0048ah3.b(FXMLLoader.RESOURCE_KEY_PREFIX);
        c0048ah3.g(0.0d);
        c0048ah3.f(0.0d);
        this.f1353g.add(c0048ah3);
        C0048ah c0048ah4 = new C0048ah();
        c0048ah4.v("dataLogTimeGauge");
        c0048ah4.a("dataLogTime");
        c0048ah4.a(0.0d);
        c0048ah4.b(600.0d);
        c0048ah4.d(200000.0d);
        c0048ah4.e(250000.0d);
        c0048ah4.c(0.0d);
        c0048ah4.h(0.0d);
        c0048ah4.c("Log Time");
        c0048ah4.b("s.");
        c0048ah4.g(2.0d);
        c0048ah4.f(2.0d);
        this.f1353g.add(c0048ah4);
        if (this.f1349c) {
            a.a();
            a.a(this.f1353g);
        }
        if (this.f1351e) {
            h.a();
            h.a(this.f1353g);
        }
        if (this.f1350d) {
            j.a();
            j.a(this.f1353g);
        }
        return this.f1353g;
    }

    public C0048ah a(String str) {
        Iterator itB = b();
        while (itB.hasNext()) {
            C0048ah c0048ah = (C0048ah) itB.next();
            if (c0048ah.aJ().equals(str)) {
                return c0048ah;
            }
        }
        return null;
    }

    public boolean c() {
        return this.f1351e;
    }

    public void a(boolean z2) {
        this.f1351e = z2;
    }

    public boolean d() {
        return this.f1352f;
    }
}
