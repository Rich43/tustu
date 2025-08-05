package ao;

import W.C0184j;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* renamed from: ao.aa, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/aa.class */
final class C0610aa implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0184j f5169a;

    C0610aa(C0184j c0184j) {
        this.f5169a = c0184j;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) {
        this.f5169a.i(((JSlider) changeEvent.getSource()).getValue());
        C0645bi.a().c().i();
        C0645bi.a().c().repaint();
    }
}
