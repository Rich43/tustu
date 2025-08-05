package bE;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/* loaded from: TunerStudioMS.jar:bE/h.class */
class h implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ e f6719a;

    h(e eVar) {
        this.f6719a = eVar;
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        this.f6719a.f6714c.selectAll();
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
        this.f6719a.e();
    }
}
