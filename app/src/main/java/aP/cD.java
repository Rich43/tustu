package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aP/cD.class */
class cD implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bZ f3113a;

    cD(bZ bZVar) {
        this.f3113a = bZVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) throws NumberFormatException {
        JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) actionEvent.getSource();
        if (jCheckBoxMenuItem.isSelected()) {
            h.i.c("numberOfOverlays", Integer.parseInt(jCheckBoxMenuItem.getActionCommand()) + "");
            com.efiAnalytics.ui.bV.d(C1818g.b("The changes will take effect after a restart."), jCheckBoxMenuItem);
        }
    }
}
