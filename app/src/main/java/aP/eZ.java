package aP;

import com.efiAnalytics.ui.InterfaceC1633dr;
import java.awt.Component;
import java.awt.Window;
import r.C1798a;

/* loaded from: TunerStudioMS.jar:aP/eZ.class */
class eZ implements InterfaceC1633dr {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3307a;

    eZ(C0308dx c0308dx) {
        this.f3307a = c0308dx;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1633dr
    public void a() {
        if (!C1798a.a().c(C1798a.f13302J, false)) {
            this.f3307a.s();
            return;
        }
        C1798a.a().b(C1798a.f13302J, Boolean.toString(false));
        if (com.efiAnalytics.ui.bV.a(C1798a.f13268b + " must restart for the changes to take effect.\n\nRestart Now?", (Component) this.f3307a.f3270h, true)) {
            C0338f.a().d((Window) this.f3307a.f3270h);
        }
    }
}
