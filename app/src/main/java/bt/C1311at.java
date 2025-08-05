package bt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: bt.at, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/at.class */
class C1311at implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1303al f8894a;

    C1311at(C1303al c1303al) {
        this.f8894a = c1303al;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) actionEvent.getSource();
        this.f8894a.f8871z.a(C1303al.f8849f, jCheckBoxMenuItem.getState() + "");
        this.f8894a.f8861p.d(jCheckBoxMenuItem.getState());
        this.f8894a.f8861p.e();
    }
}
