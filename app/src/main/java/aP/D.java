package aP;

import com.efiAnalytics.ui.InterfaceC1535a;

/* loaded from: TunerStudioMS.jar:aP/D.class */
class D implements InterfaceC1535a {

    /* renamed from: a, reason: collision with root package name */
    String f2739a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0338f f2740b;

    D(C0338f c0338f, String str) {
        this.f2740b = c0338f;
        this.f2739a = null;
        this.f2739a = str;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1535a
    public boolean a() {
        cZ.a().h().a(this.f2739a).h();
        return true;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1535a
    public void b() {
        cZ.a().h().b(this.f2739a);
        aE.a.A().s(this.f2739a);
    }

    @Override // com.efiAnalytics.ui.InterfaceC1535a
    public void c() {
    }
}
