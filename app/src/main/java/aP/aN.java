package aP;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JTextField;

/* loaded from: TunerStudioMS.jar:aP/aN.class */
class aN extends FocusAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aJ f2835a;

    aN(aJ aJVar) {
        this.f2835a = aJVar;
    }

    @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        ((JTextField) focusEvent.getSource()).selectAll();
    }

    @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
    }
}
