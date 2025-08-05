package aP;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/* loaded from: TunerStudioMS.jar:aP/jH.class */
class jH implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ jG f3777a;

    jH(jG jGVar) {
        this.f3777a = jGVar;
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        this.f3777a.c();
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
        this.f3777a.b();
    }
}
