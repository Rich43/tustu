package bt;

import com.efiAnalytics.ui.InterfaceC1662et;

/* loaded from: TunerStudioMS.jar:bt/aO.class */
class aO implements InterfaceC1662et {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1303al f8768a;

    aO(C1303al c1303al) {
        this.f8768a = c1303al;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1662et
    public void a(String str, String str2) {
        aE.a.A().setProperty(this.f8768a.f8862q.aJ() + "_" + str, str2);
    }

    @Override // com.efiAnalytics.ui.InterfaceC1662et
    public String a(String str) {
        return b(str, "");
    }

    @Override // com.efiAnalytics.ui.InterfaceC1662et
    public String b(String str, String str2) {
        return aE.a.A().getProperty(this.f8768a.f8862q.aJ() + "_" + str, str2);
    }
}
