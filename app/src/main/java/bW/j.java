package bw;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;

/* loaded from: TunerStudioMS.jar:bw/j.class */
class j implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ f f9185a;

    j(f fVar) {
        this.f9185a = fVar;
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        if (focusEvent.getSource() instanceof JTextField) {
            ((JTextField) focusEvent.getSource()).selectAll();
        }
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
    }
}
