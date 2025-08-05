package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:ao/dA.class */
class dA implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5514a;

    dA(bP bPVar) {
        this.f5514a = bPVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (((JCheckBoxMenuItem) itemEvent.getSource()).getState()) {
            h.i.c(h.i.f12286G, h.i.f12287H);
        }
    }
}
