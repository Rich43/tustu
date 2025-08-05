package bt;

import G.C0113cs;
import G.InterfaceC0109co;

/* loaded from: TunerStudioMS.jar:bt/Y.class */
class Y implements InterfaceC0109co, bN {

    /* renamed from: a, reason: collision with root package name */
    double f8733a = Double.NaN;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ U f8734b;

    Y(U u2) {
        this.f8734b = u2;
    }

    @Override // G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) {
        if (this.f8734b.f8712c == null) {
            return;
        }
        if (this.f8734b.f8712c.d() != null && str.equals(this.f8734b.f8712c.d())) {
            this.f8734b.b(d2);
        } else {
            if (this.f8734b.f8712c.f() == null || !str.equals(this.f8734b.f8712c.f())) {
                return;
            }
            this.f8734b.a(d2);
        }
    }

    @Override // bt.bN
    public void a() {
        C0113cs c0113csA = C0113cs.a();
        try {
            c0113csA.a(this.f8734b.f8709q.c(), this.f8734b.f8712c.d(), this);
            c0113csA.a(this.f8734b.f8709q.c(), this.f8734b.f8712c.f(), this);
        } catch (V.a e2) {
            com.efiAnalytics.ui.bV.d("Error subscribing output channel.", this.f8734b.f8711b);
            e2.printStackTrace();
        }
    }

    @Override // bt.bN
    public void b() {
        C0113cs.a().a(this);
    }
}
