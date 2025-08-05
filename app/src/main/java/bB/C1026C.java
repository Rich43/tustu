package bb;

import G.bT;
import aP.cZ;
import com.efiAnalytics.ui.bV;
import s.C1818g;

/* renamed from: bb.C, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bb/C.class */
class C1026C implements bT {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ x f7714a;

    C1026C(x xVar) {
        this.f7714a = xVar;
    }

    @Override // G.bT
    public void a() {
    }

    @Override // G.bT
    public void a(boolean z2) {
        if (z2) {
            return;
        }
        bV.d(C1818g.b("Error reading current settings. Please load your tune manually."), cZ.a().c());
    }
}
