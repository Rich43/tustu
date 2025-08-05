package K;

import G.C0116cv;
import G.C0134q;
import G.R;
import G.S;
import G.T;
import bH.C;
import java.io.IOException;

/* loaded from: TunerStudioMS.jar:K/h.class */
public class h implements S, S.d {

    /* renamed from: d, reason: collision with root package name */
    private C0116cv f1511d;

    /* renamed from: a, reason: collision with root package name */
    S.h f1512a = new S.h();

    /* renamed from: b, reason: collision with root package name */
    R f1513b;

    /* renamed from: c, reason: collision with root package name */
    int f1514c;

    public h(R r2, int i2, String str) {
        this.f1513b = r2;
        this.f1514c = i2;
        this.f1511d = new C0116cv(r2);
        this.f1512a.g("TriggeredPage" + i2 + " Refresh: " + str);
        this.f1512a.e(str);
        this.f1512a.f("!(" + str + ")");
        this.f1512a.a(this);
        if (T.a().c(r2.c()) == null) {
            T.a().a(this);
            return;
        }
        try {
            S.e.a().a(r2.c(), this.f1512a);
        } catch (C0134q e2) {
            C.a("EcuConfiguration '" + r2.c() + "' not loaded, cannot add TriggeredPageRead: " + this.f1512a.a());
        }
    }

    @Override // S.d
    public void a() throws IOException {
        this.f1511d.a(this.f1514c);
    }

    @Override // S.d
    public void b() {
    }

    @Override // G.S
    public void a(R r2) {
    }

    @Override // G.S
    public void b(R r2) {
    }

    @Override // G.S
    public void c(R r2) {
        if (r2.c().equals(this.f1513b.c())) {
            try {
                S.e.a().a(this.f1513b.c(), this.f1512a);
            } catch (C0134q e2) {
                C.a("EcuConfiguration '" + this.f1513b.c() + "' not loaded, cannot add TriggeredPageRead: " + this.f1512a.a());
            }
        }
    }
}
