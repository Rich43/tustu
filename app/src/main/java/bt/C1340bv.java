package bt;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/* renamed from: bt.bv, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/bv.class */
class C1340bv implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1337bs f9071a;

    C1340bv(C1337bs c1337bs) {
        this.f9071a = c1337bs;
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        this.f9071a.c(this.f9071a.f9057b.aJ());
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
        this.f9071a.b(this.f9071a.f9057b.aJ());
    }
}
