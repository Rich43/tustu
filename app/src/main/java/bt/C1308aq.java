package bt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: bt.aq, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/aq.class */
class C1308aq implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1303al f8891a;

    C1308aq(C1303al c1303al) {
        this.f8891a = c1303al;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) actionEvent.getSource();
        this.f8891a.f8871z.a(C1303al.f8852i, jCheckBoxMenuItem.getState() + "");
        this.f8891a.f8861p.e(jCheckBoxMenuItem.getState());
        this.f8891a.f8861p.e();
    }
}
