package t;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* loaded from: TunerStudioMS.jar:t/aJ.class */
class aJ implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aF f13743a;

    aJ(aF aFVar) {
        this.f13743a = aFVar;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) throws IllegalArgumentException {
        int value = ((JSlider) changeEvent.getSource()).getValue();
        if (this.f13743a.f13739p || !this.f13743a.a((JSlider) changeEvent.getSource(), aF.f13737n)) {
            return;
        }
        this.f13743a.c().e(value);
        this.f13743a.f13734k.setText("" + value);
    }
}
