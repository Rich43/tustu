package bt;

import com.efiAnalytics.apps.ts.dashboard.Gauge;
import com.efiAnalytics.ui.InterfaceC1649eg;

/* loaded from: TunerStudioMS.jar:bt/Z.class */
class Z implements InterfaceC1649eg {

    /* renamed from: a, reason: collision with root package name */
    Gauge f8735a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ U f8736b;

    Z(U u2, Gauge gauge) {
        this.f8736b = u2;
        this.f8735a = null;
        this.f8735a = gauge;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1649eg
    public void a(int i2, int i3, double d2) {
        if (this.f8735a != null) {
            this.f8735a.setCurrentOutputChannelValue("", d2);
            this.f8735a.repaint();
        }
    }
}
