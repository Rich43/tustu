package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/* loaded from: TunerStudioMS.jar:ao/cO.class */
class cO implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5471a;

    cO(bP bPVar) {
        this.f5471a = bPVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f5471a.a(((JMenuItem) actionEvent.getSource()).getActionCommand(), (JMenu) null);
    }
}
