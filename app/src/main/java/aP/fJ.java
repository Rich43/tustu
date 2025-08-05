package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import r.C1798a;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aP/fJ.class */
class fJ implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3354a;

    fJ(C0308dx c0308dx) {
        this.f3354a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) actionEvent.getSource();
        C1798a.a().b(C1798a.f13410bN, jCheckBoxMenuItem.getState() + "");
        com.efiAnalytics.ui.bV.d(C1818g.b("Changes will take effect next time you start TunerStudio."), jCheckBoxMenuItem);
    }
}
