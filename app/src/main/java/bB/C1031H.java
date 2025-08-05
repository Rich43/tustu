package bb;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/* renamed from: bb.H, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bb/H.class */
class C1031H implements ListSelectionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1028E f7730a;

    C1031H(C1028E c1028e) {
        this.f7730a = c1028e;
    }

    @Override // javax.swing.event.ListSelectionListener
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        C1034K c1034k = (C1034K) this.f7730a.f7724h.getSelectedValue();
        if (this.f7730a.f7722k == null || listSelectionEvent.getValueIsAdjusting()) {
            return;
        }
        if (c1034k != null) {
            this.f7730a.f7722k.f(c1034k.a());
        } else {
            this.f7730a.f7722k.f(null);
        }
    }
}
