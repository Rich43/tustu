package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* loaded from: TunerStudioMS.jar:ao/gZ.class */
class gZ implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ gS f5969a;

    gZ(gS gSVar) {
        this.f5969a = gSVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == 1) {
            C0801hd c0801hd = (C0801hd) itemEvent.getSource();
            this.f5969a.a((String) itemEvent.getItem(), c0801hd);
        }
    }
}
