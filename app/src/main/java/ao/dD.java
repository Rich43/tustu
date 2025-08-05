package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:ao/dD.class */
class dD implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5517a;

    dD(bP bPVar) {
        this.f5517a = bPVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        this.f5517a.a("loadReverse", ((JCheckBoxMenuItem) itemEvent.getSource()).getState());
    }
}
