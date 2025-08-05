package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:ao/dC.class */
class dC implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5516a;

    dC(bP bPVar) {
        this.f5516a = bPVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        h.i.c(h.i.f12337aD, Boolean.toString(((JCheckBoxMenuItem) itemEvent.getSource()).getState()));
    }
}
