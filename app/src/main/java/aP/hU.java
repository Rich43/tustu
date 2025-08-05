package aP;

import bh.C1152l;
import y.C1895c;

/* loaded from: TunerStudioMS.jar:aP/hU.class */
class hU implements G.S {

    /* renamed from: a, reason: collision with root package name */
    G.R f3544a = null;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ hJ f3545b;

    hU(hJ hJVar) {
        this.f3545b = hJVar;
    }

    @Override // G.S
    public void a(G.R r2) {
        this.f3544a = r2;
        C1152l.b().a(this.f3544a);
    }

    @Override // G.S
    public void b(G.R r2) {
        I.k.a().a(r2.c(), this.f3544a != null && this.f3544a.equals(r2));
        I.k.a().b();
    }

    @Override // G.S
    public void c(G.R r2) {
        r2.a(new C1895c(r2));
    }
}
