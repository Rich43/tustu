package aP;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import r.C1798a;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aP/fL.class */
class fL implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3356a;

    fL(C0308dx c0308dx) {
        this.f3356a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) actionEvent.getSource();
        C1798a.a().b(C1798a.f13382bl, jCheckBoxMenuItem.getState() + "");
        if (com.efiAnalytics.ui.bV.a(C1818g.b("The Project must be re-loaded for changes to take effect.") + "\n" + C1818g.b("Would you like to reload the project now?"), (Component) jCheckBoxMenuItem, true)) {
            C0338f.a().z();
        }
    }
}
