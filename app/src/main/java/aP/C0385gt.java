package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import r.C1798a;

/* renamed from: aP.gt, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/gt.class */
class C0385gt implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3496a;

    C0385gt(C0308dx c0308dx) {
        this.f3496a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) actionEvent.getSource();
        C1798a.a().b(C1798a.f13386bp, jCheckBoxMenuItem.getState() + "");
        C1798a.a().b(C1798a.f13384bn, jCheckBoxMenuItem.getState() + "");
        if (jCheckBoxMenuItem.getState()) {
            com.efiAnalytics.ui.bV.d("Warning!!\n\nThe VE Analyze and Diagnostics tabs are not accessible in Lite Mode.\n\nYou must restart for the changes to take effect.", jCheckBoxMenuItem);
        } else {
            com.efiAnalytics.ui.bV.d("You must restart for the changes to take effect.", jCheckBoxMenuItem);
        }
    }
}
