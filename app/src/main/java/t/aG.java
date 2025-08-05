package t;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* loaded from: TunerStudioMS.jar:t/aG.class */
class aG implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aF f13740a;

    aG(aF aFVar) {
        this.f13740a = aFVar;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) throws IllegalArgumentException {
        int value = ((JSlider) changeEvent.getSource()).getValue();
        if (this.f13740a.f13739p || !this.f13740a.a((JSlider) changeEvent.getSource(), aF.f13735l)) {
            return;
        }
        this.f13740a.c().a(value);
        this.f13740a.f13731h.setText("" + value);
    }
}
