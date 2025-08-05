package bx;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/* loaded from: TunerStudioMS.jar:bx/v.class */
class v implements ListSelectionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ t f9228a;

    private v(t tVar) {
        this.f9228a = tVar;
    }

    @Override // javax.swing.event.ListSelectionListener
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if (listSelectionEvent.getValueIsAdjusting()) {
            return;
        }
        this.f9228a.a(this.f9228a.getSelectedRow());
    }
}
