package t;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* loaded from: TunerStudioMS.jar:t/aH.class */
class aH implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aF f13741a;

    aH(aF aFVar) {
        this.f13741a = aFVar;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) throws IllegalArgumentException {
        int value = ((JSlider) changeEvent.getSource()).getValue();
        if (this.f13741a.f13739p || !this.f13741a.a((JSlider) changeEvent.getSource(), aF.f13736m)) {
            return;
        }
        this.f13741a.c().b(value);
        this.f13741a.f13732i.setText("" + value);
    }
}
