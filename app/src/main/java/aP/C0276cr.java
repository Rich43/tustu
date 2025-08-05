package aP;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: aP.cr, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/cr.class */
class C0276cr implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bZ f3170a;

    C0276cr(bZ bZVar) {
        this.f3170a = bZVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        if (((JCheckBoxMenuItem) itemEvent.getSource()).getState()) {
            String actionCommand = ((JCheckBoxMenuItem) itemEvent.getSource()).getActionCommand();
            h.i.c("lineTraceSize", actionCommand);
            this.f3170a.f3027b.p().c(Integer.parseInt(actionCommand));
            this.f3170a.f3027b.p().i();
            this.f3170a.f3027b.p().repaint();
        }
    }
}
