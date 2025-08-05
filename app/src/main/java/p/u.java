package p;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/* loaded from: TunerStudioMS.jar:p/u.class */
class u implements ListSelectionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1790p f13246a;

    private u(C1790p c1790p) {
        this.f13246a = c1790p;
    }

    @Override // javax.swing.event.ListSelectionListener
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if (listSelectionEvent.getValueIsAdjusting()) {
            return;
        }
        this.f13246a.a(this.f13246a.f13237c.getSelectedRow());
    }

    /* synthetic */ u(C1790p c1790p, C1791q c1791q) {
        this(c1790p);
    }
}
