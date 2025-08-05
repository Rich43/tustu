package t;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;

/* loaded from: TunerStudioMS.jar:t/bd.class */
class bd implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aO f13832a;

    bd(aO aOVar) {
        this.f13832a = aOVar;
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        ((JTextField) focusEvent.getSource()).selectAll();
    }
}
