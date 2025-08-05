package t;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

/* loaded from: TunerStudioMS.jar:t/aW.class */
class aW extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aO f13772a;

    aW(aO aOVar) {
        this.f13772a = aOVar;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        JTextField jTextField = (JTextField) keyEvent.getSource();
        if (jTextField.getText().equals("")) {
            return;
        }
        try {
            this.f13772a.c().k(jTextField.getText());
        } catch (V.a e2) {
        }
    }
}
