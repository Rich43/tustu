package t;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:t/aM.class */
class aM extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aK f13748a;

    aM(aK aKVar) {
        this.f13748a = aKVar;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        JTextField jTextField = (JTextField) keyEvent.getSource();
        if (this.f13748a.a(jTextField, C1818g.b("Gauge Units"))) {
            this.f13748a.c().j(jTextField.getText());
        }
    }
}
