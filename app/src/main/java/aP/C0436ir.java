package aP;

import G.InterfaceC0124g;
import com.efiAnalytics.apps.ts.dashboard.C1425x;
import java.util.logging.Level;
import java.util.logging.Logger;
import s.C1818g;

/* renamed from: aP.ir, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/ir.class */
public class C0436ir implements G.aG, InterfaceC0124g {

    /* renamed from: a, reason: collision with root package name */
    G.R f3746a;

    /* renamed from: c, reason: collision with root package name */
    C1425x f3748c;

    /* renamed from: d, reason: collision with root package name */
    int f3749d;

    /* renamed from: b, reason: collision with root package name */
    String f3747b = C1818g.b("Sending Tune to Controller");

    /* renamed from: e, reason: collision with root package name */
    int f3750e = 0;

    public C0436ir(G.R r2, C1425x c1425x, int i2) {
        this.f3746a = null;
        this.f3748c = null;
        this.f3749d = 0;
        this.f3746a = r2;
        this.f3748c = c1425x;
        this.f3749d = i2;
    }

    public void a() {
        if (this.f3746a.R()) {
            this.f3746a.C().a((G.aG) this);
            this.f3746a.C().a((InterfaceC0124g) this);
            this.f3748c.k(this.f3747b);
            C0338f.a().e(this.f3747b);
            if (this.f3746a.C() instanceof bQ.l) {
                try {
                    ((bQ.l) this.f3746a.C()).m();
                } catch (V.b e2) {
                    Logger.getLogger(C0436ir.class.getName()).log(Level.WARNING, "Failed to stop all DAQ's", (Throwable) e2);
                }
            }
        }
    }

    @Override // G.aG
    public boolean a(String str, G.bS bSVar) {
        return true;
    }

    @Override // G.aG
    public void a(String str) {
        c();
    }

    @Override // G.InterfaceC0124g
    public void b(String str, int i2) {
        this.f3750e++;
        if (this.f3749d > 0) {
            double d2 = this.f3750e / this.f3749d;
            C0338f.a().a(d2);
            if (d2 == 1.0d) {
                b();
            }
        }
        if (this.f3750e <= this.f3749d) {
            C0338f.a().f(this.f3747b + ". Burned page " + i2);
        }
    }

    @Override // G.InterfaceC0124g
    public void a(String str, boolean z2) {
        b();
        C0338f.a().l();
    }

    private void b() {
        new C0437is(this).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        C1425x c1425x = this.f3748c;
        if (c1425x != null) {
            synchronized (c1425x) {
                String strAa = c1425x.aa();
                if (strAa != null && strAa.equals(this.f3747b)) {
                    c1425x.ab();
                    this.f3748c = null;
                }
            }
        }
        if (this.f3746a.C() instanceof bQ.l) {
            bQ.l lVar = (bQ.l) this.f3746a.C();
            try {
                lVar.l();
            } catch (Exception e2) {
                try {
                    bH.C.b("Failed to restart DAQ, retrying.");
                    lVar.l();
                } catch (Exception e3) {
                    Logger.getLogger(C0436ir.class.getName()).log(Level.SEVERE, "Failed to restart DAQ's after 2 attempt", (Throwable) e3);
                }
            }
        }
        C0338f.a().l();
        this.f3746a.C().b((InterfaceC0124g) this);
        this.f3746a.C().b((G.aG) this);
    }

    @Override // G.InterfaceC0124g
    public void a(String str, int i2) {
    }
}
