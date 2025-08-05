package t;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: t.E, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:t/E.class */
class C1826E implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1875w f13690a;

    C1826E(C1875w c1875w) {
        this.f13690a = c1875w;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f13690a.b(((JCheckBoxMenuItem) actionEvent.getSource()).getState());
    }
}
