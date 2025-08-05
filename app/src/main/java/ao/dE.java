package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:ao/dE.class */
class dE implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5518a;

    dE(bP bPVar) {
        this.f5518a = bPVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) actionEvent.getSource();
        h.i.c("fieldNameNormaization", Boolean.toString(jCheckBoxMenuItem.isSelected()));
        this.f5518a.f5356j.setEnabled(jCheckBoxMenuItem.isSelected());
    }
}
