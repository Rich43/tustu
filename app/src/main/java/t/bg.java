package t;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:t/bg.class */
class bg extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ be f13837a;

    bg(be beVar) {
        this.f13837a = beVar;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        JTextField jTextField = (JTextField) keyEvent.getSource();
        if (this.f13837a.a(jTextField, C1818g.b("Indicator Off Text"))) {
            this.f13837a.c().r(jTextField.getText());
        }
    }
}
