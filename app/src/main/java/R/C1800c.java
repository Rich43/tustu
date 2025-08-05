package r;

import bH.C;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* renamed from: r.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:r/c.class */
class C1800c implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1799b f13430a;

    C1800c(C1799b c1799b) {
        this.f13430a = c1799b;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == 1) {
            if (!(this.f13430a.f13424c.getSelectedItem() instanceof C1804g)) {
                C.c("Didn't show Dash");
            } else {
                this.f13430a.b(((C1804g) this.f13430a.f13424c.getSelectedItem()).a());
            }
        }
    }
}
