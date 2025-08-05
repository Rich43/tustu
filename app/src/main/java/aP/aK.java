package aP;

import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* loaded from: TunerStudioMS.jar:aP/aK.class */
class aK implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aJ f2829a;

    aK(aJ aJVar) {
        this.f2829a = aJVar;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) {
        this.f2829a.f2825g.setEnabled(((JCheckBox) changeEvent.getSource()).isSelected());
    }
}
