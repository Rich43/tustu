package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;

/* renamed from: ao.ar, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/ar.class */
class C0627ar implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0625ap f5296a;

    C0627ar(C0625ap c0625ap) {
        this.f5296a = c0625ap;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) throws NumberFormatException {
        this.f5296a.a(((JMenuItem) actionEvent.getSource()).getName(), actionEvent.getActionCommand());
    }
}
