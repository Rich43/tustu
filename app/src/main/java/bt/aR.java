package bt;

import com.efiAnalytics.ui.InterfaceC1648ef;
import com.efiAnalytics.ui.InterfaceC1657eo;

/* loaded from: TunerStudioMS.jar:bt/aR.class */
class aR implements InterfaceC1657eo {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1303al f8771a;

    aR(C1303al c1303al) {
        this.f8771a = c1303al;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1657eo
    public void a(InterfaceC1648ef[] interfaceC1648efArr) {
        this.f8771a.a(interfaceC1648efArr);
        if (this.f8771a.f8861p != null) {
            this.f8771a.f8861p.a(interfaceC1648efArr);
            this.f8771a.f8861p.repaint();
        }
    }
}
