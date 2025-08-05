package t;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: t.A, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:t/A.class */
class C1822A implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1875w f13686a;

    C1822A(C1875w c1875w) {
        this.f13686a = c1875w;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f13686a.d(((JCheckBoxMenuItem) actionEvent.getSource()).getState());
    }
}
