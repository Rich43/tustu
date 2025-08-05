package aP;

import bt.C1350i;
import com.efiAnalytics.tunerStudio.panels.TriggerLoggerPanel;
import n.InterfaceC1761a;
import r.C1806i;
import s.C1818g;

/* renamed from: aP.bg, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/bg.class */
public class C0238bg extends n.n implements aE.e, InterfaceC1761a, n.g {

    /* renamed from: a, reason: collision with root package name */
    TriggerLoggerPanel f3071a = null;

    /* renamed from: b, reason: collision with root package name */
    G.R f3072b = null;

    public C0238bg() {
        setTabPlacement(1);
    }

    @Override // aE.e
    public void a(aE.a aVar, G.R r2) {
        this.f3072b = r2;
    }

    @Override // aE.e
    public void e_() {
        if (this.f3071a != null && this.f3072b != null) {
            this.f3071a.b(this.f3072b);
        }
        removeAll();
        this.f3071a = null;
        this.f3072b = null;
    }

    private void c() throws IllegalArgumentException {
        this.f3071a = new TriggerLoggerPanel(C1806i.a().a("pokfr09i0943"));
        this.f3071a.a(new C1350i("ignitionLogger"));
        this.f3071a.c();
        addTab(C1818g.b("High Speed Logger"), this.f3071a);
        this.f3071a.d(this.f3072b);
    }

    @Override // n.InterfaceC1761a
    public boolean a() throws IllegalArgumentException {
        if (this.f3071a != null) {
            return true;
        }
        c();
        return true;
    }

    @Override // aE.e
    public void a(aE.a aVar) {
    }

    @Override // n.g
    public void b() {
        if (this.f3071a != null) {
            this.f3071a.b();
        }
    }
}
