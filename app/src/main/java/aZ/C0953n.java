package az;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/* renamed from: az.n, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:az/n.class */
class C0953n implements DocumentListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0945f f6495a;

    C0953n(C0945f c0945f) {
        this.f6495a = c0945f;
    }

    @Override // javax.swing.event.DocumentListener
    public void insertUpdate(DocumentEvent documentEvent) {
        try {
            this.f6495a.j();
        } catch (Exception e2) {
        }
    }

    @Override // javax.swing.event.DocumentListener
    public void removeUpdate(DocumentEvent documentEvent) {
    }

    @Override // javax.swing.event.DocumentListener
    public void changedUpdate(DocumentEvent documentEvent) {
    }
}
