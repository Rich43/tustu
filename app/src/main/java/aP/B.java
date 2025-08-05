package aP;

import com.efiAnalytics.ui.InterfaceC1662et;
import r.C1798a;

/* loaded from: TunerStudioMS.jar:aP/B.class */
class B implements InterfaceC1662et {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0338f f2733a;

    B(C0338f c0338f) {
        this.f2733a = c0338f;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1662et
    public void a(String str, String str2) {
        C1798a.a().b(str, str2);
    }

    @Override // com.efiAnalytics.ui.InterfaceC1662et
    public String a(String str) {
        return C1798a.a().c(str);
    }

    @Override // com.efiAnalytics.ui.InterfaceC1662et
    public String b(String str, String str2) {
        return str2;
    }
}
