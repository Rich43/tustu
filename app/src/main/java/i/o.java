package I;

import G.C0113cs;
import G.InterfaceC0109co;
import G.R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: TunerStudioMS.jar:I/o.class */
public class o implements InterfaceC0109co {

    /* renamed from: a, reason: collision with root package name */
    public static int f1392a = 255;

    /* renamed from: b, reason: collision with root package name */
    public static int f1393b = 65535;

    /* renamed from: e, reason: collision with root package name */
    R f1396e;

    /* renamed from: c, reason: collision with root package name */
    int f1394c = 0;

    /* renamed from: d, reason: collision with root package name */
    int f1395d = f1393b;

    /* renamed from: f, reason: collision with root package name */
    String f1397f = null;

    /* renamed from: g, reason: collision with root package name */
    public List f1398g = new ArrayList();

    public o(R r2) {
        this.f1396e = r2;
    }

    public void a(String str, int i2) throws V.a {
        this.f1397f = str;
        this.f1395d = i2;
        this.f1394c = 0;
        C0113cs.a().a(this.f1396e.c(), str, this);
    }

    @Override // G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) {
        if (this.f1394c <= this.f1395d - 1 && d2 < this.f1394c) {
            b();
        }
        this.f1394c = (int) d2;
    }

    public void a() {
        C0113cs.a().a(this);
    }

    public void a(n nVar) {
        this.f1398g.add(nVar);
    }

    public void b(n nVar) {
        this.f1398g.remove(nVar);
    }

    private void b() {
        Iterator it = this.f1398g.iterator();
        while (it.hasNext()) {
            ((n) it.next()).a(this.f1396e.c());
        }
    }
}
