package bt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: bt.au, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/au.class */
class C1312au implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1303al f8895a;

    C1312au(C1303al c1303al) {
        this.f8895a = c1303al;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) actionEvent.getSource();
        this.f8895a.f8871z.a(C1303al.f8855l, jCheckBoxMenuItem.getState() + "");
        this.f8895a.f8861p.f(jCheckBoxMenuItem.getState());
        this.f8895a.f8861p.o();
        this.f8895a.f8861p.repaint();
    }
}
