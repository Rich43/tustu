package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: ao.dg, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/dg.class */
class C0697dg implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5554a;

    C0697dg(bP bPVar) {
        this.f5554a = bPVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) throws NumberFormatException {
        JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) actionEvent.getSource();
        if (jCheckBoxMenuItem.isSelected()) {
            this.f5554a.a("numberOfGraphs", Integer.parseInt(jCheckBoxMenuItem.getActionCommand()));
        }
    }
}
