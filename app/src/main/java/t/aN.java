package t;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;

/* loaded from: TunerStudioMS.jar:t/aN.class */
class aN implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aK f13749a;

    aN(aK aKVar) {
        this.f13749a = aKVar;
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        ((JTextField) focusEvent.getSource()).selectAll();
    }
}
