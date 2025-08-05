package ao;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* renamed from: ao.gl, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/gl.class */
class C0782gl implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ fX f5981a;

    C0782gl(fX fXVar) {
        this.f5981a = fXVar;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) throws IllegalArgumentException {
        this.f5981a.o();
        String strA = bH.W.a(this.f5981a.a(), 2);
        this.f5981a.f5742r.setToolTipText("Minimum Total Hit Weight: " + strA);
        this.f5981a.f5740p.setText(strA);
    }
}
