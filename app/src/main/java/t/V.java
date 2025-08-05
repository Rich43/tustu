package t;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* loaded from: TunerStudioMS.jar:t/V.class */
class V implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1875w f13707a;

    V(C1875w c1875w) {
        this.f13707a = c1875w;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) {
        JSlider jSlider = (JSlider) changeEvent.getSource();
        int value = jSlider.getValue();
        if (this.f13707a.a(jSlider, C1875w.f13938x)) {
            this.f13707a.f13910a.f(value);
        }
    }
}
