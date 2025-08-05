package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import r.C1798a;

/* renamed from: aP.gw, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/gw.class */
class C0388gw implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3499a;

    C0388gw(C0308dx c0308dx) {
        this.f3499a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) actionEvent.getSource();
        C1798a.a().b(C1798a.f13319aa, Boolean.toString(jCheckBoxMenuItem.getState()));
        if (jCheckBoxMenuItem.getState()) {
            com.efiAnalytics.ui.dI.a().b();
        } else {
            com.efiAnalytics.ui.dI.a().c();
        }
    }
}
