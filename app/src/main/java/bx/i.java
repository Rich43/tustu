package bx;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/* loaded from: TunerStudioMS.jar:bx/i.class */
class i implements ListSelectionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1377c f9199a;

    private i(C1377c c1377c) {
        this.f9199a = c1377c;
    }

    @Override // javax.swing.event.ListSelectionListener
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if (listSelectionEvent.getValueIsAdjusting()) {
            return;
        }
        this.f9199a.a(this.f9199a.f9189b.getSelectedRow());
    }

    /* synthetic */ i(C1377c c1377c, C1378d c1378d) {
        this(c1377c);
    }
}
