package t;

import com.efiAnalytics.ui.bV;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:t/aX.class */
class aX implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aO f13773a;

    aX(aO aOVar) {
        this.f13773a = aOVar;
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
        JTextField jTextField = (JTextField) focusEvent.getSource();
        if (jTextField.getText().equals("")) {
            return;
        }
        try {
            this.f13773a.c().k(jTextField.getText());
        } catch (V.a e2) {
            bV.d(C1818g.b("Invalid Expression or value."), jTextField);
            jTextField.setText(this.f13773a.f13759k);
        }
    }
}
