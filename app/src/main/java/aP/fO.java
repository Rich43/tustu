package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import r.C1798a;

/* loaded from: TunerStudioMS.jar:aP/fO.class */
class fO implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3359a;

    fO(C0308dx c0308dx) {
        this.f3359a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) actionEvent.getSource();
        C1798a.a().b(C1798a.f13298F, Boolean.toString(jCheckBoxMenuItem.getState()));
        C1798a.a().b(C1798a.f13300H, Boolean.toString(!jCheckBoxMenuItem.getState()));
        this.f3359a.r();
    }
}
