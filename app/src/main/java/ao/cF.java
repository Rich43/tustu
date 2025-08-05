package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:ao/cF.class */
class cF implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5460a;

    cF(bP bPVar) {
        this.f5460a = bPVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        JCheckBoxMenuItem jCheckBoxMenuItem = (JCheckBoxMenuItem) itemEvent.getSource();
        if (this.f5460a.c(jCheckBoxMenuItem.getName(), jCheckBoxMenuItem.getActionCommand(), itemEvent.getStateChange() == 1)) {
            return;
        }
        jCheckBoxMenuItem.setState(false);
    }
}
