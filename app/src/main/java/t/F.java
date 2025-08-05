package t;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:t/F.class */
class F implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1875w f13691a;

    F(C1875w c1875w) {
        this.f13691a = c1875w;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f13691a.c(((JCheckBoxMenuItem) actionEvent.getSource()).getState());
    }
}
