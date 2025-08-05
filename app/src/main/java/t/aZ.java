package t;

import com.efiAnalytics.ui.bV;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:t/aZ.class */
class aZ implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aO f13775a;

    aZ(aO aOVar) {
        this.f13775a = aOVar;
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
            this.f13775a.c().l(jTextField.getText());
        } catch (V.a e2) {
            bV.d(C1818g.b("Valid Expressions or Numeric Values only.") + "\n" + e2.getLocalizedMessage(), jTextField);
            jTextField.setText(this.f13775a.f13760l);
        }
    }
}
