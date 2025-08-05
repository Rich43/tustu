package t;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* loaded from: TunerStudioMS.jar:t/W.class */
class W implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1875w f13708a;

    W(C1875w c1875w) {
        this.f13708a = c1875w;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) {
        JSlider jSlider = (JSlider) changeEvent.getSource();
        int value = jSlider.getValue();
        if (this.f13708a.a(jSlider, C1875w.f13939y)) {
            this.f13708a.f13910a.g(value);
        }
    }
}
