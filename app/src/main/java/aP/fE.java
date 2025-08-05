package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import r.C1798a;

/* loaded from: TunerStudioMS.jar:aP/fE.class */
class fE implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3349a;

    fE(C0308dx c0308dx) {
        this.f3349a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) actionEvent.getSource();
        jCheckBoxMenuItem.setSelected(true);
        C1798a.a().b("navigationStyle", jCheckBoxMenuItem.getActionCommand());
        this.f3349a.b(jCheckBoxMenuItem.getActionCommand());
    }
}
