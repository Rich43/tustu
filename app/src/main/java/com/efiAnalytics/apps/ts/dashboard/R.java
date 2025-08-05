package com.efiAnalytics.apps.ts.dashboard;

import com.efiAnalytics.ui.InterfaceC1535a;
import com.efiAnalytics.ui.bV;
import java.awt.Component;
import r.C1799b;
import r.C1807j;
import v.C1883c;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/R.class */
class R implements InterfaceC1535a {

    /* renamed from: b, reason: collision with root package name */
    private C1799b f9392b;

    /* renamed from: c, reason: collision with root package name */
    private String[] f9393c;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1425x f9394a;

    R(C1425x c1425x, C1799b c1799b, String[] strArr) {
        this.f9394a = c1425x;
        this.f9392b = null;
        this.f9393c = null;
        this.f9392b = c1799b;
        this.f9393c = strArr;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1535a
    public boolean a() {
        d();
        if (this.f9394a.ad() == null) {
            return true;
        }
        this.f9394a.h();
        return true;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1535a
    public void b() {
    }

    @Override // com.efiAnalytics.ui.InterfaceC1535a
    public void c() {
        d();
        if (this.f9394a.ad() != null) {
            this.f9394a.h();
        }
    }

    private void d() {
        try {
            Z zA = new C1883c(C1807j.G()).a(this.f9392b.b().getAbsolutePath());
            if (this.f9392b.c() && !a(zA.d())) {
                String str = this.f9393c[0];
                for (int i2 = 1; i2 < this.f9393c.length; i2++) {
                    str = str + " or " + this.f9393c[i2];
                }
                if (!bV.a("Warning: Gauge Cluster firmware signature (" + zA.d() + ")\ndoes not match current firmware (" + str + ").\nYou may need to reset gauge output channels.\n \nContinue Loading?", (Component) this.f9394a.getParent(), true)) {
                    return;
                }
            }
            zA.b(this.f9393c[0]);
            Z zA2 = C1389ab.a(G.T.a().d(), zA);
            G.R r2 = this.f9394a.f9584k;
            if (r2 != null) {
                new C1388aa().b(r2, zA2);
            }
            this.f9394a.a(zA2);
            this.f9394a.d(this.f9392b.g());
            this.f9394a.aB();
            this.f9394a.f9593aG = false;
        } catch (V.a e2) {
            bH.C.a("Unable to load dash file:\n" + ((Object) this.f9392b.b()), e2, this.f9394a.getParent());
        }
    }

    private boolean a(String str) {
        for (int i2 = 0; i2 < this.f9393c.length; i2++) {
            if (this.f9393c[i2].equals(str)) {
                return true;
            }
        }
        return false;
    }
}
