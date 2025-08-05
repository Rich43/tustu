package bE;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/* loaded from: TunerStudioMS.jar:bE/j.class */
class j implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ e f6721a;

    j(e eVar) {
        this.f6721a = eVar;
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        this.f6721a.f6715d.selectAll();
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
        this.f6721a.f();
    }
}
