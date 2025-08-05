package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:aP/cF.class */
class cF implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bZ f3115a;

    cF(bZ bZVar) {
        this.f3115a = bZVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (((JCheckBoxMenuItem) actionEvent.getSource()).getState()) {
            h.i.c("fieldSelectionStyle", "selectFromDash");
        } else {
            h.i.c("fieldSelectionStyle", "standardSelection");
        }
        com.efiAnalytics.ui.bV.d("The Changes will take effect after restarting.", com.efiAnalytics.ui.bV.c());
    }
}
