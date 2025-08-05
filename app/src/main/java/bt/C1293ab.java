package bt;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/* renamed from: bt.ab, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/ab.class */
class C1293ab extends FocusAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1292aa f8814a;

    C1293ab(C1292aa c1292aa) {
        this.f8814a = c1292aa;
    }

    @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        this.f8814a.c(this.f8814a.f8805b.a());
    }

    @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
        this.f8814a.b(this.f8814a.f8805b.a());
    }
}
