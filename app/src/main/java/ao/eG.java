package ao;

import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* loaded from: TunerStudioMS.jar:ao/eG.class */
class eG implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0737eu f5585a;

    eG(C0737eu c0737eu) {
        this.f5585a = c0737eu;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) {
        if (((JRadioButton) changeEvent.getSource()).isSelected()) {
            this.f5585a.c("AFR");
        }
    }
}
