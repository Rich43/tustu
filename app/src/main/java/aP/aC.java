package aP;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.text.JTextComponent;

/* loaded from: TunerStudioMS.jar:aP/aC.class */
class aC extends FocusAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0224at f2801a;

    aC(C0224at c0224at) {
        this.f2801a = c0224at;
    }

    @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        if (focusEvent.getSource() instanceof JTextComponent) {
            ((JTextComponent) focusEvent.getSource()).selectAll();
        }
    }
}
