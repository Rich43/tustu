package bt;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/* loaded from: TunerStudioMS.jar:bt/bK.class */
class bK extends FocusAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bI f8914a;

    bK(bI bIVar) {
        this.f8914a = bIVar;
    }

    @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        this.f8914a.selectAll();
    }

    @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
        this.f8914a.b();
    }
}
