package aP;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:aP/cA.class */
class cA implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bZ f3110a;

    cA(bZ bZVar) {
        this.f3110a = bZVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        this.f3110a.a(h.i.f12284E, ((JCheckBoxMenuItem) itemEvent.getSource()).getState());
    }
}
