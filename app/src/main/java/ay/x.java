package aY;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/* loaded from: TunerStudioMS.jar:aY/x.class */
class x implements ListSelectionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ s f4085a;

    x(s sVar) {
        this.f4085a = sVar;
    }

    @Override // javax.swing.event.ListSelectionListener
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        boolean z2 = this.f4085a.f4076b.a() != null;
        this.f4085a.f4078d.setEnabled(z2);
        this.f4085a.f4079e.setEnabled(z2);
    }
}
