package bt;

import G.C0072be;
import G.C0113cs;
import G.InterfaceC0109co;
import com.efiAnalytics.ui.C1705w;

/* renamed from: bt.H, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/H.class */
class C1280H implements InterfaceC0109co, bN {

    /* renamed from: b, reason: collision with root package name */
    C1705w f8673b;

    /* renamed from: c, reason: collision with root package name */
    G.R f8675c;

    /* renamed from: d, reason: collision with root package name */
    C0072be f8676d;

    /* renamed from: a, reason: collision with root package name */
    double f8672a = Double.NaN;

    /* renamed from: e, reason: collision with root package name */
    private InterfaceC1357p f8674e = null;

    public C1280H(G.R r2, C0072be c0072be, C1705w c1705w) {
        this.f8675c = r2;
        this.f8676d = c0072be;
        this.f8673b = c1705w;
    }

    @Override // G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) {
        if (str.equals(this.f8673b.g().w())) {
            this.f8672a = d2;
            return;
        }
        if (!str.equals(this.f8673b.g().v()) || this.f8672a == Double.NaN) {
            return;
        }
        try {
            this.f8673b.h().a(d2 + "", this.f8672a + "");
            if (this.f8674e != null) {
                this.f8674e.a(d2, this.f8672a);
            }
        } catch (Exception e2) {
        }
    }

    @Override // bt.bN
    public void a() {
        try {
            C0113cs.a().a(this.f8675c.c(), this.f8676d.f(), this);
            C0113cs.a().a(this.f8675c.c(), this.f8676d.d(), this);
        } catch (V.a e2) {
            bH.C.a("Unable to subscribe x or y axis for hightlights.", e2, this);
        }
    }

    @Override // bt.bN
    public void b() {
        C0113cs.a().a(this);
    }

    public void a(InterfaceC1357p interfaceC1357p) {
        this.f8674e = interfaceC1357p;
    }
}
