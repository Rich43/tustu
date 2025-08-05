package ao;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/* loaded from: TunerStudioMS.jar:ao/fY.class */
class fY implements ListSelectionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ fX f5792a;

    fY(fX fXVar) {
        this.f5792a = fXVar;
    }

    @Override // javax.swing.event.ListSelectionListener
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        this.f5792a.f5773O.a();
    }
}
