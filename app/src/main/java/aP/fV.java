package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import r.C1798a;

/* loaded from: TunerStudioMS.jar:aP/fV.class */
class fV implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3366a;

    fV(C0308dx c0308dx) {
        this.f3366a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) actionEvent.getSource();
        C1798a.a().b(C1798a.f13376bf, jCheckBoxMenuItem.getState() + "");
        if (jCheckBoxMenuItem.getState()) {
            return;
        }
        C1798a.a().b(C1798a.f13377bg, jCheckBoxMenuItem.getState() + "");
    }
}
