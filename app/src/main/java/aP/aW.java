package aP;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/* loaded from: TunerStudioMS.jar:aP/aW.class */
class aW implements ListSelectionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aV f2875a;

    aW(aV aVVar) {
        this.f2875a = aVVar;
    }

    @Override // javax.swing.event.ListSelectionListener
    public void valueChanged(ListSelectionEvent listSelectionEvent) throws IllegalArgumentException {
        if (listSelectionEvent.getValueIsAdjusting()) {
            return;
        }
        this.f2875a.l();
    }
}
