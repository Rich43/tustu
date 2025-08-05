package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:ao/cZ.class */
class cZ implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5482a;

    cZ(bP bPVar) {
        this.f5482a = bPVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) actionEvent.getSource();
        h.i.c(h.i.f12311af, Boolean.toString(jCheckBoxMenuItem.isSelected()));
        this.f5482a.f5347a.p().b(jCheckBoxMenuItem.isSelected());
        this.f5482a.f5347a.p().i();
        this.f5482a.f5347a.p().repaint();
    }
}
