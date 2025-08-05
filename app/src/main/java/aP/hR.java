package aP;

import G.C0125h;
import br.C1254r;
import java.io.File;
import r.C1807j;

/* loaded from: TunerStudioMS.jar:aP/hR.class */
class hR implements aE.e {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ hJ f3541a;

    hR(hJ hJVar) {
        this.f3541a = hJVar;
    }

    @Override // aE.e
    public void a(aE.a aVar, G.R r2) {
    }

    @Override // aE.e
    public void e_() {
        C1254r.a().b();
        C0125h.a().b();
        B.g.a().f();
        B.b.c().a(null);
        com.efiAnalytics.tuningwidgets.panels.W.a();
    }

    @Override // aE.e
    public void a(aE.a aVar) {
        if (G.T.a().c() == null || B.b.c().d() == null || B.b.c().d().e() == null || B.b.c().d().e().isEmpty()) {
            return;
        }
        C1807j.a(new File(aVar.t()), B.b.c().d());
    }
}
