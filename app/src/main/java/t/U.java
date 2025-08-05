package t;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* loaded from: TunerStudioMS.jar:t/U.class */
class U implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1875w f13706a;

    U(C1875w c1875w) {
        this.f13706a = c1875w;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) {
        this.f13706a.f13910a.l(((JSlider) changeEvent.getSource()).getValue());
    }
}
