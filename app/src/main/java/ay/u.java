package aY;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/* loaded from: TunerStudioMS.jar:aY/u.class */
class u implements ListSelectionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ s f4082a;

    u(s sVar) {
        this.f4082a = sVar;
    }

    @Override // javax.swing.event.ListSelectionListener
    public void valueChanged(ListSelectionEvent listSelectionEvent) throws IllegalArgumentException {
        this.f4082a.f4077c.a(this.f4082a.f4076b.a());
    }
}
