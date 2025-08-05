package aP;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import r.C1798a;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aP/eD.class */
class eD implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3284a;

    eD(C0308dx c0308dx) {
        this.f3284a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (com.efiAnalytics.ui.bV.a(C1818g.b("Are you sure you wish to remove the registration information?") + "\n\n" + C1818g.b("This installation of " + C1798a.f13268b + " will run as the unregistered version."), (Component) this.f3284a.f3270h, true)) {
            C0338f.a().A();
        }
    }
}
