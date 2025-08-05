package t;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* loaded from: TunerStudioMS.jar:t/bo.class */
class bo implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bn f13852a;

    bo(bn bnVar) {
        this.f13852a = bnVar;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) throws IllegalArgumentException {
        this.f13852a.f13851e.setText("" + this.f13852a.f13849c.getValue());
    }
}
