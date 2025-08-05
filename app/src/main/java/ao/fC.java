package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:ao/fC.class */
class fC implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0764fu f5701a;

    fC(C0764fu c0764fu) {
        this.f5701a = c0764fu;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (((JCheckBoxMenuItem) itemEvent.getSource()).getState()) {
            this.f5701a.d(bE.m.f6723b);
        }
    }
}
