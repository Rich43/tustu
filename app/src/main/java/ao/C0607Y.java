package ao;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;

/* renamed from: ao.Y, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/Y.class */
final class C0607Y implements ActionListener {
    C0607Y() {
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(((JMenuItem) actionEvent.getSource()).getActionCommand()), new C0608Z(this));
    }
}
