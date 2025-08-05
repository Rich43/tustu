package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:ao/cU.class */
class cU implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5477a;

    cU(bP bPVar) {
        this.f5477a = bPVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        ((JCheckBoxMenuItem) actionEvent.getSource()).setSelected(true);
        h.i.c("lookAndFeelClass", actionEvent.getActionCommand());
        com.efiAnalytics.ui.bV.d("The changes will take effect after the next restart.", this.f5477a.f5355i);
    }
}
