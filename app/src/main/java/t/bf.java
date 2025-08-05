package t;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:t/bf.class */
class bf extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ be f13836a;

    bf(be beVar) {
        this.f13836a = beVar;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        JTextField jTextField = (JTextField) keyEvent.getSource();
        if (this.f13836a.a(jTextField, C1818g.b("Indicator On Text"))) {
            this.f13836a.c().q(jTextField.getText());
        }
    }
}
