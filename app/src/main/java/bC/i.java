package bC;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/* loaded from: TunerStudioMS.jar:bC/i.class */
class i implements ListSelectionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ e f6588a;

    i(e eVar) {
        this.f6588a = eVar;
    }

    @Override // javax.swing.event.ListSelectionListener
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if (listSelectionEvent.getValueIsAdjusting()) {
            return;
        }
        this.f6588a.h();
    }
}
