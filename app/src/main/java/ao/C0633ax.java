package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;

/* renamed from: ao.ax, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/ax.class */
class C0633ax implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0625ap f5302a;

    C0633ax(C0625ap c0625ap) {
        this.f5302a = c0625ap;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) throws NumberFormatException {
        this.f5302a.a(((JMenuItem) actionEvent.getSource()).getName(), actionEvent.getActionCommand());
    }
}
