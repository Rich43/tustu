package I;

import G.C0051ak;
import G.C0094c;
import G.C0113cs;
import G.C0128k;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: TunerStudioMS.jar:I/d.class */
public class d {

    /* renamed from: b, reason: collision with root package name */
    private static d f1354b = null;

    /* renamed from: a, reason: collision with root package name */
    ArrayList f1355a = new ArrayList();

    private d() {
    }

    public static d a() {
        if (f1354b == null) {
            f1354b = new d();
        }
        return f1354b;
    }

    public void a(C0051ak c0051ak) {
        if (a(c0051ak.aJ())) {
            return;
        }
        e().add(c0051ak);
    }

    public boolean a(String str) {
        for (int i2 = 0; i2 < e().size(); i2++) {
            if (((C0051ak) this.f1355a.get(i2)).aJ().equals(str)) {
                return true;
            }
        }
        return false;
    }

    public void b(String str) {
        for (int i2 = 0; i2 < e().size(); i2++) {
            if (((C0051ak) this.f1355a.get(i2)).aJ().equals(str)) {
                this.f1355a.remove(i2);
            }
        }
    }

    public Iterator b() {
        return e().iterator();
    }

    private ArrayList e() {
        if (this.f1355a.isEmpty()) {
            this.f1355a = f();
        }
        return this.f1355a;
    }

    public Iterator c() {
        return e().iterator();
    }

    public void d() {
        this.f1355a.clear();
        f();
    }

    private ArrayList f() {
        C0051ak c0051ak = new C0051ak();
        c0051ak.v("dataLoggingActive");
        c0051ak.a(new C0094c("Data Logging"));
        c0051ak.b(new C0094c("Data Logging"));
        c0051ak.a(C0128k.f1268s);
        c0051ak.b(C0128k.f1250a);
        c0051ak.c(C0128k.f1258i);
        c0051ak.d(C0128k.f1252c);
        c0051ak.b("toggleDatalogging?");
        c0051ak.a("dataLoggingActive");
        this.f1355a.add(c0051ak);
        if (C0113cs.a().e("powerCycleRequired")) {
            C0051ak c0051ak2 = new C0051ak();
            c0051ak2.v("powerCycleRequired");
            c0051ak2.a(new C0094c("Power Cycle"));
            c0051ak2.b(new C0094c("Power Cycle"));
            c0051ak2.a(C0128k.f1260k);
            c0051ak2.b(C0128k.f1250a);
            c0051ak2.c(C0128k.f1258i);
            c0051ak2.d(C0128k.f1252c);
            c0051ak2.a("powerCycleRequired");
            this.f1355a.add(c0051ak2);
        }
        if (c.a().c()) {
            h.b(this.f1355a);
        }
        if (c.a().d()) {
            e.a();
            e.a(this.f1355a);
        }
        return this.f1355a;
    }

    public C0051ak c(String str) {
        Iterator itC = c();
        while (itC.hasNext()) {
            C0051ak c0051ak = (C0051ak) itC.next();
            if (c0051ak.aJ().equals(str)) {
                return c0051ak;
            }
        }
        return null;
    }
}
