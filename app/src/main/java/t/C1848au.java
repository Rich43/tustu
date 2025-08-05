package t;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;

/* renamed from: t.au, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:t/au.class */
class C1848au implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1846as f13807a;

    C1848au(C1846as c1846as) {
        this.f13807a = c1846as;
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        ((JTextField) focusEvent.getSource()).selectAll();
    }
}
