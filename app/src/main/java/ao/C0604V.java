package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;

/* renamed from: ao.V, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/V.class */
final class C0604V implements ActionListener {
    C0604V() {
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JMenuItem jMenuItem = (JMenuItem) actionEvent.getSource();
        String strA = h.i.a("FIELD_MIN_MAX_" + jMenuItem.getName(), "");
        C0601S.a(jMenuItem.getName(), strA.substring(0, strA.indexOf(";")), strA.substring(strA.indexOf(";") + 1, strA.length()));
    }
}
