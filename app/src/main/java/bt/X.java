package bt;

import com.efiAnalytics.ui.InterfaceC1662et;
import r.C1798a;

/* loaded from: TunerStudioMS.jar:bt/X.class */
class X implements InterfaceC1662et {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ U f8732a;

    X(U u2) {
        this.f8732a = u2;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1662et
    public void a(String str, String str2) {
        C1798a.a().b(this.f8732a.f8718i + "_" + str, str2);
    }

    @Override // com.efiAnalytics.ui.InterfaceC1662et
    public String a(String str) {
        return b(str, "");
    }

    @Override // com.efiAnalytics.ui.InterfaceC1662et
    public String b(String str, String str2) {
        return C1798a.a().c(this.f8732a.f8718i + "_" + str, str2);
    }
}
