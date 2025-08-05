package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;

/* renamed from: ao.as, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/as.class */
class C0628as implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0625ap f5297a;

    C0628as(C0625ap c0625ap) {
        this.f5297a = c0625ap;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) throws NumberFormatException {
        this.f5297a.a(((JMenuItem) actionEvent.getSource()).getName(), actionEvent.getActionCommand());
    }
}
