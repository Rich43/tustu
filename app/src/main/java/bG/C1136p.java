package bg;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/* renamed from: bg.p, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bg/p.class */
class C1136p implements ListSelectionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1135o f8093a;

    C1136p(C1135o c1135o) {
        this.f8093a = c1135o;
    }

    @Override // javax.swing.event.ListSelectionListener
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if (listSelectionEvent.getValueIsAdjusting()) {
            return;
        }
        this.f8093a.e();
    }
}
