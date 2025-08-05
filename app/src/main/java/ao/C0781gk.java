package ao;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* renamed from: ao.gk, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/gk.class */
class C0781gk implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ fX f5980a;

    C0781gk(fX fXVar) {
        this.f5980a = fXVar;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) throws IllegalArgumentException {
        this.f5980a.o();
        String strA = bH.W.a(this.f5980a.i(), 2);
        this.f5980a.f5741q.setToolTipText("<html>Minimum Individual Hit Weight is the minimum weight of<br>each hit before it will be included.<br>Value of 0-1 where the closer to 1, <br>the more centered a hit needs to be before it is included.<br>0 all hits will be counted with the associated weighting,<br>a value of 1 requires a direct center hit to be included. ");
        this.f5980a.f5739o.setText(strA);
    }
}
