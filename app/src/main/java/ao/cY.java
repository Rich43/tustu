package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* loaded from: TunerStudioMS.jar:ao/cY.class */
class cY implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5481a;

    cY(bP bPVar) {
        this.f5481a = bPVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (((JCheckBoxMenuItem) itemEvent.getSource()).getState()) {
            String actionCommand = ((JCheckBoxMenuItem) itemEvent.getSource()).getActionCommand();
            h.i.c("lineTraceSize", actionCommand);
            this.f5481a.f5347a.p().c(Integer.parseInt(actionCommand));
            this.f5481a.f5347a.p().i();
            this.f5481a.f5347a.p().repaint();
        }
    }
}
