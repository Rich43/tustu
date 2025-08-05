package t;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* renamed from: t.ax, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:t/ax.class */
class C1851ax implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1850aw f13824a;

    C1851ax(C1850aw c1850aw) {
        this.f13824a = c1850aw;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) {
        this.f13824a.d();
    }
}
