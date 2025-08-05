package ao;

import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* loaded from: TunerStudioMS.jar:ao/eQ.class */
class eQ implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0737eu f5595a;

    eQ(C0737eu c0737eu) {
        this.f5595a = c0737eu;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) {
        if (((JRadioButton) changeEvent.getSource()).isSelected()) {
            this.f5595a.c("Gego");
        }
    }
}
