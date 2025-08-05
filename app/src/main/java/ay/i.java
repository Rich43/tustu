package aY;

import G.aF;
import bH.W;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aY/i.class */
class i implements aF {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ f f4064a;

    i(f fVar) {
        this.f4064a = fVar;
    }

    @Override // G.aF
    public void a(String str, byte[] bArr) throws IllegalArgumentException {
        if (bArr == null) {
            this.f4064a.f4054g.setText(C1818g.b("Polling device") + "...");
        } else if (this.f4064a.f4060m.f() > 0.0d) {
            this.f4064a.f4050c.setText(W.b(this.f4064a.f4060m.f(), 3) + " sec.");
            this.f4064a.f4051d.setText(this.f4064a.f4060m.e() + "");
            this.f4064a.f4052e.setText(W.b(this.f4064a.f4060m.o(), 1) + "/s");
            this.f4064a.f4054g.setText(C1818g.b("Logging") + "...");
        }
    }
}
