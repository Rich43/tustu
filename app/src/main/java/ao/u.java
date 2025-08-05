package aO;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/* loaded from: TunerStudioMS.jar:aO/u.class */
class u implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ k f2720a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ q f2721b;

    u(q qVar, k kVar) {
        this.f2721b = qVar;
        this.f2720a = kVar;
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        this.f2721b.f2712c.selectAll();
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
        this.f2721b.a();
    }
}
