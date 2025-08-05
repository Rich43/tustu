package bt;

import com.efiAnalytics.ui.BinTableView;
import com.efiAnalytics.ui.InterfaceC1662et;

/* renamed from: bt.by, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/by.class */
class C1343by implements InterfaceC1662et {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1337bs f9074a;

    C1343by(C1337bs c1337bs) {
        this.f9074a = c1337bs;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1662et
    public void a(String str, String str2) {
        aE.a.A().setProperty("2DTable_" + this.f9074a.getName() + "_" + str, str2);
    }

    @Override // com.efiAnalytics.ui.InterfaceC1662et
    public String a(String str) {
        return str.equals(BinTableView.f10646n) ? aE.a.A().t() : b(str, "");
    }

    @Override // com.efiAnalytics.ui.InterfaceC1662et
    public String b(String str, String str2) {
        return aE.a.A().getProperty("2DTable_" + this.f9074a.getName() + "_" + str, str2);
    }
}
