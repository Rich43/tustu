package bt;

import G.C0072be;
import com.efiAnalytics.ui.C1705w;

/* loaded from: TunerStudioMS.jar:bt/bV.class */
public class bV implements G.aG, bN {

    /* renamed from: a, reason: collision with root package name */
    C1280H f8976a;

    /* renamed from: b, reason: collision with root package name */
    com.efiAnalytics.tuningwidgets.panels.Q f8977b;

    /* renamed from: c, reason: collision with root package name */
    bN f8978c;

    public bV(G.R r2, C0072be c0072be, C1705w c1705w, InterfaceC1357p interfaceC1357p) {
        this.f8976a = new C1280H(r2, c0072be, c1705w);
        this.f8976a.a(interfaceC1357p);
        this.f8977b = new com.efiAnalytics.tuningwidgets.panels.Q(r2, c0072be, interfaceC1357p);
        if (r2.R()) {
            this.f8978c = this.f8976a;
        } else {
            this.f8978c = this.f8977b;
        }
        a();
    }

    @Override // bt.bN
    public void a() {
        this.f8978c.a();
    }

    @Override // bt.bN
    public void b() {
        this.f8978c.b();
    }

    @Override // G.aG
    public boolean a(String str, G.bS bSVar) {
        b();
        this.f8978c = this.f8976a;
        a();
        return true;
    }

    @Override // G.aG
    public void a(String str) {
        b();
        this.f8978c = this.f8977b;
        a();
    }
}
