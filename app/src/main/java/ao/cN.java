package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;

/* loaded from: TunerStudioMS.jar:ao/cN.class */
class cN implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5470a;

    cN(bP bPVar) {
        this.f5470a = bPVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JMenuItem jMenuItem = (JMenuItem) actionEvent.getSource();
        this.f5470a.a(jMenuItem.getName(), jMenuItem.getActionCommand());
    }
}
