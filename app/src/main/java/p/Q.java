package p;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/* loaded from: TunerStudioMS.jar:p/Q.class */
class Q implements ListSelectionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ J f13177a;

    private Q(J j2) {
        this.f13177a = j2;
    }

    @Override // javax.swing.event.ListSelectionListener
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if (listSelectionEvent.getValueIsAdjusting()) {
            return;
        }
        this.f13177a.a(this.f13177a.f13166c.getSelectedRow());
    }

    /* synthetic */ Q(J j2, K k2) {
        this(j2);
    }
}
