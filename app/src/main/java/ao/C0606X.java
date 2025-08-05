package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;

/* renamed from: ao.X, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/X.class */
final class C0606X implements ActionListener {
    C0606X() {
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JMenuItem jMenuItem = (JMenuItem) actionEvent.getSource();
        C0645bi.a().b().a(jMenuItem.getName(), jMenuItem.getActionCommand());
    }
}
