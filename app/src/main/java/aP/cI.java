package aP;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:aP/cI.class */
class cI implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bZ f3118a;

    cI(bZ bZVar) {
        this.f3118a = bZVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        h.i.c(h.i.f12337aD, Boolean.toString(((JCheckBoxMenuItem) itemEvent.getSource()).getState()));
    }
}
