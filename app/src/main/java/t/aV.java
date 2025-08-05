package t;

import com.efiAnalytics.ui.bV;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:t/aV.class */
class aV extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aO f13771a;

    aV(aO aOVar) {
        this.f13771a = aOVar;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        JTextField jTextField = (JTextField) keyEvent.getSource();
        if (jTextField.getText().equals("")) {
            return;
        }
        try {
            double dA = this.f13771a.a(jTextField.getText());
            if (this.f13771a.a(jTextField, C1818g.b("Historical Peak Value"))) {
                this.f13771a.c().b(dA);
            }
        } catch (Exception e2) {
            bV.d(C1818g.b("Numeric Values only"), jTextField);
        }
    }
}
