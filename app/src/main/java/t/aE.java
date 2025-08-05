package t;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* loaded from: TunerStudioMS.jar:t/aE.class */
class aE implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aD f13724a;

    aE(aD aDVar) {
        this.f13724a = aDVar;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) {
        this.f13724a.d();
    }
}
