package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: ao.U, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/U.class */
final class C0603U implements ActionListener {
    C0603U() {
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) actionEvent.getSource();
        if (jCheckBoxMenuItem.isSelected()) {
            C0601S.a(jCheckBoxMenuItem.getName());
            return;
        }
        String strC = h.i.c("FIELD_MIN_MAX_" + jCheckBoxMenuItem.getName());
        if (strC == null || strC.indexOf(";") == -1) {
            C0601S.a(jCheckBoxMenuItem.getName(), "", "");
            return;
        }
        C0601S.a(jCheckBoxMenuItem.getName(), strC.substring(0, strC.indexOf(";")), strC.substring(strC.indexOf(";") + 1, strC.length()));
    }
}
