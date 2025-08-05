package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:ao/dB.class */
class dB implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5515a;

    dB(bP bPVar) {
        this.f5515a = bPVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (((JCheckBoxMenuItem) itemEvent.getSource()).getState()) {
            h.i.c(h.i.f12286G, h.i.f12288I);
        }
    }
}
