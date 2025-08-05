package t;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

/* loaded from: TunerStudioMS.jar:t/aR.class */
class aR extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aO f13767a;

    aR(aO aOVar) {
        this.f13767a = aOVar;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        JTextField jTextField = (JTextField) keyEvent.getSource();
        if (jTextField.getText().equals("")) {
            return;
        }
        try {
            this.f13767a.c().o(jTextField.getText());
        } catch (Exception e2) {
        }
    }
}
