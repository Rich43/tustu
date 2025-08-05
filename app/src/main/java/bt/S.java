package bt;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/* loaded from: TunerStudioMS.jar:bt/S.class */
class S implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1290R f8705a;

    S(C1290R c1290r) {
        this.f8705a = c1290r;
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        this.f8705a.c(this.f8705a.f8697a.b(0));
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
        this.f8705a.b(this.f8705a.f8697a.b(0));
    }
}
