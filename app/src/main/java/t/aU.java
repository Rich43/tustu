package t;

import com.efiAnalytics.ui.bV;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:t/aU.class */
class aU implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aO f13770a;

    aU(aO aOVar) {
        this.f13770a = aOVar;
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
            this.f13770a.c().p(jTextField.getText());
        } catch (V.a e2) {
            bV.d(C1818g.b("Valid Expressions or Numeric Values only.") + "\n" + e2.getLocalizedMessage(), jTextField);
            jTextField.setText(this.f13770a.f13764p);
        }
    }
}
