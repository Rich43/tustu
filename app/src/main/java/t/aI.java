package t;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* loaded from: TunerStudioMS.jar:t/aI.class */
class aI implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aF f13742a;

    aI(aF aFVar) {
        this.f13742a = aFVar;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) throws IllegalArgumentException {
        int value = ((JSlider) changeEvent.getSource()).getValue();
        if (this.f13742a.f13739p || !this.f13742a.a((JSlider) changeEvent.getSource(), aF.f13738o)) {
            return;
        }
        this.f13742a.c().c(value);
        this.f13742a.f13733j.setText("" + value);
    }
}
