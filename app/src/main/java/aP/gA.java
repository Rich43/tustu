package aP;

import bH.C1007o;
import com.efiAnalytics.ui.InterfaceC1598ci;

/* loaded from: TunerStudioMS.jar:aP/gA.class */
class gA implements InterfaceC1598ci {

    /* renamed from: a, reason: collision with root package name */
    G.R f3398a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0308dx f3399b;

    public gA(C0308dx c0308dx, G.R r2) {
        this.f3399b = c0308dx;
        this.f3398a = null;
        this.f3398a = r2;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1598ci
    public boolean a(String str) {
        try {
            return C1007o.a(str, this.f3398a);
        } catch (Exception e2) {
            bH.C.c(e2.getMessage());
            return true;
        }
    }
}
