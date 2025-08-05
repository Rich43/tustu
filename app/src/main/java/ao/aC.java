package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;

/* loaded from: TunerStudioMS.jar:ao/aC.class */
class aC implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0625ap f5115a;

    aC(C0625ap c0625ap) {
        this.f5115a = c0625ap;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) throws NumberFormatException {
        this.f5115a.a(((JMenuItem) actionEvent.getSource()).getName(), actionEvent.getActionCommand());
    }
}
