package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;

/* loaded from: TunerStudioMS.jar:ao/aA.class */
class aA implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0625ap f5113a;

    aA(C0625ap c0625ap) {
        this.f5113a = c0625ap;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) throws NumberFormatException {
        this.f5113a.a(((JMenuItem) actionEvent.getSource()).getName(), actionEvent.getActionCommand());
    }
}
