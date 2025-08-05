package ao;

import g.C1733k;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:ao/cP.class */
class cP implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5472a;

    cP(bP bPVar) {
        this.f5472a = bPVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == 1) {
            String actionCommand = ((JCheckBoxMenuItem) itemEvent.getSource()).getActionCommand();
            String name = ((JCheckBoxMenuItem) itemEvent.getSource()).getName();
            String text = ((JCheckBoxMenuItem) itemEvent.getSource()).getText();
            if (!name.equals("Other")) {
                this.f5472a.c(actionCommand, name, true);
                return;
            }
            String strA = C1733k.a("{Enter '" + text + "' value }", false, (Component) C0645bi.a().b());
            if (strA == null || strA.equals("")) {
                return;
            }
            this.f5472a.c(actionCommand, strA, true);
        }
    }
}
