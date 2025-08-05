package aI;

import G.C0113cs;
import G.InterfaceC0109co;
import G.R;
import G.S;
import G.T;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:aI/b.class */
public class b implements S, InterfaceC0109co {

    /* renamed from: c, reason: collision with root package name */
    private static HashMap f2424c = new HashMap();

    /* renamed from: d, reason: collision with root package name */
    private R f2425d;

    /* renamed from: a, reason: collision with root package name */
    int f2426a = 1;

    /* renamed from: b, reason: collision with root package name */
    int f2427b = 0;

    private b(R r2) {
        this.f2425d = null;
        this.f2425d = r2;
        b();
    }

    private void b() {
        try {
            if (this.f2425d.g("rpm") != null) {
                C0113cs.a().a(this.f2425d.c(), "rpm", this);
            }
            if (this.f2425d.g("engine") != null) {
                C0113cs.a().a(this.f2425d.c(), "engine", this);
            }
            T.a().a(this);
        } catch (V.a e2) {
            Logger.getLogger(b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    public static b d(R r2) {
        b bVar = (b) f2424c.get(r2.c());
        if (bVar == null) {
            bVar = new b(r2);
            f2424c.put(r2.c(), bVar);
        }
        return bVar;
    }

    private void c() {
        C0113cs.a().a(this);
        f2424c.remove(this.f2425d.c());
    }

    @Override // G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) {
        if (str.equals("rpm")) {
            this.f2427b = (int) d2;
        }
        if (str.equals("engine")) {
            this.f2426a = (int) d2;
        }
    }

    @Override // G.S
    public void a(R r2) {
    }

    @Override // G.S
    public void b(R r2) {
        if (r2.equals(this.f2425d)) {
            c();
        }
    }

    @Override // G.S
    public void c(R r2) {
    }

    public boolean a() {
        return this.f2427b > 0;
    }
}
