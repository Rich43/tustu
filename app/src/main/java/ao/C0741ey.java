package ao;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* renamed from: ao.ey, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/ey.class */
class C0741ey implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0737eu f5694a;

    C0741ey(C0737eu c0737eu) {
        this.f5694a = c0737eu;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) {
        h.i.c("WBafrOffSet", "" + ((JSlider) changeEvent.getSource()).getValue());
    }
}
