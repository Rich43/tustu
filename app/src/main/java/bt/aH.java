package bt;

import W.C0188n;
import bH.InterfaceC0999g;
import k.C1756d;

/* loaded from: TunerStudioMS.jar:bt/aH.class */
class aH implements InterfaceC0999g, com.efiAnalytics.ui.bT {

    /* renamed from: a, reason: collision with root package name */
    C0188n f8754a;

    /* renamed from: c, reason: collision with root package name */
    private String f8755c;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1303al f8756b;

    aH(C1303al c1303al, C0188n c0188n, String str) {
        this.f8756b = c1303al;
        this.f8754a = c0188n;
        this.f8755c = str;
    }

    @Override // bH.InterfaceC0999g
    public boolean a(int i2) {
        if (this.f8755c == null || this.f8755c.isEmpty()) {
            return true;
        }
        try {
            return C1756d.a().a(this.f8755c).a(this.f8754a, i2) == 0.0d;
        } catch (ax.U e2) {
            bH.C.a("Unable to evaluate CurvePlotFilter: " + this.f8755c);
            return true;
        }
    }

    public void a(String str) {
        this.f8755c = str;
    }
}
