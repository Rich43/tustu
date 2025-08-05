package bB;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/* loaded from: TunerStudioMS.jar:bB/p.class */
class p implements ListSelectionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ l f6567a;

    private p(l lVar) {
        this.f6567a = lVar;
    }

    @Override // javax.swing.event.ListSelectionListener
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if (listSelectionEvent.getValueIsAdjusting()) {
            return;
        }
        this.f6567a.a(this.f6567a.f6557a.getSelectedRow());
    }

    /* synthetic */ p(l lVar, m mVar) {
        this(lVar);
    }
}
