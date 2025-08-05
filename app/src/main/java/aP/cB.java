package aP;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:aP/cB.class */
class cB implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bZ f3111a;

    cB(bZ bZVar) {
        this.f3111a = bZVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        this.f3111a.a(h.i.f12300U, ((JCheckBoxMenuItem) itemEvent.getSource()).getState());
    }
}
