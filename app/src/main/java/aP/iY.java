package aP;

import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* loaded from: TunerStudioMS.jar:aP/iY.class */
class iY implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ iX f3709a;

    iY(iX iXVar) {
        this.f3709a = iXVar;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) {
        this.f3709a.b(((Integer) ((JSpinner) changeEvent.getSource()).getValue()).intValue());
    }
}
