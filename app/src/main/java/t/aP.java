package t;

import com.efiAnalytics.ui.bV;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:t/aP.class */
class aP extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aO f13765a;

    aP(aO aOVar) {
        this.f13765a = aOVar;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        JTextField jTextField = (JTextField) keyEvent.getSource();
        if (jTextField.getText().equals("")) {
            return;
        }
        try {
            double dA = this.f13765a.a(jTextField.getText());
            if (this.f13765a.a(jTextField, C1818g.b("Current Value"))) {
                this.f13765a.c().a(dA);
            }
        } catch (Exception e2) {
            bV.d(C1818g.b("Numeric Values only"), jTextField);
        }
    }
}
