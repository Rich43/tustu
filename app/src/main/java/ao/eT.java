package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* loaded from: TunerStudioMS.jar:ao/eT.class */
class eT implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0737eu f5598a;

    eT(C0737eu c0737eu) {
        this.f5598a = c0737eu;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == 1) {
            this.f5598a.b((String) itemEvent.getItem());
        }
    }
}
