package ao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JSplitPane;

/* loaded from: TunerStudioMS.jar:ao/cV.class */
class cV implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5478a;

    cV(bP bPVar) {
        this.f5478a = bPVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (((JCheckBoxMenuItem) itemEvent.getSource()).getState()) {
            this.f5478a.f5347a.p().q();
        }
        if (((JCheckBoxMenuItem) itemEvent.getSource()).getState()) {
            h.i.c(h.i.f12298S, JSplitPane.TOP);
            this.f5478a.f5347a.p().i();
            this.f5478a.f5347a.p().repaint();
        }
    }
}
