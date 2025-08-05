package t;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: t.B, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:t/B.class */
class C1823B implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1875w f13687a;

    C1823B(C1875w c1875w) {
        this.f13687a = c1875w;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f13687a.a(((JCheckBoxMenuItem) actionEvent.getSource()).getState());
    }
}
