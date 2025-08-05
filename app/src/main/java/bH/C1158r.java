package bh;

import G.R;
import bH.C;
import com.efiAnalytics.ui.aO;

/* renamed from: bh.r, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bh/r.class */
class C1158r implements aO {

    /* renamed from: a, reason: collision with root package name */
    R f8129a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1154n f8130b;

    C1158r(C1154n c1154n, R r2) {
        this.f8130b = c1154n;
        this.f8129a = r2;
    }

    @Override // com.efiAnalytics.ui.aO
    public void d() {
        try {
            this.f8129a.p().d();
        } catch (V.g e2) {
            C.a("Error performing redo:", e2, this);
        }
        this.f8130b.f8118h.requestFocus();
        a();
    }

    @Override // com.efiAnalytics.ui.aO
    public void e() {
        try {
            this.f8129a.p().c();
        } catch (V.g e2) {
            C.a("Error performing undo:", e2, this);
        }
        a();
    }

    @Override // com.efiAnalytics.ui.aO
    public void f() {
        new C1159s(this).start();
        a();
    }

    @Override // com.efiAnalytics.ui.aO
    public void i() {
    }

    public void a() {
    }
}
