package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:aP/fT.class */
class fT implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3364a;

    fT(C0308dx c0308dx) {
        this.f3364a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) actionEvent.getSource();
        aE.a.A().b(jCheckBoxMenuItem.getState());
        if (jCheckBoxMenuItem.getState()) {
            C0338f.a().J();
        } else {
            C0338f.a().K();
        }
    }
}
