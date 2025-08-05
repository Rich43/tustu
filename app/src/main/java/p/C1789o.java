package p;

import bH.W;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* renamed from: p.o, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:p/o.class */
class C1789o implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1781g f13232a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ JLabel f13233b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C1787m f13234c;

    C1789o(C1787m c1787m, C1781g c1781g, JLabel jLabel) {
        this.f13234c = c1787m;
        this.f13232a = c1781g;
        this.f13233b = jLabel;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) throws IllegalArgumentException {
        this.f13233b.setText(W.a(this.f13234c.f13220d.getValue() + " s.", ' ', 8));
    }
}
