package t;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* renamed from: t.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:t/c.class */
class C1855c implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1854b f13854a;

    C1855c(C1854b c1854b) {
        this.f13854a = c1854b;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) {
        this.f13854a.d();
    }
}
