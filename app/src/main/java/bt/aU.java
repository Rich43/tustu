package bt;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/* loaded from: TunerStudioMS.jar:bt/aU.class */
class aU extends FocusAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aT f8797a;

    aU(aT aTVar) {
        this.f8797a = aTVar;
    }

    @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        this.f8797a.j();
        this.f8797a.d(this.f8797a.f8777a.b());
    }

    @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
        this.f8797a.c(this.f8797a.f8777a.b());
    }
}
