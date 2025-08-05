package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/* loaded from: TunerStudioMS.jar:ao/cQ.class */
class cQ implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5473a;

    cQ(bP bPVar) {
        this.f5473a = bPVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JMenuItem jMenuItem = (JMenuItem) actionEvent.getSource();
        String actionCommand = jMenuItem.getActionCommand();
        h.i.d(actionCommand);
        JPopupMenu jPopupMenu = (JPopupMenu) jMenuItem.getParent();
        for (int i2 = 0; i2 < jPopupMenu.getComponentCount(); i2++) {
            if (jPopupMenu.getComponent(i2) instanceof JCheckBoxMenuItem) {
                JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) jPopupMenu.getComponent(i2);
                jCheckBoxMenuItem.setState(jCheckBoxMenuItem.getName().equals(h.i.c("DEFAULT_" + actionCommand)));
            }
        }
    }
}
