package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:ao/dG.class */
class dG implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5520a;

    dG(bP bPVar) {
        this.f5520a = bPVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (((JCheckBoxMenuItem) itemEvent.getSource()).getState()) {
            String actionCommand = ((JCheckBoxMenuItem) itemEvent.getSource()).getActionCommand();
            h.i.c("prefFontSize", actionCommand);
            this.f5520a.f5347a.c(Integer.parseInt(actionCommand));
            C0636b.a().b();
            this.f5520a.D();
        }
    }
}
