package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* loaded from: TunerStudioMS.jar:ao/ht.class */
class ht implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ hn f6137a;

    ht(hn hnVar) {
        this.f6137a = hnVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == 1) {
            this.f6137a.a(1, (String) itemEvent.getItem());
        }
    }
}
