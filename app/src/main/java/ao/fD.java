package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:ao/fD.class */
class fD implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0764fu f5702a;

    fD(C0764fu c0764fu) {
        this.f5702a = c0764fu;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (((JCheckBoxMenuItem) itemEvent.getSource()).getState()) {
            this.f5702a.d(bE.m.f6724c);
        }
    }
}
