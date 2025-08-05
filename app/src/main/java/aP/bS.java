package aP;

import s.C1818g;

/* loaded from: TunerStudioMS.jar:aP/bS.class */
public class bS implements G.aG, G.dm {

    /* renamed from: a, reason: collision with root package name */
    private static bS f3005a = null;

    private bS() {
    }

    public static bS e() {
        if (f3005a == null) {
            f3005a = new bS();
        }
        return f3005a;
    }

    @Override // G.dm
    public void a() {
    }

    @Override // G.dm
    public void b() {
    }

    @Override // G.dm
    public void c() {
        C0338f.a().e(C1818g.b("Reading Controller Settings"));
    }

    @Override // G.dm
    public void a(String str, double d2) {
        if (str != null) {
            C0338f.a().f(str);
        }
        if (d2 >= 0.0d) {
            C0338f.a().a(d2);
        }
    }

    @Override // G.dm
    public void d() {
        C0338f.a().b(true);
    }

    @Override // G.aG
    public boolean a(String str, G.bS bSVar) {
        return true;
    }

    @Override // G.aG
    public void a(String str) {
        C0338f.a().l();
    }
}
