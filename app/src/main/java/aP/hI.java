package aP;

import r.C1798a;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aP/hI.class */
class hI implements u.g {

    /* renamed from: a, reason: collision with root package name */
    G.R f3528a;

    /* renamed from: b, reason: collision with root package name */
    G.Y f3529b;

    /* renamed from: c, reason: collision with root package name */
    boolean f3530c = false;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ hC f3531d;

    hI(hC hCVar, G.R r2, G.Y y2) {
        this.f3531d = hCVar;
        this.f3528a = r2;
        this.f3529b = y2;
    }

    @Override // u.g
    public String a() {
        return "Send Current " + C1798a.f13268b + " Settings";
    }

    @Override // u.g
    public String b() {
        return "Update the Controller with the current " + C1798a.f13268b + " settings.";
    }

    @Override // u.g
    public boolean d() {
        double dO = 0.0d;
        if (this.f3528a.g("rpm") != null && this.f3528a.O().C() == null) {
            dO = this.f3528a.g("rpm").o();
        }
        int i2 = 0;
        while (true) {
            int i3 = i2;
            i2++;
            if (i3 >= 3 || dO <= 10.0d) {
                break;
            }
            com.efiAnalytics.ui.bV.d(C1818g.b("Turn off engine before sending local Tune."), cZ.a().c());
            dO = this.f3528a.g("rpm").o();
        }
        boolean zA = this.f3531d.a(this.f3528a, this.f3529b);
        this.f3530c = true;
        return zA;
    }

    @Override // u.g
    public boolean c() {
        return this.f3530c;
    }
}
