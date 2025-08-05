package ao;

import W.C0184j;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* loaded from: TunerStudioMS.jar:ao/aD.class */
class aD implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0184j f5116a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0625ap f5117b;

    aD(C0625ap c0625ap, C0184j c0184j) {
        this.f5117b = c0625ap;
        this.f5116a = c0184j;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) {
        this.f5116a.i(((JSlider) changeEvent.getSource()).getValue());
        this.f5117b.i();
        this.f5117b.repaint();
    }
}
