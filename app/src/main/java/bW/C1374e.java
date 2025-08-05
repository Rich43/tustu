package bw;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;

/* renamed from: bw.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bw/e.class */
class C1374e implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1371b f9166a;

    C1374e(C1371b c1371b) {
        this.f9166a = c1371b;
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
