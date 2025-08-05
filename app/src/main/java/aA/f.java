package aA;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/* loaded from: TunerStudioMS.jar:aA/f.class */
class f implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ e f2258a;

    f(e eVar) {
        this.f2258a = eVar;
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        this.f2258a.f2255a.selectAll();
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
    }
}
