package aS;

import G.InterfaceC0109co;
import G.R;
import aP.cZ;

/* loaded from: TunerStudioMS.jar:aS/i.class */
class i implements InterfaceC0109co {

    /* renamed from: a, reason: collision with root package name */
    boolean f3919a = false;

    /* renamed from: b, reason: collision with root package name */
    k f3920b = null;

    /* renamed from: c, reason: collision with root package name */
    R f3921c;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ g f3922d;

    i(g gVar, R r2) {
        this.f3922d = gVar;
        this.f3921c = null;
        this.f3921c = r2;
    }

    @Override // G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) {
        if (this.f3920b != null) {
            this.f3920b.a(false);
        }
        if (d2 != 0.0d) {
            if (this.f3920b == null || !this.f3920b.isAlive()) {
                this.f3920b = new k(this.f3922d, this.f3921c);
                this.f3920b.start();
            } else {
                this.f3920b.a(true);
            }
            this.f3919a = true;
            return;
        }
        if (this.f3919a && d2 == 0.0d) {
            if (this.f3920b.b() != null) {
                this.f3920b.b().ac();
            }
            cZ.a().b().ac();
            this.f3919a = false;
        }
    }
}
