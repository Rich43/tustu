package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: ao.dh, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/dh.class */
class C0698dh implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5555a;

    C0698dh(bP bPVar) {
        this.f5555a = bPVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) throws NumberFormatException {
        JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) actionEvent.getSource();
        if (jCheckBoxMenuItem.isSelected()) {
            this.f5555a.a("numberOfOverlays", Integer.parseInt(jCheckBoxMenuItem.getActionCommand()));
        }
    }
}
