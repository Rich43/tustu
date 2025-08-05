package t;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:t/aL.class */
class aL extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aK f13747a;

    aL(aK aKVar) {
        this.f13747a = aKVar;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        JTextField jTextField = (JTextField) keyEvent.getSource();
        if (this.f13747a.a(jTextField, C1818g.b("Gauge Title"))) {
            this.f13747a.c().i(jTextField.getText());
        }
    }
}
