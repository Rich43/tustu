package bC;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/* loaded from: TunerStudioMS.jar:bC/p.class */
class p implements ListSelectionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ k f6605a;

    private p(k kVar) {
        this.f6605a = kVar;
    }

    @Override // javax.swing.event.ListSelectionListener
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if (listSelectionEvent.getValueIsAdjusting()) {
            return;
        }
        this.f6605a.a(this.f6605a.f6591b.getSelectedRow());
    }

    /* synthetic */ p(k kVar, l lVar) {
        this(kVar);
    }
}
