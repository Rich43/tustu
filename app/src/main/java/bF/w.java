package bF;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/* loaded from: TunerStudioMS.jar:bF/w.class */
class w implements ListSelectionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ v f6892a;

    w(v vVar) {
        this.f6892a = vVar;
    }

    @Override // javax.swing.event.ListSelectionListener
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        this.f6892a.f6890c.getTableHeader().repaint();
    }
}
