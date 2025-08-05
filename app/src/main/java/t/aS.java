package t;

import com.efiAnalytics.ui.bV;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:t/aS.class */
class aS implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aO f13768a;

    aS(aO aOVar) {
        this.f13768a = aOVar;
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
            this.f13768a.c().o(jTextField.getText());
        } catch (V.a e2) {
            bV.d(C1818g.b("Valid Expressions or Numeric Values only.") + "\n" + e2.getLocalizedMessage(), jTextField);
            jTextField.setText(this.f13768a.f13763o);
        }
    }
}
