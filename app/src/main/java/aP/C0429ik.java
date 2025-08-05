package aP;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* renamed from: aP.ik, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/ik.class */
class C0429ik implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0423id f3734a;

    C0429ik(C0423id c0423id) {
        this.f3734a = c0423id;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) throws IllegalArgumentException {
        int value = ((JSlider) changeEvent.getSource()).getValue();
        this.f3734a.f3723c.b(value);
        this.f3734a.f3724d.setText("" + value);
    }
}
