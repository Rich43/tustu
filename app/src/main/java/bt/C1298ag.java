package bt;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/* renamed from: bt.ag, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/ag.class */
class C1298ag extends FocusAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1297af f8827a;

    C1298ag(C1297af c1297af) {
        this.f8827a = c1297af;
    }

    @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        this.f8827a.d(this.f8827a.f8821b.a());
    }

    @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
        this.f8827a.c(this.f8827a.f8821b.a());
    }
}
