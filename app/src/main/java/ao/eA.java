package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* loaded from: TunerStudioMS.jar:ao/eA.class */
class eA implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0737eu f5577a;

    eA(C0737eu c0737eu) {
        this.f5577a = c0737eu;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == 1) {
            h.i.c("baseWeight", ((eX) itemEvent.getItem()).a());
        }
    }
}
