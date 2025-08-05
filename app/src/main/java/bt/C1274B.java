package bt;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/* renamed from: bt.B, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/B.class */
class C1274B extends FocusAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1273A f8654a;

    C1274B(C1273A c1273a) {
        this.f8654a = c1273a;
    }

    @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        this.f8654a.selectAll();
    }

    @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
        this.f8654a.b();
    }
}
