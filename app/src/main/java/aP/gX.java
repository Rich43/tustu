package aP;

import com.efiAnalytics.apps.ts.dashboard.C1425x;

/* loaded from: TunerStudioMS.jar:aP/gX.class */
class gX implements InterfaceC0285d {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ gW f3472a;

    gX(gW gWVar) {
        this.f3472a = gWVar;
    }

    @Override // aP.InterfaceC0285d
    public void a() {
        C0338f.a().a(this.f3472a.f3451b);
        if (aE.a.A() != null) {
            aE.a.A().d(true);
        }
    }

    @Override // aP.InterfaceC0285d
    public boolean a(String str, C1425x c1425x) {
        aE.a.A().s(str);
        if (aE.a.A() == null) {
            return true;
        }
        aE.a.A().d(true);
        return true;
    }
}
