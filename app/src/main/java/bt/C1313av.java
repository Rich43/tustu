package bt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: bt.av, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/av.class */
class C1313av implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1303al f8896a;

    C1313av(C1303al c1303al) {
        this.f8896a = c1303al;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f8896a.f8871z.a(C1303al.f8857n, ((JCheckBoxMenuItem) actionEvent.getSource()).getState() + "");
        this.f8896a.s();
        this.f8896a.f8861p.o();
        this.f8896a.f8861p.repaint();
    }
}
