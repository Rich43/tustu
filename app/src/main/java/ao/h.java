package aO;

import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.eB;

/* loaded from: TunerStudioMS.jar:aO/h.class */
class h implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ g f2662a;

    h(g gVar) {
        this.f2662a = gVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f2662a.f2658b = new eB(bV.a(this.f2662a.f2661e.f2649k), "Loading Ignition Log", "Loading Ignition Log File, please wait....", true, false);
        this.f2662a.f2658b.a(this.f2662a.f2660d);
        this.f2662a.f2658b.setVisible(true);
    }
}
