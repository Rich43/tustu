package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* loaded from: TunerStudioMS.jar:ao/hq.class */
class hq implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ hn f6134a;

    hq(hn hnVar) {
        this.f6134a = hnVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == 1) {
            this.f6134a.a(0, (String) itemEvent.getItem());
        }
    }
}
